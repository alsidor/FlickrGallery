package net.lxndrsdrnk.flickrgallery.di;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.lxndrsdrnk.flickrgallery.api.FlickrAPI;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by alsidor on 14/03/2018.
 */


@Module
public class FGModule {

    private final Application application;

    public FGModule(Application application) {
        this.application = application;
    }

//    @Provides
//    @Singleton
//    Bus provideEventBus() {
//        return new Bus(ThreadEnforcer.ANY);
//    }

    @Provides
    @Singleton
    FlickrAPI provideFlickrAPI(){

        OkHttpClient client = new OkHttpClient.Builder()
                .build();

        Gson gson = new GsonBuilder()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(FlickrAPI.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit.create(FlickrAPI.class);
    }

}
