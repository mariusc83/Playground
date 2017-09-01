package org.mariusc.gitdemo.data.network.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * Created by MConstantin on 5/30/2017.
 */

data class RepoOwnerModel(@field:SerializedName("id") val id: Long = System.nanoTime(),
                          @field:SerializedName("avatar_url") val avatarUrl: String = "",
                          @field:SerializedName("login") val login: String = "") {
    @Ignore constructor() : this(System.nanoTime(), "", "")
}