package com.example.awebber.grocery.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.awebber.grocery.R;
import com.example.awebber.grocery.adapter.GroceryPagerAdapter;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by awebber on 5/1/15.
 */
public class MainFragment extends Fragment {
    static final CharSequence CategoryFragment = ("CategoryFragment");
    static final CharSequence InventoryFragment = ("InventoryFragment");
    static final CharSequence SearchFragment = ("SearchFragment");
    private ViewPager mViewPager;

    /**
     * List of {@link GroceryPagerAdapter.SamplePagerItem} which represent this sample's tabs.
     */
    private List<GroceryPagerAdapter.SamplePagerItem> mTabs = new ArrayList<GroceryPagerAdapter.SamplePagerItem>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // BEGIN_INCLUDE (populate_tabs)
        /**
         * Populate our tab list with tabs. Each item contains a title, indicator color and divider
         * color, which are used by {@link SlidingTabLayout}.
         */
        mTabs.add(new GroceryPagerAdapter.SamplePagerItem(
                getString(R.string.tab_fridge_pantry), // Title
                Color.WHITE, // Indicator color
                Color.GRAY ,// Divider color
                InventoryFragment,
                false

        ));

        mTabs.add(new GroceryPagerAdapter.SamplePagerItem(
                getString(R.string.tab_grocery_shopping_list), // Title
                Color.WHITE, // Indicator color
                Color.GRAY,// Divider color
                "ShoppingList",
                false
        ));


        mTabs.add(new GroceryPagerAdapter.SamplePagerItem(
                getString(R.string.tab_Categories), // Title
                Color.WHITE, // Indicator color
                Color.GRAY ,// Divider color
                CategoryFragment,
                false
        ));

        mTabs.add(new GroceryPagerAdapter.SamplePagerItem(
                getString(R.string.tab_search_grocery_shopping_list), // Title
                Color.WHITE, // Indicator color
                Color.GRAY ,// Divider color
                SearchFragment,
                false
        ));
        // END_INCLUDE (populate_tabs)
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated
        // properly.
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);


        return rootView;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mViewPager.setAdapter(new GroceryPagerAdapter(getChildFragmentManager(),mTabs));

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.sliding_tabs);
        tabLayout.setTabTextColors(getResources().getColorStateList(R.color.tab_text_color));
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }




}
