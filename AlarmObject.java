package com.dvnech.randomalarm;

/**
 * Created by Dmitry on 31.10.2016.
 */
public class AlarmObject  {
    private String title;
    private long dateStart;
    private long dateEnd;
    private long dateReal;
    private int repeat;
    private long repeatTime;
    private long snooze;
    private int soundVolume;
    private boolean isAlarmActive;
    private final long id;
    private boolean isVibrationActive;
    private int soundPath;




    //Добавить long по дням недели

    public int getSoundPath() {
        return soundPath;
    }

    public void setSoundPath(int sound_path) {
        this.soundPath = sound_path;
    }

    public AlarmObject(long start, long end,int repeat,int soundVolume){
        this.dateStart = start;
        this.dateEnd = end;
        this.repeat = repeat;
        this.soundVolume = soundVolume;

        id = start-end;

    }

    public long getRepeatTime() {
        return repeatTime;
    }

    public void setRepeatTime(long repeatTime) {
        this.repeatTime = repeatTime;
    }
    public boolean isVibrationActive() {
        return isVibrationActive;
    }

    public void setIsVibrationActive(boolean isVibrationActive) {
        this.isVibrationActive = isVibrationActive;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getDateStart() {
        return dateStart;
    }

    public void setDateStart(long dateStart) {
        this.dateStart = dateStart;
    }

    public long getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(long dateEnd) {
        this.dateEnd = dateEnd;
    }

    public long getDateReal() {
        return dateReal;
    }

    public void setDateReal(long dateReal) {
        this.dateReal = dateReal;
    }

    public int getRepeat() {
        return repeat;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }

    public long getSnooze() {
        return snooze;
    }

    public void setSnooze(long snooze) {
        this.snooze = snooze;
    }

    public int getSoundVolume() {
        return soundVolume;
    }

    public void setSoundVolume(int soundVolume) {
        this.soundVolume = soundVolume;
    }

    public boolean isAlarmActive() {
        return isAlarmActive;
    }

    public void setIsAlarmActive(boolean isAlarmActive) {
        this.isAlarmActive = isAlarmActive;
    }

    public long getId(){
        return id;
    }
}
