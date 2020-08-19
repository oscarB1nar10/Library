package com.example.library.navigation

import javax.inject.Inject

class RootCoordinator
@Inject
constructor(private val navigator: Navigator){

    fun showDashboard() {
        navigator.showDashboard()
    }
}