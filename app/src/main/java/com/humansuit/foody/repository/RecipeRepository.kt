package com.humansuit.foody.repository

import com.humansuit.foody.R
import com.humansuit.foody.database.RecipeDao
import com.humansuit.foody.model.Error
import com.humansuit.foody.model.RecipeLoadingState
import com.humansuit.foody.network.RecipeApi
import com.humansuit.foody.utils.Constants.ErrorMessage.ON_ERROR
import com.humansuit.foody.utils.Constants.ErrorMessage.ON_EXCEPTION
import com.humansuit.foody.utils.Constants.ErrorMessage.ON_FAILURE
import com.humansuit.foody.model.RecipeSectionType
import com.skydoves.sandwich.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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
        onError: (message: String, error: Error) -> Unit
    ) = flow {
        delay(1000)
        val recipeLoadingState = RecipeLoadingState(
            RecipeSectionType.POPULAR_RECIPE,
            number, page
        )
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
                .onError {
                    val error = Error(ON_ERROR, R.drawable.ic_error, recipeLoadingState)
                    onError(errorBody.toString(), error)
                }
                .onFailure {
                    val error = Error(ON_FAILURE, R.drawable.ic_error, recipeLoadingState)
                    onError("Unknown failure", error)
                }
                .onException {
                    val error = Error(ON_EXCEPTION, R.drawable.ic_error, recipeLoadingState)
                    onError(exception.message.toString(), error)
                }
        } else emit(recipesListFromDb)
    }.flowOn(Dispatchers.IO)


    suspend fun fetchBreakfastRecipes(
        number: Int, page: Int,
        onError: (message: String, error: Error) -> Unit
    ) = flow {
        delay(1000)
        val recipeLoadingState = RecipeLoadingState(RecipeSectionType.BREAKFAST_RECIPE, number, page)
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
                .onError {
                    val error = Error(ON_ERROR, R.drawable.ic_error, recipeLoadingState)
                    onError(errorBody.toString(), error)
                }
                .onFailure {
                    val error = Error(ON_FAILURE, R.drawable.ic_error, recipeLoadingState)
                    onError("Unknown failure", error)
                }
                .onException {
                    val error = Error(ON_EXCEPTION, R.drawable.ic_error, recipeLoadingState)
                    onError(exception.message.toString(), error)
                }
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