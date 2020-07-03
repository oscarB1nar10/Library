package com.example.library.di.main

import androidx.lifecycle.ViewModel
import com.example.library.ui.add_books.AddBookViewModel
import com.example.library.ui.book_gender.BookGenderViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MainViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(AddBookViewModel::class)
    abstract fun bindAddBookViewModel(viewModel: AddBookViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BookGenderViewModel::class)
    abstract fun bindAddBookGenderViewModel(viewModel: BookGenderViewModel): ViewModel
}