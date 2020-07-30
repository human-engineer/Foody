package com.humansuit.foody.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.humansuit.foody.databinding.RecipeItemBinding
import com.humansuit.foody.model.Recipe
import com.humansuit.foody.utils.CustomViewHolder


class RecipeListAdapter : ListAdapter<Recipe, CustomViewHolder>(Companion) {

    companion object : DiffUtil.ItemCallback<Recipe>() {
        override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
            return  oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
            return  oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RecipeItemBinding.inflate(inflater, parent, false)
        return CustomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val currentRecipe = getItem(position)
        val recipeBinding = holder.binding as RecipeItemBinding

        recipeBinding.recipe = currentRecipe
        recipeBinding.root.setOnClickListener {
            Toast.makeText(
                recipeBinding.root.context,
                currentRecipe.title,
                Toast.LENGTH_SHORT
            ).show() }

        recipeBinding.executePendingBindings()
    }

}