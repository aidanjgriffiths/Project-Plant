package com.example.plantproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";

    private static final String TABLE_NAME = "profile_table";
    private static final String COL1 = "ID";
    private static final String COL2 = "name";
    private static final String COL3 = "class_";
    private static final String COL4 = "moist_min";
    private static final String COL5 = "moist_max";
    private static final String COL6 = "temp_min";
    private static final String COL7 = "temp_max";
    private static final String COL8 = "humid_min";
    private static final String COL9 = "humid_max";
    private static final String COL10 = "light_min";
    private static final String COL11 = "light_max";
    private static final String COL12 = "act_moist";
    private static final String COL13 = "act_temp";
    private static final String COL14 = "act_humid";
    private static final String COL15 = "act_light";

    public DatabaseHelper(@Nullable Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    // create the database
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" + COL1 + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL2 + " TEXT, " + COL3 + " TEXT, " + COL4 + " REAL, " + COL5 + " REAL, " + COL6 + " REAL,"
                + COL7 + " REAL," + COL8 + " REAL, " + COL9 + " REAL, " + COL10 + " REAL, " + COL11 + " REAL, "
                + COL12 + " REAL, " + COL13 + " REAL, " + COL14 + " REAL, "+ COL15 + " REAL)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // add single profile to the database
    public boolean addData(String name, String class_, Double moist_min, Double moist_max, Double temp_min,
                           Double temp_max, Double humid_min, Double humid_max, Double light_min, Double light_max,
                           Double act_moist, Double act_temp, Double act_humid, Double act_light){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, name);
        contentValues.put(COL3, class_);
        contentValues.put(COL4, moist_min);
        contentValues.put(COL5, moist_max);
        contentValues.put(COL6, temp_min);
        contentValues.put(COL7, temp_max);
        contentValues.put(COL8, humid_min);
        contentValues.put(COL9, humid_max);
        contentValues.put(COL10, light_min);
        contentValues.put(COL11, light_max);
        contentValues.put(COL12, act_moist);
        contentValues.put(COL13, act_temp);
        contentValues.put(COL14, act_humid);
        contentValues.put(COL15, act_light);


        Log.d(TAG, "addData: Adding " + name + " to " + TABLE_NAME);
        long result = db.insert(TABLE_NAME, null, contentValues);

        //if data inserted incorrectly, return -1
        if(result == -1) {
            return false;
        }else{
            return true;
        }
    }
    // adding profiles to the database function
    public boolean editData(int id, String new_name, String class_, Double moist_min, Double moist_max, Double temp_min,
                            Double temp_max, Double humid_min, Double humid_max, Double light_min, Double light_max,
                            Double act_moist, Double act_temp, Double act_humid, Double act_light){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, new_name);
        contentValues.put(COL3, class_);
        contentValues.put(COL4, moist_min);
        contentValues.put(COL5, moist_max);
        contentValues.put(COL6, temp_min);
        contentValues.put(COL7, temp_max);
        contentValues.put(COL8, humid_min);
        contentValues.put(COL9, humid_max);
        contentValues.put(COL10, light_min);
        contentValues.put(COL11, light_max);
        contentValues.put(COL12, act_moist);
        contentValues.put(COL13, act_temp);
        contentValues.put(COL14, act_humid);
        contentValues.put(COL15, act_light);


        Log.d(TAG, "editData: Updating " + id + " to " + TABLE_NAME);
        long result = db.update(TABLE_NAME, contentValues, "id='" + id + "'", null);

        //if data inserted incorrectly, return -1
        if(result == -1) {
            return false;
        }else{
            return true;
        }
    }

    // returns the data from the database
    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + COL2 + " COLLATE NOCASE ASC";
        return db.rawQuery(query, null);
    }
    // returns single profile from the database
    public Cursor getSingleProfile(String prof) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL2 + "= '" + prof + "'";
        return db.rawQuery(query, null);
    }
    // queries single profile from the database
    public Boolean checkSingleProfile(String prof) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL2 + "= '" + prof + "'";
        Cursor profile = db.rawQuery(query, null);
        if(profile.getCount() <= 0){
            return false;
        }
        return true;
    }
    // delete single profile from the database
    public void deleteSingleProfile(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME+ " WHERE " + COL2 + "= '" + name + "'");
        db.close();
    }
}
