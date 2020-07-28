package com.humansuit.foody.ui.view

import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.humansuit.foody.model.Recipe
import com.humansuit.foody.repository.RecipeRepository
import com.humansuit.foody.utils.Constants.OnExceptionLog
import com.humansuit.foody.utils.Constants.OnSuccessLog
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


class LoungeViewModel @ViewModelInject constructor(
    private val recipeRepository: RecipeRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val TAG = "MainViewModel"
    val progressBarState = MutableLiveData<Boolean>()
    val popularRecipes = MutableLiveData<List<Recipe>>()
    val cheapRecipes = MutableLiveData<List<Recipe>>()
    val globalRecipeList = MediatorLiveData<List<Recipe>>()


    fun fetchPopularRecipes(number: Int) = viewModelScope.launch {
        val popularRecipesLiveData = recipeRepository.fetchPopularRecipes(
            number = number,
            onSuccess = { },
            onError = { }
        ).asLiveData()

        globalRecipeList.addSource(popularRecipesLiveData) {
            Log.e(TAG, "fetchPopularRecipes: Hereeee, size is ${it.size}")
            globalRecipeList.value = it
        }

    }


    fun fetchCheapRecipes(number: Int) = viewModelScope.launch {
        recipeRepository.fetchPopularRecipes(
            number = number,
            onSuccess = { },
            onError = { }
        )
            .onStart { progressBarState.postValue(true) }
            .catch { OnExceptionLog(TAG, "Exception -> ${it.message}") }
            .collect { recipesList ->
                OnSuccessLog(TAG, "Setup recipesList to recipesLiveData")
                progressBarState.postValue(false)
            }
    }



    fun fetchBreakfastRecipes(number: Int) = viewModelScope.launch {

    }


    fun fetchLunchRecipes(number: Int) = viewModelScope.launch {

    }



    fun fetchHealthyRecipes(number: Int) = viewModelScope.launch {

    }

}