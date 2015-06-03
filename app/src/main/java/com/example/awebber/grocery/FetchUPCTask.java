package com.example.awebber.grocery;

import android.app.Dialog;
import android.support.v4.app.FragmentManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.example.awebber.grocery.data.GroceryContract;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;


/**
 * Created by awebber on 5/15/15.
 */
public class FetchUPCTask extends AsyncTask<Void, Void, HashMap<String,String>> {
    private static Context mContext;
    private static FragmentManager mFragmentManager;
    public static final String LOG_TAG =  FetchUPCTask.class.getSimpleName();
    public  FetchUPCTask(){

    }
    public FetchUPCTask(CharSequence upcCode,Context context,FragmentManager fragmentManager){
         mUpcCode = upcCode;
         mContext = context;
         mFragmentManager = fragmentManager ;
    }

    //  http://upcdatabase.org/api
    private static final String  APIKEY = "4855f3f36a7f931b3a681f0a4f22e7bd";
    private static CharSequence mUpcCode;
    @Override
    protected HashMap<String,String> doInBackground(Void... params) {
        HashMap<String,String > results = null;
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String XmlStr = null;

        try {
            // Construct the URL for the upcdatabase query

            URL url = new URL("http://api.upcdatabase.org/xml/"+APIKEY +"/"+mUpcCode);

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's xml, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.

                buffer.append(line.trim() );
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            Log.i(LOG_TAG, buffer.toString());

            {
                try {

                    XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
                    parser.setInput(new StringReader(buffer.toString()));
                    results = Utility.xmlParse(parser);

                } catch (IOException t) {
                    Log.i(LOG_TAG, "ERROR:" + t.getMessage());
                } catch (XmlPullParserException xppe) {
                    Log.e(LOG_TAG, "Failed to parse items", xppe);
                }
            }



        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the upc data, there's no point in attemping
            // to parse it.
            return null;
        } finally {
            if (urlConnection != null) {
                Log.i(LOG_TAG, "Disconnected from upc");
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        return results;
    }

    @Override
    protected void onPostExecute (HashMap<String,String> result){
        if(result.get("valid").equals("true")){
            ContentValues input = new ContentValues();
            if(result.get("itemname")!=null){
               input.put(GroceryContract.GroceryEntry.COLUMN_PRODUCT_NAME, result.get("itemname"));
            }
            else{
               input.put(GroceryContract.GroceryEntry.COLUMN_PRODUCT_NAME, result.get("description"));
            }

            //One is UnknownCategory and UnkownBrandLocKey =1;
            input.put(GroceryContract.GroceryEntry.COLUMN_BRAND_LOC_KEY,1);
            input.put(GroceryContract.GroceryEntry.COLUMN_CATEGORY_LOC_KEY,1);
            Uri returnUri = mContext.getContentResolver().insert(
                   GroceryContract.GroceryEntry.CONTENT_URI,
                   input);


            int groceryID = Integer.parseInt(GroceryContract.GroceryEntry.getIdFromUri(returnUri));
            input  = new ContentValues();
            input.put(GroceryContract.InventoryEntry.COLUMN_GROCERY_LOC_KEY,groceryID);
            input.put(GroceryContract.InventoryEntry.COLUMN_QUANTITY,1);

            mContext.getContentResolver().insert(
             GroceryContract.InventoryEntry.buildInventoryUri(groceryID),
             input);


        }
            else{
            Bundle args =new Bundle();
            args.putString("message",result.get("reason"));
            reasonDialogFragment Reason = new reasonDialogFragment();
            Reason.setArguments(args);
            Reason.show(mFragmentManager, LOG_TAG);
        }
    }

    public static class reasonDialogFragment extends DialogFragment {
        String mMessage;
        public reasonDialogFragment(){

        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
          mMessage   = getArguments().getString("message");

        }
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState ) {

            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(mMessage)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    })                    ;
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }






}
