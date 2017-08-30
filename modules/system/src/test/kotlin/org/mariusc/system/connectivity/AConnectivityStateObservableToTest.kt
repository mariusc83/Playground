/*
package org.mariusc.system.connectivity

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkInfo
import com.nhaarman.mockito_kotlin.anyOrNull

import org.junit.After
import org.junit.Test
import org.mariusc.system.connectivity.supplier.IConnectionSupplier
import org.mariusc.system.connectivity.supplier.PreLollipopSupplier
import org.mockito.BDDMockito
import org.mockito.Mock

import io.reactivex.observers.TestObserver

import org.assertj.core.api.Java6Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.junit.Before
import org.mariusc.system.mock
import org.mariusc.system.rxInTestMode
import org.mariusc.system.rxOutFromTestMode
import org.mockito.ArgumentMatchers
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.nullable
import org.mockito.Mockito
import org.mockito.invocation.InvocationOnMock
import org.mockito.stubbing.Answer
import java.util.*
import kotlin.properties.Delegates


*/
/**
 * Created by MConstantin on 4/6/2017.
 *//*


abstract class AConnectivityStateObservableToTest(val childBody: DescribeBody.() -> Unit) :
        Spek({
            var mockContext: Context by Delegates.notNull<Context>()
            var mockNetworkInfo: NetworkInfo by Delegates.notNull<NetworkInfo>()
            var mockNetwork: Network by Delegates.notNull<Network>()
            var mockIntent: Intent by Delegates.notNull<Intent>()
            var receiver: BroadcastReceiver by Delegates.notNull<BroadcastReceiver>()
            var supplier: IConnectionSupplier by Delegates.notNull<IConnectionSupplier>()
            var mockConnectivityManager: ConnectivityManager by Delegates.notNull<ConnectivityManager>()
            val networkInfos = arrayOf<NetworkInfo>(mockNetworkInfo)
            val networks = arrayOf<Network>(mockNetwork)

            val byDefault: DescribeBody.() -> Unit = {
                on("no active network state") {
                    beforeEach {
                        given(mockConnectivityManager.allNetworkInfo).willReturn(networkInfos)
                        given(mockConnectivityManager.allNetworks).willReturn(networks)
                    }

                    it("returns current state when subscribed") {
                        // when
                        val observable = ConnectivityStateObservable(supplier)
                        val observer = observable.test()

                        // then
                        observer.assertNoErrors()
                        val values = observer.values()
                        val (_, _, networks1) = values[0]
                        assertThat(networks1).containsExactly(mockNetworkInfo)

                    }

                }

                on("active network state") {
                    beforeEach {
                        given(mockConnectivityManager.allNetworkInfo).willReturn(networkInfos)
                        given(mockConnectivityManager.allNetworks).willReturn(networks)
                        given(mockNetworkInfo!!.isConnectedOrConnecting).willReturn(true)
                    }
                    it("returns current state when subscribed") {
                        // when
                        val observable = ConnectivityStateObservable(supplier)
                        val observer = observable.test()

                        // then
                        observer.assertNoErrors()
                        val values = observer.values()
                        val (_, _, networks1) = values[0]
                        assertThat(networks1).containsExactly(mockNetworkInfo)

                    }
                }

                on("no network info available") {
                    it("will return empty network list") {
                        val observable = ConnectivityStateObservable(supplier)
                        // when
                        val observer = observable.test()

                        // then
                        val values = observer.values()
                        val (isActive, _, networks) = values[0]
                        assertThat(networks).isEmpty()
                        assertThat(isActive).isFalse()
                    }
                }
            }

            beforeEach {
                rxInTestMode()
                mockContext = mock<Context>()
                mockIntent = mock<Intent>()
                mockConnectivityManager = mock<ConnectivityManager>()
            }


            given("A PreLollipop ConnectivityManager") {

                beforeEach {
                    mockNetworkInfo = mock<NetworkInfo>()
                    mockNetwork = mock<Network>()
                    given(mockContext.getSystemService(Context.CONNECTIVITY_SERVICE)).willReturn(mockConnectivityManager);
                    given(mockContext.registerReceiver(anyOrNull(), anyOrNull())).willAnswer(Answer<Any>
                    { argument ->
                        receiver = argument.getArgument(0)
                        return@Answer mockIntent

                    })
                    supplier = PreLollipopSupplier(mockContext)
                }


                byDefault.invoke(this)
            }


*/
/*

            @SuppressLint("NewApi")
            @Test
            @Throws(Exception::class)
            fun returns_current_state_when_subscribed_and_a_connection_is_active() {

                // given
                given(mockNetworkInfo!!.isConnectedOrConnecting).willReturn(true)
                val networks = arrayOf<Network>(mockNetwork)
                val networkInfos = arrayOf<NetworkInfo>(mockNetworkInfo)
                given(mockConnectivityManager!!.allNetworkInfo).willReturn(networkInfos)
                given(mockConnectivityManager!!.allNetworks).willReturn(networks)
                val observable = ConnectivityStateObservable(supplier!!)

                // when
                val observer = observable.test()

                // then
                observer.assertNoErrors()
                val values = observer.values()
                val (isActive, activeNetworkInfo, networks1) = values[0]
                assertThat(isActive).isTrue()
                assertThat(activeNetworkInfo).isSameAs(mockNetworkInfo)
                assertThat(networks1).containsExactly(mockNetworkInfo)
            }

            @Test
            @Throws(Exception::class)
            fun returns_empty_networks_list_when_no_network_info_available() {
                // given
                val observable = ConnectivityStateObservable(supplier!!)

                // when
                val observer = observable.test()

                // then
                val values = observer.values()
                val (isActive, _, networks) = values[0]
                assertThat(networks).isEmpty()
                assertThat(isActive).isFalse()
            }

            @Test
            @Throws(Exception::class)
            fun throws_UOE_if_trying_to_subscribe_twice() {
                // given
                val observable = ConnectivityStateObservable(supplier!!)
                val testObserver = TestObserver.create<ConnectivityState>()

                // when
                observable.subscribe(testObserver)
                observable.subscribe(testObserver)

                // then
                testObserver.assertError(IllegalStateException::class.java)
            }

            @Test
            @Throws(Exception::class)
            fun releases_the_supplier_when_disposed() {
                // given
                val mock = Mockito.mock(PreLollipopSupplier::class.java)
                val observable = ConnectivityStateObservable(mock)
                val observer = observable.test()

                // when
                observer.dispose()

                // then
                Mockito.verify(mock).release()
            }
*//*


*/
/*
            @After
            fun tearDown() {
                BDDMockito.reset<NetworkInfo>(mockNetworkInfo)
            }*//*


            afterEach {
                rxOutFromTestMode()
            }

        })



*/
