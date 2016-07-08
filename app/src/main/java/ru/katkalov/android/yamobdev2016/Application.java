package ru.katkalov.android.yamobdev2016;

import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

public class Application extends android.app.Application{
    private RefWatcher refWatcher;

    public static RefWatcher getRefWatcher(Context context) {
        Application application = (Application) context.getApplicationContext();
        return application.refWatcher;
    }


    @Override
    public void onCreate(){
        super.onCreate();
        refWatcher = LeakCanary.install(this);
    }
}

