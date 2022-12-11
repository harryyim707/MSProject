package com.example.dietapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class food_data_view extends AppCompatActivity {

    DBManager dbManager;
    SQLiteDatabase db = null;
    TextView name, cal, car, pro, fat, review, time, place;

    Intent intent = getIntent();
    int selectMeal = intent.getExtras().getInt("selectMeal");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_data_view);
    }

    protected void onResume(){
        super.onResume();

        SimpleDateFormat dtf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();

        Date dateObj = calendar.getTime();
        String today = dtf.format(dateObj);

        dbManager = new DBManager(this, DBManager.DB, null, DBManager.DB_VERSION);

        name = (TextView) findViewById(R.id.foodName);
        cal = (TextView) findViewById(R.id.foodName);
        car = (TextView) findViewById(R.id.foodName);
        pro = (TextView) findViewById(R.id.foodName);
        fat = (TextView) findViewById(R.id.foodAmount);
        review = (TextView) findViewById(R.id.calval);
        time = (TextView) findViewById(R.id.carval);
        place = (TextView) findViewById(R.id.proval);

        if(selectMeal ==1){
            Cursor cursor = db.rawQuery("select name, sum(calories), sum(carbohydrate), sum(protein), sum(fat), review, mealtime, address from Nutrition where Nutrition.mealdate==today and meal=1;", null);
            if(cursor != null){
                while(cursor.moveToNext()){
                    name.setText(cursor.getString(0));
                    cal.setText(cursor.getString(1)+" g");
                    car.setText(cursor.getString(2)+" g");
                    pro.setText(cursor.getString(3)+" g");
                    fat.setText(cursor.getString(4)+" g");
                    review.setText(cursor.getString(5));
                    time.setText(cursor.getString(6));
                    place.setText(cursor.getString(7));
                }
            }
        }

        if(selectMeal == 2){
            Cursor cursor = db.rawQuery("select name, sum(calories), sum(carbohydrate), sum(protein), sum(fat), review, mealtime, address from Nutrition where Nutrition.mealdate=?"+" and meal=2;", new String[]{today});
            if(cursor != null){
                while(cursor.moveToNext()){
                    name.setText(cursor.getString(0));
                    cal.setText(cursor.getString(1)+" g");
                    car.setText(cursor.getString(2)+" g");
                    pro.setText(cursor.getString(3)+" g");
                    fat.setText(cursor.getString(4)+" g");
                    review.setText(cursor.getString(5));
                    time.setText(cursor.getString(6));
                    place.setText(cursor.getString(7));
                }
            }
        }

        if(selectMeal == 3){
            Cursor cursor = db.rawQuery("select name, sum(calories), sum(carbohydrate), sum(protein), sum(fat), review, mealtime, address from Nutrition where Nutrition.mealdate=?"+" and meal=3;", new String[]{today});
            if(cursor != null){
                while(cursor.moveToNext()){
                    name.setText(cursor.getString(0));
                    cal.setText(cursor.getString(1)+" g");
                    car.setText(cursor.getString(2)+" g");
                    pro.setText(cursor.getString(3)+" g");
                    fat.setText(cursor.getString(4)+" g");
                    review.setText(cursor.getString(5));
                    time.setText(cursor.getString(6));
                    place.setText(cursor.getString(7));
                }
            }
        }
    }


}