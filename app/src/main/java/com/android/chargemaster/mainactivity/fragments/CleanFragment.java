package com.android.chargemaster.mainactivity.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.android.chargemaster.R;
import com.android.chargemaster.entities.ActionItem;
import com.android.chargemaster.mainactivity.adapters.ActionListAdapter;

import java.util.ArrayList;

public class CleanFragment extends Fragment {
    private ListView listviewAction;
    private ArrayList<ActionItem> listAction = new ArrayList<>();
    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.clean_fragment_layout, container,false);
        listviewAction = view.findViewById(R.id.clean_fragment_list_action);
        listAction.add(new ActionItem(0, getString(R.string.trash_cleaner), "", ""));
        listAction.add(new ActionItem(0, getString(R.string.security_inspection), "", ""));
        listAction.add(new ActionItem(0, getString(R.string.memory_boost), "", ""));
        listAction.add(new ActionItem(0, getString(R.string.fast_charge), "", ""));
        listAction.add(new ActionItem(0, getString(R.string.app_lock), "", ""));
        listAction.add(new ActionItem(0, getString(R.string.duplicated_photo), "", ""));

        ActionListAdapter actionListAdapter = new ActionListAdapter(getContext(), listAction);
        listviewAction.setAdapter(actionListAdapter);

        return view;
    }


}
