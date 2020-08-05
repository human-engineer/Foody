package com.humansuit.foody.ui.lounge

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.humansuit.foody.databinding.FragmentLoungeBinding
import com.humansuit.foody.model.RecipeSection
import com.humansuit.foody.ui.adapter.RecipeSectionAdapter
import dagger.hilt.android.AndroidEntryPoint


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

        val recipeSectionAdapter = RecipeSectionAdapter(viewModel)
        val recipeSectionList = arrayListOf<RecipeSection>()
        val observeList = { data: Any ->
            Observer.observeData(
                data, recipeSectionList,
                recipeSectionAdapter,
                removeObservers = { viewModel.initialListLiveData.removeObservers(viewLifecycleOwner) }
            )
        }

        viewModel.apply {
            loadInitialRecipeSections()
            initialListLiveData.observe(viewLifecycleOwner) { data -> observeList(data) }
            paginationListLivaData.observe(viewLifecycleOwner) { data -> observeList(data) }
        }


        viewModel.errorLiveData.observe(viewLifecycleOwner) { error ->
            binding.recipeList.visibility = View.GONE
            binding.errorLayout.root.visibility = View.VISIBLE
            binding.errorLayout.error = error
        }

        binding.apply {
            viewModel = this@LoungeFragment.viewModel
            loungeRecipeList.adapter = recipeSectionAdapter
        }
    }

}