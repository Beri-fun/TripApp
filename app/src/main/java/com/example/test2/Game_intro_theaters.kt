package com.example.test2

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


@Suppress("DEPRECATION")
class Game_intro_theaters : AppCompatActivity() {

    var username: String = ""
    private lateinit var button: Button
    var pickedBitMap : Bitmap? = null
    var game_id: String = "2"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_intro_theaters)
        supportActionBar?.hide()
        username = intent.getStringExtra("username").toString()
        pickedBitMap = intent.getParcelableExtra("BitmapImage")
        button = findViewById(R.id.button)

        button.setOnClickListener {
            val intent = Intent(this, Loading::class.java)
            intent.putExtra("username", username)
            intent.putExtra("BitmapImage", pickedBitMap)
            intent.putExtra("gameid", game_id)
            startActivity(intent)
        }

    }
}