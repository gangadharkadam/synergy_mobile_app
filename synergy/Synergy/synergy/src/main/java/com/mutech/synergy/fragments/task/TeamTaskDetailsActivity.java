package com.mutech.synergy.fragments.task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.mutech.synergy.App;
import com.mutech.synergy.R;
import com.mutech.synergy.R.id;
import com.mutech.synergy.R.layout;
import com.mutech.synergy.R.menu;
import com.mutech.synergy.SynergyValues.Commons;
import com.mutech.synergy.SynergyValues.Web.GetAllTasksService;
import com.mutech.synergy.activities.cellMasters.AllMemberListActivity;
import com.mutech.synergy.activities.task.CreateTaskActivity;

import com.mutech.synergy.activities.task.CreateTaskActivity.MastersListAdapterTask;
import com.mutech.synergy.activities.task.ToDoTaskActivity;
import com.mutech.synergy.models.CellMembersRespModel;
import com.mutech.synergy.models.MeetingListRequestModel;
import com.mutech.synergy.models.ResponseMessageModel;
import com.mutech.synergy.models.ResponseMessageModel2;
import com.mutech.synergy.models.SaveTaskRequestModel;
import com.mutech.synergy.utils.InputValidation;
import com.mutech.synergy.utils.Methods;
import com.mutech.synergy.utils.NetworkHelper;
import com.mutech.synergy.utils.PreferenceHelper;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class TeamTaskDetailsActivity extends AppCompatActivity {

	EditText txtTaskDetailsName,txtPriority,txtTaskCell,txtAssignee,txtcomment;
	Spinner spnTaskStatus,spnAssignedTo;
	TextView txtDueDate;
	Button btnUpdateTask;
	private PreferenceHelper mPreferenceHelper;
	ArrayList<String> statusarray;
	private SimpleDateFormat dateFormatter,dateFormatter01;

	private ArrayList<String> membersList;
	private ArrayList<String> useridList;
	private ArrayList<String> idList;
	private Gson gson;

	private DatePickerDialog dueDatePickerDialog;
	private String mName;
	private String m_assign;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_team_task_details);
	
		getSupportActionBar().hide();
		
		//open working,
		
		statusarray=new ArrayList<String>();
		
		txtTaskDetailsName=(EditText) findViewById(R.id.txtTaskDetailsName);
		txtDueDate=(TextView) findViewById(R.id.txtDueDate);	
		txtPriority=(EditText) findViewById(R.id.txtPriority);
		spnTaskStatus=(Spinner) findViewById(R.id.spnTaskStatus);
		txtTaskCell=(EditText) findViewById(R.id.txtTaskCell);
		txtAssignee=(EditText) findViewById(R.id.txtAssignee);
		spnAssignedTo= (Spinner) findViewById(R.id.spnAssignedTo);
		txtcomment=(EditText) findViewById(R.id.txtcomment);
		
		btnUpdateTask = (Button) findViewById(id.btnUpdateTask);

		dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
		dateFormatter01=new SimpleDateFormat("yyyy-MM-dd");

		btnUpdateTask.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {

				if(isValid()) {
					try {
						if(txtDueDate.getText().toString().contentEquals("")) {
							Methods.showProgressDialog(TeamTaskDetailsActivity.this);
							updateTask();
						} else {
							if(System.currentTimeMillis() > dateFormatter01.parse(txtDueDate.getText().toString()).getTime()) {
								new AlertDialog.Builder(TeamTaskDetailsActivity.this)
										.setMessage("Due date can not be smaller than today")
										.setCancelable(false)
										.setPositiveButton("OK", new DialogInterface.OnClickListener() {
											@Override
											public void onClick(DialogInterface dialogInterface, int i) {
											}
										})
										.show();
							} else {
								Methods.showProgressDialog(TeamTaskDetailsActivity.this);
								updateTask();
							}
						}
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
			}
		});
		mPreferenceHelper=new PreferenceHelper(TeamTaskDetailsActivity.this);
		gson=new Gson();

		Calendar newCalendar = Calendar.getInstance();
		dueDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
				txtDueDate.setText(dateFormatter01.format(newDate.getTime()));
			}

		},newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

		statusarray=new ArrayList<String>();
		statusarray.add("Open");
		statusarray.add("Working");
		statusarray.add("Pending Review");
		statusarray.add("Closed");
		statusarray.add("Cancelled");
		
		ArrayAdapter<String> adapterZone = new ArrayAdapter<String>(TeamTaskDetailsActivity.this, android.R.layout.simple_spinner_item, statusarray);
		adapterZone.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnTaskStatus.setAdapter(adapterZone);
		
		String str=getIntent().getStringExtra("status").toString();
		
		spnTaskStatus.setSelection(adapterZone.getPosition(str));
		
		
	//	String comment=(getIntent().getStringExtra("comment").toString().equals("null"))?getIntent().getStringExtra("comment"):"";

		mName = (getIntent().getStringExtra("name").toString().equals("null"))?"":getIntent().getStringExtra("name");
		m_assign = (getIntent().getStringExtra("_assign").toString().equals("null"))?"":getIntent().getStringExtra("_assign");
		txtPriority.setText((getIntent().getStringExtra("priority").toString().equals("null"))?"":getIntent().getStringExtra("priority"));
		txtTaskCell.setText((getIntent().getStringExtra("TaskCell").toString().equals("null"))?"":getIntent().getStringExtra("TaskCell"));
		txtDueDate.setText((getIntent().getStringExtra("dueDate").toString().equals("null"))?"":getIntent().getStringExtra("dueDate"));
		txtTaskDetailsName.setText((getIntent().getStringExtra("description").toString().equals("null"))?"":getIntent().getStringExtra("description"));
		txtAssignee.setText((getIntent().getStringExtra("assignee").toString().equals("null"))?"":getIntent().getStringExtra("assignee"));
		//spnAssignedTo.setText((getIntent().getStringExtra("assignedTo").toString().equals("null"))?"":getIntent().getStringExtra("assignedTo"));
		txtcomment.setText((getIntent().getStringExtra("comment").toString().equals("null"))?"":getIntent().getStringExtra("comment"));
		
		if (NetworkHelper.isOnline(TeamTaskDetailsActivity.this)) {
			getMembersOfSelectedCell();
		}
	
		txtDueDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dueDatePickerDialog.show();
			}
		});

	}

	public boolean isValid() {

		if(!InputValidation.hasText(txtTaskDetailsName)) {
			new AlertDialog.Builder(TeamTaskDetailsActivity.this)
					.setCancelable(false)
					.setTitle("Mandatory Input")
					.setMessage("Please enter 'Task Details'")
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface, int i) {

						}
					})
					.show();
			return false;
		}
		return true;
	}


	private void getMembersOfSelectedCell() {
		StringRequest reqGetCellMember = new StringRequest(Method.POST,GetAllTasksService.FETCH_DATA, new Listener<String>() {
			@Override
			public void onResponse(String response) {
				Methods.closeProgressDialog();
				Log.e("Responce droid", "get all cell members ---------------"
						+ response);

				/*{
			        "user_id": "region3@gmail.com",
			        "ID": "M00000062",
			        "member_name": "Region3 Lead Member"
			    },
				*/
				
				membersList = new ArrayList<String>();
				 useridList=new ArrayList<String>();
				idList=new ArrayList<String>();
					
				gson = new Gson();
				CellMembersRespModel model = gson.fromJson(response,CellMembersRespModel.class);
				if (null != model.getMessage()) {
					for (int i = 0; i < model.getMessage().size(); i++) {
						
						idList.add(model.getMessage().get(i).getID());
						useridList.add(model.getMessage().get(i).getUser_id());
						membersList.add(model.getMessage().get(i).getMemberName());
					}	
				}
				Log.d("droid", "members " + membersList.size());
				MastersListAdapterTask adapter = new MastersListAdapterTask(TeamTaskDetailsActivity.this, membersList,idList,useridList);
				spnAssignedTo.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				// }
				/*
				 * } catch (JSONException e) { e.printStackTrace(); }
				 */
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Methods.closeProgressDialog();
				Log.d("droid", "get all cell error---------------"
						+ error.getCause());
			}
		}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Log.d("droid", "get params");
				Map<String, String> params = new HashMap<String, String>();

				MeetingListRequestModel model = new MeetingListRequestModel();
				model.setUsername(mPreferenceHelper.getString(Commons.USER_EMAILID));
				model.setUserpass(mPreferenceHelper.getString(Commons.USER_PASSWORD));
				
				String dataString = gson.toJson(model,MeetingListRequestModel.class);

				Log.e("Request droid", "Params : " + dataString);
				params.put(GetAllTasksService.DATA, dataString);
				return params;
			}
		};
		App.getInstance().addToRequestQueue(reqGetCellMember,"reqGetCellMember");
		reqGetCellMember.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1,1.0f));

	}

	public class MastersListAdapterTask extends BaseAdapter{

		private Context mContext;
		private ArrayList<String> mList;
		private ArrayList<String> idList;
		private ArrayList<String> useridList;

		public MastersListAdapterTask(Context context,
				ArrayList<String> mCellList,ArrayList<String> idList,ArrayList<String> useridList) {
			mContext=context;
			mList=new ArrayList<String>();
			mList=mCellList;
			
			this.idList=new ArrayList<String>();
			this.useridList=new ArrayList<String>();
			
			this.idList=idList;
			this.useridList=useridList;
			
		}

		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			return useridList.get(position);
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
				mHolder.txtuserid=(TextView) convertView.findViewById(R.id.txtuserid);
				mHolder.txtid=(TextView) convertView.findViewById(R.id.txtid);
				
				convertView.setTag(mHolder);
			} else {
				mHolder = (Holder) convertView.getTag();
			}
			mHolder.txtMasterName.setText(mList.get(position));
			mHolder.txtuserid.setText(useridList.get(position));
			mHolder.txtid.setText(idList.get(position));

			return convertView;
		}
	}

	static class Holder{
		private TextView txtMasterName;
		private TextView txtuserid;
		private TextView txtid;
	}

	private void updateTask() {
		StringRequest reqCreateTasksList = new StringRequest(Method.POST,GetAllTasksService.UPDATE_TASK, new Listener<String>() {

			@Override
			public void onResponse(String response) {
				Methods.closeProgressDialog();
				Log.d("droid", "create task list ---------------"+ response);

				if(response.contains("status")){
					ResponseMessageModel2 resp2Model=gson.fromJson(response, ResponseMessageModel2.class);
					Methods.longToast(resp2Model.getMessage().getMessage(), TeamTaskDetailsActivity.this);
				}else{
					ResponseMessageModel model=gson.fromJson(response, ResponseMessageModel.class);
					if(null !=model.getMessage() && model.getMessage().trim().length() >0){
						Methods.longToast(model.getMessage(), TeamTaskDetailsActivity.this);
					}
				}
				/*for(int i=0;i<bitmapList.size();i++){
					Methods.showProgressDialog(TeamTaskDetailsActivity.this);
					uploadProfPicService(bitmapList.get(i),i);
				}*/
				Intent intentTODO = new Intent(TeamTaskDetailsActivity.this, ToDoTaskActivity.class);
				intentTODO.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				startActivity(intentTODO);
				finish();

			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Methods.closeProgressDialog();
				Log.d("droid", "create tasks error---------------"+ error.getCause());

			}
		}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();

				SaveTaskRequestModel model = new SaveTaskRequestModel();
				model.setUserName(mPreferenceHelper.getString(Commons.USER_EMAILID));
				model.setUserpass(mPreferenceHelper.getString(Commons.USER_PASSWORD));
				model.setStatus(spnTaskStatus.getSelectedItem().toString());

				Log.e(null, "xx--"+spnTaskStatus.getSelectedItem().toString());

				//MeetingListModel modelSaveCell=(MeetingListModel) spnTaskCell.getSelectedItem();
				model.setCell(txtTaskCell.getText().toString());
				model.setDescription(txtTaskDetailsName.getText().toString());

				if(!txtDueDate.getText().toString().contentEquals("")){
					try {
						model.setExp_end_date(dateFormatter01.format(dateFormatter01.parse(txtDueDate.getText().toString())));
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
//				Log.e(null, "xxbb--"+spnAssignedTo.getSelectedItem().toString());
//				model.set_assign("["+spnAssignedTo.getSelectedItem().toString()+"]");
				model.set_assign(m_assign);
				model.setPriority(txtPriority.getText().toString());
				model.setSubject("task");
				model.setAssignee(txtAssignee.getText().toString());
				model.setComment(txtcomment.getText().toString());
				model.setName(mName);

//				if(null !=txtFollowUpTask.getText().toString())
//					model.setFollowup_task(txtFollowUpTask.getText().toString());
//				else
					model.setFollowup_task("");

				//model.setPcf(spnTaskPCF.getSelectedItem().toString());
				//model.setSenior_cell(spnTaskSrCell.getSelectedItem().toString());

				String dataString = gson.toJson(model,SaveTaskRequestModel.class);

				Log.d("droid", dataString);
				params.put(GetAllTasksService.DATA, dataString);
				return params;
			}
		};

		App.getInstance().addToRequestQueue(reqCreateTasksList, "reqTasksList");
		reqCreateTasksList.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
	}

}
