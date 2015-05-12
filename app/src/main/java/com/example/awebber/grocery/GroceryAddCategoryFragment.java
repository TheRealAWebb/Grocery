package com.example.awebber.grocery;

import android.content.ContentValues;

import android.database.Cursor;
import android.util.Log;

import android.support.v4.app.Fragment;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;


import com.example.awebber.grocery.data.GroceryContract;


/**
 * A placeholder fragment containing a simple view.
 */
public class GroceryAddCategoryFragment extends Fragment  {
    public static final String TAG ="GroceryAddCategoryFrag";
    public GroceryAddCategoryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_grocery_add_category, container, false);
    // final   View popupView = inflater.inflate(R.layout.popup_empty_category, null);

      final  EditText editText = (EditText) rootView.findViewById(R.id.add_category_edit_text);

        ImageButton cateogryConfirm = (ImageButton) rootView.findViewById(R.id.confirm_category);
        cateogryConfirm.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                String selection = GroceryContract.CategoryEntry.TABLE_NAME +
                        "." + GroceryContract.CategoryEntry.COLUMN_CATEGORY_NAME + " = ? ";
                String[] projections =  {GroceryContract.CategoryEntry.COLUMN_CATEGORY_NAME, GroceryContract.CategoryEntry._ID};
                String editTextInput =editText.getText().toString();
                String[] selectionArgs ={editTextInput};
               Cursor checkifexist = getActivity().getContentResolver().query(GroceryContract.CategoryEntry.CONTENT_URI,projections,selection,selectionArgs ,null);

                if (editTextInput.isEmpty() ){
                    Log.i(TAG, " The Category Is Blank");
                          checkifexist.close();
                }
                else  if (checkifexist.moveToFirst()){
                    Log.i(TAG," The Category  Already Exist");
                    checkifexist.close();
                }
                    else   {
                    ContentValues newCategory = new ContentValues();

                    newCategory.put(GroceryContract.CategoryEntry.COLUMN_CATEGORY_NAME,editTextInput);
                    getActivity().getContentResolver().insert(GroceryContract.CategoryEntry.CONTENT_URI, newCategory);
                    checkifexist.close();
                    getActivity().finish();
                }

            }
        });


        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
    }





}
