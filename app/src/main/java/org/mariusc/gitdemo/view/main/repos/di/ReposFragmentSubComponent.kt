
package org.mariusc.gitdemo.view.main.repos.di

import dagger.Subcomponent
import dagger.android.AndroidInjector
import org.mariusc.gitdemo.di.scope.FragmentScope
import org.mariusc.gitdemo.view.main.repos.ReposFragment

/**
 * Created by MConstantin on 5/26/2017.
 */

@Subcomponent(modules = arrayOf(ReposFragmentModule::class))
@FragmentScope
interface ReposFragmentSubComponent:AndroidInjector<ReposFragment> {
    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<ReposFragment>()
}