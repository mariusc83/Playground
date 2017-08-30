package org.mariusc.gitdemo.data.network.di

import dagger.Subcomponent
import org.mariusc.gitdemo.data.network.repository.IReposRepository

/**
 * Created by MConstantin on 5/26/2017.
 */
@Subcomponent
interface RepositoriesSubComponent {
    fun reposRepository(): IReposRepository

    @Subcomponent.Builder
    interface Builder {
        fun build(): RepositoriesSubComponent
    }
}