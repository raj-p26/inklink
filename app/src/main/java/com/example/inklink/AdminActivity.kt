package com.example.inklink

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class AdminActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var headerView: View
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        navigationView = findViewById(R.id.admin_navigation_view)
        drawerLayout = findViewById(R.id.admin_drawer_layout)
        actionBarDrawerToggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            R.string.admin_nav_open,
            R.string.admin_nav_close
        )

        headerView = navigationView.getHeaderView(0)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        navigationView.menu.getItem(0).isChecked = true
        navigationView.setNavigationItemSelectedListener(this)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.admin_fragment_layout, AdminListArticleFragment())
            .commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return actionBarDrawerToggle.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        when (p0.itemId) {
            R.id.nav_list_articles -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.admin_fragment_layout, AdminListArticleFragment())
                    .commit()
                drawerLayout.closeDrawer(Gravity.LEFT)
            }

            R.id.nav_list_users -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.admin_fragment_layout, AdminListUserFragment())
                    .commit()
                drawerLayout.closeDrawer(Gravity.LEFT)
            }

            R.id.nav_list_reported_users -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.admin_fragment_layout, ViewReportedUsersFragment())
                    .commit()
                drawerLayout.closeDrawer(Gravity.LEFT)
            }

            R.id.nav_list_reported_articles -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.admin_fragment_layout, ViewReportedArticlesFragment())
                    .commit()
                drawerLayout.closeDrawer(Gravity.LEFT)
            }

            R.id.admin_logout -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Log out?")
                builder.setMessage("Are you sure you want to log out?")
                builder.setPositiveButton("Yes") {_, _ ->
                    val intent = Intent(this, MainActivity::class.java)
                    val prefs = getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
                    val editor = prefs.edit()

                    editor.clear().apply()

                    startActivity(intent)
                    finish()
                }

                builder.setNegativeButton("No", null)

                builder.create().show()
            }
        }

        return true
    }
}
