package com.example.littlebooks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.example.littlebooks.ui.dashboard.DashboardFragment;
import com.example.littlebooks.ui.home.HomeFragment;
import com.example.littlebooks.ui.notifications.NotificationsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivityT extends AppCompatActivity {
    public List<ModelMainData> knihy;
    private ViewPager pager;
    private PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_t);

        List<Fragment> list = new ArrayList<>();
        list.add(new HomeFragment());
        list.add(new NotificationsFragment());
        list.add(new DashboardFragment());

        pager = findViewById(R.id.main_tabPager);
        pagerAdapter = new SlidePagerAdapter(getSupportFragmentManager(), list);

        pager.setAdapter(pagerAdapter);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        //NavController navController = Navigation.findNavController(this, R.id.main_tabPager);
        //NavigationUI.setupWithNavController(bottomNavigationView, navController);

    }
}