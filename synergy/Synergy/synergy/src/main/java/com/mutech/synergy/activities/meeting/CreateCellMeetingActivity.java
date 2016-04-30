package com.mutech.synergy.activities.meeting;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

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
import com.mutech.synergy.SynergyValues.Web.ChruchDataService;
import com.mutech.synergy.SynergyValues.Web.CreateCellMeetingService;
import com.mutech.synergy.SynergyValues.Web.GetAllMastersService;
import com.mutech.synergy.SynergyValues.Web.GetAllTasksService;
import com.mutech.synergy.SynergyValues.Web.GetHigherHierarchyService;
import com.mutech.synergy.activities.cellMasters.DisplayMastersListActivity;
import com.mutech.synergy.activities.task.CreateTaskActivity;
import com.mutech.synergy.activities.task.CreateTaskActivity.MastersListAdapterTask;
import com.mutech.synergy.adapters.MastersListAdapter;
import com.mutech.synergy.models.CellMembersRespModel;
import com.mutech.synergy.models.CreateCellMeetingModel;
import com.mutech.synergy.models.HigherHierarchyRespModel;
import com.mutech.synergy.models.HigherHierarchyRespModel.HHSubModel;
import com.mutech.synergy.models.MeetingListRequestModel;
import com.mutech.synergy.models.MeetingModel;
import com.mutech.synergy.models.MeetingModel.MeetingListModel;
import com.mutech.synergy.models.ResponseMessageModel2;
import com.mutech.synergy.utils.GooglePlacesApiAdapter;
import com.mutech.synergy.utils.InputValidation;
import com.mutech.synergy.utils.Methods;
import com.mutech.synergy.utils.NetworkHelper;
import com.mutech.synergy.utils.PreferenceHelper;

public class CreateCellMeetingActivity extends ActionBarActivity implements OnClickListener, OnCheckedChangeListener{

	//private RadioGroup rgrpMeetingCategory;
//	private RadioButton radioButton1,radioButton2;
	private EditText txtMeetingSubject;
	private TextView txtFromTime,txtToTime,churchlable;
	private EditText txtFromDate,txtToDate;
	private Button btnSaveMettingDetails;
	private DatePickerDialog fromDatePickerDialog,toDatePickerDialog;
	private TimePickerDialog fromTimePickerDialog,toTimePickerDialog;
	private SimpleDateFormat dateFormatter,dateFormatter01;
	private PreferenceHelper mPreferenceHelper;
	private Spinner spnCellMaster,spnCellMeetingSubject,spnChruch,spnCellMeetingtype;
	private ArrayList<MeetingListModel> mResultList;
	private ArrayList<MeetingListModel> mResultList1;
	private Gson gson;
	private GooglePlacesApiAdapter mPlacesApiAdapter;
	private AutoCompleteTextView txtVenue;
	private String mAutocompleteText;
	private ArrayList<String> mChurchSubjList,meetingtype;
	private ArrayAdapter mSubjAdapter,mtypeAdpter;
	private ArrayList<String> membersList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cell_meetingdetails);
		initialize();
	}
	@SuppressLint("NewApi")
	private void initialize() {

		getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		getSupportActionBar().setHomeButtonEnabled(false);
		getSupportActionBar().setCustomView(R.layout.custom_actionbar);
		TextView tvTitle=(TextView)getSupportActionBar().getCustomView().findViewById(R.id.title_text);
		tvTitle.setText("Create Meeting");
		
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2E9AFE")));
		
		getSupportActionBar().setDisplayShowCustomEnabled(true);

		spnCellMeetingtype=(Spinner) findViewById(R.id.spnCellMeetingtype);
		//rgrpMeetingCategory=(RadioGroup) findViewById(R.id.rgrpMeetingCategory);
		//radioButton1=(RadioButton) findViewById(R.id.radioButton1);
		//radioButton2=(RadioButton) findViewById(R.id.radioButton2);
		txtMeetingSubject=(EditText) findViewById(R.id.txtMeetingSubject);
		txtFromDate=(EditText) findViewById(R.id.txtFromDate);
		txtToDate=(EditText) findViewById(R.id.txtToDate);
		txtVenue=(AutoCompleteTextView) findViewById(R.id.txtVenue);
		txtFromTime=(TextView) findViewById(R.id.txtFromTime);
		txtToTime=(TextView) findViewById(R.id.txtToTime);
		btnSaveMettingDetails=(Button) findViewById(R.id.btnSaveMettingDetails);
		spnCellMaster=(Spinner) findViewById(R.id.spnCellMaster);
		spnChruch=(Spinner) findViewById(R.id.spnChruch);
		spnCellMeetingSubject=(Spinner) findViewById(R.id.spnCellMeetingSubject);
		churchlable=(TextView) findViewById(R.id.churchlable);
		
		churchlable.setVisibility(View.GONE);
		spnChruch.setVisibility(View.GONE);
		
		
		txtFromDate.setOnClickListener(this);
		txtToDate.setOnClickListener(this);
		txtFromTime.setOnClickListener(this);
		txtToTime.setOnClickListener(this);
		btnSaveMettingDetails.setOnClickListener(this);
	//	rgrpMeetingCategory.setOnCheckedChangeListener(this);
//		spnChruch.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				
//					getMembersOfSelectedCell();
					getCellMaster1();
//			
//			}
//		});
			
			

		mPreferenceHelper=new PreferenceHelper(this);
		String str=mPreferenceHelper.getString(Commons.USER_ROLE);
		
		Log.e(null, "Role--"+str);
		
//		if(str=="Church Pastor"||str=="Group Church Pastor"||str=="Zonal Pastor"||str=="Regional Pastor"){
//			//enable
//		}else{
//			//disable
//		}
	
		gson=new Gson();
	
		mChurchSubjList=new ArrayList<String>();
		mChurchSubjList.add("Sunday Service (Weekly)");
		mChurchSubjList.add("Wednesday Service (Weekly)");
		mChurchSubjList.add("Saturday Morning Prayer (Weekly)");
		mChurchSubjList.add("Communion Service (Monthly)");
		mChurchSubjList.add("All-Night Service (Monthly)");
		mChurchSubjList.add("Bible Study Preparatory Class (Monthly)");

		mSubjAdapter = new ArrayAdapter<String>(CreateCellMeetingActivity.this, android.R.layout.simple_spinner_item, mChurchSubjList);
		mSubjAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnCellMeetingSubject.setAdapter(mSubjAdapter);
		
		
		meetingtype=new ArrayList<String>();
		meetingtype.add("Cell Meeting");
		meetingtype.add("Sunday Service");
		meetingtype.add("Wednesday Service");
		
		mtypeAdpter= new ArrayAdapter<String>(CreateCellMeetingActivity.this, android.R.layout.simple_spinner_item, meetingtype);
		mtypeAdpter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnCellMeetingtype.setAdapter(mtypeAdpter);
		
		
		
		

		if(NetworkHelper.isOnline(this)){
			Methods.showProgressDialog(this);
			getCellMaster();
			
		}
		else
			Methods.longToast("Please connect to Internet", this);

		mPlacesApiAdapter=new GooglePlacesApiAdapter(this,R.layout.list_item_autocomplete_for_google_places);
		txtVenue.setAdapter(mPlacesApiAdapter);

		txtVenue.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2,
					int arg3) {
				mAutocompleteText = s.toString().toLowerCase(Locale.getDefault());
				mPlacesApiAdapter.getFilter();
			}
		});




		dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
		//2015-05-24 06:22:22
		dateFormatter01=new SimpleDateFormat("yyyy-MM-dd");

		Calendar newCalendar = Calendar.getInstance();
		fromDatePickerDialog = new DatePickerDialog(this, new OnDateSetListener() {

			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
				txtFromDate.setText(dateFormatter.format(newDate.getTime()));
			}

		},newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

		toDatePickerDialog = new DatePickerDialog(this, new OnDateSetListener() {

			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
				txtToDate.setText(dateFormatter.format(newDate.getTime()));
			}

		},newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

		fromTimePickerDialog=new TimePickerDialog(this, new OnTimeSetListener() {

			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				txtFromTime.setText(hourOfDay+":"+minute);
				
				switch(minute){
				case 0:
					txtFromTime.setText(hourOfDay+":0"+minute);
					break;
				case 1:
					txtFromTime.setText(hourOfDay+":0"+minute);
					break;
				
				case 2:
					txtFromTime.setText(hourOfDay+":0"+minute);
					break;
				
				case 3:
					txtFromTime.setText(hourOfDay+":0"+minute);
					break;
				
				case 4:
					txtFromTime.setText(hourOfDay+":0"+minute);
					break;
				
				case 5:
					txtFromTime.setText(hourOfDay+":0"+minute);
					break;
				
				case 6:
					txtFromTime.setText(hourOfDay+":0"+minute);
					break;
				
				case 7:
					txtFromTime.setText(hourOfDay+":0"+minute);
					break;
				
				case 8:
					txtFromTime.setText(hourOfDay+":0"+minute);
					break;
				
				case 9:
					txtFromTime.setText(hourOfDay+":0"+minute);
					break;
				default:
					txtFromTime.setText(hourOfDay+":"+minute);
				
				}

			}
		}, newCalendar.get(Calendar.HOUR_OF_DAY), newCalendar.get(Calendar.MINUTE), true);

		toTimePickerDialog=new TimePickerDialog(this, new OnTimeSetListener() {

			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				
				switch(minute){
				case 0:
					txtToTime.setText(hourOfDay+":0"+minute);
					break;
				case 1:
					txtToTime.setText(hourOfDay+":0"+minute);
					break;
				
				case 2:
					txtToTime.setText(hourOfDay+":0"+minute);
					break;
				
				case 3:
					txtToTime.setText(hourOfDay+":0"+minute);
					break;
				
				case 4:
					txtToTime.setText(hourOfDay+":0"+minute);
					break;
				
				case 5:
					txtToTime.setText(hourOfDay+":0"+minute);
					break;
				
				case 6:
					txtToTime.setText(hourOfDay+":0"+minute);
					break;
				
				case 7:
					txtToTime.setText(hourOfDay+":0"+minute);
					break;
				
				case 8:
					txtToTime.setText(hourOfDay+":0"+minute);
					break;
				
				case 9:
					txtToTime.setText(hourOfDay+":0"+minute);
					break;
				default:
					txtToTime.setText(hourOfDay+":"+minute);
				
				}
				

			}
		}, newCalendar.get(Calendar.HOUR_OF_DAY), newCalendar.get(Calendar.MINUTE), true);
	}

	/*@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.radioButton1:
			//txtMeetingSubject.setVisibility(View.VISIBLE);
			//spnCellMeetingSubject.setVisibility(View.GONE);
			break;
		case R.id.radioButton2:
			//txtMeetingSubject.setVisibility(View.GONE);
			//spnCellMeetingSubject.setVisibility(View.VISIBLE);
			break;

		default:
			break;
		}
	}*/

	
////	
//	private void getMembersOfSelectedCell() {
//		StringRequest reqGetCellMember = new StringRequest(Method.POST,GetAllMastersService.SERVICE_URL, new Listener<String>() {
//			@Override
//			public void onResponse(String response) {
//				Methods.closeProgressDialog();
//				Log.d("droid", "get all cell members ---------------"
//						+ response);
//
//				membersList = new ArrayList<String>();
//
//				gson = new Gson();
//				CellMembersRespModel model = gson.fromJson(response,CellMembersRespModel.class);
//				if (null != model.getMessage()) {
//					for (int i = 0; i < model.getMessage().size(); i++) {
//						membersList.add(model.getMessage().get(i).getMemberName());
//					}	
//				}
//				Log.d("droid", "members " + membersList.size());
//				MastersListAdapterTask adapter = new MastersListAdapterTask(CreateCellMeetingActivity.this, membersList);
//				spnChruch.setAdapter(adapter);
//				adapter.notifyDataSetChanged();
//				
//			}
//		}, new ErrorListener() {
//			@Override
//			public void onErrorResponse(VolleyError error) {
//				Methods.closeProgressDialog();
//				Log.d("droid", "get all cell error---------------"
//						+ error.getCause());
//			}
//		}) {
//			@Override
//			protected Map<String, String> getParams() throws AuthFailureError {
//				Log.d("droid", "get params");
//				Map<String, String> params = new HashMap<String, String>();
//
//				MeetingListRequestModel model = new MeetingListRequestModel();
//				model.setUsername(mPreferenceHelper.getString(Commons.USER_EMAILID));
//				model.setUserpass(mPreferenceHelper.getString(Commons.USER_PASSWORD));
//				model.setChurch(model.getChurch());
//
//				String dataString = gson.toJson(model,MeetingListRequestModel.class);
//
//				Log.d("droid", "Params : " + dataString);
//				params.put(GetHigherHierarchyService.DATA, dataString);
//				return params;
//			}
//		};
//		App.getInstance().addToRequestQueue(reqGetCellMember,"reqGetCellMember");
//		reqGetCellMember.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1,1.0f));
//
//	}
	
	
//
private void getCellMaster1() {
		
		StringRequest reqgetLowerHierarchy=new StringRequest(Method.POST,GetAllMastersService.SERVICE_URL,new Listener<String>() {

			@Override
			public void onResponse(String response) {
				Methods.closeProgressDialog();
				Log.e("droid get reqgetLowerHierarchy ---------------", response);

				if(response.contains("status"))
				{
					ResponseMessageModel2 respModel=gson.fromJson(response, ResponseMessageModel2.class);
					if(respModel.getMessage().getStatus()=="401"){
						Methods.longToast("User name or Password is incorrect", CreateCellMeetingActivity.this);
					}else{
						Methods.longToast(respModel.getMessage().getMessage(), CreateCellMeetingActivity.this);
					}
				}else{
					MeetingModel mMeetingModel=gson.fromJson(response, MeetingModel.class);
					//Object meetingmsg=mMeetingModel.getMessage();
					if(null != mMeetingModel.getStatus() && mMeetingModel.getStatus().trim().length() >0){

						if(mMeetingModel.getStatus()=="401"){
							Methods.longToast("User name or Password is incorrect", CreateCellMeetingActivity.this);
						}
					}else{
						if(null !=mMeetingModel.getMessage() && mMeetingModel.getMessage().size() >0){
							mResultList1=new ArrayList<MeetingModel.MeetingListModel>();
							//if(meetingmsg instanceof JSONArray){
							mResultList1=mMeetingModel.getMessage();
							Log.d("droid", "isjsonarray----------------------------------");
							if(mResultList1.size() > 0){
								MastersListAdapter adapter=new MastersListAdapter(CreateCellMeetingActivity.this,mResultList1);
								spnChruch.setAdapter(adapter);
								adapter.notifyDataSetChanged();
							}
						}else{
							Methods.longToast("No results found", CreateCellMeetingActivity.this);
						}
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

				MeetingListRequestModel model=new MeetingListRequestModel();
				model.setUsername(mPreferenceHelper.getString(Commons.USER_EMAILID));
				model.setUserpass(mPreferenceHelper.getString(Commons.USER_PASSWORD));
				model.setTbl("Churches");
				//model.setName(mPreferenceHelper.getString(Commons.USER_DEFVALUE));

				String dataString=gson.toJson(model, MeetingListRequestModel.class);

				Log.d("droid", dataString);
				params.put(GetHigherHierarchyService.DATA, dataString);
				return params;
			}
		};

		App.getInstance().addToRequestQueue(reqgetLowerHierarchy, "reqgetLowerHierarchy");
		reqgetLowerHierarchy.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
	
	
}
	
	
	
	
	
	private void getCellMaster() {
		
		StringRequest reqgetLowerHierarchy=new StringRequest(Method.POST,GetAllMastersService.SERVICE_URL,new Listener<String>() {

			@Override
			public void onResponse(String response) {
				Methods.closeProgressDialog();
				Log.e("droid get reqgetLowerHierarchy ---------------", response);

				if(response.contains("status"))
				{
					ResponseMessageModel2 respModel=gson.fromJson(response, ResponseMessageModel2.class);
					if(respModel.getMessage().getStatus()=="401"){
						Methods.longToast("User name or Password is incorrect", CreateCellMeetingActivity.this);
					}else{
						Methods.longToast(respModel.getMessage().getMessage(), CreateCellMeetingActivity.this);
					}
				}else{
					MeetingModel mMeetingModel=gson.fromJson(response, MeetingModel.class);
					//Object meetingmsg=mMeetingModel.getMessage();
					if(null != mMeetingModel.getStatus() && mMeetingModel.getStatus().trim().length() >0){

						if(mMeetingModel.getStatus()=="401"){
							Methods.longToast("User name or Password is incorrect", CreateCellMeetingActivity.this);
						}
					}else{
						if(null !=mMeetingModel.getMessage() && mMeetingModel.getMessage().size() >0){
							mResultList=new ArrayList<MeetingModel.MeetingListModel>();
							//if(meetingmsg instanceof JSONArray){
							mResultList=mMeetingModel.getMessage();
							Log.d("droid", "isjsonarray----------------------------------");
							if(mResultList.size() > 0){
								MastersListAdapter adapter=new MastersListAdapter(CreateCellMeetingActivity.this,mResultList);
								spnCellMaster.setAdapter(adapter);
//								spnChruch.setAdapter(adapter);
								adapter.notifyDataSetChanged();
							}
						}else{
							Methods.longToast("No results found", CreateCellMeetingActivity.this);
						}
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

				MeetingListRequestModel model=new MeetingListRequestModel();
				model.setUsername(mPreferenceHelper.getString(Commons.USER_EMAILID));
				model.setUserpass(mPreferenceHelper.getString(Commons.USER_PASSWORD));
				model.setTbl("Cells");
				//model.setName(mPreferenceHelper.getString(Commons.USER_DEFVALUE));

				String dataString=gson.toJson(model, MeetingListRequestModel.class);

				Log.d("droid", dataString);
				params.put(GetHigherHierarchyService.DATA, dataString);
				return params;
			}
		};

		App.getInstance().addToRequestQueue(reqgetLowerHierarchy, "reqgetLowerHierarchy");
		reqgetLowerHierarchy.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));

//1): [24996] BasicNetwork.performRequest: Unexpected response code 417 for http://verve.indictranstech.com/api/method/church_ministry.church_ministry.doctype.member.member.create_event/



	}
	private void saveCellMeeting()
	{
//		if(validateFields()){
		if(NetworkHelper.isOnline(this)){
			if(isValid()) {
				Methods.showProgressDialog(this);
				getHigherHierarchyService();
			}
		}
		else
			Methods.longToast("Please connect to Internet", this);
//		}
	}

	public boolean isValid() {

		if(!InputValidation.hasText(txtFromDate)) {
			new AlertDialog.Builder(CreateCellMeetingActivity.this)
					.setCancelable(false)
					.setTitle("Invalid Input")
					.setMessage("Please enter valid value in the 'From Date'")
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface, int i) {

						}
					})
					.show();
			return false;
		}
		if(!InputValidation.hasText(txtToDate)) {
			new AlertDialog.Builder(CreateCellMeetingActivity.this)
					.setCancelable(false)
					.setTitle("Invalid Input")
					.setMessage("Please enter valid value in the 'To Date'")
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
	private void getHigherHierarchyService() {
		StringRequest reqsaveMeeting=new StringRequest(Method.POST,GetHigherHierarchyService.SERVICE_URL,new Listener<String>() {

			@Override
			public void onResponse(String response) {
				Methods.closeProgressDialog();
				Log.e("droid","get reqsaveMeeting ---------------"+ response);

				HigherHierarchyRespModel mHHModel=gson.fromJson(response, HigherHierarchyRespModel.class);
				//Object meetingmsg=mMeetingModel.getMessage();
				if(null != mHHModel.getStatus() && mHHModel.getStatus().trim().length() >0){

					if(mHHModel.getStatus()=="401"){
						Methods.longToast("User name or Password is incorrect", CreateCellMeetingActivity.this);
					}
				}else{
					if(null !=mHHModel.getMessage() && mHHModel.getMessage().size() >0){
						ArrayList<HHSubModel> mHHSubModel=new ArrayList<HHSubModel>();
						//if(meetingmsg instanceof JSONArray){
						mHHSubModel=mHHModel.getMessage();
						Log.d("droid", "isjsonarray----------------------------------");
						String zone= null,church= null,churchGroup= null,pcf= null,region= null,senior_cell = null;
						for(int i=0;i<mHHSubModel.size();i++){
							zone=mHHSubModel.get(i).getZone();
							church=mHHSubModel.get(i).getChurch();
							churchGroup=mHHSubModel.get(i).getChurch_group();
							pcf=mHHSubModel.get(i).getPcf();
							region=mHHSubModel.get(i).getRegion();
							senior_cell=mHHSubModel.get(i).getSenior_cell();
						}
						Methods.showProgressDialog(CreateCellMeetingActivity.this);
						callCreateCellMeetingService(zone,church,churchGroup,pcf,region,senior_cell);

					}else{
					}
				}

			}
		},new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Methods.closeProgressDialog();
				Log.d("droid","get reqsaveMeeting error---------------"+ error.getCause());

				if(error.networkResponse.statusCode==403){
					Methods.longToast("No access to create Meeting", CreateCellMeetingActivity.this);
				}
				else
					Methods.longToast("Some Error Occured....try again later", CreateCellMeetingActivity.this);
			}
		}){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError{
				Map<String, String> params = new HashMap<String, String>();

				MeetingListRequestModel model=new MeetingListRequestModel();
				model.setUsername(mPreferenceHelper.getString(Commons.USER_EMAILID));
				model.setUserpass(mPreferenceHelper.getString(Commons.USER_PASSWORD));
				model.setTbl("Cells");
				MeetingListModel model123=(MeetingListModel) spnCellMaster.getSelectedItem();
				model.setName(model123.getName().toString());
				
//				MeetingListModel model123=(MeetingListModel) spn.getSelectedItem();
//				model.setName(model123.getName().toString());

				String dataString=gson.toJson(model, MeetingListRequestModel.class);

				Log.d("droid", dataString);
				params.put(GetHigherHierarchyService.DATA, dataString);
				return params;
			}
		};

		App.getInstance().addToRequestQueue(reqsaveMeeting, "reqsaveMeeting");
		reqsaveMeeting.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
	}
	
	private void callCreateCellMeetingService(final String zone, final String church,final String churchGroup,final String pcf,final String region,final String senior_cell) {
		StringRequest reqsaveMeeting=new StringRequest(Method.POST,CreateCellMeetingService.SERVICE_URL,new Listener<String>() {

			@Override
			public void onResponse(String response) {
				Methods.closeProgressDialog();
							
				Log.e("droid","get reqsaveMeeting ---------------"+ response);
				ResponseMessageModel2 model=gson.fromJson(response, ResponseMessageModel2.class);
			
				if(response.contains("status")){
					
					try {
						JSONObject obj=new JSONObject(response);
						String statusval=obj.getJSONObject("message").getString("status");
						if(statusval.endsWith("402")){
							
							Methods.longToast(model.getMessage().getMessage(), CreateCellMeetingActivity.this);
							txtFromDate.setText("");
							txtToDate.setText("");
						}
						
						if(statusval.endsWith("401")){
							
							Methods.longToast(model.getMessage().getMessage(), CreateCellMeetingActivity.this);
							txtFromDate.setText("");
							txtToDate.setText("");
						}
					
					
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
				}else{
				
				
				if(null !=model.getMessage().getMessage() && model.getMessage().getMessage().trim().length() >0){
		
					
				
			//	txtMeetingSubject.setText("");
			//	txtFromDate.setText("");
			//	txtToDate.setText("");
			//	txtFromTime.setText("");
			//	txtToTime.setText("");
			//	txtVenue.setText("");
				
				
				
					Methods.longToast(model.getMessage().getMessage(), CreateCellMeetingActivity.this);
			
				}
				}
				/*finish();
				Intent in= new Intent(getApplicationContext(), MyMeetingListActivity.class);
				startActivity(in);*/
			}
		},new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Methods.closeProgressDialog();
				Log.d("droid","get reqsaveMeeting error---------------"+ error.getCause());

				if(error.networkResponse.statusCode==403){
					Methods.longToast("No access to create Meeting", CreateCellMeetingActivity.this);
				}
				else
					Methods.longToast("Some Error Occured....try again later", CreateCellMeetingActivity.this);
			}
		}){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError{
				Map<String, String> params = new HashMap<String, String>();

				CreateCellMeetingModel model=new CreateCellMeetingModel();
				MeetingListModel model123=(MeetingListModel) spnCellMaster.getSelectedItem();
				model.setCell(model123.getName().toString());
				model.setChurch(church);
				model.setChurch_group(churchGroup);
				model.setDoctype("Attendance Record");
				model.setPcf(pcf);
				model.setRegion(region);
				model.setSenior_cell(senior_cell);
				model.setZone(zone);
				model.setMeeting_category(spnCellMeetingtype.getSelectedItem().toString());

				try {
					String mStart = dateFormatter01.format(dateFormatter.parse(txtFromDate.getText().toString().trim()));
					String mEnd = dateFormatter01.format(dateFormatter.parse(txtToDate.getText().toString().trim()));
					Log.e(null, "start="+mStart);
					Log.e(null, "end="+mEnd);
					String from = txtFromTime.getText().toString().trim();
					String to = txtToTime.getText().toString().trim();
					model.setFrom_date(mStart+" "+from);
					model.setTo_date(mEnd+" "+to);
					Log.e(null, "start="+mStart+" "+from);
					Log.e(null, "end="+mEnd+" "+to);
				} catch (ParseException e1) {
					e1.printStackTrace();
				}

				
				model.setMeeting_sub(txtMeetingSubject.getText().toString().trim());
				model.setVenue(txtVenue.getText().toString().trim());
				model.setUsername(mPreferenceHelper.getString(Commons.USER_EMAILID));
				model.setUserpass(mPreferenceHelper.getString(Commons.USER_PASSWORD));


				String dataString=gson.toJson(model,CreateCellMeetingModel.class);

				Log.d("droid", "dataString ::::::::::::"+dataString);

				params.put(CreateCellMeetingService.DATA, dataString);

				return params;  
			}
		};

		App.getInstance().addToRequestQueue(reqsaveMeeting, "reqsaveMeeting");
		reqsaveMeeting.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
	}
	/*private String getMeetingCategory() {
		int id = rgrpMeetingCategory.getCheckedRadioButtonId();
		switch (id) {
		case R.id.radioButton1:
			return "Cell Meeting";
		case R.id.radioButton2:
			return "Church Meeting";
		default:
			break;
		}
		return "Cell Meeting";
	}*/

	private boolean validateFields() {

		if(spnCellMeetingtype.getSelectedItem().toString().length() > 0){

			if((null !=txtMeetingSubject.getText().toString().trim() && txtMeetingSubject.getText().toString().trim().length() > 0) || (null !=spnCellMeetingSubject.getSelectedItem().toString() && spnCellMeetingSubject.getSelectedItem().toString().length() > 0))
			{
				if(null !=txtFromDate.getText().toString().trim() && txtFromDate.getText().toString().trim().length() > 0){
					if(null !=txtFromTime.getText().toString().trim() && txtFromTime.getText().toString().trim().length() > 0){
						if(null !=txtToDate.getText().toString().trim() && txtToDate.getText().toString().trim().length() > 0){
							if(null !=txtToTime.getText().toString().trim() && txtToTime.getText().toString().trim().length() > 0){
//								if(null !=txtVenue.getText().toString().trim() && txtVenue.getText().toString().trim().length() > 0)
//								{
									//long oneDay = 24 * 60 * 60 * 1000
									
									/*try {
										
										String d1=txtFromDate.getText().toString().trim();
										 String today=getToday();
										
											Date todate=dateFormatter.parse(today);
											Date sdate=dateFormatter.parse(d1);
										
										if(sdate.getTime()<todate.getTime()){
											
											Methods.longToast("Plesae Enter Valid Date", this);
									
										}else{
											
											String d2=txtToDate.getText().toString().trim();
											Date edate=dateFormatter.parse(d2);
											
											if(edate.getTime()<todate.getTime()){
												
												Methods.longToast("Plesae Enter Valid Date", this);
										
											}else{
												
												if(edate.getTime()<sdate.getTime()){
													
													Methods.longToast("Plesae Enter Valid Date", this);
													
												}else{
											
													return true;
												}
												
											
											
											}
										}
										
										
									
									} catch (ParseException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}*/
									
									
									
									
									
//									return true;
//								}else{
//									Methods.longToast("Please enter Meeting Venue", this);
//								}
								return true;
							}else{
								Methods.longToast("Please select Meeting To Time", this);
							}
						}else{
							Methods.longToast("Please select Meeting To Date", this);
						}
					}else{
						Methods.longToast("Please select Meeting From Time", this);
					}
				}else{
					Methods.longToast("Please select Meeting From Date", this);
				}
			}else{
				Methods.longToast("Meeting Subject is Blank", this);
			}
		}else{
			Methods.longToast("Please select Meeting Category", this);
		}
		return false;

	}

	public  String getToday(){
	     Date date = new Date();
	     return dateFormatter.format(date);
	 }
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.txtFromDate:
			fromDatePickerDialog.show();
			break;
		case R.id.txtToDate:
			toDatePickerDialog.show();
			break;
		case R.id.btnSaveMettingDetails:
			saveCellMeeting();			
			break;
		case R.id.txtFromTime:
			fromTimePickerDialog.show();
			break;
		case R.id.txtToTime:
			toTimePickerDialog.show();
			break;
			
		
		default:
			break;
		}
	}

	
	
	
	public class MastersListAdapterTask extends BaseAdapter{

		private Context mContext;
		private ArrayList<String> mList;

		public MastersListAdapterTask(Context context,
				ArrayList<String> mCellList) {
			mContext=context;
			mList=new ArrayList<String>();
			mList=mCellList;
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
				convertView.setTag(mHolder);
			} else {
				mHolder = (Holder) convertView.getTag();
			}
			mHolder.txtMasterName.setText(mList.get(position));

			return convertView;
		}
	}

	static class Holder{
		private TextView txtMasterName;
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		
	}


}
