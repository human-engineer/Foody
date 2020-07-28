package com.humansuit.foody.ui.view

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.humansuit.foody.binding.bindAdapter
import com.humansuit.foody.databinding.RecipeSectionBinding
import com.humansuit.foody.model.RecipeSection
import com.humansuit.foody.utils.RecipeSectionType


class RecipeListAdapter(private val viewModel: LoungeViewModel)
    : RecyclerView.Adapter<RecipeListAdapter.ViewHolder>() {

    private var sectionList: Map<RecipeSectionType, RecipeSection>? = null

    inner class ViewHolder(val binding: RecipeSectionBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(section: RecipeSectionType) {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RecipeSectionBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = sectionList?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(RecipeSectionType.values()[position])
    }

    fun setupSectionList(recipeSectionList: Map<RecipeSectionType, RecipeSection>?) {
        sectionList = recipeSectionList
    }

}