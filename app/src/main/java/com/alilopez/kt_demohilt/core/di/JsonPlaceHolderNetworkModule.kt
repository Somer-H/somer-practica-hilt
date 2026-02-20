package com.alilopez.kt_demohilt.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object JsonPlaceHolderNetworkModule {
    @Provides
    @Singleton
    @JsonPlaceHolderRetrofit
    fun provideJsonPlaceHolderRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("BuildConfig.BASE_URL_JSON")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
