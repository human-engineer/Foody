package com.humansuit.foody.ui.view

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.humansuit.foody.model.Recipe
import com.humansuit.foody.model.RecipesWrapper
import com.humansuit.foody.repository.RecipesRepository
import com.humansuit.foody.utils.Constants.OnExceptionLog
import com.humansuit.foody.utils.Constants.OnSuccessLog
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel(private val recipesRepository: RecipesRepository) : ViewModel() {

    private val TAG = "MainViewModel"

    val recipesListLiveData = MutableLiveData<List<Recipe>>()
    val progressBarState = MutableLiveData<Boolean>()


    fun fetchRecipesByType(type: String) = viewModelScope.launch {
        recipesRepository.fetchRecipesByType(type = type)
            .onStart { progressBarState.postValue(true) }
            .catch { OnExceptionLog(TAG, "Exception -> ${it.message}") }
            .collect { response ->
                fetchRecipesList(response)
                progressBarState.postValue(false)
            }
    }


    private fun fetchRecipesList(response: Response<RecipesWrapper>) {
        if (response.isSuccessful) {
            OnSuccessLog(TAG, "Response<RecipeWrapper> -> Success")
            response.body()?.let { recipesWrapper -> recipesListLiveData.postValue(recipesWrapper.results) }
        } else OnExceptionLog(TAG, "fetchRecipesByType: Response -> Invalid")
    }





}