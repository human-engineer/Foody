package com.humansuit.foody.repository

import com.humansuit.foody.network.RecipesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.Dispatcher
import okhttp3.ResponseBody

class RecipesRepository(private val recipesApi: RecipesApi) {


    suspend fun fetchRecipesByType(type: String) = flow {
        emit(recipesApi.fetchRecipes(type = type))
    }.flowOn(Dispatchers.IO)



}