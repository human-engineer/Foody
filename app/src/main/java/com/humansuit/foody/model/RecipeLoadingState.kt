package com.humansuit.foody.model

import com.humansuit.foody.utils.RecipeSectionType

data class RecipeLoadingState(
    val recipeSectionType: RecipeSectionType,
    val number: Int,
    val page: Int
)