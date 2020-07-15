package com.example.library.ui.book_gender

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.library.BaseFragment
import com.example.library.R
import com.example.library.states.State
import kotlinx.android.synthetic.main.fragment_book_gender.*
import javax.inject.Inject


class BookGenderFragment : BaseFragment() {

    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory

    internal val viewModel: BookGenderViewModel by viewModels {
        viewModelProvider
    }

    override fun getLayoutResourceId(): Int = R.layout.fragment_book_gender

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureUI()
        subscribeObservers()
    }

    private fun configureUI() {

        fab_add_book_gender.setOnClickListener {
            showAddGenderDialog()
        }
    }

    private fun subscribeObservers() {
        viewModel.addBookGenderResponse.observe(viewLifecycleOwner, Observer {state ->
            when (state) {
                is State.Loading -> {
                    Log.i("subscribeObservers", "Loading: $state")
                }

                is State.Success -> {
                    Log.i("subscribeObservers", "Success: $state")
                }

                is State.Failed -> {
                    Log.i("subscribeObservers", "Failed: $state")
                }
            }
        })
    }
}