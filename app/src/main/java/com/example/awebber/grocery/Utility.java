/**
 * Created by awebber on 4/27/15.
 * Description:
 *
 * Purpose:
 *
 *
 *
 * Usage:
 *
 *
 * */
package com.example.awebber.grocery;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import android.widget.ImageButton;

import com.example.awebber.grocery.data.GroceryContract;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class Utility {

    public static final String TAG ="Utility";
   public Utility(){

    }

    public static String  groceryDetailTableIdentifer(String theTable,String theValue){
    String returnString ="None of the following";
    switch (theTable){
        case GroceryContract.GroceryEntry.TABLE_NAME: {
            returnString = theValue + " is a item "  ;
            break;
        }
        case GroceryContract.CategoryEntry.TABLE_NAME: {
            returnString =  theValue + " is a Category Some " +theValue +" Are" ;
            break;
        }
        case GroceryContract.BrandEntry.TABLE_NAME: {
            returnString = theValue + " is a Brand other products by " +theValue +" Are" ;
            break;
        }
    }
    return returnString;
}

    public  List<String>   LoadTextFile(Context context,int theTextFile){
        List<String> theList = new ArrayList<String>();
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = context.getResources().openRawResource(theTextFile);

        BufferedReader reader = null;
        try {
            reader =  new BufferedReader(new InputStreamReader(inputStream));
            String text = null;
            while ((text = reader.readLine()) != null) {
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
    return theList;
    }

      //Desgin FOR "http://www.upcdatabase.org/

    public static HashMap<String,String> xmlParse (XmlPullParser xpp) throws XmlPullParserException, IOException
    {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        HashMap<String,String> xmlParsed = new HashMap<String,String>();
    String tag ="";
    String text ;
        int eventType = xpp.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if(eventType == XmlPullParser.START_DOCUMENT) {
                System.out.println("Start document");
            } else if(eventType == XmlPullParser.START_TAG) {
               tag = xpp.getName();
                System.out.println("Start tag "+tag);
            } else if(eventType == XmlPullParser.END_TAG) {
                System.out.println("End tag "+xpp.getName());
            } else if(eventType == XmlPullParser.TEXT) {
                text = xpp.getText();
                if(!tag.equals("") || !text.equals("")||!text.equals("\n") ) {
                    Log.i(TAG, "TAG = :" + tag + ": Text = ->" + text+ "<-");
                    xmlParsed.put(tag, text);
                }
            }
            eventType = xpp.next();
        }
        System.out.println("End document");
    return  xmlParsed;
    }
    public void addToInventory( HashMap<String,String> result) {


    }



}
