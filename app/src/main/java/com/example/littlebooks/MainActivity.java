package com.example.littlebooks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    RecyclerView recyclerView;
    //pridat adapter a prepojenie knih
    FirebaseAuth fAuth;
    MainAdapter adapter;

    DatabaseReference mbase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        recyclerView = findViewById(R.id.recyclerView);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        mbase = FirebaseDatabase.getInstance("https://kniznicaprosim-default-rtdb.firebaseio.com/").getReferenceFromUrl("https://kniznicaprosim-default-rtdb.firebaseio.com/");


        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<Main> options
                = new FirebaseRecyclerOptions.Builder<Main>()
                .setQuery(mbase.child("Books"), Main.class)
                .build();

        adapter = new MainAdapter(options, MainActivity.this);

        recyclerView.setAdapter(adapter);

/*
        fAuth = FirebaseAuth.getInstance();
        if (fAuth.getCurrentUser()==null)logout();*/
    }

    /*public void logout(){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), Register.class));
        finish();
    }*/

    public void ClickMenu(View view){
        openDrawer(drawerLayout);
    }

    public static void openDrawer(DrawerLayout drawerLayout){
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void ClickLogo(View view){
        closeDrawer(drawerLayout);
    }

    public static void closeDrawer(DrawerLayout drawerLayout) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }


    public void ClickBooks(View view){
        MainActivity.redirectActivity(this, BooksActivity.class);
    }

    public void ClickAccount(View view){
        MainActivity.redirectActivity(this, Account.class);
    }

    public void ClickNewBook(View view){
        startActivity(new Intent(this, NewBook.class));
    }

    public void ClickLogout(View view){
        logout(this);
    }

    public void ClickMojeKnihy(View view){
        MainActivity.redirectActivity(this, MojeKnihy.class);
    }

    public void ClickDomov(View view){recreate();}


    public static void logout(final Activity activity){
        FirebaseAuth.getInstance().signOut();
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Odhlásenie");
        builder.setMessage("Ste si istý, že sa chcete odhlásiť?");
        builder.setPositiveButton("Áno", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth.getInstance().signOut();
                activity.finishAffinity();
                System.exit(0);
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

    @Override
    protected void onPause(){
        super.onPause();
        closeDrawer(drawerLayout);
    }

    public static void redirectActivity(Activity activity, Class aClass) {
        Intent intent = new Intent(activity, aClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }
}