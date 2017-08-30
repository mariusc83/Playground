package org.mariusc.gitdemo.di

import dagger.Component
import org.mariusc.gitdemo.GitDemoApplication
import org.mariusc.gitdemo.data.network.IGitApiService
import org.mariusc.gitdemo.view.main.di.MainActivityAbstractModule
import javax.inject.Singleton


@Component(modules = arrayOf(GitDemoAppModule::class, MainActivityAbstractModule::class))
@Singleton
interface GitDemoAppComponent {

    fun apiService(): IGitApiService

    fun inject(gitDemoApplication: GitDemoApplication)

}
