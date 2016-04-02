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


public class MasterSpinnerAdpter extends BaseAdapter{

	private Context mContext;
	private ArrayList<String> mList;

	public MasterSpinnerAdpter(Context context,	ArrayList<String> mResultList) {
		mContext=context;
		mList=new ArrayList<String>();
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
			convertView = inflater.inflate(R.layout.spinner_row, parent, false);
			mHolder.txtMasterName = (TextView) convertView.findViewById(R.id.txtid);
			mHolder.txtMasterName1 = (TextView) convertView.findViewById(R.id.txtname);
			
			mHolder.txtuserid = (TextView) convertView.findViewById(R.id.txtemail);
			
			mHolder.txtMasterName1 .setVisibility(View.GONE);
			
			mHolder.txtuserid.setVisibility(View.GONE);
			

			convertView.setTag(mHolder);
		} else {
			mHolder = (Holder) convertView.getTag();
		}
		mHolder.txtMasterName.setText(mList.get(position));
		
	
		
		return convertView;
	}

	static class Holder{
		private TextView txtMasterName,txtMasterName1,txtuserid;
	}
}