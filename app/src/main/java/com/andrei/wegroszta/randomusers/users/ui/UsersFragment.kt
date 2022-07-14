package com.andrei.wegroszta.randomusers.users.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andrei.wegroszta.randomusers.R
import com.andrei.wegroszta.randomusers.common.ViewBindingFragment
import com.andrei.wegroszta.randomusers.databinding.FragmentUsersBinding
import com.andrei.wegroszta.randomusers.ext.doOnLifecycleStartedState
import com.andrei.wegroszta.randomusers.ext.setSupportTitle
import com.andrei.wegroszta.randomusers.ext.showLongToast
import com.andrei.wegroszta.randomusers.ext.showShortToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UsersFragment : ViewBindingFragment<FragmentUsersBinding>() {

    private val viewModel: UsersViewModel by viewModels()

    private val usersAdapter: UsersRVAdapter by lazy { UsersRVAdapter() }

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentUsersBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setSupportTitle(getString(R.string.users_title))

        setupRVUsers()

        doOnLifecycleStartedState {
            viewModel.usersUIState.collect {
                onNewUIState(it)
            }
        }

        binding.fabEdit.setOnClickListener {
            requireContext().showShortToast(getString(R.string.users_edit_action))
        }
    }

    private fun onNewUIState(uiState: UsersUIState) {
        uiState.errorMessage?.let {
            requireContext().showLongToast(it)
        }

        if (uiState.isLoading) {
            showLoading()
            return
        }

        hideLoading()

        if (uiState.isLoadingMoreItems) {
            showMoreItemsLoading()
            return
        }

        hideMoreItemsLoading()

        if (uiState.reachedTheEnd) {
            requireContext().showLongToast(getString(R.string.users_end_of_data))
            return
        }

        usersAdapter.submitList(uiState.userItems.toList())
    }

    private fun showLoading() {
        binding.loadingContainer.isVisible = true
        binding.fabEdit.isVisible = false
    }

    private fun hideLoading() {
        binding.loadingContainer.isVisible = false
        binding.fabEdit.isVisible = true
    }

    private fun showMoreItemsLoading() {
        binding.itemsLoadingContainer.isVisible = true
        binding.fabEdit.isVisible = false
    }

    private fun hideMoreItemsLoading() {
        binding.itemsLoadingContainer.isVisible = false
        binding.fabEdit.isVisible = true
    }

    private fun setupRVUsers() = with(binding.rvUsers) {
        val lm = LinearLayoutManager(requireContext()).apply {
            orientation = LinearLayoutManager.VERTICAL
        }.also {
            layoutManager = it
        }

        addItemDecoration(
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        )
        adapter = usersAdapter

        addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val visibleItemCount = lm.childCount
                val firstVisibleItemPosition: Int = lm.findFirstVisibleItemPosition()

                viewModel.loadNextPage(firstVisibleItemPosition + visibleItemCount - 1)
            }
        })
    }
}
