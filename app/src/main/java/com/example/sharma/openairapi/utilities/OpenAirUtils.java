
//region Import Namespaces
package com.example.sharma.openairapi.utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
//endregion


public class OpenAirUtils {

    // region Variables
    private static final String CLASS_NAME = OpenAirUtils.class.getSimpleName();
    private static final String AIR_DBI_URL = "https://api.openaq.org/v1/cities";
    //endregion

    //region Public Methods
    /*
    This Method Builds URL to fetch City List and Measurements
    @param - Null
    @return - URL
    @throws MalformedURLException
     */
    public static URL buildOpenAirUrl() {
        Uri builtUri = Uri.parse(AIR_DBI_URL).buildUpon().build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            Log.e(CLASS_NAME, "buildOpenAirUrl() - " + e.getMessage());
            e.printStackTrace();
        }
        return url;
    }

    /*
      This method returns the entire result from the HTTP response.
      @param url The URL to fetch the HTTP response from.
      @return The contents of the HTTP response.
      @throws IOException Related to network and stream reading
     */

    public static String getResponseFromHttpUrl(URL url) {
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            if (hasInput) return scanner.next();
            else return null;
        } catch (IOException e) {
            Log.e(CLASS_NAME, "getResponseFromHttpUrl(URL url) - " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }
        return null;
    }
    //endregion


}
