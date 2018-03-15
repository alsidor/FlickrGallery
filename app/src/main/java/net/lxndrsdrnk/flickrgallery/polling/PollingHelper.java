package net.lxndrsdrnk.flickrgallery.polling;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import net.lxndrsdrnk.flickrgallery.R;
import net.lxndrsdrnk.flickrgallery.SettingsKeys;

/**
 * Created by alsidor on 15/03/2018.
 */

public class PollingHelper {

    private final Context context;
    private final SharedPreferences sharedPreferences;

    public PollingHelper(Context context, SharedPreferences sharedPreferences) {
        this.context = context;
        this.sharedPreferences = sharedPreferences;
    }

    public void startPolling(){

        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));


        Job myJob = dispatcher.newJobBuilder()
                .setService(PollingJobService.class)
                .setTag(PollingJobService.TAG)
                .setRecurring(true)
                .setLifetime(Lifetime.FOREVER)
                .setTrigger(Trigger.executionWindow(10, 20))
                .setConstraints(Constraint.ON_UNMETERED_NETWORK)
                .setReplaceCurrent(true)
                .build();

        dispatcher.mustSchedule(myJob);

        Toast.makeText(context, R.string.polling_started, Toast.LENGTH_SHORT).show();

        sharedPreferences.edit().putBoolean(SettingsKeys.POLLING_ENABLED, true).commit();
    }

    public void stopPolling(){

        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));

        dispatcher.cancel(PollingJobService.TAG);

        if(isPollingEnabled()){
            Toast.makeText(context, R.string.polling_stopped, Toast.LENGTH_SHORT).show();
        }

        sharedPreferences.edit().putBoolean(SettingsKeys.POLLING_ENABLED, false).commit();

    }

    public boolean isPollingEnabled(){
        return sharedPreferences.getBoolean(SettingsKeys.POLLING_ENABLED, false);
    }
}
