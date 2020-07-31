package com.humansuit.foody.model

import com.humansuit.foody.utils.RecipeSectionType

data class RecipeSection(
    val id: Int,
    val title: String,
    var recipes: ArrayList<Recipe>,
    val recipeSectionType: RecipeSectionType
)