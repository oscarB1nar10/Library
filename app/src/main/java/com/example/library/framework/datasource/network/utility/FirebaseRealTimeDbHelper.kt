package com.example.library.framework.datasource.network.utility

import com.example.library.business.domain.states.State
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

@ExperimentalCoroutinesApi
object FirebaseRealTimeDbHelper {

    fun DatabaseReference.valueEventFlow(successfulMessage: String): Flow<State<String>> = callbackFlow {

        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                sendBlocking(State.Success(successfulMessage))
            }

            override fun onCancelled(error: DatabaseError) {
                sendBlocking(State.Failed(error.message))
            }
        }

        addValueEventListener(valueEventListener)

        awaitClose {
            removeEventListener(valueEventListener)
        }
    }

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

                            offer(State.Success(listT))

                        } catch (exp: Exception) {
                            if (!isClosedForSend) offer(State.failed(exp.message.toString()))
                        }
                    }
                }
                addListenerForSingleValueEvent(valueListener)

                awaitClose { removeEventListener(valueListener) }
            }


}