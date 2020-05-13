package com.example.library

import android.os.Bundle
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.layout_toolbar_component.*

abstract class BaseActivity: DaggerAppCompatActivity(){

    abstract fun getLayoutResourceId(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResourceId())

        setSupportActionBar(my_toolbar)
    }

    protected open fun setupToolbar() {
        // screens with toolbar should override this method
        setupBackButton()
    }

}