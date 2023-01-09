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
import com.example.mobilecomputingproject.adapters.ActiveBorrowRequestsAdapter
import com.example.mobilecomputingproject.databinding.FragmentActiveBorrowRequestsBinding
import com.example.mobilecomputingproject.models.BorrowRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import java.text.DateFormat
import java.util.*
import java.text.SimpleDateFormat

/**
 * Fragment for Active Borrow Requests
 */
class ActiveBorrowRequestsFragment : Fragment() {


    lateinit var adapter: ActiveBorrowRequestsAdapter

    private var _binding: FragmentActiveBorrowRequestsBinding? = null
    private val binding get() = _binding!!

    val database = FirebaseFirestore.getInstance()

    lateinit var simpleDateFormat: DateFormat

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentActiveBorrowRequestsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val borrowRequestsList = ArrayList<BorrowRequest>()

        val sharedPref = requireActivity().getSharedPreferences("Data", Context.MODE_PRIVATE)
        var userId: String? = sharedPref.getString("user_id", "1")

        binding.recyclerViewActiveBorrowRequests.layoutManager =
            LinearLayoutManager(requireContext())
        adapter = ActiveBorrowRequestsAdapter(borrowRequestsList, requireContext(), userId)
        binding.recyclerViewActiveBorrowRequests.adapter = adapter

        binding.noBorrowRequestText.setVisibility(View.GONE)
        binding.recyclerViewActiveBorrowRequests.setVisibility(View.VISIBLE)

        val context: Context = requireContext()

        updateStatus(userId, context)

        populateActiveBorrowRequest(userId, context)

        realtimeUpdates(userId, context)

        return root
    }


    private fun updateStatus(userId: String?, context: Context) {

        database.collection("laptop_booking")
            .whereEqualTo("user_id", "$userId")
            .whereEqualTo("status", "active")
            .get().addOnSuccessListener { result ->

                var currDate = Date()
                simpleDateFormat = SimpleDateFormat("dd-MM-yyyy")

                for (document in result) {

                    var expiryDate = document.data.getValue("expiryDate").toString()
                    var compExpiryDate = simpleDateFormat.parse(expiryDate)
                    var status = document.data.getValue("status").toString()

                    if (compExpiryDate.compareTo(currDate) <= 0 && status == "active") {
                        Log.d(TAG, "Expiry date is less")

                        var status_data = hashMapOf("status" to "expired")

                        database.collection("laptop_booking").document("${document.id}")
                            .set(status_data, SetOptions.merge())

                    }

                }

            }.addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }
    }

    private fun populateActiveBorrowRequest(userId: String?, context: Context) {
        val borrowRequestsList = ArrayList<BorrowRequest>()

        database.collection("laptop_booking")
            .whereEqualTo("user_id", "$userId")
            .whereIn("status", Arrays.asList("active", "accepted"))
            .get().addOnSuccessListener { result ->

                if (result.size() > 0) {
                    for (document in result) {

                        var status = document.data.getValue("status").toString()

                        //Log.d(TAG,"${document.id}")
                        val laptop_id: String = document.data.getValue("laptop_id").toString()
                        val date: String = document.data.getValue("pickup_date").toString()

                        Log.d(ContentValues.TAG, "$laptop_id")

                        database.collection("laptop_information").document(laptop_id).get()
                            .addOnSuccessListener { document ->
                                if (document != null) {
                                    Log.d(ContentValues.TAG, "${document.id}")
                                    Log.d(
                                        ContentValues.TAG,
                                        "DocumentSnapshot data: ${document.data}"
                                    )

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
                                                Log.d(ContentValues.TAG, "${document.id}")
                                                Log.d(
                                                    ContentValues.TAG,
                                                    "DocumentSnapshot data: ${document.data}"
                                                )

                                                val library: String =
                                                    document.data?.getValue("name").toString()

                                                var borrow_request: BorrowRequest = BorrowRequest(
                                                    "$userId",
                                                    "$configuration",
                                                    "$laptopType",
                                                    "$loanType",
                                                    "$library",
                                                    "$status",
                                                    "$date"
                                                )
                                                borrowRequestsList.add(borrow_request)

                                                binding.recyclerViewActiveBorrowRequests.layoutManager =
                                                    LinearLayoutManager(context)
                                                adapter = ActiveBorrowRequestsAdapter(
                                                    borrowRequestsList,
                                                    context,
                                                    userId
                                                )
                                                binding.recyclerViewActiveBorrowRequests.adapter =
                                                    adapter

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
                } else {
                    binding.noBorrowRequestText.setVisibility(View.VISIBLE)
                    binding.recyclerViewActiveBorrowRequests.setVisibility(View.GONE)
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }

    }

    private fun realtimeUpdates(userId: String?, context: Context) {

        database.collection("laptop_booking")
            .whereEqualTo("user_id", "$userId")
            .addSnapshotListener { value, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                    return@addSnapshotListener
                }


                populateActiveBorrowRequest(userId, context)


            }
    }

}