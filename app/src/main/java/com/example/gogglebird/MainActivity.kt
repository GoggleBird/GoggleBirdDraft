package com.example.gogglebird

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var fragmentManagerVar: FragmentManager
    private lateinit var floatingActionButton: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        floatingActionButton = findViewById(R.id.fab)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView: NavigationView = findViewById(R.id.navigation_drawer)
        navigationView.setNavigationItemSelectedListener(this)

        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.background = null

        // create intent for sightings
        val toSightings = Intent(this, SavedSightingsPage::class.java)
        val addSighting = Intent(this, AddSightings::class.java)


        bottomNavigationView.setOnItemSelectedListener(object : NavigationBarView.OnItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                val itemID = item.itemId

                if (itemID == R.id.map)
                {
                    openFragment(HomeFragment())
                    return true
                }
                else if (itemID == R.id.saved_sightings)
                {
                    startActivity(toSightings)
                    return true
                } else if (itemID == R.id.add_sighting)
                {
                    openFragment(AboutFragment())
                    return true
                } else if (itemID == R.id.profile_page)
                {
                    openFragment(ProfileFragment())
                    return true
                }

                return false
            }
        })

        fragmentManagerVar = supportFragmentManager
        openFragment(HomeFragment())

        floatingActionButton.setOnClickListener {
            startActivity(addSighting)
        }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val itemID = item.itemId

        if (itemID == R.id.Recordings) {
            val intentRecordings = Intent(this, Recordings::class.java)
            startActivity(intentRecordings)
            //Toast.makeText(this,"Temp One",Toast.LENGTH_SHORT).show()
        } else if (itemID == R.id.TopSightings) {

            val intentTopSightings = Intent(this, TopSightings::class.java)
            startActivity(intentTopSightings)
            //Toast.makeText(this,"Temp Two",Toast.LENGTH_SHORT).show()
        } else if (itemID == R.id.tempThree) {
            Toast.makeText(this,"Temp Three",Toast.LENGTH_SHORT).show()
        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }

}