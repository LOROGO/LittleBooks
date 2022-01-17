package com.example.littlebooks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.littlebooks.old.MainActivity;
import com.example.littlebooks.ui.main.UserFragment;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static java.security.AccessController.getContext;

public class Settings extends AppCompatActivity {
    FirebaseAuth fAuth;
    RequestQueue requestQueue;
    JSONArray jsonArray;
    Button update;
    EditText meno, priezvisko;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        meno = findViewById(R.id.meno);
        priezvisko = findViewById(R.id.priezvisko);
        update = findViewById(R.id.update);

        fAuth = FirebaseAuth.getInstance();
        String uid = fAuth.getUid();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("connPEP", "ok");
                String menoo = meno.getText().toString().trim();
                String priezviskoo = priezvisko.getText().toString().trim();

                String url = "http://159.223.112.133/user1.php";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        response -> {
                    Log.d("connPP", response);
                    if (response.equals("Row was updated!")){

                        //klavesnica hide
                        InputMethodManager imm =  (InputMethodManager) Settings.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                        Intent intent = new Intent(Settings.this, UserFragment.class);
                        startActivity(intent);
                        finish();
                        startActivity(new Intent(getApplicationContext(), UserFragment.class));

                    }
                        },
                        error ->{
                        Log.d("error", error.toString());
                        }
                ){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("action", "updateUser");
                        params.put("meno", menoo);
                        params.put("priezvisko", priezviskoo);
                        params.put("uid", uid);
                        return params;
                    }
                };

                requestQueue = Volley.newRequestQueue(Settings.this);
                requestQueue.add(stringRequest);

            }
        });



    }
}