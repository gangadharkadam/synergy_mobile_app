package com.mutech.synergy.activities;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;

import com.mutech.synergy.R;

public class MinistryMaterials extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ministry_materials);


        getSupportActionBar().setCustomView(R.layout.custom_actionbar);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2E9AFE")));
        getSupportActionBar().setTitle("Ministry Materials");
    }

}
