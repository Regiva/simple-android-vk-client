package com.regiva.simple_vk_client

import com.regiva.simple_vk_client.entity.newsfeed.PostModel
import com.regiva.simple_vk_client.ui.auth.AuthFlowFragment
import com.regiva.simple_vk_client.ui.auth.AuthorizeFragment
import com.regiva.simple_vk_client.ui.home.detail.DetailedPostFragment
import com.regiva.simple_vk_client.ui.home.list.HomeFlowFragment
import com.regiva.simple_vk_client.ui.home.list.HomeFragment
import com.regiva.simple_vk_client.ui.main.MainFragment
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

    //todo
    /*object ProfileFlow : SupportAppScreen() {
        override fun getFragment() = ProfileFlowFragment()
    }*/

    //screens
    object Authorize : SupportAppScreen() {
        override fun getFragment() = AuthorizeFragment()
    }

    data class Main(val currentTab: Int? = null) : SupportAppScreen() {
        override fun getFragment() = MainFragment.create(currentTab)
    }

    object Home : SupportAppScreen() {
        override fun getFragment() = HomeFragment()
    }

    data class DetailedPost(val post: PostModel) : SupportAppScreen() {
        override fun getFragment() = DetailedPostFragment.create(post)
    }
}