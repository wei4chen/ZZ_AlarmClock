package com.thorwei.zz_alarmclock;

public class AlarmClockBuilder {
    public boolean enable;
    public int hour, minute;
    public int repeat;
    public String tag;
    public int ringPosition;
    public String ring;
    public boolean vibrate;

    public AlarmClockBuilder() {
    }

    public AlarmClockBuilder enable(boolean enable) {
        this.enable = enable;
        return this;
    }

    public AlarmClockBuilder hour(int hour) {
        this.hour = hour;
        return this;
    }

    public AlarmClockBuilder minute(int minute) {
        this.minute = minute;
        return this;
    }

    public AlarmClockBuilder repeat(int repeat) {
        this.repeat = repeat;
        return this;
    }


    public AlarmClockBuilder tag(String tag) {
        this.tag = tag;
        return this;
    }

    public AlarmClockBuilder ringPosition(int ringPosition) {
        this.ringPosition = ringPosition;
        return this;
    }

    public AlarmClockBuilder ring(String ring) {
        this.ring = ring;
        return this;
    }

    public AlarmClockBuilder vibrate(boolean vibrate) {
        this.vibrate = vibrate;
        return this;
    }

    public AlarmModel builder(int id) {
        return new AlarmModel(id, this);
    }

    public AlarmClockLab builderLab(int id) {
        return AlarmClockLab.getClockLab(id, this);
    }
}
