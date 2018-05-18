
//region Import Namespaces
package com.example.sharma.openairapi.sync;

import android.content.Context;
import android.os.AsyncTask;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
//endregion

public class OpenAirFirebaseJobService extends JobService {

    //region Variables
    private AsyncTask<Void, Void, Void> mFetchOpenAirAsyncTask;
    //endregion


    //region Override
    @Override
    public boolean onStartJob(final JobParameters jobParameters) {
        mFetchOpenAirAsyncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Context context = getApplicationContext();
                OpenAirSyncTask.syncOpenAir(context);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                jobFinished(jobParameters, false);
            }
        };
        mFetchOpenAirAsyncTask.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        if (mFetchOpenAirAsyncTask != null) {
            mFetchOpenAirAsyncTask.cancel(true);
        }
        return true;
    }
    //endregion
}