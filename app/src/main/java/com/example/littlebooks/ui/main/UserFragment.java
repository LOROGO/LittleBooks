package com.example.littlebooks.ui.main;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.littlebooks.AdapterOblubene;
import com.example.littlebooks.BackgroundTask;
import com.example.littlebooks.ModelMainDataFavourite;
import com.example.littlebooks.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

public class UserFragment extends Fragment implements BackgroundTask.ApiCallback{

    private UserViewModel userViewModel;
    private UserViewModel mViewModel;

    TextView oblubene, chcemPrecitat, precitane;

    FirebaseAuth fAuth;
    RequestQueue requestQueue;
    JSONArray jsonArray;
    TextView priezviskoMeno;
    RecyclerView recyclerview;
    View root;

    DatabaseReference mbase;
    public List<ModelMainDataFavourite> mKnihy;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        root = inflater.inflate(R.layout.fragment_user, container, false);


        Toast.makeText(getActivity(), "oncreate", Toast.LENGTH_LONG).show();

        recyclerview =root.findViewById(R.id.recyclerView);
        oblubene = root.findViewById(R.id.oblubene);
        chcemPrecitat = root.findViewById(R.id.chcemPrecitat);
        precitane = root.findViewById(R.id.precitane);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        mbase = FirebaseDatabase.getInstance("https://kniznicaprosim-default-rtdb.firebaseio.com/").getReferenceFromUrl("https://kniznicaprosim-default-rtdb.firebaseio.com/");

        priezviskoMeno = root.findViewById(R.id.priezviskoMeno);
        fAuth = FirebaseAuth.getInstance();
        String uid = fAuth.getUid();

        String url = "http://159.223.112.133/user1.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {                                               //spusti sa ak appka dostane odpoved v JSONE od php/db
                    Log.d("RegR", response.toString());

                    try {
                        jsonArray = new JSONArray(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JSONObject a = null;
                    try {
                        a = jsonArray.getJSONObject(0);
                        Log.d("RegR", "cele: "+a.toString());
                        Log.d("RegR1", "meno: " + a.getString("meno"));
                        try {
                            Log.d("RegR", a.getString("meno"));
                            priezviskoMeno.setText(a.getString("meno"));
                            priezviskoMeno.append(" "+a.getString("priezvisko"));
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
                params.put("uid", uid);
                return params;
            }
        };
        requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

        BackgroundTask backgroundTask = new BackgroundTask(5);
        backgroundTask.table = "kniha";
        backgroundTask.action = "select";
        backgroundTask.scr = "4";
        backgroundTask.php = "get_knihy4";
        backgroundTask.uid = uid;
        backgroundTask.setApiCallback(UserFragment.this);
        backgroundTask.execute();


        oblubene.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackgroundTask backgroundTask = new BackgroundTask(5);
                backgroundTask.table = "kniha";
                backgroundTask.action = "select";
                backgroundTask.scr = "4";
                backgroundTask.php = "get_knihy4";
                backgroundTask.uid = uid;
                //v.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.enter_from_right));
                backgroundTask.setApiCallback(UserFragment.this);
                backgroundTask.execute();
            }
        });



        chcemPrecitat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mKnihy.clear();
                BackgroundTask backgroundTask1 = new BackgroundTask(5);
                backgroundTask1.table = "kniha";
                backgroundTask1.action = "select";
                backgroundTask1.scr = "6";
                backgroundTask1.php = "get_knihy4";
                backgroundTask1.uid = uid;
                //v.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.enter_from_right));
                backgroundTask1.setApiCallback(UserFragment.this);
                backgroundTask1.execute();
            }
        });



        precitane.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mKnihy.clear();
                BackgroundTask backgroundTask2 = new BackgroundTask(5);
                backgroundTask2.table = "kniha";
                backgroundTask2.action = "select";
                backgroundTask2.scr = "5";
                backgroundTask2.php = "get_knihy4";
                backgroundTask2.uid = uid;
                //v.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.enter_from_right));
                backgroundTask2.setApiCallback(UserFragment.this);
                backgroundTask2.execute();
            }

        });

        return root;
    }


    @Override
    public void populateLay(JSONArray obj) {
        //vytvorenie noveho listu
        Log.d("UserFrJ", "populateLay");

        List<ModelMainDataFavourite> knihy = new ArrayList<>();
        if (obj!=null){
            for (int i = 0; i < obj.length(); i++) {
                try {
                    //spracuje json a da do listu
                    JSONObject a = obj.getJSONObject(i);
                    knihy.add(new ModelMainDataFavourite(
                            a.getString("id_kniha"),
                            a.getString("nazov"),
                            a.getString("obrazok"),
                            a.getString("autor")
                    ));
                }catch (Exception e){
                    Log.e("PopulateRec", e.getMessage());
                }

            }
            mKnihy = knihy;
            //posle list do adapteru a nastavi adapter pre recyclerview
            populateRecView(knihy, root);
        }
    }


    public void populateRecView(List<ModelMainDataFavourite> knihy, View root){             //knihy z db da do layoutu
        RecyclerView recyclerView = root.findViewById(R.id.recyclerView);
        AdapterOblubene adapterOblubene = new AdapterOblubene(knihy, getActivity(), "");
            // Attach the adapter to the recyclerview to populate items
            recyclerView.setAdapter(adapterOblubene);
            // Set layout manager to position the items

            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setItemAnimator(new SlideInLeftAnimator());
            recyclerView.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.enter_from_right));
            SnapHelper snapHelper = new LinearSnapHelper();
            //snapHelper.attachToRecyclerView(recyclerView);
    }

}