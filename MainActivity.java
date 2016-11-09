package com.dvnech.randomalarm;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.dvnech.randomalarm.customAdapter.CustomBaseAdapter;
import com.dvnech.randomalarm.databaseopenhelper.SQLiteHelper;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;


//Входная точка приложения
public class MainActivity extends AppCompatActivity {
    String[] arr = {"Alarm 1","Alarm 2","Alarm 3"};
    CustomBaseAdapter adapter;
    ListView listView;
    Toolbar toolbar;
    SQLiteHelper database;

    @Override
    protected void onResume() {
        super.onResume();
        adapter = new CustomBaseAdapter(this,getAllDataFromDatabase(database),database);
        if(listView != null){
            listView.setAdapter(adapter);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.RED);
        setSupportActionBar(toolbar);

        database = new SQLiteHelper(this);

        listView = (ListView)findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), EditAlarmActivity.class);
                startActivity(intent);
            }
        });

        listView.setAdapter(adapter);
    }

    private ArrayList<AlarmObject> getAllDataFromDatabase(SQLiteHelper database){
        Cursor res = database.getAllData();
        ArrayList<AlarmObject> list = new ArrayList<>();
        if(res.getCount()!=0){



            while(res.moveToNext()){
                AlarmObject alarmObject = new AlarmObject(res.getLong(1),res.getLong(2),res.getInt(3),res.getInt(5));
                alarmObject.setTitle(res.getString(7));
                Log.i("MY_TAG_OUT",res.getString(7) + " title");
                if(res.getInt(6) == 1){
                    alarmObject.setIsAlarmActive(true);
                }else
                    alarmObject.setIsAlarmActive(false);
                list.add(alarmObject);
            }
            return list;
        }
        return list;
    }

    public void addNew(View v){
        Intent intent = new Intent(getApplicationContext(),EditAlarmActivity.class);
        startActivity(intent);
    }

    public void refreshList(){
        adapter = new CustomBaseAdapter(this,getAllDataFromDatabase(database),database);
        if(listView != null){
            listView.setAdapter(adapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent  = new Intent(this,SettingsActivity.class);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }
}
