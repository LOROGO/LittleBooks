package com.example.littlebooks;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.littlebooks.old.BooksActivity;
import com.example.littlebooks.old.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class NewBook extends AppCompatActivity {
    DrawerLayout drawerLayout;
    EditText meno, nazov, pocetStran, obsah;
    Button button;
    ImageButton obrazok;
    private static final int Gallery_Code = 1;
    Uri imageUrl = null;
    ProgressDialog progressDialog;

    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference root = db.getReference().child("Books");
    FirebaseStorage mStorage = FirebaseStorage.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_book);

        drawerLayout = findViewById(R.id.drawer_layout);
        meno = findViewById(R.id.meno);
        nazov = findViewById(R.id.nazov);
        button = findViewById(R.id.button);
        obrazok = findViewById(R.id.pouzivatel);
        pocetStran = findViewById(R.id.pocetStran);
        obsah = findViewById(R.id.obsah);
        progressDialog = new ProgressDialog(this);

        obrazok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, Gallery_Code);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Gallery_Code && resultCode==RESULT_OK){
            imageUrl = data.getData();
            obrazok.setImageURI(imageUrl);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Meno = meno.getText().toString();
                String Nazov = nazov.getText().toString();
                String PocetStran = pocetStran.getText().toString();
                String Obsah = obsah.getText().toString();

                if (TextUtils.isEmpty(Meno)){
                    meno.setError("Vyplňte toto pole!");
                    return;
                }
                if (TextUtils.isEmpty(Nazov)){
                    nazov.setError("Vyplňte toto pole!");
                    return;
                }
                if (TextUtils.isEmpty(PocetStran)){
                    pocetStran.setError("Vyplňte toto pole!");
                    return;
                }
                if (TextUtils.isEmpty(Obsah)){
                    obsah.setError("Vyplňte toto pole!");
                    return;
                }

                if (!(Meno.isEmpty() && Nazov.isEmpty() && PocetStran.isEmpty() && imageUrl!=null)){
                    progressDialog.setTitle("Uploading...");
                    progressDialog.show();

                    StorageReference filepath = mStorage.getReference().child("imagePost").child(imageUrl.getLastPathSegment());
                    filepath.putFile(imageUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    String t = task.getResult().toString();

                                    DatabaseReference newPost = root.push();

                                    newPost.child("meno").setValue(Meno);
                                    newPost.child("nazov").setValue(Nazov);
                                    newPost.child("pocetStran").setValue(PocetStran);
                                    newPost.child("obrazok").setValue(task.getResult().toString());
                                    newPost.child("obsah").setValue(Obsah);
                                    progressDialog.dismiss();
                                    Toast.makeText(NewBook.this, "Kniha bola úspešne vytvorená", Toast.LENGTH_SHORT).show();

                                }
                            });
                        }
                    });
                }
            }
        });

    }

    public void ClickMenu(View view){
        MainActivity.openDrawer(drawerLayout);
    }

    public void ClickLogo(View view){
        MainActivity.closeDrawer(drawerLayout);
    }

    public void ClickBooks(View view){
        MainActivity.redirectActivity(this, BooksActivity.class);
    }

    public void ClickAccount(View view){
        MainActivity.redirectActivity(this, Account.class);
    }

    public void ClickNewBook(View view){
        recreate();
    }

    public void ClickLogout(View view){
        MainActivity.logout(this);
    }

    public void ClickMojeKnihy(View view){MainActivity.redirectActivity(this, MojeKnihy.class);}

    public void ClickDomov(View view){MainActivity.redirectActivity(this, MainActivity.class);}


    @Override
    protected void onPause() {
        super.onPause();
        MainActivity.closeDrawer(drawerLayout);
    }
}