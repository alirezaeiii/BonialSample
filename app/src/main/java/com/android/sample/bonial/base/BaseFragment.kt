package com.android.sample.bonial.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.android.sample.bonial.BR

abstract class BaseFragment<VM: ViewModel, T: ViewDataBinding>(
    @LayoutRes private val layoutId: Int
) : Fragment() {

    protected abstract val viewModel: VM

    private var _binding: T? = null

    protected val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        binding.apply {
            setVariable(BR.vm, viewModel)
            // Set the lifecycleOwner so DataBinding can observe LiveData
            lifecycleOwner = viewLifecycleOwner
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}