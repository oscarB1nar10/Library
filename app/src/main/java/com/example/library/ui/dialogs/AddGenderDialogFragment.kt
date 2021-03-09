package com.example.library.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.library.R
import com.example.library.business.domain.model.GenderModel
import com.example.library.framework.presentation.book_gender.createGender
import com.example.library.framework.presentation.book_gender.genderToUpdate
import com.example.library.util.hideKeyboard
import com.example.library.util.showKeyboard
import kotlinx.android.synthetic.main.layout_gender_dialog.*
import kotlinx.android.synthetic.main.layout_gender_dialog.view.*

class AddGenderDialogFragment : DialogFragment() {

    var onAddGenderAction: ((gender: GenderModel) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(STYLE_NO_TITLE, R.style.BaseDialogFragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.layout_gender_dialog, container, false)
        configureUI(rootView)
        return rootView
    }

    private fun configureUI(rootView: View) {

        rootView.tv_ok.setOnClickListener {
            onAddGenderAction?.invoke(
                createGender(
                    name = edit_book_gender_name.text.toString(),
                    description = notes_book_description.getText()
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
        val gender: GenderModel? = getGender()
        if (gender != null) {
            populateFields(gender)
            setupClickListerWhenUpdatedIsSelected()
            setupClickListerWhenEditionIsCanceled()
        }
    }

    private fun populateFields(gender: GenderModel) {
        edit_book_gender_name.setText(gender.name.toString())
        notes_book_description.setText(gender.description.toString())
        notes_book_description.requestFocus()
        activity?.showKeyboard()
    }

    private fun setupClickListerWhenUpdatedIsSelected(){
        tv_ok.setOnClickListener {
            onAddGenderAction?.invoke(
                genderToUpdate(
                    name = edit_book_gender_name.text.toString(),
                    description = notes_book_description.getText(),
                    currentGender = getGender() ?: GenderModel()
                )
            )
            dismiss()
            activity?.let { tv_ok.hideKeyboard(it) }
        }
    }

    private fun setupClickListerWhenEditionIsCanceled() {
        tv_cancel.setOnClickListener {
            dismiss()
            activity?.let { tv_cancel.hideKeyboard(it) }
        }
    }

    private fun getGender(): GenderModel? = arguments?.getParcelable(BOOK_GENDER_TO_EDIT)

    companion object {

        const val BOOK_GENDER_TO_EDIT = "book_gender_to_edit"
        const val BOOK_GENDER_DIALOG = "book_gender_dialog"

        fun newInstance(): AddGenderDialogFragment {
            return AddGenderDialogFragment()
        }

        fun newInstance(genderToEdit: GenderModel): AddGenderDialogFragment {
            val fragment = AddGenderDialogFragment()

            val bundle = Bundle()
            bundle.putParcelable(BOOK_GENDER_TO_EDIT, genderToEdit)
            fragment.arguments = bundle

            return fragment
        }
    }
}