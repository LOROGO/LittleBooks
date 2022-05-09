package com.example.littlebooks;

import static com.example.littlebooks.R.drawable.ic_baseline_favorite_24;
import static com.example.littlebooks.R.drawable.plusfarebne;
import static com.example.littlebooks.R.drawable.precitanefarebne;

import android.annotation.SuppressLint;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;


public class DetailKnihy extends AppCompatActivity implements BackgroundTask.ApiCallback, BackgroundTask.CallbackReview {
    TextView zaner, pocetStran, pocetStran2, autor, autor2, nazovKnihy, nazovKnihy2, obsah, obsah2, recenzia, meno;
    ImageView obrazokKnihy, pouzivatel, srdiecko, book, book2;
    EditText recenziaPopis;
    Button odoslat;
    RecyclerView recyclerView;
    java.sql.Connection conn;
    String result = "";
    String id;
    String LOG;
    String TAG = "DetailKnihy:   ";
    FirebaseAuth fAuth;
    RequestQueue requestQueue;
    JSONArray jsonArray;
    JSONObject jsonObject;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_knihy);
        LOG = "DetailKnihy4";

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
        book = findViewById(R.id.precitat);
        book2 = findViewById(R.id.precitane);
        recenziaPopis = findViewById(R.id.recenziaPopis);
        odoslat = findViewById(R.id.odoslat);
        recyclerView = findViewById(R.id.recyclerViewRecenzia);
        meno = findViewById(R.id.meno);

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
        backgroundTask.id_kniha = id;
        backgroundTask.setApiCallback(this);
        backgroundTask.execute();


        //zobrazenie recenzii ku danej knihe
        Log.d("url1", "detail2");
        BackgroundTask backgroundTask1 = new BackgroundTask(4);
        backgroundTask1.action = "getReviews";
        backgroundTask1.php = "recenzia";
        backgroundTask1.uid = fAuth.getUid();
        backgroundTask1.id_kniha = id;
        backgroundTask1.setApiCallback1(this);
        backgroundTask1.execute();


        String url = "http://178.62.196.85/user1.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {                                               //spusti sa ak appka dostane odpoved v JSONE od php/db
                    Log.d("RegR", response.toString());

                    try {
                        jsonArray = new JSONArray(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JSONObject json = null;
                    try {
                        json = jsonArray.getJSONObject(0);
                        Log.d("RegR", "cele: "+json.toString());
                        Log.d("RegR1", "meno: " + json.getString("meno"));
                        try {
                            Log.d("RegR", json.getString("meno"));
                            meno.setText(json.getString("meno"));
                            meno.append(" "+json.getString("priezvisko"));
                            if (!json.getString("obrazok").equals("null")){
                                Picasso.with(this).load(json.getString("obrazok")).into(pouzivatel);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("RegR", e.getMessage());
                        }
                    }
                    catch (NullPointerException e){
                        Log.d("uid", fAuth.getUid());
                    }
                    catch (JSONException e) {
                        Log.d("uid", fAuth.getUid());
                    }
                },
                error -> {
                    Log.d("RegE", error.toString());

                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {     //posiela udaje do php, tie co zadam v appke
                Map<String, String> params = new HashMap<>();
                params.put("action", "selectUser");
                params.put("uid", fAuth.getUid());
                return params;
            }
        };
        requestQueue = Volley.newRequestQueue(DetailKnihy.this);
        requestQueue.add(stringRequest);

        odoslat.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: prosim");
                String url = "http://178.62.196.85/recenzia.php?action=newReview";
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

        url = "http://178.62.196.85/get_knihy4.php?action=checkOblubene&uid="+fAuth.getUid()+"&id_kniha="+id;
        Log.d(LOG, url);
        stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {

                    Log.d("DetailKJur", response);
                    JSONObject sprava;
                    try {
                        sprava = (JSONObject) new JSONArray(response).get(0);
                        try {
                            sprava.getString("id_kniha");
                            //zafarbi na cerveno srdiecko

                            srdiecko.setImageResource(ic_baseline_favorite_24);
                            srdiecko.setOnClickListener(new View.OnClickListener() {        //delete
                                @Override
                                public void onClick(View v) {
                                    Log.d(LOG, "setDel");
                                    deleteOCP("deleteOblubene");
                                }
                            });
                        }catch (Exception e){
                            Log.d("DetailKJur1", e.getMessage());
                            srdiecko.setImageResource(R.drawable.ic_favorite_prazdne);
                            srdiecko.setOnClickListener(new View.OnClickListener() {        //pridanie riadku do db
                                @Override
                                public void onClick(View v) {
                                    Log.d(LOG, "setInsert");
                                    insertOCP("insertOblubene");
                                }
                            });

                        }
                    } catch (JSONException e) {
                        Log.d("DetailKJur1", e.getMessage());
                        srdiecko.setImageResource(R.drawable.ic_favorite_prazdne);
                        srdiecko.setOnClickListener(new View.OnClickListener() {        //pridanie riadku do db
                            @Override
                            public void onClick(View v) {
                                insertOCP("insertOblubene");
                            }
                        });                    }

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


        url = "http://178.62.196.85/get_knihy4.php?action=checkPrecitat&uid="+fAuth.getUid()+"&id_kniha="+id;
        Log.d(LOG, url);
        stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {

                    Log.d("DetailKJur", response);
                    JSONObject sprava;
                    try {
                        sprava = (JSONObject) new JSONArray(response).get(0);
                        try {
                            sprava.getString("id_kniha");
                            //zafarbi na cerveno srdiecko

                            book.setImageResource(R.drawable.plusfarebne);
                            book.setOnClickListener(new View.OnClickListener() {        //delete
                                @Override
                                public void onClick(View v) {
                                    Log.d(LOG, "setDel");
                                    deleteOCP("deletePrecitat");
                                }
                            });
                        }catch (Exception e){
                            Log.d("DetailKJur1", e.getMessage());
                            book.setImageResource(R.drawable.plusprazdna);
                            book.setOnClickListener(new View.OnClickListener() {        //pridanie riadku do db
                                @Override
                                public void onClick(View v) {
                                    Log.d(LOG, "setInsert");
                                    insertOCP("insertPrecitat");
                                }
                            });

                        }
                    } catch (JSONException e) {
                        Log.d("DetailKJur1", e.getMessage());
                        book.setImageResource(R.drawable.plusprazdna);
                        book.setOnClickListener(new View.OnClickListener() {        //pridanie riadku do db
                            @Override
                            public void onClick(View v) {
                                insertOCP("insertPrecitat");
                            }
                        });                    }

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


        url = "http://178.62.196.85/get_knihy4.php?action=checkPrecitane&uid="+fAuth.getUid()+"&id_kniha="+id;
        Log.d(LOG, url);
        stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {

                    Log.d("DetailKJur", response);
                    JSONObject sprava;
                    try {
                        sprava = (JSONObject) new JSONArray(response).get(0);
                        try {
                            sprava.getString("id_kniha");
                            //zafarbi na cerveno srdiecko

                            book2.setImageResource(precitanefarebne);
                            book2.setOnClickListener(new View.OnClickListener() {        //delete
                                @Override
                                public void onClick(View v) {
                                    Log.d(LOG, "setDel");
                                    deleteOCP("deletePrecitane");
                                }
                            });
                        }catch (Exception e){
                            Log.d("DetailKJur1", e.getMessage());
                            book2.setImageResource(R.drawable.precitaneciste);
                            book2.setOnClickListener(new View.OnClickListener() {        //pridanie riadku do db
                                @Override
                                public void onClick(View v) {
                                    Log.d(LOG, "setInsert");
                                    insertOCP("insertPrecitane");
                                }
                            });

                        }
                    } catch (JSONException e) {
                        Log.d("DetailKJur1", e.getMessage());
                        book2.setImageResource(R.drawable.precitaneciste);
                        book2.setOnClickListener(new View.OnClickListener() {        //pridanie riadku do db
                            @Override
                            public void onClick(View v) {
                                insertOCP("insertPrecitane");
                            }
                        });                    }

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



       /* book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://178.62.196.85/get_knihy4.php?&action=insertPrecitat"+ "&uid=" + fAuth.getUid() + "&id_kniha=" + id;
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
        });*/

    }


    public  void deleteOCP(String action){
        String url = "http://178.62.196.85/get_knihy4.php?&action="+action+"&uid=" + fAuth.getUid() + "&id_kniha=" + id;
        Log.d(LOG, "deleteOCP "+url);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    if (response.equals("ok-oblubene")){
                        srdiecko.setImageResource(R.drawable.ic_favorite_prazdne);
                        Log.d(LOG, "deleteSetInsert");
                        srdiecko.setOnClickListener(v -> insertOCP("insertOblubene"));
                    }

                    else if (response.equals("ok-precitat")){
                        book.setImageResource(R.drawable.plusprazdna);
                        Log.d(LOG, "deleteSetInsert");
                        book.setOnClickListener(v -> insertOCP("insertPrecitat"));
                    }

                    else if (response.equals("ok-precitane")){
                        book2.setImageResource(R.drawable.precitaneciste);
                        Log.d(LOG, "deleteSetInsert");
                        book2.setOnClickListener(v -> insertOCP("insertPrecitane"));
                    }

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
    public void insertOCP(String action){
        String url = "http://178.62.196.85/get_knihy4.php?&action="+action+"&uid=" + fAuth.getUid() + "&id_kniha=" + id;
        Log.d(LOG, "insertOCP "+url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    if (response.equals("ok-oblubene")){
                        Log.d(LOG, "insertSetDel");
                        srdiecko.setImageResource(ic_baseline_favorite_24);
                        srdiecko.setOnClickListener(v -> deleteOCP("deleteOblubene"));
                    }
                    else if(response.equals("ok-precitat")) {
                        Log.d(LOG, "insertSetDel");
                        book.setImageResource(plusfarebne);
                        book.setOnClickListener(v -> deleteOCP("deletePrecitat"));

                        Log.d(LOG, response.toString());
                    }

                    else if(response.equals("ok-precitane")) {
                        Log.d(LOG, "insertSetDel");
                        book2.setImageResource(precitanefarebne);
                        book2.setOnClickListener(v -> deleteOCP("deletePrecitane"));

                        Log.d(LOG, response.toString());
                    }
                },
                error -> {
                    Log.d(LOG, error.toString());

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


    public void setMinObsah(){
        obsah2.setMaxLines(5);
        obsah.setEllipsize(TextUtils.TruncateAt.END);
        obsah2.setOnClickListener(v -> {
            setMaxObsah();
        });
    }
    public void setMaxObsah(){
        obsah2.setMaxLines(Integer.MAX_VALUE);
        obsah2.setOnClickListener(v -> {
            setMinObsah();
        });

    }

    @Override
    public void populateLay(JSONArray obj) {

        try {
            JSONObject a = obj.getJSONObject(0);
            //meno editextu.append(a.getString("meno stlpca v tabulke")); - append pridava text na koniec napr ked je ze Nazov: po append je to Nazov: Basne
            nazovKnihy2.setText(a.getString("autor"));
            autor2.setText(a.getString("nazov"));
            obsah2.setText(a.getString("obsah"));
            setMinObsah();
            zaner.setText(a.getString("podkategoria"));
            pocetStran.setText(a.getString("pocet_stran"));
            if (!a.getString("obrazok").equals("null"))
            Picasso.with(DetailKnihy.this).load(a.getString("obrazok")).into(obrazokKnihy);

            Log.d("popDet", a.getString("nazov"));


        }catch (Exception e){
            Log.d("popDet", e.getMessage());
        }

    }

    @Override
    public void populateLayReview(JSONArray obj) {
        //vytvorenie noveho listu
        List<ModelRecenzia> recenzie = new ArrayList<>();
        char up = 'n';
        if (obj!=null){
            for (int i = 0; i < obj.length(); i++) {
                try {

                    //spracuje json a da do listu
                    JSONObject a = obj.getJSONObject(i);
                    try {
                        up = a.getString("up").charAt(0);
                    }catch (Exception e){
                        Log.d(TAG, "populateLayReview: ");
                    }
                    recenzie.add(new ModelRecenzia(

                            a.getString("uid"),
                            a.getString("popis"),
                            a.getString("hodnotenie"),
                            id,
                            a.getString("id_recenzia"),
                            a.getString("obrazok"),
                            a.getString("meno"),
                            a.getString("priezvisko"),
                            up
                    ));
                }catch (Exception e){
                    Log.e("PopulateRec", e.getMessage());
                }

            }

            //posle list do adapteru a nastavi adapter pre recyclerview
            populateRecView2(recenzie);
        }
    }

    public void populateRecView2(List<ModelRecenzia> recenzie){
        recyclerView.setVisibility(View.VISIBLE);
            AdapterReview adapterReview = new AdapterReview(recenzie, DetailKnihy.this);
            // Attach the adapter to the recyclerview to populate items
            recyclerView.setAdapter(adapterReview);

            // Set layout manager to position the items

            recyclerView.setLayoutManager(new LinearLayoutManager(DetailKnihy.this){
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            });
            recyclerView.setItemAnimator(new SlideInLeftAnimator());
            SnapHelper snapHelper = new LinearSnapHelper();
            //snapHelper.attachToRecyclerView(recyclerView);
    }
}