package org.mariusc.gitdemo.data.network.persistence.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import org.mariusc.gitdemo.data.network.model.RepoModel
import org.mariusc.gitdemo.data.network.model.ReposPage
import org.mariusc.gitdemo.data.network.persistence.dao.PagesDao
import org.mariusc.gitdemo.data.network.persistence.dao.ReposDao

/**
 * Created by MConstantin on 8/11/2017.
 */

@Database(entities = arrayOf(ReposPage::class, RepoModel::class), version = 1)
abstract class ReposDatabase : RoomDatabase() {
    companion object {
        const val PAGES: String = "pages"
        const val REPOS: String = "repos"
    }


    abstract fun pagesDao(): PagesDao

    abstract fun reposDao(): ReposDao
}