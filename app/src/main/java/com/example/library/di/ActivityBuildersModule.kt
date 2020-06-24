package com.example.library.di

import com.example.library.di.main.MainFragmentBuilderModule
import com.example.library.di.main.MainModule
import com.example.library.di.main.MainScope
import com.example.library.di.main.MainViewModelsModule
import com.example.library.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {

    @MainScope
    @ContributesAndroidInjector(
        modules = [MainFragmentBuilderModule::class, MainViewModelsModule::class, MainModule::class]
    )
    abstract fun contributeMainActivity(): MainActivity

}