package com.example.gogglebird


import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {

    //variables
    private lateinit var imageLogo : ImageView
    private lateinit var imageReturn : ImageView

    private lateinit var btnLogin : Button
    private lateinit var btnReg: Button

    private lateinit var tvWelcome : TextView
    private lateinit var tvLoginInfo : TextView
    private lateinit var tvRegInfo : TextView

    private lateinit var edEmail: EditText
    private lateinit var edPassword: EditText

    private lateinit var mAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //typecasting
        imageLogo = findViewById(R.id.imageViewLogo)
        imageReturn = findViewById(R.id.imageViewReturn)
        btnLogin = findViewById(R.id.btnLogin)
        btnReg = findViewById(R.id.btnRegScreen)
        tvWelcome = findViewById(R.id.txtWelcome)
        tvLoginInfo = findViewById(R.id.txtLoginInfo)
        tvRegInfo = findViewById(R.id.txtReg)
        edEmail = findViewById(R.id.editTextEmailAddress)
        edPassword = findViewById(R.id.editTextPassword)

        mAuth = FirebaseAuth.getInstance()

        btnLogin.setOnClickListener {
            loginUser()
        }

        //Redirect to Landing Page
        imageReturn.setOnClickListener {
            val intent = Intent(this, LandingPage::class.java)
            startActivity(intent)
        }

    } //end of onClick

    //Redirect to Reg Page
    fun regScreenOnClick(view: View) {
        val intentReg = Intent(this, Register::class.java)
        startActivity(intentReg)
    }

    //Login
    private fun loginUser() {
        //Error Handling -- Try Catch
        try {
            val email = edEmail.text.toString().trim()
            val password = edPassword.text.toString().trim()


            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this, "No email entered! Please enter an email address.", Toast.LENGTH_SHORT).show()
                edEmail.requestFocus()
                return
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(this, "No password entered! Please enter a password.", Toast.LENGTH_SHORT).show()
                edPassword.requestFocus()
                return
            }

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Store email in shared preference to be held in that instance
                    val sharedPreferences = getSharedPreferences("LoginEmail", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("email", email)
                    editor.apply()

                    Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()
                    // take user to next page
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                else
                {
                    Toast.makeText(this, "Uh Oh! Login Unsuccessful! Please try again.", Toast.LENGTH_SHORT).show()
                    //Clear fields
                    edEmail.setText("")
                    edPassword.setText("")
                    edEmail.requestFocus()
                }
            }

        } catch (eer: Exception) {
            //Display error
            Toast.makeText(this, "Error Occurred: " + eer.message, Toast.LENGTH_SHORT).show()
        }

    }// ends login

}