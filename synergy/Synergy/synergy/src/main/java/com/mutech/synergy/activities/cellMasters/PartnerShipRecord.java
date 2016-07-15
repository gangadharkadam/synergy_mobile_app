package com.mutech.synergy.activities.cellMasters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.mutech.messagebraudcast.MessageBroadcastActivity;
import com.mutech.partnershiprecord.CreatePartnershipActivity;
import com.mutech.partnershiprecord.MyPartnershipActivity;
import com.mutech.partnershiprecord.MyPartnershipAdpter;
import com.mutech.partnershiprecord.PartnershipAdpter;
import com.mutech.synergy.App;
import com.mutech.synergy.R;
import com.mutech.synergy.SynergyValues;
import com.mutech.synergy.Todoadpternew;
import com.mutech.synergy.SynergyValues.Commons;
import com.mutech.synergy.SynergyValues.Web.GetPartnerShipService;
import com.mutech.synergy.SynergyValues.Web.ShowAllMembersService;
import com.mutech.synergy.activities.Feedback;
import com.mutech.synergy.activities.FoundationSchool;
import com.mutech.synergy.activities.HomeActivity;
import com.mutech.synergy.activities.LoginActivity;
import com.mutech.synergy.activities.LogoutActivity;
import com.mutech.synergy.activities.MessageLogs;
import com.mutech.synergy.activities.MinistryMaterials;
import com.mutech.synergy.activities.cellMasters.AllMemberListActivity.Holder;
import com.mutech.synergy.activities.cellMasters.AllMemberListActivity.MemberListAdapter;
import com.mutech.synergy.activities.event.EventListActivity;
import com.mutech.synergy.activities.event.MyEventListActivity;
import com.mutech.synergy.activities.meeting.MyMeetingListActivity;
import com.mutech.synergy.activities.profile.MyProfileActivity;
import com.mutech.synergy.activities.profile.ProfileView;
import com.mutech.synergy.activities.task.ToDoTaskActivity;
import com.mutech.synergy.adapters.CustomDrawerAdapter;
import com.mutech.synergy.adapters.TODOAdapter;
import com.mutech.synergy.models.AllMembersResponseModel;
import com.mutech.synergy.models.DrawerItem;
import com.mutech.synergy.models.MeetingListRequestModel;
import com.mutech.synergy.models.ResponseMessageModel2;
import com.mutech.synergy.models.AllMembersResponseModel.AllMemSubResponseModel;
import com.mutech.synergy.utils.Methods;
import com.mutech.synergy.utils.NetworkHelper;
import com.mutech.synergy.utils.PreferenceHelper;
import com.mutech.synergy.widget.SlidingTabLayout;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class PartnerShipRecord extends ActionBarActivity implements OnItemClickListener{

	//private ListView lvAllMembers;
	private ArrayList<String> mMembersList;
	private ArrayList<AllMemSubResponseModel> subModelList;

	private PreferenceHelper mPreferenceHelper;
	private Gson gson;

	private ViewPager vpgrHome;
	private SlidingTabLayout mSlidingTabLayout;

	private ArrayList<DrawerItem> mDrawerList;
	private CustomDrawerAdapter mCustomDrawerAdapter;
	private ListView mLvDrawer;
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	private  String str;
	public static PartnershipAdpter mAdapter;
	private String Role,Name,Status,Designation,Image;
	Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_partnershiplist);
		initialize();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_partnership_adpter, menu);


		String UserRoll=mPreferenceHelper.getString(Commons.USER_ROLE);

		if(UserRoll.equals("Member")||UserRoll.equals("PCF Leader")||UserRoll.equals("Senior Cell Leader")||UserRoll.equals("Cell Leader")){
			MenuItem item = menu.findItem(R.id.allartnership);
			item.setVisible(false);
		}


		return true;
	}

	@SuppressLint("NewApi")
	private void initialize() {

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setCustomView(R.layout.custom_actionbar);
		getSupportActionBar().setHomeAsUpIndicator(R.drawable.actiontop);
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2E9AFE")));
		getSupportActionBar().setDisplayShowCustomEnabled(true);
		TextView tvTitle=(TextView)getSupportActionBar().getCustomView().findViewById(R.id.title_text);

		mMembersList=new ArrayList<String>();

		mPreferenceHelper=new PreferenceHelper(this);

		if(mPreferenceHelper.getString(Commons.FROM_ACTIVITY).equals("true")) {
			tvTitle.setText("Partnership Records    ");
		}else{
			tvTitle.setText("My Partnership Records    ");}

		str=mPreferenceHelper.getString(Commons.USER_ROLE);

		gson=new Gson();

		vpgrHome = (ViewPager) findViewById(R.id.vpgr_home);
		mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
		mSlidingTabLayout.setDistributeEvenly(true);

		mAdapter = new PartnershipAdpter(PartnerShipRecord.this, getSupportFragmentManager());
		vpgrHome.setAdapter(mAdapter);

		mSlidingTabLayout.setViewPager(vpgrHome);
		mSlidingTabLayout.setSelectedIndicatorColors(Color.parseColor("#33B5E5"));

		mDrawerList = new ArrayList<DrawerItem>();

		intent=getIntent();
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

		getSupportActionBar().setIcon(
				new ColorDrawable(getResources().getColor(android.R.color.transparent)));





		if(NetworkHelper.isOnline(this)){
			//Methods.showProgressDialog(this);
			//getMemberList();
		}
		else
			Methods.longToast("Please connect to Internet", this);
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
			item12.setItemName("Ministry Materials");
			item12.setImgResID(R.drawable.ministry_materials);

			DrawerItem item13=new DrawerItem();
			item13.setItemName("Foundation School");
			item13.setImgResID(R.drawable.todo);

			DrawerItem item14=new DrawerItem();
			item14.setItemName("Message Logs");
			item14.setImgResID(R.drawable.msg);

			DrawerItem item15=new DrawerItem();
			item15.setItemName("Logout");
			item15.setImgResID(R.drawable.signout);

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
			mDrawerList.add(item14);
			mDrawerList.add(item15);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;

		}

		//	String UserRoll=mPreferenceHelper.getString(Commons.USER_ROLE);
		//	if(UserRoll)

		int id = item.getItemId();
		if (id == R.id.allartnership) {

			startActivity(new Intent(PartnerShipRecord.this, MyPartnershipActivity.class));
			return true;
		}

		if (id == R.id.action_create) {

			startActivity(new Intent(PartnerShipRecord.this, CreatePartnershipActivity.class));
			return true;
		}
		//action_create

		return super.onOptionsItemSelected(item);
	}

	private void selectItem(int position) {


		if(str.equalsIgnoreCase("Member")){

			switch (position) {
				case 0:

					Intent intForm1=new Intent(this,ProfileView.class);
					intForm1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
					startActivity(intForm1);
					break;
				case 1:

					//Intent partner=new Intent(this,PartnerShipRecord.class);
					//	startActivity(partner);

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

					Intent intentTODO = new Intent(this, ToDoTaskActivity.class);
					intentTODO.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
					startActivity(intentTODO);
					finish();
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
					intLogout.putExtra("classname", "PartnerShipRecord");
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

				case 2:
                Intent intForm1=new Intent(this,ProfileView.class);
                intForm1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intForm1);
					finish();
					break;
//            case 1:
//				Intent intForm1=new Intent(this,MyProfileActivity.class);
//				startActivity(intForm1);
//                break;

				case 3:
					Log.d("NonStop", "Going to Database");
					Intent intForm=new Intent(this,MasterSelectorScreenActivity.class);
					intForm.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
					startActivity(intForm);
					finish();
					break;

				case 4:
					Log.d("NonStop", "Going to Partnership Record");
				/*	Intent partner=new Intent(this,PartnerShipRecord.class);
					partner.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
					startActivity(partner);
					finish();*/
					break;

				case 5:
					//	Intent intMyMeetings=new Intent(this,MeetingListActivity.class);
					//startActivity(intMeeting);
					Log.d("NonStop", "Going to Attendance");
					Intent intMyMeetings=new Intent(this,MyMeetingListActivity.class);
					intMyMeetings.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
					startActivity(intMyMeetings);
					finish();
					break;
				case 6:
					Log.d("NonStop", "Going to Calendar");
					Intent intEvents=new Intent(this,MyEventListActivity.class);
					intEvents.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
					startActivity(intEvents);
					finish();

					break;
				case 7:
					Log.d("NonStop", "Going to ToDo");
					Intent intentTODO = new Intent(this, ToDoTaskActivity.class);
					intentTODO.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
					startActivity(intentTODO);
					finish();
					break;

				case 8:
					Intent intentMsg = new Intent(this, MessageBroadcastActivity.class);
					intentMsg.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
					startActivity(intentMsg);
					finish();
					break;

				case 9:
					Log.d("NonStop", "Going to Search");
					Intent intSearchMembers=new Intent(this,SearchFunctionActivity.class);
					intSearchMembers.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
					startActivity(intSearchMembers);
					finish();

					break;

				case 10:

					Intent intfeedback=new Intent(this,Feedback.class);
					intfeedback.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
					startActivity(intfeedback);
					finish();
					break;

				case 11:
					Intent int3=new Intent(this,MinistryMaterials.class);
					int3.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
					startActivity(int3);
					break;

				case 12:
					Intent int2=new Intent(this,FoundationSchool.class);
					int2.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
					startActivity(int2);
					break;

				case 13:
					Intent intmsglogs=new Intent(this,MessageLogs.class);
					intmsglogs.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
					startActivity(intmsglogs);
					break;


				case 14://logout
//			mPreferenceHelper.addBoolean(Commons.ISUSER_LOGGEDIN, false);
//			mPreferenceHelper.addString(Commons.USER_EMAILID, null);
//			mPreferenceHelper.addString(Commons.USER_PASSWORD, null);

					Intent intLogout=new Intent(this,LogoutActivity.class);
					intLogout.putExtra("classname", "PartnerShipRecord");
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


	private void getMemberList() {

		StringRequest reqGetMembers=new StringRequest(Method.POST,GetPartnerShipService.SERVICE_URL,new Listener<String>() {
			@Override
			public void onResponse(String response) {
				Methods.closeProgressDialog();
				Log.d("droid","getMemberList ---------------"+ response);

				if(response.contains("status")){
					ResponseMessageModel2 resp2Model=gson.fromJson(response, ResponseMessageModel2.class);
					Methods.longToast(resp2Model.getMessage().getMessage(), PartnerShipRecord.this);
				}else{
					AllMembersResponseModel model=gson.fromJson(response, AllMembersResponseModel.class);
					if(null !=model.getMessage() && model.getMessage().size() >0){
						subModelList=new ArrayList<AllMemSubResponseModel>();
						for(int i=0;i<model.getMessage().size();i++){
							subModelList.add(model.getMessage().get(i));
							Log.d("droid", "subModelList :::::::"+model.getMessage().get(i));
						}
						Log.d("droid", "subModelList final size ::: "+subModelList.size());
						MemberListAdapter adapter=new MemberListAdapter(PartnerShipRecord.this,subModelList);
						//	lvAllMembers.setAdapter(adapter);
					}
				}

			}
		},new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Methods.closeProgressDialog();
				Log.d("droid","get all pcf error---------------"+ error.getCause());
			}
		})
		{
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();

				MeetingListRequestModel model=new MeetingListRequestModel();
				model.setUsername(mPreferenceHelper.getString(Commons.USER_EMAILID));
				model.setUserpass(mPreferenceHelper.getString(Commons.USER_PASSWORD));

				String dataString=gson.toJson(model, MeetingListRequestModel.class);

				Log.d("droid","data passed is ::::::::"+dataString);
				params.put(ShowAllMembersService.DATA, dataString);
				return params;
			}
		};
		App.getInstance().addToRequestQueue(reqGetMembers, "reqGetMembers");
		reqGetMembers.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		Intent intPartnershipDetails=new Intent(this,PartnerRecordInformation.class);
		intPartnershipDetails.putExtra("PartnerNo", subModelList.get(position).getName());
		startActivity(intPartnershipDetails);
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
				convertView = layout.inflate(R.layout.row_partnerlist, null);

				holder.txtMasterName=(TextView) convertView.findViewById(R.id.txtMasterName);
				holder.txtPartnershiparm =(TextView) convertView.findViewById(R.id.txtPartnershiparm);
				holder.txtAmount =(TextView) convertView.findViewById(R.id.txtAmount);
				convertView.setTag(holder);
			}else{
				holder = (Holder) convertView.getTag();
			}

			holder.txtMasterName.setText(mList.get(position).getName());
			holder.txtPartnershiparm.setText(mList.get(position).getPartnership_arms());
			holder.txtAmount.setText(mList.get(position).getAmount());
			return convertView;
		}

	}
	public static class Holder{
		private TextView txtMasterName;
		private TextView txtPartnershiparm;
		private TextView txtAmount;
	}

	public void onBackPressed() {
		if(intent.hasExtra("fromactivity")){
			super.onBackPressed();
		}else{
			DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
			if (drawer.isDrawerOpen(GravityCompat.START)) {
				drawer.closeDrawer(GravityCompat.START);
			} else {
				AlertDialog dialog = new AlertDialog.Builder(this)
						.setIcon(android.R.drawable.ic_dialog_alert)
						.setTitle("Closing Activity")
						.setMessage("Are you sure you want to exit?")
						.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								moveTaskToBack(true);
							}

						})
						.setNegativeButton("No", null)
						.show();
				TextView textView = (TextView) dialog.findViewById(android.R.id.message);
				textView.setTextSize(18);
			}
	}
	}

}