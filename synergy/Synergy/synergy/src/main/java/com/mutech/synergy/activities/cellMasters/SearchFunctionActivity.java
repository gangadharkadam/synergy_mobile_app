package com.mutech.synergy.activities.cellMasters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;

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
import com.mutech.synergy.SynergyValues.Web.GetAllChurchesService;
import com.mutech.synergy.SynergyValues.Web.GetAllGroupChurchesService;
import com.mutech.synergy.SynergyValues.Web.GetAllRegionsService;
import com.mutech.synergy.SynergyValues.Web.GetAllZonesService;
import com.mutech.synergy.SynergyValues.Web.GetHigherHierarchyService;
import com.mutech.synergy.SynergyValues.Web.LowerHierarchyService;
import com.mutech.synergy.SynergyValues.Web.SearchServiceforSpinner;
import com.mutech.synergy.activities.HomeActivity;
import com.mutech.synergy.activities.LoginActivity;
import com.mutech.synergy.activities.LogoutActivity;
import com.mutech.synergy.activities.dashboard.FirstTimeMonthActivity;
import com.mutech.synergy.activities.event.EventListActivity;
import com.mutech.synergy.activities.event.MyEventListActivity;
import com.mutech.synergy.activities.meeting.MyMeetingListActivity;
import com.mutech.synergy.activities.profile.MyProfileActivity;
import com.mutech.synergy.activities.task.ToDoTaskActivity;
import com.mutech.synergy.adapters.CustomDrawerAdapter;
import com.mutech.synergy.models.DrawerItem;
import com.mutech.synergy.models.HigherHierarchyRespModel;
import com.mutech.synergy.models.MeetingListRequestModel;
import com.mutech.synergy.models.HigherHierarchyRespModel.HHSubModel;
import com.mutech.synergy.utils.Methods;
import com.mutech.synergy.utils.NetworkHelper;
import com.mutech.synergy.utils.PreferenceHelper;

public class SearchFunctionActivity extends ActionBarActivity implements OnClickListener, OnCheckedChangeListener{

	private Spinner spnChurch,spnGroupChurch,spnZone,spnRegion;
	private Button btnSearch;
	private ArrayList<String> mChurchList,mGroupList,mZoneList,mRegionList;
	private String searchString;
	private EditText txtMember;
	
	TextView lblresion,lblzone,lblgroupchurch,lblchurch;
	RelativeLayout layresion,layzone,laychurch,laygroupchurch;
	
	CheckBox chkaddfilter;
	
	private RadioGroup rgrpSearchCategory;
	private RadioButton radioButton1,radioButton2,radioButton3;
	
	private PreferenceHelper mPreferenceHelper;
	private Gson gson;

	private ArrayList<DrawerItem> mDrawerList;
	private CustomDrawerAdapter mCustomDrawerAdapter;
	private ListView mLvDrawer;
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	String str;
	
	TextView spresionTextView,spzoneTextView,spgroupchurchTextView,spchurchTextView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		initialize();
	}

	@SuppressLint("NewApi")
	private void initialize() {

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setCustomView(R.layout.custom_actionbar);
		getSupportActionBar().setHomeAsUpIndicator(R.drawable.actiontop);
		getSupportActionBar().setDisplayShowCustomEnabled(true);
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2E9AFE")));

//		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//		getSupportActionBar().setHomeButtonEnabled(true);
//		getSupportActionBar().setCustomView(R.layout.custom_actionbar);
//		getSupportActionBar().setIcon(R.drawable.actiontop);
		TextView tvTitle=(TextView)getSupportActionBar().getCustomView().findViewById(R.id.title_text);
		tvTitle.setText("Search       ");
		

		lblgroupchurch=(TextView) findViewById(R.id.lblgroupchurch);
		lblzone=(TextView) findViewById(R.id.lblzone);
		lblresion=(TextView) findViewById(R.id.lblresion);
		lblchurch=(TextView) findViewById(R.id.lblchurch);
	
		
		laychurch=(RelativeLayout) findViewById(R.id.laychurch);
		layresion=(RelativeLayout) findViewById(R.id.layresion);
		layzone=(RelativeLayout) findViewById(R.id.layzone);
		laygroupchurch=(RelativeLayout) findViewById(R.id.laygroupchurch);
		
		chkaddfilter=(CheckBox) findViewById(R.id.chkaddfilter);
		spgroupchurchTextView=(TextView) findViewById(R.id.spgroupchurchTextView);
		spresionTextView=(TextView) findViewById(R.id.spresionTextView);
		spzoneTextView=(TextView) findViewById(R.id.spzoneTextView);
		spchurchTextView=(TextView) findViewById(R.id.spchurchTextView);
		
		
		spnChurch=(Spinner) findViewById(R.id.spnChurch);
		spnGroupChurch=(Spinner) findViewById(R.id.spnGroupChurch);
		spnZone=(Spinner) findViewById(R.id.spnZone);
		spnRegion=(Spinner) findViewById(R.id.spnRegion);
		txtMember=(EditText) findViewById(R.id.txtMemberName);

		btnSearch=(Button) findViewById(R.id.btnSearch);
		btnSearch.setOnClickListener(this);

		rgrpSearchCategory=(RadioGroup) findViewById(R.id.rgrpMeetingCategory);
		radioButton1=(RadioButton) findViewById(R.id.radioButton1);
		radioButton2=(RadioButton) findViewById(R.id.radioButton2);
		radioButton3=(RadioButton) findViewById(R.id.radioButton3);
		rgrpSearchCategory.setOnCheckedChangeListener(this);

		mChurchList=new ArrayList<String>();
		mGroupList=new ArrayList<String>();
		mZoneList=new ArrayList<String>();
		mRegionList=new ArrayList<String>();

		mPreferenceHelper=new PreferenceHelper(this);
		
		str=mPreferenceHelper.getString(Commons.USER_ROLE);
		
		gson=new Gson();
		
		
		
		mDrawerList = new ArrayList<DrawerItem>();
		// mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
		// GravityCompat.START);
		
		lblresion.setVisibility(View.GONE);
		layresion.setVisibility(View.GONE);
		lblzone.setVisibility(View.GONE);
		layzone.setVisibility(View.GONE);
		lblgroupchurch.setVisibility(View.GONE);
		laygroupchurch.setVisibility(View.GONE);
		lblchurch.setVisibility(View.GONE);
		laychurch.setVisibility(View.GONE);
		
		
		chkaddfilter.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(chkaddfilter.isChecked()){
					
					lblchurch.setVisibility(View.VISIBLE);
					lblresion.setVisibility(View.VISIBLE);
					layresion.setVisibility(View.VISIBLE);
					lblzone.setVisibility(View.VISIBLE);
					layzone.setVisibility(View.VISIBLE);
					lblgroupchurch.setVisibility(View.VISIBLE);
					laygroupchurch.setVisibility(View.VISIBLE);
					laychurch.setVisibility(View.VISIBLE);
					
				}else{
					
					lblresion.setVisibility(View.GONE);
					layresion.setVisibility(View.GONE);
					lblzone.setVisibility(View.GONE);
					layzone.setVisibility(View.GONE);
					lblgroupchurch.setVisibility(View.GONE);
					laygroupchurch.setVisibility(View.GONE);
					lblchurch.setVisibility(View.GONE);
					laychurch.setVisibility(View.GONE);
					
					
					
					
				}
			}
		});
			
			
		spresionTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getAllRegions();
				spnRegion.setVisibility(View.VISIBLE);
				spresionTextView.setVisibility(View.GONE);
			}
		});
		
		
		spnRegion.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				getSpinnerData("Regions",spnRegion.getSelectedItem().toString());
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});
		
		spzoneTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				spzoneTextView.setVisibility(View.GONE);
				spnZone.setVisibility(View.VISIBLE);
				
				if(spnRegion.getVisibility()==View.GONE)
					getAllZones();
				else
					getSpinnerData("Regions",spnRegion.getSelectedItem().toString());
			}
		});
		
		
		spnZone.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				getSpinnerData("Zones",spnZone.getSelectedItem().toString());
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});
		
		spgroupchurchTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				spnGroupChurch.setVisibility(View.VISIBLE);
				spgroupchurchTextView.setVisibility(View.GONE);
				
				if(spnZone.getVisibility()==View.GONE)
					getAllGroupChurches();
				else
					getSpinnerData("Zones",spnZone.getSelectedItem().toString());
			
				
			}
		});
		
		spnGroupChurch.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				getSpinnerData("Group Churches",spnGroupChurch.getSelectedItem().toString());
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});
		
		
		
		spchurchTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				spnChurch.setVisibility(View.VISIBLE);
				spchurchTextView.setVisibility(View.GONE);
				
				if(spnGroupChurch.getVisibility()==View.GONE)
					getAllChurches();
				else
					getSpinnerData("Group Churches",spnGroupChurch.getSelectedItem().toString());
			
			}
		});
		
		
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

		//searchString=getIntent().getExtras().getString("Search");

		if(NetworkHelper.isOnline(this)){
		//	Methods.showProgressDialog(this);
			getAllChurches();
			//getTopHierarchy();
		}else
		{
			Methods.longToast("Please connect to Internet", this);
		}
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		return super.onOptionsItemSelected(item);
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
		/*	case 5:
			
				Intent intentMsg = new Intent(this, MessageBroadcastActivity.class);
				intentMsg.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				startActivity(intentMsg);
				finish();
				break;*/
				
				
			case 5:
				
				//Intent intSearchMembers=new Intent(this,SearchFunctionActivity.class);
				//startActivity(intSearchMembers);
				
				break;
			case 6:
				
				Intent intLogout=new Intent(this,LogoutActivity.class);
				intLogout.putExtra("classname", "SearchFunctionActivity");
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
			//Intent intMeeting=new Intent(this,MeetingListActivity.class);
			//startActivity(intMeeting);
			Intent intMyMeetings=new Intent(this,MyMeetingListActivity.class);
			intMyMeetings.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(intMyMeetings);
			finish();
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
//			Intent intSearchMembers=new Intent(this,SearchFunctionActivity.class);
//			//intSearchMembers.putExtra("Search", "Member");
//			//intSearchGroup.putExtra("Search", "Group");
//			//intSearchLeaders.putExtra("Search", "Leader");
//			startActivity(intSearchMembers);

			break;

		case 9://logout
//			mPreferenceHelper.addBoolean(Commons.ISUSER_LOGGEDIN, false);
//			mPreferenceHelper.addString(Commons.USER_EMAILID, null);
//			mPreferenceHelper.addString(Commons.USER_PASSWORD, null);

			Intent intLogout=new Intent(this,LogoutActivity.class);
			intLogout.putExtra("classname", "SearchFunctionActivity");
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
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnSearch:
			if(rgrpSearchCategory.getCheckedRadioButtonId() !=-1){
				Intent intSearch=new Intent(this,ShowSearchResultFunctionality.class);
				try{
					
					String zoneval="",Regionval="",Churchval="",GroupChurchval="",Memberval="";
				
					if(spnZone.getVisibility()!=View.GONE){
						zoneval=spnZone.getSelectedItem().toString();
					}
					
					if(spnRegion.getVisibility()!=View.GONE){
						Regionval=spnRegion.getSelectedItem().toString();
					}
					if(spnGroupChurch.getVisibility()!=View.GONE){
						GroupChurchval=spnGroupChurch.getSelectedItem().toString();
					}
					
					if(spnChurch.getVisibility()!=View.GONE){
						Churchval=spnChurch.getSelectedItem().toString();
					}
				
				
					Memberval=txtMember.getText().toString();
					
					intSearch.putExtra("Zone",zoneval);
					intSearch.putExtra("Region",Regionval);
					intSearch.putExtra("Church", Churchval);
					intSearch.putExtra("GroupChurch", GroupChurchval);
					intSearch.putExtra("Member", Memberval);
					intSearch.putExtra("Search", searchString);
					startActivity(intSearch);
					
					
				}catch(Exception ex){
					
				}
				
			}else{
				Methods.longToast("Please select Search Category", this);
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.radioButton1:
			searchString="Member";
			break;
		case R.id.radioButton2:
			searchString="Group";
			break;
		case R.id.radioButton3:
			searchString="Church";
			break;

		default:
			break;
		}
	}

	private void getTopHierarchy() {

		StringRequest reqgetTopHierarchy=new StringRequest(Method.POST,GetHigherHierarchyService.SERVICE_URL,new Listener<String>() {

			@Override
			public void onResponse(String response) {
				Methods.closeProgressDialog();
				Log.d("droid","get reqgetTopHierarchy ---------------"+ response);

				HigherHierarchyRespModel mHHModel=gson.fromJson(response, HigherHierarchyRespModel.class);
				//Object meetingmsg=mMeetingModel.getMessage();
				if(null != mHHModel.getStatus() && mHHModel.getStatus().trim().length() >0){

					if(mHHModel.getStatus()=="401"){
						Methods.longToast("User name or Password is incorrect", SearchFunctionActivity.this);
					}
				}else{
					if(null !=mHHModel.getMessage() && mHHModel.getMessage().size() >0){
						ArrayList<HHSubModel> mHHSubModel=new ArrayList<HHSubModel>();
						//if(meetingmsg instanceof JSONArray){
						mHHSubModel=mHHModel.getMessage();

						for(int i=0;i<mHHSubModel.size();i++){	
							if(null !=mHHSubModel.get(i).getZone())
								mZoneList.add(mHHSubModel.get(i).getZone());
							if(null !=mHHSubModel.get(i).getRegion())
								mRegionList.add(mHHSubModel.get(i).getRegion());
							if(null !=mHHSubModel.get(i).getChurch())
								mChurchList.add(mHHSubModel.get(i).getChurch());
							if(null !=mHHSubModel.get(i).getChurch_group())
								mGroupList.add(mHHSubModel.get(i).getChurch_group());
							/*if(null !=mHHSubModel.get(i).getPcf())
								mPCFList.add(mHHSubModel.get(i).getPcf());
							if(null !=mHHSubModel.get(i).getSenior_cell())
								mSrCellList.add(mHHSubModel.get(i).getSenior_cell());*/
						}

					}else{
					}
				}
				Methods.showProgressDialog(SearchFunctionActivity.this);
				getLowerHierarchy();
			}
		},new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Methods.closeProgressDialog();
				Log.d("droid","get reqgetTopHierarchy error---------------"+ error.getCause());

				if(error.networkResponse.statusCode==403){
					//		Methods.longToast("Access Denied", CreateSeniorCellMasterActivity.this);
				}
				else
					//		Methods.longToast("Some Error Occured,please try again later", CreateSeniorCellMasterActivity.this);

					Methods.showProgressDialog(SearchFunctionActivity.this);
				getLowerHierarchy();
			}

		}){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError{
				Map<String, String> params = new HashMap<String, String>();

				MeetingListRequestModel model=new MeetingListRequestModel();
				model.setUsername(mPreferenceHelper.getString(Commons.USER_EMAILID));
				model.setUserpass(mPreferenceHelper.getString(Commons.USER_PASSWORD));
				model.setTbl(mPreferenceHelper.getString(Commons.USER_DEFKEY));
				model.setName(mPreferenceHelper.getString(Commons.USER_DEFVALUE));

				String dataString=gson.toJson(model, MeetingListRequestModel.class);

				Log.d("droid", dataString);
				params.put(GetHigherHierarchyService.DATA, dataString);
				return params;
			}
		};

		App.getInstance().addToRequestQueue(reqgetTopHierarchy, "reqgetTopHierarchy");
		reqgetTopHierarchy.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));

	}

	private void getLowerHierarchy(){


		StringRequest reqgetLowerHierarchy=new StringRequest(Method.POST,LowerHierarchyService.SERVICE_URL,new Listener<String>() {

			@Override
			public void onResponse(String response) {
				Methods.closeProgressDialog();
				Log.d("droid","get reqgetLowerHierarchy ---------------"+ response);

				HigherHierarchyRespModel mHHModel=gson.fromJson(response, HigherHierarchyRespModel.class);
				//Object meetingmsg=mMeetingModel.getMessage();
				if(null != mHHModel.getStatus() && mHHModel.getStatus().trim().length() >0){

					if(mHHModel.getStatus()=="401"){
						Methods.longToast("User name or Password is incorrect", SearchFunctionActivity.this);
					}
				}else{
					if(null !=mHHModel.getMessage() && mHHModel.getMessage().size() >0){
						ArrayList<HHSubModel> mHHSubModel=new ArrayList<HHSubModel>();
						//if(meetingmsg instanceof JSONArray){
						mHHSubModel=mHHModel.getMessage();


						for(int i=0;i<mHHSubModel.size();i++){	
							if(null !=mHHSubModel.get(i).getZone())
								mZoneList.add(mHHSubModel.get(i).getZone());
							if(null !=mHHSubModel.get(i).getRegion())
								mRegionList.add(mHHSubModel.get(i).getRegion());
							if(null !=mHHSubModel.get(i).getChurch())
								mChurchList.add(mHHSubModel.get(i).getChurch());
							if(null !=mHHSubModel.get(i).getChurch_group())
								mGroupList.add(mHHSubModel.get(i).getChurch_group());
							/*if(null !=mHHSubModel.get(i).getPcf())
								mPCFList.add(mHHSubModel.get(i).getPcf());
							if(null !=mHHSubModel.get(i).getSenior_cell())
								mSrCellList.add(mHHSubModel.get(i).getSenior_cell());*/
						}


					}else{
					}
				}
				setAdapters();

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
				setAdapters();
			}

		}){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError{
				Map<String, String> params = new HashMap<String, String>();

				MeetingListRequestModel model=new MeetingListRequestModel();
				model.setUsername(mPreferenceHelper.getString(Commons.USER_EMAILID));
				model.setUserpass(mPreferenceHelper.getString(Commons.USER_PASSWORD));
				model.setTbl(mPreferenceHelper.getString(Commons.USER_DEFKEY));
				model.setName(mPreferenceHelper.getString(Commons.USER_DEFVALUE));

				String dataString=gson.toJson(model, MeetingListRequestModel.class);

				Log.d("droid", dataString);
				params.put(GetHigherHierarchyService.DATA, dataString);
				return params;
			}
		};

		App.getInstance().addToRequestQueue(reqgetLowerHierarchy, "reqgetLowerHierarchy");
		reqgetLowerHierarchy.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));


	}

	private void setAdapters() {
		
		ArrayAdapter<String> adapterZone = new ArrayAdapter<String>(SearchFunctionActivity.this, android.R.layout.simple_spinner_item, mZoneList);
		adapterZone.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnZone.setAdapter(adapterZone);

		ArrayAdapter<String> adapterRegion = new ArrayAdapter<String>(SearchFunctionActivity.this, android.R.layout.simple_spinner_item, mRegionList);
		adapterRegion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnRegion.setAdapter(adapterRegion);

		ArrayAdapter<String> adapterChurch = new ArrayAdapter<String>(SearchFunctionActivity.this, android.R.layout.simple_spinner_item, mChurchList);
		adapterChurch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnChurch.setAdapter(adapterChurch);

		ArrayAdapter<String> adapterGrpChurch = new ArrayAdapter<String>(SearchFunctionActivity.this, android.R.layout.simple_spinner_item, mGroupList);
		adapterGrpChurch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnGroupChurch.setAdapter(adapterGrpChurch);

	}

	private void getAllZones() {
		StringRequest reqGetZones=new StringRequest(Method.GET,GetAllZonesService.SERVICE_URL,new Listener<String>() {

			@Override
			public void onResponse(String response) {
			//	Methods.closeProgressDialog();
				Log.d("droid","get all zones ---------------"+ response);

				try {
					JSONObject jsonObjectZones=new JSONObject(response);
					JSONArray jsonArrayZones=jsonObjectZones.getJSONArray("data");

					for(int i=0;i<jsonArrayZones.length();i++)
					{
						JSONObject curr = jsonArrayZones.getJSONObject(i);
						String keyValue = curr.getString("name");
						Log.d("Key",keyValue);
						mZoneList.add(keyValue);
					}

					ArrayAdapter<String> adapter = new ArrayAdapter<String>(
							SearchFunctionActivity.this, android.R.layout.simple_spinner_item, mZoneList);

					adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spnZone.setAdapter(adapter);

				} catch (JSONException e) {
					e.printStackTrace();
				}

				//Methods.showProgressDialog(SearchFunctionActivity.this);
				getAllRegions();
			}
		},new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Methods.closeProgressDialog();
				Log.d("droid","get all zones error---------------"+ error.getCause());
			}
		});

		App.getInstance().addToRequestQueue(reqGetZones, "reqGetZones");
		reqGetZones.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
	}

	private void getAllRegions() {
		StringRequest reqGetRegions=new StringRequest(Method.GET,GetAllRegionsService.SERVICE_URL,new Listener<String>() {

			@Override
			public void onResponse(String response) {
				Methods.closeProgressDialog();
				Log.d("droid", "get all regions ---------------" + response);

				try {
					JSONObject jsonObjectZones=new JSONObject(response);
					JSONArray jsonArrayZones=jsonObjectZones.getJSONArray("data");
					
					mRegionList.clear();
					
					for(int i=0;i<jsonArrayZones.length();i++)
					{
						JSONObject curr = jsonArrayZones.getJSONObject(i);
						String keyValue = curr.getString("name");
						Log.d("Key",keyValue);
						mRegionList.add(keyValue);
					}

					ArrayAdapter<String> adapter = new ArrayAdapter<String>(
							SearchFunctionActivity.this, android.R.layout.simple_spinner_item, mRegionList);

					adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spnRegion.setAdapter(adapter);

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		},new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Methods.closeProgressDialog();
				Log.d("droid","get all regions error---------------"+ error.getCause());
			}
		});

		App.getInstance().addToRequestQueue(reqGetRegions, "reqGetRegions");
		reqGetRegions.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
	}

	private void getAllChurches() {
		StringRequest reqGetChurches=new StringRequest(Method.GET,GetAllChurchesService.SERVICE_URL,new Listener<String>() {

			@Override
			public void onResponse(String response) {
				//Methods.closeProgressDialog();
				Log.d("droid", "get all Churches ---------------" + response);

				try {
					JSONObject jsonObjectZones=new JSONObject(response);
					JSONArray jsonArrayZones=jsonObjectZones.getJSONArray("data");

					mChurchList.clear();
					for(int i=0;i<jsonArrayZones.length();i++)
					{
						JSONObject curr = jsonArrayZones.getJSONObject(i);
						String keyValue = curr.getString("name");
						Log.d("Key",keyValue);
						mChurchList.add(keyValue);
					}

					ArrayAdapter<String> adapter = new ArrayAdapter<String>(
							SearchFunctionActivity.this, android.R.layout.simple_spinner_item, mChurchList);

					adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spnChurch.setAdapter(adapter);

				} catch (JSONException e) {
					e.printStackTrace();
				}

			//	Methods.showProgressDialog(SearchFunctionActivity.this);
			//	getAllGroupChurches();

			}
		},new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Methods.closeProgressDialog();
				Log.d("droid","get all Churches ---------------"+ error.getCause());
			}
		});

		App.getInstance().addToRequestQueue(reqGetChurches, "reqGetChurches");
		reqGetChurches.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
	}

	private void getAllGroupChurches(){
		StringRequest reqGetGroupChurches=new StringRequest(Method.GET,GetAllGroupChurchesService.SERVICE_URL,new Listener<String>() {

			@Override
			public void onResponse(String response) {
				//Methods.closeProgressDialog();
				Log.d("droid","get all Group Churches ---------------"+ response);

				try {
					JSONObject jsonObjectZones=new JSONObject(response);
					JSONArray jsonArrayZones=jsonObjectZones.getJSONArray("data");
					mGroupList.clear();
					for(int i=0;i<jsonArrayZones.length();i++)
					{
						JSONObject curr = jsonArrayZones.getJSONObject(i);
						String keyValue = curr.getString("name");
						Log.d("Key",keyValue);
						mGroupList.add(keyValue);
					}

					ArrayAdapter<String> adapter = new ArrayAdapter<String>(
							SearchFunctionActivity.this, android.R.layout.simple_spinner_item, mGroupList);

					adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spnGroupChurch.setAdapter(adapter);

				} catch (JSONException e) {
					e.printStackTrace();
				}

			//	Methods.showProgressDialog(SearchFunctionActivity.this);
				//getAllZones();

			}
		},new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Methods.closeProgressDialog();
				Log.d("droid","get all Group Churches ---------------"+ error.getCause());
			}
		});

		App.getInstance().addToRequestQueue(reqGetGroupChurches, "reqGetGroupChurches");
		reqGetGroupChurches.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
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
						mZoneList.clear();
					for(int i=0;i<jarray.length();i++){
						mZoneList.add(jarray.getJSONObject(i).getString("name"));
						}
						
					ArrayAdapter<String> adapterZone = new ArrayAdapter<String>(SearchFunctionActivity.this, android.R.layout.simple_spinner_item, mZoneList);
					adapterZone.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spnZone.setAdapter(adapterZone);
					
					}
					
					if(tblname.equals("Zones")){
						
						mGroupList.clear();
						
						for(int i=0;i<jarray.length();i++){
							mGroupList.add(jarray.getJSONObject(i).getString("name"));
						}
						
						ArrayAdapter<String> adapterGrpChurch = new ArrayAdapter<String>(SearchFunctionActivity.this, android.R.layout.simple_spinner_item, mGroupList);
						adapterGrpChurch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						spnGroupChurch.setAdapter(adapterGrpChurch);
					
					}
					
					if(tblname.equals("Group Churches")){
						
						mChurchList.clear();
						
						for(int i=0;i<jarray.length();i++){
							mChurchList.add(jarray.getJSONObject(i).getString("name"));
						}
						
						ArrayAdapter<String> adapterChurch = new ArrayAdapter<String>(SearchFunctionActivity.this, android.R.layout.simple_spinner_item, mChurchList);
						adapterChurch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						spnChurch.setAdapter(adapterChurch);
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
					Methods.longToast("Access Denied", SearchFunctionActivity.this);
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
	
	private void getExactSpinnerData(final String tblname) {
		Methods.closeProgressDialog();
		StringRequest reqgetTopHierarchy=new StringRequest(Method.POST,SearchServiceforSpinner.SERVICE_URL,new Listener<String>() {

			@Override
			public void onResponse(String response) {
				Methods.closeProgressDialog();
				Log.e("droid","getExactSpinnerData ---------------"+ response);

				try {
					JSONObject obj=new JSONObject(response);
					JSONArray jarray=obj.getJSONArray("message");
					
					if(tblname.equals("Regions")){
					for(int i=0;i<jarray.length();i++){
						mRegionList.clear();
						mRegionList.add(jarray.getJSONObject(i).getString("name"));
						
						}
					}
					
					if(tblname.equals("Zones")){
						for(int i=0;i<jarray.length();i++){
							mZoneList.clear();
							mZoneList.add(jarray.getJSONObject(i).getString("name"));
							
						}
					
					}
					
					if(tblname.equals("Group Churches")){
						for(int i=0;i<jarray.length();i++){
							mGroupList.clear();
							mGroupList.add(jarray.getJSONObject(i).getString("name"));
							
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
					Methods.longToast("Access Denied", SearchFunctionActivity.this);
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
			
				String dataString=gson.toJson(model, MeetingListRequestModel.class);

				Log.d("droid", dataString);
				params.put(LowerHierarchyService.DATA, dataString);
				return params;
			}
		};

		App.getInstance().addToRequestQueue(reqgetTopHierarchy, "reqgetTopHierarchy");
		reqgetTopHierarchy.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));

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
