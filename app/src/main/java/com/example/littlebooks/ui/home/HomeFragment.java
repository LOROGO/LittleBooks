package com.example.littlebooks.ui.home;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Scene;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.example.littlebooks.Account;
import com.example.littlebooks.Adapter;
import com.example.littlebooks.BackgroundTask;
import com.example.littlebooks.ModelMainData;
import com.example.littlebooks.MojeKnihy;
import com.example.littlebooks.NewBook;
import com.example.littlebooks.R;
import com.example.littlebooks.old.BooksActivity;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

public class HomeFragment extends Fragment implements BackgroundTask.ApiCallback {

    private HomeViewModel homeViewModel;
    DrawerLayout drawerLayout;
    RecyclerView recyclerView;
    EditText searchBar;
    Scene sceneMain;
    static Scene sceneSearch;
    Scene currentScene;
    ImageButton back;
    Transition mainTrans;
    Transition mainTrans2;          //prechody
    View root;

    public List<ModelMainData> mKnihy;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        root = inflater.inflate(R.layout.fragment_home, container, false);

        sceneMain = Scene.getSceneForLayout(root.findViewById(R.id.rootFrameLayoutMain), R.layout.main_basic, getContext());
        sceneSearch = Scene.getSceneForLayout(root.findViewById(R.id.rootFrameLayoutMain), R.layout.main_search, getContext());
        sceneMain.enter();
        mainTrans = TransitionInflater.from(getContext()).inflateTransition(R.transition.transition1);
        mainTrans2 = TransitionInflater.from(getContext()).inflateTransition(R.transition.transition2);
        currentScene = sceneMain;


        createSceneMain(root);
        mainTrans2.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {

            }

            @Override
            public void onTransitionEnd(Transition transition) {
                createSceneMain(root);
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }
        });
        mainTrans.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {

            }

            @Override
            public void onTransitionEnd(Transition transition) {
                createSceneSearch(root);
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }
        });

// VSETKY VECI PISANE DO ONCREATU SA TRZ PISU DO CREATESCENEMAIN() ALEBO SCREATESCENESEARCH() PODLA TOHO O KTORY LAYOUT SA JEDNA

        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        return root;
    }

    public void createSceneMain(View root){             //scena1

        drawerLayout = root.findViewById(R.id.drawer_layout);
        recyclerView = root.findViewById(R.id.recyclerViewMain);
        searchBar = root.findViewById(R.id.vyhladavanie);

        //zavolanie a poslanie parametrov do triedy Background task ktora komunikuje s php
        if (mKnihy==null) {
            BackgroundTask bs = new BackgroundTask("kniha", "select", "2", "get_knihy4", "", "");
            bs.setApiCallback(this);
            bs.execute();
        }
        else populateRecView(mKnihy, root);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        searchBar.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                TransitionManager.go(sceneSearch, mainTrans);
                currentScene = sceneSearch;

            }
        });

    }
    public void createSceneSearch(View root){       //scena2
        searchBar = root.findViewById(R.id.vyhladavanie);
        recyclerView = root.findViewById(R.id.recyclerViewMain);
        back = root.findViewById(R.id.backButton);

        searchBar.requestFocus();
        InputMethodManager imm = (InputMethodManager)a.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(searchBar, 0);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm =  (InputMethodManager) a.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(root.getWindowToken(), 0);
                TransitionManager.go(sceneMain, mainTrans2);
                currentScene = sceneMain;

            }
        });
        searchBar.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == 66) {
                    InputMethodManager imm =  (InputMethodManager) a.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    searchRequest(searchBar.getText().toString());
                    searchBar.clearFocus();
                    return true; //this is required to stop sending key event to parent
                }
                return false;
            }
        });
    }


    private void searchRequest(String text) {
        Log.d("url", text);
        BackgroundTask bs = new BackgroundTask("kniha", "select", "3", "getKniha", "", text);
        bs.setApiCallback(this);
        String a = String.valueOf(bs.execute());
    }

    Activity a;
    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        if(context instanceof Activity){
            a = (Activity) context;
        }

    }

    //metoda zavolana z background tasku
    @Override
    public void populateLay(JSONArray obj) {
        //vytvorenie noveho listu
        List<ModelMainData> knihy = new ArrayList<>();
        if (obj!=null){
            for (int i = 0; i < obj.length(); i++) {
                try {
                    //spracuje json a da do listu
                    JSONObject a = obj.getJSONObject(i);
                    knihy.add(new ModelMainData(

                            a.getString("id_kniha"),
                            a.getString("nazov"),
                            a.getString("obrazok")
                    ));
                }catch (Exception e){
                    Log.e("PopulateRec", e.getMessage());
                }

            }
            mKnihy = knihy;
            //posle list do adapteru a nastavi adapter pre recyclerview
            populateRecView(knihy, root);
        }}

        public void populateRecView(List<ModelMainData> knihy, View root){
        recyclerView = root.findViewById(R.id.recyclerViewMain);
        if (currentScene == sceneMain){
            Adapter adapter = new Adapter(knihy, getActivity());
            // Attach the adapter to the recyclerview to populate items
            recyclerView.setAdapter(adapter);
            // Set layout manager to position the items

            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
            recyclerView.setItemAnimator(new SlideInLeftAnimator());
            SnapHelper snapHelper = new LinearSnapHelper();
            snapHelper.attachToRecyclerView(recyclerView);
        }else {
            Adapter adapter = new Adapter(knihy, getActivity());
            // Attach the adapter to the recyclerview to populate items
            recyclerView.setAdapter(adapter);
            // Set layout manager to position the items

            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
            recyclerView.setItemAnimator(new SlideInLeftAnimator());
            SnapHelper snapHelper = new LinearSnapHelper();
            //snapHelper.attachToRecyclerView(recyclerView);
        }
    }


    public void ClickMenu(View view){
        openDrawer(drawerLayout);
    }

    public static void openDrawer(DrawerLayout drawerLayout){
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void ClickLogo(View view){
        closeDrawer(drawerLayout);
    }

    public static void closeDrawer(DrawerLayout drawerLayout) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }


    public void ClickBooks(View view){
        redirectActivity(getActivity(), BooksActivity.class);
    }

    public void ClickAccount(View view){
        redirectActivity(getActivity(), Account.class);
    }

    public void ClickNewBook(View view){
        startActivity(new Intent(getActivity(), NewBook.class));
    }

    public void ClickLogout(View view){
        logout(getActivity());
    }

    public void ClickMojeKnihy(View view){
        redirectActivity(getActivity(), MojeKnihy.class);
    }

    public void ClickDomov(View view){
        //recreate();
    }


    public static void logout(final Activity activity){
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
    }


    public static void redirectActivity(Activity activity, Class aClass) {
        Intent intent = new Intent(activity, aClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }
    public void populateRecyclerView(){
        Adapter adapter = new Adapter(mKnihy, getActivity());
        // Attach the adapter to the recyclerview to populate items
        recyclerView.setAdapter(adapter);
        // Set layout manager to position the items
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setItemAnimator(new SlideInLeftAnimator());
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

    }
}