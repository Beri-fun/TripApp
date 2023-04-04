package com.example.test2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Ready_to_go : AppCompatActivity() {

    private lateinit var btn_finalise: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ready_to_go)
        supportActionBar?.hide()

        btn_finalise = findViewById(R.id.finalise)
        btn_finalise.setOnClickListener {
            val intent = Intent(this, Allow_loc::class.java)
            startActivity(intent)

        }


    }



}