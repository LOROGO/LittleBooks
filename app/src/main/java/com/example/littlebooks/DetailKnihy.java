package com.example.littlebooks;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.sql.ResultSet;
import java.sql.Statement;

public class DetailKnihy extends AppCompatActivity {
    TextView zaner, pocetStran, pocetStran2, autor, autor2, nazovKnihy, nazovKnihy2, obsah, obsah2, recenzia;
    ImageView obrazokKnihy;
    java.sql.Connection conn;
    String result = "";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obsah);

        zaner = findViewById(R.id.zaner);
        pocetStran = findViewById(R.id.pocetStran);
        pocetStran2 = findViewById(R.id.pocetStran2);
        autor = findViewById(R.id.autor);
        autor2 = findViewById(R.id.autor2);
        nazovKnihy = findViewById(R.id.nazovKnihy);
        nazovKnihy2 = findViewById(R.id.nazovKnihy2);
        obsah = findViewById(R.id.obsah);
        obsah2 = findViewById(R.id.obsah2);
        obrazokKnihy = findViewById(R.id.obrazokKnihy);
        recenzia = findViewById(R.id.recenzia);

        autor.setPaintFlags(autor.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        nazovKnihy.setPaintFlags(nazovKnihy.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        obsah.setPaintFlags(obsah.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        recenzia.setPaintFlags(recenzia.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

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
                //nazov.setText(rs.getString(2));
            }

        }catch (Exception e){

        }
    }
}