package com.mutech.synergy.activities.cellMasters;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ActionBar.LayoutParams;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.mutech.synergy.App;
import com.mutech.synergy.R;
import com.mutech.synergy.SynergyValues;
import com.mutech.synergy.SynergyValues.Commons;
import com.mutech.synergy.SynergyValues.Web.GetAllListMastersService;
import com.mutech.synergy.SynergyValues.Web.GetAllMastersService;
import com.mutech.synergy.SynergyValues.Web.GetHigherHierarchyService;
import com.mutech.synergy.SynergyValues.Web.LowerHierarchyService;
import com.mutech.synergy.SynergyValues.Web.ShowAllMembersService;

import com.mutech.synergy.activities.AttendanceHistory;
import com.mutech.synergy.activities.CAttendanceHistory;
import com.mutech.synergy.activities.CellLeaderMsg;
import com.mutech.synergy.activities.ChurchAttendanceHistory;
import com.mutech.synergy.activities.MemberProfile;
import com.mutech.synergy.activities.ShortBio;
import com.mutech.synergy.activities.ShortBioSrCell;
import com.mutech.synergy.activities.ViewMembers;
import com.mutech.synergy.activities.dashboard.FirstTimeMonthActivity;
import com.mutech.synergy.activities.dashboard.FirstTimeMonthActivity.SpinnerDataFlag;
import com.mutech.synergy.activities.event.CreateEventActivity;
import com.mutech.synergy.activities.event.EventListActivity;
import com.mutech.synergy.adapters.MastersListAdapter;
import com.mutech.synergy.models.AllMembersResponseModel;
import com.mutech.synergy.models.AllMembersResponseModel.AllMemSubResponseModel;
import com.mutech.synergy.models.MeetingModel.MeetingListModel;
import com.mutech.synergy.models.MeetingListRequestModel;
import com.mutech.synergy.models.MeetingModel;
import com.mutech.synergy.models.MemberShortProfile;
import com.mutech.synergy.models.ResponseMessageModel2;
import com.mutech.synergy.utils.Methods;
import com.mutech.synergy.utils.NetworkHelper;
import com.mutech.synergy.utils.PreferenceHelper;

public class AllMemberListActivity extends ActionBarActivity implements OnItemClickListener{

	private ListView lvAllMembers;
	private ArrayList<String> mMembersList;
	private ArrayList<AllMemSubResponseModel> subModelList;
	private ArrayList<MeetingListModel> mResultList;
	MeetingListRequestModel model;

	private DatePickerDialog fromDatePickerDialog,toDatePickerDialog;
	private SimpleDateFormat dateFormatter,dateFormatter01;
	Calendar newCalendar;
	
	private PreferenceHelper mPreferenceHelper;
	private Gson gson;
	String UserRoll;
	public int TOTAL_LIST_ITEMS;
    public int NUM_ITEMS_PAGE   = 20;
    private int noOfBtns;
    private Button[] btns;
	
	JSONObject jsonobj;
	JSONArray jsonarray;
	//ImageView filterimg;
	TextView txtcount;
	TextView txtFromDate,txtToDate;
	Spinner spresion,spzone,sppcf,spgroupchurch,spchurch,spSeniorCell,spCell;
	private ArrayList<String> mZoneList,mRegionList,mChurchList,mSeniorCellList,mGrpChurchList,mPCFList,mCellList;
	private Dialog dialogPopup=null;
	private Button btnviewgivinghistory,btnviewcellattendancehistory,btnviewchurchattendancehistory,btnviewprofile;
	String name1,cellcode,role;
	String memberno = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_allmembers);
		initialize();
	}

	@SuppressLint("NewApi")
	private void initialize() {

		    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		    getSupportActionBar().setHomeButtonEnabled(false);
			getSupportActionBar().setCustomView(R.layout.custom_actionbar);
			txtcount=(TextView)getSupportActionBar().getCustomView().findViewById(R.id.title_text);
			
			getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2E9AFE")));
			
			txtcount.setText("Members   ");
			getSupportActionBar().setDisplayShowCustomEnabled(true);

		cellcode=getIntent().getStringExtra("cellcode");
		role = getIntent().getStringExtra("role");
		
		lvAllMembers=(ListView) findViewById(R.id.lvAllMembers);
		lvAllMembers.setOnItemClickListener(this);
		mMembersList=new ArrayList<String>();

		mPreferenceHelper=new PreferenceHelper(this);
		gson=new Gson();
		

		mZoneList=new ArrayList<String>();
		mRegionList=new ArrayList<String>();
		mChurchList=new ArrayList<String>();
		mSeniorCellList=new ArrayList<String>();
		mGrpChurchList=new ArrayList<String>();
		mPCFList=new ArrayList<String>();
		mCellList=new ArrayList<String>();
		
		UserRoll=mPreferenceHelper.getString(Commons.USER_ROLE);
		
		dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
		//2015-05-24 06:22:22
		dateFormatter01=new SimpleDateFormat("yyyy-MM-dd");

		newCalendar = Calendar.getInstance();
		
		fromDatePickerDialog = new DatePickerDialog(AllMemberListActivity.this, new OnDateSetListener() {

			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
				txtFromDate.setText(dateFormatter.format(newDate.getTime()));
			}

		},newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

		toDatePickerDialog = new DatePickerDialog(AllMemberListActivity.this, new OnDateSetListener() {
			
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
				txtToDate.setText(dateFormatter.format(newDate.getTime()));
			}
		
		},newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

		fromDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
		toDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

		if(NetworkHelper.isOnline(AllMemberListActivity.this)){
			Methods.showProgressDialog(AllMemberListActivity.this);

			switch(role) {

				case "Member":
					getListNew("Member", "1", "", "", "", "", "", "", "", "", "");
					break;

				case "Church":
					getListNew("Member","1","","","",cellcode,"","","","","");
					break;

				case "Region":
					getListNew("Member","1",cellcode,"","","","","","","","");
					break;

				case "Zone":
					getListNew("Member","1","",cellcode,"","","","","","","");
					break;

				case "Group Church":
					getListNew("Member","1","","",cellcode,"","","","","","");
					break;

				case "New Converts":
					txtcount.setText("New Converts   ");
					getListNew("New Converts","1","","","",cellcode,"","","","","");
					break;

				case "First Timer":
					txtcount.setText("First Timer   ");
					getListNew("First Timer","1","","","",cellcode,"","","","","");
					break;

				case "Region New Converts":
					txtcount.setText("New Converts   ");
					getListNew("New Converts","1",cellcode,"","","","","","","","");
					break;

				case "Zone New Converts":
					txtcount.setText("New Converts   ");
					getListNew("New Converts","1","",cellcode,"","","","","","","");
					break;

				case "Group Church New Converts":
					txtcount.setText("New Converts   ");
					getListNew("New Converts","1","","",cellcode,"","","","","","");
					break;

				case "Region First Timer":
					txtcount.setText("First Timer   ");
					getListNew("First Timer","1",cellcode,"","","","","","","","");
					break;

				case "Zone First Timer":
					txtcount.setText("First Timer   ");
					getListNew("First Timer","1","",cellcode,"","","","","","","");
					break;

				case "Group Church First Timer":
					txtcount.setText("First Timer   ");
					getListNew("First Timer","1","","",cellcode,"","","","","","");
					break;

			}

		}else{

			Methods.longToast("Please connect to Internet", AllMemberListActivity.this);

		}

	/*	if (NetworkHelper.isOnline(this)){
			Methods.showProgressDialog(this);
			getListNew("Member", "1", "", "", "", "", "", "", "", "", "");

		}
		else
			Methods.longToast("Please connect to Internet", this);*/

	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_todo, menu);
		String roll=mPreferenceHelper.getString(Commons.USER_ROLE);




			MenuItem item = (MenuItem) menu.findItem(R.id.menu_addTo);
            item.setVisible(false);
		
            MenuItem item1 = (MenuItem) menu.findItem(R.id.menu_option2);
            item1.setVisible(false);
		
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
		case R.id.menu_addTo:
			
			Intent intCreateEvent=new Intent(AllMemberListActivity.this,CreateEventActivity.class);
			startActivity(intCreateEvent);
			
			break;

		case R.id.menu_option2:
			Intent intMyEventList=new Intent(AllMemberListActivity.this,EventListActivity.class);
			startActivity(intMyEventList);
			break;
			
		case R.id.menu_option3:
		
			showDialog();	
			
			
			break;

		default:
			break;
		}
		return false;
	}
	
private void getList(final String tbl){

		StringRequest reqgetLowerHierarchy=new StringRequest(Method.POST,GetAllMastersService.SERVICE_URL,new Listener<String>() {

			@Override
			public void onResponse(String response) {
				Methods.closeProgressDialog();
				Log.e("droid get reqgetLowerHierarchy ---------------", response);

				if(response.contains("status"))
				{
					ResponseMessageModel2 respModel=gson.fromJson(response, ResponseMessageModel2.class);
					if(respModel.getMessage().getStatus()=="401"){
						Methods.longToast("User name or Password is incorrect", AllMemberListActivity.this);
					}else{
						Methods.longToast(respModel.getMessage().getMessage(), AllMemberListActivity.this);
					}
				}else{
					MeetingModel mMeetingModel=gson.fromJson(response, MeetingModel.class);
					//Object meetingmsg=mMeetingModel.getMessage();
					if(null != mMeetingModel.getStatus() && mMeetingModel.getStatus().trim().length() >0){

						if(mMeetingModel.getStatus()=="401"){
							Methods.longToast("User name or Password is incorrect", AllMemberListActivity.this);
						}
					}else{
						if(null !=mMeetingModel.getMessage() && mMeetingModel.getMessage().size() >0){
							mResultList=new ArrayList<MeetingModel.MeetingListModel>();
							//if(meetingmsg instanceof JSONArray){
							mResultList=mMeetingModel.getMessage();
							Log.d("droid", "isjsonarray----------------------------------");
							if(mResultList.size() > 0){
								MastersListAdapter adapter=new MastersListAdapter(AllMemberListActivity.this,mResultList);
								lvAllMembers.setAdapter(adapter);
								adapter.notifyDataSetChanged();
							}
						}else{
							Methods.longToast("No results found", AllMemberListActivity.this);
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
						Methods.longToast("Access Denied", AllMemberListActivity.this);
				}
				//else
				//	Methods.longToast("Some Error Occured,please try again later", CreateSeniorCellMasterActivity.this);

			}

		}){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError{
				Map<String, String> params = new HashMap<String, String>();

			    model=new MeetingListRequestModel();
				model.setUsername(mPreferenceHelper.getString(Commons.USER_EMAILID));
				model.setUserpass(mPreferenceHelper.getString(Commons.USER_PASSWORD));
				model.setTbl(tbl);
				model.setRecord_name(tbl);
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


	private void getListNew2(final String tbl,final String pageno,final String resion,final String zone,final String gchurch,final String church,final String pcf,final String srcell,final String cell,final String fdate,final String todate, final String name){

		//	StringRequest reqgetLowerHierarchy=new StringRequest(Method.POST,GetAllMastersService.SERVICE_URL,new Listener<String>() {
		StringRequest reqgetLowerHierarchy=new StringRequest(Method.POST, SynergyValues.Web.SearchService.SERVICE_URL,new Listener<String>() {

			@Override
			public void onResponse(String response) {
				Methods.closeProgressDialog();
				Log.e("droid get reqResponce ---------------", response);



				if(response.contains("status"))
				{
					ResponseMessageModel2 respModel=gson.fromJson(response, ResponseMessageModel2.class);
					if(respModel.getMessage().getStatus()=="401"){
						Methods.longToast("User name or Password is incorrect", AllMemberListActivity.this);
					}else{
						Methods.longToast(respModel.getMessage().getMessage(), AllMemberListActivity.this);
					}
				}else{


					try {

						jsonobj=new JSONObject(response);
						jsonobj.getJSONObject("message");

						int i=Integer.parseInt(jsonobj.getJSONObject("message").getString("total_count"));

						TOTAL_LIST_ITEMS=Integer.parseInt(jsonobj.getJSONObject("message").getString("total_count"));

						Btnfooter();
						txtcount.setText("Members ("+i+")");
						jsonarray=jsonobj.getJSONObject("message").getJSONArray("records");

						if(jsonarray.length()>0){


							DetailAdapter adapter=new DetailAdapter(AllMemberListActivity.this,jsonarray);
							lvAllMembers.setAdapter(adapter);
							adapter.notifyDataSetChanged();


						}else{

							DetailAdapter adapter=new DetailAdapter(AllMemberListActivity.this,jsonarray);
							lvAllMembers.setAdapter(adapter);
							adapter.notifyDataSetChanged();

							Methods.longToast("No results found", AllMemberListActivity.this);
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

				if(error!=null) {
					if(error.networkResponse.statusCode==403){
						//	Methods.longToast("Access Denied", CreateSeniorCellMasterActivity.this);
					}
				}

			}

		}){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError{
				Map<String, String> params = new HashMap<String, String>();

				try{

					jsonobj=new JSONObject();

					jsonobj.put("username", mPreferenceHelper.getString(Commons.USER_EMAILID));
					jsonobj.put("userpass", mPreferenceHelper.getString(Commons.USER_PASSWORD));

					jsonobj.put("search", "member");
                    if(!church.equals(""))
                        jsonobj.put("church", church);
                    if(!gchurch.equals(""))
                        jsonobj.put("group_church", gchurch);
                    if(!zone.equals(""))
                        jsonobj.put("zone", zone);
                    if(!resion.equals(""))
                        jsonobj.put("region", resion);
                    if(!name.equals(""))
                        jsonobj.put("member", name);

					JSONObject jsonfilter=new JSONObject();

					if(!fdate.equals(""))
						jsonfilter.put("from_date", fdate);
					if(!todate.equals(""))
						jsonfilter.put("to_date", todate);
                    if(!fdate.equals("") || !todate.equals(""))
					    jsonobj.put("filters", jsonfilter);

				}catch(Exception ex){

				}

				String dataString=jsonobj.toString();//gson.toJson(model, MeetingListRequestModel.class);

				Log.e("Request droid", dataString);
				params.put(GetHigherHierarchyService.DATA, dataString);
				return params;
			}
		};

		App.getInstance().addToRequestQueue(reqgetLowerHierarchy, "reqgetLowerHierarchy");
		reqgetLowerHierarchy.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
	}

	private void getListNew(final String tbl,final String pageno,final String resion,final String zone,final String gchurch,final String church,final String pcf,final String srcell,final String cell,final String fdate,final String todate){

	//	StringRequest reqgetLowerHierarchy=new StringRequest(Method.POST,GetAllMastersService.SERVICE_URL,new Listener<String>() {
	StringRequest reqgetLowerHierarchy=new StringRequest(Method.POST,GetAllListMastersService.SERVICE_URL,new Listener<String>() {

			@Override
			public void onResponse(String response) {
				Methods.closeProgressDialog();
				Log.e("droid get reqResponce ---------------", response);

				

				if(response.contains("status"))
				{
					ResponseMessageModel2 respModel=gson.fromJson(response, ResponseMessageModel2.class);
					if(respModel.getMessage().getStatus()=="401"){
						Methods.longToast("User name or Password is incorrect", AllMemberListActivity.this);
					}else{
						Methods.longToast(respModel.getMessage().getMessage(), AllMemberListActivity.this);
					}
				}else{
					
					
				 try {
						 
					 jsonobj=new JSONObject(response);
					 jsonobj.getJSONObject("message");
						 
					 int i=Integer.parseInt(jsonobj.getJSONObject("message").getString("total_count"));
					 
					 TOTAL_LIST_ITEMS=Integer.parseInt(jsonobj.getJSONObject("message").getString("total_count"));
					 
					 Btnfooter();

					 if(role.contentEquals("New Converts") ||role.contentEquals("Region New Converts") ||role.contentEquals("Zone New Converts")||role.contentEquals("Group Church New Converts") ){
						 txtcount.setText("New Converts ("+i+")");
					 }
					 else if(role.contentEquals("First Timer") ||role.contentEquals("Region First Timer") ||role.contentEquals("Zone First Timer")||role.contentEquals("Group Church First Timer") ){
						 txtcount.setText("First Timer ("+i+")");
					 }else
					 {
						 txtcount.setText("Members ("+i+")");}


					 jsonarray=jsonobj.getJSONObject("message").getJSONArray("records");

					  name1=jsonarray.getJSONObject(0).getString("name");
						if(jsonarray.length()>0){
						
															
								DetailAdapter adapter=new DetailAdapter(AllMemberListActivity.this,jsonarray);
								lvAllMembers.setAdapter(adapter);
								adapter.notifyDataSetChanged();
							
						
						}else{
							
							DetailAdapter adapter=new DetailAdapter(AllMemberListActivity.this,jsonarray);
							lvAllMembers.setAdapter(adapter);
							adapter.notifyDataSetChanged();
							
							Methods.longToast("No results found", AllMemberListActivity.this);
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

				if(error!=null) {
					if(error.networkResponse.statusCode==403){
						//	Methods.longToast("Access Denied", CreateSeniorCellMasterActivity.this);
					}
				}
				//else
				//	Methods.longToast("Some Error Occured,please try again later", CreateSeniorCellMasterActivity.this);

			}

		}){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError{
				Map<String, String> params = new HashMap<String, String>();

			try{
				
					jsonobj=new JSONObject();
				
					jsonobj.put("username", mPreferenceHelper.getString(Commons.USER_EMAILID));
					jsonobj.put("userpass", mPreferenceHelper.getString(Commons.USER_PASSWORD));
				
					jsonobj.put("tbl",tbl);
					jsonobj.put("page_no",pageno);
				
					
					JSONObject jsonfilter=new JSONObject();

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
					
					if(!fdate.equals(""))
						jsonfilter.put("from_date", fdate);
					
					if(!todate.equals(""))
						jsonfilter.put("to_date", todate);
					
					
					jsonobj.put("filters", jsonfilter);
		
			}catch(Exception ex){
				
			}
		
				String dataString=jsonobj.toString();//gson.toJson(model, MeetingListRequestModel.class);
				
				Log.e("Request droid", dataString);
				params.put(GetHigherHierarchyService.DATA, dataString);
				return params;
			}
		};

		App.getInstance().addToRequestQueue(reqgetLowerHierarchy, "reqgetLowerHierarchy");
		reqgetLowerHierarchy.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {


		try {

			memberno=jsonarray.getJSONObject(position).getString("name");

			mPreferenceHelper.addString(Commons.MEMBER_NO, memberno);
			mPreferenceHelper.addString(Commons.FROM_ACTIVITY, "true");
			mPreferenceHelper.addString(Commons.FROM_ACTIVITY1, "true");

	/*		Intent intMemberDetails=new Intent(this,MemberInfoActivtiy.class);
			intMemberDetails.putExtra("MemberNo",jsonarray.getJSONObject(position).getString("name"));
			startActivity(intMemberDetails);*/

			dialogPopup = new Dialog(AllMemberListActivity.this);
			dialogPopup.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialogPopup.setContentView(R.layout.custom_all_member_dialogbox);

			btnviewprofile= (Button) dialogPopup.findViewById(R.id.viewprofile);
			btnviewchurchattendancehistory= (Button) dialogPopup.findViewById(R.id.viewchurchattendancehistory);
			btnviewcellattendancehistory= (Button) dialogPopup.findViewById(R.id.viewcellattendancehistory);
			btnviewgivinghistory= (Button) dialogPopup.findViewById(R.id.viewgivinghistory);


			btnviewprofile.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					Intent Int = new Intent(AllMemberListActivity.this, ShortBio.class);
					Int.putExtra("cellcode",memberno);
					Int.putExtra("role","Member");
					//Int.putExtra("cellcode", name);
					startActivity(Int);
					dialogPopup.dismiss();
				}
			});

			btnviewchurchattendancehistory.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent Int = new Intent(AllMemberListActivity.this, AttendanceHistory.class);
					Int.putExtra("churchah","churchah");
					Int.putExtra("role", "Member");
					startActivity(Int);
					dialogPopup.dismiss();
				}
			});

			btnviewcellattendancehistory.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					Intent Int = new Intent(AllMemberListActivity.this, CAttendanceHistory.class);
					//Int.putExtra("cellcode", name);
					Int.putExtra("cellah","cellah");
					startActivity(Int);
					dialogPopup.dismiss();
				}
			});

			btnviewgivinghistory.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent partner=new Intent(AllMemberListActivity.this,PartnerShipRecord.class);
					partner.putExtra("fromactivity","Allmemberlist");
					partner.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
					startActivity(partner);
					dialogPopup.dismiss();
				}
			});
			dialogPopup.show();

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	class MemberListAdapter extends BaseAdapter{

		private Context mContext;
		private ArrayList<AllMemSubResponseModel> mList;
		private Holder holder;

		public MemberListAdapter(Context context,
				ArrayList<AllMemSubResponseModel> meetingList) {
			mContext=context;
			mList=new ArrayList<AllMemSubResponseModel>();
			mList=meetingList;
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
		public View getView(final int position, View convertView, ViewGroup parent) {

			LayoutInflater layout = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			holder = new Holder();
			if (convertView == null) {
				convertView = layout.inflate(R.layout.row_masterlist, null);

				holder.txtMasterName=(TextView) convertView.findViewById(R.id.txtMasterName);
				
				
				convertView.setTag(holder);
			}else{
				holder = (Holder) convertView.getTag();
			}

			holder.txtMasterName.setText(mList.get(position).getName());
			return convertView;
		}

	}
	public static class Holder{
		private TextView txtMasterName;
	}

	
public class DetailAdapter extends BaseAdapter {

		private Context context;
		private JSONArray jsonarray;
		View row;
		
	public DetailAdapter(Context context, JSONArray Jarray) {
		this.context = context;
		this.jsonarray = Jarray;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return jsonarray.length();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub

		Object obj = null;
		try {
			obj = jsonarray.get(position);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return obj;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		row = li.inflate(R.layout.row_meeting, parent, false);
		
		
		Button btn=(Button) row.findViewById(R.id.btnMarkAttendance);
		TextView id=(TextView) row.findViewById(R.id.lblMeetingName);
		TextView membername=(TextView) row.findViewById(R.id.lblMeetingTime);
		TextView emailid=(TextView) row.findViewById(R.id.lblMeetingVenue);
		TextView name=(TextView) row.findViewById(R.id.lblMeetingsubject);

		membername.setTextSize(18);
		name.setTextSize(18);
		btn.setVisibility(View.GONE);
		id.setVisibility(View.GONE);
		membername.setVisibility(View.GONE);

		try {

            String str = "";
			if(jsonarray.getJSONObject(position).has("surname"))
                str=(jsonarray.getJSONObject(position).getString("surname").equals("null"))?"":jsonarray.getJSONObject(position).getString("surname");
            if(jsonarray.getJSONObject(position).has("member_name"))
    			name.setText(jsonarray.getJSONObject(position).getString("member_name")+" "+str);
            else
                name.setText(jsonarray.getJSONObject(position).getString("name")+" "+str);
            if(jsonarray.getJSONObject(position).has("email_id"))
			    emailid.setText(jsonarray.getJSONObject(position).getString("email_id"));
            else
                emailid.setText(jsonarray.getJSONObject(position).getString("contact_email_id"));

            //id.setText(jsonarray.getJSONObject(position).getString("name"));
			
		//	surname.setText((jsonarray.getJSONObject(position).getString("surname").equals("null"))?"":jsonarray.getJSONObject(position).getString("surname"));
		//	name.setText(jsonarray.getJSONObject(position).getString("email_id"));
	
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return row;
	}

	

}

private void Btnfooter()
{
    int val = TOTAL_LIST_ITEMS%NUM_ITEMS_PAGE;
    val = val==0?0:1;
    noOfBtns=TOTAL_LIST_ITEMS/NUM_ITEMS_PAGE+val;
     
    LinearLayout ll = (LinearLayout)findViewById(R.id.btnLay);
    ll.removeAllViews();
    
    HorizontalScrollView horizontalscroll=(HorizontalScrollView) findViewById(R.id.horizontalscroll);
    horizontalscroll.setBackgroundColor(getResources().getColor(android.R.color.white));
    
    btns    =new Button[noOfBtns];
     
    for(int i=0;i<noOfBtns;i++)
    {
        btns[i] =   new Button(this);
        btns[i].setBackgroundColor(getResources().getColor(android.R.color.transparent));
        btns[i].setText(""+(i+1));
         
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        ll.addView(btns[i], lp);
         
        final int j = i;
        btns[j].setOnClickListener(new OnClickListener() {
             
            public void onClick(View v) 
            {
               // loadList(j);
            	//Toast.makeText(DisplayMastersListActivity.this, btns[j].getText(), Toast.LENGTH_LONG).show();
            	
            	if(NetworkHelper.isOnline(AllMemberListActivity.this)){
        			Methods.showProgressDialog(AllMemberListActivity.this);
        			       				
        				getListNew("Member",btns[j].getText().toString(),"","","","","","","","","");
        			
        		}else{
        			Methods.longToast("Please connect to Internet", AllMemberListActivity.this);
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
	
	LayoutInflater layoutInflater = LayoutInflater.from(AllMemberListActivity.this);
	View promptView = layoutInflater.inflate(R.layout.filter_dialog, null);
	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AllMemberListActivity.this);
	
	LinearLayout pcflayout=(LinearLayout) promptView.findViewById(R.id.pcflayout);
	LinearLayout srlayout=(LinearLayout) promptView.findViewById(R.id.srlayout);
	LinearLayout cellLayout=(LinearLayout) promptView.findViewById(R.id.cellLayout);
	LinearLayout layoutRegion=(LinearLayout) promptView.findViewById(R.id.layoutRegion);
	LinearLayout layoutzone=(LinearLayout) promptView.findViewById(R.id.layoutzone);
	LinearLayout layoutchurchgroup=(LinearLayout) promptView.findViewById(R.id.layoutchurchgroup);
	LinearLayout layoutchurch=(LinearLayout) promptView.findViewById(R.id.layoutchurch);
	LinearLayout layoutname=(LinearLayout) promptView.findViewById(R.id.nameLayout);
	layoutname.setVisibility(View.GONE);

	final TextView spzoneTextView=(TextView) promptView.findViewById(R.id.spzoneTextView);
	final TextView spresionTextView=(TextView) promptView.findViewById(R.id.spresionTextView);
	final TextView spgroupchurchTextView=(TextView) promptView.findViewById(R.id.spgroupchurchTextView);
	final TextView spchurchTextView=(TextView) promptView.findViewById(R.id.spchurchTextView);
	final TextView sppcfTextView=(TextView) promptView.findViewById(R.id.sppcfTextView);
	final TextView spSeniorCellTextView=(TextView) promptView.findViewById(R.id.spSeniorCellTextView);
	final TextView spCellTextView=(TextView) promptView.findViewById(R.id.spCellTextView);
	final TextView nameET =(TextView) promptView.findViewById(R.id.etName);

	txtFromDate=(TextView) promptView.findViewById(R.id.txtFromDate);
	txtToDate=(TextView) promptView.findViewById(R.id.txtToDate);
	spresion=(Spinner) promptView.findViewById(R.id.spresion);
	spzone=(Spinner) promptView.findViewById(R.id.spzone);
	sppcf=(Spinner) promptView.findViewById(R.id.sppcf);
	spgroupchurch=(Spinner) promptView.findViewById(R.id.spgroupchurch);
	spchurch =(Spinner) promptView.findViewById(R.id.spchurch);
	spSeniorCell=(Spinner) promptView.findViewById(R.id.spSeniorCell);
	spCell=(Spinner) promptView.findViewById(R.id.spCell);
	
	String str=mPreferenceHelper.getString(Commons.USER_DEFVALUE);
	Log.e("default user", str);
	
	
	
	spzone.setOnItemSelectedListener(new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			
			String str=spzone.getSelectedItem().toString();
			Methods.showProgressDialog(AllMemberListActivity.this);
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
			Methods.showProgressDialog(AllMemberListActivity.this);
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
			Methods.showProgressDialog(AllMemberListActivity.this);
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
			Methods.showProgressDialog(AllMemberListActivity.this);
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
			Methods.showProgressDialog(AllMemberListActivity.this);
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
						
						Methods.showProgressDialog(AllMemberListActivity.this);
						getSpinnerData("Regions",str);
						
						spzone.setVisibility(View.VISIBLE);
						spzoneTextView.setVisibility(View.GONE);
						SpinnerDataFlag.Zones_flag=false;
					
					}catch(NullPointerException ex){
						
						Toast.makeText(AllMemberListActivity.this, "Please Select Resion", Toast.LENGTH_LONG).show();
					
					}
					
				}
				
			}
			else{
				
				if(mPreferenceHelper.getString(Commons.USER_ROLE).equals("Zonal Pastor")){
					
					spzone.setVisibility(View.VISIBLE);
					spzoneTextView.setVisibility(View.GONE);
					setAdapters();
				
				}
				else
				{
					
					
					if(spresionTextView.getVisibility()!=View.VISIBLE){
						spzone.setVisibility(View.VISIBLE);
						spzoneTextView.setVisibility(View.GONE);
						setAdapters();
					}
					else{
						
						Toast.makeText(AllMemberListActivity.this, "Please Select Resion", Toast.LENGTH_LONG).show();
						
					}
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
						
						Methods.showProgressDialog(AllMemberListActivity.this);
						getSpinnerData("Zones",str);
						
						spgroupchurch.setVisibility(View.VISIBLE);
						spgroupchurchTextView.setVisibility(View.GONE);
						SpinnerDataFlag.GroupChurches_flag=false;
						
					}catch(NullPointerException ex){
						
						Toast.makeText(AllMemberListActivity.this, "Please Select Zone", Toast.LENGTH_LONG).show();
					
					}
					
				}
				
			
			}else{
				
				if(mPreferenceHelper.getString(Commons.USER_ROLE).equals("Group Church Pastor")){
					
					spgroupchurch.setVisibility(View.VISIBLE);
					spgroupchurchTextView.setVisibility(View.GONE);
					setAdapters();
			
				}
				else
				{
					if(spzoneTextView.getVisibility()!=View.VISIBLE){
					
						spgroupchurch.setVisibility(View.VISIBLE);
						spgroupchurchTextView.setVisibility(View.GONE);
						setAdapters();
					
					}else{
						
						Toast.makeText(AllMemberListActivity.this, "Please Select Zone", Toast.LENGTH_LONG).show();
					}
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
						
							Methods.showProgressDialog(AllMemberListActivity.this);
							getSpinnerData("Group Churches",str);
						
							spchurch.setVisibility(View.VISIBLE);
							spchurchTextView.setVisibility(View.GONE);
							SpinnerDataFlag.Churches_flag=false;
						
						}else{
							
							Toast.makeText(AllMemberListActivity.this, "Please Select Group Church", Toast.LENGTH_LONG).show();
						}
					}catch(NullPointerException ex){
						
						Toast.makeText(AllMemberListActivity.this, "Please Select Group Church", Toast.LENGTH_LONG).show();
					
					}
					
				}
				
			}else{
				
				if(mPreferenceHelper.getString(Commons.USER_ROLE).equals("Church Pastor")){
				
					spchurch.setVisibility(View.VISIBLE);
					spchurchTextView.setVisibility(View.GONE);
					setAdapters();
			
				}
				else{
					
					if(spgroupchurchTextView.getVisibility()!=View.VISIBLE){
						spchurch.setVisibility(View.VISIBLE);
						spchurchTextView.setVisibility(View.GONE);
						setAdapters();
					}else{
						
						Toast.makeText(AllMemberListActivity.this, "Please Select Group Church", Toast.LENGTH_LONG).show();
						
					}
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
						
						Methods.showProgressDialog(AllMemberListActivity.this);
						getSpinnerData("Churches",str);
						
						sppcf.setVisibility(View.VISIBLE);
						sppcfTextView.setVisibility(View.GONE);
						SpinnerDataFlag.PCFs_flag=false;
				
					}catch(NullPointerException ex){
						
						Toast.makeText(AllMemberListActivity.this, "Please Select Church", Toast.LENGTH_LONG).show();
					
					}
					
				}
				
			}else{
				
				if(mPreferenceHelper.getString(Commons.USER_ROLE).equals("PCF Leader")){
					
					sppcf.setVisibility(View.VISIBLE);
					sppcfTextView.setVisibility(View.GONE);
					setAdapters();
				
				}
				else
				{
					
					if(spchurchTextView.getVisibility()!=View.VISIBLE){
					
						sppcf.setVisibility(View.VISIBLE);
						sppcfTextView.setVisibility(View.GONE);
						setAdapters();
					
					}else{
						
						Toast.makeText(AllMemberListActivity.this, "Please Select Church", Toast.LENGTH_LONG).show();
					}
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
						
						Methods.showProgressDialog(AllMemberListActivity.this);
						getSpinnerData("PCFs",str);
						
						spSeniorCell.setVisibility(View.VISIBLE);
						spSeniorCellTextView.setVisibility(View.GONE);
						SpinnerDataFlag.SeniorCells_flag=false;
						
					}catch(NullPointerException ex){
						
						Toast.makeText(AllMemberListActivity.this, "Please Select Pcf", Toast.LENGTH_LONG).show();
					
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
						
						Methods.showProgressDialog(AllMemberListActivity.this);
						getSpinnerData("Senior Cells",str);
						
						spCell.setVisibility(View.VISIBLE);
						spCellTextView.setVisibility(View.GONE);
						SpinnerDataFlag.Cells_flag=false;	
						
					}catch(NullPointerException ex){
						
						Toast.makeText(AllMemberListActivity.this, "Please Select Senior cell", Toast.LENGTH_LONG).show();
					
					}
					
				}
				
			}else{
				
				spCell.setVisibility(View.VISIBLE);
				spCellTextView.setVisibility(View.GONE);
				setAdapters();
			}
		}
	});
	

	if(UserRoll.equals("Zonal Pastor")){
		
		layoutRegion.setVisibility(View.GONE);
	}
	
	if(UserRoll.equals("Group Church Pastor")){
		layoutRegion.setVisibility(View.GONE);
		layoutzone.setVisibility(View.GONE);
	
	}

			
	if(UserRoll.equals("Church Pastor")){
		layoutRegion.setVisibility(View.GONE);
		layoutzone.setVisibility(View.GONE);
		layoutchurchgroup.setVisibility(View.GONE);
	
	}
	
	if(UserRoll.equals("PCF Leader")){
		layoutRegion.setVisibility(View.GONE);
		layoutzone.setVisibility(View.GONE);
		layoutchurchgroup.setVisibility(View.GONE);
		layoutchurch.setVisibility(View.GONE);
	
	}
	
	
	
	if(UserRoll.equals("Senior Cell Leader")){
	
		layoutRegion.setVisibility(View.GONE);
		layoutzone.setVisibility(View.GONE);
		layoutchurchgroup.setVisibility(View.GONE);
		layoutchurch.setVisibility(View.GONE);
		pcflayout.setVisibility(View.GONE);
	
		
	}
	
	if(UserRoll.equals("Cell Leader")){
		layoutRegion.setVisibility(View.GONE);
		layoutzone.setVisibility(View.GONE);
		layoutchurchgroup.setVisibility(View.GONE);
		layoutchurch.setVisibility(View.GONE);
		pcflayout.setVisibility(View.GONE);
		srlayout.setVisibility(View.GONE);
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
		
		String name="", resion="",zone="",groupchurch="",church="",pcf="",srcell="",cell="",fdate="",tdate="";

		name = nameET.getText().toString();

		try{
			if(spresionTextView.getVisibility()==View.GONE)
				resion=spresion.getSelectedItem().toString();
		}catch(Exception ex){
			resion="";
		}

		try{
			if(spzoneTextView.getVisibility()==View.GONE)
		 zone=spzone.getSelectedItem().toString();
		}catch(Exception ex){
			zone="";
		}
		
		try{
			if(spgroupchurchTextView.getVisibility()==View.GONE)
		 groupchurch=spgroupchurch.getSelectedItem().toString();
		}catch(Exception ex){
			groupchurch="";
		}
		
		try{
		if(spchurchTextView.getVisibility()==View.GONE)
		church=spchurch.getSelectedItem().toString();
		}catch(Exception ex){
			church="";
		}
		
		try{
		if(sppcfTextView.getVisibility()==View.GONE)
		 pcf=sppcf.getSelectedItem().toString();
		}catch(Exception ex){
			pcf="";
		}
		
		try{
			if(spSeniorCellTextView.getVisibility()==View.GONE)
				srcell=spSeniorCell.getSelectedItem().toString();
			
		}catch(Exception ex){
			srcell="";
		}
		try{
		if(spCellTextView.getVisibility()==View.GONE)
		 cell=spCell.getSelectedItem().toString();
		}catch(Exception ex){
			cell="";
		}
		
		
		 fdate=txtFromDate.getText().toString();
		 tdate=txtToDate.getText().toString();
				
		 if(!checkValidation(resion,zone,groupchurch,church,pcf,srcell,cell,fdate,tdate, name)){
				
			 Methods.longToast("Please select any Filter",AllMemberListActivity.this);
			 
		 }else{	
			if(NetworkHelper.isOnline(AllMemberListActivity.this)){
				Methods.showProgressDialog(AllMemberListActivity.this);
                if(name.equals(""))
				    getListNew("Member","1",resion,zone,groupchurch,church,pcf,srcell,cell,fdate,tdate);
                else
                    getListNew2("Member", "1", resion, zone, groupchurch,church,pcf,srcell,cell,fdate,tdate, name);

				dialog.cancel();
	
				
			}else{
			
				Methods.longToast("Please connect to Internet", AllMemberListActivity.this);

            }
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
//	TextView textView = (TextView) alertD.findViewById(android.R.id.message);
//	textView.setTextSize(18);


}

boolean checkValidation(String resion,String zone,String groupchurch,String church,String pcf,String srcell,String cell,String fdate,String tdate, String name){
	if(resion.equals("")){
		if(zone.equals("")){
			if(groupchurch.equals("")){
				if(church.equals("")){
					if(pcf.equals("")){
						if(srcell.equals("")){
							if(cell.equals("")){
								if(fdate.equals("") && tdate.equals("")){
										if(name.equals("")) {
											return false;
										} else {
											return true;
										}
									}else{
										return true;
									}
							}else{
								return true;
							}
						}else{
								return true;
						}
					}else{
							return true;
						  }
				}else{
					return true;
					}
			}else{
				return true;
		  }
		}else{
			return true;
		}
	}else{
		return true;
 }	
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
					
					ArrayAdapter<String> adapterchurchgropu = new ArrayAdapter<String>(AllMemberListActivity.this, android.R.layout.simple_spinner_item, mGrpChurchList);
					adapterchurchgropu.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spgroupchurch.setAdapter(adapterchurchgropu);
				
				}
				
				if(tblname.equals("Group Churches")){
					mChurchList.clear();
					
					for(int i=0;i<jarray.length();i++){
						mChurchList.add(jarray.getJSONObject(i).getString("name"));
					}
					
					ArrayAdapter<String> adapterChurch = new ArrayAdapter<String>(AllMemberListActivity.this, android.R.layout.simple_spinner_item, mChurchList);
					adapterChurch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spchurch.setAdapter(adapterChurch);
					
					
				}
				
				if(tblname.equals("Churches")){
					mPCFList.clear();
					for(int i=0;i<jarray.length();i++){
						mPCFList.add(jarray.getJSONObject(i).getString("name"));
					}
					
					ArrayAdapter<String> adaptercell = new ArrayAdapter<String>(AllMemberListActivity.this, android.R.layout.simple_spinner_item, mCellList);
					adaptercell.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spCell.setAdapter(adaptercell);
				}
				
				if(tblname.equals("PCFs")){
					
					mSeniorCellList.clear();
					for(int i=0;i<jarray.length();i++){
						mSeniorCellList.add(jarray.getJSONObject(i).getString("name"));
					}
					
					ArrayAdapter<String> adapterSrCell = new ArrayAdapter<String>(AllMemberListActivity.this, android.R.layout.simple_spinner_item, mSeniorCellList);
					adapterSrCell.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spSeniorCell.setAdapter(adapterSrCell);
				}
				
				if(tblname.equals("Senior Cells")){
					
					mCellList.clear();
					
					for(int i=0;i<jarray.length();i++){
						mCellList.add(jarray.getJSONObject(i).getString("name"));
					}
					
					ArrayAdapter<String> adaptercell = new ArrayAdapter<String>(AllMemberListActivity.this, android.R.layout.simple_spinner_item, mCellList);
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
				Methods.longToast("Access Denied", AllMemberListActivity.this);
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

	ArrayAdapter<String> adapterZone = new ArrayAdapter<String>(AllMemberListActivity.this, android.R.layout.simple_spinner_item, mZoneList);
	adapterZone.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	spzone.setAdapter(adapterZone);

	ArrayAdapter<String> adapterRegion = new ArrayAdapter<String>(AllMemberListActivity.this, android.R.layout.simple_spinner_item, mRegionList);
	adapterRegion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	spresion.setAdapter(adapterRegion);

	ArrayAdapter<String> adapterChurch = new ArrayAdapter<String>(AllMemberListActivity.this, android.R.layout.simple_spinner_item, mChurchList);
	adapterChurch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	spchurch.setAdapter(adapterChurch);

	ArrayAdapter<String> adapterSrCell = new ArrayAdapter<String>(AllMemberListActivity.this, android.R.layout.simple_spinner_item, mSeniorCellList);
	adapterSrCell.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	spSeniorCell.setAdapter(adapterSrCell);

	ArrayAdapter<String> adapterchurchgropu = new ArrayAdapter<String>(AllMemberListActivity.this, android.R.layout.simple_spinner_item, mGrpChurchList);
	adapterchurchgropu.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	spgroupchurch.setAdapter(adapterchurchgropu);

	ArrayAdapter<String> adapterPCF = new ArrayAdapter<String>(AllMemberListActivity.this, android.R.layout.simple_spinner_item, mPCFList);
	adapterPCF.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	sppcf.setAdapter(adapterPCF);
	
	ArrayAdapter<String> adaptercell = new ArrayAdapter<String>(AllMemberListActivity.this, android.R.layout.simple_spinner_item, mCellList);
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
				Methods.longToast("Access Denied", AllMemberListActivity.this);
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


	private void getGivingHistory() {

		StringRequest reqgetCellDetails=new StringRequest(Request.Method.POST, SynergyValues.Web.ViewGivingPledge.SERVICE_URL,new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				Methods.closeProgressDialog();

				Log.d("droid","get pledge ---------------"+ response);
				try {

					JSONObject jsonobj=new JSONObject(response);

					Intent partner=new Intent(AllMemberListActivity.this,PartnerShipRecord.class);
					partner.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
					startActivity(partner);
					finish();


				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				Methods.closeProgressDialog();

			}
		},new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Methods.closeProgressDialog();
				Log.d("droid","get reqgetTopHierarchy error---------------"+ error.getCause());

				if(error.networkResponse.statusCode==403){
					Methods.longToast("Access Denied", AllMemberListActivity.this);
				}
				else
					Methods.longToast("Some Error Occured,please try again later", AllMemberListActivity.this);



			}

		}){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError{
				Map<String, String> params = new HashMap<String, String>();

				MemberShortProfile model=new MemberShortProfile();
				model.setUsername(mPreferenceHelper.getString(SynergyValues.Commons.USER_EMAILID));
				model.setUserpass(mPreferenceHelper.getString(SynergyValues.Commons.USER_PASSWORD));
				model.setName(memberno);
//                model.setRole("Cell Leader");

				String dataString=gson.toJson(model, MemberShortProfile.class);

				Log.d("droid", dataString);
				params.put(SynergyValues.Web.GetHigherHierarchyService.DATA, dataString);
				return params;
			}
		};

		App.getInstance().addToRequestQueue(reqgetCellDetails, "reqgetCellDetails");
		reqgetCellDetails.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));

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

@Override
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
	
}

}
