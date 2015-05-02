package com.example.awebber.grocery;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.awebber.grocery.view.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by awebber on 5/1/15.
 */
public class GroceryMainFragment extends Fragment {
    /**
     * This class represents a tab to be displayed by {@link android.support.v4.view.ViewPager} and it's associated
     * {@link SlidingTabLayout}.
     */
    static class SamplePagerItem {
        private final CharSequence mTitle;
        private final int mIndicatorColor;
        private final int mDividerColor;
        private final CharSequence mInstanceofFragment;

        SamplePagerItem(CharSequence title, int indicatorColor, int dividerColor,CharSequence instanceofFragment) {
            mTitle = title;
            mIndicatorColor = indicatorColor;
            mDividerColor = dividerColor;
            mInstanceofFragment = instanceofFragment;
        }

        /**
         * @return A new {@link Fragment} to be displayed by a {@link android.support.v4.view.ViewPager}
         */
        Fragment createFragment() {
            return GrocerySearchFragment.newInstance();
        }

        /**
         * @return the title which represents this tab. In this sample this is used directly by
         * {@link android.support.v4.view.PagerAdapter#getPageTitle(int)}
         */
        CharSequence getTitle() {

            return mTitle;
        }
        /**
         * @return the getInstanceofFragment which represents type of fragment to return. In this sample this is used directly by
         * {@link android.support.v4.view.PagerAdapter#getPageTitle(int)}
         */
        CharSequence getInstanceofFragment() {

            return  mInstanceofFragment;
        }




        /**
         * @return the color to be used for indicator on the {@link SlidingTabLayout}
         */
        int getIndicatorColor() {
            return mIndicatorColor;
        }

        /**
         * @return the color to be used for right divider on the {@link SlidingTabLayout}
         */
        int getDividerColor() {

            return mDividerColor;
        }
    }

   /** A custom {@link ViewPager
    } title strip which looks much like Tabs present in Android v4.0 and
    * above, but is designed to give continuous feedback to the user when scrolling.
      *  */
    private SlidingTabLayout mSlidingTabLayout;

    /**
     * A {@link ViewPager} which will be used in conjunction with the {@link SlidingTabLayout} above.
     */
    private ViewPager mViewPager;

    /**
     * List of {@link SamplePagerItem} which represent this sample's tabs.
     */
    private List<SamplePagerItem> mTabs = new ArrayList<SamplePagerItem>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // BEGIN_INCLUDE (populate_tabs)
        /**
         * Populate our tab list with tabs. Each item contains a title, indicator color and divider
         * color, which are used by {@link SlidingTabLayout}.
         */
        mTabs.add(new SamplePagerItem(
                getString(R.string.tab_fridge_pantry), // Title
                Color.BLUE, // Indicator color
                Color.GRAY ,// Divider color
                "GrocerySearchFragment"

        ));

        mTabs.add(new SamplePagerItem(
                getString(R.string.tab_grocery_shopping_list), // Title
                Color.RED, // Indicator color
                Color.GRAY ,// Divider color
                "GrocerySearchFragment"
        ));

        mTabs.add(new SamplePagerItem(
                getString(R.string.tab_search_grocery_shopping_list), // Title
                Color.YELLOW, // Indicator color
                Color.GRAY ,// Divider color
                "GrocerySearchFragment"
        ));
        // END_INCLUDE (populate_tabs)
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated
        // properly.

        return inflater.inflate(R.layout.fragment_grocery_main, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // BEGIN_INCLUDE (setup_viewpager)
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mViewPager.setAdapter(new GroceryPagerAdapter(getChildFragmentManager(),mTabs));
        // END_INCLUDE (setup_viewpager)

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
