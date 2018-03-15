package net.lxndrsdrnk.flickrgallery.api;

import net.lxndrsdrnk.flickrgallery.BuildConfig;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by alsidor on 14/03/2018.
 */

public interface FlickrAPI {

    String API_BASE_URL = "https://api.flickr.com/";

    String API_KEY = BuildConfig.FLICKR_API_KEY;

    @GET("services/rest/?method=flickr.photos.getRecent&extras=url_s&format=json&nojsoncallback=1&api_key=" + API_KEY)
    Call<FlickrResponse> getRecent(@Query("page") int page, @Query("per_page") int perPage);

    @GET("services/rest/?method=flickr.photos.search&extras=url_s&format=json&nojsoncallback=1&api_key=" + API_KEY)
    Call<FlickrResponse> search(@Query("text") String text, @Query("page") int page, @Query("per_page") int perPage);

}
