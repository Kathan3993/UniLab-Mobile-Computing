package com.example.mobilecomputingproject.ui.laptop_borrowing

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.mobilecomputingproject.R
import com.example.mobilecomputingproject.databinding.FragmentSearchAvailableLaptopsBinding
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Fragment for Searching Available Laptops
 */
class SearchAvailableLaptopsFragment : Fragment() {

    // Database instance
    val database = FirebaseFirestore.getInstance()

    // Binding for view binding
    private var _binding: FragmentSearchAvailableLaptopsBinding? = null
    private val binding get() = _binding!!

    // Entered Text
    var enteredLaptopType: String = ""
    var enteredModel: String = ""
    var enteredProcessor: String = ""
    var enteredStorage: String = ""
    var enteredLoanType: String = ""
    var enteredLibrary: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchAvailableLaptopsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Detect and navigate from user clicks on Cancel button
        binding.buttonCancel.setOnClickListener(View.OnClickListener {
            findNavController().navigate(R.id.action_searchAvailableLaptopsFragment_to_laptopBorrowingFragment)
        })

        // Detect the click on Search button
        binding.buttonSearch.setOnClickListener(View.OnClickListener {

            val bundle = bundleOf(
                "laptopType" to binding.AutoCompleteTextviewLaptopType.text.toString(),
                "model" to binding.AutoCompleteTextviewModel.text.toString(),
                "processor" to binding.AutoCompleteTextviewProcessor.text.toString(),
                "storage" to binding.AutoCompleteTextviewStorage.text.toString(),
                "loanType" to binding.AutoCompleteTextviewLoanType.text.toString(),
                "library" to binding.AutoCompleteTextviewLibrary.text.toString()
            )

            findNavController().navigate(
                R.id.action_searchAvailableLaptopsFragment_to_availableLaptopsFragment,
                bundle
            )
        })

        // Populate the efault options available from database
        defaultPopulateFilterOptions()

        binding.AutoCompleteTextviewLaptopType.setOnItemClickListener { adapterView, view, i, l ->
            enteredLaptopType = adapterView.getItemAtPosition(i) as String
            var selectedlaptopType: String = adapterView.getItemAtPosition(i) as String
            //Log.d(TAG, selectedText)
            populateAutoCompleteModel(selectedlaptopType)
            populateAutoCompleteProcessor(selectedlaptopType)
            populateAutoCompleteStorage(selectedlaptopType)
        }

        // Get the data entered by the users
        binding.AutoCompleteTextviewModel.setOnItemClickListener { adapterView, view, i, l ->
            enteredModel = adapterView.getItemAtPosition(i) as String
        }

        binding.AutoCompleteTextviewProcessor.setOnItemClickListener { adapterView, view, i, l ->
            enteredProcessor = adapterView.getItemAtPosition(i) as String
        }

        binding.AutoCompleteTextviewStorage.setOnItemClickListener { adapterView, view, i, l ->
            enteredStorage = adapterView.getItemAtPosition(i) as String
        }

        binding.AutoCompleteTextviewLoanType.setOnItemClickListener { adapterView, view, i, l ->
            enteredLoanType = adapterView.getItemAtPosition(i) as String
        }

        binding.AutoCompleteTextviewLibrary.setOnItemClickListener { adapterView, view, i, l ->
            enteredLibrary = adapterView.getItemAtPosition(i) as String
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Get the default data from the database
    private fun defaultPopulateFilterOptions() {

        // Laptop Type list
        val laptopTypeList = ArrayList<String>()

        database.collection("laptop_information")
            .whereEqualTo("is_booked", false)
            .get().addOnSuccessListener { result ->
                for (document in result) {
                    if (!laptopTypeList.contains(document.data.getValue("laptop_type"))) {
                        laptopTypeList.add("${document.data.getValue("laptop_type")}")
                    }
                }

                val adapterLaptopType = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_list_item_1,
                    laptopTypeList
                )
                binding.AutoCompleteTextviewLaptopType.setAdapter(adapterLaptopType)
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }

        // Model
        val laptopModelList = ArrayList<String>()

        database.collection("laptop_information")
            .whereEqualTo("is_booked", false)
            .get().addOnSuccessListener { result ->
                for (document in result) {
                    if (!laptopModelList.contains(document.data.getValue("laptop_model"))) {
                        laptopModelList.add("${document.data.getValue("laptop_model")}")
                    }
                }

                val adapterLaptopModel = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_list_item_1,
                    laptopModelList
                )
                binding.AutoCompleteTextviewModel.setAdapter(adapterLaptopModel)
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }


        // Processor
        val laptopProcessorList = ArrayList<String>()

        database.collection("laptop_information")
            .whereEqualTo("is_booked", false)
            .get().addOnSuccessListener { result ->
                for (document in result) {
                    if (!laptopProcessorList.contains(document.data.getValue("ram"))) {
                        laptopProcessorList.add("${document.data.getValue("ram")}")
                    }
                }

                val adapterProcessor = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_list_item_1,
                    laptopProcessorList
                )
                binding.AutoCompleteTextviewProcessor.setAdapter(adapterProcessor)
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }


        // Storage
        val laptopStorageList = ArrayList<String>()

        database.collection("laptop_information")
            .whereEqualTo("is_booked", false)
            .get().addOnSuccessListener { result ->
                for (document in result) {
                    if (!laptopStorageList.contains(document.data.getValue("storage"))) {
                        laptopStorageList.add("${document.data.getValue("storage")}")
                    }
                }

                val adapterStorage = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_list_item_1,
                    laptopStorageList
                )
                binding.AutoCompleteTextviewStorage.setAdapter(adapterStorage)
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }

        //Loan Type
        val laptopLoanTypeList = ArrayList<String>()

        database.collection("laptop_information")
            .whereEqualTo("is_booked", false)
            .get().addOnSuccessListener { result ->
                for (document in result) {
                    if (!laptopLoanTypeList.contains(document.data.getValue("lending_type"))) {
                        laptopLoanTypeList.add("${document.data.getValue("lending_type")}")
                    }
                }

                val adapterLaptopLoanType = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_list_item_1,
                    laptopLoanTypeList
                )
                binding.AutoCompleteTextviewLoanType.setAdapter(adapterLaptopLoanType)
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }

        // Library
        val laptopLibraryIdList = ArrayList<String>()
        val laptopLibraryList = ArrayList<String>()
        val libraryMap = mutableMapOf<String, String>()

        database.collection("laptop_information")
            .whereEqualTo("is_booked", false)
            .get().addOnSuccessListener { result ->
                for (document in result) {
                    if (!laptopLibraryIdList.contains(document.data.getValue("library_id"))) {
                        laptopLibraryIdList.add("${document.data.getValue("library_id")}")
                    }
                }

                database.collection("library")
                    .get().addOnSuccessListener { result ->
                        for (document in result) {
                            libraryMap["${document.id}"] = "${document.data.getValue("name")}"
                        }

                        for (item in laptopLibraryIdList) {
                            laptopLibraryList.add("${libraryMap.getValue(item)}")
                        }

                        val adapterLibrary = ArrayAdapter(
                            requireContext(),
                            android.R.layout.simple_list_item_1,
                            laptopLibraryList
                        )
                        binding.AutoCompleteTextviewLibrary.setAdapter(adapterLibrary)
                    }
                    .addOnFailureListener { exception ->
                        Log.w(ContentValues.TAG, "Error getting documents.", exception)
                    }
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }

    }

    // Populate the suggestions for the processor
    private fun populateAutoCompleteProcessor(selectedlaptopType: String) {

        val laptopProcessor = ArrayList<String>()

        database.collection("laptop_information")
            .whereEqualTo("is_booked", false)
            .get().addOnSuccessListener { result ->
                for (document in result) {
                    if (!laptopProcessor.contains(document.data.getValue("ram")) && document.data.getValue(
                            "laptop_type"
                        ) == selectedlaptopType
                    ) {
                        laptopProcessor.add("${document.data.getValue("ram")}")
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }

        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, laptopProcessor)
        binding.AutoCompleteTextviewProcessor.setAdapter(adapter)
    }

    // Populate the suggestions for the storage
    private fun populateAutoCompleteStorage(selectedlaptopType: String) {

        val laptopStorage = ArrayList<String>()

        database.collection("laptop_information")
            .whereEqualTo("is_booked", false)
            .get().addOnSuccessListener { result ->
                for (document in result) {
                    if (!laptopStorage.contains(document.data.getValue("storage")) && document.data.getValue(
                            "laptop_type"
                        ) == selectedlaptopType
                    ) {
                        laptopStorage.add("${document.data.getValue("storage")}")
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }

        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, laptopStorage)
        binding.AutoCompleteTextviewStorage.setAdapter(adapter)
    }

    // Populate the suggestions for the laptop model
    private fun populateAutoCompleteModel(selectedlaptopType: String) {

        val laptopModel = ArrayList<String>()

        database.collection("laptop_information")
            .whereEqualTo("is_booked", false)
            .get().addOnSuccessListener { result ->
                for (document in result) {
                    if (!laptopModel.contains(document.data.getValue("laptop_model")) && document.data.getValue(
                            "laptop_type"
                        ) == selectedlaptopType
                    ) {
                        laptopModel.add("${document.data.getValue("laptop_model")}")
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }

        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, laptopModel)
        binding.AutoCompleteTextviewModel.setAdapter(adapter)
    }
}