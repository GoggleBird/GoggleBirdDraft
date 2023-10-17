package com.example.gogglebird

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class AddSightings : AppCompatActivity() {
    // Declare variables
    private lateinit var tvHeading: TextView
    private lateinit var currentLocalInfoLabel: TextView
    private lateinit var UserLatitudeText: TextView
    private lateinit var UserLongitudeText: TextView
    private lateinit var SpeciesLabel: TextView
    private lateinit var speciesSpinner: Spinner
    private lateinit var NumberOfBirdsLabel: TextView
    private lateinit var edSpeciesName: EditText
    private lateinit var edNumBirds: EditText
    private lateinit var btnDate: Button
    private lateinit var btnTime: Button
    private lateinit var btnNext: Button

    private var selectedDate: Date? = null
    private var selectedTime: Date? = null

    private lateinit var btnShowEdSpecies: Button
    private lateinit var btnShowSpeciesSpinner: Button


    private val LOCATION_REQUEST_CODE = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_sightings)

        // Initialize
        tvHeading = findViewById(R.id.tvHeading)
        currentLocalInfoLabel = findViewById(R.id.currentLocalInfoLabel)
        UserLatitudeText = findViewById(R.id.UserLatitudeText)
        UserLongitudeText = findViewById(R.id.UserLongitudeText)
        SpeciesLabel = findViewById(R.id.SpeciesLabel)
        speciesSpinner = findViewById(R.id.speciesSpinner)
        NumberOfBirdsLabel = findViewById(R.id.NumberOfBirdsLabel)
        edSpeciesName = findViewById(R.id.edSpeciesName)
        edNumBirds = findViewById(R.id.edNumBirds)
        btnDate = findViewById(R.id.btnDate)
        btnTime = findViewById(R.id.btnTime)
        btnNext = findViewById(R.id.btnNext)
        btnShowEdSpecies = findViewById(R.id.btnShowEdSpecies)
        btnShowSpeciesSpinner = findViewById(R.id.btnShowSpeciesSpinner)

        //Set to invisible
        edSpeciesName.visibility = View.INVISIBLE
        btnShowSpeciesSpinner.visibility = View.INVISIBLE

        //Load onCreate
        managePermissions()

        getLocation()
        loadSpecies()

        //Set
        btnDate.setOnClickListener { showDatePicker(dateListener) }
        btnTime.setOnClickListener { showTimePicker(timeListener) }

        btnShowSpeciesSpinner.setOnClickListener {
            ShowSpinner()
        }
        btnShowEdSpecies.setOnClickListener {
            ShowSpeciesEditText()
        }

        btnNext.setOnClickListener {
            val speciesSelected = speciesSpinner.visibility == View.VISIBLE
            val species = if (speciesSelected) speciesSpinner.selectedItem.toString() else edSpeciesName.text.toString()

            //Check if required fields are filled
            if (UserLatitudeText.text != "Latitude: " && UserLongitudeText.text != "Longitude: "
                && species.isNotEmpty() && edNumBirds.text != null &&
                btnDate.text != "Date" && btnTime.text != "Time") {
                //Validate and parse the number of birds
                val numBirdsText = edNumBirds.text.toString()
                val numberOfBirds: Int? = numBirdsText.toIntOrNull()

                if (numberOfBirds == null) {
                    edNumBirds.error = "Please enter a valid number"
                    edNumBirds.requestFocus()
                } else {
                    // Save the entries to SharedPreferences and move to Confirmation screen
                    saveEntriesToSharedPreferences()
                    val intent = Intent(this, ConfirmSightingsPage::class.java)
                    startActivity(intent)
                }
            } else {
                //Error message near the missing/invalid field
                if (speciesSelected && species.isEmpty()) {
                    Toast.makeText(
                        this,
                        "Please select a species or enter a valid species name",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                }
            }
        }



    }
    //Methods

    //Get current Location
    private fun getLocation() {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 10000 //Update interval in milliseconds
        }

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations) {
                    // Update UI with the current location (location.latitude, location.longitude)
                    UserLatitudeText.text = location.latitude.toString()
                    UserLongitudeText.text = location.longitude.toString()
                    // If you only need one update, you can remove location updates here
                    fusedLocationProviderClient.removeLocationUpdates(this)
                }
            }
        }

        try {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null)
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }



    //Method to switch between edit text and spinner
    fun ShowSpinner() {
        speciesSpinner.visibility = View.VISIBLE
        edSpeciesName.visibility = View.INVISIBLE

        btnShowEdSpecies.visibility = View.VISIBLE
        btnShowSpeciesSpinner.visibility = View.INVISIBLE
    }

    fun ShowSpeciesEditText() {
        speciesSpinner.visibility = View.INVISIBLE
        edSpeciesName.visibility = View.VISIBLE

        btnShowEdSpecies.visibility = View.INVISIBLE
        btnShowSpeciesSpinner.visibility = View.VISIBLE
    }

    //method for DatePicker
    private fun showDatePicker(dateSetListener: DatePickerDialog.OnDateSetListener) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(this, dateSetListener, year, month, day)
        datePickerDialog.show()
    }

    //Method for timepicker
    private fun showTimePicker(timeSetListener: TimePickerDialog.OnTimeSetListener) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val timePickerDialog = TimePickerDialog(this, timeSetListener, hour, minute, true)
        timePickerDialog.show()
    }

    //listener methods
    private val dateListener =
        DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, month: Int, day: Int ->
            val selectedCalendar = Calendar.getInstance()
            selectedCalendar.set(year, month, day)
            selectedDate = selectedCalendar.time

            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val selectedDateString = dateFormat.format(selectedDate!!)
            btnDate.text = selectedDateString
        }

    private val timeListener =
        TimePickerDialog.OnTimeSetListener { _: TimePicker, hourOfDay: Int, minute: Int ->
            val selectedCalendar = Calendar.getInstance()
            if (selectedDate != null) {

                selectedCalendar.time = selectedDate!!
                selectedCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                selectedCalendar.set(Calendar.MINUTE, minute)
                selectedTime = selectedCalendar.time

                val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                val selectedTimeString = timeFormat.format(selectedTime!!)
                btnTime.text = selectedTimeString
            } else {
                Toast.makeText(
                    this, "Please Select Date First",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }

    //Load Spinner using JSON file
    private fun loadSpecies() {
        try {
            val inputStream: InputStream = assets.open("data.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            val json = String(buffer, Charset.forName("UTF-8"))

            // Parse the JSON data
            val jsonArray = JSONArray(json)

            // Create a list to store the common names
            val commonNames = ArrayList<String>()

            for (i in 0 until jsonArray.length()) {
                try {
                    val item: JSONObject = jsonArray.getJSONObject(i)
                    val commonName: String = item.getString("common_name")
                    commonNames.add(commonName)
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(this, "JSON Parsing Error", Toast.LENGTH_SHORT).show()
                }
            }

            // Populate your Spinner with the common names
            val spinner: Spinner = findViewById(R.id.speciesSpinner)
            val adapter: ArrayAdapter<String> =
                ArrayAdapter(this, android.R.layout.simple_spinner_item, commonNames)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter

        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
        }
    }

    // Method to save entries to SharedPreferences file
    private fun saveEntriesToSharedPreferences() {
        val sharedPrefs = getSharedPreferences("TempSightingData", Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        val latitude = UserLatitudeText.text.toString().toDoubleOrNull() ?: 0.0
        val longitude = UserLongitudeText.text.toString().toDoubleOrNull() ?: 0.0

        val species: String = if (edSpeciesName.visibility == View.VISIBLE) {
            edSpeciesName.text.toString()
        } else {
            speciesSpinner.selectedItem.toString()
        }

        val numberOfBirds = edNumBirds.text.toString().toIntOrNull() ?: 0
        // Date and Time Variables
        val dateString = btnDate.text.toString()
        val timeString = btnTime.text.toString()

        // Save selectedDate and selectedTime as strings
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        var selectedDate: Date? = null
        var selectedTime: Date? = null

        try {
            selectedDate = dateFormat.parse(dateString)
            selectedTime = timeFormat.parse(timeString)
        } catch (e: ParseException) {
            e.printStackTrace()
            Toast.makeText(this, "Error parsing date or time", Toast.LENGTH_SHORT).show()
        }
        // Store the values in SharedPreferences
        editor.putFloat("latitude", latitude.toFloat())
        editor.putFloat("longitude", longitude.toFloat())
        editor.putString("species", species)
        editor.putInt("numberOfBirds", numberOfBirds)

        // Store selectedDate and selectedTime as strings
        if (selectedDate != null) {
            editor.putString("selectedDate", dateFormat.format(selectedDate))
        }
        if (selectedTime != null) {
            editor.putString("selectedTime", timeFormat.format(selectedTime))
            editor.apply()

        }
    }

    //Methods to handle Permissions
    private fun managePermissions()
    {
        val requestPermission = mutableListOf<String>()
        if(!isLocationPermissionGranted()){
            //If these weren't granted
            requestPermission.add(android.Manifest.permission.ACCESS_FINE_LOCATION)
            requestPermission.add(android.Manifest.permission.ACCESS_COARSE_LOCATION)
        }
        if(requestPermission.isNotEmpty())
        {
            ActivityCompat.requestPermissions(this,
                requestPermission.toTypedArray(),
                LOCATION_REQUEST_CODE)
        }
    }
    private fun isLocationPermissionGranted(): Boolean{
        val fineLocation = ActivityCompat.checkSelfPermission(this,
            android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

        val coarseLocation = ActivityCompat.checkSelfPermission(this,
            android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

        return fineLocation && coarseLocation
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == LOCATION_REQUEST_CODE)
        {
            if(grantResults.isNotEmpty())
            {
                for (result in grantResults) {
                    if (result == PackageManager.PERMISSION_GRANTED) {
                        // Handle permission granted
                        // You can re-initialise the map here if needed
                        //setupMap()

                    } else {
                        Toast.makeText(this, "Permissions are granted", Toast.LENGTH_SHORT).show()

                    }
                }
            }
        }
    } // method ends

}
