package net.lxndrsdrnk.flickrgallery.polling;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.text.TextUtils;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import net.lxndrsdrnk.flickrgallery.FGApplication;
import net.lxndrsdrnk.flickrgallery.MainActivity;
import net.lxndrsdrnk.flickrgallery.R;
import net.lxndrsdrnk.flickrgallery.SettingsKeys;
import net.lxndrsdrnk.flickrgallery.api.FlickrAPI;
import net.lxndrsdrnk.flickrgallery.api.FlickrResponse;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by alsidor on 15/03/2018.
 */

public class PollingJobService extends JobService {

    public static final String TAG = "polling_job_service";

    @Inject
    FlickrAPI flickrAPI;

    @Inject
    SharedPreferences sharedPreferences;


    @Override
    public void onCreate() {
        super.onCreate();
        ((FGApplication) getApplication()).component().inject(this);
    }

    @Override
    public boolean onStartJob(JobParameters job) {

        final String searchValue = sharedPreferences.getString(SettingsKeys.CURRENT_SEARCH_VALUE, null);
        if (!TextUtils.isEmpty(searchValue)) {

            flickrAPI.search(searchValue, 1, 1).enqueue(new Callback<FlickrResponse>() {
                @Override
                public void onResponse(Call<FlickrResponse> call, Response<FlickrResponse> response) {
                    handleFlickrResponse(response);
                }

                @Override
                public void onFailure(Call<FlickrResponse> call, Throwable t) {

                }
            });


        }

        return false;
    }

    private void handleFlickrResponse(Response<FlickrResponse> response) {
        if (response.isSuccessful()) {
            FlickrResponse flickrResponse = response.body();
            if (flickrResponse.photos != null
                    && flickrResponse.photos.photo != null
                    && !flickrResponse.photos.photo.isEmpty()) {
                final String lastPhotoId = flickrResponse.photos.photo.get(0).id;

                final String storedPhotoId = sharedPreferences.getString(SettingsKeys.LAST_PHOTO_ID, "");
                if (!storedPhotoId.equals(lastPhotoId)) {
                    // We have new photo!
                    sharedPreferences.edit().putString(SettingsKeys.LAST_PHOTO_ID, lastPhotoId);
                    postNotification();
                }
            }
        }
    }

    public void postNotification() {

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra(MainActivity.EXTRA_REFRESH_DATA, true);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

        Notification notification = new NotificationCompat.Builder(getApplicationContext(), NotificationCompat.CATEGORY_SOCIAL)
                .setSmallIcon(R.drawable.ic_notification_icon_24dp)
                .setContentTitle(getString(R.string.new_flickr_pictures))
                .setContentText(getString(R.string.new_pictures_description))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .build();

        NotificationManagerCompat.from(getApplicationContext()).notify(0, notification);
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false;
    }
}
