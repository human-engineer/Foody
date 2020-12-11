package com.humansuit.foody.model

import com.humansuit.foody.utils.Constants.AMOUNT_OF_PAGES
import com.humansuit.foody.utils.Constants.RECIPE_PAGE_SIZE

data class RecipeSection(
    val id: Int,
    val title: String,
    var recipes: ArrayList<Recipe>,
    val recipeSectionType: RecipeSectionType,
    val pages: Int = AMOUNT_OF_PAGES,
    var currentPage: Int = 1,
    val pageSize: Int = RECIPE_PAGE_SIZE
)