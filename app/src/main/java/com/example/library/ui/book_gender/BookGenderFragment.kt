package com.example.library.ui.book_gender

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.library.BaseActivity
import com.example.library.BaseFragment
import com.example.library.R
import com.example.library.business.domain.model.GenderModel
import com.example.library.ui.adapters.BookGenderRecyclerAdapter
import com.example.library.util.observe
import com.example.library.util.observeAndPreventsHandleEventAgain
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_book_gender.*


@AndroidEntryPoint
class BookGenderFragment : BaseFragment() {

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
            showAddGenderDialog()
        }
    }

    private fun setupRecyclerAdapter() {
        recycler_gender_items.apply {
            layoutManager = LinearLayoutManager(activity)
            bookGenderRecyclerAdapter = BookGenderRecyclerAdapter(
                ::editGender,
                ::deleteGender
            )
            adapter = bookGenderRecyclerAdapter
        }
    }

    private fun subscribeObservers() {

        observe(bookGenderViewModel.saveGenderResponse, ::handleSaveGenderResponse)

        observe(bookGenderViewModel.updateGenderResponse, ::handleUpdateGenderResponse)

        observeAndPreventsHandleEventAgain(
            bookGenderViewModel.getRemoteGendersResponse,
            ::handleGetRemoteGendersResponse
        )

        observe(
            bookGenderViewModel.synchronizeRemoteAndLocalGendersResponse,
            ::handleSynchronizeRemoteAndLocalGendersResponse
        )

        observeAndPreventsHandleEventAgain(
            bookGenderViewModel.removeGenderResponse,
            ::handleRemoveGenderResponse
        )
    }

    private fun handleSaveGenderResponse(wasDataSaved: Boolean) {
        Log.i("saveGenderR", "Success: $wasDataSaved")
    }

    private fun handleUpdateGenderResponse(wasGenderUpdated: Boolean) {
        if (wasGenderUpdated) {
            (activity as BaseActivity).showBanner(
                getString(R.string.add_book_gender_gender_updated),
                BaseActivity.BannerType.SUCCESS
            )
        }
    }

    private fun handleGetRemoteGendersResponse(genders: List<GenderModel>) {
        bookGenderViewModel.saveBookGendersInLocalDB(genders)
    }

    private fun handleSynchronizeRemoteAndLocalGendersResponse(genders: List<GenderModel>) {
        bookGenderRecyclerAdapter.submitList(genders)
    }

    private fun handleRemoveGenderResponse(wasDeleted: Boolean) {
        Toast.makeText(activity, "Gender removed", Toast.LENGTH_LONG).show()
    }

    private fun editGender(gender: GenderModel) {
        showGenderToEdit(gender)
    }

    private fun deleteGender(gender: GenderModel) {
        bookGenderViewModel.removeBookGender(gender)
    }
}