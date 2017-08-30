package org.mariusc.gitdemo

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by MConstantin on 5/30/2017.
 */
val GIT_API_REST_URL: String = "https://api.github.com"


inline fun <reified T : Parcelable> createParcel(crossinline factory: (parcel: Parcel) -> T): Parcelable
.Creator<T> {
    return object : Parcelable.Creator<T> {
        override fun createFromParcel(source: Parcel?): T {
            val nonNullSource = source!!
            return factory.invoke(source)
        }

        override fun newArray(size: Int): Array<T?> {
            return arrayOfNulls<T>(size)
        }
    }
}