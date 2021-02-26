package com.example.library.framework.presentation.book_gender

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.library.BaseActivity
import com.example.library.R
import com.example.library.business.domain.model.GenderModel
import com.example.library.databinding.FragmentBookGenderBinding
import com.example.library.ui.adapters.BookGenderRecyclerAdapter
import com.example.library.util.observe
import com.example.library.util.observeAndPreventsHandleEventAgain
import com.example.library.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_book_gender.*


@AndroidEntryPoint
class BookGenderFragment : Fragment(R.layout.fragment_book_gender) {

    private val binding by viewBinding(FragmentBookGenderBinding::bind)

    val bookGenderViewModel: BookGenderViewModel by viewModels()

    lateinit var bookGenderRecyclerAdapter: BookGenderRecyclerAdapter

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

        observeAndPreventsHandleEventAgain(
                bookGenderViewModel.getRemoteGendersFromRemoteResponse,
                ::handleGetRemoteGendersResponse
        )

        observe(
                bookGenderViewModel.synchronizeRemoteAndLocalGendersResponse,
                ::handleSynchronizeRemoteAndLocalGendersResponse
        )

        observe(bookGenderViewModel.getBookGendersFromCacheResponse, ::handleGetBookGendersFromCache)

        observe(bookGenderViewModel.saveGenderResponse, ::handleSaveGenderResponse)

        observe(bookGenderViewModel.updateGenderResponse, ::handleUpdateGenderResponse)

        observeAndPreventsHandleEventAgain(
                bookGenderViewModel.removeGenderResponse,
                ::handleRemoveGenderResponse
        )
    }

    private fun handleGetRemoteGendersResponse(genders: List<GenderModel>) {
        bookGenderViewModel.saveBookGendersInLocalDB(genders)
    }

    private fun handleSynchronizeRemoteAndLocalGendersResponse(genders: List<GenderModel>) {
        bookGenderRecyclerAdapter.submitList(genders)
    }

    private fun handleGetBookGendersFromCache(genders: List<GenderModel>) {
        bookGenderRecyclerAdapter.submitList(genders)
    }

    private fun handleSaveGenderResponse(successFulMessage: String) {
        Log.i("saveGenderR", "Success: $successFulMessage")
    }

    private fun handleUpdateGenderResponse(successFulMessage: String) {
        if (successFulMessage.isNotEmpty()) {
            (activity as BaseActivity).showBanner(
                    getString(R.string.add_book_gender_gender_updated),
                    BaseActivity.BannerType.SUCCESS
            )
        }
    }

    private fun handleRemoveGenderResponse(successFulMessage: String) {
        Toast.makeText(activity, "Gender removed $successFulMessage", Toast.LENGTH_LONG).show()
    }

    private fun editGender(gender: GenderModel) {
        showGenderToEdit(gender)
    }

    private fun deleteGender(gender: GenderModel) {
        bookGenderViewModel.removeBookGender(gender)
    }
}