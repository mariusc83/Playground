package org.mariusc.gitdemo.data.network.persistence.dao

import android.arch.persistence.room.*
import org.mariusc.gitdemo.data.network.model.RepoModel
import org.mariusc.gitdemo.data.network.persistence.db.ReposDatabase

/**
 * Created by MConstantin on 8/31/2017.
 */
@Dao
interface ReposDao {

    @Query(value = "SELECT * FROM ${ReposDatabase.REPOS} WHERE page_id = :pageId")
    fun repos(pageId: String): List<RepoModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(repo: RepoModel): Long

    @Delete
    fun delete(repo:RepoModel)
}