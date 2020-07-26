package com.humansuit.foody.ui.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.humansuit.foody.databinding.FragmentLoungeBinding
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

        viewModel.fetchPopularRecipes(10)
        val recyclerViewAdapter = RecipeListAdapter(viewModel)
        binding.viewModel = viewModel

        viewModel.recipesListLiveData.observe(viewLifecycleOwner, Observer {
            recyclerViewAdapter.setupList(it)
            binding.recipeSectionRecyclerView.adapter = recyclerViewAdapter
        })

        viewModel.progressBarState.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = it
        })

    }

}