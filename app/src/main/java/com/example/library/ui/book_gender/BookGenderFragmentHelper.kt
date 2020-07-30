package com.example.library.ui.book_gender

import com.example.library.ui.dialogs.AddGenderDialogFragment
import com.example.library.ui.dialogs.AddGenderDialogFragment.Companion.BOOK_GENDER_DIALOG

fun BookGenderFragment.showAddGenderDialog(){
    val addBookGenderDialog = AddGenderDialogFragment.newInstance()

    activity?.let {
        requireActivity().supportFragmentManager.let {
            addBookGenderDialog.show(it, BOOK_GENDER_DIALOG)
        }
    }

    addBookGenderDialog.onAddGenderAction = {gender ->
        viewModel.gender = gender
        viewModel.saveBookGender(gender)
    }
}