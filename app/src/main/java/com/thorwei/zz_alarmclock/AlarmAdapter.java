package com.thorwei.zz_alarmclock;

import static com.thorwei.zz_alarmclock.MainActivity.TAG;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

public class AlarmAdapter extends BaseAdapter {

    private LayoutInflater listlayoutInflater;
    private Context mContext;
    private List<AlarmModel> mList;

    public AlarmAdapter(Context c, List<AlarmModel> list) {
        mContext = c;
        mList = list;
        listlayoutInflater = LayoutInflater.from(c);
    }

    public void setData(List<AlarmModel> list) {
        mList = list;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return mList != null ? mList.size() : 0;
        //return mm.TabTimes.length;
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
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

        final AlarmModel bufAlarm = mList.get(position);

        String strTime = String.format("%02d:%02d", bufAlarm.hour, bufAlarm.minute);
        alarmlistTime.setText(strTime);
        String strTag = bufAlarm.tag + "  " + getRepeatString(bufAlarm.repeat);
        alarmlistTag.setText(strTag);
        alarmlistSwitch.setChecked(bufAlarm.enable);
        alarmlistSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                bufAlarm.setEnable(isChecked);
                if (isChecked) {
                    AlarmManagerHelper.startAlarmClock(mContext, bufAlarm);
                } else {
                    AlarmManagerHelper.cancelAlarmClock(mContext, bufAlarm.id);
                }
                AlarmDBUtils.updateAlarmClock(mContext, bufAlarm);
            }
        });


        return convertView;
    }

    private String getRepeatString(int repeat) {
        boolean[] flags = new boolean[] {false,false,false,false,false,false,false};
        String remindString = "";
        if (repeat == 0) {
            remindString = "";
        } else if (repeat == 127) {
            Arrays.fill(flags, true);
            remindString = mContext.getString(R.string.alarmRepeatEveryDay);
        } else {
            int[] StrweekId = new int[] {R.string.sunday, R.string.monday, R.string.tuesday, R.string.wednesday, R.string.thursday, R.string.friday, R.string.saturday};
            for(int i=0;i<7;i++) {
                if ((repeat&(0x1<<i)) != 0) {
                    flags[i] = true;
                    if(!remindString.equals(""))
                        remindString += ",";
                    remindString += mContext.getString(StrweekId[i]);
                }
            }
        }
        return remindString;
    }


}
