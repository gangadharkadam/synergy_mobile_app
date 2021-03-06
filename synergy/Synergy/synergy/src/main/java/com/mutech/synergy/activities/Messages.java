package com.mutech.synergy.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListView;

import com.mutech.synergy.R;
import com.mutech.synergy.adapters.MessagesListAdapter;

import java.util.ArrayList;

public class Messages extends ActionBarActivity {

    private ListView lvMsgs;
    private ArrayList<String> mResultList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        lvMsgs=(ListView) findViewById(R.id.lvMsgs);

        mResultList=new ArrayList<>();

        mResultList.add("hello");
        mResultList.add("there");
        mResultList.add("world");

        MessagesListAdapter adapter=new MessagesListAdapter(Messages.this,mResultList);
        lvMsgs.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

}
