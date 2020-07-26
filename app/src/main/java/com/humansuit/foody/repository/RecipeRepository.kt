package com.humansuit.foody.repository

import com.humansuit.foody.database.RecipeDao
import com.humansuit.foody.network.RecipeApi
import com.humansuit.foody.utils.Constants.OnExceptionLog
import com.skydoves.sandwich.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RecipeRepository @Inject constructor(
    private val recipeApi: RecipeApi,
    private val recipeDao: RecipeDao
) {

    private val TAG = "RecipeRepository"

    suspend fun fetchRecipesByType(type: String) = flow {
        emit(recipeApi.fetchRecipes(type = type))
    }.flowOn(Dispatchers.IO)


    suspend fun fetchPopularRecipes(
        number: Int,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) = flow {
        val recipesListFromDb = recipeDao.getRecipeList()
        if (recipesListFromDb.isEmpty()) {
            OnExceptionLog(TAG, "Data from network")
            recipeApi.fetchPopularRecipes(number)
                .suspendOnSuccess {
                    data?.let { response ->
                        recipeDao.insertRecipeList(response.recipes)
                        emit(response.recipes)
                        onSuccess()
                    }
                }
                .onError { onError() }
                .onFailure { onError() }
                .onException { onError() }
        } else {
            OnExceptionLog(TAG, "Data from database, size: ${recipesListFromDb.size}")
            emit(recipesListFromDb)
            onSuccess()
        }
    }.flowOn(Dispatchers.IO)

}