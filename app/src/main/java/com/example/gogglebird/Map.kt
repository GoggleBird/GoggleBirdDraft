package com.example.gogglebird

import android.app.AlertDialog
import android.app.Dialog
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Rect
import android.location.GpsStatus
import android.location.Location
import android.os.Bundle
import android.preference.PreferenceManager
import android.telephony.SmsManager
import android.view.MotionEvent
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.res.ResourcesCompat
import com.example.gogglebird.databinding.ActivityMapBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.osmdroid.api.IMapController
import org.osmdroid.bonuspack.routing.OSRMRoadManager
import org.osmdroid.bonuspack.routing.Road
import org.osmdroid.bonuspack.routing.RoadManager
import org.osmdroid.config.Configuration.getInstance
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Overlay
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.IMyLocationConsumer
import org.osmdroid.views.overlay.mylocation.IMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay


class Map : AppCompatActivity(), IMyLocationProvider, MapListener, GpsStatus.Listener {

    //variables
    private var latitudeUser: Double = 0.0
    private var longitudeUser: Double = 0.0


    private lateinit var mMap: MapView
    private lateinit var controller: IMapController
    private lateinit var mMyLocationOverlay: MyLocationNewOverlay

    private val LOCATION_REQUEST_CODE = 100

    private val noteMap = HashMap<GeoPoint, String>()

    // init binding
    private val binding: ActivityMapBinding by lazy {
        ActivityMapBinding.inflate(layoutInflater)
    }

    //mapview and map controller

    //  private lateinit var mapView: MapView
    private lateinit var mapController: IMapController

    val hotspotLocations = listOf(
        Pair("Hluhluwe iMfolozi Park", GeoPoint(-28.219831, 31.951865)),
        Pair("Umgeni River Bird Park", GeoPoint(-29.808167, 31.017467)),
        Pair("Durban Japanese Gardens", GeoPoint(-29.7999, 31.03758)),
    )

    private val hotSpotMarkers = mutableListOf<Marker>()

    private lateinit var routeMsg: String
    private var mileConversionFactor: Double = 0.621371

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val sendSmsButton = findViewById<Button>(R.id.sendLocationButton)
        val phoneNumEditText = findViewById<EditText>(R.id.editTextTextNumber)

        getInstance().load(
            applicationContext,
            getSharedPreferences("Open Street Map Android", MODE_PRIVATE)
        )

        mMap = binding.mapView
        mMap.setTileSource(TileSourceFactory.MAPNIK) // tile source -> change tile source for different map styles
        mMap.mapCenter
        mMap.setMultiTouchControls(true)
        mMap.getLocalVisibleRect(Rect())

        mMyLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(this), mMap)
        controller = mMap.controller


        mMyLocationOverlay.enableMyLocation()
        mMyLocationOverlay.enableFollowLocation()
        mMyLocationOverlay.isDrawAccuracyEnabled = true

        // set initial zoom level
        controller.setZoom(6.0)

        mMap.overlays.add(mMyLocationOverlay)
        setupMap()
        mMap.addMapListener(this)

        // check and request permissions
        managePermissions()


        val animatedMarkerOverlay = object : Overlay(this) {

            override fun onSingleTapConfirmed(e: MotionEvent?, mapView: MapView?): Boolean {

                // Calculate the latitude and longitude from the GeoPoint

                val geoPoint = mMyLocationOverlay.myLocation
                latitudeUser = geoPoint.latitude
                longitudeUser = geoPoint.longitude

                //create a custom dialog or info window to display the latitude and longitude

                val dialog = Dialog(this@Map)
                dialog.setContentView(R.layout.custom)

                val latitudeTextView = dialog.findViewById<TextView>(R.id.textViewLatitude)
                val longitudeTextView = dialog.findViewById<TextView>(R.id.textViewLongitude)

                latitudeTextView.text = "Latitude: $latitudeUser"
                longitudeTextView.text = "Longitude: $longitudeUser"

                dialog.show()

                return true
            }


        }
        mMap.overlays.add(animatedMarkerOverlay)

        // get ref to view hotspots button
        val viewHotspotsBtn = findViewById<Button>(R.id.viewHotspotsButton)

        // add on click
        viewHotspotsBtn.setOnClickListener {
            addHotspotMarkers()
        }

        val showRoutesBtn = findViewById<Button>(R.id.buttonRouting)

        showRoutesBtn.setOnClickListener {
            calculateAndDisplayRoutes()
        }


        sendSmsButton.setOnClickListener {
            // check if location is available
            if (latitudeUser != 0.0 && longitudeUser != 0.0) {
                // get phone num from edit text
                val phoneNum = phoneNumEditText.text.toString().trim()

                // check if phone num is empty
                if (phoneNum.isNotEmpty()) {
                    val message = "Latitude: $latitudeUser, Longitude: $longitudeUser"
                    // call sms function
                    sendSMS(phoneNum, message)

                } else {
                    Toast.makeText(this, "Please enter a valid phone number", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                Toast.makeText(this, "Location information not available", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun calculateAndDisplayRoutes() {
        val startpoint = mMyLocationOverlay.myLocation

        if (startpoint == null) {
            Toast.makeText(this, "Location loading error", Toast.LENGTH_SHORT).show()
            return
        }

        // Define a maximum distance you want to filter by (in meters)
        val maxDistance = 100.0  // Change this to your desired value

        for ((startLocationName, endPoint) in hotspotLocations) {
            GlobalScope.launch(Dispatchers.IO) {
                val roadManager = OSRMRoadManager(this@Map, "OBP_Tuto/1.0")
                var road: Road? = null
                var retryCount = 0

                //    val roadWidth = 6 // Set the width of the route in pixels
                //   val roadColor = Color.RED // Set the color of the route

                while (road == null && retryCount < 3) {
                    road = try {
                        roadManager.getRoad(arrayListOf(startpoint, endPoint))
                    } catch (e: java.lang.Exception) {
                        null
                    }
                    retryCount++
                }

                withContext(Dispatchers.Main) {
                    if (road != null && road.mStatus == Road.STATUS_OK) {
                        // Check if the road length is less than the maximum distance

                        if (Settings.unitSelected == "km") {
                            if (road.mLength <= Settings.distanceSet.toDouble()) {
                                val roadOverlay = RoadManager.buildRoadOverlay(road)
                                mMap.overlays.add(roadOverlay)

                                // Display the route in an alert dialog
                                routeMsg = "Start location: Your current location\nEnd location: $startLocationName\nDistance: ${road.mLength} kms"

                                showRouteDetailsDialog(routeMsg)
                            }
                        } else {
                            if (road.mLength <= (Settings.distanceSet.toDouble() * mileConversionFactor)) {
                                val roadOverlay = RoadManager.buildRoadOverlay(road)
                                mMap.overlays.add(roadOverlay)

                                val roadInMiles = road.mLength * mileConversionFactor
                                routeMsg = "Start location: Your current location\nEnd location: $startLocationName\nDistance: $roadInMiles miles"

                                showRouteDetailsDialog(routeMsg)
                            }
                        }



                        mMap.invalidate()
                    } else {
                        Toast.makeText(
                            this@Map,
                            "Error when loading road - status=${road?.mStatus ?: "unknown"}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }


            }
        }


    }


    private fun showRouteDetailsDialog(routeDetails: String) {
        runOnUiThread {
            val alertDialog = AlertDialog.Builder(this@Map)
            alertDialog.setTitle("Route Details")
            alertDialog.setMessage(routeDetails)
            alertDialog.setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            alertDialog.create().show()
        }
    }

    private fun sendSMS(phoneNum: String, message: String) {
        try {
            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(phoneNum, null, message, null, null)
            Toast.makeText(this, "SMS sent successfully", Toast.LENGTH_SHORT).show()
        } catch (e: java.lang.Exception) {
            Toast.makeText(this, "SMS failed to send", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }


    }

    private fun addHotspotMarkers() {
        mMap.overlays.removeAll(hotSpotMarkers)

        for ((name, location) in hotspotLocations) {
            val marker = Marker(mMap)
            marker.position = location
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            marker.icon = ResourcesCompat.getDrawable(
                resources,
                org.osmdroid.library.R.drawable.marker_default,
                null
            )/// add the marker to to the list

            // Create a custom dialog for displaying location name and adding a note

            marker.setOnMarkerClickListener { marker, mapView ->
                val dialog = Dialog(this@Map)
                dialog.setContentView(R.layout.custom_marker_dialog)

                val noteEditText = dialog.findViewById<EditText>(R.id.noteEditText)
                val saveNoteButton = dialog.findViewById<Button>(R.id.saveNoteButton)
                val displayNoteTextView = dialog.findViewById<TextView>(R.id.displayNoteTextView)


                //Display the location name

                val locationNameTextView = dialog.findViewById<TextView>(R.id.locationNameTextView)
                locationNameTextView.text = name


                //Load and display the saved note for this marker

                val savedNote = loadNote(marker.position)
                displayNoteTextView.text = savedNote

                saveNoteButton.setOnClickListener {
                    val note = noteEditText.text.toString()
                    savedNote(marker.position, note)
                    displayNoteTextView.text = note
                }
                dialog.show()
                true
            }

            hotSpotMarkers.add(marker)
        }

        mMap.overlays.addAll(hotSpotMarkers)
        mMap.invalidate()
    }

    private fun savedNote(location: GeoPoint, note: String) {

        // Store the note in the HashMap with the location as the key
        noteMap[location] = note
    }

    private fun loadNote(location: GeoPoint?): String {

        // Retrieve the note from the HashMap based on the location
        return noteMap[location] ?: ""

    }


    //handle permissions


    private fun isLocationPermissionGranted(): Boolean {

        val fineLocation = ActivityCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val coarseLocation = ActivityCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED



        return fineLocation && coarseLocation

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)


        if (requestCode == LOCATION_REQUEST_CODE) {

            if (grantResults.isNotEmpty()) {

                for (result in grantResults) {

                    if (result == PackageManager.PERMISSION_GRANTED) {
                        //Handles permissions granted
                        //Re-initialize map if needed
                        //setupMap()

                    } else {

                        Toast.makeText(this, "Permissions are denied", Toast.LENGTH_SHORT).show()

                    }

                }
            }

        }
    } // method ends

    private fun managePermissions() {

        val requestPermission = mutableListOf<String>()
        if (!isLocationPermissionGranted()) {

            // if these were not granted

            requestPermission.add(android.Manifest.permission.ACCESS_FINE_LOCATION)
            requestPermission.add(android.Manifest.permission.ACCESS_COARSE_LOCATION)

        }

        // SMS permission
        if (!isSmsPermissionGranted()) {
            requestPermission.add(android.Manifest.permission.SEND_SMS)
        }

        if (requestPermission.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                requestPermission.toTypedArray(),
                LOCATION_REQUEST_CODE
            )


        }

    }

    private fun isSmsPermissionGranted(): Boolean {

        return ActivityCompat.checkSelfPermission(
            this,
            android.Manifest.permission.SEND_SMS
        ) == PackageManager.PERMISSION_GRANTED
    }


    private fun setupMap() {

        getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))

        mapController = mMap.controller
        mMap.setMultiTouchControls(true)

        //init the start point
        val startPoint = GeoPoint(-29.8587, 31.0218)
        mapController.setCenter(startPoint)
        mapController.setZoom(6.0)

        //create marker
        val icLocationMarker = Marker(mMap)
        icLocationMarker.position = startPoint
        icLocationMarker.icon =
            ResourcesCompat.getDrawable(resources, R.drawable.baseline_my_location_24, null)
        //add marker to map view


        //Add a click listener to the location marker
        icLocationMarker.setOnMarkerClickListener { marker, mapView ->

            val latitude = marker.position.latitude
            val longitude = marker.position.longitude

            val dialog = Dialog(this@Map)
            dialog.setContentView(R.layout.custom)

            val latitudeTextView = dialog.findViewById<TextView>(R.id.textViewLatitude)
            val longitudeTextView = dialog.findViewById<TextView>(R.id.textViewLongitude)

            latitudeTextView.text = "Latitude: $latitude"
            longitudeTextView.text = "Longitude: $longitude"

            dialog.show()

            true// return true to indicate that the event is consumed
        }


        mMap.overlays.add(icLocationMarker)


    }

    override fun startLocationProvider(myLocationConsumer: IMyLocationConsumer?): Boolean {
        // Start location provider here

        return true
    }

    override fun stopLocationProvider() {
        // stop location provider here
    }

    override fun getLastKnownLocation(): Location {
        // get last known location here

        return Location("last_known_location")
    }

    override fun destroy() {
        // Destroy resources here
    }

    override fun onScroll(event: ScrollEvent?): Boolean {
        // Handle map scroll event here
        return true
    }

    override fun onZoom(event: ZoomEvent?): Boolean {
        // Handles map zoom event here
        return false
    }

    override fun onGpsStatusChanged(p0: Int) {
        // handles gps status
    }


}