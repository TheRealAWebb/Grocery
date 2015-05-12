/**
 * Title: GroceryFragment.java
 * Created by Alton Webber on 5/2/15.
 * Description:
 *
 * Purpose: Will contain the Fragment that allows users to add
 * and delete items int their fridge and pantry.  Home inventory
 *
 *
 * Usage:
 *
 *
 **/
package com.example.awebber.grocery;



import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;



import com.example.awebber.grocery.adapter.GroceryCursorAdapter;
import com.example.awebber.grocery.data.GroceryContract;
import com.example.awebber.grocery.zxingbarcode.IntentIntegrator;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroceryInventoryFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public final int SCANNER_REQUEST_CODE = 123;
    public static final String TAG ="InventoryFragment";
    GroceryCursorAdapter mGroceryCursorAdapter;
    private static final int INVENTORY_LOADER = 0;
    private String mInputBarcode;
int test =1;
    public GroceryInventoryFragment() {
        // Required empty public constructor
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(INVENTORY_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        mGroceryCursorAdapter = new GroceryCursorAdapter(getActivity(),null,0);
        View rootView = inflater.inflate(R.layout.fragment_grocery_inventory, container, false);
       // Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.my_awesome_toolbar);
     //   toolbar.inflateMenu(R.menu.menu_grocery_inventory);
        ListView groceryListView = (ListView) rootView.findViewById(R.id.list_view_inventory);

        View empty = rootView.findViewById(R.id.emptyListElem);
        groceryListView.setEmptyView(empty);
        groceryListView.setAdapter(mGroceryCursorAdapter);
        Button btnBarcodeScanner = (Button) rootView.findViewById(R.id.btn_barcode_scan);

        btnBarcodeScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(getActivity());
                integrator.initiateScan();

            }

        });
    return rootView;

    }
    private  Fragment getFragment(){
        return this;
    }



    public static GroceryInventoryFragment newInstance() {
        GroceryInventoryFragment fragment = new GroceryInventoryFragment();
        return fragment;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        Uri groceriesUri = GroceryContract.InventoryEntry.CONTENT_URI;
        //Selection is the Where clause
        String selection = null ;
        String[] projections =  {GroceryContract.InventoryEntry.COLUMN_GROCERY_LOC_KEY,
                GroceryContract.InventoryEntry._ID};

        return new CursorLoader(getActivity(),
                groceriesUri,
                projections,
                selection,
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

}
