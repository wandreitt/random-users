package com.andrei.wegroszta.randomusers.users.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.andrei.wegroszta.randomusers.R
import com.andrei.wegroszta.randomusers.common.ViewBindingFragment
import com.andrei.wegroszta.randomusers.databinding.FragmentUsersBinding
import com.andrei.wegroszta.randomusers.ext.doOnLifecycleStartedState
import com.andrei.wegroszta.randomusers.ext.setSupportTitle
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
    }

    private fun onNewUIState(uiState: UsersUIState) {
        if (uiState.isLoading) {
            showLoading()
            return
        }

        hideLoading()

        usersAdapter.submitList(uiState.userItems)
    }

    private fun showLoading() {

    }

    private fun hideLoading() {

    }

    private fun setupRVUsers() = with(binding.rvUsers) {
        layoutManager = LinearLayoutManager(requireContext()).apply {
            orientation = LinearLayoutManager.VERTICAL
        }
        addItemDecoration(
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        )
        adapter = usersAdapter
    }
}
