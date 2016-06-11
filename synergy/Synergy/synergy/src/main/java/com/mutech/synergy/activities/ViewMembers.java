package com.mutech.synergy.activities;

import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListView;

import com.mutech.synergy.R;
import com.mutech.synergy.adapters.MessagesListAdapter;
import com.mutech.synergy.adapters.ViewMemberListAdapter;
import com.mutech.synergy.models.MessagesModel;

import java.util.ArrayList;

public class ViewMembers extends ActionBarActivity {

    private ListView lvViewMembers;
    private ArrayList<String> mResultList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_members);

        lvViewMembers=(ListView) findViewById(R.id.lvViewMembers);

        mResultList=new ArrayList<>();

        mResultList.add("hello");
        mResultList.add("there");
        mResultList.add("world");


        ViewMemberListAdapter adapter=new ViewMemberListAdapter(ViewMembers.this,mResultList);
        lvViewMembers.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

}
