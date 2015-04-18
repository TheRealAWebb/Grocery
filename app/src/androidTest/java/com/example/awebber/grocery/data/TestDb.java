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
package com.example.awebber.grocery.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.HashSet;


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


    public void testCreateDb() throws Throwable {
        // build a HashSet of all of the table names we wish to look for
        // Note that there will be another table in the DB that stores the
        // Android metadata (db version information)
        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(GroceryContract.BasicDescriptionEntry.TABLE_NAME);
        tableNameHashSet.add(GroceryContract.BrandEntry.TABLE_NAME);
        tableNameHashSet.add(GroceryContract.GroceryEntry.TABLE_NAME);

        mContext.deleteDatabase(GroceryDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new GroceryDbHelper(this.mContext).getWritableDatabase();

        assertEquals(true, db.isOpen());

        // have we created the tables we want?
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error: This means that the database has not been created correctly",
                c.moveToFirst());

        // verify that the tables have been created
        do {
            tableNameHashSet.remove(c.getString(0));
        } while( c.moveToNext() );

        // if this fails, it means that your database doesn't contain  the Brand entry
        // and Grocery entry , BasicDesc entry
        // and Brand entry tables
        assertTrue("Error: Your database was created without the Brand entry and Grocery entry and Basic Desc tables",
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

        // if this fails, it means that your database doesn't contain all of the required Grocery
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required Groceryentry columns",
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

        // if this fails, it means that your database doesn't contain all of the required Brand
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required Brand entry columns",
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
        BasicDescriptionEntryColumnHashSet.add(GroceryContract.BasicDescriptionEntry.COLUMN_PRODUCT_TYPE);
        int columnBasicDescriptionNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnBasicDescriptionNameIndex);
            BasicDescriptionEntryColumnHashSet.remove(columnName);
        } while(c.moveToNext());

        // if this fails, it means that your database doesn't contain all of the required BasicDescription
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required BasicDescription entry columns",
                BasicDescriptionEntryColumnHashSet.isEmpty());

        db.close();
    }

    public void testGroceryTable() {
        // First insert the Brand and Basic Description, and then use the BrandRowId and BasicDescRowId to insert
        // the grocery. Make sure to cover as many failure cases as you can.


        long BrandRowId = insertBrand();
        long BasicDescRowId = insertBasiceDesc();

        // Make sure we have a valid row ID.
        assertFalse("Error: Brand Not Inserted Correctly", BrandRowId == -1L);
        assertFalse("Error: Basic_Description Not Inserted Correctly", BasicDescRowId == -1L);
        // First step: Get reference to writable database
        // If there's an error in those massive SQL table creation Strings,
        // errors will be thrown here when you try to get a writable database.
        GroceryDbHelper dbHelper = new GroceryDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Second Step (Grocery): Create grocery values
        ContentValues groceryValues = TestUtilities. createGroceryValues(BasicDescRowId, BrandRowId);

        // Third Step (Grocery): Insert ContentValues into database and get a row ID back
        long groceryRowId = db.insert(GroceryContract.GroceryEntry.TABLE_NAME, null, groceryValues);
        assertTrue(groceryRowId != -1);

        // Fourth Step: Query the database and receive a Cursor back
        // A cursor is your primary interface to the query results.
        Cursor groceryCursor = db.query(
                GroceryContract.GroceryEntry.TABLE_NAME,  // Table to Query
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null  // sort order
        );

        // Move the cursor to the first valid database row and check to see if we have any rows
        assertTrue( "Error: No Records returned from grocery query", groceryCursor.moveToFirst() );

        // Fifth Step: Validate the grocery Query
        TestUtilities.validateCurrentRecord("testInsertReadDb  groceryEntry failed to validate",
                groceryCursor, groceryValues);

        // Move the cursor to demonstrate that there is only one record in the database
        assertFalse( "Error: More than one record returned from  grocery query",
                groceryCursor.moveToNext() );

        // Sixth Step: Close cursor and database
        groceryCursor.close();
        dbHelper.close();

    }

    public long insertBrand(){
        // First step: Get reference to writable database
        // If there's an error in those massive SQL table creation Strings,
        // errors will be thrown here when you try to get a writable database.
        GroceryDbHelper dbHelper = new GroceryDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Second Step: Create ContentValues of what you want to insert
        // (you can use the createNikeBrandValues if you wish)
        ContentValues testValues = TestUtilities.createNikeBrandValues();

        // Third Step: Insert ContentValues into database and get a row ID back
        long BrandRowId;
        BrandRowId = db.insert(GroceryContract.BrandEntry.TABLE_NAME, null, testValues);

        // Verify we got a row back.
        assertTrue(BrandRowId != -1);

        // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
        // the round trip.

        // Fourth Step: Query the database and receive a Cursor back
        // A cursor is your primary interface to the query results.
        Cursor cursor = db.query(
                GroceryContract.BrandEntry.TABLE_NAME,  // Table to Query
                null, // all columns
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );

        // Move the cursor to a valid database row and check to see if we got any records back
        // from the query
        assertTrue( "Error: No Records returned from Brand query", cursor.moveToFirst() );

        // Fifth Step: Validate data in resulting Cursor with the original ContentValues
        // (you can use the validateCurrentRecord function in TestUtilities to validate the
        // query if you like)
        TestUtilities.validateCurrentRecord("Error: Brand Query Validation Failed",
                cursor, testValues);

        // Move the cursor to demonstrate that there is only one record in the database
        assertFalse( "Error: More than one record returned from Brand query",
                cursor.moveToNext() );

        // Sixth Step: Close Cursor and Database
        cursor.close();
        db.close();
        return BrandRowId;
    }


     public void testBrandTable() {
         insertBrand();
     }
    public long insertBasiceDesc(){
        // First step: Get reference to writable database
        // If there's an error in those massive SQL table creation Strings,
        // errors will be thrown here when you try to get a writable database.
        GroceryDbHelper dbHelper = new  GroceryDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Second Step: Create ContentValues of what you want to insert
        // (you can use the createSneakerBasicDescValues if you wish)
        ContentValues testValues = TestUtilities.createSneakerBasicDescValues();

        // Third Step: Insert ContentValues into database and get a row ID back
        long BasiceDescId;
        BasiceDescId = db.insert(GroceryContract.BasicDescriptionEntry.TABLE_NAME, null, testValues);

        // Verify we got a row back.
        assertTrue(BasiceDescId != -1);

        // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
        // the round trip.

        // Fourth Step: Query the database and receive a Cursor back
        // A cursor is your primary interface to the query results.
        Cursor cursor = db.query(
                GroceryContract.BasicDescriptionEntry.TABLE_NAME,  // Table to Query
                null, // all columns
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );

        // Move the cursor to a valid database row and check to see if we got any records back
        // from the query
        assertTrue( "Error: No Records returned from  basic_description query", cursor.moveToFirst() );

        // Fifth Step: Validate data in resulting Cursor with the original ContentValues
        // (you can use the validateCurrentRecord function in TestUtilities to validate the
        // query if you like)
        TestUtilities.validateCurrentRecord("Error:  Basic_Description Query Validation Failed",
                cursor, testValues);

        // Move the cursor to demonstrate that there is only one record in the database
        assertFalse( "Error: More than one record returned from basic_description query",
                cursor.moveToNext() );

        // Sixth Step: Close Cursor and Database
        cursor.close();
        db.close();
        return BasiceDescId;
    }
    public void testBasicDescTable() {
        insertBasiceDesc();
    }
}
