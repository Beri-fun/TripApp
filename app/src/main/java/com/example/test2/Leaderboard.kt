package com.example.test2

import android.annotation.SuppressLint
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.test2.databinding.LeaderboardBinding
import java.sql.Statement

@Suppress("DEPRECATION")
class Leaderboard : AppCompatActivity() {

    var if_connection: Boolean = false
    var if_internet: Boolean = false
    var internet: Boolean = false
    lateinit var text_lead : TextView
    lateinit var position: TextView
    lateinit var points_lead: TextView

    private lateinit var binding: LeaderboardBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val username: String? = intent.getStringExtra("username")

        binding = LeaderboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkInternet()
        if (!internet) {
            val intent = Intent(this, Transparent::class.java)
            startActivity(intent)
        }
        val checks = mapOf(
            "1" to R.id.text_1,
            "2" to R.id.text_2,
            "3" to R.id.text_3,
            "4" to R.id.text_4,
            "5" to R.id.text_5,
            "6" to R.id.text_6,
            "7" to R.id.text_7,
            "8" to R.id.text_8,
            "9" to R.id.text_9,
            "10" to R.id.text_10
        )
        val points = mapOf(
            "1" to R.id.points_1,
            "2" to R.id.points_2,
            "3" to R.id.points_3,
            "4" to R.id.points_4,
            "5" to R.id.points_5,
            "6" to R.id.points_6,
            "7" to R.id.points_7,
            "8" to R.id.points_8,
            "9" to R.id.points_9,
            "10" to R.id.points_10
        )
        var temp = 0
        position = findViewById(R.id.your_place)
        val c = Connection_DB()
        val connection = c.conclass()
        connection.autoCommit = true
        val query =
            "SELECT username, sum(points) AS SUMP FROM [dbo].[Points] GROUP BY game_id, username ORDER BY sum(points) DESC"
        val smt: Statement
        smt = connection.createStatement()
        val resultset = smt.executeQuery(query)
        var find_postion = false

        for (i in 1..10) {
            resultset.next()
            temp = checks[i.toString()]!!
            text_lead = findViewById(temp)
            val actual_username = resultset.getString("username")
            points_lead = findViewById(points[i.toString()]!!)
            points_lead.text = resultset.getInt("SUMP").toString() + " pts"
            text_lead.text = actual_username
            if (actual_username == username) {
                find_postion = true
                if (i == 1) position.text = i.toString() + "st"
                if (i == 2) position.text = i.toString() + "nd"
                if (i == 3) position.text = i.toString() + "rd"
                if (i > 3) position.text = i.toString() + "th"
            }
        }
        var counter = 11
        if (!find_postion) {
            while (resultset.next()) {
                if (username == resultset.getString("username")) {
                    position.text = counter.toString() + "th"
                    break
                } else {
                    counter += 1
                }
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