package org.mariusc.gitdemo.data.network

import android.content.Context

import org.mariusc.gitdemo.data.network.cache.HttpClientNetworkInterceptor
import org.mariusc.gitdemo.data.network.cache.HttpClientOfflineInterceptor
import org.mariusc.system.connectivity.ConnectivityState
import org.mariusc.system.log.ILogger

import java.io.File
import java.util.concurrent.TimeUnit

import io.reactivex.Observable
import okhttp3.Cache
import okhttp3.OkHttpClient

/**
 * Created by MConstantin on 4/27/2017.
 */

class OkHttpClientWithCacheFactory(private val context: Context, private val connectivityStateObservable: Observable<ConnectivityState>) {
    private var cacheDirName = "cache"
    private var maxStale = 7 // by default 7 days
    private var maxStaleTimeUnit = TimeUnit.DAYS // by default days

    private var maxAge = 2 // by default 2
    private var cacheSize = 10 // 10 MB by default
    private val maxAgeTimeUnit = TimeUnit.MINUTES

    private var logger = ILogger.NULL

    fun cacheDirName(cacheDirName: String): OkHttpClientWithCacheFactory {
        this.cacheDirName = cacheDirName
        return this
    }

    fun maxStale(maxStale: Int, timeUnit: TimeUnit): OkHttpClientWithCacheFactory {
        this.maxStale = maxStale
        this.maxStaleTimeUnit = timeUnit
        return this
    }

    fun maxAge(maxAge: Int, timeUnit: TimeUnit): OkHttpClientWithCacheFactory {
        this.maxAge = maxAge
        this.maxStaleTimeUnit = timeUnit
        return this

    }

    fun cacheSizeInMB(size: Int): OkHttpClientWithCacheFactory {
        cacheSize = size
        return this
    }

    fun logger(logger: ILogger): OkHttpClientWithCacheFactory {
        this.logger = logger
        return this
    }


    private fun cacheDir(): File? {
        try {
            return File(context.cacheDir, cacheDirName)
        } catch (e: Exception) {
            logger.e(TAG, e, "Could not create the CACHE dir")
            return null
        }

    }

    fun build(): OkHttpClient {
        val cacheDir = cacheDir()
        val builder = OkHttpClient.Builder()
        if (cacheDir != null) {
            builder.cache(Cache(cacheDir, cacheSize.toLong()))
        }
        builder.addNetworkInterceptor(HttpClientNetworkInterceptor(maxAge, maxAgeTimeUnit))
        builder.addInterceptor(HttpClientOfflineInterceptor(connectivityStateObservable,
                maxStale, maxStaleTimeUnit))
        return builder.build()
    }

    companion object {
        private val TAG = OkHttpClientWithCacheFactory::class.java.simpleName
    }
}
