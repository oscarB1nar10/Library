package com.example.library.ui.book_gender

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.library.BaseFragment
import com.example.library.R
import com.example.library.models.Gender
import com.example.library.states.State
import com.example.library.ui.adapters.BookGenderRecyclerAdapter
import kotlinx.android.synthetic.main.fragment_book_gender.*
import javax.inject.Inject


class BookGenderFragment : BaseFragment(), BookGenderRecyclerAdapter.Interaction {

    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory

    lateinit var bookGenderRecyclerAdapter: BookGenderRecyclerAdapter

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

        setupRecyclerAdapter()

        fab_add_book_gender.setOnClickListener {
            showAddGenderDialog()
        }
    }

    private fun setupRecyclerAdapter() {
        recycler_gender_items.apply {
            layoutManager = LinearLayoutManager(activity)
            bookGenderRecyclerAdapter = BookGenderRecyclerAdapter(this@BookGenderFragment)
            adapter = bookGenderRecyclerAdapter
        }
    }

    private fun subscribeObservers() {
        viewModel.addBookGenderResponseRemoteDB.observe(viewLifecycleOwner, Observer {state ->
            when (state) {
                is State.Loading -> {
                    Log.i("subscribeObservers", "Loading: $state")
                }

                is State.Success -> {
                    Log.i("subscribeObservers", "Success: $state")
                    viewModel.addBookGenderLocalDB()
                }

                is State.Failed -> {
                    Log.i("subscribeObservers", "Failed: $state")
                }
            }
        })

        viewModel.addBookGenderResponseLocalDB.observe(viewLifecycleOwner, Observer {state ->
            when (state) {
                is State.Loading -> {
                    Log.i("subscribeObservers", "Loading: $state")
                }

                is State.Success -> {
                    Log.i("genderSaved:", "Success: ${state.data}")
                    //bookGenderRecyclerAdapter.submitList()
                }

                is State.Failed -> {
                    Log.i("subscribeObservers", "Failed: $state")
                }
            }
        })
    }

    override fun onItemSelected(position: Int, item: Gender) {
        //TODO("Not yet implemented")
    }
}