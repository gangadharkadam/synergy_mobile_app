package com.mutech.synergy.activities;

import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListView;

import com.mutech.synergy.R;
import com.mutech.synergy.adapters.MessagesListAdapter;

import java.util.ArrayList;

public class MessageLogs extends ActionBarActivity {

    private ListView lvMsgs;
    private ArrayList<String> mResultList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_logs);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar);

        lvMsgs=(ListView) findViewById(R.id.lvviewMsgLogs);

        mResultList=new ArrayList<>();

        mResultList.add("hello");
        mResultList.add("there");
        mResultList.add("world");

        MessagesListAdapter adapter=new MessagesListAdapter(MessageLogs.this,mResultList);
        lvMsgs.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

}
