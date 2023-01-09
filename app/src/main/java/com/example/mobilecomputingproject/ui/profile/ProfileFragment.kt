package com.example.mobilecomputingproject.ui.profile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mobilecomputingproject.activities.LoginActivity
import com.example.mobilecomputingproject.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth

/* Profile screen fragment
* */
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        //setting the name by taking it from shared preferences
        val sharedPref = requireActivity().getSharedPreferences("Data", Context.MODE_PRIVATE)
        binding.lastName.text = sharedPref.getString("lastname", "1")
        binding.firstName.text = sharedPref.getString("firstname", "xyz")
        binding.edtEmailProfile.text = sharedPref.getString("email", "abc")
        binding.btnChangepass.setOnClickListener {
            findNavController().navigate(com.example.mobilecomputingproject.R.id.action_navigation_dashboard_to_updatePassword)

        }
        //onLogout clear session and navigate to login screen
        binding.btnLogout.setOnClickListener {
            val preferences: SharedPreferences =
                requireActivity().getSharedPreferences("Data", Context.MODE_PRIVATE)
            val editor = preferences.edit()
            editor.clear()
            editor.apply()
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
        return root
    }


}