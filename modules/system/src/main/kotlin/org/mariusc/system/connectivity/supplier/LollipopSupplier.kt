package org.mariusc.system.connectivity.supplier

import android.annotation.TargetApi
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkInfo
import android.net.NetworkRequest
import android.os.Build

import org.mariusc.system.connectivity.ConnectivityState

import java.util.ArrayList
import java.util.Collections

/**
 * Created by MConstantin on 4/4/2017.
 */

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
internal class LollipopSupplier(context: Context, private val builder: NetworkRequest.Builder) : BaseConnectionSupplier(context) {

    override fun resolveConnectivityState(): ConnectivityState {
        val networks: MutableList<NetworkInfo> = mutableListOf<NetworkInfo>()
        var isActive = false
        var activeNetworkInfo: NetworkInfo? = null

        val allNetworks = connectivityManager.allNetworks

        if (allNetworks != null) {
            for (network in allNetworks) {
                if (network != null) {
                    var networkInfo: NetworkInfo?
                    try {
                        networkInfo = connectivityManager.getNetworkInfo(network)
                    } catch (e: NullPointerException) {
                        networkInfo = null
                    }

                    if (networkInfo != null) {
                        networks.add(networkInfo)
                        if (networkInfo.isConnectedOrConnecting) {
                            activeNetworkInfo = networkInfo
                            isActive = true
                        }
                    }
                }
            }
        }

        return ConnectivityState(isActive, activeNetworkInfo, networks)
    }

    override fun registerCallback() {
        connectivityManager.registerNetworkCallback(builder.build(), networkCallback)
    }

    override fun unregisterCallback() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            notifyState()
        }

        override fun onLosing(network: Network, maxMsToLive: Int) {
            notifyState()
        }

    }

}
