package net.lxndrsdrnk.flickrgallery.di;

import net.lxndrsdrnk.flickrgallery.FGApplication;
import net.lxndrsdrnk.flickrgallery.MainActivity;
import net.lxndrsdrnk.flickrgallery.list.PhotosFragment;
import net.lxndrsdrnk.flickrgallery.polling.PollingJobService;

import javax.inject.Singleton;

import dagger.Component;


/**
 * Created by alsidor on 14/03/2018.
 */

@Singleton
@Component(modules = {
        AndroidModule.class,
        FGModule.class
})
public interface ApplicationComponent {
    void inject(FGApplication application);
    void inject(MainActivity activity);
    void inject(PhotosFragment fragment);
    void inject(PollingJobService service);
}

