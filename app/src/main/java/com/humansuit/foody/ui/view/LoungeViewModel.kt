package com.humansuit.foody.ui.view

import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.humansuit.foody.model.Recipe
import com.humansuit.foody.model.RecipeSection
import com.humansuit.foody.repository.RecipeRepository
import com.humansuit.foody.utils.MergedRecipes
import com.humansuit.foody.utils.RecipeSectionType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class LoungeViewModel @ViewModelInject constructor(
    private val recipeRepository: RecipeRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val TAG = "MainViewModel"
    val progressBarState = MutableLiveData<Boolean>()
    private val popularRecipesLiveData = MutableLiveData<List<Recipe>>()
    private val cheapRecipesLiveData = MutableLiveData<List<Recipe>>()
    val globalRecipeList = MediatorLiveData<MergedRecipes>()
    var isDataLoading = false
    var isLastPage = false
    var currentPage = 1


    fun fetchStartData(number: Int = 10) = viewModelScope.launch {
        Log.e(TAG, "fetchPopularRecipes: Starting collect data")

        combine(getPopularRecipesFlow(number), getTypedRecipeFlow()) {
                popularRecipes,
                cheapRecipes ->
            popularRecipesLiveData.postValue(popularRecipes)
            cheapRecipesLiveData.postValue(cheapRecipes)
        }
            .onStart { progressBarState.value = true }
            .collect {
            globalRecipeList.apply {
                addSource(popularRecipesLiveData) { globalRecipeList.value = MergedRecipes.PopularRecipes(it) }
                addSource(cheapRecipesLiveData) { globalRecipeList.value = MergedRecipes.BreakfastRecipes(it) }
                progressBarState.postValue(false)
            }
        }
    }


    private fun fetchMorePopularRecipes(number: Int = 10) = viewModelScope.launch {
        Log.e(TAG, "fetchPopularRecipes: Collect more data")
        getPopularRecipesFlow(number)
            .onStart { progressBarState.value = true }
            .collect {
                popularRecipesLiveData.postValue(it)
                progressBarState.postValue(false)
            }
    }


    private suspend fun getPopularRecipesFlow(number: Int) = withContext(Dispatchers.IO) {
        recipeRepository.fetchPopularRecipes(
            number = number,
            onSuccess = { },
            onError = { }
        )
    }


    private suspend fun getTypedRecipeFlow(type: String = "breakfast") = withContext(Dispatchers.IO) {
        recipeRepository.fetchRecipesByType(type)
    }



    fun loadNextRecipePage(sectionType: RecipeSectionType) {
        when(sectionType) {
            RecipeSectionType.POPULAR_RECIPE -> {
                fetchMorePopularRecipes()
            }
            RecipeSectionType.BREAKFAST_RECIPE -> {
                Log.e(TAG, "loadNextRecipePage: Breakfast recipe fetching..")
            }
        }
    }

}