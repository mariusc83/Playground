package org.mariusc.gitdemo.data.network.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import java.util.*


/**
 * Created by MConstantin on 4/28/2017.
 */

data class ReposPageInfo(@PrimaryKey @ColumnInfo(name = "current_since") var currentSince: String,
                         @ColumnInfo(name = "next_since") var nextSince: String,
                         @ColumnInfo(name = "has_next") var hasNext: Boolean){

    @Ignore constructor():this(UUID.randomUUID().toString(), "", false)
}


