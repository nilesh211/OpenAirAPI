
//region Import Namespace
package com.example.sharma.openairapi.utilities;

import android.content.ContentValues;
import android.util.Log;


import com.example.sharma.openairapi.data.OpenAirContract;
import com.example.sharma.openairapi.model.OpenAir;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
//endregion

public class JSONUtils {

    //region Variables
    private static final String CLASS_NAME = JSONUtils.class.getSimpleName();
    //endregion

    //region Public Methods
    public static List<OpenAir> parseOpenAirResponse(String json) {
        ArrayList<OpenAir> resultData = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            if (jsonObject == null || jsonObject.length() == 0) return resultData;
            JSONArray results = jsonObject.getJSONArray("results");
            if (results == null || results.length() <= 0) return resultData;

            for (int i = 0; i < results.length(); i++) {
                JSONObject result = (JSONObject) results.get(i);
                String cityName = result.getString("city");
                String countryCode = result.getString("country");
                int locationCount = result.getInt("locations");
                int measurementCount = result.getInt("count");
                if (measurementCount >= 10000) {
                    OpenAir data = new OpenAir(cityName, countryCode, locationCount, measurementCount);
                    resultData.add(data);
                }
            }
        } catch (JSONException e) {
            Log.e(CLASS_NAME, "parseOpenAirResponse(String json) - " + e.getMessage());
            e.printStackTrace();
        }
        return resultData;
    }


    public static ContentValues[] parseOpenAirResponse1(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            if (jsonObject == null || jsonObject.length() == 0) return null;

            // Check if there is error
            if (jsonObject.has("statusCode")) {

                int errorCode = jsonObject.getInt("statusCode");
                switch (errorCode) {
                    case HttpURLConnection.HTTP_OK:
                        break;
                    case HttpURLConnection.HTTP_NOT_FOUND:
                    /* Location invalid */
                        return null;
                    default:
                    /* Server probably down */
                        return null;
                }
            }
            JSONArray results = jsonObject.getJSONArray("results");
            if (results == null || results.length() <= 0) return null;
            ArrayList<OpenAir> resultData = new ArrayList<>();
            for (int i = 0; i < results.length(); i++) {
                JSONObject result = (JSONObject) results.get(i);
                String cityName = result.getString(OpenAirContract.OpenAirEntry.CITY_NAME);
                String countryCode = result.getString(OpenAirContract.OpenAirEntry.COUNTRY_CODE);
                int locationCount = result.getInt(OpenAirContract.OpenAirEntry.LOCATION_COUNT);
                int measurementCount = result.getInt(OpenAirContract.OpenAirEntry.MEASUREMENT_COUNT);
                if (measurementCount >= 10000) {
                    OpenAir data = new OpenAir(cityName, countryCode, locationCount, measurementCount);
                    resultData.add(data);
                }
            }
            if (resultData.size() == 0) return null;
            ContentValues[] openAirContentValues = new ContentValues[resultData.size()];

            for (int i = 0; i < resultData.size(); i++) {
                ContentValues contentValue = new ContentValues();
                OpenAir data = resultData.get(i);
                contentValue.put(OpenAirContract.OpenAirEntry.CITY_NAME, data.getCityName());
                contentValue.put(OpenAirContract.OpenAirEntry.COUNTRY_CODE, data.getCountryCode());
                contentValue.put(OpenAirContract.OpenAirEntry.LOCATION_COUNT, data.getLocationCount());
                contentValue.put(OpenAirContract.OpenAirEntry.MEASUREMENT_COUNT, data.getMeasurementCount());
                openAirContentValues[i] = contentValue;
            }
            return openAirContentValues;

        } catch (JSONException e) {
            Log.e(CLASS_NAME, "parseOpenAirResponse1(String json) - " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    //endregion
}
