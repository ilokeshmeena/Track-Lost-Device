package com.lmmarketings.unigurad.auth

import android.content.Intent
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lmmarketings.unigurad.location.ShareLocation
import android.Manifest
import android.app.Activity
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.savedstate.SavedStateRegistryController.Companion.create
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import com.lmmarketings.unigurad.R
import java.net.URI.create

val REQUEST_CODE_LOCATION_PERMISSION=123
class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth= Firebase.auth

        var loginEmail=findViewById<EditText>(R.id.login_email)
        var loginPassword=findViewById<EditText>(R.id.login_password)
        val loginBtn=findViewById<Button>(R.id.login_btn)

        checkLocationPermission()

        loginBtn.setOnClickListener {
            loginBtn.isClickable=false
            auth.signInWithEmailAndPassword(loginEmail.text.toString(), loginPassword.text.toString())
                .addOnCompleteListener(this){
                    if(it.isSuccessful){
                        startActivity(Intent(this, ShareLocation::class.java))
                        this.finish()
                    }else{
                        Toast.makeText(this,"Unable to login",Toast.LENGTH_SHORT).show()
                        loginBtn.isClickable=true
                    }
                }
        }
        findViewById<TextView>(R.id.create_account_text).setOnClickListener {
            startActivity(Intent(this,SignupActivity::class.java))
            this.finish()
        }

    }
    private fun checkLocationPermission() {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
            ) {

                val permissionList =
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                requestPermissions(
                    permissionList,
                    REQUEST_CODE_LOCATION_PERMISSION
                )

            } else {

                //This function checks gps is enabled or not in the device
                showGpsEnablePopup()

            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.size >= 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                //This function checks gps is enabled or not in the device
                showGpsEnablePopup()

            } else {

                Toast.makeText(this, "Permission Denied !", Toast.LENGTH_SHORT).show()

            }
        }

    }

    private fun showGpsEnablePopup() {

        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        builder.setAlwaysShow(true) //this displays dialog box like Google Maps with two buttons - OK and NO,THANKS

        val task =
            LocationServices.getSettingsClient(this).checkLocationSettings(builder.build())

        task.addOnCompleteListener {
            try {
                val response = task.getResult(ApiException::class.java)
                // All location settings are satisfied. The client can initialize location
                // requests here.

                //Gps enabled

            } catch (exception: ApiException) {
                when (exception.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> // Location settings are not satisfied. But could be fixed by showing the
                        // user dialog.
                        try {
                            // Cast to a resolvable exception.
                            val resolvable = exception as ResolvableApiException
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            resolvable.startResolutionForResult(
                                this,
                                123
                            )
                        } catch (e: IntentSender.SendIntentException) {
                            // Ignore the error.
                        }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                    }
                }
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 123) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    //Gps enabled
                }
                Activity.RESULT_CANCELED -> {
                    Toast.makeText(this, "Gps is required, please turn it on", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
}