package com.example.library.di

import com.example.library.di.auth.AuthModule
import com.example.library.di.auth.AuthScope
import com.example.library.di.main.MainFragmentBuilderModule
import com.example.library.di.main.MainModule
import com.example.library.di.main.MainScope
import com.example.library.di.main.MainViewModelsModule
import com.example.library.ui.MainActivity
import com.example.library.ui.auth.AuthActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {

    @MainScope
    @ContributesAndroidInjector(
        modules = [MainFragmentBuilderModule::class,
            MainViewModelsModule::class,
            MainModule::class,
            NavigationModule::class]
    )
    abstract fun contributeMainActivity(): MainActivity

    @AuthScope
    @ContributesAndroidInjector(
        modules = [AuthModule::class,  NavigationModule::class]
    )
    abstract fun contributeAuthActivity(): AuthActivity

}