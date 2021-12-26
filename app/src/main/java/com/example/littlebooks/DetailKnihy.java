package com.example.littlebooks;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.sql.ResultSet;
import java.sql.Statement;

public class DetailKnihy extends AppCompatActivity {
    TextView nazov;
    java.sql.Connection conn;
    String result = "";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_knihy);

        nazov = findViewById(R.id.nazov);

        Bundle extras = getIntent().getExtras();
        String id = "";
        if (extras != null) {
             id = extras.getString("key");
        }
        try {



        }catch (Exception e){
            Log.e("DetailKnihy", e.toString());
        }




    }
    public void GetTextFromSQL(View view){
        try {
            Connect connect = new Connect();
            conn = connect.connectionClass();
            if (connect !=null){
                String query = "Select * from kniha where id = 1";
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(query);
                nazov.setText(rs.getString(2));
            }

        }catch (Exception e){

        }
    }
}