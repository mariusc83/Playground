package org.mariusc.gitdemo

import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import org.mariusc.gitdemo.data.network.cache.CACHE_CONTROL_HEADER
import org.mockito.ArgumentMatchers
import org.mockito.BDDMockito
import org.mockito.Mockito

/**
 * Created by MConstantin on 5/31/2017.
 */

inline fun <reified T : Any> mock(): T = Mockito.mock(T::class.java)

fun rxInTestMode(): Unit {
    RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
    RxJavaPlugins.setInitIoSchedulerHandler { Schedulers.trampoline() }

    RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
    RxJavaPlugins.setInitComputationSchedulerHandler { Schedulers.trampoline() }

    RxJavaPlugins.setNewThreadSchedulerHandler { Schedulers.trampoline() }
    RxJavaPlugins.setInitNewThreadSchedulerHandler { Schedulers.trampoline() }

}

fun rxOutFromTestMode(): Unit {
    RxJavaPlugins.setIoSchedulerHandler(null)
    RxJavaPlugins.setInitIoSchedulerHandler(null)
    RxJavaPlugins.setComputationSchedulerHandler(null)
    RxJavaPlugins.setInitComputationSchedulerHandler(null)
    RxJavaPlugins.setNewThreadSchedulerHandler(null)
    RxJavaPlugins.setInitNewThreadSchedulerHandler(null)
}
