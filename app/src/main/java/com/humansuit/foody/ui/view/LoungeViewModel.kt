package com.humansuit.foody.ui.view

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.humansuit.foody.model.Recipe
import com.humansuit.foody.model.RecipeSection
import com.humansuit.foody.repository.RecipeRepository
import com.humansuit.foody.utils.Constants.RECIPE_PAGE_SIZE
import com.humansuit.foody.utils.Event
import com.humansuit.foody.utils.MergedRecipes
import com.humansuit.foody.utils.RecipeSectionType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class LoungeViewModel @ViewModelInject constructor(
    private val recipeRepository: RecipeRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val TAG = "MainViewModel"
    val progressBarState = MutableLiveData<Boolean>()
    private val popularRecipesLiveData = MutableLiveData<Event<List<Recipe>>>()
    private val breakfastRecipesLiveData = MutableLiveData<Event<List<Recipe>>>()

    val paginationListLivaData = MediatorLiveData<MergedRecipes>()
    val initialListLiveData = MediatorLiveData<MergedRecipes>()
    var isDataLoading = false


    fun fetchInitialRecipeSections(number: Int = RECIPE_PAGE_SIZE) = viewModelScope.launch {
        combine(getPopularRecipeFlow(number), getBreakfastRecipeFlow(number)) { popularRecipes,
                                                                                breakfastRecipes ->
            popularRecipesLiveData.postValue(Event(popularRecipes))
            breakfastRecipesLiveData.postValue(Event(breakfastRecipes))
        }
            .onStart { progressBarState.value = true }
            .collect {
                initialListLiveData.apply {
                    addSource(popularRecipesLiveData) { list ->
                        initialListLiveData.value =
                            MergedRecipes.PopularRecipes(list, RecipeSectionType.POPULAR_RECIPE)
                    }
                    addSource(breakfastRecipesLiveData) { list ->
                        initialListLiveData.value =
                            MergedRecipes.BreakfastRecipes(list, RecipeSectionType.BREAKFAST_RECIPE)
                    }
                    progressBarState.postValue(false)
                }
            }
    }


    fun loadNextSectionPage(recipeSection: RecipeSection, page: Int) {
        when(recipeSection.recipeSectionType) {
            RecipeSectionType.POPULAR_RECIPE -> {
                fetchMorePopularRecipes(
                    number = recipeSection.pageSize,
                    page = page
                )
            }
            RecipeSectionType.BREAKFAST_RECIPE -> {
                fetchMoreBreakfastRecipes(
                    number = recipeSection.pageSize,
                    page = page
                )
            }
        }
    }


    private fun fetchMorePopularRecipes(number: Int = RECIPE_PAGE_SIZE, page: Int) = viewModelScope.launch {
        getPopularRecipeFlow(number, page)
            .onStart { progressBarState.value = true }
            .collect { recipes ->
                popularRecipesLiveData.postValue(Event(recipes))
                try {
                    paginationListLivaData.addSource(popularRecipesLiveData) {
                        paginationListLivaData.value =
                            MergedRecipes.PopularRecipes(it, RecipeSectionType.POPULAR_RECIPE)
                    }
                } catch (e: Exception) { }
                progressBarState.postValue(false)
            }
    }


    private fun fetchMoreBreakfastRecipes(number: Int, page: Int) = viewModelScope.launch {
        getBreakfastRecipeFlow(number, page)
            .onStart { progressBarState.value = true }
            .collect { recipes ->
                breakfastRecipesLiveData.postValue(Event(recipes))
                try {
                    paginationListLivaData.addSource(breakfastRecipesLiveData) {
                        paginationListLivaData.value =
                            MergedRecipes.BreakfastRecipes(it, RecipeSectionType.BREAKFAST_RECIPE)
                    }
                } catch (e: Exception) { }
                progressBarState.postValue(false)
            }
    }


    private suspend fun getPopularRecipeFlow(number: Int, page: Int = 0) =
        withContext(Dispatchers.IO) {
            recipeRepository.fetchPopularRecipes(
                number,
                page,
                onError = { }
            )
        }


    private suspend fun getBreakfastRecipeFlow(number: Int, page: Int = 0) =
        withContext(Dispatchers.IO) {
            recipeRepository.fetchBreakfastRecipes(
                number, page,
                onError = { }
            )
        }


    private suspend fun getPopularRecipesFromApi(number: Int, page: Int)
            = withContext(Dispatchers.IO) { recipeRepository.fetchPopularRecipesFromApi(number, page) }


    private suspend fun getBreakfastRecipesFromApi(number: Int, page: Int)
            = withContext(Dispatchers.IO) { recipeRepository.fetchBreakfastRecipesFromApi(number, page) }

}