package com.regiva.simple_vk_client

import com.regiva.simple_vk_client.ui.auth.AuthFlowFragment
import com.regiva.simple_vk_client.ui.auth.AuthorizeFragment
import com.regiva.simple_vk_client.ui.main.MainFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

object Screens {

    //flows
    object AuthFlow : SupportAppScreen() {
        override fun getFragment() = AuthFlowFragment()
    }

    //todo
    /*//tab flows
    object HomeFlow : SupportAppScreen() {
        override fun getFragment() = HomeFlowFragment()
    }

    object ProfileFlow : SupportAppScreen() {
        override fun getFragment() = ProfileFlowFragment()
    }*/

    //screens
    object Authorize : SupportAppScreen() {
        override fun getFragment() = AuthorizeFragment()
    }

    data class Main(val currentTab: Int? = null) : SupportAppScreen() {
        override fun getFragment() = MainFragment.create(currentTab)
    }


}