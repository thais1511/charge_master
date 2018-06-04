package com.android.chargemaster.activities;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.chargemaster.R;
import com.android.chargemaster.commons.AppPreferences;
import com.android.chargemaster.mainactivity.utils.MainActivityUtils;
import com.android.chargemaster.mainactivity.adapters.ViewPagerAdapter;
import com.android.chargemaster.mainactivity.fragments.ChargerFragment;
import com.android.chargemaster.mainactivity.interfaces.SetRingerMode;
import com.example.tapimac1.progressviewlibrary.CircleSegmentBar;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private static final String CHANNEL_ID = "ChannelID";
    private ChargerFragment chargerFragment;
    private static final int REQUEST_LOCATION = 111;
    private NotificationManager mNotificationManager;

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Toast.makeText(this, getString(R.string.connect_success), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, getString(R.string.connect_fail), Toast.LENGTH_SHORT).show();
    }

    public class PowerConnectionReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            final int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(chargerFragment.waveLoadingView != null){
                        chargerFragment.waveLoadingView.setTopTitle(String.valueOf(level) + "%");
                        chargerFragment.waveLoadingView.setProgressValue(level);
                    }
                }
            }, 500);

            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                    status == BatteryManager.BATTERY_STATUS_FULL;

            int chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
            boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
            boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;

            int voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);

            final int temp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);

            String technology = intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY);

            int health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, -1);

            int small_icon = intent.getIntExtra(BatteryManager.EXTRA_ICON_SMALL, -1);

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    chargerFragment.temperature.setText(String.valueOf(temp / 10f) + "°C");
                }
            }, 501);

            boolean isReminderOn = AppPreferences.INSTANCE.getBoolean(AppPreferences.REMINDER_STATUS, true);
            if(isReminderOn){
                boolean silentModeStt = AppPreferences.INSTANCE.getBoolean(AppPreferences.SILENT_STATUS, true);
                if(silentModeStt){
                    if (level == 100 && MainActivityUtils.checkInSilentTime()) {
                        pushNotification();
                    }
                } else {
                    if (level == 100) {
                        pushNotification();
                    }
                }
            }

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
                    ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                    activityManager.getMemoryInfo(mi);
                    double availableMegs = mi.availMem / (1000f * 1024f * 1024f);
                    String available = String.valueOf(availableMegs);
                    double totalMemory = mi.totalMem / (1000f * 1024f * 1024f);
                    String totalMemoryStr = String.valueOf(totalMemory);
                    chargerFragment.memoryUsage.setText(available.substring(0, available.lastIndexOf(".") + 3) + " / "+ totalMemoryStr.substring(0,
                            totalMemoryStr.lastIndexOf(".") + 3) +"G");
                }
            }, 1000);

            CircleSegmentBar circleSegmentBar = findViewById(R.id.segmentProgress);
            circleSegmentBar.setProgress(100-level);

        }
    }

    public class AirPlaneReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("AirplaneMode", "Service state changed");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabLayout = findViewById(R.id.main_tablayout);
        viewPager = findViewById(R.id.main_viewpager);

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        final ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Fragment page = getSupportFragmentManager().findFragmentByTag("android:switcher:" +
                        R.id.main_viewpager + ":" + 0);
                chargerFragment = (ChargerFragment) page;
                registerReceiver(new PowerConnectionReceiver(), new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

                chargerFragment.ringerClick(new SetRingerMode() {
                    @Override
                    public void setRinger() {
                        MainActivityUtils.getNotificationPolicy(MainActivity.this);
                    }
                });
            }
        }, 1000);

        IntentFilter intentFilter = new IntentFilter("android.intent.action.AIRPLANE_MODE");
        registerReceiver(new AirPlaneReceiver(), intentFilter);
    }

    private void pushNotification() {
        Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
//        resultIntent.putExtra(RESULT_FILE, mOutFile.getAbsolutePath());
//        resultIntent.putExtra(ACTION, OPEN_NOTI);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        String ringtoneSt = AppPreferences.INSTANCE.getString(AppPreferences.REMINDER_RINGTONE, AppPreferences.DEFAULT_RINGTONE);
        Uri uri = Uri.parse(ringtoneSt);
        Notification noti = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            noti = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText(getString(R.string.full_charge_notification))
                    .setSound(uri)
                    .setAutoCancel(true)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(resultPendingIntent)
                    .setPriority(NotificationManager.IMPORTANCE_MAX)
                    .setVibrate(new long[1])
                    .setWhen(System.currentTimeMillis())
                    .setBadgeIconType(R.mipmap.ic_launcher)
                    .build();
            mNotificationManager.notify(1, noti);
        } else {
            noti = new NotificationCompat.Builder(MainActivity.this)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText(getString(R.string.full_charge_notification))
                    .setSound(uri)
                    .setAutoCancel(true)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(resultPendingIntent)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setVibrate(new long[1])
                    .setWhen(System.currentTimeMillis())
                    .setBadgeIconType(R.mipmap.ic_launcher)
                    .build();
            mNotificationManager.notify(1, noti);
        }
    }

}
