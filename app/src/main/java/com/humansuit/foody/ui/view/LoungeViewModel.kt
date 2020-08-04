package com.humansuit.foody.ui.view

import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.humansuit.foody.model.Recipe
import com.humansuit.foody.repository.RecipeRepository
import com.humansuit.foody.utils.Event
import com.humansuit.foody.utils.MergedRecipes
import com.humansuit.foody.utils.RecipeSectionType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception


class LoungeViewModel @ViewModelInject constructor(
    private val recipeRepository: RecipeRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val TAG = "MainViewModel"
    val progressBarState = MutableLiveData<Boolean>()
    private val popularRecipesLiveData = MutableLiveData<Event<List<Recipe>>>()
    private val breakfastRecipesLiveData = MutableLiveData<Event<List<Recipe>>>()

    val paginationLivaData = MediatorLiveData<MergedRecipes>()
    val initRecipeList = MediatorLiveData<MergedRecipes>()
    var isDataLoading = false
    var isLastPage = false
    var currentPage = 1
    var breakfastRecipesPage = 1


    fun fetchStartData(number: Int = 10) = viewModelScope.launch {
        combine(getPopularRecipesFlow(number), getBreakfastRecipeFlow()) {
                popularRecipes,
                breakfastRecipes ->
            popularRecipesLiveData.postValue(Event(popularRecipes))
            breakfastRecipesLiveData.postValue(Event(breakfastRecipes))
        }
            .onStart { progressBarState.value = true }
            .collect {
            initRecipeList.apply {
                addSource(popularRecipesLiveData) { initRecipeList.value = MergedRecipes.PopularRecipes(it) }
                addSource(breakfastRecipesLiveData) { initRecipeList.value = MergedRecipes.BreakfastRecipes(it) }
                progressBarState.postValue(false)
            }
        }
    }


    private fun fetchMorePopularRecipes(number: Int = 10) = viewModelScope.launch {
        getPopularRecipesFromApi(number)
            .onStart { progressBarState.value = true }
            .collect { recipes ->
                popularRecipesLiveData.postValue(Event(recipes))
                try {
                    paginationLivaData.addSource(popularRecipesLiveData) {
                        paginationLivaData.value = MergedRecipes.PopularRecipes(it)
                    }
                } catch (e: Exception) {}
                progressBarState.postValue(false)
            }
    }


    private fun fetchMoreBreakfastRecipes() = viewModelScope.launch {
        getBreakfastRecipesFromApi("breakfast", breakfastRecipesPage)
            .onStart { progressBarState.value = true }
            .collect { recipes ->
                breakfastRecipesLiveData.postValue(Event(recipes))
                try {
                    paginationLivaData.addSource(breakfastRecipesLiveData) {
                        paginationLivaData.value = MergedRecipes.BreakfastRecipes(it)
                    }
                } catch (e: Exception) { }
                breakfastRecipesPage++
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


    private suspend fun getBreakfastRecipeFlow(type: String = "breakfast") = withContext(Dispatchers.IO) {
        recipeRepository.fetchRecipesByType(type)
    }


    private suspend fun getPopularRecipesFromApi(number: Int) = withContext(Dispatchers.IO) {
        recipeRepository.fetchPopularRecipesFromApi(number)
    }


    private suspend fun getBreakfastRecipesFromApi(type: String = "breakfast", page: Int = 0) = withContext(Dispatchers.IO) {
        recipeRepository.fetchBreakfastRecipesFromApi(type, page)
    }



    fun loadNextRecipePage(sectionType: RecipeSectionType) {
        isDataLoading = false
        when(sectionType) {
            RecipeSectionType.POPULAR_RECIPE -> {
                fetchMorePopularRecipes()
            }
            RecipeSectionType.BREAKFAST_RECIPE -> {
                fetchMoreBreakfastRecipes()
            }
        }

    }

}