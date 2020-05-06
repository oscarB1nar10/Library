package com.example.library.di

import com.example.library.di.main.MainScope
import com.example.library.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {

    @MainScope
    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity

}