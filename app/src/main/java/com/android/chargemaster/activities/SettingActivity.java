package com.android.chargemaster.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.android.chargemaster.R;
import com.android.chargemaster.commons.AppPreferences;
import com.android.chargemaster.mainactivity.interfaces.SelectModeHandler;

public class SettingActivity extends AppCompatActivity {
    private static final int REQUEST_REMINDER = 2;
    private TextView modeNameText;
    private TextView notificationStatus;
    private TextView reminderStatus;
    private SwitchCompat switchActive;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.LightTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        startInitial();
        switchActive.setOnCheckedChangeListener(switchActiveChange);

        findViewById(R.id.setting_layout_selectmode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectModeDialog selectModeDialog = new SelectModeDialog(SettingActivity.this);
                selectModeDialog.show();
                selectModeDialog.onItemSelect(new SelectModeHandler() {
                    @Override
                    public void onItemSelect(int mode) {
                        if(mode == AppPreferences.LOCK_SCREEN_CHARGING_MODE){
                            modeNameText.setText(getString(R.string.lock_screen_mode));
                        } else {
                            modeNameText.setText(getString(R.string.smart_charge_only));
                        }
                    }
                });
            }
        });
        findViewById(R.id.setting_layout_reminder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ReminderActivity.class);
                startActivityForResult(intent, REQUEST_REMINDER);
            }
        });
        findViewById(R.id.setting_layout_wallpaper).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    private SwitchCompat.OnCheckedChangeListener switchActiveChange = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked){
                AppPreferences.INSTANCE.putBoolean(AppPreferences.APP_ACTIVE, true);
            } else {
                AppPreferences.INSTANCE.putBoolean(AppPreferences.APP_ACTIVE, false);
            }
        }
    };

    private void startInitial() {
        AppPreferences.INSTANCE.load(SettingActivity.this);
        notificationStatus = findViewById(R.id.setting_noti_status);
        reminderStatus = findViewById(R.id.setting_reminder_status);
        switchActive = findViewById(R.id.setting_switchActive);

        boolean isAppActive = AppPreferences.INSTANCE.getBoolean(AppPreferences.APP_ACTIVE, false);
        if(isAppActive){
            switchActive.setChecked(true);
        } else {
            switchActive.setChecked(false);
        }

        int chargeMode = AppPreferences.INSTANCE.getInt(AppPreferences.CHARGING_MODE, 0);
        if(chargeMode == 0){
            modeNameText.setText(getString(R.string.lock_screen_mode));
        } else {
            modeNameText.setText(getString(R.string.smart_charge_only));
        }

        boolean reminderStt = AppPreferences.INSTANCE.getBoolean(AppPreferences.REMINDER_STATUS, true);
        if(reminderStt){
            reminderStatus.setText(getString(R.string.on));
        } else {
            reminderStatus.setText(getString(R.string.off));
        }

        boolean notificationStt = AppPreferences.INSTANCE.getBoolean(AppPreferences.NOTIFICATION_STATUS, true);
        if(notificationStt){
            notificationStatus.setText(getString(R.string.on));
        } else {
            notificationStatus.setText(getString(R.string.off));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case REQUEST_REMINDER:
                    boolean reminderStt = data.getBooleanExtra(AppPreferences.REMINDER_STATUS, true);
                    if(reminderStt){
                        reminderStatus.setText(getString(R.string.on));
                    } else {
                        reminderStatus.setText(getString(R.string.off));
                    }
                    break;
            }
        }

    }
}
