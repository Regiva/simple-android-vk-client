package com.regiva.simple_vk_client.model.data.network.interceptors

import com.google.gson.Gson
import com.regiva.simple_vk_client.entity.responses.base.ErrorResponse
import okhttp3.Interceptor
import okhttp3.Response

class ErrorResponseInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        val body = response.peekBody(Long.MAX_VALUE)

        val code = response.code()
        if (code in 400..600) {
            val errorBody = Gson().fromJson<ErrorResponse>(body.string(), ErrorResponse::class.java)
            throw Throwable(errorBody.error.error_mgs)
        }

        return response
    }
}