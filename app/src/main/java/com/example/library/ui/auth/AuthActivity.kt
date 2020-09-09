package com.example.library.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log.d
import android.widget.Toast
import com.example.library.BaseActivity
import com.example.library.R
import com.example.library.navigation.RootCoordinator
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_auth_layout.*
import javax.inject.Inject

@AndroidEntryPoint
class AuthActivity : BaseActivity() {

    companion object{
        const val SIGN_IN_REQUEST_CODE = 100
    }

    @Inject
    lateinit var googleSignIn: GoogleSignInClient

    @Inject
    lateinit var coordinator: RootCoordinator

    override fun getLayoutResourceId(): Int  = R.layout.activity_auth_layout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        configureUi()

    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null)
        val account: GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(this)
        account?.let {
            coordinator.showDashboard()
        }
    }

    private fun configureUi() {
        sign_in_button.setOnClickListener {
            signInGoogle()
        }
    }

    private fun signInGoogle(){
        val signInIntent: Intent = googleSignIn.signInIntent
        startActivityForResult(signInIntent, SIGN_IN_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == SIGN_IN_REQUEST_CODE){
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult(task)
        }
    }

    private fun handleResult(completedTask: Task<GoogleSignInAccount>) {
        try{
            completedTask.getResult(ApiException::class.java)?.let {account ->
               d("handleResult", "account info: $account")
                coordinator.showDashboard()
            }
        }catch (e: ApiException){
            d("handleResult", e.message.toString())
            Toast.makeText(this, getString(R.string.auth_login_error),Toast.LENGTH_SHORT)
        }
    }
}