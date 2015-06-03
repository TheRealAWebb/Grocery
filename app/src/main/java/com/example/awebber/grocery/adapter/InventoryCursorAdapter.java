package com.example.awebber.grocery.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
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


/**
 * Created by awebber on 6/3/15.
 */
public class InventoryCursorAdapter  extends CursorAdapter {

    private static final String TAG =InventoryCursorAdapter.class.getSimpleName();


    public  InventoryCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);


    }

    public Context getContext(){
        return this.getContext();
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.list_item_inventory, parent, false);
        return view;
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
         final Context cursorcontext =context;
        TextView itemNameTextView = (TextView) view.findViewById(R.id.list_item_grocery_textview);
        ImageButton increaseQuantity = (ImageButton)view.findViewById(R.id.increase_quantity);
        TextView quantityTextView =(TextView) view.findViewById(R.id.item_quantity_textview);
        LinearLayout textViewPortion= (LinearLayout) view.findViewById(R.id.text_portion);
        cursor.getPosition();

        final String ItemName =cursor.getString(cursor.getColumnIndex(GroceryContract.GroceryEntry.COLUMN_PRODUCT_NAME));
        final int quantity = cursor.getInt(cursor.getColumnIndex(GroceryContract.InventoryEntry.COLUMN_QUANTITY));
        final int quantityPlusOne = cursor.getInt(cursor.getColumnIndex(GroceryContract.InventoryEntry.COLUMN_QUANTITY)) + 1;
        final int id = cursor.getInt(cursor.getColumnIndex( GroceryContract.InventoryEntry._ID));
        final String categoryLocKey = cursor.getString(cursor.getColumnIndex( GroceryContract.GroceryEntry.COLUMN_CATEGORY_LOC_KEY));
        increaseQuantity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ContentValues contentValues = new ContentValues();
                    contentValues.put(GroceryContract.InventoryEntry.COLUMN_QUANTITY, quantityPlusOne);

                    cursorcontext.getContentResolver().update(
                            GroceryContract.InventoryEntry.buildInventoryUri(1),
                            contentValues,
                            GroceryContract.InventoryEntry._ID + " =? ",
                            new String[]{Long.toString(id)});


                    Uri groceriesUri = GroceryContract.GroceryEntry.buildGroceriesInventory();
                    String[] projections = {
                            GroceryContract.GroceryEntry.TABLE_NAME + "." + GroceryContract.GroceryEntry.COLUMN_PRODUCT_NAME,
                            GroceryContract.InventoryEntry.TABLE_NAME + "." + GroceryContract.InventoryEntry.COLUMN_QUANTITY,
                            GroceryContract.InventoryEntry.TABLE_NAME + "." + GroceryContract.InventoryEntry._ID,
                            GroceryContract.GroceryEntry.TABLE_NAME+"."+GroceryContract.GroceryEntry.COLUMN_CATEGORY_LOC_KEY};
                    Cursor newCusor = cursorcontext.getContentResolver().query(
                            groceriesUri,
                            projections,
                            null,
                            null,
                            null
                    );
                   InventoryCursorAdapter.this.swapCursor(newCusor);

                }
            });
            textViewPortion.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Intent intent = new Intent(context, EditInventoryItemActivity.class);
                    intent.putExtra("ID", id);
                    intent.putExtra("Quantity", quantity);
                    intent.putExtra("ItemName", ItemName);
                    intent.putExtra("GroceryCategoryLocKey", categoryLocKey);
                    context.startActivity(intent);
                    return true;
                }
            });
            itemNameTextView.setText(ItemName);
            quantityTextView.setText(" Quantity " +  quantity);





    }




}

