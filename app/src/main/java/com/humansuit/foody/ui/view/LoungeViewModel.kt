package com.humansuit.foody.ui.view

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.humansuit.foody.model.PopularRecipes
import com.humansuit.foody.model.Recipe
import com.humansuit.foody.model.RecipesWrapper
import com.humansuit.foody.repository.RecipeRepository
import com.humansuit.foody.utils.Constants.OnExceptionLog
import com.humansuit.foody.utils.Constants.OnSuccessLog
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import retrofit2.Response


class LoungeViewModel @ViewModelInject constructor(
    private val recipeRepository: RecipeRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val TAG = "MainViewModel"
    val recipesListLiveData = MutableLiveData<List<Recipe>>()
    val progressBarState = MutableLiveData<Boolean>()


    fun fetchRecipesByType(type: String) = viewModelScope.launch {
        recipeRepository.fetchRecipesByType(type = type)
            .onStart { progressBarState.postValue(true) }
            .catch { OnExceptionLog(TAG, "Exception -> ${it.message}") }
            .collect { response ->
                fetchRecipesList(response)
                progressBarState.postValue(false)
            }
    }


    fun fetchPopularRecipes(number: Int) = viewModelScope.launch {
        recipeRepository.fetchPopularRecipes(number = number)
            .onStart { progressBarState.postValue(true) }
            .catch { OnExceptionLog(TAG, "Exception -> ${it.message}") }
            .collect { response ->
                fetchPopularRecipesList(response)
                progressBarState.postValue(false)
            }
    }


    private fun fetchPopularRecipesList(response: Response<PopularRecipes>) {
        if (response.isSuccessful) {
            OnSuccessLog(TAG, "Response<List<Recipe>> -> Success")
            response.body()?.let { recipes -> recipesListLiveData.postValue(recipes.recipes)}
        }
    }


    private fun fetchRecipesList(response: Response<RecipesWrapper>) {
        if (response.isSuccessful) {
            OnSuccessLog(TAG, "Response<RecipeWrapper> -> Success")
            response.body()?.let { recipesWrapper -> recipesListLiveData.postValue(recipesWrapper.results) }
        } else OnExceptionLog(TAG, "fetchRecipesList: Response -> Invalid")
    }







}