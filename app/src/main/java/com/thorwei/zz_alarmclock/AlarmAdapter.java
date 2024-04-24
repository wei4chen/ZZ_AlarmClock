package com.thorwei.zz_alarmclock;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Switch;
import android.widget.TextView;
import java.util.List;

public class AlarmAdapter extends BaseAdapter {

    private LayoutInflater listlayoutInflater;
    private MainActivity mm;
    private List<AlarmModel> mList;

    public AlarmAdapter(Context c, List<AlarmModel> list) {
        mm = (MainActivity) c;
        mList = list;
        listlayoutInflater = LayoutInflater.from(c);
    }

    public void setData(List<AlarmModel> list) {
        mList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mm.TabTimes.length;
    }

    @Override
    public Object getItem(int position) {
        return mm.TabTimes[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = listlayoutInflater.inflate(R.layout.alarm_list, null);

        TextView alarmlistTime = (TextView) convertView.findViewById(R.id.alarmlist_time);
        TextView alarmlistTag = (TextView) convertView.findViewById(R.id.alarmlist_tag);
        Switch alarmlistSwitch = (Switch) convertView.findViewById(R.id.alarmlist_switch);

        alarmlistTime.setText(mm.TabTimes[position]);
        alarmlistTag.setText(mm.TabTags[position]);
        alarmlistSwitch.setChecked(mm.TabEnbale[position]);

        return convertView;
    }
}
