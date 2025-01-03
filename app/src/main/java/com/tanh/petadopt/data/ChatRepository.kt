package com.tanh.petadopt.data

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.tanh.petadopt.domain.dto.UserDto
import com.tanh.petadopt.domain.model.Chat
import com.tanh.petadopt.domain.model.Message
import com.tanh.petadopt.domain.model.Result
import com.tanh.petadopt.util.Util
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ChatRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    private val userCollection = firestore.collection(Util.USERS_COLLECTION)
    private val chatCollection = firestore.collection(Util.CHATS_COLLECTION)

    //lịch sử tin nhắn
    fun getMessages(chatId: String): Flow<Result<List<Message>, Exception>> {
        return callbackFlow<Result<List<Message>, Exception>> {
            var snapshotStateListener: ListenerRegistration? = null
            try {
                snapshotStateListener = firestore
                    .collection(Util.CHATS_COLLECTION)
                    .document(chatId)
                    .collection(Util.MESSAGE_COLLECTION)
                    .orderBy("time", Query.Direction.DESCENDING)
                    .limit(100)
                    .addSnapshotListener { snapshot, exception ->
                        if(exception != null) {
                            trySend(Result.Error(exception ?: Exception("Not found"))).isSuccess
                            return@addSnapshotListener
                        }
                        val response = if(snapshot != null) {
                            val messages = snapshot.toObjects(Message::class.java).mapNotNull { it }
                            Result.Success(messages)
                        } else {
                            Result.Success(emptyList())
                        }
                        trySend(response).isSuccess
                    }

            } catch (e: Exception) {
                trySend(Result.Error(e))
            }
            awaitClose {
                snapshotStateListener?.remove()
            }
        }.flowOn(Dispatchers.IO)
    }

    //danh sách chat của user
    fun getChats(userId: String): Flow<Result<List<Chat>, Exception>> {
        return callbackFlow {
            var snapshotStateListener: ListenerRegistration? = null
            try {
                snapshotStateListener = chatCollection
                    .whereEqualTo("fromId", userId)
                    .addSnapshotListener { snapshot, exception ->
                        if(exception != null) {
                            trySend(Result.Error(exception ?: Exception("Not found chat"))).isSuccess
                            return@addSnapshotListener
                        }
                        val response = if(snapshot != null) {
                            val chats = snapshot.toObjects(Chat::class.java).mapNotNull { it }
                            Result.Success(chats)
                        } else {
                            Result.Success(emptyList())
                        }
                        trySend(response).isSuccess
                    }
            } catch (e: Exception) {
                trySend(Result.Error(e))
            }
            awaitClose {
                snapshotStateListener?.remove()
            }
        }.flowOn(Dispatchers.IO)
    }

    //tạo message
    suspend fun createMessage(chatId: String, message: Message) {
        try {
            withContext(Dispatchers.IO) {
                val newMessage = hashMapOf(
                    "content" to message.content,
                    "uid" to message.uid,
                    "time" to message.time
                )
                firestore
                    .collection(Util.CHATS_COLLECTION)
                    .document(chatId)
                    .collection(Util.MESSAGE_COLLECTION)
                    .add(newMessage)
                    .await()

                val updates = mapOf(
                    "lastMessage" to message.content,
                    "lastTime" to message.time
                )
                chatCollection.document(chatId).update(updates).await()
            }
        } catch (e: Exception) {
            Log.d("ChatRepository", "createMessage: ${e.message}")
        }
    }

    //tạo chat mới khi lần đầu nhắn tin
    suspend fun createChat(fromId: String, toId: String): Unit {
        try {
            withContext(Dispatchers.IO) {
                val fromUser = getUser(id = fromId)
                val toUser = getUser(id = toId)

                if (fromUser != null && toUser != null) {
                    val newChatId = chatCollection.document().id
                    val chat = Chat(
                        chatId = newChatId,
                        fromId = fromUser.id,
                        fromName = fromUser.name,
                        fromAvatar = fromUser.avatar,
                        toId = toUser.id,
                        toName = toUser.name,
                        toAvatar = toUser.avatar,
                        lastMessage = "",
                        lastTime = null
                    )
                    chatCollection.document(newChatId).set(chat).await()

                } else {
                    Log.d("ChatRepository", "createChat: User not found")
                }
            }
        } catch (e: Exception) {
            Log.d("ChatRepository", "createChat: ${e.message}")
        }
    }

    //lấy user theo id
    suspend fun getUser(id: String): UserDto? {
        return try {
            var user: UserDto? = null
            val userSnapShot = userCollection.whereEqualTo("userId", id).get().await()
            if (userSnapShot != null) {
                user = userSnapShot.documents.first().toObject(UserDto::class.java) ?: UserDto()
            }
            user
        } catch (e: Exception) {
            return null
        }
    }

}