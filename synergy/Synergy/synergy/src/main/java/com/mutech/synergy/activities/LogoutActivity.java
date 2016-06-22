package com.mutech.synergy.activities;

import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;


import com.mutech.synergy.R;
import com.mutech.synergy.SynergyValues.Commons;
import com.mutech.synergy.activities.cellMasters.MasterSelectorScreenActivity;
import com.mutech.synergy.activities.cellMasters.PartnerShipRecord;
import com.mutech.synergy.activities.cellMasters.SearchFunctionActivity;
import com.mutech.synergy.activities.event.EventListActivity;
import com.mutech.synergy.activities.meeting.MyMeetingListActivity;
import com.mutech.synergy.activities.profile.MyProfileActivity;
import com.mutech.synergy.activities.task.ToDoTaskActivity;
import com.mutech.synergy.utils.PreferenceHelper;


public class LogoutActivity extends Activity{
	
	private Button yesbtn,nobtn;
	private PreferenceHelper mPreferenceHelper;
	
	String classname;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_logout);
		
		classname=getIntent().getStringExtra("classname");
		
		yesbtn = (Button) findViewById(R.id.button2);
		nobtn = (Button) findViewById(R.id.button1);
		
		mPreferenceHelper=new PreferenceHelper(this);
		
		yesbtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mPreferenceHelper.addBoolean(Commons.ISUSER_LOGGEDIN, false);
				mPreferenceHelper.addString(Commons.USER_EMAILID, null);
				mPreferenceHelper.addString(Commons.USER_PASSWORD, null);

		            
				//Intent intLogout=new Intent(getApplicationContext(),LoginActivity.class);
				Intent i = new Intent(getApplicationContext(), LoginActivity.class);
				startActivity(i);
				finish();
				
			
			
			}
		});
		
		nobtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stu
				
				
				
				if(classname.equals("MyProfileActivity"))
					startActivity(new Intent(LogoutActivity.this, MyProfileActivity.class));
				
				if(classname.equals("MasterSelectorScreenActivity"))
					startActivity(new Intent(LogoutActivity.this, MasterSelectorScreenActivity.class));
				
				if(classname.equals("PartnerShipRecord"))
					startActivity(new Intent(LogoutActivity.this,PartnerShipRecord.class));
				
				if(classname.equals("SearchFunctionActivity"))
					startActivity(new Intent(LogoutActivity.this,SearchFunctionActivity.class));
				
				if(classname.equals("EventListActivity"))
					startActivity(new Intent(LogoutActivity.this,EventListActivity.class));
				
				if(classname.equals("MyMeetingListActivity"))
					startActivity(new Intent(LogoutActivity.this,MyMeetingListActivity.class));
				
				if(classname.equals("ToDoTaskActivity"))
					startActivity(new Intent(LogoutActivity.this,ToDoTaskActivity.class));

				if(classname.equals("HomeActivity"))
					startActivity(new Intent(LogoutActivity.this,HomeActivity.class));
				
				finish();
				
			}
		});
		
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		
		if(classname.equals("MyProfileActivity"))
			startActivity(new Intent(LogoutActivity.this, MyProfileActivity.class));
		
		if(classname.equals("MasterSelectorScreenActivity"))
			startActivity(new Intent(LogoutActivity.this, MasterSelectorScreenActivity.class));
		
		if(classname.equals("PartnerShipRecord"))
			startActivity(new Intent(LogoutActivity.this,PartnerShipRecord.class));
		
		if(classname.equals("SearchFunctionActivity"))
			startActivity(new Intent(LogoutActivity.this,SearchFunctionActivity.class));
		
		if(classname.equals("EventListActivity"))
			startActivity(new Intent(LogoutActivity.this,EventListActivity.class));
		
		if(classname.equals("MyMeetingListActivity"))
			startActivity(new Intent(LogoutActivity.this,MyMeetingListActivity.class));
		
		
		if(classname.equals("ToDoTaskActivity"))
			startActivity(new Intent(LogoutActivity.this,ToDoTaskActivity.class));

		if(classname.equals("Feedback"))
			startActivity(new Intent(LogoutActivity.this,Feedback.class));

		finish();

	}
}
