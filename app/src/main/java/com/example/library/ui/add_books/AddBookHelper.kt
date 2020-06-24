package com.example.library.ui.add_books

import com.example.library.models.Book

internal fun AddBookFragment.buildBookFromFields(): Book{
    return Book(
        pk = 1,
        name = "Black holes and small universes",
        image = "nothing",
        description = "scientific disclosure",
        editorial = "Critica",
        bookGenderId = 1,
        bookAuthorId = 1,
        bookOwnerId = 1
    )
}