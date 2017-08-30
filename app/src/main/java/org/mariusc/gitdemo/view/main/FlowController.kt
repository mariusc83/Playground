package org.mariusc.gitdemo.view.main

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity

import org.mariusc.gitdemo.R
import org.mariusc.gitdemo.view.main.repos.ReposFragment


class FlowController(private val activity: FragmentActivity) {

    private fun switchToFragment(fragment: Fragment) {
        val supportFragmentManager = activity.supportFragmentManager
        val ft = supportFragmentManager.beginTransaction()
        if (supportFragmentManager.findFragmentById(R.id.fragment_container) != null) {
            ft.replace(R.id.fragment_container, fragment)
        } else {
            ft.add(R.id.fragment_container, fragment)
        }
        ft.commit()
    }

    fun goToPublicRepos() {
        switchToFragment(ReposFragment())
    }
}
