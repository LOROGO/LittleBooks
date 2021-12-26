package com.example.littlebooks;

import android.os.StrictMode;
import android.util.Log;

import com.mysql.jdbc.Connection;

import java.sql.DriverManager;

public class Connect {
    Connect con;
    String uname, pass, ip, port, database;

    public java.sql.Connection connectionClass(){
        uname = "doadmin";
        pass = "vhVfELGWlFZ7nzOa";
        ip = "db-mysql-fra1-76684-do-user-10334479-0.b.db.ondigitalocean.com";
        database = "defaultdb";
        port = "25060";

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        java.sql.Connection connect = null;
        String ConnectionURL = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            ConnectionURL= "jdbc:mysql://"+ ip + ":"+ port+";"+ "databasename="+ database+";user="+uname+";password="+pass+";";
            connect = (Connection) DriverManager.getConnection(ConnectionURL);
        }catch (Exception ex){
            Log.e("Con1", ex.getMessage());
        }

        return connect;

    }
}
