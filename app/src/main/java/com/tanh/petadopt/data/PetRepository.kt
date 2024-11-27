package com.tanh.petadopt.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.tanh.petadopt.domain.model.Pet
import com.tanh.petadopt.domain.model.Result
import com.tanh.petadopt.util.Util
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class PetRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    private val collection = firestore.collection(Util.ANIMALS_COLLECTION)

    suspend fun getAllPets(): Flow<Result<List<Pet>, Exception>> {
        return callbackFlow {
            var snapshotStateListener: ListenerRegistration? = null
            try {
                snapshotStateListener = collection
                    .orderBy("category")
                    .addSnapshotListener { value, error ->
                        val response = if (value != null) {
                            val pets = value.toObjects(Pet::class.java).mapNotNull { it }
                            Result.Success(pets)
                        } else {
                            Result.Error(error = error ?: Exception("Unknown error"))
                        }
                        trySend(response)
                    }
            } catch (e: Exception) {
                trySend(Result.Error(e))
            }
            awaitClose {
                snapshotStateListener?.remove()
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getAllPetsByCategory(category: String): Flow<Result<List<Pet>, Exception>> {
        return callbackFlow<Result<List<Pet>, Exception>> {
            var snapshotStateListener: ListenerRegistration? = null
            try {
                snapshotStateListener = collection
                    .orderBy("age")
                    .whereEqualTo("category", category)
                    .addSnapshotListener { value, error ->
                        val response = if (value != null) {
                            val pets = value.toObjects(Pet::class.java).mapNotNull { it }
                            Result.Success(pets)
                        } else {
                            Result.Error(error = error ?: Exception("Unknown error"))
                        }
                        trySend(response)
                    }
            } catch (e: Exception) {
                trySend(Result.Error(e))
            }
            awaitClose {
                snapshotStateListener?.remove()
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getPetById(id: String): Result<Pet, Exception> {
        return try {
            withContext(Dispatchers.IO) {
                suspendCoroutine { continuation ->
                    collection.document(id)
                        .get()
                        .addOnSuccessListener { snapshot ->
                            val pet = snapshot.toObject(Pet::class.java)
                            if (pet != null) {
                                continuation.resume(Result.Success(pet))
                            } else {
                                continuation.resume(Result.Error(Exception("Pet not found")))
                            }
                        }
                }
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun insertPet(
        ownerId: String,
        name: String,
        age: Long,
        weight: Long,
        breed: String,
        category: String,
        gender: Boolean,
        photoUrl: String,
        address: String,
        about: String
    ): Result<Boolean, Exception> {
        return try {
            withContext(Dispatchers.IO) {
                suspendCoroutine { continuation ->
                    val documentId = collection.document().id
                    val newPet = Pet(
                        animalId = documentId,
                        ownerId = ownerId,
                        name = name,
                        age = age,
                        weight = weight,
                        breed = breed,
                        category = category,
                        gender = gender,
                        photoUrl = photoUrl,
                        address = address,
                        about = about
                    )
                    collection.document(documentId)
                        .set(newPet)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                continuation.resume(Result.Success(true))
                            } else {
                                continuation.resume(
                                    Result.Error(
                                        it.exception ?: Exception("Unknown error")
                                    )
                                )
                            }
                        }
                }
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

}