package com.mutech.partnershiprecord;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.mutech.synergy.R;
import com.mutech.synergy.fragments.task.TeamTaskFragment;

public class PartnershipAdpter extends FragmentPagerAdapter {

	private final int ITEM_COUNT = 2, MY_TASKS = 0, TEAM_TASKS = 1;

	private final Integer[] titles = new Integer[] { R.string.screen_giving,
			R.string.screen_pledge };
	private Context mContext;

	public PartnershipAdpter(Context context, FragmentManager fm) {
		super(fm);
		mContext = context;
	}

	@Override
	public Fragment getItem(int position) {
		Fragment screen = null;
		switch (position) {
		case MY_TASKS:
			Log.d("droid", "MyTaskFragment loaded");
			screen = new GivingActivity();
			break;

		case TEAM_TASKS:
			Log.d("droid", "TeamTaskFragment loaded");
			screen = new PledgeActivity();
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
		return mContext.getString(titles[position]);
	}
}