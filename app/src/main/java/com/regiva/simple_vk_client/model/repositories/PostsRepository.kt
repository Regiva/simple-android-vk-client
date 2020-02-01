package com.regiva.simple_vk_client.model.repositories

import com.regiva.simple_vk_client.model.data.network.ApiService
import com.regiva.simple_vk_client.model.data.storage.Prefs
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class PostsRepository @Inject constructor(
    private val apiService: ApiService,
    private val prefs: Prefs
) {

    fun getNewsfeed() =
        apiService.getNewsfeed(prefs.token ?: "")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}