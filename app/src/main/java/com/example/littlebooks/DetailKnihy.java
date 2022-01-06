package com.example.littlebooks;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;


public class DetailKnihy extends AppCompatActivity implements BackgroundTask.ApiCallback {
    TextView zaner, pocetStran, pocetStran2, autor, autor2, nazovKnihy, nazovKnihy2, obsah, obsah2, recenzia;
    ImageView obrazokKnihy;
    java.sql.Connection conn;
    String result = "";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_knihy);

        zaner = findViewById(R.id.zaner);
        pocetStran = findViewById(R.id.pocetStran);
        pocetStran2 = findViewById(R.id.pocetStran2);
        autor = findViewById(R.id.autor);
        autor2 = findViewById(R.id.autor2);
        nazovKnihy = findViewById(R.id.nazovKnihy);
        nazovKnihy2 = findViewById(R.id.nazovKnihy2);
        obsah = findViewById(R.id.obsah);
        //obsah2 = findViewById(R.id.obsah2);
        obrazokKnihy = findViewById(R.id.obrazokKnihy);
        //recenzia = findViewById(R.id.recenzia);

        autor.setPaintFlags(autor.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        nazovKnihy.setPaintFlags(nazovKnihy.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        obsah.setPaintFlags(obsah.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        //recenzia.setPaintFlags(recenzia.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        Bundle extras = getIntent().getExtras();
        String id = "";
        if (extras != null) {
             id = extras.getString("id");
        }
        try {


        }catch (Exception e){
            Log.e("DetailKnihy", e.toString());
        }
        BackgroundTask backgroundTask = new BackgroundTask("kniha", "select", "1","get_knihy4", "id_kniha="+id, "");
        backgroundTask.setApiCallback(this);
        backgroundTask.execute();

    }

    @Override
    public void populateLay(JSONArray obj) {

        try {
            JSONObject a = obj.getJSONObject(0);
            //meno editextu.append(a.getString("meno stlpca v tabulke")); - append pridava text na koniec napr ked je ze Nazov: po append je to Nazov: Basne
            nazovKnihy2.setText(a.getString("autor"));
            autor2.setText(a.getString("nazov"));
            //obsah2.setText(a.getString("obsah"));
            //zaner.setText(a.getString("zaner"));
            pocetStran.setText(a.getString("pocet_stran"));

            Picasso.with(DetailKnihy.this).load("https://images-ext-2.discordapp.net/external/LKjW88WNz2jZApjPZm9y5as66RrkbQAjwmEKoCdUBqA/https/www.pantarhei.sk/media/catalog/product/cache/8e1ec8304136dcb6f8c9c4e5913fff01/4/9/b22329f-00491129-23.jpg").into(obrazokKnihy);

            Log.d("popDet", a.getString("nazov"));


        }catch (Exception e){
            Log.d("popDet", e.getMessage());
        }

    }
}