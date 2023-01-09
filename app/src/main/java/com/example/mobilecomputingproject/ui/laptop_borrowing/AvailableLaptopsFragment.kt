package com.example.mobilecomputingproject.ui.laptop_borrowing

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobilecomputingproject.adapters.AvailableLaptopsAdapter
import com.example.mobilecomputingproject.databinding.FragmentAvailableLaptopsBinding
import com.example.mobilecomputingproject.models.LaptopInformationItem
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

/**
 * Fragment for Available Laptops
 */
class AvailableLaptopsFragment : Fragment() {

    // Adapter for Available Laptops
    lateinit var adapter: AvailableLaptopsAdapter

    // Database Instance
    val database = FirebaseFirestore.getInstance()

    // Binding for view binding
    private var _binding: FragmentAvailableLaptopsBinding? = null
    private val binding get() = _binding!!

    // Variables for storing bundle arguments
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

        _binding = FragmentAvailableLaptopsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Get the bundle arguments
        if(arguments?.getString("laptopType") != null ){
            laptopType = arguments?.getString("laptopType")!!
        }

        if(arguments?.getString("model") != null ){
            model = arguments?.getString("model")!!
        }

        if(arguments?.getString("processor") != null ){
            processor = arguments?.getString("processor")!!
        }

        if(arguments?.getString("storage") != null ){
            storage = arguments?.getString("storage")!!
        }

        if(arguments?.getString("loanType") != null ){
            loanType = arguments?.getString("loanType")!!
        }

        if(arguments?.getString("loanType") != null ){
            loanType = arguments?.getString("loanType")!!
        }

        if(arguments?.getString("library") != null ){
            library = arguments?.getString("library")!!
        }

        return root
    }

    // Populate recycler view when view created
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        populateRecyclerView()
    }

    // Function to populating recycler view
    private fun populateRecyclerView(){

        var laptopType: String? = laptopType
        var model: String? = model
        var processor: String? = processor
        var storage: String? = storage
        var loanType: String? = loanType
        var library: String? = library

        // List to store Laptop Information Item
        val availableLaptopsList = ArrayList<LaptopInformationItem>()

        var library_id: String = ""

        // Get the available laptops based on filters selected
        if(library!!.length > 0){
            database.collection("library")
                .whereEqualTo("name","$library")
                .get().addOnSuccessListener{result->
                    for(document in result){
                        library_id = "${document.id}"
                        Log.d(TAG,library_id)

                        var databaseRef = database.collection("laptop_information") as Query

                        if(model!!.isNotEmpty() ){
                            databaseRef = databaseRef.whereEqualTo("laptop_model",model)
                        }

                        if(laptopType!!.isNotEmpty()){
                            databaseRef = databaseRef.whereEqualTo("laptop_type",laptopType)
                        }

                        if(processor!!.isNotEmpty()){
                            databaseRef = databaseRef.whereEqualTo("ram",processor)
                        }

                        if(storage!!.isNotEmpty()){
                            databaseRef = databaseRef.whereEqualTo("storage",storage)
                        }

                        if(loanType!!.isNotEmpty()){
                            databaseRef = databaseRef.whereEqualTo("lending_type",loanType)
                        }

                        databaseRef
                            .whereEqualTo("is_booked", false)
                            .whereEqualTo("library_id",library_id)
                            .get().addOnSuccessListener{result->
                                for(document in result){
                                    Log.d(TAG,"${document.id} --> ${document.data}")

                                    var laptop : LaptopInformationItem = LaptopInformationItem("${document.id}", "${document.data.getValue("laptop_type")}", "${document.data.getValue("ram")}", "${document.data.getValue("storage")}","${document.data.getValue("lending_type")}","${document.data.getValue("lending_type")}".toBoolean(),"${document.data.getValue("laptop_model")}","${document.data.getValue("library_id")}","$library")
                                    availableLaptopsList.add(laptop)
                                    Log.d(TAG,"$availableLaptopsList")

                                }

                                binding.recyclerViewAvailableLaptops.layoutManager = LinearLayoutManager(requireContext())
                                adapter = AvailableLaptopsAdapter(availableLaptopsList, requireContext())
                                binding.recyclerViewAvailableLaptops.adapter = adapter
                            }
                            .addOnFailureListener{exception->
                                Log.w(TAG,"Error getting documents.",exception)
                            }
                    }
                }
                .addOnFailureListener{exception->
                    Log.w(TAG,"Error getting documents.",exception)
                }
        }else{
            var databaseRef = database.collection("laptop_information") as Query

            if(model!!.isNotEmpty()){
                Log.d(TAG, "Model is not empty")
                databaseRef = databaseRef.whereEqualTo("laptop_model",model)
            }

            if(laptopType!!.isNotEmpty()){
                Log.d(TAG, "Laptop type is not empty")
                databaseRef = databaseRef.whereEqualTo("laptop_type",laptopType)
            }

            if(processor!!.isNotEmpty()){
                Log.d(TAG, "Processor is not empty")
                databaseRef = databaseRef.whereEqualTo("ram",processor)
            }

            if(storage!!.isNotEmpty()){
                Log.d(TAG, "Storage is not empty")
                databaseRef = databaseRef.whereEqualTo("storage",storage)
            }

            if(loanType!!.isNotEmpty()){
                Log.d(TAG, "Loan type is not empty")
                databaseRef = databaseRef.whereEqualTo("lending_type",loanType)
            }

            databaseRef
                .whereEqualTo("is_booked", false)
                .get().addOnSuccessListener{result->
                    for(document in result){
                        Log.d(ContentValues.TAG,"${document.id} --> ${document.data}")

                        var laptop : LaptopInformationItem = LaptopInformationItem("${document.id}", "${document.data.getValue("laptop_type")}", "${document.data.getValue("ram")}", "${document.data.getValue("storage")}","${document.data.getValue("lending_type")}","${document.data.getValue("is_booked")}".toBoolean(),"${document.data.getValue("laptop_model")}","${document.data.getValue("library_id")}","$library")
                        availableLaptopsList.add(laptop)
                        Log.d(ContentValues.TAG,"$availableLaptopsList")

                        binding.recyclerViewAvailableLaptops.layoutManager = LinearLayoutManager(requireContext())
                        adapter = AvailableLaptopsAdapter(availableLaptopsList ,requireContext())
                        binding.recyclerViewAvailableLaptops.adapter = adapter
                    }
                }
                .addOnFailureListener{exception->
                    Log.w(ContentValues.TAG,"Error getting documents.",exception)
                }
        }

    }

}