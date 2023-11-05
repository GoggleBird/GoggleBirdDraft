package com.example.gogglebird

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.*

class ChallengesPage : AppCompatActivity() {
    // Declare variables
    private lateinit var tvHeading: TextView
    private lateinit var tvInstructions: TextView

    private lateinit var challengeOneImage: ImageView
    private lateinit var challengeTwoImage: ImageView
    private lateinit var challengeThreeImage: ImageView
    private lateinit var challengeFourImage: ImageView
    private lateinit var returnBtn: Button

    // Declare booleans -> track challenge completion status
    private var isChallengeOneCompleted = false
    private var isChallengeTwoCompleted = false
    private var isChallengeThreeCompleted = false
    private var isChallengeFourCompleted = false


    private lateinit var userEmail: String
    private lateinit var emailSharedPreferences: SharedPreferences
    //List to store user's stored data
    private lateinit var sightingsList: MutableList<Sightings>


    private lateinit var database: DatabaseReference


    //Challenge name constants
    companion object {
        const val CHALLENGE_ONE_NAME = "Just the Beginning..."
        const val CHALLENGE_TWO_NAME = "Sightings Pro"
        const val CHALLENGE_THREE_NAME = "Nocturnal Birder"
        const val CHALLENGE_FOUR_NAME = "Exotic Birdwatcher"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_challenges_page)

        // Typecast
        tvHeading = findViewById(R.id.tvHeading)
        tvInstructions = findViewById(R.id.tvInstructions)

        challengeOneImage = findViewById(R.id.ChallengeOneImage)
        challengeTwoImage = findViewById(R.id.ChallengeTwoImage)
        challengeThreeImage = findViewById(R.id.ChallengeThreeImage)
        challengeFourImage = findViewById(R.id.ChallengeFourImage)
        returnBtn = findViewById(R.id.returnBtn)

        // init database
        database =  FirebaseDatabase.getInstance().reference

        //Return to Home Page
        returnBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Get userEmail/UserId from sharedPref file
        emailSharedPreferences = getSharedPreferences("LoginEmail", Context.MODE_PRIVATE)
        userEmail = emailSharedPreferences.getString("email", "").toString()

        //Load Progress from sharedpref
        loadChallengeStatus()
        //Check Unlocked Status
        unlockChecker()
        //LoadSightings and Check Requirements
        LoadSightings()

    }


    //Check if options have been unlocked
    fun unlockChecker(){
        //Set enabled to unlocked boolean value
        //Set image based on unlocked status
        //Else Call touch listener method
        if (isChallengeOneCompleted){
            challengeOneImage.isEnabled = false
            challengeOneImage.setImageResource(R.drawable.challenge_one_unlocked)
        } else {
            setChallengeImageTouchListener(challengeOneImage)
        }

        if (isChallengeTwoCompleted){
            challengeTwoImage.isEnabled = false
            challengeTwoImage.setImageResource(R.drawable.challenge_two_unlocked)
        } else {
            setChallengeImageTouchListener(challengeTwoImage)
        }

        if (isChallengeThreeCompleted){
            challengeThreeImage.isEnabled = false
            challengeThreeImage.setImageResource(R.drawable.challenge_three_unlocked)
        } else {
            setChallengeImageTouchListener(challengeThreeImage)
        }

        if (isChallengeFourCompleted){
            challengeFourImage.isEnabled = false
            challengeFourImage.setImageResource(R.drawable.challenge_four_unlocked)
        } else {
            setChallengeImageTouchListener(challengeFourImage)
        }


    }

    //Method to set Image Touch listener
    private fun setChallengeImageTouchListener(challengeImage: ImageView) {
        challengeImage.setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // User pressed on the image, change the image to the pressed state
                    if (challengeImage == challengeOneImage){
                        challengeImage.setImageResource(R.drawable.challenge_one_pressed)
                    } else if (challengeImage == challengeTwoImage){
                        challengeImage.setImageResource(R.drawable.challenge_two_pressed)
                    }else if (challengeImage == challengeThreeImage){
                        challengeImage.setImageResource(R.drawable.challenge_three_pressed)
                    }else if (challengeImage == challengeFourImage){
                        challengeImage.setImageResource(R.drawable.challenge_four_pressed)
                    }
                }
                MotionEvent.ACTION_UP -> {
                    // User released their finger, open a dialog with requirements
                    showRequirementsDialog(challengeImage)
                }
            }
            true
        }
    }

    //Method to display Dialog Box functions
    private fun showRequirementsDialog(challengeImage: ImageView) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.challenge_requirements)
        dialog.setTitle("Challenge Requirements")

        // Declare Variables and Typecast
        val tvChallengesName = dialog.findViewById<TextView>(R.id.tvChallengeName)
        val tvReq = dialog.findViewById<TextView>(R.id.tvRequirements)

        var challengeName = ""
        var challengeReq  = ""

        // If statement to set values to string
        if (challengeImage == challengeOneImage){
            challengeName = CHALLENGE_ONE_NAME

            challengeReq = "Record One Sighting"

        } else if (challengeImage == challengeTwoImage){
            challengeName = CHALLENGE_TWO_NAME

            challengeReq = "Record Five Sightings"

        } else if (challengeImage == challengeThreeImage){
            challengeName = CHALLENGE_THREE_NAME

            challengeReq = "Record a Barn Owl"

        } else if (challengeImage == challengeFourImage){
            challengeName = CHALLENGE_FOUR_NAME

            challengeReq = "Record an Egyptian Goose"

        }

        //Set Values
        tvChallengesName.text = "$challengeName"
        tvReq.text = "$challengeReq"

        dialog.setOnDismissListener{
            // If the dialog is dismissed, you can reset the image to the default state
            if (!isChallengeOneCompleted){
                challengeOneImage.setImageResource(R.drawable.challenge_one_base)
            }
            if (!isChallengeTwoCompleted){
                challengeTwoImage.setImageResource(R.drawable.challenge_two_base)
            }
            if (!isChallengeThreeCompleted) {
                challengeThreeImage.setImageResource(R.drawable.challenge_three_base)
            }
            if (!isChallengeFourCompleted){
                challengeFourImage.setImageResource(R.drawable.challenge_four_base)
            }
        }

        dialog.show()
    }

    // Load sightings from database to RecyclerView
    private fun LoadSightings(){
        sightingsList = mutableListOf()

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

                //Check if Challenge Requirements have been met
                RequirementChecker(userSightings)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the error
                Toast.makeText(this@ChallengesPage,
                    "Failed to load sightings entries: ${error.message}",
                    Toast.LENGTH_SHORT).show()
            }
        })
    }

    //Method to check if user has met challenge requirements
    fun RequirementChecker(userSightings: List<Sightings>) {
        // Bool to avoid the unnecessary UnlockChecker method call
        var challengesCompleted = false

        // Challenge One Requirements
        if (!isChallengeOneCompleted) {
            val challengeOneRequirementsMet = userSightings.size >= 1
            if (challengeOneRequirementsMet) {
                isChallengeOneCompleted = true
                challengesCompleted = true
                showChallengeCompletedDialog(CHALLENGE_ONE_NAME)
            }
        }

        // Challenge Two Requirements
        if (!isChallengeTwoCompleted) {
            val challengeTwoRequirementsMet = userSightings.size >= 5
            if (challengeTwoRequirementsMet) {
                isChallengeTwoCompleted = true
                challengesCompleted = true
                showChallengeCompletedDialog(CHALLENGE_TWO_NAME)
            }
        }

        // Challenge Three Requirements
        if (!isChallengeThreeCompleted) {
            val challengeThreeRequirementsMet = userSightings.any { it.speciesName == "Barn Owl" }
            if (challengeThreeRequirementsMet) {
                isChallengeThreeCompleted = true
                challengesCompleted = true
                showChallengeCompletedDialog(CHALLENGE_THREE_NAME)
            }
        }

        // Challenge Four Requirements
        if (!isChallengeFourCompleted) {
            val challengeFourRequirementsMet = userSightings.any { it.speciesName == "Egyptian Goose" }
            if (challengeFourRequirementsMet) {
                isChallengeFourCompleted = true
                challengesCompleted = true
                showChallengeCompletedDialog(CHALLENGE_FOUR_NAME)
            }
        }

        if (challengesCompleted) {
            unlockChecker()
            //Call method - Save Challenge status to sharedPref
            saveChallengeStatus()
        }

    }
    // Method to show a challenge completed dialog
    fun showChallengeCompletedDialog(challengeName: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Challenge Completed")
        builder.setMessage("Congratulations! You have completed $challengeName.")
        builder.setPositiveButton("OK") { dialog, which -> dialog.dismiss() }
        builder.show()
    }

    // Function to save challenge completion status to SharedPreferences
    private fun saveChallengeStatus() {
        val challengesSharedPreferences =
            getSharedPreferences("ChallengesStatus", Context.MODE_PRIVATE)
        val editor = challengesSharedPreferences.edit()
        editor.putBoolean("isChallengeOneCompleted", isChallengeOneCompleted)
        editor.putBoolean("isChallengeTwoCompleted", isChallengeTwoCompleted)
        editor.putBoolean("isChallengeThreeCompleted", isChallengeThreeCompleted)
        editor.putBoolean("isChallengeFourCompleted", isChallengeFourCompleted)
        editor.apply()
    }
    private fun loadChallengeStatus() {
        val challengesSharedPreferences = getSharedPreferences("ChallengesStatus", Context.MODE_PRIVATE)
        isChallengeOneCompleted = challengesSharedPreferences.getBoolean("isChallengeOneCompleted", false)
        isChallengeTwoCompleted = challengesSharedPreferences.getBoolean("isChallengeTwoCompleted", false)
        isChallengeThreeCompleted = challengesSharedPreferences.getBoolean("isChallengeThreeCompleted", false)
        isChallengeFourCompleted = challengesSharedPreferences.getBoolean("isChallengeFourCompleted", false)
    }



}