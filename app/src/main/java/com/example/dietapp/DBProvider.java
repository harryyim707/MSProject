package com.example.dietapp;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public class DBProvider extends ContentProvider {
    static final String PROVIDER_NAME = "com.example.DBProvider";
    static final String URL = "content://"+PROVIDER_NAME+"/Nutrition";
    static final Uri CONTENT_URI = Uri.parse(URL);
    static final String ID = "id";
    static final String NAME = "name";
    static final String CAL = "calories";
    static final String CAR = "carbohydrate";
    static final String PRO = "protein";
    static final String FAT = "fat";
    static final String REVIEW = "review";
    static final String TIME = "mealtime";
    static final String IMG = "img_dir";
    static final String ADDRESS = "address";
    public DBManager dbManager;

    public DBProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return dbManager.delete(selection, selectionArgs);
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowid = dbManager.insert(values);
        return null;
    }

    @Override
    public boolean onCreate() {
        dbManager = DBManager.getInstance(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        return dbManager.query(projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}