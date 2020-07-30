package com.humansuit.foody.binding

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.humansuit.foody.R

@BindingAdapter("withImage")
fun ImageView.loadImage(imageUrl: String?) {
    Glide.with(context)
        .load(imageUrl)
        .placeholder(R.drawable.ic_recipe_placeholder_temp)
        .error(R.drawable.ic_recipe_placeholder_temp)
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .centerCrop()
        .into(this)
}


@BindingAdapter("gone")
fun ProgressBar.bindGone(state: Boolean) {
    Log.e("TAG", "bindGone: State -> $state")
    this.visibility = if (state) View.VISIBLE else View.INVISIBLE
}