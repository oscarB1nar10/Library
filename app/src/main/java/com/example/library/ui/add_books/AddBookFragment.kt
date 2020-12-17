package com.example.library.ui.add_books

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.library.BaseFragment
import com.example.library.R
import com.example.library.business.domain.states.State
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_add_books.*
import kotlinx.android.synthetic.main.layout_book_gender.*

@AndroidEntryPoint
class AddBookFragment : BaseFragment() {

    private val addBookViewModel: AddBookViewModel by viewModels()

    override fun getLayoutResourceId(): Int = R.layout.fragment_add_books

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureUI()
        subscribeObservers()
    }

    private fun configureUI() {

        cb_add_book.setOnClickListener {
            addBookViewModel.addBook(buildBookFromFields())
        }

        text_gender_label.setOnClickListener {
            findNavController().navigate(R.id.action_addBooksFragment_to_genderFragment)
        }

    }

    private fun subscribeObservers() {
        addBookViewModel.addBookResponse.observe(viewLifecycleOwner, Observer { state ->
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
