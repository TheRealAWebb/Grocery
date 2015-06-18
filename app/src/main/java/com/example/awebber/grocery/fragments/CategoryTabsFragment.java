package com.example.awebber.grocery.fragments;

import android.database.Cursor;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import com.example.awebber.grocery.R;
import com.example.awebber.grocery.adapter.GroceryPagerAdapter;
import com.example.awebber.grocery.data.GroceryContract.CategoryEntry;


/**
 * A placeholder fragment containing a simple view.
 */
public class CategoryTabsFragment extends Fragment {
  
    public static final String TAG = CategoryTabsFragment.class.getSimpleName();
    static final CharSequence SearchFragment = ("SearchFragment");
    public CategoryTabsFragment() {
    }

    private int CategoryPosition = -1;

    /** A custom {@link ViewPager
    } title strip which looks much like Tabs present in Android v4.0 and
     * above, but is designed to give continuous feedback to the user when scrolling.
     *  */

    private ViewPager mViewPager;

    private List<GroceryPagerAdapter.SamplePagerItem> mTabs = new ArrayList<GroceryPagerAdapter.SamplePagerItem>();

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CategoryPosition = getActivity().getIntent().getIntExtra("Position",-1);
        String[] projection ={CategoryEntry.COLUMN_CATEGORY_NAME,CategoryEntry._ID};
        String    selection    = null;
        String[] selectionArgs = null;
        String sortOrder = CategoryEntry.COLUMN_CATEGORY_NAME +" COLLATE NOCASE";
        Cursor categories =  getActivity().getContentResolver().
                query(CategoryEntry.CONTENT_URI, projection, selection, selectionArgs, sortOrder );


           while (categories.moveToNext()) {
               mTabs.add(new GroceryPagerAdapter.SamplePagerItem(
                       categories.getString(categories.getColumnIndex(CategoryEntry.COLUMN_CATEGORY_NAME)), // Title
                       Color.YELLOW, // Indicator color
                       Color.GRAY,// Divider color
                       SearchFragment,
                       true

               ));
           }

        categories.close();
       }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cateogory_tabs, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mViewPager.setAdapter(new GroceryPagerAdapter(getChildFragmentManager(),mTabs));

        mViewPager.setCurrentItem( CategoryPosition);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.sliding_tabs);
        tabLayout.setTabTextColors(getResources().getColorStateList(R.color.tab_text_color));
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

    }

}
