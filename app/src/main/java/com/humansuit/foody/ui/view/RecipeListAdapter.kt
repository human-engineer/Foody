package com.humansuit.foody.ui.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.humansuit.foody.databinding.RecipeSectionBinding
import com.humansuit.foody.model.Recipe


class RecipeListAdapter(private val mViewModel: LoungeViewModel)
    : RecyclerView.Adapter<RecipeListAdapter.ViewHolder>() {

    private lateinit var items: List<Recipe>

    inner class ViewHolder(val binding: RecipeSectionBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.apply {
                val adapter = RecipeSectionAdapter(mViewModel)
                adapter.setupList(items)
                recipeRecyclerView.adapter = adapter
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RecipeSectionBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = 20


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }


    fun setupList(recipes: List<Recipe>) {
        items = recipes
    }

}