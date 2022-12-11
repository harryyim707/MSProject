package com.example.dietapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class food_data_view extends AppCompatActivity {
    DBManager dbManager;
    SQLiteDatabase db = null;
    int selectMeal;

    private ListView listView;
    FoodViewItemAdapter adapter = null;
    ArrayList<FoodViewItem> itemList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_data_view);
        Intent intent = getIntent();
        selectMeal = intent.getExtras().getInt("selectMeal");
        dbManager = new DBManager(this, DBManager.DB, null, DBManager.DB_VERSION);
        db = dbManager.getWritableDatabase();

        listView = findViewById(R.id.listView2);
        itemList = new ArrayList<FoodViewItem>();
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    public void init(){
        SimpleDateFormat dtf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();

        Date dateObj = calendar.getTime();
        String today = dtf.format(dateObj);

        Cursor cursor = db.rawQuery("select id, name, calories, carbohydrate, protein, fat, quantity, review, mealtime, address, img_dir from Nutrition where mealdate=?"+" and meal=?;", new String[]{today, String.format("%d", selectMeal)});
        if(cursor != null){
            while(cursor.moveToNext()){
                itemList.add(new FoodViewItem(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10)));
            }
        }
        cursor.close();
        adapter = new FoodViewItemAdapter(itemList, getApplicationContext());
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(food_data_view.this, dataFix.class);
                FoodViewItem item = itemList.get(position);
                intent.putExtra("id", item.getId());
                intent.putExtra("name", item.getName());
                intent.putExtra("calories", item.getCal());
                intent.putExtra("carbo", item.getCar());
                intent.putExtra("pro", item.getPro());
                intent.putExtra("fat", item.getFat());
                intent.putExtra("quantity", item.getQuantity());
                intent.putExtra("review", item.getReview());
                intent.putExtra("time", item.getTime());
                intent.putExtra("place", item.getPlace());
                intent.putExtra("imgDir", item.getImg());
                startActivity(intent);
            }
        });
    }




}