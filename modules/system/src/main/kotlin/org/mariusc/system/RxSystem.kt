package org.mariusc.system

import android.annotation.SuppressLint
import android.content.Context
import android.net.NetworkRequest
import android.os.Build

import org.mariusc.system.connectivity.ConnectivityStateObservable
import org.mariusc.system.connectivity.supplier.LollipopSupplier
import org.mariusc.system.connectivity.supplier.PreLollipopSupplier

/**
 * Created by MConstantin on 4/23/2017.
 */

object RxSystem {

    @SuppressLint("NewApi")
    fun connectivityStateObservable(context: Context): ConnectivityStateObservable {
        return ConnectivityStateObservable(if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            PreLollipopSupplier(context)
        else
            LollipopSupplier(context, NetworkRequest.Builder()))
    }

}
