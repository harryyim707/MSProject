package com.example.dietapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Analysis extends AppCompatActivity {
    Button tod;
    Button week;
    Button month;
    TextView showBr;
    TextView showLc;
    TextView showDn;
    TextView showTot;

    BarChart barChart;
    BarDataSet carbo, pro, fat;
    ArrayList<Float> br = null;
    ArrayList<Float> lc = null;
    ArrayList<Float> dn = null;
    ArrayList<BarEntry> entryChart;
    ArrayList barEntries;
    String[] meals = new String[]{"Breakfast", "Lunch", "Dinner"};

    DBManager dbManager;
    SQLiteDatabase db = null;

    SimpleDateFormat format;
    String today;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);

        BottomNavigationView bottomNavigationView = findViewById(R.id.menu_bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.menu_analysis);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_analysis:
                        return true;
                    case R.id.menu_home:
                        startActivity(new Intent(Analysis.this, MainActivity.class));
                        return true;
                    case R.id.menu_calendar:
                        startActivity(new Intent(Analysis.this, Calendar.class));
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        tod = (Button) findViewById(R.id.btnToday);
        week = (Button) findViewById(R.id.btnWeek);
        month = (Button) findViewById(R.id.btnMonth);
        showBr = (TextView) findViewById(R.id.showTotBr);
        showLc = (TextView) findViewById(R.id.showTotLc);
        showDn = (TextView) findViewById(R.id.showTotDn);
        showTot = (TextView) findViewById(R.id.showTotCal);

        barChart = (BarChart) findViewById(R.id.chart);

        Date currentTime = java.util.Calendar.getInstance().getTime();
        format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        today = format.format(currentTime);

        dbManager = new DBManager(this, DBManager.DB, null, DBManager.DB_VERSION);
        db = dbManager.getReadableDatabase();

        initialize();

        tod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = db.rawQuery("select sum(calories) from Nutrition where Nutrition.mealdate=date('now', 'localtime');", null);
                int sumBr=0;
                int sumLc=0;
                int sumDn=0;
                int sum=0;
                if(cursor != null){
                    while(cursor.moveToNext()){
                        sum = cursor.getInt(0);
                    }
                }

                cursor = db.rawQuery("select sum(calories) from Nutrition where mealdate = date('now', 'localtime') and meal=1;", null);
                if(cursor != null){
                    while(cursor.moveToNext()){
                        sumBr = cursor.getInt(0);
                    }
                }
                cursor = db.rawQuery("select sum(calories) from Nutrition where mealdate = date('now', 'localtime') and meal=2;", null);
                if(cursor != null){
                    while(cursor.moveToNext()){
                        sumLc = cursor.getInt(0);
                    }
                }
                cursor = db.rawQuery("select sum(calories) from Nutrition where mealdate = date('now', 'localtime') and meal=3;", null);
                if(cursor != null){
                    while(cursor.moveToNext()){
                        sumDn = cursor.getInt(0);
                    }
                }
                showBr.setText(""+sumBr+" Cal (kcal)");
                showLc.setText(""+sumLc+" Cal (kcal)");
                showDn.setText(""+sumDn+" Cal (kcal)");
                showTot.setText(""+sum+" Cal (kcal)");

                cursor = db.rawQuery("select sum(carbohydrate), sum(protein), sum(fat) from Nutrition where Nutrition.mealdate=date('now', 'localtime') and meal = 1;", null);
                if(cursor != null){
                    while(cursor.moveToNext()){
                        float car = cursor.getFloat(0);
                        float p =cursor.getFloat(1);
                        float f = cursor.getFloat(2);
                        br = new ArrayList<>();
                        br.add(car);
                        br.add(p);
                        br.add(f);
                    }
                }
                cursor = db.rawQuery("select sum(carbohydrate), sum(protein), sum(fat) from Nutrition where Nutrition.mealdate=date('now', 'localtime') and meal = 2;", null);
                if(cursor != null){
                    while(cursor.moveToNext()){
                        float car = cursor.getFloat(0);
                        float p =cursor.getFloat(1);
                        float f = cursor.getFloat(2);
                        lc = new ArrayList<>();
                        lc.add(car);
                        lc.add(p);
                        lc.add(f);
                    }
                }
                cursor = db.rawQuery("select sum(carbohydrate), sum(protein), sum(fat) from Nutrition where Nutrition.mealdate=date('now', 'localtime') and meal = 3;", null);
                if(cursor != null){
                    while(cursor.moveToNext()){
                        float car = cursor.getFloat(0);
                        float p =cursor.getFloat(1);
                        float f = cursor.getFloat(2);
                        dn = new ArrayList<>();
                        dn.add(car);
                        dn.add(p);
                        dn.add(f);
                    }
                }
                setChart();
                cursor.close();
            }
        });

        week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = db.rawQuery("select sum(calories) from Nutrition where mealdate>=date('now', '-7 days', 'localtime') and mealdate <= date('now', '0 days', 'localtime');", null);
                int sumBr=0;
                int sumLc=0;
                int sumDn=0;
                int sum=0;
                if(cursor != null){
                    while(cursor.moveToNext()){
                        sum = cursor.getInt(0);
                    }
                }

                cursor = db.rawQuery("select sum(calories) from Nutrition where mealdate>=date('now', '-7 days', 'localtime') and mealdate <= date('now', '0 days', 'localtime') and meal=1;", null);
                if(cursor != null){
                    while(cursor.moveToNext()){
                        sumBr = cursor.getInt(0);
                    }
                }
                cursor = db.rawQuery("select sum(calories) from Nutrition where mealdate>=date('now', '-7 days', 'localtime') and mealdate <= date('now', '0 days', 'localtime') and meal=2;", null);
                if(cursor != null){
                    while(cursor.moveToNext()){
                        sumLc = cursor.getInt(0);
                    }
                }
                cursor = db.rawQuery("select sum(calories) from Nutrition where mealdate>=date('now', '-7 days', 'localtime') and mealdate <= date('now', '0 days', 'localtime') and meal=3;", null);
                if(cursor != null){
                    while(cursor.moveToNext()){
                        sumDn = cursor.getInt(0);
                    }
                }
                showBr.setText(""+sumBr+" Cal (kcal)");
                showLc.setText(""+sumLc+" Cal (kcal)");
                showDn.setText(""+sumDn+" Cal (kcal)");
                showTot.setText(""+sum+" Cal (kcal)");
                cursor = db.rawQuery("select sum(carbohydrate), sum(protein), sum(fat) from Nutrition where mealdate>=date('now', '-7 days', 'localtime') and mealdate <= date('now', '0 days', 'localtime') and meal = 1;", null);
                if(cursor != null){
                    while(cursor.moveToNext()){
                        float car = cursor.getFloat(0);
                        float p =cursor.getFloat(1);
                        float f = cursor.getFloat(2);
                        br = new ArrayList<>();
                        br.add(car);
                        br.add(p);
                        br.add(f);
                    }
                }
                cursor = db.rawQuery("select sum(carbohydrate), sum(protein), sum(fat) from Nutrition where mealdate>=date('now', '-7 days', 'localtime') and mealdate <= date('now', '0 days', 'localtime') and meal = 2;", null);
                if(cursor != null){
                    while(cursor.moveToNext()){
                        float car = cursor.getFloat(0);
                        float p =cursor.getFloat(1);
                        float f = cursor.getFloat(2);
                        lc = new ArrayList<>();
                        lc.add(car);
                        lc.add(p);
                        lc.add(f);
                    }
                }
                cursor = db.rawQuery("select sum(carbohydrate), sum(protein), sum(fat) from Nutrition where mealdate>=date('now', '-7 days', 'localtime') and mealdate <= date('now', '0 days', 'localtime') and meal = 3;", null);
                if(cursor != null){
                    while(cursor.moveToNext()){
                        float car = cursor.getFloat(0);
                        float p =cursor.getFloat(1);
                        float f = cursor.getFloat(2);
                        dn = new ArrayList<>();
                        dn.add(car);
                        dn.add(p);
                        dn.add(f);
                    }
                }
                setChart();
                cursor.close();
            }
        });
        month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = db.rawQuery("select sum(calories) from Nutrition where mealdate>=date('now', 'start of month', 'localtime') and mealdate <= date('now', 'start of month', '+1 month', '-1 day',  'localtime');", null);
                int sumBr=0;
                int sumLc=0;
                int sumDn=0;
                int sum=0;
                if(cursor != null){
                    while(cursor.moveToNext()){
                        sum = cursor.getInt(0);
                    }
                }

                cursor = db.rawQuery("select sum(calories) from Nutrition where mealdate>=date('now', 'start of month', 'localtime') and mealdate <= date('now', 'start of month', '+1 month', '-1 day',  'localtime') and meal=1;", null);
                if(cursor != null){
                    while(cursor.moveToNext()){
                        sumBr = cursor.getInt(0);
                    }
                }
                cursor = db.rawQuery("select sum(calories) from Nutrition where mealdate>=date('now', 'start of month', 'localtime') and mealdate <= date('now', 'start of month', '+1 month', '-1 day',  'localtime') and meal=2;", null);
                if(cursor != null){
                    while(cursor.moveToNext()){
                        sumLc = cursor.getInt(0);
                    }
                }
                cursor = db.rawQuery("select sum(calories) from Nutrition where mealdate>=date('now', 'start of month', 'localtime') and mealdate <= date('now', 'start of month', '+1 month', '-1 day',  'localtime') and meal=3;", null);
                if(cursor != null){
                    while(cursor.moveToNext()){
                        sumDn = cursor.getInt(0);
                    }
                }
                showBr.setText(""+sumBr+" Cal (kcal)");
                showLc.setText(""+sumLc+" Cal (kcal)");
                showDn.setText(""+sumDn+" Cal (kcal)");
                showTot.setText(""+sum+" Cal (kcal)");
                cursor = db.rawQuery("select sum(carbohydrate), sum(protein), sum(fat) from Nutrition where mealdate>=date('now', 'start of month', 'localtime') and mealdate <= date('now', 'start of month', '+1 month', '-1 day',  'localtime') and meal = 1;", null);
                if(cursor != null){
                    while(cursor.moveToNext()){
                        float car = cursor.getFloat(0);
                        float p =cursor.getFloat(1);
                        float f = cursor.getFloat(2);
                        br = new ArrayList<>();
                        br.add(car);
                        br.add(p);
                        br.add(f);
                    }
                }
                cursor = db.rawQuery("select sum(carbohydrate), sum(protein), sum(fat) from Nutrition where mealdate>=date('now', 'start of month', 'localtime') and mealdate <= date('now', 'start of month', '+1 month', '-1 day',  'localtime') and meal = 2;", null);
                if(cursor != null){
                    while(cursor.moveToNext()){
                        float car = cursor.getFloat(0);
                        float p =cursor.getFloat(1);
                        float f = cursor.getFloat(2);
                        lc = new ArrayList<>();
                        lc.add(car);
                        lc.add(p);
                        lc.add(f);
                    }
                }
                cursor = db.rawQuery("select sum(carbohydrate), sum(protein), sum(fat) from Nutrition where mealdate>=date('now', 'start of month', 'localtime') and mealdate <= date('now', 'start of month', '+1 month', '-1 day',  'localtime') and meal = 3;", null);
                if(cursor != null){
                    while(cursor.moveToNext()){
                        float car = cursor.getFloat(0);
                        float p =cursor.getFloat(1);
                        float f = cursor.getFloat(2);
                        dn = new ArrayList<>();
                        dn.add(car);
                        dn.add(p);
                        dn.add(f);
                    }
                }
                setChart();
                cursor.close();
            }
        });

    }

    private void initialize() {
        Cursor cursor = db.rawQuery("select sum(calories) from Nutrition where Nutrition.mealdate=date('now', 'localtime');", null);
        int sumBr=0;
        int sumLc=0;
        int sumDn=0;
        int sum=0;
        if(cursor != null){
            while(cursor.moveToNext()){
                sum = cursor.getInt(0);
            }
        }

        cursor = db.rawQuery("select sum(calories) from Nutrition where mealdate = date('now', 'localtime') and meal=1;", null);
        if(cursor != null){
            while(cursor.moveToNext()){
                sumBr = cursor.getInt(0);
            }
        }
        cursor = db.rawQuery("select sum(calories) from Nutrition where mealdate = date('now', 'localtime') and meal=2;", null);
        if(cursor != null){
            while(cursor.moveToNext()){
                sumLc = cursor.getInt(0);
            }
        }
        cursor = db.rawQuery("select sum(calories) from Nutrition where mealdate = date('now', 'localtime') and meal=3;", null);
        if(cursor != null){
            while(cursor.moveToNext()){
                sumDn = cursor.getInt(0);
            }
        }
        showBr.setText(""+sumBr+" Cal (kcal)");
        showLc.setText(""+sumLc+" Cal (kcal)");
        showDn.setText(""+sumDn+" Cal (kcal)");
        showTot.setText(""+sum+" Cal (kcal)");

        cursor = db.rawQuery("select sum(carbohydrate), sum(protein), sum(fat) from Nutrition where Nutrition.mealdate=date('now', 'localtime') and meal = 1;", null);
        if(cursor != null){
            while(cursor.moveToNext()){
                float car = cursor.getFloat(0);
                float p =cursor.getFloat(1);
                float f = cursor.getFloat(2);
                br = new ArrayList<>();
                br.add(car);
                br.add(p);
                br.add(f);
            }
        }
        cursor = db.rawQuery("select sum(carbohydrate), sum(protein), sum(fat) from Nutrition where Nutrition.mealdate=date('now', 'localtime') and meal = 2;", null);
        if(cursor != null){
            while(cursor.moveToNext()){
                float car = cursor.getFloat(0);
                float p =cursor.getFloat(1);
                float f = cursor.getFloat(2);
                lc = new ArrayList<>();
                lc.add(car);
                lc.add(p);
                lc.add(f);
            }
        }
        cursor = db.rawQuery("select sum(carbohydrate), sum(protein), sum(fat) from Nutrition where Nutrition.mealdate=date('now', 'localtime') and meal = 3;", null);
        if(cursor != null){
            while(cursor.moveToNext()){
                float car = cursor.getFloat(0);
                float p =cursor.getFloat(1);
                float f = cursor.getFloat(2);
                dn = new ArrayList<>();
                dn.add(car);
                dn.add(p);
                dn.add(f);
            }
        }
        setChart();
        cursor.close();
    }

    private void setChart() {
        carbo = new BarDataSet(getCarEntries(), "Carbohydrate");
        carbo.setColor(getApplicationContext().getResources().getColor(R.color.purple_200));
        pro = new BarDataSet(getProEntries(), "Protein");
        pro.setColor(Color.CYAN);
        fat = new BarDataSet(getFatEntries(), "Fat");
        fat.setColor(Color.GREEN);
        BarData data = new BarData(carbo, pro, fat);
        barChart.setData(data);
        barChart.getDescription().setEnabled(false);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(meals));
        xAxis.setCenterAxisLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1);
        xAxis.setGranularityEnabled(true);
        barChart.setVisibleXRangeMaximum(3);
        float barSpace = 0.1f;
        float groupSpace = 0.5f;
        data.setBarWidth(0.15f);
        barChart.getXAxis().setAxisMinimum(0);
        barChart.animate();
        barChart.groupBars(0, groupSpace, barSpace);
        barChart.invalidate();
    }

    private ArrayList<BarEntry> getFatEntries() {
        barEntries = new ArrayList();
        if(br!=null && lc!=null && dn!=null){
            barEntries.add(new BarEntry(1f, br.get(2)));
            barEntries.add(new BarEntry(2f, lc.get(2)));
            barEntries.add(new BarEntry(3f, dn.get(2)));
        }
        return barEntries;
    }

    private ArrayList<BarEntry> getProEntries() {
        barEntries = new ArrayList();
        if(br!=null && lc!=null && dn!=null){
            barEntries.add(new BarEntry(1f, br.get(1)));
            barEntries.add(new BarEntry(2f, lc.get(1)));
            barEntries.add(new BarEntry(3f, dn.get(1)));
        }
        return barEntries;
    }

    private ArrayList<BarEntry> getCarEntries() {
        barEntries = new ArrayList();
        if(br!=null && lc!=null && dn!=null){
            barEntries.add(new BarEntry(1f, br.get(0)));
            barEntries.add(new BarEntry(2f, lc.get(0)));
            barEntries.add(new BarEntry(3f, dn.get(0)));
        }
        return barEntries;
    }

}
