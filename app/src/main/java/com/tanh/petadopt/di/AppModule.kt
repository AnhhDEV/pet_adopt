package com.tanh.petadopt.di

import android.content.Context
import com.tanh.petadopt.data.GoogleAuthUiClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideGoogleAuthClient(@ApplicationContext context: Context) = GoogleAuthUiClient(context)

}