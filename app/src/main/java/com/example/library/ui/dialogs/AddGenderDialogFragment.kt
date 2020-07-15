package com.example.library.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.library.R
import com.example.library.models.Gender
import com.example.library.ui.book_gender.getGender
import kotlinx.android.synthetic.main.layout_gender_dialog.*
import kotlinx.android.synthetic.main.layout_gender_dialog.view.*

class AddGenderDialogFragment : DialogFragment(){

    var onAddGenderAction: ((gender: Gender) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(STYLE_NO_TITLE, R.style.BaseDialogFragment)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
         val rootView = inflater.inflate(R.layout.layout_gender_dialog, container, false)
        configureUI(rootView)
        return rootView
    }

    private fun configureUI(rootView: View) {

        rootView.tv_ok.setOnClickListener {
            onAddGenderAction?.invoke(
                getGender(
                    edit_book_gender_name.text.toString(),
                    notes_book_description.getText()
                )
            )
            dismiss()
        }

        rootView.tv_cancel.setOnClickListener {
            dismiss()
        }
    }

    companion object {

        const val BOOK_GENDER_DIALOG = "book_gender_dialog"

        fun newInstance() : AddGenderDialogFragment {
            return AddGenderDialogFragment()
        }
    }
}