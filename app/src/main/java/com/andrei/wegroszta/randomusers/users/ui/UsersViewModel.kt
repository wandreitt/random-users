package com.andrei.wegroszta.randomusers.users.ui

import com.andrei.wegroszta.randomusers.common.ErrorHandlingViewModel
import com.andrei.wegroszta.randomusers.entities.RandomUsersException
import com.andrei.wegroszta.randomusers.ext.convertTimestampToOffsetHourMinutes
import com.andrei.wegroszta.randomusers.users.data.User
import com.andrei.wegroszta.randomusers.users.data.UsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val usersRepository: UsersRepository
) : ErrorHandlingViewModel() {

    private val _usersUIState = MutableStateFlow(UsersUIState())
    val usersUIState: StateFlow<UsersUIState> = _usersUIState.asStateFlow()

    private var currentPage = 0

    init {
        loadNextPage()
    }

    private fun loadNextPage() {
        _usersUIState.update { it.copy(isLoading = currentPage == 0) }

        launchWithErrorHandling {
            val users = usersRepository.loadUsers(currentPage++)

            val newItems = _usersUIState.value.userItems.toMutableSet()
            newItems.addAll(users.toUserItemUIStateList())
            _usersUIState.update {
                it.copy(
                    isLoading = false,
                    userItems = newItems,
                    isLoadingMoreItems = false
                )
            }
        }
    }

    fun loadNextPage(lastVisibleItemPosition: Int) {
        if (_usersUIState.value.userItems.size - lastVisibleItemPosition > 3) return

        if (_usersUIState.value.isLoading || _usersUIState.value.isLoadingMoreItems) return

        if (currentPage >= 3) {
            _usersUIState.update { it.copy(reachedTheEnd = true) }
            return
        }

        _usersUIState.update { it.copy(isLoadingMoreItems = true) }

        loadNextPage()
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
        val currentUserItems = _usersUIState.value.userItems.toMutableList()
        val index = currentUserItems.indexOf(uiUser)
        if (index == -1) {
            _usersUIState.update { it.copy(isLoading = false) }
            return
        }

        val item = currentUserItems[index]
        val newItem = item.copy(isFavourite = !item.isFavourite)

        currentUserItems[index] = newItem
        _usersUIState.update {
            it.copy(
                isLoading = false,
                userItems = currentUserItems.toSet()
            )
        }
    }

    override fun onError(exception: RandomUsersException) {
        super.onError(exception)
        _usersUIState.update {
            it.copy(
                isLoading = false,
                //not a real scenario message
                errorMessage = exception.message
            )
        }
    }
}
