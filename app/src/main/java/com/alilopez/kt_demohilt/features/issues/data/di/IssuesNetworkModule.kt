package com.alilopez.kt_demohilt.features.issues.data.di

import com.alilopez.kt_demohilt.core.di.IssuesRetrofit
import com.alilopez.kt_demohilt.features.issues.data.datasources.remote.api.IssuesApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object IssuesNetworkModule {
    @Provides
    @Singleton
    fun provideIssuesApi(@IssuesRetrofit retrofit: Retrofit): IssuesApi {
        return retrofit.create(IssuesApi::class.java)
    }
}