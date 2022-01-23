package com.example.littlebooks;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.littlebooks.ui.main.UserFragment;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

public class Settings extends AppCompatActivity {
    FirebaseAuth fAuth;
    RequestQueue requestQueue;
    JSONArray jsonArray;
    Button update;
    ImageView logout;
    EditText meno, priezvisko;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        meno = findViewById(R.id.meno);
        priezvisko = findViewById(R.id.priezvisko);
        update = findViewById(R.id.update);
        logout = findViewById(R.id.logout);

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


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                AlertDialog.Builder builder = new AlertDialog.Builder(Settings.this);
                builder.setTitle("Odhlásenie");
                builder.setMessage("Ste si istý, že sa chcete odhlásiť?");
                builder.setPositiveButton("Áno", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        Settings.this.finishAffinity();
                        //System.exit(0);
                    }
                });
                builder.setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });

    }
}