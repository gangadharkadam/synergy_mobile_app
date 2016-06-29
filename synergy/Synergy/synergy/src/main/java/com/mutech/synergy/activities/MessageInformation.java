package com.mutech.synergy.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

import com.mutech.synergy.R;

public class MessageInformation extends ActionBarActivity {

    private TextView txtTo, txtFrom, txtMessage, txtDateTime, txtName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_information);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar);

        txtTo=(TextView) findViewById(R.id.txtTo);
        txtFrom=(TextView) findViewById(R.id.txtFrom);
        txtMessage = (TextView) findViewById(R.id.txtMessage);
        txtDateTime = (TextView) findViewById(R.id.txtDateTime);
        txtName = (TextView) findViewById(R.id.txtName);


       if(getIntent().getStringExtra("to_user")!= null);
        {
            txtTo.setText(getIntent().getStringExtra("to_user"));
        }

        if(getIntent().getStringExtra("Message")!= null);
        {
            txtMessage.setText(getIntent().getStringExtra("Message"));
        }

        if(getIntent().getStringExtra("datetime")!= null);
        {
            txtDateTime.setText(getIntent().getStringExtra("datetime"));
        }

        if(getIntent().getStringExtra("name")!= null);
        {
            txtName.setText(getIntent().getStringExtra("name"));
        }

        if(getIntent().getStringExtra("from_user")!= null);
        {
            txtFrom.setText(getIntent().getStringExtra("from_user"));
        }
    }

}
