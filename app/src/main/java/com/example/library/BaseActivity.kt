package com.example.library

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.layout_toolbar_component.*

abstract class BaseActivity: AppCompatActivity(){

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