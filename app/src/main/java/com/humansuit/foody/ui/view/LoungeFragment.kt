package com.humansuit.foody.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.humansuit.foody.databinding.FragmentLoungeBinding
import com.humansuit.foody.model.Recipe
import com.humansuit.foody.model.RecipeSection
import com.humansuit.foody.ui.adapter.RecipeSectionAdapter
import com.humansuit.foody.utils.MergedRecipes
import com.humansuit.foody.utils.RecipeSectionType
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoungeFragment : Fragment() {

    private lateinit var binding: FragmentLoungeBinding
    private val viewModel by viewModels<LoungeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoungeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recipeSectionList = arrayListOf<RecipeSection>()
        val recipeSectionAdapter = RecipeSectionAdapter(viewModel)

        binding.apply {
            viewModel = this@LoungeFragment.viewModel.apply { fetchInitialRecipeSections() }
            loungeRecipeList.adapter = recipeSectionAdapter
        }

        val initialListObserver = createInitialListObserver(recipeSectionList, recipeSectionAdapter)
        val paginationListObserver = createPaginationObserver(recipeSectionAdapter)

        viewModel.initialListLiveData.observe(viewLifecycleOwner, initialListObserver)
        viewModel.paginationListLivaData.observe(viewLifecycleOwner, paginationListObserver)
    }


    private fun createInitialListObserver(
        recipeSectionList: ArrayList<RecipeSection>,
        recipeSectionAdapter: RecipeSectionAdapter
    ) = Observer<MergedRecipes> { mergedRecipes ->
        when (mergedRecipes) {
            is MergedRecipes.PopularRecipes -> {
                mergedRecipes.popularRecipes.getContentIfNotHandled()?.let { popularRecipes ->
                    val recipeSection = createRecipeSection(
                        sectionType = mergedRecipes.sectionType,
                        recipeList = popularRecipes as ArrayList<Recipe>
                    )
                    recipeSectionList.add(recipeSection)
                    recipeSectionAdapter.submitList(recipeSectionList)
                }
            }
            is MergedRecipes.BreakfastRecipes -> {
                mergedRecipes.breakfastRecipes.getContentIfNotHandled()?.let { breakfastRecipes ->
                    val recipeSection = createRecipeSection(
                        sectionType = mergedRecipes.sectionType,
                        recipeList = breakfastRecipes as ArrayList<Recipe>
                    )
                    recipeSectionList.add(recipeSection)
                    recipeSectionAdapter.submitList(recipeSectionList)
                }
                viewModel.initialListLiveData.removeObservers(viewLifecycleOwner)
            }
        }
        return@Observer
    }


    private fun createPaginationObserver(
        recipeSectionAdapter: RecipeSectionAdapter
    ) = Observer<MergedRecipes> { mergedRecipes ->
        when (mergedRecipes) {
            is MergedRecipes.PopularRecipes -> {
                mergedRecipes.popularRecipes.getContentIfNotHandled()?.let { recipeList ->
                    recipeSectionAdapter.addMoreRecipes(
                        recipeList,
                        mergedRecipes.sectionType
                    )
                }
            }
            is MergedRecipes.BreakfastRecipes -> {
                mergedRecipes.breakfastRecipes.getContentIfNotHandled()?.let { recipeList ->
                    recipeSectionAdapter.addMoreRecipes(
                        recipeList,
                        mergedRecipes.sectionType
                    )
                }
            }
        }
        return@Observer
    }


    private fun createRecipeSection(
        sectionType: RecipeSectionType,
        recipeList: ArrayList<Recipe>
    ) = RecipeSection(sectionType.id, sectionType.title, recipeList, sectionType)

}