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
import com.example.mobilecomputingproject.adapters.FutureRoomBookingsAdapter
import com.example.mobilecomputingproject.databinding.FragmentFutureRoomBookingBinding
import com.example.mobilecomputingproject.models.RoomBooking
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class FutureRoomBookingFragment : Fragment() {

    private var _binding: FragmentFutureRoomBookingBinding? = null
    private val binding get() = _binding!!
    var database = FirebaseFirestore.getInstance()

    lateinit var recyclerViewFutureRoomBookings: RecyclerView

    lateinit var adapter: FutureRoomBookingsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFutureRoomBookingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerViewFutureRoomBookings = binding.recyclerViewFutureRoomBookings

        // Set the layout to Linear Layout
        recyclerViewFutureRoomBookings.layoutManager =
            LinearLayoutManager(requireActivity().applicationContext)
        val futureBookingList = ArrayList<RoomBooking>()
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
                        Log.d("Date:", "${localDate}")
                        if (LocalDate.now() >= localDate || status == "Cancelled") {

                        } else {
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
                            futureBookingList.add(booking)
                        }
                    }
                }

                var fragmentManager: FragmentManager? = this.fragmentManager
                adapter =
                    FutureRoomBookingsAdapter(futureBookingList, requireContext(), fragmentManager)

                recyclerViewFutureRoomBookings.adapter = adapter
            }
    }
}