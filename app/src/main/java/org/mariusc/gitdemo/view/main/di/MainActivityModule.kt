package org.mariusc.gitdemo.view.main.di

import android.view.LayoutInflater
import dagger.Module
import dagger.Provides
import org.mariusc.gitdemo.data.network.IGitApiService
import org.mariusc.gitdemo.data.network.repository.IReposRepository
import org.mariusc.gitdemo.data.network.repository.NetworkReposRepository
import org.mariusc.gitdemo.data.network.repository.ReposRepository
import org.mariusc.gitdemo.data.network.repository.parse.ReposPageParser
import org.mariusc.gitdemo.di.scope.ActivityScope
import org.mariusc.gitdemo.provider.TextProvider
import org.mariusc.gitdemo.view.main.FlowController
import org.mariusc.gitdemo.view.main.MainActivity
import org.mariusc.gitdemo.view.utils.ErrorResolver

/**
 * Created by MConstantin on 5/22/2017.
 */
@Module
class MainActivityModule {
    @ActivityScope
    @Provides
    fun provideFlowController(activity: MainActivity): FlowController {
        return FlowController(activity)
    }

    @ActivityScope
    @Provides
    fun provideLayoutInflater(activity: MainActivity): LayoutInflater {
        return activity.layoutInflater
    }

    @ActivityScope
    @Provides
    fun provideReposRepository(apiService: IGitApiService): IReposRepository {
        return ReposRepository(NetworkReposRepository(apiService, ReposPageParser()))
    }

    @ActivityScope
    @Provides
    internal fun provideTextProvider(activity: MainActivity): TextProvider {
        return TextProvider(activity)
    }

    @ActivityScope
    @Provides
    internal fun provideErrorResolver(textProvider: TextProvider): ErrorResolver {
        return ErrorResolver(textProvider)
    }
}