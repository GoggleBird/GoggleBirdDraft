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
    private lateinit var startDateButton: Button
    private lateinit var endDateButton: Button
    private lateinit var filterButton: Button

    private lateinit var rvSavedSightings: RecyclerView
    private lateinit var sightingsAdapter: SightingsAdapter
    private lateinit var sightingsList: MutableList<Sightings>

    private lateinit var returnBtn: Button


    private lateinit var database: DatabaseReference
    private var storageRef: StorageReference? = null

    //DatePicker variables
    private var selectedStartDate: Date? = null
    private var selectedEndDate: Date? = null

    private lateinit var userEmail: String
    private lateinit var emailSharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_sightings_page)

        // init database
        database =  FirebaseDatabase.getInstance().reference

        // Initialize variables
        tvHeading = findViewById(R.id.tvHeading)
        startDateButton = findViewById(R.id.startDateButton)
        endDateButton = findViewById(R.id.endDateButton)
        filterButton = findViewById(R.id.filterButton)
        rvSavedSightings = findViewById(R.id.rvSavedSightings)
        returnBtn = findViewById(R.id.returnBtn)

        //Set
        startDateButton.setOnClickListener { showDatePicker(startDateListener) }
        endDateButton.setOnClickListener { showDatePicker(endDateListener) }

        // Get userEmail/UserId from sharedPref file
        emailSharedPreferences = getSharedPreferences("LoginEmail", Context.MODE_PRIVATE)
        userEmail = emailSharedPreferences.getString("email", "").toString()

        //Load Saved Sightings
        LoadSightings()

        //Filter onClickListener
        filterButton.setOnClickListener { filterSightings() }

    }//end OnCreate



    //Methods
    //DatePicker and Listener
    private fun showDatePicker(dateSetListener: DatePickerDialog.OnDateSetListener) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(this, dateSetListener, year, month, day)
        datePickerDialog.show()
    }

    private val startDateListener = DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, month: Int, day: Int ->
        val selectedCalendar = Calendar.getInstance()
        selectedCalendar.set(year, month, day)
        selectedStartDate = selectedCalendar.time

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val selectedDateString = dateFormat.format(selectedStartDate!!)
        startDateButton.text = selectedDateString
    }
    private val endDateListener = DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, month: Int, day: Int ->
        val selectedCalendar = Calendar.getInstance()
        selectedCalendar.set(year, month, day)
        selectedEndDate = selectedCalendar.time

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val selectedDateString = dateFormat.format(selectedEndDate!!)
        endDateButton.text = selectedDateString
    }

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

    private fun filterSightings() {
        val startDateString = startDateButton.text.toString()
        val endDateString = endDateButton.text.toString()

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        var selectedStartDate: Date? = null
        var selectedEndDate: Date? = null

        try {
            selectedStartDate = dateFormat.parse(startDateString)
            selectedEndDate = dateFormat.parse(endDateString)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        // Filter the sightings entries based on the selected start and end dates and userId
        val filteredSightings = sightingsList.filter { sightings ->
            val entryDate = sightings.Date
            entryDate != null && (entryDate >= selectedStartDate || selectedStartDate == null)
                    && (entryDate <= selectedEndDate || selectedEndDate == null)
                    && sightings.userId == userEmail
        }

        // Update the adapter with the filtered sightings entries
        sightingsAdapter = SightingsAdapter(filteredSightings, this)

        // Set RecyclerView layout manager and adapter
        rvSavedSightings.layoutManager = LinearLayoutManager(this)
        rvSavedSightings.adapter = sightingsAdapter
    }

}