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
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.gcm.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    RecyclerView recyclerView;
    //pridat adapter a prepojenie knih
    FirebaseAuth fAuth;
    MainAdapter adapter;
    ImageView pozadie;
    TextView test;

    DatabaseReference mbase;
    public List<ModelMainData> mKnihy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pozadie = findViewById(R.id.imageView3);
        drawerLayout = findViewById(R.id.drawer_layout);
        recyclerView = findViewById(R.id.recyclerViewMain);

        test = findViewById(R.id.test);
        Toast.makeText(MainActivity.this, "oncreate", Toast.LENGTH_LONG);


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        mbase = FirebaseDatabase.getInstance("https://kniznicaprosim-default-rtdb.firebaseio.com/").getReferenceFromUrl("https://kniznicaprosim-default-rtdb.firebaseio.com/");


       /* recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        FirebaseRecyclerOptions<Main> options
                = new FirebaseRecyclerOptions.Builder<Main>()
                .setQuery(mbase.child("Books"), Main.class)
                .build();

        adapter = new MainAdapter(options, MainActivity.this);

        recyclerView.setAdapter(adapter);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));*/
        // Lookup the recyclerview in activity layout


        // Initialize contacts

        // Create adapter passing in the sample user data
        try {
            new Async().execute();

        }catch (Exception e){
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }


/*
        fAuth = FirebaseAuth.getInstance();
        if (fAuth.getCurrentUser()==null)logout();*/

    }

    /*public void logout(){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), Register.class));
        finish();
    }*/

    class Async extends AsyncTask<Void, Void, Void> {



        String records = "",error="";
        public List<ModelMainData> knihy;

        @Override

        protected Void doInBackground(Void... voids) {
            knihy = new ArrayList<>();

            try

            {

                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection("jdbc:mysql://db-mysql-fra1-76684-do-user-10334479-0.b.db.ondigitalocean.com:25060/defaultdb", "doadmin", "vhVfELGWlFZ7nzOa");
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT id_kniha, nazov, obrazok FROM kniha limit 30");
                while(resultSet.next()) {

                   // records += resultSet.getString(1) + " " + resultSet.getString(2) + "\n";
                    knihy.add(new ModelMainData(
                            resultSet.getString(1),
                            resultSet.getString(2),
                            resultSet.getString(3))
                    );

                }
            }

            catch(Exception e)
            {
                error = e.toString();
           }

            return null;

        }



        @Override

        protected void onPostExecute(Void aVoid) {
            mKnihy = knihy;
            for (int i = 0; i < knihy.size(); i++) {
                Log.d("knihy", knihy.get(i).toString());
            }
            test.setText(mKnihy.get(1).getNazov());

            // Toast.makeText(MainActivity.this, Integer.toString(knihy.size()),Toast.LENGTH_LONG).show();
            populateRecyclerView();
            super.onPostExecute(aVoid);

        }





    }

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
    public void populateRecyclerView(){
        AdapterProcess adapter = new AdapterProcess(mKnihy, MainActivity.this);
        // Attach the adapter to the recyclerview to populate items
        recyclerView.setAdapter(adapter);
        // Set layout manager to position the items
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
    }
}