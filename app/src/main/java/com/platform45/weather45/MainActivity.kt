package com.platform45.weather45

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        setSupportActionBar(toolbar)
        val navController = this.findNavController(R.id.navControllerFragment)
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
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //findNavController().navigate(R.id.history_to_conversion)
        return NavigationUI.onNavDestinationSelected(item, this.findNavController(R.id.navControllerFragment)) || super.onOptionsItemSelected(item)
    }
}