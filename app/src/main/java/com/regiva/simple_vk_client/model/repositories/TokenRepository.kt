package com.regiva.simple_vk_client.model.repositories

import com.regiva.simple_vk_client.model.data.storage.Prefs
import javax.inject.Inject

class TokenRepository @Inject constructor(
    private val prefs: Prefs
) {

    fun getToken() = prefs.token

    fun isLoggedIn() = prefs.token != null

    fun clearAccount() = prefs.removeSharedPreferences()

}