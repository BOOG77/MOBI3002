package com.codelab.basics;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executor;

public class DBClass extends SQLiteOpenHelper implements DB_Interface {

    public static final int DATABASE_VERSION = 5;

    // [20%] Change the DB to list Pokémon characters.
    public static final String DATABASE_NAME = "pokeDB.db"; // had to change this to match pokedb

    // If you change the database schema, you must increment the database version.
    private static final String TABLE_NAME = "pokeTable";
//    private static final String TEXT_TYPE = " TEXT";
//    private static final String NUM_TYPE = " INTEGER";
//    private static final String COMMA_SEP = ",";

    // [20%] Change the DB to list Pokémon characters.
    // all these needed to be changed to match the columns from the db
    private static final String _COL_1 = "pokeName";
    private static final String _COL_2 = "Description";
    private static final String _COL_3 = "Level";
    private static final String _COL_4 = "accessCount";
    private static final String _COL_5 = "id";
    // was using this when i was initially trying to make the images display
    // unneeded but keeping it so it still matches the db.
    private static final String _COL_6 = "image";

    // [20%] Change the DB to list Pokémon characters.
    // had to change this to create table if not exists to avoid crashing from the table already existing in the apps dp
    // also had to change it to match the create table arguments from the db
    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(PokeName TEXT, Level INTEGER, Description TEXT, accessCount INTEGER, id INTEGER)";
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
   }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i2) {
        Log.d("DBClass", "DB onUpgrade() to version " + DATABASE_VERSION);
        db.execSQL(SQL_DELETE_TABLE);
        onCreate(db);
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
        Log.v("DBClass", "add=>  " + dataModel.toString());

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        // [20%] Change the DB to list Pokémon characters.
        // these columns need to match the ones in your db
        ContentValues values = new ContentValues();
        values.put(_COL_1, dataModel.getPokeName());
        values.put(_COL_2, dataModel.getDescription());
        values.put(_COL_3, dataModel.getLevel());
        values.put(_COL_4, dataModel.getAccessCount());
        values.put(_COL_5, dataModel.getId());
        values.put(_COL_6, dataModel.getImage());

        // 3. insert
        db.insert(TABLE_NAME, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        //
        //db.execSQL("update .......");


        // 4. close
        db.close();

        // debug output to see what we're doing
        dump();

        return 0;
    }

    @Override
    public int update(DataModel dataModel) {
        return 0;
    }

    @Override
    public int deleteById(Long id) {
        return 0;
    }

    // Add Sample rows
    private void addDefaultRows(){
        // Call count once
        int doCount = this.count();
        if (doCount > 1) {
            Log.v("DBClass", "already rows in DB");
        }
    }

    @Override
    public List<DataModel> findAll() {
        List<DataModel> temp = new ArrayList<DataModel>();

        // if no rows, add
        addDefaultRows();

        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_NAME;

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build and add it to list
        DataModel item;
        if (cursor.moveToFirst()) {
            do {
                // This code puts a dataModel object into the PlaceHolder for the fragment
                // if you had more columns in the DB, you'd format  them in the non-details
                // list here
                item = new DataModel(cursor.getString(0), cursor.getString(2), cursor.getInt(1), cursor.getInt(3), cursor.getLong(4), cursor.getString(5));
                temp.add(item);
            } while (cursor.moveToNext());
        }

        Log.v("DBClass", "findAll=> " + temp.toString());
        cursor.close();
        db.close();

        // return all
        return temp;
    }




    @Override
    public String getNameById(Long id) {
        return null;
    }

    @Override
    public DataModel getMax() {
        return null;

        // get all

        // find max

        // return max datamodel

    }

    // [20%] Your compose main screen shows Favorite Pokémon and then the list of Pokémon.
    @Override
    public void incAccessCount(long id) {
        Log.v("DBClass", "id = " + id);

        String cmdString = "update " + TABLE_NAME + " set accessCount = accessCount + 1 where id="+id;
        Log.v("DBClass", "cmdString = " + cmdString);

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(cmdString);
        db.close();

    }

    // [20%] Your compose main screen shows Favorite Pokémon and then the list of Pokémon.
    @Override
    public long getMostAccessed() {
        // instead of using this i used names.maxByOrNull{it.accessCount} in the MainActivity
        // it returns the first element yielding the largest value of the given selector function or null if there are no elements.
        // https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/max-by-or-null.html
        // it.accessCount is passed in as the argument
        // it is similar to this. it refers to the current object in the instance
        // https://kotlinlang.org/docs/keyword-reference.html#special-identifiers

        // get most accessed ID from DB


        // loop through all records to get most accessed
        long mostID = 0;


        return 0;
    }

    // Dump the DB as a test
    private void dump() {
    }  // oops, never got around to this...but findall is dump-ish

}
