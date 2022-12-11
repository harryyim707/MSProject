package com.example.dietapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;


public class SelectFood extends AppCompatActivity {
    private ListView foodList;
    String url;
    ArrayList<SingleItem> itemList;
    SingleItemAdapter adapter = null;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_food);

        Intent intent = getIntent();
        url = intent.getStringExtra("link");
        String name = intent.getStringExtra("name");

        foodList = findViewById(R.id.listView);
        textView = findViewById(R.id.nodata);
        itemList = new ArrayList<SingleItem>();

        textView.setText("Loading...");

        NewThread newThread = new NewThread();
        newThread.execute();

        foodList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                SingleItem item = itemList.get(position);
                String res = item.getName()+","+item.getBrand()+","+item.getStandard().toString()+","+item.getWeight().toString()+","
                        +item.getCalVal().toString()+","+item.getCarVal().toString()+","+item.getProVal().toString()+","+item.getFatVal().toString();
                intent.putExtra("result", res);
                setResult(Activity.RESULT_OK, intent);
                try{
                    if(newThread.getStatus() == AsyncTask.Status.RUNNING){
                        newThread.cancel(true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finish();
            }
        });
    }


    private class NewThread extends AsyncTask<String, Void, Void>{

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... strings) {
            try{
                Document doc = Jsoup.connect(url).get();
                Elements contents = doc.select("td.borderBottom");
                if(!contents.isEmpty()){
                    textView.setVisibility(View.INVISIBLE);
                    for(Element c : contents){
                        String name = c.getElementsByClass("prominent").text();
                        String brand = c.getElementsByClass("brand").text();

                        Elements divTag = c.select("div");
                        String[] info = divTag.text().split(" ",15);

                        String servingSize = info[0];
                        String servingWeight = info[1];

                        String standard = info[0];
                        String weight = info[1];

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

                        itemList.add(new SingleItem(name, brand, standard, weight, cal, car, pro, fat));
                    }
                }
                else{
                    textView.setVisibility(View.VISIBLE);
                    textView.setText("No Data");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            adapter = new SingleItemAdapter(itemList, getApplicationContext());
            foodList.setAdapter(adapter);
        }
    }
}