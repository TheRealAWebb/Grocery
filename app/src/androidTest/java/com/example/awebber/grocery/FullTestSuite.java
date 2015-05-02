package com.example.awebber.grocery;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.test.suitebuilder.TestSuiteBuilder;
import android.util.Log;


import junit.framework.Test;
import junit.framework.TestSuite;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class FullTestSuite extends TestSuite {
    Context mCtx ;
    public static final String TAG ="Utility";
    public static Test suite() {
       return new TestSuiteBuilder(FullTestSuite.class)
                .includeAllPackagesUnderHere().build();
    }
     public FullTestSuite() {
        super();
    }

    public void LoadTextFile( List<String>  theList){


        theList = new ArrayList<String>();

        AssetManager assetManager = mCtx.getAssets();
        InputStream is =  mCtx.getResources().openRawResource(R.raw.brandslist);


        BufferedReader reader = null;

        try {
            reader =  new BufferedReader(new InputStreamReader(is));
            String text = null;

            while ((text = reader.readLine()) != null) {
                Log.i(TAG,text);
                theList.add(text);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
            }
        }
        Log.i(TAG, "We made it ");
        for(String item :   theList ) {
            Log.i(TAG, "item ");
        }
    }



}