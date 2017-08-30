package org.mariusc.gitdemo.data.network.cache

import com.nhaarman.mockito_kotlin.*
import org.mariusc.system.connectivity.ConnectivityState
import java.util.concurrent.TimeUnit

import io.reactivex.subjects.BehaviorSubject
import okhttp3.CacheControl
import okhttp3.Request

import org.assertj.core.api.Java6Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import org.mariusc.gitdemo.data.rxInTestMode
import org.mariusc.gitdemo.data.rxOutFromTestMode
import org.mariusc.gitdemo.data.mockChain

/**
 * Created by MConstantin on 4/27/2017.
 */
@RunWith(JUnitPlatform::class)
class InterceptorTest : Spek(
        {

            beforeGroup {
                rxInTestMode()
            }

            given("An Offline Interceptor") {


                val maxStale = 7
                val maxStaleTimeUnit = TimeUnit.DAYS
                val requestArgumentCaptor: KArgumentCaptor<Request> = argumentCaptor<Request>()
                val connectivityStateObservable = BehaviorSubject.create<ConnectivityState>()


                on("online mode") {
                    val chain = mockChain()
                    var clientOfflineInterceptor: HttpClientOfflineInterceptor? = null


                    it("request header should not be modified") {
                        clientOfflineInterceptor = HttpClientOfflineInterceptor(connectivityStateObservable, maxStale, maxStaleTimeUnit)
                        connectivityStateObservable.onNext(ConnectivityState.active())
                        clientOfflineInterceptor?.intercept(chain)

                        verify(chain).proceed(requestArgumentCaptor.capture())
                        val cacheControl = CacheControl.parse(requestArgumentCaptor.lastValue.headers())
                        assertThat(cacheControl.maxStaleSeconds()).isEqualTo(-1)
                        assertThat(cacheControl.onlyIfCached()).isFalse()
                    }
                }

                on("offline mode") {
                    val chain = mockChain()
                    var clientOfflineInterceptor: HttpClientOfflineInterceptor? = null

                    it("the cache control header should be added to the request") {
                        clientOfflineInterceptor = HttpClientOfflineInterceptor(connectivityStateObservable, maxStale, maxStaleTimeUnit)
                        connectivityStateObservable.onNext(ConnectivityState.notActive())
                        clientOfflineInterceptor?.intercept(chain)

                        verify(chain).proceed(requestArgumentCaptor.capture())
                        val cacheControl = CacheControl.parse(requestArgumentCaptor.lastValue.headers())
                        assertThat(cacheControl.maxStaleSeconds()).isEqualTo(maxStaleTimeUnit.toSeconds(maxStale.toLong()).toInt())
                        assertThat(cacheControl.onlyIfCached()).isTrue()
                    }
                }

                on("going from online mode to offline mode") {
                    val chain = mockChain()
                    var clientOfflineInterceptor: HttpClientOfflineInterceptor? = null

                    it("the cache control header should be added to the request") {
                        clientOfflineInterceptor = HttpClientOfflineInterceptor(connectivityStateObservable, maxStale, maxStaleTimeUnit)
                        connectivityStateObservable.onNext(ConnectivityState.active())
                        clientOfflineInterceptor!!.intercept(chain)
                        connectivityStateObservable.onNext(ConnectivityState.notActive())
                        clientOfflineInterceptor?.intercept(chain)

                        verify(chain, times(2)).proceed(requestArgumentCaptor.capture())
                        val allValues = requestArgumentCaptor.allValues
                        val cacheControl1 = CacheControl.parse(allValues[0].headers())
                        val cacheControl2 = CacheControl.parse(allValues[1].headers())
                        assertThat(cacheControl1.maxStaleSeconds()).isEqualTo(-1)
                        assertThat(cacheControl1.onlyIfCached()).isFalse()
                        assertThat(cacheControl2.maxStaleSeconds()).isEqualTo(maxStaleTimeUnit.toSeconds(maxStale.toLong()).toInt())
                        assertThat(cacheControl2.onlyIfCached()).isTrue()
                    }
                }
            }

            afterEachTest {
                rxOutFromTestMode()
            }
        })