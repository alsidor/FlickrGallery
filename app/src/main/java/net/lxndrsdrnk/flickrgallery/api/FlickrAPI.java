package net.lxndrsdrnk.flickrgallery.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by alsidor on 14/03/2018.
 */

public interface FlickrAPI {

    String API_BASE_URL = "https://api.flickr.com/";

    String API_KEY = "dd92117578eb0e66f9494028694dcdc2";

    @GET("services/rest/?method=flickr.photos.getRecent&extras=url_s&format=json&nojsoncallback=1&api_key=" + API_KEY)
    Call<FlickrResponse> getRecent(@Query("page") Integer page, @Query("per_page") Integer perPage);

}
