package com.example.gogglebird


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class LandingPage : AppCompatActivity() {
    private lateinit var imageLogo : ImageView
    private lateinit var btnLogin : Button
    private lateinit var btnRegister : Button
    private lateinit var tvWelcome : TextView
    private lateinit var tvDeveloperInfo : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing_page)

        //typecasting
        imageLogo = findViewById(R.id.imageView4)
        btnLogin = findViewById(R.id.buttonLogin)
        btnRegister = findViewById(R.id.buttonReg)
        tvWelcome = findViewById(R.id.textView)
        tvDeveloperInfo = findViewById(R.id.textView2)


    }

    //Redirect methods
    fun loginOnClick(view: View) {
        val intentLogin = Intent(this, Login::class.java)
        startActivity(intentLogin)
    }
    fun regOnClick(view: View) {
        val intentLogin = Intent(this, Register::class.java)
        startActivity(intentLogin)
    }
}