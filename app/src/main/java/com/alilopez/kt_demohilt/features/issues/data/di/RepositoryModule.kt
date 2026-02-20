package com.alilopez.kt_demohilt.features.issues.data.di

import com.alilopez.demo.features.jsonplaceholder.data.repositories.PostsRepositoryImpl
import com.alilopez.demo.features.jsonplaceholder.domain.repositories.PostsRepository
import com.alilopez.kt_demohilt.features.issues.data.repositories.IssuesRepositoryImpl
import com.alilopez.kt_demohilt.features.issues.domain.repositories.IssuesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindIssuesRepository(
        issuesRepositoryImpl: IssuesRepositoryImpl
    ): IssuesRepository
}