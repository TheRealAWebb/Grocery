/**
 * Title: GroceryDbHelper.java
 * Created by Alton Webber on 4/15/15.
 * Description:
 *
 * Purpose:  Contains code to create and initialize  the DataBase.
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
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.awebber.grocery.R;
import com.example.awebber.grocery.Utility;

import com.example.awebber.grocery.data.GroceryContract.BrandEntry;
import com.example.awebber.grocery.data.GroceryContract.BasicDescriptionEntry;
import com.example.awebber.grocery.data.GroceryContract.GroceryEntry;

import java.util.List;

public class GroceryDbHelper extends SQLiteOpenHelper{
    public static final String TAG ="GroceryDbHelpe";
    // If you change the database schema, you must increment the database version.
    //Incermented to 2 because of row name change
    private static final int DATABASE_VERSION =1;
    private static Context mContext;
    static final String DATABASE_NAME = "grocery.db";

    public GroceryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext =context;

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {



     final String SQL_CREATE_BRANDS_TABLE ="CREATE TABLE "+
                BrandEntry.TABLE_NAME + " (" +
                BrandEntry._ID + " INTEGER PRIMARY KEY, " +
                BrandEntry.COLUMN_PRODUCT_BRAND_NAME + " TEXT NOT NULL, " +

                " UNIQUE (" + BrandEntry.COLUMN_PRODUCT_BRAND_NAME +
                     ") ON CONFLICT ABORT);";


        final String SQL_CREATE_Basic_Description_TABLE ="CREATE TABLE "+
                BasicDescriptionEntry.TABLE_NAME + " (" +
                BasicDescriptionEntry._ID + " INTEGER PRIMARY KEY," +
                BasicDescriptionEntry.COLUMN_PRODUCT_TYPE + " TEXT NOT NULL, " +

                " UNIQUE (" +  BasicDescriptionEntry.COLUMN_PRODUCT_TYPE +
                                ") ON CONFLICT ABORT);";


        final String SQL_CREATE_GROCERIES_TABLE = "CREATE TABLE " +
                GroceryEntry.TABLE_NAME + " (" +
                GroceryEntry._ID + " INTEGER PRIMARY KEY, " +
                GroceryEntry.COLUMN_BASIC_DESC_LOC_KEY  + " INTEGER NOT NULL, " +
                GroceryEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL, " +
                GroceryEntry.COLUMN_BRAND_LOC_KEY  + " INTEGER NOT NULL, " +


                // Set up the basic_description column as a foreign key to basic_description table.
                " FOREIGN KEY " +"(" + GroceryEntry.COLUMN_BASIC_DESC_LOC_KEY + ")" +
                " REFERENCES " +
                BasicDescriptionEntry.TABLE_NAME + " (" + BasicDescriptionEntry._ID + "), " +

                // Set up the brand column as a foreign key to brands table.
                " FOREIGN KEY (" + GroceryEntry.COLUMN_BRAND_LOC_KEY + ") REFERENCES " +
                BrandEntry.TABLE_NAME + " (" + BrandEntry._ID + "), " +

                // To assure the application have just one weather entry per day
                // per location, it's created a UNIQUE constraint with REPLACE strategy
                " UNIQUE (" + GroceryEntry.COLUMN_PRODUCT_NAME + ", " +
                GroceryEntry.COLUMN_BASIC_DESC_LOC_KEY + ", " +
                GroceryEntry.COLUMN_BRAND_LOC_KEY + ") ON CONFLICT REPLACE);";





        sqLiteDatabase.execSQL(SQL_CREATE_BRANDS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_Basic_Description_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_GROCERIES_TABLE);
        Log.i(TAG,"This is the helper");

        Utility utility = new Utility();
        List<String> brands =   utility.LoadTextFile( mContext, R.raw.brandslist);

        for (String brand : brands)
        {
            ContentValues brandValues = new ContentValues();
            Log.i(TAG, brand);
            brandValues.put(BrandEntry.COLUMN_PRODUCT_BRAND_NAME, brand);
            sqLiteDatabase.insert(BrandEntry.TABLE_NAME,null,brandValues);
        }

        /*
        try {



            sqLiteDatabase.setTransactionSuccessful();
        } finally {
            sqLiteDatabase.endTransaction();
        } */
    }
        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
           // TODO: Implement Alter table to user data can be kept
           //https://www.sqlite.org/lang_altertable.html Alter Table notes t keep data

            // This database is only a cache for online data, so its upgrade policy is
            // to simply to discard the data and start over
            // Note that this only fires if you change the version number for your database.
            // It does NOT depend on the version number for your application.
            // If you want to update the schema without wiping data, commenting out the next 2 lines
            // should be your top priority before modifying this method.
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + GroceryEntry.TABLE_NAME);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + BrandEntry.TABLE_NAME);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + BasicDescriptionEntry.TABLE_NAME);
            onCreate(sqLiteDatabase);
        }

}
