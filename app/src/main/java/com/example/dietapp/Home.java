package com.example.dietapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home extends Fragment {

    private TextView calVal;
    private TextView carVal;
    private TextView proVal;
    private TextView fatVal;
    private ImageButton addBtn1;
    private ImageButton addBtn2;
    private ImageButton addBtn3;
    TextView sumBr;
    TextView sumLc;
    TextView sumDn;
    TextView brCar, brPro, brFat, lcCar, lcPro, lcFat, dnCar, dnPro, dnFat;
    Button brName, lcName, dnName;

    SimpleDateFormat format;
    String today;
    DBManager dbManager;
    SQLiteDatabase db = null;




    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Home() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Home.
     */
    // TODO: Rename and change types and number of parameters
    public static Home newInstance(String param1, String param2) {
        Home fragment = new Home();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        dbManager = new DBManager(getActivity(), DBManager.DB, null, DBManager.DB_VERSION);
        db = dbManager.getWritableDatabase();
        // create views
        calVal = (TextView) view.findViewById(R.id.cal_val);
        carVal = (TextView) view.findViewById(R.id.car_val);
        proVal = (TextView) view.findViewById(R.id.pro_val);
        fatVal = (TextView) view.findViewById(R.id.fat_val);
        sumBr = (TextView) view.findViewById(R.id.sumBr);
        sumLc = (TextView) view.findViewById(R.id.sumLc);
        sumDn = (TextView) view.findViewById(R.id.sumDn);

        brName = view.findViewById(R.id.brName);
        brCar = view.findViewById(R.id.brCar);
        brPro = view.findViewById(R.id.brPro);
        brFat = view.findViewById(R.id.brFat);

        lcName = view.findViewById(R.id.lcName);
        lcCar = view.findViewById(R.id.lcCar);
        lcPro = view.findViewById(R.id.lcPro);
        lcFat = view.findViewById(R.id.lcFat);

        dnName = view.findViewById(R.id.dnName);
        dnCar = view.findViewById(R.id.dnCar);
        dnPro = view.findViewById(R.id.dnPro);
        dnFat = view.findViewById(R.id.dnFat);

        //apply click event on buttons
        addBtn1 = view.findViewById(R.id.addBtn1);
        addBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), food_data_input.class);
                intent.putExtra("meal", 1);
                startActivity(intent);
            }
        });
        addBtn2 = view.findViewById(R.id.addBtn2);
        addBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), food_data_input.class);
                intent.putExtra("meal", 2);
                startActivity(intent);
            }
        });
        addBtn3 = view.findViewById(R.id.addBtn3);
        addBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), food_data_input.class);
                intent.putExtra("meal", 3);
                startActivity(intent);
            }
        });

        brName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), food_data_view.class);
                intent.putExtra("selectMeal", 1);
                startActivity(intent);
            }
        });
        lcName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), food_data_view.class);
                intent.putExtra("selectMeal", 2);
                startActivity(intent);
            }
        });
        dnName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), food_data_view.class);
                intent.putExtra("selectMeal", 3);
                startActivity(intent);
            }
        });
        //get DB
        getDB();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getDB();
    }

    private void getDB() {
        Date currentTime = java.util.Calendar.getInstance().getTime();
        format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        today = format.format(currentTime);
        Cursor c = db.rawQuery("select sum(calories), sum(carbohydrate), sum(protein), sum(fat) from Nutrition where "
                +DBManager.COLUMN_DATE+"=?", new String[]{today});
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
        }
        calVal.setText(String.format("%d", sumCal));
        carVal.setText(String.format("%.2f", sumCar));
        proVal.setText(String.format("%.2f", sumPro));
        fatVal.setText(String.format("%.2f", sumFat));
        c.close();

        Cursor cb = db.rawQuery("select sum(calories) from nutrition where mealdate =? and meal=?", new String[]{today, "1"});
        int brSum = 0;
        if(cb!=null){
            while(cb.moveToNext()){
                brSum = cb.getInt(0);
            }
            sumBr.setText(String.format("%d kcal", brSum));
        }
        cb = db.rawQuery("select sum(carbohydrate), sum(protein), sum(fat) from Nutrition where Nutrition.mealdate=?"+" and meal=1;", new String[]{today});
        String bC = "0 g";
        String bP = "0 g";
        String bF = "0 g";
        if(cb != null){
            while(cb.moveToNext()){
                bC = cb.getString(0)+" g";
                bP = cb.getString(1)+" g";
                bF = cb.getString(2)+" g";
            }
        }
        if(!bC.contains("null")) brCar.setText(bC);
        else brCar.setText("");
        if(!bP.contains("null")) brPro.setText(bP);
        else brPro.setText("");
        if(!bF.contains("null")) brFat.setText(bF);
        else brFat.setText("");
        cb.close();

        Cursor cl = db.rawQuery("select sum(calories) from nutrition where mealdate =? and meal=?", new String[]{today, "2"});
        int lcSum = 0;
        if(cl!=null){
            while(cl.moveToNext()){
                lcSum = cl.getInt(0);
            }
            sumLc.setText(String.format("%d kcal", lcSum));
        }
        cl = db.rawQuery("select sum(carbohydrate), sum(protein), sum(fat) from Nutrition where Nutrition.mealdate=?"+" and meal=2;", new String[]{today});
        String lC = "0 g";
        String lP = "0 g";
        String lF = "0 g";
        if(cl != null){
            while(cl.moveToNext()){
                lC = cl.getString(0)+" g";
                lP = cl.getString(1)+" g";
                lF = cl.getString(2)+" g";
            }
        }
        if(!lC.contains("null")) lcCar.setText(lC);
        else lcCar.setText("");
        if(!lP.contains("null")) lcPro.setText(lP);
        else lcPro.setText("");
        if(!lF.contains("null")) lcFat.setText(lF);
        else lcFat.setText("");
        cl.close();
        Cursor cd = db.rawQuery("select sum(calories) from nutrition where mealdate =? and meal=?", new String[]{today, "3"});
        int dnSum = 0;
        if(cd!=null){
            while(cd.moveToNext()){
                dnSum = cd.getInt(0);
            }
            sumDn.setText(String.format("%d kcal", dnSum));
        }
        cd = db.rawQuery("select sum(carbohydrate), sum(protein), sum(fat) from Nutrition where Nutrition.mealdate=?"+" and meal=3;", new String[]{today});
        String dC = "0 g";
        String dP = "0 g";
        String dF = "0 g";
        if(cd != null){
            while(cd.moveToNext()){
                dC = cd.getString(0)+" g";
                dP = cd.getString(1)+" g";
                dF = cd.getString(2)+" g";
            }
        }
        if(!dC.contains("null")) dnCar.setText(dC);
        else dnCar.setText("");
        if(!dP.contains("null")) dnPro.setText(dP);
        else dnPro.setText("");
        if(!dF.contains("null")) dnFat.setText(dF);
        else dnFat.setText("");
        cd.close();
    }
}