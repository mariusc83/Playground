package org.mariusc.system.connectivity

import android.net.NetworkInfo

import com.google.auto.value.AutoValue

/**
 * Created by MConstantin on 4/4/2017.
 */
data class ConnectivityState(val isActive: Boolean = false, val activeNetworkInfo:
NetworkInfo? = null, val networks: List<NetworkInfo>? = null) {
    companion object {

        fun notActive(): ConnectivityState {
            return ConnectivityState(false)
        }

        fun active(): ConnectivityState {
            return ConnectivityState(true)
        }
    }
}
