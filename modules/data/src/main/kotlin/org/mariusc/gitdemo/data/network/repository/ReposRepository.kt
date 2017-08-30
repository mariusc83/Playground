package org.mariusc.gitdemo.data.network.repository

import org.mariusc.gitdemo.data.network.model.ReposPage

import io.reactivex.Observable
import io.reactivex.Single

/**
 * Created by MConstantin on 5/1/2017.
 */

open class ReposRepository(private val networkReposRepository: NetworkReposRepository) :
        IReposRepository {

    companion object {
        @field:JvmField
        val myClass = ReposRepository::class.java.simpleName.toString();
    }

    override fun publicRepositories(since: String): Single<ReposPage> {
        return networkReposRepository
                .fetchRepositories(since)
                .singleOrError()
    }

    override fun publicRepositories(): Single<ReposPage> {
        return publicRepositories("0")
    }

    override fun allPagesUpToSince(since3: String): Observable<ReposPage> {
        val upToSince = getSinceAsInt(since3)
        return allPagesWithLimit("0", upToSince)
    }

    private fun allPagesWithLimit(since: String, sinceLimit: Int): Observable<ReposPage> {
        return networkReposRepository
                .fetchRepositories(since)
                .flatMap { reposPage ->
                    if (canStillTake(reposPage, sinceLimit))
                        Observable
                                .just(reposPage)
                                .concatWith(allPagesWithLimit(reposPage.pageInfo.nextSince, sinceLimit))
                    else
                        Observable.just(reposPage)
                }
    }

    private fun canStillTake(@io.reactivex.annotations.NonNull reposPage: ReposPage, upToSince: Int): Boolean {
        return getSinceAsInt(reposPage.pageInfo.currentSince) < upToSince
    }

    private fun getSinceAsInt(since: String): Int {
        return if (since.isEmpty()) 0 else Integer.parseInt(since)
    }
}
