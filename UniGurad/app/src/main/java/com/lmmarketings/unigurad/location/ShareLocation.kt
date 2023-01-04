package com.lmmarketings.unigurad.location

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lmmarketings.unigurad.DeviceAdmin
import com.lmmarketings.unigurad.MainActivity
import com.lmmarketings.unigurad.R

class ShareLocation : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var deviceManger: DevicePolicyManager
    private lateinit var compName: ComponentName
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share_location)
        deviceManger = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        compName= ComponentName(this, DeviceAdmin::class.java)
        auth= Firebase.auth
        var isLocationSharing=false
//        startService(this)
        val fakeSwitchOffLayout=findViewById<ConstraintLayout>(R.id.power_off_layout)
        val locationSharingLayout=findViewById<LinearLayout>(R.id.location_sharing_layout)
        val locationBtn=findViewById<Button>(R.id.location_sharing_button)
        val locationText=findViewById<TextView>(R.id.location_sharing_text)
        findViewById<Button>(R.id.location_sharing_logout_button).setOnClickListener {
            auth.signOut()
            startActivity(Intent(this,MainActivity::class.java))
            this.finish()
        }
        findViewById<Button>(R.id.location_sharing_fake_switchoff_button).setOnClickListener {
            locationSharingLayout.visibility=View.INVISIBLE
            fakeSwitchOffLayout.visibility= View.VISIBLE
            startService(this)
            isLocationSharing=true
            locationText.text="Location Sharing Started"
            locationBtn.text="Stop Sharing"
            val handler = Handler()
            handler.postDelayed({
                locationSharingLayout.visibility=View.VISIBLE
                fakeSwitchOffLayout.visibility= View.INVISIBLE
                deviceManger.lockNow()
            }, 3000)
        }
        locationBtn.setOnClickListener {
            if(isLocationSharing){
                stopService(this)
                isLocationSharing=false
                locationText.text="Location Sharing Disabled...."
                locationBtn.text="Start Sharing"
            }else{
                startService(this)
                isLocationSharing=true
                locationText.text="Location Sharing Started"
                locationBtn.text="Stop Sharing"
            }
        }
    }
    private fun startService(context: Context){
        val startServiceIntent = Intent(context, LocationService::class.java)
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            context.startForegroundService(startServiceIntent)
        } else {
            context.startService(startServiceIntent)
        }
    }
    private fun stopService(context: Context) {
        val stopServiceIntent = Intent(context, LocationService::class.java)
        context.stopService(stopServiceIntent)
    }
}