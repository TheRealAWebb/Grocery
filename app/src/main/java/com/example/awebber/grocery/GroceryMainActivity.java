

package com.example.awebber.grocery;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentTransaction;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.awebber.grocery.zxingbarcode.IntentIntegrator;
import com.example.awebber.grocery.zxingbarcode.IntentResult;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class GroceryMainActivity extends AppCompatActivity {
    public static final String TAG = "GroceryMainActivity";
     String UpcCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_main);



        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            GroceryMainFragment fragment = new GroceryMainFragment();
            transaction.replace(R.id.grocery_main_fragment, fragment);
            transaction.commit();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            Log.i(TAG, scanResult.getContents());
              UpcCode =scanResult.getContents();
            new  FetchUPCTask().execute();
        }


    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_grocery_main, menu);
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
    public class FetchUPCTask extends AsyncTask<Void, Void, Void> {

        private final String LOG_TAG = FetchUPCTask.class.getSimpleName();
      //  http://upcdatabase.org/api
      String  APIKEY = "4855f3f36a7f931b3a681f0a4f22e7bd";

        @Override
        protected Void doInBackground(Void... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String XmlStr = null;

            try {
                // Construct the URL for the upcdatabase query

                URL url = new URL("http://api.upcdatabase.org/xml/"+APIKEY +"/"+UpcCode);

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
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                Log.i(TAG, buffer.toString());

              {
                    try {

                        XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
                        parser.setInput(new StringReader(buffer.toString()));
                        Utility.xmlParse(parser);
                    } catch (IOException t) {
                        Log.i(TAG, "ERROR:" + t.getMessage());
                    } catch (XmlPullParserException xppe) {
                        Log.e(TAG, "Failed to parse items", xppe);
                    }
                }


                XmlStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the upc data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
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
            return null;
        }
    }

}
