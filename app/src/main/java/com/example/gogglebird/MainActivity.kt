package com.example.gogglebird

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
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

    private lateinit var avatarImage : ImageView
    private lateinit var btnCustomise : Button


    //AVATAR DISPLAY
    // SharedPreferences for optionSelect
    private lateinit var optionSelectSharedPreferences: SharedPreferences

    //int to check which option is selected
    private var optionSelect : Int = 0


    //USER WELCOME

    private lateinit var userEmail :String
    private lateinit var emailSharedPreferences: SharedPreferences
    private lateinit var userGreeting: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //AVATAR DISPLAY
        //Typecast
        avatarImage = findViewById(R.id.ImageUserAvatar)
        btnCustomise = findViewById(R.id.btnCustomise)


        // Initialize SharedPreferences
        optionSelectSharedPreferences =
            getSharedPreferences("OptionSelect", Context.MODE_PRIVATE)

        // Load optionSelect value from SharedPreferences, or default to 0
        optionSelect = optionSelectSharedPreferences.getInt("optionSelect", 0)


        AvatarImageUpdater()

        //USER WELCOME

        //Get userEmail from sharedPref file
        emailSharedPreferences = getSharedPreferences("LoginEmail", Context.MODE_PRIVATE)
        userEmail = emailSharedPreferences.getString("email", "").toString()

        userGreeting = findViewById(R.id.tvWelcomeHeading)
        userGreeting.text = "Hello, $userEmail!"

        btnCustomise.setOnClickListener {
            val intent = Intent(this, AvatarCustomisation::class.java)
            startActivity(intent) }

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
        val toChallengesPage = Intent(this, ChallengesPage::class.java)
        val toMapview = Intent(this, Map::class.java)

        val toSettings = Intent(this, Settings::class.java)

        bottomNavigationView.setOnItemSelectedListener(object : NavigationBarView.OnItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                val itemID = item.itemId

                if (itemID == R.id.map)
                {
                    startActivity(toMapview)
                    return true
                }
                else if (itemID == R.id.saved_sightings)
                {
                    startActivity(toSightings)
                    return true
                } else if (itemID == R.id.challenges)
                {
                    startActivity(toChallengesPage)
                    return true
                } else if (itemID == R.id.profile_page)
                {
                    startActivity(toSettings)
                    return true
                }

                return false
            }
        })

        fragmentManagerVar = supportFragmentManager
       // openFragment(HomeFragment())
       // startActivity(toMapview)

        floatingActionButton.setOnClickListener {
            startActivity(addSighting)
        }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val itemID = item.itemId

        if (itemID == R.id.AboutPage) {
            val intentAboutPage = Intent(this, AboutPage::class.java)
            startActivity(intentAboutPage )
        } else if (itemID == R.id.TopSightings) {

            val intentTopSightings = Intent(this, TopSightings::class.java)
            startActivity(intentTopSightings)
        }else if (itemID == R.id.CommonBirds) {

            val intentCommonBirds = Intent(this, CommonBirds::class.java)
            startActivity(intentCommonBirds)
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

    //Method to update avatar image
    fun AvatarImageUpdater(){
        //If option selection matches one of the following buttons
        if (optionSelect == 0){
            //Update Avatar image
            avatarImage.setImageResource(R.drawable.avatar_base)
        }
        else if (optionSelect == 1){
            avatarImage.setImageResource(R.drawable.avatar_cone)
        }
        else if (optionSelect == 2){
            avatarImage.setImageResource(R.drawable.avatar_paper)
        }
        else if (optionSelect == 3){
            avatarImage.setImageResource(R.drawable.avatar_top_hat)
        }
        else if (optionSelect == 4){
            avatarImage.setImageResource(R.drawable.avatar_crown)
        } else{
            avatarImage.setImageResource(R.drawable.avatar_base)
        }
    }

}