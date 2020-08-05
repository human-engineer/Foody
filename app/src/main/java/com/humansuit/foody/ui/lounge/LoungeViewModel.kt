package com.humansuit.foody.ui.lounge

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.humansuit.foody.model.Error
import com.humansuit.foody.model.Recipe
import com.humansuit.foody.model.RecipeSection
import com.humansuit.foody.repository.RecipeRepository
import com.humansuit.foody.utils.Constants.API_ERROR_LOG
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
    private val popularRecipesLiveData = MutableLiveData<Event<List<Recipe>>>()
    private val breakfastRecipesLiveData = MutableLiveData<Event<List<Recipe>>>()

    val paginationListLivaData = MediatorLiveData<MergedRecipes>()
    val initialListLiveData = MediatorLiveData<Event<MergedRecipes>>()
    val progressBarState = MutableLiveData<Boolean>()
    var isDataLoading = false


    val errorLiveData = MutableLiveData<Error>()


    init {
        addInitialListSources()
        addPaginationListSources()
    }


    fun loadInitialRecipeSections(number: Int = RECIPE_PAGE_SIZE) = viewModelScope.launch {
        Timber.tag(TAG).d("Fetching initial recipes.")
        combine(
            getPopularRecipeFlow(number),
            getBreakfastRecipeFlow(number)
        ) { popularRecipes, breakfastRecipes ->
            popularRecipesLiveData.postValue(Event(popularRecipes))
            breakfastRecipesLiveData.postValue(Event(breakfastRecipes))
        }
            .onStart { progressBarState.value = true }
            .collect { progressBarState.postValue(false) }
    }


    fun loadNextSectionPage(recipeSection: RecipeSection, page: Int) = viewModelScope.launch {
        Timber.tag(TAG).d("Loading next section page.")
        when(recipeSection.recipeSectionType) {
            RecipeSectionType.POPULAR_RECIPE -> {
                loadPopularRecipes(
                    number = recipeSection.pageSize,
                    page = page
                )
            }
            RecipeSectionType.BREAKFAST_RECIPE -> {
                loadBreakfastRecipes(
                    number = recipeSection.pageSize,
                    page = page
                )
            }
        }
    }


    private suspend fun loadPopularRecipes(number: Int, page: Int) {
        getPopularRecipeFlow(number, page)
            .onStart { progressBarState.value = true }
            .collect { recipes ->
                popularRecipesLiveData.postValue(Event(recipes))
                progressBarState.postValue(false)
            }
    }


    private suspend fun loadBreakfastRecipes(number: Int, page: Int) {
        getBreakfastRecipeFlow(number, page)
            .onStart { progressBarState.value = true }
            .collect { recipes ->
                breakfastRecipesLiveData.postValue(Event(recipes))
                progressBarState.postValue(false)
            }
    }


    private suspend fun getPopularRecipeFlow(number: Int, page: Int = 0) =
        withContext(Dispatchers.IO) {
            recipeRepository.fetchPopularRecipes(
                number, page,
                onError = { message, error ->
                    API_ERROR_LOG("Error while fetching popular recipe: $message")
                    errorLiveData.postValue(error)
                }
            )
        }


    private suspend fun getBreakfastRecipeFlow(number: Int, page: Int = 0) =
        withContext(Dispatchers.IO) {
            recipeRepository.fetchBreakfastRecipes(
                number, page,
                onError = { message, error  ->
                    API_ERROR_LOG("Error while fetching breakfast recipe: $message")
                    errorLiveData.postValue(error)
                }
            )
        }


    private fun addInitialListSources() {
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
                    MergedRecipes.PopularRecipes(it, RecipeSectionType.POPULAR_RECIPE)
            }
            addSource(breakfastRecipesLiveData) {
                paginationListLivaData.value =
                    MergedRecipes.BreakfastRecipes(it, RecipeSectionType.BREAKFAST_RECIPE)
            }
        }
    }

}