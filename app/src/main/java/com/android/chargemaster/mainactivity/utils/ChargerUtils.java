package com.android.chargemaster.mainactivity.utils;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.android.chargemaster.mainactivity.utils.MainActivityUtils;
import com.android.chargemaster.mainactivity.interfaces.SetRingerModeCallBack;

import java.lang.reflect.Method;

public class ChargerUtils {
    private static final String SETTINGS_PACKAGE = "com.android.settings";
    private static final String SETTINGS_CLASS_DATA_USAGE_SETTINGS = "com.android.settings.Settings$DataUsageSummaryActivity";

    public static boolean switchWifi(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null) {
            wifiManager.setWifiEnabled(!wifiManager.isWifiEnabled());
            int numberOfLevels = 5;
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int level = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), numberOfLevels);
            Toast.makeText(context, String.valueOf(level), Toast.LENGTH_SHORT).show();
            return wifiManager.isWifiEnabled();
        }
        return false;
    }

    public static boolean switchBluetooth(){
        BluetoothAdapter mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBtAdapter != null){
            if (! mBtAdapter.isEnabled()) {
                mBtAdapter.enable();
            } else {
                mBtAdapter.disable();
            }
            return mBtAdapter.isEnabled();
        }
        return false;
    }



    public static void switchGPS(Context context){
        context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    }

    public static void setMobileDataState(Context context, boolean mobileDataEnabled)
    {
        try {
            TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
            Method methodSet = Class.forName(tm.getClass().getName()).getDeclaredMethod( "setDataEnabled", Boolean.TYPE);
            methodSet.invoke(tm,true);
        } catch (Exception e){

        }
    }

    public static boolean getMobileDataState(Context context)
    {
        try
        {
            TelephonyManager telephonyService = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

            Method getMobileDataEnabledMethod = telephonyService.getClass().getDeclaredMethod("getDataEnabled");

            if (null != getMobileDataEnabledMethod)
            {
                boolean mobileDataEnabled = (Boolean) getMobileDataEnabledMethod.invoke(telephonyService);

                return mobileDataEnabled;
            }
        }
        catch (Exception ex)
        {
            Log.e("getMobileDataState", "Error getting mobile data state", ex);
        }

        return false;
    }

    public static void setRingerMode(Context context,SetRingerModeCallBack setRingerModeCallBack){
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if(audioManager != null) {
            if(audioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL) {
                audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                setRingerModeCallBack.onSetModeDone(AudioManager.RINGER_MODE_VIBRATE);
            } else if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE){
                audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                setRingerModeCallBack.onSetModeDone(AudioManager.RINGER_MODE_SILENT);
            } else if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT) {
                audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                setRingerModeCallBack.onSetModeDone(AudioManager.RINGER_MODE_NORMAL);
            }
        }
    }

    public static void setRotation(Activity activity){
        int orientation = activity.getRequestedOrientation();
        if(orientation == ActivityInfo.SCREEN_ORIENTATION_LOCKED) {

        } else {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        }
    }

    public static void setSync(Context context){
        boolean syncEnable = ContentResolver.getMasterSyncAutomatically();
        ContentResolver.setMasterSyncAutomatically(!syncEnable);
    }

    public static int setBrightness(Context context){
//        Settings.System.putInt(context.getContentResolver(),
//                Settings.System.SCREEN_BRIGHTNESS, 20);
        int brightnessMode = Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.System.canWrite(context.getApplicationContext())) {
                try {
                    brightnessMode = Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE,
                            Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
                } catch (Exception e){
                    brightnessMode = Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
                }
                if( brightnessMode == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC) {
                    Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE,
                            Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
                    return Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL;
                } else {
                    Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE,
                            Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
                    return Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
                }
            } else {
                MainActivityUtils.settingPermission(context);
            }
        } else {
            try {
                brightnessMode = Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE,
                        Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
            } catch (Exception e){
                brightnessMode = Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
            }
            if( brightnessMode == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC) {
                Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE,
                        Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
                return Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL;
            } else {
                Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE,
                        Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
                return Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
            }
        }
        return brightnessMode;
    }
    
}
