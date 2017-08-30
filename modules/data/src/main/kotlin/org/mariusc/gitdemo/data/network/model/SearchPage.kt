package org.mariusc.gitdemo.data.network.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Ignore


/**
 * Created by MConstantin on 4/28/2017.
 */

data class SearchPage(@ColumnInfo(name = "has_more") override val hasMore: Boolean = false,
                      @ColumnInfo(name = "is_first") override val isFirst: Boolean = false,
                      @Ignore override val data: List<RepoModel> = emptyList(),
                      @Embedded
                      val pageInfo: SearchPageInfo = SearchPageInfo()) : Page
