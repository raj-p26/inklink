package com.example.inklink

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView

import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout

import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    /*
    *   -- CLIENT SIDE START DATE 22/10/23 --
    *   -- CLIENT SIDE COMPLETE --
    *
    *   -- ADMIN SIDE START DATE: 27/10/2023 --
    *   Expected End Date: 31/10/2023
    *
    *   -- ADMIN SIDE DONE DATE 28/10/23 --
    *   Project is finally finished!!!
    * */

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var headerView: View
    private var menu: Menu? = null
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var username: TextView
    private lateinit var email: TextView
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var frameLayout: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.navigation_view)
        navView.setNavigationItemSelectedListener(this)
        menu = navView.menu
        actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close)
        sharedPreferences = getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        frameLayout = findViewById(R.id.fragment_layout)

        if (
            sharedPreferences
                .getString("email", null)
                .equals("admin@inklink.com")
        ) {
            val intent = Intent(this, AdminActivity::class.java)

            startActivity(intent)
            finish()
        }

        // Header of navigation drawer
        headerView = navView.getHeaderView(0)
        username = headerView.findViewById(R.id.nav_header_user_name)
        email = headerView.findViewById(R.id.nav_header_user_email)

        drawerLayout.addDrawerListener(this.actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        updateNavMenu()
        checkPreferences()

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_layout, HomeFragment())
            .commit()
    }

    override fun onCreateOptionsMenu(optMenu: Menu?): Boolean {
        val sharedPrefs = sharedPreferences
        if (sharedPrefs.getBoolean("isLoggedIn", false)) {
            optMenu?.add(Menu.NONE, R.integer.create_article_opt_id, Menu.NONE, "Create a new Article")
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when {
            actionBarDrawerToggle.onOptionsItemSelected(item) -> true
            item.itemId == R.integer.create_article_opt_id -> {
                val intent = Intent(this, CreateArticleActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun updateNavMenu() {
        val sharedPreferences = sharedPreferences
        val subMenu2 = menu?.addSubMenu(2, Menu.NONE, 0, "User")

        if (!sharedPreferences.getBoolean("isLoggedIn", false)) {
            subMenu2?.add(Menu.NONE, R.integer.login_opt_id, 0, "Login")
            subMenu2?.add(2, R.integer.signup_opt_id, 0, "Sign Up")
        } else {
            subMenu2?.add(Menu.NONE, R.integer.my_posts_id, 1, "My Posts")
            subMenu2?.add(Menu.NONE, R.integer.my_profile_id, 2, "Profile")
            subMenu2?.add(Menu.NONE, R.integer.logout_opt_id, 3, "Log Out")
        }
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        when(p0.itemId) {
            R.integer.login_opt_id -> {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
            R.integer.signup_opt_id -> {
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
            }
            R.integer.logout_opt_id -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Log out?")
                builder.setMessage("Are you sure you want to log out?")
                builder.setPositiveButton("Yes"
                ) { _, _ ->
                    val editor = sharedPreferences.edit()
                    val intent = Intent(this@MainActivity, MainActivity::class.java)

                    editor.clear()
                    editor.apply()

                    startActivity(intent)
                    finish()
                }
                builder.setNegativeButton("No", null)

                builder.create().show()
            }

            R.integer.my_posts_id -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_layout, MyPostsFragment())
                    .commit()
                drawerLayout.closeDrawer(GravityCompat.START)
            }

            R.integer.my_profile_id -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_layout, ProfileFragment())
                    .commit()
                drawerLayout.closeDrawer(GravityCompat.START)
            }

            R.id.nav_home -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_layout, HomeFragment())
                    .commit()
                drawerLayout.closeDrawer(GravityCompat.START)
            }

            R.id.nav_users -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_layout, UsersFragment())
                    .commit()
                drawerLayout.closeDrawer(GravityCompat.START)
            }

            R.id.nav_articles -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_layout, ArticlesFragment())
                    .commit()
                drawerLayout.closeDrawer(GravityCompat.START)
            }
        }
        return true
    }

    private fun checkPreferences() {
        if (
            sharedPreferences.getString("fname", null) != null ||
            sharedPreferences.getString("email", null) != null
        ) {
            username.text = sharedPreferences.getString("fname", "Guest")
            email.text = sharedPreferences.getString("email", "guest@inklink.com")
        }
    }
}
