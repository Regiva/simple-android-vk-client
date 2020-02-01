package com.regiva.simple_vk_client.model.data.network

import com.regiva.simple_vk_client.entity.responses.BaseResponse
import com.regiva.simple_vk_client.entity.responses.GetNewsfeedResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("newsfeed.get")
    fun getNewsfeed(
        @Query("access_token") token: String,
        @Query("v") v: String = "5.60",
        @Query("filter") filter: String = "post"
    ): Observable<BaseResponse<GetNewsfeedResponse>>
}