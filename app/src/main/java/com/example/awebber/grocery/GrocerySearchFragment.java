/**
 * Title: GroceryFragment.java
 * Created by Alton Webber on 4/20/15.
 * Description:
 *
 * Purpose:
 *
 *
 *
 * Usage:
 *
 *
 **/
package com.example.awebber.grocery;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.app.LoaderManager;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;


import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.awebber.grocery.adapter.GroceryCursorAdapter;
import com.example.awebber.grocery.data.GroceryContract;

public class GrocerySearchFragment extends Fragment implements SearchView.OnQueryTextListener, LoaderManager.LoaderCallbacks<Cursor>{
    public static final String TAG ="GrocerySearchFragment";
    private static Context mContext ;

    // If non-null, this is the current filter the user has provided.
    String mCurFilter;
    private static final int GROCERY_LOADER = 0;

    GroceryCursorAdapter mGroceryCursorAdapter;
    public GrocerySearchFragment() {
            }





   // implemention of SearchView.OnQueryTextListener
    public boolean onQueryTextChange(String newText) {
        // Called when the action bar search text has changed.  Update
        // the search filter, and restart the loader to do a new query
        // with this filter.
        mCurFilter = !TextUtils.isEmpty(newText) ? newText : null;
        getLoaderManager().restartLoader(GROCERY_LOADER, null, this);
        return true;
    }
    @Override public boolean onQueryTextSubmit(String query) {
        // Don't care about this.
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
        mContext =  getActivity();

        //intent.putExtra("Position", position);
        addGrocery("Chicken");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mGroceryCursorAdapter = new GroceryCursorAdapter(getActivity(),null,0);

        View rootView = inflater.inflate(R.layout.fragment_grocery_search, container, false);
        SearchView grocerySearch = ( SearchView) rootView.findViewById(R.id.search_products);
        grocerySearch.setOnQueryTextListener(this);

        ListView groceryListView = (ListView) rootView.findViewById(R.id.list_view_grocery);
        View empty = rootView.findViewById(R.id.emptyListElement);
        groceryListView.setEmptyView(empty);
        groceryListView.setAdapter(mGroceryCursorAdapter);

        /*TODO Onclick listern that starts the detail Actitvity and passes the URI to query the data
        and a
        */

        // We'll call our GrocerySearchActivity
        groceryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            // CursorAdapter returns a cursor at the correct position for getItem(), or null
            // if it cannot seek to that position.
            Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
            if (cursor != null) {
                Log.e( TAG, "Column Value : "+ cursor.getString(0) +" Column name :"+ cursor.getColumnName(0));
                  Intent intent = new Intent(getActivity(), GroceryDetailActivity.class);
                    intent.putExtra("Table",cursor.getString(0));
                    intent.putExtra("Value",cursor.getString(1));
                startActivity(intent);
            }
        }
    });
                 return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(GROCERY_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        String sortOrder = GroceryContract.GroceryEntry.COLUMN_PRODUCT_NAME;
        String selection = GroceryContract.CategoryEntry.COLUMN_CATEGORY_NAME;
        Uri groceriesUri ;
        String[] projections = new String[3];
        String[] selectionArgs = new String[1];
        if ( !getArguments().getString("theCategory").equals("GrocerySearchFragment"))
        {
            projections[0] = GroceryContract.CategoryEntry.COLUMN_CATEGORY_NAME;
            projections[1] = GroceryContract.CategoryEntry._ID;
            projections[2] =null;
            groceriesUri = GroceryContract.CategoryEntry.CONTENT_URI;
            selection = GroceryContract.CategoryEntry.COLUMN_CATEGORY_NAME;
            Log.i(TAG, getArguments().getString("theCategory"));
            selectionArgs[0] = getArguments().getString("theCategory");
            sortOrder = GroceryContract.CategoryEntry.COLUMN_CATEGORY_NAME +" COLLATE NOCASE";
        }
         else {

             sortOrder = GroceryContract.GroceryEntry.COLUMN_PRODUCT_NAME;
             selectionArgs = null;
             groceriesUri = GroceryContract.GroceryEntry.CONTENT_URI;
             projections[0] = "'" + GroceryContract.GroceryEntry.TABLE_NAME + "'" + " AS table_name";
             projections[1] = GroceryContract.GroceryEntry.COLUMN_PRODUCT_NAME;
             projections[2] = GroceryContract.GroceryEntry._ID;


            if (mCurFilter == null) {
                //A Value that will never be used
                selection = GroceryContract.GroceryEntry.COLUMN_PRODUCT_NAME + " like '%555%' ";

            } else {
                //Search  brands.brand_name ,basic_descriptions.product_type, groceries.product_name
                //For a statment that matches the selection
                //A query that searches all tables for the information
                selection = GroceryContract.GroceryEntry.TABLE_NAME + "." +
                        GroceryContract.GroceryEntry.COLUMN_PRODUCT_NAME +
                        " like '%" + mCurFilter + "%' " +
                        "UNION ALL" +
                        " SELECT " + "'" + GroceryContract.BrandEntry.TABLE_NAME + "'" + " AS table_name, " +
                        GroceryContract.BrandEntry.TABLE_NAME + "." +
                        GroceryContract.BrandEntry.COLUMN_PRODUCT_BRAND_NAME +
                        ", _id FROM " + GroceryContract.BrandEntry.TABLE_NAME +
                        " WHERE " + GroceryContract.BrandEntry.TABLE_NAME + "." +
                        GroceryContract.BrandEntry.COLUMN_PRODUCT_BRAND_NAME +
                        " like '" + mCurFilter + "%' " +
                        "UNION ALL" +
                        " SELECT " + "'" + GroceryContract.CategoryEntry.TABLE_NAME + "'" + " AS table_name, " +
                        GroceryContract.CategoryEntry.TABLE_NAME + "." +
                        GroceryContract.CategoryEntry.COLUMN_CATEGORY_NAME +
                        ", _id FROM " + GroceryContract.CategoryEntry.TABLE_NAME +
                        " Where " + GroceryContract.CategoryEntry.TABLE_NAME + "." +
                        GroceryContract.CategoryEntry.COLUMN_CATEGORY_NAME +
                        " like '%" + mCurFilter + "%' ";
                Log.i(TAG, " " + projections[0] + " " + projections[1] + selection);

                // selection = GroceryContract.GroceryEntry.COLUMN_PRODUCT_NAME + " like '%" + mCurFilter + "%'" ;
            }
        }
        return new CursorLoader(getActivity(),
                groceriesUri,
                projections,
                selection,
                null,
                sortOrder);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mGroceryCursorAdapter.swapCursor(cursor);
    }
    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mGroceryCursorAdapter.swapCursor(null);
    }

//TODO IMplemete correctly
    long addGrocery(String product_name) {
        long locationId;

        // First, check if the grocery with this city name exists in the db
        Cursor locationCursor = mContext.getContentResolver().query(
                GroceryContract.GroceryEntry.CONTENT_URI,
                new String[]{GroceryContract.GroceryEntry._ID},
                GroceryContract.GroceryEntry.COLUMN_PRODUCT_NAME + " = ?",
                new String[]{product_name},
                null);

        if (locationCursor.moveToFirst()) {
            int locationIdIndex = locationCursor.getColumnIndex(GroceryContract.GroceryEntry._ID);
            locationId = locationCursor.getLong(locationIdIndex);
        } else {
            // Now that the content provider is set up, inserting rows of data is pretty simple.
            // First create a ContentValues object to hold the data you want to insert.
            ContentValues locationValues = new ContentValues();

            // Then add the data, along with the corresponding name of the data type,
            // so the content provider knows what kind of value is being inserted.
            locationValues.put(GroceryContract.GroceryEntry.COLUMN_PRODUCT_NAME, product_name);
            locationValues.put(GroceryContract.GroceryEntry.COLUMN_BRAND_LOC_KEY,0);
            locationValues.put(GroceryContract.GroceryEntry.COLUMN_CATEGORY_LOC_KEY,0);
            // Finally, insert location data into the database.
            Uri insertedUri = mContext.getContentResolver().insert(
                    GroceryContract.GroceryEntry.CONTENT_URI,
                    locationValues
            );

            // The resulting URI contains the ID for the row.  Extract the locationId from the Uri.
            locationId = ContentUris.parseId(insertedUri);
        }

        locationCursor.close();
        // Wait, that worked?  Yes!
        return locationId;
    }

    /**
     * @return a new instance of {@link GrocerySearchFragment}
     *String theCategory allows the Category to be passed for the query
     *
     **/
    public static GrocerySearchFragment newInstance(String theCategory) {
        GrocerySearchFragment fragment = new GrocerySearchFragment();


        Bundle args = new Bundle();
        args.putString("theCategory", theCategory);
        fragment.setArguments(args);

        return fragment;
    }


}
