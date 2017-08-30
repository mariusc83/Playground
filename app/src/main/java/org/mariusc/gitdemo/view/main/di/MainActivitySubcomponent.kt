package org.mariusc.gitdemo.view.main.di

import dagger.Subcomponent
import dagger.android.AndroidInjector
import org.mariusc.gitdemo.di.scope.ActivityScope
import org.mariusc.gitdemo.view.main.MainActivity
import org.mariusc.gitdemo.view.main.repos.di.ReposFragmentAbstractModule

/**
 * Created by MConstantin on 5/26/2017.
 */
@Subcomponent(modules = arrayOf(MainActivityModule::class, ReposFragmentAbstractModule::class))
@ActivityScope
interface MainActivitySubcomponent:AndroidInjector<MainActivity> {
    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<MainActivity>()
}