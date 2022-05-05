package com.example.littlebooks;

import static java.security.AccessController.getContext;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.littlebooks.ui.home.HomeFragment;
import com.example.littlebooks.ui.main.UserFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Settings extends AppCompatActivity {
    FirebaseAuth fAuth;
    RequestQueue requestQueue;
    JSONArray jsonArray;
    Button update;
    ImageView logout, user;
    EditText meno, priezvisko;
    private Uri imageUri;
    private static final int PICK_IMAGE = 100;
    FirebaseStorage mStorage = FirebaseStorage.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        meno = findViewById(R.id.meno);
        priezvisko = findViewById(R.id.priezvisko);
        update = findViewById(R.id.update);
        logout = findViewById(R.id.logout);
        user = findViewById(R.id.user);

        fAuth = FirebaseAuth.getInstance();
        String uid = fAuth.getUid();


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("connPEP", "ok");
                String menoo = meno.getText().toString().trim();
                String priezviskoo = priezvisko.getText().toString().trim();

                String url = "http://178.62.196.85/user1.php";
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

        //po kliknuti sa mi spusti cropImage - galeria
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();
            user.setImageURI(imageUri);
        }
    }

/*
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                imageUrl = result.getUri();
                user.setImageURI(imageUrl);
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }

            final StorageReference imgPath = mStorage.getReference().child("ImagePost");

            imgPath.putFile(imageUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imgPath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(final Uri uri) {


                        }
                    });
                }
            });

            *//*Intent intent = new Intent(Settings.this, UserFragment.class);
            intent.putExtra("viewPagerPos", 1);
            startActivity(intent);*//*
        }
    }
    public void cropStart(){
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1,1)
                .start(this);
    }*/
}