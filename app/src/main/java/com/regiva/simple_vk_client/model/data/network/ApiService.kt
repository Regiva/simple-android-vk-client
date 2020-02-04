package com.regiva.simple_vk_client.model.data.network

import com.regiva.simple_vk_client.Constants
import com.regiva.simple_vk_client.entity.responses.base.BaseResponse
import com.regiva.simple_vk_client.entity.responses.newsfeed.GetCommentsResponse
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

    @GET("wall.getComments")
    fun getComments(
        @Query("access_token") token: String,
        @Query("v") v: String = Constants.Api.API_VERSION,
        @Query("owner_id") owner_id: Long,
        @Query("need_likes") need_likes: Int = 0,
        @Query("extended") extended: Int = 0,
        @Query("post_id") post_id: Long,
        @Query("sort") sort: String = "asc",
        @Query("preview_length") preview_length: Int = 0/*, todo
        @Query("comment_id") comment_id: Long*/
    ): Observable<BaseResponse<GetCommentsResponse>>
}