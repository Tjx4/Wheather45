package com.platform45.weather45

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
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
}