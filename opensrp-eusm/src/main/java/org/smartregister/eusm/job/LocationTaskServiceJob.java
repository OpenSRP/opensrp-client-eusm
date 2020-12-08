package org.smartregister.eusm.job;

import android.content.Intent;

import androidx.annotation.NonNull;

import com.evernote.android.job.Job;

import org.smartregister.AllConstants;
import org.smartregister.job.BaseJob;
import org.smartregister.tasking.sync.LocationTaskIntentService;

public class LocationTaskServiceJob extends BaseJob {

    public static final String TAG = "LocationTaskServiceJob";

    private final Class<? extends LocationTaskIntentService> locationTaskIntentService;

    public LocationTaskServiceJob() {
        locationTaskIntentService = LocationTaskIntentService.class;
    }

    public LocationTaskServiceJob(Class<? extends LocationTaskIntentService> locationTaskIntentService) {
        this.locationTaskIntentService = locationTaskIntentService;
    }

    @NonNull
    @Override
    protected Job.Result onRunJob(@NonNull Job.Params params) {
        Intent intent = new Intent(getApplicationContext(), locationTaskIntentService);
        getApplicationContext().startService(intent);
        return params != null && params.getExtras().getBoolean(AllConstants.INTENT_KEY.TO_RESCHEDULE, false) ? Job.Result.RESCHEDULE : Job.Result.SUCCESS;
    }
}
