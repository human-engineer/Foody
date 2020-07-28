package com.humansuit.foody.ui.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.humansuit.foody.databinding.RecipeItemBinding
import com.humansuit.foody.model.Recipe

class RecipeSectionAdapter(private val viewModel: LoungeViewModel)
    : RecyclerView.Adapter<RecipeSectionAdapter.ViewHolder>() {


    private var items: List<Recipe>? = null

    inner class ViewHolder(val binding: RecipeItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Recipe) {
            binding.apply {
                recipeNameText.text = item.title
                Glide.with(binding.root.context)
                    .asDrawable()
                    .load(item.image)
                    .centerInside()
                    .skipMemoryCache(true)
                    //.apply(RequestOptions().transform(RoundedCorners(30)))
                    .into(binding.recipeImage)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RecipeItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = items?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        items?.get(position)?.let { holder.bind(it) }
    }


    fun setupList(recipes: List<Recipe>) {
        items = recipes
        notifyDataSetChanged()
    }


}