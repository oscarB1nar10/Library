package com.example.library.ui.book_gender

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.library.BaseFragment
import com.example.library.R
import com.example.library.models.Gender
import com.example.library.ui.adapters.BookGenderRecyclerAdapter
import com.example.library.util.observe
import com.example.library.util.observeAndPreventsHandleEventAgain
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
    }

    private fun setupRecyclerAdapter() {
        recycler_gender_items.apply {
            layoutManager = LinearLayoutManager(activity)
            bookGenderRecyclerAdapter = BookGenderRecyclerAdapter(this@BookGenderFragment)
            adapter = bookGenderRecyclerAdapter
        }
    }

    private fun subscribeObservers() {

        observe(bookGenderViewModel.saveGenderResponse, ::handleSaveGenderResponse)

        observeAndPreventsHandleEventAgain(bookGenderViewModel.getRemoteGendersResponse, ::handleGetRemoteGendersResponse)

        observe(bookGenderViewModel.synchronizeRemoteAndLocalGendersResponse, ::handleSynchronizeRemoteAndLocalGendersResponse)

        observeAndPreventsHandleEventAgain(bookGenderViewModel.removeGenderResponse, ::handleRemoveGenderResponse)
    }

    private fun handleSaveGenderResponse(wasDataSaved: Boolean){
        Log.i("saveGenderR", "Success: $wasDataSaved")
    }

    private fun handleGetRemoteGendersResponse(genders: List<Gender>){
        bookGenderViewModel.saveBookGendersInLocalDB(genders)
    }

    private fun handleSynchronizeRemoteAndLocalGendersResponse(genders: List<Gender>){
        bookGenderRecyclerAdapter.submitList(genders)
    }

    private fun handleRemoveGenderResponse(wasDeleted: Boolean){
        Toast.makeText(activity, "Gender removed", Toast.LENGTH_LONG).show()
    }

    override fun onItemSelected(position: Int, gender: Gender) {
        //TODO("Not implemented yet")
        bookGenderViewModel.removeBookGender(gender)
    }
}