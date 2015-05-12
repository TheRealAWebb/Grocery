/**
 * Title: GroceryAdapter.java
 * Created by Alton Webber on 4/20/15.
 * Description:
 *
 * Purpose:
 *
 *
 *
 * Usage:
 *
 *
 **/
package com.example.awebber.grocery.adapter;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.example.awebber.grocery.R;
import com.example.awebber.grocery.data.GroceryContract;

public class GroceryCursorAdapter extends CursorAdapter {
    private static final String TAG = "GroceryCursorAdapter";
    private Context mContext;
    public GroceryCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        mContext =context;

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_grocery, parent, false);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

       //cursor.getString(0)) Represents the first column returned
       //In the case the Table Name the cursor.getString(1)) will be the item
        Log.e("cursor.getCount()", String.valueOf(cursor.getCount()));


        TextView tv = (TextView) view.findViewById(R.id.list_item_grocery_textview);
        if( GroceryContract.GroceryEntry.TABLE_NAME.equals( cursor.getString(0) ) ||
                GroceryContract.CategoryEntry.TABLE_NAME.equals( cursor.getString(0) ) ||
                GroceryContract.BrandEntry.TABLE_NAME.equals( cursor.getString(0) )  )  {
            Log.e("cursor.getString","Table " + cursor.getString(0) + " Value: "+ cursor.getString(1));
            tv.setText("Table: " + cursor.getString(0) + " Value: " + cursor.getString(1));
        }


        else  {
            tv.setText(  cursor.getString(0) );
            Log.e("Category ", cursor.getString(0));
        }

        }

    }
