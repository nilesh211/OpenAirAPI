
///region Import Namespace
package com.example.sharma.openairapi.sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
//endregion


public class OpenAirIntentService extends IntentService {

    //region Public Constructors
    public OpenAirIntentService() {
        super("OpenAirIntentService");
    }
    //endregion

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        OpenAirSyncTask.syncOpenAir(this);
    }
}
