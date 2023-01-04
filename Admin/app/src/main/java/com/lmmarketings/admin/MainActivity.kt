package com.lmmarketings.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth= Firebase.auth

        var loginEmail=findViewById<EditText>(R.id.login_email)
        var loginPassword=findViewById<EditText>(R.id.login_password)
        val loginBtn=findViewById<Button>(R.id.login_btn)
        loginBtn.setOnClickListener {
//            checkLocationPermission()
            loginBtn.isClickable=false
            auth.signInWithEmailAndPassword(loginEmail.text.toString(), loginPassword.text.toString())
                .addOnCompleteListener(this){
                    if(it.isSuccessful){
                        startActivity(Intent(this, ViewLocation::class.java))
                        this.finish()
                    }else{
                        Toast.makeText(this,"Unable to login", Toast.LENGTH_SHORT).show()
                        loginBtn.isClickable=true
                    }
                }
        }
    }
}