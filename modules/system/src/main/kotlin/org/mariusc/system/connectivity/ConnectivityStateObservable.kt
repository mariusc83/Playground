package org.mariusc.system.connectivity

import org.mariusc.system.connectivity.supplier.IConnectionSupplier

import java.util.concurrent.atomic.AtomicBoolean

import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * Created by MConstantin on 4/4/2017.
 */

class ConnectivityStateObservable(private val supplier: IConnectionSupplier) :
        Observable<ConnectivityState>() {

    override fun subscribeActual(observer: Observer<in ConnectivityState>) {
        val toReturn = ConnectionObserver(supplier, observer)
        observer.onSubscribe(toReturn)
        supplier.registerConsumer(toReturn)
    }

    class ConnectionObserver internal constructor(private val supplier: IConnectionSupplier,
                                                   private val observer: Observer<in ConnectivityState>) : Disposable {

        private val unsubscribed = AtomicBoolean()

        override fun dispose() {
            if (unsubscribed.compareAndSet(false, true)) {
                supplier.release()
            }
        }

        override fun isDisposed(): Boolean {
            return unsubscribed.get()
        }

        fun onNext(connectivityState: ConnectivityState) {
            if (!isDisposed) observer.onNext(connectivityState)
        }

        fun onError(connectivityState: ConnectivityState) {
            if (!isDisposed) observer.onNext(connectivityState)
        }
    }

}
