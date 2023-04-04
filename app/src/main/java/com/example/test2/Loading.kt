package com.example.test2

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity

@Suppress("DEPRECATION")
class Loading : AppCompatActivity() {

    var if_connection: Boolean = false
    var if_internet: Boolean = false
    var internet: Boolean = false
    lateinit var progressBar: ProgressBar
    var game_id: String = ""
    var username: String = ""
    var pickedBitMap : Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loading)
        progressBar = findViewById(R.id.pBar)
        progressBar.visibility = View.VISIBLE
        username = intent.getStringExtra("username").toString()
        game_id = intent.getStringExtra("gameid").toString()
        pickedBitMap = intent.getParcelableExtra("BitmapImage")


        if (game_id == "1") {
            val i = Intent(this, Wroclaw::class.java)
            i.putExtra("username", username)
            i.putExtra("BitmapImage", pickedBitMap)
            Handler().postDelayed({
                startActivity(i)
                finish()
            }, 500)
        }
        if (game_id == "2") {
            val i = Intent(this, Wroclaw_theaters::class.java)
            i.putExtra("username", username)
            i.putExtra("BitmapImage", pickedBitMap)
            Handler().postDelayed({
                startActivity(i)
                finish()
            }, 500)
        }
    }
}
