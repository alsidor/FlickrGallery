package net.lxndrsdrnk.flickrgallery;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import net.lxndrsdrnk.flickrgallery.api.FlickrAPI;
import net.lxndrsdrnk.flickrgallery.api.FlickrResponse;
import net.lxndrsdrnk.flickrgallery.api.Photo;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements PhotosFragment.OnListFragmentInteractionListener {

    @Inject
    FlickrAPI flickrAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((FGApplication) getApplication()).component().inject(this);

        setContentView(R.layout.activity_main);

        if(null == savedInstanceState) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, PhotosFragment.newInstance(3))
                    .commit();
        }

        flickrAPI.getRecent(1, 100).enqueue(new Callback<FlickrResponse>() {
            @Override
            public void onResponse(Call<FlickrResponse> call, Response<FlickrResponse> response) {
                Log.d("API", response.message());
            }

            @Override
            public void onFailure(Call<FlickrResponse> call, Throwable t) {

            }
        });

    }

    @Override
    public void onListFragmentInteraction(Photo photo) {

    }
}
