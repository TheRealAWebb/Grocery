package com.example.awebber.grocery.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.awebber.grocery.R;
import com.example.awebber.grocery.activites.CategoryAddActivity;
import com.example.awebber.grocery.activites.CategoryTabsActivity;
import com.example.awebber.grocery.adapter.CategoryRecyclerAdapter;
import com.example.awebber.grocery.data.GroceryContract;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
e
 * to handle interaction events.
 */
public class CategoryFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    public static final String TAG = CategoryFragment.class.getSimpleName();
    public static final int COLUMN_CATEGORY_NAME = 0;
    public static final int COLUMN_CATEGORY_ID = 1;
    public static final int ADD_CATEGORY_REQUEST = 2;
    private RecyclerView mRecyclerView;
    private static final int CATEGORY_LOADER = 0;

    CategoryRecyclerAdapter mCategoryRecyclerAdapter;

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
        View empty = rootView.findViewById(R.id.empty_recycler_view);

        mCategoryRecyclerAdapter = new CategoryRecyclerAdapter(getActivity(),
                new CategoryRecyclerAdapter.CategoryRecyclerAdapterOnClickHandler() {
            @Override
            public void onClick(int adapterPosition, String categoryName,
                                CategoryRecyclerAdapter.CategoryAdapterViewHolder categoryAdapterViewHolder) {
                Intent intent = new Intent(getActivity(), CategoryTabsActivity.class);
                intent.putExtra("Position", adapterPosition);
                intent.putExtra("ColumnCategory", categoryName);
                startActivity(intent);
            }
        },empty);

        mCategoryRecyclerAdapter.setOnItemLongClickListener(new CategoryRecyclerAdapter.CategoryRecyclerAdapterOnLongClickHandler() {
                                                                @Override
                                                                public void onLongClick(int adapterPosition,
                                                                                        String categoryName, CategoryRecyclerAdapter.CategoryAdapterViewHolder categoryAdapterViewHolder) {
                                                                    Intent intent = new Intent(getActivity(), CategoryTabsActivity.class);
                                                                    intent.putExtra("Position", adapterPosition);
                                                                    intent.putExtra("ColumnCategory", categoryName);
                                                                    startActivity(intent);
                                                                }
                                                            }

        );
        mRecyclerView= (RecyclerView) rootView.findViewById(R.id.recyclerview_category);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mCategoryRecyclerAdapter);

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
        mCategoryRecyclerAdapter.swapCursor(cursor);
        updateEmptyView();
    }
    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mCategoryRecyclerAdapter.swapCursor(null);
    }

    private void updateEmptyView() {
        if ( mCategoryRecyclerAdapter.getItemCount() == 0 ) {
           TextView tv = (TextView) getView().findViewById(R.id.empty_recycler_view);

        }
    }
    /**
     * @return a new instance of {@link CategoryFragment}
     */
    public static CategoryFragment newInstance() {
        CategoryFragment fragment = new CategoryFragment();
        return fragment;
    }


}
