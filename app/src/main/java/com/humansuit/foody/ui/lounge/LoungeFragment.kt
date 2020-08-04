package com.humansuit.foody.ui.lounge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.humansuit.foody.databinding.FragmentLoungeBinding
import com.humansuit.foody.model.Recipe
import com.humansuit.foody.model.RecipeSection
import com.humansuit.foody.ui.adapter.RecipeSectionAdapter
import com.humansuit.foody.utils.MergedRecipes
import com.humansuit.foody.utils.RecipeSectionType
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class LoungeFragment : Fragment() {

    private val TAG = this.javaClass.name
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
        Timber.tag(TAG).d("onViewCreated called!")

        val recipeSectionAdapter = RecipeSectionAdapter(viewModel)
        val recipeSectionList = arrayListOf<RecipeSection>()
//        val paginationListObserver = ObserverCreator.createPaginationObserver(recipeSectionAdapter)
//        val initialListObserver = ObserverCreator.createInitialListObserver(
//            recipeSectionAdapter = recipeSectionAdapter,
//            removeObservers = {
//
////                    viewModel.initialListLiveData.removeSource(viewModel.popularRecipesLiveData)
////                    viewModel.initialListLiveData.removeSource(viewModel.breakfastRecipesLiveData)
//                    viewModel.initialListLiveData.removeObservers(viewLifecycleOwner)
//            }
//        )

//        viewModel.initialListLiveData.observe(viewLifecycleOwner, initialListObserver)
//        viewModel.paginationListLivaData.observe(viewLifecycleOwner, paginationListObserver)


        viewModel.initialListLiveData.observe(viewLifecycleOwner) { data ->
            data.getContentIfNotHandled()?.let { mergedRecipes ->
                when (mergedRecipes) {
                    is MergedRecipes.PopularRecipes -> {
                        Timber.d("popular recipes handled = ${mergedRecipes.popularRecipes.hasBeenHandled}")
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
                        Timber.d("breakfast recipes handled = ${mergedRecipes.breakfastRecipes.hasBeenHandled}")
                        mergedRecipes.breakfastRecipes.getContentIfNotHandled()?.let { breakfastRecipes ->
                            val recipeSection = createRecipeSection(
                                sectionType = mergedRecipes.sectionType,
                                recipeList = breakfastRecipes as ArrayList<Recipe>
                            )
                            recipeSectionList.add(recipeSection)
                            recipeSectionAdapter.submitList(recipeSectionList)
                        }
                    }
                }
            }
            if (data.hasBeenHandled && recipeSectionList.size >= 2)
                viewModel.initialListLiveData.removeObservers(viewLifecycleOwner)
        }


        viewModel.paginationListLivaData.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { mergedRecipes ->
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
            }
        }

        binding.apply {
            viewModel = this@LoungeFragment.viewModel.apply { fetchInitialRecipeSections() }
            loungeRecipeList.adapter = recipeSectionAdapter
        }
    }

    private fun createRecipeSection(
        sectionType: RecipeSectionType,
        recipeList: ArrayList<Recipe>
    ) = RecipeSection(sectionType.id, sectionType.title, recipeList, sectionType)

}