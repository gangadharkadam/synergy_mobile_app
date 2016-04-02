package com.mutech.synergy.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mutech.synergy.R;
import com.mutech.synergy.models.MeetingModel.MeetingListModel;

public class MastersListAdapter extends BaseAdapter{

	private Context mContext;
	private ArrayList<MeetingListModel> mList;

	public MastersListAdapter(Context context,
			ArrayList<MeetingListModel> mResultList) {
		mContext=context;
		mList=new ArrayList<MeetingListModel>();
		mList=mResultList;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder mHolder;
		LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
		mHolder = new Holder();
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.row_masterlist, parent, false);
			mHolder.txtMasterName = (TextView) convertView.findViewById(R.id.txtMasterName);
			mHolder.txtMasterName1 = (TextView) convertView.findViewById(R.id.txtMasterName1);
			
			mHolder.txtuserid = (TextView) convertView.findViewById(R.id.txtuserid);
			mHolder.txtid = (TextView) convertView.findViewById(R.id.txtid);
			mHolder.txtMasterName1 .setVisibility(View.VISIBLE);
			
			mHolder.txtuserid.setVisibility(View.GONE);
			mHolder.txtid.setVisibility(View.GONE);

			convertView.setTag(mHolder);
		} else {
			mHolder = (Holder) convertView.getTag();
		}
		mHolder.txtMasterName.setText(mList.get(position).getName());
		mHolder.txtMasterName1.setText(mList.get(position).getRecord_name());
	
		
		return convertView;
	}

	static class Holder{
		private TextView txtMasterName,txtMasterName1,txtuserid,txtid;
	}

}
