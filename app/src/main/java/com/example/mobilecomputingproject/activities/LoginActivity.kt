package com.example.mobilecomputingproject.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.mobilecomputingproject.MainActivity
import com.example.mobilecomputingproject.databinding.ActivityForgotPasswordBinding
import com.example.mobilecomputingproject.databinding.ActivityLoginBinding
import com.example.mobilecomputingproject.databinding.ResetpasswordBinding
import com.google.firebase.auth.FirebaseAuth


/**
 * Activity to handle login & reset password
 */
class LoginActivity : AppCompatActivity() {
    private lateinit var loginBinding: ActivityLoginBinding
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var forgotPasswordBinding: ActivityForgotPasswordBinding
    private lateinit var resetPageLogin: Button
    private lateinit var resetPasswordBinding: ResetpasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Inflate the login layout
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)


        //Navigating user to registration  page
        loginBinding.reg.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        //Forgot password logic
        loginBinding.forgotPass.setOnClickListener {
            forgotPasswordBinding = ActivityForgotPasswordBinding.inflate(layoutInflater)
            setContentView(forgotPasswordBinding.root)
            forgotPasswordBinding.button2.setOnClickListener {
                val forgotPasswordEmail = forgotPasswordBinding.fpemail.text.toString()
                //Validating the null check
                if (forgotPasswordEmail.isEmpty()) {
                    Toast.makeText(this, "Please enter an email.", Toast.LENGTH_SHORT).show()
                } else {
                    auth.sendPasswordResetEmail(forgotPasswordEmail).addOnSuccessListener {
                        resetPasswordBinding = ResetpasswordBinding.inflate(layoutInflater)
                        setContentView(resetPasswordBinding.root)
                        resetPageLogin = resetPasswordBinding.LoginButton
                        resetPageLogin.setOnClickListener {
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                        }
                    }.addOnFailureListener {
                        Toast.makeText(
                            this,
                            "Couldn't find a registered account with this email",
                            Toast.LENGTH_LONG
                        ).show()
                    }


                }

            }
        }

        //User login
        loginBinding.btnSignIn.setOnClickListener {

            val email = loginBinding.etSignInEmail.text.toString()
            val password = loginBinding.etSignInPassword.text.toString()
            //Validating the null check
            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val verification = auth.currentUser?.isEmailVerified
                        println(verification)
                        if (verification == true) {
                            //OnSuccess navigating user to home screen page
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, "Please verify your email", Toast.LENGTH_SHORT)
                                .show()
                        }

                    }
                }.addOnFailureListener { exception ->
                    Toast.makeText(
                        this,
                        "Invalid login",
                        Toast.LENGTH_LONG
                    ).show()

                }
            } else {
                Toast.makeText(this, "Empty Fields are not Allowed !!", Toast.LENGTH_SHORT).show()
            }

        }
    }

}