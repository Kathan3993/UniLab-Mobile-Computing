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
import com.example.mobilecomputingproject.adapters.InactiveBorrowRequestsAdapter
import com.example.mobilecomputingproject.databinding.FragmentInactiveBorrowRequestsBinding
import com.example.mobilecomputingproject.models.BorrowRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class InactiveBorrowRequestsFragment : Fragment() {

    lateinit var adapter: InactiveBorrowRequestsAdapter
    private var _binding: FragmentInactiveBorrowRequestsBinding? = null
    private val binding get() = _binding!!

    val database = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInactiveBorrowRequestsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref = requireActivity().getSharedPreferences("Data", Context.MODE_PRIVATE)
        var userId: String? = sharedPref.getString("user_id", "1")
        val context = requireContext()

        realtimeUpdates(userId, context)

        populateInactiveBorrowRequest(userId, context)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun populateInactiveBorrowRequest(userId: String?, context: Context){


        val sharedPref = requireActivity().getSharedPreferences("Data", Context.MODE_PRIVATE)
        var userId: String? = sharedPref.getString("user_id", "1")
        val borrowRequestsList = ArrayList<BorrowRequest>()

        database.collection("laptop_booking")
            .whereEqualTo("user_id","$userId")
            .get().addOnSuccessListener{result->

                if(result.size() > 0){

                    Log.d(ContentValues.TAG, "In line 59: ${ result.size() }")

                    for(document in result){

                        if(document.data.getValue("status") != "active" && document.data.getValue("status") != "accepted"){
                            val laptopId: String = document.data.getValue("laptop_id").toString()
                            val date: String = document.data.getValue("pickup_date").toString()
                            val status: String = document.data.getValue("status").toString()
                            var statusData = hashMapOf("is_booked" to false)
                            database.collection("laptop_information").document("$laptopId")
                                .set(statusData, SetOptions.merge())

                            database.collection("laptop_information").document(laptopId).get()
                                .addOnSuccessListener { document ->
                                    if (document != null) {
                                        val configuration: String = document.data?.getValue("laptop_model").toString() + " " + document.data?.getValue("ram")
                                            .toString() + " " + document.data?.getValue("storage").toString()

                                        val laptopType: String = document.data?.getValue("laptop_type").toString()
                                        val loanType: String = document.data?.getValue("lending_type").toString()
                                        val library_id: String = document.data?.getValue("library_id").toString()

                                        database.collection("library").document(library_id).get()
                                            .addOnSuccessListener { document ->
                                                if (document != null) {
                                                    val library: String = document.data?.getValue("name").toString()

                                                    var borrow_request = BorrowRequest("$userId", "$configuration", "$laptopType", "$loanType", "$library", "$status","$date")
                                                    borrowRequestsList.add(borrow_request)

                                                    binding.recyclerViewInactiveBorrowRequests.layoutManager = LinearLayoutManager(context)
                                                    adapter = InactiveBorrowRequestsAdapter(borrowRequestsList,context)
                                                    binding.recyclerViewInactiveBorrowRequests.adapter = adapter

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

                    }

                }else{
                    Log.d(ContentValues.TAG, "In empty inactive borrow requests in line 128")
                    binding.noBorrowRequestText.setVisibility(View.VISIBLE)
                    binding.recyclerViewInactiveBorrowRequests.setVisibility(View.GONE)
                }


            }
            .addOnFailureListener{exception->
                Log.w(ContentValues.TAG,"Error getting documents.",exception)
            }

    }

    private fun realtimeUpdates(userId: String?, context: Context){

        database.collection("laptop_booking")
            .whereEqualTo("user_id", "$userId")
            .addSnapshotListener { value, e ->
                if (e != null) {
                    Log.w(ContentValues.TAG, "Listen failed.", e)
                    return@addSnapshotListener
                }

                val borrowRequestsList = ArrayList<BorrowRequest>()

                binding.recyclerViewInactiveBorrowRequests.layoutManager = LinearLayoutManager(context)
                adapter = InactiveBorrowRequestsAdapter(borrowRequestsList,context)
                binding.recyclerViewInactiveBorrowRequests.adapter = adapter

                populateInactiveBorrowRequest(userId, context)

                Log.d(ContentValues.TAG, "laptop_booking has been updated.")
            }
    }



}