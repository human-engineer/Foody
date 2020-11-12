package com.humansuit.foody.ui.lounge

import com.humansuit.foody.model.Recipe
import com.humansuit.foody.model.RecipeSection
import com.humansuit.foody.adapter.RecipeSectionAdapter
import com.humansuit.foody.utils.Event
import com.humansuit.foody.model.MergedRecipes
import com.humansuit.foody.model.RecipeSectionType


object LoungeObserver {


    fun observeInitialLiveData(
        data: Event<MergedRecipes>,
        recipeSectionList: ArrayList<RecipeSection>,
        recipeSectionAdapter: RecipeSectionAdapter,
        removeObservers: () -> Unit
    ) {
        data.getContentIfNotHandled()?.let { mergedRecipes ->
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
                }
            }
        }
        if (data.hasBeenHandled && recipeSectionList.size >= RecipeSectionType.values().size)
            removeObservers()
    }


    fun observePaginationLivaData(
        mergedRecipes: MergedRecipes,
        recipeSectionAdapter: RecipeSectionAdapter
    ) {
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


    private fun createRecipeSection(
        sectionType: RecipeSectionType,
        recipeList: ArrayList<Recipe>
    ) = RecipeSection(sectionType.id, sectionType.title, recipeList, sectionType)

}