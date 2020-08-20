package com.example.library.di

import android.content.Context
import com.example.library.navigation.Navigator
import com.example.library.navigation.RootCoordinator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext

@Module
@InstallIn(ActivityComponent::class)
object NavigationModule {

    @Provides
    fun provideCoordinator(navigator: Navigator): RootCoordinator {
        return RootCoordinator(navigator)
    }

    @Provides
    fun provideNavigator(@ActivityContext context: Context): Navigator {
        return Navigator(context)
    }
}