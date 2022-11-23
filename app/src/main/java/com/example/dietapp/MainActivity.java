package com.example.dietapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    private View view;
    private ImageButton addBtn1;
    private ImageButton addBtn2;
    private ImageButton addBtn3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Navigation Bar
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.menu_bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.menu_home);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_analysis:
                        startActivity(new Intent(MainActivity.this, Analysis.class));
                        return true;
                    case R.id.menu_home:
                        return true;
                    case R.id.menu_calendar:
                        startActivity(new Intent(MainActivity.this, Calendar.class));
                        return true;
                }
                return false;
            }
        });
        //apply click event on buttons

        addBtn1 = findViewById(R.id.addBtn1);
        addBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, food_data_input.class);
                startActivity(intent);
            }
        });
        addBtn2 = findViewById(R.id.addBtn2);
        addBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, food_data_input.class);
                startActivity(intent);
            }
        });
        addBtn3 = findViewById(R.id.addBtn3);
        addBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, food_data_input.class);
                startActivity(intent);
            }
        });

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