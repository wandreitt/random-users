package com.andrei.wegroszta.randomusers.users.ui

data class UsersUIState(
    val isLoading: Boolean = false,
    val userItems: List<UserItemUIState> = emptyList()
)

data class UserItemUIState(
    val username: String,
    val pictureThumbnailUrl: String,
    val fullName: String,
    val age: Int,
    val nationality: String,
    val userTime: String,
    val hasAttachment: Boolean,
    val isFavourite: Boolean,
    val changeFavouriteStatus: (uiUser: UserItemUIState) -> Unit = {}
)
