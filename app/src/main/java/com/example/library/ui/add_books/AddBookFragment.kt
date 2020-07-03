package com.example.library.ui.add_books

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.library.BaseFragment
import com.example.library.R
import com.example.library.states.State
import kotlinx.android.synthetic.main.fragment_add_books.*
import kotlinx.android.synthetic.main.layout_book_gender.*
import javax.inject.Inject

class AddBookFragment : BaseFragment() {

    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory

    private val viewModel: AddBookViewModel by viewModels {
        viewModelProvider
    }

    override fun getLayoutResourceId(): Int = R.layout.fragment_add_books

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureUI()
        subscribeObservers()
    }

    private fun configureUI() {

        cb_add_book.setOnClickListener {
            viewModel.addBook(buildBookFromFields())
        }

        text_gender_label.setOnClickListener {
            findNavController().navigate(R.id.action_addBooksFragment_to_genderFragment)
        }

    }

    private fun subscribeObservers() {
       viewModel.addBookResponse.observe(viewLifecycleOwner, Observer { state ->
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
