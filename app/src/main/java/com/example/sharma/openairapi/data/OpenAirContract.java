
//region Namespaces
package com.example.sharma.openairapi.data;

import android.net.Uri;
import android.provider.BaseColumns;
//endregion


public class OpenAirContract {

    //region Private Constructor
    private OpenAirContract() {
    }
    //endregion

    //region Constants
    public static final String AUTHORITY = "com.example.sharma.openairapi";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_AIR = "air";
    //endregion

    //region Public Class
    public static class OpenAirEntry implements BaseColumns {

        // Open Air Content URI
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_AIR).build();

        public static final String TABLE_NAME = "measurement";
        public static final String CITY_NAME = "city";
        public static final String COUNTRY_CODE = "country";
        public static final String LOCATION_COUNT = "locations";
        public static final String MEASUREMENT_COUNT = "count";
    }
    //endregion
}
