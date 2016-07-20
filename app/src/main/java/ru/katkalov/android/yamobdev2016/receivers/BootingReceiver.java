package ru.katkalov.android.yamobdev2016.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ru.katkalov.android.yamobdev2016.services.HeadsetService;

public class BootingReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, HeadsetService.class));
    }
}
