package com.example.m03_bounce;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class DBClass extends SQLiteOpenHelper implements DB_Interface {

    public static final int DATABASE_VERSION = 8;
    public static final String DATABASE_NAME = "Bounce_DB_Name.db";

    // If you change the database schema, you must increment the database version.
    private static final String TABLE_NAME = "sample_table";
    private static final String TEXT_TYPE = " TEXT";
    private static final String NUM_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String _ID = "_ID";
    private static final String _COL_1 = "X";  // Names of the columns
    private static final String _COL_2 = "Y";
    private static final String _COL_3 = "DX";
    private static final String _COL_4 = "DY";
    private static final String _COL_5 = "color";
    private static final String _COL_6 = "name";

    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "X FLOAT, Y FLOAT, DX FLOAT, DY FLOAT, color INTEGER, name TEXT)";
    private static final String SQL_DELETE_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME;


    public DBClass(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    /**
     * This JavaDoc goes with this method type / * * and hit enter
     */
    public void onCreate(SQLiteDatabase db) {

        Log.d("DBClass", "DB onCreate() " + SQL_CREATE_TABLE);
        db.execSQL(SQL_CREATE_TABLE);
        Log.d("DBClass", "DB onCreate()");

//        db.execSQL("INSERT INTO " + TABLE_NAME + " (X, Y, DX, DY)    VALUES" +
//                "(3.0, 22.1, 0.5, 0.7)," +
//                "(132.0, 22.3, 0.5, -1.7)," +
//                "(134.0, 122.5, 0.5, 2.7)," +
//                "(163.0, 122.7, 0.5, -6.7)," +
//                "(283.0, 222.9, 0.5, 4.7)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i2) {
        Log.d("DBClass", "DB onUpgrade() to version " + DATABASE_VERSION);
        db.execSQL(SQL_DELETE_TABLE);
        onCreate(db);
    }

    public void emptyTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
        db.close();
    }

    /////////// Implement Interface ///////////////////////////
    @Override
    public int count() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        Log.v("DBClass", "getCount=" + cnt);
        return cnt;
    }

    @Override
    public int save(DataModel dataModel) {
        Log.v("DBClass", "save()=>  " + dataModel.toString());

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        //emptyTable();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(_COL_1, dataModel.getModelX());
        values.put(_COL_2, dataModel.getModelY());
        values.put(_COL_3, dataModel.getModelDX());
        values.put(_COL_4, dataModel.getModelDY());
        values.put(_COL_5, dataModel.getColor());
        values.put(_COL_6, dataModel.getName());

        // 3. insert
        db.insert(TABLE_NAME, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();

        // debug output to see what we're doing
        dump();

        return 0;
    }

    public void saveList(List<DataModel> dataList){
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();

        for (DataModel dataModel : dataList) {
            Log.v("DBClass", "saveList()=>  " + dataModel.toString());

            // 3. Prepare ContentValues
            ContentValues values = new ContentValues();
            values.put(_COL_1, dataModel.getModelX());
            values.put(_COL_2, dataModel.getModelY());
            values.put(_COL_3, dataModel.getModelDX());
            values.put(_COL_4, dataModel.getModelDY());
            values.put(_COL_5, dataModel.getColor());
            values.put(_COL_6, dataModel.getName());

            // 4. Insert (inside the transaction)
            db.insert(TABLE_NAME, null, values);
        }

        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        dump();
    }

    @Override
    public int update(DataModel dataModel) {
        return 0;
    }

    @Override
    public int deleteById(Long id) {
        return 0;
    }

    private Random r = new Random();

//    // Add Sample rows
//    private void addDefaultRows() {
//        // Call count once
//        int doCount = this.count();
//        if (doCount > 2) {
//            Log.v("DBClass", "already rows in DB");
//
//        } else {
//            Log.v("DBClass", "n...add some rows");
//            DataModel a = new DataModel(1, 20.0f,20.0f,-4.0f,4.0f);
//            this.save(a);
//            a = new DataModel(2, 30f,30f,3f,-3f);
//            this.save(a);
////            a = new DataModel(3, 40f,40f,1f,4f);
////            this.save(a);
////            a = new DataModel(4, 60f,60f,-4f,1f);
////            this.save(a);
//
//        }

//        DataModel a = new DataModel(1, "Rusty Bucket", r.nextInt(500));
//        this.save(a);
//    }

    @Override
    public List<DataModel> findAll() {
        List<DataModel> temp = new ArrayList<DataModel>();

        // if no rows, add
        //addDefaultRows();

        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_NAME;

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build and add it to list
        DataModel item;
        if (cursor.moveToFirst()) {
            do {
                item = new DataModel(cursor.getInt(0), cursor.getFloat(1), cursor.getFloat(2), cursor.getFloat(3), cursor.getFloat(4), cursor.getInt(5), cursor.getString(6) );
                temp.add(item);
            } while (cursor.moveToNext());
        }

        Log.v("DBClass", "findAll=> " + temp.toString());

        // return all
        return temp;
    }

    @Override
    public String getNameById(Long id) {





        return null;
    }

    // Dump the DB as a test
    private void dump() {
    }  // oops, never got around to this...but findall is dump-ish

}
