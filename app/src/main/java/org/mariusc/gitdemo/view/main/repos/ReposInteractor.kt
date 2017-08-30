package org.mariusc.gitdemo.view.main.repos

import org.mariusc.gitdemo.data.network.repository.IReposRepository
import org.mariusc.gitdemo.data.network.model.ReposPage
import org.mariusc.system.connectivity.ConnectivityState

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

/**
 * Created by MConstantin on 5/1/2017.
 */

class ReposInteractor(private val reposRepository: IReposRepository,
                      private val connectivityStateObservable: Observable<ConnectivityState>) {


    fun publicReposPage(since: String): Observable<ReposPage> {
        return reposRepository.publicRepositories(since).toObservable()
    }

    fun allPublicReposPagesUpUntil(since: String): Observable<ReposPage> {

        return Observable
                .just(since)
                .switchMap {
                    connectivityStateObservable.distinctUntilChanged { t1, t2 ->
                        t1.isActive == t2.isActive
                    }
                }
                .switchMap { (isActive, activeNetworkInfo, networks) -> reposRepository.allPagesUpToSince(since) }
                .subscribeOn(Schedulers.io())
    }
}
