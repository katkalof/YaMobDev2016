package ru.katkalov.android.yamobdev2016.receivers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import ru.katkalov.android.yamobdev2016.R;


public class HeadsetReceiver extends BroadcastReceiver {
    private final static String TAG = "HEADSET";
    private static final String ACTION_YA_MUSIC = "ru.yandex.music";
    private static final String ACTION_YA_RADIO = "ru.yandex.radio";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
            int state = intent.getIntExtra("state", -1);
            NotificationManager mNotificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            switch (state) {
                case 0:
                    mNotificationManager.cancel("TAG", 313);
                    break;
                case 1:
                    RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.notification_headset);
                    PackageManager packageManager = context.getPackageManager();

                    Intent ya_music = packageManager.getLaunchIntentForPackage(ACTION_YA_MUSIC);
                    if (ya_music == null) {
                        ya_music = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + ACTION_YA_MUSIC));
                    }
                    contentView.setOnClickPendingIntent(R.id.ya_music, PendingIntent.getActivity(context, 0, ya_music, 0));

                    Intent ya_radio = packageManager.getLaunchIntentForPackage(ACTION_YA_RADIO);
                    if (ya_radio == null) {
                        ya_radio = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + ACTION_YA_RADIO));
                    }
                    contentView.setOnClickPendingIntent(R.id.ya_radio, PendingIntent.getActivity(context, 0, ya_radio, 0));

                    Notification.Builder builder = new Notification.Builder(context)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContent(contentView)
                            .setOngoing(true);
                    mNotificationManager.notify("TAG", 313, builder.build());
                    break;
                default:
                    Toast.makeText(context, "I have no idea what the headset state is", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "I have no idea what the headset state is");
            }
        }
    }
}
