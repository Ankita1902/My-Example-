package com.example.jiexunxu.tinderapp

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.support.constraint.ConstraintLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton


import pl.droidsonroids.gif.GifImageButton

class MainActivity : AppCompatActivity(), LocationListener {
    // GPS location related variables
    private var lastKnownLocation: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val settings = SettingsParams()
        settings.readSettingsFromFile(this.applicationContext)
        initUI()
        initMainActivityButtons()
    }

    override fun onBackPressed() {
        this.finish()
    }

    private fun initUI() {
        title = "Pick a category"
        val layout = findViewById(R.id.mainLayout) as ConstraintLayout
        val customSearchButton = findViewById(R.id.mainPageCustomzieSearchButton) as Button
        val opts = AppOptions.getUIOptions(SettingsParams.themeID)
        layout.setBackgroundColor(applicationContext.resources.getColor(opts.backgroundColor))
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(applicationContext.resources.getColor(opts.primaryColor)))
        customSearchButton.setBackgroundResource(opts.buttonStyle)
    }

    private fun initMainActivityButtons() {
        val restaurantGifButton = findViewById(R.id.restaurantGif) as ImageButton
        val shoppingGifButton = findViewById(R.id.shoppingGif) as ImageButton
        val hotelsGifButton = findViewById(R.id.hotelsGif) as ImageButton
        val fitnessGifButton = findViewById(R.id.fitnessGif) as ImageButton
        val entertainmentGifButton = findViewById(R.id.entertainmentGif) as ImageButton
        val beautyGifButton = findViewById(R.id.beautyGif) as ImageButton
        val nightlifeGifButton = findViewById(R.id.nightlifeGif) as ImageButton
        val petsGifButton = findViewById(R.id.petsGif) as ImageButton
        val foodDeliveryButton = findViewById(R.id.foodDeliveryGif) as ImageButton
        val customSearchButton = findViewById(R.id.mainPageCustomzieSearchButton) as Button

        customSearchButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@MainActivity, SettingsActivity::class.java)
            this@MainActivity.startActivity(intent)
        })

        restaurantGifButton.setOnClickListener(View.OnClickListener { startSearch("food,restaurants") })

        shoppingGifButton.setOnClickListener(View.OnClickListener { startSearch("shopping") })

        hotelsGifButton.setOnClickListener(View.OnClickListener { startSearch("hotelstravel") })

        fitnessGifButton.setOnClickListener(View.OnClickListener { startSearch("active") })

        entertainmentGifButton.setOnClickListener(View.OnClickListener { startSearch("arts") })

        beautyGifButton.setOnClickListener(View.OnClickListener { startSearch("beautysvc,health") })

        nightlifeGifButton.setOnClickListener(View.OnClickListener { startSearch("nightlife") })

        petsGifButton.setOnClickListener(View.OnClickListener { startSearch("pets") })

        foodDeliveryButton.setOnClickListener(View.OnClickListener { startSearch("fooddelivery") })

    }

    private fun startSearch(category: String) {
        val settings = SettingsParams()
        settings.readSettingsFromFile(this.applicationContext)
        val params = settings.settingsToYelpParams()
        if (category == "fooddelivery")
            params.mustHaveFoodDelivery = true
        else
            params.setCategories(category)
        if (!params.params.containsKey("location")) {
            try {
                getDeviceLocation()
                params.setLatLng(lastKnownLocation!!.latitude, lastKnownLocation!!.longitude)
            } catch (ex: Exception) {
                ErrorActivity.start(this@MainActivity, "GPS service unavailable. Please specify a search address in advanced search to search around that area.")
            }

        }
        val intent = Intent(this@MainActivity, WaitSearchActivity::class.java)
        intent.putExtra("params", params)
        this@MainActivity.startActivity(intent)
    }

    private fun getDeviceLocation() {
        if (ContextCompat.checkSelfPermission(this.applicationContext,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            val location_context = Context.LOCATION_SERVICE
            val lm = getSystemService(location_context) as LocationManager
            val providers = lm.getProviders(true)
            for (provider in providers) {
                lm.requestLocationUpdates(provider, 1000, 0f, object : LocationListener {
                    override fun onLocationChanged(location: Location) {
                        lastKnownLocation = location
                    }

                    override fun onProviderDisabled(provider: String) {}

                    override fun onProviderEnabled(provider: String) {}

                    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
                })
                val l = lm.getLastKnownLocation(provider)
                if (lastKnownLocation == null || l != null && l.accuracy < lastKnownLocation!!.accuracy) {
                    lastKnownLocation = l
                }
            }
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }
    }

    override fun onLocationChanged(loc: Location) {
        lastKnownLocation = loc
        if (SettingsParams.debugMode) {
            val Text = ("My current location is: " + "Latitud = "
                    + loc.latitude + "Longitud = " + loc.longitude)
            Log.d("loc", "onLocationChanged$Text")
        }
    }

    override fun onProviderDisabled(provider: String) {}

    override fun onProviderEnabled(provider: String) {}

    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
}
