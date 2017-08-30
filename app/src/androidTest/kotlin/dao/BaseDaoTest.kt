package org.mariusc.gitdemo.data.persistence.dao

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import org.junit.After
import org.junit.Before
import org.mariusc.gitdemo.data.network.persistence.db.ReposDatabase
import kotlin.properties.Delegates

/**
 * Created by MConstantin on 8/28/2017.
 */
abstract class BaseDaoTest {

    protected var db: ReposDatabase by Delegates.notNull<ReposDatabase>()


    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(), ReposDatabase::class.java).build();
    }


    @After
    fun cleanup() {
        db.close()
    }

}