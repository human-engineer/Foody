package com.humansuit.foody.model.response

import com.humansuit.foody.model.Recipe

data class PopularRecipesResponse(
    val recipes: List<Recipe>
)