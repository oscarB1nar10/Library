package com.example.library.ui.add_books

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.library.R
import com.example.library.business.domain.states.State
import com.example.library.databinding.FragmentAddBooksBinding
import com.example.library.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddBookFragment : Fragment(R.layout.fragment_add_books) {

    private val binding by viewBinding(FragmentAddBooksBinding::bind)

    private val addBookViewModel: AddBookViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureUI()
        subscribeObservers()
    }

    private fun configureUI() {

        binding.cbAddBook.setOnClickListener {
            addBookViewModel.addBook(buildBookFromFields())
        }

        binding.clBookGender.textGenderLabel.setOnClickListener {
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
