
//region Using Namespaces
package com.example.sharma.openairapi.model;

import android.os.Parcel;
import android.os.Parcelable;
//endregion

// Created Parcelable object using http://www.parcelabler.com/

public class OpenAir implements Parcelable {

    private String cityName;
    private String countryCode;
    private int locationCount;
    private int measurementCount;

    public String getCityName() {
        return cityName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public int getLocationCount() {
        return locationCount;
    }

    public int getMeasurementCount() {
        return measurementCount;
    }

    public OpenAir(String strCityName, String strCountryCode, int intLocationCount, int intMeasurementCount) {
        this.cityName = strCityName;
        this.countryCode = strCountryCode;
        this.locationCount = intLocationCount;
        this.measurementCount = intMeasurementCount;
    }

    protected OpenAir(Parcel in) {
        cityName = in.readString();
        countryCode = in.readString();
        locationCount = in.readInt();
        measurementCount = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(cityName);
        dest.writeString(countryCode);
        dest.writeInt(locationCount);
        dest.writeInt(measurementCount);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<OpenAir> CREATOR = new Parcelable.Creator<OpenAir>() {
        @Override
        public OpenAir createFromParcel(Parcel in) {
            return new OpenAir(in);
        }

        @Override
        public OpenAir[] newArray(int size) {
            return new OpenAir[size];
        }
    };
}