package com.humansuit.foody.ui.view

import android.os.Bundle
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
import com.humansuit.foody.utils.RecipeSectionType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*


@AndroidEntryPoint
class LoungeFragment : Fragment() {

    private val TAG = "MainFragment"
    private lateinit var binding: FragmentLoungeBinding
    private val viewModel by viewModels<LoungeViewModel>()
    private var bool = true
    private var previousTotal = 0
    private var visibleThreshold = 5

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
        val recipeSectionAdapter = RecipeSectionAdapter(viewModel)

        viewModel.fetchStartData()

        binding.apply {
            viewModel = this@LoungeFragment.viewModel
            loungeRecipeList.adapter = recipeSectionAdapter
        }


        viewModel.globalRecipeList.observe(viewLifecycleOwner) { mergedRecipes ->
            when(mergedRecipes) {
                is MergedRecipes.PopularRecipes -> {
                    val popularRecipeSection = RecipeSection(
                        0,
                        "Popular recipes",
                        mergedRecipes.popularRecipes,
                        RecipeSectionType.POPULAR_RECIPE
                    )

                    if (recipeSectionAdapter.itemCount > 0) {
                            recipeSectionList.add(popularRecipeSection)
                            recipeSectionAdapter.notifyItemInserted(recipeSectionList.size)
                    } else {
                        recipeSectionList.add(popularRecipeSection)
                        recipeSectionAdapter.submitList(recipeSectionList)
                    }
                }
                is MergedRecipes.BreakfastRecipes -> {
                    val breakfastRecipeSection = RecipeSection(
                        1,
                        "Breakfast recipes",
                        mergedRecipes.breakfastRecipes,
                        RecipeSectionType.BREAKFAST_RECIPE
                    )


                    GlobalScope.launch(Dispatchers.Main) {
                        delay(500)
                        recipeSectionList.add(breakfastRecipeSection)
                        recipeSectionAdapter.notifyItemInserted(recipeSectionAdapter.itemCount)
                    }
                }

            }
        }
    }

}