package org.mariusc.gitdemo

import android.app.Activity
import android.app.Application
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasDispatchingActivityInjector
import org.mariusc.gitdemo.di.DaggerGitDemoAppComponent
import org.mariusc.gitdemo.di.GitDemoAppComponent
import org.mariusc.gitdemo.di.GitDemoAppModule
import javax.inject.Inject


class GitDemoApplication : Application(), HasDispatchingActivityInjector {
    @Inject
    lateinit var dispatchingAndroidInject: DispatchingAndroidInjector<Activity>;

    lateinit var appComponent: GitDemoAppComponent;

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerGitDemoAppComponent.builder().gitDemoAppModule(GitDemoAppModule
        (this, GIT_API_REST_URL)).build()
        appComponent.inject(this);
    }

    override fun activityInjector(): DispatchingAndroidInjector<Activity> {
        return dispatchingAndroidInject
    }
}
