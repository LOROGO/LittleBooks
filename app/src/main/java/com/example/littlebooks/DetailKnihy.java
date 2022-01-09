package com.example.littlebooks;

import android.annotation.SuppressLint;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class DetailKnihy extends AppCompatActivity implements BackgroundTask.ApiCallback, BackgroundTask.CallbackReview {
    TextView zaner, pocetStran, pocetStran2, autor, autor2, nazovKnihy, nazovKnihy2, obsah, obsah2, recenzia, meno;
    ImageView obrazokKnihy, pouzivatel, hviezdicky;
    EditText recenziaPopis;
    Button odoslat;
    java.sql.Connection conn;
    String result = "";
    String id;

    FirebaseAuth fAuth;
    RequestQueue requestQueue;

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
        nazovKnihy = findViewById(R.id.menoUsera);
        nazovKnihy2 = findViewById(R.id.nazovKnihy2);
        obsah = findViewById(R.id.obsah);
        obsah2 = findViewById(R.id.obsah2);
        obrazokKnihy = findViewById(R.id.obrazokKnihy);
        recenzia = findViewById(R.id.recenzia);
        pouzivatel = findViewById(R.id.pouzivatel);
        hviezdicky = findViewById(R.id.hviezdicky);
        recenziaPopis = findViewById(R.id.recenziaPopis);
        odoslat = findViewById(R.id.odoslat);

        autor.setPaintFlags(autor.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        nazovKnihy.setPaintFlags(nazovKnihy.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        obsah.setPaintFlags(obsah.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        recenzia.setPaintFlags(recenzia.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        fAuth = FirebaseAuth.getInstance();

        Bundle extras = getIntent().getExtras();
        id = "";
        if (extras != null) {
             id = extras.getString("id");
        }
        try {


        }catch (Exception e){
            Log.e("DetailKnihy", e.toString());
        }

        BackgroundTask backgroundTask = new BackgroundTask(2);
        backgroundTask.table = "kniha";
        backgroundTask.action = "select";
        backgroundTask.scr = "1";
        backgroundTask.php = "get_knihy4";
        backgroundTask.podmienka = "id_kniha="+id;
        backgroundTask.setApiCallback(this);
        backgroundTask.execute();



        BackgroundTask backgroundTask1 = new BackgroundTask(4);
        backgroundTask1.action = "selectReview";
        backgroundTask1.php = "recenzia";
        backgroundTask1.uid = fAuth.getUid();
        backgroundTask1.id_kniha = id;
        backgroundTask1.popis = recenziaPopis.getText().toString();
        backgroundTask1.hodnotenie = "5";
        backgroundTask1.setApiCallback1(this);
        backgroundTask1.execute();

        odoslat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://159.223.112.133/recenzia.php?action=newReview";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        response -> {
                            Log.d("DetailKJur", response);
                        },
                        error -> {
                            Log.d("RegE", error.toString());
                        }
                ){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError { //berie to udaje z editextov a posiela ich do php
                        Map<String, String> params = new HashMap<>();
                        params.put("uid", fAuth.getUid());
                        params.put("popis", recenziaPopis.getText().toString());
                        params.put("hodnotenie", "5");
                        params.put("id_kniha", id);
                        return params;
                    }
                };
                requestQueue = Volley.newRequestQueue(DetailKnihy.this);
                requestQueue.add(stringRequest);
            }
        });
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

    @Override
    public void populateLayReview(JSONArray obj) {

    }
}