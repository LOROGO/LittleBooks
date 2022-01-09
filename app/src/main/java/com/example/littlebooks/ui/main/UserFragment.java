package com.example.littlebooks.ui.main;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.littlebooks.ModelMainData;
import com.example.littlebooks.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserFragment extends Fragment{

    private UserViewModel userViewModel;
    private UserViewModel mViewModel;

    FirebaseAuth fAuth;
    RequestQueue requestQueue;
    JSONArray jsonArray;
    TextView userName;


    DatabaseReference mbase;
    public List<ModelMainData> mKnihy;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        View root = inflater.inflate(R.layout.fragment_user, container, false);



        Toast.makeText(getActivity(), "oncreate", Toast.LENGTH_LONG);


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        mbase = FirebaseDatabase.getInstance("https://kniznicaprosim-default-rtdb.firebaseio.com/").getReferenceFromUrl("https://kniznicaprosim-default-rtdb.firebaseio.com/");

        userName = root.findViewById(R.id.userName);
        fAuth = FirebaseAuth.getInstance();
        String uid = fAuth.getUid();



        String url = "http://159.223.112.133/user1.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d("RegR", response.toString());

                    try {
                        jsonArray = new JSONArray(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JSONObject a = null;
                    try {
                        a = jsonArray.getJSONObject(0);
                        Log.d("RegR", a.toString());
                        try {
                            Log.d("RegR", a.getString("meno"));
                            userName.setText(a.getString("meno"));
                            userName.append(" "+a.getString("priezvisko"));
                        } catch (JSONException e) {
                            e.printStackTrace();
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
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("action", "selectUser");
                params.put("uid", uid);
                return params;
            }
        };
        requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);


        return root;
    }



}