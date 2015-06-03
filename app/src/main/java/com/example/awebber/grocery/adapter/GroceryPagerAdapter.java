package com.example.awebber.grocery.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.example.awebber.grocery.fragments.MainFragment.SamplePagerItem;
import java.util.List;


/**
 * Created by awebber on 5/1/15.
 */
public class GroceryPagerAdapter extends FragmentPagerAdapter {
    private List<SamplePagerItem> mTabs;
    public GroceryPagerAdapter(FragmentManager fm,List<SamplePagerItem> Tabs) {

        super(fm);
        mTabs = Tabs;
    }

   @Override
   public Fragment getItem(int i) {

       return mTabs.get(i).createFragment();
   }

    @Override
    public int getCount() {

        return mTabs.size();
    }

    // BEGIN_INCLUDE (pageradapter_getpagetitle)
    /**
     * Return the title of the item at {@code position}. This is important as what this method
     * returns is what is displayed in the {@link  }.
     * <p>
     * Here we return the value returned from {@link SamplePagerItem#getTitle()}.
     */
    @Override
    public CharSequence getPageTitle(int position) {

        return mTabs.get(position).getTitle();
    }
    // END_INCLUDE (pageradapter_getpagetitle)

}



