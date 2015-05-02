package com.example.awebber.grocery;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.LoaderManager;
import android.widget.ListView;
import android.widget.TextView;
import com.example.awebber.grocery.data.GroceryContract;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroceryDetailFragament extends Fragment  implements LoaderManager.LoaderCallbacks<Cursor> {

    TextView grocery;
    String theTable;
    String theValue;
    private static final int GROCERY_LOADER = 1;

    public static final String TAG ="GroceryDetailFragment";
    private static Context mContext ;
    GroceryCursorAdapter mGroceryCursorAdapter;
    public GroceryDetailFragament() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
        mContext =  getActivity();
        theTable= getActivity().getIntent().getStringExtra("Table");
        theValue=  getActivity().getIntent().getStringExtra("Value");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        //THe following line gave redundant warning
        mGroceryCursorAdapter = new GroceryCursorAdapter(getActivity(),null,0);
        View rootView = inflater.inflate(R.layout.fragment_grocery_detail, container, false);

        TextView grocery = ( TextView) rootView.findViewById(R.id.detail_text);
       grocery.setText( theValue + Utility.groceryDetailTableIdentifer(theTable));
      //  grocery.setText( theValue);

       ListView groceryListView = (ListView) rootView.findViewById(R.id.list_view_grocery_Detail);
        View empty = rootView.findViewById(R.id.emptyListElement);
     //   groceryListView.setEmptyView(empty);
     //   groceryListView.setAdapter(mGroceryCursorAdapter);


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

        switch (theTable){
                case GroceryContract.GroceryEntry.TABLE_NAME: {
             //IF its a grocery proper name return
                break;
            }
            case GroceryContract.BasicDescriptionEntry.TABLE_NAME: {
                //
                break;
            }
            case GroceryContract.BrandEntry.TABLE_NAME: {
                //Get the ID of where the brand name is equal to  thevalue
                break;
            }
        }

        //Get the ID of where the brand name is equal to brand value
        mContext.getContentResolver().query(GroceryContract.BrandEntry.CONTENT_URI,null,null,null,null);

        String[] projections = null;

        String selection =
        GroceryContract.GroceryEntry.COLUMN_BRAND_LOC_KEY + "=";
       // String[] selectionargs =
        //        String.valueOf(mContext.getContentResolver().query(GroceryContract.GroceryEntry.TABLE_NAME),null,null,null,null).getInt(0));


        //mContext.getContentResolver().query(i,null,null,null,null);

         return new CursorLoader(getActivity(),
                 GroceryContract.GroceryEntry.CONTENT_URI,
                 projections,
                 null,
                 null,
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
