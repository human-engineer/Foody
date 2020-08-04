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
) : Repository {

    private val TAG = "RecipeRepository"

    suspend fun fetchRecipesByType(type: String) = flow {
        val recipesListFromDb = recipeDao.getRecipeListByType(0, type)
        if (recipesListFromDb.isEmpty()) {
            OnExceptionLog(TAG, "Recipes by type fetching...")
            recipeApi.fetchRecipesByType(type, 0)
                .suspendOnSuccess {
                    data?.let { response ->
                        response.results.forEach { it.recipeType = type }
                        recipeDao.insertRecipeList(response.results)
                        emit(response.results)
                    }
                }
        } else {
            OnExceptionLog(TAG, "Data from database, size: ${recipesListFromDb.size}")
            emit(recipesListFromDb)
        }
    }.flowOn(Dispatchers.IO)


    suspend fun fetchPopularRecipes(
        number: Int,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) = flow {
        val recipesListFromDb = recipeDao.getPopularRecipeList()
        if (recipesListFromDb.isEmpty()) {
            OnExceptionLog(TAG, "Popular recipes fetching...")
            recipeApi.fetchPopularRecipes(number)
                .suspendOnSuccess {
                    data?.let { response ->
                        response.recipes.forEach { it.recipeType = "popular" }
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


    suspend fun fetchPopularRecipesFromApi(number: Int) = flow {
        recipeApi.fetchPopularRecipes(number)
            .suspendOnSuccess {
                data?.let { response ->
                    response.recipes.forEach { it.recipeType = "popular" }
                    recipeDao.insertRecipeList(response.recipes)
                    emit(response.recipes)
                }
            }
    }.flowOn(Dispatchers.IO)


    suspend fun fetchBreakfastRecipesFromApi(type: String, page: Int) = flow {
        recipeApi.fetchRecipesByType(type, page = page * 10)
            .suspendOnSuccess {
                data?.let { response ->
                    response.results.forEach { it.recipeType = type }
                    recipeDao.insertRecipeList(response.results)
                    emit(response.results)
                }
            }
    }.flowOn(Dispatchers.IO)

}