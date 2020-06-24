package com.example.library.ui.add_books

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.library.R
import com.example.library.states.State
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_add_books.*
import javax.inject.Inject

class AddBookFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory

    private val viewModel: AddBookViewModel by viewModels {
        viewModelProvider
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_books,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureUI()
        subscribeObservers()
    }

    private fun configureUI() {

        cb_add_book.setOnClickListener {
            viewModel.addBook(buildBookFromFields())
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
