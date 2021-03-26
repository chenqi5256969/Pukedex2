package com.revenco.pokedex2.network

import okhttp3.Interceptor
import okhttp3.Response


class HttpRequestInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        //最开始的网络请求数据
        val originalRequest = chain.request()
        val request = originalRequest.newBuilder().url(originalRequest.url).build()
        return chain.proceed(request)
    }
}