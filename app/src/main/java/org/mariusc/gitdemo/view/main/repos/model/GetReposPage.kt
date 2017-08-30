package org.mariusc.gitdemo.view.main.repos.model

import android.os.Parcel
import android.os.Parcelable

import com.google.auto.value.AutoValue
import org.mariusc.gitdemo.createParcel

import org.mariusc.gitdemo.view.model.BaseUIAction

/**
 * Created by MConstantin on 5/1/2017.
 */

data class GetReposPage constructor(val since: String = "") : BaseUIAction {

    constructor(parcel: Parcel) : this(parcel.readString())

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(since)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<GetReposPage> = createParcel { GetReposPage(it) }
    }
}
