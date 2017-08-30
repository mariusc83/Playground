package org.mariusc.gitdemo.data.persistence.dao

import android.support.test.runner.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import org.mariusc.gitdemo.data.network.model.ReposPage
import org.mariusc.gitdemo.data.network.model.ReposPageInfo
import org.mariusc.gitdemo.data.network.persistence.dao.PagesDao

/**
 * Created by MConstantin on 8/28/2017.
 */
@RunWith(AndroidJUnit4::class)
class PagesDaoTest : BaseDaoTest() {
    val pagesDao: PagesDao by lazy {
        db.pagesDao()
    }

    @Test
    fun adds_a_new_page_when_required() {
        // given
        val pageToAdd = ReposPage(false, true, ReposPageInfo())

        // when
        val rowId = pagesDao.insert(pageToAdd)

        // then
        assert(rowId == 1L)
    }

    @Test
    fun returns_the_requested_page_when_required() {
        // given
        val since = "300"
        val nextSince = "600"
        val pageToAdd = ReposPage(false, true, ReposPageInfo(since, nextSince, true))
        pagesDao.insert(pageToAdd)

        // when
        val addedPage = pagesDao.reposPage(since)

        // then
        assert(addedPage.hasMore == false)
        assert(addedPage.isFirst)
        assert(addedPage.pageInfo.currentSince == since)
        assert(addedPage.pageInfo.nextSince == nextSince)
    }


    @Test
    fun returns_all_pages_when_requested() {
        // given
        pagesDao.insert(ReposPage(false, true, ReposPageInfo()))
        pagesDao.insert(ReposPage(false, true, ReposPageInfo()))

        // when
        val pages = pagesDao.pages()

        // then
        assert(pages.size == 2)
    }


    @Test
    fun removes_a_page_when_requested() {
        // given
        val since = "300"
        val nextSince = "600"
        val pageToAdd = ReposPage(false, true, ReposPageInfo(since, nextSince, true))
        pagesDao.insert(pageToAdd)

        // when
        val deletedRows = pagesDao.delete(pageToAdd)

        // then
        assert(pagesDao.pages().size == 0)
    }

    @Test
    fun removes_a_page_by_id_when_requested() {
        // given
        val since = "300"
        val nextSince = "600"
        val pageToAdd = ReposPage(false, true, ReposPageInfo(since, nextSince, true))
        pagesDao.insert(pageToAdd)

        // when
        val deletedRows = pagesDao.delete(withSince = since)

        // then
        assert(pagesDao.pages().size == 0)
    }

    @Test
    fun removes_multiple_pages_at_once_when_requested() {
        // given
        val pageToAdd1 = ReposPage(false, true, ReposPageInfo())
        val pageToAdd2 = ReposPage(false, true, ReposPageInfo())
        pagesDao.insert(pageToAdd1)
        pagesDao.insert(pageToAdd2)

        // when
        val deletedRows = pagesDao.delete(pageToAdd1, pageToAdd2)

        // then
        assert(pagesDao.pages().size == 0)
    }
}