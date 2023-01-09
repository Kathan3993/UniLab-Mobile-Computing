package com.example.mobilecomputingproject.ui.laptop_borrowing

import android.R
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
import com.example.mobilecomputingproject.databinding.FragmentSearchLaptopsBinding
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Fragment for Search Laptops
 */
class SearchLaptopsFragment : Fragment() {

    // Database instance
    val database = FirebaseFirestore.getInstance()

    // Binding for view binding
    private var _binding: FragmentSearchLaptopsBinding? = null
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
        _binding = FragmentSearchLaptopsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Detect the click on cancel button
        binding.buttonCancel.setOnClickListener(View.OnClickListener {
            findNavController().navigate(com.example.mobilecomputingproject.R.id.action_searchLaptopsFragment_to_laptopBorrowingFragment)
        })

        // Detect the click on search button
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
                com.example.mobilecomputingproject.R.id.action_searchLaptopsFragment_to_laptopsFragment,
                bundle
            )
        })

        // Populate the default filter options
        defaultPopulateFilterOptions()

        // Detect and capture data when user clicks on filters
        binding.AutoCompleteTextviewLaptopType.setOnItemClickListener { adapterView, view, i, l ->
            enteredLaptopType = adapterView.getItemAtPosition(i) as String
        }

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

    // Populate the default suggestions/ filter options from database
    private fun defaultPopulateFilterOptions() {

        // Laptop Type
        val laptopTypeList = ArrayList<String>()

        database.collection("laptop_information")
            .get().addOnSuccessListener { result ->
                for (document in result) {
                    if (!laptopTypeList.contains(document.data.getValue("laptop_type"))) {
                        laptopTypeList.add("${document.data.getValue("laptop_type")}")
                    }
                }

                val adapterLaptopType =
                    ArrayAdapter(requireContext(), R.layout.simple_list_item_1, laptopTypeList)
                binding.AutoCompleteTextviewLaptopType.setAdapter(adapterLaptopType)
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }

        // Model
        val laptopModelList = ArrayList<String>()

        database.collection("laptop_information")
            .get().addOnSuccessListener { result ->
                for (document in result) {
                    if (!laptopModelList.contains(document.data.getValue("laptop_model"))) {
                        laptopModelList.add("${document.data.getValue("laptop_model")}")
                    }
                }

                val adapterLaptopModel =
                    ArrayAdapter(requireContext(), R.layout.simple_list_item_1, laptopModelList)
                binding.AutoCompleteTextviewModel.setAdapter(adapterLaptopModel)
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }


        // Processor
        val laptopProcessorList = ArrayList<String>()

        database.collection("laptop_information")
            .get().addOnSuccessListener { result ->
                for (document in result) {
                    if (!laptopProcessorList.contains(document.data.getValue("ram"))) {
                        laptopProcessorList.add("${document.data.getValue("ram")}")
                    }
                }

                val adapterProcessor =
                    ArrayAdapter(requireContext(), R.layout.simple_list_item_1, laptopProcessorList)
                binding.AutoCompleteTextviewProcessor.setAdapter(adapterProcessor)
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }


        // Storage
        val laptopStorageList = ArrayList<String>()

        database.collection("laptop_information")
            .get().addOnSuccessListener { result ->
                for (document in result) {
                    if (!laptopStorageList.contains(document.data.getValue("storage"))) {
                        laptopStorageList.add("${document.data.getValue("storage")}")
                    }
                }

                val adapterStorage =
                    ArrayAdapter(requireContext(), R.layout.simple_list_item_1, laptopStorageList)
                binding.AutoCompleteTextviewStorage.setAdapter(adapterStorage)
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }

        //Loan Type
        val laptopLoanTypeList = ArrayList<String>()

        database.collection("laptop_information")
            .get().addOnSuccessListener { result ->
                for (document in result) {
                    if (!laptopLoanTypeList.contains(document.data.getValue("lending_type"))) {
                        laptopLoanTypeList.add("${document.data.getValue("lending_type")}")
                    }
                }

                val adapterLaptopLoanType =
                    ArrayAdapter(requireContext(), R.layout.simple_list_item_1, laptopLoanTypeList)
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
                            R.layout.simple_list_item_1,
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

}