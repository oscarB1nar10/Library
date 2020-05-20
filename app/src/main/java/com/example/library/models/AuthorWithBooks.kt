package com.example.library.models

import androidx.room.Embedded
import androidx.room.Relation

data class AuthorWithBooks(
    @Embedded val author: Author,

    @Relation(
        parentColumn = "pk",
        entityColumn = "bookAuthorId"
    )
    val book: List<Book>
)