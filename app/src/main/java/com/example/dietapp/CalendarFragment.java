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
        calendarView = view.findViewById(R.id.calendarView);

//        init();
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {

                today.setText(year + "년" + (month + 1) + "월" + day + "일");
                selectDate = year + "-" + (month + 1) + "-" + day;

                Intent intent = new Intent(getActivity(), FoodListView.class);
                intent.putExtra("today", selectDate);
                startActivity(intent);

            }
        });
        return view;
    }

//    private void init(){
//        Date currentTime = java.util.Calendar.getInstance().getTime();
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
//        String selectDate = format.format(currentTime);
//        String tod = selectDate;
//        String[] date = selectDate.split("-");
//        today.setText(date[0]+"년"+date[1]+"월"+date[2]+"일");
//        Cursor cursor = db.rawQuery("select sum(calories), sum(carbohydrate), sum(protein), sum(fat) from Nutrition where Nutrition.mealdate=?"+" and meal=1;", new String[]{tod});
//        if(cursor != null){
//            while(cursor.moveToNext()){
//                brCal.setText(cursor.getString(0)+" kcal");
//                brCar.setText(cursor.getString(1)+" g");
//                brPro.setText(cursor.getString(2)+" g");
//                brFat.setText(cursor.getString(3)+" g");
//            }
//        }
//
//        cursor = db.rawQuery("select sum(calories), sum(carbohydrate), sum(protein), sum(fat) from Nutrition where Nutrition.mealdate=?"+" and meal=2;", new String[]{tod});
//        if(cursor != null){
//            while(cursor.moveToNext()){
//                lcCal.setText(cursor.getString(0)+" kcal");
//                lcCar.setText(cursor.getString(1)+" g");
//                lcPro.setText(cursor.getString(2)+" g");
//                lcFat.setText(cursor.getString(3)+" g");
//            }
//        }
//
//        cursor = db.rawQuery("select sum(calories), sum(carbohydrate), sum(protein), sum(fat) from Nutrition where Nutrition.mealdate=?"+" and meal=3;", new String[]{tod});
//        if(cursor != null){
//            while(cursor.moveToNext()){
//                dnCal.setText(cursor.getString(0)+" g");
//                dnCar.setText(cursor.getString(1)+" g");
//                dnPro.setText(cursor.getString(2)+" g");
//                dnFat.setText(cursor.getString(3)+" g");
//            }
//        }
//        cursor.close();
//    }
}