package com.example.library.di

import android.app.Application
import com.example.library.BaseApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
            AndroidInjectionModule::class,
            ActivityBuildersModule::class,
            ViewModelFactoryModule::class,
            AppModule::class]
)
interface AppComponent : AndroidInjector<BaseApplication>{


    @Component.Builder
    interface Builder{

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}