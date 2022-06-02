package com.android.sample.bonial.ui

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.android.sample.bonial.R
import com.android.sample.bonial.base.BaseFragment
import com.android.sample.bonial.common.ViewState
import com.android.sample.bonial.databinding.FragmentBrochureBinding
import com.android.sample.bonial.viewmodel.BrochureViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BrochureFragment : BaseFragment<BrochureViewModel, FragmentBrochureBinding>
    (R.layout.fragment_brochure) {

    override val viewModel: BrochureViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        val brochureAdapter = BrochureAdapter()
        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(
                requireContext(),
                if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    2
                } else {
                    3
                }
            ).apply {
                spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return if (brochureAdapter.isPremium(position)) spanCount else 1
                    }
                }
            }
            adapter = brochureAdapter
        }

        lifecycleScope.launch {
            viewModel.stateFlow.collect {
                if (it is ViewState.Success) {
                    brochureAdapter.submitList(it.data)
                }
            }
        }
        return binding.root
    }
}