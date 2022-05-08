package com.example.littlebooks;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterPodkat extends RecyclerView.Adapter<AdapterPodkat.ViewHolder> {             //naplnenie jedneho riadku recycler view a uloženie dat do recycler view, nie vypísanie
    public List<ModelMainPodkat> podkategorie;
    public Context con;
    public String scena;

    public AdapterPodkat(List<ModelMainPodkat> oblubene, Context context, String scena) {
        //1. zavola sa tato metoda, ulozi ten poslany zoznam do lokalnej premennej podkategorie
        podkategorie = oblubene;
        Log.d("KnihySize", Integer.toString(getItemCount()));
        con = context;
        this.scena = scena;
    }

    @Override
    public AdapterPodkat.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //vytvorenie viewu idk
        LayoutInflater inflater = LayoutInflater.from(con);
        View knihyView;

        knihyView = inflater.inflate(R.layout.single_podkat, parent, false);

        return new AdapterPodkat.ViewHolder(knihyView);
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(AdapterPodkat.ViewHolder holder, int position) {

        // nastavenie nazvu holder.getAdapterPosition je cisclo itemviewu ktory chceme naplnit datami, ktore zoberieme z podkategorie
        holder.podkategoria.setText(podkategorie.get(holder.getAdapterPosition()).getPodkategoria().trim());

    }

    @Override
    public int getItemCount() {
        return podkategorie.size();
    }

    // Returns the total count of items in the list


    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        //2. zadefinovanie premennych z layoutu
        public TextView podkategoria;


        public ViewHolder(View itemView) {
            // ulozenie tych veci z layoutu u know idk
            super(itemView);
            podkategoria = (TextView) itemView.findViewById(R.id.podkategoria);

        }
    }
}