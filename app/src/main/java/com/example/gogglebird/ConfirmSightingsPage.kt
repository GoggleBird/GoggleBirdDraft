package com.example.gogglebird

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import java.io.FileOutputStream
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ConfirmSightingsPage : AppCompatActivity() {
    private lateinit var tvHeading: TextView
    private lateinit var currentLocalInfoLabel: TextView
    private lateinit var CurrentLocLabel: TextView
    private lateinit var tvCurrentLat: TextView
    private lateinit var tvCurrentLong: TextView
    private lateinit var SpeciesLabel: TextView
    private lateinit var tvSpecies: TextView
    private lateinit var DateTimeLabel: TextView
    private lateinit var tvDateTime: TextView
    private lateinit var NumBirdsLabel: TextView
    private lateinit var tvNumBirds: TextView
    private lateinit var btnAddPhoto: Button

    private lateinit var btnSaveSighting: Button
    private lateinit var btnReturn: Button
    private lateinit var edExtraInfo: EditText

    private lateinit var imageViewCamera: ImageView
    //ImagePath
    private var imagePath: String? = null


    //User ID
    private lateinit var userEmail: String
    private lateinit var emailSharedPreferences: SharedPreferences

    //Database
    private lateinit var database: DatabaseReference
    private var storageRef: StorageReference? = null

    private  var selectedDateGlobal: Date? = null
    private  var selectedTimeGlobal: Date? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_sightings_page)

        // Initialize the views
        tvHeading = findViewById(R.id.tvHeading)
        currentLocalInfoLabel = findViewById(R.id.currentLocalInfoLabel)
        CurrentLocLabel = findViewById(R.id.CurrentLocLabel)
        tvCurrentLat = findViewById(R.id.tvCurrentLat)
        tvCurrentLong = findViewById(R.id.tvCurrentLong)
        SpeciesLabel = findViewById(R.id.SpeciesLabel)
        tvSpecies = findViewById(R.id.tvSpecies)
        DateTimeLabel = findViewById(R.id.DateTimeLabel)
        tvDateTime = findViewById(R.id.tvDateTime)
        NumBirdsLabel = findViewById(R.id.NumBirdsLabel)
        tvNumBirds = findViewById(R.id.tvNumBirds)
        btnAddPhoto = findViewById(R.id.btnAddPhoto)
        imageViewCamera = findViewById(R.id.imageViewCamera)
        btnSaveSighting = findViewById(R.id.btnSaveSighting)
        btnReturn = findViewById(R.id.btnReturn)
        edExtraInfo = findViewById(R.id.edExtraInfo)

        //init the db
        database = FirebaseDatabase.getInstance().reference
        storageRef = FirebaseStorage.getInstance().reference

        //Get userEmail/UserId from sharedPref file
        emailSharedPreferences = getSharedPreferences("LoginEmail", Context.MODE_PRIVATE)
        userEmail = emailSharedPreferences.getString("email", "").toString()

        //Pull Retrieval Method
        SharedPrefSightingInfo()

        //Submit OnClickListener
        btnSaveSighting.setOnClickListener{ SaveSighting()}


    }

    //Methods

    //Generate a unique ID for entry
    private fun generateUniqueId(email: String): String {
        val timestamp = System.currentTimeMillis().toString()
        return email + "_" + timestamp
    }

    //Retrieve info from Shared Pref
    fun SharedPrefSightingInfo()
    {
        // Retrieving data from SharedPreferences
        val sharedPreferences = getSharedPreferences("TempSightingData", Context.MODE_PRIVATE)
        val currentLat = sharedPreferences.getFloat("latitude", 0.0F).toString()
        val currentLong = sharedPreferences.getFloat("longitude", 0.0F).toString()
        val species = sharedPreferences.getString("species", "")
        val date = sharedPreferences.getString("selectedDate", "")
        val time = sharedPreferences.getString("selectedTime", "")
        val numBirds = sharedPreferences.getInt("numberOfBirds", 0).toString()

        //Set Retrieved Info to TextViews
        tvCurrentLat.text = currentLat
        tvCurrentLong.text = currentLong
        tvSpecies.text = species
        tvDateTime.text = "$date $time" // Combine date and time
        tvNumBirds.text = numBirds

        //Convert Date and Time to Date?
        // Date and Time Variables
        val dateString = sharedPreferences.getString("selectedDate", "")
        val timeString = sharedPreferences.getString("selectedTime", "")

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        var selectedDate: Date? = null
        var selectedTime: Date? = null

        try {
            selectedDate = dateFormat.parse(dateString)
            selectedTime = timeFormat.parse(timeString)
        } catch (e: ParseException) {
            e.printStackTrace()
            Toast.makeText(this, "Error! Cannot save Date and Time correctly.", Toast.LENGTH_SHORT)
                .show()
        }

        if (selectedDate != null) {
            selectedDateGlobal = selectedDate
        }

        if (selectedTime != null) {
            selectedTimeGlobal = selectedTime
        }

    }

    //Save Sighting Entry
    fun SaveSighting(){

        if (tvCurrentLat.text != null && tvCurrentLong.text != null &&  tvSpecies.text != null
            && selectedDateGlobal != null && selectedTimeGlobal != null && imagePath!= null
            && tvSpecies.text != null && tvNumBirds.text != null && edExtraInfo.text != null) {
            //variables to add entry
            val entryId = generateUniqueId(userEmail)
            //Create entryName
            val entryName = "${tvSpecies.text}_${tvDateTime.text}"
            val currentLat = tvCurrentLat.text.toString()
            val currentLong = tvCurrentLong.text.toString()
            val speciesName = tvSpecies.text.toString()
            val date = selectedDateGlobal
            val time = selectedTimeGlobal
            val extraInfo = edExtraInfo.text.toString()
            val numBirds = tvNumBirds.text.toString()
            val imagePathInsert = imagePath

            //Set userEmail as userID
            val userId = userEmail


            // Save the sighting entry to Firebase
            saveToFirebase(entryId, entryName, currentLat, currentLong, speciesName, date , time,
                           extraInfo, numBirds, imagePathInsert, userId)

            Toast.makeText(this, "Entry saved!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Error! Fields cannot be left blank.", Toast.LENGTH_SHORT)
                .show()
        }
    }

    //Save entries to firebase
    private fun saveToFirebase(entryId: String, entryName: String, currentLat: String,
                               currentLong: String, speciesName: String, date: Date?, time: Date?,
                               extraInfo: String, numBirds: String, imagePathInsert: String?,
                               userId: String) {

        // Create a Sightings object with the entry details
        val sightingsEntry = Sightings(
            entryId = entryId,
            entryName = entryName,
            currentLatitude = currentLat,
            currentLongitude = currentLong,
            speciesName = speciesName,
            Date = date,
            Time = time,
            NumBirds = numBirds,
            extraInfo = extraInfo,
            imagePath = imagePathInsert,
            userId = userId
        )

        val sanitizedEntryId = entryId.replace(".", "")
            .replace("#", "")
            .replace("$", "")
            .replace("[", "")
            .replace("]", "")

        // Save sighting entry to the Realtime Database
        val entryRef = database.child("sightingsEntries").child(sanitizedEntryId)
        entryRef.setValue(sightingsEntry)
            .addOnSuccessListener {
                Toast.makeText(this, "Entry saved!", Toast.LENGTH_SHORT).show()

                // Upload image to Firebase Storage
                val imageUri = Uri.fromFile(File(imagePathInsert))
                val imageRef = storageRef?.child("sightingsImages")?.child("$sanitizedEntryId.jpg")
                val uploadTask = imageRef?.putFile(imageUri)

                uploadTask?.addOnSuccessListener {
                    // Get the image download URL
                    imageRef.downloadUrl.addOnSuccessListener { uri ->
                        // Update image path in the sighting entry
                        sightingsEntry.imagePath = uri.toString()

                        // Update the entry in the Realtime Database with the image URL
                        entryRef.setValue(sightingsEntry)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Image uploaded successfully", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, "Failed to link the image to the entry", Toast.LENGTH_SHORT).show()
                            }
                    }
                }?.addOnFailureListener {
                    Toast.makeText(this, "Failed to upload the image", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Entry could not be saved", Toast.LENGTH_SHORT).show()
            }

    }
    //Image Capture Methods
    //Camera Code
    fun captureOnClick(view: View?) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        try {
            super.onActivityResult(requestCode, resultCode, data)

            if (requestCode == 0 && resultCode == RESULT_OK) {
                val photoUri: Uri? = data?.data
                val bm: Bitmap? = if (photoUri != null) {
                    BitmapFactory.decodeStream(contentResolver.openInputStream(photoUri))
                } else {
                    data?.extras?.get("data") as Bitmap?
                }

                imageViewCamera?.setImageBitmap(bm)

                // Save the photo to the device and upload it to Firebase storage
                val photoPath = saveTemporaryPhotoToDevice(bm)
                imagePath = photoPath
            }
        } catch (ex: Exception) {
            Toast.makeText(this, "Pic not saved", Toast.LENGTH_SHORT).show()
        }
    }


    //Method to temporarily save image to device first, before uploading to firebase
    private fun saveTemporaryPhotoToDevice(bitmap: Bitmap?): String {
        val fileName = "temp_photo_${System.currentTimeMillis()}.jpg"
        val file = File(getExternalFilesDir("temp"), fileName)

        try {
            val fos = FileOutputStream(file)
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.flush()
            fos.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return file.absolutePath
    }

    //Delete temp images when the user exits the activity
    override fun onDestroy() {
        super.onDestroy()
        deleteTemporaryImages()
    }
    private fun deleteTemporaryImages() {
        val tempDirectory = File(getExternalFilesDir("temp")?.absolutePath.toString())
        if (tempDirectory.exists()) {
            val files = tempDirectory.listFiles()
            for (file in files!!) {
                file.delete()
            }
        }
    }

}