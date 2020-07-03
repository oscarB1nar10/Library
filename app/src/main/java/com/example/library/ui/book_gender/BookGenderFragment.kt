package com.example.library.ui.book_gender

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.library.BaseFragment
import com.example.library.R
import kotlinx.android.synthetic.main.fragment_book_gender.*


class BookGenderFragment : BaseFragment() {

    lateinit var viewModelProvider: ViewModelProvider.Factory

    private val viewModel: BookGenderViewModel by viewModels {
        viewModelProvider
    }

    override fun getLayoutResourceId(): Int = R.layout.fragment_book_gender

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureUI()
    }

    private fun configureUI() {

        fab_add_book_gender.setOnClickListener {
            showAddGenderDialog()
        }
    }

}