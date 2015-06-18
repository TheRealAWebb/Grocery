package com.example.awebber.grocery.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import java.util.List;


/**
 * Created by awebber on 5/1/15.
 */
public class GroceryPagerAdapter extends FragmentPagerAdapter {
    static final CharSequence CategoryFragment = ("CategoryFragment");
    static final CharSequence InventoryFragment = ("InventoryFragment");
    static final CharSequence SearchFragment = ("SearchFragment");

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

    public static class SamplePagerItem {
        private final CharSequence mTitle;
        private final int mIndicatorColor;
        private final int mDividerColor;
        private  final CharSequence mInstanceofFragment;
        private boolean misCategory = false;
        public   SamplePagerItem(CharSequence title, int indicatorColor, int dividerColor,
                        CharSequence instanceofFragment,boolean isCategory) {
            mTitle = title;
            mIndicatorColor = indicatorColor;
            mDividerColor = dividerColor;
            mInstanceofFragment = instanceofFragment;
            misCategory = isCategory;
        }


        /**
         * @return A new {@link Fragment} to be displayed by a {@link android.support.v4.view.ViewPager}
         */
        public  Fragment createFragment() {


            if (mInstanceofFragment.equals(CategoryFragment)) {
                return com.example.awebber.grocery.fragments.CategoryFragment.newInstance();
            }
            else if(mInstanceofFragment.equals(InventoryFragment)){
                return com.example.awebber.grocery.fragments.InventoryFragment.newInstance();
            }
            else
                return    com.example.awebber.grocery.fragments.SearchFragment.newInstance(getisCategory(), getTitle());
        }

        /**
         * @return the title which represents this tab. In this sample this is used directly by
         * {@link android.support.v4.view.PagerAdapter#getPageTitle(int)}
         */
        public   CharSequence getTitle() {

            return mTitle;
        }
        /**
         * @return the getInstanceofFragment which represents type of fragment to return. In this sample this is used directly by
         * {@link android.support.v4.view.PagerAdapter#getPageTitle(int)}
         */
        public   CharSequence getInstanceofFragment() {

            return  mInstanceofFragment;
        }


        public   int getIndicatorColor() {
            return mIndicatorColor;
        }


        public   int getDividerColor() {

            return mDividerColor;
        }
        /**
         * @return the boolean in SearchFragment used to query List{@link  com.example.awebber.grocery.fragments.SearchFragment}
         */
      public   boolean getisCategory(){
            return  misCategory;
        }

    }

}



