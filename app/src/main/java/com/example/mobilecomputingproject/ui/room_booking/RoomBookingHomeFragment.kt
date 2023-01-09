package com.example.mobilecomputingproject.ui.room_booking

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.navigation.fragment.findNavController
import com.example.mobilecomputingproject.R
import com.example.mobilecomputingproject.databinding.FragmentRoomBookingHomeBinding

class RoomBookingHomeFragment : Fragment() {

    private var _binding: FragmentRoomBookingHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRoomBookingHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bookroom: CardView = binding.cardBookRoom
        val previousbooking: CardView = binding.cardPreviousBooking
        val futureiousbooking: CardView = binding.cardFutureBookings

        previousbooking.setOnClickListener {
            findNavController().navigate(R.id.action_roomBookingHomeFragment_to_pastRoomBookingFragment)
        }
        futureiousbooking.setOnClickListener {
            findNavController().navigate(R.id.action_roomBookingHomeFragment_to_futureRoomBookingFragment)
        }
        bookroom.setOnClickListener {
            findNavController().navigate(R.id.action_roomBookingHomeFragment_to_searchRoomsFragment)
        }

    }
}