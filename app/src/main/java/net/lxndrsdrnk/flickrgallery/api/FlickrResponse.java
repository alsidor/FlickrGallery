package net.lxndrsdrnk.flickrgallery.api;

import java.util.List;

/**
 * Created by alsidor on 14/03/2018.
 */

public class FlickrResponse {

    public static class Photos {

        Integer page;
        Integer pages;
        Integer perpage;
        Integer total;

        List<Photo> photo;

    }

    public Photos photos;

}
