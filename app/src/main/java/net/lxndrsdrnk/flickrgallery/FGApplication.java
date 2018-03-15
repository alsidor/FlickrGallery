package net.lxndrsdrnk.flickrgallery;

import android.app.Application;

import net.lxndrsdrnk.flickrgallery.di.AndroidModule;
import net.lxndrsdrnk.flickrgallery.di.ApplicationComponent;
import net.lxndrsdrnk.flickrgallery.di.DaggerApplicationComponent;
import net.lxndrsdrnk.flickrgallery.di.FGModule;

/**
 * Created by alsidor on 14/03/2018.
 */

public class FGApplication extends Application{

    private ApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerApplicationComponent.builder()
                .androidModule(new AndroidModule(this))
                .fGModule(new FGModule(this))
                .build();
        component().inject(this); // As of now, LocationManager should be injected into this.
    }

    public ApplicationComponent component() {
        return component;
    }
}