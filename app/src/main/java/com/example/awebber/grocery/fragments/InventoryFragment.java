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
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;


import com.example.awebber.grocery.R;
import com.example.awebber.grocery.activites.EditInventoryItemActivity;
import com.example.awebber.grocery.adapter.InventoryCursorAdapter;
import com.example.awebber.grocery.adapter.InventoryRecyclerAdapter;
import com.example.awebber.grocery.adapter.RecyclerAdapter;
import com.example.awebber.grocery.data.GroceryContract;
import com.example.awebber.grocery.zxingbarcode.IntentIntegrator;

/**
 * A simple {@link Fragment} subclass.
 */
public class InventoryFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String TAG = InventoryFragment.class.getSimpleName();
    private InventoryRecyclerAdapter mInventoryRecyclerAdapter;
    private static final int INVENTORY_LOADER = 0;
    public static final int COLUMN_PRODUCT_NAME = 0 ;
    public static final int COLUMN_QUANTITY = 1;
    public static final int _ID =2;
    public static final int COLUMN_CATEGORY_LOC_KEY =3;

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
        FloatingActionButton scanItem = (FloatingActionButton)  rootView.findViewById(R.id.fab_scan_item);
        FloatingActionButton addItem = (FloatingActionButton)  rootView.findViewById(R.id.fab_add_item);
        // RecyclerAdapter mRecyclerAdapter = new RecyclerAdapter(getActivity();
        RecyclerView mRecyclerView = (RecyclerView) rootView.findViewById(R.id.list_view_inventory);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        View empty = rootView.findViewById(R.id.emptyListElem);
        mInventoryRecyclerAdapter = new InventoryRecyclerAdapter(getActivity(),empty);
        mRecyclerView.setAdapter(mInventoryRecyclerAdapter);
        mInventoryRecyclerAdapter.setOnItemClickListener(new InventoryRecyclerAdapter.RecyclerAdapterOnClickHandler() {
            @Override
            public void onClick(int adapterPosition, String quantity, String id, InventoryRecyclerAdapter.AdapterViewHolder AdapterViewHolder) {
                int quantityPlusOne = Integer.valueOf(quantity) + 1;
                ContentValues contentValues = new ContentValues();
                contentValues.put(GroceryContract.InventoryEntry.COLUMN_QUANTITY, quantityPlusOne);

                getActivity().getContentResolver().update(
                        GroceryContract.InventoryEntry.buildInventoryUri(1),
                        contentValues,
                        GroceryContract.InventoryEntry._ID + " =? ",
                        new String[]{id});


                Uri groceriesUri = GroceryContract.GroceryEntry.buildGroceriesInventory();
                String[] projections = {
                        GroceryContract.GroceryEntry.TABLE_NAME + "." + GroceryContract.GroceryEntry.COLUMN_PRODUCT_NAME,
                        GroceryContract.InventoryEntry.TABLE_NAME + "." + GroceryContract.InventoryEntry.COLUMN_QUANTITY,
                        GroceryContract.InventoryEntry.TABLE_NAME + "." + GroceryContract.InventoryEntry._ID,
                        GroceryContract.GroceryEntry.TABLE_NAME + "." + GroceryContract.GroceryEntry.COLUMN_CATEGORY_LOC_KEY};
                Cursor newCusor = getActivity().getContentResolver().query(
                        groceriesUri,
                        projections,
                        null,
                        null,
                        null
                );
                mInventoryRecyclerAdapter.swapCursor(newCusor);
            }
        });
        mInventoryRecyclerAdapter.setOnItemLongClickListener(new InventoryRecyclerAdapter.RecyclerAdapterOnLongClickHandler() {
            @Override
            public void onLongClick(String id,String quantity,String productName,String categoryLocKey) {
                Intent intent = new Intent(getActivity(), EditInventoryItemActivity.class);
                intent.putExtra("ID", id);
                intent.putExtra("Quantity", quantity);
                intent.putExtra("ItemName",  productName);
                intent.putExtra("GroceryCategoryLocKey", categoryLocKey);
                getActivity().startActivity(intent);
            }
        });
        scanItem.setOnClickListener(new View.OnClickListener() {
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
        mInventoryRecyclerAdapter.swapCursor(cursor);
    }
    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mInventoryRecyclerAdapter.swapCursor(null);
    }

    public static InventoryFragment newInstance() {
        InventoryFragment fragment = new InventoryFragment();
        return fragment;
    }
}
