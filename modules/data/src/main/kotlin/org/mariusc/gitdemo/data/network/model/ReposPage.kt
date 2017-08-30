package org.mariusc.gitdemo.data.network.model


import android.arch.persistence.room.*
import kotlin.collections.emptyList;

/**
 * Created by MConstantin on 4/28/2017.
 */


@Entity(tableName = "pages",primaryKeys = arrayOf("current_since"))
data class ReposPage(@ColumnInfo(name = "has_more") override var hasMore: Boolean = false,
                     @ColumnInfo(name = "is_first") override var isFirst: Boolean = false,
                     @Embedded
                     var pageInfo: ReposPageInfo = ReposPageInfo(),
                     @Ignore override var data: List<RepoModel> = emptyList()) : Page
