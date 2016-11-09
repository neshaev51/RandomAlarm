package com.dvnech.randomalarm.alarmreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.dvnech.randomalarm.R;
import com.dvnech.randomalarm.WakeupAlarm;

/**
 * Created by Dmitry on 04.11.2016.
 */
public class AlarmReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {

        long snooze = intent.getLongExtra("SNOOZE", 60000 * 5);
        long request_code = intent.getLongExtra("REQUEST_CODE", 0);
        boolean vibration = intent.getBooleanExtra("VIBRATION", true);
        String title = intent.getStringExtra("TITLE");
        int soundPath = intent.getIntExtra("SOUND_PATH",R.raw.track_1);
        int soundVolume = intent.getIntExtra("SOUND_VOLUME",100);
        int repeatTimes = intent.getIntExtra("REPEAT_TIMES",0);
        long repeatDuration = intent.getLongExtra("REPEAT_DURATION",60000*5);

        Log.i("MY_TAG_RECEIVER", "Receiver works");
        Intent alarmIntent = new Intent(context.getApplicationContext(), WakeupAlarm.class);
        alarmIntent.putExtra("SNOOZE",snooze);
        alarmIntent.putExtra("REQUEST_CODE", request_code);
        alarmIntent.putExtra("VIBRATION", vibration);
        alarmIntent.putExtra("TITLE",title);
        alarmIntent.putExtra("SOUND_PATH",soundPath);
        alarmIntent.putExtra("SOUND_VOLUME",soundVolume);
        alarmIntent.putExtra("REPEAT_TIMES",repeatTimes);
        alarmIntent.putExtra("REPEAT_DURATION",repeatDuration);
        alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(alarmIntent);
    }
}
