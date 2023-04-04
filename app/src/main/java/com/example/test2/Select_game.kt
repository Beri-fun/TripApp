package com.example.test2

import android.content.Intent
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import java.sql.Statement

@Suppress("DEPRECATION")
class Select_game : AppCompatActivity() {

    var if_connection: Boolean = false
    var if_internet: Boolean = false
    var username: String = ""
    var internet: Boolean = false
    var pickedBitMap : Bitmap? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.select_game)
        supportActionBar?.hide()

        username = intent.getStringExtra("username").toString()
        pickedBitMap = intent.getParcelableExtra("BitmapImage")

        checkInternet()
        if (!internet) {
            val intent = Intent(this, Transparent::class.java)
            startActivity(intent)
        }
    }

    fun checkInternet() {
        if_connection = NetworkIsConnected()
        if_internet = InternetIsConnected()
        internet = if_connection && if_internet
        return
    }

    fun wroclaw(view: View) {
        checkInternet()
        if (!internet) {
            val intent = Intent(this, Transparent::class.java)

            startActivity(intent)
        } else {
            updateDB(1)
        }
    }

    fun updateDB(game_id: Int) {
        var sth_was: Boolean = false
        val c = Connection_DB()
        val connection = c.conclass()
        connection.autoCommit = true
        Connection_DB.getDatabaseMetaData(connection)

        val query4 =
            "SELECT 1 FROM [dbo].[UserInfo] WHERE username ='" + username + "';"
        val smt4: Statement
        smt4 = connection.createStatement()
        val resultset4 = smt4.executeQuery(query4)
        var sth_was2: Boolean = false

        while (resultset4.next()) {
            sth_was2 = true
        }
        if (!sth_was2) {
            val query5 =
                "INSERT INTO [dbo].[UserInfo] (username, try) VALUES ('" + username + "'," + 0 + ");"
            val smt5: Statement
            smt5 = connection.createStatement()
            smt5.executeUpdate(query5)
        }

        val query =
            "SELECT 1 FROM [dbo].[Points] WHERE username ='" + username + "' AND game_id=" + game_id + ";"
        val smt: Statement
        smt = connection.createStatement()
        val resultset = smt.executeQuery(query)

        while (resultset.next()) {
            sth_was = true
        }
        if (sth_was) {
            val intent1 = Intent(this, Loading::class.java)
            intent1.putExtra("username", username)
            intent1.putExtra("BitmapImage", pickedBitMap)
            intent1.putExtra("gameid", game_id.toString())
            startActivity(intent1)

        } else {
            val query3 =
                "INSERT INTO [dbo].[Points] (username, game_id, point_id, points, active) VALUES ('" + username + "', " + game_id + ", " + 1 + ", " + 0 + "," + 0 + ");"
            val smt3: Statement
            smt3 = connection.createStatement()
            val resultset3 = smt3.executeUpdate(query3)
            if (game_id == 1) {
                val intent = Intent(this, Game_intro::class.java)
                intent.putExtra("username", username)
                intent.putExtra("BitmapImage", pickedBitMap)
                startActivity(intent)
            } else {
                val intent = Intent(this, Game_intro_theaters::class.java)
                intent.putExtra("username", username)
                intent.putExtra("BitmapImage", pickedBitMap)
                startActivity(intent)
            }
        }

    }
    fun wroclaw_artist(view: View) {
        checkInternet()
        if (!internet) {
            val intent = Intent(this, Transparent::class.java)
            startActivity(intent)
        } else {
            updateDB(2)
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