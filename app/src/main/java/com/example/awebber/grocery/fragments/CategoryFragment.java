package com.example.awebber.grocery.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ListView;

import com.example.awebber.grocery.activites.CategoryTabsActivity;
import com.example.awebber.grocery.R;
import com.example.awebber.grocery.activites.CategoryAddActivity;
import com.example.awebber.grocery.adapter.GroceryCursorAdapter;
import com.example.awebber.grocery.data.GroceryContract;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
e
 * to handle interaction events.
 */
public class CategoryFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    public static final String TAG = CategoryFragment.class.getSimpleName();
    public static final int ADD_CATEGORY_REQUEST = 2;
  //  private static Context mContext ;
    private static final int CATEGORY_LOADER = 0;
    //CursorAdapter mGroceryCursorAdapter;
    GroceryCursorAdapter mGroceryCursorAdapter;

    public CategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
     //   mContext =  getActivity();

    }
    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_category, menu);
        //  ((AppCompatActivity) getFragment().getActivity()).getSupportActionBar().
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if( item.getItemId()==R.id.add_category){
            Intent intent = new Intent(getActivity(), CategoryAddActivity.class);
            startActivityForResult(intent,ADD_CATEGORY_REQUEST);
        }

        return true;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_category, container, false);

        ListView groceryListView = (ListView) rootView.findViewById(R.id.list_view_grocery_category);
        View empty = rootView.findViewById(R.id.emptyListElem);
        groceryListView.setEmptyView(empty);

        mGroceryCursorAdapter = new GroceryCursorAdapter(getActivity(),null,0);
        mGroceryCursorAdapter.setDisplayStyle(CategoryFragment.class.getSimpleName());

        groceryListView.setAdapter(mGroceryCursorAdapter);

        getLoaderManager().restartLoader(CATEGORY_LOADER, null, this);


        groceryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

              Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (cursor != null) {

                    Intent intent = new Intent(getActivity(), CategoryTabsActivity.class);
                    intent.putExtra("Position", position);
                    intent.putExtra("ColumnCategory", cursor.getString(0));
                    startActivity(intent);
                }
            }
        });




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
        String sortOrder = GroceryContract.CategoryEntry.COLUMN_CATEGORY_NAME +" COLLATE NOCASE";
        String[] projections =  {GroceryContract.CategoryEntry.COLUMN_CATEGORY_NAME,
                                  GroceryContract.CategoryEntry._ID};

        return new CursorLoader(getActivity(),
                groceriesUri,
                projections,
                null,
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
     * @return a new instance of {@link SearchFragment}
     */
    public static CategoryFragment newInstance() {
        CategoryFragment fragment = new CategoryFragment();
        return fragment;
    }


}
