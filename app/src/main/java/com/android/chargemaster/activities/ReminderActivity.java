package com.android.chargemaster.activities;

import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.android.chargemaster.R;
import com.android.chargemaster.commons.AppPreferences;
import com.github.stephenvinouze.materialnumberpickercore.MaterialNumberPicker;

public class ReminderActivity extends AppCompatActivity {
    private MaterialNumberPicker pickerStartHour;
    private MaterialNumberPicker pickerEndHour;
    private MaterialNumberPicker pickerStartMinute;
    private MaterialNumberPicker pickerEndMinute;
    private SwitchCompat switchOnOff;
    private SwitchCompat switchSilentMode;
    private TextView ringtoneName;
    private TextView silentModeText;
    private int startHourValue;
    private int endHourValue;
    private int startMinuteValue;
    private int endMinuteValue;
    private int REQUEST_RINGTONE = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        initial();

        boolean isReminderOn = AppPreferences.INSTANCE.getBoolean(AppPreferences.REMINDER_STATUS, true);
        setSwitch(isReminderOn, switchOnOff);
        switchOnOff.setOnCheckedChangeListener(switchReminderChange);
        boolean isSilentModeOn = AppPreferences.INSTANCE.getBoolean(AppPreferences.SILENT_STATUS, true);
        setSwitch(isSilentModeOn, switchSilentMode);
        switchSilentMode.setOnCheckedChangeListener(swSilentChange);

        findViewById(R.id.reminder_layout_ringtone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Tone");
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, (Uri) null);
                startActivityForResult(intent, REQUEST_RINGTONE);
            }
        });

        findViewById(R.id.toolbar_back_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean reminderStt = AppPreferences.INSTANCE.getBoolean(AppPreferences.REMINDER_STATUS, true);
                Intent resultIntent = new Intent();
                resultIntent.putExtra(AppPreferences.REMINDER_STATUS, reminderStt);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    private void initial() {
        pickerStartHour = findViewById(R.id.reminder_start_hour);
        pickerStartHour.setFormatter(formatter);
        pickerEndHour = findViewById(R.id.reminder_end_hour);
        pickerEndHour.setFormatter(formatter);
        pickerStartMinute = findViewById(R.id.reminder_start_minute);
        pickerStartMinute.setFormatter(formatter);
        pickerEndMinute = findViewById(R.id.reminder_end_minute);
        pickerEndMinute.setFormatter(formatter);
        switchOnOff = findViewById(R.id.reminder_switch_onoff);
        switchSilentMode = findViewById(R.id.reminder_switch_silent);
        ringtoneName = findViewById(R.id.reminder_ringtone_name);
        silentModeText = findViewById(R.id.silent_mode_text);

        AppPreferences.INSTANCE.load(getBaseContext());
        startHourValue = AppPreferences.INSTANCE.getInt(AppPreferences.SILENT_START_HOUR, 22);
        startMinuteValue = AppPreferences.INSTANCE.getInt(AppPreferences.SILENT_START_MINUTE, 0);
        endHourValue = AppPreferences.INSTANCE.getInt(AppPreferences.SILENT_END_HOUR, 8);
        endMinuteValue = AppPreferences.INSTANCE.getInt(AppPreferences.SILENT_END_MINUTE, 0);
        updateSilentText();
        pickerStartHour.setValue(startHourValue);
        pickerStartMinute.setValue(startMinuteValue);
        pickerEndHour.setValue(endHourValue);
        pickerEndMinute.setValue(endMinuteValue);

        pickerStartHour.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                pickerValueChange(picker, AppPreferences.SILENT_START_HOUR, 0);
            }
        });
        pickerStartMinute.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                pickerValueChange(picker, AppPreferences.SILENT_START_MINUTE, 1);
            }
        });
        pickerEndHour.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                pickerValueChange(picker, AppPreferences.SILENT_END_HOUR, 2);
            }
        });
        pickerEndMinute.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                pickerValueChange(picker, AppPreferences.SILENT_END_MINUTE, 3);
            }
        });

        String ringtoneStr = AppPreferences.INSTANCE.getString(AppPreferences.REMINDER_RINGTONE, AppPreferences.DEFAULT_RINGTONE);
        Uri ringtoneUri = Uri.parse(ringtoneStr);
        Ringtone ringtone = RingtoneManager.getRingtone(ReminderActivity.this, ringtoneUri);
        String ringtoneTitle = ringtone.getTitle(ReminderActivity.this);
        ringtoneName.setText(ringtoneTitle);

        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText(R.string.full_charge_reminder);

    }

    NumberPicker.Formatter formatter = new NumberPicker.Formatter() {
        @Override
        public String format(int value) {
            if (value < 10) {
                return "0" + String.valueOf(value);
            }
            return String.valueOf(value);
        }
    };

    private void setSwitch(boolean status, SwitchCompat switchCompat) {
        if (status) {
            switchCompat.setChecked(true);
        } else {
            switchCompat.setChecked(false);
        }
    }

    private SwitchCompat.OnCheckedChangeListener switchReminderChange = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                AppPreferences.INSTANCE.putBoolean(AppPreferences.REMINDER_STATUS, true);
            } else {
                AppPreferences.INSTANCE.putBoolean(AppPreferences.REMINDER_STATUS, false);
            }
        }
    };

    private SwitchCompat.OnCheckedChangeListener swSilentChange = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                AppPreferences.INSTANCE.putBoolean(AppPreferences.SILENT_STATUS, true);
                updateSilentText();
                findViewById(R.id.reminder_layout_set_time).setEnabled(true);
            } else {
                AppPreferences.INSTANCE.putBoolean(AppPreferences.SILENT_STATUS, false);
                silentModeText.setText(getString(R.string.always_remind));
                findViewById(R.id.reminder_layout_set_time).setEnabled(false);
            }
        }
    };

    private String getValuetoString(int value) {
        if (value < 10) {
            return "0" + value;
        }
        return String.valueOf(value);
    }

    private void pickerValueChange(NumberPicker picker, final String preference, int valueIndex) {
        switch (valueIndex) {
            case 0:
                startHourValue = picker.getValue();
                break;
            case 1:
                startMinuteValue = picker.getValue();
                break;
            case 2:
                endHourValue = picker.getValue();
                break;
            case 3:
                endMinuteValue = picker.getValue();
                break;
        }
        AppPreferences.INSTANCE.putInt(preference, picker.getValue());
        updateSilentText();
    }

    private void updateSilentText() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getValuetoString(startHourValue) + ":");
        stringBuilder.append(getValuetoString(startMinuteValue) + " - ");
        stringBuilder.append(getValuetoString(endHourValue) + ":");
        stringBuilder.append(getValuetoString(endMinuteValue));
        silentModeText.setText(stringBuilder);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_RINGTONE) {
            Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);

            if (uri != null) {
                AppPreferences.INSTANCE.putString(AppPreferences.REMINDER_RINGTONE, uri.toString());
                Ringtone ringtone = RingtoneManager.getRingtone(ReminderActivity.this, uri);
                String title = ringtone.getTitle(ReminderActivity.this);
                ringtoneName.setText(title);
            } else {
                AppPreferences.INSTANCE.putString(AppPreferences.REMINDER_RINGTONE, "Default");
            }
        }
    }
}
