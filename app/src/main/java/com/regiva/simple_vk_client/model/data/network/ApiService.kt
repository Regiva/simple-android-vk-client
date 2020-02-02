package com.regiva.simple_vk_client.model.data.network

import com.regiva.simple_vk_client.Constants
import com.regiva.simple_vk_client.entity.responses.base.BaseResponse
import com.regiva.simple_vk_client.entity.responses.newsfeed.GetNewsfeedResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("newsfeed.get")
    fun getNewsfeed(
        @Query("access_token") token: String,
        @Query("v") v: String = Constants.Api.API_VERSION,
        @Query("filter") filter: String = "post"
    ): Observable<BaseResponse<GetNewsfeedResponse>>
}