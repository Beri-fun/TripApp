package com.example.test2

import android.net.ConnectivityManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

@Suppress("DEPRECATION")
class Transparent : AppCompatActivity() {

    var if_connection: Boolean = false
    var if_internet: Boolean = false
    var internet: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.transparent)
        val pullToRefresh = findViewById<SwipeRefreshLayout>(R.id.swipe)
        pullToRefresh.setOnRefreshListener {
            checkInternet()
            pullToRefresh.isRefreshing = false
            if (internet) {
                finish()
            }
        }
    }
    fun checkInternet() {
        if_connection = NetworkIsConnected()
        if_internet = InternetIsConnected()
        internet = if_connection && if_internet
        return
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