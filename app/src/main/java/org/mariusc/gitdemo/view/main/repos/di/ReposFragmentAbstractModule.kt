package org.mariusc.gitdemo.view.main.repos.di

import android.support.v4.app.Fragment
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.support.FragmentKey
import dagger.multibindings.IntoMap
import org.mariusc.gitdemo.view.main.repos.ReposFragment

/**
 * Created by MConstantin on 5/26/2017.
 */
@Module(subcomponents = arrayOf(ReposFragmentSubComponent::class))
abstract class ReposFragmentAbstractModule {
    @Binds
    @IntoMap
    @FragmentKey(ReposFragment::class)
    abstract fun provideInjectorFactory(builder: ReposFragmentSubComponent.Builder): AndroidInjector.Factory<out Fragment>
}