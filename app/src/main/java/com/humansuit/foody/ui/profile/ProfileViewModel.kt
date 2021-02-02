package com.humansuit.foody.ui.profile

import android.os.Bundle
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.humansuit.foody.repository.RecipeRepository

class ProfileViewModel @ViewModelInject constructor(
    private val recipeRepository: RecipeRepository,
    @Assisted private val savedInstanceState: Bundle?
) : ViewModel() {



}