package com.mutech.synergy.adapters;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mutech.synergy.R;
import com.mutech.synergy.models.DrawerItem;

public class CustomDrawerAdapter extends ArrayAdapter<DrawerItem>{

	private Context mContext;
	private List<DrawerItem> mDrawerItemList;

	public CustomDrawerAdapter(Context context,int resource, List<DrawerItem> listItems) {
		super(context, resource,listItems);
		mContext=context;
		mDrawerItemList=new ArrayList<DrawerItem>();
		mDrawerItemList=listItems;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder mHolder;
		LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        mHolder = new Holder();
        if (convertView == null) {
              convertView = inflater.inflate(R.layout.custom_dashboard_drawer_item, parent, false);
              mHolder.itemName = (TextView) convertView
                          .findViewById(R.id.drawer_itemName);
              mHolder.icon = (ImageView) convertView.findViewById(R.id.drawer_icon);

              convertView.setTag(mHolder);
        } else {
              mHolder = (Holder) convertView.getTag();
        }
        mHolder.icon.setImageDrawable(convertView.getResources().getDrawable(
        		mDrawerItemList.get(position).getImgResID()));
        mHolder.itemName.setText(mDrawerItemList.get(position).getItemName());

        return convertView;
	}
	
	private static class Holder {
        TextView itemName;
        ImageView icon;
  }

}
