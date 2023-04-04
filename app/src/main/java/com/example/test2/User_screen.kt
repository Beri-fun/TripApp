package com.example.test2

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.*
import java.sql.Statement


@Suppress("DEPRECATION")
class User_screen : AppCompatActivity() {

    lateinit var imgGallery: ImageView
    lateinit var userName: TextView
    var pickedBitMap : Bitmap? = null
    var if_connection: Boolean = false
    var if_internet: Boolean = false
    var username: String = ""
    var internet: Boolean = false
    lateinit var pointFiled : TextView
    lateinit var open_gamesFiled : TextView
    lateinit var finished_gamesFiled : TextView
    lateinit var triesFiled : TextView
    lateinit var placeFiled : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.userscreen)
        imgGallery = findViewById(R.id.image)
        userName = findViewById(R.id.user)
        pointFiled = findViewById(R.id.points)
        placeFiled = findViewById(R.id.place)
        open_gamesFiled = findViewById(R.id.open_games)
        finished_gamesFiled = findViewById(R.id.finished_games)
        triesFiled = findViewById(R.id.tries)
        username = intent.getStringExtra("username").toString()
        pickedBitMap = intent.getParcelableExtra("BitmapImage")
        userName.text = username
        getPhoto()

        checkInternet()
        if (!internet) {
            val intent = Intent(this, Transparent::class.java)
            startActivity(intent)
        }
        getDataFromDB()

    }

    private fun getPhoto() {
        val selectedImageUri: Uri?
        val selectedImgFile = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "selectedImg.jpg")

        selectedImageUri= Uri.fromFile(selectedImgFile)
        if (selectedImageUri != null) {
            if (Build.VERSION.SDK_INT >= 28) {
                val source = ImageDecoder.createSource(this.contentResolver,selectedImageUri!!)
                pickedBitMap = ImageDecoder.decodeBitmap(source)
                imgGallery.setImageBitmap(Bitmap.createScaledBitmap(pickedBitMap!!, pickedBitMap!!.width, pickedBitMap!!.width, false))

            }
            else {
                pickedBitMap = MediaStore.Images.Media.getBitmap(this.contentResolver,selectedImageUri)
                imgGallery.setImageBitmap(Bitmap.createScaledBitmap(pickedBitMap!!, pickedBitMap!!.width, pickedBitMap!!.width, false))

            }
        }

    }

    fun getDataFromDB() {
        val c = Connection_DB()
        val connection = c.conclass()
        connection.autoCommit = true
        val query =
            "SELECT sum(points) AS SUMP FROM [dbo].[Points] WHERE username='" + username + "' GROUP BY username ORDER BY sum(points) DESC;"
        val smt: Statement
        smt = connection.createStatement()
        val resultset = smt.executeQuery(query)
        while (resultset.next()) {
            pointFiled.text = "Points: " + resultset.getString("SUMP")
        }
        val query1 =
            "SELECT username, count (distinct game_id) AS CGAMES FROM [dbo].[Points] where username = '"+ username + "' group by username;"
        val smt1: Statement
        smt1 = connection.createStatement()
        val resultset1 = smt1.executeQuery(query1)
        while (resultset1.next()) {
            open_gamesFiled.text = "Open games: " + resultset1.getInt("CGAMES").toString()
        }
        val query2 =
            "SELECT username, sum(points) AS SUM_P, count (distinct game_id) AS C_GAMES FROM [dbo].[Points] where username = '"+ username + "' group by username;"
        val smt2: Statement
        smt2 = connection.createStatement()
        var count_games = 0
        val resultset2 = smt2.executeQuery(query2)
        while (resultset2.next()) {
            if (resultset2.getString("SUM_P").toInt() == 10) {
                count_games += 1
            }
            finished_gamesFiled.text = "Finished games: " + count_games.toString()
        }

        val query3 =
            "SELECT username, sum(points) AS SUMP FROM [dbo].[Points] GROUP BY game_id, username ORDER BY sum(points) DESC"
        val smt3: Statement
        smt3 = connection.createStatement()
        val resultset3 = smt3.executeQuery(query3)
        var counter = 1
        while (resultset3.next()) {
            if (username == resultset3.getString("username")) {
                placeFiled.text = counter.toString()
                break
            }
            else {
                counter += 1
            }
        }
        if (counter == 1)  placeFiled.text = "Place: " + counter.toString() + "st"
        if (counter == 2)  placeFiled.text = "Place: " + counter.toString() + "nd"
        if (counter == 3)  placeFiled.text = "Place: " + counter.toString() + "rd"
        if (counter > 3) placeFiled.text = "Place: " + counter.toString() + "th"

        val query4 =
            "SELECT * FROM [dbo].[UserInfo] WHERE username ='" + username + "';"
        val smt4: Statement
        smt4 = connection.createStatement()
        val resultset4 = smt4.executeQuery(query4)
        while (resultset4.next()) {
            triesFiled.text = "Tries: " + resultset4.getInt("try").toString()

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
