package com.example.library.framework.datasource.network.utility

import com.example.library.business.domain.states.State
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@ExperimentalCoroutinesApi
object FirebaseRealTimeDbHelper {

    // Stream
    fun DatabaseReference.valueEventFlow(successfulMessage: String): Flow<State<String>> =
        callbackFlow {

            val valueEventListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    trySendBlocking(State.Success(successfulMessage))
                }

                override fun onCancelled(error: DatabaseError) {
                    trySendBlocking(State.Failed(error.message))
                }
            }

            addValueEventListener(valueEventListener)

            awaitClose {
                removeEventListener(valueEventListener)
            }
        }

    // One shot
    suspend fun DatabaseReference.valueEvenOneShot(successfulMessage: String): State<String> =
        suspendCoroutine { continuation ->
            val valueEventListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    continuation.resume(State.Success(successfulMessage))
                }

                override fun onCancelled(error: DatabaseError) {
                    continuation.resume(State.Failed(error.message))
                }

            }
            addListenerForSingleValueEvent(valueEventListener) // Subscribe to the event
        }

    // Stream
    @ExperimentalCoroutinesApi
    inline fun <reified T : Any> DatabaseReference.listen(): Flow<State<List<T>>> =
        callbackFlow {
            val valueListener = object : ValueEventListener {
                override fun onCancelled(databaseError: DatabaseError) {
                    close(databaseError.toException())
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    try {
                        val listT: MutableList<T> = mutableListOf()
                        dataSnapshot.children.mapNotNullTo(listT) {
                            it.getValue<T>(T::class.java)
                        }

                        trySend(State.Success(listT)).isSuccess

                    } catch (exp: Exception) {
                        if (!isClosedForSend) trySend(State.failed(exp.message.toString())).isSuccess
                    }
                }
            }
            addListenerForSingleValueEvent(valueListener)

            awaitClose { removeEventListener(valueListener) }
        }

    // One shot
    suspend fun DatabaseReference.singleValueEvent(): State<DataSnapshot> =
        suspendCoroutine { continuation ->
            val valueEventListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    continuation.resume(State.Success(snapshot))
                }

                override fun onCancelled(error: DatabaseError) {
                    continuation.resume(State.Failed(error.message))
                }

            }
            addListenerForSingleValueEvent(valueEventListener) // Subscribe to the event
        }

}