package org.mariusc.gitdemo.data.network.cache

import android.support.annotation.IntRange

import java.io.IOException
import java.util.concurrent.TimeUnit

import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Created by MConstantin on 4/26/2017.
 */

class HttpClientNetworkInterceptor(@param:IntRange(from = 0, to = Integer.MAX_VALUE.toLong()) val maxAge: Int,
                                   val maxAgeTimeUnit: TimeUnit) : Interceptor {


    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse = chain.proceed(chain.request())
        val cacheControl = CacheControl.Builder()
                .maxAge(maxAge, maxAgeTimeUnit)
                .build()
        return originalResponse
                .newBuilder()
                .header(CACHE_CONTROL_HEADER, cacheControl.toString())
                .build()
    }
}
