package com.humansuit.foody.utils

import com.humansuit.foody.model.Recipe

sealed class MergedRecipes {
    data class PopularRecipes(val popularRecipes: List<Recipe>) : MergedRecipes()
    data class TypedRecipes(val cheapRecipes: List<Recipe>) : MergedRecipes()
}