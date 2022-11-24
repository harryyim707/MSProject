package com.example.dietapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

public class food_data_input extends AppCompatActivity {
    String food;
    private ImageButton srchBtn;
    private EditText foodName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_data_input);

        srchBtn = findViewById(R.id.searchBtn);
        foodName = findViewById(R.id.foodName);

        srchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                food = foodName.getText().toString();
                String url = "https://www.fatsecret.kr/칼로리-영양소/search?q="+food;
                Intent selectFood = new Intent(food_data_input.this, SelectFood.class);
                selectFood.putExtra("link", url);
                selectFood.putExtra("name", food);
                startActivity(selectFood);

            }
        });


    }
}