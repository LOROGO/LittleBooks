package com.example.littlebooks.ui.dashboard;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.ToneGenerator;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
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
import com.example.littlebooks.Account;
import com.example.littlebooks.AdapterBooks;
import com.example.littlebooks.BackgroundTask;
import com.example.littlebooks.ModelMainData;
import com.example.littlebooks.MojeKnihy;
import com.example.littlebooks.NewBook;
import com.example.littlebooks.R;
import com.example.littlebooks.old.BooksActivity;
import com.example.littlebooks.old.MainActivity;
import com.example.littlebooks.old.MainAdapter;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

public class DashboardFragment extends Fragment implements BackgroundTask.ApiCallback {

    private DashboardViewModel dashboardViewModel;
    DrawerLayout drawerLayout;
    RecyclerView recyclerView;
    //pridat adapter a prepojenie knih
    FirebaseAuth fAuth;
    MainAdapter adapter;
    ImageView pozadie;

    private SurfaceView surfaceView;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    //This class provides methods to play DTMF tones
    private ToneGenerator toneGen1;
    private TextView barcodeText;
    private String barcodeData;


    DatabaseReference mbase;
    public List<ModelMainData> mKnihy;
    RequestQueue requestQueue;

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


        surfaceView = root.findViewById(R.id.surface_view);
        barcodeText = root.findViewById(R.id.barcode_text);
        initialiseDetectorsAndSources();

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

    private void initialiseDetectorsAndSources() {

        //Toast.makeText(getApplicationContext(), "Barcode scanner started", Toast.LENGTH_SHORT).show();

        barcodeDetector = new BarcodeDetector.Builder(requireContext())
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(requireContext(), barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(getActivity(), new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                // Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {


                    barcodeText.post(new Runnable() {

                        @Override
                        public void run() {

                            if (barcodes.valueAt(0).email != null) {
                                barcodeText.removeCallbacks(null);
                                barcodeData = barcodes.valueAt(0).email.address;
                                barcodeText.setText(barcodeData);
                                toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
                                String url = "http://159.223.112.133/get_knihy4.php?action=checkISBN&isbn="+barcodeData;
                                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                                        response -> {


                                                Log.d("RegR", response.toString());
                                        },
                                        error -> {
                                            Log.d("RegE", error.toString());

                                        }
                                ){
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError { //berie to udaje z editextov a posiela ich do php
                                        Map<String, String> params = new HashMap<>();

                                        return params;
                                    }
                                };
                                requestQueue = Volley.newRequestQueue(getContext());
                                requestQueue.add(stringRequest);
                            } else {

                                barcodeData = barcodes.valueAt(0).displayValue;
                                String url = "http://159.223.112.133/get_knihy4.php?action=checkISBN&isbn="+barcodeData;
                                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                                        response -> {
                                                Log.d("RegR", response.toString());
                                            try {
                                                JSONArray array = new JSONArray(response);
                                                JSONObject object = array.getJSONObject(0);
                                                Log.d("RegR", object.getString("id_kniha"));
                                            } catch (JSONException e) {
                                                Log.d("RegR", e.getMessage());
                                            }

                                        },
                                        error -> {
                                            Log.d("RegE", error.toString());

                                        }
                                ){
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError { //berie to udaje z editextov a posiela ich do php
                                        Map<String, String> params = new HashMap<>();
                                        params.put("action", "checkISBN");
                                        params.put("isbn", barcodeData);
                                        Log.d("Dashboard", barcodeData);
                                        return params;
                                    }
                                };
                                requestQueue = Volley.newRequestQueue(getContext());
                                requestQueue.add(stringRequest);
                                barcodeText.setText(barcodeData);

                            }
                        }
                    });

                }
            }
        });
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
        AdapterBooks adapterBooks = new AdapterBooks(knihy, getActivity(), "");
        // Attach the adapter to the recyclerview to populate items
        recyclerView.setAdapter(adapterBooks);
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
        AdapterBooks adapterBooks = new AdapterBooks(mKnihy, getActivity(),"");
        // Attach the adapter to the recyclerview to populate items
        recyclerView.setAdapter(adapterBooks);
        // Set layout manager to position the items
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setItemAnimator(new SlideInLeftAnimator());
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

    }
}