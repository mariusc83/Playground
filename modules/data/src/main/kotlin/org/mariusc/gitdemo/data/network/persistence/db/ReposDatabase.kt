package org.mariusc.gitdemo.data.network.persistence.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import org.mariusc.gitdemo.data.network.model.ReposPage
import org.mariusc.gitdemo.data.network.persistence.dao.PagesDao

/**
 * Created by MConstantin on 8/11/2017.
 */

@Database(entities = arrayOf(ReposPage::class), version = 1)
abstract class ReposDatabase:RoomDatabase() {

    abstract fun pagesDao(): PagesDao
}