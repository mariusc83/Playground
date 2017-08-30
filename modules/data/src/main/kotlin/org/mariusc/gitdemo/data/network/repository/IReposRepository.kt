package org.mariusc.gitdemo.data.network.repository

import org.mariusc.gitdemo.data.network.model.ReposPage

import io.reactivex.Observable
import io.reactivex.Single

/**
 * Created by MConstantin on 5/1/2017.
 */

interface IReposRepository {

    fun publicRepositories(since: String): Single<ReposPage>
    fun publicRepositories(): Single<ReposPage>

    fun allPagesUpToSince(since3: String): Observable<ReposPage>
}
