package com.regiva.simple_vk_client.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import com.regiva.simple_vk_client.Constants
import com.regiva.simple_vk_client.R
import com.regiva.simple_vk_client.Screens
import com.regiva.simple_vk_client.model.interactors.AuthInteractor
import com.regiva.simple_vk_client.model.system.FlowRouter
import com.regiva.simple_vk_client.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_authorize.*

class AuthorizeFragment : BaseFragment() {

    private val flowRouter: FlowRouter by lazy {
        scope.getInstance(FlowRouter::class.java)
    }

    private val authInteractor: AuthInteractor by lazy {
        scope.getInstance(AuthInteractor::class.java)
    }

    override val layoutRes: Int
        get() = R.layout.fragment_authorize

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        inflater.inflate(layoutRes, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListeners()
        openAuthPage()
    }

    private fun openAuthPage() {
        wv_auth.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                //todo
                Log.d("rere", "url = $url")
                if ((url?.contains("https://oauth.vk.com/blank.html")) == true) {
                    Log.d("rere", "ya zawel)")

                    val token = url
                        .substringAfter("access_token=")
                        .substringBefore("&expires_in")
                    Log.d("rere", "save token blean $token")
                    authInteractor.storeToken(token)
                    flowRouter.newRootFlow(Screens.Main())
                }
                else {
                    Log.d("rere", "vot tak vot)")
                }
                return false
            }
        }
        //todo 23429620123402ab3988552cb543802cb7c0e26f4547f5b302b1e91ffdd442d467fdd0e065002c1ac1de7
        wv_auth.loadUrl("https://oauth.vk.com/authorize?client_id=${Constants.Api.APP_ID}&display=page&redirect_uri=https://oauth.vk.com/blank.html&scope=wall,friends&response_type=token&v=5.60")
    }

    private fun setOnClickListeners() {
//        btn_login?.setOnClickListener { goToLogin() }
//        tv_sign_up?.setOnClickListener{ goToRegister()}
    }
}