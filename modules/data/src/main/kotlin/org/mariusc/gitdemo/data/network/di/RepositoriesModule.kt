package org.mariusc.gitdemo.data.network.di

import dagger.Module
import dagger.Provides
import org.mariusc.gitdemo.data.network.IGitApiService
import org.mariusc.gitdemo.data.network.repository.IReposRepository
import org.mariusc.gitdemo.data.network.repository.NetworkReposRepository
import org.mariusc.gitdemo.data.network.repository.ReposRepository
import org.mariusc.gitdemo.data.network.repository.parse.ReposPageParser

/**
 * Created by MConstantin on 5/26/2017.
 */
@Module(subcomponents = arrayOf(RepositoriesSubComponent::class))
class RepositoriesModule {

    @Provides
    fun providePageParser(): ReposPageParser {
        return ReposPageParser()
    }

    @Provides
    fun provideNetworkRepository(networkApi: IGitApiService,
                                 pageParser: ReposPageParser): NetworkReposRepository {
        return NetworkReposRepository(networkApi, pageParser)
    }

    @Provides
    fun provideReposRepository(networkReposRepository: NetworkReposRepository): IReposRepository {
        return ReposRepository(networkReposRepository)
    }
}