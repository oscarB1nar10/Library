package com.example.library.util

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.library.states.State


fun <T> Fragment.observe(liveData: LiveData<State<T>>, success: (T) -> Unit){

    liveData.observe(this.viewLifecycleOwner, Observer { state ->
        when(state){
            is State.Loading -> {
                Log.i("subscribeObservers", "Loading: $state")
            }

            is State.Success -> {
                Log.i("subscribeObservers", "Success: $state")
                success(state.data)
                //viewModel.getBookGenderResponse
            }

            is State.Failed -> {
                Log.i("subscribeObservers", "Failed: $state")
            }
        }
    })
}

fun <T> AppCompatActivity.observe(liveData: LiveData<State<T>>, success: (T) -> Unit){

    liveData.observe(this, Observer { state ->
        when(state){
            is State.Loading -> {
                Log.i("subscribeObservers", "Loading: $state")
            }

            is State.Success -> {
                Log.i("subscribeObservers", "Success: $state")
                success(state.data)
                //viewModel.getBookGenderResponse
            }

            is State.Failed -> {
                Log.i("subscribeObservers", "Failed: $state")
            }
        }
    })
}