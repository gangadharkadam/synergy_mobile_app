package com.mutech.partnershiprecord;

import com.mutech.synergy.R;
import com.mutech.synergy.R.id;
import com.mutech.synergy.R.layout;
import com.mutech.synergy.R.menu;
import com.mutech.synergy.SynergyValues;
import com.mutech.synergy.activities.cellMasters.PartnerShipRecord;
import com.mutech.synergy.utils.PreferenceHelper;
import com.mutech.synergy.widget.SlidingTabLayout;

import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MyPartnershipActivity extends ActionBarActivity {
	
	private ViewPager vpgrHome;
	private SlidingTabLayout mSlidingTabLayout;
	private PreferenceHelper mPreferenceHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_partnership);
		
		getSupportActionBar().hide();

		mPreferenceHelper=new PreferenceHelper(this);

		vpgrHome = (ViewPager) findViewById(R.id.vpgr_home);
		mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
		mSlidingTabLayout.setDistributeEvenly(true);
		
		MyPartnershipAdpter mAdapter = new MyPartnershipAdpter(MyPartnershipActivity.this, getSupportFragmentManager());
		vpgrHome.setAdapter(mAdapter);
		
		mSlidingTabLayout.setViewPager(vpgrHome);
		mSlidingTabLayout.setSelectedIndicatorColors(Color.parseColor("#33B5E5"));
	}

	
}
