package com.example.awebber.grocery.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.awebber.grocery.R;
import com.example.awebber.grocery.fragments.CategoryFragment;

/**
 * Created by awebber on 6/12/15.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.AdapterViewHolder> {

    private Cursor mCursor;
    final private Context mContext;
    static private RecyclerAdapterOnClickHandler mOnClickHandler;
    final private View mEmptyView;
    static private RecyclerAdapterOnLongClickHandler mOnLongClickHandler;

    public class AdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public final TextView mTextView;

        public AdapterViewHolder(View view) {
            super(view);
            mTextView = (TextView) view.findViewById(R.id.list_item_grocery_textview);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v){
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            if (mCursor != null) {
                mOnClickHandler.onClick(adapterPosition,mCursor.getString(CategoryFragment.COLUMN_CATEGORY_NAME), this);}
        }
        @Override
        public boolean onLongClick(View v){
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            if (mCursor != null) {
                mOnLongClickHandler.onLongClick(adapterPosition,
                        mCursor.getString(CategoryFragment.COLUMN_CATEGORY_NAME), this);
            }
            return true;
        }
    }
    public static interface RecyclerAdapterOnClickHandler {
        void onClick(int adapterPosition,String categoryName, AdapterViewHolder categoryAdapterViewHolder);
    }
    public static interface RecyclerAdapterOnLongClickHandler{
        void onLongClick(int adapterPosition,String categoryName, AdapterViewHolder categoryAdapterViewHolder);
    }

    public void setOnItemLongClickListener(RecyclerAdapterOnLongClickHandler recyclerAdapterOnLongClickHandler){
        mOnLongClickHandler = recyclerAdapterOnLongClickHandler;
    }

    public RecyclerAdapter(Context context,RecyclerAdapterOnClickHandler adapterViewHolder,View emptyView) {
        mContext = context;
        mOnClickHandler  = adapterViewHolder;
        mEmptyView = emptyView;
    }
    public void setOnItemClickListener(RecyclerAdapterOnClickHandler recyclerAdapterOnClickHandler){
        mOnClickHandler = recyclerAdapterOnClickHandler;
    }

    @Override
    public AdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewGroup instanceof RecyclerView) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_grocery, viewGroup, false);
            view.setFocusable(true);
            return new AdapterViewHolder(view);
        } else {
            throw new RuntimeException("Not bound to RecyclerViewSelection");
        }
    }

    @Override
    public void onBindViewHolder(AdapterViewHolder categoryAdapterViewHolder, int position) {
        mCursor.moveToPosition(position);
        categoryAdapterViewHolder.mTextView.setText(
                mCursor.getString(CategoryFragment.COLUMN_CATEGORY_NAME)
        );
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

