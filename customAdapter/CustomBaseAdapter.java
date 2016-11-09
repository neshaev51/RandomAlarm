package com.dvnech.randomalarm.customAdapter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.dvnech.randomalarm.AlarmObject;
import com.dvnech.randomalarm.MainActivity;
import com.dvnech.randomalarm.R;
import com.dvnech.randomalarm.alarmreceiver.AlarmReceiver;
import com.dvnech.randomalarm.databaseopenhelper.SQLiteHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Dmitry on 31.10.2016.
 */
public class CustomBaseAdapter extends BaseAdapter {

    private ArrayList<AlarmObject> array;
    Context context;
    ViewHolder holder;
    LayoutInflater inflater;
    SQLiteHelper database;
    View view;
    AlarmManager alarmManager;


    public CustomBaseAdapter(Context context, ArrayList<AlarmObject> arr,SQLiteHelper database){
        array = arr;
        this.context = context;
        this.database = database;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

    }
    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public Object getItem(int position) {
        return array.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        view = convertView;


        if (view == null) {
            view = inflater.inflate(R.layout.list_row, parent, false);

            holder = new ViewHolder();
            holder.dateStart = (TextView) view.findViewById(R.id.pick_time_start_row);
            holder.dateEnd = (TextView) view.findViewById(R.id.pick_time_end_row);
            holder.isAlarmActive = (Switch) view.findViewById(R.id.isAlarmActiveSwitchRow);
            holder.removeAlarm = (Button) view.findViewById(R.id.removeAkarmBtn);
            holder.title = (TextView) view.findViewById(R.id.alarmName);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        final AlarmObject alarmObject = array.get(position);

        long startTime = alarmObject.getDateStart();
        long endTime = alarmObject.getDateEnd();

        Calendar dateStart = Calendar.getInstance();
        Calendar dateEnd = Calendar.getInstance();
        dateStart.setTimeInMillis(startTime);
        dateEnd.setTimeInMillis(endTime);

        //Log.i("MY_TAG_ADAPTER", DateFormat.getDateTimeInstance().format(new Date(startTime)) + "   row " + position);
        //Log.i("MY_TAG_ADAPTER", DateFormat.getDateTimeInstance().format(new Date(endTime)) + "   row " +position);

            holder.dateStart.setText(dateStart.get(Calendar.HOUR_OF_DAY) + ":" + dateStart.get(Calendar.MINUTE));
            holder.dateEnd.setText(dateEnd.getTime().getHours() + ":" + dateEnd.getTime().getMinutes());
            holder.isAlarmActive.setChecked(alarmObject.isAlarmActive());
            holder.title.setText(alarmObject.getTitle());

        holder.isAlarmActive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AlarmObject alarm = alarmObject;
                alarm.setIsAlarmActive(isChecked);
                database.updateRow(alarmObject);
                if(isChecked){
                    Intent intent = new Intent(context.getApplicationContext(), AlarmReceiver.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(),(int)alarmObject.getId(),intent,PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.set(AlarmManager.RTC_WAKEUP,alarmObject.getDateStart(),pendingIntent);
                    Log.i("MY_TAG_SWITCH","Alarm set id = " + alarmObject.getId());

                }else{
                    Intent intent = new Intent(context.getApplicationContext(), AlarmReceiver.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(),(int)alarmObject.getId(),intent,PendingIntent.FLAG_UPDATE_CURRENT);
                    pendingIntent.cancel();
                    Log.i("MY_TAG_SWITCH", "Alarm canceled id = " + alarmObject.getId());
                }
            }
        });
        holder.removeAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getApplicationContext(), AlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(),(int)alarmObject.getId(),intent,PendingIntent.FLAG_UPDATE_CURRENT);
                pendingIntent.cancel();

                database.removeRow(alarmObject.getId());
                array.remove(position);
                notifyDataSetChanged();

            }
        });
        return view;
    }


    private static class ViewHolder{
        public TextView title;
        public TextView dateStart;
        public TextView dateEnd;
        public Button removeAlarm;
        public Switch isAlarmActive;
        public TextView id;
    }
}
