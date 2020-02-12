package com.regiva.simple_vk_client.model.data.storage

import android.content.Context
import com.google.gson.Gson
import javax.inject.Inject

class Prefs @Inject constructor(
    private val context: Context,
    private val gson: Gson
) {

    private fun getSharedPreferences(prefsName: String) =
        context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)

    fun removeSharedPreferences() =
        context.getSharedPreferences(AUTH_DATA, Context.MODE_PRIVATE)
            .edit()
            .clear()
            .apply()

    private val AUTH_DATA = "auth_data"
    private val APP_DATA = "app_data" // не очищается при выходе
    private val KEY_ACCOUNT = "ad_user"
    private val KEY_TOKEN = "ad_token"
    private val KEY_LOGGED_IN = "logged_in"

    private val authPrefs by lazy { getSharedPreferences(AUTH_DATA) }
    private val appPrefs by lazy { getSharedPreferences(APP_DATA) }

    var token: String?
        get() = authPrefs.getString(KEY_TOKEN, null)
        set(value) = authPrefs.edit().putString(KEY_TOKEN, value).apply()

    var loggedIn: Boolean
        get() = appPrefs.getBoolean(KEY_LOGGED_IN, false)
        set(value) = appPrefs.edit().putBoolean(KEY_LOGGED_IN, value).apply()

}