package com.example.library.models

import androidx.room.Embedded
import androidx.room.Relation

data class OwnerWithBooks(
    @Embedded val owner: Owner,

    @Relation(
        parentColumn = "pk",
        entityColumn = "bookOwnerId"
    )
    val book: List<Book>
)