package com.humansuit.foody.model

import com.humansuit.foody.utils.Constants.RECIPE_PAGE_SIZE
import com.humansuit.foody.utils.RecipeSectionType

data class RecipeSection(
    val id: Int,
    val title: String,
    var recipes: ArrayList<Recipe>,
    val recipeSectionType: RecipeSectionType,
    val pages: Int = 3,
    var currentPage: Int = 1,
    val pageSize: Int = RECIPE_PAGE_SIZE
)