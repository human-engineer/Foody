package com.humansuit.foody.binding

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.humansuit.foody.R

@BindingAdapter("withImage")
fun ImageView.loadImage(imageUrl: String) {
    Glide.with(context)
        .load(imageUrl)
        .error(ColorDrawable(Color.RED))
        .placeholder(R.drawable.ic_food)
        .centerCrop()
        .into(this)
}


@BindingAdapter("gone")
fun ProgressBar.bindGone(state: Boolean) {
    Log.e("TAG", "bindGone: State -> $state")
    this.visibility = if (state) View.VISIBLE else View.INVISIBLE
}