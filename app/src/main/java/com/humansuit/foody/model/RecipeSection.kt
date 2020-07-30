package com.humansuit.foody.model

import com.humansuit.foody.utils.RecipeSectionType

data class RecipeSection(
    val id: Int,
    val title: String,
    val recipes: List<Recipe>,
    val recipeSectionType: RecipeSectionType
)