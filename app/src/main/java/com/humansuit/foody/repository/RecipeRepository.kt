package com.humansuit.foody.repository

import com.humansuit.foody.database.RecipeDao
import com.humansuit.foody.network.RecipeApi
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RecipeRepository @Inject constructor(
    private val recipeApi: RecipeApi,
    private val recipeDao: RecipeDao
) : Repository {

    private val TAG = this.javaClass.name

    suspend fun fetchPopularRecipes(
        number: Int, page: Int,
        onError: (error: String) -> Unit
    ) = flow {
        val recipesListFromDb = recipeDao.getPopularRecipeList(page * 10)
        if (recipesListFromDb.isEmpty()) {
            recipeApi.fetchPopularRecipes(number, page)
                .suspendOnSuccess {
                    data?.let { response ->
                        response.recipes.forEach { it.recipeType = "popular" }
                        recipeDao.insertRecipeList(response.recipes)
                        emit(response.recipes)
                    }
                }
                .onError { onError(errorBody.toString()) }
                .onFailure { onError("Unknown failure") }
                .onException { onError(exception.message.toString()) }
        } else emit(recipesListFromDb)
    }.flowOn(Dispatchers.IO)


    suspend fun fetchBreakfastRecipes(
        number: Int, page: Int,
        onError: (error: String) -> Unit
    ) = flow {
        val recipesListFromDb = recipeDao.getRecipeListByType(page * 10, "breakfast")
        if (recipesListFromDb.isEmpty()) {
            recipeApi.fetchRecipesByType(number, page)
                .suspendOnSuccess {
                    data?.let { response ->
                        response.results.forEach { it.recipeType = "breakfast" }
                        recipeDao.insertRecipeList(response.results)
                        emit(response.results)
                    }
                }
                .onError { onError(errorBody.toString()) }
                .onFailure { onError("Unknown failure") }
                .onException { onError(exception.message.toString()) }
        } else emit(recipesListFromDb)
    }.flowOn(Dispatchers.IO)


    suspend fun fetchPopularRecipesFromApi(number: Int, page: Int) = flow {
        recipeApi.fetchPopularRecipes(number, page)
            .suspendOnSuccess {
                data?.let { response ->
                    response.recipes.forEach { it.recipeType = "popular" }
                    recipeDao.insertRecipeList(response.recipes)
                    emit(response.recipes)
                }
            }
    }.flowOn(Dispatchers.IO)


    suspend fun fetchBreakfastRecipesFromApi(number: Int, page: Int) = flow {
        recipeApi.fetchRecipesByType(number, page, type = "breakfast")
            .suspendOnSuccess {
                data?.let { response ->
                    response.results.forEach { it.recipeType = "breakfast" }
                    recipeDao.insertRecipeList(response.results)
                    emit(response.results)
                }
            }
    }.flowOn(Dispatchers.IO)


    suspend fun fetchRecipesByType(number: Int, page: Int, type: String) = flow {
        val recipesListFromDb = recipeDao.getRecipeListByType(page * 10, type)
        if (recipesListFromDb.isEmpty()) {
            recipeApi.fetchRecipesByType(number, page, type)
                .suspendOnSuccess {
                    data?.let { response ->
                        response.results.forEach { it.recipeType = type }
                        recipeDao.insertRecipeList(response.results)
                        emit(response.results)
                    }
                }
        } else {
            emit(recipesListFromDb)
        }
    }.flowOn(Dispatchers.IO)

}