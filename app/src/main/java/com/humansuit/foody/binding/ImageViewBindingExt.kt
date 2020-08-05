package com.humansuit.foody.binding

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.humansuit.foody.R

@BindingAdapter("withImageUrl")
fun ImageView.loadImage(imageUrl: String?) {
    Glide.with(context)
        .load(imageUrl)
        .placeholder(R.drawable.ic_recipe_placeholder_temp)
        .error(R.drawable.ic_recipe_placeholder_temp)
        .centerCrop()
        .into(this)
}


@BindingAdapter("withImageResource")
fun ImageView.loadImageResource(imageResource: Int) {
    Glide.with(context)
        .load(imageResource)
        .placeholder(R.drawable.ic_recipe_placeholder_temp)
        .error(R.drawable.ic_recipe_placeholder_temp)
        .centerCrop()
        .into(this)
}



@BindingAdapter("gone")
fun ProgressBar.bindGone(state: Boolean) {
    this.visibility = if (state) View.VISIBLE else View.GONE
}