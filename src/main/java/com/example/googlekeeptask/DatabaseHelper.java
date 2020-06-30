package com.example.googlekeeptask;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static String TABLE_NAME = "CHECKLIST";
    public static String COL_ID = "ID";
    public static String COL_TITLE = "TITLE";
    public static String COL_ITEM = "ITEMS";


    public static String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_TITLE + " TEXT," + COL_ITEM + " TEXT)";

    public DatabaseHelper(Context context) {
        super(context, "title.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertDataToDatabase(RemainderItems remainderItems, SQLiteDatabase database) {
        ContentValues cv = new ContentValues();
       // cv.put(COL_ID, remainderItems.id);
        cv.put(COL_TITLE, remainderItems.title);
        cv.put(COL_ITEM, remainderItems.items);

        database.insert(TABLE_NAME, null, cv);
    }

    public void updateDataToDatabase(RemainderItems remainderItems, SQLiteDatabase database) {
        ContentValues cv = new ContentValues();
        cv.put(COL_ID, remainderItems.id);
        cv.put(COL_TITLE, remainderItems.title);
        cv.put(COL_ITEM, remainderItems.items);

        database.update(TABLE_NAME, cv, COL_ID + "=" + remainderItems.id, null);
    }

    public ArrayList<RemainderItems> getDataFromDatabase(SQLiteDatabase database) {
        ArrayList<RemainderItems> remainderItemsList = new ArrayList<>();

        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                RemainderItems data = new RemainderItems();
                data.id = cursor.getInt(cursor.getColumnIndex(COL_ID));
                data.title = cursor.getString(cursor.getColumnIndex(COL_TITLE));
                data.items = cursor.getString(cursor.getColumnIndex(COL_ITEM));

                remainderItemsList.add(data);
            } while (cursor.moveToNext());

            cursor.close();
        }
        return remainderItemsList;
    }

    public void deleteDataFromDatabase(RemainderItems remainderItems, SQLiteDatabase database) {
        database.delete(TABLE_NAME, COL_ID + " = " + remainderItems.id, null);
    }
}

