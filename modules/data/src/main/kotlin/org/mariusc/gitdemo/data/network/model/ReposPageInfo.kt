package org.mariusc.gitdemo.data.network.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import java.util.*


/**
 * Created by MConstantin on 4/28/2017.
 */

data class ReposPageInfo(@PrimaryKey @ColumnInfo(name = "current_since") val currentSince: String = "",
                         @ColumnInfo(name = "next_since") val nextSince: String = "",
                         @ColumnInfo(name = "has_next") val hasNext: Boolean = false) {

    @Ignore constructor() : this(UUID.randomUUID().toString(), "", false)
}


