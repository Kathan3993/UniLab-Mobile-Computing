package com.example.mobilecomputingproject.ui.laptop_borrowing

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobilecomputingproject.adapters.CurrentDevicesAdapter
import com.example.mobilecomputingproject.databinding.FragmentCurrentDevicesBinding
import com.example.mobilecomputingproject.models.CurrentDevices
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

/**
 * Fragment for Current Devices
 */
class CurrentDevicesFragment : Fragment() {

    // Adapter for current devices
    lateinit var adapter: CurrentDevicesAdapter

    // Binding for the view binding
    private var _binding: FragmentCurrentDevicesBinding? = null
    private val binding get() = _binding!!

    // Database instance
    val database = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCurrentDevicesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val currentDevicesList = ArrayList<CurrentDevices>()

        // Set the adapter for the view
        binding.recyclerViewCurrentDevices.layoutManager = LinearLayoutManager(requireContext())
        adapter = CurrentDevicesAdapter(currentDevicesList, requireContext())
        binding.recyclerViewCurrentDevices.adapter = adapter

        binding.noCurrentDevicesText.setVisibility(View.GONE)
        binding.recyclerViewCurrentDevices.setVisibility(View.VISIBLE)

        // Get the user data from Shared Preferences
        val sharedPref = requireActivity().getSharedPreferences("Data", Context.MODE_PRIVATE)
        var user_id: String? = sharedPref.getString("user_id", "1")

        val context = requireContext()

        // Detect the realtime updates to laptop booking collection
        realtimeUpdatesLaptopBooking(user_id, context)

        // Populate the recycler view
        populateCurrentDevices(user_id, context)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //realtimeUpdates()
    }

    /*
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    */

    // Populate the current devices
    private fun populateCurrentDevices(userId: String?, context: Context){

        //val sharedPref = requireActivity().getSharedPreferences("Data", Context.MODE_PRIVATE)
        //var user_id: String? = sharedPref.getString("user_id", "1")
        val currentDevicesList = ArrayList<CurrentDevices>()

        // Get the user current devices
        database.collection("user_devices")
            .whereEqualTo("user_id","$userId")
            .whereEqualTo("status","active")
            .get().addOnSuccessListener{result->

                // Check if user currently has devices
                if(result.size() > 0){
                    for(document in result){

                        val laptop_id: String = document.data.getValue("laptop_id").toString()
                        val pickUpDate: String = document.data.getValue("pickup_date").toString()
                        val dropOffDate: String = document.data.getValue("drop_off_date").toString()

                        Log.d(TAG, "$laptop_id")

                        database.collection("laptop_information").document(laptop_id).get()
                            .addOnSuccessListener { document ->
                                if (document != null) {
                                    //Log.d(ContentValues.TAG, "${document.id}")
                                    //Log.d(ContentValues.TAG, "DocumentSnapshot data: ${document.data}")

                                    val configuration: String = document.data?.getValue("laptop_model").toString() + " " + document.data?.getValue("ram")
                                        .toString() + " " + document.data?.getValue("storage").toString()

                                    val laptopType: String = document.data?.getValue("laptop_type").toString()
                                    val loanType: String = document.data?.getValue("lending_type").toString()
                                    val library_id: String = document.data?.getValue("library_id").toString()

                                    //Log.d(ContentValues.TAG,"$laptopType")
                                    //Log.d(ContentValues.TAG,"$library_id")

                                    database.collection("library").document(library_id).get()
                                        .addOnSuccessListener { document ->
                                            if (document != null) {
                                                //Log.d(ContentValues.TAG, "${document.id}")
                                                //Log.d(ContentValues.TAG, "DocumentSnapshot data: ${document.data}")

                                                val library: String = document.data?.getValue("name").toString()

                                                var device : CurrentDevices = CurrentDevices("$configuration","$laptopType","$loanType","$library","$pickUpDate","$dropOffDate")
                                                currentDevicesList.add(device)

                                                binding.recyclerViewCurrentDevices.layoutManager = LinearLayoutManager(context)
                                                adapter = CurrentDevicesAdapter(currentDevicesList, context)
                                                binding.recyclerViewCurrentDevices.adapter = adapter

                                            } else {
                                                Log.d(ContentValues.TAG, "No such document")
                                            }
                                        }
                                        .addOnFailureListener { exception ->
                                            Log.d(ContentValues.TAG, "get failed with ", exception)
                                        }


                                } else {
                                    Log.d(ContentValues.TAG, "No such document")
                                }
                            }
                            .addOnFailureListener { exception ->
                                Log.d(ContentValues.TAG, "get failed with ", exception)
                            }
                    }
                }else{
                    // Set the text that user currently has no devices
                    binding.noCurrentDevicesText.setVisibility(View.VISIBLE)
                    binding.recyclerViewCurrentDevices.setVisibility(View.GONE)

                }
            }
            .addOnFailureListener{exception->
                Log.w(ContentValues.TAG,"Error getting documents.",exception)
            }
    }

    /*
    private fun realtimeUpdates(){

        val user_id: String = "1"

        database.collection("user_devices")
            .whereEqualTo("user_id", "$user_id")
            .addSnapshotListener { value, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                    return@addSnapshotListener
                }

                val currentDevicesList = ArrayList<CurrentDevices>()

                binding.recyclerViewCurrentDevices.layoutManager = LinearLayoutManager(requireContext())
                adapter = CurrentDevicesAdapter(currentDevicesList, requireContext())
                binding.recyclerViewCurrentDevices.adapter = adapter

                binding.noCurrentDevicesText.setVisibility(View.GONE)
                binding.recyclerViewCurrentDevices.setVisibility(View.VISIBLE)

                populateCurrentDevices()

                Log.d(TAG, "User devices has been updated.")
            }
    }
    */

    // Listen for realtime updates for laptop booking collection and update current devices view
    private fun realtimeUpdatesLaptopBooking(userId: String?, context: Context){

        //val sharedPref = requireActivity().getSharedPreferences("Data", Context.MODE_PRIVATE)
        //var user_id: String? = sharedPref.getString("user_id", "1")

        database.collection("laptop_booking")
            .whereEqualTo("user_id", "$userId")
            .addSnapshotListener { value, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                    return@addSnapshotListener
                }

                for (dc in value!!.documentChanges) {

                    when (dc.type) {

                        DocumentChange.Type.MODIFIED -> {

                            val status: String = dc.document.data.getValue("status").toString()
                            Log.d(TAG, "Status Realtime Updates Lapptop Booking: $status")

                            if(status != "active" && status != "accepted"){

                                database.collection("user_devices")
                                    .whereEqualTo("user_id","$userId")
                                    .whereEqualTo("status","active")
                                    .get().addOnSuccessListener{result->

                                        for(document in result){

                                            var user_devices_data = hashMapOf("status" to "inactive")

                                            Log.d(TAG, "Current Devices document id: ${document.id}")

                                            database.collection("user_devices").document("${document.id}")
                                                .set(user_devices_data, SetOptions.merge())

                                            populateCurrentDevices(userId,context)

                                        }

                                    }.addOnFailureListener{
                                            e->Log.w(TAG,"Error adding document",e)
                                    }
                            }
                        }
                        DocumentChange.Type.ADDED -> {

                        }
                        DocumentChange.Type.REMOVED -> {

                        }
                    }

                }

                Log.d(TAG, "User devices has been updated.")
            }

    }

}