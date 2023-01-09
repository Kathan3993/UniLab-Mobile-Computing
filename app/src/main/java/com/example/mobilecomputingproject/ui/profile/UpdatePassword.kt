package com.example.mobilecomputingproject.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.mobilecomputingproject.databinding.FragmentUpdatePasswordBinding
import com.google.firebase.auth.FirebaseAuth


class UpdatePassword : Fragment() {
    private var _binding: FragmentUpdatePasswordBinding? = null
    private val binding get() = _binding!!
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var user = auth.currentUser
        _binding = FragmentUpdatePasswordBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val button: Button = binding.passup
        button.setOnClickListener {
            var password = binding.pass.text.toString()
            var confirm_password = binding.pass.text.toString()
            //Checking null password and confirmed password
            if (password.isEmpty() || confirm_password.isEmpty()) {
                Toast.makeText(activity, "Please enter all the fields", Toast.LENGTH_SHORT).show()
            } else if (password != confirm_password) {
                Toast.makeText(
                    activity,
                    "Password and confirm password didn't matched",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (password.length < 6) {
                Toast.makeText(activity, "Password length must be 6", Toast.LENGTH_SHORT).show()
            } else {
                //updating the user password
                user?.updatePassword(password)?.addOnSuccessListener {
                    Toast.makeText(activity, "Password updated successfully", Toast.LENGTH_SHORT)
                        .show()
                }?.addOnFailureListener {
                    Log.e("tag", it.toString())
                    Toast.makeText(
                        activity,
                        "Fail to update password please try again later",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
        }
        // Inflate the layout for this fragment
        return root
    }


}