package com.humansuit.foody.model

import com.humansuit.foody.utils.Event

sealed class MergedRecipes {

    data class PopularRecipes(
        val popularRecipes: Event<List<Recipe>>,
        val sectionType: RecipeSectionType
    ) : MergedRecipes()

    data class BreakfastRecipes(
        val breakfastRecipes: Event<List<Recipe>>,
        val sectionType: RecipeSectionType
    ) : MergedRecipes()

}