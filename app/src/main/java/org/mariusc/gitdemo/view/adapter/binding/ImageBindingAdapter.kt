package org.mariusc.gitdemo.view.adapter.binding

import android.databinding.BindingAdapter
import android.graphics.drawable.Drawable
import android.widget.ImageView

/**
 * Created by MConstantin on 4/23/2017.
 */

interface ImageBindingAdapter {
    @BindingAdapter(value = *arrayOf("imageUrl", "errorPlaceholder"), requireAll = false)
    fun loadImage(imageView: ImageView, imageUrl: String?, onError: Drawable?)
}
