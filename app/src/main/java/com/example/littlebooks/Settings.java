package com.example.littlebooks;

import static java.security.AccessController.getContext;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.littlebooks.ui.home.HomeFragment;
import com.example.littlebooks.ui.main.UserFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Settings extends AppCompatActivity {
    FirebaseAuth fAuth;
    RequestQueue requestQueue;
    JSONArray jsonArray;
    Button update;
    ImageView logout;
    CircleImageView user;
    EditText meno, priezvisko;

    String uid;
    String menoP;
    String priezviskoP;
    String obrazok;
    String userP;

    private Uri imageUri;
    private static final int PICK_IMAGE = 100;
    String TAG = "Setting1L";
    FirebaseStorage mStorage = FirebaseStorage.getInstance("gs://kniznicaprosim.appspot.com");
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        meno = findViewById(R.id.meno);
        priezvisko = findViewById(R.id.priezvisko);
        update = findViewById(R.id.update);
        logout = findViewById(R.id.logout);
        user = findViewById(R.id.user);
        progressDialog = new ProgressDialog(this);

        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        fAuth = FirebaseAuth.getInstance();
        uid = fAuth.getUid();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null){
            menoP = bundle.getString("Meno");
            priezviskoP = bundle.getString("Priezvisko");
            userP = bundle.getString("Obrazok");
            meno.setText(menoP);
            priezvisko.setText(priezviskoP);
            Picasso.with(this).load(Uri.parse(userP)).into(user);
            progressDialog.dismiss();
        }

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("connPEP", "ok");
                String menoo = meno.getText().toString().trim();
                String priezviskoo = priezvisko.getText().toString().trim();

                if(menoo.equals(menoP) && priezviskoo.equals(priezviskoP) && obrazok.isEmpty()){
                    /*Intent intent = new Intent(Settings.this, UserFragment.class);
                    startActivity(intent);
                    finish();*/
                    startActivity(new Intent(getApplicationContext(), UserFragment.class));
                }

                String url = "http://178.62.196.85/user1.php";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        response -> {
                    Log.d("connPP", response);
                    if (response.equals("Row was updated!")){

                        //klavesnica hide
                        InputMethodManager imm =  (InputMethodManager) Settings.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                        /*Intent intent = new Intent(Settings.this, UserFragment.class);
                        startActivity(intent);
                        finish();*/
                        startActivity(new Intent(getApplicationContext(), UserFragment.class));

                    }
                        },
                        error ->{
                        Log.d("error", error.toString());
                        }
                ){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {     //udaje poslem do nasej db
                        Map<String, String> params = new HashMap<>();
                        params.put("action", "updateUser");
                        params.put("meno", menoo);
                        params.put("priezvisko", priezviskoo);
                        params.put("obrazok", obrazok);
                        Log.d(TAG, "overenie: "+obrazok);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data){      //po skonceni obrazka
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();
            user.setImageURI(imageUri);

            final StorageReference imgPath = mStorage.getReference().child(uid);

            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            imgPath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imgPath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                        @Override
                        public void onSuccess(final Uri uri) {
                            Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {       //ziskam adresu obrazka z firebase
                                    obrazok = task.getResult().toString();
                                    Log.d(TAG, "onFailure: "+obrazok);
                                    progressDialog.dismiss();
                                }
                            });
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "onFailure: "+e.getMessage());
                }
            });
        }
    }


   /* @Override
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

            Intent intent = new Intent(Settings.this, UserFragment.class);
            intent.putExtra("viewPagerPos", 1);
            startActivity(intent);
        }
    }
    public void cropStart(){
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1,1)
                .start(this);
    }*/
}