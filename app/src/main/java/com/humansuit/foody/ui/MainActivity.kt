package com.humansuit.foody.ui

import android.os.Bundle
import android.os.Message
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.andrognito.flashbar.Flashbar
import com.andrognito.flashbar.anim.FlashAnim
import com.google.android.material.navigation.NavigationView
import com.humansuit.foody.R
import com.humansuit.foody.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


    private val navController by lazy { findNavController(R.id.navHostFragment) }
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setupDrawerLayout()
        setupBottomNavigationView()

    }


    private fun setupBottomNavigationView() {
        NavigationUI.setupWithNavController(
            binding.mainContent.bottomNavigationView,
            navController
        )
    }


    private fun setupDrawerLayout() {
        binding.apply {
            val toggle = ActionBarDrawerToggle(
                this@MainActivity,
                binding.drawerLayout,
                binding.mainContent.toolbar,
                0,
                0
            )
            drawerLayout.addDrawerListener(toggle)
            toggle.syncState()
            navigationView.setNavigationItemSelectedListener(this@MainActivity)
        }
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

        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }


}