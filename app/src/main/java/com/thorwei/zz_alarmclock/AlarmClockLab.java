package com.thorwei.zz_alarmclock;

public class AlarmClockLab extends AlarmModel{

    private static AlarmClockLab clockLab = null;

	public AlarmClockLab(int id, AlarmClockBuilder builder) {
		super(id, builder);
		// TODO Auto-generated constructor stub
	}

    public static AlarmClockLab getClockLab(int id, AlarmClockBuilder builder) {
        if (clockLab == null) {
            synchronized (AlarmClockLab.class) {
                if (clockLab == null) {
                    clockLab = new AlarmClockLab(id, builder);
                }
            }
        }
        return clockLab;
    }

    public void setId(int id) {
        this.id = id;
    }
}
