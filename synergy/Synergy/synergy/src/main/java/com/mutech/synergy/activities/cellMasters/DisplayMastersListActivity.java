package com.mutech.synergy.activities.cellMasters;
import android.app.ActionBar.LayoutParams;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import com.mutech.synergy.SynergyValues.SpinnerDataFlag;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import com.github.mikephil.charting.data.LineRadarDataSet;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.mutech.databasedetails.CellDetailsActivity;
import com.mutech.databasedetails.PcfDetailsActivity;
import com.mutech.databasedetails.SrCellDetailsActivity;
import com.mutech.synergy.App;
import com.mutech.synergy.R;
import com.mutech.synergy.SynergyValues.Commons;
import com.mutech.synergy.SynergyValues.Web.GetAllListMastersService;
import com.mutech.synergy.SynergyValues.Web.GetAllMastersService;
import com.mutech.synergy.SynergyValues.Web.GetHigherHierarchyService;
import com.mutech.synergy.SynergyValues.Web.LowerHierarchyService;
import com.mutech.synergy.activities.AttendanceHistory;
import com.mutech.synergy.activities.CellLeaderMsg;
import com.mutech.synergy.activities.ShortBio;
import com.mutech.synergy.activities.ShortBioCell;
import com.mutech.synergy.activities.ViewMembers;
import com.mutech.synergy.activities.dashboard.FirstTimeMonthActivity;
import com.mutech.synergy.activities.event.CreateEventActivity;
import com.mutech.synergy.activities.event.EventListActivity;
import com.mutech.synergy.adapters.MastersListAdapter;

import com.mutech.synergy.models.HigherHierarchyRespModel;
import com.mutech.synergy.models.MeetingListRequestModel;
import com.mutech.synergy.models.MeetingModel;
import com.mutech.synergy.models.HigherHierarchyRespModel.HHSubModel;
import com.mutech.synergy.models.MeetingModel.MeetingListModel;
import com.mutech.synergy.models.ResponseMessageModel2;
import com.mutech.synergy.utils.Methods;
import com.mutech.synergy.utils.NetworkHelper;
import com.mutech.synergy.utils.PreferenceHelper;
import com.squareup.picasso.Picasso;
import android.widget.LinearLayout;


public class DisplayMastersListActivity extends ActionBarActivity{

	private ArrayList<MeetingListModel> mResultList;
	private ListView lvSelectedList;
	private String optionSelected,actionbarTitle;
	private PreferenceHelper mPreferenceHelper;
	private Gson gson;
	private Button btnviewprofile,btnviewattendancehistory,btncellleaderprofile,btncellleadermsg,btnviewmembers;
//	ImageView filterimg;
	TextView textView1;
	private Dialog dialogPopup=null;
	
	Spinner spresion,spzone,sppcf,spgroupchurch,spchurch,spSeniorCell,spCell;
	private ArrayList<String> mZoneList,mRegionList,mChurchList,mSeniorCellList,mGrpChurchList,mPCFList,mCellList;
	
	TextView txtFromDate,txtToDate;
	private DatePickerDialog fromDatePickerDialog,toDatePickerDialog;
	private SimpleDateFormat dateFormatter,dateFormatter01;
	Calendar newCalendar;
	String UserRoll;
	
	public int TOTAL_LIST_ITEMS;
    public int NUM_ITEMS_PAGE   = 20;
    private int noOfBtns;
    private Button[] btns;
	
	JSONObject jsonobj;
	JSONArray jsonarray;
	static boolean pageflag=true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_allmembers);
		
		//textView1=(TextView) findViewById(R.id.textView1);
	//	filterimg=(ImageView) findViewById(R.id.imageView2);
		
		jsonobj=new JSONObject();
		jsonarray=new JSONArray();
		initialize();
	}

	@SuppressLint("NewApi")
	private void initialize() {

		optionSelected=getIntent().getExtras().getString("OptionSelected");
		mPreferenceHelper=new PreferenceHelper(this);
		mResultList=new ArrayList<MeetingListModel>();
		gson=new Gson();
		
		UserRoll=mPreferenceHelper.getString(Commons.USER_ROLE);
		

		 getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		    getSupportActionBar().setHomeButtonEnabled(false);
			getSupportActionBar().setCustomView(R.layout.custom_actionbar);
			textView1=(TextView)getSupportActionBar().getCustomView().findViewById(R.id.title_text);
			
			getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2E9AFE")));
			
			
			getSupportActionBar().setDisplayShowCustomEnabled(true);

		
		
		
		if(optionSelected.equalsIgnoreCase("PCF")){
			textView1.setText("PCFs");
		}else if(optionSelected.equalsIgnoreCase("Sr Cell")){
			textView1.setText("Senior Cells");
		}else if(optionSelected.equalsIgnoreCase("Cell")){
			textView1.setText("Cells");
		}

		/*filterimg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(NetworkHelper.isOnline(DisplayMastersListActivity.this)){
					//Methods.showProgressDialog(DisplayMastersListActivity.this);
					
					showDialog();
					
					
				}else{
				
					Methods.longToast("Please connect to Internet",DisplayMastersListActivity.this);
				
				}
				
			}
		});*/
		
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
		
		fromDatePickerDialog = new DatePickerDialog(DisplayMastersListActivity.this, new OnDateSetListener() {

			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
				txtFromDate.setText(dateFormatter.format(newDate.getTime()));
			}

		},newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

		toDatePickerDialog = new DatePickerDialog(DisplayMastersListActivity.this, new OnDateSetListener() {
			
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
				txtToDate.setText(dateFormatter.format(newDate.getTime()));
			}
		
		},newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

		fromDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
		toDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

		lvSelectedList=(ListView) findViewById(R.id.lvAllMembers);
		
		lvSelectedList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				if(NetworkHelper.isOnline(DisplayMastersListActivity.this)){
					Methods.showProgressDialog(DisplayMastersListActivity.this);
				
					if(optionSelected.equalsIgnoreCase("PCF")){
					
						getList(Commons.USER_TBL_LABEL_PCF);
						String name;
						try {
							//String name=mResultList.get(position).getName();
							name = jsonarray.getJSONObject(position).getString("name");
							startActivity(new Intent(DisplayMastersListActivity.this, PcfDetailsActivity.class).putExtra("cellcode", name));
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						
						
											
						//getAllPCFMaster();
					}else if(optionSelected.equalsIgnoreCase("Sr Cell")){
						getList(Commons.USER_TBL_LABEL_SRCELLS);
						
						/*String name=mResultList.get(position).getName();
						startActivity(new Intent(DisplayMastersListActivity.this, SrCellDetailsActivity.class).putExtra("cellcode", name));
						Toast.makeText(DisplayMastersListActivity.this, name, Toast.LENGTH_LONG).show();*/
			
						String name;
						try {
							//String name=mResultList.get(position).getName();
							name = jsonarray.getJSONObject(position).getString("name");
							startActivity(new Intent(DisplayMastersListActivity.this, SrCellDetailsActivity.class).putExtra("cellcode", name));
					
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						
						//getAllSrCellMaster();
					}else if(optionSelected.equalsIgnoreCase("Cell")){
						
						/*String name=mResultList.get(position).getName();
						startActivity(new Intent(DisplayMastersListActivity.this, CellDetailsActivity.class).putExtra("cellcode", name));*/
						
						final String name;
						try {
							//String name=mResultList.get(position).getName();
							name = jsonarray.getJSONObject(position).getString("name");
							Log.d("NonStop", "Going to Cell. Name: " + name);

							dialogPopup = new Dialog(DisplayMastersListActivity.this);
							dialogPopup.requestWindowFeature(Window.FEATURE_NO_TITLE);
							dialogPopup.setContentView(R.layout.custom_cell_dialogbox);

							btnviewprofile= (Button) dialogPopup.findViewById(R.id.viewprofile);
							btnviewattendancehistory= (Button) dialogPopup.findViewById(R.id.viewattendancehistory);
							btncellleaderprofile= (Button) dialogPopup.findViewById(R.id.cellleaderprofile);
							btncellleadermsg= (Button) dialogPopup.findViewById(R.id.msgcellleader);
							btnviewmembers= (Button) dialogPopup.findViewById(R.id.viewmembers);

							btnviewprofile.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {

									Intent Int = new Intent(DisplayMastersListActivity.this, ShortBioCell.class);
									Int.putExtra("cellcode", name);
									startActivity(Int);
									dialogPopup.dismiss();
								}
							});

							btnviewattendancehistory.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {

									Intent Int = new Intent(DisplayMastersListActivity.this, AttendanceHistory.class);
									startActivity(Int);
									dialogPopup.dismiss();
								}
							});

							btncellleaderprofile.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {

									Intent Int = new Intent(DisplayMastersListActivity.this, ShortBio.class);
									Int.putExtra("cellcode", name);
									startActivity(Int);
									dialogPopup.dismiss();
								}
							});

							btncellleadermsg.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {

									Intent Int = new Intent(DisplayMastersListActivity.this, CellLeaderMsg.class);
									startActivity(Int);
									dialogPopup.dismiss();
								}
							});

							btnviewmembers.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Intent Int = new Intent(DisplayMastersListActivity.this, ViewMembers.class);
									Int.putExtra("cellcode", name);
                                    startActivity(Int);
                                    dialogPopup.dismiss();
                                }
                            });

							dialogPopup.show();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						
					}
					
					Methods.closeProgressDialog();
					
				}else{
					Methods.longToast("Please connect to Internet",DisplayMastersListActivity.this);
					Methods.closeProgressDialog();
				}
				
			}
		
		
		});
		
		
		if(NetworkHelper.isOnline(this)){
			Methods.showProgressDialog(this);
			if(optionSelected.equalsIgnoreCase("PCF")){
				//getList(Commons.USER_TBL_LABEL_PCF);
				getListNewMethod(Commons.USER_TBL_LABEL_PCF,"1");
				//getAllPCFMaster();
			}else if(optionSelected.equalsIgnoreCase("Sr Cell")){
				//getList(Commons.USER_TBL_LABEL_SRCELLS);
				getListNewMethod(Commons.USER_TBL_LABEL_SRCELLS,"1");
				//getAllSrCellMaster();
			}else if(optionSelected.equalsIgnoreCase("Cell")){
				//getList(Commons.USER_TBL_LABEL_CELLS);
				getListNewMethod(Commons.USER_TBL_LABEL_CELLS,"1");
				//getAllCellMaster();
			}
		}else{
			Methods.longToast("Please connect to Internet", this);
		}

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
			
			Intent intCreateEvent=new Intent(DisplayMastersListActivity.this,CreateEventActivity.class);
			startActivity(intCreateEvent);
			
			break;

		case R.id.menu_option2:
			Intent intMyEventList=new Intent(DisplayMastersListActivity.this,EventListActivity.class);
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

	
	
	private void getListNewMethod(final String tbl,final String pageno){

			StringRequest reqgetLowerHierarchy=new StringRequest(Method.POST,GetAllListMastersService.SERVICE_URL,new Listener<String>() {

			@Override
			public void onResponse(String response) {
				Methods.closeProgressDialog();
				Log.e("droid get reqgetList Redord--------", response);

				

				if(response.contains("status"))
				{
					ResponseMessageModel2 respModel=gson.fromJson(response, ResponseMessageModel2.class);
				
					if(respModel.getMessage().getStatus()=="401"){
						Methods.longToast("User name or Password is incorrect", DisplayMastersListActivity.this);
					}else{
						Methods.longToast(respModel.getMessage().getMessage(), DisplayMastersListActivity.this);
					}
				}else{
					
						
						try {
							
							 jsonobj=new JSONObject(response);
							 
							 jsonobj.getJSONObject("message");
							 
							 int i=Integer.parseInt(jsonobj.getJSONObject("message").getString("total_count"));
								
								TOTAL_LIST_ITEMS=Integer.parseInt(jsonobj.getJSONObject("message").getString("total_count"));
								
								Log.e("totalcount=", ""+TOTAL_LIST_ITEMS);
								
								if(optionSelected.equalsIgnoreCase("PCF")){
									textView1.setText("PCFs "+"("+i+")");
								}else if(optionSelected.equalsIgnoreCase("Sr Cell")){
									textView1.setText("Senior Cells "+"("+i+")");
								}else if(optionSelected.equalsIgnoreCase("Cell")){
									textView1.setText("Cells "+"("+i+")");
								}
								
							
						
							 Btnfooter();
							 
							 jsonarray=jsonobj.getJSONObject("message").getJSONArray("records");
						
							if(jsonarray.length()>0){

								Log.d("NonStop", "JSON Array: " + jsonarray.toString());
								DetailAdapter adapter=new DetailAdapter(DisplayMastersListActivity.this,jsonarray);
								lvSelectedList.setAdapter(adapter);
								adapter.notifyDataSetChanged();
							
							}else{
								
									Methods.longToast("No results found", DisplayMastersListActivity.this);
					
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

				MeetingListRequestModel model=new MeetingListRequestModel();
				model.setUsername(mPreferenceHelper.getString(Commons.USER_EMAILID));
				model.setUserpass(mPreferenceHelper.getString(Commons.USER_PASSWORD));
				model.setTbl(tbl);
				model.setRecord_name(tbl);
				model.setPage_no(pageno);

				String dataString=gson.toJson(model, MeetingListRequestModel.class);

				Log.e("Request", dataString);
				params.put(GetHigherHierarchyService.DATA, dataString);
				return params;
			}
		};

		App.getInstance().addToRequestQueue(reqgetLowerHierarchy, "reqgetLowerHierarchy");
		reqgetLowerHierarchy.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));


	}

	private void getUpdatedListMethod(final String tbl,final String resion,final String zone,final String gchurch,final String church,final String pcf,final String srcell,final String cell,final String fdate,final String todate){

		StringRequest reqgetLowerHierarchy=new StringRequest(Method.POST,GetAllListMastersService.SERVICE_URL,new Listener<String>() {

		@Override
		public void onResponse(String response) {
			Methods.closeProgressDialog();
			Log.e("droid get reqgetList Redord--------", response);

			

			if(response.contains("status"))
			{
				ResponseMessageModel2 respModel=gson.fromJson(response, ResponseMessageModel2.class);
			
				if(respModel.getMessage().getStatus()=="401"){
					Methods.longToast("User name or Password is incorrect", DisplayMastersListActivity.this);
				}else{
					Methods.longToast(respModel.getMessage().getMessage(), DisplayMastersListActivity.this);
				}
			}else{
				
					
					try {
						
						 jsonobj=new JSONObject(response);
						 
						 jsonobj.getJSONObject("message");
							
							TOTAL_LIST_ITEMS=Integer.parseInt(jsonobj.getJSONObject("message").getString("total_count"));
						
						Log.e("totalcount=", ""+TOTAL_LIST_ITEMS);
						

						if(optionSelected.equalsIgnoreCase("PCF")){
							
							textView1.setText("PCFs("+TOTAL_LIST_ITEMS+")");
						
						}else if(optionSelected.equalsIgnoreCase("Sr Cell")){
						
							textView1.setText("Senior Cells("+TOTAL_LIST_ITEMS+")");
						
						}else if(optionSelected.equalsIgnoreCase("Cell")){
							
							textView1.setText("Cells("+TOTAL_LIST_ITEMS+")");
						}
						
						 
						 jsonarray=jsonobj.getJSONObject("message").getJSONArray("records");
					
						if(jsonarray.length()>0){
						
							 Btnfooter();
							
							DetailAdapter adapter=new DetailAdapter(DisplayMastersListActivity.this,jsonarray);
							lvSelectedList.setAdapter(adapter);
							adapter.notifyDataSetChanged();
						
						}else{
							
								Methods.longToast("No results found", DisplayMastersListActivity.this);
								DetailAdapter adapter=new DetailAdapter(DisplayMastersListActivity.this,jsonarray);
								lvSelectedList.setAdapter(adapter);
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

			/*MeetingListRequestModel model=new MeetingListRequestModel();
			model.setUsername(mPreferenceHelper.getString(Commons.USER_EMAILID));
			model.setUserpass(mPreferenceHelper.getString(Commons.USER_PASSWORD));
			model.setTbl(tbl);
			model.setRecord_name(tbl);*/
			
			JSONObject jsonobj=new JSONObject();
			try {
			
				jsonobj.put("username", mPreferenceHelper.getString(Commons.USER_EMAILID));
				jsonobj.put("userpass", mPreferenceHelper.getString(Commons.USER_PASSWORD));
			
				jsonobj.put("tbl",tbl);
				
				
				//resion, zone,gchurch, church, pcf,srcell,cell,fdate, todate)
				
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
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			String dataString=jsonobj.toString();

			Log.e("request droid", dataString);
			params.put(GetHigherHierarchyService.DATA, dataString);
			return params;
		}
	};

	App.getInstance().addToRequestQueue(reqgetLowerHierarchy, "reqgetLowerHierarchy");
	reqgetLowerHierarchy.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));


}
	
	
	
	private void getList(final String tbl){


		StringRequest reqgetLowerHierarchy=new StringRequest(Method.POST,GetAllMastersService.SERVICE_URL,new Listener<String>() {
	//		StringRequest reqgetLowerHierarchy=new StringRequest(Method.POST,GetAllListMastersService.SERVICE_URL,new Listener<String>() {

			@Override
			public void onResponse(String response) {
				Methods.closeProgressDialog();
				Log.e("droid get reqgetList Redord--------", response);

				

				if(response.contains("status"))
				{
					ResponseMessageModel2 respModel=gson.fromJson(response, ResponseMessageModel2.class);
				
					if(respModel.getMessage().getStatus()=="401"){
						Methods.longToast("User name or Password is incorrect", DisplayMastersListActivity.this);
					}else{
						Methods.longToast(respModel.getMessage().getMessage(), DisplayMastersListActivity.this);
					}
				}else{
					MeetingModel mMeetingModel=gson.fromJson(response, MeetingModel.class);
					//Object meetingmsg=mMeetingModel.getMessage();
					
					if(null != mMeetingModel.getStatus() && mMeetingModel.getStatus().trim().length() >0){

						if(mMeetingModel.getStatus()=="401"){
							Methods.longToast("User name or Password is incorrect", DisplayMastersListActivity.this);
						}
					}else{
						if(null !=mMeetingModel.getMessage() && mMeetingModel.getMessage().size() >0){
							mResultList=new ArrayList<MeetingModel.MeetingListModel>();
							//if(meetingmsg instanceof JSONArray){
							mResultList=mMeetingModel.getMessage();
							Log.d("droid", "isjsonarray----------------------------------");
							if(mResultList.size() > 0){
								MastersListAdapter adapter=new MastersListAdapter(DisplayMastersListActivity.this,mResultList);
								lvSelectedList.setAdapter(adapter);
								adapter.notifyDataSetChanged();
							}
						}else{
							Methods.longToast("No results found", DisplayMastersListActivity.this);
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
				model.setTbl(tbl);
				model.setRecord_name(tbl);
				//model.setName(mPreferenceHelper.getString(Commons.USER_DEFVALUE));
//{"record_name":"PCFs","tbl":"PCFs","username":"nikhil.k@indictranstech.com","userpass":"password"}
				String dataString=gson.toJson(model, MeetingListRequestModel.class);

				Log.d("droid", dataString);
				params.put(GetHigherHierarchyService.DATA, dataString);
				return params;
			}
		};

		App.getInstance().addToRequestQueue(reqgetLowerHierarchy, "reqgetLowerHierarchy");
		reqgetLowerHierarchy.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));


	}

	// E/default user(26993): 3A_Zone/CHR0001/SCL0001

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
					Methods.longToast("Access Denied", DisplayMastersListActivity.this);
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
						
						ArrayAdapter<String> adapterchurchgropu = new ArrayAdapter<String>(DisplayMastersListActivity.this, android.R.layout.simple_spinner_item, mGrpChurchList);
						adapterchurchgropu.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						spgroupchurch.setAdapter(adapterchurchgropu);
					
					}
					
					if(tblname.equals("Group Churches")){
						mChurchList.clear();
						
						for(int i=0;i<jarray.length();i++){
							mChurchList.add(jarray.getJSONObject(i).getString("name"));
						}
						
						ArrayAdapter<String> adapterChurch = new ArrayAdapter<String>(DisplayMastersListActivity.this, android.R.layout.simple_spinner_item, mChurchList);
						adapterChurch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						spchurch.setAdapter(adapterChurch);
						
						
					}
					
					if(tblname.equals("Churches")){
						mPCFList.clear();
						for(int i=0;i<jarray.length();i++){
							mPCFList.add(jarray.getJSONObject(i).getString("name"));
						}
						
						ArrayAdapter<String> adaptercell = new ArrayAdapter<String>(DisplayMastersListActivity.this, android.R.layout.simple_spinner_item, mCellList);
						adaptercell.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						spCell.setAdapter(adaptercell);
					}
					
					if(tblname.equals("PCFs")){
						
						mSeniorCellList.clear();
						for(int i=0;i<jarray.length();i++){
							mSeniorCellList.add(jarray.getJSONObject(i).getString("name"));
						}
						
						ArrayAdapter<String> adapterSrCell = new ArrayAdapter<String>(DisplayMastersListActivity.this, android.R.layout.simple_spinner_item, mSeniorCellList);
						adapterSrCell.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						spSeniorCell.setAdapter(adapterSrCell);
					}
					
					if(tblname.equals("Senior Cells")){
						
						mCellList.clear();
						
						for(int i=0;i<jarray.length();i++){
							mCellList.add(jarray.getJSONObject(i).getString("name"));
						}
						
						ArrayAdapter<String> adaptercell = new ArrayAdapter<String>(DisplayMastersListActivity.this, android.R.layout.simple_spinner_item, mCellList);
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
					Methods.longToast("Access Denied", DisplayMastersListActivity.this);
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




public void showDialog(){
		
		LayoutInflater layoutInflater = LayoutInflater.from(DisplayMastersListActivity.this);
		View promptView = layoutInflater.inflate(R.layout.filter_dialog, null);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DisplayMastersListActivity.this);
		
		LinearLayout pcflayout=(LinearLayout) promptView.findViewById(R.id.pcflayout);
		LinearLayout srlayout=(LinearLayout) promptView.findViewById(R.id.srlayout);
		LinearLayout cellLayout=(LinearLayout) promptView.findViewById(R.id.cellLayout);
		LinearLayout layoutRegion=(LinearLayout) promptView.findViewById(R.id.layoutRegion);
		LinearLayout layoutzone=(LinearLayout) promptView.findViewById(R.id.layoutzone);
		LinearLayout layoutchurchgroup=(LinearLayout) promptView.findViewById(R.id.layoutchurchgroup);
		LinearLayout layoutchurch=(LinearLayout) promptView.findViewById(R.id.layoutchurch);
		
		
		
		final TextView spzoneTextView=(TextView) promptView.findViewById(R.id.spzoneTextView);
		final TextView spresionTextView=(TextView) promptView.findViewById(R.id.spresionTextView);
		final TextView spgroupchurchTextView=(TextView) promptView.findViewById(R.id.spgroupchurchTextView);
		final TextView spchurchTextView=(TextView) promptView.findViewById(R.id.spchurchTextView);
		final TextView sppcfTextView=(TextView) promptView.findViewById(R.id.sppcfTextView);
		final TextView spSeniorCellTextView=(TextView) promptView.findViewById(R.id.spSeniorCellTextView);
		final TextView spCellTextView=(TextView) promptView.findViewById(R.id.spCellTextView);
		
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
				Methods.showProgressDialog(DisplayMastersListActivity.this);
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
				Methods.showProgressDialog(DisplayMastersListActivity.this);
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
				Methods.showProgressDialog(DisplayMastersListActivity.this);
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
				Methods.showProgressDialog(DisplayMastersListActivity.this);
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
				Methods.showProgressDialog(DisplayMastersListActivity.this);
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
							
							Methods.showProgressDialog(DisplayMastersListActivity.this);
							getSpinnerData("Regions",str);
							
							spzone.setVisibility(View.VISIBLE);
							spzoneTextView.setVisibility(View.GONE);
							SpinnerDataFlag.Zones_flag=false;
						
						}catch(NullPointerException ex){
							
							Toast.makeText(DisplayMastersListActivity.this, "Please Select Region", Toast.LENGTH_LONG).show();
						
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
								
								Toast.makeText(DisplayMastersListActivity.this, "Please Select Region", Toast.LENGTH_LONG).show();
								
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
							
							Methods.showProgressDialog(DisplayMastersListActivity.this);
							getSpinnerData("Zones",str);
							
							spgroupchurch.setVisibility(View.VISIBLE);
							spgroupchurchTextView.setVisibility(View.GONE);
							SpinnerDataFlag.GroupChurches_flag=false;
							
						}catch(NullPointerException ex){
							
							Toast.makeText(DisplayMastersListActivity.this, "Please Select Zone", Toast.LENGTH_LONG).show();
						
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
								
								Toast.makeText(DisplayMastersListActivity.this, "Please Select Zone", Toast.LENGTH_LONG).show();
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
							
								Methods.showProgressDialog(DisplayMastersListActivity.this);
								getSpinnerData("Group Churches",str);
							
								spchurch.setVisibility(View.VISIBLE);
								spchurchTextView.setVisibility(View.GONE);
								SpinnerDataFlag.Churches_flag=false;
							
							}else{
								
								Toast.makeText(DisplayMastersListActivity.this, "Please Select Group Church", Toast.LENGTH_LONG).show();
							}
						}catch(NullPointerException ex){
							
							Toast.makeText(DisplayMastersListActivity.this, "Please Select Group Church", Toast.LENGTH_LONG).show();
						
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
							
							Toast.makeText(DisplayMastersListActivity.this, "Please Select Group Church", Toast.LENGTH_LONG).show();
							
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
						
							if(spchurchTextView.getVisibility()!=View.VISIBLE){
								
								String str=spchurch.getSelectedItem().toString();
								Log.e("selected value", str);
								
								Methods.showProgressDialog(DisplayMastersListActivity.this);
								getSpinnerData("Churches",str);
								
								sppcf.setVisibility(View.VISIBLE);
								sppcfTextView.setVisibility(View.GONE);
								SpinnerDataFlag.PCFs_flag=false;
							}else{
								
								Toast.makeText(DisplayMastersListActivity.this, "Please Select Church", Toast.LENGTH_LONG).show();
							}
						
						}catch(NullPointerException ex){
							
							Toast.makeText(DisplayMastersListActivity.this, "Please Select Church", Toast.LENGTH_LONG).show();
						
						}
						
					}
					
				}else{
					
					if(mPreferenceHelper.getString(Commons.USER_ROLE).equals("PCF Leader")){
				
						sppcf.setVisibility(View.VISIBLE);
						sppcfTextView.setVisibility(View.GONE);
						setAdapters();
					
					}
					else{
						
						if(spchurchTextView.getVisibility()!=View.VISIBLE){
						
							sppcf.setVisibility(View.VISIBLE);
							sppcfTextView.setVisibility(View.GONE);
							setAdapters();
						
						}else{
							
							Toast.makeText(DisplayMastersListActivity.this, "Please Select Church", Toast.LENGTH_LONG).show();
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
							
							Methods.showProgressDialog(DisplayMastersListActivity.this);
							getSpinnerData("PCFs",str);
							
							spSeniorCell.setVisibility(View.VISIBLE);
							spSeniorCellTextView.setVisibility(View.GONE);
							SpinnerDataFlag.SeniorCells_flag=false;
							
						}catch(NullPointerException ex){
							
							Toast.makeText(DisplayMastersListActivity.this, "Please Select PCF", Toast.LENGTH_LONG).show();
						
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
							
							Methods.showProgressDialog(DisplayMastersListActivity.this);
							getSpinnerData("Senior Cells",str);
							
							spCell.setVisibility(View.VISIBLE);
							spCellTextView.setVisibility(View.GONE);
							SpinnerDataFlag.Cells_flag=false;	
							
						}catch(NullPointerException ex){
							
							if(mPreferenceHelper.getString(Commons.USER_ROLE).equals("Senior Cell Leader")){
								getSpinnerData("Senior Cells",Commons.USER_DEFVALUE);
								spCell.setVisibility(View.VISIBLE);
								spCellTextView.setVisibility(View.GONE);
								SpinnerDataFlag.Cells_flag=false;	
								
							}else{
								
								Toast.makeText(DisplayMastersListActivity.this, "Please Select Senior cell", Toast.LENGTH_LONG).show();
							}
						}
						
					}
					
				}else{
					
					spCell.setVisibility(View.VISIBLE);
					spCellTextView.setVisibility(View.GONE);
					setAdapters();
				}
					
				
			}
		});
		
		
		
		//Login with Regional Paster Leader and serch in pcf
		
				if(UserRoll.equals("Regional Pastor") && optionSelected.equals("PCF")){
					
					srlayout.setVisibility(View.GONE);
					cellLayout.setVisibility(View.GONE);
					pcflayout.setVisibility(View.GONE);
					
				}
		
		//Login with Regional Paster and serch in Sr Cell
				
		if(UserRoll.equals("Regional Pastor") && optionSelected.equals("Sr Cell")){
				srlayout.setVisibility(View.GONE);
				cellLayout.setVisibility(View.GONE);
			}
		
		//Login with Regional Paster and serch in Cell
		if(UserRoll.equals("Regional Pastor") && optionSelected.equals("Cell")){
		
			cellLayout.setVisibility(View.GONE);
		}
		

		//Login with Zonal Pastor and serch in  Pcf
		if(UserRoll.equals("Zonal Pastor") && optionSelected.equals("PCF")){
					
		layoutRegion.setVisibility(View.GONE);
		pcflayout.setVisibility(View.GONE);
		srlayout.setVisibility(View.GONE);
		cellLayout.setVisibility(View.GONE);
			
		}
				
		//Login with Zonal Pastor and serch in Sr Cell
				
		if(UserRoll.equals("Zonal Pastor") && optionSelected.equals("Sr Cell")){
								
			layoutRegion.setVisibility(View.GONE);
			srlayout.setVisibility(View.GONE);
			cellLayout.setVisibility(View.GONE);
		}
		
		
				//Login with Zonal Pastor and serch in Cell
		if(UserRoll.equals("Zonal Pastor") && optionSelected.equals("Cell")){
				layoutRegion.setVisibility(View.GONE);
				cellLayout.setVisibility(View.GONE);
				}

		
		
		//Login with Group Church Pastor and serch in pcf
		
				if(UserRoll.equals("Group Church Pastor") && optionSelected.equals("PCF")){
					layoutRegion.setVisibility(View.GONE);
					layoutzone.setVisibility(View.GONE);
					pcflayout.setVisibility(View.GONE);
					srlayout.setVisibility(View.GONE);
					cellLayout.setVisibility(View.GONE);
				}

				//Login with Group Church Pastor and serch in Sr Cell
				
				if(UserRoll.equals("Group Church Pastor") && optionSelected.equals("Sr Cell")){
					
					layoutRegion.setVisibility(View.GONE);
					layoutzone.setVisibility(View.GONE);
					cellLayout.setVisibility(View.GONE);
					srlayout.setVisibility(View.GONE);
				}
				
				//Login with Group Church Pastor and serch in  Cell
				
				if(UserRoll.equals("Group Church Pastor") && optionSelected.equals("Cell")){
					layoutRegion.setVisibility(View.GONE);
					layoutzone.setVisibility(View.GONE);
					cellLayout.setVisibility(View.GONE);				
				}
		
		
				//Login with Church Pastor and serch in pcf
				
				if(UserRoll.equals("Church Pastor") && optionSelected.equals("PCF")){
					layoutRegion.setVisibility(View.GONE);
					layoutzone.setVisibility(View.GONE);
					layoutchurchgroup.setVisibility(View.GONE);
					pcflayout.setVisibility(View.GONE);
					srlayout.setVisibility(View.GONE);
					cellLayout.setVisibility(View.GONE);
				}
				
				
				//Login with Church Pastor and serch in Sr Cell
				
				if(UserRoll.equals("Church Pastor") && optionSelected.equals("Sr Cell")){
					layoutRegion.setVisibility(View.GONE);
					layoutzone.setVisibility(View.GONE);
					layoutchurchgroup.setVisibility(View.GONE);
					cellLayout.setVisibility(View.GONE);
					srlayout.setVisibility(View.GONE);
				}
				
				//Login with Church Pastor and serch in  Cell
				if(UserRoll.equals("Church Pastor") && optionSelected.equals("Cell")){
					layoutRegion.setVisibility(View.GONE);
					layoutzone.setVisibility(View.GONE);
					layoutchurchgroup.setVisibility(View.GONE);
					cellLayout.setVisibility(View.GONE);			
				}
				
		
		
		//Login with PCF Leader and serch in pcf
		
		if(UserRoll.equals("PCF Leader") && optionSelected.equals("PCF")){
			
			layoutRegion.setVisibility(View.GONE);
			layoutzone.setVisibility(View.GONE);
			layoutchurchgroup.setVisibility(View.GONE);
			layoutchurch.setVisibility(View.GONE);
			pcflayout.setVisibility(View.GONE);
			srlayout.setVisibility(View.GONE);
			cellLayout.setVisibility(View.GONE);
			
		}
		
		//Login with PCF Leader and serch in Sr Cell
		
		if(UserRoll.equals("PCF Leader") && optionSelected.equals("Sr Cell")){
			
			layoutRegion.setVisibility(View.GONE);
			layoutzone.setVisibility(View.GONE);
			layoutchurchgroup.setVisibility(View.GONE);
			layoutchurch.setVisibility(View.GONE);
			srlayout.setVisibility(View.GONE);
			cellLayout.setVisibility(View.GONE);
		}
		
		//Login with PCF Leader and serch in  Cell
		if(UserRoll.equals("PCF Leader") && optionSelected.equals("Cell")){
			layoutRegion.setVisibility(View.GONE);
			layoutzone.setVisibility(View.GONE);
			layoutchurchgroup.setVisibility(View.GONE);
			layoutchurch.setVisibility(View.GONE);
			cellLayout.setVisibility(View.GONE);				
		}
		
				
		

		
		
		//Login with Senior Cell Leader and serch in pcf
		
		if(UserRoll.equals("Senior Cell Leader") && optionSelected.equals("PCF")){
			
			layoutRegion.setVisibility(View.GONE);
			layoutzone.setVisibility(View.GONE);
			layoutchurchgroup.setVisibility(View.GONE);
			layoutchurch.setVisibility(View.GONE);
			pcflayout.setVisibility(View.GONE);
			srlayout.setVisibility(View.GONE);
			cellLayout.setVisibility(View.GONE);
			
		}
		
		//Login with Senior Cell Leader and serch in Sr Cell
		
		if(UserRoll.equals("Senior Cell Leader") && optionSelected.equals("Sr Cell")){
			layoutRegion.setVisibility(View.GONE);
			layoutzone.setVisibility(View.GONE);
			layoutchurchgroup.setVisibility(View.GONE);
			layoutchurch.setVisibility(View.GONE);
			pcflayout.setVisibility(View.GONE);
			srlayout.setVisibility(View.GONE);
			cellLayout.setVisibility(View.GONE);
			
		}
		
		//Login with Senior Cell Leader and serch in  Cell
		if(UserRoll.equals("Senior Cell Leader") && optionSelected.equals("Cell")){
			layoutRegion.setVisibility(View.GONE);
			layoutzone.setVisibility(View.GONE);
			layoutchurchgroup.setVisibility(View.GONE);
			pcflayout.setVisibility(View.GONE);
			layoutchurch.setVisibility(View.GONE);
			cellLayout.setVisibility(View.GONE);
			
		}
		
		
		//Login with Cell Leader and serch in pcf
		
		/*if(UserRoll.equals("Cell Leader") && optionSelected.equals("PCF")){
					layoutRegion.setVisibility(View.GONE);
					layoutzone.setVisibility(View.GONE);
					layoutchurchgroup.setVisibility(View.GONE);
					layoutchurch.setVisibility(View.GONE);
					pcflayout.setVisibility(View.GONE);
					srlayout.setVisibility(View.GONE);
					cellLayout.setVisibility(View.GONE);
					
				}*/
		
		//Login with Cell Leader and serch in Sr Cell
		
		/*if(UserRoll.equals("Cell Leader") && optionSelected.equals("Sr Cell")){
			layoutRegion.setVisibility(View.GONE);
			layoutzone.setVisibility(View.GONE);
			layoutchurchgroup.setVisibility(View.GONE);
			layoutchurch.setVisibility(View.GONE);
			pcflayout.setVisibility(View.GONE);
			srlayout.setVisibility(View.GONE);
			cellLayout.setVisibility(View.GONE);
		}*/
				
		//Login with Cell Leader and serch in Cell
		if(UserRoll.equals("Cell Leader") && optionSelected.equals("Cell")){
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
			
			String resion="",zone="",groupchurch="",church="",pcf="",srcell="",cell="",fdate="",tdate="";
			
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
					
			 if(!checkValidation(resion,zone,groupchurch,church,pcf,srcell,cell,fdate,tdate)){
				
				 Methods.longToast("Please select any Filter",DisplayMastersListActivity.this);
				 
			 }else{
			
					if(NetworkHelper.isOnline(DisplayMastersListActivity.this)){
						Methods.showProgressDialog(DisplayMastersListActivity.this);
					
						if(optionSelected.equalsIgnoreCase("PCF")){
						
							getUpdatedListMethod("PCFs",resion,zone,groupchurch,church,pcf,srcell,cell,fdate,tdate);
						
						}else if(optionSelected.equalsIgnoreCase("Sr Cell")){
						
							getUpdatedListMethod("Senior Cells",resion,zone,groupchurch,church,pcf,srcell,cell,fdate,tdate);
					
						}else if(optionSelected.equalsIgnoreCase("Cell")){
							
							getUpdatedListMethod("Cells",resion,zone,groupchurch,church,pcf,srcell,cell,fdate,tdate);
						}
					
						dialog.cancel();
		
						
					}else{
					
						Methods.longToast("Please connect to Internet",DisplayMastersListActivity.this);
					
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
//		TextView textView = (TextView) alertD.findViewById(android.R.id.message);
//		textView.setTextSize(18);

	}



boolean checkValidation(String resion,String zone,String groupchurch,String church,String pcf,String srcell,String cell,String fdate,String tdate){
	if(resion.equals("")){
		if(zone.equals("")){
			if(groupchurch.equals("")){
				if(church.equals("")){
					if(pcf.equals("")){
						if(srcell.equals("")){
							if(cell.equals("")){
								if(fdate.equals("") && tdate.equals("")){
										
									
									
										return false;
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

private void setAdapters() {
	ArrayAdapter<String> adapterZone = new ArrayAdapter<String>(DisplayMastersListActivity.this, android.R.layout.simple_spinner_item, mZoneList);
	adapterZone.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	spzone.setAdapter(adapterZone);

	ArrayAdapter<String> adapterRegion = new ArrayAdapter<String>(DisplayMastersListActivity.this, android.R.layout.simple_spinner_item, mRegionList);
	adapterRegion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	spresion.setAdapter(adapterRegion);

	ArrayAdapter<String> adapterChurch = new ArrayAdapter<String>(DisplayMastersListActivity.this, android.R.layout.simple_spinner_item, mChurchList);
	adapterChurch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	spchurch.setAdapter(adapterChurch);

	ArrayAdapter<String> adapterSrCell = new ArrayAdapter<String>(DisplayMastersListActivity.this, android.R.layout.simple_spinner_item, mSeniorCellList);
	adapterSrCell.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	spSeniorCell.setAdapter(adapterSrCell);

	ArrayAdapter<String> adapterchurchgropu = new ArrayAdapter<String>(DisplayMastersListActivity.this, android.R.layout.simple_spinner_item, mGrpChurchList);
	adapterchurchgropu.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	spgroupchurch.setAdapter(adapterchurchgropu);

	ArrayAdapter<String> adapterPCF = new ArrayAdapter<String>(DisplayMastersListActivity.this, android.R.layout.simple_spinner_item, mPCFList);
	adapterPCF.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	sppcf.setAdapter(adapterPCF);
	
	ArrayAdapter<String> adaptercell = new ArrayAdapter<String>(DisplayMastersListActivity.this, android.R.layout.simple_spinner_item, mCellList);
	adaptercell.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	spCell.setAdapter(adaptercell);
	
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
	pageflag=true;
	
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
		row = li.inflate(R.layout.database_raw_layout, parent, false);
		//database_raw_layout
		
		TextView txtMasterName = (TextView) row.findViewById(R.id.txtMasterName);
		TextView txtMasterName1 = (TextView) row.findViewById(R.id.txtMasterName1);
		
		TextView txtuserid = (TextView) row.findViewById(R.id.txtuserid);
		TextView txtid = (TextView) row.findViewById(R.id.txtid);
		
		
		

		try {
			
			if(optionSelected.equalsIgnoreCase("PCF")){
				txtMasterName.setText(jsonarray.getJSONObject(position).getString("name"));
			//	txtMasterName1.setText(jsonarray.getJSONObject(position).getString("pcf_name"));
			
				txtMasterName1.setText((jsonarray.getJSONObject(position).getString("pcf_name").toString().equals("null"))?"":jsonarray.getJSONObject(position).getString("pcf_name").toString());
				
				txtuserid.setVisibility(View.GONE);
				txtid.setVisibility(View.GONE);
				
			//	txtuserid.setText(jsonarray.getJSONObject(position).getString("church"));
			//	txtid.setText(jsonarray.getJSONObject(position).getString("church_name"));
				
			//	txtid.setText((jsonarray.getJSONObject(position).getString("church_name").toString().equals("null"))?"":jsonarray.getJSONObject(position).getString("church_name").toString());
			}
			
			if(optionSelected.equalsIgnoreCase("Sr Cell")){
				txtMasterName.setText(jsonarray.getJSONObject(position).getString("name"));
			//	txtMasterName1.setText(jsonarray.getJSONObject(position).getString("senior_cell_name"));
				
				txtMasterName1.setText((jsonarray.getJSONObject(position).getString("senior_cell_name").toString().equals("null"))?"":jsonarray.getJSONObject(position).getString("senior_cell_name").toString());
				
				txtuserid.setVisibility(View.GONE);
				txtid.setVisibility(View.GONE);
				
				//txtuserid.setText(jsonarray.getJSONObject(position).getString("pcf"));
				
				//txtid.setText(jsonarray.getJSONObject(position).getString("pcf_name"));
				
				//txtid.setText((jsonarray.getJSONObject(position).getString("pcf_name").toString().equals("null"))?"":jsonarray.getJSONObject(position).getString("pcf_name").toString());
			}
		
			if(optionSelected.equalsIgnoreCase("Cell")){
				txtMasterName.setText(jsonarray.getJSONObject(position).getString("name"));
			//	txtMasterName1.setText(jsonarray.getJSONObject(position).getString("cell_name"));
				txtMasterName1.setText((jsonarray.getJSONObject(position).getString("cell_name").toString().equals("null"))?"":jsonarray.getJSONObject(position).getString("cell_name").toString());
				
				txtuserid.setVisibility(View.GONE);
				txtid.setVisibility(View.GONE);
			//	txtuserid.setText(jsonarray.getJSONObject(position).getString("senior_cell"));
			//	txtid.setText((jsonarray.getJSONObject(position).getString("senior_cell_name").toString().equals("null"))?"":jsonarray.getJSONObject(position).getString("senior_cell_name").toString());
			}
		
		
	
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
            	
            	if(NetworkHelper.isOnline(DisplayMastersListActivity.this)){
        			Methods.showProgressDialog(DisplayMastersListActivity.this);
        			if(optionSelected.equalsIgnoreCase("PCF")){
        				
        				getListNewMethod(Commons.USER_TBL_LABEL_PCF,btns[j].getText().toString());
        				
        			}else if(optionSelected.equalsIgnoreCase("Sr Cell")){
        				
        				getListNewMethod(Commons.USER_TBL_LABEL_SRCELLS,btns[j].getText().toString());
        				
        			}else if(optionSelected.equalsIgnoreCase("Cell")){
        				
        				getListNewMethod(Commons.USER_TBL_LABEL_CELLS,btns[j].getText().toString());
        				
        			}
        		}else{
        			Methods.longToast("Please connect to Internet", DisplayMastersListActivity.this);
        		}
            	
                CheckBtnBackGroud(j);
            }
        });
        
        pageflag=false;
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
	
}
