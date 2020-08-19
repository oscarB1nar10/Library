package com.example.library.navigation

import android.content.Intent
import com.example.library.BaseActivity
import com.example.library.ui.MainActivity
import javax.inject.Inject

class Navigator
@Inject
constructor(private val activity: BaseActivity){


    fun showDashboard(){
        activity.startActivity(Intent(activity, MainActivity::class.java))
    }
}