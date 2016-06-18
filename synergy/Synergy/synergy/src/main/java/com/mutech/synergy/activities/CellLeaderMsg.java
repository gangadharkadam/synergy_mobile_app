package com.mutech.synergy.activities;

import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.widget.EditText;

import com.mutech.synergy.R;

public class CellLeaderMsg extends ActionBarActivity {

    private EditText edmsg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cell_leader_msg);

        edmsg=(EditText)findViewById(R.id.edmsg);
    }

}
