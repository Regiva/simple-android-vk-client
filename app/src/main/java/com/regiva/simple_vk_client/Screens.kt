package com.regiva.simple_vk_client

import ru.terrakok.cicerone.android.support.SupportAppScreen

object Screens {

    //flows
    object AuthFlow : SupportAppScreen() {
        override fun getFragment() = AuthFlowFragment()
    }

    //tab flows
    object HomeFlow : SupportAppScreen() {
        override fun getFragment() = HomeFlowFragment()
    }

    object ProfileFlow : SupportAppScreen() {
        override fun getFragment() = ProfileFlowFragment()
    }

    //screens
    object Onboarding : SupportAppScreen() {
        override fun getFragment() = OnboardingFragment()
    }

    object Authorize : SupportAppScreen() {
        override fun getFragment() = AuthorizeFragment()
    }

    object LogIn : SupportAppScreen() {
        override fun getFragment() = LogInFragment()
    }

    object Register : SupportAppScreen() {
        override fun getFragment() = RegisterFragment()
    }

    data class Main(
        val currentTab: Int? = null
    ) : SupportAppScreen() {
        override fun getFragment() = MainFragment.create(currentTab)
    }


}