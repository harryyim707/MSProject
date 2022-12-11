package com.example.dietapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class FoodListView extends AppCompatActivity {

    DBManager db = null;
    ArrayList<InputItem> food_list = null;
    ListView lv_list;

    Intent intent = getIntent();
    String today = intent.getExtras().getString("today");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        lv_list = findViewById(R.id.foodList);
        db = new DBManager(this, DBManager.DB, null, DBManager.DB_VERSION);

        //데이터 목록 화면에 불러오기
        display_food_list();

    }

    public void display_food_list() {
        //데이터 목록 가져오기
        food_list = db.selectList(today);
        lv_list.setAdapter(new InputItemAdapter());
    }

    class InputItemAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return food_list.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_item, null);
            }

            //컨트롤의 참조값
            TextView when = convertView.findViewById(R.id.MealWhen);
            TextView name = convertView.findViewById(R.id.FoodName);
            TextView cal = convertView.findViewById(R.id.showcalval);
            TextView car = convertView.findViewById(R.id.showcarval);
            TextView pro = convertView.findViewById(R.id.showproval);
            TextView fat = convertView.findViewById(R.id.showfatval);

            //배치해야될 데이터 1개 얻어오기
            final InputItem fo = food_list.get(position);

            //컨트롤에 배치
            when.setText(fo.getWhen());
            name.setText(fo.getName());
            cal.setText(fo.getCal());
            car.setText(fo.getCar());
            pro.setText(fo.getPro());
            fat.setText(fo.getFat());

            return convertView;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }
    }

}