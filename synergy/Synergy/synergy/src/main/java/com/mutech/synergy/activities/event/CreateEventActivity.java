package com.mutech.synergy.activities.event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

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
import com.mutech.synergy.SynergyValues.Web.CreateCellMeetingService;
import com.mutech.synergy.SynergyValues.Web.CreateEventService;
import com.mutech.synergy.SynergyValues.Web.GetAllGroupChurchesService;
import com.mutech.synergy.SynergyValues.Web.GetAllListMastersService;
import com.mutech.synergy.SynergyValues.Web.GetAllMastersService;
import com.mutech.synergy.SynergyValues.Web.GetHigherHierarchyService;
import com.mutech.synergy.activities.cellMasters.SearchFunctionActivity;
import com.mutech.synergy.activities.dashboard.FirstTimeMonthActivity;

import com.mutech.synergy.activities.task.CreateTaskActivity;
import com.mutech.synergy.models.CreateCellMeetingModel;
import com.mutech.synergy.models.CreateEventModel;
import com.mutech.synergy.models.MeetingModel.MeetingListModel;
import com.mutech.synergy.models.ResponseMessageModel2;
import com.mutech.synergy.utils.InputValidation;
import com.mutech.synergy.utils.Methods;
import com.mutech.synergy.utils.NetworkHelper;
import com.mutech.synergy.utils.PreferenceHelper;

public class CreateEventActivity extends AppCompatActivity implements OnClickListener{

	private EditText txtEventCode,txtEventName,txtVenue,txtEventManager,txtDescription;
	private TextView txtFromTime,txtToTime;
	private EditText txtFromDate,txtToDate;
	private Button btnSaveMettingDetails;
	private PreferenceHelper mPreferenceHelper;
	//private ListView listtype;
	private Gson gson;
	private Spinner sptype,speventgrpoup,spvalue;
	private DatePickerDialog fromDatePickerDialog,toDatePickerDialog;
	private TimePickerDialog fromTimePickerDialog,toTimePickerDialog;
	private SimpleDateFormat dateFormatter,dateFormatter01;
	MultiSelectionSpinner multiSelectionSpinner;
	LinearLayout participantrollLayout,valuelayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_createevent);
		initialize();
	}

	private void initialize() {
		
		txtDescription=(EditText) findViewById(R.id.txtDescription);
		valuelayout=(LinearLayout) findViewById(R.id.valuelayout);
		spvalue=(Spinner) findViewById(R.id.spvalue);
		participantrollLayout=(LinearLayout) findViewById(R.id.participantrollLayout);
		txtEventCode=(EditText) findViewById(R.id.txtEventCode);
		txtEventName=(EditText) findViewById(R.id.txtEventName);
		txtFromDate=(EditText) findViewById(R.id.txtFromDate);
		txtToDate=(EditText) findViewById(R.id.txtToDate);
		txtFromTime=(TextView) findViewById(R.id.txtFromTime);
		txtToTime=(TextView) findViewById(R.id.txtToTime);
		txtVenue=(EditText) findViewById(R.id.txtVenue);
		txtEventManager=(EditText) findViewById(R.id.txtEventManager);
		btnSaveMettingDetails=(Button) findViewById(R.id.btnSaveMettingDetails);
		sptype=(Spinner) findViewById(R.id.sptype);
		speventgrpoup=(Spinner) findViewById(R.id.speventgrpoup);
	//	listtype=(ListView) findViewById(R.id.listtype);
		
		multiSelectionSpinner = (MultiSelectionSpinner) findViewById(R.id.mySpinner);
		
		btnSaveMettingDetails.setOnClickListener(this);

		mPreferenceHelper=new PreferenceHelper(this);
		gson=new Gson();
	
		String str=mPreferenceHelper.getString(Commons.USER_ROLE);

		getSupportActionBar().setTitle("Create Event");


		txtFromDate.setOnClickListener(this);
		txtToDate.setOnClickListener(this);
		txtFromTime.setOnClickListener(this);
		txtToTime.setOnClickListener(this);
		
		
		ArrayList<String> typeList=new ArrayList<String>();
		typeList.add("Private");
		typeList.add("Public");
		typeList.add("Cancel");
		

		ArrayAdapter<String> adapterPriority = new ArrayAdapter<String>(CreateEventActivity.this, android.R.layout.simple_spinner_item, typeList);
		adapterPriority.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sptype.setAdapter(adapterPriority);
		
		
		ArrayList<String> groupList=new ArrayList<String>();
		
		if(str.equals("Regional Pastor")){
		
		groupList.add("Only Leaders");
		groupList.add("Regional");
		groupList.add("Zonal");
		groupList.add("Church Group");
		groupList.add("Church");
		groupList.add("PCF");
		groupList.add("Sr Cell");
		groupList.add("Cell");
		
		}
		
		if(str.equals("Zonal Pastor")){
		
			groupList.add("Only Leaders");
			groupList.add("Zonal");
			groupList.add("Church Group");
			groupList.add("Church");
			groupList.add("PCF");
			groupList.add("Sr Cell");
			groupList.add("Cell");
		}
		
		if(str.equals("Group Church Pastor")){
			
			groupList.add("Only Leaders");
			groupList.add("Church Group");
			groupList.add("Church");
			groupList.add("PCF");
			groupList.add("Sr Cell");
			groupList.add("Cell");
		}
		
		if(str.equals("Church Pastor")){
			
			groupList.add("Only Leaders");
			groupList.add("Church");
			groupList.add("PCF");
			groupList.add("Sr Cell");
			groupList.add("Cell");
		}
		
		if(str.equals("PCF Leader")){
			
			groupList.add("Only Leaders");
			groupList.add("PCF");
			groupList.add("Sr Cell");
			groupList.add("Cell");
		}
		
		
		if(str.equals("Senior Cell Leader")){
			
			groupList.add("Only Leaders");
			groupList.add("Sr Cell");
			groupList.add("Cell");
		}
		
		if(str.equals("Cell Leader")){
			
			groupList.add("Cell");
		}
		
		ArrayAdapter<String> adaptergroup = new ArrayAdapter<String>(CreateEventActivity.this, android.R.layout.simple_spinner_item, groupList);
		adaptergroup.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		speventgrpoup.setAdapter(adaptergroup);
		
		if(str.equals("Regional Pastor")){
			
			String[] typelist={"Cell Leader","Senior Cell Leader","PCF Leader","Church Pastor",
					"Group Church Pastor","Zonal Pastor","Foundation School Teacher",	"Call Center Operator",
					"Partnership Rep","Support Team"};
					
					 multiSelectionSpinner.setItems(typelist);
			
		}
		
		if(str.equals("Zonal Pastor")){
			
			String[] typelist={"Cell Leader","Senior Cell Leader","PCF Leader","Church Pastor",
					"Group Church Pastor","Foundation School Teacher","Call Center Operator",
					"Partnership Rep","Support Team"};
					
					 multiSelectionSpinner.setItems(typelist);
		}
		
		if(str.equals("Group Church Pastor")){
	
		String[] typelist={"Cell Leader","Senior Cell Leader","PCF Leader","Church Pastor",
			"Foundation School Teacher","Call Center Operator",
			"Partnership Rep","Support Team"};
			
			 multiSelectionSpinner.setItems(typelist);
		}
		 
		if(str.equals("Church Pastor")){
			
			String[] typelist={"Cell Leader","Senior Cell Leader","PCF Leader",
					"Foundation School Teacher","Call Center Operator",
					"Partnership Rep","Support Team"};
					
					 multiSelectionSpinner.setItems(typelist);
		}
		
		if(str.equals("PCF Leader")){
			
			String[] typelist={"Cell Leader","Senior Cell Leader"};
					 multiSelectionSpinner.setItems(typelist);
		}
		
		if(str.equals("Senior Cell Leader")){
			
			String[] typelist={"Cell Leader"};
					 multiSelectionSpinner.setItems(typelist);
		}
		
		
		
		speventgrpoup.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
				String eventgroupval=speventgrpoup.getSelectedItem().toString();
				
				if(eventgroupval.equals("Only Leaders")){
					participantrollLayout.setVisibility(view.VISIBLE);
					valuelayout.setVisibility(View.GONE);
					
				}else{
					
					participantrollLayout.setVisibility(view.GONE);
					valuelayout.setVisibility(View.VISIBLE);
					
					if(NetworkHelper.isOnline(CreateEventActivity.this)){
						Methods.showProgressDialog(CreateEventActivity.this);
						
						if(eventgroupval.equals("Regional"))
							getSpinnerData("Regions");
						
						if(eventgroupval.equals("Zonal"))
							getSpinnerData("Zones");
						
						if(eventgroupval.equals("Church Group"))
							getSpinnerData("Group Churches");
						
						if(eventgroupval.equals("Church"))
							getSpinnerData("Churches");
						
						if(eventgroupval.equals("PCF"))
							getSpinnerData("PCFs");
						
						if(eventgroupval.equals("Sr Cell"))
							getSpinnerData("Senior Cells");
						
						
						if(eventgroupval.equals("Cell"))
							getSpinnerData("Cells");
					}
					else
						Methods.longToast("Please connect to Internet", CreateEventActivity.this);
					
					
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});
		 	
		
		
		dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
		//2015-05-24 06:22:22
		dateFormatter01=new SimpleDateFormat("yyyy-MM-dd");

		txtEventManager.setText(mPreferenceHelper.getString(Commons.USER_EMAILID));

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
					break;
				
					
				}


			}
		}, newCalendar.get(Calendar.HOUR_OF_DAY), newCalendar.get(Calendar.MINUTE), true);

		toTimePickerDialog=new TimePickerDialog(this, new OnTimeSetListener() {

			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			//	txtToTime.setText(hourOfDay+":"+minute+":00");	
				
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
				break;
				}
				
				
			}
		}, newCalendar.get(Calendar.HOUR_OF_DAY), newCalendar.get(Calendar.MINUTE), true);
	}


	public boolean isValid() {

		if(!InputValidation.hasText(txtEventName)) {
			new AlertDialog.Builder(CreateEventActivity.this)
					.setCancelable(false)
					.setTitle("Mandatory Input")
					.setMessage("Please enter an event name")
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface, int i) {

						}
					})
					.show();
			return false;
		}
		if(!InputValidation.hasText(txtFromDate)) {
			new AlertDialog.Builder(CreateEventActivity.this)
					.setCancelable(false)
					.setTitle("Mandatory Input")
					.setMessage("Please enter 'From Date")
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface, int i) {

						}
					})
					.show();
			return false;
		}
		if(!InputValidation.hasText(txtToDate)) {
			new AlertDialog.Builder(CreateEventActivity.this)
					.setCancelable(false)
					.setTitle("Mandatory Input")
					.setMessage("Please enter 'To Date'")
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
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnSaveMettingDetails:

//			if(validateFields()){
			if(NetworkHelper.isOnline(this)){
				if(isValid()) {
					if(validateFields()) {
						Methods.showProgressDialog(this);
						saveEventService();
					}
				}
			}
			else
				Methods.longToast("Please connect to Internet", this);
//			}


			break;

		case R.id.txtFromDate:
			fromDatePickerDialog.show();
			break;
		case R.id.txtToDate:
			toDatePickerDialog.show();
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

	private boolean validateFields() {

		
			if(null !=txtEventName.getText().toString() && txtEventName.getText().toString().trim().length() > 0){
				if(null !=txtEventName.getText().toString() && txtEventName.getText().toString().trim().length() > 0){
					if(null !=txtFromDate.getText().toString().trim() && txtFromDate.getText().toString().trim().length() > 0){
						
						if(null !=txtFromTime.getText().toString().trim() && txtFromTime.getText().toString().trim().length() > 0){
							if(null !=txtToDate.getText().toString().trim() && txtToDate.getText().toString().trim().length() > 0){
								if(null !=txtToTime.getText().toString().trim() && txtToTime.getText().toString().trim().length() > 0){
//									if(null !=txtVenue.getText().toString().trim() && txtVenue.getText().toString().trim().length() > 0)
//									{
									if(null !=txtEventManager.getText().toString().trim() && txtEventManager.getText().toString().trim().length() > 0)
									{

										String d1=txtFromDate.getText().toString().trim();
										 String today=getToday();
										try {

											Date todate=dateFormatter.parse(today);
											Date sdate=dateFormatter.parse(d1);

											if(sdate.getTime()<todate.getTime()){

												Methods.longToast("Date should be Current or Future  Date ", this);

											}else{

												String d2=txtToDate.getText().toString().trim();
												Date edate=dateFormatter.parse(d2);

												if(edate.getTime()<todate.getTime()){

													Methods.longToast("Date should be Current or Future  Date ", this);

												}else{

													if(edate.getTime()<sdate.getTime()){

														Methods.longToast("To Date should be greater than From Date", this);

													}else{

														return true;
													}



												}
											}



										} catch (ParseException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}





									}else{
										Methods.longToast("Plesae Enter Event Manager Name or Email id", this);
									}
//									}else{
//										Methods.longToast("Please enter Meeting Venue", this);
//									}
								}else{
									Methods.longToast("Please select Event End Time", this);
								}
							}else{
								Methods.longToast("Please select Event End Date", this);
							}
						}else{
							Methods.longToast("Please select Event Start Time", this);
						}
					
					
					}else{
						Methods.longToast("Please select Event Start Date", this);
					}
				}else{
					Methods.longToast("Please Enter Event Subject", this);
				}
			}else{
				Methods.longToast("Please Enter Event Subject", this);
			}
		
		return false;

	}
	
	public  String getToday(){
	     Date date = new Date();
	     return dateFormatter.format(date);
	 }

	private void saveEventService() {
		StringRequest reqsaveEvent=new StringRequest(Method.POST,CreateEventService.SERVICE_URL,new Listener<String>() {

			@Override
			public void onResponse(String response) {
				Methods.closeProgressDialog();
				Log.e("droid","get reqsaveEvent ---------------"+ response);
				ResponseMessageModel2 model=gson.fromJson(response, ResponseMessageModel2.class);
				if(null !=model.getMessage().getMessage() && model.getMessage().getMessage().trim().length() >0){
					Methods.longToast(model.getMessage().getMessage(), CreateEventActivity.this);
				}
				finish();
			}
		},new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Methods.closeProgressDialog();
				Log.d("droid","get reqsaveEvent error---------------"+ error.getCause());

				if(error.networkResponse.statusCode==403){
					Methods.longToast("No access to create Event", CreateEventActivity.this);
				}
				else
					Methods.longToast("Some Error Occured....try again later", CreateEventActivity.this);
			}
		}){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError{
				Map<String, String> params = new HashMap<String, String>();

				String type=sptype.getSelectedItem().toString();
				
				
				CreateEventModel model=new CreateEventModel();
				model.setUsername(mPreferenceHelper.getString(Commons.USER_EMAILID));
				model.setUserpass(mPreferenceHelper.getString(Commons.USER_PASSWORD));
				model.setSubject(txtEventName.getText().toString());
				model.setAddress(txtVenue.getText().toString());
				
				model.setEvent_group(speventgrpoup.getSelectedItem().toString());
				
				String eventgroupval=speventgrpoup.getSelectedItem().toString();
				
				if(eventgroupval.equals("Only Leaders")){
					String selVal=multiSelectionSpinner.getSelectedItemsAsString();


					model.setValue(selVal);	
					
					//model.setValue();
				}else{
					
					model.setValue(spvalue.getSelectedItem().toString());
				}
				
				model.setType(type);
				model.setDescription(txtDescription.getText().toString());
				Log.e(null, "mstarthj--");
				try {
					
					String mStart = dateFormatter01.format(dateFormatter.parse(txtFromDate.getText().toString().trim()));
					String mEnd = dateFormatter01.format(dateFormatter.parse(txtToDate.getText().toString().trim()));
					
					String sdt=mStart+" "+txtFromTime.getText().toString().trim();
					String edt=mEnd+" "+txtToTime.getText().toString().trim();
					
				//	+ " "+txtToTime.getText().toString().trim()
				//	+" "+ txtFromTime.getText().toString().trim()
					
					Log.e(null, "mstart--"+sdt);
					Log.e(null, "mEnd--"+edt);
					model.setStarts_on(sdt);
					model.setEnds_on(edt);
				} catch (ParseException e1) {
					e1.printStackTrace();
				}

				String dataString=gson.toJson(model,CreateEventModel.class);

				Log.e("create event", "dataString ::::::::::::"+dataString);

				params.put(CreateCellMeetingService.DATA, dataString);

				return params;  
			}
		};

		App.getInstance().addToRequestQueue(reqsaveEvent, "reqsaveEvent");
		reqsaveEvent.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
	}
	
private void getSpinnerData(final String tbl){

		StringRequest reqgetLowerHierarchy=new StringRequest(Method.POST,GetAllMastersService.SERVICE_URL,new Listener<String>() {

		@Override
		public void onResponse(String response) {
			Methods.closeProgressDialog();
			Log.e("droid get reqgetList Redord--------", response);

			

			if(response.contains("status"))
			{
				ResponseMessageModel2 respModel=gson.fromJson(response, ResponseMessageModel2.class);
			
				if(respModel.getMessage().getStatus()=="401"){
					Methods.longToast("User name or Password is incorrect", CreateEventActivity.this);
				}else{
					Methods.longToast(respModel.getMessage().getMessage(), CreateEventActivity.this);
				}
			}else{
				
					
					try {
						
						JSONObject jsonobj=new JSONObject(response);
							
						ArrayList<String> mRegionList=new ArrayList<String>();
						 
						JSONArray jsonarray=jsonobj.getJSONArray("message");
					
						if(jsonarray.length()>0){
							
							mRegionList.clear();
							
							for(int i=0;i<jsonarray.length();i++)
							{	
								mRegionList.add(jsonarray.getJSONObject(i).getString("name"));
							}

							ArrayAdapter<String> adapter = new ArrayAdapter<String>(
									CreateEventActivity.this, android.R.layout.simple_spinner_item, mRegionList);

							adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
							spvalue.setAdapter(adapter);
							
						
						}else{
							
								Methods.longToast("No results found", CreateEventActivity.this);
								
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
			
				jsonobj.put("tbl",tbl);
					
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			String dataString=jsonobj.toString();

			Log.e("Request droid", dataString);
			params.put(GetHigherHierarchyService.DATA, dataString);
			return params;
		}
	};

	App.getInstance().addToRequestQueue(reqgetLowerHierarchy, "reqgetLowerHierarchy");
	reqgetLowerHierarchy.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));


	}


}
