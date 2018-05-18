
//region Using Namespaces
package com.example.sharma.openairapi.data;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
//endregion

public class OpenAirDBIHelper extends SQLiteOpenHelper {

    //region Constants
    private static final String CLASS_NAME = OpenAirDBIHelper.class.getSimpleName();
    private static final String DB_NAME = "openair.db";
    public static final int VERSION_NUMBER = 4;
    //endregion

    //region Constructor
    public OpenAirDBIHelper(Context context) {
        super(context, DB_NAME, null, VERSION_NUMBER);
    }
    //endregion

    //region Override
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try {
            final String SQL_OPEN_AIR_MEASUREMENT_TABLE = "CREATE TABLE " +
                    OpenAirContract.OpenAirEntry.TABLE_NAME + "( " +
                    OpenAirContract.OpenAirEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    OpenAirContract.OpenAirEntry.CITY_NAME + " TEXT NOT NULL," +
                    OpenAirContract.OpenAirEntry.COUNTRY_CODE + " TEXT NOT NULL," +
                    OpenAirContract.OpenAirEntry.LOCATION_COUNT + " INTEGER NOT NULL," +
                    OpenAirContract.OpenAirEntry.MEASUREMENT_COUNT + " INTEGER NOT NULL" +
                    " );";
            sqLiteDatabase.execSQL(SQL_OPEN_AIR_MEASUREMENT_TABLE);
        } catch (SQLException e) {
            Log.e(CLASS_NAME, "onCreate(SQLiteDatabase sqLiteDatabase) - " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        try {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + OpenAirContract.OpenAirEntry.TABLE_NAME);
            onCreate(sqLiteDatabase);
        } catch (SQLException e) {
            Log.e(CLASS_NAME, "onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) - " + e.getMessage());
            e.printStackTrace();
        }
    }
    //endregion
}
