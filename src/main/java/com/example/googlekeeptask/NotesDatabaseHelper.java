package com.example.googlekeeptask;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class NotesDatabaseHelper extends SQLiteOpenHelper {
    public static String TABLE_NAME = "NOTES";
    public static String COL_ID = "NOTE_ID";
    public static String COL_TITLE = "NOTE_TITLE";
    public static String COL_CONTENT = "CONTENT";

    public static String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_TITLE + " TEXT," + COL_CONTENT + " TEXT)";


    public NotesDatabaseHelper(Context context) {
        super(context, "notes.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertNote(RemainderItems remainderItems, SQLiteDatabase database) {
        ContentValues cv = new ContentValues();
        cv.put(COL_TITLE, remainderItems.title);
        cv.put(COL_CONTENT, remainderItems.items);
        database.insert(TABLE_NAME, null, cv);
    }

    public ArrayList<RemainderItems> getNote(SQLiteDatabase database){
        ArrayList<RemainderItems> remainderItems = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME,null);
        if(cursor.moveToFirst()){
            do{
                RemainderItems note = new RemainderItems();
                note.title = cursor.getString(cursor.getColumnIndex(COL_TITLE));
                note.items = cursor.getString(cursor.getColumnIndex(COL_CONTENT));
            }while (cursor.moveToNext());
            cursor.close();
        }
        return remainderItems;
    }
}
