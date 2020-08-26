package com.humansuit.foody.binding

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import com.andrognito.flashbar.Flashbar
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.humansuit.foody.R
import java.time.Duration

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
fun View.bindGone(state: Boolean) {
    this.visibility = if (state) View.VISIBLE else View.GONE
}


fun View.showErrorSnackBar(
    message: String,
    onRetryClick: () -> Unit,
) {
    startAlphaAnimation(1.0F, 0.2F, this)
    Snackbar.make(this, message, Snackbar.LENGTH_INDEFINITE)
        .setAction("Retry") {
            onRetryClick()
            startAlphaAnimation(0.2F, 1.0F, this)
        }
        .setActionTextColor(resources.getColor(R.color.colorPrimaryDark))
        .show()
}


private fun startAlphaAnimation(
    startPoint: Float, endPoint: Float,
    view: View, duration: Long = 300
) {
    val alphaAnimation = AlphaAnimation(startPoint, endPoint)
    alphaAnimation.duration = duration
    alphaAnimation.fillAfter = true
    view.startAnimation(alphaAnimation)
}