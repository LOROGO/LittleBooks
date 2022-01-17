package com.example.littlebooks;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.littlebooks.ui.dashboard.DashboardFragment;
import com.example.littlebooks.ui.home.HomeFragment;
import com.example.littlebooks.ui.main.UserFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivityT extends AppCompatActivity {
    private ViewPager viewPager;

    UserFragment fragmentuser;
    DashboardFragment dashboardFragment;
    HomeFragment homeFragment;
    MenuItem prevMenuItem;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_t);

        viewPager = findViewById(R.id.main_tabPager);

        fAuth = FirebaseAuth.getInstance();
        if (fAuth.getCurrentUser()==null)logout(MainActivityT.this);

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

        setupViewPager(viewPager);
    }
    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "ikd", Toast.LENGTH_LONG).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
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