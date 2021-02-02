package com.humansuit.foody.ui.lounge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.humansuit.foody.binding.showErrorSnackBar
import com.humansuit.foody.databinding.FragmentLoungeBinding
import com.humansuit.foody.model.Error
import com.humansuit.foody.model.RecipeSection
import com.humansuit.foody.ui.BaseFragment
import com.humansuit.foody.adapter.RecipeSectionAdapter
import com.humansuit.foody.utils.Event
import com.humansuit.foody.model.MergedRecipes
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoungeFragment : BaseFragment() {

    private val TAG = this.javaClass.name
    private lateinit var binding: FragmentLoungeBinding
    private val viewModel by viewModels<LoungeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoungeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recipeSectionAdapter = RecipeSectionAdapter(viewModel)
        val recipeSectionList = arrayListOf<RecipeSection>()
        val removeObserverClosure = { viewModel.initialListLiveData.removeObservers(viewLifecycleOwner) }
        val observePaginationList = { data: MergedRecipes ->
            LoungeObserver.observePaginationLivaData(data, recipeSectionAdapter)
        }
        val observeInitialList = { data: Event<MergedRecipes> ->
            LoungeObserver.observeInitialLiveData(
                data, recipeSectionList,
                recipeSectionAdapter,
                removeObserverClosure
            )
        }

        val observeErrorState = { error: Error ->
            binding.root.showErrorSnackBar(error.message) {
                Toast.makeText(requireContext(), "Hello", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.apply {
            loadInitialRecipeSections()
            initialListLiveData.observe(viewLifecycleOwner) { data -> observeInitialList(data) }
            paginationListLivaData.observe(viewLifecycleOwner) { data -> observePaginationList(data) }
            errorLiveData.observe(viewLifecycleOwner) { error -> observeErrorState(error)}
        }

        binding.apply {
            viewModel = this@LoungeFragment.viewModel
            loungeRecipeList.adapter = recipeSectionAdapter

        }
    }

}