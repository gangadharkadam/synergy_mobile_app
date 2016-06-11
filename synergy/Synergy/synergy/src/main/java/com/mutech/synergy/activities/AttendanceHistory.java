package com.mutech.synergy.activities;

import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.ActionBarActivity;

import com.mutech.synergy.R;

public class AttendanceHistory extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_history);

       /* getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.actiontop);*/
    }

}
