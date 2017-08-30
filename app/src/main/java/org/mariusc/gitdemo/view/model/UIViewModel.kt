package org.mariusc.gitdemo.view.model

import android.databinding.BaseObservable

import com.google.auto.value.AutoValue

/**
 * Created by Marius on 4/1/2017.
 */

interface UIViewModel {
    val errorMessage: String
    val isSuccess: Boolean
    val inProgress: Boolean
}