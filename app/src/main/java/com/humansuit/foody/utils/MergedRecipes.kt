package com.humansuit.foody.utils

import com.humansuit.foody.model.Recipe

sealed class MergedRecipes {
    data class PopularRecipes(val popularRecipes: Event<List<Recipe>>) : MergedRecipes()
    data class BreakfastRecipes(val breakfastRecipes: Event<List<Recipe>>) : MergedRecipes()
}