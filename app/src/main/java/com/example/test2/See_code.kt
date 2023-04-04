package com.example.test2

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

@Suppress("DEPRECATION")
class See_code : AppCompatActivity() {

    lateinit var text_code: TextView
    private lateinit var button_continue: Button
    var username: String = ""
    var pickedBitMap : Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.see_code)
        supportActionBar?.hide()

        username = intent.getStringExtra("username").toString()
        pickedBitMap = intent.getParcelableExtra("BitmapImage")

        text_code = findViewById(R.id.code)
        button_continue = findViewById(R.id.button)
        val recovery_code: String? = intent.getStringExtra("recovery_code")
        text_code.text = recovery_code
        button_continue.setOnClickListener {
            val intent = Intent(this, Allow_loc::class.java)
            intent.putExtra("username", username)
            intent.putExtra("BitmapImage", pickedBitMap)
            startActivity(intent)
            finish()
        }



    }
}