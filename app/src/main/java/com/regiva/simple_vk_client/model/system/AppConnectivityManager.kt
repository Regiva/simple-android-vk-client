package com.regiva.simple_vk_client.model.system

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class AppConnectivityManager @Inject constructor() : BroadcastReceiver() {

    val source = PublishSubject.create<Boolean>()

    override fun onReceive(context: Context, intent: Intent) {
        val conn = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo: NetworkInfo? = conn.activeNetworkInfo

        when {
            networkInfo?.type == ConnectivityManager.TYPE_WIFI -> source.onNext(true)
            networkInfo?.type == ConnectivityManager.TYPE_MOBILE -> source.onNext(true)
            else -> source.onNext(false)
        }
    }
}