package com.example.library.di

import com.example.library.BaseActivity
import com.example.library.navigation.Navigator
import com.example.library.navigation.RootCoordinator
import dagger.Module
import dagger.Provides

@Module
object NavigationModule {

    @Provides
    fun provideCoordinator(navigator: Navigator): RootCoordinator {
        return RootCoordinator(navigator)
    }

    @Provides
    fun provideNavigator(activity: BaseActivity): Navigator {
        return Navigator(activity)
    }
}