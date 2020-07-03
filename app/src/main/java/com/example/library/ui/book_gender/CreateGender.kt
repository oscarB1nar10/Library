package com.example.library.ui.book_gender

import com.example.library.models.Gender

fun getGender(name: String, description: String): Gender{
    return Gender(
        name = name,
        description = description
    )
}