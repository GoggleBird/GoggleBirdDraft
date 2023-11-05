package com.example.gogglebird

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView

class AvatarCustomisation : AppCompatActivity() {
    private lateinit var avatarImage : ImageView
    private lateinit var noneButton : ImageView
    private lateinit var coneButton : ImageView
    private lateinit var paperButton : ImageView
    private lateinit var topHatButton : ImageView
    private lateinit var crownButton : ImageView
    private lateinit var returnBtn : Button

    //boolean to check if option is unlocked
    private var coneUnlocked = false
    private var paperUnlocked = false
    private var topHatUnlocked = false
    private var crownUnlocked = false

    //int to check which option is selected
    private var optionSelect : Int = 0


    // SharedPreferences for optionSelect
    private lateinit var optionSelectSharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_avatar_customisation)

        //Typecast
        avatarImage = findViewById(R.id.AvatarImage)
        noneButton = findViewById(R.id.ImageNoneButton)
        coneButton = findViewById(R.id.ImageConeButton)
        paperButton = findViewById(R.id.ImagePaperButton)
        topHatButton = findViewById(R.id.ImageTopHatButton)
        crownButton = findViewById(R.id.ImageCrownButton)
        returnBtn = findViewById(R.id.returnBtn)


        //Return to Home Page
        returnBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Initialize SharedPreferences
        optionSelectSharedPreferences =
            getSharedPreferences("OptionSelect", Context.MODE_PRIVATE)

        // Load optionSelect value from SharedPreferences, or default to 0
        optionSelect = optionSelectSharedPreferences.getInt("optionSelect", 0)

        //Check Unlock Status
        unlockChecker()
        ButtonUpdater()
        AvatarImageUpdater()

        noneButton.setOnClickListener{

            optionSelect = 0

            //Update Avatar
            AvatarImageUpdater()
            ButtonUpdater()
            saveOptionSelect()
        }

        coneButton.setOnClickListener{

            optionSelect = 1

            //Update Avatar
            AvatarImageUpdater()
            ButtonUpdater()
            saveOptionSelect()
        }

        paperButton.setOnClickListener {
            optionSelect = 2
            // Update Avatar
            AvatarImageUpdater()
            ButtonUpdater()
            saveOptionSelect()
        }

        topHatButton.setOnClickListener {
            optionSelect = 3
            // Update Avatar
            AvatarImageUpdater()
            ButtonUpdater()
            saveOptionSelect()
        }

        crownButton.setOnClickListener {
            optionSelect = 4
            // Update Avatar
            AvatarImageUpdater()
            ButtonUpdater()
            saveOptionSelect()
        }

    }
    // Save optionSelect value to SharedPreferences
    private fun saveOptionSelect() {
        val editor = optionSelectSharedPreferences.edit()
        editor.putInt("optionSelect", optionSelect)
        editor.apply()
    }

    //Methods
    //Check if options have been unlocked
    fun unlockChecker(){

        //Retrieve information from SharedPref
        val challengesSharedPreferences =
            getSharedPreferences("ChallengesStatus", Context.MODE_PRIVATE)
        coneUnlocked = challengesSharedPreferences.getBoolean("isChallengeOneCompleted", false)
        paperUnlocked = challengesSharedPreferences.getBoolean("isChallengeTwoCompleted", false)
        topHatUnlocked = challengesSharedPreferences.getBoolean("isChallengeThreeCompleted", false)
        crownUnlocked = challengesSharedPreferences.getBoolean("isChallengeFourCompleted", false)


        //Set enabled to unlocked boolean value
        coneButton.isEnabled = coneUnlocked
        paperButton.isEnabled = paperUnlocked
        topHatButton.isEnabled = topHatUnlocked
        crownButton.isEnabled = crownUnlocked

        //Set image based on unlocked status
        coneButton.setImageResource(
            if (coneUnlocked) R.drawable.cone_button_unselected
            else R.drawable.cone_button_locked
        )

        paperButton.setImageResource(
            if (paperUnlocked) R.drawable.paper_button_unselected
            else R.drawable.paper_button_locked
        )

        topHatButton.setImageResource(
            if (topHatUnlocked) R.drawable.top_hat_button_unselected
            else R.drawable.top_hat_button_locked
        )

        crownButton.setImageResource(
            if (crownUnlocked) R.drawable.crown_button_unselected
            else R.drawable.crown_button_locked
        )

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

    // Method to update button images
    fun ButtonUpdater() {
        if (optionSelect == 0) {
            // None
            noneButton.setImageResource(R.drawable.none_button_selected)

            // If button has been unlocked then display the unselected image
            coneButton.setImageResource(
                if (coneUnlocked) R.drawable.cone_button_unselected
                else R.drawable.cone_button_locked)
            paperButton.setImageResource(
                if (paperUnlocked) R.drawable.paper_button_unselected
                else R.drawable.paper_button_locked)
            topHatButton.setImageResource(
                if (topHatUnlocked) R.drawable.top_hat_button_unselected
                else R.drawable.top_hat_button_locked)
            crownButton.setImageResource(
                if (crownUnlocked) R.drawable.crown_button_unselected
                else R.drawable.crown_button_locked)
        } else if (optionSelect == 1) {
            // Cone
            coneButton.setImageResource(R.drawable.cone_button_selected)

            noneButton.setImageResource(R.drawable.none_button_unselected)
            paperButton.setImageResource(
                if (paperUnlocked) R.drawable.paper_button_unselected
                else R.drawable.paper_button_locked)
            topHatButton.setImageResource(
                if (topHatUnlocked) R.drawable.top_hat_button_unselected
                else R.drawable.top_hat_button_locked)
            crownButton.setImageResource(
                if (crownUnlocked) R.drawable.crown_button_unselected
                else R.drawable.crown_button_locked)
        } else if (optionSelect == 2) {
            // Paper Hat
            paperButton.setImageResource(R.drawable.paper_button_selected)

            noneButton.setImageResource(R.drawable.none_button_unselected)
            coneButton.setImageResource(
                if (coneUnlocked) R.drawable.cone_button_unselected
                else R.drawable.cone_button_locked)
            topHatButton.setImageResource(
                if (topHatUnlocked) R.drawable.top_hat_button_unselected
                else R.drawable.top_hat_button_locked)
            crownButton.setImageResource(
                if (crownUnlocked) R.drawable.crown_button_unselected
                else R.drawable.crown_button_locked)
        } else if (optionSelect == 3) {
            // Top Hat
            topHatButton.setImageResource(R.drawable.top_hat_button_selected)

            noneButton.setImageResource(R.drawable.none_button_unselected)
            coneButton.setImageResource(
                if (coneUnlocked) R.drawable.cone_button_unselected
                else R.drawable.cone_button_locked)
            paperButton.setImageResource(
                if (paperUnlocked) R.drawable.paper_button_unselected
                else R.drawable.paper_button_locked)
            crownButton.setImageResource(
                if (crownUnlocked) R.drawable.crown_button_unselected
                else R.drawable.crown_button_locked)
        } else if (optionSelect == 4) {
            // Crown
            crownButton.setImageResource(R.drawable.crown_button_selected)

            noneButton.setImageResource(R.drawable.none_button_unselected)
            coneButton.setImageResource(
                if (coneUnlocked) R.drawable.cone_button_unselected
                else R.drawable.cone_button_locked)
            paperButton.setImageResource(
                if (paperUnlocked) R.drawable.paper_button_unselected
                else R.drawable.paper_button_locked)
            topHatButton.setImageResource(
                if (topHatUnlocked) R.drawable.top_hat_button_unselected
                else R.drawable.top_hat_button_locked)
        } else {
            // Default state
            noneButton.setImageResource(R.drawable.none_button_selected)

            coneButton.setImageResource(
                if (coneUnlocked) R.drawable.cone_button_unselected
                else R.drawable.cone_button_locked)
            paperButton.setImageResource(
                if (paperUnlocked) R.drawable.paper_button_unselected
                else R.drawable.paper_button_locked)
            topHatButton.setImageResource(
                if (topHatUnlocked) R.drawable.top_hat_button_unselected
                else R.drawable.top_hat_button_locked)
            crownButton.setImageResource(
                if (crownUnlocked) R.drawable.crown_button_unselected
                else R.drawable.crown_button_locked)
        }
    }
}