package com.example.library.ui

import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.library.BaseActivity
import com.example.library.R
import com.example.library.navigation.RootCoordinator
import com.example.library.ui.auth.UserPreferencesPresenter
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private val navController by lazy{
        Navigation.findNavController(this, R.id.my_nav_host_fragment)
    }

    @Inject lateinit var coordinator: RootCoordinator

    @Inject lateinit var mGoogleSignInClient: GoogleSignInClient

    @Inject lateinit var userPreferencesPresenter: UserPreferencesPresenter

    override fun getLayoutResourceId() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupDrawerLayout()
        configureUI()
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, drawer_layout)
    }

    private fun setupDrawerLayout(){
        nav_view.setupWithNavController(navController)
        NavigationUI.setupActionBarWithNavController(this, navController, drawer_layout)
    }

    private fun configureUI() {
        nav_view.menu.findItem(R.id.logout).setOnMenuItemClickListener {
            lifecycleScope.launch {
                userPreferencesPresenter.removeUserToken()
            }
            logout()
            true
        }
    }

    private fun logout(){
        mGoogleSignInClient.signOut()
        coordinator.navigateToAuth()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
