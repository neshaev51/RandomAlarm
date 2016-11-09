package com.dvnech.randomalarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;



import com.dvnech.randomalarm.alarmreceiver.AlarmReceiver;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by Dmitry on 04.11.2016.
 */
public class WakeupAlarm extends AppCompatActivity {
    PowerManager.WakeLock wl;
    AlarmManager alarmManager;
    long snoozeTime;
    long request_code;
    boolean vibration;
    TextView titleView;
    Timer timer;
    MediaPlayer player;
    int soundPath;
    int soundVolume;
    int repeatTimes;
    long repeatDuration;

    Vibrator vibrator;


    @Override
    protected void onResume() {
        super.onResume();
        if(vibration) {
            long[] pattern = {0, 1000, 1000};
            vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(pattern, 0);
        }
        player = MediaPlayer.create(this,soundPath);
        player.setLooping(true);
        player.setVolume((float)soundVolume/100,(float)soundVolume/100);
        player.start();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        timer = new Timer();
        timer.execute();
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        PowerManager powerManager = (PowerManager)getSystemService(POWER_SERVICE);
        wl = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK,"TAG");
        wl.acquire();
        setContentView(R.layout.wakeup_alarm);

        titleView = (TextView)findViewById(R.id.wakeup_title);

        snoozeTime = getIntent().getLongExtra("SNOOZE", 60000 * 5);
        request_code = getIntent().getLongExtra("REQUEST_CODE", 0);
        vibration = getIntent().getBooleanExtra("VIBRATION", true);
        String title = getIntent().getStringExtra("TITLE");
        soundPath = getIntent().getIntExtra("SOUND_PATH",R.raw.track_1);
        soundVolume = getIntent().getIntExtra("SOUND_VOLUME",100);
        repeatTimes = getIntent().getIntExtra("REPEAT_TIMES",0);
        repeatDuration = getIntent().getLongExtra("REPEAT_DURATION",60000*5);
        titleView.setText(title);

        alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);



    }

    public void onStopAlarm(View v){
        //release activity
        //And set new alarm if has repeats
        Log.i("MY_TAG_REPEAT",repeatTimes + " left");
        if(repeatTimes > 0){
            setRepeatedAlarm();
        }
        timer.cancel(true);
                finish();
    }
    public void onSnooze(View v){
        if(repeatTimes>0)
            setRepeatedAlarm();
        setSnooze();

    }

    private void setSnooze(){
        timer.cancel(true);
        Calendar date = Calendar.getInstance();
        long currentTime = date.getTimeInMillis();
        date.setTimeInMillis(currentTime + snoozeTime);

        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("SNOOZE", snoozeTime);
        intent.putExtra("REQUEST_CODE",request_code);
        intent.putExtra("SOUND_PATH",soundPath);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),(int)request_code+101,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP,date.getTimeInMillis(),pendingIntent);
        Log.i("MY_TAG_SNOOZE_REPEAT", DateFormat.getDateTimeInstance().format(new Date(date.getTimeInMillis())) + " " + snoozeTime);
        Log.i("MY_TAG_WAKEUP_ACTIVITY", "Alarm set id = " + request_code);
        finish();
    }

    private void setRepeatedAlarm(){

        Calendar date = Calendar.getInstance();
        long currentTime = date.getTimeInMillis();
        date.setTimeInMillis(currentTime + repeatDuration);

        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("SNOOZE", snoozeTime);
        intent.putExtra("REQUEST_CODE",request_code);
        intent.putExtra("SOUND_PATH",soundPath);
        intent.putExtra("REPEAT_TIMES",repeatTimes-1);
        intent.putExtra("REPEAT_DURATION",repeatDuration);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),(int)request_code,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP,date.getTimeInMillis(),pendingIntent);
        Log.i("MY_TAG_SNOOZE_REPEAT", DateFormat.getDateTimeInstance().format(new Date(date.getTimeInMillis())) + " " + snoozeTime);
        Log.i("MY_TAG_WAKEUP_ACTIVITY", "Alarm set id = " + request_code);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(vibrator != null)
            vibrator.cancel();

        if(wl.isHeld())
            wl.release();

        player.release();
        player = null;
    }


    private class Timer extends AsyncTask<Void,Void,Integer>{

        @Override
        protected Integer doInBackground(Void... params) {
            try {
                Thread.currentThread();
                Thread.sleep(30000);
                if(isCancelled()){
                    Log.i("MY_TAG_ASYNC_TASK","Cancelled");
                    return 0;
                }
                return 1;

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if(integer == 1){
                Log.i("MY_TAG_ASYNC_TASK","Auto lock screen");
                setSnooze();
            }
        }

    }
}
