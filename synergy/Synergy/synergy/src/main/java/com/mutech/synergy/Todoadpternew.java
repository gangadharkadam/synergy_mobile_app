package com.mutech.synergy;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.mutech.synergy.fragments.task.MyTaskFragment;
import com.mutech.synergy.fragments.task.TeamTaskFragment;

public class Todoadpternew extends FragmentPagerAdapter {

	private final int ITEM_COUNT = 1, MY_TASKS = 0;

	private final Integer[] titles = new Integer[] { R.string.screen_my_tasks };
	private Context mContext;

	public Todoadpternew(Context context, FragmentManager fm) {
		super(fm);
		mContext = context;
	}

	@Override
	public Fragment getItem(int position) {
		Fragment screen = null;
		switch (position) {
		case MY_TASKS:
			Log.d("droid", "MyTaskFragment loaded");
			screen = new MyTaskFragment();
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

