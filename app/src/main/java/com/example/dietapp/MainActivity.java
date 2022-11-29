package com.example.dietapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TextView calVal;
    private TextView carVal;
    private TextView proVal;
    private TextView fatVal;
    private ImageButton addBtn1;
    private ImageButton addBtn2;
    private ImageButton addBtn3;
    private RecyclerView recyclerView1;
    private RecyclerView recyclerView2;
    private RecyclerView recyclerView3;

    SimpleDateFormat format;
    String today;
    int i = 0;
    DBManager dbManager;
    SQLiteDatabase db = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbManager = new DBManager(this, DBManager.DB, null, DBManager.DB_VERSION);
        db = dbManager.getWritableDatabase();
    }

    @Override
    protected void onStart() {
        super.onStart();
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
                intent.putExtra("meal", 1);
                startActivity(intent);
            }
        });
        addBtn2 = findViewById(R.id.addBtn2);
        addBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, food_data_input.class);
                intent.putExtra("meal", "2");
                startActivity(intent);
            }
        });
        addBtn3 = findViewById(R.id.addBtn3);
        addBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, food_data_input.class);
                intent.putExtra("meal", "3");
                startActivity(intent);
            }
        });
        //get DB
        getDB();
        showName();
    }

    private void getDB() {
        System.out.println(i);
        i++;
        Date currentTime = java.util.Calendar.getInstance().getTime();
        format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        today = format.format(currentTime);
        Cursor c = db.rawQuery("select sum(calories), sum(carbohydrate), sum(protein), sum(fat) from Nutrition where "
                +DBManager.COLUMN_DATE+"=?", new String[]{today});

        calVal = (TextView) findViewById(R.id.cal_val);
        carVal = (TextView) findViewById(R.id.car_val);
        proVal = (TextView) findViewById(R.id.pro_val);
        fatVal = (TextView) findViewById(R.id.fat_val);
        int sumCal = 0;
        double sumCar = 0;
        double sumPro = 0;
        double sumFat = 0;
        if(c!=null){
            while (c.moveToNext()){
                sumCal += c.getInt(0);
                sumCar += c.getDouble(1);
                sumPro += c.getDouble(2);
                sumFat += c.getDouble(3);
            }
            calVal.setText(String.format("%d", sumCal));
            carVal.setText(String.format("%.2f", sumCar));
            proVal.setText(String.format("%.2f", sumPro));
            fatVal.setText(String.format("%.2f", sumFat));
        }
        else{
            calVal.setText("");
            carVal.setText("");
            proVal.setText("");
            fatVal.setText("");
        }
    }
    private void showName(){
        recyclerView1 = (RecyclerView) findViewById(R.id.recyclerview1);
        recyclerView1.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        List<ExpandableListAdapter.Item> data1 = new ArrayList<>();
        data1.add(new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "Meal List"));
        Cursor c = db.rawQuery("select "+DBManager.COLUMN_NAME+" from "+DBManager.TABLE_NAME+
                " where "+DBManager.COLUMN_WHEN+"=?", new String[]{"1"});
        while(c.moveToNext()){
            data1.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, c.getString(0)));
        }
        recyclerView1.setAdapter(new ExpandableListAdapter(data1));

        recyclerView2 = (RecyclerView) findViewById(R.id.recyclerview2);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        List<ExpandableListAdapter.Item> data2 = new ArrayList<>();
        data2.add(new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "Meal List"));
        Cursor c2 = db.rawQuery("select "+DBManager.COLUMN_NAME+" from "+DBManager.TABLE_NAME+
                " where "+DBManager.COLUMN_WHEN+"=?", new String[]{"2"});
        while(c2.moveToNext()){
            data2.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, c2.getString(0)));
        }
        recyclerView2.setAdapter(new ExpandableListAdapter(data2));

        recyclerView3 = (RecyclerView) findViewById(R.id.recyclerview3);
        recyclerView3.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        List<ExpandableListAdapter.Item> data3 = new ArrayList<>();
        data3.add(new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "Meal List"));
        Cursor c3 = db.rawQuery("select "+DBManager.COLUMN_NAME+" from "+DBManager.TABLE_NAME+
                " where "+DBManager.COLUMN_WHEN+"=?", new String[]{"3"});
        while(c3.moveToNext()){
            data3.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, c3.getString(0)));
        }
        recyclerView3.setAdapter(new ExpandableListAdapter(data3));

    }
}