package com.example.library.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.library.R
import com.example.library.ui.book_gender.getGender
import kotlinx.android.synthetic.main.layout_book_gender_item.*
import kotlinx.android.synthetic.main.layout_gender_dialog.*

class AddGenderDialogFragment : DialogFragment(){

    var onAddGenderAction: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(STYLE_NO_TITLE, R.style.BaseDialogFragment)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

         val rootView = inflater.inflate(R.layout.layout_gender_dialog, container)

        cb_add_book_gender.setOnClickListener {
            onAddGenderAction?.invoke()
        }

        getGender(
            edit_book_gender_name.text.toString(),
            notes_book_description.getText()
        )

        return rootView
    }

    companion object {

        fun newInstance() : AddGenderDialogFragment {
            return AddGenderDialogFragment()
        }
    }
}