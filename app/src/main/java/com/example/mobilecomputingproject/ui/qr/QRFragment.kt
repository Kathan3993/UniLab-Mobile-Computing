package com.example.mobilecomputingproject.ui.qr

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mobilecomputingproject.databinding.FragmentNotificationsBinding
import android.Manifest
import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.example.mobilecomputingproject.R
import com.example.mobilecomputingproject.models.QRCheckIn
import com.example.mobilecomputingproject.models.RoomBooking
import com.google.firebase.firestore.FirebaseFirestore
import com.google.zxing.qrcode.encoder.QRCode
import java.text.SimpleDateFormat
import java.util.*

class QRFragment : Fragment() {

    private lateinit var codeScanner: CodeScanner
    private lateinit var db: FirebaseFirestore
    private var _binding: FragmentNotificationsBinding? = null
    private lateinit var slot: List<String>
    private lateinit var docId: String

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var context = requireContext()

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) ==
            PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CAMERA),
                123
            )
        } else {
            startScanning(view, context)
        }
    }

    private fun startScanning(view: View, context: Context) {


        val sharedPref = requireActivity().getSharedPreferences("Data", Context.MODE_PRIVATE)
        var enteredEmail = sharedPref.getString("email", "xyz")

        val scannerView: CodeScannerView = view.findViewById(R.id.scanner_view)
        codeScanner = CodeScanner(context, scannerView)
        codeScanner.camera = CodeScanner.CAMERA_BACK
        codeScanner.formats = CodeScanner.ALL_FORMATS
        codeScanner.autoFocusMode = AutoFocusMode.SAFE
        codeScanner.scanMode = ScanMode.SINGLE

        var roomNotFoundFlag = true
        codeScanner.decodeCallback = DecodeCallback {
            val roomNumber = it.text
            db = FirebaseFirestore.getInstance()
            val ref = db.collection("RoomBooking")
            val format = SimpleDateFormat("HH", Locale.US)
            val hour = format.format(Date())
            val currentHour: Int = hour.toInt()
            ref.whereEqualTo("room", roomNumber).whereEqualTo("userId", enteredEmail).whereEqualTo(
                "checkedIn", false
            ).get().addOnSuccessListener { task ->
                if(task.size() == 0){
                    Toast.makeText(
                        requireContext(),
                        "Already checked in",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigate(R.id.action_navigation_notifications_to_navigation_home)
                }else {
                    for (result in task) {
                        slot = result.data.getValue("slot").toString().split('-')
                        docId = result.id
                        println(docId)
                        println(slot)
                    }
                    if (currentHour >= slot[0].toInt() && currentHour <= slot[1].toInt()) {
                        db.collection("RoomBooking").document(docId)
                            .update("checkedIn", true)
                        Toast.makeText(
                            requireContext(),
                            "Checked in successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        val successPage = Dialog(
                            context,
                            R.style.Theme_MobileComputingProject_PopupOverlay
                        )
                        successPage.setContentView(R.layout.fragment_qr_check_in_sucess)
                        successPage.show()

                    }else {
                        // qr code check in failed
                        val tryAgainDialog = Dialog(context, R.style.dialog_popup)
                        tryAgainDialog.setContentView(R.layout.qr_code_failed_try_again)
                        tryAgainDialog.show()

                        val buttonTryAgain: Button =
                            tryAgainDialog.findViewById(R.id.buttonTryAgain)
                        buttonTryAgain.setOnClickListener {
                            tryAgainDialog.dismiss()
                        }
                    }

                }



            }
            roomNotFoundFlag = false

            activity?.runOnUiThread {
                if (roomNotFoundFlag) {
                    val tryAgainDialog = Dialog(context, R.style.dialog_popup)
                    tryAgainDialog.setContentView(R.layout.qr_invalid_room)
                    tryAgainDialog.show()

                    val buttonTryAgain: Button =
                        tryAgainDialog.findViewById(R.id.buttonWrongQRTryAgain)
                    buttonTryAgain.setOnClickListener {
                        tryAgainDialog.dismiss()
                    }
                }
            }
        }


        codeScanner.errorCallback = ErrorCallback {
                    Toast.makeText(
                        requireContext(),
                        "Camera Initialization Error: ${it.message}",
                        Toast.LENGTH_SHORT
                    ).show()

                }

                scannerView.setOnClickListener {
                    codeScanner.startPreview()
                }
            }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



        }

