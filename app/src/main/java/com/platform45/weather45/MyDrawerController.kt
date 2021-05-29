package com.platform45.weather45

import android.view.Menu
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.platform45.weather45.features.history.HistoryFragment

interface MyDrawerController {
     var navController: NavController
     var toobarMenu: Menu?
    fun showMenu()
    fun hideMenu()
    fun badFrag(bf: HistoryFragment)
    fun showContent()
    fun showSelectionMode()
}