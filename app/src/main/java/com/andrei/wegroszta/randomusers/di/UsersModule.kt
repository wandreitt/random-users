package com.andrei.wegroszta.randomusers.di

import com.andrei.wegroszta.randomusers.BuildConfig
import com.andrei.wegroszta.randomusers.users.data.UsersRepository.UsersRemoteDataSource
import com.andrei.wegroszta.randomusers.users.io.RetrofitUsersRemoteDataSource
import com.andrei.wegroszta.randomusers.users.io.UsersAPIService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object UsersModule {

    @Singleton
    @Provides
    fun provideUsersAPIService(
        retrofit: Retrofit
    ): UsersAPIService = retrofit.create(UsersAPIService::class.java)

    @Provides
    @Singleton
    fun provideDashboardDataSource(
        apiService: UsersAPIService
    ): UsersRemoteDataSource = RetrofitUsersRemoteDataSource(
        service = apiService,
        resultsPerPage = BuildConfig.USERS_PER_PAGE,
        seed = BuildConfig.USERS_SEED
    )
}
