package org.mariusc.gitdemo.view.main.repos.di

import dagger.Module
import dagger.Provides
import io.reactivex.Observable
import org.mariusc.gitdemo.data.network.repository.IReposRepository
import org.mariusc.gitdemo.provider.TextProvider
import org.mariusc.gitdemo.view.main.repos.ReposFragmentPresenter
import org.mariusc.gitdemo.view.main.repos.ReposInteractor
import org.mariusc.gitdemo.view.utils.ErrorResolver
import org.mariusc.system.connectivity.ConnectivityState
import org.mariusc.system.log.ILogger

/**
 * Created by MConstantin on 5/26/2017.
 */
@Module
class ReposFragmentModule {

    @Provides
    fun providePresenter(reposInteractor: ReposInteractor, textProvider: TextProvider, logger: ILogger, errorResolver: ErrorResolver): ReposFragmentPresenter {
        return ReposFragmentPresenter(reposInteractor, logger, errorResolver)
    }

    @Provides
    fun provideInteractor(reposRepository: IReposRepository,
                          observable: Observable<ConnectivityState>): ReposInteractor {
        return ReposInteractor(reposRepository, observable)
    }
}