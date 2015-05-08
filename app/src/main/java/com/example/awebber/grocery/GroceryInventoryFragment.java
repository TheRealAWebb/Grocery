/**
 * Title: GroceryFragment.java
 * Created by Alton Webber on 5/2/15.
 * Description:
 *
 * Purpose: Will contain the Fragment that allows users to add
 * and delete items int their fridge and pantry.  Home inventory
 *
 *
 * Usage:
 *
 *
 **/
package com.example.awebber.grocery;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.awebber.grocery.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroceryInventoryFragment extends Fragment {
    public static final String TAG ="GroceryInventoryFragment";

    public GroceryInventoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        textView.setText(R.string.hello_blank_fragment);
        return textView;
    }
    public static GroceryInventoryFragment newInstance() {
        GroceryInventoryFragment fragment = new GroceryInventoryFragment();
        return fragment;
    }

}
