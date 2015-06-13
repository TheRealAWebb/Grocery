package com.example.awebber.grocery.fragments;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.LoaderManager;
import android.widget.ListView;
import android.widget.TextView;

import com.example.awebber.grocery.adapter.GroceryCursorAdapter;
import com.example.awebber.grocery.R;
import com.example.awebber.grocery.Utility;
import com.example.awebber.grocery.data.GroceryContract;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment  implements LoaderManager.LoaderCallbacks<Cursor> {

    TextView grocery;
    String theTable;
    String theValue;
    private static final int GROCERY_LOADER = 1;

    public static final String TAG = DetailFragment.class.getSimpleName();
    private static Context mContext ;
    GroceryCursorAdapter mGroceryCursorAdapter;
    public DetailFragment() {

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
        mContext  =  getActivity();
        theTable = getActivity().getIntent().getStringExtra("Table");
        theValue =  getActivity().getIntent().getStringExtra("Value");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        mGroceryCursorAdapter = new GroceryCursorAdapter(getActivity(),null,1);
        mGroceryCursorAdapter.setDisplayStyle(TAG);
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

       TextView grocery = ( TextView) rootView.findViewById(R.id.detail_text);
       grocery.setText(Utility.groceryDetailTableIdentifer(theTable,theValue));
       ListView groceryListView = (ListView) rootView.findViewById(R.id.list_view_grocery_Detail);
       View empty = rootView.findViewById(R.id.emptyListElement);
       groceryListView.setEmptyView(empty);
                                                                                                                                                                                 groceryListView.setAdapter(mGroceryCursorAdapter);


        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {

        /**TODO Decide what I want list to return
         * Ideas if person clicks on brands show products of that brand
         * if clicks on basic desciption show e.g cookies , show cookies and brands that make them
         * if person clicks on grocery item show brands that makes it and
         *          show what the Item basic description
         *
         * **/
        Uri uri = GroceryContract.GroceryEntry.CONTENT_URI;
        String[] projections = {GroceryContract.GroceryEntry.COLUMN_PRODUCT_NAME ,GroceryContract.GroceryEntry._ID };
        String selection = null;
        String[] selectionArgs = new String[1];

        switch (theTable){
            case GroceryContract.GroceryEntry.TABLE_NAME: {
             //IF its a grocery proper name return
                selectionArgs = null;
                break;
            }
            case GroceryContract.CategoryEntry.TABLE_NAME: {
                Uri theUri = GroceryContract.CategoryEntry.CONTENT_URI;
                ///Get the ID of where the brand name is equal to brand value
                String[] categoryProjection = {GroceryContract.CategoryEntry._ID};
                String categorySelection =  GroceryContract.CategoryEntry.TABLE_NAME +
                        "." + GroceryContract.CategoryEntry.COLUMN_CATEGORY_NAME + " = ? ";
                String[] categorySelectionArgs ={theValue};
                Cursor theID = mContext.getContentResolver().
                        query(
                                theUri,
                                categoryProjection,    //
                                categorySelection,
                                categorySelectionArgs,
                                null
                        );
                theID.moveToFirst(); //Move to first Element
                Log.i(TAG, "This is the id of " + theValue + " " + theID.getString(0));
                selection = GroceryContract.GroceryEntry.TABLE_NAME +
                        "." + GroceryContract.GroceryEntry.COLUMN_CATEGORY_LOC_KEY + " = ? ";

                selectionArgs[0] = String.valueOf(theID.getInt(0));
                theID.close();
                break;
            }
            case GroceryContract.BrandEntry.TABLE_NAME: {
                Uri theUri = GroceryContract.BrandEntry.CONTENT_URI;
                ///Get the ID of where the brand name is equal to brand value
                String[] brandProjection = {GroceryContract.BrandEntry._ID};
                String brandSelection =  GroceryContract.BrandEntry.TABLE_NAME +
                        "." + GroceryContract.BrandEntry.COLUMN_PRODUCT_BRAND_NAME + " = ? ";
                String[] brandSelectionArgs ={theValue};
                Cursor theID = mContext.getContentResolver().
                        query(
                                theUri,
                                brandProjection,    //
                                brandSelection,
                                brandSelectionArgs,
                                null
                        );

               theID.moveToFirst(); //Move to first Element
                Log.i(TAG, "This is the id of " + theValue + " " + theID.getString(0));

                selection = GroceryContract.GroceryEntry.TABLE_NAME +
                        "." + GroceryContract.GroceryEntry.COLUMN_BRAND_LOC_KEY + " = ? ";

                selectionArgs[0] = theID.getString(0);
                theID.close();

                break;
            }

            default:
        }
         return new CursorLoader(getActivity(),
                 uri,
                 projections,
                 selection,
                 selectionArgs,
                 null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mGroceryCursorAdapter.swapCursor(cursor);
    }
    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mGroceryCursorAdapter.swapCursor(null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(GROCERY_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }
}
