package com.dvnech.randomalarm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.ListView;

import com.dvnech.randomalarm.R;

/**
 * Created by Dmitry on 09.11.2016.
 */
public class RepeatSettings extends AppCompatActivity {

    ListView repeatArr;
    ListView durationArr;
    final long ONE_MINUTE = 60000;

    int resultRepeat = 0;
    long resultRepeatDuration = ONE_MINUTE*5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub_layout_set_repeat_settings);

        repeatArr = (ListView)findViewById(R.id.repeat_list);
        durationArr = (ListView)findViewById(R.id.repeat_duration_list);

        final String[] repeatTxt = {"1 time","2 times","3 times","5 times","10 times"};
        final int[] repeatTimes = {1,2,3,5,10};


        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_single_choice,repeatTxt);
        repeatArr.setAdapter(adapter);
        repeatArr.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        repeatArr.setSelection(2);
        repeatArr.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                resultRepeat = repeatTimes[position];
                repeatArr.setItemChecked(position,true);
            }
        });

        final String[] repeatDurationTxt= {"1 min","3 min","5 min","10 min","20 min","30 min"};
        final long[] repeatDurationTime = {ONE_MINUTE,ONE_MINUTE*3,ONE_MINUTE*5,ONE_MINUTE*10,ONE_MINUTE*20,ONE_MINUTE*30};

        ArrayAdapter adapter1 = new ArrayAdapter(this,android.R.layout.simple_list_item_single_choice,repeatDurationTxt);
        durationArr.setAdapter(adapter1);
        durationArr.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        durationArr.setSelection(2);
        durationArr.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                resultRepeatDuration = repeatDurationTime[position];
                durationArr.setItemChecked(position,true);
            }
        });
    }

    public void setResult(View v){
        Intent resultIntent = new Intent();
        resultIntent.putExtra("REPEATS",resultRepeat);
        resultIntent.putExtra("DURATION",resultRepeatDuration);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

}

