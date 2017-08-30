package org.mariusc.gitdemo.view.adapter.binding


/**
 * Created by MConstantin on 4/23/2017.
 */

class DefaultRowBindingComponent : android.databinding.DataBindingComponent {

    override fun getImageBindingAdapter(): ImageBindingAdapter {
        return DefaultImageBindingAdapter()
    }
}
