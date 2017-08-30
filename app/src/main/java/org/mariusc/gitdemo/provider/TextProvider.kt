package org.mariusc.gitdemo.provider

import android.content.Context
import android.support.annotation.StringRes

/**
 * Created by Marius on 4/1/2017.
 */


open class TextProvider(private val context: Context) {

    fun getString(@StringRes stringId: Int): String {
        return this.context.getString(stringId)
    }
}
