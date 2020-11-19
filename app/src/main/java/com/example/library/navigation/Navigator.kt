package com.example.library.navigation

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.example.library.ui.MainActivity
import com.example.library.ui.auth.AuthActivity
import javax.inject.Inject

class Navigator
@Inject
constructor(context: Context){

    private val activity = context as Activity

    fun showDashboard(){
        activity.startActivity(Intent(activity, MainActivity::class.java).also {
            it.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        })
        activity.finish()
    }

    fun showAuthScreen(){
        activity.startActivity(Intent(activity, AuthActivity::class.java).also {
            it.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        })
    }
}