package com.humansuit.foody.ui.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.humansuit.foody.databinding.FragmentLoungeBinding
import com.humansuit.foody.model.RecipeSection
import com.humansuit.foody.ui.adapter.RecipeSectionAdapter
import com.humansuit.foody.utils.MergedRecipes
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoungeFragment : Fragment() {

    private val TAG = "MainFragment"
    private lateinit var binding: FragmentLoungeBinding
    private val viewModel by viewModels<LoungeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoungeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recipeSectionList = arrayListOf<RecipeSection>()
        val recipeSectionAdapter = RecipeSectionAdapter()
        viewModel.fetchStartData()

        binding.apply {
            viewModel = this@LoungeFragment.viewModel
            loungeRecipeList.adapter = recipeSectionAdapter
        }


        viewModel.globalRecipeList.observe(viewLifecycleOwner) { mergedRecipes ->
            when(mergedRecipes) {
                is MergedRecipes.PopularRecipes -> {
                    Log.e(TAG, "onViewCreated: ${mergedRecipes.popularRecipes.first().title}")
                    val popularRecipeSection = RecipeSection(
                        0,
                        "Popular recipes",
                        mergedRecipes.popularRecipes)

                    recipeSectionList.add(popularRecipeSection)
                    recipeSectionList.add(popularRecipeSection)
                    recipeSectionList.add(popularRecipeSection)
                    recipeSectionList.add(popularRecipeSection)
                    recipeSectionAdapter.submitList(recipeSectionList)
                }
                is MergedRecipes.TypedRecipes -> {
                    Log.e(TAG, "onViewCreated: ${mergedRecipes.cheapRecipes.first().title}")
                    val breakfastRecipeSection = RecipeSection(
                        1,
                        "Breakfast recipes",
                        mergedRecipes.cheapRecipes)

                    recipeSectionList.add(breakfastRecipeSection)
                    recipeSectionList.add(breakfastRecipeSection)
                    recipeSectionList.add(breakfastRecipeSection)
                    recipeSectionList.add(breakfastRecipeSection)
                    recipeSectionAdapter.submitList(recipeSectionList)
                }
            }
        }
    }

}