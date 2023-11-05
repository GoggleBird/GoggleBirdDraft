package com.example.gogglebird

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import org.w3c.dom.Text

class Settings : AppCompatActivity() {

    private lateinit var textViewKM: TextView
    private lateinit var textViewMiles: TextView
    private lateinit var editTextDistanceFilter: EditText
    private lateinit var btnSubmitChange: Button
    private lateinit var returnBtn: Button

    companion object {
        var unitSelected: String = "km"
        var isKMselected: Boolean = true
        var isMilesSelected: Boolean = false
        var distanceSet: String = "1000"
        var isFirstLanding : Boolean = true
    }

    private var mileConversionFactor: Double = 0.621371
    private var kmConversionFactor: Double = 1.60934

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        textViewKM = findViewById(R.id.textViewKilometers)
        textViewMiles = findViewById(R.id.textViewMiles)
        editTextDistanceFilter = findViewById(R.id.editTextMaxDistanceVal)
        btnSubmitChange = findViewById(R.id.buttonSubmitDistanceChange)
        returnBtn = findViewById(R.id.returnBtn)


        //Return to Home Page
        returnBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }



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
            Toast.makeText(this,"Filter changed successfully", Toast.LENGTH_SHORT).show()
        }


    }
}