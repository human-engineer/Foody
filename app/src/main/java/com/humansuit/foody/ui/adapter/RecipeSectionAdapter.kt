package com.humansuit.foody.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.humansuit.foody.databinding.RecipeSectionBinding
import com.humansuit.foody.model.Recipe
import com.humansuit.foody.model.RecipeSection
import com.humansuit.foody.ui.view.LoungeViewModel
import com.humansuit.foody.utils.CustomViewHolder
import com.humansuit.foody.utils.RecipeSectionType
import com.humansuit.foody.utils.RecyclerViewPaginator

class RecipeSectionAdapter(private val viewModel: LoungeViewModel)
    : ListAdapter<RecipeSection, CustomViewHolder>(Companion) {

    private val viewPool = RecyclerView.RecycledViewPool()
    private val viewHolderBindings = mutableMapOf<RecipeSectionType, ViewDataBinding>()

    companion object : DiffUtil.ItemCallback<RecipeSection>() {
        override fun areItemsTheSame(oldItem: RecipeSection, newItem: RecipeSection): Boolean {
            return  oldItem === newItem
        }
        override fun areContentsTheSame(oldItem: RecipeSection, newItem: RecipeSection): Boolean {
            return  oldItem.id == newItem.id
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RecipeSectionBinding.inflate(inflater, parent, false)

        return CustomViewHolder(binding)
    }


    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val currentRecipeSection = getItem(position)
        val binding = holder.binding as RecipeSectionBinding
        val layoutManager = binding.recipeRecyclerView.layoutManager
        val scrollListener = getRecyclerViewOnScrollListener(
            layoutManager = layoutManager as LinearLayoutManager,
            sectionType = currentRecipeSection.recipeSectionType
        )

        binding.apply {
            recipeSection = currentRecipeSection
            recipeRecyclerView.setHasFixedSize(true)
            recipeRecyclerView.setItemViewCacheSize(20)
            recipeRecyclerView.setRecycledViewPool(viewPool)
            recipeRecyclerView.addOnScrollListener(scrollListener)
            viewHolderBindings[currentRecipeSection.recipeSectionType] = this
            executePendingBindings()
        }
    }


    fun addMoreRecipes(recipeList: List<Recipe>, sectionType: RecipeSectionType) {
        currentList.forEach { recipeSection ->
            if (recipeSection.recipeSectionType == sectionType) {
                val sectionBinding = viewHolderBindings[recipeSection.recipeSectionType] as RecipeSectionBinding
                val adapter = sectionBinding.recipeRecyclerView.adapter as RecipeListAdapter
                val lastPosition = recipeSection.recipes.size
                recipeSection.recipes.addAll(recipeList)
                adapter.notifyItemRangeInserted(lastPosition, recipeList.size)
                return@forEach
            }
        }
    }


    private fun getRecyclerViewOnScrollListener(
        layoutManager: LinearLayoutManager,
        sectionType: RecipeSectionType
    ) = object : RecyclerViewPaginator(layoutManager) {
        override fun loadMoreItems() {
            viewModel.isDataLoading = true
            viewModel.currentPage += 1
            viewModel.loadNextRecipePage(sectionType)
        }

        override fun isLastPage() = viewModel.isLastPage

        override fun isLoading() = viewModel.isDataLoading
    }


}