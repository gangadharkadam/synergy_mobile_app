package com.mutech.synergy.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.mutech.synergy.R;
import com.mutech.synergy.SynergyValues.Commons;
import com.mutech.synergy.activities.profile.MyProfileActivity;
import com.mutech.synergy.activities.profile.ProfileView;
import com.mutech.synergy.utils.PreferenceHelper;

public class SplashScreenActivity extends Activity{

	private PreferenceHelper mPreferenceHelper;
	String str;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_splash);

		mPreferenceHelper=new PreferenceHelper(this);
		
		 str=mPreferenceHelper.getString(Commons.USER_ROLE);

		Thread screenTimer = new Thread() {
			@Override
			public void run() {
				{
					try {
						int screenTimer = 0;
						while (screenTimer < 5000) {
							sleep(100);
							screenTimer = screenTimer + 100;
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					} finally {
						if(!mPreferenceHelper.getBoolean(Commons.ISUSER_LOGGEDIN))
						{
							Intent intent = new Intent(SplashScreenActivity.this,LoginActivity.class);
							startActivity(intent);
							finish();
						}else
						{
							if(str.equals("Member")){
								Intent intprof=new Intent(SplashScreenActivity.this,ProfileView.class);
								startActivity(intprof);
								finish();
								
							}else{
								Log.d("NonStop", "Going to HomeActivity");
							Intent intent = new Intent(SplashScreenActivity.this,HomeActivity.class);
							startActivity(intent);
							finish();
							}
						}
					}
				}
			}
		};
		screenTimer.start();

	}


}
