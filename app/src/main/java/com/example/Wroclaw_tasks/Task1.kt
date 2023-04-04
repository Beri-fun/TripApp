package com.example.Wroclaw_tasks

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.provider.Settings
import android.text.Html
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.test2.Connection_DB
import com.example.test2.R
import com.example.test2.Transparent
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.math.RoundingMode
import java.sql.Statement
import kotlin.math.*

@Suppress("DEPRECATION")
class Task1 : AppCompatActivity() {

    lateinit var title_text: TextView
    lateinit var image: ImageView
    lateinit var describe: TextView
    var if_connection: Boolean = false
    var if_internet: Boolean = false
    var u_loc_lat2: Float = 0.0F
    var u_loc_long2: Float = 0.0F
    var username: String = ""
    var image_name: String = ""
    var title: String = ""
    var point_id: Int = 0
    var game_id: Int = 0
    var internet: Boolean = false
    var x_loc: Float = 0.0f
    var y_loc: Float = 0.0f
    var dbname: String = ""
    private lateinit var tvLatitude: TextView
    private lateinit var tvLongtitude: TextView
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient


    @SuppressLint("DiscouragedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.test2.R.layout.task1)
        supportActionBar?.hide()
        tvLatitude = findViewById(R.id.textView7)
        tvLongtitude = findViewById(R.id.textView8)

        checkInternet()
        if (!internet) {
            val intent = Intent(this, Transparent::class.java)
            startActivity(intent)
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        username = intent.getStringExtra("username")!!
        point_id = intent.getStringExtra("point_id")!!.toInt()
        game_id = intent.getStringExtra("game_id")!!.toInt()
        image_name = intent.getStringExtra("image")!!
        title = intent.getStringExtra("title")!!
        dbname = intent.getStringExtra("dbname")!!
        getCurrentLocation()

        title_text = findViewById(com.example.test2.R.id.title_text)
        image = findViewById(com.example.test2.R.id.image)
        describe = findViewById(com.example.test2.R.id.describe)


        title_text.text = Html.fromHtml(title)
        image.setImageResource(resources.getIdentifier(image_name, "drawable", packageName))
        val c = Connection_DB()
        val connection = c.conclass()
        connection.autoCommit = true
        Connection_DB.getDatabaseMetaData(connection)
        val query = "SELECT descr from [dbo].[game_data] WHERE point_name='" + dbname + "'"
        val smt: Statement
        smt = connection.createStatement()
        val resultset = smt.executeQuery(query)
        var description = ""
        while (resultset.next()) {
            description = resultset.getString("descr")
            describe.text = Html.fromHtml(description)

        }

        checkInternet()
        if (!internet) {
            val intent = Intent(this, Transparent::class.java)
            startActivity(intent)
        }
        val button_confirm = findViewById<Button>(R.id.button_confirm)
        button_confirm.setOnClickListener {
            if (dbname != null) {
                checkInternet()
                if (!internet) {
                    val intent = Intent(this, Transparent::class.java)
                    startActivity(intent)
                } else {
                    confirm_loc()
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
    fun get_loc_db() {
        val c = Connection_DB()
        val connection = c.conclass()
        connection.autoCommit = true
        Connection_DB.getDatabaseMetaData(connection)
        val query = "SELECT x_loc, y_loc from [dbo].[game_data] WHERE point_name='" + dbname + "'"
        val smt: Statement
        smt = connection.createStatement()
        val resultset = smt.executeQuery(query)
        var loc_ok = false

        while (resultset.next()) {
            x_loc = resultset.getFloat("x_loc")
            y_loc = resultset.getFloat("y_loc")
        }

    }

    fun confirm_loc() {
        getCurrentLocation()

        val c = Connection_DB()
        val connection = c.conclass()
        connection.autoCommit = true
        Connection_DB.getDatabaseMetaData(connection)
        val query = "SELECT x_loc, y_loc from [dbo].[game_data] WHERE point_name='" + dbname + "'"
        val smt: Statement
        smt = connection.createStatement()
        val resultset = smt.executeQuery(query)
        var loc_ok = false

        while (resultset.next()) {
            x_loc = resultset.getFloat("x_loc")
            y_loc = resultset.getFloat("y_loc")

            if (abs(u_loc_lat2 - x_loc) <= 0.002) {
                if (abs(u_loc_long2 - y_loc) <= 0.002) {
                    loc_ok = true
                    break
                }
            }
        }
        if (loc_ok) {
            val t = Toast.makeText(this, "Well done", Toast.LENGTH_SHORT)
            t.show()
            if (point_id == 1) {
                val query =
                    "UPDATE [dbo].[Points] SET points=" + 1 + " WHERE username = '" + username + "' AND game_id =" + game_id + " AND  point_id = " + point_id + ";"
                val smt: Statement
                smt = connection.createStatement()
                val resultset = smt.executeUpdate(query)
                finish()
            } else {
                val query =
                    "INSERT INTO [dbo].[Points] ( username, game_id, point_id, points, active) VALUES ('" + username + "', " + game_id + ", " + point_id + ", " + 1 + "," + 0 + ");"
                val smt: Statement
                smt = connection.createStatement()
                val resultset = smt.executeUpdate(query)
                finish()
            }
        } else {
            val query =
                "UPDATE [dbo].[UserInfo] SET try = try + 1 WHERE username = '" + username + "';"
            val smt: Statement
            smt = connection.createStatement()
            smt.executeUpdate(query)
            val t = Toast.makeText(this, "You're too far from goal. Try again", Toast.LENGTH_SHORT)
            t.show()
        }
    }


    fun hint(view: View) {
        getCurrentLocation()
        get_loc_db()
        val theta = u_loc_long2 - y_loc
        var dist = sin((x_loc/180) * Math.PI) * sin((u_loc_lat2/180) * Math.PI) + cos(((x_loc/180) * Math.PI)) * cos((u_loc_lat2/180)* Math.PI) * cos((theta/180) * Math.PI)
        dist = acos(dist)
        dist = (dist * 180) / Math.PI
        val miles = dist * 60 * 1.1515
        val distance = (miles * 1.609344)
        val round_dist = distance.toBigDecimal().setScale(1, RoundingMode.UP).toDouble()
        val t = Toast.makeText(this, "Your are ~$round_dist km from the goal", Toast.LENGTH_SHORT)
        t.show()
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

    private fun getCurrentLocation() {
        if (CheckPermissions()) {
            if (isLocationEnabled()) {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermissions()
                    return
                }
                fusedLocationProviderClient.lastLocation.addOnCompleteListener(this) { task ->
                    val location: Location? = task.result
                    if (location == null) {
                        Toast.makeText(this, "Non location received", Toast.LENGTH_SHORT).show()
                    } else {

                        u_loc_lat2 = location.latitude.toFloat()
                        u_loc_long2 = location.longitude.toFloat()
                        tvLatitude.text = location.latitude.toString()
                        tvLongtitude.text = location.longitude.toString()


                    }
                }
            } else {
                Toast.makeText(this, "Please turn on location", Toast.LENGTH_SHORT).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_REQUEST_ACCESS_LOCATION
        )


    }

    companion object {
        private const val PERMISSION_REQUEST_ACCESS_LOCATION = 100
    }

    private fun CheckPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

}
