package com.example.mobilecomputingproject


import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mobilecomputingproject.activities.LoginActivity
import com.example.mobilecomputingproject.activities.MainActivityNavigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class MainActivity : AppCompatActivity() {
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (auth.getCurrentUser() != null) {
//           Setting shared preferences for the session management
            val uid = auth.currentUser?.uid.toString()
            val sharedPref = this?.getSharedPreferences("Data", Context.MODE_PRIVATE) ?: return
            val intent = Intent(this, MainActivityNavigation::class.java)
            val email = sharedPref.getString("email", "1")
            if (email == "1") {
                with(sharedPref.edit()) {
                    db.collection("User").document(uid).get().addOnSuccessListener { tasks ->
                        putString("user_id", auth.currentUser?.uid.toString())
                        putString("email", tasks.get("email").toString())
                        putString("firstname", tasks.get("firstname").toString())
                        putString("lastname", tasks.get("lastname").toString())
                        apply()
                    }
                }
            }
            startActivity(intent)
            finish()
        } else {
            //Return to login if user is not logged in
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}
