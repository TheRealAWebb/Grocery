package com.example.awebber.grocery.fragments;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.awebber.grocery.R;
import com.example.awebber.grocery.data.GroceryContract;


public class EditInventoryItemFragment extends Fragment  implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String TAG = EditInventoryItemFragment.class.getSimpleName();
    private static final int CATEGORY_LOADER = 0;
    private static final int UPDATE_CATEGORY_LOADER = 1;
    private  String    mCategoryName;
    private  EditText  mQuantityEditText;
    private  TextView  mNameTextView;
    private  EditText  mCategoryEditText;

    public EditInventoryItemFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }
    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_edit_inventory_item, menu);
    }

  // private Ask
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if( item.getItemId() == R.id.confirm_edit) {
         /*  ContentValues contentValues = new ContentValues();
           contentValues.put(GroceryContract.GroceryEntry.COLUMN_CATEGORY_LOC_KEY, 4);
            //If Category text change
            if(!mCategoryName.equals(mCategoryEditText.getText().toString())) {
                String newCategoryId
           //TODO set cateogry local key of grocery item to Lockey of Cateogryedit text if it doesnt exist create it
            ContentResolver contentResolver = getActivity().getContentResolver();
              Cursor cursor = contentResolver.query(
                       GroceryContract.CategoryEntry.CONTENT_URI,
                       new String[]{GroceryContract.CategoryEntry._ID},
                       GroceryContract.CategoryEntry.COLUMN_CATEGORY_NAME+"=?",
                       new String[]{mCategoryEditText.getText().toString()},
                       null
                       );
               //If Category exist set it   to newCategoryId
                if(cursor.moveToFirst()) {
                     newCategoryId = cursor.getString(cursor.getColumnIndex(GroceryContract.CategoryEntry._ID));
                    cursor.close();
                }
               else {
                    cursor.close();
                    contentValues =  new ContentValues;
                    contentValues.put(GroceryContract.CategoryEntry.COLUMN_CATEGORY_NAME,mCategoryEditText.getText().toString());
                    newCategoryId =  contentResolver.insert(
                            GroceryContract.CategoryEntry.CONTENT_URI,
                            contentValues
                    );
                }


                contentResolver.update(
                        GroceryContract.GroceryEntry.CONTENT_URI,
                        contentValues,
                        GroceryContract.GroceryEntry.COLUMN_PRODUCT_NAME + " =?",
                        new String[]{getActivity().getIntent().getStringExtra("ItemName")});
            }*/
            getActivity().finish();
        }

            return true;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(CATEGORY_LOADER, null, this);


        super.onActivityCreated(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        getLoaderManager().restartLoader(CATEGORY_LOADER,null,this);
        View rootView = inflater.inflate(R.layout.fragment_edit_inventory_item, container, false);
        mQuantityEditText = ( EditText) rootView.findViewById(R.id.quantity);
        mNameTextView = ( TextView) rootView.findViewById(R.id.name);
        mQuantityEditText.setText(getActivity().getIntent().getStringExtra("Quantity"));
        mNameTextView.setText(getActivity().getIntent().getStringExtra("ItemName"));


        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {

      String[]  projections  = {GroceryContract.CategoryEntry.COLUMN_CATEGORY_NAME,GroceryContract.CategoryEntry._ID};
      Uri uri                = GroceryContract.CategoryEntry.CONTENT_URI;
      String selection       = GroceryContract.CategoryEntry._ID + " = ? ";
      String[] selectionArgs = {getActivity().getIntent().getStringExtra("GroceryCategoryLocKey")};


        return new CursorLoader(getActivity(),
                uri,
                projections,
                selection,
                selectionArgs,
                null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        cursor.moveToFirst();
        mCategoryName = cursor.getString(0);
        mCategoryEditText = ( EditText) getView().findViewById(R.id.category);
        mCategoryEditText.setText(mCategoryName);
    }
    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

}
