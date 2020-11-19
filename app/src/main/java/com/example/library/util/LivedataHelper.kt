package com.example.library.util

import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.library.states.State
import kotlinx.android.synthetic.main.activity_auth_layout.*
import kotlinx.android.synthetic.main.activity_main.*

fun <T> Fragment.observe(liveData: LiveData<State<T>>, success: (T) -> Unit){

    liveData.observe(this.viewLifecycleOwner, Observer { state ->
        when(state){
            is State.Loading -> {
                Log.i("subscribeObservers", "Loading: $state")
                this.activity?.progress_circular?.visibility = View.VISIBLE
            }

            is State.Success -> {
                Log.i("subscribeObservers", "Success: $state")
                success(state.data)
                this.activity?.progress_circular?.visibility = View.GONE
            }

            is State.Failed -> {
                Log.i("subscribeObservers", "Failed: $state")
                this.activity?.progress_circular?.visibility = View.GONE
            }
        }
    })
}

fun <T> Fragment.observeAndPreventsHandleEventAgain(liveData: LiveData<State<T>>, success: (T) -> Unit){

    liveData.observe(this.viewLifecycleOwner, Observer { state ->
        when(state){
            is State.Loading -> {
                Log.i("subscribeObservers", "Loading: $state")
                this.activity?.progress_circular?.visibility = View.VISIBLE
            }

            is State.Success -> {
                Log.i("subscribeObservers", "Success: $state")
                state.getContentIfNotHandled()?.let {
                    success(state.data)
                    this.activity?.progress_circular?.visibility = View.GONE
                }
            }

            is State.Failed -> {
                Log.i("subscribeObservers", "Failed: $state")
                this.activity?.progress_circular?.visibility = View.GONE
            }
        }
    })
}

fun <T> AppCompatActivity.observe(liveData: LiveData<State<T>>, success: (T) -> Unit){

    liveData.observe(this, Observer { state ->
        when(state){
            is State.Loading -> {
                Log.i("subscribeObservers", "Loading: $state")
                progress_circular_auth.visibility = View.VISIBLE
            }

            is State.Success -> {
                Log.i("subscribeObservers", "Success: $state")
                success(state.data)
                progress_circular_auth.visibility = View.GONE
            }

            is State.Failed -> {
                Log.i("subscribeObservers", "Failed: $state")
                progress_circular_auth.visibility = View.GONE
            }
        }
    })
}
