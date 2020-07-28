package com.humansuit.foody.binding


import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.humansuit.foody.model.Recipe
import kotlinx.coroutines.flow.Flow

@BindingAdapter("adapter")
fun bindAdapter(view: RecyclerView, adapter: RecyclerView.Adapter<*>) {
    view.adapter = adapter
}
