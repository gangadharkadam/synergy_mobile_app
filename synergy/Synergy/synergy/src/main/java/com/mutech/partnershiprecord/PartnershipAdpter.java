package com.mutech.partnershiprecord;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.mutech.synergy.R;

public class PartnershipAdpter extends FragmentPagerAdapter {

	private final int ITEM_COUNT = 2, MY_TASKS = 0, TEAM_TASKS = 1;

	private final Integer[] titles = new Integer[] { R.string.screen_giving,
			R.string.screen_pledge };
	private Context mContext;
    private static String totalValGiving;
    public static String totalValPrice;

//	private LayoutInflater li;

	public PartnershipAdpter(Context context, FragmentManager fm) {
		super(fm);
		mContext = context;
	}

    public static String getTotalValGiving() {
        return totalValGiving;
    }

    public static void setTotalValGiving(String totalValGiving) {
        PartnershipAdpter.totalValGiving = totalValGiving;
    }

/*    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        TotalViewHolder viewHolder;
        if (convertView == null) {
            li = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.custom_tablayout, null);
            viewHolder = new TotalViewHolder(v);
            v.setTag(viewHolder);
        } else {
            viewHolder = (TotalViewHolder) v.getTag();
        }
        viewHolder.total.setText("Total");
        viewHolder.count.setText("100");

        return v;
    }*/

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
        String title=mContext.getString(titles[position]);
//        if(position==0)
//        {
//			title=title +"\n Total-" + " " + getTotalValGiving();
//        }
//        else {
//            title=title +"\n Total-" + " " + totalValPrice;
//        }
		return title;
	}



/*	class TotalViewHolder
	{
		TextView total;
		TextView count;

		public TotalViewHolder(View base) {
			total = (TextView) base.findViewById(R.id.texttotal);
			count = (TextView) base.findViewById(R.id.textcount);

		}
	}*/
}