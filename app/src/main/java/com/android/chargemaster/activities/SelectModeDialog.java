package com.android.chargemaster.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.android.chargemaster.R;
import com.android.chargemaster.commons.AppPreferences;
import com.android.chargemaster.mainactivity.interfaces.SelectModeHandler;

public class SelectModeDialog extends AlertDialog {
    private Context context;
    private RadioButton rbLockMode;
    private RadioButton rbNormalMode;
    private SelectModeHandler selectModeHandler;
    protected SelectModeDialog(Context context) {
        super(context);
        this.context = context;
        selectModeHandler = new SelectModeHandler() {
            @Override
            public void onItemSelect(int mode) {

            }
        };
    }

    public void onItemSelect(SelectModeHandler selectModeHandler){
        this.selectModeHandler = selectModeHandler;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_selectmode);
        AppPreferences.INSTANCE.load(context);
        rbLockMode = findViewById(R.id.select_mode_radio_lock);
        rbNormalMode = findViewById(R.id.select_mode_radio_normal);

        int chargingMode = AppPreferences.INSTANCE.getInt(AppPreferences.CHARGING_MODE, 0);
        if(chargingMode == 0){
            rbLockMode.setChecked(true);
            rbNormalMode.setChecked(false);
        } else {
            rbLockMode.setChecked(false);
            rbNormalMode.setChecked(true);
        }

        rbLockMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    AppPreferences.INSTANCE.putInt(AppPreferences.CHARGING_MODE, AppPreferences.LOCK_SCREEN_CHARGING_MODE);
                    selectModeHandler.onItemSelect(AppPreferences.LOCK_SCREEN_CHARGING_MODE);
                    dismiss();
                }
            }
        });

        rbNormalMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    AppPreferences.INSTANCE.putInt(AppPreferences.CHARGING_MODE, AppPreferences.CHARGING_ONLY_MODE);
                    selectModeHandler.onItemSelect(AppPreferences.CHARGING_ONLY_MODE);
                    dismiss();
                }
            }
        });

    }
}
