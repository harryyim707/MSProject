package com.example.dietapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBManager extends SQLiteOpenHelper {
    Context context = null;
    private static DBManager dbManager = null;
    static final int DB_VERSION = 1;
    static final String DB = "Nutrition.db";
    static final String TABLE_NAME = "Nutrition";
    static final String COLUMN_ID = "id";
    static final String COLUMN_WHEN = "meal";
    static final String COLUMN_NAME = "name";
    static final String COLUMN_CAL = "calories";
    static final String COLUMN_CAR = "carbohydrate";
    static final String COLUMN_PRO = "protein";
    static final String COLUMN_FAT = "fat";
    static final String COLUMN_REVIEW = "review";
    static final String COLUMN_DATE = "mealdate";
    static final String COLUMN_TIME = "mealtime";
    static final String COLUMN_IMG_DIR = "img_dir";
    static final String COLUMN_ADDRESS = "address";


    static final String CREATE_DB = "CREATE TABLE IF NOT EXISTS "+ TABLE_NAME+" ("+ COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+COLUMN_WHEN
            +" INTEGER NOT NULL, "+COLUMN_NAME+" TEXT NOT NULL, "+COLUMN_CAL+" INTEGER NOT NULL, "+COLUMN_CAR+" REAL NOT NULL, "+COLUMN_PRO+" REAL NOT NULL, "+
            COLUMN_FAT+" REAL NOT NULL, "+COLUMN_REVIEW+" TEXT, "+COLUMN_DATE+" DATE(10), "+COLUMN_TIME+" TEXT, "+COLUMN_IMG_DIR+" TEXT, "+COLUMN_ADDRESS+" TEXT);";

    public DBManager(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context=context;
    }

    @Override
    public void onOpen(SQLiteDatabase db){
        super.onOpen(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        db.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_NAME);
    }
    public void insertData(String name, int when, int cal, double car, double pro, double fat, @Nullable String review,
                           @Nullable String dateInfo, @Nullable String timeInfo, @Nullable String imgDir, @Nullable String address){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues addValues = new ContentValues();
        addValues.put(DBManager.COLUMN_NAME, name);
        addValues.put(DBManager.COLUMN_WHEN, when);
        addValues.put(DBManager.COLUMN_CAL, cal);
        addValues.put(DBManager.COLUMN_CAR, car);
        addValues.put(DBManager.COLUMN_PRO, pro);
        addValues.put(DBManager.COLUMN_FAT, fat);
        addValues.put(DBManager.COLUMN_REVIEW, review);
        addValues.put(DBManager.COLUMN_DATE, dateInfo);
        addValues.put(DBManager.COLUMN_TIME, timeInfo);
        addValues.put(DBManager.COLUMN_IMG_DIR, imgDir);
        addValues.put(DBManager.COLUMN_ADDRESS, address);
        db.insert(DBManager.TABLE_NAME,null,addValues);
    }
}
