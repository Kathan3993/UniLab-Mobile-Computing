package com.example.mobilecomputingproject.ui.laptop_borrowing

import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobilecomputingproject.adapters.PastDevicesAdapter
import com.example.mobilecomputingproject.databinding.FragmentPastDevicesBinding
import com.example.mobilecomputingproject.models.PastDevices
import com.google.firebase.firestore.FirebaseFirestore

class PastDevicesFragment : Fragment() {


    lateinit var adapter: PastDevicesAdapter

    private var _binding: FragmentPastDevicesBinding? = null
    private val binding get() = _binding!!

    val database = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPastDevicesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref = requireActivity().getSharedPreferences("Data", Context.MODE_PRIVATE)
        var userId: String? = sharedPref.getString("user_id", "1")

        val context = requireContext()

        realtimeUpdates(userId, context)

        populatePastDevices(userId, context)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun populatePastDevices(userId: String?, context: Context) {
        val pastDevicesList = ArrayList<PastDevices>()

        database.collection("user_devices")
            .whereEqualTo("user_id", "$userId")
            .get().addOnSuccessListener { result ->

                if (result.size() > 0) {
                    for (document in result) {

                        val status: String = document.data.getValue("status").toString()

                        if (status != "active") {
                            val laptop_id: String = document.data.getValue("laptop_id").toString()
                            val pickUpDate: String =
                                document.data.getValue("pickup_date").toString()
                            val dropOffDate: String =
                                document.data.getValue("drop_off_date").toString()

                            Log.d(ContentValues.TAG, "$laptop_id")

                            database.collection("laptop_information").document(laptop_id).get()
                                .addOnSuccessListener { document ->
                                    if (document != null) {

                                        val configuration: String =
                                            document.data?.getValue("laptop_model")
                                                .toString() + " " + document.data?.getValue("ram")
                                                .toString() + " " + document.data?.getValue("storage")
                                                .toString()

                                        val laptopType: String =
                                            document.data?.getValue("laptop_type").toString()
                                        val loanType: String =
                                            document.data?.getValue("lending_type").toString()
                                        val library_id: String =
                                            document.data?.getValue("library_id").toString()

                                        database.collection("library").document(library_id).get()
                                            .addOnSuccessListener { document ->
                                                if (document != null) {
                                                    //Log.d(ContentValues.TAG, "${document.id}")
                                                    //Log.d(ContentValues.TAG, "DocumentSnapshot data: ${document.data}")

                                                    val library: String =
                                                        document.data?.getValue("name").toString()

                                                    var device: PastDevices = PastDevices(
                                                        "$configuration",
                                                        "$laptopType",
                                                        "$loanType",
                                                        "$library",
                                                        "$pickUpDate",
                                                        "$dropOffDate"
                                                    )
                                                    pastDevicesList.add(device)

                                                    binding.recyclerViewPastDevices.layoutManager =
                                                        LinearLayoutManager(context)
                                                    adapter =
                                                        PastDevicesAdapter(pastDevicesList, context)
                                                    binding.recyclerViewPastDevices.adapter =
                                                        adapter

                                                } else {
                                                    Log.d(ContentValues.TAG, "No such document")
                                                }
                                            }
                                            .addOnFailureListener { exception ->
                                                Log.d(
                                                    ContentValues.TAG,
                                                    "get failed with ",
                                                    exception
                                                )
                                            }


                                    } else {
                                        Log.d(ContentValues.TAG, "No such document")
                                    }
                                }
                                .addOnFailureListener { exception ->
                                    Log.d(ContentValues.TAG, "get failed with ", exception)
                                }
                        }
                    }
                } else {

                    binding.noPastDevicesText.setVisibility(View.VISIBLE)
                    binding.recyclerViewPastDevices.setVisibility(View.GONE)

                }


            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }
    }

    private fun realtimeUpdates(userId: String?, context: Context) {
        database.collection("user_devices")
            .whereEqualTo("user_id", "$userId")
            .addSnapshotListener { value, e ->
                if (e != null) {
                    Log.w(ContentValues.TAG, "Listen failed.", e)
                    return@addSnapshotListener
                }

                val pastDevicesList = ArrayList<PastDevices>()

                binding.recyclerViewPastDevices.layoutManager = LinearLayoutManager(context)
                adapter = PastDevicesAdapter(pastDevicesList, context)
                binding.recyclerViewPastDevices.adapter = adapter

                populatePastDevices(userId, context)

                Log.d(ContentValues.TAG, "User devices has been updated.")
            }
    }

}