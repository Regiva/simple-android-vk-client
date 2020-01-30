package com.regiva.simple_vk_client.ui.auth

import com.regiva.simple_vk_client.Screens
import com.regiva.simple_vk_client.ui.base.FlowFragment

class AuthFlowFragment : FlowFragment() {

    override fun getLaunchScreen() = Screens.Authorize
}