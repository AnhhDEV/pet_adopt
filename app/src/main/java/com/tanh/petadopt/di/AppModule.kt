package com.tanh.petadopt.di

import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.tanh.petadopt.data.GoogleAuthUiClient
import com.tanh.petadopt.data.PetRepository
import com.tanh.petadopt.data.UserRepository
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

    @Provides
    @Singleton
    fun provideFirestore() = Firebase.firestore

    @Provides
    @Singleton
    fun provideFireauth() = Firebase.auth

    @Provides
    @Singleton
    fun provideUserRepository(firestore: FirebaseFirestore) = UserRepository(firestore)

    @Provides
    @Singleton
    fun providePetRepository(firestore: FirebaseFirestore) = PetRepository(firestore)

}