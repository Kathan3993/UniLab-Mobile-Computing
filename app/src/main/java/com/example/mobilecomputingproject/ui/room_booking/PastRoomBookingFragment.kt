package com.example.mobilecomputingproject.ui.room_booking

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobilecomputingproject.adapters.PastRoomBookingsAdapter
import com.example.mobilecomputingproject.databinding.FragmentPastRoomBookingBinding
import com.example.mobilecomputingproject.models.RoomBooking
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class PastRoomBookingFragment : Fragment() {

    private var _binding: FragmentPastRoomBookingBinding? = null
    private val binding get() = _binding!!

    var database = FirebaseFirestore.getInstance()

    lateinit var recyclerViewPastRoomBookings: RecyclerView

    lateinit var adapter: PastRoomBookingsAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPastRoomBookingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerViewPastRoomBookings = binding.recyclerViewPastRoomBookings

        // Set the layout to Linear Layout
        recyclerViewPastRoomBookings.layoutManager =
            LinearLayoutManager(requireActivity().applicationContext)

        val pastBookingList = ArrayList<RoomBooking>()

        val sharedPref = requireActivity().getSharedPreferences("Data", Context.MODE_PRIVATE)
        val userID = sharedPref.getString("email", "1")
        database.collection("RoomBooking").whereEqualTo("userId", userID)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if (document.data.get("checkedIn") != null) {
                        var roomNumber = document.data.get("room").toString()
                        var userId = document.data.get("firstName")
                            .toString() + " " + document.data.get("lastName").toString()
                        var purpose = document.data.get("purpose").toString()
                        var bookingSlot = document.data.get("slot").toString()
                        var date = document.data.get("date").toString()
                        var timestamp = document.data.get("timestamp").toString()
                        var telephone = document.data.get("telephone").toString()
                        var qrcode = document.data.get("qrcode").toString()
                        var status = document.data.get("status").toString()
                        var listOfParticipants = document.data.get("listOfParticipants").toString()
                        var participants = document.data.get("participants").toString()
                        var checkedIn = document.data.get("checkedIn") as Boolean
                        var email = document.data.get("userId").toString()
                        var library = document.data.get("library").toString()
                        val localDate =
                            LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy"))

                        if (LocalDate.now() >= localDate || status == "Cancelled") {
                            var booking: RoomBooking = RoomBooking(
                                roomNumber,
                                userId,
                                purpose,
                                bookingSlot,
                                date,
                                timestamp,
                                telephone,
                                qrcode,
                                status,
                                listOfParticipants,
                                participants,
                                checkedIn,
                                email,
                                library
                            )
                            pastBookingList.add(booking)
                        } else {

                        }
                    }
                }

                var fragmentManager: FragmentManager? = this.fragmentManager
                adapter = PastRoomBookingsAdapter(
                    pastBookingList,
                    requireActivity().applicationContext,
                    fragmentManager
                )

                recyclerViewPastRoomBookings.adapter = adapter
            }


    }


}