package com.example.library.models

import androidx.room.Embedded
import androidx.room.Relation

data class GenderWithBooks(
    @Embedded val gender: Gender,

    @Relation(
        parentColumn = "pk",
        entityColumn = "bookGenderId"
    )
    val book: List<Book>
)