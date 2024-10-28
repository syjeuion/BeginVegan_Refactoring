/*
package com.example.presentation.view.main

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.location.LocationRequest
import android.os.Handler
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.isInvisible
import com.example.beginvegan.config.ApplicationClass
import com.example.beginvegan.config.BaseActivity
import com.example.beginvegan.databinding.ActivityWelcomeBinding
import com.google.android.gms.location.LocationServices

class WelcomeActivity : BaseActivity<ActivityWelcomeBinding>({ActivityWelcomeBinding.inflate(it)}) {
    override fun init() {
        getLocation()
        Handler(mainLooper).postDelayed({
            fadeIn(binding.ivWelcomeBeginvegan)
        },500)
        Handler(mainLooper).postDelayed({
            moveToMain()
        },4000)
    }
    private fun moveToMain(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
    private fun fadeIn(imageView: ImageView) {
        imageView.isInvisible = false
        val fadeIn = ObjectAnimator.ofFloat(imageView, "alpha", 0f, 1f)
        fadeIn.duration = 2500
        fadeIn.start()
    }
    @SuppressLint("MissingPermission")
    private fun getLocation(){
        val fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this)

        fusedLocationProviderClient.getCurrentLocation(LocationRequest.QUALITY_HIGH_ACCURACY,null)
            .addOnSuccessListener { success: Location? ->
                success?.let { location ->
                    ApplicationClass.xLatitude = (location.latitude).toString()
                    ApplicationClass.xLongitude = (location.longitude).toString()
                }
            }
            .addOnFailureListener { fail ->
                Toast.makeText(this,fail.message, Toast.LENGTH_SHORT).show()
            }
    }

    // 비건 타입 수정

}*/
