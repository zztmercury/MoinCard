package com.lovemoin.card.app.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.lovemoin.card.app.R;

/**
 * Created by zzt on 15-9-23.
 * 活动日历界面
 */
public class ActivityCalendarActivity extends AppCompatActivity {
    private RecyclerView viewCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_calendar);
        initView();

    }

    private void initView() {
        viewCalendar = (RecyclerView) findViewById(R.id.view_calendar);
        viewCalendar.setLayoutManager(new GridLayoutManager(getApplicationContext(), 7));
    }
}
