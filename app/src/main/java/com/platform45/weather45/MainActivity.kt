package com.platform45.weather45

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.platform45.weather45.features.history.HistoryFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), MyDrawerController{
    override lateinit var navController: NavController
    override var toobarMenu: Menu? = null
    var historFrag: HistoryFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        setSupportActionBar(toolbar)
         navController = this.findNavController(R.id.navControllerFragment)
         setupWithNavController(toolbar, navController)


        /*
        setSupportActionBar(toolbar)
        val navController = this.findNavController(R.id.navControllerFragment)
        setupActionBarWithNavController(this, navController, drawer_layout)
        setupWithNavController(nav_view, navController)
        */
    }

/*
    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.navControllerFragment)
        return NavigationUI.navigateUp(navController, drawer_layout)
    }
*/
    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.history_menu, menu)
        toobarMenu = menu
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_convert -> navController.navigate(R.id.history_to_conversion)
            R.id.action_find -> historFrag?.toggleSelector()
        }
        return NavigationUI.onNavDestinationSelected(item, navController) || super.onOptionsItemSelected(item)
    }

    override fun showMenu() {
        toobarMenu?.let {
            menuInflater.inflate(R.menu.history_menu, it)
        }
    }

    override fun hideMenu() {
        toobarMenu?.clear()
    }

    override fun badFrag(historyFragment: HistoryFragment) {
        historFrag = historyFragment
    }
}