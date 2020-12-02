package com.example.library.ui.book_gender

import com.example.library.models.Gender

fun getGender(id: Int, name: String, description: String): Gender{
    return Gender(
        pk = id,
        name = name,
        description = description
    )
}