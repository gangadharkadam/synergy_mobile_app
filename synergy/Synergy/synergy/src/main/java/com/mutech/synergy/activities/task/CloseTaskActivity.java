package com.mutech.synergy.activities.task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.mutech.synergy.App;
import com.mutech.synergy.R;
import com.mutech.synergy.SynergyValues.Commons;
import com.mutech.synergy.SynergyValues.Web.GetAllTasksService;
import com.mutech.synergy.models.CellMembersRespModel;
import com.mutech.synergy.models.CellMembersRespModel.MemberMessage;
import com.mutech.synergy.models.MeetingListRequestModel;
import com.mutech.synergy.models.ResponseMessageModel;
import com.mutech.synergy.models.ResponseMessageModel2;
import com.mutech.synergy.models.SaveTaskRequestModel;
import com.mutech.synergy.utils.InputValidation;
import com.mutech.synergy.utils.Methods;
import com.mutech.synergy.utils.NetworkHelper;
import com.mutech.synergy.utils.PreferenceHelper;

public class CloseTaskActivity extends AppCompatActivity implements OnClickListener{

	private EditText txtTaskDetailsName,txtAssignee,txtFollowUpTask,txtTaskCell,txtTaskSrCell,txtTaskPCF,txtcomment;
	private TextView txtDueDate,lblFollowUpTask,spnAssignedTo;
	private Spinner spnTaskStatus,spinnerAssignedTo;//spnTaskCell,spnTaskSrCell,spnTaskPCF,spnPriority,
	private Button btnUpdateTask;

	private PreferenceHelper mPreferenceHelper;
	private Gson gson;
	private String mType,mStatus,mCell,mDescription,mExp_end_date,m_Assign,
	mPriority,mSubject,mName,mAssignee,mFollowup_task,mPcf,mSenior_cell;
	List<MemberMessage> list;
	
	private ArrayList<String> membersList;
	private Bundle b;
	private SimpleDateFormat dateFormatter,dateFormatter01;

	private DatePickerDialog dueDatePickerDialog;
	private Spinner txtPriority;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_taskclose);
		setContentView(R.layout.activity_taskupdate);
		Log.d("NonStop", "In Update Task");
		initialize();
	}

	@SuppressLint("NewApi")
	private void initialize() {

		getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		getSupportActionBar().setHomeButtonEnabled(false);
		getSupportActionBar().setCustomView(R.layout.custom_actionbar);
		TextView tvTitle=(TextView)getSupportActionBar().getCustomView().findViewById(R.id.title_text);
		
		
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2E9AFE")));
		
		getSupportActionBar().setDisplayShowCustomEnabled(true);


		Calendar newCalendar = Calendar.getInstance();
		dueDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
				txtDueDate.setText(dateFormatter.format(newDate.getTime()));
			}

		},newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

		mPreferenceHelper = new PreferenceHelper(this);
		gson = new Gson();

		dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
		dateFormatter01=new SimpleDateFormat("yyyy-MM-dd");
		spinnerAssignedTo=(Spinner) findViewById(R.id.AssignedTo);
		
		txtTaskDetailsName=(EditText) findViewById(R.id.txtTaskDetailsName);
		txtAssignee=(EditText) findViewById(R.id.txtAssignee);
		txtFollowUpTask=(EditText) findViewById(R.id.txtFollowUpTask);
		txtDueDate=(TextView) findViewById(R.id.txtDueDate);
		spnTaskStatus=(Spinner) findViewById(R.id.spnTaskStatus);
		spnAssignedTo=(TextView) findViewById(R.id.spnAssignedTo);

		/*spnPriority=(Spinner) findViewById(R.id.spnPriority);
		spnTaskPCF=(Spinner) findViewById(R.id.spnTaskPCF);
		spnTaskSrCell=(Spinner) findViewById(R.id.spnTaskSrCell);
		spnTaskCell=(Spinner) findViewById(R.id.spnTaskCell);*/
		txtPriority=(Spinner) findViewById(R.id.txtPriority);
		txtTaskPCF=(EditText) findViewById(R.id.txtTaskPCF);
		txtTaskSrCell=(EditText) findViewById(R.id.txtTaskSrCell);
		txtTaskCell=(EditText) findViewById(R.id.txtTaskCell);
		lblFollowUpTask=(TextView) findViewById(R.id.lblFollowUpTask);
		txtcomment=(EditText) findViewById(R.id.txtcomment);


		txtPriority.setEnabled(true);
		txtTaskPCF.setEnabled(false);
		txtTaskSrCell.setEnabled(false);
		txtTaskCell.setEnabled(false);
//		txtDueDate.setEnabled(false);
		txtDueDate.setEnabled(true);
		txtAssignee.setEnabled(false);

		txtDueDate.setOnClickListener(this);
		btnUpdateTask=(Button) findViewById(R.id.btnUpdateTask);
		btnUpdateTask.setOnClickListener(this);
		spnAssignedTo.setOnClickListener(this);
		membersList=new ArrayList<String>();

		ArrayList<String> statusList=new ArrayList<String>();
		statusList.add("Open");
		statusList.add("Working");
		statusList.add("Pending");
		statusList.add("Review");
		statusList.add("Closed");
		statusList.add("Cancelled");

		ArrayList<String> priorityList=new ArrayList<String>();
		priorityList.add("Low");
		priorityList.add("Medium");
		priorityList.add("High");
		priorityList.add("Urgent");


		ArrayAdapter<String> adapterStatus = new ArrayAdapter<String>(CloseTaskActivity.this, android.R.layout.simple_spinner_item, statusList);
		adapterStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnTaskStatus.setAdapter(adapterStatus);

		ArrayAdapter<String> priorityStatus = new ArrayAdapter<String>(CloseTaskActivity.this, android.R.layout.simple_spinner_item, priorityList);
		priorityStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		txtPriority.setAdapter(priorityStatus);

		b=getIntent().getExtras();
		if(null !=b){
			mType=b.getString("Type");
			mStatus=b.getString("status");

			mSubject=b.getString("subject");
			mName=b.getString("name");	
			
			Log.e(null, "Name--"+mName);

			if(mType.equalsIgnoreCase("Update")){
				txtFollowUpTask.setVisibility(View.GONE);
				lblFollowUpTask.setVisibility(View.GONE);
				/*spnAssignedTo.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						spinnerAssignedTo.setVisibility(View.VISIBLE);
						spnAssignedTo.setVisibility(View.GONE);
					}
				});*/
				tvTitle.setText("UPDATE TASK");
			}else{
				txtFollowUpTask.setVisibility(View.VISIBLE);
				lblFollowUpTask.setVisibility(View.VISIBLE);
				tvTitle.setText("CLOSE & FOLLOW TASK");
				spnAssignedTo.setEnabled(false);
				spnTaskStatus.setEnabled(false);
				txtTaskDetailsName.setEnabled(false);
				btnUpdateTask.setText("CLOSE & FOLLOW TASK");
			}

			if(null !=b.getString("cell")){
				txtTaskCell.setText(b.getString("cell"));
//				getMembersOfSelectedCell(b.getString("cell"));
			}

			if(null !=b.getString("pcf"))
				txtTaskPCF.setText(b.getString("pcf"));

			if(null !=b.getString("senior_cell"))
				txtTaskSrCell.setText(b.getString("senior_cell"));

			if(null !=b.getString("description"))
				txtTaskDetailsName.setText(b.getString("description"));

			if(null !=b.getString("exp_end_date"))
				try {
					Log.d("droid", "dateFormatter.format(dateFormatter01.parse(txtDueDate.getText().toString() ------"+dateFormatter.format(dateFormatter01.parse(b.getString("exp_end_date"))));
					txtDueDate.setText(dateFormatter.format(dateFormatter01.parse(b.getString("exp_end_date"))));
				} catch (ParseException e) {
					e.printStackTrace();
				}

			if(null !=b.getString("assignee"))
				txtAssignee.setText(b.getString("assignee"));

			if(null !=b.getString("priority")) {
				if(b.getString("priority").contentEquals("Low"))
					txtPriority.setSelection(0);
				else if(b.getString("priority").contentEquals("Medium"))
					txtPriority.setSelection(1);
				else if(b.getString("priority").contentEquals("High"))
					txtPriority.setSelection(2);
				else if(b.getString("priority").contentEquals("Urgent"))
					txtPriority.setSelection(3);
			}

			if(null !=b.getString("followup_task"))
				txtFollowUpTask.setText(b.getString("followup_task"));
			
			if(null !=b.getString("comment"))
				txtcomment.setText(b.getString("followup_task"));
			
				
			if(null !=b.getString("_assign"))
				spnAssignedTo.setText(b.getString("_assign"));

			if(null !=b.getString("status"))
				spnTaskStatus.setSelection(adapterStatus.getPosition(b.getString("status")));
			else
				spnTaskStatus.setSelection(0);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btnUpdateTask:
				if(NetworkHelper.isOnline(this)){

					if(InputValidation.hasText(txtTaskDetailsName)) {
						try {
							if(txtDueDate.getText().toString().contentEquals("")) {
								Methods.showProgressDialog(this);
								updateTask();
							} else {
								if(System.currentTimeMillis() > dateFormatter.parse(txtDueDate.getText().toString()).getTime()) {
									new AlertDialog.Builder(CloseTaskActivity.this)
											.setMessage("Due date can not be smaller than today")
											.setCancelable(false)
											.setPositiveButton("OK", new DialogInterface.OnClickListener() {
												@Override
												public void onClick(DialogInterface dialogInterface, int i) {
												}
											})
											.show();
								} else {
									Methods.showProgressDialog(this);
									updateTask();
								}
							}
						} catch (ParseException e) {
							e.printStackTrace();
						}
					} else {
						new AlertDialog.Builder(CloseTaskActivity.this)
								.setCancelable(false)
								.setTitle("Invalid Input")
								.setMessage("Please enter valid value in the field marked red")
								.setPositiveButton("OK", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialogInterface, int i) {

									}
								})
								.show();
					}
				}else{
					Methods.longToast("Please connect to Internet", this);
				}
				break;
			case R.id.spnAssignedTo:
				spinnerAssignedTo.setVisibility(View.VISIBLE);
				spnAssignedTo.setVisibility(View.GONE);
				if(NetworkHelper.isOnline(CloseTaskActivity.this)){
					Methods.showProgressDialog(CloseTaskActivity.this);
					getMembersOfSelectedCell();

				}else{
					Methods.longToast("Please connect to Internet", CloseTaskActivity.this);
				}
				break;
			case R.id.txtDueDate:
				dueDatePickerDialog.show();
				break;
			default:
			break;
		}
	}

	private void updateTask() {
		StringRequest reqCreateTasksList = new StringRequest(Method.POST,GetAllTasksService.UPDATE_TASK, new Listener<String>() {

			@Override
			public void onResponse(String response) {
				Methods.closeProgressDialog();
				Log.d("droid", "create task list ---------------"+ response);

				if(response.contains("status")){
					ResponseMessageModel2 resp2Model=gson.fromJson(response, ResponseMessageModel2.class);
					Methods.longToast(resp2Model.getMessage().getMessage(), CloseTaskActivity.this);
				}else{
					ResponseMessageModel model=gson.fromJson(response, ResponseMessageModel.class);
					if(null !=model.getMessage() && model.getMessage().trim().length() >0){
						Methods.longToast(model.getMessage(), CloseTaskActivity.this);
					}
				}
				/*for(int i=0;i<bitmapList.size();i++){
					Methods.showProgressDialog(CloseTaskActivity.this);
					uploadProfPicService(bitmapList.get(i),i);
				}*/
				Intent intentTODO = new Intent(CloseTaskActivity.this, ToDoTaskActivity.class);
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
				if(!txtDueDate.getText().toString().contentEquals("")) {
					try {
						model.setExp_end_date(dateFormatter01.format(dateFormatter.parse(txtDueDate.getText().toString())));
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
//				Log.e(null, "xxbb--"+spnAssignedTo.getSelectedItem().toString());
//				model.set_assign("["+spnAssignedTo.getSelectedItem().toString()+"]");
				model.set_assign(spnAssignedTo.getText().toString());
				model.setPriority(txtPriority.getSelectedItem().toString());
				model.setSubject("task");
				model.setAssignee(txtAssignee.getText().toString());
				model.setName(mName);

				model.setComment(txtcomment.getText().toString());
				if(null !=txtFollowUpTask.getText().toString())
					model.setFollowup_task(txtFollowUpTask.getText().toString());
				else
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

private void getMembersOfSelectedCell() {
		StringRequest reqGetCellMember = new StringRequest(Method.POST,GetAllTasksService.FETCH_DATA, new Listener<String>() {
			@Override
			public void onResponse(String response) {
				Methods.closeProgressDialog();
				Log.e("droid", "get all cell members ---------------"+ response);

				membersList = new ArrayList<String>();

				gson = new Gson();
				CellMembersRespModel model = gson.fromJson(response,CellMembersRespModel.class);
				
				
				if (null != model.getMessage()) {
				
					list=model.getMessage();
					
				}
				Log.d("droid", "members " + membersList.size());
				
				MyAdapter adpter=new MyAdapter(CloseTaskActivity.this,list);
				spinnerAssignedTo.setAdapter(adpter);
			
				
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

				Log.d("droid", "Params : " + dataString);
				params.put(GetAllTasksService.DATA, dataString);
				return params;
			}
		};
		App.getInstance().addToRequestQueue(reqGetCellMember,"reqGetCellMember");
		reqGetCellMember.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1,1.0f));

	}
public class MyAdapter extends BaseAdapter {
	 
    Context c;
    List<MemberMessage> list;
 
    public MyAdapter(Context context, List<MemberMessage> list2) {
        super();
        this.c = context;
        this.list = list2;
    }
 
    @Override
    public int getCount() {
        return list.size();
    }
 
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }
 
    @Override
    public long getItemId(int position) {
        return 0;
    }
 
    public View getView(int position, View convertView, ViewGroup parent) {
 
    	MemberMessage cur_obj = list.get(position);
        LayoutInflater inflater = ((Activity) c).getLayoutInflater();
        View row = inflater.inflate(R.layout.spinner_row, parent, false);
     
        TextView txtid = (TextView) row.findViewById(R.id.txtid);
        txtid.setText(cur_obj.getID());
        
        TextView txtname = (TextView) row.findViewById(R.id.txtname);
        txtname.setText(cur_obj.getMemberName());
        
        TextView txtemilid = (TextView) row.findViewById(R.id.txtemail);
        txtemilid.setText(cur_obj.getUser_id());
       
        return row;
    }
}
	
}
