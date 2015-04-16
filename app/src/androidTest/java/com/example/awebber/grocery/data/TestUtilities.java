/**
 * Title: TestUtilities.java
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
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.test.AndroidTestCase;

import com.example.awebber.grocery.Utils.PollingCheck;

import java.util.Map;
import java.util.Set;


public class TestUtilities extends AndroidTestCase {

    static final String TEST_BRAND ="NIKE";
    static final String TEST_BASICS_DES ="SNEAKER";
    static final long TEST_DATE = 1419033600L;  // December 20th, 2014

    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }

    /*
        Students: Use this to create some default grocery values for your database tests.
     */
    static ContentValues createGroceryValues(long BasicDescRowId,long brandRowId) {
        ContentValues GroceryValues = new ContentValues();
        GroceryValues.put(GroceryContract.GroceryEntry.COLUMN_BASIC_DESC_LOC_KEY ,BasicDescRowId);
        GroceryValues.put(GroceryContract.GroceryEntry.COLUMN_BRAND_LOC_KEY,brandRowId );
        GroceryValues.put(GroceryContract.GroceryEntry.COLUMN_NAME, "Pure Sport");

        return GroceryValues;
    }


    static ContentValues createNikeBrandValues() {
        // Create a new map of values, where column names are the keys
        ContentValues testValues = new ContentValues();
        testValues.put(GroceryContract.BrandEntry.COLUMN_BRAND_NAME, TEST_BRAND);
        return testValues;
    }
    static ContentValues createSneakerBasicDescValues() {
        // Create a new map of values, where column names are the keys
        ContentValues testValues = new ContentValues();
        testValues.put(GroceryContract.BasicDescriptionEntry.COLUMN_PRODUCT_DESC, TEST_BASICS_DES );
        return testValues;
    }


      static long insertNikeBrandValues(Context context) {
       // insert our test records into the database
       GroceryDbHelper dbHelper = new GroceryDbHelper(context);
       SQLiteDatabase db = dbHelper.getWritableDatabase();
       ContentValues testValuesBrand = TestUtilities.createNikeBrandValues();

       long BrandRowId;
       BrandRowId = db.insert(GroceryContract.BrandEntry.TABLE_NAME, null,  testValuesBrand );


       // Verify we got a row back.
       assertTrue("Error: Failure to insert Nike Brand Values", BrandRowId != -1);


       return  BrandRowId;
   }
    static long insertSneakerBasicDescValues(Context context) {
        // insert our test records into the database
        GroceryDbHelper dbHelper = new GroceryDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValuesBasicDesc = TestUtilities.createSneakerBasicDescValues();

        long  BasicDescRowId;
        BasicDescRowId = db.insert( GroceryContract.BasicDescriptionEntry.TABLE_NAME, null, testValuesBasicDesc );

        // Verify we got a row back.

        assertTrue("Error: Failure to insert Sneaker BasicDesc Values", BasicDescRowId != -1);

        return BasicDescRowId;
    }



    /*
        Students: The functions we provide inside of TestProvider use this utility class to test
        the ContentObserver callbacks using the PollingCheck class that we grabbed from the Android
        CTS tests.

        Note that this only tests that the onChange function is called; it does not test that the
        correct Uri is returned.
     */
    static class TestContentObserver extends ContentObserver {
        final HandlerThread mHT;
        boolean mContentChanged;

        static TestContentObserver getTestContentObserver() {
            HandlerThread ht = new HandlerThread("ContentObserverThread");
            ht.start();
            return new TestContentObserver(ht);
        }

        private TestContentObserver(HandlerThread ht) {
            super(new Handler(ht.getLooper()));
            mHT = ht;
        }

        // On earlier versions of Android, this onChange method is called
        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            mContentChanged = true;
        }

        public void waitForNotificationOrFail() {
            // Note: The PollingCheck class is taken from the Android CTS (Compatibility Test Suite).
            // It's useful to look at the Android CTS source for ideas on how to test your Android
            // applications.  The reason that PollingCheck works is that, by default, the JUnit
            // testing framework is not running on the main Android application thread.
            new PollingCheck(5000) {
                @Override
                protected boolean check() {
                    return mContentChanged;
                }
            }.run();
            mHT.quit();
        }
    }

    static TestContentObserver getTestContentObserver() {
        return TestContentObserver.getTestContentObserver();
    }
}
