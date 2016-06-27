package com.mutech.partnershiprecord;

import com.mutech.synergy.R;
import com.mutech.synergy.R.id;
import com.mutech.synergy.R.layout;
import com.mutech.synergy.R.menu;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MyPartnershipAdpter extends FragmentPagerAdapter {
	
	private final int ITEM_COUNT = 2, MY_TASKS = 0, TEAM_TASKS = 1;

	private final Integer[] titles = new Integer[] { R.string.screen_giving,
			R.string.screen_pledge };
	private Context mContext;
	
	public MyPartnershipAdpter(Context context, FragmentManager fm) {
		super(fm);
		mContext = context;
	}

	@Override
	public Fragment getItem(int position) {
		Fragment screen = null;
		switch (position) {
		case MY_TASKS:
			Log.d("droid", "MyTaskFragment loaded");
			screen = new MyPartnershipGivingActivity();
			break;

		case TEAM_TASKS:
			Log.d("droid", "TeamTaskFragment loaded");
			screen = new MypartnershipPledgeActivity();
			break;
		}
		return screen;
	}

	@Override
	public int getCount() {
		return ITEM_COUNT;
	}

	@Override
	public CharSequence getPageTitle(int position) {

		String title=mContext.getString(titles[position]);

//		if(position==0)
//		{
//			title=title +"\n Total-" + " " +"100";
//		}
//		else {
//			title=title +"\n Total-" + " " +"10";
//		}
		return title;
	}
	
}
