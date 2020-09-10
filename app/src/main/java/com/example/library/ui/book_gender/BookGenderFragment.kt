package com.example.library.ui.book_gender

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.library.BaseFragment
import com.example.library.R
import com.example.library.models.Gender
import com.example.library.states.State
import com.example.library.ui.adapters.BookGenderRecyclerAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_book_gender.*


@AndroidEntryPoint
class BookGenderFragment : BaseFragment(), BookGenderRecyclerAdapter.Interaction {

    val bookGenderViewModel: BookGenderViewModel by viewModels()

    lateinit var bookGenderRecyclerAdapter: BookGenderRecyclerAdapter

    override fun getLayoutResourceId(): Int = R.layout.fragment_book_gender

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureUI()
        subscribeObservers()
    }

    private fun configureUI() {

        setupRecyclerAdapter()

        fab_add_book_gender.setOnClickListener {
            showAddGenderDialog() }

        bookGenderViewModel.getRemoteGenders()
    }

    private fun setupRecyclerAdapter() {
        recycler_gender_items.apply {
            layoutManager = LinearLayoutManager(activity)
            bookGenderRecyclerAdapter = BookGenderRecyclerAdapter(this@BookGenderFragment)
            adapter = bookGenderRecyclerAdapter
        }
    }

    private fun subscribeObservers() {
        bookGenderViewModel.saveGenderResponse.observe(viewLifecycleOwner, Observer {state ->
            when (state) {
                is State.Loading -> {
                    Log.i("subscribeObservers", "Loading: $state")
                }

                is State.Success -> {
                    Log.i("subscribeObservers", "Success: $state")
                    //viewModel.getBookGenderResponse
                }

                is State.Failed -> {
                    Log.i("subscribeObservers", "Failed: $state")
                }
            }
        })

        bookGenderViewModel.getRemoteGendersResponse.observe(viewLifecycleOwner, Observer {state ->
            when(state){
                is State.Loading -> {
                    Log.i("subscribeObservers", "Loading: $state")
                }

                is State.Success -> {
                    bookGenderViewModel.saveBookGendersInLocalDB( state.data)
                }

                is State.Failed -> {
                    Log.i("subscribeObservers", "Failed: $state")
                }
            }
        })

        bookGenderViewModel.synchronizeRemoteAndLocalGendersResponse.observe(viewLifecycleOwner, Observer {state ->
            when(state){
                is State.Loading -> {
                    Log.i("subscribeObservers", "Loading: $state")
                }

                is State.Success -> {
                   // After remote data synchronized with local data
                    bookGenderRecyclerAdapter.submitList(state.data)
                }

                is State.Failed -> {
                    Log.i("subscribeObservers", "Failed: $state")
                }
            }
        })

        bookGenderViewModel.removeGenderResponse.observe(viewLifecycleOwner, Observer {state ->
            when(state){
                is State.Loading -> {
                    Log.i("subscribeObservers", "Loading: $state")
                }

                is State.Success -> {
                    Toast.makeText(activity, "Gender removed", Toast.LENGTH_LONG).show()
                }

                is State.Failed -> {
                    Log.i("subscribeObservers", "Failed: $state")
                }
            }
        })
    }

    override fun onItemSelected(position: Int, gender: Gender) {
        //TODO("Not yet implemented")
        bookGenderViewModel.removeBookGender(gender)
    }
}