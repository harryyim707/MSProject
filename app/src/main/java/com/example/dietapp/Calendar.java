package com.example.dietapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.DatePicker;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Calendar extends AppCompatActivity {

    DBManager dbManager;
    SQLiteDatabase db = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        BottomNavigationView bottomNavigationView = findViewById(R.id.menu_bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.menu_calendar);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_analysis:
                        startActivity(new Intent(Calendar.this, Analysis.class));
                        return true;
                    case R.id.menu_home:
                        startActivity(new Intent(Calendar.this, MainActivity.class));
                        return true;
                    case R.id.menu_calendar:
                        return true;
                }
                return false;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        TextView today = findViewById(R.id.todayDate);

        TextView br = findViewById(R.id.brName);
        TextView brCal = findViewById(R.id.brCal);
        TextView brCar = findViewById(R.id.brCar);
        TextView brPro = findViewById(R.id.brPro);
        TextView brFat = findViewById(R.id.brFat);

        TextView lc = findViewById(R.id.lcName);
        TextView lcCal = findViewById(R.id.lcCal);
        TextView lcCar = findViewById(R.id.lcCar);
        TextView lcPro = findViewById(R.id.lcPro);
        TextView lcFat = findViewById(R.id.lcFat);

        TextView dn = findViewById(R.id.dnName);
        TextView dnCal = findViewById(R.id.dnCal);
        TextView dnCar = findViewById(R.id.dnCar);
        TextView dnPro = findViewById(R.id.dnPro);
        TextView dnFat = findViewById(R.id.dnFat);


        CalendarView calendarView =  findViewById(R.id.calendarView);

        dbManager = new DBManager(this, DBManager.DB, null, DBManager.DB_VERSION);
        db = dbManager.getReadableDatabase();

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                String selectDate;

                today.setText(year + "년" + (month + 1) + "월" + day + "일");
                selectDate = Integer.toString(year) + "-" + Integer.toString(month+1) + "-" + Integer.toString(day);

                Cursor cursor = db.rawQuery("select name, calories, carbohydrate, protein, fat from Nutrition where Nutrition.mealdate="+selectDate+" and meal=1;", null);
                if(cursor != null){
                    while(cursor.moveToNext()){
                         br.setText(cursor.getString(0));
                         brCal.setText(cursor.getString(1));
                         brCar.setText(cursor.getString(2));
                         brPro.setText(cursor.getString(3));
                         brFat.setText(cursor.getString(4));
                    }
                }

                cursor = db.rawQuery("select name, calories, carbohydrate, protein, fat from Nutrition where Nutrition.mealdate="+selectDate+" and meal=2;", null);
                if(cursor != null){
                    while(cursor.moveToNext()){
                        lc.setText(cursor.getString(0));
                        lcCal.setText(cursor.getString(1));
                        lcCar.setText(cursor.getString(2));
                        lcPro.setText(cursor.getString(3));
                        lcFat.setText(cursor.getString(4));
                    }
                }

                cursor = db.rawQuery("select name, calories, carbohydrate, protein, fat from Nutrition where Nutrition.mealdate="+selectDate+" and meal=3;", null);
                if(cursor != null){
                    while(cursor.moveToNext()){
                        dn.setText(cursor.getString(0));
                        dnCal.setText(cursor.getString(1));
                        dnCar.setText(cursor.getString(2));
                        dnPro.setText(cursor.getString(3));
                        dnFat.setText(cursor.getString(4));
                    }
                }
            }

        });
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.activity_calendar, container, false);


        return rootView;
    }
}