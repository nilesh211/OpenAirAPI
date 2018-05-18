
//region Import Namespaces
package com.example.sharma.openairapi;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sharma.openairapi.data.OpenAirContract;
import com.example.sharma.openairapi.model.OpenAir;
//endregion


public class OpenAirAdapter extends RecyclerView.Adapter<OpenAirAdapter.OpenAirViewHolder> {

    //region Variables
    private final OpenAirAdapterOnClickHandler mClickHandler;
    private Cursor mCursor;
    private static final String CLASS_NAME = OpenAirAdapter.class.getSimpleName();
    //endregion

    //region Constructor & Interface
    public OpenAirAdapter(OpenAirAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public interface OpenAirAdapterOnClickHandler {
        void onClick(OpenAir openAir);
    }
    //endregion

    //region Override
    @Override
    public OpenAirViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        try {
            Context context = parent.getContext();
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View view = layoutInflater.inflate(R.layout.open_air_list, parent, false);
            return new OpenAirViewHolder(view);
        } catch (Exception e) {
            Log.e(CLASS_NAME, "onCreateViewHolder(ViewGroup parent, int viewType) - " + e.getMessage());
        }
        return null;
    }

    @Override
    public void onBindViewHolder(OpenAirViewHolder holder, int position) {
        try {
            mCursor.moveToPosition(position);

            String cityName = mCursor.getString(mCursor.getColumnIndex(OpenAirContract.OpenAirEntry.CITY_NAME));
            String countryCode = mCursor.getString(mCursor.getColumnIndex(OpenAirContract.OpenAirEntry.COUNTRY_CODE));
            int count = mCursor.getInt(mCursor.getColumnIndex(OpenAirContract.OpenAirEntry.MEASUREMENT_COUNT));

            holder.tvCity.setText(cityName);
            holder.tvCountryCode.setText(countryCode);
            holder.tvMeasurementCount.setText(count + "");


        } catch (Exception e) {
            Log.e(CLASS_NAME, "onBindViewHolder(OpenAirViewHolder holder, int position) - " + e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        try {
            if (mCursor == null) return 0;
            else return mCursor.getCount();
        } catch (Exception e) {
            Log.e(CLASS_NAME, "getItemCount() - " + e.getMessage());
        }
        return 0;
    }
    //endregion

    //region Public Methods
    public void swapCursor(Cursor newCursor) {
        try {
            mCursor = newCursor;
            notifyDataSetChanged();
        } catch (Exception e) {
            Log.e(CLASS_NAME, "swapCursor(Cursor newCursor) - " + e.getMessage());
        }
    }
    //endregion

    //region View Holder
    public class OpenAirViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView tvCity;
        public final TextView tvCountryCode;
        public final TextView tvMeasurementCount;

        public OpenAirViewHolder(View view) {
            super(view);
            tvCity = view.findViewById(R.id.cityTV);
            tvCountryCode = view.findViewById(R.id.countryCodeTV);
            tvMeasurementCount = view.findViewById(R.id.measurementCountTV);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mCursor.moveToPosition(getAdapterPosition());
            OpenAir openAir = new OpenAir(mCursor.getString(mCursor.getColumnIndex(OpenAirContract.OpenAirEntry.CITY_NAME)),
                    mCursor.getString(mCursor.getColumnIndex(OpenAirContract.OpenAirEntry.COUNTRY_CODE)),
                    mCursor.getInt(mCursor.getColumnIndex(OpenAirContract.OpenAirEntry.LOCATION_COUNT)),
                    mCursor.getInt(mCursor.getColumnIndex(OpenAirContract.OpenAirEntry.MEASUREMENT_COUNT)));
            mClickHandler.onClick(openAir);
        }
    }
    //endregion
}
