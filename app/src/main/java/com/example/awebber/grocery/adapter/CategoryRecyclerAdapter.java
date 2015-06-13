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
 * Created by awebber on 6/10/15.
 * {@link CategoryRecyclerAdapter} a list of GroceryStore Categories
 * from a {@link android.database.Cursor} to a {@link android.support.v7.widget.RecyclerView}.
 *
 *
 */
public class CategoryRecyclerAdapter extends RecyclerView.Adapter<CategoryRecyclerAdapter.CategoryAdapterViewHolder> {

    private Cursor mCursor;
    final private Context mContext;
    final private CategoryRecyclerAdapterOnClickHandler mOnClickHandler;
    final private View  mEmptyView;
    static private CategoryRecyclerAdapterOnLongClickHandler mOnLongClickHandler;

    public class CategoryAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

            public final TextView mCategoryTextView;

        public CategoryAdapterViewHolder(View view) {
            super(view);
            mCategoryTextView = (TextView) view.findViewById(R.id.list_item_grocery_textview);
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
    public static interface CategoryRecyclerAdapterOnClickHandler {
        void onClick(int adapterPosition,String categoryName, CategoryAdapterViewHolder categoryAdapterViewHolder);
    }
    public static interface CategoryRecyclerAdapterOnLongClickHandler{
    void onLongClick(int adapterPosition,String categoryName, CategoryAdapterViewHolder categoryAdapterViewHolder);
}

   public void setOnItemLongClickListener(CategoryRecyclerAdapterOnLongClickHandler categoryRecyclerAdapterOnLongClickHandler){
       mOnLongClickHandler = categoryRecyclerAdapterOnLongClickHandler;
   }

    public CategoryRecyclerAdapter(Context context,CategoryRecyclerAdapterOnClickHandler categoryAdapterViewHolder,View emptyView) {
        mContext = context;
        mOnClickHandler  = categoryAdapterViewHolder;
        mEmptyView = emptyView;
    }

        @Override
        public CategoryAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            if (viewGroup instanceof RecyclerView) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_grocery, viewGroup, false);
                view.setFocusable(true);
                return new CategoryAdapterViewHolder(view);
            } else {
                throw new RuntimeException("Not bound to RecyclerViewSelection");
            }
        }

        @Override
        public void onBindViewHolder(CategoryAdapterViewHolder categoryAdapterViewHolder, int position) {
            mCursor.moveToPosition(position);
           categoryAdapterViewHolder.mCategoryTextView.setText(
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