package dao

import android.support.test.runner.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import org.mariusc.gitdemo.data.network.model.RepoModel
import org.mariusc.gitdemo.data.network.persistence.dao.ReposDao
import org.mariusc.gitdemo.data.persistence.dao.BaseDaoTest
import org.junit.Assert.fail
import org.mariusc.gitdemo.data.network.model.ReposPage
import org.mariusc.gitdemo.data.network.persistence.dao.PagesDao

/**
 * Created by MConstantin on 8/31/2017.
 */
@RunWith(AndroidJUnit4::class)
class ReposDaoTest : BaseDaoTest() {

    val reposDao: ReposDao by lazy {
        db.reposDao()
    }

    val pagesDao: PagesDao by lazy {
        db.pagesDao()
    }

    @Test
    fun throws_an_exception_when_inserting_a_repo_without_a_relation() {
        // given
        val repo = RepoModel(pageId = "1")

        // when
        try {
            reposDao.insert(repo)
            fail("An exception was expected here")
        } catch (e: Exception) {
            // everything is fine
            print(e.message)
        }
    }

    @Test
    fun removes_a_repo_when_requested() {
        // given
        val page = ReposPage()
        val repo = RepoModel(pageId = page.pageInfo.currentSince)
        pagesDao.insert(page)
        reposDao.insert(repo)

        // when
        reposDao.delete(repo)

        // then
        assert(reposDao.repos(page.pageInfo.currentSince).size == 0)
    }

    @Test
    fun removes_all_related_repos_when_removing_a_page() {
        // given
        val page = ReposPage()
        val repo1 = RepoModel(pageId = page.pageInfo.currentSince)
        val repo2 = RepoModel(pageId = page.pageInfo.currentSince)
        pagesDao.insert(page)
        reposDao.insert(repo1)
        reposDao.insert(repo2)

        // when
        pagesDao.delete(page)

        // then
        assert(reposDao.repos(page.pageInfo.currentSince).size == 0)
    }

    @Test
    fun updates_all_related_repos_when_updating_a_page() {
        // given
        val page = ReposPage()
        val repo1 = RepoModel(pageId = page.pageInfo.currentSince)
        val repo2 = RepoModel(pageId = page.pageInfo.currentSince)
        pagesDao.insert(page)
        reposDao.insert(repo1)
        reposDao.insert(repo2)

        // when
        pagesDao.update(page)

        // then
        val repos = reposDao.repos(page.pageInfo.currentSince)
        repos.forEach {
            assert(it.pageId == "23")
        }
        assert(repos.size == 2)
    }

    @Test
    fun inserts_a_repo_when_requested() {
        // given
        val page = ReposPage()
        val repo = RepoModel(pageId = page.pageInfo.currentSince)

        // when
        pagesDao.insert(page)
        val rowId = reposDao.insert(repo)

        // then
        assert(rowId > 0)
    }


    @Test
    fun returns_all_repos_by_page_id_when_requested() {

    }

}