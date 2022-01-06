package com.example.littlebooks.ui.dashboard;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.example.littlebooks.Account;
import com.example.littlebooks.Adapter;
import com.example.littlebooks.BackgroundTask;
import com.example.littlebooks.old.BooksActivity;
import com.example.littlebooks.old.MainActivity;
import com.example.littlebooks.old.MainAdapter;
import com.example.littlebooks.ModelMainData;
import com.example.littlebooks.MojeKnihy;
import com.example.littlebooks.NewBook;
import com.example.littlebooks.R;
import com.google.firebase.auth.FirebaseAuth;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONObject;

public class DashboardFragment extends Fragment implements BackgroundTask.ApiCallback {

    private DashboardViewModel dashboardViewModel;
    DrawerLayout drawerLayout;
    RecyclerView recyclerView;
    //pridat adapter a prepojenie knih
    FirebaseAuth fAuth;
    MainAdapter adapter;
    ImageView pozadie;


    DatabaseReference mbase;
    public List<ModelMainData> mKnihy;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        //pozadie = root.findViewById(R.id.imageView3);
        drawerLayout = root.findViewById(R.id.drawer_layout);
        recyclerView = root.findViewById(R.id.recyclerViewMain);

        Toast.makeText(getActivity(), "oncreate", Toast.LENGTH_LONG);


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
            //new Async().execute();

        }catch (Exception e){
            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
        }
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        return root;
    }

    @Override
    public void populateLay(JSONArray obj) {
        List<ModelMainData> knihy = new ArrayList<>();
        if (obj!=null){
        for (int i = 0; i < obj.length(); i++) {
            try {
                JSONObject a = obj.getJSONObject(i);
                knihy.add(new ModelMainData(

                        a.getString("id_kniha"),
                        a.getString("nazov"),
                        a.getString("obrazok")
                        ));
            }catch (Exception e){
                Log.e("PopulateRec", e.getMessage());
            }

        }
        Adapter adapter = new Adapter(knihy, getActivity());
        // Attach the adapter to the recyclerview to populate items
        recyclerView.setAdapter(adapter);
        // Set layout manager to position the items
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setItemAnimator(new SlideInLeftAnimator());
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

    }}

    public class Async extends AsyncTask<Void, Void, Void> {



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
            if (!knihy.isEmpty()) {
                mKnihy = knihy;
                for (int i = 0; i < knihy.size(); i++) {
                    Log.d("knihy", knihy.get(i).toString());
                }

                // Toast.makeText(MainActivity.this, Integer.toString(knihy.size()),Toast.LENGTH_LONG).show();
                populateRecyclerView();
            }else Log.e("AsyncPostEx", error);
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
        MainActivity.redirectActivity(getActivity(), BooksActivity.class);
    }

    public void ClickAccount(View view){
        MainActivity.redirectActivity(getActivity(), Account.class);
    }

    public void ClickNewBook(View view){
        startActivity(new Intent(getActivity(), NewBook.class));
    }

    public void ClickLogout(View view){
        logout(getActivity());
    }

    public void ClickMojeKnihy(View view){
        MainActivity.redirectActivity(getActivity(), MojeKnihy.class);
    }

    public void ClickDomov(View view){
        //recreate();
    }


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



    public static void redirectActivity(Activity activity, Class aClass) {
        Intent intent = new Intent(activity, aClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }
    public void populateRecyclerView(){
        Adapter adapter = new Adapter(mKnihy, getActivity());
        // Attach the adapter to the recyclerview to populate items
        recyclerView.setAdapter(adapter);
        // Set layout manager to position the items
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setItemAnimator(new SlideInLeftAnimator());
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

    }
}