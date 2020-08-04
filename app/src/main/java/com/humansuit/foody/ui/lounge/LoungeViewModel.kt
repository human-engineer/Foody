package com.humansuit.foody.ui.lounge

import android.util.Log
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
import timber.log.Timber


class LoungeViewModel @ViewModelInject constructor(
    private val recipeRepository: RecipeRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val TAG = this.javaClass.name
    val progressBarState = MutableLiveData<Boolean>()
    val popularRecipesLiveData = MutableLiveData<Event<List<Recipe>>>()
    val breakfastRecipesLiveData = MutableLiveData<Event<List<Recipe>>>()

    val paginationListLivaData = MediatorLiveData<Event<MergedRecipes>>()
    val initialListLiveData = MediatorLiveData<Event<MergedRecipes>>()
    var isDataLoading = false


    init {
        Timber.d("$TAG is initialized")
        addInitialListSources()
        addPaginationListSources()
    }

    fun fetchInitialRecipeSections(number: Int = RECIPE_PAGE_SIZE) = viewModelScope.launch {
        Timber.tag(TAG).d("fetchInitialRecipeSection called")
        combine(getPopularRecipeFlow(number), getBreakfastRecipeFlow(number)) { popularRecipes,
                                                                                breakfastRecipes ->
            popularRecipesLiveData.value = Event(popularRecipes)
            breakfastRecipesLiveData.value = Event(breakfastRecipes)
        }
            .onStart { progressBarState.value = true }
            .collect { progressBarState.postValue(false) }
    }





    fun loadNextSectionPage(recipeSection: RecipeSection, page: Int) = viewModelScope.launch {
        when(recipeSection.recipeSectionType) {
            RecipeSectionType.POPULAR_RECIPE -> {
                fetchPopularRecipes(
                    number = recipeSection.pageSize,
                    page = page
                )
            }
            RecipeSectionType.BREAKFAST_RECIPE -> {
                fetchBreakfastRecipes(
                    number = recipeSection.pageSize,
                    page = page
                )
            }
        }
    }


    private suspend fun fetchPopularRecipes(number: Int = RECIPE_PAGE_SIZE, page: Int = 0) {
        getPopularRecipeFlow(number, page)
            .onStart { progressBarState.value = true }
            .collect { recipes ->
                popularRecipesLiveData.value = Event(recipes)
                progressBarState.postValue(false)
            }
    }


    private suspend fun fetchBreakfastRecipes(number: Int = RECIPE_PAGE_SIZE, page: Int = 0) {
        getBreakfastRecipeFlow(number, page)
            .onStart { progressBarState.value = true }
            .collect { recipes ->
                breakfastRecipesLiveData.value = Event(recipes)
                progressBarState.postValue(false)
            }
    }


    private suspend fun getPopularRecipeFlow(number: Int, page: Int = 0) =
        withContext(Dispatchers.IO) {
            recipeRepository.fetchPopularRecipes(
                number,
                page,
                onError = { Log.e(TAG, "getPopularRecipeFlow: here") }
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



    fun addInitialListSources() {
        initialListLiveData.apply {
            addSource(popularRecipesLiveData) { list ->
                initialListLiveData.value =
                    Event(MergedRecipes.PopularRecipes(list, RecipeSectionType.POPULAR_RECIPE))
            }
            addSource(breakfastRecipesLiveData) { list ->
                initialListLiveData.value =
                    Event(MergedRecipes.BreakfastRecipes(list, RecipeSectionType.BREAKFAST_RECIPE))
            }
        }
    }


    private fun addPaginationListSources() {
        paginationListLivaData.apply {
            addSource(popularRecipesLiveData) {
                paginationListLivaData.value =
                    Event(MergedRecipes.PopularRecipes(it, RecipeSectionType.POPULAR_RECIPE))
            }
            addSource(breakfastRecipesLiveData) {
                paginationListLivaData.value =
                    Event(MergedRecipes.BreakfastRecipes(it, RecipeSectionType.BREAKFAST_RECIPE))
            }
        }
    }

}