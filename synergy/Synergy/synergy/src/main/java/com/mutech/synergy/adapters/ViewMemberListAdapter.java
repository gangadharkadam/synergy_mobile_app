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
import com.mutech.synergy.models.MessagesModel;

public class ViewMemberListAdapter extends BaseAdapter{

    private Context mContext;
    private ArrayList<String> mList;

    public ViewMemberListAdapter(Context context,
                               ArrayList<String> mResultList) {
        mContext=context;
        mList= new ArrayList<>();
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
            convertView = inflater.inflate(R.layout.row_view_members, parent, false);
            mHolder.txtMsg = (TextView) convertView.findViewById(R.id.txtMsg);
            mHolder.txtDate = (TextView) convertView.findViewById(R.id.txtDate);

            mHolder.txtMsg .setVisibility(View.VISIBLE);

            convertView.setTag(mHolder);
        } else {
            mHolder = (Holder) convertView.getTag();
        }
        return convertView;
    }

    static class Holder{
        private TextView txtDate,txtMsg;
    }

}
