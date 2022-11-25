package com.example.dietapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class food_data_input extends AppCompatActivity {
    String food;
    ImageButton srchBtn;
    private EditText foodName;
    private EditText foodAmount;
    private TextView calVal;
    private TextView carVal;
    private TextView proVal;
    private TextView fatVal;
    private SingleItem item;
    private TextView nu;
    EditText review;
    EditText timeInput;
    Button svBtn;
    Button delBtn;

    Integer number;
    Integer tmpcal;
    Float tmpcar;
    Float tmppro;
    Float tmpfat;
    String reviewStr;
    String inputTime;

    String info;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_data_input);

        srchBtn = (ImageButton) findViewById(R.id.searchBtn);
        foodName = (EditText) findViewById(R.id.foodName);
        foodAmount = (EditText) findViewById(R.id.foodAmount);
        calVal = (TextView) findViewById(R.id.calval);
        carVal = (TextView) findViewById(R.id.carval);
        proVal = (TextView) findViewById(R.id.proval);
        fatVal = (TextView) findViewById(R.id.fatval);
        nu=(TextView) findViewById(R.id.textView4);
        review = (EditText) findViewById(R.id.foodReview);
        timeInput = (EditText) findViewById(R.id.inputTime);
        svBtn = (Button) findViewById(R.id.saveBtn);
        delBtn = (Button) findViewById(R.id.delBtn);

        reviewStr = review.getText().toString();
        inputTime = timeInput.getText().toString();



        srchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                food = foodName.getText().toString();
                String url = "https://www.fatsecret.kr/칼로리-영양소/search?q="+food;
                Intent selectFood = new Intent(food_data_input.this, SelectFood.class);
                selectFood.putExtra("link", url);
                selectFood.putExtra("name", food);
                launcher.launch(selectFood);
            }
        });
    }

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == RESULT_OK){
                        Intent intent = result.getData();
                        info = intent.getStringExtra("result");

                        String[] tmp = info.split(",");
                        item = new SingleItem(tmp[0], tmp[1], Integer.parseInt(tmp[2]), Integer.parseInt(tmp[3]),
                                Integer.parseInt(tmp[4]), Float.parseFloat(tmp[5]), Float.parseFloat(tmp[6]), Float.parseFloat(tmp[7]));
                        nu.setText(""+item.getStandard()+" 개/ "+item.getWeight()+" g 당 영양성분");
                        AfterFoodInput();
                    }
                }
            });
    protected void AfterFoodInput(){
        String str = foodAmount.getText().toString().trim();
        if(str.equals("")){
            str="1";
        }
        number = Integer.parseInt(str);
        tmpcal = item.getCalVal()*number;
        tmpcar = item.getCarVal()*number;
        tmppro = item.getProVal()*number;
        tmpfat = item.getCarVal()*number;

        calVal.setText(""+tmpcal);
        carVal.setText(""+tmpcar);
        proVal.setText(""+tmppro);
        fatVal.setText(""+tmpfat);
    }

    protected void endOfSeq(){
        svBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //save in db
            }
        });
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}