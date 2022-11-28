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
    static final String DB = "db";
    static final String TABLE_NAME = "Nutrition";
    static final String COLUMN_ID = "id";
    static final String COLUMN_NAME = "name";
    static final String COLUMN_CAL = "calories";
    static final String COLUMN_CAR = "cabohydrate";
    static final String COLUMN_PRO = "protein";
    static final String COLUMN_FAT = "fat";
    static final String COLUMN_REVIEW = "review";
    static final String COLUMN_TIME = "mealtime";
    static final String COLUMN_IMG_DIR = "img_dir";
    static final String COLUMN_ADDRESS = "address";


    static final String CREATE_DB = "CREATE TABLE IF NOT EXISTS "+ TABLE_NAME+" ("+ COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
            COLUMN_NAME+" TEXT NOT NULL, "+COLUMN_CAL+" INTEGER NOT NULL, "+COLUMN_CAR+" REAL NOT NULL, "+COLUMN_PRO+" REAL NOT NULL, "+
            COLUMN_FAT+" REAL NOT NULL, "+COLUMN_REVIEW+" TEXT, "+COLUMN_TIME+" TEXT, "+COLUMN_IMG_DIR+" TEXT, "+COLUMN_ADDRESS+" TEXT);";

    public static DBManager getInstance(Context context){
        if(dbManager == null){
            dbManager = new DBManager(context, DB, null, DB_VERSION);
        }
        return dbManager;
    }

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
        if(newVersion>DB_VERSION){
            db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
            db.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_NAME);
        }
    }

    public long insert(ContentValues addValue){
        return getWritableDatabase().insert(TABLE_NAME, null, addValue);
    }
    public Cursor query(String [] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy){
        return getReadableDatabase().query(TABLE_NAME, columns, selection, selectionArgs, groupBy, having, orderBy);
    }
    public int delete(String whereClause, String[] whereArgs){
        return getWritableDatabase().delete(TABLE_NAME, whereClause, whereArgs);
    }
}
