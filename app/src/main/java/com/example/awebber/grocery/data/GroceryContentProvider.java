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

public class GroceryContentProvider extends ContentProvider {
//Uri Matcher
//Content providers implete functionality bases upo uri's passsed ot them

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    //
    static final int GROCERIES = 100;
    static final int GROCERIES_WITH_BRANDS = 102;
    static final int GROCERIES_WITH_BASIC_DESCRIPTIONS =103;
    static final int GROCERIES_WITH_BASIC_DESCRIPTIONS_AND_BRANDS =105;

    static final int BRANDS = 200;
    //static final int BRANDS_WITH_GROCERIES = 201;

    static final int BASIC_DESCRIPTIONS = 300;
   // static final int BASIC_DESCRIPTIONS_WITH_GROCERIES = 301;

    private GroceryDbHelper mOpenHelper;




    //This defines the "From" statemeny of the Query
    private static final SQLiteQueryBuilder sGroceryByBrandorBasicDescriptionQueryBuilder;
    static{
        sGroceryByBrandorBasicDescriptionQueryBuilder = new SQLiteQueryBuilder();

    //This is an inner join which looks like
    //groceries INNER JOIN brands ON groceries.brand_id = brands._id
    //groceries INNER JOIN basic_descriptions ON groceries.basic_description_id = basic_description_id
        sGroceryByBrandorBasicDescriptionQueryBuilder.setTables(
                GroceryContract.GroceryEntry.TABLE_NAME + " INNER JOIN " +
                        GroceryContract.BrandEntry.TABLE_NAME +
                        " ON " + GroceryContract.GroceryEntry.TABLE_NAME +
                        "." + GroceryContract.GroceryEntry.COLUMN_BRAND_LOC_KEY +
                        " = " + GroceryContract.BrandEntry.TABLE_NAME +
                        "." + GroceryContract.BrandEntry._ID + "%n" +
                        " INNER JOIN " + GroceryContract.BasicDescriptionEntry.TABLE_NAME +
                        " ON " + GroceryContract.GroceryEntry.TABLE_NAME +
                        "." + GroceryContract.GroceryEntry.COLUMN_BASIC_DESC_LOC_KEY +
                        " = " + GroceryContract.BasicDescriptionEntry.TABLE_NAME +
                        "." + GroceryContract.BasicDescriptionEntry._ID) ;

    }
    //Buildings the select statements that set up the condition
    // Where condtion1 (< ?,= ?,<= ?,> ?,>= ? ,= ?)  selectionArgs[]


    //Where Brands.brand_name = ?
    private static final String sBrandSelection =
            GroceryContract.BrandEntry.TABLE_NAME +
                    "." + GroceryContract.BrandEntry.COLUMN_BRAND_NAME + " = ? ";

    //Where basic_descriptions.product_type = ?
    private static final String sBasicDescriptionSelection =
            GroceryContract.BasicDescriptionEntry.TABLE_NAME +
                    "." + GroceryContract.BasicDescriptionEntry.COLUMN_PRODUCT_TYPE + " = ? ";

    //Wherebasic_descriptions.product_type = ? AND Brands.brand_name = ?
    private static final String sBasicDescriptionAndBrandSelection =
            GroceryContract.BasicDescriptionEntry.TABLE_NAME +
                    "." + GroceryContract.BasicDescriptionEntry.COLUMN_PRODUCT_TYPE + " = ? AND " +
                    GroceryContract.BrandEntry.TABLE_NAME +
                    "." + GroceryContract.BrandEntry.COLUMN_BRAND_NAME + " = ? ";

    //When the Uri is passed through the Uri matcher this function will be called
    //During the query and return this cursor
    private Cursor getGrociesByBrandName(Uri uri, String[] projection, String sortOrder) {
        String BrandName = GroceryContract.GroceryEntry.getBrandNameFromUri(uri);

        String[] selectionArgs ={BrandName} ;
        String selection = sBrandSelection;

        return sGroceryByBrandorBasicDescriptionQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    public GroceryContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
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
            case GROCERIES_WITH_BASIC_DESCRIPTIONS:
                return GroceryContract.GroceryEntry.CONTENT_TYPE;
            case  GROCERIES_WITH_BRANDS:
                return GroceryContract.GroceryEntry.CONTENT_TYPE;
            case GROCERIES_WITH_BASIC_DESCRIPTIONS_AND_BRANDS:
                return GroceryContract.GroceryEntry.CONTENT_ITEM_TYPE;
            case BASIC_DESCRIPTIONS:
                return GroceryContract.BasicDescriptionEntry.CONTENT_TYPE;
            case  BRANDS:
                return GroceryContract.BrandEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
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
            case BASIC_DESCRIPTIONS: {
                long _id = db.insert(
                        GroceryContract.BasicDescriptionEntry.TABLE_NAME, //table
                        null,                                             //nullColumnHack
                        values);                                           //Values
                if ( _id > 0 )
                returnUri = GroceryContract.BasicDescriptionEntry.buildBasicDescriptionsUri(_id);
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
    public boolean onCreate() {
        //  Implement this to initialize your content provider on startup.
       mOpenHelper = new GroceryDbHelper(getContext());
        return true;
    }
    // Queries the user dictionary and returns results
   // mCursor = getContentResolver().query(
   //         UserDictionary.Words.CONTENT_URI,   // The content URI of the words table
   //         mProjection,                        // The columns to return for each row
   //         mSelectionClause                    // Selection criteria
   //                 mSelectionArgs,                     // Selection criteria
   //         mSortOrder);                        // The sort order for the returned rows
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
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
            case BASIC_DESCRIPTIONS:
            {  // Return the entire basic_desriptions Table
                retCursor = mOpenHelper.getReadableDatabase().query(
                        GroceryContract.BasicDescriptionEntry.TABLE_NAME, // The  table
                        projection,                                       // The columns to return for each row
                        selection,          // Selection criteria
                        selectionArgs,      // Selection criteria Args
                        null,                 //  groupBy
                        null,                  //     having
                        sortOrder               // orderBy

                );
                break;
            }
            //"brands/"
            case BRANDS :
            { //Return the entire brands    Table
                retCursor =mOpenHelper.getReadableDatabase().query(
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
   /*         //"groceries/brands/*"
            case  GROCERIES_WITH_BRANDS:
            {
             //   retCursor = getGrociesByBrandName();
                break;
            }

            // "groceries/basic_Descriptions/*"
            case  GROCERIES_WITH_BASIC_DESCRIPTIONS:
            {

                break;
            }


            // "groceries/basic_descriptions/Brands/*"
            case GROCERIES_WITH_BASIC_DESCRIPTIONS_AND_BRANDS:
            {

                break;
            }
*/

 /*   case GROCERIES_WITH_BASIC_DESCRIPTIONS_AND_BRANDS:
            {
                retCursor =mOpenHelper.getReadableDatabase().query(
                        GroceryContract.BrandEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            } */



            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
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
        matcher.addURI(authority, GroceryContract.PATH_GROCERIES, GROCERIES);
        matcher.addURI(authority, GroceryContract.PATH_BRANDS , BRANDS);
        matcher.addURI(authority, GroceryContract.PATH_BASIC_DESC , BASIC_DESCRIPTIONS);

        matcher.addURI(authority, GroceryContract.PATH_GROCERIES + GroceryContract.PATH_BASIC_DESC + "/*", GROCERIES_WITH_BASIC_DESCRIPTIONS);
        matcher.addURI(authority, GroceryContract.PATH_GROCERIES + GroceryContract.PATH_BRANDS + "/*", GROCERIES_WITH_BRANDS);
        matcher.addURI(authority, GroceryContract.PATH_GROCERIES + "/*/*", GROCERIES_WITH_BASIC_DESCRIPTIONS_AND_BRANDS);

      //TODO NOT SURE IF THIS WILL CONFLICTION WITH BRANDS THEY SHARE THE SAME FORMAT
      //matcher.addURI(authority, GroceryContract.PATH_GROCERIES + "/*", GROCERIES_WITH_BASIC_DESCRIPTIONS);
//TODO how to add a grocery item add brand get back the id add basic_Depscrtion get back and id then add the grocery with the id's as location for key values


//



        return matcher;
    }


}
