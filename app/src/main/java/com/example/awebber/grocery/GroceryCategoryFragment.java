package com.example.awebber.grocery;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.awebber.grocery.adapter.GroceryCursorAdapter;
import com.example.awebber.grocery.data.GroceryContract;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
e
 * to handle interaction events.
 */
public class GroceryCategoryFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    public static final String TAG ="GroceryCategoryFragment";
    public static final int ADD_CATEGORY_REQUEST = 2;
    private static Context mContext ;
    private static final int CATEGORY_LOADER = 0;
    //CursorAdapter mGroceryCursorAdapter;
    GroceryCursorAdapter mGroceryCursorAdapter;

    public GroceryCategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
        mContext =  getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        mGroceryCursorAdapter = new GroceryCursorAdapter(getActivity(),null,0);
        View rootView = inflater.inflate(R.layout.fragment_grocery_category, container, false);


        ListView groceryListView = (ListView) rootView.findViewById(R.id.list_view_grocery_category);

        View empty = rootView.findViewById(R.id.emptyListElem);
        groceryListView.setEmptyView(empty);
        groceryListView.setAdapter(mGroceryCursorAdapter);

        getLoaderManager().restartLoader(CATEGORY_LOADER, null, this);
        ImageButton cateogryadd = (ImageButton) rootView.findViewById(R.id.add_category);

        groceryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // CursorAdapter returns a cursor at the correct position for getItem(), or null
                //   if it cannot seek to that position.
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (cursor != null) {
                   // Log.e(TAG, "Column Value : " + cursor.getString(0));
                    Intent intent = new Intent(getActivity(), GroceryCateogoryTabs.class);
                    intent.putExtra("Position", position);
                    intent.putExtra("ColumnCategory", cursor.getString(0));
                    startActivity(intent);
                }
            }
        });





        cateogryadd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), GroceryAddCategoryActivity.class);
               startActivityForResult(intent,ADD_CATEGORY_REQUEST);
            }
        });
        // Inflate the layout for this fragment
        return rootView;
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(CATEGORY_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }



    @Override
     public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        Uri groceriesUri = GroceryContract.CategoryEntry.CONTENT_URI;
        //Selection is the Where clause
        String selection = null ;
        String sortOrder = GroceryContract.CategoryEntry.COLUMN_CATEGORY_NAME +" COLLATE NOCASE";
        String[] projections =  {GroceryContract.CategoryEntry.COLUMN_CATEGORY_NAME, GroceryContract.CategoryEntry._ID};

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
    /**
     * @return a new instance of {@link GrocerySearchFragment}
     */
    public static GroceryCategoryFragment newInstance() {
        GroceryCategoryFragment fragment = new GroceryCategoryFragment();
        return fragment;
    }


}
