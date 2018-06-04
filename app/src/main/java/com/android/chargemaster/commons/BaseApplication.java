package com.android.chargemaster.commons;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import com.android.chargemaster.R;
import com.android.chargemaster.commons.AppPreferences;

public class BaseApplication extends Application{
    private static final String CHANNEL_ID = "ChannelID";
    @Override
    public void onCreate() {
        super.onCreate();

        AppPreferences.INSTANCE.load(this);
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        NotificationChannel channel;
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_LOW;
            channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            channel.setSound(uri, null);
            channel.enableLights(true);
            mNotificationManager.createNotificationChannel(channel);
        }
    }
}
