package org.mariusc.system.connectivity.supplier


import android.content.Context
import android.net.ConnectivityManager

import org.mariusc.system.connectivity.ConnectivityState
import org.mariusc.system.connectivity.ConnectivityStateObservable

import java.util.concurrent.atomic.AtomicReference


/**
 * Created by MConstantin on 4/4/2017.
 */

internal abstract class BaseConnectionSupplier protected constructor(protected val context: Context) : IConnectionSupplier {

    protected var connectivityManager: ConnectivityManager

    private val consumerReference = AtomicReference<ConnectivityStateObservable.ConnectionObserver>()

    init {
        this.connectivityManager = context.getSystemService(Context
                .CONNECTIVITY_SERVICE) as ConnectivityManager
    }


    override fun release() {
        val current = consumerReference.get()
        if (current != null) {
            consumerReference.compareAndSet(current, null)
        }
        unregisterCallback()
    }

    override fun registerConsumer(consumer: ConnectivityStateObservable.ConnectionObserver): IConnectionSupplier {
        if (consumerReference.compareAndSet(null, consumer)) {
            notifyState(consumer)
            registerCallback()
        }
        return this
    }


    fun notifyState(consumer: ConnectivityStateObservable.ConnectionObserver) {
        consumer.onNext(resolveConnectivityState())
    }

    fun notifyState() {
        val consumer = consumerReference.get()
        consumer?.onNext(resolveConnectivityState())
    }

    protected abstract fun registerCallback()

    protected abstract fun unregisterCallback()

    protected abstract fun resolveConnectivityState(): ConnectivityState
}
