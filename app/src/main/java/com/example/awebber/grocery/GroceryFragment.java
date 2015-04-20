package com.example.awebber.grocery;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class GroceryFragment extends Fragment {

    ArrayAdapter<String> mGroceryAdapter;
    public GroceryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String[] groceriesArray ={"Milk","Juice","Water","Cookies","soda","TolietPaper","Cold Cuts","Bread" };
        List<String> groceriesList = new ArrayList<String>(Arrays.asList(groceriesArray));
        mGroceryAdapter =
                new ArrayAdapter<String>(getActivity(),
                        R.layout.list_item_grocery,
                        R.id.list_item_grocery_textview,
                        groceriesList);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ListView groceryListView = (ListView) rootView.findViewById(R.id.list_view_grocery);
        groceryListView.setAdapter(mGroceryAdapter);
        return rootView;
    }



}
