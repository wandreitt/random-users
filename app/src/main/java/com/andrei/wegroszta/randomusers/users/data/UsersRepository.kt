package com.andrei.wegroszta.randomusers.users.data

import javax.inject.Inject

class UsersRepository @Inject constructor(
    private val dataSource: UsersRemoteDataSource
) {

    suspend fun loadUsers(page: Int): List<User> = dataSource.loadUsers(page)

    interface UsersRemoteDataSource {
        suspend fun loadUsers(page: Int): List<User>
    }
}
