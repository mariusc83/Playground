package org.mariusc.gitdemo.view.main

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity

import org.mariusc.gitdemo.R
import org.mariusc.gitdemo.databinding.MainBinding

import javax.inject.Inject

import dagger.android.AndroidInjection
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasDispatchingSupportFragmentInjector

/**
 * Created by MConstantin on 4/25/2017.
 */

class MainActivity : AppCompatActivity(), HasDispatchingSupportFragmentInjector {
    @set:Inject
    lateinit internal var supportFragmentInjector: DispatchingAndroidInjector<Fragment>
    @set:Inject
    lateinit internal var flowController: FlowController
    lateinit internal var mainBinding: MainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        mainBinding = DataBindingUtil.setContentView<MainBinding>(this, R.layout.main)
        setSupportActionBar(mainBinding.toolbar)
        if (savedInstanceState == null)
            flowController!!.goToPublicRepos()
    }


    override fun supportFragmentInjector(): DispatchingAndroidInjector<Fragment> {
        return supportFragmentInjector
    }
}
