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
import com.example.awebber.grocery.data.GroceryContract.CategoryEntry;
import com.example.awebber.grocery.data.GroceryContract.GroceryEntry;
import com.example.awebber.grocery.data.GroceryContract.InventoryEntry;
import java.util.List;

public class GroceryDbHelper extends SQLiteOpenHelper{
    public static final String TAG ="GroceryDbHelper";
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
                CategoryEntry.TABLE_NAME + " (" +
                GroceryContract.CategoryEntry._ID + " INTEGER PRIMARY KEY," +
                CategoryEntry.COLUMN_CATEGORY_NAME + " TEXT NOT NULL, " +

                " UNIQUE (" +  CategoryEntry.COLUMN_CATEGORY_NAME +
                                ") ON CONFLICT ABORT);";


        final String SQL_CREATE_GROCERIES_TABLE = "CREATE TABLE " +
                GroceryEntry.TABLE_NAME + " (" +
                GroceryEntry._ID + " INTEGER PRIMARY KEY, " +
                GroceryEntry.COLUMN_CATEGORY_LOC_KEY + " INTEGER, " +
                GroceryEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL, " +
                GroceryEntry.COLUMN_BRAND_LOC_KEY  + " INTEGER, " +
                GroceryEntry.COLUMN_QUANTITY        + " INTEGER, " +

                // Set up the basic_description column as a foreign key to basic_description table.
                " FOREIGN KEY " +"(" + GroceryEntry.COLUMN_CATEGORY_LOC_KEY + ")" +
                " REFERENCES " +
                GroceryContract.CategoryEntry.TABLE_NAME + " (" + CategoryEntry._ID + "), " +

                // Set up the brand column as a foreign key to brands table.
                " FOREIGN KEY (" + GroceryEntry.COLUMN_BRAND_LOC_KEY + ") REFERENCES " +
                BrandEntry.TABLE_NAME + " (" + BrandEntry._ID + "), " +

                // To assure the application have just one weather entry per day
                // per location, it's created a UNIQUE constraint with REPLACE strategy
                " UNIQUE (" + GroceryEntry.COLUMN_PRODUCT_NAME + ", " +
                GroceryEntry.COLUMN_CATEGORY_LOC_KEY + ", " +
                GroceryEntry.COLUMN_BRAND_LOC_KEY + ") ON CONFLICT REPLACE);";

        final String SQL_CREATE_INVENTORY_TABLE = "CREATE TABLE " +
                GroceryContract.InventoryEntry.TABLE_NAME + " (" +
                InventoryEntry._ID + " INTEGER PRIMARY KEY, " +
                InventoryEntry.COLUMN_CATEGORY_LOC_KEY + " INTEGER, " +
                InventoryEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL, " +
                InventoryEntry.COLUMN_BRAND_LOC_KEY  + " INTEGER, " +
                InventoryEntry.COLUMN_QUANTITY       + " INTEGER, " +

                // Set up the basic_description column as a foreign key to basic_description table.
                " FOREIGN KEY " +"(" + InventoryEntry.COLUMN_CATEGORY_LOC_KEY + ")" +
                " REFERENCES " +
                GroceryContract.CategoryEntry.TABLE_NAME + " (" + CategoryEntry._ID + "), " +

                // Set up the brand column as a foreign key to brands table.
                " FOREIGN KEY (" + InventoryEntry.COLUMN_BRAND_LOC_KEY + ") REFERENCES " +
                BrandEntry.TABLE_NAME + " (" + BrandEntry._ID + "), " +

                // To assure the application have just one weather entry per day
                // per location, it's created a UNIQUE constraint with REPLACE strategy
                " UNIQUE (" + InventoryEntry.COLUMN_PRODUCT_NAME + ", " +
                InventoryEntry.COLUMN_CATEGORY_LOC_KEY + ", " +
                InventoryEntry.COLUMN_BRAND_LOC_KEY + ") ON CONFLICT REPLACE);";



        sqLiteDatabase.execSQL(SQL_CREATE_BRANDS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_Basic_Description_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_INVENTORY_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_GROCERIES_TABLE);
        Log.i(TAG,"This is the helper");

        Utility utility = new Utility();
        List<String> brands =   utility.LoadTextFile( mContext, R.raw.brandslist);
        List<String> groceries =   utility.LoadTextFile( mContext, R.raw.grocerieslist);
        List<String> categories =   utility.LoadTextFile( mContext, R.raw.categories);
        for (String brand : brands)
        {
            ContentValues brandValues = new ContentValues();
            Log.i(TAG, brand);
            brandValues.put(BrandEntry.COLUMN_PRODUCT_BRAND_NAME, brand);
            sqLiteDatabase.insert(BrandEntry.TABLE_NAME,null,brandValues);
        }


        for (String category : categories)
        {
            ContentValues categoryValues = new ContentValues();
            Log.i(TAG,  category );
            categoryValues.put(CategoryEntry.COLUMN_CATEGORY_NAME, category);
            sqLiteDatabase.insert(CategoryEntry.TABLE_NAME,null, categoryValues);
        }
        for (String grocery :  groceries )
        {
            ContentValues  groceryValues = new ContentValues();
            Log.i(TAG, grocery);
            groceryValues.put(GroceryEntry.COLUMN_PRODUCT_NAME, grocery);
            sqLiteDatabase.insert(GroceryEntry.TABLE_NAME,null, groceryValues);
        }


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
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + GroceryContract.CategoryEntry.TABLE_NAME);
            onCreate(sqLiteDatabase);
        }

}
