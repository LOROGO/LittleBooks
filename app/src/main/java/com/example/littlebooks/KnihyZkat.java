package com.example.littlebooks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.content.Intent;
import android.os.Bundle;
import android.transition.Scene;
import android.transition.Transition;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.littlebooks.ui.home.HomeViewModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

public class KnihyZkat extends AppCompatActivity implements BackgroundTask.ApiCallback {

    private HomeViewModel homeViewModel;
    DrawerLayout drawerLayout;
    RecyclerView recyclerView;
    EditText searchBar;
    Scene sceneMain;
    static Scene sceneSearch;
    Scene currentScene;
    ImageButton back;
    Transition mainTrans;
    Transition mainTrans2;          //prechody
    View root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knihy_zkat);
        recyclerView = findViewById(R.id.recyclerViewMain);
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
    }
