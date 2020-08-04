package com.humansuit.foody.binding


import android.util.Log
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.humansuit.foody.model.Recipe
import com.humansuit.foody.ui.adapter.RecipeListAdapter

@BindingAdapter("setRecipeList")
fun RecyclerView.setRecipes(recipeList: List<Recipe>) {
    val recipeListAdapter = RecipeListAdapter()
    recipeListAdapter.submitList(recipeList)
    adapter = recipeListAdapter
}
