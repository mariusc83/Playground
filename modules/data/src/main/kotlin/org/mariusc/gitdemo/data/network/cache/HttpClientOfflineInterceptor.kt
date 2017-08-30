package org.mariusc.gitdemo.data.network.cache

import android.support.annotation.IntRange
import org.mariusc.system.connectivity.ConnectivityState

import java.io.IOException
import java.util.concurrent.TimeUnit

import io.reactivex.Observable
import io.reactivex.functions.Consumer
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * Created by MConstantin on 4/27/2017.
 */

class HttpClientOfflineInterceptor(val connectivityStateObservable: Observable<ConnectivityState>,
                                   @param:IntRange(from = 0,to = Int.MAX_VALUE.toLong()) val maxStale: Int,
                                   val maxStaleTimeUnit: TimeUnit) : Interceptor {

    var isConnected: Boolean = false

    val onConnectionStateReceived = Consumer<ConnectivityState> { connectivityState: ConnectivityState ->
        isConnected = connectivityState.isActive
    }

    val onConnectionStateFailed = Consumer<Throwable> {
        it.printStackTrace();
    }

    init {
        this.connectivityStateObservable.subscribe(onConnectionStateReceived, onConnectionStateFailed)
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        if (isConnected) return chain.proceed(request)

        val cacheControl = CacheControl.Builder()
                .maxStale(maxStale, maxStaleTimeUnit)
                .onlyIfCached()
                .build()

        request = request
                .newBuilder()
                .cacheControl(cacheControl)
                .build()

        return chain.proceed(request)
    }


}
