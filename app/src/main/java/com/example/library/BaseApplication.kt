package com.example.library

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.components.ApplicationComponent

@HiltAndroidApp
class BaseApplication: Application()