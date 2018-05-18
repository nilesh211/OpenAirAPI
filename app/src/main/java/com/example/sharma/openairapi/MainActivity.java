
//region Import Namespaces
package com.example.sharma.openairapi;

import android.content.Intent;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.sharma.openairapi.data.OpenAirContract;
import com.example.sharma.openairapi.model.OpenAir;
import com.example.sharma.openairapi.sync.OpenAirSyncUtils;
//endregion

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>,
        OpenAirAdapter.OpenAirAdapterOnClickHandler {

    //region Variables
    private static final String CLASS_NAME = MainActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private TextView tvErrorMessage;
    private ProgressBar mProgressBar;
    private static final int ID_OPEN_AIR_LOADER = 11;
    private OpenAirAdapter mOpenAirAdapter;
    private String sortParameter = OpenAirContract.OpenAirEntry.MEASUREMENT_COUNT + " DESC";
    private String menuSort = "";
    private int mPosition = RecyclerView.NO_POSITION;
    public static final String[] MAIN_OPEN_AIR_PROJECTION = {
            OpenAirContract.OpenAirEntry.CITY_NAME,
            OpenAirContract.OpenAirEntry.COUNTRY_CODE,
            OpenAirContract.OpenAirEntry.LOCATION_COUNT,
            OpenAirContract.OpenAirEntry.MEASUREMENT_COUNT,
    };
    //endregion

    //region Override
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_main);
            setTitle(getString(R.string.city_list));
            mRecyclerView = findViewById(R.id.openAirRV);
            tvErrorMessage = findViewById(R.id.errorMessageTV);
            mProgressBar = findViewById(R.id.loadingPB);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setHasFixedSize(true);
            mOpenAirAdapter = new OpenAirAdapter( this);
            mRecyclerView.setAdapter(mOpenAirAdapter);
            menuSort = getResources().getString(R.string.count_sort_desc);
            getSupportLoaderManager().initLoader(ID_OPEN_AIR_LOADER, null, this);
            OpenAirSyncUtils.initialize(this);
        } catch (Exception e) {
            Log.e(CLASS_NAME, "onCreate(Bundle savedInstanceState) - " + e.getMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.measurementsort, menu);
        } catch (Exception e) {
            Log.e(CLASS_NAME, "onCreateOptionsMenu(Menu menu) - " + e.getMessage());
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            if (item.getItemId() == R.id.action_asc) {
                if (!menuSort.equals(getResources().getString(R.string.count_sort_asc))) {
                    menuSort = getResources().getString(R.string.count_sort_asc);
                    sortParameter = OpenAirContract.OpenAirEntry.MEASUREMENT_COUNT + " ASC";
                    mProgressBar.setVisibility(View.VISIBLE);
                    //restart the loader
                    getSupportLoaderManager().restartLoader(ID_OPEN_AIR_LOADER, null, this);
                }
            } else if (item.getItemId() == R.id.action_desc) {
                if (!menuSort.equals(getResources().getString(R.string.count_sort_desc))) {
                    menuSort = getResources().getString(R.string.count_sort_desc);
                    sortParameter = OpenAirContract.OpenAirEntry.MEASUREMENT_COUNT + " DESC";
                    mProgressBar.setVisibility(View.VISIBLE);
                    getSupportLoaderManager().restartLoader(ID_OPEN_AIR_LOADER, null, this);
                }
            }
        } catch (Exception e) {
            Log.e(CLASS_NAME, "onOptionsItemSelected(MenuItem item) - " + e.getMessage());
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {
        try {
            switch (loaderId) {
                case ID_OPEN_AIR_LOADER:
                /* URI for all rows of weather data in our weather table */
                    Uri openAirURI = OpenAirContract.OpenAirEntry.CONTENT_URI;
                /* Sort order: Ascending by date */
                    //  String sortOrder = OpenAirContract.OpenAirEntry.MEASUREMENT_COUNT + " DESC";
                    return new CursorLoader(this,
                            openAirURI,
                            MAIN_OPEN_AIR_PROJECTION,
                            null,
                            null,
                            sortParameter);
                default:
                    throw new RuntimeException("Loader Not Implemented: " + loaderId);
            }
        } catch (Exception e) {
            Log.e(CLASS_NAME, "onCreateLoader(int loaderId, Bundle bundle) - loaderId - " + loaderId + " " + e.getMessage());
            return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        try {
            mOpenAirAdapter.swapCursor(data);
            if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
            mRecyclerView.smoothScrollToPosition(mPosition);
            if (data.getCount() != 0) showOpenAirDataView();
        } catch (Exception e) {
            Log.e(CLASS_NAME, "onLoadFinished(Loader<Cursor> loader, Cursor data) - " + e.getMessage());
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        try {
            mOpenAirAdapter.swapCursor(null);
        } catch (Exception e) {
            Log.e(CLASS_NAME, "onLoaderReset(Loader<Cursor> loader) - " + e.getMessage());
        }
    }

    @Override
    public void onClick(OpenAir openAir) {
        try {
            Intent intent = new Intent(this, CityDetail.class);
            intent.putExtra(Intent.EXTRA_TEXT, openAir);
            startActivity(intent);
        } catch (Exception e) {
            Log.e(CLASS_NAME, "onClick(OpenAir openAir) - " + e.getMessage());
        }
    }
    //endregion

    //region Private Methods
    private void showErrorMessage() {
        try {
            mRecyclerView.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.GONE);
            tvErrorMessage.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            Log.e(CLASS_NAME, "showErrorMessage() - " + e.getMessage());
        }
    }

    private void showOpenAirDataView() {
        try {
            mProgressBar.setVisibility(View.INVISIBLE);
            tvErrorMessage.setVisibility(View.INVISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            Log.e(CLASS_NAME, "showOpenAirDataView() - " + e.getMessage());
        }
    }
    //endregion
}
