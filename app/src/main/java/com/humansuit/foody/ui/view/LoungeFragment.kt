package com.humansuit.foody.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.humansuit.foody.databinding.FragmentLoungeBinding
import com.humansuit.foody.model.RecipeSection
import com.humansuit.foody.utils.RecipeSectionType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.recipe_section.*


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

        binding.apply {
            viewModel = viewModel
        }

        viewModel.progressBarState.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = it
        })

    }

}