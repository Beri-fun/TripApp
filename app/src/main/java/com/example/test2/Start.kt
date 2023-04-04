package com.example.test2

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import `in`.insideandroid.logintemplate.LoginActivity
import `in`.insideandroid.logintemplate.SignUpActivity
import java.io.*
import java.sql.Statement
import java.util.*


@Suppress("DEPRECATION", "NAME_SHADOWING")
class Start : AppCompatActivity() {

    var if_connection: Boolean = false
    var if_internet: Boolean = false
    var internet: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.start)
        supportActionBar?.hide()

        val sign_up = findViewById<Button>(R.id.sign_up)
        val log_in = findViewById<Button>(R.id.log_in)

        checkInternet()
        if (!internet) {
            val intent = Intent(this, Transparent::class.java)
            startActivity(intent)
        } else {
            checkRememberMe()
        }

        sign_up.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        log_in.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }

    fun checkInternet() {
        if_connection = NetworkIsConnected()
        if_internet = InternetIsConnected()
        internet = if_connection && if_internet
        return
    }

    fun checkRememberMe() {
        var uid: String? = null
        val c = Connection_DB()
        val connection = c.conclass()
        connection.autoCommit = true
        Connection_DB.getDatabaseMetaData(connection)
        val context = applicationContext
        uid = read_file(context)

        if (uid != null) {
            try {
                val query = "SELECT * from [dbo].[Users]"
                val smt: Statement
                smt = connection.createStatement()
                val resultset = smt.executeQuery(query)

                while (resultset.next()) {
                    if (uid == resultset.getString("deviceid")) {
                        if (1 == resultset.getInt("rememberme")) {
                            val intent = Intent(this, Allow_loc::class.java)
                            intent.putExtra("username", resultset.getString("username"))
                            startActivity(intent)
                        }
                    }
                }
            } catch (e: Exception) {
                println("No connection to database")
            }
        } else {
            val context = applicationContext
            val filename = "uid.txt"
            var playerID: String = UUID.randomUUID().toString()
            val query = "SELECT * from [dbo].[Users]"
            val smt: Statement
            smt = connection.createStatement()
            val resultset = smt.executeQuery(query)

            val device_id_list = ArrayList<String>()
            val device_remeber_list = ArrayList<String>()
            while (resultset.next()) {
                device_id_list += resultset.getString("deviceid")
                device_remeber_list += listOf(
                    resultset.getString("deviceid"),
                    resultset.getString("rememberme")
                )
            }
            var not_the_same = 0
            while (true) {
                not_the_same = 0
                var counter = 0
                for (value in device_id_list) {
                    if (playerID == value) {
                        playerID = UUID.randomUUID().toString()
                        not_the_same = 1
                    }
                    counter++
                }
                if (not_the_same == 0) {
                    break
                }
            }
            context.openFileOutput(filename, Context.MODE_PRIVATE).use {
                it.write(playerID.toByteArray())
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (!internet) {
            val intent = Intent(this, Transparent::class.java)
            startActivity(intent)
        } else {
            checkRememberMe()
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

    private fun read_file(context: Context): String? {
        var ret = ""
        try {
            val inputStream: InputStream? = context.openFileInput("uid.txt")
            if (inputStream != null) {
                val inputStreamReader = InputStreamReader(inputStream)
                val bufferedReader = BufferedReader(inputStreamReader)
                var receiveString: String? = ""
                val stringBuilder = StringBuilder()
                while (bufferedReader.readLine().also { receiveString = it } != null) {
                    stringBuilder.append("\n").append(receiveString)
                }
                inputStream.close()
                ret = stringBuilder.toString()
            }
        } catch (e: FileNotFoundException) {
            Log.e("login activity", "File not found: " + e.toString())
        } catch (e: IOException) {
            Log.e("login activity", "Can not read file: " + e.toString())
        }
        return ret
    }
}