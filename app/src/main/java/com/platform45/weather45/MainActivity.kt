package com.platform45.weather45

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.platform45.weather45.features.history.HistoryFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MyDrawerController{
    override lateinit var navController: NavController
    override var toobarMenu: Menu? = null
    var convertMenuItem: MenuItem? = null
    var findMenuItem: MenuItem? = null
    var closeMenuItem: MenuItem? = null
    var historFrag: HistoryFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        navController = this.findNavController(R.id.navControllerFragment)
        setupWithNavController(toolbar, navController)

        //NavigationUI.setupActionBarWithNavController(this, navController)
        /*
        val navController = this.findNavController(R.id.navControllerFragment)
        setupActionBarWithNavController(this, navController, drawer_layout)
        setupWithNavController(nav_view, navController)
        */
    }

/*
    override fun onSupportNavigateUp(): Boolean {
        return Navigation.findNavController(this, R.id.navControllerFragment).navigateUp();
    }
*/
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.history_menu, menu)
        convertMenuItem = menu.findItem(R.id.action_convert)
        findMenuItem = menu.findItem(R.id.action_select)
        closeMenuItem = menu.findItem(R.id.action_close_selection)
        toobarMenu = menu
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_convert -> navController.navigate(R.id.history_to_conversion)
            R.id.action_select -> historFrag?.showPairSelector()
            R.id.action_close_selection -> historFrag?.showPairSeriesInfo()
        }
        return NavigationUI.onNavDestinationSelected(item, navController) || super.onOptionsItemSelected(item)
    }

    override fun showMenu() {
        toobarMenu?.let {
            menuInflater.inflate(R.menu.history_menu, it)
            convertMenuItem = it.findItem(R.id.action_convert)
            findMenuItem = it.findItem(R.id.action_select)
            closeMenuItem = it.findItem(R.id.action_close_selection)
        }
    }

    override fun hideMenu() {
        toobarMenu?.clear()
    }

    override fun setHistoryFragment(historyFragment: HistoryFragment) {
        historFrag = historyFragment
    }

    override fun setTitle(title: String) {
        toolbar?.title = title
    }

    override fun showContent() {
        findMenuItem?.isVisible = true
        closeMenuItem?.isVisible = false
        convertMenuItem?.isVisible = true
        toolbar.isVisible = true
    }

    override fun showSelectionMode() {
        findMenuItem?.isVisible = false
        closeMenuItem?.isVisible = true
        convertMenuItem?.isVisible = false
        toolbar.isVisible = true
    }

    override fun hideToolbar() {
        toolbar.isVisible = false
    }
}