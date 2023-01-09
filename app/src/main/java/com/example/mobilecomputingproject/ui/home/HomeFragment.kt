package com.example.mobilecomputingproject.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mobilecomputingproject.databinding.FragmentHomeBinding
import androidx.navigation.fragment.findNavController
import com.example.mobilecomputingproject.R

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.cardLaptopBorrowing.setOnClickListener(View.OnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_laptopBorrowingFragment)
        })

        binding.cardLibrary.setOnClickListener(View.OnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_libraryListFragment)
        })

        binding.cardStudyRooms.setOnClickListener(View.OnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_roomBookingHomeFragment)
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}