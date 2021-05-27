package com.platform45.weather45

import android.view.Menu
import androidx.navigation.NavController

interface MyDrawerController {
     var navController: NavController
     var toobarMenu: Menu?
    fun showMenu()
    fun hideMenu()
}