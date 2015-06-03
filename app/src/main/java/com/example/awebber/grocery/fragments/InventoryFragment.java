/**
 * Title: GroceryFragment.java
 * Created by Alton Webber on 5/2/15.
 * Description:
 *
 * Purpose: Will contain the Fragment that allows users to add
 * and delete items int their fridge and pantry.  Home inventory
 *
 *
 * Usage: Barcode Scan to Add elements.
 * Search Drop down Add elements
 *
 *
 **/
package com.example.awebber.grocery.fragments;


import android.app.Dialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;

import android.widget.ListView;


import com.example.awebber.grocery.R;
import com.example.awebber.grocery.adapter.InventoryCursorAdapter;
import com.example.awebber.grocery.data.GroceryContract;
import com.example.awebber.grocery.zxingbarcode.IntentIntegrator;

/**
 * A simple {@link Fragment} subclass.
 */
public class InventoryFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String TAG = InventoryFragment.class.getSimpleName();
    private InventoryCursorAdapter mInventoryCursorAdapter;
    private static final int INVENTORY_LOADER = 0;

    public InventoryFragment() {

    }

    public static class inventoryDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            return builder.create();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(INVENTORY_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(INVENTORY_LOADER, null, InventoryFragment.this);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_inventory, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
           if( item.getItemId() == R.id.barcode_scan){
               IntentIntegrator integrator = new IntentIntegrator(getActivity());
               integrator.initiateScan();
   }

    return true;
}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_inventory, container, false);

        mInventoryCursorAdapter = new InventoryCursorAdapter(getActivity(),null,0);

        ListView groceryListView = (ListView) rootView.findViewById(R.id.list_view_inventory);
        View empty = rootView.findViewById(R.id.emptyListElem);
        groceryListView.setEmptyView(empty);
        groceryListView.setAdapter(mInventoryCursorAdapter);

        groceryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                int index = cursor.getColumnIndex(GroceryContract.InventoryEntry.COLUMN_QUANTITY);
                Log.i(TAG, Integer.toString(cursor.getInt(index)));
                int quantityPlusOne = cursor.getInt(index) + 1;
                Log.i(TAG, Integer.toString(quantityPlusOne));
                ContentValues contentValues = new ContentValues();
                contentValues.put(GroceryContract.InventoryEntry.COLUMN_QUANTITY, quantityPlusOne);

                getFragment().getActivity().getContentResolver().update(
                        GroceryContract.InventoryEntry.buildInventoryUri(id),
                        contentValues,
                        GroceryContract.InventoryEntry._ID + " =? ",
                        new String[]{Long.toString(id)});

                getLoaderManager().restartLoader(INVENTORY_LOADER, null, InventoryFragment.this);

            }
        });
        return rootView;

    }
    private  Fragment getFragment(){
        return this;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        Uri groceriesUri = GroceryContract.GroceryEntry.buildGroceriesInventory();
        String[] projections =  {
                GroceryContract.GroceryEntry.TABLE_NAME+"."+GroceryContract.GroceryEntry.COLUMN_PRODUCT_NAME,
                GroceryContract.InventoryEntry.TABLE_NAME+"."+GroceryContract.InventoryEntry.COLUMN_QUANTITY,
                GroceryContract.InventoryEntry.TABLE_NAME+"."+GroceryContract.InventoryEntry._ID,
                GroceryContract.GroceryEntry.TABLE_NAME+"."+GroceryContract.GroceryEntry.COLUMN_CATEGORY_LOC_KEY};

        return new CursorLoader(getActivity(),
                groceriesUri,
                projections,
                null, //selection
                null,
                null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mInventoryCursorAdapter.swapCursor(cursor);
    }
    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mInventoryCursorAdapter.swapCursor(null);
    }

    public static InventoryFragment newInstance() {
        InventoryFragment fragment = new InventoryFragment();
        return fragment;
    }
}
