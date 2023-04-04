package com.example.test2

//import com.example.test2.R.id.btn_get_loc
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


@Suppress("DEPRECATION")
class Allow_loc : AppCompatActivity() {


    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    var permision: Boolean = false
    var loc_enabled: Boolean = false
    var username: String = ""
    var pickedBitMap : Bitmap? = null

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.allow_loc)

        supportActionBar?.hide()
        username= intent.getStringExtra("username").toString()
        pickedBitMap = intent.getParcelableExtra("BitmapImage")

        permision = CheckPermissions()
        if (permision) {
            loc_enabled = isLocationEnabled()
        }
        if (loc_enabled) {
            val intent = Intent(this, Select_game::class.java)
            intent.putExtra("username", username)
            intent.putExtra("BitmapImage", pickedBitMap)
            startActivity(intent)
            finish()
        }

        val sw1 = findViewById<Switch>(R.id.switchButton)


        sw1?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) getCurrentLocation()
        }

        val buttonClick = findViewById<Button>(R.id.continue_after_loc)
        buttonClick.setOnClickListener {
            if (sw1.isChecked) {
                val intent = Intent(this, Select_game::class.java)
                intent.putExtra("username", username)
                startActivity(intent)
                finish()

            } else {
                Toast.makeText(this, "Please enable your location to continue", Toast.LENGTH_SHORT)
                    .show()
            }

        }
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
    }

    private fun getCurrentLocation() {
        if (CheckPermissions()) {
            if (isLocationEnabled()) {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermissions()
                    return
                }
                fusedLocationProviderClient.lastLocation.addOnCompleteListener(this) { task ->
                    val location: Location? = task.result
                    if (location == null) {
                        Toast.makeText(this, "Non location received", Toast.LENGTH_SHORT).show()
                    }
                }

            } else {
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }

        } else {
            requestPermissions()
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_REQUEST_ACCESS_LOCATION
        )


    }

    companion object {
        private const val PERMISSION_REQUEST_ACCESS_LOCATION = 100
    }

    private fun CheckPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val buttonClick = findViewById<Button>(R.id.continue_after_loc)


        if (requestCode == PERMISSION_REQUEST_ACCESS_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                buttonClick.isEnabled = true

            } else {
                Toast.makeText(this, "Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}