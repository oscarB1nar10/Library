package com.example.library.di.main

import com.example.library.ui.add_books.AddBookFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainFragmentBuilderModule{

    @ContributesAndroidInjector
    abstract fun contributeAddBookFragment(): AddBookFragment
}