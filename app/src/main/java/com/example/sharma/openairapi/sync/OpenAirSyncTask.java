
//region Import Namespaces
package com.example.sharma.openairapi.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.example.sharma.openairapi.data.OpenAirContract;
import com.example.sharma.openairapi.utilities.JSONUtils;
import com.example.sharma.openairapi.utilities.OpenAirUtils;

import java.net.URL;
//endregion

public class OpenAirSyncTask {

    synchronized public static void syncOpenAir(Context context) {
        try {
            URL url = OpenAirUtils.buildOpenAirUrl();
            String jsonOpenAirResponse = OpenAirUtils.getResponseFromHttpUrl(url);
            ContentValues[] contentValues = JSONUtils.parseOpenAirResponse1(jsonOpenAirResponse);
            if (contentValues != null && contentValues.length > 0) {
                // build ContextResolver to deleted old data and insert New Data
                ContentResolver contentResolver = context.getContentResolver();
                // delete Old Open Air Data
                contentResolver.delete(OpenAirContract.OpenAirEntry.CONTENT_URI, null, null);
                // Insert New Data
                contentResolver.bulkInsert(OpenAirContract.OpenAirEntry.CONTENT_URI, contentValues);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(OpenAirSyncTask.class.getSimpleName(), "onCreate(SQLiteDatabase sqLiteDatabase) - " + e.getMessage());
        }
    }
}
