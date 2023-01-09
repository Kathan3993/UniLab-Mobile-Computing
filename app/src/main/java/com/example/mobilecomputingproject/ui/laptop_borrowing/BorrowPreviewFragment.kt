package com.example.mobilecomputingproject.ui.laptop_borrowing

import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.mobilecomputingproject.R
import com.example.mobilecomputingproject.databinding.FragmentBorrowPreviewBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Fragment for Borrow Preview
 */
class BorrowPreviewFragment : Fragment() {

    // Binding for view binding
    private var _binding: FragmentBorrowPreviewBinding? = null
    private val binding get() = _binding!!

    // Variables for storing bundle arguments
    private var laptopId = ""
    private var laptopType = ""
    private var configuration = ""
    private var loanType = ""
    private var library = ""
    private var firstName = ""
    private var lastName = ""
    private var street = ""
    private var city = ""
    private var state = ""
    private var postal = ""
    private var phone = ""
    private var email = ""

    // Variables for extracting the Date
    lateinit var simpleDateFormat: DateFormat
    lateinit var dropCalendarInstance: Calendar
    lateinit var expiryCalendarInstance: Calendar
    lateinit var testCalendarInstance: Calendar
    lateinit var pickupDate: String
    lateinit var dropOffDate: String
    lateinit var expiryDate: String

    // Database instance
    val database = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBorrowPreviewBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Initialize the bundle arguments
        laptopId = arguments?.getString("laptopId")!!
        laptopType = arguments?.getString("laptopType")!!
        configuration = arguments?.getString("configuration")!!
        loanType = arguments?.getString("loanType")!!
        library = arguments?.getString("library")!!
        firstName = arguments?.getString("firstName")!!
        lastName = arguments?.getString("lastName")!!
        street = arguments?.getString("street")!!
        city = arguments?.getString("city")!!
        state = arguments?.getString("state")!!
        postal = arguments?.getString("postal")!!
        phone = arguments?.getString("phone")!!
        email = arguments?.getString("email")!!

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Populate the data on screen
        binding.textViewPreviewFirstNameVal.text = firstName
        binding.simpleTextViewPreviewLastNameVal.text = lastName
        binding.simpleTextViewPreviewStreetVal.text = street
        binding.simpleTextViewPreviewCityVal.text = city
        binding.simpleTextViewPreviewStateVal.text = state
        binding.simpleTextViewPreviewPostalVal.text = postal
        binding.simpleTextViewPreviewPhoneNumberVal.text = phone
        binding.simpleTextViewPreviewEmailVal.text = email
        binding.simpleTextViewPreviewLaptopTypeVal.text = laptopType
        binding.simpleTextViewPreviewConfigurationVal.text = configuration
        binding.simpleTextViewPreviewLoanTypeVal.text = loanType
        binding.simpleTextViewPreviewLibraryVal.text = library

        // Detect the click on Borrow button
        binding.buttonBorrow.setOnClickListener {

            var laptop_id: String = laptopId

            // Get the user data from Shared Preferences
            val sharedPref = requireActivity().getSharedPreferences("Data", Context.MODE_PRIVATE)
            var user_id: String? = sharedPref.getString("user_id", "1")

            // Get the calendar instance
            dropCalendarInstance = Calendar.getInstance()
            expiryCalendarInstance = Calendar.getInstance()
            testCalendarInstance = Calendar.getInstance()

            simpleDateFormat = SimpleDateFormat("dd-MM-yyyy")

            var datePlusMonth = Date()
            var datePlusDays = Date()

            pickupDate = simpleDateFormat.format(dropCalendarInstance.time)
            Log.d(TAG, "Pickup Off Date: $pickupDate")

            dropCalendarInstance.time = datePlusMonth
            dropCalendarInstance.add(Calendar.MONTH, 1)
            datePlusMonth = dropCalendarInstance.time

            dropOffDate = simpleDateFormat.format(datePlusMonth.time)
            Log.d(TAG, "Drop Off Date: $dropOffDate")

            expiryCalendarInstance.add(Calendar.HOUR, 48)
            datePlusDays = expiryCalendarInstance.time

            expiryDate = simpleDateFormat.format(datePlusDays.time)
            Log.d(TAG, "Expiry Date: $expiryDate")

            // User borrow request data
            val borrowReqData = hashMapOf(
                "user_id" to "$user_id",
                "laptop_id" to "$laptop_id",
                "pickup_date" to "$pickupDate",
                "drop_off_date" to "$dropOffDate",
                "apt" to "$street",
                "city" to "$city",
                "province" to "$state",
                "postal_code" to "$postal",
                "telephone" to "$phone",
                "status" to "active",
                "expiryDate" to "$expiryDate"
            )

            // Store record in Laptop Booking
            database.collection("laptop_booking")
                .whereEqualTo("user_id","$user_id")
                .whereIn("status", Arrays.asList("active","accepted"))
                .get().addOnSuccessListener { result ->

                    // Check if user already has active borrow requests
                    if(result.size() == 0){
                        database.collection("laptop_booking").add(borrowReqData)
                            .addOnSuccessListener{
                                    documentReference -> Log.d(TAG,"DocumentSnapshot written with ID:${documentReference.id}")

                                    // Store record in the user devices
                                    val deviceData = hashMapOf(
                                        "user_id" to "$user_id",
                                        "laptop_id" to "$laptop_id",
                                        "status" to "active",
                                        "drop_off_date" to "$dropOffDate",
                                        "pickup_date" to "$pickupDate"
                                    )

                                database.collection("user_devices").add(deviceData)
                                    .addOnSuccessListener{
                                            documentReference -> Log.d(TAG,"DocumentSnapshot written with ID:${documentReference.id}")

                                            var booking_data = hashMapOf("is_booked" to true)

                                            database.collection("laptop_information").document("$laptop_id")
                                                .set(booking_data, SetOptions.merge())
                                        Toast.makeText(
                                            activity,
                                            "A borrow request has been created for laptop.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        findNavController().navigate(R.id.action_borrowPreviewFragment_to_laptopBorrowingFragment)

                                    }.addOnFailureListener{
                                            e->Log.w(TAG,"Error adding document",e)
                                    }

                            }
                            .addOnFailureListener{
                                    e->Log.w(TAG,"Error adding document",e)
                            }
                    }else{

                        // Show the Duplicate Borrow Request popup if user already has a borrow request
                        val borrowRequestDialog = Dialog(requireContext(), R.style.dialog_popup)
                        borrowRequestDialog.setContentView(R.layout.duplicate_borrow_request)
                        borrowRequestDialog.show()

                        // Detect user click on No button in Duplicate Borrow Request popup
                        val buttonNo: Button = borrowRequestDialog.findViewById(R.id.buttonNo)
                        buttonNo.setOnClickListener {
                            borrowRequestDialog.dismiss()
                        }

                        // Detect user click on requests button in Duplicate Borrow Request popup
                        val buttonBorrowRequests: Button = borrowRequestDialog.findViewById(R.id.buttonBorrowRequests)
                        buttonBorrowRequests.setOnClickListener {
                            borrowRequestDialog.dismiss()

                            findNavController().navigate(R.id.action_borrowPreviewFragment_to_borrowRequestsFragment)
                        }

                    }

                }.addOnFailureListener{exception->
                    Log.w(TAG,"Error getting documents.",exception)
                }
        }

        // Detect user click on Edit button
        binding.buttonEdit.setOnClickListener {

            // Send the data and navigate back to the Borrow Details screen
            val bundle = bundleOf(
                "laptopId" to laptopId,
                "laptopType" to laptopType,
                "configuration" to configuration,
                "loanType" to loanType,
                "library" to library,
                "firstName" to firstName,
                "lastName" to lastName,
                "street" to street,
                "city" to city,
                "state" to state,
                "postal" to postal,
                "phone" to phone,
                "email" to email
            )

            findNavController().navigate(R.id.action_borrowPreviewFragment_to_borrowDetailsFragment,bundle)
        }
    }

}