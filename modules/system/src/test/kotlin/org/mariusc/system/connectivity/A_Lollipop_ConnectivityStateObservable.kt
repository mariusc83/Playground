/*
package org.mariusc.system.connectivity


import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkInfo
import android.net.NetworkRequest

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mariusc.system.RxJavaScheduleOnTrampolineRule
import org.mariusc.system.connectivity.supplier.LollipopSupplier
import org.mockito.Mock
import org.mockito.invocation.InvocationOnMock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.stubbing.Answer

import io.reactivex.observers.TestObserver

import org.assertj.core.api.Java6Assertions.assertThat
import org.mariusc.system.rxInTestMode
import org.mariusc.system.rxOutFromTestMode
import org.mockito.ArgumentMatchers.any
import org.mockito.BDDMockito.given
import org.mockito.Mockito.doAnswer

*/
/**
 * Created by MConstantin on 4/4/2017.
 *//*



class A_Lollipop_ConnectivityStateObservable : AConnectivityStateObservableToTest({
    @Mock
    var mockNetworkRequest: NetworkRequest? = null

    @Mock
    internal var mockBuilder: NetworkRequest.Builder? = null

    internal var networkCallback: ConnectivityManager.NetworkCallback

    beforeEach {
        rxInTestMode()
    }

    afterEach {
        rxOutFromTestMode()
    }
    @SuppressLint("NewApi")
    @Before
    @Throws(Exception::class)
    fun setUp() {
        // given
        given<NetworkInfo>(mockConnectivityManager.getNetworkInfo(mockNetwork)).willReturn(mockNetworkInfo)
        given(mockBuilder!!.build()).willReturn(mockNetworkRequest)
        given(mockContext.getSystemService(Context.CONNECTIVITY_SERVICE)).willReturn(mockConnectivityManager)
        doAnswer { invocation ->
            networkCallback = invocation.getArgument<Any>(1) as ConnectivityManager.NetworkCallback
            null
        }.`when`(mockConnectivityManager).registerNetworkCallback(any(NetworkRequest::class.java), any(ConnectivityManager.NetworkCallback::class.java))
        supplier = LollipopSupplier(mockContext, mockBuilder!!)
    }

    @SuppressLint("NewApi")
    @Test
    @Throws(Exception::class)
    fun keeps_receiving_states_when_connectivity_state_changes() {
        // given
        val networks = arrayOf(mockNetwork)
        given(mockConnectivityManager.allNetworks).willReturn(networks)
        val observable = ConnectivityStateObservable(supplier)
        val observer = observable.test()
        given(mockIntent.action).willReturn(ConnectivityManager.CONNECTIVITY_ACTION)

        // when
        networkCallback.onAvailable(mockNetwork)

        // then
        observer.assertValueCount(2)
        val values = observer.values()
        assertThat(values[0].networks()).containsExactly(mockNetworkInfo)
        assertThat(values[1].networks()).containsExactly(mockNetworkInfo)
    }

    @SuppressLint("NewApi")
    @Test
    @Throws(Exception::class)
    fun stops_emitting_events_when_disposed() {
        // given
        val networks = arrayOf(mockNetwork)
        given(mockConnectivityManager.allNetworks).willReturn(networks)
        val observable = ConnectivityStateObservable(supplier)
        val observer = observable.test()
        given(mockIntent.action).willReturn(ConnectivityManager.CONNECTIVITY_ACTION)
        observer.dispose()

        // when
        networkCallback.onAvailable(mockNetwork)

        // then
        observer.assertValueCount(1)
    }
})*/
