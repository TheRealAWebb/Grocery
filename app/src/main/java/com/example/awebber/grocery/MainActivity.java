package com.example.awebber.grocery;


import android.net.Uri;
import android.support.v7.app.ActionBarActivity;

import android.support.v4.app.Fragment;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.awebber.grocery.data.GroceryContract;
import com.example.awebber.grocery.data.GroceryContract.GroceryEntry;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            //todo DELETE THIS TEST
        Uri j =   GroceryContract.GroceryEntry.buildGroceriesBasicDescWBrand("Hello", "Wordl");

        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(  R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        ArrayAdapter<String> mGroceryAdapter;
        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {


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
}
