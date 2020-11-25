package com.example.library.ui.book_gender

import com.example.library.models.Gender
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
        bookGenderViewModel.gender = gender
        bookGenderViewModel.saveGender(gender)
    }
}

fun BookGenderFragment.showGenderToEdit(gender: Gender){
    val addBookGenderDialog = AddGenderDialogFragment.newInstance(gender)

    activity?.let {
        requireActivity().supportFragmentManager.let {
            addBookGenderDialog.show(it, BOOK_GENDER_DIALOG)
        }
    }

    addBookGenderDialog.onAddGenderAction = {gender ->
        bookGenderViewModel.gender = gender
        bookGenderViewModel.saveGender(gender)
    }
}


