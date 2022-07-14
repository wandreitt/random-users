package com.andrei.wegroszta.randomusers.users.io

import retrofit2.http.GET
import retrofit2.http.Query

interface UsersAPIService {
    @GET(".")
    suspend fun getUsers(
        @Query("page") pageNum: Int,
        @Query("results") results: Int,
        @Query("seed") seed: String,
    ): NetworkUserResponseWrapper
}
