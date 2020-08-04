package com.humansuit.foody.binding

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.humansuit.foody.R

@BindingAdapter("withImage")
fun ImageView.loadImage(imageUrl: String?) {
    Glide.with(context)
        .load(imageUrl)
        .placeholder(R.drawable.ic_recipe_placeholder_temp)
        .error(R.drawable.ic_recipe_placeholder_temp)
        .centerCrop()
        .into(this)
}


@BindingAdapter("gone")
fun ProgressBar.bindGone(state: Boolean) {
    this.visibility = if (state) View.VISIBLE else View.INVISIBLE
}