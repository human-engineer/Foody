package com.humansuit.foody.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.navigation.NavigationView
import com.humansuit.foody.R
import com.humansuit.foody.databinding.ActivityMainBinding
import com.humansuit.foody.network.RecipesApi
import com.humansuit.foody.network.RecipesApiFactory
import com.humansuit.foody.repository.RecipesRepository
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


    private val navController by lazy { findNavController(R.id.navHostFragment) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding : ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setupDrawerLayout()
        setupBottomNavigationView()

    }


    private fun setupBottomNavigationView() {
        NavigationUI.setupWithNavController(
            bottomNavigationView,
            navController
        )
    }


    private fun setupDrawerLayout() {
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            0,
            0
        )

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navigationView.setNavigationItemSelectedListener(this)
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.nav_profile -> {
                Toast.makeText(this, "Profile clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_messages -> {
                Toast.makeText(this, "Messages clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_friends -> {
                Toast.makeText(this, "Friends clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_update -> {
                Toast.makeText(this, "Update clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_logout -> {
                Toast.makeText(this, "Sign out clicked", Toast.LENGTH_SHORT).show()
            }
        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }


}