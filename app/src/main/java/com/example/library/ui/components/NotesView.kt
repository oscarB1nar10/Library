package com.example.library.ui.components

import android.content.Context
import android.text.InputFilter
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.library.R
import kotlinx.android.synthetic.main.layout_notes.view.*

class NotesView : ConstraintLayout{

    companion object {
        const val DEFAULT_CHAR_LIMIT = 256
    }

    private var attrs: AttributeSet? = null

    private var title: String = ""
    private var characterLimit: Int =
        DEFAULT_CHAR_LIMIT


    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        this.attrs = attrs
        initialize()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        this.attrs = attrs
        initialize()
    }

    private fun initialize() {

        // create the notes layout and attach it to this view
        val root = inflate(context, R.layout.layout_notes, this)

        attrs?.let {
            context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.NotesView,
                0, 0).apply {

                try {
                    title = getString(R.styleable.NotesView_title) ?: context.getString(R.string.notes)
                    characterLimit = getInteger(R.styleable.NotesView_charLimit,
                        DEFAULT_CHAR_LIMIT
                    )
                } finally {
                    recycle()
                }
            }
        }

        // set title
        root.notes_label.text = title

        // set max character count
        root.notes_textarea.filters = listOf(
            InputFilter.LengthFilter(characterLimit)
        ).toTypedArray()

        root.notes_textarea.requestLayout()

        showNotesCharCounter("")

    }

    private fun showNotesCharCounter(notes: String) {
        notes_counter.text = context.getString(R.string.notes_counter, notes.length, characterLimit)
    }


    fun setText(notes: String) {
        notes_textarea.setText(notes)
    }

    fun getText(): String = notes_textarea.text.toString()
}