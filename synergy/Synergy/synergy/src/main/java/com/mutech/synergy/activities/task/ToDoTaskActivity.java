package com.mutech.synergy.activities.task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.mutech.messagebraudcast.MessageBroadcastActivity;
import com.mutech.synergy.App;
import com.mutech.synergy.R;
import com.mutech.synergy.SynergyValues;
import com.mutech.synergy.SynergyValues.Commons;
import com.mutech.synergy.Todoadpternew;
import com.mutech.synergy.activities.Feedback;
import com.mutech.synergy.activities.HomeActivity;
import com.mutech.synergy.activities.LogoutActivity;
import com.mutech.synergy.activities.MessageLogs;
import com.mutech.synergy.activities.cellMasters.MasterSelectorScreenActivity;
import com.mutech.synergy.activities.cellMasters.PartnerShipRecord;
import com.mutech.synergy.activities.cellMasters.SearchFunctionActivity;
import com.mutech.synergy.activities.event.EventListActivity;
import com.mutech.synergy.activities.event.MyEventListActivity;
import com.mutech.synergy.activities.meeting.MyMeetingListActivity;
import com.mutech.synergy.activities.profile.MyProfileActivity;
import com.mutech.synergy.activities.profile.ProfileView;
import com.mutech.synergy.adapters.CustomDrawerAdapter;
import com.mutech.synergy.adapters.TODOAdapter;
import com.mutech.synergy.models.DrawerItem;
import com.mutech.synergy.models.EventParticipantsModel;
import com.mutech.synergy.models.MeetingListRequestModel;
import com.mutech.synergy.models.MyTasksResponseModel;
import com.mutech.synergy.models.MyTasksResponseModel.Message;
import com.mutech.synergy.models.ResponseMessageModel2;
import com.mutech.synergy.utils.Methods;
import com.mutech.synergy.utils.NetworkHelper;
import com.mutech.synergy.utils.PreferenceHelper;
import com.mutech.synergy.widget.SlidingTabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ToDoTaskActivity extends AppCompatActivity {

	private ViewPager vpgrHome;
	private SlidingTabLayout mSlidingTabLayout;
	private TODOAdapter mAdapter;
	private PreferenceHelper mPreferenceHelper;
	private Gson gson;
	private ArrayList<Message> taskList;
	private MyTasksResponseModel model;
	
	private ArrayList<DrawerItem> mDrawerList;
	private CustomDrawerAdapter mCustomDrawerAdapter;
	private ListView mLvDrawer;
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	String str;
	private String Role,Name,Status,Designation,Image;

	Spinner spresion,spzone,sppcf,spgroupchurch,spchurch,spSeniorCell,spCell;
	private ArrayList<String> mZoneList,mRegionList,mChurchList,mSeniorCellList,mGrpChurchList,mPCFList,mCellList;
	public int TOTAL_LIST_ITEMS;
	public int NUM_ITEMS_PAGE   = 20;
	private int noOfBtns;
	private Button[] btns;
	String UserRoll;
	private DatePickerDialog fromDatePickerDialog,toDatePickerDialog;
	JSONObject jsonobj;
	JSONArray jsonarray;
	TextView titletextView;
	ListView weeklist;

	TextView txtFromDate,txtToDate;
	Spinner spTaskCategory, spTaskPriority;
	private SimpleDateFormat dateFormatter;
	Calendar newCalendar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo);
		mCellList=new ArrayList<String>();
		mPreferenceHelper=new PreferenceHelper(this);
		UserRoll=mPreferenceHelper.getString(Commons.USER_ROLE);
		initialize();
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	private void initialize() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setCustomView(R.layout.custom_actionbar);
		getSupportActionBar().setHomeAsUpIndicator(R.drawable.actiontop);
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2E9AFE")));
		getSupportActionBar().setDisplayShowCustomEnabled(true);

//		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//		getSupportActionBar().setHomeButtonEnabled(true);
//		getSupportActionBar().setCustomView(R.layout.custom_actionbar);
//		getSupportActionBar().setIcon(R.drawable.actiontop);

		TextView tvTitle = (TextView) getSupportActionBar().getCustomView().findViewById(R.id.title_text);	
		tvTitle.setText("To-Do       ");
		
		

		dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

		newCalendar = Calendar.getInstance();

		fromDatePickerDialog = new DatePickerDialog(ToDoTaskActivity.this, new DatePickerDialog.OnDateSetListener() {

			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
				txtFromDate.setText(dateFormatter.format(newDate.getTime()));
			}

		},newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

		toDatePickerDialog = new DatePickerDialog(ToDoTaskActivity.this, new DatePickerDialog.OnDateSetListener() {

			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
				txtToDate.setText(dateFormatter.format(newDate.getTime()));
			}

		},newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

		vpgrHome = (ViewPager) findViewById(R.id.vpgr_home);

		mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
		mSlidingTabLayout.setDistributeEvenly(true);
		mPreferenceHelper = new PreferenceHelper(this);
		str=mPreferenceHelper.getString(Commons.USER_ROLE);
		taskList = new ArrayList<MyTasksResponseModel.Message>();
		gson = new Gson();
		mDrawerList = new ArrayList<DrawerItem>();
		// mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
		// GravityCompat.START);

		addDrawerListData();

		mLvDrawer = (ListView) findViewById(R.id.left_drawer);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		mCustomDrawerAdapter = new CustomDrawerAdapter(this,R.layout.custom_dashboard_drawer_item, mDrawerList);
		mLvDrawer.setAdapter(mCustomDrawerAdapter);
		mLvDrawer.setOnItemClickListener(new DrawerItemClickListener());

		

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
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	//	actionBar.setHomeAsUpIndicator(R.drawable.actiontop);
		getSupportActionBar().setIcon(
				   new ColorDrawable(getResources().getColor(android.R.color.transparent))); 
	
//
		
		Log.e("roll",str);
		
		if(!str.equals("Member")){
		mAdapter = new TODOAdapter(ToDoTaskActivity.this, getSupportFragmentManager());
		vpgrHome.setAdapter(mAdapter);
		
		}else{
		
			Todoadpternew mAdapter = new Todoadpternew(ToDoTaskActivity.this, getSupportFragmentManager());
			vpgrHome.setAdapter(mAdapter);
			//mSlidingTabLayout.setViewPager(vpgrHome);
			//mSlidingTabLayout.setSelectedIndicatorColors(Color.parseColor("#33B5E5"));
			
		}
		
		mSlidingTabLayout.setViewPager(vpgrHome);
		mSlidingTabLayout.setSelectedIndicatorColors(Color.parseColor("#33B5E5"));
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




			if(mPreferenceHelper.getString(SynergyValues.Commons.USER_ROLE) != null){
				Role=mPreferenceHelper.getString(SynergyValues.Commons.USER_ROLE);}

			if(mPreferenceHelper.getString(Commons.USER_NAME) != null){
				Name=mPreferenceHelper.getString(SynergyValues.Commons.USER_NAME);}

			if(mPreferenceHelper.getString(Commons.USER_STATUS) != null){
				Status=mPreferenceHelper.getString(SynergyValues.Commons.USER_STATUS);}

			if(mPreferenceHelper.getString(Commons.USER_DESIGNATION) != null){
				Designation=mPreferenceHelper.getString(SynergyValues.Commons.USER_DESIGNATION);}

			if(mPreferenceHelper.getString(Commons.USER_IMAGE) != null){
				Image=mPreferenceHelper.getString(Commons.USER_IMAGE);}

			DrawerItem item00 = new DrawerItem();
			item00.setItemName(Name + "\n" + "Role: " + Role + "\n" + "Designation: " +Designation + "\n" + Status);
			item00.setImgResID(R.drawable.user);

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
			item11.setItemName("Feedback");
			item11.setImgResID(R.drawable.msg);

			DrawerItem item12=new DrawerItem();
			item12.setItemName("Message Logs");
			item12.setImgResID(R.drawable.msg);

			DrawerItem item13=new DrawerItem();
			item13.setItemName("Logout");
			item13.setImgResID(R.drawable.signout);

			mDrawerList.add(item00);
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
			mDrawerList.add(item12);
			mDrawerList.add(item13);
		}
	}

	private void selectItem(int position) {
		
		
		if(str.equalsIgnoreCase("Member")){
			
			switch (position) {
			case 0:
				
				//Intent intForm1=new Intent(this,MyProfileActivity.class);
				//startActivity(intForm1);
				
				break;
			case 1:	
				
				Intent partner=new Intent(this,PartnerShipRecord.class);
				partner.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				startActivity(partner);
				finish();
				break;
			
			case 2:
				
				Intent intMyMeetings=new Intent(this,MyMeetingListActivity.class);
				intMyMeetings.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				startActivity(intMyMeetings);
				finish();
				
				break;
			case 3:
				
				Intent intEvents=new Intent(this,MyEventListActivity.class);
				intEvents.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				startActivity(intEvents);
				finish();
				break;
			case 4:

				/*Intent intentTODO = new Intent(this, ToDoTaskActivity.class);
				intentTODO.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				startActivity(intentTODO);
				finish();*/
				
				break;
			/*case 5:
			
				Intent intentMsg = new Intent(this, MessageBroadcastActivity.class);
				intentMsg.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				startActivity(intentMsg);
				finish();
				break;*/
				
			case 5:
				
				Intent intSearchMembers=new Intent(this,SearchFunctionActivity.class);
				intSearchMembers.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				startActivity(intSearchMembers);
				finish();
				break;
			
			case 6:
				
				Intent intLogout=new Intent(this,LogoutActivity.class);
				intLogout.putExtra("classname", "ToDoTaskActivity");
				startActivity(intLogout);
				finish();
				
				break;
			
			}

			
		}else{
		
				switch (position) {
					case 0:
						//			Intent intForm1=new Intent(this,MyProfileActivity.class);
						//			startActivity(intForm1);
						break;

					case 1:

						Intent int1=new Intent(this,HomeActivity.class);
						int1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
						startActivity(int1);
						finish();
						break;
					case 3:
						Intent intForm=new Intent(this,MasterSelectorScreenActivity.class);
						intForm.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
						startActivity(intForm);
						finish();
						break;
					//		case 2:
					//			break;
					//		case 3:
					//			break;
					case 4:
						Intent partner=new Intent(this,PartnerShipRecord.class);
						partner.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
						startActivity(partner);
						finish();
						break;
					case 2:
						Intent intForm1=new Intent(this,ProfileView.class);
						intForm1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
						startActivity(intForm1);
						break;

					case 5:
						//Intent intMeeting=new Intent(this,MeetingListActivity.class);
						//startActivity(intMeeting);
						Intent intMyMeetings=new Intent(this,MyMeetingListActivity.class);
						intMyMeetings.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
						startActivity(intMyMeetings);
						finish();
						break;

					case 6:
						Intent intEvents=new Intent(this,MyEventListActivity.class);
						intEvents.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
						startActivity(intEvents);
						finish();
						break;
					case 7:
						//			Intent intentTODO = new Intent(this, ToDoTaskActivity.class);
						//			startActivity(intentTODO);
						break;

					case 8:
						Intent intentMsg = new Intent(this, MessageBroadcastActivity.class);
						intentMsg.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
						startActivity(intentMsg);
						finish();
						break;

					case 9:
						Intent intSearchMembers=new Intent(this,SearchFunctionActivity.class);
						intSearchMembers.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
						startActivity(intSearchMembers);
						finish();

						break;

					case 10:
//			mPreferenceHelper.addBoolean(Commons.ISUSER_LOGGEDIN, false);
//			mPreferenceHelper.addString(Commons.USER_EMAILID, null);
//			mPreferenceHelper.addString(Commons.USER_PASSWORD, null);

						Intent intfeedback=new Intent(this,Feedback.class);
						intfeedback.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
						startActivity(intfeedback);
						finish();
						break;

					case 11:
						Intent intmsglogs=new Intent(this,MessageLogs.class);
						intmsglogs.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
						startActivity(intmsglogs);
						break;
					
				case 12://logout
		//			mPreferenceHelper.addBoolean(Commons.ISUSER_LOGGEDIN, false);
		//			mPreferenceHelper.addString(Commons.USER_EMAILID, null);
		//			mPreferenceHelper.addString(Commons.USER_PASSWORD, null);
		
					Intent intLogout=new Intent(this,LogoutActivity.class);
					intLogout.putExtra("classname", "ToDoTaskActivity");
					startActivity(intLogout);
					finish();
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_todo1, menu);
		menu.findItem(R.id.menu_addTo).setIcon(R.drawable.ic_launcher).setTitle("Create Task");
//		menu.findItem(R.id.menu_option2).setIcon(R.drawable.ic_launcher).setTitle("Close Task");
//		return super.onCreateOptionsMenu(menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		switch (item.getItemId()) {
			case R.id.menu_addTo:
				startActivity(new Intent(this,CreateTaskActivity.class));
				break;
			case R.id.menu_filter:
				return false;
			default:
				break;
		}
		return false;
	}

	public void showDialog(){

		LayoutInflater layoutInflater = LayoutInflater.from(ToDoTaskActivity.this);
		View promptView = layoutInflater.inflate(R.layout.todo_filter_layout, null);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ToDoTaskActivity.this);

		txtFromDate=(TextView) promptView.findViewById(R.id.txtFromDate);
		txtToDate=(TextView) promptView.findViewById(R.id.txtToDate);
		spTaskCategory=(Spinner) promptView.findViewById(R.id.spTaskCategory);
		spTaskPriority=(Spinner) promptView.findViewById(R.id.spTaskPriority);

		String[] taskCategory = new String[] {"Open", "Working", "Pending Review", "Closed", "Cancelled"};
		ArrayAdapter<String> adapterTskC = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, taskCategory);
		adapterTskC.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spTaskCategory.setAdapter(adapterTskC);

		String[] taskPriority = new String[] {"Low", "Medium", "High", "Urgent"};
		ArrayAdapter<String> adapterTskP = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, taskPriority);
		adapterTskP.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spTaskPriority.setAdapter(adapterTskP);

		txtFromDate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				fromDatePickerDialog.show();

			}
		});

		txtToDate.setOnClickListener(new View.OnClickListener() {

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

				String fdate="",tdate="";

				fdate=txtFromDate.getText().toString();
				tdate=txtToDate.getText().toString();



//				if(!checkValidation(fdate,tdate)){
//					Methods.longToast("Please select any Filter",ToDoTaskActivity.this);
//				}
//				else
//				{
//					if(NetworkHelper.isOnline(ToDoTaskActivity.this)){
//
//						Methods.showProgressDialog(ToDoTaskActivity.this);
//
//						getUpdatedListMethod("First Timer",fdate,tdate);
//
//						dialog.cancel();
//
//					}else{
//
//						Methods.longToast("Please connect to Internet",ToDoTaskActivity.this);
//
//					}
//				}
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
