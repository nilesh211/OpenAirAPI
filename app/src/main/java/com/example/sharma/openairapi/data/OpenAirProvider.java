
//region Using Namespaces
package com.example.sharma.openairapi.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.sharma.openairapi.OpenAirAdapter;
//endregion


public class OpenAirProvider extends ContentProvider {

    //region Variables
    private OpenAirDBIHelper openAirDBIHelper;
    private static final int OPEN_AIR = 100;
    private static final String CLASS_NAME = OpenAirProvider.class.getSimpleName();
    public static final UriMatcher sUriMatcher = buildUriMatcher();
    //endregion

    //region Public Methods
    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = null;
        try {
            uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
            uriMatcher.addURI(OpenAirContract.AUTHORITY, OpenAirContract.PATH_AIR, OPEN_AIR);
            return uriMatcher;
        } catch (Exception e) {
            Log.e(CLASS_NAME, "buildUriMatcher() - " + e.getMessage());
            return uriMatcher;
        }
    }
    //endregion

    //region Override
    @Override
    public boolean onCreate() {
        try {
            openAirDBIHelper = new OpenAirDBIHelper(getContext());
            return true;
        } catch (SQLiteException e) {
            Log.e(CLASS_NAME, "onCreate() - " + e.getMessage());
        }
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrderBy) {
        Cursor returnCursor = null;
        try {
            final SQLiteDatabase db = openAirDBIHelper.getReadableDatabase();
            int matchID = sUriMatcher.match(uri);
            switch (matchID) {
                case OPEN_AIR:
                    returnCursor = db.query(OpenAirContract.OpenAirEntry.TABLE_NAME, null, selection, selectionArgs, null, null, sortOrderBy);
                    break;
                default:
                    throw new UnsupportedOperationException("Unknown Uri - " + uri);
            }
            returnCursor.setNotificationUri(getContext().getContentResolver(), uri);
            return returnCursor;
        } catch (SQLiteException e) {
            Log.e(CLASS_NAME, "onCreate(SQLiteDatabase sqLiteDatabase) - " + e.getMessage());
            return returnCursor;

        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {

        int numRowsDeleted = 0;
        try {

            //Passing null wont give number of rows deleted, setting selection as 1 will return number of rows.
            if (null == selection) selection = "1";

            switch (sUriMatcher.match(uri)) {

                case OPEN_AIR:
                    numRowsDeleted = openAirDBIHelper.getWritableDatabase().delete(
                            OpenAirContract.OpenAirEntry.TABLE_NAME,
                            selection,
                            selectionArgs);
                    break;
                default:
                    throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
            if (numRowsDeleted != 0) {
                getContext().getContentResolver().notifyChange(uri, null);
            }
        } catch (SQLiteException e) {
            Log.e(CLASS_NAME, "delete(@NonNull Uri uri, String selection, String[] selectionArgs) - " + e.getMessage());
        }
        return numRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = openAirDBIHelper.getWritableDatabase();
        int matchID = sUriMatcher.match(uri);
        long rowsInserted = 0;
        switch (matchID) {
            case OPEN_AIR:
                try {
                    db.beginTransaction();
                    for (ContentValues value : values) {
                        rowsInserted = db.insert(OpenAirContract.OpenAirEntry.TABLE_NAME, null, value);
                        if (rowsInserted != -1)
                            rowsInserted++;
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                if (rowsInserted > 0)
                    getContext().getContentResolver().notifyChange(uri, null);
                return (int) rowsInserted;
            default:
                return super.bulkInsert(uri, values);
        }
    }
    //endregion

}
