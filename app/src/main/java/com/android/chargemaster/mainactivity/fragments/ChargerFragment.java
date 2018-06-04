package com.android.chargemaster.mainactivity.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.chargemaster.R;
import com.android.chargemaster.activities.ReminderActivity;
import com.android.chargemaster.mainactivity.interfaces.SetRingerMode;
import com.android.chargemaster.mainactivity.utils.ChargerUtils;

import me.itangqi.waveloadingview.WaveLoadingView;

@SuppressLint("ValidFragment")
public class ChargerFragment extends Fragment {
    public WaveLoadingView waveLoadingView;
    public TextView temperature;
    public TextView memoryUsage;
    SetRingerMode setRingerMode;
    private boolean isEnabled;

    public ChargerFragment() {
        setRingerMode = new SetRingerMode() {
            @Override
            public void setRinger() {

            }
        };
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.charger_fragment_layout, container, false);
        waveLoadingView = view.findViewById(R.id.waveloadingView);
        temperature = view.findViewById(R.id.charger_temperature);
        memoryUsage = view.findViewById(R.id.charger_memory_usage);


        if (getActivity() != null) {
            view.findViewById(R.id.charge_action_wifi).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ChargerUtils.switchWifi(getActivity())) {
                        Toast.makeText(getContext(), "a", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "b", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            view.findViewById(R.id.charge_action_bluetooth).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ChargerUtils.switchBluetooth()) {

                    }
                }
            });

            view.findViewById(R.id.charge_action_airplane).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().startActivity(new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS));

                }
            });

            view.findViewById(R.id.charge_action_brightness).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ChargerUtils.setBrightness(getContext()) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC) {
                        Toast.makeText(getContext(), "Automatic", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Normal", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            view.findViewById(R.id.charge_action_sound).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setRingerMode.setRinger();
                }
            });

            view.findViewById(R.id.charge_action_gps).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ChargerUtils.switchGPS(getActivity());
                }
            });

            view.findViewById(R.id.charge_action_data).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().startActivity(new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS));
                }
            });

            view.findViewById(R.id.charge_action_rotate).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ChargerUtils.setRotation(getActivity());
                }
            });

            view.findViewById(R.id.charge_action_sync).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ChargerUtils.setSync(getActivity());
                }
            });

            view.findViewById(R.id.setting_layout_reminder).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ReminderActivity.class);
                    startActivity(intent);
                }
            });
        }

        return view;
    }

    public void ringerClick(SetRingerMode setRingerMode) {
        this.setRingerMode = setRingerMode;
    }

}
