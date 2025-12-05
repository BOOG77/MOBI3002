package com.example.m03_bounce;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class) // https://developer.android.com/training/testing/instrumented-tests/androidx-test-libraries/runner#java
public class DawsonsDbTests {

    private DBClass dbClass;
    private SQLiteDatabase db;
    private static final String TABLE_NAME = "tempTable";
    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "X FLOAT, Y FLOAT, DX FLOAT, DY FLOAT, color INTEGER, name TEXT NOT NULL)";

    // https://developer.android.com/training/data-storage/room/testing-db
    // ^ test and debug your database android source
    @Before
    public void setup(){
        Context context = ApplicationProvider.getApplicationContext();
        dbClass = new DBClass(context);
        db = dbClass.getWritableDatabase();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void testingCreatingTable(){
        db.execSQL(SQL_CREATE_TABLE);

        // https://tableplus.com/blog/2018/04/sqlite-check-whether-a-table-exists.html
        // ^ how to query if a table exists
        String query = "SELECT name FROM sqlite_master WHERE type='table' AND name='" + TABLE_NAME + "'";

        // try to query the table, if it fails then an exception will be thrown that will make the test fail.
        try{
            Cursor cursor = db.rawQuery(query, null);
            assertTrue("Table was created", true);
            cursor.close();
        } catch (Exception e) {
            fail("Table wasnt created");
        }
    }

    @Test
    public void testingFindAll(){
        db.execSQL(SQL_CREATE_TABLE);
        List<DataModel> temp = new ArrayList<DataModel>();

        String query = "SELECT  * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);

        DataModel item;
        if (cursor.moveToFirst()) {
            do {
                item = new DataModel(cursor.getInt(0), cursor.getFloat(1), cursor.getFloat(2), cursor.getFloat(3), cursor.getFloat(4), cursor.getInt(5), cursor.getString(6) );
                temp.add(item);
            } while (cursor.moveToNext());
        }

        if(!temp.isEmpty()){ // if temp list isnt empty. (it would be empty if the cursor failed to add items to the list)
            assertTrue("List was populated, meaning findAll works", true);
        }else{
            fail("List wasnt populated, meaning findAll isnt working");
        }
        cursor.close();
    }

    @Test
    public void testingSave(){
        db.execSQL(SQL_CREATE_TABLE);

        DataModel dataModel = new DataModel(0, 100F, 100F, 5F, 5F, Color.RED, "Dawson");

        ContentValues values = new ContentValues();
        values.put("X", dataModel.getModelX());
        values.put("Y", dataModel.getModelY());
        values.put("DX", dataModel.getModelDX());
        values.put("DY", dataModel.getModelDY());
        values.put("color", dataModel.getColor());
        values.put("name", dataModel.getName());

        try {
            //db.close(); the switch to make it fail on purpose
            db.insert(TABLE_NAME, null, values);
            assertTrue("Insert command succeeded", true);
        } catch (Exception e) {
            fail("Insert command failed, db didnt save");
        }
    }

    @Test
    public void testingSaveList(){
        db.execSQL(SQL_CREATE_TABLE);
        // make a list with three datamodels
        List<DataModel> dataList = new ArrayList<>();
        DataModel dataOne = new DataModel(0, 100F, 100F, 5F, 5F, Color.RED, "Dawson");
        DataModel dataTwo = new DataModel(0, 100F, 100F, 5F, 5F, Color.RED, "Suzy");
        DataModel dataThree = new DataModel(0, 100F, 100F, 5F, 5F, Color.RED, "Alfred");

        dataList.add(dataOne);
        dataList.add(dataTwo);
        dataList.add(dataThree);

        // add them to the db
        for(DataModel dataModel : dataList){
            ContentValues values = new ContentValues();
            values.put("X", dataModel.getModelX());
            values.put("Y", dataModel.getModelY());
            values.put("DX", dataModel.getModelDX());
            values.put("DY", dataModel.getModelDY());
            values.put("color", dataModel.getColor());
            values.put("name", dataModel.getName());

            db.insert(TABLE_NAME, null, values);
        }

        // count the lines of the db to make sure its 3
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        int lineCount = 0;
        while(cursor.moveToNext()){
            lineCount++;
        }
        cursor.close();

        assertEquals(3, lineCount);
    }

    @Test
    public void testingEmptyTable(){
        db.execSQL(SQL_CREATE_TABLE);

        DataModel dataModel = new DataModel(0, 100F, 100F, 5F, 5F, Color.RED, "Dawson");
        ContentValues values = new ContentValues();
        values.put("X", dataModel.getModelX());
        values.put("Y", dataModel.getModelY());
        values.put("DX", dataModel.getModelDX());
        values.put("DY", dataModel.getModelDY());
        values.put("color", dataModel.getColor());
        values.put("name", dataModel.getName());

        db.insert(TABLE_NAME, null, values); // comment this line to make the first test fail
        
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);

        if(!cursor.moveToNext()){
            fail("Empty Table method wasn't tested, the test failed because the DB wasnt populated");
        }

        db.execSQL("DELETE FROM " + TABLE_NAME); // comment this line to make the second part of the test fail
        cursor = db.rawQuery(query, null); // gotta refresh the cursor with the updated db info
        if(!cursor.moveToNext()){
            assertTrue("DB was cleared, test successful", true);
        }else{
            fail("DB wasn't cleared");
        }
        cursor.close();
    }
}
