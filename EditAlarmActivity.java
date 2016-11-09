package com.dvnech.randomalarm;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.dvnech.randomalarm.alarmreceiver.AlarmReceiver;
import com.dvnech.randomalarm.databaseopenhelper.SQLiteHelper;

import java.util.Calendar;

/**
 * Created by Dmitry on 31.10.2016.
 */
//Активити для настройки будильника
public class EditAlarmActivity extends AppCompatActivity {
    Toolbar toolbar;

    Switch isAlarmActive;
    TextView startTime;
    TextView endTime;
    FrameLayout repeatRow;
    FrameLayout snoozeRow;
    FrameLayout soundRow;
    EditText titleEdit;
    Switch vibration;
    SeekBar volumeSound;

    TextView repeatShow;
    TextView snoozeShow;

    AlarmManager alarmManager;

    long startAlarm;
    long endAlarm;
    int repeatTimes = 1;
    long repeatDuration = 60000*5;
    long snoozeTimeInMills = 60000;
    int soundPath = R.raw.track_1;

    SQLiteHelper database;

    Vibrator vibrator;

    MediaPlayer player ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_alarm);

        toolbar = (Toolbar)findViewById(R.id.edit_alarm_toolbar);
        toolbar.setBackgroundColor(Color.RED);
        setSupportActionBar(toolbar);

        database = new SQLiteHelper(this);
        alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);


        startTime = (TextView)findViewById(R.id.pick_time_start);
        endTime = (TextView)findViewById(R.id.pick_time_end);
        repeatRow = (FrameLayout)findViewById(R.id.repeat_row);
        snoozeRow = (FrameLayout)findViewById(R.id.snooze_row);
        titleEdit = (EditText)findViewById(R.id.title_edit);
        vibration = (Switch)findViewById(R.id.vibrationSwitch);
        volumeSound = (SeekBar)findViewById(R.id.volumeSeekBar);

        repeatShow = (TextView)findViewById(R.id.repeatShow);
        snoozeShow = (TextView)findViewById(R.id.snoozeShow);
        soundRow = (FrameLayout)findViewById(R.id.sound_row);

        vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);


        //Сейчас самая скучная часть активити настройка кликов на все элементы
        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar currentTime = Calendar.getInstance();
                int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                int minute = currentTime.get(Calendar.MINUTE);

                TimePickerDialog dialog = new TimePickerDialog(EditAlarmActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Calendar date = Calendar.getInstance();
                        date.set(currentTime.get(Calendar.YEAR), currentTime.get(Calendar.MONTH), currentTime.get(Calendar.DAY_OF_MONTH), hourOfDay, minute,0);
                        if(date.getTimeInMillis()<currentTime.getTimeInMillis()){
                            date.set(currentTime.get(Calendar.YEAR), currentTime.get(Calendar.MONTH), currentTime.get(Calendar.DAY_OF_MONTH)+1, hourOfDay, minute,0);
                        }
                        startAlarm = date.getTimeInMillis();
                        startTime.setText(hourOfDay+":"+minute);
                    }
                },hour,minute,true);
                dialog.show();
            }
        });
        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar currentTime = Calendar.getInstance();
                int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                int minute = currentTime.get(Calendar.MINUTE);

                TimePickerDialog dialog = new TimePickerDialog(EditAlarmActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Calendar date = Calendar.getInstance();
                        date.set(currentTime.get(Calendar.YEAR), currentTime.get(Calendar.MONTH), currentTime.get(Calendar.DAY_OF_MONTH), hourOfDay, minute,0);
                        if(date.getTimeInMillis()<currentTime.getTimeInMillis()){
                            date.set(currentTime.get(Calendar.YEAR), currentTime.get(Calendar.MONTH), currentTime.get(Calendar.DAY_OF_MONTH)+1, hourOfDay, minute,0);
                        }
                        endAlarm = date.getTimeInMillis();

                        endTime.setText(hourOfDay+":"+minute);
                    }
                },hour,minute,true);
                dialog.show();
            }
        });
        repeatRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditAlarmActivity.this,RepeatSettings.class);
                startActivityForResult(intent,1);
            }
        });
        snoozeRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int oneMinute = 60000; //One minute in millis
                final String[] snoozeArr = {"1 min", "3 minutes", "5 minutes", "10 minutes"};
                final int[] snoozeArrTime = {oneMinute,oneMinute*3,oneMinute*5,oneMinute*10};
                AlertDialog.Builder builder = new AlertDialog.Builder(EditAlarmActivity.this);
                builder.setSingleChoiceItems(snoozeArr, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        snoozeShow.setText(snoozeArr[which]);
                        snoozeTimeInMills = snoozeArrTime[which];
                        dialog.dismiss();
                    }
                });
                builder.setTitle("Repeat");
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        soundRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] soundArr = {"track 1","track 2","track_3"};
                AlertDialog.Builder builder = new AlertDialog.Builder(EditAlarmActivity.this);
                builder.setSingleChoiceItems(soundArr, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0: {
                                soundPath = R.raw.track_1;
                                break;
                            }
                            case 1: {
                                soundPath = R.raw.track_2;
                                break;
                            }
                            case 2: {
                                soundPath = R.raw.track_3;
                                break;
                            }
                        }
                    }
                });
                builder.setTitle("Pick a song");

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        volumeSound.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.i("MY_TAG_SEEK_BAR", "onProgressChanged");
                if(progress == 0){
                    vibrator.vibrate(500);
                }
               /* if(player!= null & player.isPlaying()){
                    player.setVolume(progress/100,progress/100);
                }*/
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.i("MY_TAG_SEEK_BAR", "onStartTrackingTouch");
                if(player != null)
                {
                    player.release();
                    player = null;
                }
                player = MediaPlayer.create(EditAlarmActivity.this,soundPath);
                player.start();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.i("MY_TAG_SEEK_BAR", "onStopTrackingTouch");
                if(player != null){
                    player.release();
                    player = null;
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            repeatTimes = data.getIntExtra("REPEATS",0);
            repeatDuration = data.getLongExtra("DURATION", 60000 * 5);
            repeatShow.setText(repeatTimes + " times, " + repeatDuration/60000 + " minutes");

        }
    }

    public void onCreateAlarm(View v){



        AlarmObject alarmObject = new AlarmObject(startAlarm,endAlarm,repeatTimes,volumeSound.getProgress());
        alarmObject.setSnooze(snoozeTimeInMills);
        alarmObject.setRepeat(repeatTimes);
        alarmObject.setRepeatTime(repeatDuration);
        alarmObject.setIsAlarmActive(true);
        alarmObject.setIsVibrationActive(vibration.isChecked());
        alarmObject.setSoundPath(soundPath);

        Log.i("MY_TAG_ALARM_OBJECT", "getSnooze " + alarmObject.getSnooze());
        Log.i("MY_TAG_ALARM_OBJECT", "getRepeat " + alarmObject.getRepeat());
        Log.i("MY_TAG_ALARM_OBJECT", "getRepeatTime " + alarmObject.getRepeatTime());
        Log.i("MY_TAG_ALARM_OBJECT", "isAlarmActive " + alarmObject.isAlarmActive());
        Log.i("MY_TAG_ALARM_OBJECT", "isVibrationActive " + alarmObject.isVibrationActive());

        if(titleEdit.getText().toString().length() == 0){
            String title = "Alarm " + database.getCountOfRows()+1;
            Log.i("MY_TAG_IN_A", title);
                     alarmObject.setTitle(title);
        }else{
            String title = titleEdit.getText().toString();
            Log.i("MY_TAG_IN_A", title);
            alarmObject.setTitle(title);
        }

        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("SNOOZE",snoozeTimeInMills);
        intent.putExtra("REQUEST_CODE",alarmObject.getId());
        intent.putExtra("VIBRATION",vibration.isChecked());
        intent.putExtra("TITLE",alarmObject.getTitle());
        intent.putExtra("SOUND_PATH",alarmObject.getSoundPath());
        intent.putExtra("SOUND_VOLUME",alarmObject.getSoundVolume());
        intent.putExtra("REPEAT_TIMES",alarmObject.getRepeat());
        intent.putExtra("REPEAT_DURATION",alarmObject.getRepeatTime());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),(int)alarmObject.getId(),intent,PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP,startAlarm,pendingIntent);


        boolean isAdded = database.addData(alarmObject);


        if(isAdded){
            Toast.makeText(this,"added",Toast.LENGTH_LONG).show();
            this.finish();
        }else
            Toast.makeText(this,"error while adding",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(vibrator != null)
            vibrator.cancel();
    }
}
