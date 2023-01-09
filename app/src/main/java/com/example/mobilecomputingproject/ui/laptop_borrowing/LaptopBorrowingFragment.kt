package com.example.mobilecomputingproject.ui.laptop_borrowing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mobilecomputingproject.databinding.FragmentLaptopBorrowingBinding
import com.example.mobilecomputingproject.R

/**
 * Fragment for Laptop Borrowing
 */
class LaptopBorrowingFragment : Fragment() {

    // Binding for the view binding
    private var _binding: FragmentLaptopBorrowingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLaptopBorrowingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Detect click on borrow laptop card
        binding.cardBorrowLaptop.setOnClickListener(View.OnClickListener {
            findNavController().navigate(R.id.action_laptopBorrowingFragment_to_searchAvailableLaptopsFragment)
        })

        // Detect click on search laptops card
        binding.cardSearchLaptops.setOnClickListener(View.OnClickListener {
            findNavController().navigate(R.id.action_laptopBorrowingFragment_to_searchLaptopsFragment)
        })

        // Detect click on borrow requests card
        binding.cardBorrowRequests.setOnClickListener(View.OnClickListener {
            findNavController().navigate(R.id.action_laptopBorrowingFragment_to_borrowRequestsFragment)
        })

        // Detect click on devices card
        binding.cardDevices.setOnClickListener(View.OnClickListener {
            findNavController().navigate(R.id.action_laptopBorrowingFragment_to_devicesFragment)
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
