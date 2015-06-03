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

import android.database.Cursor;
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
    static Context mContext;
    public static final String TAG = GroceryDbHelper.class.getSimpleName();
    // If you change the database schema, you must increment the database version.

    //Incermented to 2 because of row name change
    private static final int DATABASE_VERSION =1;

    static final String DATABASE_NAME = "grocery.db";

    public GroceryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
       mContext = context;

    }

    private static Context getContext(){

        return mContext;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

     final String SQL_CREATE_BRANDS_TABLE ="CREATE TABLE "+
                BrandEntry.TABLE_NAME + " (" +
                BrandEntry._ID + " INTEGER PRIMARY KEY, " +
                BrandEntry.COLUMN_PRODUCT_BRAND_NAME + " TEXT NOT NULL, " +

                " UNIQUE (" + BrandEntry.COLUMN_PRODUCT_BRAND_NAME +
                     ") ON CONFLICT ABORT);";


        final String SQL_CREATE_CATEGORY_TABLE ="CREATE TABLE "+
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
              //  GroceryEntry.COLUMN_QUANTITY        + " INTEGER, " +

                // Set up the basic_description column as a foreign key to basic_description table.
                " FOREIGN KEY " +"(" + GroceryEntry.COLUMN_CATEGORY_LOC_KEY + ")" +
                " REFERENCES " +
                GroceryContract.CategoryEntry.TABLE_NAME + " (" + CategoryEntry._ID + "), " +

                // Set up the brand column as a foreign key to brands table.
                " FOREIGN KEY (" + GroceryEntry.COLUMN_BRAND_LOC_KEY + ") REFERENCES " +
                BrandEntry.TABLE_NAME + " (" + BrandEntry._ID + "), " +

                // To assure the application have  entry
                // ,  created a UNIQUE constraint with REPLACE strategy
                " UNIQUE (" + GroceryEntry.COLUMN_PRODUCT_NAME + ", " +
                GroceryEntry.COLUMN_CATEGORY_LOC_KEY + ", " +
                GroceryEntry.COLUMN_BRAND_LOC_KEY + ") ON CONFLICT REPLACE);";

        final String SQL_CREATE_INVENTORY_TABLE = "CREATE TABLE " +
                GroceryContract.InventoryEntry.TABLE_NAME + " (" +
                InventoryEntry._ID + " INTEGER PRIMARY KEY, " +
                InventoryEntry.COLUMN_GROCERY_LOC_KEY + " INTEGER, " +
                InventoryEntry.COLUMN_QUANTITY        + " INTEGER, " +

                // Set up the grocery._id column as a foreign key to grocery._ID table.
                " FOREIGN KEY " +"(" + InventoryEntry.COLUMN_GROCERY_LOC_KEY + ")" +
                " REFERENCES " +
                GroceryContract.GroceryEntry.TABLE_NAME + " (" + GroceryEntry._ID + "), " +

                // To assure the application have just one Instance of a product in inventory
                " UNIQUE (" +InventoryEntry.COLUMN_GROCERY_LOC_KEY +") ON CONFLICT ABORT);";



        sqLiteDatabase.execSQL(SQL_CREATE_BRANDS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_CATEGORY_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_GROCERIES_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_INVENTORY_TABLE);
        Log.i(TAG,SQL_CREATE_INVENTORY_TABLE);

        Utility utility = new Utility();
        List<String> brands =               utility.LoadTextFile(  getContext(), R.raw.brandslist);
        List<String> categories =           utility.LoadTextFile( getContext(), R.raw.categories);
        List<String> categoriesFileNames =    utility.LoadTextFile(getContext(), R.raw.categoryfilenames);
        List<String> CategoryDatabaseNames =    utility.LoadTextFile(getContext(), R.raw.grocerydata);
        List<String>  groceries;
        for (String brand : brands)
        {
            ContentValues brandValues = new ContentValues();
            brandValues.put(BrandEntry.COLUMN_PRODUCT_BRAND_NAME, brand);
            sqLiteDatabase.insert(BrandEntry.TABLE_NAME,null,brandValues);
        }
        for (String category : categories)
        {
            ContentValues categoryValues = new ContentValues();
            categoryValues.put(CategoryEntry.COLUMN_CATEGORY_NAME, category);
            sqLiteDatabase.insert(CategoryEntry.TABLE_NAME,null, categoryValues);
        }

        int categoryId ;
        String Filename;
        String category;
        String productName;
        for(int postition = 0;postition<  categoriesFileNames.size();postition++) {
            Filename = categoriesFileNames.get(postition);
            groceries =   utility.LoadTextFile(
                    getContext(),
                    getContext().getResources().
                    getIdentifier(Filename, "raw", "com.example.awebber.grocery"));

                Log.i(TAG, " This is the file name " + Filename);

                category = CategoryDatabaseNames.get(postition);
                Log.i(TAG, " This is the file Category " + category);
                Cursor x = sqLiteDatabase.query(
                        CategoryEntry.TABLE_NAME,
                        new String[]{CategoryEntry._ID},
                        CategoryEntry.COLUMN_CATEGORY_NAME + " =?",
                        new String[]{category},
                        null,
                        null,
                        null);


                int CategoryEntryID =0;
                x.moveToFirst();
                categoryId = x.getInt(CategoryEntryID);
                x.close();


                for (int z=0; z< groceries.size();z++) {
                    productName = groceries.get(z);
                    ContentValues groceryValues = new ContentValues();
                    int UnknownBrandLocKey =1;
                    groceryValues.put(GroceryEntry.COLUMN_BRAND_LOC_KEY,UnknownBrandLocKey);
                    groceryValues.put(GroceryEntry.COLUMN_PRODUCT_NAME, productName);
                    groceryValues.put(GroceryEntry.COLUMN_CATEGORY_LOC_KEY, categoryId);
                    sqLiteDatabase.insert(GroceryEntry.TABLE_NAME, null, groceryValues);
                }


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
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CategoryEntry.TABLE_NAME);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + InventoryEntry.TABLE_NAME);
            onCreate(sqLiteDatabase);
        }

}
