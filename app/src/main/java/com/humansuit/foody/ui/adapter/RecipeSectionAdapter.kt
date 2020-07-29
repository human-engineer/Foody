package com.humansuit.foody.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.humansuit.foody.databinding.RecipeSectionBinding
import com.humansuit.foody.model.RecipeSection
import com.humansuit.foody.utils.CustomViewHolder

class RecipeSectionAdapter : ListAdapter<RecipeSection, CustomViewHolder>(Companion) {

    private val viewPool = RecyclerView.RecycledViewPool()


    companion object : DiffUtil.ItemCallback<RecipeSection>() {
        override fun areItemsTheSame(oldItem: RecipeSection, newItem: RecipeSection): Boolean {
            return  oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: RecipeSection, newItem: RecipeSection): Boolean {
            return  oldItem.id == newItem.id
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RecipeSectionBinding.inflate(inflater, parent, false)
        return CustomViewHolder(binding)
    }


    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val currentRecipeSection = getItem(position)
        val sectionBinding = holder.binding as RecipeSectionBinding

        sectionBinding.recipeSection = currentRecipeSection
        sectionBinding.recipeRecyclerView.setHasFixedSize(true)
        sectionBinding.recipeRecyclerView.setItemViewCacheSize(20)
        sectionBinding.recipeRecyclerView.setRecycledViewPool(viewPool)
        sectionBinding.executePendingBindings()
    }


}