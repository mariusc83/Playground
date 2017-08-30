package org.mariusc.gitdemo.view.adapter.binding

import android.graphics.drawable.Drawable
import android.widget.ImageView

import com.bumptech.glide.Glide

/**
 * Created by MConstantin on 4/23/2017.
 */

class DefaultImageBindingAdapter : ImageBindingAdapter {

    override fun loadImage(imageView: ImageView, imageUrl: String?, onError: Drawable?) {
        if (onError != null && imageUrl != null) {
            Glide.with(imageView.context).load(imageUrl).error(onError).centerCrop().into(imageView)
        } else if (onError != null) {
            imageView.setImageDrawable(onError)
        } else {
            Glide.with(imageView.context).load(imageUrl).centerCrop().into(imageView)
        }
    }

}
