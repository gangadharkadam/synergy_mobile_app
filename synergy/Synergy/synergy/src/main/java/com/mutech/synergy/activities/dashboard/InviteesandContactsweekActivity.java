package com.mutech.synergy.activities.dashboard;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import com.mutech.synergy.SynergyValues.Web.DashboardDataService;
import com.mutech.synergy.SynergyValues.Web.DashboardListDataService;
import com.mutech.synergy.SynergyValues.Web.GetAllListMastersService;
import com.mutech.synergy.SynergyValues.Web.GetHigherHierarchyService;
import com.mutech.synergy.SynergyValues.Web.LowerHierarchyService;
import com.mutech.synergy.activities.dashboard.FirstTimeMonthActivity.SpinnerDataFlag;
import com.mutech.synergy.activities.event.CreateEventActivity;
import com.mutech.synergy.activities.event.EventListActivity;
import com.mutech.synergy.activities.event.MyEventListActivity.Holder;
import com.mutech.synergy.models.MeetingListRequestModel;
import com.mutech.synergy.models.ResponseMessageModel2;
import com.mutech.synergy.models.EventParticipantsModel.EventParticipantSubModel;
import com.mutech.synergy.utils.Methods;
import com.mutech.synergy.utils.NetworkHelper;
import com.mutech.synergy.utils.PreferenceHelper;

import android.support.v7.app.ActionBarActivity;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ActionBar.LayoutParams;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class InviteesandContactsweekActivity extends ActionBarActivity {
	
	
	ListView Yearlist;
	private PreferenceHelper mPreferenceHelper;
	
	String week;
	private Gson gson;
	
	Spinner spresion,spzone,sppcf,spgroupchurch,spchurch,spSeniorCell,spCell;
	private ArrayList<String> mZoneList,mRegionList,mChurchList,mSeniorCellList,mGrpChurchList,mPCFList,mCellList;
	
	TextView txtFromDate,txtToDate;
	private DatePickerDialog fromDatePickerDialog,toDatePickerDialog;
	private SimpleDateFormat dateFormatter,dateFormatter01;
	Calendar newCalendar;
	String UserRoll;
	//ImageView filterimg;
	

	public int TOTAL_LIST_ITEMS;
    public int NUM_ITEMS_PAGE   = 20;
    private int noOfBtns;
    private Button[] btns;
	
    TextView titletextView;
    
	JSONObject jsonobj;
	JSONArray jsonarray;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inviteesand_contactsweek);
		
		Yearlist=(ListView) findViewById(R.id.yearlist);
		
		mPreferenceHelper=new PreferenceHelper(this);
		week=getIntent().getStringExtra("week");
		gson=new Gson();
		jsonarray=new JSONArray();
		
		 getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		    getSupportActionBar().setHomeButtonEnabled(false);
			getSupportActionBar().setCustomView(R.layout.custom_actionbar);
			titletextView=(TextView)getSupportActionBar().getCustomView().findViewById(R.id.title_text);
			
			getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2E9AFE")));
			
			titletextView.setText("INVITEES AND CONTACTS  ");
			getSupportActionBar().setDisplayShowCustomEnabled(true);
		
	//	filterimg=(ImageView) findViewById(R.id.imageView2);
		
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
		
		fromDatePickerDialog = new DatePickerDialog(InviteesandContactsweekActivity.this, new OnDateSetListener() {

			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
				txtFromDate.setText(dateFormatter.format(newDate.getTime()));
			}

		},newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

		toDatePickerDialog = new DatePickerDialog(InviteesandContactsweekActivity.this, new OnDateSetListener() {
			
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
				txtToDate.setText(dateFormatter.format(newDate.getTime()));
			}
		
		},newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

		fromDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
		toDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
		
		/*filterimg.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
						
					if(NetworkHelper.isOnline(InviteesandContactsweekActivity.this)){
					
						showDialog();
					
					}else{
					
						Methods.longToast("Please connect to Internet",InviteesandContactsweekActivity.this);
					
					}
				}
			});
			*/
		
		Yearlist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				try {
					
					Log.e("Name", jsonarray.getJSONObject(position).getString("name"));
					
					Intent it=new Intent(InviteesandContactsweekActivity.this,InviteesandContacts_ListDetailsActivity.class);
					it.putExtra("tblename","Invitees and Contacts" );
					it.putExtra("name", jsonarray.getJSONObject(position).getString("name"));
					it.putExtra("title", "Invitees And Contacts");
					startActivity(it);
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
				
			}
		});
		
		
		
		if(NetworkHelper.isOnline(this)){
			Methods.showProgressDialog(this);
			getDashboardDataService("1");
	
		}
		else
			Methods.longToast("Please connect to Internet", this);
	
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
			
			Intent intCreateEvent=new Intent(InviteesandContactsweekActivity.this,CreateEventActivity.class);
			startActivity(intCreateEvent);
			
			break;

		case R.id.menu_option2:
			Intent intMyEventList=new Intent(InviteesandContactsweekActivity.this,EventListActivity.class);
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
	
	private void getDashboardDataService(final String pageno) {
		StringRequest reqDashboard=new StringRequest(Method.POST,DashboardListDataService.SERVICE_URL,new Listener<String>() {

			@Override
			public void onResponse(String response) {
				Methods.closeProgressDialog();
				Log.e("droid","get all First Timer Week---------"+ response);

				if(response.contains("status"))
				{
					//filterimg.setVisibility(View.GONE);
					ResponseMessageModel2 respModel=gson.fromJson(response, ResponseMessageModel2.class);
					if(respModel.getMessage().getStatus()=="401"){
						Methods.longToast("User name or Password is incorrect", InviteesandContactsweekActivity.this);
					}else{
						Methods.longToast(respModel.getMessage().getMessage(), InviteesandContactsweekActivity.this);
					}
				}else{
					
					try {
							
						
							JSONObject jsonobj=new JSONObject(response);
						
							int i=Integer.parseInt(jsonobj.getJSONObject("message").getString("total_count"));
							
							TOTAL_LIST_ITEMS=Integer.parseInt(jsonobj.getJSONObject("message").getString("total_count"));
						
							Log.e("totalcount=", ""+TOTAL_LIST_ITEMS);
							 
							 jsonarray=jsonobj.getJSONObject("message").getJSONArray("records");
							 
							if(jsonarray.length()>0){
								
								titletextView.setText("INVITEES AND CONTACTS("+i+")");
							
								Btnfooter();
								
								EventListAdapter adpter=new EventListAdapter(InviteesandContactsweekActivity.this,jsonarray);
								Yearlist.setAdapter(adpter);
							}else{
								//filterimg.setVisibility(View.GONE);
								Methods.longToast("No results found", InviteesandContactsweekActivity.this);
							}
							
						} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
						}
					
					}
				
				
				//}
			}
		},new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Methods.closeProgressDialog();
				Log.e("droid","get all Invitees and Contacts Year---------------"+ error.getCause());
			}
		})
		{
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();

				MeetingListRequestModel model=new MeetingListRequestModel();
				model.setUsername(mPreferenceHelper.getString(Commons.USER_EMAILID));
				model.setUserpass(mPreferenceHelper.getString(Commons.USER_PASSWORD));

				JSONObject jsonobj=new JSONObject();
				try {
					
					jsonobj.put("username", mPreferenceHelper.getString(Commons.USER_EMAILID));
					jsonobj.put("userpass", mPreferenceHelper.getString(Commons.USER_PASSWORD));
					jsonobj.put("tbl", "Invitees and Contacts");
					jsonobj.put("page_no",pageno);
					
					JSONObject obj=new JSONObject();
					obj.put("Week", week);
					
					jsonobj.put("filters", obj);
					
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				String dataString=jsonobj.toString();
			//	String dataString=gson.toJson(model, MeetingListRequestModel.class);

				Log.d("droid","data passed is ::::::::"+dataString);
				params.put(DashboardDataService.DATA, dataString);
				return params; 
			}
		};

		App.getInstance().addToRequestQueue(reqDashboard, "reqDashboard");
		reqDashboard.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
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
			LayoutInflater layout = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layout.inflate(R.layout.row_meeting, null);
			
			
			
			Button btn=(Button) convertView.findViewById(R.id.btnMarkAttendance);
			TextView name=(TextView) convertView.findViewById(R.id.lblMeetingName);
			TextView sex=(TextView) convertView.findViewById(R.id.lblMeetingTime);
			TextView ftv_name=(TextView) convertView.findViewById(R.id.lblMeetingVenue);
			TextView email=(TextView) convertView.findViewById(R.id.lblMeetingsubject);
			
			btn.setVisibility(View.GONE);
			try {

				if(!jsonarray.getJSONObject(position).getString("name").toString().equals("null") && !jsonarray.getJSONObject(position).getString("name").toString().equals("")) {
					name.setText(jsonarray.getJSONObject(position).getString("name"));
				} else {
					name.setText("");
				}
				if(!jsonarray.getJSONObject(position).getString("email_id").toString().equals("null") && !jsonarray.getJSONObject(position).getString("email_id").toString().equals("")) {
					email.setText(jsonarray.getJSONObject(position).getString("email_id"));
				} else {
					email.setText("");
				}
				if(!jsonarray.getJSONObject(position).getString("invitee_contact_name").toString().equals("null") && !jsonarray.getJSONObject(position).getString("invitee_contact_name").toString().equals("")) {
					ftv_name.setText(jsonarray.getJSONObject(position).getString("invitee_contact_name"));
				} else {
					ftv_name.setText("");
				}
				if(!jsonarray.getJSONObject(position).getString("sex").toString().equals("null") && !jsonarray.getJSONObject(position).getString("sex").toString().equals("")) {
					sex.setText(jsonarray.getJSONObject(position).getString("sex"));
				} else {
					sex.setText("");
				}
//				name.setText(jsonarray.getJSONObject(position).getString("name"));
//				email.setText(jsonarray.getJSONObject(position).getString("email_id"));
//				ftv_name.setText(jsonarray.getJSONObject(position).getString("invitee_contact_name"));
//				sex.setText(jsonarray.getJSONObject(position).getString("sex"));
				
			
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return convertView;

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
	               	if(NetworkHelper.isOnline(InviteesandContactsweekActivity.this)){
	        			Methods.showProgressDialog(InviteesandContactsweekActivity.this);
	        			getDashboardDataService( btns[j].getText().toString());
	        			
	        		}else{
	        			Methods.longToast("Please connect to Internet", InviteesandContactsweekActivity.this);
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
		
		LayoutInflater layoutInflater = LayoutInflater.from(InviteesandContactsweekActivity.this);
		View promptView = layoutInflater.inflate(R.layout.filter_dialog, null);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(InviteesandContactsweekActivity.this);
		
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
				Methods.showProgressDialog(InviteesandContactsweekActivity.this);
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
				Methods.showProgressDialog(InviteesandContactsweekActivity.this);
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
				Methods.showProgressDialog(InviteesandContactsweekActivity.this);
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
				Methods.showProgressDialog(InviteesandContactsweekActivity.this);
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
				Methods.showProgressDialog(InviteesandContactsweekActivity.this);
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
							
							Methods.showProgressDialog(InviteesandContactsweekActivity.this);
							getSpinnerData("Regions",str);
							
							spzone.setVisibility(View.VISIBLE);
							spzoneTextView.setVisibility(View.GONE);
							SpinnerDataFlag.Zones_flag=false;
						
						}catch(NullPointerException ex){
							
							Toast.makeText(InviteesandContactsweekActivity.this, "Please Select Resion", Toast.LENGTH_LONG).show();
						
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
						
						Toast.makeText(InviteesandContactsweekActivity.this, "Please Select Resion", Toast.LENGTH_LONG).show();
						
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
							
							Methods.showProgressDialog(InviteesandContactsweekActivity.this);
							getSpinnerData("Zones",str);
							
							spgroupchurch.setVisibility(View.VISIBLE);
							spgroupchurchTextView.setVisibility(View.GONE);
							SpinnerDataFlag.GroupChurches_flag=false;
							
						}catch(NullPointerException ex){
							
							Toast.makeText(InviteesandContactsweekActivity.this, "Please Select Zone", Toast.LENGTH_LONG).show();
						
						}
						
					}
					
				
				}else{
					if(spzoneTextView.getVisibility()!=View.VISIBLE){
					
						spgroupchurch.setVisibility(View.VISIBLE);
						spgroupchurchTextView.setVisibility(View.GONE);
						setAdapters();
					
					}else{
						
						Toast.makeText(InviteesandContactsweekActivity.this, "Please Select Zone", Toast.LENGTH_LONG).show();
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
							
								Methods.showProgressDialog(InviteesandContactsweekActivity.this);
								getSpinnerData("Group Churches",str);
							
								spchurch.setVisibility(View.VISIBLE);
								spchurchTextView.setVisibility(View.GONE);
								SpinnerDataFlag.Churches_flag=false;
							
							}else{
								
								Toast.makeText(InviteesandContactsweekActivity.this, "Please Select Group Church", Toast.LENGTH_LONG).show();
							}
						}catch(NullPointerException ex){
							
							Toast.makeText(InviteesandContactsweekActivity.this, "Please Select Group Church", Toast.LENGTH_LONG).show();
						
						}
						
					}
					
				}else{
					
					if(spgroupchurchTextView.getVisibility()!=View.VISIBLE){
						spchurch.setVisibility(View.VISIBLE);
						spchurchTextView.setVisibility(View.GONE);
						setAdapters();
					}else{
						
						Toast.makeText(InviteesandContactsweekActivity.this, "Please Select Group Church", Toast.LENGTH_LONG).show();
						
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
							
							Methods.showProgressDialog(InviteesandContactsweekActivity.this);
							getSpinnerData("Churches",str);
							
							sppcf.setVisibility(View.VISIBLE);
							sppcfTextView.setVisibility(View.GONE);
							SpinnerDataFlag.PCFs_flag=false;
					
						}catch(NullPointerException ex){
							
							Toast.makeText(InviteesandContactsweekActivity.this, "Please Select Church", Toast.LENGTH_LONG).show();
						
						}
						
					}
					
				}else{
					
					if(spchurchTextView.getVisibility()!=View.VISIBLE){
					
						sppcf.setVisibility(View.VISIBLE);
						sppcfTextView.setVisibility(View.GONE);
						setAdapters();
					
					}else{
						
						Toast.makeText(InviteesandContactsweekActivity.this, "Please Select Church", Toast.LENGTH_LONG).show();
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
							
							Methods.showProgressDialog(InviteesandContactsweekActivity.this);
							getSpinnerData("PCFs",str);
							
							spSeniorCell.setVisibility(View.VISIBLE);
							spSeniorCellTextView.setVisibility(View.GONE);
							SpinnerDataFlag.SeniorCells_flag=false;
							
						}catch(NullPointerException ex){
							
							Toast.makeText(InviteesandContactsweekActivity.this, "Please Select Pcf", Toast.LENGTH_LONG).show();
						
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
							
							Methods.showProgressDialog(InviteesandContactsweekActivity.this);
							getSpinnerData("Senior Cells",str);
							
							spCell.setVisibility(View.VISIBLE);
							spCellTextView.setVisibility(View.GONE);
							SpinnerDataFlag.Cells_flag=false;	
							
						}catch(NullPointerException ex){
							
							Toast.makeText(InviteesandContactsweekActivity.this, "Please Select Senior cell", Toast.LENGTH_LONG).show();
						
						}
						
					}
					
				}else{
					
					spCell.setVisibility(View.VISIBLE);
					spCellTextView.setVisibility(View.GONE);
					setAdapters();
				}
			}
		});
		
		
		if(UserRoll.equals("PCF Leader")){
			layoutRegion.setVisibility(View.GONE);
			layoutzone.setVisibility(View.GONE);
			layoutchurchgroup.setVisibility(View.GONE);
			layoutchurch.setVisibility(View.GONE);
			
			/*pcflayout.setVisibility(View.GONE);
			srlayout.setVisibility(View.GONE);
			cellLayout.setVisibility(View.GONE);
			*/
		}
		
		if(UserRoll.equals("Zonal Pastor")){
			
			layoutRegion.setVisibility(View.GONE);
			/*srlayout.setVisibility(View.GONE);
			cellLayout.setVisibility(View.GONE);*/
		}
		
		if(UserRoll.equals("Group Church Pastor")){
			/*layoutRegion.setVisibility(View.GONE);
			layoutzone.setVisibility(View.GONE);
			*/
			layoutRegion.setVisibility(View.GONE);
			layoutzone.setVisibility(View.GONE);
		/*
			pcflayout.setVisibility(View.GONE);
			srlayout.setVisibility(View.GONE);
			cellLayout.setVisibility(View.GONE);*/
		}

				
		if(UserRoll.equals("Church Pastor")){
			layoutRegion.setVisibility(View.GONE);
			layoutzone.setVisibility(View.GONE);
			layoutchurchgroup.setVisibility(View.GONE);
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
			
			String resion="",zone="",groupchurch="",church="",pcf="",srcell="",cell="",fdate="",tdate="";
			
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
			
			 fdate=txtFromDate.getText().toString();
			 tdate=txtToDate.getText().toString();
			
			 if(!checkValidation(resion,zone,groupchurch,church,pcf,srcell,cell,fdate,tdate)){
					
				 Methods.longToast("Please select any Filter",InviteesandContactsweekActivity.this);
				 
			 }
			 else
			 {
				if(NetworkHelper.isOnline(InviteesandContactsweekActivity.this)){
					Methods.showProgressDialog(InviteesandContactsweekActivity.this);
					getUpdatedListMethod("Invitees and Contacts",resion,zone,groupchurch,church,pcf,srcell,cell,fdate,tdate);
				
					dialog.cancel();
	
					
				}else{
				
					Methods.longToast("Please connect to Internet",InviteesandContactsweekActivity.this);
				
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
					
					ArrayAdapter<String> adapterchurchgropu = new ArrayAdapter<String>(InviteesandContactsweekActivity.this, android.R.layout.simple_spinner_item, mGrpChurchList);
					adapterchurchgropu.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spgroupchurch.setAdapter(adapterchurchgropu);
				
				}
				
				if(tblname.equals("Group Churches")){
					mChurchList.clear();
					
					for(int i=0;i<jarray.length();i++){
						mChurchList.add(jarray.getJSONObject(i).getString("name"));
					}
					
					ArrayAdapter<String> adapterChurch = new ArrayAdapter<String>(InviteesandContactsweekActivity.this, android.R.layout.simple_spinner_item, mChurchList);
					adapterChurch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spchurch.setAdapter(adapterChurch);
					
					
				}
				
				if(tblname.equals("Churches")){
					mPCFList.clear();
					for(int i=0;i<jarray.length();i++){
						mPCFList.add(jarray.getJSONObject(i).getString("name"));
					}
					
					ArrayAdapter<String> adaptercell = new ArrayAdapter<String>(InviteesandContactsweekActivity.this, android.R.layout.simple_spinner_item, mCellList);
					adaptercell.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spCell.setAdapter(adaptercell);
				}
				
				if(tblname.equals("PCFs")){
					
					mSeniorCellList.clear();
					for(int i=0;i<jarray.length();i++){
						mSeniorCellList.add(jarray.getJSONObject(i).getString("name"));
					}
					
					ArrayAdapter<String> adapterSrCell = new ArrayAdapter<String>(InviteesandContactsweekActivity.this, android.R.layout.simple_spinner_item, mSeniorCellList);
					adapterSrCell.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spSeniorCell.setAdapter(adapterSrCell);
				}
				
				if(tblname.equals("Senior Cells")){
					
					mCellList.clear();
					
					for(int i=0;i<jarray.length();i++){
						mCellList.add(jarray.getJSONObject(i).getString("name"));
					}
					
					ArrayAdapter<String> adaptercell = new ArrayAdapter<String>(InviteesandContactsweekActivity.this, android.R.layout.simple_spinner_item, mCellList);
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
				Methods.longToast("Access Denied", InviteesandContactsweekActivity.this);
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

	ArrayAdapter<String> adapterZone = new ArrayAdapter<String>(InviteesandContactsweekActivity.this, android.R.layout.simple_spinner_item, mZoneList);
	adapterZone.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	spzone.setAdapter(adapterZone);

	ArrayAdapter<String> adapterRegion = new ArrayAdapter<String>(InviteesandContactsweekActivity.this, android.R.layout.simple_spinner_item, mRegionList);
	adapterRegion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	spresion.setAdapter(adapterRegion);

	ArrayAdapter<String> adapterChurch = new ArrayAdapter<String>(InviteesandContactsweekActivity.this, android.R.layout.simple_spinner_item, mChurchList);
	adapterChurch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	spchurch.setAdapter(adapterChurch);

	ArrayAdapter<String> adapterSrCell = new ArrayAdapter<String>(InviteesandContactsweekActivity.this, android.R.layout.simple_spinner_item, mSeniorCellList);
	adapterSrCell.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	spSeniorCell.setAdapter(adapterSrCell);

	ArrayAdapter<String> adapterchurchgropu = new ArrayAdapter<String>(InviteesandContactsweekActivity.this, android.R.layout.simple_spinner_item, mGrpChurchList);
	adapterchurchgropu.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	spgroupchurch.setAdapter(adapterchurchgropu);

	ArrayAdapter<String> adapterPCF = new ArrayAdapter<String>(InviteesandContactsweekActivity.this, android.R.layout.simple_spinner_item, mPCFList);
	adapterPCF.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	sppcf.setAdapter(adapterPCF);
	
	ArrayAdapter<String> adaptercell = new ArrayAdapter<String>(InviteesandContactsweekActivity.this, android.R.layout.simple_spinner_item, mCellList);
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
				Methods.longToast("Access Denied", InviteesandContactsweekActivity.this);
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
				Methods.longToast("User name or Password is incorrect", InviteesandContactsweekActivity.this);
			}else{
				Methods.longToast(respModel.getMessage().getMessage(), InviteesandContactsweekActivity.this);
			}
		}else{
			
				
				try {
					
					 jsonobj=new JSONObject(response);
					 
					 jsonobj.getJSONObject("message");
						
					TOTAL_LIST_ITEMS=Integer.parseInt(jsonobj.getJSONObject("message").getString("total_count"));
					
						Log.e("totalcount=", ""+TOTAL_LIST_ITEMS);
					
					//if(pageflag)
					 Btnfooter();
					 
					 titletextView.setText("INVITEES AND CONTACTS("+TOTAL_LIST_ITEMS+")");
					 
					 jsonarray=jsonobj.getJSONObject("message").getJSONArray("records");
				
					if(jsonarray.length()>0){
						
						EventListAdapter adapter=new EventListAdapter(InviteesandContactsweekActivity.this,jsonarray);
						Yearlist.setAdapter(adapter);
						adapter.notifyDataSetChanged();
					
					}else{
						
							Methods.longToast("No results found", InviteesandContactsweekActivity.this);
							EventListAdapter adapter=new EventListAdapter(InviteesandContactsweekActivity.this,jsonarray);
							Yearlist.setAdapter(adapter);
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
		
			jsonobj.put("tbl",tbl);
			
			
			//resion, zone,gchurch, church, pcf,srcell,cell,fdate, todate)
			
			JSONObject jsonfilter=new JSONObject();
			
			jsonfilter.put("Week", week);

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
