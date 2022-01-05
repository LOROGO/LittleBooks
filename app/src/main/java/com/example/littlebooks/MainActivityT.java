package com.example.littlebooks;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.littlebooks.ui.dashboard.DashboardFragment;
import com.example.littlebooks.ui.home.HomeFragment;
import com.example.littlebooks.ui.main.UserFragment;
import com.example.littlebooks.ui.notifications.NotificationsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivityT extends AppCompatActivity {
    public List<ModelMainData> knihy;
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;

    UserFragment fragmentuser;
    DashboardFragment dashboardFragment;
    HomeFragment homeFragment;
    MenuItem prevMenuItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_t);

        List<Fragment> list = new ArrayList<>();
        list.add(new HomeFragment());
        list.add(new NotificationsFragment());
        list.add(new DashboardFragment());

        viewPager = findViewById(R.id.main_tabPager);
        //pagerAdapter = new SlidePagerAdapter(getSupportFragmentManager(), list);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.fragment_home:
                                viewPager.setCurrentItem(0);
                                break;
                            case R.id.fragment_user:
                                viewPager.setCurrentItem(1);
                                break;
                            case R.id.fragment_dashboard:
                                viewPager.setCurrentItem(2);
                                break;
                        }
                        return false;
                    }
                });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                }
                else
                {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                Log.d("page", "onPageSelected: "+position);
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

       /*  //Disable ViewPager Swipe
       viewPager.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                return true;
            }
        });
        */

        setupViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        dashboardFragment =new DashboardFragment();
        fragmentuser =new UserFragment();
        homeFragment =new HomeFragment();
        adapter.addFragment(homeFragment);
        adapter.addFragment(fragmentuser);
        adapter.addFragment(dashboardFragment);
        viewPager.setAdapter(adapter);
    }
}