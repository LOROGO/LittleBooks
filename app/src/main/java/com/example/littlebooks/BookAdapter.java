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

public class BookAdapter extends FirebaseRecyclerAdapter<Books, BookAdapter.viewholder> {
    BooksActivity context;
    DatabaseReference mdatabase = FirebaseDatabase.getInstance("https://kniznicaprosim-default-rtdb.firebaseio.com/").getReferenceFromUrl("https://kniznicaprosim-default-rtdb.firebaseio.com/").child("Favorite");
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    public BookAdapter(@NonNull FirebaseRecyclerOptions<Books> options, BooksActivity context){
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull BookAdapter.viewholder holder, int position, @NonNull Books model){

        holder.autor.setText(model.getMeno());
        holder.nazov.setText(model.getNazov());
        holder.pocetStran.setText(model.getPocetStran());

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

        mdatabase.child(firebaseAuth.getUid()).child(getRef(position).getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot) {
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();

                try {
                    if (snapshot.getValue().toString().equals("1")) {
                        holder.srdiecko.setBackgroundResource(R.drawable.ic_baseline_favorite_24);
                    }
                    else
                    {
                        holder.srdiecko.setBackgroundResource(R.drawable.ic_favorite_prazdne);
                    }
                }catch (NullPointerException e){
                    holder.srdiecko.setBackgroundResource(R.drawable.ic_favorite_prazdne);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.srdiecko.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mdatabase.child(firebaseAuth.getUid()).child(getRef(position).getKey()).setValue(1).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        holder.srdiecko.setBackgroundResource(R.drawable.ic_baseline_favorite_24);
                    }
                });
            }
        });
    }

    @NonNull
    @Override
    public BookAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_book, parent, false);
        return new BookAdapter.viewholder(view);
    }

    class viewholder extends RecyclerView.ViewHolder {
        TextView nazov, autor, pocetStran, score1;
        ImageView obrazok, srdiecko;

        public viewholder(@NonNull View itemView) {
            super(itemView);

            nazov = itemView.findViewById(R.id.nazovKnihy);
            autor = itemView.findViewById(R.id.autorKnihy);
            obrazok = itemView.findViewById(R.id.obrazok);
        }
    }
}