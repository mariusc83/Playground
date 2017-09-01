package org.mariusc.gitdemo.data.network.persistence.dao

import android.arch.persistence.room.*
import org.mariusc.gitdemo.data.network.DB
import org.mariusc.gitdemo.data.network.model.Page
import org.mariusc.gitdemo.data.network.model.ReposPage
import org.mariusc.gitdemo.data.network.persistence.db.ReposDatabase

/**
 * Created by MConstantin on 8/11/2017.
 */
@Dao
interface PagesDao {

    @Query(value = "SELECT * FROM ${ReposDatabase.PAGES} WHERE current_since = :forSince")
    fun reposPage(forSince: String): ReposPage

    @Query(value = "SELECT * FROM ${ReposDatabase.PAGES}")
    fun pages(): List<ReposPage>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(page: ReposPage): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(page: ReposPage)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(vararg pages: ReposPage)

    @Delete
    fun delete(page: ReposPage)

    @Delete
    fun delete(vararg page: ReposPage)

    @Query(value = "DELETE FROM ${ReposDatabase.PAGES} WHERE current_since= :withSince")
    fun delete(withSince: String): Int
}