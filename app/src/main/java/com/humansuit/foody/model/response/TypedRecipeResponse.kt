package com.humansuit.foody.model.response

import com.humansuit.foody.model.Recipe

data class TypedRecipeResponse(
    val results : List<Recipe>,
    val offset : Int,
    val number : Int,
    val totalResults : Int
)