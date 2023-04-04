package com.example.test2;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connection_DB {

    Connection con;

    @SuppressLint({"NewApi"})
    public Connection conclass() {
        StrictMode.ThreadPolicy a = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(a);
        String connectURL = null;

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            connectURL = "jdbc:jtds:sqlserver://tripappserver.database.windows.net:1433/TripAppGameData;" +
                    "user=None;password=None;ssl=require;" +
                    "encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
            con = DriverManager.getConnection(connectURL);
            con.setCatalog("TripAppGameData");
        } catch (ClassNotFoundException e) {
            Log.e("Error2 :", e.getMessage());
        } catch (SQLException e) {
            Log.e("ErrorSQL :", e.getMessage());
        } catch (Exception e) {
            Log.e("Error :", e.getMessage());
        }
        return con;
    }

    public static void getDatabaseMetaData(Connection con) {
        try {
            DatabaseMetaData dbmd = con.getMetaData();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


