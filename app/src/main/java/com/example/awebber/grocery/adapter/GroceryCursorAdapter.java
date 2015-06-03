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

import android.content.Intent;
import android.net.Uri;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.awebber.grocery.R;

import com.example.awebber.grocery.activites.EditInventoryItemActivity;
import com.example.awebber.grocery.data.GroceryContract;
import com.example.awebber.grocery.fragments.InventoryFragment;
import com.example.awebber.grocery.fragments.SearchFragment;

public class GroceryCursorAdapter extends CursorAdapter{

    private static final String TAG = GroceryCursorAdapter.class.getSimpleName();
    private   String mDisplayStyleType;

    public GroceryCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);

    }
   public void setDisplayStyle(String displayStyleType){
       mDisplayStyleType = displayStyleType;
   }

    public Context getContext(){
       return this.getContext();
   }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view;
        if(mDisplayStyleType.equals(InventoryFragment.class.getSimpleName())){
        view = LayoutInflater.from(context).inflate(R.layout.list_item_inventory, parent, false);
        }
        else{
         view = LayoutInflater.from(context).inflate(R.layout.list_item_grocery, parent, false);
        }
        return view;
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        TextView tv = (TextView) view.findViewById(R.id.list_item_grocery_textview);

        final Context cursorcontext =context;
          cursor.getPosition();

         if(mDisplayStyleType.equals(SearchFragment.class.getSimpleName()))  {
            tv.setText("Table: " + cursor.getString(0) + " Value: " + cursor.getString(1));
        }

        else  {
            tv.setText(  cursor.getString(0));
            Log.e(TAG, " Category " + cursor.getString(0));
        }

        }




}
