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
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.example.awebber.grocery.data.GroceryDbHelper;

public class GroceryContentProvider extends ContentProvider {
//Uri Matcher
//Content providers implete functionality bases upo uri's passsed ot them

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    //
    static final int GROCERIES = 100;
    static final int GROCERIES_WITH_BRANDS = 102;
    static final int GROCERIES_WITH_BASIC_DESCRIPTION =103;
    static final int GROCERIES_WITH_BASIC_DESCRIPTION_AND_BRANDS =105;

    static final int BRANDS = 200;

    static final int  BASIC_DESCRIPTION = 300;

    private GroceryDbHelper mOpenHelper;

    //This defines the From statemen of the Query
    private static final SQLiteQueryBuilder sGroceryByBrandQueryBuilder;
    static{
        sGroceryByBrandQueryBuilder = new SQLiteQueryBuilder();

    //This is an inner join which looks like
    //weather INNER JOIN location ON weather.location_id = location._id
        sGroceryByBrandQueryBuilder.setTables(
    GroceryContract.GroceryEntry.TABLE_NAME + " INNER JOIN " +
    GroceryContract.BrandEntry.TABLE_NAME +
            " ON " + GroceryContract.GroceryEntry.TABLE_NAME +
            "." + GroceryContract.GroceryEntry.COLUMN_BRAND_LOC_KEY +
            " = " +GroceryContract.BrandEntry.TABLE_NAME +
            "." + GroceryContract.BrandEntry._ID);
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
            // Student: Uncomment and fill out these two cases
            case GROCERIES:
                return GroceryContract.GroceryEntry.CONTENT_TYPE;
            case  GROCERIES_WITH_BRANDS:
                return GroceryContract.GroceryEntry.CONTENT_TYPE;
            case GROCERIES_WITH_BASIC_DESCRIPTION:
                return GroceryContract.GroceryEntry.CONTENT_TYPE;
            case GROCERIES_WITH_BASIC_DESCRIPTION_AND_BRANDS:
                return GroceryContract.GroceryEntry.CONTENT_TYPE;
            case BASIC_DESCRIPTION:
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
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean onCreate() {
        //  Implement this to initialize your content provider on startup.
       mOpenHelper = new GroceryDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            // "weather/*/*"
            case GROCERIES :
            {
                retCursor =mOpenHelper.getReadableDatabase().query(
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
            case BASIC_DESCRIPTION :
            {
                retCursor =mOpenHelper.getReadableDatabase().query(
                        GroceryContract.BasicDescriptionEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case BRANDS :
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
            }

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
        matcher.addURI(authority, GroceryContract.PATH_GROCERIES, GROCERIES);
        matcher.addURI(authority, GroceryContract.PATH_BRANDS , BRANDS);
        matcher.addURI(authority, GroceryContract.PATH_BASIC_DESC , BASIC_DESCRIPTION);

       // matcher.addURI(authority, WeatherContract.PATH_LOCATION, LOCATION);
        return matcher;
    }


}
