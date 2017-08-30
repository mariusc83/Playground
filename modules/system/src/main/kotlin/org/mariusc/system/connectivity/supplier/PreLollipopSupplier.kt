package org.mariusc.system.connectivity.supplier

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkInfo

import org.mariusc.system.connectivity.ConnectivityState

import java.util.ArrayList
import java.util.Collections


/**
 * Created by MConstantin on 4/4/2017.
 */

internal class PreLollipopSupplier(context: Context) : BaseConnectionSupplier(context) {


    override fun resolveConnectivityState(): ConnectivityState {
        val networks: MutableList<NetworkInfo> = mutableListOf<NetworkInfo>()
        var isActive = false
        var activeNetworkInfo: NetworkInfo? = null

        val allNetworks = connectivityManager.allNetworkInfo
        if (allNetworks != null) {
            for (network in allNetworks) {
                if (network != null) {
                    networks.add(network)
                    if (network.isConnectedOrConnecting) {
                        activeNetworkInfo = network
                        isActive = true
                    }
                }

            }
        }
        return ConnectivityState(isActive, activeNetworkInfo, networks)
    }

    override fun registerCallback() {
        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        context.registerReceiver(broadcastReceiver, intentFilter)
    }

    override fun unregisterCallback() {
        context.unregisterReceiver(broadcastReceiver)
    }

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            if (intent != null && ConnectivityManager.CONNECTIVITY_ACTION == intent.action) {
                notifyState()
            }
        }
    }
}
