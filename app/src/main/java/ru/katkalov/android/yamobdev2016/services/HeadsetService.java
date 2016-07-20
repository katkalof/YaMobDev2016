package ru.katkalov.android.yamobdev2016.services;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;

import ru.katkalov.android.yamobdev2016.receivers.HeadsetReceiver;

public class HeadsetService extends Service {
    private HeadsetReceiver headsetReceiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        headsetReceiver = new HeadsetReceiver();
        registerHeadsetReceiver();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterHeadsetReceiver();
    }

    private void registerHeadsetReceiver() {
        registerReceiver(headsetReceiver, new IntentFilter(Intent.ACTION_HEADSET_PLUG));
    }

    private void unregisterHeadsetReceiver() {
        unregisterReceiver(headsetReceiver);
    }
}