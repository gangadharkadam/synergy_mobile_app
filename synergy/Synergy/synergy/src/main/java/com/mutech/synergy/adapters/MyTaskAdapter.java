package com.mutech.synergy.adapters;

import java.util.ArrayList;
import java.util.List;

import com.mutech.synergy.R;
import com.mutech.synergy.models.MyTasksResponseModel.Message;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class MyTaskAdapter extends ArrayAdapter<Message> {

	private Context mContext;
	private ArrayList<Message> taskList;

	public MyTaskAdapter(Context context, int resource, List<Message> objects) {
		super(context, resource, objects);
		this.mContext = context;
		taskList = (ArrayList<Message>) objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.row_todolist, null);
			holder = new ViewHolder();
			holder.lblTaskName = (TextView) convertView.findViewById(R.id.txtToDoTitle);
			holder.lblTaskassignee = (TextView) convertView.findViewById(R.id.txtAssignee);
			holder.lblTasksubject = (TextView) convertView.findViewById(R.id.txtsubject);
			
//			holder.chkTask = (CheckBox) convertView.findViewById(R.id.chktodo);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.lblTaskName.setText(getItem(position).getName());
		holder.lblTaskassignee.setText(getItem(position).getAssignee());
		holder.lblTasksubject.setText(getItem(position).getSubject());
	
		Log.e(null, "`todo--"+holder.lblTaskName.getText());
	
		return convertView;
	}

	static class ViewHolder {
		TextView lblTaskName,lblTaskassignee,lblTasksubject;
	}

}
