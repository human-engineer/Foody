package com.humansuit.foody.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.get
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

class RecipeSectionAdapter(private val viewModel: LoungeViewModel) : ListAdapter<RecipeSection, CustomViewHolder>(Companion) {

    private val viewPool = RecyclerView.RecycledViewPool()
    private val binding: RecipeSectionBinding? = null
    private val bindings: Map<RecipeSectionType, ViewDataBinding>? = null


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
        binding = RecipeSectionBinding.inflate(inflater, parent, false)

        return CustomViewHolder(binding)
    }


    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        Log.e("TAG", "onBindViewHolder: ViewHolder binded")
        val currentRecipeSection = getItem(position)
        val binding = holder.binding as RecipeSectionBinding
        val layoutManager = binding.recipeRecyclerView.layoutManager
        val scrollListener = getRecyclerViewOnScrollListener(
            layoutManager = layoutManager as LinearLayoutManager,
            sectionType = currentRecipeSection.recipeSectionType,
            recyclerView = binding.recipeRecyclerView
        )

        binding.apply {
            recipeSection = currentRecipeSection
            recipeRecyclerView.setHasFixedSize(true)
            recipeRecyclerView.setItemViewCacheSize(20)
            recipeRecyclerView.setRecycledViewPool(viewPool)
            recipeRecyclerView.addOnScrollListener(scrollListener)
            executePendingBindings()
        }
    }


    fun addMoreRecipes(recipeList: List<Recipe>, sectionType: RecipeSectionType) {
//        currentList.forEach { recipeSection ->
//            if (recipeSection.recipeSectionType == sectionType) {
//                recipeSection.recipes.addAll(recipeList)
//                notifyItemRangeInserted(lastIndex, recipeList.size)
//                return@forEach
//            }
//        }

        Log.e("SectionAdapter", "addMoreRecipes: Add more items to list")
//        for ((index, recipeSection) in currentList.withIndex()) {
//            if (recipeSection.recipeSectionType == sectionType) {
//                val currentListSize = currentList[index].recipes.size
//                currentList[index].recipes.addAll(recipeList)
//                notifyItemRangeInserted(currentListSize, recipeList.size)
//                return
//            }
//        }

        val adapter = binding.recipeRecyclerView.adapter as RecipeListAdapter
        Log.e("FFFFFFFFFFF", "addMoreRecipes: ${adapter.currentList[1].recipeType}")



    }


    private fun getRecyclerViewOnScrollListener(
        layoutManager: LinearLayoutManager,
        sectionType: RecipeSectionType,
        recyclerView: RecyclerView
    ) = object : RecyclerViewPaginator(layoutManager) {
        override fun loadMoreItems() {
            viewModel.isDataLoading = true
            viewModel.currentPage += 1
            addMoreRecipes(emptyList(), RecipeSectionType.POPULAR_RECIPE)
            //viewModel.loadNextRecipePage(sectionType)
        }

        override fun isLastPage() = viewModel.isLastPage

        override fun isLoading() = viewModel.isDataLoading

    }


}