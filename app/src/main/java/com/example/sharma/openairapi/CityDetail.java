package com.example.sharma.openairapi;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sharma.openairapi.model.OpenAir;

public class CityDetail extends AppCompatActivity {

    //region Variables
    private static final String CLASS_NAME = CityDetail.class.getSimpleName();
    //endregion

    //region Override
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_city_detail);
            setTitle(getString(R.string.city_detail));
            Intent intent = getIntent();
            if (intent.hasExtra(Intent.EXTRA_TEXT)) {
                OpenAir openAir = intent.getParcelableExtra(Intent.EXTRA_TEXT);
                TextView tvCity = findViewById(R.id.cityNameTv);
                TextView tvCountryCode = findViewById(R.id.countryCodeDataTv);
                TextView tvLocationCount = findViewById(R.id.locationTv);
                TextView tvMeasurementCount = findViewById(R.id.cityMeasurementCountTV);
                if (openAir.getCityName() == null || openAir.getCityName().trim().equals(""))
                    tvCity.setVisibility(View.GONE);
                else
                    tvCity.setText(getSpannableData(tvCity.getText().toString(), openAir.getCityName()));
                if (openAir.getCountryCode() == null || openAir.getCityName().trim().equals(""))
                    tvCountryCode.setVisibility(View.GONE);
                else
                    tvCountryCode.setText(getSpannableData(tvCountryCode.getText().toString(), openAir.getCountryCode()));
                tvLocationCount.setText(getSpannableData(tvLocationCount.getText().toString(), String.valueOf(openAir.getLocationCount())));
                tvMeasurementCount.setText(getSpannableData(tvMeasurementCount.getText().toString(), String.valueOf(openAir.getMeasurementCount())));
            }
        } catch (Exception e) {
            Log.e(CLASS_NAME, "onCreate(Bundle savedInstanceState) - " + e.getMessage());
        }
    }
    //endregion

    //region Private Methods
    private SpannableString getSpannableData(String fieldName, String fieldValue) {
        SpannableString spannableString = new SpannableString("");
        try {
            String spannableText = String.format("%s %s", fieldName, fieldValue);
            spannableString = new SpannableString(spannableText);
            spannableString.setSpan(new RelativeSizeSpan(.9f), fieldName.length(), spannableText.length(), 0); // set size
            spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorGrayDark)), fieldName.length(), spannableText.length(), 0);// set color
            return spannableString;
        } catch (Exception e) {
            Log.e(CLASS_NAME, "getSpannableData(String fieldName, String fieldValue) - " + e.getMessage());
        }
        return spannableString;
    }
    //endregion
}
