package com.example.library.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.library.R
import com.example.library.models.Gender
import com.example.library.ui.book_gender.getGender
import com.example.library.util.hideKeyboard
import com.example.library.util.showKeyboard
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val gender: Gender? = getGender()
        if(gender != null){
            populateFields(gender)
            setupClickListerWhenEditionIsCanceled()
        }
    }

    private fun populateFields(gender: Gender) {
        edit_book_gender_name.setText(gender.name.toString())
        notes_book_description.setText(gender.description.toString())
        notes_book_description.requestFocus()
        activity?.showKeyboard()
    }

    private fun setupClickListerWhenEditionIsCanceled() {
        tv_cancel.setOnClickListener {
            dismiss()
            activity?.let { tv_cancel.hideKeyboard(it) }
        }
    }

    private fun getGender() : Gender? = arguments?.getParcelable(BOOK_GENDER_TO_EDIT)

    companion object {

        const val BOOK_GENDER_TO_EDIT = "book_gender_to_edit"
        const val BOOK_GENDER_DIALOG = "book_gender_dialog"

        fun newInstance() : AddGenderDialogFragment {
            return AddGenderDialogFragment()
        }

        fun newInstance(genderToEdit: Gender): AddGenderDialogFragment {
            val fragment = AddGenderDialogFragment()

            val bundle = Bundle()
            bundle.putParcelable(BOOK_GENDER_TO_EDIT, genderToEdit)
            fragment.arguments = bundle

            return fragment
        }
    }
}