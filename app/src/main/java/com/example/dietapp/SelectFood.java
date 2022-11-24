package com.example.dietapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class SelectFood extends AppCompatActivity {

    private TextView foodName;
    private ListView foodList = null;
//    private ListViewAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_food);

        Intent intent = getIntent();
        String url = intent.getStringExtra("link");
        String name = intent.getStringExtra("name");

        foodName = findViewById(R.id.foodToSrch);
        foodName.setText(name);

        foodList = findViewById(R.id.foodList);
//        adapter = new ListViewAdapter();

        new Thread(){
            @Override
            public void run(){
                Document doc = null;
                try{
                    doc = Jsoup.connect(url).get();
                    Elements contents = doc.select("td.borderBottom");
                    for(Element c : contents){
                        String name = c.getElementsByClass("prominent").text();
                        String brand = c.getElementsByClass("brand").text();
                        Elements divTag = c.select("div");
                        String[] info = divTag.text().split(" ",15);
                        System.out.println(name+" "+brand+" ");

                        String servingSize = info[0];
                        String servingWeight = info[1];
                        Integer standard = Integer.parseInt(servingSize.substring(0,1));
                        int idx = servingWeight.indexOf("g");
                        Integer weight = Integer.parseInt(servingWeight.substring(1,idx));

                        String calories = info[4];
                        int idx_cal = calories.indexOf("k");
                        Integer cal = Integer.parseInt(calories.substring(0, idx_cal));

                        String fats = info[7];
                        int idx_fats = fats.indexOf("g");
                        Float fat = Float.parseFloat(fats.substring(0, idx_fats));

                        String carbo = info[10];
                        int idx_carbo = carbo.indexOf("g");
                        Float car = Float.parseFloat(carbo.substring(0, idx_carbo));

                        String protein = info[13];
                        int idx_pro = protein.indexOf("g");
                        Float pro = Float.parseFloat(protein.substring(0, idx_pro));

                        System.out.println(standard+" "+weight+" "+" "+cal+" "+fat+" "+car+" "+pro);
//                        adapter.addItem(new FoodItem(name, brand));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("test", "Error: "+e.toString());
                }
            }
        }.start();
    }

//    private class ListViewAdapter extends BaseAdapter {
//        ArrayList<FoodItem> items = new ArrayList<FoodItem>();
//
//        @Override
//        public int getCount() {
//            return items.size();
//        }
//
//        public void addItem(FoodItem item){
//            items.add(item);
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return items.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            return null;
//        }
//    }
}