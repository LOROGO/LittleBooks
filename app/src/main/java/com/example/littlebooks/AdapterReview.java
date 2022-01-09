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

public class AdapterReview extends RecyclerView.Adapter<AdapterReview.ViewHolder> {
    public List<ModelMainData2> mReview;
    public Context con;

    public AdapterReview(List<ModelMainData2> recenzie, Context context) {
        //1. zavola sa tato metoda, ulozi ten poslany zoznam do lokalnej premennej mReview
        mReview = recenzie;
        Log.d("recenzieSize", Integer.toString(getItemCount()));
        con = context;
    }

    @Override
    public AdapterReview.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //vytvorenie viewu idk
        LayoutInflater inflater = LayoutInflater.from(con);
        View recenzieView = inflater.inflate(R.layout.single_review, parent, false);

        return new AdapterReview.ViewHolder(recenzieView);

    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(AdapterReview.ViewHolder holder, int position) {

        //
        ModelMainData2 recenzia = mReview.get(holder.getAdapterPosition());

        // nastavenie nazvu holder.getAdapterPosition je cisclo itemviewu ktory chceme naplnit datami, ktore zoberieme z mReview
        holder.menoUsera.setText(mReview.get(holder.getAdapterPosition()).getMeno().trim());
        holder.recenziaPopis.setText(mReview.get(holder.getAdapterPosition()).getPopis().trim());

        //nastavenie obrazka to split je tam preto lebo v db mame ze napr obrazky/obrazok1.jpg+a kopec bludov za tym takze takto len tie bludy dame prec
        String imageUri = null;
        imageUri = mReview.get(position).getObrazok();
        Log.d("recenzieObrazok", "Kniha "+imageUri+"\n");

        Picasso.with(con).load(imageUri).into(holder.pouzivatel);
        Log.d("recenzieAdapter", "Kniha "+mReview.get(holder.getAdapterPosition()).toString()+"\n");

    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mReview.size();
    }


    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        //2. zadefinovanie premennych z layoutu
        public TextView recenziaPopis, menoUsera;
        public ImageView hviezdicky, pouzivatel;


        public ViewHolder(View itemView) {
            // ulozenie tych veci z layoutu u know idk
            super(itemView);
            recenziaPopis = (TextView) itemView.findViewById(R.id.recenziaPopis);
            hviezdicky = itemView.findViewById(R.id.hviezdicky);
            menoUsera = itemView.findViewById(R.id.menoUsera);
            pouzivatel = itemView.findViewById(R.id.pouzivatel);

        }
    }
}