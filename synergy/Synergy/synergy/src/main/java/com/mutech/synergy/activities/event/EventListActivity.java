package com.mutech.synergy.activities.event;

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
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import com.google.gson.Gson;
import com.mutech.messagebraudcast.MessageBroadcastActivity;
import com.mutech.synergy.App;
import com.mutech.synergy.R;
import com.mutech.synergy.SynergyValues.Commons;
import com.mutech.synergy.SynergyValues.Web.GetAllEventListService;
import com.mutech.synergy.SynergyValues.Web.GetAllListMastersService;
import com.mutech.synergy.SynergyValues.Web.GetHigherHierarchyService;
import com.mutech.synergy.SynergyValues.Web.LowerHierarchyService;
import com.mutech.synergy.activities.HomeActivity;
import com.mutech.synergy.activities.LoginActivity;
import com.mutech.synergy.activities.LogoutActivity;
import com.mutech.synergy.activities.cellMasters.DisplayMastersListActivity;
import com.mutech.synergy.activities.cellMasters.MasterSelectorScreenActivity;
import com.mutech.synergy.activities.cellMasters.PartnerShipRecord;
import com.mutech.synergy.activities.cellMasters.SearchFunctionActivity;
import com.mutech.synergy.activities.dashboard.FirstTimeMonthActivity;
import com.mutech.synergy.activities.meeting.MyMeetingListActivity;

import com.mutech.synergy.activities.meeting.MyMeetingListActivity.SpinnerDataFlag;
import com.mutech.synergy.activities.profile.MyProfileActivity;
import com.mutech.synergy.activities.task.ToDoTaskActivity;
import com.mutech.synergy.adapters.CustomDrawerAdapter;
import com.mutech.synergy.models.EventModel;
import com.mutech.synergy.models.EventModel.EventSubModel;
import com.mutech.synergy.models.DrawerItem;
import com.mutech.synergy.models.MeetingListRequestModel;
import com.mutech.synergy.models.ResponseMessageModel2;
import com.mutech.synergy.utils.Methods;
import com.mutech.synergy.utils.NetworkHelper;
import com.mutech.synergy.utils.PreferenceHelper;

public class EventListActivity extends ActionBarActivity implements OnItemClickListener{

	private ListView lvEvents;
	private PreferenceHelper mPreferenceHelper;
	private Gson gson;
	private EventModel mEventModel;
	private ArrayList<EventSubModel> mEventList;
	private EventListAdapter mEventListAdapter;

	String str;
	JSONArray jsonarray;
	
	public int TOTAL_LIST_ITEMS;
    public int NUM_ITEMS_PAGE   = 20;
    private int noOfBtns;
    private Button[] btns;
    
    private SimpleDateFormat displayDateFormat;
	
    Spinner spresion,spzone,sppcf,spgroupchurch,spchurch,spSeniorCell,spCell,speventtype;
	private ArrayList<String> mZoneList,mRegionList,mChurchList,mSeniorCellList,mGrpChurchList,mPCFList,mCellList,meventtype;
	
	TextView txtFromDate,txtToDate;
	String UserRoll;
	//ImageView filterimg;
	
	
	private DatePickerDialog fromDatePickerDialog,toDatePickerDialog;
	private SimpleDateFormat dateFormatter,dateFormatter01;
	Calendar newCalendar;
	TextView tvTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_eventlist_layout);
		initialize();
	}

	@SuppressLint("NewApi")
	private void initialize() {
		
		
		tvTitle=(TextView) findViewById(R.id.textView1);
		lvEvents=(ListView) findViewById(R.id.lvMeeting);
	
		
	
		
		//filterimg=(ImageView) findViewById(R.id.imageView2);
		lvEvents.setOnItemClickListener(this);
		
		    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		    getSupportActionBar().setHomeButtonEnabled(false);
			getSupportActionBar().setCustomView(R.layout.custom_actionbar);
			tvTitle=(TextView)getSupportActionBar().getCustomView().findViewById(R.id.title_text);
			
			getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2E9AFE")));
			
			tvTitle.setText("All Events   ");
			getSupportActionBar().setDisplayShowCustomEnabled(true);
		
		
		
		jsonarray=new JSONArray();
		meventtype=new ArrayList<String>();
		mZoneList=new ArrayList<String>();
		mRegionList=new ArrayList<String>();
		mChurchList=new ArrayList<String>();
		mSeniorCellList=new ArrayList<String>();
		mGrpChurchList=new ArrayList<String>();
		mPCFList=new ArrayList<String>();
		mCellList=new ArrayList<String>();
		
		
		
		meventtype.add("Private");
		meventtype.add("Public");
		meventtype.add("Cancel");
		
		dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
		//2015-05-24 06:22:22
		dateFormatter01=new SimpleDateFormat("yyyy-MM-dd");

		newCalendar = Calendar.getInstance();
		
		
		fromDatePickerDialog = new DatePickerDialog(EventListActivity.this, new OnDateSetListener() {

			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
				txtFromDate.setText(dateFormatter.format(newDate.getTime()));
			}

		},newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

		toDatePickerDialog = new DatePickerDialog(EventListActivity.this, new OnDateSetListener() {
			
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
					
				if(NetworkHelper.isOnline(EventListActivity.this)){
				
					showDialog();
				
				}else{
				
					Methods.longToast("Please connect to Internet",EventListActivity.this);
				
				}
			}
		});*/
		
		
		
		mPreferenceHelper=new PreferenceHelper(this);
		
		 str=mPreferenceHelper.getString(Commons.USER_ROLE);
		UserRoll=mPreferenceHelper.getString(Commons.USER_ROLE);

		
		
		mEventModel=new EventModel();

		gson=new Gson();
		
		
		if(NetworkHelper.isOnline(this)){
			Methods.showProgressDialog(this);
			getAllEventList("1");
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
			
			Intent intCreateEvent=new Intent(EventListActivity.this,CreateEventActivity.class);
			startActivity(intCreateEvent);
			
			break;

		case R.id.menu_option2:
			Intent intMyEventList=new Intent(EventListActivity.this,EventListActivity.class);
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
	
	private void getAllEventList(final String pageno) {
		StringRequest reqEventList=new StringRequest(Method.POST,GetAllEventListService.SERVICE_URL,new Listener<String>() {

			@Override
			public void onResponse(String response) {
				Methods.closeProgressDialog();
				Log.d("droid","get all reqEventList ---------------"+ response);
				
				
				if(response.contains("status")){
					ResponseMessageModel2 respModel=gson.fromJson(response, ResponseMessageModel2.class);
					if(respModel.getMessage().getStatus()=="401"){
						Methods.longToast("User name or Password is incorrect", EventListActivity.this);
					}else{
						Methods.longToast(respModel.getMessage().getMessage(), EventListActivity.this);
					}
				}else{
					/*mEventModel=gson.fromJson(response, EventModel.class);
					//Object meetingmsg=mMeetingModel.getMessage();
					if(null !=mEventModel){
						
							if(null !=mEventModel.getMessage() && mEventModel.getMessage().size() >0){
								mEventList=new ArrayList<EventSubModel>();
								//if(meetingmsg instanceof JSONArray){
								mEventList=mEventModel.getMessage();
								Log.d("droid", "isjsonarray----------------------------------");
								if(mEventList.size() > 0){
									mEventListAdapter=new EventListAdapter(EventListActivity.this,mEventList);
									lvEvents.setAdapter(mEventListAdapter);
								}
							}else{
								Methods.longToast("No Events Found", EventListActivity.this);
							}
						}*/
					
					try {
						JSONObject jsonobj=new JSONObject(response);
						jsonarray=jsonobj.getJSONObject("message").getJSONArray("records");

						int i=Integer.parseInt(jsonobj.getJSONObject("message").getString("total_count"));
						
						TOTAL_LIST_ITEMS=Integer.parseInt(jsonobj.getJSONObject("message").getString("total_count"));
					
						Log.e("totalcount=", ""+TOTAL_LIST_ITEMS);
						
						tvTitle.setText("All Events("+i+")       ");
						
						
						if(jsonarray.length()>0){
							
							Btnfooter();
							
							mEventListAdapter=new EventListAdapter(EventListActivity.this,jsonarray);
							lvEvents.setAdapter(mEventListAdapter);
					
						}else{
							
							Methods.longToast("No Events Found", EventListActivity.this);
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
				Log.d("droid","get all zones error---------------"+ error.getCause());
				Methods.longToast("No Events Found,Please Try Again Later", EventListActivity.this);
			}
		})
		{
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();

				MeetingListRequestModel model=new MeetingListRequestModel();
				model.setUsername(mPreferenceHelper.getString(Commons.USER_EMAILID));
				model.setUserpass(mPreferenceHelper.getString(Commons.USER_PASSWORD));
				model.setPage_no(pageno);

				String dataString=gson.toJson(model, MeetingListRequestModel.class);

				Log.d("droid", dataString);
				params.put(GetAllEventListService.DATA, dataString);
				return params; 
			}
		};

		App.getInstance().addToRequestQueue(reqEventList, "reqEventList");
		reqEventList.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
	}

	

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent i=new Intent(this,SingleEventDetails.class);
		Bundle b=new Bundle();
		
		try {
		
			b.putString("Event_name",jsonarray.getJSONObject(position).getString("event_name"));
			b.putString("EventSubj",jsonarray.getJSONObject(position).getString("subject"));
			b.putString("Event_venue", jsonarray.getJSONObject(position).getString("subject"));
			b.putString("Event_date", jsonarray.getJSONObject(position).getString("event_date"));
			
			/*b.putString("Event_name",mEventList.get(position).getEvent_name());
			b.putString("EventSubj",mEventList.get(position).getSubject());
			b.putString("Event_venue", mEventList.get(position).getAddress());
			b.putString("Event_date", mEventList.get(position).getEvent_date());*/
			i.putExtra("Bundle",b);
			startActivity(i);
		
		
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	//***********************************************************************************************///

	/*class EventListAdapter extends BaseAdapter{

		private Context mContext;
		private ArrayList<EventSubModel> mList;
		private Holder holder;

		public EventListAdapter(Context context,
				ArrayList<EventSubModel> eventList) {
			mContext=context;
			mList=new ArrayList<EventSubModel>();
			mList=eventList;
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
				convertView = layout.inflate(R.layout.row_meeting, null);

				holder.btnMarkAttendance=(Button) convertView.findViewById(R.id.btnMarkAttendance);
				holder.lblEventName=(TextView) convertView.findViewById(R.id.lblMeetingName);
				holder.lblEventTime=(TextView) convertView.findViewById(R.id.lblMeetingTime);
				holder.lblEventVenue=(TextView) convertView.findViewById(R.id.lblMeetingVenue);
				holder.lblEventsubject=(TextView) convertView.findViewById(R.id.lblMeetingsubject);
				
				convertView.setTag(holder);
			}else{
				holder = (Holder) convertView.getTag();
			}

			if(null !=mList.get(position).getEvent_name())
				holder.lblEventName.setText(mList.get(position).getEvent_name());
			
			if(null !=mList.get(position).getSubject())
			holder.lblEventsubject.setText(mList.get(position).getSubject());
			
			if(null !=mList.get(position).getEvent_date())
				holder.lblEventTime.setText(mList.get(position).getEvent_date());
			holder.lblEventVenue.setText(mList.get(position).getAddress());

			holder.btnMarkAttendance.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent i=new Intent(EventListActivity.this,MarkMemberEventAttendanceActivity.class);
					Bundle b=new Bundle();
					b.putString("Event_name",mEventList.get(position).getEvent_name());
					b.putString("EventSubj",mEventList.get(position).getSubject());
					b.putString("Event_date", mEventList.get(position).getEvent_date());
					i.putExtra("Bundle",b);
					startActivity(i);
				}
			});
			return convertView;
		}

	}*/
	
	class EventListAdapter extends BaseAdapter{

		private Context mContext;
		JSONArray jarray;
		private Holder holder;

		public EventListAdapter(Context context,JSONArray jarray) {
			mContext=context;
			this.jarray=jarray;
		}

		@Override
		public int getCount() {
			return jarray.length();
		}

		@Override
		public Object getItem(int position) {
			return position;
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
				convertView = layout.inflate(R.layout.row_meeting, null);

				holder.btnMarkAttendance=(Button) convertView.findViewById(R.id.btnMarkAttendance);
				holder.lblEventName=(TextView) convertView.findViewById(R.id.lblMeetingName);
				holder.lblEventTime=(TextView) convertView.findViewById(R.id.lblMeetingTime);
				holder.lblEventVenue=(TextView) convertView.findViewById(R.id.lblMeetingVenue);
				holder.lblEventsubject=(TextView) convertView.findViewById(R.id.lblMeetingsubject);
				
				convertView.setTag(holder);
			}else{
				holder = (Holder) convertView.getTag();
				
			}

			try {
				Log.d("NonStop", "All Events Message" + jarray.getJSONObject(position).toString());

				if(null !=jarray.getJSONObject(position).getString("event_name"))
					holder.lblEventName.setText(jarray.getJSONObject(position).getString("event_name"));
			
				if(null !=jarray.getJSONObject(position).getString("subject"))
					holder.lblEventsubject.setText(jarray.getJSONObject(position).getString("subject"));
					
				if(null !=jarray.getJSONObject(position).getString("event_date"))
						holder.lblEventTime.setText(jarray.getJSONObject(position).getString("event_date"));

				if(jarray.getJSONObject(position).getString("address").contentEquals("null")) {
					Log.d("NonStop", "Here!!!");
					holder.lblEventVenue.setText("");
				}
				else
					holder.lblEventVenue.setText(jarray.getJSONObject(position).getString("address"));
//				if(jarray.getJSONObject(position).getString("address") != null)
//					holder.lblEventVenue.setText(jarray.getJSONObject(position).getString("address"));

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			holder.btnMarkAttendance.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent i=new Intent(EventListActivity.this,MarkMemberEventAttendanceActivity.class);
					Bundle b=new Bundle();
					
					try {
						
						b.putString("Event_name",jarray.getJSONObject(position).getString("event_name"));
						b.putString("EventSubj",jarray.getJSONObject(position).getString("subject"));
						b.putString("Event_date",jarray.getJSONObject(position).getString("event_date"));
						i.putExtra("Bundle",b);
						startActivity(i);
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				}
			});
			return convertView;
		}

	}

	public static class Holder{
		private TextView lblEventName,lblEventVenue,lblEventTime,lblEventsubject;
		private Button btnMarkAttendance;
	}
	
	private void Btnfooter()
	{
	    int val = TOTAL_LIST_ITEMS%NUM_ITEMS_PAGE;
	    val = val==0?0:1;
	    noOfBtns=TOTAL_LIST_ITEMS/NUM_ITEMS_PAGE+val;
	     
	    LinearLayout ll = (LinearLayout)findViewById(R.id.btnLay);
	    ll.removeAllViews();
	    
	    HorizontalScrollView horizontalScrollView=(HorizontalScrollView) findViewById(R.id.horizontalscroll);
	    horizontalScrollView.setBackgroundColor(getResources().getColor(android.R.color.white));
	 
	    
	    
	    btns    =new Button[noOfBtns];
	     
	    for(int i=0;i<noOfBtns;i++)
	    {
	        btns[i] =   new Button(this);
	        btns[i].setBackgroundColor(getResources().getColor(android.R.color.white));
	        btns[i].setText(""+(i+1));
	         
	        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	        ll.addView(btns[i], lp);
	         
	        final int j = i;
	        btns[j].setOnClickListener(new OnClickListener() {
	             
	            public void onClick(View v) 
	            {
	               	if(NetworkHelper.isOnline(EventListActivity.this)){
	        			Methods.showProgressDialog(EventListActivity.this);
	        			getAllEventList( btns[j].getText().toString());
	        			
	        		}else{
	        			
	        			Methods.longToast("Please connect to Internet", EventListActivity.this);
	        		
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
		
		LayoutInflater layoutInflater = LayoutInflater.from(EventListActivity.this);
		View promptView = layoutInflater.inflate(R.layout.filter_dialog, null);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EventListActivity.this);
		
		LinearLayout pcflayout=(LinearLayout) promptView.findViewById(R.id.pcflayout);
		LinearLayout srlayout=(LinearLayout) promptView.findViewById(R.id.srlayout);
		LinearLayout cellLayout=(LinearLayout) promptView.findViewById(R.id.cellLayout);
		LinearLayout layoutRegion=(LinearLayout) promptView.findViewById(R.id.layoutRegion);
		LinearLayout layoutzone=(LinearLayout) promptView.findViewById(R.id.layoutzone);
		LinearLayout layoutchurchgroup=(LinearLayout) promptView.findViewById(R.id.layoutchurchgroup);
		LinearLayout layoutchurch=(LinearLayout) promptView.findViewById(R.id.layoutchurch);
		LinearLayout eventtypeLayout=(LinearLayout) promptView.findViewById(R.id.eventtypeLayout);

//		eventtypeLayout.setVisibility(View.VISIBLE);
		eventtypeLayout.setVisibility(View.GONE);

		final TextView spzoneTextView=(TextView) promptView.findViewById(R.id.spzoneTextView);
		final TextView spresionTextView=(TextView) promptView.findViewById(R.id.spresionTextView);
		final TextView spgroupchurchTextView=(TextView) promptView.findViewById(R.id.spgroupchurchTextView);
		final TextView spchurchTextView=(TextView) promptView.findViewById(R.id.spchurchTextView);
		final TextView sppcfTextView=(TextView) promptView.findViewById(R.id.sppcfTextView);
		final TextView spSeniorCellTextView=(TextView) promptView.findViewById(R.id.spSeniorCellTextView);
		final TextView spCellTextView=(TextView) promptView.findViewById(R.id.spCellTextView);
		final TextView speventtypeTextView=(TextView) promptView.findViewById(R.id.speventtypeTextView);
		
		speventtype=(Spinner) promptView.findViewById(R.id.speventtype);
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
		
		ArrayAdapter<String> adapterspeventtype = new ArrayAdapter<String>(EventListActivity.this, android.R.layout.simple_spinner_item, meventtype);
		adapterspeventtype.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		speventtype.setAdapter(adapterspeventtype);
		
		spzone.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				
				String str=spzone.getSelectedItem().toString();
				Methods.showProgressDialog(EventListActivity.this);
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
				Methods.showProgressDialog(EventListActivity.this);
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
				Methods.showProgressDialog(EventListActivity.this);
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
				Methods.showProgressDialog(EventListActivity.this);
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
				Methods.showProgressDialog(EventListActivity.this);
				getUpdatedSpinnerData("Senior Cells",str);
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});	
		
		speventtypeTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				speventtypeTextView.setVisibility(View.GONE);
				speventtype.setVisibility(View.VISIBLE);
			}
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
					
						mZoneList.add(mPreferenceHelper.getString(Commons.USER_ROLE));
						spzone.setVisibility(View.VISIBLE);
						spzoneTextView.setVisibility(View.GONE);
						setAdapters();
						SpinnerDataFlag.Zones_flag=false;
					}else{
						
						try{
						
							String str=spresion.getSelectedItem().toString();
							Log.e("selected value", str);
							
							Methods.showProgressDialog(EventListActivity.this);
							getSpinnerData("Regions",str);
							
							spzone.setVisibility(View.VISIBLE);
							spzoneTextView.setVisibility(View.GONE);
							SpinnerDataFlag.Zones_flag=false;
						
						}catch(NullPointerException ex){
							
							Toast.makeText(EventListActivity.this, "Please Select Resion", Toast.LENGTH_LONG).show();
						
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
						
						Toast.makeText(EventListActivity.this, "Please Select Resion", Toast.LENGTH_LONG).show();
						
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
					
						mGrpChurchList.add(mPreferenceHelper.getString(Commons.USER_ROLE));
						spgroupchurch.setVisibility(View.VISIBLE);
						spgroupchurchTextView.setVisibility(View.GONE);
						setAdapters();
						SpinnerDataFlag.GroupChurches_flag=false;
						
					}else{
						
						try{
						
							String str=spzone.getSelectedItem().toString();
							Log.e("selected value", str);
							
							Methods.showProgressDialog(EventListActivity.this);
							getSpinnerData("Zones",str);
							
							spgroupchurch.setVisibility(View.VISIBLE);
							spgroupchurchTextView.setVisibility(View.GONE);
							SpinnerDataFlag.GroupChurches_flag=false;
							
						}catch(NullPointerException ex){
							
							Toast.makeText(EventListActivity.this, "Please Select Zone", Toast.LENGTH_LONG).show();
						
						}
						
					}
					
				
				}else{
					if(spzoneTextView.getVisibility()!=View.VISIBLE){
					
						spgroupchurch.setVisibility(View.VISIBLE);
						spgroupchurchTextView.setVisibility(View.GONE);
						setAdapters();
					
					}else{
						
						Toast.makeText(EventListActivity.this, "Please Select Zone", Toast.LENGTH_LONG).show();
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
					
						mChurchList.add(mPreferenceHelper.getString(Commons.USER_ROLE));
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
							
								Methods.showProgressDialog(EventListActivity.this);
								getSpinnerData("Group Churches",str);
							
								spchurch.setVisibility(View.VISIBLE);
								spchurchTextView.setVisibility(View.GONE);
								SpinnerDataFlag.Churches_flag=false;
							
							}else{
								
								Toast.makeText(EventListActivity.this, "Please Select Group Church", Toast.LENGTH_LONG).show();
							}
						}catch(NullPointerException ex){
							
							Toast.makeText(EventListActivity.this, "Please Select Group Church", Toast.LENGTH_LONG).show();
						
						}
						
					}
					
				}else{
					
					if(spgroupchurchTextView.getVisibility()!=View.VISIBLE){
						spchurch.setVisibility(View.VISIBLE);
						spchurchTextView.setVisibility(View.GONE);
						setAdapters();
					}else{
						
						Toast.makeText(EventListActivity.this, "Please Select Group Church", Toast.LENGTH_LONG).show();
						
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
					
						mPCFList.add(mPreferenceHelper.getString(Commons.USER_ROLE));
						sppcf.setVisibility(View.VISIBLE);
						sppcfTextView.setVisibility(View.GONE);
						setAdapters();
						SpinnerDataFlag.PCFs_flag=false;
						
					}else{
						
						try{
						
							String str=spchurch.getSelectedItem().toString();
							Log.e("selected value", str);
							
							Methods.showProgressDialog(EventListActivity.this);
							getSpinnerData("Churches",str);
							
							sppcf.setVisibility(View.VISIBLE);
							sppcfTextView.setVisibility(View.GONE);
							SpinnerDataFlag.PCFs_flag=false;
					
						}catch(NullPointerException ex){
							
							Toast.makeText(EventListActivity.this, "Please Select Church", Toast.LENGTH_LONG).show();
						
						}
						
					}
					
				}else{
					
					if(spchurchTextView.getVisibility()!=View.VISIBLE){
					
						sppcf.setVisibility(View.VISIBLE);
						sppcfTextView.setVisibility(View.GONE);
						setAdapters();
					
					}else{
						
						Toast.makeText(EventListActivity.this, "Please Select Church", Toast.LENGTH_LONG).show();
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
					
						mSeniorCellList.add(mPreferenceHelper.getString(Commons.USER_ROLE));
						spSeniorCell.setVisibility(View.VISIBLE);
						spSeniorCellTextView.setVisibility(View.GONE);
						setAdapters();
						SpinnerDataFlag.SeniorCells_flag=false;
						
					}else{
						
						try{
						
							String str=sppcf.getSelectedItem().toString();
							Log.e("selected value", str);
							
							Methods.showProgressDialog(EventListActivity.this);
							getSpinnerData("PCFs",str);
							
							spSeniorCell.setVisibility(View.VISIBLE);
							spSeniorCellTextView.setVisibility(View.GONE);
							SpinnerDataFlag.SeniorCells_flag=false;
							
						}catch(NullPointerException ex){
							
							Toast.makeText(EventListActivity.this, "Please Select Pcf", Toast.LENGTH_LONG).show();
						
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
					
						mCellList.add(mPreferenceHelper.getString(Commons.USER_ROLE));
						spCell.setVisibility(View.VISIBLE);
						spCellTextView.setVisibility(View.GONE);
						setAdapters();
						SpinnerDataFlag.Cells_flag=false;
						
					}else{
						
						try{
						
							String str=spSeniorCell.getSelectedItem().toString();
							Log.e("selected value", str);
							
							Methods.showProgressDialog(EventListActivity.this);
							getSpinnerData("Senior Cells",str);
							
							spCell.setVisibility(View.VISIBLE);
							spCellTextView.setVisibility(View.GONE);
							SpinnerDataFlag.Cells_flag=false;	
							
						}catch(NullPointerException ex){
							
							Toast.makeText(EventListActivity.this, "Please Select Senior cell", Toast.LENGTH_LONG).show();
						
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
			
			String resion="",zone="",groupchurch="",church="",pcf="",srcell="",cell="",fdate="",tdate="",eventtype="Private";
			
			try{
				eventtype=spresion.getSelectedItem().toString();
			}catch(Exception ex){
				eventtype="Private";
			}
			
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
					
			
			if(NetworkHelper.isOnline(EventListActivity.this)){
				Methods.showProgressDialog(EventListActivity.this);
				getUpdatedListMethod("First Timer",resion,zone,groupchurch,church,pcf,srcell,cell,fdate,tdate,eventtype);
			
				dialog.cancel();

				
			}else{
			
				Methods.longToast("Please connect to Internet",EventListActivity.this);
			
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
					
					ArrayAdapter<String> adapterchurchgropu = new ArrayAdapter<String>(EventListActivity.this, android.R.layout.simple_spinner_item, mGrpChurchList);
					adapterchurchgropu.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spgroupchurch.setAdapter(adapterchurchgropu);
				
				}
				
				if(tblname.equals("Group Churches")){
					mChurchList.clear();
					
					for(int i=0;i<jarray.length();i++){
						mChurchList.add(jarray.getJSONObject(i).getString("name"));
					}
					
					ArrayAdapter<String> adapterChurch = new ArrayAdapter<String>(EventListActivity.this, android.R.layout.simple_spinner_item, mChurchList);
					adapterChurch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spchurch.setAdapter(adapterChurch);
					
					
				}
				
				if(tblname.equals("Churches")){
					mPCFList.clear();
					for(int i=0;i<jarray.length();i++){
						mPCFList.add(jarray.getJSONObject(i).getString("name"));
					}
					
					ArrayAdapter<String> adaptercell = new ArrayAdapter<String>(EventListActivity.this, android.R.layout.simple_spinner_item, mCellList);
					adaptercell.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spCell.setAdapter(adaptercell);
				}
				
				if(tblname.equals("PCFs")){
					
					mSeniorCellList.clear();
					for(int i=0;i<jarray.length();i++){
						mSeniorCellList.add(jarray.getJSONObject(i).getString("name"));
					}
					
					ArrayAdapter<String> adapterSrCell = new ArrayAdapter<String>(EventListActivity.this, android.R.layout.simple_spinner_item, mSeniorCellList);
					adapterSrCell.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spSeniorCell.setAdapter(adapterSrCell);
				}
				
				if(tblname.equals("Senior Cells")){
					
					mCellList.clear();
					
					for(int i=0;i<jarray.length();i++){
						mCellList.add(jarray.getJSONObject(i).getString("name"));
					}
					
					ArrayAdapter<String> adaptercell = new ArrayAdapter<String>(EventListActivity.this, android.R.layout.simple_spinner_item, mCellList);
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
				Methods.longToast("Access Denied", EventListActivity.this);
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

	ArrayAdapter<String> adapterZone = new ArrayAdapter<String>(EventListActivity.this, android.R.layout.simple_spinner_item, mZoneList);
	adapterZone.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	spzone.setAdapter(adapterZone);

	ArrayAdapter<String> adapterRegion = new ArrayAdapter<String>(EventListActivity.this, android.R.layout.simple_spinner_item, mRegionList);
	adapterRegion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	spresion.setAdapter(adapterRegion);

	ArrayAdapter<String> adapterChurch = new ArrayAdapter<String>(EventListActivity.this, android.R.layout.simple_spinner_item, mChurchList);
	adapterChurch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	spchurch.setAdapter(adapterChurch);

	ArrayAdapter<String> adapterSrCell = new ArrayAdapter<String>(EventListActivity.this, android.R.layout.simple_spinner_item, mSeniorCellList);
	adapterSrCell.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	spSeniorCell.setAdapter(adapterSrCell);

	ArrayAdapter<String> adapterchurchgropu = new ArrayAdapter<String>(EventListActivity.this, android.R.layout.simple_spinner_item, mGrpChurchList);
	adapterchurchgropu.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	spgroupchurch.setAdapter(adapterchurchgropu);

	ArrayAdapter<String> adapterPCF = new ArrayAdapter<String>(EventListActivity.this, android.R.layout.simple_spinner_item, mPCFList);
	adapterPCF.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	sppcf.setAdapter(adapterPCF);
	
	ArrayAdapter<String> adaptercell = new ArrayAdapter<String>(EventListActivity.this, android.R.layout.simple_spinner_item, mCellList);
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
				Methods.longToast("Access Denied", EventListActivity.this);
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

private void getUpdatedListMethod(final String tbl,final String resion,final String zone,final String gchurch,final String church,final String pcf,final String srcell,final String cell,final String fdate,final String todate,final String eventtype){

	StringRequest reqgetLowerHierarchy=new StringRequest(Method.POST,GetAllEventListService.SERVICE_URL,new Listener<String>() {

	@Override
	public void onResponse(String response) {
		Methods.closeProgressDialog();
		Log.e("droid get reqgetList Redord--------", response);

		

		if(response.contains("status"))
		{
			ResponseMessageModel2 respModel=gson.fromJson(response, ResponseMessageModel2.class);
		
			if(respModel.getMessage().getStatus()=="401"){
				Methods.longToast("User name or Password is incorrect", EventListActivity.this);
			}else{
				Methods.longToast(respModel.getMessage().getMessage(), EventListActivity.this);
			}
		}else{
			
				
				try {
					
					JSONObject jsonobj=new JSONObject(response);
					 
					 jsonobj.getJSONObject("message");
						
					TOTAL_LIST_ITEMS=Integer.parseInt(jsonobj.getJSONObject("message").getString("total_count"));
					
						Log.e("totalcount=", ""+TOTAL_LIST_ITEMS);
					
					//if(pageflag)
					 Btnfooter();
					 tvTitle.setText("All Events("+TOTAL_LIST_ITEMS+")       ");
					 jsonarray=jsonobj.getJSONObject("message").getJSONArray("records");
				
					if(jsonarray.length()>0){
						
						EventListAdapter adapter=new EventListAdapter(EventListActivity.this,jsonarray);
						lvEvents.setAdapter(adapter);
						adapter.notifyDataSetChanged();
					
					}else{
						
							Methods.longToast("No results found", EventListActivity.this);
							EventListAdapter adapter=new EventListAdapter(EventListActivity.this,jsonarray);
							lvEvents.setAdapter(adapter);
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
		
			//jsonobj.put("tbl",tbl);
			
			
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

			if(!todate.equals(""))
				jsonfilter.put("to_date", todate);

			if(!fdate.equals(""))
				jsonfilter.put("from_date", fdate);

//			jsonfilter.put("event_type", eventtype);
			
			jsonobj.put("filters", jsonfilter);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String dataString=jsonobj.toString();

		Log.e("filter request droid", dataString);
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
