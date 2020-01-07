package com.regiva.simple_vk_client.di.provider

import com.google.gson.Gson
import com.regiva.simple_vk_client.Constants
import com.regiva.simple_vk_client.model.data.network.ApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Provider

class ApiProvider @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val gson: Gson
) : Provider<ApiService> {

    override fun get(): ApiService =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .baseUrl(Constants.Api.BASE_URL)
            .build()
            .create(ApiService::class.java)
}