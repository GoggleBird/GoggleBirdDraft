package com.example.gogglebird


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class Splash : AppCompatActivity() {
    //Variables
    //Delay splash in milliseconds (for 2 seconds)
    private val splashDelay: Long = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        //Typecasting
        // Delay for the specified time and then start the main activity
        Handler().postDelayed({
            val intent = Intent(this@Splash, LandingPage::class.java)
            startActivity(intent)
            finish()
        }, splashDelay)
    }
}