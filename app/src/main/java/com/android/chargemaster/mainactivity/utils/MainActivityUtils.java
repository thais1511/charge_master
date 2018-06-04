package com.android.chargemaster.mainactivity.utils;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;

import com.android.chargemaster.commons.AppPreferences;
import com.android.chargemaster.mainactivity.interfaces.SetRingerModeCallBack;

import java.text.SimpleDateFormat;

public class MainActivityUtils {

    public static void getNotificationPolicy(final Context contextActivity) {
        NotificationManager notificationManager =
                (NotificationManager) contextActivity.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!notificationManager.isNotificationPolicyAccessGranted()) {
                Intent intent = new Intent(
                        android.provider.Settings
                                .ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                contextActivity.startActivity(intent);
            } else {
                setRingerMode(contextActivity);
            }
        } else {
            setRingerMode(contextActivity);
        }
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
//                ActivityCompat.checkSelfPermission(contextActivity,
//                Manifest.permission.READ_CONTACTS)
//                == PackageManager.PERMISSION_GRANTED){
//        } else {
//            ActivityCompat.requestPermissions((Activity) contextActivity,
//                    new String[]{ Manifest.permission.READ_CONTACTS },
//                    1);
//        }
    }

    private static void setRingerMode(final Context contextActivity){
        ChargerUtils.setRingerMode(contextActivity, new SetRingerModeCallBack() {
            @Override
            public void onSetModeDone(int mode) {
                if (mode == AudioManager.RINGER_MODE_SILENT) {
                    Toast.makeText(contextActivity, "Silent", Toast.LENGTH_SHORT).show();
                } else if (mode == AudioManager.RINGER_MODE_VIBRATE) {
                    Toast.makeText(contextActivity, "Vibrate", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(contextActivity, "Normal", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void settingPermission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(context.getApplicationContext())) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + context.getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        } else {

        }
    }

    public static boolean checkInSilentTime() {
        SimpleDateFormat hourFormat = new SimpleDateFormat("HH");
        SimpleDateFormat minuteFormat = new SimpleDateFormat("mm");
        int currentHour = Integer.parseInt(hourFormat.format(System.currentTimeMillis()));
        int currentMinute = Integer.parseInt(minuteFormat.format(System.currentTimeMillis()));
        int startSilentHour = AppPreferences.INSTANCE.getInt(AppPreferences.SILENT_START_HOUR, 22);
        int startSilentMinute = AppPreferences.INSTANCE.getInt(AppPreferences.SILENT_START_MINUTE, 0);
        int endSilentHour = AppPreferences.INSTANCE.getInt(AppPreferences.SILENT_END_HOUR, 8);
        int endSilentMinute = AppPreferences.INSTANCE.getInt(AppPreferences.SILENT_END_MINUTE, 0);

        if (startSilentHour < endSilentHour) {
            if ((currentHour >= startSilentHour && currentMinute >= startSilentMinute) &&
                    currentHour <= endSilentHour && currentHour <= endSilentMinute) {
                return true;
            } else {
                return false;
            }
        } else {
            if ((currentHour >= endSilentHour && currentMinute >= endSilentMinute) &&
                    currentHour <= startSilentHour && currentHour <= startSilentMinute) {
                return false;
            } else {
                return true;
            }
        }
    }

}
