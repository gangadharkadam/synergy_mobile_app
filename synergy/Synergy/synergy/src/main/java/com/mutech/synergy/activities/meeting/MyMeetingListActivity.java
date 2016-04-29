package com.mutech.synergy.activities.meeting;

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
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ActionBar.LayoutParams;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Display;
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
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
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
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.mutech.messagebraudcast.MessageBroadcastActivity;
import com.mutech.synergy.App;
import com.mutech.synergy.R;
import com.mutech.synergy.SynergyValues.Commons;
import com.mutech.synergy.SynergyValues.Web.GetAllListMastersService;
import com.mutech.synergy.SynergyValues.Web.GetHigherHierarchyService;
import com.mutech.synergy.SynergyValues.Web.GetMyMeetingService;
import com.mutech.synergy.SynergyValues.Web.LowerHierarchyService;
import com.mutech.synergy.activities.HomeActivity;
import com.mutech.synergy.activities.LoginActivity;
import com.mutech.synergy.activities.LogoutActivity;
import com.mutech.synergy.activities.cellMasters.MasterSelectorScreenActivity;
import com.mutech.synergy.activities.cellMasters.PartnerShipRecord;
import com.mutech.synergy.activities.cellMasters.SearchFunctionActivity;
import com.mutech.synergy.activities.dashboard.FirstTimeMonthActivity;

import com.mutech.synergy.activities.dashboard.FirstTimeMonthActivity.SpinnerDataFlag;
import com.mutech.synergy.activities.event.EventListActivity;
import com.mutech.synergy.activities.event.MyEventListActivity;
import com.mutech.synergy.activities.profile.MyProfileActivity;
import com.mutech.synergy.activities.task.ToDoTaskActivity;
import com.mutech.synergy.adapters.CustomDrawerAdapter;
import com.mutech.synergy.models.MeetingListRequestModel;
import com.mutech.synergy.models.MeetingModel;
import com.mutech.synergy.models.MeetingModel.MeetingListModel;
import com.mutech.synergy.models.DrawerItem;
import com.mutech.synergy.models.ResponseMessageModel;
import com.mutech.synergy.models.ResponseMessageModel2;
import com.mutech.synergy.models.StatusCheckModel;
import com.mutech.synergy.utils.Methods;
import com.mutech.synergy.utils.NetworkHelper;
import com.mutech.synergy.utils.PreferenceHelper;

@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
public class MyMeetingListActivity  extends ActionBarActivity implements OnItemClickListener{

	private ListView lvMeeting;
	private MeetingModel mMeetingModel;
	//private ArrayList<MeetingListModel> mMeetingList;
	private MeetingListAdapter mMeetingListAdapter;
	private PreferenceHelper mPreferenceHelper;
	private SimpleDateFormat displayDateFormat;
	private Gson gson;
	JSONArray jsonarray;
	String str;
	
	public int TOTAL_LIST_ITEMS;
    public int NUM_ITEMS_PAGE   = 20;
    private int noOfBtns;
    private Button[] btns;
	
	private ArrayList<DrawerItem> mDrawerList;
	private CustomDrawerAdapter mCustomDrawerAdapter;
	private ListView mLvDrawer;
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;

	Spinner spresion,spzone,sppcf,spgroupchurch,spchurch,spSeniorCell,spCell;
	private ArrayList<String> mZoneList,mRegionList,mChurchList,mSeniorCellList,mGrpChurchList,mPCFList,mCellList;
	
	TextView txtFromDate,txtToDate;
	String UserRoll;
	
	private DatePickerDialog fromDatePickerDialog,toDatePickerDialog;
	private SimpleDateFormat dateFormatter,dateFormatter01;
	Calendar newCalendar;
	TextView tvTitle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_attendance_layout);
//		setContentView(R.layout.activity_meetinglist);

		jsonarray=new JSONArray();
		
		mZoneList=new ArrayList<String>();
		mRegionList=new ArrayList<String>();
		mChurchList=new ArrayList<String>();
		mSeniorCellList=new ArrayList<String>();
		mGrpChurchList=new ArrayList<String>();
		mPCFList=new ArrayList<String>();
		mCellList=new ArrayList<String>();
		
		initialize();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.metting_listmenu, menu);
		menu.findItem(R.id.menu_addTo).setIcon(R.drawable.ic_launcher).setTitle("Create Attendance");
		menu.findItem(R.id.menu_option2).setIcon(R.drawable.ic_launcher).setTitle("All Members Attendance");
		menu.findItem(R.id.filter).setIcon(R.drawable.ic_launcher).setTitle("Filter Records");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		switch (item.getItemId()) {
		case R.id.menu_addTo:
			Log.e(null, "Role-"+str);
			if(str.equalsIgnoreCase("Member")){
				Toast.makeText(MyMeetingListActivity.this, "NOT ACCESSIBLE FOR MEMBER", Toast.LENGTH_SHORT).show();
			}else{				
				Log.e(null, "Role222-"+str);
				Intent intCreateMeeting=new Intent(MyMeetingListActivity.this,CreateCellMeetingActivity.class);
				startActivity(intCreateMeeting);
			}
			break;
		case R.id.menu_option2:
				
			Intent intAllMeeting=new Intent(MyMeetingListActivity.this,MeetingListActivity.class);
			startActivity(intAllMeeting);
				
			break;
			
		case R.id.filter:
			
			showDialog();	
			
			break;


		default:
			break;
		}
		return false;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	private void initialize() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setCustomView(R.layout.custom_actionbar);
		getSupportActionBar().setHomeAsUpIndicator(R.drawable.actiontop);

//		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//		getSupportActionBar().setHomeButtonEnabled(true);
//		getSupportActionBar().setCustomView(R.layout.custom_actionbar);
//		getSupportActionBar().setIcon(R.drawable.actiontop);
		tvTitle=(TextView)getSupportActionBar().getCustomView().findViewById(R.id.title_text);
		tvTitle.setText("My Attendance    ");
		
		dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
		//2015-05-24 06:22:22
		dateFormatter01=new SimpleDateFormat("yyyy-MM-dd");

		newCalendar = Calendar.getInstance();
		
		fromDatePickerDialog = new DatePickerDialog(MyMeetingListActivity.this, new OnDateSetListener() {

			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
				txtFromDate.setText(dateFormatter.format(newDate.getTime()));
			}

		},newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

		toDatePickerDialog = new DatePickerDialog(MyMeetingListActivity.this, new OnDateSetListener() {
			
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
				txtToDate.setText(dateFormatter.format(newDate.getTime()));
			}
		
		},newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

		
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2E9AFE")));
				
		getSupportActionBar().setDisplayShowCustomEnabled(true);


		lvMeeting=(ListView) findViewById(R.id.lvMeeting);
		lvMeeting.setOnItemClickListener(this);

		mMeetingModel=new MeetingModel();

		mPreferenceHelper=new PreferenceHelper(this);
		UserRoll=mPreferenceHelper.getString(Commons.USER_ROLE);
		 str=mPreferenceHelper.getString(Commons.USER_ROLE);
		gson=new Gson();
		
		mDrawerList = new ArrayList<DrawerItem>();
		// mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
		// GravityCompat.START);

		addDrawerListData();

		mLvDrawer = (ListView) findViewById(R.id.left_drawer);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		mCustomDrawerAdapter = new CustomDrawerAdapter(this,R.layout.custom_dashboard_drawer_item, mDrawerList);
		mLvDrawer.setAdapter(mCustomDrawerAdapter);
		mLvDrawer.setOnItemClickListener(new DrawerItemClickListener());
		
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	//	actionBar.setHomeAsUpIndicator(R.drawable.actiontop);
		getSupportActionBar().setIcon(
				   new ColorDrawable(getResources().getColor(android.R.color.transparent))); 
	

		

		mDrawerToggle = new ActionBarDrawerToggle(
				this,
				mDrawerLayout,
				R.drawable.dashboard, 
				R.string.app_name, 
				R.string.app_name 
				) {

			/** Called when a drawer has settled in a completely closed state. */
			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
				//getSupportActionBar().setTitle(mTitle);
			}

			/** Called when a drawer has settled in a completely open state. */
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				//getSupportActionBar().setTitle(mDrawerTitle);
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);	
		
		if(NetworkHelper.isOnline(this)){
			Methods.showProgressDialog(this);
			getMyMeetingList("1");
		}else{
			Methods.longToast("Please connect to Internet", this);
		}

		//Date MM-DD-YYYY|Time HH:MM
		displayDateFormat=new SimpleDateFormat("MM-dd-yyyy hh:mm",Locale.US);
	}

	private void addDrawerListData() {
		
		if(str.equalsIgnoreCase("Member")){

			DrawerItem item0 = new DrawerItem();
			item0.setItemName("My Profile");
			item0.setImgResID(R.drawable.myprofile);

			DrawerItem item1 = new DrawerItem();
			item1.setItemName("Partnership \n Records");
			item1.setImgResID(R.drawable.partnership_record);

			DrawerItem item2 = new DrawerItem();
			item2.setItemName("Search");
			item2.setImgResID(R.drawable.search);

			DrawerItem item3 = new DrawerItem();
			item3.setItemName("Attendance");
			item3.setImgResID(R.drawable.my_meetings);

			DrawerItem item4 = new DrawerItem();
			item4.setItemName("Calendar");
			item4.setImgResID(R.drawable.carlender);

			DrawerItem item5 = new DrawerItem();
			item5.setItemName("To Do");
			item5.setImgResID(R.drawable.todo);
			
			DrawerItem item6 = new DrawerItem();
			item6.setItemName("Broadcast Message");
			item6.setImgResID(R.drawable.msg);

			DrawerItem item7=new DrawerItem();
			item7.setItemName("Logout");
			item7.setImgResID(R.drawable.signout);

			mDrawerList.add(item0);
			mDrawerList.add(item1);
		//	mDrawerList.add(item2);
			mDrawerList.add(item3);
			mDrawerList.add(item4);
			mDrawerList.add(item5);
		//	mDrawerList.add(item6);
			mDrawerList.add(item2);
			mDrawerList.add(item7);
			
		}else{
				DrawerItem item01 = new DrawerItem();
				item01.setItemName("Dashboard");
				item01.setImgResID(R.drawable.dashboard);		
		
				DrawerItem item05 = new DrawerItem();
				item05.setItemName("My Profile");
				item05.setImgResID(R.drawable.myprofile);
		
		//		DrawerItem item03 = new DrawerItem();
		//		item03.setItemName("Ministry \n Material");
		//		item03.setImgResID(R.drawable.ministry_materials);
		
				DrawerItem item03 = new DrawerItem();
				item03.setItemName("Partnership \n Records");
				item03.setImgResID(R.drawable.partnership_record);
		
				DrawerItem item04 = new DrawerItem();
				item04.setItemName("Database");
				item04.setImgResID(R.drawable.database);
		
		
				DrawerItem item06 = new DrawerItem();
				item06.setItemName("Search");
				item06.setImgResID(R.drawable.search);
		
				DrawerItem item07 = new DrawerItem();
				item07.setItemName("Attendance");
				item07.setImgResID(R.drawable.my_meetings);
		
				DrawerItem item08 = new DrawerItem();
				item08.setItemName("Calendar");
				item08.setImgResID(R.drawable.carlender);
		
				DrawerItem item9 = new DrawerItem();
				item9.setItemName("To Do");
				item9.setImgResID(R.drawable.todo);
				
				DrawerItem item10 = new DrawerItem();
				item10.setItemName("Broadcast Message");
				item10.setImgResID(R.drawable.msg);
		
				DrawerItem item11=new DrawerItem();
				item11.setItemName("Logout");
				item11.setImgResID(R.drawable.signout);
		
				mDrawerList.add(item01);
				mDrawerList.add(item05);
				mDrawerList.add(item04);
		//		mDrawerList.add(item02);
		//		mDrawerList.add(item03);
				mDrawerList.add(item03);	
				
			//	mDrawerList.add(item06);
				mDrawerList.add(item07);
				mDrawerList.add(item08);
				mDrawerList.add(item9);
				mDrawerList.add(item10);
				mDrawerList.add(item06);
				mDrawerList.add(item11);
		}
	}


	private void selectItem(int position) {
		
		if(str.equalsIgnoreCase("Member")){
			
			switch (position) {
			case 0:
				
				Intent intForm1=new Intent(this,MyProfileActivity.class);
				intForm1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				startActivity(intForm1);
				finish();
				
				break;
			case 1:	
				
				Intent partner=new Intent(this,PartnerShipRecord.class);
				partner.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				startActivity(partner);
				finish();
		
			case 2:
				
				//Intent intMyMeetings=new Intent(this,MyMeetingListActivity.class);
				//startActivity(intMyMeetings);
				
				break;
			case 3:
				
				Intent intEvents=new Intent(this,MyEventListActivity.class);
				intEvents.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				startActivity(intEvents);
				finish();
				
				break;
			case 4:

				Intent intentTODO = new Intent(this, ToDoTaskActivity.class);
				intentTODO.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				startActivity(intentTODO);
				finish();
				
				break;
		/*	case 5:
			
				Intent intentMsg = new Intent(this, MessageBroadcastActivity.class);
				intentMsg.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				startActivity(intentMsg);
				finish();
				break;
		*/	
			case 5:
				
				Intent intSearchMembers=new Intent(this,SearchFunctionActivity.class);
				intSearchMembers.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				startActivity(intSearchMembers);
				finish();
				break;
				
			case 6:
				
				Intent intLogout=new Intent(this,LogoutActivity.class);
				intLogout.putExtra("classname", "MyMeetingListActivity");
				startActivity(intLogout);
				finish();
				break;
			
			}

			
		}else{
		
				
				switch (position) {
				case 0:
					Intent int1=new Intent(this,HomeActivity.class);
					int1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
					startActivity(int1);
					finish();
					break;
				case 2:	
					Intent intForm=new Intent(this,MasterSelectorScreenActivity.class);
					intForm.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
					startActivity(intForm);
					finish();
					break;
		//		case 2:
		//			break;
		//		case 3:
		//			break;
				case 3:
					Intent partner=new Intent(this,PartnerShipRecord.class);
					partner.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
					startActivity(partner);
					finish();
					break;
				case 1:
					Intent intForm1=new Intent(this,MyProfileActivity.class);
					intForm1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
					startActivity(intForm1);
					finish();
		
					break;
				
				case 4:
		//			//Intent intMeeting=new Intent(this,MeetingListActivity.class);
		//			//startActivity(intMeeting);
		//			Intent intMyMeetings=new Intent(this,MyMeetingListActivity.class);
		//			startActivity(intMyMeetings);
		
					break;
				case 5:
					Intent intEvents=new Intent(this,MyEventListActivity.class);
					intEvents.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
					startActivity(intEvents);
					finish();
					break;
				case 6:
					Intent intentTODO = new Intent(this, ToDoTaskActivity.class);
					intentTODO.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
					startActivity(intentTODO);
					finish();
					break;
					
				case 7:
					Intent intentMsg = new Intent(this, MessageBroadcastActivity.class);
					intentMsg.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
					startActivity(intentMsg);
					finish();
					break;
					
				case 8:
					Intent intSearchMembers=new Intent(this,SearchFunctionActivity.class);
					intSearchMembers.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
					startActivity(intSearchMembers);
					finish();
					break;
					
				case 9://logout
		//			mPreferenceHelper.addBoolean(Commons.ISUSER_LOGGEDIN, false);
		//			mPreferenceHelper.addString(Commons.USER_EMAILID, null);
		//			mPreferenceHelper.addString(Commons.USER_PASSWORD, null);
		
					Intent intLogout=new Intent(this,LogoutActivity.class);
					intLogout.putExtra("classname", "MyMeetingListActivity");
					startActivity(intLogout);
					finish();
					break;
		
		
				default:
					break;
				}

		}
		mDrawerLayout.closeDrawer(mLvDrawer);
//		getSupportActionBar().invalidateOptionsMenu();
	}

	

	private class DrawerItemClickListener implements
	ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);

		}
	}

	
	
	private void getMyMeetingList(final String pageno) {
	
		StringRequest reqMyMeetingList=new StringRequest(Method.POST,GetMyMeetingService.SERVICE_URL,new Listener<String>() {

			@Override
			public void onResponse(String response) {
				Methods.closeProgressDialog();
				Log.d("droid","get all reqMyMeetingList ---------------"+ response);

				if(response.contains("status"))
				{
					ResponseMessageModel2 respModel=gson.fromJson(response, ResponseMessageModel2.class);
					if(respModel.getMessage().getStatus()=="401"){
						Methods.longToast("User name or Password is incorrect", MyMeetingListActivity.this);
					}else{
						Methods.longToast(respModel.getMessage().getMessage(), MyMeetingListActivity.this);
					}
				}else{
					
					/*mMeetingModel=gson.fromJson(response, MeetingModel.class);
					//Object meetingmsg=mMeetingModel.getMessage();
					if(null !=mMeetingModel){
						//if(meetingmsg instanceof JSONArray){
						if(null !=mMeetingModel.getMessage() && mMeetingModel.getMessage().size() >0){
							mMeetingList=new ArrayList<MeetingModel.MeetingListModel>();
							mMeetingList=mMeetingModel.getMessage();
							Log.d("droid", "isjsonarray----------------------------------");
							if(mMeetingList.size() > 0){
								
								mMeetingListAdapter=new MeetingListAdapter(MyMeetingListActivity.this,mMeetingList);
								lvMeeting.setAdapter(mMeetingListAdapter);
							}
							
						}else{
							Methods.longToast("No meetings found", MyMeetingListActivity.this);
						}
					}*/
					
					
					try {
					
						JSONObject obj=new JSONObject(response);
						
						TOTAL_LIST_ITEMS=Integer.parseInt(obj.getJSONObject("message").getString("total_count"));
						
						Log.e("totalcount=", ""+TOTAL_LIST_ITEMS);
						tvTitle.setText("My Attendance("+TOTAL_LIST_ITEMS+")    ");
						
						
						
						jsonarray=obj.getJSONObject("message").getJSONArray("records");
						
						if(jsonarray.length() > 0){
							
							 Btnfooter();
							mMeetingListAdapter=new MeetingListAdapter(MyMeetingListActivity.this,jsonarray);
							lvMeeting.setAdapter(mMeetingListAdapter);
						}
						else{
							
						Methods.longToast("No meetings found", MyMeetingListActivity.this);
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
			}
		})
		{
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();

			/*	MeetingListRequestModel model=new MeetingListRequestModel();
				model.setUsername(mPreferenceHelper.getString(Commons.USER_EMAILID));
				model.setUserpass(mPreferenceHelper.getString(Commons.USER_PASSWORD));
*/
				JSONObject obj=new JSONObject();
				try {
					
					obj.put("username", mPreferenceHelper.getString(Commons.USER_EMAILID));
					obj.put("userpass", mPreferenceHelper.getString(Commons.USER_PASSWORD));
					obj.put("page_no", pageno);
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				String dataString=obj.toString();//gson.toJson(model, MeetingListRequestModel.class);
				
				//String dataString=gson.toJson(model, MeetingListRequestModel.class);

				Log.d("droid", dataString);
				params.put(GetMyMeetingService.DATA, dataString);
				return params; 
			}
		};

		App.getInstance().addToRequestQueue(reqMyMeetingList, "reqMyMeetingList");
		reqMyMeetingList.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		//Intent i=new Intent(this,.class);
		//startActivity(i);
	}

	class MeetingListAdapter extends BaseAdapter{

		private Context mContext;
		//private ArrayList<MeetingListModel> mList;
		private JSONArray jsonList;
		private Holder holder;

		public MeetingListAdapter(Context context,
				JSONArray meetingList) {
			mContext=context;
			//mList=new ArrayList<MeetingModel.MeetingListModel>();
			jsonList=meetingList;
		}

		@Override
		public int getCount() {
			return jsonList.length();
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
				holder.lblMeetingName=(TextView) convertView.findViewById(R.id.lblMeetingName);
				holder.lblMeetingsubject=(TextView) convertView.findViewById(R.id.lblMeetingsubject);
				holder.lblMeetingTime=(TextView) convertView.findViewById(R.id.lblMeetingTime);
				holder.lblMeetingVenue=(TextView) convertView.findViewById(R.id.lblMeetingVenue);
				holder.btnMarkAttendance=(Button) convertView.findViewById(R.id.btnMarkAttendance);
				convertView.setTag(holder);
			}else{
				holder = (Holder) convertView.getTag();
			}

			try {
				if(null !=jsonList.getJSONObject(position).getString("meeting_name").toString());
				holder.lblMeetingName.setText(jsonList.getJSONObject(position).getString("meeting_name").toString());
				
				if(null !=jsonList.getJSONObject(position).getString("meeting_subject").toString())
					holder.lblMeetingsubject.setText(jsonList.getJSONObject(position).getString("meeting_subject").toString());
				
				if(null !=jsonList.getJSONObject(position).getString("from_date").toString())
					holder.lblMeetingTime.setText(jsonList.getJSONObject(position).getString("from_date").toString());

				if(null !=jsonList.getJSONObject(position).getString("venue").toString())
					holder.lblMeetingVenue.setText((jsonList.getJSONObject(position).getString("venue").equals("null"))?"":jsonList.getJSONObject(position).getString("venue"));
//					holder.lblMeetingVenue.setText(jsonList.getJSONObject(position).getString("venue").toString());

				if(null !=jsonList.getJSONObject(position).getString("present").toString() && jsonList.getJSONObject(position).getString("present").toString().equals("1")){
				
					holder.btnMarkAttendance.setText("Already Attendance Marked");
	
				}else{
					
					holder.btnMarkAttendance.setText("Mark Attendance");
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
				
			holder.btnMarkAttendance.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent i=new Intent(MyMeetingListActivity.this,MarkMyAttendanceActivity.class);
					Bundle b=new Bundle();
					try {

						b.putString("MeetingCategory", jsonList.getJSONObject(position).getString("meeting_category").toString());
						b.putString("MeetingSubject", jsonList.getJSONObject(position).getString("meeting_subject").toString());
						b.putString("FromDate", jsonList.getJSONObject(position).getString("from_date").toString());
						b.putString("ToDate",  jsonList.getJSONObject(position).getString("to_date").toString());
//						b.putString("Venue", jsonList.getJSONObject(position).getString("venue").toString());
						b.putString("Venue", (jsonList.getJSONObject(position).getString("venue").equals("null"))?"":jsonList.getJSONObject(position).getString("venue"));
						b.putString("Name",jsonList.getJSONObject(position).getString("name").toString());
						b.putInt("Position_Clicked", position);
						b.putInt("status", jsonList.getJSONObject(position).getInt("present"));
						
					
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
					
					i.putExtra("Bundle", b);
					startActivityForResult(i,1);
				}
			});

			return convertView;
		}
	}

	public static class Holder{
		private TextView lblMeetingName,lblMeetingVenue,lblMeetingTime,lblMeetingsubject;
		private Button btnMarkAttendance;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode,resultCode,data);
	/*	if(requestCode==2)  
		{  
			
			if(null !=data){
				String updatedVal=data.getStringExtra("Value");
				int mPosition_Clicked=data.getIntExtra("Position_Clicked", 0);
				MeetingModel parentmodel=new MeetingModel();
				MeetingListModel model=parentmodel.new MeetingListModel();
			//	model=mMeetingList.get(mPosition_Clicked);
				model.setPresent(Integer.valueOf(updatedVal));
			//	mMeetingList.set(mPosition_Clicked, model);
				mMeetingListAdapter.notifyDataSetChanged();*/
				getMyMeetingList("1");
		/*	}
		}  */
	}
	//java.lang.ClassCastException: android.widget.LinearLayout$LayoutParams cannot be cast to android.widget.FrameLayout$LayoutParams

	private void Btnfooter()
	{
	    int val = TOTAL_LIST_ITEMS%NUM_ITEMS_PAGE;
	    val = val==0?0:1;
	    noOfBtns=TOTAL_LIST_ITEMS/NUM_ITEMS_PAGE+val;
	    
	   /* Display display=getWindowManager().getDefaultDisplay();
	    Point size=new Point();
	    display.getSize(size);
	    int width=size.x;*/
	    
	    
	    LinearLayout ll = (LinearLayout)findViewById(R.id.btnLay);
	 
	 /*   FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, LayoutParams.WRAP_CONTENT);
	    ll.setLayoutParams(params);*/
	   
	    ll.setBackgroundColor(getResources().getColor(android.R.color.white));
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
	               	if(NetworkHelper.isOnline(MyMeetingListActivity.this)){
	        			Methods.showProgressDialog(MyMeetingListActivity.this);
	        			getMyMeetingList(btns[j].getText().toString());
	        			
	        		}else{
	        			
	        			Methods.longToast("Please connect to Internet", MyMeetingListActivity.this);
	        		
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
		
		LayoutInflater layoutInflater = LayoutInflater.from(MyMeetingListActivity.this);
		View promptView = layoutInflater.inflate(R.layout.filter_dialog, null);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MyMeetingListActivity.this);
		
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
				Methods.showProgressDialog(MyMeetingListActivity.this);
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
				Methods.showProgressDialog(MyMeetingListActivity.this);
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
				Methods.showProgressDialog(MyMeetingListActivity.this);
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
				Methods.showProgressDialog(MyMeetingListActivity.this);
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
				Methods.showProgressDialog(MyMeetingListActivity.this);
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
					
						mZoneList.add(mPreferenceHelper.getString(Commons.USER_ROLE));
						spzone.setVisibility(View.VISIBLE);
						spzoneTextView.setVisibility(View.GONE);
						setAdapters();
						SpinnerDataFlag.Zones_flag=false;
					}else{
						
						try{
						
							String str=spresion.getSelectedItem().toString();
							Log.e("selected value", str);
							
							Methods.showProgressDialog(MyMeetingListActivity.this);
							getSpinnerData("Regions",str);
							
							spzone.setVisibility(View.VISIBLE);
							spzoneTextView.setVisibility(View.GONE);
							SpinnerDataFlag.Zones_flag=false;
						
						}catch(NullPointerException ex){
							
							Toast.makeText(MyMeetingListActivity.this, "Please Select Resion", Toast.LENGTH_LONG).show();
						
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
						
						Toast.makeText(MyMeetingListActivity.this, "Please Select Resion", Toast.LENGTH_LONG).show();
						
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
							
							Methods.showProgressDialog(MyMeetingListActivity.this);
							getSpinnerData("Zones",str);
							
							spgroupchurch.setVisibility(View.VISIBLE);
							spgroupchurchTextView.setVisibility(View.GONE);
							SpinnerDataFlag.GroupChurches_flag=false;
							
						}catch(NullPointerException ex){
							
							Toast.makeText(MyMeetingListActivity.this, "Please Select Zone", Toast.LENGTH_LONG).show();
						
						}
						
					}
					
				
				}else{
					if(spzoneTextView.getVisibility()!=View.VISIBLE){
					
						spgroupchurch.setVisibility(View.VISIBLE);
						spgroupchurchTextView.setVisibility(View.GONE);
						setAdapters();
					
					}else{
						
						Toast.makeText(MyMeetingListActivity.this, "Please Select Zone", Toast.LENGTH_LONG).show();
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
							
								Methods.showProgressDialog(MyMeetingListActivity.this);
								getSpinnerData("Group Churches",str);
							
								spchurch.setVisibility(View.VISIBLE);
								spchurchTextView.setVisibility(View.GONE);
								SpinnerDataFlag.Churches_flag=false;
							
							}else{
								
								Toast.makeText(MyMeetingListActivity.this, "Please Select Group Church", Toast.LENGTH_LONG).show();
							}
						}catch(NullPointerException ex){
							
							Toast.makeText(MyMeetingListActivity.this, "Please Select Group Church", Toast.LENGTH_LONG).show();
						
						}
						
					}
					
				}else{
					
					if(spgroupchurchTextView.getVisibility()!=View.VISIBLE){
						spchurch.setVisibility(View.VISIBLE);
						spchurchTextView.setVisibility(View.GONE);
						setAdapters();
					}else{
						
						Toast.makeText(MyMeetingListActivity.this, "Please Select Group Church", Toast.LENGTH_LONG).show();
						
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
							
							Methods.showProgressDialog(MyMeetingListActivity.this);
							getSpinnerData("Churches",str);
							
							sppcf.setVisibility(View.VISIBLE);
							sppcfTextView.setVisibility(View.GONE);
							SpinnerDataFlag.PCFs_flag=false;
					
						}catch(NullPointerException ex){
							
							Toast.makeText(MyMeetingListActivity.this, "Please Select Church", Toast.LENGTH_LONG).show();
						
						}
						
					}
					
				}else{
					
					if(spchurchTextView.getVisibility()!=View.VISIBLE){
					
						sppcf.setVisibility(View.VISIBLE);
						sppcfTextView.setVisibility(View.GONE);
						setAdapters();
					
					}else{
						
						Toast.makeText(MyMeetingListActivity.this, "Please Select Church", Toast.LENGTH_LONG).show();
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
							
							Methods.showProgressDialog(MyMeetingListActivity.this);
							getSpinnerData("PCFs",str);
							
							spSeniorCell.setVisibility(View.VISIBLE);
							spSeniorCellTextView.setVisibility(View.GONE);
							SpinnerDataFlag.SeniorCells_flag=false;
							
						}catch(NullPointerException ex){
							
							Toast.makeText(MyMeetingListActivity.this, "Please Select Pcf", Toast.LENGTH_LONG).show();
						
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
							
							Methods.showProgressDialog(MyMeetingListActivity.this);
							getSpinnerData("Senior Cells",str);
							
							spCell.setVisibility(View.VISIBLE);
							spCellTextView.setVisibility(View.GONE);
							SpinnerDataFlag.Cells_flag=false;	
							
						}catch(NullPointerException ex){
							
							Toast.makeText(MyMeetingListActivity.this, "Please Select Senior cell", Toast.LENGTH_LONG).show();
						
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
					
			
			if(NetworkHelper.isOnline(MyMeetingListActivity.this)){
				Methods.showProgressDialog(MyMeetingListActivity.this);
		
				getUpdatedListMethod(resion,zone,groupchurch,church,pcf,srcell,cell,fdate,tdate);
			
				dialog.cancel();

				
			}else{
			
				Methods.longToast("Please connect to Internet",MyMeetingListActivity.this);
			
			}
		}
	 })
	   .setNegativeButton("Cancel",  new DialogInterface.OnClickListener() {
		
		   public void onClick(DialogInterface dialog, int id) {
		
			   dialog.cancel();
			   
		   }
		
     });
		AlertDialog alertD = alertDialogBuilder.create();
		alertD.show();

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
					
					ArrayAdapter<String> adapterchurchgropu = new ArrayAdapter<String>(MyMeetingListActivity.this, android.R.layout.simple_spinner_item, mGrpChurchList);
					adapterchurchgropu.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spgroupchurch.setAdapter(adapterchurchgropu);
				
				}
				
				if(tblname.equals("Group Churches")){
					mChurchList.clear();
					
					for(int i=0;i<jarray.length();i++){
						mChurchList.add(jarray.getJSONObject(i).getString("name"));
					}
					
					ArrayAdapter<String> adapterChurch = new ArrayAdapter<String>(MyMeetingListActivity.this, android.R.layout.simple_spinner_item, mChurchList);
					adapterChurch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spchurch.setAdapter(adapterChurch);
					
					
				}
				
				if(tblname.equals("Churches")){
					mPCFList.clear();
					for(int i=0;i<jarray.length();i++){
						mPCFList.add(jarray.getJSONObject(i).getString("name"));
					}
					
					ArrayAdapter<String> adaptercell = new ArrayAdapter<String>(MyMeetingListActivity.this, android.R.layout.simple_spinner_item, mCellList);
					adaptercell.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spCell.setAdapter(adaptercell);
				}
				
				if(tblname.equals("PCFs")){
					
					mSeniorCellList.clear();
					for(int i=0;i<jarray.length();i++){
						mSeniorCellList.add(jarray.getJSONObject(i).getString("name"));
					}
					
					ArrayAdapter<String> adapterSrCell = new ArrayAdapter<String>(MyMeetingListActivity.this, android.R.layout.simple_spinner_item, mSeniorCellList);
					adapterSrCell.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spSeniorCell.setAdapter(adapterSrCell);
				}
				
				if(tblname.equals("Senior Cells")){
					
					mCellList.clear();
					
					for(int i=0;i<jarray.length();i++){
						mCellList.add(jarray.getJSONObject(i).getString("name"));
					}
					
					ArrayAdapter<String> adaptercell = new ArrayAdapter<String>(MyMeetingListActivity.this, android.R.layout.simple_spinner_item, mCellList);
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
				Methods.longToast("Access Denied", MyMeetingListActivity.this);
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

	ArrayAdapter<String> adapterZone = new ArrayAdapter<String>(MyMeetingListActivity.this, android.R.layout.simple_spinner_item, mZoneList);
	adapterZone.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	spzone.setAdapter(adapterZone);

	ArrayAdapter<String> adapterRegion = new ArrayAdapter<String>(MyMeetingListActivity.this, android.R.layout.simple_spinner_item, mRegionList);
	adapterRegion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	spresion.setAdapter(adapterRegion);

	ArrayAdapter<String> adapterChurch = new ArrayAdapter<String>(MyMeetingListActivity.this, android.R.layout.simple_spinner_item, mChurchList);
	adapterChurch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	spchurch.setAdapter(adapterChurch);

	ArrayAdapter<String> adapterSrCell = new ArrayAdapter<String>(MyMeetingListActivity.this, android.R.layout.simple_spinner_item, mSeniorCellList);
	adapterSrCell.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	spSeniorCell.setAdapter(adapterSrCell);

	ArrayAdapter<String> adapterchurchgropu = new ArrayAdapter<String>(MyMeetingListActivity.this, android.R.layout.simple_spinner_item, mGrpChurchList);
	adapterchurchgropu.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	spgroupchurch.setAdapter(adapterchurchgropu);

	ArrayAdapter<String> adapterPCF = new ArrayAdapter<String>(MyMeetingListActivity.this, android.R.layout.simple_spinner_item, mPCFList);
	adapterPCF.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	sppcf.setAdapter(adapterPCF);
	
	ArrayAdapter<String> adaptercell = new ArrayAdapter<String>(MyMeetingListActivity.this, android.R.layout.simple_spinner_item, mCellList);
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
				Methods.longToast("Access Denied", MyMeetingListActivity.this);
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

private void getUpdatedListMethod(final String resion,final String zone,final String gchurch,final String church,final String pcf,final String srcell,final String cell,final String fdate,final String todate){

	StringRequest reqgetLowerHierarchy=new StringRequest(Method.POST,GetMyMeetingService.SERVICE_URL,new Listener<String>() {

	@Override
	public void onResponse(String response) {
		Methods.closeProgressDialog();
		Log.e("droid get reqgetList Redord--------", response);

		

		if(response.contains("status"))
		{
			ResponseMessageModel2 respModel=gson.fromJson(response, ResponseMessageModel2.class);
		
			if(respModel.getMessage().getStatus()=="401"){
				Methods.longToast("User name or Password is incorrect", MyMeetingListActivity.this);
			}else{
				Methods.longToast(respModel.getMessage().getMessage(), MyMeetingListActivity.this);
			}
		}else{
			
				
				try {
					
					JSONObject jsonobj=new JSONObject(response);
					 
					 jsonobj.getJSONObject("message");
						
					TOTAL_LIST_ITEMS=Integer.parseInt(jsonobj.getJSONObject("message").getString("total_count"));
					
						Log.e("totalcount=", ""+TOTAL_LIST_ITEMS);
					
					//if(pageflag)
					 Btnfooter();
					 
					 jsonarray=jsonobj.getJSONObject("message").getJSONArray("records");
				
					if(jsonarray.length()>0){
						
						MeetingListAdapter adapter=new MeetingListAdapter(MyMeetingListActivity.this,jsonarray);
						lvMeeting.setAdapter(adapter);
						adapter.notifyDataSetChanged();
					
					}else{
						
							Methods.longToast("No results found", MyMeetingListActivity.this);
							MeetingListAdapter adapter=new MeetingListAdapter(MyMeetingListActivity.this,jsonarray);
							lvMeeting.setAdapter(adapter);
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

	public void onBackPressed() {
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		if (drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START);
		} else {
			new AlertDialog.Builder(this)
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setTitle("Closing Activity")
					.setMessage("Are you sure you want to exit?")
					.setPositiveButton("Yes", new DialogInterface.OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialog, int which) {
							moveTaskToBack(true);
						}

					})
					.setNegativeButton("No", null)
					.show();
		}
	}

}
