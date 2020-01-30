package com.regiva.simple_vk_client.model.repositories

import com.google.gson.Gson
import com.regiva.simple_vk_client.model.data.storage.Prefs
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val prefs: Prefs,
    private val gson: Gson/*,
    authHeaderInterceptor: AuthHeaderInterceptor*/
) {

    private val okHttpClient = OkHttpClient.Builder()
        //.addInterceptor(authHeaderInterceptor)
        .addNetworkInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        .build()

//    fun storeAccount(user: UserDto) {
//        prefs.account = user
//    }

    fun storeToken(token: String) {
        if (token != prefs.token) prefs.token = token
    }

    fun logOut() {
        prefs.removeSharedPreferences()
        prefs.loggedIn = false
    }

}