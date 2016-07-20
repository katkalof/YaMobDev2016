package ru.katkalov.android.yamobdev2016.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;


public class HeadsetReceiver extends BroadcastReceiver {
    final static String TAG = "HEADSET";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
            int state = intent.getIntExtra("state", -1);
            switch (state) {
                case 0:
                    Toast.makeText(context,"Headset is unplugged",Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Headset is unplugged");
                    break;
                case 1:
                    Toast.makeText(context,"Headset is plugged",Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Headset is plugged");
                    break;
                default:
                    Toast.makeText(context,"I have no idea what the headset state is",Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "I have no idea what the headset state is");
            }
        }
    }
}
