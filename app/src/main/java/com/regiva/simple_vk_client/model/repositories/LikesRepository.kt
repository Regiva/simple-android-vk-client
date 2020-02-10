package com.regiva.simple_vk_client.model.repositories

import com.regiva.simple_vk_client.model.data.network.ApiService
import com.regiva.simple_vk_client.model.data.storage.Prefs
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class LikesRepository @Inject constructor(
    private val apiService: ApiService,
    private val prefs: Prefs
) {

    fun like(type: String, owner_id: Long, item_id: Long) =
        apiService.like(
            token = prefs.token ?: "",
            type = type,
            owner_id = owner_id,
            item_id = item_id
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun unlike(type: String, owner_id: Long, item_id: Long) =
        apiService.unlike(
            token = prefs.token ?: "",
            type = type,
            owner_id = owner_id,
            item_id = item_id
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun checkIsLiked(type: String, owner_id: Long, item_id: Long) =
        apiService.checkIsLiked(
            token = prefs.token ?: "",
            type = type,
            owner_id = owner_id,
            item_id = item_id
        )
            .subscribeOn(Schedulers.io())

}