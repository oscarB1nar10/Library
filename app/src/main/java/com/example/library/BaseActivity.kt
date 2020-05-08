package com.example.library

import android.os.Bundle
import androidx.appcompat.app.ActionBar
import dagger.android.support.DaggerAppCompatActivity

abstract class BaseActivity: DaggerAppCompatActivity(){

    abstract fun getLayoutResourceId(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResourceId())

        supportActionBar?.let {
            it.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
            it.setCustomView(R.layout.layout_toolbar_component)
        }
    }

    protected open fun setupToolbar() {
        // screens with toolbar should override this method
        setupBackButton()
    }

}