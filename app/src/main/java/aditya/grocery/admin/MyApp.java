package aditya.grocery.admin;

import android.app.Application;

import com.beardedhen.androidbootstrap.TypefaceProvider;

/**
 * Created by adi on 19/10/16.
 */
public class MyApp extends Application {

    @Override
    public void onCreate() {
        TypefaceProvider.registerDefaultIconSets();
        super.onCreate();
    }
}
