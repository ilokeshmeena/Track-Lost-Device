package com.lmmarketings.unigurad

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lmmarketings.unigurad.auth.LoginActivity
import com.lmmarketings.unigurad.location.ShareLocation
val RESULT_ENABLE = 1

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var deviceManger:DevicePolicyManager
    private lateinit var compName:ComponentName
    private lateinit var btnEnable:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnEnable=findViewById(R.id.enable_admin_btn)
        deviceManger = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        compName= ComponentName(this,DeviceAdmin::class.java)
        var active=deviceManger.isAdminActive(compName)
        auth= Firebase.auth

        if (active){
            if(auth.currentUser!=null){
                startActivity(Intent(this, ShareLocation::class.java))
                this.finish()
            }else{
                startActivity(Intent(this,LoginActivity::class.java))
                this.finish()
            }
        }

        btnEnable.setOnClickListener {
            enablePhone()
        }

    }

    fun enablePhone(){
        var active=deviceManger.isAdminActive(compName)
        if(active){
            deviceManger .removeActiveAdmin( compName ) ;
        }else{
            val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, compName)
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "You should enable the app!")
            startActivityForResult(intent, RESULT_ENABLE)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RESULT_ENABLE -> {
                if (resultCode == RESULT_OK) {
                    Log.d("device Admin","done")
                    if(auth.currentUser!=null){
                        startActivity(Intent(this, ShareLocation::class.java))
                        this.finish()
                    }else{
                        startActivity(Intent(this,LoginActivity::class.java))
                        this.finish()
                    }
                } else {
                    Toast.makeText(
                        applicationContext, "Failed!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return
            }
        }
    }
}