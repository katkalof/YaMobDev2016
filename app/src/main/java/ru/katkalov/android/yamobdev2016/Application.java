package ru.katkalov.android.yamobdev2016;

import android.content.Context;
import android.content.Intent;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import ru.katkalov.android.yamobdev2016.services.HeadsetService;

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
        startService(new Intent(this, HeadsetService.class));
    }
}

