package net.lxndrsdrnk.flickrgallery.api;

import java.util.List;

/**
 * Created by alsidor on 14/03/2018.
 */

public class FlickrResponse {

    public static class Photos {

        public Integer page;
        public Integer pages;
        public Integer perpage;
        public Integer total;

        public List<Photo> photo;

    }

    public Photos photos;

}
