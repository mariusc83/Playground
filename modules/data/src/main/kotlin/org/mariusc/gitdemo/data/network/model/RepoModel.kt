package org.mariusc.gitdemo.data.network.model

import android.arch.persistence.room.*
import com.google.gson.annotations.SerializedName
import org.mariusc.gitdemo.data.network.persistence.db.ReposDatabase

/**
 * Created by MConstantin on 5/30/2017.
 */
@Entity(tableName = ReposDatabase.REPOS, foreignKeys = arrayOf(
        ForeignKey(entity = ReposPage::class,
                parentColumns = arrayOf("current_since"),
                childColumns = arrayOf("page_id"),
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE)))
data class RepoModel(@PrimaryKey @ColumnInfo(name = "id") @field:SerializedName("id") val id: Long = 0L,
                     @field:SerializedName("name") val name: String = "",
                     @ColumnInfo(name = "full_name") @field:SerializedName("full_name") val fullName: String = "",
                     @field:SerializedName("description") val description: String = "",
                     @Transient @ColumnInfo(name = "page_id") val pageId: String = "") {

    @Ignore @field:SerializedName("owner") var owner: RepoOwnerModel = RepoOwnerModel()

    @Ignore constructor() : this(System.nanoTime(), "", "", "")
}