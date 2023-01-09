package com.example.mobilecomputingproject.ui.library

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mobilecomputingproject.R
import com.example.mobilecomputingproject.databinding.FragmentLibraryMapBinding
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.*

class LibraryMapFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentLibraryMapBinding? = null
    private val binding get() = _binding!!


    private lateinit var googleMap: GoogleMap
    var fusedLocationProviderClient:FusedLocationProviderClient?=null

    lateinit var libName: TextView
    lateinit var campusName: TextView
    lateinit var postalCode: TextView
    lateinit var timmings: TextView
    var latlong : LatLng = LatLng(0.0, 0.0)
    var id: String?=null
    var currentLatLong : LatLng= LatLng(0.0,0.0)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLibraryMapBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val i=requireArguments().get("libraryId")
        id=i.toString()
        // Inflate the layout for this fragment
        libName=binding.libraryNameValue
        campusName=binding.campusNameValue
        postalCode=binding.postalCodeValue
        timmings=binding.timmingsValue

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())



        permissionCheck()


        val events=view.findViewById<Button>(R.id.eventsButton)
        events.setOnClickListener{

            val bundle = hashMapOf(
                "lib_id" to id
            )

            findNavController().navigate(R.id.action_libraryMapFragment_to_libraryFeedFragment,Bundle().apply {
                putString("libraryId",id)
            })

        }

        val mapView=binding.mapView
        mapView.onCreate(savedInstanceState)
        mapView.onResume()
        mapView.getMapAsync(this)

        // get data and set to textview from firebase database
        var database= FirebaseFirestore.getInstance()
        database.collection("library").addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                for (dc : DocumentChange in value?.documentChanges!!){
                    val libId=dc.document.get("libId")
                    Log.e("hello",libId.toString())
                    if(i == libId) {
                        val name = dc.document.get("name")
                        val campus = dc.document.get("campus")
                        val postal = dc.document.get("postal")
                        val timming = dc.document.get("timmings")
                        val latitude = dc.document.get("latitude").toString()
                        val longitude = dc.document.get("longitude").toString()

                        latlong=LatLng(latitude.toDouble(),longitude.toDouble())



                        googleMap.addMarker(MarkerOptions().position(latlong))
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlong,15f))
                        libName.text= name.toString()
                        campusName.text= campus.toString()
                        postalCode.text= postal.toString()
                        timmings.text= timming.toString()

                        break
                    }

                }
            }
        })

        val googleMapApp=view.findViewById<Button>(R.id.googleMapsButton)
        googleMapApp.setOnClickListener()
        {
            val gmmIntentUri = Uri.parse("https://www.google.com/maps/dir/?api=1&origin="+currentLatLong.latitude.toString()+","+currentLatLong.longitude.toString()+"&destination="+latlong.latitude.toString()+","+latlong.longitude.toString()+"&travelmode=driving")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }

    }

    private fun permissionCheck() {
        // check permission
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            
            currentLocation()
        } else {
            
            requestPermissions(arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION), 100)
        }
    }
    // to Display map
    override fun onMapReady(map: GoogleMap) {
        map?.let {
            googleMap=it
            val location = LatLng(latlong.latitude, latlong.longitude)
            googleMap.addMarker(MarkerOptions().position(location).title("DAL"))
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 15F))
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        
        if (requestCode == 100 && grantResults.size > 0
            && (grantResults[0] + grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
            
            currentLocation()
        } else {

            permissionCheck()


            Toast.makeText(activity, "Permission denied please grant permission to open maps", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("MissingPermission")
    private fun currentLocation() {
        
        val locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        // Check condition
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        ) {
            
            // get last location
            fusedLocationProviderClient!!.lastLocation.addOnCompleteListener {
                
                val location = it.result
                // null condition check
                if (location != null) {
                    
                    currentLatLong= LatLng(location.latitude,location.longitude)

                } else {
                    
                    val locationRequest = LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                        .setInterval(10000)
                        .setFastestInterval(1000)
                        .setNumUpdates(1)

                    
                    val locationCallback: LocationCallback = object : LocationCallback() {
                        override fun onLocationResult(
                            locationResult: LocationResult
                        ) {
                            
                            val location1 = locationResult.lastLocation

                            if (location1 != null) {
                                Toast.makeText(requireContext(),location1.latitude.toString(),Toast.LENGTH_LONG).show()
                                Toast.makeText(requireContext(),location1.longitude.toString(),Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                    // Request location updates
                    fusedLocationProviderClient!!.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
                }
            }
        } else {
            
            // open location setting
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
        }
    }

}
