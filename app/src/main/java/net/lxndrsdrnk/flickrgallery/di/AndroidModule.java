package net.lxndrsdrnk.flickrgallery.di;


import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.view.inputmethod.InputMethodManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Module for all Android related provisions
 */
@Module
public class AndroidModule {

    private final Application application;

    public AndroidModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Context provideApplicationContext() {
        return application;
    }

    @Provides
    SharedPreferences provideDefaultSharedPreferences(final Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Provides
    PackageInfo providePackageInfo(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    @SuppressWarnings("unchecked")
    public <T> T getSystemService(Context context, String serviceConstant) {
        return (T) context.getSystemService(serviceConstant);
    }

    @Provides
    InputMethodManager provideInputMethodManager(final Context context) {
        return (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Provides
    ApplicationInfo provideApplicationInfo(final Context context) {
        return context.getApplicationInfo();
    }

    @Provides
    ClassLoader provideClassLoader(final Context context) {
        return context.getClassLoader();
    }

    @Provides
    @Singleton
    LocationManager provideLocationManager(final Context context) {
        return (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }
}
