package com.example.mobilecomputingproject.ui.laptop_borrowing

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobilecomputingproject.adapters.LaptopsAdapter
import com.example.mobilecomputingproject.databinding.FragmentLaptopsBinding
import com.example.mobilecomputingproject.models.LaptopInformationItem
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

/**
 * Fragment for Laptops
 */
class LaptopsFragment : Fragment() {

    // Declare the adapter
    lateinit var adapter: LaptopsAdapter

    // Database instance
    val database = FirebaseFirestore.getInstance()

    // Binding for the view binding
    private var _binding: FragmentLaptopsBinding? = null
    private val binding get() = _binding!!

    // Initialize the variables for bundle arguments
    private var laptopType = ""
    private var model = ""
    private var processor = ""
    private var storage = ""
    private var loanType = ""
    private var library = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLaptopsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //defaultPopulateFilterOptions()

        // Get the bundle arguments
        if (arguments?.getString("laptopType") != null) {
            laptopType = arguments?.getString("laptopType")!!
        }

        if (arguments?.getString("model") != null) {
            model = arguments?.getString("model")!!
        }

        if (arguments?.getString("processor") != null) {
            processor = arguments?.getString("processor")!!
        }

        if (arguments?.getString("storage") != null) {
            storage = arguments?.getString("storage")!!
        }

        if (arguments?.getString("loanType") != null) {
            loanType = arguments?.getString("loanType")!!
        }

        if (arguments?.getString("loanType") != null) {
            loanType = arguments?.getString("loanType")!!
        }

        if (arguments?.getString("library") != null) {
            library = arguments?.getString("library")!!
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Populate the recycler view
        populateRecyclerView()
    }

    // Function to populate the recycler view
    private fun populateRecyclerView() {

        var laptopType: String? = laptopType
        var model: String? = model
        var processor: String? = processor
        var storage: String? = storage
        var loanType: String? = loanType
        var library: String? = library

        // Store the laptops
        val availableLaptopsList = ArrayList<LaptopInformationItem>()

        var library_id: String = ""

        // Check if library name is entered by the user
        if (library!!.length > 0) {

            database.collection("library")
                .whereEqualTo("name", "$library")
                .get().addOnSuccessListener { result ->
                    for (document in result) {
                        library_id = "${document.id}"
                        Log.d(ContentValues.TAG, library_id)

                        // Get the laptops from database based on filters
                        var databaseRef = database.collection("laptop_information") as Query

                        if (model!!.isNotEmpty()) {
                            databaseRef = databaseRef.whereEqualTo("laptop_model", model)
                        }

                        if (laptopType!!.isNotEmpty()) {
                            databaseRef = databaseRef.whereEqualTo("laptop_type", laptopType)
                        }

                        if (processor!!.isNotEmpty()) {
                            databaseRef = databaseRef.whereEqualTo("ram", processor)
                        }

                        if (storage!!.isNotEmpty()) {
                            databaseRef = databaseRef.whereEqualTo("storage", storage)
                        }

                        if (loanType!!.isNotEmpty()) {
                            databaseRef = databaseRef.whereEqualTo("lending_type", loanType)
                        }

                        databaseRef
                            .whereEqualTo("library_id", library_id)
                            .get().addOnSuccessListener { result ->
                                for (document in result) {
                                    Log.d(ContentValues.TAG, "${document.id} --> ${document.data}")

                                    var laptop: LaptopInformationItem = LaptopInformationItem(
                                        "${document.id}",
                                        "${document.data.getValue("laptop_type")}",
                                        "${document.data.getValue("ram")}",
                                        "${document.data.getValue("storage")}",
                                        "${document.data.getValue("lending_type")}",
                                        "${document.data.getValue("is_booked")}".toBoolean(),
                                        "${document.data.getValue("laptop_model")}",
                                        "${document.data.getValue("library_id")}",
                                        "$library"
                                    )
                                    availableLaptopsList.add(laptop)
                                    Log.d(ContentValues.TAG, "$availableLaptopsList")

                                    binding.recyclerViewLaptops.layoutManager =
                                        LinearLayoutManager(requireContext())
                                    adapter = LaptopsAdapter(availableLaptopsList, requireContext())
                                    binding.recyclerViewLaptops.adapter = adapter
                                }
                            }
                            .addOnFailureListener { exception ->
                                Log.w(ContentValues.TAG, "Error getting documents.", exception)
                            }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w(ContentValues.TAG, "Error getting documents.", exception)
                }
        } else {

            // Get the laptops from the database based on filters
            var databaseRef = database.collection("laptop_information") as Query

            if (model!!.isNotEmpty()) {
                Log.d(ContentValues.TAG, "Model is not empty")
                databaseRef = databaseRef.whereEqualTo("laptop_model", model)
            }

            if (laptopType!!.isNotEmpty()) {
                Log.d(ContentValues.TAG, "Laptop type is not empty")
                databaseRef = databaseRef.whereEqualTo("laptop_type", laptopType)
            }

            if (processor!!.isNotEmpty()) {
                Log.d(ContentValues.TAG, "Processor is not empty")
                databaseRef = databaseRef.whereEqualTo("ram", processor)
            }

            if (storage!!.isNotEmpty()) {
                Log.d(ContentValues.TAG, "Storage is not empty")
                databaseRef = databaseRef.whereEqualTo("storage", storage)
            }

            if (loanType!!.isNotEmpty()) {
                Log.d(ContentValues.TAG, "Loan type is not empty")
                databaseRef = databaseRef.whereEqualTo("lending_type", loanType)
            }

            databaseRef
                .get().addOnSuccessListener { result ->
                    for (document in result) {
                        Log.d(ContentValues.TAG, "${document.id} --> ${document.data}")

                        var laptop: LaptopInformationItem = LaptopInformationItem(
                            "${document.id}",
                            "${document.data.getValue("laptop_type")}",
                            "${document.data.getValue("ram")}",
                            "${document.data.getValue("storage")}",
                            "${document.data.getValue("lending_type")}",
                            "${document.data.getValue("is_booked")}".toBoolean(),
                            "${document.data.getValue("laptop_model")}",
                            "${document.data.getValue("library_id")}",
                            "$library"
                        )
                        availableLaptopsList.add(laptop)
                        Log.d(ContentValues.TAG, "$availableLaptopsList")

                        binding.recyclerViewLaptops.layoutManager =
                            LinearLayoutManager(requireContext())
                        adapter = LaptopsAdapter(availableLaptopsList, requireContext())
                        binding.recyclerViewLaptops.adapter = adapter
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w(ContentValues.TAG, "Error getting documents.", exception)
                }
        }
    }
}