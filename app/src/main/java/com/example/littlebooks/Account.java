package com.example.littlebooks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.littlebooks.old.BooksActivity;
import com.example.littlebooks.old.MainActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Account extends AppCompatActivity {
    DrawerLayout drawerLayout;
    TextView meno, email, verifyMsg;
    Button resendCode, testB;

    DatabaseReference mUserDatabase;
    FirebaseUser mCurrentUser;
    FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        drawerLayout = findViewById(R.id.drawer_layout);
        meno = findViewById(R.id.meno);
        email = findViewById(R.id.email);
        resendCode = findViewById(R.id.resendCode);
        verifyMsg = findViewById(R.id.verifyMsg);


        fAuth = FirebaseAuth.getInstance();

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = mCurrentUser.getUid();
        mUserDatabase = FirebaseDatabase.getInstance("https://kniznicaprosim-default-rtdb.firebaseio.com/").getReferenceFromUrl("https://kniznicaprosim-default-rtdb.firebaseio.com/").child("Users").child(current_uid);

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("name").getValue().toString();
                String mail = snapshot.child("email").getValue().toString();

                meno.setText(name);
                email.setText(mail);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        FirebaseUser user = fAuth.getCurrentUser();

        if (!user.isEmailVerified()){
            verifyMsg.setVisibility(View.VISIBLE);
            resendCode.setVisibility(View.VISIBLE);

            resendCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(Account.this, "Email na verifikáciu bol zaslaný.", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(v.getContext(), "Email nebol zaslaný" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        } else resendCode.setVisibility(View.INVISIBLE);
    }

    public void ClickMenu(View view){
        MainActivity.openDrawer(drawerLayout);
    }

    public void ClickLogo(View view){
        MainActivity.closeDrawer(drawerLayout);
    }

    public void ClickMojeKnihy(View view){MainActivity.redirectActivity(this, MojeKnihy.class);}

    public void ClickBooks(View view){
        MainActivity.redirectActivity(this, BooksActivity.class);
    }

    public void ClickAccount(View view){
        recreate();
    }

    public void ClickNewBook(View view){
        MainActivity.redirectActivity(this, NewBook.class);
    }

    public void ClickLogout(View view){
        MainActivity.logout(this);
    }

    public void ClickDomov(View view){MainActivity.redirectActivity(this, MainActivity.class);}


    @Override
    protected void onPause() {
        super.onPause();
        MainActivity.closeDrawer(drawerLayout);
    }
}
