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


    fun fetchPopularRecipes(number: Int) = viewModelScope.launch {
        recipeRepository.fetchPopularRecipes(
            number = number,
            onSuccess = { },
            onError = { }
        )
            .onStart { progressBarState.postValue(true) }
            .catch { OnExceptionLog(TAG, "Exception -> ${it.message}") }
            .collect { recipesList ->
                OnSuccessLog(TAG, "Setup recipesList to recipesLiveData")
                recipesListLiveData.postValue(recipesList)
                progressBarState.postValue(false)
            }
    }


    fun fetchBreakfastRecipes(number: Int) = viewModelScope.launch {

    }


    fun fetchLanchRecipes(number: Int) = viewModelScope.launch {

    }


    fun fetchCheapRecipes(number: Int) = viewModelScope.launch {

    }


    fun fetchHealthyRecipes(number: Int) = viewModelScope.launch {

    }

}