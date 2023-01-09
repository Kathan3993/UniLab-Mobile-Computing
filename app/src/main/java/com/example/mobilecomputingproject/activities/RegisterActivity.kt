package com.example.mobilecomputingproject.activities

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mobilecomputingproject.databinding.ActivityRegisterBinding
import com.example.mobilecomputingproject.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

import java.util.*

/**
 * Activity to handle user registration
 */
class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var userEmail: String
    private lateinit var userFname: String
    private lateinit var userLname: String
    private lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Navigating user to login page
        binding.login.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        //Validating all the fields before creating the account
        binding.btnCreateAccount.setOnClickListener {
            dataValidation()
        }
    }

    //Saving the user data into database
    private fun saveUserData() {
        val user = User(userFname, userLname, userEmail, timestamp = Date())
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        db.collection("User").document(userId).set(user)
    }

    //Data validation
    private fun dataValidation() {
        userEmail = binding.Email.text.toString()
        userFname = binding.Fname.text.toString()
        userLname = binding.Lname.text.toString()
        password = binding.Password.text.toString()

        if (userFname.length < 3 || userFname.isEmpty()) {
            binding.Fname.error = "More than 3 letters required"
        } else if (userLname.isEmpty()) {
            binding.Lname.error = "This field cannot be empty"
        } else if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches() || userEmail.isEmpty()) {
            binding.Email.error = "Invalid email format"
        } else if (password.length < 8) {
            binding.Password.error = "Password length should be atleast 6 characters"
        } else {
            //If all checks passed it will register the user
            firebaseRegister()
        }
    }

    //User firebase registration logic
    private fun firebaseRegister() {
        auth.createUserWithEmailAndPassword(userEmail, password).addOnSuccessListener { task ->
            saveUserData()
            auth.currentUser?.sendEmailVerification()?.addOnSuccessListener {
                Toast.makeText(
                    this,
                    "A verification email have been sent to your account",
                    Toast.LENGTH_SHORT
                ).show()
            }?.addOnFailureListener {
                Toast.makeText(this, "Sending verification email failed", Toast.LENGTH_SHORT).show()
            }
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()

        }.addOnFailureListener { exception ->
            Toast.makeText(this, "User Registration failed", Toast.LENGTH_SHORT).show()
        }

    }

}











