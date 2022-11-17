package com.example.dietapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private MainMenuAnalisysFragment fragmentAnalysis = new MainMenuAnalisysFragment();
    private MainMenyHomeFragment fragmentHome = new MainMenyHomeFragment();
    private MainMenuCalendarFragment fragmentCalendar = new MainMenuCalendarFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.menu_frame_layout, fragmentAnalysis).commitAllowingStateLoss();

        BottomNavigationView bottomNavigationView = findViewById(R.id.menu_bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListner());
    }

    class ItemSelectedListner implements BottomNavigationView.OnNavigationItemSelectedListener {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch (menuItem.getItemId()) {
                case R.id.menu_analysis:
                    transaction.replace(R.id.menu_frame_layout, fragmentAnalysis).commitAllowingStateLoss();
                    break;
                case R.id.menu_home:
                    transaction.replace(R.id.menu_frame_layout, fragmentHome).commitAllowingStateLoss();
                    break;
                case R.id.menu_calendar:
                    transaction.replace(R.id.menu_frame_layout, fragmentCalendar).commitAllowingStateLoss();
                    break;
            }
            return true;
        }


        private void calculate() {

        }

        private void summary() {
            TextView cal = (TextView) findViewById(R.id.cal_val);
            TextView carbo = (TextView) findViewById(R.id.car_val);
            TextView prot = (TextView) findViewById(R.id.pro_val);
            TextView fat = (TextView) findViewById(R.id.fat_val);
        }
    }
}