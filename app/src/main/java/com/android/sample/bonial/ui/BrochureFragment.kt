package com.android.sample.bonial.ui

import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.android.sample.bonial.R
import com.android.sample.bonial.base.BaseFragment
import com.android.sample.bonial.common.ViewState
import com.android.sample.bonial.databinding.FragmentBrochureBinding
import com.android.sample.bonial.viewmodel.BrochureViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BrochureFragment : BaseFragment<BrochureViewModel, FragmentBrochureBinding>
    (R.layout.fragment_brochure) {

    override val viewModel: BrochureViewModel by viewModels()

    @Inject
    lateinit var brochureAdapter: BrochureAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        handleResults()
        initUiElements()
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.filter_menu, menu)
        val menuItem = menu.findItem(R.id.action_filter)
        menuItem.setFilterColor()
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_filter -> {
                viewModel.enableFilter()
                item.setFilterColor()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun handleResults() {
        lifecycleScope.launch {
            viewModel.stateFlow.collect {
                when (it) {
                    is ViewState.Loading -> showLoading(true)
                    is ViewState.Success -> {
                        brochureAdapter.submitList(it.data)
                        showLoading(false)
                    }
                    is ViewState.Error -> {
                        binding.errorMsg.text = it.message
                        showLoading(false)
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.loadingSpinner.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun initUiElements() {
        binding.recyclerView.apply {
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
    }

    private fun MenuItem.setFilterColor() {
        setIcon(if (viewModel.isFilter) R.drawable.ic_filter else R.drawable.ic_no_filter)
    }
}