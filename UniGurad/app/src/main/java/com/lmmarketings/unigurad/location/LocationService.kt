package com.lmmarketings.unigurad.location

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlin.collections.Map as Map1

class LocationService : Service() {

    private val CHANNEL_ID = "LocationService"
    var latitude = 0.0
    var longitude = 0.0
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var databaseReference: DatabaseReference
    lateinit var auth:FirebaseAuth
    override fun onCreate() {
        super.onCreate()

        //To get instance of our firebase database
        firebaseDatabase = FirebaseDatabase.getInstance()
        auth=Firebase.auth
        //Location is the root node in the realtime database
        //To get reference of our database
        databaseReference = firebaseDatabase.getReference("${auth.uid.toString()}/Location")

        //Function to show notification with latitude and longitude of user
        showNotification(latitude, longitude)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        //Function to get current location using LocationManager Api
        getCurrentLocation(this)

        return START_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    private fun showNotification(latitude: Double, longitude: Double) {

        //It requires to create notification channel when android version is more than or equal to oreo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }

        val notificationIntent = Intent(this, ShareLocation::class.java)

        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getActivity(
                this,
                0,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        } else {
            PendingIntent.getActivity(
                this,
                0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Location Service")
            .setContentText("Latitude = $latitude Longitude = $longitude")
            .setSmallIcon(androidx.core.R.drawable.notification_bg)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
        startForeground(1, notification)
    }

    private fun createNotificationChannel() {
        val serviceChannel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(
                CHANNEL_ID, "Location Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        val manager = getSystemService(NotificationManager::class.java)
        manager!!.createNotificationChannel(serviceChannel)
    }

    @SuppressLint("ServiceCast")
    private fun getCurrentLocation(context: Context?) {

        //Check all location permission granted or not
        if (ActivityCompat.checkSelfPermission(
                context!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            //Criteria class indicating the application criteria for selecting a location provider
            val criteria = Criteria()
            criteria.accuracy = Criteria.ACCURACY_FINE
            criteria.isSpeedRequired = true

            val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val provider = locationManager.getBestProvider(criteria, true)

            if (provider != null) {
                locationManager.requestLocationUpdates(
                    provider, 1, 0.1f, object : LocationListener {
                        override fun onLocationChanged(location: Location) {

                            //Location changed

                            latitude = location.latitude
                            longitude = location.longitude

                            //Function to add data to firebase realtime database
                            addDataToDatabase()

                            //Update notification with latest latitude and longitude
                            showNotification(latitude, longitude)

                        }

                        override fun onProviderDisabled(provider: String) {
                            //Provider disabled
                        }

                        override fun onProviderEnabled(provider: String) {
                            //Provider enabled
                        }

                        override fun onStatusChanged(
                            provider: String?,
                            status: Int,
                            extras: Bundle?
                        ) {

                        }

                    })
            }
        }
    }
    private fun addDataToDatabase() {

        //setValue method is used to add value to RTFD
//        databaseReference.child("latitude").setValue(latitude)
//        databaseReference.child("longitude").setValue(longitude)
        databaseReference.setValue(
            CurrentLocation(latitude,longitude)
        )
    }

}