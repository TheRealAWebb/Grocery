/**
 * Title: GroceryContentProvider.java
 * Created by Alton Webber on 4/16/15.
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

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

public class GroceryContentProvider extends ContentProvider {
    public GroceryContentProvider() {
    }

//Uri Matcher
//Content providers implete functionality bases upo uri's passsed ot them
String TAG ="Content Provider";
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    //Uri Matcher Match cases
    static final int GROCERIES = 100;
    static final int GROCERIES_WITH_BRANDS = 102;
    static final int GROCERIES_WITH_CATEGORIES =103;
    static final int GROCERIES_WITH_INVENTORY =104;
    static final int GROCERIES_WITH_CATEGORIES_AND_BRANDS =105;

    static final int BRANDS = 200;

    static final int CATEGORIES = 300;
    static final int CATEGORIES_SEARCH =301;
    static final int INVENTORY = 400;
    private GroceryDbHelper mOpenHelper;


    //This defines the "From" statemeny of the Query
    private static final SQLiteQueryBuilder sGroceryByBrandorCategoryQueryBuilder;
    private static final SQLiteQueryBuilder sGroceryByBrandQueryBuilder;
    private static final SQLiteQueryBuilder sGroceryByCategoryQueryBuilder;
    private static final SQLiteQueryBuilder sGroceryByInventoryQueryBuilder;
    private static final SQLiteQueryBuilder  sGroceryQueryBuilder;
    static{
       sGroceryByBrandQueryBuilder  = new SQLiteQueryBuilder();
       //groceries INNER JOIN brands ON groceries.brand_id = brands._id
       sGroceryByBrandQueryBuilder.setTables(
               GroceryContract.GroceryEntry.TABLE_NAME + " INNER JOIN " +
                       GroceryContract.BrandEntry.TABLE_NAME +
                       " ON " + GroceryContract.GroceryEntry.TABLE_NAME +
                       "." + GroceryContract.GroceryEntry.COLUMN_BRAND_LOC_KEY +
                       " = " + GroceryContract.BrandEntry.TABLE_NAME +
                       "." + GroceryContract.BrandEntry._ID);
   }

   static{
       sGroceryByCategoryQueryBuilder = new SQLiteQueryBuilder();
       //This is an inner join which looks like
       //groceries INNER JOIN basic_descriptions ON groceries.basic_description_id = basic_description_id
       sGroceryByCategoryQueryBuilder.setTables(
               GroceryContract.GroceryEntry.TABLE_NAME + " INNER JOIN " +
                      GroceryContract.CategoryEntry.TABLE_NAME +
                       " ON " + GroceryContract.GroceryEntry.TABLE_NAME +
                       "." + GroceryContract.GroceryEntry.COLUMN_CATEGORY_LOC_KEY +
                       " = " + GroceryContract.CategoryEntry.TABLE_NAME +
                       "." + GroceryContract.CategoryEntry._ID
       );

   }

    static{
        sGroceryByInventoryQueryBuilder = new SQLiteQueryBuilder();
        //This is an inner join which looks like
        //groceries INNER JOIN basic_descriptions ON groceries.basic_description_id = basic_description_id
        sGroceryByInventoryQueryBuilder.setTables(
                GroceryContract.GroceryEntry.TABLE_NAME + " INNER JOIN " +
                        GroceryContract.InventoryEntry.TABLE_NAME +
                        " ON " + GroceryContract.GroceryEntry.TABLE_NAME +
                        "." + GroceryContract.GroceryEntry._ID +
                        " = " + GroceryContract.InventoryEntry.TABLE_NAME +
                        "." + GroceryContract.InventoryEntry.COLUMN_GROCERY_LOC_KEY
        );

    }



    static{
        sGroceryQueryBuilder = new SQLiteQueryBuilder();
        //This is an inner join which looks like
        //groceries INNER JOIN basic_descriptions ON groceries.basic_description_id = basic_description_id


    }

    static{
        sGroceryByBrandorCategoryQueryBuilder = new SQLiteQueryBuilder();

    //This is an inner join which looks like
    //groceries INNER JOIN brands ON groceries.brand_id = brands._id
    //groceries INNER JOIN basic_descriptions ON groceries.basic_description_id = basic_description_id
        sGroceryByBrandorCategoryQueryBuilder.setTables(
                GroceryContract.GroceryEntry.TABLE_NAME + "." +
                        GroceryContract.GroceryEntry.COLUMN_PRODUCT_NAME +
                        " like '%" + "%s" + "%%' " +
                        "UNION ALL" +
                        " SELECT " + "'" + GroceryContract.BrandEntry.TABLE_NAME + "'" + " AS table_name, " +
                        GroceryContract.BrandEntry.TABLE_NAME + "." +
                        GroceryContract.BrandEntry.COLUMN_PRODUCT_BRAND_NAME +
                        ", _id FROM " + GroceryContract.BrandEntry.TABLE_NAME +
                        " WHERE " + GroceryContract.BrandEntry.TABLE_NAME + "." +
                        GroceryContract.BrandEntry.COLUMN_PRODUCT_BRAND_NAME +
                        " like '" +  "%s" + "%%' " +
                        "UNION ALL" +
                        " SELECT " + "'" + GroceryContract.CategoryEntry.TABLE_NAME + "'" + " AS table_name, " +
                        GroceryContract.CategoryEntry.TABLE_NAME + "." +
                        GroceryContract.CategoryEntry.COLUMN_CATEGORY_NAME +
                        ", _id FROM " + GroceryContract.CategoryEntry.TABLE_NAME +
                        " Where " + GroceryContract.CategoryEntry.TABLE_NAME + "." +
                        GroceryContract.CategoryEntry.COLUMN_CATEGORY_NAME +
                        " like '%" + "%s" + "%%' ") ;

    }

    //Buildings the select statements that set up the condition
    // Where condtion1 (< ?,= ?,<= ?,> ?,>= ? ,= ?)  selectionArgs[]
    //Where Brands.brand_name = ?
    private static final String sBrandSelection =
            GroceryContract.BrandEntry.TABLE_NAME +
                    "." + GroceryContract.BrandEntry.COLUMN_PRODUCT_BRAND_NAME + " = ? ";

    //Where categories.category_name = ?
    private static final String sCategoriesSelection =
            GroceryContract.CategoryEntry.TABLE_NAME +
                    "." + GroceryContract.CategoryEntry.COLUMN_CATEGORY_NAME + " = ? ";

    //Wherebasic_descriptions.product_type = ? AND Brands.brand_name = ?
    private static final String sGroceryUnionCategoryUnionBrandSelection =
            GroceryContract.GroceryEntry.TABLE_NAME + "." +
                    GroceryContract.GroceryEntry.COLUMN_PRODUCT_NAME +
                    " like '%%" + "%s" + "%%' " +
                    "UNION ALL" +
                    " SELECT " + "'" + GroceryContract.BrandEntry.TABLE_NAME + "'" + " AS table_name, " +
                    GroceryContract.BrandEntry.TABLE_NAME + "." +
                    GroceryContract.BrandEntry.COLUMN_PRODUCT_BRAND_NAME +
                    ", _id FROM " + GroceryContract.BrandEntry.TABLE_NAME +
                    " WHERE " + GroceryContract.BrandEntry.TABLE_NAME + "." +
                    GroceryContract.BrandEntry.COLUMN_PRODUCT_BRAND_NAME +
                    " like '%%" +  "%s" + "%%' " +
                    "UNION ALL" +
                    " SELECT " + "'" + GroceryContract.CategoryEntry.TABLE_NAME + "'" + " AS table_name, " +
                    GroceryContract.CategoryEntry.TABLE_NAME + "." +
                    GroceryContract.CategoryEntry.COLUMN_CATEGORY_NAME +
                    ", _id FROM " + GroceryContract.CategoryEntry.TABLE_NAME +
                    " Where " + GroceryContract.CategoryEntry.TABLE_NAME + "." +
                    GroceryContract.CategoryEntry.COLUMN_CATEGORY_NAME +
                    " like '%%" + "%s" + "%%' ";



    //When the Uri is passed through the Uri matcher this function will be called
    //During the query and return this cursor
    private Cursor getGrociesByBrandName(Uri uri, String[] projection, String sortOrder) {
        String BrandName = GroceryContract.GroceryEntry.getBrandNameFromUri(uri);
        String[] selectionArgs ={BrandName} ;
        String selection = sBrandSelection;
        return  sGroceryByBrandQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getGrociesByInventory(Uri uri, String[] projection, String  selection,
                                         String[] selectionArgs ,String sortOrder) {

        return  sGroceryByInventoryQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }


    private Cursor getGrociesByCategory(Uri uri, String[] projection, String sortOrder) {
        Log.i(TAG, " Category is "+GroceryContract.GroceryEntry.getCategoryFromUri(uri));
        String category = GroceryContract.GroceryEntry.getCategoryFromUri(uri);
        String[] selectionArgs ={category};

        String selection =  sCategoriesSelection ;
        return sGroceryByCategoryQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }



    private Cursor getGrociesBySearch(Uri uri, String[] projection, String sortOrder) {

        String  theSearch = GroceryContract.GroceryEntry.getSearchStringFromUri(uri);
        String[] selectionArgs = null;
       // String selection = null;
        String selection = String.format(sGroceryUnionCategoryUnionBrandSelection,
                theSearch,theSearch,theSearch);

        return   sGroceryQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }



    @Override
    public boolean onCreate() {
        //  Implement this to initialize your content provider on startup.
        mOpenHelper = new GroceryDbHelper(getContext());
        return true;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        //To remove all rows and get a count pass "1" as the whereClause.
       //if no selection is passed assumed to mean delete all rows
        if(null == selection){
            rowsDeleted = 1;
        }
        switch (match) {
            case GROCERIES: {     //(table  ,whereClause , whereArgs )
                rowsDeleted = db.delete(GroceryContract.GroceryEntry.TABLE_NAME,selection,selectionArgs);
                break;
            }
            case BRANDS: {
                rowsDeleted = db.delete(GroceryContract.BrandEntry.TABLE_NAME,selection,selectionArgs);
                break;
            }
            case CATEGORIES: {
                rowsDeleted = db.delete(GroceryContract.CategoryEntry.TABLE_NAME,selection,selectionArgs);
                break;
            }
            case INVENTORY: {
                rowsDeleted = db.delete(GroceryContract.InventoryEntry.TABLE_NAME,selection,selectionArgs);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
       if(rowsDeleted != 0){
        getContext().getContentResolver().notifyChange(uri, null);
       }
        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        // Use the Uri Matcher to determine what kind of URI this is
        // A list a table of items or a single item
        final int match = sUriMatcher.match(uri);

        switch (match) {

            case GROCERIES:
                return GroceryContract.GroceryEntry.CONTENT_TYPE;
            // Return groceries Table rows that match w brands&  that match the Basic
            case GROCERIES_WITH_CATEGORIES:
                return GroceryContract.GroceryEntry.CONTENT_TYPE;
            case  GROCERIES_WITH_BRANDS:
                return GroceryContract.GroceryEntry.CONTENT_TYPE;
            case GROCERIES_WITH_CATEGORIES_AND_BRANDS:
                return GroceryContract.GroceryEntry.CONTENT_TYPE;
            case CATEGORIES:
                return GroceryContract.CategoryEntry.CONTENT_TYPE;
            case  BRANDS:
                return GroceryContract.BrandEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case GROCERIES: {
                long _id = db.insert(
                        GroceryContract.GroceryEntry.TABLE_NAME,    //table
                        null,                                       //nullColumnHack
                        values);                                    //Values
                if ( _id > 0 )
                returnUri = GroceryContract.GroceryEntry.buildGroceriesUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case BRANDS: {
                long _id = db.insert(
                        GroceryContract.BrandEntry.TABLE_NAME, //table
                        null,                                   //nullColumnHack
                        values);                                //Values
                if ( _id > 0 )
                returnUri = GroceryContract.BrandEntry.buildBrandsUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case CATEGORIES: {
                long _id = db.insert(
                        GroceryContract.CategoryEntry.TABLE_NAME, //table
                        null,                                             //nullColumnHack
                        values);                                           //Values
                if ( _id > 0 )
                returnUri = GroceryContract.CategoryEntry.buildCategoriesUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case INVENTORY: {
                long _id = db.insert(
                        GroceryContract.InventoryEntry.TABLE_NAME, //table
                        null,                                             //nullColumnHack
                        values);                                           //Values
                if ( _id > 0 )
                    returnUri = GroceryContract.InventoryEntry.buildInventoryUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
        }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        Cursor retCursor;

        switch (sUriMatcher.match(uri)) {

            //"groceries/brands/*"
            case  GROCERIES_WITH_BRANDS:
            {
                retCursor = getGrociesByBrandName(uri,projection,sortOrder);
                break;
            }


            /**  content://com.example.awebber.grocery/groceries/categories/*
             * Return   GroceryContract.GroceryEntry.COLUMN_PRODUCT_NAME
             * FROM  GroceryContract.GroceryEntry.TABLE_NAME
             * WHERE GroceryContract.GroceryEntry.COLUMN_CATEGORY_LOC_KEY = GroceryContract.CategoryEntry._ID
             * ON  GroceryContract.CategoryEntry.COLUMN_CATEGORY_NAME =  *
             */
            case GROCERIES_WITH_CATEGORIES:
            {
                Log.i(TAG," GROCERIES_WITH_CATEGORIES");
                retCursor = getGrociesByCategory(uri, projection, sortOrder);
                break;
            }

            case GROCERIES_WITH_INVENTORY:
            {
                retCursor = getGrociesByInventory(
                        uri,
                        projection,
                        selection,
                        selectionArgs,
                        sortOrder);
                break;

            }
            // "groceries/categories/brands/*"
            case GROCERIES_WITH_CATEGORIES_AND_BRANDS:
            {
                String  theSearch = GroceryContract.GroceryEntry.getSearchStringFromUri(uri);
                Log.i(TAG," The selection "+ String.format(sGroceryUnionCategoryUnionBrandSelection,
                        theSearch,theSearch,theSearch));
                retCursor = mOpenHelper.getReadableDatabase().query(
                        GroceryContract.GroceryEntry.TABLE_NAME,
                        projection,
                        String.format(sGroceryUnionCategoryUnionBrandSelection,
                                theSearch,theSearch,theSearch),
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }


            // "groceries/"
            case GROCERIES :
            {    // Return the entire groceries Table
                retCursor = mOpenHelper.getReadableDatabase().query(
                     GroceryContract.GroceryEntry.TABLE_NAME,
                     projection,
                     selection,
                     selectionArgs,
                     null,
                     null,
                     sortOrder
                );
                break;
            }

            //"basic_desriptions/"
            case CATEGORIES:
            {  // Return the entire basic_desriptions Table
                retCursor = mOpenHelper.getReadableDatabase().query(
                        GroceryContract.CategoryEntry.TABLE_NAME, // The  table
                        projection,                                       // The columns to return for each row
                        selection,            // Selection criteria
                        selectionArgs,         // Selection criteria Args
                        null,                 //  groupBy
                        null,                  //   having
                        sortOrder               // orderBy

                );
                break;
            }



            //"brands/"
            case BRANDS :
            { //Return the entire brands    Table
                retCursor = mOpenHelper.getReadableDatabase().query(
                        GroceryContract.BrandEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            case INVENTORY :
            { //Return the entire brands    Table
                retCursor = mOpenHelper.getReadableDatabase().query(
                        GroceryContract.InventoryEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            default:
                throw new UnsupportedOperationException( "Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
          int rowsUpdated;

        switch (match) {
            case GROCERIES: {      //  (table ,values,whereClause,whereArgs)
                rowsUpdated = db.update(GroceryContract.GroceryEntry.TABLE_NAME,values,selection,selectionArgs);
                break;
            }
            case BRANDS: { //  (table ,values,whereClause,whereArgs)
                rowsUpdated = db.update(GroceryContract.BrandEntry.TABLE_NAME, values, selection,selectionArgs);
                break;
            }
            case CATEGORIES: { //  (table ,values,whereClause,whereArgs)
                rowsUpdated = db.update(GroceryContract.CategoryEntry.TABLE_NAME, values, selection,selectionArgs);
                break;
            }
            case INVENTORY: { //  (table ,values,whereClause,whereArgs)
                rowsUpdated = db.update(GroceryContract.InventoryEntry.TABLE_NAME, values, selection,selectionArgs);
                break;
            }


            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
                 if(rowsUpdated != 0){
            getContext().getContentResolver().notifyChange(uri,null);
             }
        return rowsUpdated;
    }
//TODO Implement Bulkinsert
    @Override
    public int bulkInsert(Uri uri, ContentValues[] values){
      /* final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match =sUriMatcher..match(uri);
        switch match;
        */
        return  0;
    }

    static UriMatcher buildUriMatcher() {
        // All paths added to the UriMatcher have a corresponding code to return when a match is
        // found.  The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = GroceryContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        //The GroceryContract.GroceryEntry.build** will build
        //Uri's to match these formats
        matcher.addURI(authority, GroceryContract.PATH_GROCERIES , GROCERIES);
        matcher.addURI(authority, GroceryContract.PATH_GROCERIES +"/"+GroceryContract.PATH_CATEGORIES + "/*", GROCERIES_WITH_CATEGORIES);
        matcher.addURI(authority, GroceryContract.PATH_GROCERIES +"/"+GroceryContract.PATH_CATEGORIES +"/*/*", GROCERIES_WITH_CATEGORIES_AND_BRANDS);
        matcher.addURI(authority, GroceryContract.PATH_GROCERIES +"/"+GroceryContract.PATH_BRANDS + "/*", GROCERIES_WITH_BRANDS);
        matcher.addURI(authority, GroceryContract.PATH_GROCERIES +"/"+ GroceryContract.PATH_INVENTORY,GROCERIES_WITH_INVENTORY);
        matcher.addURI(authority, GroceryContract.PATH_BRANDS , BRANDS);
        matcher.addURI(authority, GroceryContract.PATH_CATEGORIES, CATEGORIES);
        matcher.addURI(authority, GroceryContract.PATH_CATEGORIES + "/*", CATEGORIES_SEARCH);
        matcher.addURI(authority, GroceryContract.PATH_INVENTORY+ "/#",INVENTORY);



       return matcher;
    }


}
