package com.example.awebber.grocery;

import android.provider.BaseColumns;

/**
 * Title: GroceryContract.java
 * Created by Alton Webber on 4/15/15.
 * Description: Tables have plural names.  If the name requires two words
 * separate words with an underscore.
 *
 * Purpose: Define the data storage of Sqlite database.  Agreement between
 * data model and view describing how information is stored.  I contains
 *
 *
 *
 * Usage:
 *
 *
 */
public  class GroceryContract  {

    //    Inner class that defines the table contents of the groceries table
    public static final class GroceryEntry implements BaseColumns {

        public static final String TABLE_NAME = "groceries";

        // Column with the foreign key into the basic_description table.
        public static final String COLUMN_BASIC_DESC_LOC_KEY = "basic_description_id";

        // Column with the foreign key into the brands table.
        public static final String COLUMN_BRAND_LOC_KEY = "brand_id";

        // The name of the product Coconut oil,Pure Sport,Procell
        public static final String  COLUMN_NAME = "product_name";
    }

    //    Inner class that defines the table contents of the brands table
    public static final class BrandEntry implements BaseColumns {

        public static final String TABLE_NAME = "brands";

        // The of the name items Brand Item e.g DURACELL,LUBRIDERM,GATORADE
        public static final String COLUMN_BRAND_NAME = "brand_name";
    }

    //   Inner class that defines the table contents of the basic_descriptions table
    public static final class BasicDescriptionEntry implements BaseColumns {

        public static final String TABLE_NAME = "basic_descriptions";

        //A basic description of what the items is e.g cookie ,cereal ,Fruit
        //name should be in singular form
        public static final String COLUMN_PRODUCT_DESC = "product_description";
    }

}
