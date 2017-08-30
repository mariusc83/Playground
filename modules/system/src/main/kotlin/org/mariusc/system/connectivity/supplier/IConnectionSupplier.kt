package org.mariusc.system.connectivity.supplier

import org.mariusc.system.connectivity.ConnectivityStateObservable


/**
 * Created by MConstantin on 4/4/2017.
 */

interface IConnectionSupplier {
    fun release()

    fun registerConsumer(consumer: ConnectivityStateObservable.ConnectionObserver): IConnectionSupplier

}
