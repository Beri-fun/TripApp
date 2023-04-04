package com.example.test2

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.Wroclaw_tasks.Task1
import com.example.test2.databinding.WroclawBinding
import java.sql.Statement

@Suppress("DEPRECATION")
class Wroclaw : AppCompatActivity() {

    private lateinit var binding: WroclawBinding
    var username: String = ""
    var if_connection: Boolean = false
    var if_internet: Boolean = false
    lateinit var check: ImageView
    lateinit var progress: TextView
    var internet: Boolean = false
    var pickedBitMap : Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        username = intent.getStringExtra("username").toString()
        pickedBitMap = intent.getParcelableExtra("BitmapImage")
        binding = WroclawBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.toolbar))
        binding.toolbarLayout.title = title
        binding.fab.setBackgroundColor(Color.parseColor("#FFBB86FC"));
        binding.fab.setOnClickListener { view ->
            val intent = Intent(this, Leaderboard::class.java)
            intent.putExtra("username", username)

            startActivity(intent)

        }

        binding.user.setBackgroundColor(Color.parseColor("#FFBB86FC"));
        binding.user.setOnClickListener { view ->
            val intent = Intent(this, User_screen::class.java)
            intent.putExtra("username", username)
            intent.putExtra("BitmapImage", pickedBitMap)
            startActivity(intent)

        }


        check = findViewById(R.id.check1)
        progress = findViewById(R.id.progress)
        checkInternet()
        if (!internet) {
            val intent = Intent(this, Transparent::class.java)
            startActivity(intent)
        } else {
            update_screen()
        }
    }

    fun checkInternet() {
        if_connection = NetworkIsConnected()
        if_internet = InternetIsConnected()
        internet = if_connection && if_internet
        return
    }

    fun update_screen() {
        var sth_was: Boolean = false
        val c = Connection_DB()
        val connection = c.conclass()
        connection.autoCommit = true
        Connection_DB.getDatabaseMetaData(connection)
        val query =
            "SELECT 1 FROM [dbo].[Points] WHERE username ='" + username + "' AND game_id=" + 1 + ";"
        val smt: Statement
        smt = connection.createStatement()
        val resultset = smt.executeQuery(query)
        while (resultset.next()) {
            sth_was = true
        }
        if (sth_was) {
            var temp = 0
            val checks = mapOf(
                "1" to R.id.check1,
                "2" to R.id.check2,
                "3" to R.id.check3,
                "4" to R.id.check4,
                "5" to R.id.check5,
                "6" to R.id.check6,
                "7" to R.id.check7,
                "8" to R.id.check8,
                "10" to R.id.check10
            )
            val query = "SELECT * FROM [dbo].[Points] WHERE username ='" + username + "' AND game_id=" + 1 + ";"
            val smt: Statement
            smt = connection.createStatement()
            val resultset = smt.executeQuery(query)
            var points = 0
            while (resultset.next()) {
                val point_id = resultset.getInt("point_id").toString()
                if (resultset.getInt("points") > 0) {
                    points += resultset.getInt("points")
                    temp = checks[point_id]!!
                    check = findViewById(temp)
                    check.isVisible = true
                }
            }
            progress.text = (points * 10).toString() + "%"
        }
    }

    override fun onResume() {
        super.onResume()
        checkInternet()
        if (!internet) {
            val intent = Intent(this, Transparent::class.java)
            startActivity(intent)
        } else {
            update_screen()
        }
    }

    fun task1(view: View) {
        val intent = Intent(this, Task1::class.java)
        val dbname = "St. John of Nepomuk"
        intent.putExtra("dbname", dbname)
        intent.putExtra("username", username)
        intent.putExtra("point_id", "1")
        intent.putExtra("game_id", "1")
        intent.putExtra("image", "nepomucen1")
        intent.putExtra("title", "<b> Location one</b> <br> "+ dbname)
        startActivity(intent)
    }

    fun task2(view: View) {
        val intent = Intent(this, Task1::class.java)
        val dbname = "St. Marcin church"
        intent.putExtra("dbname", dbname)
        intent.putExtra("username", username)
        intent.putExtra("point_id", "2")
        intent.putExtra("game_id", "1")
        intent.putExtra("image", "kosciol_sw_marcina1")
        intent.putExtra("title", "<b> Location two</b> <br> "+ dbname)
        startActivity(intent)
    }

    fun task3(view: View) {
        val intent = Intent(this, Task1::class.java)
        val dbname = "Wroclovka"
        intent.putExtra("dbname", dbname)
        intent.putExtra("username", username)
        intent.putExtra("point_id", "3")
        intent.putExtra("game_id", "1")
        intent.putExtra("image", "wroclovka1")
        intent.putExtra("title", "<b> Location three</b> <br> "+ dbname)
        startActivity(intent)
    }

    fun task4(view: View) {
        val intent = Intent(this, Task1::class.java)
        val dbname = "Market Hall"
        intent.putExtra("dbname", dbname)
        intent.putExtra("username", username)
        intent.putExtra("point_id", "4")
        intent.putExtra("game_id", "1")
        intent.putExtra("image", "hala_targowa")
        intent.putExtra("title", "<b> Location four</b> <br> "+ dbname)
        startActivity(intent)
    }

    fun task5(view: View) {
        val intent = Intent(this, Task1::class.java)
        val dbname = "Drawer Koparkus"
        intent.putExtra("dbname", dbname)
        intent.putExtra("username", username)
        intent.putExtra("point_id", "5")
        intent.putExtra("game_id", "1")
        intent.putExtra("image", "koparkus")
        intent.putExtra("title", "<b> Location five</b> <br> "+ dbname)
        startActivity(intent)
    }

    fun task6(view: View) {
        val intent = Intent(this, Task1::class.java)
        val dbname = "Bernardine church"
        intent.putExtra("dbname", dbname)
        intent.putExtra("username", username)
        intent.putExtra("point_id", "6")
        intent.putExtra("game_id", "1")
        intent.putExtra("image", "bernardynow0")
        intent.putExtra("title", "<b> Location six</b> <br> "+ dbname)
        startActivity(intent)
    }

    fun task7(view: View) {
        val intent = Intent(this, Task1::class.java)
        val dbname = "Juliusz Slowacki"
        intent.putExtra("dbname", dbname)
        intent.putExtra("username", username)
        intent.putExtra("point_id", "7")
        intent.putExtra("game_id", "1")
        intent.putExtra("image", "slowacki0")
        intent.putExtra("title", "<b> Location seven</b> <br> "+ dbname)
        startActivity(intent)
    }

    fun task8(view: View) {
        val intent = Intent(this, Task1::class.java)
        val dbname = "Sculpture 'Birds'"
        intent.putExtra("dbname", dbname)
        intent.putExtra("username", username)
        intent.putExtra("point_id", "8")
        intent.putExtra("game_id", "1")
        intent.putExtra("image", "ptaki1")
        intent.putExtra("title", "<b> Location eight</b> <br> "+ dbname)
        startActivity(intent)
    }

    fun task10(view: View) {
        val intent = Intent(this, Task1::class.java)
        val dbname = "Cathedral"
        intent.putExtra("dbname", dbname)
        intent.putExtra("username", username)
        intent.putExtra("point_id", "10")
        intent.putExtra("game_id", "1")
        intent.putExtra("image", "katedra0")
        intent.putExtra("title", "<b> Location ten</b> <br> "+ dbname)
        startActivity(intent)
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