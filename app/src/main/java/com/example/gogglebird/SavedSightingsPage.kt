package com.example.gogglebird

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.StorageReference
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class SavedSightingsPage : AppCompatActivity() {
    // Declare variables
    private lateinit var tvHeading: TextView

    private lateinit var rvSavedSightings: RecyclerView
    private lateinit var sightingsAdapter: SightingsAdapter
    private lateinit var sightingsList: MutableList<Sightings>

    private lateinit var returnBtn: Button


    private lateinit var database: DatabaseReference
    private var storageRef: StorageReference? = null

    private lateinit var userEmail: String
    private lateinit var emailSharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_sightings_page)

        // init database
        database =  FirebaseDatabase.getInstance().reference

        // Initialize variables
        tvHeading = findViewById(R.id.tvHeading)
        rvSavedSightings = findViewById(R.id.rvSavedSightings)
        returnBtn = findViewById(R.id.returnBtn)


        //Return to Home Page
        returnBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Get userEmail/UserId from sharedPref file
        emailSharedPreferences = getSharedPreferences("LoginEmail", Context.MODE_PRIVATE)
        userEmail = emailSharedPreferences.getString("email", "").toString()

        //Load Saved Sightings
        LoadSightings()

    }//end OnCreate



    //Methods

    // Load sightings from database to RecyclerView
    private fun LoadSightings(){
        // Initialize RecyclerView and adapter
        sightingsList = mutableListOf()
        sightingsAdapter = SightingsAdapter(sightingsList, this@SavedSightingsPage)
        rvSavedSightings.layoutManager = LinearLayoutManager(this@SavedSightingsPage)

        rvSavedSightings.adapter = sightingsAdapter

        // Fetch sightings entries from the database
        val sightingsRef = database.child("sightingsEntries")
        sightingsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                sightingsList.clear()

                for (sightingsSnapshot in snapshot.children) {
                    val sighting = sightingsSnapshot.getValue(Sightings::class.java)
                    sighting?.let {
                        sightingsList.add(it)
                    }
                }

                // Filter the sighting entries based on the user ID
                val userSightings = sightingsList.filter { sighting ->
                    sighting.userId == userEmail
                }

                // Update the adapter with the updated sightings list
                sightingsAdapter.setData(userSightings)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the error
                Toast.makeText(this@SavedSightingsPage,
                    "Failed to load sightings entries: ${error.message}",
                    Toast.LENGTH_SHORT).show()
            }
        })
    }
    //Click Image to open in new Activity (Fullscreen)
    fun onItemClick(sightings: Sightings) {

        val imagePath = sightings.imagePath
        val intent = Intent(this@SavedSightingsPage, FullImage::class.java)
        intent.putExtra("imagePath", imagePath)
        startActivity(intent)
    }
}