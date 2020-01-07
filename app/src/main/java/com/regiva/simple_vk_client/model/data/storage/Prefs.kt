package com.regiva.simple_vk_client.model.data.storage

import android.content.Context
import com.google.gson.Gson
import javax.inject.Inject

class Prefs @Inject constructor(
    private val context: Context,
    private val gson: Gson
) {

}