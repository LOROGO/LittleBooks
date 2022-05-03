package com.example.littlebooks.old;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.littlebooks.MojeKnihy;
import com.example.littlebooks.R;
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

public class FavoriteAdapter extends FirebaseRecyclerAdapter<FavoriteBook, FavoriteAdapter.viewholder> {
    MojeKnihy context;
    DatabaseReference mdatabase = FirebaseDatabase.getInstance("https://kniznicaprosim-default-rtdb.firebaseio.com/").getReferenceFromUrl("https://kniznicaprosim-default-rtdb.firebaseio.com/").child("Favorite");
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    public FavoriteAdapter(@NonNull FirebaseRecyclerOptions<FavoriteBook> options, MojeKnihy context){
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull FavoriteAdapter.viewholder holder, int position, @NonNull FavoriteBook model){

        holder.autor.setText(model.getMeno());
        holder.nazov.setText(model.getNazov());
        holder.pocetStran.setText(model.getPocetStran());

        String imageUri = null;
        imageUri = model.getObrazok();
        Picasso.with(context).load(imageUri).into(holder.obrazok);

        holder.srdiecko.setBackgroundResource(R.drawable.ic_baseline_favorite_24);

                mdatabase.child(firebaseAuth.getUid()).child(getRef(position).getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot) {
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
                holder.nazov.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, Obsah.class);
                        intent.putExtra("Nazov", getRef(position).getKey());
                        context.startActivity(intent);
                    }
                });
                try {
                    if (snapshot.getValue().toString().equals("1")) {
                        holder.itemView.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        holder.itemView.setVisibility(View.GONE);
                    }
                }catch (NullPointerException e){
                    holder.itemView.setOnClickListener(null);
                    holder.itemView.setVisibility(View.GONE);
                    holder.itemView.getLayoutParams().height = 0;
                    holder.itemView.getLayoutParams().width = 0;
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(context, snapshot.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.srdiecko.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mdatabase.child(firebaseAuth.getUid()).child(getRef(position).getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        holder.srdiecko.setBackgroundResource(R.drawable.ic_favorite_prazdne);
                        holder.itemView.setVisibility(View.GONE);
                        holder.itemView.getLayoutParams().height = 0;
                        holder.itemView.getLayoutParams().width = 0;
                    }
                });
            }
        });
    }

    @NonNull
    @Override
    public FavoriteAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_book, parent, false);
        return new FavoriteAdapter.viewholder(view);
    }

    class viewholder extends RecyclerView.ViewHolder {
        TextView nazov, autor, pocetStran;
        ImageView obrazok, srdiecko;

        public viewholder(@NonNull View itemView) {
            super(itemView);

            nazov = itemView.findViewById(R.id.menoUsera);
            autor = itemView.findViewById(R.id.recenziaPopis);
            obrazok = itemView.findViewById(R.id.pouzivatel);
            pocetStran = itemView.findViewById(R.id.pocetStran);
            srdiecko = itemView.findViewById(R.id.hviezdicky);
        }
    }
}