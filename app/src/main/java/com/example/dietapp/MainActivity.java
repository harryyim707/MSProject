package com.example.dietapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void calculate(){

    }

    private void summary(){
        TextView cal = (TextView) findViewById(R.id.cal_val);
        TextView carbo = (TextView) findViewById(R.id.car_val);
        TextView prot = (TextView) findViewById(R.id.pro_val);
        TextView fat = (TextView) findViewById(R.id.fat_val);

        
    }
}