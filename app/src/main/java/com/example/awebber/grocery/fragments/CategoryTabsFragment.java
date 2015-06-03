package com.example.awebber.grocery.fragments;

import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import com.example.awebber.grocery.fragments.MainFragment.SamplePagerItem;
import com.example.awebber.grocery.R;
import com.example.awebber.grocery.adapter.GroceryPagerAdapter;
import com.example.awebber.grocery.data.GroceryContract.CategoryEntry;
import com.example.awebber.grocery.view.SlidingTabLayout;

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
    private SlidingTabLayout mSlidingTabLayout;


    /**
     * A {@link ViewPager} which will be used in conjunction with the {@link SlidingTabLayout} above.
     */
    private ViewPager mViewPager;


    private List<SamplePagerItem> mTabs = new ArrayList<SamplePagerItem>();

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
               mTabs.add(new SamplePagerItem(
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

        // BEGIN_INCLUDE (setup_viewpager)
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mViewPager.setAdapter(new GroceryPagerAdapter(getChildFragmentManager(),mTabs));
        // END_INCLUDE (setup_viewpager)
        mViewPager.setCurrentItem( CategoryPosition);
        // BEGIN_INCLUDE (setup_slidingtablayout)
        // Give the SlidingTabLayout the ViewPager, this must be done AFTER the ViewPager has had
        // it's PagerAdapter set.
        mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setViewPager(mViewPager);

        // BEGIN_INCLUDE (tab_colorizer)
        // Set a TabColorizer to customize the indicator and divider colors. Here we just retrieve
        // the tab at the position, and return it's set color
        mSlidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {

            @Override
            public int getIndicatorColor(int position) {
                return mTabs.get(position).getIndicatorColor();
            }

            @Override
            public int getDividerColor(int position) {
                return mTabs.get(position).getDividerColor();
            }

        });
        // END_INCLUDE (tab_colorizer)
        // END_INCLUDE (setup_slidingtablayout)
    }

}
