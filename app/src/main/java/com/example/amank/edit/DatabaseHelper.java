package com.example.amank.edit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by AmanK on 29-05-2017.
 */


public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME ="Taskdatabase.db";
    public static final String TABLE_NAME ="Task_table";
    public static final String COL_1 ="ID";
    public static final String COL_2 ="DOC_TITLE";
    public static final String COL_3 ="AMOUNT";
    public static final String COL_4 ="PHOTOS";



    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " +TABLE_NAME+ " (ID INTEGER PRIMARY KEY AUTOINCREMENT, DOC_TITLE TEXT, AMOUNT TEXT, PHOTOS BLOG)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_NAME);
        onCreate(db);

    }



    public  void insertData(String doc_title,String amount,byte[] photos){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_2,doc_title);
        contentValues.put(COL_3,amount);
        contentValues.put(COL_4,photos);

        db.insert(TABLE_NAME, null, contentValues);
    }

    public Cursor getData(){
        SQLiteDatabase db = getWritableDatabase();
        String query = "select * from " +TABLE_NAME;
        Cursor res = db.rawQuery(query,null);
        return res;
    }






}
