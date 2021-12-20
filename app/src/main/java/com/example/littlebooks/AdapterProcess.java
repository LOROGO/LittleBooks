package com.example.littlebooks;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterProcess extends
        RecyclerView.Adapter<AdapterProcess.ViewHolder> {
    public List<ModelMainData> mKnihy;
    public Context con;

    public AdapterProcess(List<ModelMainData> knihy, Context context) {
        mKnihy = knihy;
        Log.d("KnihySize", Integer.toString(getItemCount()));
        con = context;
    }

    @Override
    public AdapterProcess.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(con);

        // Inflate the custom layout
        View knihyView = inflater.inflate(R.layout.single_book_main, parent, false);

        // Return a new holder instance

        return new AdapterProcess.ViewHolder(knihyView);

    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(AdapterProcess.ViewHolder holder, int position) {
        // Get the data model based on position
        ModelMainData kniha = mKnihy.get(holder.getAdapterPosition());

        // Set item views based on your views and data model
        holder.nazovKnihy.setText(mKnihy.get(holder.getAdapterPosition()).getNazov().trim());

        String imageUri = null;
        imageUri = "http:"+mKnihy.get(position).getObrazok().split(".jpg")[0]+".jpg";
        Log.d("knihyObrazok", "Kniha "+imageUri+"\n");

        Picasso.with(con).load(imageUri).into(holder.obrazok);
        Log.d("knihyAdapter", "Kniha "+mKnihy.get(holder.getAdapterPosition()).toString()+"\n");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             /*   Intent i = new Intent(con, NewActivity.class);
                i.putExtra("id",mKnihy.get(holder.getAdapterPosition()).getId());
                con.startActivity(i);*/
            }
        });
        holder.obrazok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView nazovKnihy;
        public ImageView obrazok;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            nazovKnihy = (TextView) itemView.findViewById(R.id.nazovKnihy);
            obrazok = itemView.findViewById(R.id.obrazok);
        }
    }
}