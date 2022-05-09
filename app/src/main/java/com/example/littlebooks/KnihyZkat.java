package com.example.littlebooks;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

public class KnihyZkat extends AppCompatActivity implements BackgroundTask.ApiCallback {

    RecyclerView recyclerView, recyclerViewPod;
    View root;
    JSONArray jsonArray;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knihy_zkat);
        recyclerView = findViewById(R.id.recyclerViewMain);
        recyclerViewPod = findViewById(R.id.recyclerViewMain1);

        Intent intent = getIntent();
        String kategoria = intent.getStringExtra("kategoria");
        BackgroundTask bs = new BackgroundTask(3);
        bs.table = "kniha";
        bs.action = "select";
        bs.scr = "3";
        bs.php = "get_knihy4";
        bs.search = kategoria;
        bs.setApiCallback(this);
        String a = String.valueOf(bs.execute());


        String url = "http://178.62.196.85/get_knihy4.php?action=select&table=kniha&scr=9&kategoria="+kategoria;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {                                               //spusti sa ak appka dostane odpoved v JSONE od php/db
                    Log.d("RegRespons", response.toString());

                    try {
                        jsonArray = new JSONArray(response);
                        populateLay1(jsonArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Log.d("RegE", error.toString());

                }
        ){

        };
        requestQueue = Volley.newRequestQueue(KnihyZkat.this);
        requestQueue.add(stringRequest);

    }

    @Override
    public void populateLay(JSONArray obj) {
        List<ModelMainData> knihy = new ArrayList<>();
        if (obj != null) {
            for (int i = 0; i < obj.length(); i++) {
                try {
                    //spracuje json a da do listu
                    JSONObject a = obj.getJSONObject(i);
                    knihy.add(new ModelMainData(
                            a.getString("id_kniha"),
                            a.getString("nazov"),
                            a.getString("obrazok")
                    ));
                } catch (Exception e) {
                    Log.e("PopulateRec", e.getMessage());
                }

            }
            //posle list do adapteru a nastavi adapter pre recyclerview
            populateRecView(knihy, root);
        }
    }


    public void populateLay1(JSONArray obj) {
        List<ModelMainPodkat> podkategorie = new ArrayList<>();
        if (obj != null) {
            for (int i = 0; i < obj.length(); i++) {
                try {
                    //spracuje json a da do listu
                    JSONObject a = obj.getJSONObject(i);
                    podkategorie.add(new ModelMainPodkat(
                            a.getString("podkategoria")
                    ));
                } catch (Exception e) {
                    Log.e("PopulateRec", e.getMessage());
                }

            }
            //posle list do adapteru a nastavi adapter pre recyclerview
            populateRecView2(podkategorie, root);
        }
    }

    public void populateRecView(List<ModelMainData> knihy, View root) {

        AdapterBooks adapterBooks = new AdapterBooks(knihy, KnihyZkat.this, "search");
        // Attach the adapter to the recyclerview to populate items
        recyclerView.setAdapter(adapterBooks);
        // Set layout manager to position the items

        recyclerView.setLayoutManager(new GridLayoutManager(KnihyZkat.this, 3));
        recyclerView.setItemAnimator(new SlideInLeftAnimator());
        SnapHelper snapHelper = new LinearSnapHelper();
        //snapHelper.attachToRecyclerView(recyclerView);
    }

    public void populateRecView2(List<ModelMainPodkat> podkategorie, View root) {

        AdapterPodkat adapterPodkat = new AdapterPodkat(podkategorie, KnihyZkat.this,"");
        // Attach the adapter to the recyclerview to populate items
        recyclerViewPod.setAdapter(adapterPodkat);
        // Set layout manager to position the items

        recyclerViewPod.setLayoutManager(new LinearLayoutManager(KnihyZkat.this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewPod.setItemAnimator(new SlideInLeftAnimator());
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
    }


    public void searchRequest(String text) {
        Log.d("url", text);
        BackgroundTask bs = new BackgroundTask(3);
        bs.table = "kniha";
        bs.action = "select";
        bs.scr = "3";
        bs.php = "get_knihy4";
        bs.search = text;
        bs.setApiCallback(this);
        String a = String.valueOf(bs.execute());
    }
}