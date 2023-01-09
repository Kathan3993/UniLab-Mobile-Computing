package com.example.mobilecomputingproject.ui.library

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobilecomputingproject.R
import com.example.mobilecomputingproject.adapters.AvailableLaptopsAdapter
import com.example.mobilecomputingproject.adapters.LibraryEventAdapter
import com.example.mobilecomputingproject.databinding.FragmentHomeBinding
import com.example.mobilecomputingproject.databinding.FragmentLibraryFeedBinding
import com.example.mobilecomputingproject.databinding.FragmentQRCodeBinding
import com.example.mobilecomputingproject.models.LaptopInformationItem
import com.example.mobilecomputingproject.models.LibraryEvent
import com.google.firebase.firestore.*

class LibraryFeedFragment : Fragment() {

    private var _binding: FragmentLibraryFeedBinding? = null
    private val binding get() = _binding!!

    lateinit var adapter: LibraryEventAdapter

    var id: String?=null
    val database = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLibraryFeedBinding.inflate(inflater, container, false)
        val root: View = binding.root

        id=requireArguments().getString("libraryId")

        populateLibraryEvents()

        return root
    }



    private fun populateLibraryEvents(){

        val eventList = ArrayList<LibraryEvent>()

        database.collection("LibraryEventInformation")
            .whereEqualTo("libraryID","$id")
            .get().addOnSuccessListener { result ->
                for (document in result) {
                    val eventTitle: String?= document.getString("eventTitle")
                    val eventDescription: String? = document.getString("eventDescription")
                    val libraryID: String?= document.getString("libraryID")
                    val targetedAudience: String ?= document.getString("targetedAudience")
                    val timestamp : com.google.firebase.Timestamp? = document.getTimestamp("timestamp")

                    eventList.add(LibraryEvent(eventDescription,eventTitle,libraryID,targetedAudience,timestamp))
                }

                binding.libraryEventRVID.layoutManager = LinearLayoutManager(requireContext())
                adapter = LibraryEventAdapter(eventList, requireContext())
                binding.libraryEventRVID.adapter = adapter
            }

    }


}