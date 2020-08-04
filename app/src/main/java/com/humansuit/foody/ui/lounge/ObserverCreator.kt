package com.humansuit.foody.ui.lounge

import androidx.lifecycle.Observer
import com.humansuit.foody.model.Recipe
import com.humansuit.foody.model.RecipeSection
import com.humansuit.foody.ui.adapter.RecipeSectionAdapter
import com.humansuit.foody.utils.MergedRecipes
import com.humansuit.foody.utils.RecipeSectionType
import timber.log.Timber


object ObserverCreator {

    private val recipeSectionList = arrayListOf<RecipeSection>()

    fun createInitialListObserver(
        recipeSectionAdapter: RecipeSectionAdapter,
        removeObservers: () -> Unit
    ) = Observer<MergedRecipes> { mergedRecipes ->
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
                removeObservers()
            }
        }
        return@Observer
    }


    fun createPaginationObserver(
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