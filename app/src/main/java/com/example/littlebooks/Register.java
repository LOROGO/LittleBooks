package com.example.littlebooks;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.littlebooks.old.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    public static final String TAG = "TAG";

    TextView nazov, podnadpis, podnadpis2, alreadyExist;
    EditText email, password1, meno, password2, priezvisko;
    Button button;
    FirebaseAuth fAuth;
    String userID;
    FirebaseUser current_user;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nazov = findViewById(R.id.nazov);
        podnadpis = findViewById(R.id.podnadpis);
        podnadpis2 = findViewById(R.id.podnadpis2);
        email = findViewById(R.id.email);
        password1 = findViewById(R.id.password1);
        password2 = findViewById(R.id.password2);
        meno = findViewById(R.id.meno);
        priezvisko = findViewById(R.id.priezvisko);
        button = findViewById(R.id.button);
        //alreadyExist = findViewById(R.id.alreadyExist);

        fAuth = FirebaseAuth.getInstance();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String emaill = email.getText().toString().trim();
                String passwordd = password1.getText().toString().trim();
                String passworddd = password2.getText().toString().trim();
                final String menoo = meno.getText().toString().trim();
                final String priezviskoo = priezvisko.getText().toString().trim();

                if (TextUtils.isEmpty(emaill)){
                    email.setError("Vyplňte toto pole!");
                    return;
                }

                if (TextUtils.isEmpty(menoo)){
                    meno.setError("Vyplňte toto pole!");
                    return;
                }

                if (TextUtils.isEmpty(priezviskoo)){
                    priezvisko.setError("Vyplňte toto pole!");
                    return;
                }

                if (TextUtils.isEmpty(passwordd)){
                    password1.setError("Vyplňte toto pole!");
                    return;
                }

                if (passwordd.length() < 6){
                    password1.setError("Heslo musí obsahovať minimálne 6 znakov.");
                    return;
                }

                if (!passwordd.equals(passworddd)) {
                    password2.setError("Hesla sa nezhodujú.");
                    return;
                }

                fAuth.createUserWithEmailAndPassword(emaill, passwordd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            current_user = fAuth.getCurrentUser();
                            userID = fAuth.getCurrentUser().getUid();


                            String url = "http://178.62.196.85/user1.php";
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                    response -> {

                                        if (response.equals("ok")){

                                            Intent mainIntent = new Intent(Register.this, MainActivity.class);
                                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(mainIntent);
                                            finish();
                                            startActivity(new Intent(getApplicationContext(), MainActivityT.class));

                                        }
                                        else
                                            Log.d("RegR", response.toString());
                                    },
                                    error -> {
                                        Log.d("RegE", error.toString());

                                    }
                            ){
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError { //berie to udaje z editextov a posiela ich do php
                                    Map<String, String> params = new HashMap<>();
                                    params.put("action", "newUser");
                                    params.put("uid", userID);
                                    params.put("meno", menoo);
                                    params.put("priezvisko", priezviskoo);
                                    params.put("email", emaill);
                                    return params;
                                }
                            };
                            requestQueue = Volley.newRequestQueue(Register.this);
                            requestQueue.add(stringRequest);

                            /*Intent mainIntent = new Intent(Register.this, MainActivity.class);
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(mainIntent);
                            finish();*/

                            startActivity(new Intent(getApplicationContext(), MainActivityT.class));
                        }
                        else {
                            Toast.makeText(Register.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        if (fAuth.getCurrentUser()!=null){
            Intent main = new Intent(getApplicationContext(), MainActivityT.class);
            startActivity(main);
        }


        podnadpis2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Login.class));
                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
            }
        });

        /*alreadyExist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });*/
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
    }
}