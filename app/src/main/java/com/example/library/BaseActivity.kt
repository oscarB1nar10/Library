package com.example.library

import android.os.Bundle
import dagger.android.support.DaggerAppCompatActivity

abstract class BaseActivity: DaggerAppCompatActivity(){

    abstract fun getLayoutResourceId(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResourceId())

        setupToolbar()
    }

    protected open fun setupToolbar() {
        // screens with toolbar should override this method
        setupBackButton()
    }

}