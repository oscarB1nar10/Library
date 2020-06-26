package com.example.library.di.main

import com.example.library.ui.add_books.AddBookFragment
import com.example.library.ui.book_gender.BookGenderFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainFragmentBuilderModule{

    @ContributesAndroidInjector
    abstract fun contributeAddBookFragment(): AddBookFragment

    @ContributesAndroidInjector
    abstract fun contributeABookGenderFragment(): BookGenderFragment
}