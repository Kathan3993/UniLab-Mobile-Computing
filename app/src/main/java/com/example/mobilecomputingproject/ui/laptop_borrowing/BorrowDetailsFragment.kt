package com.example.mobilecomputingproject.ui.laptop_borrowing

import android.content.Context
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.mobilecomputingproject.R
import com.example.mobilecomputingproject.databinding.FragmentBorrowDetailsBinding

/**
 * Fragment for Borrow Details
 */
class BorrowDetailsFragment : Fragment() {

    // Binding for view binding
    private var _binding: FragmentBorrowDetailsBinding? = null
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

    var configList = mutableListOf<String>()

    // Text entered by the user
    var enteredFirstName: String? = ""
    var enteredLastName: String? = ""
    var enteredStreet: String = ""
    var enteredCity: String = ""
    var enteredState: String = ""
    var enteredPostal: String = ""
    var enteredPhone: String = ""
    var enteredEmail: String? = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBorrowDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Get the user details from shared preferences
        val sharedPref = requireActivity().getSharedPreferences("Data", Context.MODE_PRIVATE)
        enteredFirstName = sharedPref.getString("firstname", "xyz")
        enteredEmail = sharedPref.getString("email", "abc")
        enteredLastName = sharedPref.getString("lastname", "1")

        // Setting the uneditable details of user
        binding.editTextFirstName.setText(enteredFirstName)
        binding.editTextLastName.setText(enteredLastName)
        binding.editTextEmail.setText(enteredEmail)
        binding.TextInputLayoutFirstName.helperText = validFirstName()
        binding.TextInputLayoutLastName.helperText = validLastName()
        binding.TextInputLayoutEmail.helperText = validEmail()

        // Get the bundle arguments
        laptopId = arguments?.getString("laptopId")!!
        laptopType = arguments?.getString("laptopType")!!
        configuration = arguments?.getString("configuration")!!
        loanType = arguments?.getString("loanType")!!
        library = arguments?.getString("library")!!

        configList = configuration.split(" ") as MutableList<String>

        // Prepopulate data if user clicks on Edit in Borrow Preview Screen
        if (arguments?.getString("firstName") != null) {
            firstName = arguments?.getString("firstName")!!
            binding.editTextFirstName.setText(firstName)
            binding.TextInputLayoutFirstName.helperText = validFirstName()
        }

        if (arguments?.getString("lastName") != null) {
            lastName = arguments?.getString("lastName")!!
            binding.editTextLastName.setText(lastName)
            binding.TextInputLayoutLastName.helperText = validLastName()
        }

        if (arguments?.getString("street") != null) {
            street = arguments?.getString("street")!!
            binding.editTextStreet.setText(street)
            binding.TextInputLayoutStreet.helperText = validStreet()

        }

        if (arguments?.getString("city") != null) {
            city = arguments?.getString("city")!!
            binding.editTextCity.setText(city)
            binding.TextInputLayoutCity.helperText = validCity()
        }

        if (arguments?.getString("state") != null) {
            state = arguments?.getString("state")!!
            binding.AutoCompleteTextviewProvince.setText(city)
            binding.TextInputLayoutState.helperText = validState()
        }

        if (arguments?.getString("postal") != null) {
            postal = arguments?.getString("postal")!!
            binding.editTextPostal.setText(postal)
            binding.TextInputLayoutPostal.helperText = validPostal()
        }

        if (arguments?.getString("phone") != null) {
            phone = arguments?.getString("phone")!!
            binding.editTextPhone.setText(phone)
            binding.TextInputLayoutPhoneNumber.helperText = validPhone()
        }

        if (arguments?.getString("email") != null) {
            email = arguments?.getString("email")!!
            binding.editTextEmail.setText(email)
            binding.TextInputLayoutEmail.helperText = validEmail()
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val autoCompleteTextviewProvince = binding.AutoCompleteTextviewProvince
        val province = resources.getStringArray(R.array.Province)
        val adapterLaptopType =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, province)
        autoCompleteTextviewProvince.setAdapter(adapterLaptopType)

        // Capture the details entered by the user
        firstNameFocusListener()

        lastNameFocusListener()

        streetFocusListener()

        cityFocusListener()

        stateFocusListener()

        postalFocusListener()

        phoneFocusListener()

        emailFocusListener()

        // Detect the click on Search button
        binding.buttonSearch.setOnClickListener {

            // Entered text validation
            val validFirstName = binding.TextInputLayoutFirstName.helperText == null
            val validLastName = binding.TextInputLayoutLastName.helperText == null
            val validStreet = binding.TextInputLayoutStreet.helperText == null
            val validCity = binding.TextInputLayoutCity.helperText == null
            val validState = binding.TextInputLayoutState.helperText == null
            val validPostal = binding.TextInputLayoutPostal.helperText == null
            val validPhone = binding.TextInputLayoutPhoneNumber.helperText == null
            val validEmail = binding.TextInputLayoutEmail.helperText == null

            // Check if data entered by the user is valid
            if (validFirstName && validLastName && validStreet && validCity && validState && validPostal && validPhone && validEmail) {
                var bottomSheet: TermsAndConditionsBottomSheetFragment =
                    TermsAndConditionsBottomSheetFragment()

                enteredFirstName = binding.editTextFirstName.text.toString()
                enteredLastName = binding.editTextLastName.text.toString()
                enteredStreet = binding.editTextStreet.text.toString()
                enteredCity = binding.editTextCity.text.toString()
                enteredState = binding.AutoCompleteTextviewProvince.text.toString()
                enteredPostal = binding.editTextPostal.text.toString()
                enteredPhone = binding.editTextPhone.text.toString()
                enteredEmail = binding.editTextEmail.text.toString()

                // Send the data to Borrow Preview fragment
                val bundle = bundleOf(
                    "laptopId" to laptopId,
                    "laptopType" to laptopType,
                    "configuration" to configuration,
                    "loanType" to loanType,
                    "library" to library,
                    "firstName" to enteredFirstName,
                    "lastName" to enteredLastName,
                    "street" to enteredStreet,
                    "city" to enteredCity,
                    "state" to enteredState,
                    "postal" to enteredPostal,
                    "phone" to enteredPhone,
                    "email" to enteredEmail
                )

                bottomSheet.arguments = bundle

                // Show the Bottom Sheet
                val activity = it.context as AppCompatActivity
                bottomSheet.show(activity.supportFragmentManager, "BottomSheet")
            }

        }

        // Redirect to Available Laptops screen when user clicks cancel
        binding.buttonCancel.setOnClickListener {
            val bundle = bundleOf(
                "laptopId" to laptopId,
                "laptopType" to laptopType,
                //"model" to configList[0],
                //"processor" to configList[1],
                //"storage" to configList[2],
                "loanType" to loanType,
                "library" to library,
            )
            findNavController().navigate(
                R.id.action_borrowDetailsFragment_to_availableLaptopsFragment,
                bundle
            )
        }
    }

    // Validation First Name
    private fun firstNameFocusListener() {
        binding.editTextFirstName.setOnFocusChangeListener { view, b ->
            if (!b) {
                binding.TextInputLayoutFirstName.helperText = validFirstName()
            }
        }
    }

    private fun validFirstName(): String? {
        val firstName = binding.editTextFirstName.text.toString()
        if (firstName.length > 1) {
            return null
        }

        return "Required"
    }

    // Validation Last name
    private fun lastNameFocusListener() {
        binding.editTextLastName.setOnFocusChangeListener { view, b ->
            if (!b) {
                binding.TextInputLayoutLastName.helperText = validLastName()
            }
        }
    }

    private fun validLastName(): String? {
        val lastName = binding.editTextLastName.text.toString()
        if (lastName.length > 1) {
            return null
        }

        return "Required"
    }

    // Validation Street
    private fun streetFocusListener() {
        binding.editTextStreet.setOnFocusChangeListener { view, b ->
            if (!b) {
                binding.TextInputLayoutStreet.helperText = validStreet()
            }
        }
    }

    private fun validStreet(): String? {
        val street = binding.editTextStreet.text.toString()
        if (street.length > 1) {
            return null
        }

        return "Required"
    }

    // Validation City
    private fun cityFocusListener() {
        binding.editTextCity.setOnFocusChangeListener { view, b ->
            if (!b) {
                binding.TextInputLayoutCity.helperText = validCity()
            }
        }
    }

    private fun validCity(): String? {
        val city = binding.editTextCity.text.toString()
        if (city.length > 1) {
            return null
        }

        return "Required"
    }

    // Validation Province
    private fun stateFocusListener() {
        binding.AutoCompleteTextviewProvince.setOnFocusChangeListener { view, b ->
            if (!b) {
                binding.TextInputLayoutState.helperText = validState()
            }
        }
    }

    private fun validState(): String? {
        val state = binding.AutoCompleteTextviewProvince.text.toString()
        if (state.length > 1) {
            return null
        }

        return "Required"
    }

    // Validation Postal
    private fun postalFocusListener() {
        binding.editTextPostal.setOnFocusChangeListener { view, b ->
            if (!b) {
                binding.TextInputLayoutPostal.helperText = validPostal()
            }
        }
    }

    private fun validPostal(): String? {
        val postal = binding.editTextPostal.text.toString()
        if (postal.length > 1) {
            return null
        }

        return "Required"
    }

    // Validation Phone
    private fun phoneFocusListener() {
        binding.editTextPhone.setOnFocusChangeListener { view, b ->
            if (!b) {
                binding.TextInputLayoutPhoneNumber.helperText = validPhone()
            }
        }
    }

    private fun validPhone(): String? {
        val phone = binding.editTextPhone.text.toString()
        if (!phone.matches(".*[0-9].*".toRegex())) {
            return "Must be all digits"
        }
        if (phone.length != 10) {
            return "Must be 10 digits"
        }

        return null
    }

    // Validation Email
    private fun emailFocusListener() {
        binding.editTextEmail.setOnFocusChangeListener { view, b ->
            if (!b) {
                binding.TextInputLayoutEmail.helperText = validEmail()
            }
        }
    }

    private fun validEmail(): String? {
        val email = binding.editTextEmail.text.toString()
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return "Invalid email address"
        }

        return null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}