package com.example.littlebooks.ui.dashboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.example.littlebooks.KnihyZkat;
import com.example.littlebooks.ModelMainData;
import com.example.littlebooks.R;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;

    RecyclerView recyclerView;
    ImageView beletria, rodicia, deti, poezia, hobby, odborna, bio;


    DatabaseReference mbase;
    public List<ModelMainData> mKnihy;
    RequestQueue requestQueue;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        beletria = root.findViewById(R.id.beletria);
        rodicia = root.findViewById(R.id.rodicia);
        deti = root.findViewById(R.id.deti);
        poezia = root.findViewById(R.id.poezia);
        hobby = root.findViewById(R.id.hobby);
        odborna = root.findViewById(R.id.odborna);
        bio = root.findViewById(R.id.bio);

        onClick1("Beletria", beletria, getActivity());
        onClick1("Partnerstvo a rodičovstvo", rodicia, getActivity());
        onClick1("Pre deti a mládež", deti, getActivity());
        onClick1("Poézia", poezia, getActivity());
        onClick1("Biografie", bio, getActivity());
        onClick1("Hobby, voľný čas", hobby, getActivity());
        onClick1("Odborná a náučná literatúra", odborna, getActivity());
        return root;
    }

    public void onClick1(String kategoria, ImageView imageView, Activity context){
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent((context), KnihyZkat.class);
                intent.putExtra("kategoria", kategoria);
                startActivity(intent);
            }
        });
    }



    /*public static void logout(final Activity activity) {
        FirebaseAuth.getInstance().signOut();
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Odhlásenie");
        builder.setMessage("Ste si istý, že sa chcete odhlásiť?");
        builder.setPositiveButton("Áno", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth.getInstance().signOut();
                activity.finishAffinity();
                System.exit(0);
            }
        });
        builder.setNegativeButton("Nie", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }*/


    /*public static void redirectActivity(Activity activity, Class aClass) {
        Intent intent = new Intent(activity, aClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }*/

   /* public void populateRecyclerView() {
        AdapterBooks adapterBooks = new AdapterBooks(mKnihy, getActivity(), "");
        // Attach the adapter to the recyclerview to populate items
        recyclerView.setAdapter(adapterBooks);
        // Set layout manager to position the items
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setItemAnimator(new SlideInLeftAnimator());
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

    }*/
}