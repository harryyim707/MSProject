package com.example.dietapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Arrays;

public class food_data_input extends AppCompatActivity {
    String food;
    private ImageButton srchBtn;
    private EditText foodName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_data_input);

        final Bundle bundle = new Bundle();

        srchBtn = findViewById(R.id.searchBtn);
        foodName = findViewById(R.id.foodName);


        srchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                food = foodName.getText().toString();
                String url = "https://www.fatsecret.kr/칼로리-영양소/search?q="+food;

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
                                System.out.println("name: " +name+" brand: "+brand);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.d("test", "Error: "+e.toString());
                        }
                    }
                }.start();
            }
        });


    }
}