package com.android.chargemaster.commons;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public enum AppPreferences {
    INSTANCE;
    //charging mode
    public static final String CHARGING_MODE = "charging_mode";
    public static final int LOCK_SCREEN_CHARGING_MODE = 0;
    public static final int CHARGING_ONLY_MODE = 1;
    //Reminder
    public static final String REMINDER_STATUS = "reminder_status";
    public static final String SILENT_STATUS = "silent_status";
    public static final String SILENT_START_HOUR = "start_hour";
    public static final String SILENT_END_HOUR = "end_hour";
    public static final String SILENT_START_MINUTE = "start_minute";
    public static final String SILENT_END_MINUTE = "end_minute";
    //ringtone
    public static final String REMINDER_RINGTONE = "reminder_ringtone";
    public static final String DEFAULT_RINGTONE = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION).toString();;

    //notification
    public static final String NOTIFICATION_STATUS = "notification_status";
    //app active
    public static final String APP_ACTIVE = "app_active";

    private SharedPreferences pre;

    public void load(Context context) {
        pre = context.getSharedPreferences("phone.cooler", Context.MODE_PRIVATE);
    }

    public String getString(String key, @Nullable String defValue) {
        return pre.getString(key, defValue);
    }

    public int getInt(String key, int defValue) {
        return pre.getInt(key, defValue);
    }

    public long getLong(String key, long defValue) {
        return pre.getLong(key, defValue);
    }

    public float getFloat(String key, float defValue) {
        return pre.getFloat(key, defValue);
    }

    public boolean getBoolean(String key, boolean defValue) {
        return pre.getBoolean(key, defValue);
    }

    public ArrayList<String> getArrayList(String key) {
        if (contains(key)) {
            Set<String> set = pre.getStringSet(key, null);
            ArrayList<String> dst = new ArrayList<>();
            dst.addAll(set);
            return dst;
        }

        return new ArrayList<>();
    }

    public boolean contains(String key) {
        return pre.contains(key);
    }

    public void putString(String key, @Nullable String value) {
        pre.edit().putString(key, value).apply();
    }

    public void putInt(String key, int value) {
        pre.edit().putInt(key, value).apply();
    }

    public void putLong(String key, long value) {
        pre.edit().putLong(key, value).apply();
    }

    public void putFloat(String key, float value) {
        pre.edit().putFloat(key, value).apply();
    }

    public void putBoolean(String key, boolean value) {
        pre.edit().putBoolean(key, value).apply();
    }

    public void putArrayList(String key, ArrayList<String> arrayList) {
        Set<String> set = new HashSet<>();
        set.addAll(arrayList);
        pre.edit().putStringSet(key, set).apply();
    }

    public void removeItem(String key, String item) {
        ArrayList<String> all = getArrayList(key);
        if (all.contains(item)) {
            all.remove(item);
            putArrayList(key, all);
        }
    }

    public void addItem(String key, String item) {
        ArrayList<String> all = getArrayList(key);
        if (!all.contains(item)) {
            all.add(item);
            putArrayList(key, all);
        }
    }

    public boolean isLongTimeScanMode(String key, long time) {
        return (System.currentTimeMillis() - getLong(key, 0)) > time;
    }
}

