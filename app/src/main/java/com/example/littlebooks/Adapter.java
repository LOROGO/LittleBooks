package com.example.littlebooks;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class Adapter extends
        RecyclerView.Adapter<Adapter.ViewHolder> {
    public List<ModelMainData> mKnihy;
    public Context con;

    public Adapter(List<ModelMainData> knihy, Context context) {
        //1. zavola sa tato metoda, ulozi ten poslany zoznam do lokalnej premennej mKnihy
        mKnihy = knihy;
        Log.d("KnihySize", Integer.toString(getItemCount()));
        con = context;
    }

    @Override
    public Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //vytvorenie viewu idk
        LayoutInflater inflater = LayoutInflater.from(con);
        View knihyView = inflater.inflate(R.layout.single_book_main, parent, false);

        return new Adapter.ViewHolder(knihyView);

    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(Adapter.ViewHolder holder, int position) {

        //
        ModelMainData kniha = mKnihy.get(holder.getAdapterPosition());

        // nastavenie nazvu holder.getAdapterPosition je cisclo itemviewu ktory chceme naplnit datami, ktore zoberieme z mKnihy
        holder.nazovKnihy.setText(mKnihy.get(holder.getAdapterPosition()).getNazov().trim());

        //nastavenie obrazka to split je tam preto lebo v db mame ze napr obrazky/obrazok1.jpg+a kopec bludov za tym takze takto len tie bludy dame prec
        String imageUri = null;
        imageUri = "http:"+mKnihy.get(position).getObrazok().split(".jpg")[0]+".jpg";
        Log.d("knihyObrazok", "Kniha "+imageUri+"\n");

        Picasso.with(con).load(imageUri).into(holder.obrazok);
        Log.d("knihyAdapter", "Kniha "+mKnihy.get(holder.getAdapterPosition()).toString()+"\n");
        
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("id_kniha_jurko", mKnihy.get(holder.getAdapterPosition()).getId());
                Intent i = new Intent(con, DetailKnihy.class);
                i.putExtra("id",mKnihy.get(holder.getAdapterPosition()).getId());
                con.startActivity(i);
            }
        });
        holder.obrazok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(con, DetailKnihy.class);
                i.putExtra("id",mKnihy.get(holder.getAdapterPosition()).getId());
                con.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mKnihy.size();
    }

    // Returns the total count of items in the list


    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
    //2. zadefinovanie premennych z layoutu
        public TextView nazovKnihy;
        public ImageView obrazok;


        public ViewHolder(View itemView) {
        // ulozenie tych veci z layoutu u know idk
            super(itemView);
            nazovKnihy = (TextView) itemView.findViewById(R.id.menoPouzivatela);
            obrazok = itemView.findViewById(R.id.pouzivatel);
        }
    }
}