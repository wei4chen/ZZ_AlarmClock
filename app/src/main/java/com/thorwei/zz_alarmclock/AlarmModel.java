package com.thorwei.zz_alarmclock;

public class AlarmModel {
    public int id;
    public boolean enable;
    public int hour, minute;
    public int repeat;
    public String tag;
    public int ringPosition;
    public String ring;
    public boolean vibrate;
    
//    public boolean sunday, monday, tuesday, wednesday, thursday, friday, saturday;
//    public int volume;
//    public int remind;
//    public boolean weather;

    public AlarmModel(int id, AlarmClockBuilder builder) {
        this.id = id;
        this.enable = builder.enable;
        this.hour = builder.hour;
        this.minute = builder.minute;
        this.repeat = builder.repeat;
        this.tag = builder.tag;
        this.ringPosition = builder.ringPosition;
        this.ring = builder.ring;
        this.vibrate = builder.vibrate;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }
    
    public void setTag(String tah) {
        this.tag = tag;
    }

    public void setRingPosition(int ringPosition) {
        this.ringPosition = ringPosition;
    }

    public void setRing(String ring) {
        this.ring = ring;
    }

    public void setVibrate(boolean vibrate) {
        this.vibrate = vibrate;
    }

}
