package com.mutech.synergy.fragments.task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ActionBar.LayoutParams;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.mutech.synergy.App;
import com.mutech.synergy.R;
import com.mutech.synergy.SynergyValues.Commons;
import com.mutech.synergy.SynergyValues.Web.GetAllListMastersService;
import com.mutech.synergy.SynergyValues.Web.GetAllTasksService;
import com.mutech.synergy.SynergyValues.Web.GetHigherHierarchyService;
import com.mutech.synergy.SynergyValues.Web.LowerHierarchyService;
import com.mutech.synergy.activities.dashboard.FirstTimeMonthActivity;
import com.mutech.synergy.activities.event.EventListActivity;

import com.mutech.synergy.activities.event.EventListActivity.SpinnerDataFlag;
import com.mutech.synergy.activities.event.MyEventListActivity.Holder;
import com.mutech.synergy.activities.meeting.MyMeetingListActivity;
import com.mutech.synergy.activities.profile.MyProfileActivity;
import com.mutech.synergy.activities.task.CloseTaskActivity;
import com.mutech.synergy.activities.task.CreateTaskActivity;
import com.mutech.synergy.adapters.MyTaskAdapter;

import com.mutech.synergy.models.MeetingListRequestModel;
import com.mutech.synergy.models.MyTasksResponseModel;
import com.mutech.synergy.models.ResponseMessageModel2;
import com.mutech.synergy.models.EventParticipantsModel.EventParticipantSubModel;
import com.mutech.synergy.models.MyTasksResponseModel.Message;
import com.mutech.synergy.utils.Methods;
import com.mutech.synergy.utils.NetworkHelper;
import com.mutech.synergy.utils.PreferenceHelper;

public class MyTaskFragment extends BaseFragment implements OnItemClickListener {

	private ListView lvMyTasks;
	EventListAdapter taskAdpt;
	private MyTasksResponseModel model;
	private ArrayList<Message> taskList;
	private Gson gson;
	private PreferenceHelper mPreferenceHelper;
	
	JSONArray jsonarray;
	ImageView filterimg;
	public int TOTAL_LIST_ITEMS;
    public int NUM_ITEMS_PAGE   = 20;
    private int noOfBtns;
    private Button[] btns;
    LinearLayout ll; 
    
    private SimpleDateFormat displayDateFormat;
	
    Spinner spresion,spzone,sppcf,spgroupchurch,spchurch,spSeniorCell,spCell,sppriority,spstatus;
	private ArrayList<String> mZoneList,mRegionList,mChurchList,mSeniorCellList,mGrpChurchList,mPCFList,mCellList;
	
	TextView txtFromDate,txtToDate;
	EditText etName;
	String UserRoll;
	
	private DatePickerDialog fromDatePickerDialog,toDatePickerDialog;
	private SimpleDateFormat dateFormatter,dateFormatter01;
	Calendar newCalendar;
	// LinearLayout ll;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setContentResource(R.layout.fragment_mytasks);
		setHasOptionsMenu(true);
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_addTo:
				return false;
			case R.id.menu_filter:
				showDialog();
				return true;
			default:
				break;
		}
		return false;
	}

	@Override
	protected void initializeComponents(View rootView) {
		
		
//		filterimg=(ImageView) rootView.findViewById(R.id.filterimg);
		
		  ll = (LinearLayout)rootView.findViewById(R.id.btnLay);
		lvMyTasks = (ListView) rootView.findViewById(R.id.lvMyTasks);
		ll = (LinearLayout)rootView.findViewById(R.id.btnLay);
		
		taskList = new ArrayList<MyTasksResponseModel.Message>();
		
		lvMyTasks.setOnItemClickListener(this);
		
		jsonarray=new JSONArray();
	
		mPreferenceHelper=new PreferenceHelper(getActivity());
		UserRoll=mPreferenceHelper.getString(Commons.USER_ROLE);
		
		model=new MyTasksResponseModel();
		gson=new Gson();
		mPreferenceHelper=new PreferenceHelper(getActivity());
		
		mZoneList=new ArrayList<String>();
		mRegionList=new ArrayList<String>();
		mChurchList=new ArrayList<String>();
		mSeniorCellList=new ArrayList<String>();
		mGrpChurchList=new ArrayList<String>();
		mPCFList=new ArrayList<String>();
		mCellList=new ArrayList<String>();
		
		dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
		//2015-05-24 06:22:22
		dateFormatter01=new SimpleDateFormat("yyyy-MM-dd");

		newCalendar = Calendar.getInstance();
		
		fromDatePickerDialog = new DatePickerDialog(getActivity(), new OnDateSetListener() {

			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
				txtFromDate.setText(dateFormatter.format(newDate.getTime()));
			}

		},newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

		toDatePickerDialog = new DatePickerDialog(getActivity(), new OnDateSetListener() {
			
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
				txtToDate.setText(dateFormatter.format(newDate.getTime()));
			}
		
		},newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

		fromDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
		toDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

//		filterimg.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//			//	Methods.longToast("hello", getActivity());
//				showDialog();
//			}
//		});

		
		if (NetworkHelper.isOnline(getActivity())) {
			Methods.showProgressDialog(getActivity());
			getTasks("1");
		} else {
			Methods.longToast("Please connect to Internet", getActivity());
		}
	}

	private void getTasks(final String pageno) {

		StringRequest reqTasksList=new StringRequest(Method.POST,GetAllTasksService.SERVICE_URL,new Listener<String>() {

			@Override
			public void onResponse(String response) {
				Methods.closeProgressDialog();
				Log.e("droid","get all req tasks list MyTaskFragment ---------------"+ response);

				try {
					
					JSONObject jsonobj=new JSONObject(response);
					
					jsonarray=jsonobj.getJSONObject("message").getJSONArray("records");
					
					int i=Integer.parseInt(jsonobj.getJSONObject("message").getString("total_count"));
					
					TOTAL_LIST_ITEMS=Integer.parseInt(jsonobj.getJSONObject("message").getString("total_count"));
				
					Log.e("totalcount=", ""+TOTAL_LIST_ITEMS);
					
				
				
				
					if(jsonarray.length()>0){
						Btnfooter();
						
						taskAdpt = new EventListAdapter(getActivity(), jsonarray);
						lvMyTasks.setAdapter(taskAdpt);
						
					}else{
						//filterimg.setVisibility(View.GONE);
						Methods.longToast("No results found", getActivity());
					}
			
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				/*model=gson.fromJson(response, MyTasksResponseModel.class);
				if(null !=model){
					if(null != model.getTaskList() && model.getTaskList().size() >0){
						Log.d("Alpha","Size "+model.getTaskList().size());
						taskList.clear();
						taskList.addAll(model.getTaskList());
					}*/
				//}

			}
		},new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Methods.closeProgressDialog();
				Log.d("droid","get all tasks error MyTaskFragment---------------"+ error.getCause());


			}
		})
		{
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();

				/*MeetingListRequestModel model=new MeetingListRequestModel();
				model.setUsername(mPreferenceHelper.getString(Commons.USER_EMAILID));
				model.setUserpass(mPreferenceHelper.getString(Commons.USER_PASSWORD));
      //          model.setName(model.getName());
*/				
				JSONObject json=new JSONObject();
				try {
				
					json.put("username", mPreferenceHelper.getString(Commons.USER_EMAILID));
					json.put("userpass", mPreferenceHelper.getString(Commons.USER_PASSWORD));
					json.put("page_no", pageno);
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				String dataString=json.toString();//gson.toJson(model, MeetingListRequestModel.class);

				Log.d("droid", dataString);
				params.put(GetAllTasksService.DATA, dataString);
				return params; 
			}
		}
		;

		App.getInstance().addToRequestQueue(reqTasksList, "reqTasksList");
		reqTasksList.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));			
	}

	@Override
	protected void setListeners() {

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		opendialog(position);
	}

	private void opendialog(final int position) {
		final CharSequence[] items = { "Update Task","Close & Follow Task",
		"Cancel" };//

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Task Action");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				if (items[item].equals("Update Task")) {
					Intent i=new Intent(getActivity(),CloseTaskActivity.class);
			
					
					
					try {
						i.putExtra("Type", "Update");
						i.putExtra("status",jsonarray.getJSONObject(position).getString("status"));
						i.putExtra("cell", jsonarray.getJSONObject(position).getString("cell"));
						i.putExtra("description",jsonarray.getJSONObject(position).getString("description"));
						i.putExtra("exp_end_date",jsonarray.getJSONObject(position).getString("exp_end_date"));
						i.putExtra("_assign",jsonarray.getJSONObject(position).getString("_assign"));
						
						i.putExtra("priority",jsonarray.getJSONObject(position).getString("priority"));

						i.putExtra("subject",jsonarray.getJSONObject(position).getString("subject"));
						i.putExtra("name",jsonarray.getJSONObject(position).getString("name"));
						i.putExtra("assignee", jsonarray.getJSONObject(position).getString("assignee"));
						i.putExtra("followup_task", jsonarray.getJSONObject(position).getString("assignee"));
						
						i.putExtra("pcf", jsonarray.getJSONObject(position).getString("pcf"));
						i.putExtra("senior_cell", jsonarray.getJSONObject(position).getString("senior_cell"));
						i.putExtra("comment", jsonarray.getJSONObject(position).getString("comment"));
						
						
						startActivity(i);

						MyTaskFragment.this.getActivity().finish();

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
				} 
				else if (items[item].equals("Close & Follow Task")) {
					Intent i=new Intent(getActivity(),CloseTaskActivity.class);
				try{
					i.putExtra("Type", "Close");
					i.putExtra("status",jsonarray.getJSONObject(position).getString("status"));
					i.putExtra("cell", jsonarray.getJSONObject(position).getString("cell"));
					i.putExtra("description",jsonarray.getJSONObject(position).getString("description"));
					i.putExtra("exp_end_date",jsonarray.getJSONObject(position).getString("exp_end_date"));
					i.putExtra("_assign",jsonarray.getJSONObject(position).getString("_assign"));
					
					i.putExtra("priority",jsonarray.getJSONObject(position).getString("priority"));

					i.putExtra("subject",jsonarray.getJSONObject(position).getString("subject"));
					i.putExtra("name",jsonarray.getJSONObject(position).getString("name"));
					i.putExtra("assignee", jsonarray.getJSONObject(position).getString("assignee"));
					i.putExtra("followup_task",jsonarray.getJSONObject(position).getString("assignee"));
					
					i.putExtra("pcf",jsonarray.getJSONObject(position).getString("pcf"));
					i.putExtra("senior_cell",jsonarray.getJSONObject(position).getString("senior_cell"));
					i.putExtra("comment",jsonarray.getJSONObject(position).getString("comment"));
					startActivity(i);
				
				}catch(JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			} 
				else if (items[item].equals("Cancel")) {
					dialog.dismiss();
				}
			}
		});
		builder.show();
	}
	
	private class EventListAdapter extends BaseAdapter{

		private Context mContext;
		private ArrayList<EventParticipantSubModel> mList;
		private Holder holder;
		JSONArray jsonarray;
		
		public EventListAdapter(Context context,JSONArray jsonarray) {
			mContext=context;
			this.jsonarray=jsonarray;
		}

		@Override
		public int getCount() {
			return jsonarray.length();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			
			ViewHolder holder;
			
			if (convertView == null) {
				convertView = View.inflate(mContext, R.layout.row_todolist, null);
				holder = new ViewHolder();
				holder.lblTaskName = (TextView) convertView.findViewById(R.id.txtToDoTitle);
				holder.lblTaskassignee = (TextView) convertView.findViewById(R.id.txtAssignee);
				holder.lblTasksubject = (TextView) convertView.findViewById(R.id.txtsubject);
				
//				holder.chkTask = (CheckBox) convertView.findViewById(R.id.chktodo);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
		
			try {
			
				holder.lblTaskName.setText(jsonarray.getJSONObject(position).getString("name"));
				holder.lblTaskassignee.setText(jsonarray.getJSONObject(position).getString("exp_end_date"));
				holder.lblTasksubject.setText(jsonarray.getJSONObject(position).getString("status"));
			
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Log.e(null, "`todo--"+holder.lblTaskName.getText());
		
			return convertView;
		}

		
		
	}
	static class ViewHolder {
		TextView lblTaskName,lblTaskassignee,lblTasksubject;
	}
	
private void Btnfooter()
	{
	    int val = TOTAL_LIST_ITEMS%NUM_ITEMS_PAGE;
	    val = val==0?0:1;
	    noOfBtns=TOTAL_LIST_ITEMS/NUM_ITEMS_PAGE+val;
	     
	  
	    ll.removeAllViews();
	    
	    
	    btns    =new Button[noOfBtns];
	     
	    for(int i=0;i<noOfBtns;i++)
	    {
	        btns[i] =   new Button(getActivity());
	        btns[i].setBackgroundColor(getResources().getColor(android.R.color.transparent));
	        btns[i].setText(""+(i+1));
	         
	        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	        ll.addView(btns[i], lp);
	         
	        final int j = i;
	        btns[j].setOnClickListener(new OnClickListener() {
	             
	            public void onClick(View v) 
	            {
	               	if(NetworkHelper.isOnline(getActivity())){
	        			Methods.showProgressDialog(getActivity());
	        			getTasks( btns[j].getText().toString());
	        			
	        		}else{
	        			
	        			Methods.longToast("Please connect to Internet", getActivity());
	        		
	        		}
	            	
	                CheckBtnBackGroud(j);
	            }
	        });
	        
	       
	    }
	     
	}

	private void CheckBtnBackGroud(int index)
	{
	  
	    for(int i=0;i<noOfBtns;i++)
	    {
	        if(i==index)
	        {
	            btns[index].setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
	            btns[i].setTextColor(getResources().getColor(android.R.color.white));
	        }
	        else
	        {
	            btns[i].setBackgroundColor(getResources().getColor(android.R.color.transparent));
	            btns[i].setTextColor(getResources().getColor(android.R.color.black));
	        }
	    }
	     
	}
	
public void showDialog(){
		
		LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
		View promptView = layoutInflater.inflate(R.layout.filter_dialog, null);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
		
		LinearLayout pcflayout=(LinearLayout) promptView.findViewById(R.id.pcflayout);
		LinearLayout srlayout=(LinearLayout) promptView.findViewById(R.id.srlayout);
		LinearLayout cellLayout=(LinearLayout) promptView.findViewById(R.id.cellLayout);
		LinearLayout layoutRegion=(LinearLayout) promptView.findViewById(R.id.layoutRegion);
		LinearLayout layoutzone=(LinearLayout) promptView.findViewById(R.id.layoutzone);
		LinearLayout layoutchurchgroup=(LinearLayout) promptView.findViewById(R.id.layoutchurchgroup);
		LinearLayout layoutchurch=(LinearLayout) promptView.findViewById(R.id.layoutchurch);
		LinearLayout priorityLayout=(LinearLayout) promptView.findViewById(R.id.priorityLayout);
		LinearLayout statusLayout=(LinearLayout) promptView.findViewById(R.id.statusLayout);
		
		final TextView spzoneTextView=(TextView) promptView.findViewById(R.id.spzoneTextView);
		final TextView spresionTextView=(TextView) promptView.findViewById(R.id.spresionTextView);
		final TextView spgroupchurchTextView=(TextView) promptView.findViewById(R.id.spgroupchurchTextView);
		final TextView spchurchTextView=(TextView) promptView.findViewById(R.id.spchurchTextView);
		final TextView sppcfTextView=(TextView) promptView.findViewById(R.id.sppcfTextView);
		final TextView spSeniorCellTextView=(TextView) promptView.findViewById(R.id.spSeniorCellTextView);
		final TextView spCellTextView=(TextView) promptView.findViewById(R.id.spCellTextView);
		final TextView sppriorityeTextView=(TextView) promptView.findViewById(R.id.sppriorityeTextView);
		final TextView spstatusTextView=(TextView) promptView.findViewById(R.id.spstatusTextView);
		
		txtFromDate=(TextView) promptView.findViewById(R.id.txtFromDate);
		txtToDate=(TextView) promptView.findViewById(R.id.txtToDate);
		spresion=(Spinner) promptView.findViewById(R.id.spresion);
		spzone=(Spinner) promptView.findViewById(R.id.spzone);
		sppcf=(Spinner) promptView.findViewById(R.id.sppcf);
		spgroupchurch=(Spinner) promptView.findViewById(R.id.spgroupchurch);
		spchurch =(Spinner) promptView.findViewById(R.id.spchurch);
		spSeniorCell=(Spinner) promptView.findViewById(R.id.spSeniorCell);
		spCell=(Spinner) promptView.findViewById(R.id.spCell);
		sppriority=(Spinner) promptView.findViewById(R.id.sppriority);
		spstatus=(Spinner) promptView.findViewById(R.id.spstatus);
	    etName =(EditText) promptView.findViewById(R.id.etName);
		
		
		String str=mPreferenceHelper.getString(Commons.USER_DEFVALUE);
		Log.e("default user", str);
		
		priorityLayout.setVisibility(View.VISIBLE);
		statusLayout.setVisibility(View.VISIBLE);

		
		ArrayList<String> priorityList=new ArrayList<String>();
		priorityList.add("Low");
		priorityList.add("Medium");
		priorityList.add("High");
		priorityList.add("Urgent");


		ArrayAdapter<String> adapterPriority = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, priorityList);
		adapterPriority.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sppriority.setAdapter(adapterPriority);

		
		ArrayList<String> statusList=new ArrayList<String>();
		statusList.add("Open");
		statusList.add("Working");
		statusList.add("Pending Review");
		statusList.add("Closed");
		statusList.add("Cancelled");
		
		ArrayAdapter<String> adapterStatus = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, statusList);
		adapterStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spstatus.setAdapter(adapterStatus);
		
		
		
		spzone.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				
				String str=spzone.getSelectedItem().toString();
				Methods.showProgressDialog(getActivity());
				getUpdatedSpinnerData("Zones",str);
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});
		
		spgroupchurch.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				
				String str=spgroupchurch.getSelectedItem().toString();
				Methods.showProgressDialog(getActivity());
				getUpdatedSpinnerData("Group Churches",str);
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});		

		spchurch.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				
				String str=spchurch.getSelectedItem().toString();
				Methods.showProgressDialog(getActivity());
				getUpdatedSpinnerData("Churches",str);
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});	
		
		sppcf.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				
				String str=sppcf.getSelectedItem().toString();
				Methods.showProgressDialog(getActivity());
				getUpdatedSpinnerData("PCFs",str);
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});	
		
		spSeniorCell.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				
				String str=spSeniorCell.getSelectedItem().toString();
				Methods.showProgressDialog(getActivity());
				getUpdatedSpinnerData("Senior Cells",str);
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});	
		
		spresionTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(SpinnerDataFlag.Regions_flag){
					spresionTextView.setVisibility(View.GONE);
					mRegionList.add(mPreferenceHelper.getString(Commons.USER_DEFVALUE));
					spresion.setVisibility(View.VISIBLE);
					setAdapters();
					SpinnerDataFlag.Regions_flag=false;
				}else{
					spresionTextView.setVisibility(View.GONE);
					spresion.setVisibility(View.VISIBLE);
					setAdapters();
					
				}
			}
		});
		
		spzoneTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
					
				
				if(SpinnerDataFlag.Zones_flag){
					
					if(mPreferenceHelper.getString(Commons.USER_ROLE).equals("Zonal Pastor")){
					
						mZoneList.add(mPreferenceHelper.getString(Commons.USER_DEFVALUE));
						spzone.setVisibility(View.VISIBLE);
						spzoneTextView.setVisibility(View.GONE);
						setAdapters();
						SpinnerDataFlag.Zones_flag=false;
					}else{
						
						try{
						
							String str=spresion.getSelectedItem().toString();
							Log.e("selected value", str);
							
							Methods.showProgressDialog(getActivity());
							getSpinnerData("Regions",str);
							
							spzone.setVisibility(View.VISIBLE);
							spzoneTextView.setVisibility(View.GONE);
							SpinnerDataFlag.Zones_flag=false;
						
						}catch(NullPointerException ex){
							
							Toast.makeText(getActivity(), "Please Select Resion", Toast.LENGTH_LONG).show();
						
						}
						
					}
					
				}
				else{
					
					if(spresionTextView.getVisibility()!=View.VISIBLE){
						spzone.setVisibility(View.VISIBLE);
						spzoneTextView.setVisibility(View.GONE);
						setAdapters();
					}
					else{
						
						Toast.makeText(getActivity(), "Please Select Resion", Toast.LENGTH_LONG).show();
						
					}
				}
			}
		});
		
		spgroupchurchTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(SpinnerDataFlag.GroupChurches_flag){
					
					if(mPreferenceHelper.getString(Commons.USER_ROLE).equals("Group Church Pastor")){
					
						mGrpChurchList.add(mPreferenceHelper.getString(Commons.USER_DEFVALUE));
						spgroupchurch.setVisibility(View.VISIBLE);
						spgroupchurchTextView.setVisibility(View.GONE);
						setAdapters();
						SpinnerDataFlag.GroupChurches_flag=false;
						
					}else{
						
						try{
						
							String str=spzone.getSelectedItem().toString();
							Log.e("selected value", str);
							
							Methods.showProgressDialog(getActivity());
							getSpinnerData("Zones",str);
							
							spgroupchurch.setVisibility(View.VISIBLE);
							spgroupchurchTextView.setVisibility(View.GONE);
							SpinnerDataFlag.GroupChurches_flag=false;
							
						}catch(NullPointerException ex){
							
							Toast.makeText(getActivity(), "Please Select Zone", Toast.LENGTH_LONG).show();
						
						}
						
					}
					
				
				}else{
					if(spzoneTextView.getVisibility()!=View.VISIBLE){
					
						spgroupchurch.setVisibility(View.VISIBLE);
						spgroupchurchTextView.setVisibility(View.GONE);
						setAdapters();
					
					}else{
						
						Toast.makeText(getActivity(), "Please Select Zone", Toast.LENGTH_LONG).show();
					}
				}
			}
		});
	
		spchurchTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(SpinnerDataFlag.Churches_flag){
					
					if(mPreferenceHelper.getString(Commons.USER_ROLE).equals("Church Pastor")){
					
						mChurchList.add(mPreferenceHelper.getString(Commons.USER_DEFVALUE));
						spchurch.setVisibility(View.VISIBLE);
						spchurchTextView.setVisibility(View.GONE);
						setAdapters();
						SpinnerDataFlag.Churches_flag=false;
						
					}else{
						
						try{
							//TvFolk1.getVisibility() == View.VISIBLE
							if(spgroupchurchTextView.getVisibility()!=View.VISIBLE){
								String str=spgroupchurch.getSelectedItem().toString();
								Log.e("selected value", str);
							
								Methods.showProgressDialog(getActivity());
								getSpinnerData("Group Churches",str);
							
								spchurch.setVisibility(View.VISIBLE);
								spchurchTextView.setVisibility(View.GONE);
								SpinnerDataFlag.Churches_flag=false;
							
							}else{
								
								Toast.makeText(getActivity(), "Please Select Group Church", Toast.LENGTH_LONG).show();
							}
						}catch(NullPointerException ex){
							
							Toast.makeText(getActivity(), "Please Select Group Church", Toast.LENGTH_LONG).show();
						
						}
						
					}
					
				}else{
					
					if(spgroupchurchTextView.getVisibility()!=View.VISIBLE){
						spchurch.setVisibility(View.VISIBLE);
						spchurchTextView.setVisibility(View.GONE);
						setAdapters();
					}else{
						
						Toast.makeText(getActivity(), "Please Select Group Church", Toast.LENGTH_LONG).show();
						
					}
				}
			}
		});

		sppcfTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(SpinnerDataFlag.PCFs_flag){
					
					if(mPreferenceHelper.getString(Commons.USER_ROLE).equals("PCF Leader")){
					
						mPCFList.add(mPreferenceHelper.getString(Commons.USER_DEFVALUE));
						sppcf.setVisibility(View.VISIBLE);
						sppcfTextView.setVisibility(View.GONE);
						setAdapters();
						SpinnerDataFlag.PCFs_flag=false;
						
					}else{
						
						try{
						
							String str=spchurch.getSelectedItem().toString();
							Log.e("selected value", str);
							
							Methods.showProgressDialog(getActivity());
							getSpinnerData("Churches",str);
							
							sppcf.setVisibility(View.VISIBLE);
							sppcfTextView.setVisibility(View.GONE);
							SpinnerDataFlag.PCFs_flag=false;
					
						}catch(NullPointerException ex){
							
							Toast.makeText(getActivity(), "Please Select Church", Toast.LENGTH_LONG).show();
						
						}
						
					}
					
				}else{
					
					if(spchurchTextView.getVisibility()!=View.VISIBLE){
					
						sppcf.setVisibility(View.VISIBLE);
						sppcfTextView.setVisibility(View.GONE);
						setAdapters();
					
					}else{
						
						Toast.makeText(getActivity(), "Please Select Church", Toast.LENGTH_LONG).show();
					}
				}
			}
		});
		
		spSeniorCellTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(SpinnerDataFlag.SeniorCells_flag){
					
					if(mPreferenceHelper.getString(Commons.USER_ROLE).equals("Senior Cell Leader")){
					
						mSeniorCellList.add(mPreferenceHelper.getString(Commons.USER_DEFVALUE));
						spSeniorCell.setVisibility(View.VISIBLE);
						spSeniorCellTextView.setVisibility(View.GONE);
						setAdapters();
						SpinnerDataFlag.SeniorCells_flag=false;
						
					}else{
						
						try{
						
							String str=sppcf.getSelectedItem().toString();
							Log.e("selected value", str);
							
							Methods.showProgressDialog(getActivity());
							getSpinnerData("PCFs",str);
							
							spSeniorCell.setVisibility(View.VISIBLE);
							spSeniorCellTextView.setVisibility(View.GONE);
							SpinnerDataFlag.SeniorCells_flag=false;
							
						}catch(NullPointerException ex){
							
							Toast.makeText(getActivity(), "Please Select Pcf", Toast.LENGTH_LONG).show();
						
						}
						
					}
					
				
				}else{
					
					spSeniorCell.setVisibility(View.VISIBLE);
					spSeniorCellTextView.setVisibility(View.GONE);
					setAdapters();
					
					
				}
			}
		});

		spCellTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(SpinnerDataFlag.Cells_flag){
					
					if(mPreferenceHelper.getString(Commons.USER_ROLE).equals("Cell Leader")){
					
						mCellList.add(mPreferenceHelper.getString(Commons.USER_DEFVALUE));
						spCell.setVisibility(View.VISIBLE);
						spCellTextView.setVisibility(View.GONE);
						setAdapters();
						SpinnerDataFlag.Cells_flag=false;
						
					}else{
						
						try{
						
							String str=spSeniorCell.getSelectedItem().toString();
							Log.e("selected value", str);
							
							Methods.showProgressDialog(getActivity());
							getSpinnerData("Senior Cells",str);
							
							spCell.setVisibility(View.VISIBLE);
							spCellTextView.setVisibility(View.GONE);
							SpinnerDataFlag.Cells_flag=false;	
							
						}catch(NullPointerException ex){
							
							Toast.makeText(getActivity(), "Please Select Senior cell", Toast.LENGTH_LONG).show();
						
						}
						
					}
					
				}else{
					
					spCell.setVisibility(View.VISIBLE);
					spCellTextView.setVisibility(View.GONE);
					setAdapters();
				}
			}
		});
		
		sppriorityeTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sppriorityeTextView.setVisibility(View.GONE);
				sppriority.setVisibility(View.VISIBLE);
			}
		});
		
		spstatusTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				spstatusTextView.setVisibility(View.GONE);
				spstatus.setVisibility(View.VISIBLE);
			}
		});
		
		
		if(UserRoll.equals("PCF Leader")){
			layoutRegion.setVisibility(View.GONE);
			layoutzone.setVisibility(View.GONE);
			layoutchurchgroup.setVisibility(View.GONE);
			layoutchurch.setVisibility(View.GONE);

			cellLayout.setVisibility(View.GONE);
			/*pcflayout.setVisibility(View.GONE);
			srlayout.setVisibility(View.GONE);
			cellLayout.setVisibility(View.GONE);
			*/
		}
		
		if(UserRoll.equals("Zonal Pastor")){
			
			layoutRegion.setVisibility(View.GONE);
			cellLayout.setVisibility(View.GONE);
			/*srlayout.setVisibility(View.GONE);
			cellLayout.setVisibility(View.GONE);*/
		}
		
		if(UserRoll.equals("Group Church Pastor")){
			/*layoutRegion.setVisibility(View.GONE);
			layoutzone.setVisibility(View.GONE);
			*/
			layoutRegion.setVisibility(View.GONE);
			layoutzone.setVisibility(View.GONE);
			cellLayout.setVisibility(View.GONE);
		/*
			pcflayout.setVisibility(View.GONE);
			srlayout.setVisibility(View.GONE);
			cellLayout.setVisibility(View.GONE);*/
		}

				
		if(UserRoll.equals("Church Pastor")){
			layoutRegion.setVisibility(View.GONE);
			layoutzone.setVisibility(View.GONE);
			layoutchurchgroup.setVisibility(View.GONE);
			cellLayout.setVisibility(View.GONE);
		}
		
		
		if(UserRoll.equals("Senior Cell Leader")){
		
			layoutRegion.setVisibility(View.GONE);
			layoutzone.setVisibility(View.GONE);
			layoutchurchgroup.setVisibility(View.GONE);
			layoutchurch.setVisibility(View.GONE);
			pcflayout.setVisibility(View.GONE);
			cellLayout.setVisibility(View.GONE);

		}
		
		if(UserRoll.equals("Cell Leader")){
			layoutRegion.setVisibility(View.GONE);
			layoutzone.setVisibility(View.GONE);
			layoutchurchgroup.setVisibility(View.GONE);
			layoutchurch.setVisibility(View.GONE);
			pcflayout.setVisibility(View.GONE);
			srlayout.setVisibility(View.GONE);
			cellLayout.setVisibility(View.GONE);

		}
		
		if(UserRoll.equals("Member")){
			layoutRegion.setVisibility(View.GONE);
			layoutzone.setVisibility(View.GONE);
			layoutchurchgroup.setVisibility(View.GONE);
			layoutchurch.setVisibility(View.GONE);
			pcflayout.setVisibility(View.GONE);
			srlayout.setVisibility(View.GONE);
			cellLayout.setVisibility(View.GONE);
		
		}
		
		
		txtFromDate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			//	showfromdate();
				fromDatePickerDialog.show();
				
			}
		});
		
		txtToDate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				toDatePickerDialog.show();
				
			}
		});
		
				
		alertDialogBuilder.setView(promptView);
		alertDialogBuilder.setCancelable(false);
		alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
		
		public void onClick(DialogInterface dialog, int id) {
			
			String resion="",zone="",groupchurch="",church="",pcf="",srcell="",cell="",fdate="",tdate="",prority="",status="",etname="";
			
			try{
				resion=spresion.getSelectedItem().toString();
			}catch(Exception ex){
				resion="";
			}
			
			try{
			 zone=spzone.getSelectedItem().toString();
			}catch(Exception ex){
				zone="";
			}
			try{
			 groupchurch=spgroupchurch.getSelectedItem().toString();
			}catch(Exception ex){
				groupchurch="";
			}
			
			try{
			church=spchurch.getSelectedItem().toString();
			}catch(Exception ex){
				church="";
			}
			try{
			 pcf=sppcf.getSelectedItem().toString();
			}catch(Exception ex){
				pcf="";
			}
			
			try{
			 srcell=spSeniorCell.getSelectedItem().toString();
			}catch(Exception ex){
				srcell="";
			}
			
			try{
			 cell=spCell.getSelectedItem().toString();
			}catch(Exception ex){
				cell="";
			}
			
			try{
				 prority=sppriority.getSelectedItem().toString();
			}catch(Exception ex){
					prority="";
			}
			
			try{
				 status=spstatus.getSelectedItem().toString();
			}catch(Exception ex){
				status="";
			}
			
			
			 fdate=txtFromDate.getText().toString();
			 tdate=txtToDate.getText().toString();
			 etname=etName.getText().toString();
					
			
			if(NetworkHelper.isOnline(getActivity())){
				Methods.showProgressDialog(getActivity());
			
				getUpdatedListMethod("First Timer",resion,zone,groupchurch,church,pcf,srcell,cell,prority,status,fdate,tdate,etname);
			
				dialog.cancel();
				
			}else{
			
				Methods.longToast("Please connect to Internet",getActivity());
			
			}
		}
	 })
	   .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

		   public void onClick(DialogInterface dialog, int id) {

			   dialog.cancel();

		   }

	   });
		AlertDialog alertD = alertDialogBuilder.create();
		alertD.show();
//		TextView textView = (TextView) alertD.findViewById(android.R.id.message);
//		textView.setTextSize(18);

	}
private void getUpdatedSpinnerData(final String tblname,final String name) {
	Methods.closeProgressDialog();
	StringRequest reqgetTopHierarchy=new StringRequest(Method.POST,LowerHierarchyService.SERVICE_URL,new Listener<String>() {

		@Override
		public void onResponse(String response) {
			Methods.closeProgressDialog();
			Log.e("droid","get getSpinnerData ---------------"+ response);

			try {
				JSONObject obj=new JSONObject(response);
				JSONArray jarray=obj.getJSONArray("message");
				
				if(tblname.equals("Regions")){
					mZoneList.clear();
				for(int i=0;i<jarray.length();i++){
					mZoneList.add(jarray.getJSONObject(i).getString("name"));
					}
				}
				
				if(tblname.equals("Zones")){
					mGrpChurchList.clear();
					for(int i=0;i<jarray.length();i++){
						mGrpChurchList.add(jarray.getJSONObject(i).getString("name"));
					}
					
					ArrayAdapter<String> adapterchurchgropu = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, mGrpChurchList);
					adapterchurchgropu.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spgroupchurch.setAdapter(adapterchurchgropu);
				
				}
				
				if(tblname.equals("Group Churches")){
					mChurchList.clear();
					
					for(int i=0;i<jarray.length();i++){
						mChurchList.add(jarray.getJSONObject(i).getString("name"));
					}
					
					ArrayAdapter<String> adapterChurch = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, mChurchList);
					adapterChurch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spchurch.setAdapter(adapterChurch);
					
					
				}
				
				if(tblname.equals("Churches")){
					mPCFList.clear();
					for(int i=0;i<jarray.length();i++){
						mPCFList.add(jarray.getJSONObject(i).getString("name"));
					}
					
					ArrayAdapter<String> adaptercell = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, mCellList);
					adaptercell.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spCell.setAdapter(adaptercell);
				}
				
				if(tblname.equals("PCFs")){
					
					mSeniorCellList.clear();
					for(int i=0;i<jarray.length();i++){
						mSeniorCellList.add(jarray.getJSONObject(i).getString("name"));
					}
					
					ArrayAdapter<String> adapterSrCell = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, mSeniorCellList);
					adapterSrCell.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spSeniorCell.setAdapter(adapterSrCell);
				}
				
				if(tblname.equals("Senior Cells")){
					
					mCellList.clear();
					
					for(int i=0;i<jarray.length();i++){
						mCellList.add(jarray.getJSONObject(i).getString("name"));
					}
					
					ArrayAdapter<String> adaptercell = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, mCellList);
					adaptercell.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spCell.setAdapter(adaptercell);
				}
				
				//setAdapters();
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	},new ErrorListener() {

		@Override
		public void onErrorResponse(VolleyError error) {
			Methods.closeProgressDialog();
			Log.d("droid","get reqgetTopHierarchy error---------------"+ error.getCause());

			if(error.networkResponse.statusCode==403){
				Methods.longToast("Access Denied", getActivity());
			}
			else
		
				Methods.closeProgressDialog();				
		}

	}){
		@Override
		protected Map<String, String> getParams() throws AuthFailureError{
			Map<String, String> params = new HashMap<String, String>();
			MeetingListRequestModel model=new MeetingListRequestModel();
			model.setUsername(mPreferenceHelper.getString(Commons.USER_EMAILID));
			model.setUserpass(mPreferenceHelper.getString(Commons.USER_PASSWORD));
			model.setTbl(tblname);
			model.setName(name);

			String dataString=gson.toJson(model, MeetingListRequestModel.class);

			Log.d("droid", dataString);
			params.put(LowerHierarchyService.DATA, dataString);
			return params;
		}
	};

	App.getInstance().addToRequestQueue(reqgetTopHierarchy, "reqgetTopHierarchy");
	reqgetTopHierarchy.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));

}

private void setAdapters() {

	ArrayAdapter<String> adapterZone = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, mZoneList);
	adapterZone.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	spzone.setAdapter(adapterZone);

	ArrayAdapter<String> adapterRegion = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, mRegionList);
	adapterRegion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	spresion.setAdapter(adapterRegion);

	ArrayAdapter<String> adapterChurch = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, mChurchList);
	adapterChurch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	spchurch.setAdapter(adapterChurch);

	ArrayAdapter<String> adapterSrCell = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, mSeniorCellList);
	adapterSrCell.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	spSeniorCell.setAdapter(adapterSrCell);

	ArrayAdapter<String> adapterchurchgropu = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, mGrpChurchList);
	adapterchurchgropu.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	spgroupchurch.setAdapter(adapterchurchgropu);

	ArrayAdapter<String> adapterPCF = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, mPCFList);
	adapterPCF.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	sppcf.setAdapter(adapterPCF);
	
	ArrayAdapter<String> adaptercell = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, mCellList);
	adaptercell.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	spCell.setAdapter(adaptercell);
	
}

private void getSpinnerData(final String tblname,final String name) {
	Methods.closeProgressDialog();
	StringRequest reqgetTopHierarchy=new StringRequest(Method.POST,LowerHierarchyService.SERVICE_URL,new Listener<String>() {

		@Override
		public void onResponse(String response) {
			Methods.closeProgressDialog();
			Log.e("droid","get getSpinnerData ---------------"+ response);

			try {
				JSONObject obj=new JSONObject(response);
				JSONArray jarray=obj.getJSONArray("message");
				
				if(tblname.equals("Regions")){
				for(int i=0;i<jarray.length();i++){
					mZoneList.add(jarray.getJSONObject(i).getString("name"));
					}
				}
				
				if(tblname.equals("Zones")){
					for(int i=0;i<jarray.length();i++){
						mGrpChurchList.add(jarray.getJSONObject(i).getString("name"));
					}
				
				}
				
				if(tblname.equals("Group Churches")){
					for(int i=0;i<jarray.length();i++){
						mChurchList.add(jarray.getJSONObject(i).getString("name"));
					}
				}
				
				if(tblname.equals("Churches")){
					for(int i=0;i<jarray.length();i++){
						mPCFList.add(jarray.getJSONObject(i).getString("name"));
					}
				}
				
				if(tblname.equals("PCFs")){
					for(int i=0;i<jarray.length();i++){
						mSeniorCellList.add(jarray.getJSONObject(i).getString("name"));
					}
				}
				
				if(tblname.equals("Senior Cells")){
					for(int i=0;i<jarray.length();i++){
						mCellList.add(jarray.getJSONObject(i).getString("name"));
					}
				}
				
				setAdapters();
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	},new ErrorListener() {

		@Override
		public void onErrorResponse(VolleyError error) {
			Methods.closeProgressDialog();
			Log.d("droid","get reqgetTopHierarchy error---------------"+ error.getCause());

			if(error.networkResponse.statusCode==403){
				Methods.longToast("Access Denied", getActivity());
			}
			else
		
				Methods.closeProgressDialog();				
		}

	}){
		@Override
		protected Map<String, String> getParams() throws AuthFailureError{
			Map<String, String> params = new HashMap<String, String>();
			MeetingListRequestModel model=new MeetingListRequestModel();
			model.setUsername(mPreferenceHelper.getString(Commons.USER_EMAILID));
			model.setUserpass(mPreferenceHelper.getString(Commons.USER_PASSWORD));
			model.setTbl(tblname);
			model.setName(name);

			String dataString=gson.toJson(model, MeetingListRequestModel.class);

			Log.d("droid", dataString);
			params.put(LowerHierarchyService.DATA, dataString);
			return params;
		}
	};

	App.getInstance().addToRequestQueue(reqgetTopHierarchy, "reqgetTopHierarchy");
	reqgetTopHierarchy.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));

}

private void getUpdatedListMethod(final String tbl,final String resion,final String zone,final String gchurch,final String church,final String pcf,final String srcell,final String cell,final String prority,final String status,final String fdate,final String todate,final String etname){

	StringRequest reqgetLowerHierarchy=new StringRequest(Method.POST,GetAllTasksService.SERVICE_URL,new Listener<String>() {

	@Override
	public void onResponse(String response) {
		Methods.closeProgressDialog();
		Log.e("droid get reqgetList Redord--------", response);



		Log.d("NonStop", "Got response");
//		if(response.contains("status"))
		if(gson.fromJson(response, ResponseMessageModel2.class).getMessage().getStatus()=="401"){
			ResponseMessageModel2 respModel=gson.fromJson(response, ResponseMessageModel2.class);
		
			if(respModel.getMessage().getStatus()=="401"){
				Methods.longToast("User name or Password is incorrect", getActivity());
			}else{
				Methods.longToast(respModel.getMessage().getMessage(), getActivity());
			}
		}else{

			Log.d("NonStop", "Going to try block");

				try {

					Log.d("NonStop", "In try block");

					JSONObject jsonobj=new JSONObject(response);
					 
					 jsonobj.getJSONObject("message");
						
					TOTAL_LIST_ITEMS=Integer.parseInt(jsonobj.getJSONObject("message").getString("total_count"));
					
						Log.e("totalcount=", ""+TOTAL_LIST_ITEMS);

					Log.d("NonStop", "Total Count: " + TOTAL_LIST_ITEMS);
					//if(pageflag)
					 Btnfooter();
					 
					 jsonarray=jsonobj.getJSONObject("message").getJSONArray("records");
				
					if(jsonarray.length()>0){
						
						EventListAdapter adapter=new EventListAdapter(getActivity(),jsonarray);
						lvMyTasks.setAdapter(adapter);
						adapter.notifyDataSetChanged();
					
					}else{
						
							Methods.longToast("No results found", getActivity());
							EventListAdapter adapter=new EventListAdapter(getActivity(),jsonarray);
							lvMyTasks.setAdapter(adapter);
							adapter.notifyDataSetChanged();
					}
				
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
		}


	}
},new ErrorListener() {

	@Override
	public void onErrorResponse(VolleyError error) {
		Methods.closeProgressDialog();
		Log.d("droid","get reqgetLowerHierarchy error---------------"+ error.getCause());

		if(error.networkResponse.statusCode==403){
			//	Methods.longToast("Access Denied", CreateSeniorCellMasterActivity.this);
		}
		//else
		//	Methods.longToast("Some Error Occured,please try again later", CreateSeniorCellMasterActivity.this);

	}

}){
	@Override
	protected Map<String, String> getParams() throws AuthFailureError{
		Map<String, String> params = new HashMap<String, String>();

		
		JSONObject jsonobj=new JSONObject();
		try {
		
			jsonobj.put("username", mPreferenceHelper.getString(Commons.USER_EMAILID));
			jsonobj.put("userpass", mPreferenceHelper.getString(Commons.USER_PASSWORD));
		
			
			
			
			//resion, zone,gchurch, church, pcf,srcell,cell,fdate, todate)
			
			JSONObject jsonfilter=new JSONObject();
			
		//	jsonfilter.put("Month", month);

			if(!resion.equals(""))
				jsonfilter.put("region", resion);
			
			if(!zone.equals(""))
				jsonfilter.put("zone", zone);
			
			if(!gchurch.equals(""))
				jsonfilter.put("group_church", gchurch);
			
			if(!church.equals(""))
				jsonfilter.put("church", church);
			
			if(!pcf.equals(""))
				jsonfilter.put("pcf", pcf);
			
			if(!srcell.equals(""))
				jsonfilter.put("senior_cell", srcell);
			
			if(!cell.equals(""))
				jsonfilter.put("cell", cell);
			
			if(!prority.equals(""))
				jsonfilter.put("priority", prority);
			
			if(!status.equals(""))
				jsonfilter.put("status", status);

			if(!fdate.equals(""))
				jsonfilter.put("from_date", fdate);
			
			if(!todate.equals(""))
				jsonfilter.put("to_date", todate);

			if(!etname.equals(""))
				jsonfilter.put("by_name", etname);

			jsonobj.put("filters", jsonfilter);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String dataString=jsonobj.toString();

		Log.d("droid", dataString);
		params.put(GetHigherHierarchyService.DATA, dataString);
		return params;
	}
};

App.getInstance().addToRequestQueue(reqgetLowerHierarchy, "reqgetLowerHierarchy");
reqgetLowerHierarchy.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));


}

public static class SpinnerDataFlag{
	public static boolean Regions_flag = true;
	public static boolean Zones_flag = true;
	public static boolean GroupChurches_flag = true;
	public static boolean Churches_flag = true;
	public static boolean PCFs_flag = true;
	public static boolean SeniorCells_flag = true;
	public static boolean Cells_flag = true;
}

/*@Override
protected void onDestroy() {
	// TODO Auto-generated method stub
	super.onDestroy();
	
	SpinnerDataFlag.Cells_flag=true;
	SpinnerDataFlag.Churches_flag=true;
	SpinnerDataFlag.GroupChurches_flag=true;
	SpinnerDataFlag.PCFs_flag=true;
	SpinnerDataFlag.Regions_flag=true;
	SpinnerDataFlag.SeniorCells_flag=true;
	SpinnerDataFlag.Zones_flag=true;
//	pageflag=true;
	
}*/

}
