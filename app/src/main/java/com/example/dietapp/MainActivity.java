package com.example.dietapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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
    TextView sumBr;
    TextView sumLc;
    TextView sumDn;


    SimpleDateFormat format;
    String today;
    DBManager dbManager;
    SQLiteDatabase db = null;

    private static final int REQUEST_IMAGE_CAPTURE = 672;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbManager = new DBManager(this, DBManager.DB, null, DBManager.DB_VERSION);
        db = dbManager.getWritableDatabase();
        int permssionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (permssionCheck!= PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(this,"권한 승인이 필요합니다",Toast.LENGTH_LONG).show();

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {
                Toast.makeText(this,"카메라 권한이 필요합니다.",Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        REQUEST_IMAGE_CAPTURE);
                Toast.makeText(this,"카메라 권한이 필요합니다.",Toast.LENGTH_LONG).show();

            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "승인이 허가되어 있습니다.", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(this, "아직 승인받지 않았습니다.", Toast.LENGTH_LONG).show();
                }
                return;
            }

        }
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
        showList();
    }

    private void getDB() {
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
                sumCal = c.getInt(0);
                sumCar = c.getDouble(1);
                sumPro = c.getDouble(2);
                sumFat = c.getDouble(3);
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
        c.close();
        sumBr = (TextView) findViewById(R.id.sumBr);
        Cursor cb = db.rawQuery("select sum(calories) from nutrition where mealdate =? and meal=?", new String[]{today, "1"});
        int brSum = 0;
        if(cb!=null){
            while(cb.moveToNext()){
                brSum = cb.getInt(0);
            }
            sumBr.setText(String.format("%d kcal", brSum));
        }
        cb.close();
        sumLc = (TextView) findViewById(R.id.sumLc);
        Cursor cl = db.rawQuery("select sum(calories) from nutrition where mealdate =? and meal=?", new String[]{today, "2"});
        int lcSum = 0;
        if(cl!=null){
            while(cl.moveToNext()){
                lcSum = cl.getInt(0);
            }
            sumLc.setText(String.format("%d kcal", lcSum));
        }
        cl.close();
        sumDn = (TextView) findViewById(R.id.sumDn);
        Cursor cd = db.rawQuery("select sum(calories) from nutrition where mealdate =? and meal=?", new String[]{today, "3"});
        int dnSum = 0;
        if(cd!=null){
            while(cd.moveToNext()){
                dnSum = cd.getInt(0);
            }
            sumDn.setText(String.format("%d kcal", dnSum));
        }
        cd.close();
    }
    private void showList(){
        recyclerView1 = (RecyclerView) findViewById(R.id.recyclerview1);
        recyclerView1.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        List<ExpandableListAdapter.Item> data1 = new ArrayList<>();
        data1.add(new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "Meal List", "", "", ""));
        Cursor c = db.rawQuery("select name, carbohydrate, protein, fat"+" from "+DBManager.TABLE_NAME+
                " where "+DBManager.COLUMN_WHEN+"=?", new String[]{"1"});
        while(c.moveToNext()){
            data1.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, c.getString(0), c.getString(1), c.getString(2), c.getString(3)));
        }
        recyclerView1.setAdapter(new ExpandableListAdapter(data1));

        recyclerView2 = (RecyclerView) findViewById(R.id.recyclerview2);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        List<ExpandableListAdapter.Item> data2 = new ArrayList<>();
        data2.add(new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "Meal List", "","",""));
        Cursor c2 = db.rawQuery("select name, carbohydrate, protein, fat from "+DBManager.TABLE_NAME+
                " where "+DBManager.COLUMN_WHEN+"=?", new String[]{"2"});
        while(c2.moveToNext()){
            data2.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, c2.getString(0), c2.getString(1), c2.getString(2), c2.getString(3)));
        }
        recyclerView2.setAdapter(new ExpandableListAdapter(data2));

        recyclerView3 = (RecyclerView) findViewById(R.id.recyclerview3);
        recyclerView3.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        List<ExpandableListAdapter.Item> data3 = new ArrayList<>();
        data3.add(new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "Meal List", "","",""));
        Cursor c3 = db.rawQuery("select name, carbohydrate, protein, fat from "+DBManager.TABLE_NAME+
                " where "+DBManager.COLUMN_WHEN+"=?", new String[]{"3"});
        while(c3.moveToNext()){
            data3.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, c3.getString(0), c3.getString(1), c3.getString(2), c3.getString(3)));
        }
        recyclerView3.setAdapter(new ExpandableListAdapter(data3));

    }
}