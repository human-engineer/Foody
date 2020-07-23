package com.humansuit.foody.ui.view

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.humansuit.foody.databinding.FragmentMainBinding
import com.humansuit.foody.network.RecipesApi
import com.humansuit.foody.network.RecipesApiFactory
import com.humansuit.foody.repository.RecipesRepository
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment() {

    private val TAG = "MainFragment"

    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recipesService = RecipesApiFactory.makeRetrofitService()
        val recipesApi = RecipesApi(recipesService = recipesService)
        val recipesRepository = RecipesRepository(recipesApi = recipesApi)
        val viewModel = MainViewModel(recipesRepository = recipesRepository)



        viewModel.fetchRecipesByType("dessert")

        viewModel.recipesListLiveData.observe(viewLifecycleOwner, Observer { recipes ->
            Log.e(TAG, "onViewCreated: Here")
            var recipesTitleList = ""
            recipes.forEach { recipesTitleList+=it.title + ",\n" }
            binding.recipesList.text = SpannableStringBuilder(recipesTitleList)
        })
    }

}