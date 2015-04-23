/*
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
 */
package com.example.awebber.grocery;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.app.LoaderManager;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;

import com.example.awebber.grocery.data.GroceryContract;
import com.example.awebber.grocery.data.GroceryDbHelper;

public class GroceryFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private static Context mContext ;
    private static final int GROCERY_LOADER = 0;
    GroceryAdapter mGroceryAdapter;
    public GroceryFragment() {
            }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Cursor cur = getActivity().getContentResolver().query(GroceryContract.GroceryEntry.CONTENT_URI,
                null, null, null, null);

        mGroceryAdapter = new GroceryAdapter(getActivity(),cur,0);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        EditText grocerySearch = (EditText) rootView.findViewById(R.id.search_products);

        ListView groceryListView = (ListView) rootView.findViewById(R.id.list_view_grocery);

        groceryListView.setAdapter(mGroceryAdapter);
        grocerySearch.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                mGroceryAdapter.getFilter().filter(s.toString());
            }
        });

        mGroceryAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            public Cursor runQuery(CharSequence constraint) {
                return   mGroceryAdapter.fetchGroceriesByName(constraint.toString());
            }
        });
                 return rootView;
    }




    @Override
    public void onStart() {
        super.onStart();
        mContext =  getActivity();
        addGrocery("bread");
        addGrocery("Cheese");
        addGrocery("MAlk with Vitamin R");
        addGrocery("Soda");
        addGrocery("Chips");
        addGrocery("Apple");

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(GROCERY_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
      //  String locationSetting = Utility.getPreferredLocation(getActivity());

       // String[] projection ={GroceryContract.GroceryEntry.COLUMN_NAME};
        // Sort order:  Ascending, by date.
       // String sortOrder = GroceryContract.GroceryEntry.COLUMN_DATE + " ASC";
        Uri groceriesUri = GroceryContract.GroceryEntry.CONTENT_URI;
        //todo DELETE THIS TEST FROM HERE
       // Uri j =   GroceryContract.GroceryEntry.CONTENT_URI;
       Log.e("TEST", GroceryContract.GroceryEntry.CONTENT_URI.toString());
      // TO HERE
        return new CursorLoader(getActivity(),
                groceriesUri,
                null,
                null,
                null,
                null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mGroceryAdapter.swapCursor(cursor);
    }
    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mGroceryAdapter.swapCursor(null);
    }
//TODO IMplemete correctly
    long addGrocery(String product_name) {
        long locationId;

        // First, check if the grocery with this city name exists in the db
        Cursor locationCursor = mContext.getContentResolver().query(
                GroceryContract.GroceryEntry.CONTENT_URI,
                new String[]{GroceryContract.GroceryEntry._ID},
                GroceryContract.GroceryEntry.COLUMN_NAME + " = ?",
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
            locationValues.put(GroceryContract.GroceryEntry.COLUMN_NAME, product_name);
            locationValues.put(GroceryContract.GroceryEntry.COLUMN_BRAND_LOC_KEY,0);
            locationValues.put(GroceryContract.GroceryEntry.COLUMN_BASIC_DESC_LOC_KEY,0);
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




}
