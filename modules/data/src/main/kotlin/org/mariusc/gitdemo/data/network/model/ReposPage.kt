package org.mariusc.gitdemo.data.network.model


import android.arch.persistence.room.*
import org.mariusc.gitdemo.data.network.persistence.db.ReposDatabase
import kotlin.collections.emptyList;

/**
 * Created by MConstantin on 4/28/2017.
 */


@Entity(tableName = ReposDatabase.PAGES, primaryKeys = arrayOf("current_since"))
data class ReposPage(@ColumnInfo(name = "has_more") override val hasMore: Boolean = false,
                     @ColumnInfo(name = "is_first") override val isFirst: Boolean = false,
                     @Embedded val pageInfo: ReposPageInfo = ReposPageInfo()) : Page{
    @Ignore
    override var data: List<RepoModel> = emptyList()

    @Ignore
    constructor():this(false,false, ReposPageInfo())
}
