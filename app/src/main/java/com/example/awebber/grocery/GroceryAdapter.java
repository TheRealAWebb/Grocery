/*
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
 */
package com.example.awebber.grocery;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.awebber.grocery.data.GroceryContract;

/**
 * Created by awebber on 4/20/15.
 */
public class GroceryAdapter extends CursorAdapter {

    public GroceryAdapter(Context context, Cursor c, int flags) {

        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_grocery, parent, false);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // our view is pretty simple here --- just a text view
        // we'll keep the UI functional with a simple (and slow!) binding.

        Log.e("cursor.getCount()", String.valueOf(cursor.getCount()));
        TextView tv = (TextView) view.findViewById(R.id.list_item_grocery_textview);
        Log.e("cursor.getString", cursor.getString(cursor.getColumnIndex(GroceryContract.GroceryEntry.COLUMN_NAME)));
        tv.setText(cursor.getString(cursor.getColumnIndex(GroceryContract.GroceryEntry.COLUMN_NAME)));
    }

}
