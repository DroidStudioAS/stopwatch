package com.aa.stopwatch;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.SystemClock;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    TextView timer;
    ImageView start, pause,
            reset, lap;
    int startImage, pauseImage,
            clickCount;
    long MillisecondTime, StartTime,
            TimeBuff, UpdateTime = 0L;
    Handler handler;
    int Seconds, Minutes, MilliSeconds;
    ListView lapContainer;
    ArrayList<String> lapList;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timer = (TextView) findViewById(R.id.tvTimer);
        start = (ImageView) findViewById(R.id.btStart);
        reset = (ImageView) findViewById(R.id.btReset);
        lap = (ImageView) findViewById(R.id.btFlag);
        lapContainer = (ListView) findViewById(R.id.lapContainer);
        lapList = new ArrayList<>();
        startImage = R.drawable.start_button_noback;
        pauseImage = R.drawable.pause_button_noback;
        clickCount = 0;

        handler = new Handler();

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickCount % 2 == 0 || clickCount == 0) {
                    StartTime = SystemClock.uptimeMillis();
                    handler.postDelayed(runnable, 0);
                    reset.setEnabled(false);
                    reset.setVisibility(View.INVISIBLE);
                    setImage(pauseImage);
                } else if (clickCount % 2 == 1) {
                    TimeBuff += MillisecondTime;
                    handler.removeCallbacks(runnable);
                    reset.setEnabled(true);
                    reset.setVisibility(View.VISIBLE);
                    setImage(startImage);
                }
                clickCount++;
            }
        });


        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MillisecondTime = 0L;
                StartTime = 0L;
                TimeBuff = 0L;
                UpdateTime = 0L;
                Seconds = 0;
                Minutes = 0;
                MilliSeconds = 0;

                lapList.clear();

                timer.setText("00:00:00");

            }
        });
        lap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String lapTime = timer.getText().toString();
                if (!lapList.contains(lapTime)) {
                    lapList.add(lapTime);
                }
                ArrayAdapter<String> items = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, lapList);
                lapContainer.setAdapter(items);

            }
        });


    }

    public void setImage(int drawableID) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                start.setImageResource(drawableID);
            }
        });
    }

    public Runnable runnable = new Runnable() {

        public void run() {

            MillisecondTime = SystemClock.uptimeMillis() - StartTime;

            UpdateTime = TimeBuff + MillisecondTime;

            Seconds = (int) (UpdateTime / 1000);

            Minutes = Seconds / 60;

            Seconds = Seconds % 60;

            MilliSeconds = (int) (UpdateTime % 1000);

            timer.setText("" + Minutes + ":"
                    + String.format("%02d", Seconds) + ":"
                    + String.format("%03d", MilliSeconds));

            handler.postDelayed(this, 0);
        }

    };

}