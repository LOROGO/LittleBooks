package com.example.littlebooks;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterReview extends RecyclerView.Adapter<AdapterReview.ViewHolder> {
    public List<ModelRecenzia> mReview;
    public Context con;
    String TAG = "ARecenzia";
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    RequestQueue requestQueue;
    String url;
    StringRequest stringRequest;
    ViewHolder holder;
    View item;

    public AdapterReview(List<ModelRecenzia> recenzie, Context context) {
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
        this.holder = holder;

        //
        ModelRecenzia recenzia = mReview.get(holder.getAdapterPosition());


        // nastavenie nazvu holder.getAdapterPosition je cisclo itemviewu ktory chceme naplnit datami, ktore zoberieme z mReview
        holder.menoUsera.setText(recenzia.getMeno().trim());
        holder.menoUsera.setOnClickListener(view -> Log.d("A", "meno"));
        holder.recenziaPopis.setText(recenzia.getPopis().trim());
        if (!recenzia.getObrazok().isEmpty()){
            Picasso.with(con).load(recenzia.getObrazok()).into(holder.pouzivatel);
        }

/*
        holder.horeSipka.setOnClickListener(view -> {
            insertHod(recenzia.getId_recenzie(), "up");
            holder.cislo.setText(Integer.toString(Integer.parseInt(holder.cislo.getText().toString())+1));
        });
        holder.doleSipka.setOnClickListener(view -> {
            insertHod(recenzia.getId_recenzie(), "down");
        });*/


       /* if (recenzia.getUp()=='n') {//neni ohodnotene

        }else if (recenzia.getUp()=='1') {//je liknute
            holder.horeSipka.setImageResource(R.drawable.hore);
            holder.horeSipka.setOnClickListener(view -> {
                deleteHod(recenzia.getId_recenzie());
            });
            holder.doleSipka.setOnClickListener(view -> {
                insertHod(recenzia.getId_recenzie(), "down");
            });
        }else
        if (recenzia.getUp()=='0') {//je disliknute
            holder.doleSipka.setImageResource(R.drawable.dole);
            holder.horeSipka.setOnClickListener(view -> {
                insertHod(recenzia.getId_recenzie(), "up");
            });
            holder.doleSipka.setOnClickListener(view -> {
                deleteHod(recenzia.getId_recenzie());
            });
        }*/
        //nastavenie obrazka to split je tam preto lebo v db mame ze napr obrazky/obrazok1.jpg+a kopec bludov za tym takze takto len tie bludy dame prec
        String imageUri = null;
        imageUri = mReview.get(position).getObrazok();
        Log.d("recenzieObrazok", "Kniha "+imageUri+"\n");

        //Picasso.with(con).load(imageUri).into(holder.pouzivatel);
       // holder.pouzivatel.setImageDrawable(R.drawable.user);
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
        public TextView recenziaPopis, menoUsera, cislo;
        /*public ImageView hviezdicky, horeSipka, doleSipka;*/
        public CircleImageView pouzivatel;


        public ViewHolder(View itemView) {
            // ulozenie tych veci z layoutu u know idk
            super(itemView);
            /*horeSipka = itemView.findViewById(R.id.hore);
            doleSipka = itemView.findViewById(R.id.dole);*/
            recenziaPopis = (TextView) itemView.findViewById(R.id.recenziaPopis);
            //hviezdicky = itemView.findViewById(R.id.hviezdicky);
            menoUsera = itemView.findViewById(R.id.menoUsera);
            pouzivatel = itemView.findViewById(R.id.pouzivatel);
            /*cislo = itemView.findViewById(R.id.textView4);*/

        }
    }

    /*public  void deleteHod(String id_recenzia){
        String url = "http://178.62.196.85/recenzia.php?&action=deleteHodnotenie&uid=" + fAuth.getUid() + "&id_recenzia=" + id_recenzia;
        Log.d(TAG, "deleteOCP "+url);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    if (response.equals("ok-hodnotenie")){
                        holder.doleSipka.setImageResource(R.drawable.sivadole);
                        holder.horeSipka.setImageResource(R.drawable.sivahore);
                        holder.horeSipka.setOnClickListener(v -> insertHod(id_recenzia,"up"));
                        holder.doleSipka.setOnClickListener(v -> insertHod(id_recenzia,"down"));
                    }

                    Log.d("RegR", response.toString());
                },
                error -> {
                    Log.d("RegE", error.toString());

                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError { //berie to udaje z editextov a posiela ich do php
                Map<String, String> params = new HashMap<>();

                return params;
            }
        };
        requestQueue = Volley.newRequestQueue(con);
        requestQueue.add(stringRequest);
    }
    public void insertHod(String id_recenzia, String hodnotenie){
        deleteHod(id_recenzia);
        String url = "http://178.62.196.85/recenzia.php?&action=addHodnotenie&uid=" + fAuth.getUid() + "&id_recenzia=" + id_recenzia + "&hodnotenie=" + hodnotenie;
        Log.d(TAG, "insertOCP "+url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    if (response.equals("ok")){
                        Log.d(TAG, "insertSetDel");
                        if (hodnotenie.equals("up")) {
                            holder.horeSipka.setImageResource(R.drawable.hore);
                            holder.horeSipka.setOnClickListener(v -> deleteHod(id_recenzia));
                        }else if(hodnotenie.equals("down")) {
                            holder.doleSipka.setImageResource(R.drawable.dole);
                            holder.doleSipka.setOnClickListener(v -> deleteHod(id_recenzia));
                        }

                    }

                },
                error -> {
                    Log.d(TAG, error.toString());

                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError { //berie to udaje z editextov a posiela ich do php
                Map<String, String> params = new HashMap<>();

                return params;
            }
        };
        requestQueue = Volley.newRequestQueue(con);
        requestQueue.add(stringRequest);
    }*/
}