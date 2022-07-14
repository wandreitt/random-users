package com.andrei.wegroszta.randomusers.users.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andrei.wegroszta.randomusers.ext.convertTimestampToOffsetHourMinutes
import com.andrei.wegroszta.randomusers.users.data.User
import com.andrei.wegroszta.randomusers.users.data.UsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val usersRepository: UsersRepository
) : ViewModel() {

    private val _usersUIState = MutableStateFlow(UsersUIState())
    val usersUIState: StateFlow<UsersUIState> = _usersUIState.asStateFlow()

    private var currentPage = 0

    init {
        loadNextPage()
    }

    fun loadNextPage() {
        _usersUIState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val users = usersRepository.loadUsers(currentPage++)

            _usersUIState.update {
                it.copy(
                    isLoading = false,
                    userItems = users.toUserItemUIStateList()
                )
            }
        }
    }

    private fun List<User>.toUserItemUIStateList(): List<UserItemUIState> {
        val now = System.currentTimeMillis()

        return mapIndexed { index, user ->
            user.toUserItemUIState(
                now = now,
                //dummy logic to show attachments for some users
                hasAttachment = index % 5 == 0,
            ).copy(
                changeFavouriteStatus = this@UsersViewModel::changeFavouriteStatusFor
            )
        }
    }

    private fun User.toUserItemUIState(
        now: Long,
        hasAttachment: Boolean = false,
        isFavourite: Boolean = false
    ) = UserItemUIState(
        username = this.login.username,
        pictureThumbnailUrl = this.picture.thumbnail,
        fullName = "${this.name.firstName} ${this.name.lastName}",
        age = this.dob.age,
        nationality = this.nat,
        userTime = now.convertTimestampToOffsetHourMinutes(this.location.timezone.offset),
        hasAttachment = hasAttachment,
        isFavourite = isFavourite
    )

    //just a mock is favourite implementation,
    //probably in a rl scenario we'd need to use an User object here instead of an UserItemUIState
    private fun changeFavouriteStatusFor(uiUser: UserItemUIState) {
        _usersUIState.update { it.copy(isLoading = true) }

        //some logic regarding the favourite status, repository -> api call? (whatever that status might reflect)
        val index = _usersUIState.value.userItems.indexOf(uiUser)
        if (index == -1) {
            _usersUIState.update { it.copy(isLoading = false) }
            return
        }

        val item = _usersUIState.value.userItems[index]
        val newItem = item.copy(isFavourite = !item.isFavourite)

        val newItems = _usersUIState.value.userItems.toMutableList()
        newItems[index] = newItem
        _usersUIState.update {
            it.copy(
                isLoading = false,
                userItems = newItems
            )
        }
    }
}
