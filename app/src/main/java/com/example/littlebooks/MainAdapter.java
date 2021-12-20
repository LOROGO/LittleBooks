package com.example.littlebooks;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class MainAdapter extends FirebaseRecyclerAdapter<Main, MainAdapter.viewholder> {
    MainActivity context;
    DatabaseReference mdatabase = FirebaseDatabase.getInstance("https://kniznicaprosim-default-rtdb.firebaseio.com/").getReferenceFromUrl("https://kniznicaprosim-default-rtdb.firebaseio.com/").child("Favorite");
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    public MainAdapter(@NonNull FirebaseRecyclerOptions<Main> options, MainActivity context){
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull MainAdapter.viewholder holder, int position, @NonNull Main model){

        holder.nazov.setText(model.getNazov());

        String imageUri = null;
        imageUri = model.getObrazok();
        Picasso.with(context).load(imageUri).into(holder.obrazok);

        holder.nazov.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Obsah.class);
                intent.putExtra("Nazov", getRef(position).getKey());
                context.startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public MainAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_hl_stranka, parent, false);
        return new MainAdapter.viewholder(view);
    }

    class viewholder extends RecyclerView.ViewHolder {
        TextView nazov;
        ImageView obrazok;

        public viewholder(@NonNull View itemView) {
            super(itemView);

            nazov = itemView.findViewById(R.id.nazovKnihy);
            obrazok = itemView.findViewById(R.id.obrazok);
        }
    }
}