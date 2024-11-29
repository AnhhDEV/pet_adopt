package com.tanh.petadopt.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.tanh.petadopt.domain.model.Preference
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

class PreferenceRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: GoogleAuthUiClient
) {

    private val userData = auth.getSignedInUser()
    private val collection = firestore.collection(Util.PREFERENCES_COLLECTION)

    suspend fun getPreferences(): Flow<Result<Preference, Exception>> {
        return callbackFlow {
            var snapshotStateListener: ListenerRegistration? = null
            try {
                 snapshotStateListener = collection.whereEqualTo("userId", userData)
                    .addSnapshotListener {snapshot, error ->
                        val response = if(snapshot != null) {
                            val preference = snapshot.documents.first().toObject(Preference::class.java) ?: Preference()
                            Result.Success(preference)
                        } else {
                            Result.Error(error ?: Exception("Unknown error"))
                        }
                        trySend(response)
                    }
            } catch (e: Exception) {
                Result.Error(e)
            }
            awaitClose {
                snapshotStateListener?.remove()
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun updatePreference(preference: Preference, preferenceId: String): Result<Boolean, Exception> {
        return try {
            withContext(Dispatchers.IO) {
                val pair = hashMapOf(
                    "pets" to preference.pets,
                    "userId" to preference.userId
                )
                suspendCoroutine { continuation ->
                    collection.document(preferenceId).update(pair)
                        .addOnSuccessListener {
                            continuation.resume(Result.Success(true))
                        }
                        .addOnFailureListener { exception ->
                            continuation.resume(Result.Error(exception))
                        }
                }
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun insertPreference(preference: Preference): Result<Boolean, Exception> {
        return try {
            withContext(Dispatchers.IO) {
                val pair= hashMapOf<String, Any>(
                    "pets" to preference.pets,
                    "userId" to preference.userId
                )
                suspendCoroutine { continuation ->
                    val documentId = collection.document().id
                    collection.document(documentId).set(pair)
                        .addOnSuccessListener {
                            continuation.resume(Result.Success(true))
                        }
                        .addOnFailureListener { exception ->
                            continuation.resume(Result.Error(exception))
                        }
                }
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

}