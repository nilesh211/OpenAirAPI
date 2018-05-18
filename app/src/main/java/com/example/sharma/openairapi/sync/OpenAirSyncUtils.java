
//region Import Namespaces
package com.example.sharma.openairapi.sync;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.example.sharma.openairapi.data.OpenAirContract;

import java.util.concurrent.TimeUnit;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;
//endregion


public class OpenAirSyncUtils {

    // region Variables
    private static final int SYNC_INTERVAL_HOURS = 3; // interval to sync data
    private static final int SYNC_INTERVAL_SECONDS = (int) TimeUnit.HOURS.toSeconds(SYNC_INTERVAL_HOURS);
    private static final int SYNC_FLEXTIME_SECONDS = SYNC_INTERVAL_SECONDS / 3;
    private static final String OPEN_AIR_SYNC_TAG = "air-sync";
    private static boolean sInitialized;
    //endregion


    static void scheduleFirebaseJobDispatcherSync(@NonNull final Context context) {

        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

        // Create the Job to periodically sync open air measurement
        Job syncSunshineJob = dispatcher.newJobBuilder()
                //The Service that will be used to sync open air data
                .setService(OpenAirFirebaseJobService.class)
                // Set the UNIQUE tag used to identify this Job
                .setTag(OPEN_AIR_SYNC_TAG)
                //Network constraints on which this Job should run
                .setConstraints(Constraint.ON_ANY_NETWORK)
                //setLifetime sets how long this job should persist
                .setLifetime(Lifetime.FOREVER)
                //set recurring to keep data up to date
                .setRecurring(true)
                // first argument is the start of the time frame when the sync should be performed
                // second argument is the time when the data will be synced.
                .setTrigger(Trigger.executionWindow(
                        SYNC_INTERVAL_SECONDS,
                        SYNC_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS))
                // if the job already exist it will replace
                .setReplaceCurrent(true)
                // if the job is ready build it
                .build();

        // Schedule the Job with the dispatcher
        dispatcher.schedule(syncSunshineJob);
    }


    synchronized public static void initialize(@NonNull final Context context) {

        // execute this method only once in app life time.
        if (sInitialized) return;

        sInitialized = true;

        // call to schedule the periodic sync
        scheduleFirebaseJobDispatcherSync(context);

        // check data exist for content provider. call it in separate thread.
        Thread checkForEmpty = new Thread(new Runnable() {
            @Override
            public void run() {

                /* URI for every row of weather data in our weather table*/
                Uri contentURI = OpenAirContract.OpenAirEntry.CONTENT_URI;

                String[] projectionColumns = {OpenAirContract.OpenAirEntry._ID};

                Cursor cursor = context.getContentResolver().query(
                        contentURI,
                        projectionColumns,
                        null,
                        null,
                        null);
                // if cursor null sync data
                if (null == cursor || cursor.getCount() == 0) {
                    startImmediateSync(context);
                }

                // close the cursor.
                cursor.close();
            }
        });

        // once the thread is prepared, start it
        checkForEmpty.start();
    }

    public static void startImmediateSync(@NonNull final Context context) {
        Intent intentToSyncImmediately = new Intent(context, OpenAirIntentService.class);
        context.startService(intentToSyncImmediately);
    }


}
