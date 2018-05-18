
//region Using Namespaces
package com.example.sharma.openairapi.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
//endregion

public class Utils {

    // region Variable Declaration
    private static final String CLASS_NAME = Utils.class.getSimpleName();
    //endregion

    // region Public Methods
    //Checks weather the device is connected to internet
    // returns true if connected false otherwise
    public static boolean isConnectedToInternet(Context context) {
        try {
            for (NetworkInfo ni : ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getAllNetworkInfo()) {
                switch (ni.getTypeName().trim().toUpperCase()) {
                    case "WIFI":
                    case "MOBILE":
                        if (ni.isConnected()) {
                            return true;
                        }
                        break;
                }
            }
        } catch (Exception e) {
            Log.e(CLASS_NAME + "isConnectedToInternet", e.getMessage());
        }
        return false;
    }
    //endregion
}
