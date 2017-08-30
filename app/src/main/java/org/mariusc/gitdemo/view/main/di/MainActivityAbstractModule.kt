package org.mariusc.gitdemo.view.main.di

import android.app.Activity
import dagger.Binds
import dagger.Module
import dagger.android.ActivityKey
import dagger.android.AndroidInjector
import dagger.multibindings.IntoMap
import org.mariusc.gitdemo.view.main.MainActivity

/**
 * Created by MConstantin on 5/26/2017.
 */
@Module(subcomponents = arrayOf(MainActivitySubcomponent::class))
abstract class MainActivityAbstractModule {
    @Binds
    @IntoMap
    @ActivityKey(MainActivity::class)
    internal abstract fun bindInjectorFactory(builder: MainActivitySubcomponent.Builder): AndroidInjector.Factory<out Activity>
}