package com.regiva.simple_vk_client.di.module

import com.bumptech.glide.annotation.GlideModule
import com.regiva.simple_vk_client.di.provider.ApiProvider
import com.regiva.simple_vk_client.di.provider.OkHttpClientProvider
import com.regiva.simple_vk_client.model.data.network.ApiService
import okhttp3.OkHttpClient
import toothpick.config.Module

class ServerModule : Module() {
    init {
        bind(OkHttpClient::class.java).toProvider(OkHttpClientProvider::class.java).providesSingletonInScope()
        bind(ApiService::class.java).toProvider(ApiProvider::class.java).providesSingletonInScope()
        bind(GlideModule::class.java).singletonInScope()
    }
}