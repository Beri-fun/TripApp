package com.example.test2

import android.net.ConnectivityManager
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

@Suppress("DEPRECATION")
class No_Internet : AppCompatActivity() {

    var if_connection: Boolean = false
    var if_internet: Boolean = false
    private lateinit var button_continue: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.no_internet)
        supportActionBar?.hide()

        button_continue = findViewById(R.id.button)
        button_continue.setOnClickListener {
            if_connection = NetworkIsConnected()
            if_internet = InternetIsConnected()
            if (if_connection && if_internet) {
                finish()
            } else {
                Toast.makeText(this, "Please check your Internet connection", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun NetworkIsConnected(): Boolean {
        val cm = applicationContext.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null
    }
    fun InternetIsConnected(): Boolean {
        return try {
            val command = "ping -c 1 google.com"
            Runtime.getRuntime().exec(command).waitFor() == 0
        } catch (e: Exception) {
            false
        }
    }
}