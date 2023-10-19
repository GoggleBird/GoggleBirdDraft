package com.example.gogglebird

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import org.w3c.dom.Text

class Settings : AppCompatActivity() {

    private lateinit var textViewKM: TextView
    private lateinit var textViewMiles: TextView
    private lateinit var userGreeting: TextView
    private lateinit var editTextDistanceFilter: EditText
    private lateinit var btnSubmitChange: Button

    companion object {
        lateinit var unitSelected: String
        var isKMselected: Boolean = true
        var isMilesSelected: Boolean = false
        lateinit var distanceSet: String
        var isFirstLanding : Boolean = true
    }

    private var mileConversionFactor: Double = 0.621371
    private var kmConversionFactor: Double = 1.60934

    private lateinit var userEmail :String
    private lateinit var emailSharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        textViewKM = findViewById(R.id.textViewKilometers)
        textViewMiles = findViewById(R.id.textViewMiles)
        editTextDistanceFilter = findViewById(R.id.editTextMaxDistanceVal)
        btnSubmitChange = findViewById(R.id.buttonSubmitDistanceChange)

        //Get userEmail from sharedPref file
        emailSharedPreferences = getSharedPreferences("LoginEmail", Context.MODE_PRIVATE)
        userEmail = emailSharedPreferences.getString("email", "").toString()

        userGreeting = findViewById(R.id.settingsTextViewUserEmail)
        userGreeting.text = "Hello, $userEmail!"



        if (isKMselected) {
            // set to help prevent crash on startup
            unitSelected = "km"
            textViewKM.setBackgroundResource(R.drawable.units_background)
            textViewMiles.setBackgroundResource(R.drawable.units_background_unselected)
        }

        if (isMilesSelected) {
            unitSelected = "miles"
            textViewMiles.setBackgroundResource(R.drawable.units_background)
            textViewKM.setBackgroundResource(R.drawable.units_background_unselected)
        }

        textViewKM.setOnClickListener {
            isKMselected = true
            isMilesSelected = false

            textViewKM.setBackgroundResource(R.drawable.units_background)
            textViewMiles.setBackgroundResource(R.drawable.units_background_unselected)

            unitSelected = "km"

            // takes current val of edittext and sets it to the converted factor
            var textToKM = editTextDistanceFilter.text.toString().toDouble() * kmConversionFactor
            editTextDistanceFilter.setText(textToKM.toString())

        }

        textViewMiles.setOnClickListener {
            isMilesSelected = true
            isKMselected = false

            textViewMiles.setBackgroundResource(R.drawable.units_background)
            textViewKM.setBackgroundResource(R.drawable.units_background_unselected)

            unitSelected = "miles"

            // takes current val of edittext and sets it to the converted factor
            var textToMiles = editTextDistanceFilter.text.toString().toDouble() * mileConversionFactor
            editTextDistanceFilter.setText(textToMiles.toString())

        }

        if (isFirstLanding)
        {
            if (isKMselected)
            {
                distanceSet = 100.00.toString()
                editTextDistanceFilter.setText(distanceSet)
            }
            else
            {
                val defaultInMiles = 100.00 * mileConversionFactor
                distanceSet = defaultInMiles.toString()
                editTextDistanceFilter.setText(distanceSet)
            }
        }
        else
        {
            editTextDistanceFilter.setText(distanceSet)
        }


        btnSubmitChange.setOnClickListener {
            distanceSet = editTextDistanceFilter.text.toString()
            editTextDistanceFilter.setText(distanceSet)
            isFirstLanding = false
        }


    }
}