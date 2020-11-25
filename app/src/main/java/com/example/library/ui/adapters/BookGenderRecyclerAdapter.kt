package com.example.library.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.example.library.R
import com.example.library.models.Gender
import kotlinx.android.synthetic.main.layout_book_gender_item.view.*
import kotlinx.android.synthetic.main.layout_notes.view.*

class BookGenderRecyclerAdapter(
    private val editGender: (Gender) -> Unit,
    private val deleteGender: (Gender) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Gender>() {

        override fun areItemsTheSame(oldItem: Gender, newItem: Gender): Boolean {
            return oldItem.pk == newItem.pk
        }

        override fun areContentsTheSame(oldItem: Gender, newItem: Gender): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return BookGenderViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_book_gender_item,
                parent,
                false
            ),
            editGender,
            deleteGender
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is BookGenderViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<Gender>) {
        differ.submitList(list)
    }

    class BookGenderViewHolder
    constructor(
        itemView: View,
        val editGender: (Gender) -> Unit,
        val deleteGender: (Gender) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: Gender) = with(itemView) {
            itemView.edit_book_gender_name.text = item.name
            itemView.notes_book_description.setText(item.description.toString())
            itemView.notes_book_description.notes_textarea.isFocusable = false
            itemView.notes_book_description.notes_textarea.isClickable = false

            setupGenderActionsListener(itemView, item)
        }

        private fun setupGenderActionsListener(view: View, gender: Gender){
            view.iv_remove_gender.setOnClickListener {
                deleteGender(gender)
            }

            view.iv_edit_gender.setOnClickListener {
                editGender(gender)
            }
        }
    }
}