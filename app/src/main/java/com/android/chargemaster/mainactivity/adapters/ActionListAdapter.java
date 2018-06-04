package com.android.chargemaster.mainactivity.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.chargemaster.R;
import com.android.chargemaster.entities.ActionItem;

import java.util.ArrayList;

public class ActionListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ActionItem> listAction;
    public ActionListAdapter(Context context, ArrayList<ActionItem> listAction) {
        this.context = context;
        this.listAction = listAction;
    }


    @Override
    public int getCount() {
        return listAction.size();
    }

    @Override
    public Object getItem(int position) {
        return listAction.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.clean_action_item, parent, false);
        TextView actionName = view.findViewById(R.id.clean_item_actionName);
        TextView description = view.findViewById(R.id.clean_item_description);
        TextView process = view.findViewById(R.id.clean_item_process);
        ImageView imageThumb = view.findViewById(R.id.clean_item_image);

        ActionItem actionItem = listAction.get(position);
        actionName.setText(actionItem.getName());
        description.setText(actionItem.getName());
        process.setText(actionItem.getProcessText());

        return view;
    }
}
