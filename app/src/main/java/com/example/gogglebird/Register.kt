package com.example.gogglebird

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class Register : AppCompatActivity() {

    //variables
    private lateinit var imageLogo: ImageView
    private lateinit var imageReturn: ImageView

    private lateinit var btnReg: Button
    private lateinit var btnLoginScreen: Button

    private lateinit var tvWelcome: TextView
    private lateinit var tvRegInfo: TextView
    private lateinit var tvLoginScreenInfo: TextView

    private lateinit var edEmail: EditText
    private lateinit var edPassword: EditText
    private lateinit var edConfirmPassword: EditText

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        //typecasting
        imageLogo = findViewById(R.id.imageViewLogo)
        imageReturn = findViewById(R.id.imageViewReturn)

        btnReg = findViewById(R.id.btnRegister)
        btnLoginScreen = findViewById(R.id.btnLoginScreen)

        tvWelcome = findViewById(R.id.txtWelcome)
        tvRegInfo = findViewById(R.id.txtRegInfo)
        tvLoginScreenInfo = findViewById(R.id.txtLoginScreen)

        edEmail = findViewById(R.id.editTextEmailAddress)
        edPassword = findViewById(R.id.editTextPassword)
        edConfirmPassword = findViewById(R.id.editTextConfirmPassword)

        mAuth = FirebaseAuth.getInstance()

        //Redirect to Landing Page -- ? --> null check
        imageReturn.setOnClickListener {
            val intent = Intent(this, LandingPage::class.java)
            startActivity(intent)
        }

    }//end onCreate


    //Redirect to Reg Page
    fun loginScreenOnClick(view: View) {
        val intentLogin = Intent(this, Login::class.java)
        startActivity(intentLogin)
    }

    // reg btn method
    fun regUserOnClick(view: View) {

        if (view.id == R.id.btnRegister) {
            val email = edEmail.text.toString().trim()
            val passwordReg = edPassword.text.toString().trim()
            val conPassword = edConfirmPassword.text.toString().trim()

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(
                    this,
                    "Email can't be blank! Please enter an email",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }

            if (TextUtils.isEmpty(passwordReg)) {
                Toast.makeText(
                    this,
                    "Password can't be blank! Please enter a password",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }

            if (TextUtils.isEmpty(conPassword)) {
                Toast.makeText(
                    this,
                    "Confirm password can't be blank! Please enter a password confirmation ",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }

            if (conPassword == passwordReg) {
                mAuth.createUserWithEmailAndPassword(email, passwordReg)
                    .addOnCompleteListener(this, OnCompleteListener<AuthResult> { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                this@Register,
                                " Registration Successful! You may Login",
                                Toast.LENGTH_SHORT
                            ).show()
                            val intent = Intent(this@Register, Login::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(
                                this@Register,
                                " ERROR: FAILED TO REGISTER",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
            } else {
                Toast.makeText(
                    this,
                    "Passwords do not match! Please enter the same password. ",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }
        }
    }
}
