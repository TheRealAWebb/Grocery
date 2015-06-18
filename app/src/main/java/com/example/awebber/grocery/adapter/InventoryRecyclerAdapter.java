package com.example.awebber.grocery.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.awebber.grocery.R;
import com.example.awebber.grocery.data.GroceryContract;
import com.example.awebber.grocery.fragments.CategoryFragment;
import com.example.awebber.grocery.fragments.InventoryFragment;

/**
 * Created by awebber on 6/13/15.
 */
public class InventoryRecyclerAdapter extends  RecyclerView.Adapter<InventoryRecyclerAdapter.AdapterViewHolder> {

    private Cursor mCursor;
    final private Context mContext;
    static private RecyclerAdapterOnClickHandler mOnClickHandler;
    final private View mEmptyView;
    static private RecyclerAdapterOnLongClickHandler mOnLongClickHandler;

    public class AdapterViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {
        public final TextView mQuantityTextView;
        public final LinearLayout mTextPortion;
        public final ImageButton mImageButton;
        public final TextView mItemNameTextView;

        public AdapterViewHolder(View view) {
            super(view);
            mItemNameTextView = (TextView) view.findViewById(R.id.list_item_grocery_textview);
            mQuantityTextView =(TextView) view.findViewById(R.id.item_quantity_textview);
            mTextPortion = (LinearLayout) view.findViewById(R.id.text_portion);
            mImageButton = (ImageButton) view.findViewById(R.id.increase_quantity);
            mImageButton.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view){
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            if (mCursor != null ) {
                //  mOnClickHandler.onClick(adapterPosition,mCursor.getString(InventoryFragment.COLUMN_QUANTITY),mCursor.getString(InventoryFragment._ID), this);

                switch (view.getId()) {
                    case R.id.increase_quantity:
                        mOnClickHandler.onClick(adapterPosition, mCursor.getString(InventoryFragment.COLUMN_QUANTITY), mCursor.getString(InventoryFragment._ID), this);

                        break;
                    case R.id.text_portion:
                        // it was the second button
                        break;
                }
           }
        }
        @Override
        public boolean onLongClick(View v){
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            if (mCursor != null) {
                mOnLongClickHandler.onLongClick(
                        mCursor.getString(InventoryFragment._ID),
                        mCursor.getString(InventoryFragment.COLUMN_QUANTITY),
                        mCursor.getString(InventoryFragment.COLUMN_PRODUCT_NAME),
                        mCursor.getString(InventoryFragment.COLUMN_CATEGORY_LOC_KEY)
                );
            }
            return true;
        }
    }
    public static interface RecyclerAdapterOnClickHandler {
        void onClick(int adapterPosition,String quantity,String id, AdapterViewHolder categoryAdapterViewHolder);
    }
    public static interface RecyclerAdapterOnLongClickHandler{
        void onLongClick(String id,String quantity,String productName,String categoryLocKey);
    }



    public  InventoryRecyclerAdapter(Context context,RecyclerAdapterOnClickHandler adapterViewHolder,View emptyView) {
        mContext = context;
        mOnClickHandler  = adapterViewHolder;
        mEmptyView = emptyView;
    }

    public  InventoryRecyclerAdapter(Context context,View emptyView) {
        mContext = context;
        mEmptyView = emptyView;
    }

    public void setOnItemClickListener(RecyclerAdapterOnClickHandler recyclerAdapterOnClickHandler){
        mOnClickHandler = recyclerAdapterOnClickHandler;
    }
    public void setOnItemLongClickListener(RecyclerAdapterOnLongClickHandler recyclerAdapterOnLongClickHandler){
        mOnLongClickHandler = recyclerAdapterOnLongClickHandler;
    }
    @Override
    public AdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewGroup instanceof RecyclerView) {
            View view = LayoutInflater.from(viewGroup.getContext()).
                    inflate(R.layout.list_item_inventory, viewGroup, false);
            view.setFocusable(true);
            return new AdapterViewHolder(view);
        } else {
            throw new RuntimeException("Not bound to RecyclerViewSelection");
        }
    }

    @Override
    public void onBindViewHolder(AdapterViewHolder adapterViewHolder, int position) {
        mCursor.moveToPosition(position);
        adapterViewHolder.mItemNameTextView.setText(mCursor.getString(InventoryFragment.COLUMN_PRODUCT_NAME));
        adapterViewHolder.mQuantityTextView.setText(mCursor.getString(InventoryFragment.COLUMN_QUANTITY));
    }



    @Override
    public int getItemViewType(int position) {
        return R.layout.list_item_grocery;
    }

    @Override
    public int getItemCount() {
        if ( null == mCursor ) return 0;
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
        mEmptyView.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }

    public Cursor getCursor() {
        return mCursor;
    }
}