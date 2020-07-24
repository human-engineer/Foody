package com.humansuit.foody.repository

import android.util.Log
import com.humansuit.foody.database.RecipeDao
import com.humansuit.foody.network.RecipeApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RecipeRepository @Inject constructor(
    private val recipeApi: RecipeApi,
    private val recipeDao: RecipeDao
) {

    suspend fun fetchRecipesByType(type: String) = flow {
        emit(recipeApi.fetchRecipes(type = type))
    }.flowOn(Dispatchers.IO)


    suspend fun fetchPopularRecipes(number: Int) = flow {
        val recipes = recipeDao.getRecipeList(0)
        if (recipes.isEmpty())
            Log.e("dfef", "fetchRecipesByType: EXCEPTION")
        emit(recipeApi.fetchPopularRecipes(number = number))
    }.flowOn(Dispatchers.IO)

}