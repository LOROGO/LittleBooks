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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;


public class DetailKnihy extends AppCompatActivity implements BackgroundTask.ApiCallback, BackgroundTask.CallbackReview {
    TextView zaner, pocetStran, pocetStran2, autor, autor2, nazovKnihy, nazovKnihy2, obsah, obsah2, recenzia, meno;
    ImageView obrazokKnihy, pouzivatel, srdiecko, book;
    EditText recenziaPopis;
    Button odoslat;
    RecyclerView recyclerView;
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
        srdiecko = findViewById(R.id.srdiecko);
        book = findViewById(R.id.book);
        recenziaPopis = findViewById(R.id.recenziaPopis);
        odoslat = findViewById(R.id.odoslat);
        recyclerView = findViewById(R.id.recyclerViewRecenzia);

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
        //nacitanie udajov o knihe detail>background>php>db>php>background a tu
        BackgroundTask backgroundTask = new BackgroundTask(2);
        backgroundTask.table = "kniha";
        backgroundTask.action = "select";
        backgroundTask.scr = "1";
        backgroundTask.php = "get_knihy4";
        backgroundTask.podmienka = "id_kniha="+id;
        backgroundTask.setApiCallback(this);
        backgroundTask.execute();


        //zobrazenie recenzii ku danej knihe
        Log.d("url1", "detail2");
        BackgroundTask backgroundTask1 = new BackgroundTask(4);
        backgroundTask1.action = "getReviews";
        backgroundTask1.php = "recenzia";
        backgroundTask1.id_kniha = id;
        backgroundTask1.setApiCallback1(this);
        backgroundTask1.execute();

        odoslat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://159.223.112.133/recenzia.php?action=newReview";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        response -> {

                            recenziaPopis.setText("");
                            Log.d("DetailKJur", response);
                            BackgroundTask backgroundTask1 = new BackgroundTask(4);
                            backgroundTask1.action = "getReviews";
                            backgroundTask1.php = "recenzia";
                            backgroundTask1.id_kniha = id;
                            backgroundTask1.setApiCallback1(DetailKnihy.this);
                            backgroundTask1.execute();

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


        srdiecko.setOnClickListener(new View.OnClickListener() {        //pridanie riadku do db
            @Override
            public void onClick(View v) {
                String url = "http://159.223.112.133/get_knihy4.php?&action=insertOblubene"+ "&uid=" + fAuth.getUid() + "&id_kniha=" + id;
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        response -> {


                            Log.d("RegR", response.toString());
                        },
                        error -> {
                            Log.d("RegE", error.toString());

                        }
                ){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError { //berie to udaje z editextov a posiela ich do php
                        Map<String, String> params = new HashMap<>();

                        return params;
                    }
                };
                requestQueue = Volley.newRequestQueue(DetailKnihy.this);
                requestQueue.add(stringRequest);
            }
        });

        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://159.223.112.133/get_knihy4.php?&action=insertPrecitat"+ "&uid=" + fAuth.getUid() + "&id_kniha=" + id;
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        response -> {


                            Log.d("RegR", response.toString());
                        },
                        error -> {
                            Log.d("RegE", error.toString());

                        }
                ){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError { //berie to udaje z editextov a posiela ich do php
                        Map<String, String> params = new HashMap<>();

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
            zaner.setText(a.getString("podkategoria"));
            pocetStran.setText(a.getString("pocet_stran"));

            Picasso.with(DetailKnihy.this).load(a.getString("obrazok")).into(obrazokKnihy);

            Log.d("popDet", a.getString("nazov"));


        }catch (Exception e){
            Log.d("popDet", e.getMessage());
        }

    }

    @Override
    public void populateLayReview(JSONArray obj) {
        //vytvorenie noveho listu
        List<ModelMainData2> recenzie = new ArrayList<>();
        if (obj!=null){
            for (int i = 0; i < obj.length(); i++) {
                try {
                    //spracuje json a da do listu
                    JSONObject a = obj.getJSONObject(i);
                    recenzie.add(new ModelMainData2(

                            a.getString("uid"),
                            a.getString("popis"),
                            a.getString("hodnotenie"),
                            id,
                            a.getString("obrazok"),
                            a.getString("meno"),
                            a.getString("priezvisko")

                    ));
                }catch (Exception e){
                    Log.e("PopulateRec", e.getMessage());
                }

            }

            //posle list do adapteru a nastavi adapter pre recyclerview
            populateRecView2(recenzie);
        }
    }

    public void populateRecView2(List<ModelMainData2> recenzie){
            AdapterReview adapterReview = new AdapterReview(recenzie, DetailKnihy.this);
            // Attach the adapter to the recyclerview to populate items
            recyclerView.setAdapter(adapterReview);
            // Set layout manager to position the items

            recyclerView.setLayoutManager(new LinearLayoutManager(DetailKnihy.this));
            recyclerView.setItemAnimator(new SlideInLeftAnimator());
            SnapHelper snapHelper = new LinearSnapHelper();
            //snapHelper.attachToRecyclerView(recyclerView);
    }
}