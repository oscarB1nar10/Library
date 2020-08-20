package com.example.library.navigation

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.example.library.ui.MainActivity
import javax.inject.Inject

class Navigator
@Inject
constructor(context: Context){

    private val activity = context as Activity

    fun showDashboard(){
        activity.startActivity(Intent(activity, MainActivity::class.java))
    }
}