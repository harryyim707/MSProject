package com.example.dietapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarFragment extends Fragment {

    DBManager dbManager;
    SQLiteDatabase db = null;
    TextView today, br, brCal, brCar, brPro, brFat, lc, lcCal, lcCar, lcPro, lcFat, dn, dnCal, dnCar, dnPro, dnFat;
    CalendarView calendarView;


    public String selectDate;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CalendarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CalendarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalendarFragment newInstance(String param1, String param2) {
        CalendarFragment fragment = new CalendarFragment();
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
        dbManager = new DBManager(getActivity(), DBManager.DB, null, DBManager.DB_VERSION);
        db = dbManager.getReadableDatabase();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        today = view.findViewById(R.id.todayDate);

        br = view.findViewById(R.id.brName);
        brCal = view.findViewById(R.id.brCal);
        brCar = view.findViewById(R.id.brCar);
        brPro = view.findViewById(R.id.brPro);
        brFat = view.findViewById(R.id.brFat);

        lc = view.findViewById(R.id.lcName);
        lcCal = view.findViewById(R.id.lcCal);
        lcCar = view.findViewById(R.id.lcCar);
        lcPro = view.findViewById(R.id.lcPro);
        lcFat = view.findViewById(R.id.lcFat);

        dn = view.findViewById(R.id.dnName);
        dnCal = view.findViewById(R.id.dnCal);
        dnCar = view.findViewById(R.id.dnCar);
        dnPro = view.findViewById(R.id.dnPro);
        dnFat = view.findViewById(R.id.dnFat);

        calendarView =  view.findViewById(R.id.calendarView);

        init();
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                String selectDate;

                today.setText(year + "년" + (month + 1) + "월" + day + "일");
                selectDate = Integer.toString(year) + "-" + Integer.toString(month+1) + "-" + Integer.toString(day);

                Cursor cursor = db.rawQuery("select sum(calories), sum(carbohydrate), sum(protein), sum(fat) from Nutrition where Nutrition.mealdate=?"+" and meal=1;", new String[]{selectDate});
                String bCal = "0 kcal";
                String bC = "0 g";
                String bP = "0 g";
                String bF = "0 g";
                if(cursor != null){
                    while(cursor.moveToNext()){
                        bCal = cursor.getString(0)+" kcal";
                        bC = cursor.getString(1)+" g";
                        bP = cursor.getString(2)+" g";
                        bF = cursor.getString(3)+" g";
                    }
                }
                if(!bCal.contains("null")) brCal.setText(bCal);
                else brCal.setText("");
                if(!bC.contains("null")) brCar.setText(bC);
                else brCar.setText("");
                if(!bP.contains("null")) brPro.setText(bP);
                else brPro.setText("");
                if(!bF.contains("null")) brFat.setText(bF);
                else brFat.setText("");

                cursor = db.rawQuery("select sum(calories), sum(carbohydrate), sum(protein), sum(fat) from Nutrition where Nutrition.mealdate=?"+" and meal=2;", new String[]{selectDate});
                String lCal = "0 kcal";
                String lC = "0 g";
                String lP = "0 g";
                String lF = "0 g";
                if(cursor != null){
                    while(cursor.moveToNext()){
                        lCal = cursor.getString(0)+" kcal";
                        lC = cursor.getString(1)+" g";
                        lP = cursor.getString(2)+" g";
                        lF = cursor.getString(3)+" g";
                    }
                }
                if(!lCal.contains("null")) lcCal.setText(lCal);
                else lcCal.setText("");
                if(!lC.contains("null")) lcCar.setText(lC);
                else lcCar.setText("");
                if(!lP.contains("null")) lcPro.setText(lP);
                else lcPro.setText("");
                if(!lF.contains("null")) lcFat.setText(lF);
                else lcFat.setText("");

                cursor = db.rawQuery("select sum(calories), sum(carbohydrate), sum(protein), sum(fat) from Nutrition where Nutrition.mealdate=?"+" and meal=3;", new String[]{selectDate});
                String dCal = "0 kcal";
                String dC = "0 g";
                String dP = "0 g";
                String dF = "0 g";
                if(cursor != null){
                    while(cursor.moveToNext()){
                        dCal = cursor.getString(0)+" kcal";
                        dC = cursor.getString(1)+" g";
                        dP = cursor.getString(2)+" g";
                        dF = cursor.getString(3)+" g";
                    }
                }
                if(!dCal.contains("null")) dnCal.setText(dCal);
                else dnCal.setText("");
                if(!dC.contains("null")) dnCar.setText(dC);
                else dnCar.setText("");
                if(!dP.contains("null")) dnPro.setText(dP);
                else dnPro.setText("");
                if(!dF.contains("null")) dnFat.setText(dF);
                else dnFat.setText("");
                cursor.close();
            }
        });
        return view;
    }

    private void init(){
        Date currentTime = java.util.Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String selectDate = format.format(currentTime);
        String tod = selectDate;
        String[] date = selectDate.split("-");
        today.setText(date[0]+"년"+date[1]+"월"+date[2]+"일");
        Cursor cursor = db.rawQuery("select sum(calories), sum(carbohydrate), sum(protein), sum(fat) from Nutrition where Nutrition.mealdate=?"+" and meal=1;", new String[]{tod});
        String bCal = "0 kcal";
        String bC = "0 g";
        String bP = "0 g";
        String bF = "0 g";
        if(cursor != null){
            while(cursor.moveToNext()){
                bCal = cursor.getString(0)+" kcal";
                bC = cursor.getString(1)+" g";
                bP = cursor.getString(2)+" g";
                bF = cursor.getString(3)+" g";
            }
        }
        if(!bCal.contains("null")) brCal.setText(bCal);
        else brCal.setText("");
        if(!bC.contains("null")) brCar.setText(bC);
        else brCar.setText("");
        if(!bP.contains("null")) brPro.setText(bP);
        else brPro.setText("");
        if(!bF.contains("null")) brFat.setText(bF);
        else brFat.setText("");

        cursor = db.rawQuery("select sum(calories), sum(carbohydrate), sum(protein), sum(fat) from Nutrition where Nutrition.mealdate=?"+" and meal=2;", new String[]{tod});
        String lCal = "0 kcal";
        String lC = "0 g";
        String lP = "0 g";
        String lF = "0 g";
        if(cursor != null){
            while(cursor.moveToNext()){
                lCal = cursor.getString(0)+" kcal";
                lC = cursor.getString(1)+" g";
                lP = cursor.getString(2)+" g";
                lF = cursor.getString(3)+" g";
            }
        }
        if(!lCal.contains("null")) lcCal.setText(lCal);
        else lcCal.setText("");
        if(!lC.contains("null")) lcCar.setText(lC);
        else lcCar.setText("");
        if(!lP.contains("null")) lcPro.setText(lP);
        else lcPro.setText("");
        if(!lF.contains("null")) lcFat.setText(lF);
        else lcFat.setText("");

        cursor = db.rawQuery("select sum(calories), sum(carbohydrate), sum(protein), sum(fat) from Nutrition where Nutrition.mealdate=?"+" and meal=3;", new String[]{tod});
        String dCal = "0 kcal";
        String dC = "0 g";
        String dP = "0 g";
        String dF = "0 g";
        if(cursor != null){
            while(cursor.moveToNext()){
                dCal = cursor.getString(0)+" kcal";
                dC = cursor.getString(1)+" g";
                dP = cursor.getString(2)+" g";
                dF = cursor.getString(3)+" g";
            }
        }
        if(!dCal.contains("null")) dnCal.setText(dCal);
        else dnCal.setText("");
        if(!dC.contains("null")) dnCar.setText(dC);
        else dnCar.setText("");
        if(!dP.contains("null")) dnPro.setText(dP);
        else dnPro.setText("");
        if(!dF.contains("null")) dnFat.setText(dF);
        else dnFat.setText("");
        cursor.close();
    }
}