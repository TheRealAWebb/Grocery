package com.example.awebber.grocery;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.HashSet;

/**
 * Title: TestDb.java
 * Created by Alton Webber on 4/15/15.
 * Description:
 *
 * Purpose:
 *
 *
 *
 * Usage:
 *
 *
 */
public class TestDb extends AndroidTestCase {

    public static final String LOG_TAG = TestDb.class.getSimpleName();

    // Since we want each test to start with a clean slate
    void deleteTheDatabase() {
        mContext.deleteDatabase(GroceryDbHelper.DATABASE_NAME);
    }

    /*
        This function gets called before each test is executed to delete the database.  This makes
        sure that we always have a clean test.     */
    public void setUp() {
        deleteTheDatabase();
    }

     /*
        Students: Uncomment this test once you've written the code to create the Location
        table.  Note that you will have to have chosen the same column names that I did in
        my solution for this test to compile, so if you haven't yet done that, this is
        a good time to change your column names to match mine.

        Note that this only tests that the Location table has the correct columns, since we
        give you the code for the weather table.  This test does not look at the
     */
    public void testCreateDb() throws Throwable {
        // build a HashSet of all of the table names we wish to look for
        // Note that there will be another table in the DB that stores the
        // Android metadata (db version information)
        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(GroceryContract.BasicDescriptionEntry.TABLE_NAME);
        tableNameHashSet.add(GroceryContract.BrandEntry.TABLE_NAME);
        tableNameHashSet.add(GroceryContract.GroceryEntry.TABLE_NAME);

        mContext.deleteDatabase(GroceryDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new GroceryDbHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        // have we created the tables we want?
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error: This means that the database has not been created correctly",
                c.moveToFirst());

        // verify that the tables have been created
        do {
            tableNameHashSet.remove(c.getString(0));
        } while( c.moveToNext() );

        // if this fails, it means that your database doesn't contain both the location entry
        // and weather entry tables
        assertTrue("Error: Your database was created without both the location entry and weather entry tables",
                tableNameHashSet.isEmpty());

        // now, do our tables contain the correct columns?
        c = db.rawQuery("PRAGMA table_info(" + GroceryContract.GroceryEntry.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> GroceryColumnHashSet = new HashSet<String>();
        GroceryColumnHashSet.add(GroceryContract.GroceryEntry._ID);
        GroceryColumnHashSet.add(GroceryContract.GroceryEntry.COLUMN_NAME);
        GroceryColumnHashSet.add(GroceryContract.GroceryEntry.COLUMN_BRAND_LOC_KEY);
        GroceryColumnHashSet.add(GroceryContract.GroceryEntry.COLUMN_BASIC_DESC_LOC_KEY);


        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            GroceryColumnHashSet.remove(columnName);
        } while(c.moveToNext());

        // if this fails, it means that your database doesn't contain all of the required location
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required location entry columns",
                GroceryColumnHashSet.isEmpty());


//***********************************************************************
        // now, do our tables contain the correct columns?
        c = db.rawQuery("PRAGMA table_info(" + GroceryContract.BrandEntry.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> BrandColumnHashSet = new HashSet<String>();
        BrandColumnHashSet.add(GroceryContract.BrandEntry._ID);
        BrandColumnHashSet.add(GroceryContract.BrandEntry.COLUMN_BRAND_NAME);

        int columnBrandNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnBrandNameIndex);
            BrandColumnHashSet.remove(columnName);
        } while(c.moveToNext());

        // if this fails, it means that your database doesn't contain all of the required location
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required location entry columns",
                BrandColumnHashSet.isEmpty());
//*************************************************************************************
// now, do our tables contain the correct columns?
        c = db.rawQuery("PRAGMA table_info(" + GroceryContract.BasicDescriptionEntry.TABLE_NAME+ ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> BasicDescriptionEntryColumnHashSet = new HashSet<String>();
        BasicDescriptionEntryColumnHashSet.add(GroceryContract.BasicDescriptionEntry._ID);
        BasicDescriptionEntryColumnHashSet.add(GroceryContract.BasicDescriptionEntry.COLUMN_PRODUCT_DESC);
        int columnBasicDescriptionNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnBasicDescriptionNameIndex);
            BasicDescriptionEntryColumnHashSet.remove(columnName);
        } while(c.moveToNext());

        // if this fails, it means that your database doesn't contain all of the required location
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required location entry columns",
                BasicDescriptionEntryColumnHashSet.isEmpty());




        db.close();
    }




}
