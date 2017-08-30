package org.mariusc.gitdemo.data.network.cache

import java.util.concurrent.TimeUnit

import okhttp3.CacheControl
import okhttp3.Interceptor

import org.assertj.core.api.Java6Assertions.assertThat

class HttpClientNetworkInterceptorTest(private val toParseChain: Interceptor.Chain) {

    val maxAge = 10
    val maxAgeTimeUnit = TimeUnit.SECONDS
    val httpClientNetworkInterceptor: HttpClientNetworkInterceptor by lazy {
        HttpClientNetworkInterceptor(maxAge, maxAgeTimeUnit)
    }

    fun returns_a_repsonse_with_a_valid_max_age_cache_control_header() {
        // when
        val intercept = httpClientNetworkInterceptor!!.intercept(toParseChain)

        // then
        val cacheControl = CacheControl.parse(intercept.headers())
        assertThat(cacheControl.isPublic).isFalse()
        assertThat(cacheControl.isPrivate).isFalse()
        assertThat(cacheControl.maxAgeSeconds()).isEqualTo(maxAge)
    }

}
