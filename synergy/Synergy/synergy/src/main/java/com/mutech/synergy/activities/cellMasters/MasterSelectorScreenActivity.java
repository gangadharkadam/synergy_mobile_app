//Package name of the class
package com.mutech.synergy.activities.cellMasters;

//Api imported
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mutech.databasedetails.CreateFirstTimerActivity;
import com.mutech.databasedetails.CreateNewMemberActivity;
import com.mutech.databasedetails.FirstTimerInDatabaseActivity;
import com.mutech.messagebraudcast.MessageBroadcastActivity;
import com.mutech.synergy.R;
import com.mutech.synergy.SynergyValues;
import com.mutech.synergy.SynergyValues.Commons;
import com.mutech.synergy.activities.Feedback;
import com.mutech.synergy.activities.FoundationSchool;
import com.mutech.synergy.activities.HomeActivity;
import com.mutech.synergy.activities.LogoutActivity;
import com.mutech.synergy.activities.MessageLogs;
import com.mutech.synergy.activities.MinistryMaterials;
import com.mutech.synergy.activities.event.MyEventListActivity;
import com.mutech.synergy.activities.meeting.MyMeetingListActivity;
import com.mutech.synergy.activities.task.ToDoTaskActivity;
import com.mutech.synergy.adapters.CustomDrawerAdapter;
import com.mutech.synergy.models.DrawerItem;
import com.mutech.synergy.utils.PreferenceHelper;


 //class extend with the ActionBarActivity with Onclicklistener
 public class MasterSelectorScreenActivity extends ActionBarActivity implements OnClickListener{

	//Button variables 
	private Button btnPCFMaster,btnSrCellMaster,btnCellMaster,btnAllMembers,btnAllfirsttimer,btnRegion,btnZoneMaster,btnGroupChurchMaster,btnChurchMaster;
	//Sharedpreference object
	private PreferenceHelper mPreferenceHelper;
	//ImageView variables
	private ImageView btnAddSrCellMaster,btnAddCellMaster,btnAddPCFMaster,btnAddmember,btnAddFirsttimer,btnAddChurchMaster,btnAddGroupChurchMaster,btnAddZoneMaster,btnAddRegion;
	//Linearlayout
	private LinearLayout llAllMembers,llCell,llSrCell,llPCF,llRegion,llZone,llGroupChurch,llChurch;

	//ArrayList variables for DrawerList
	private ArrayList<DrawerItem> mDrawerList;
	//Adapater object
	private CustomDrawerAdapter mCustomDrawerAdapter;
	//Listview variables
	private ListView mLvDrawer;
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	String str;
	private String Role,Name,Status,Designation,Image;



	//onCreate method
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_masterselector);
		initialize();
	}

	//initization of variables method
	@SuppressLint("NewApi")
	private void initialize() {
//		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//		getSupportActionBar().setHomeButtonEnabled(true);
//		getSupportActionBar().setCustomView(R.layout.custom_actionbar);
//		getSupportActionBar().setIcon(R.drawable.actiontop);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setCustomView(R.layout.custom_actionbar);
		getSupportActionBar().setHomeAsUpIndicator(R.drawable.actiontop);
		TextView tvTitle=(TextView)getSupportActionBar().getCustomView().findViewById(R.id.title_text);
		tvTitle.setText("Database       ");

		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2E9AFE")));

		getSupportActionBar().setDisplayShowCustomEnabled(true);
		btnAllfirsttimer=(Button) findViewById(R.id.btnAllfirsttimer);
		btnPCFMaster=(Button) findViewById(R.id.btnPCFMaster);
		btnSrCellMaster=(Button) findViewById(R.id.btnSrCellMaster);
		btnCellMaster=(Button) findViewById(R.id.btnCellMaster);
		btnAddPCFMaster=(ImageView) findViewById(R.id.btnAddPCFMaster);
		btnAddSrCellMaster=(ImageView) findViewById(R.id.btnAddSrCellMaster);
		btnAddCellMaster=(ImageView) findViewById(R.id.btnAddCellMaster);
		btnAllMembers=(Button) findViewById(R.id.btnAllMembers);
		btnAddmember=(ImageView) findViewById(R.id.btnAddmember);
		btnAddFirsttimer=(ImageView) findViewById(R.id.btnAddFirsttimer);

		btnChurchMaster=(Button) findViewById(R.id.btnChurchMaster);
		btnGroupChurchMaster=(Button) findViewById(R.id.btnGroupChurchMaster);
		btnZoneMaster=(Button) findViewById(R.id.btnZoneMaster);
		btnRegion=(Button) findViewById(R.id.btnRegion);
		btnAddChurchMaster=(ImageView) findViewById(R.id.btnAddChurchMaster);
		btnAddGroupChurchMaster=(ImageView) findViewById(R.id.btnAddGroupChurchMaster);
		btnAddZoneMaster=(ImageView) findViewById(R.id.btnAddZoneMaster);
		btnAddRegion=(ImageView) findViewById(R.id.btnAddRegion);


		btnAddFirsttimer.setOnClickListener(this);
		btnAddmember.setOnClickListener(this);
		btnPCFMaster.setOnClickListener(this);
		btnSrCellMaster.setOnClickListener(this);
		btnCellMaster.setOnClickListener(this);
		btnAddPCFMaster.setOnClickListener(this);
		btnAddSrCellMaster.setOnClickListener(this);
		btnAddCellMaster.setOnClickListener(this);
		btnAllMembers.setOnClickListener(this);
		btnAllfirsttimer.setOnClickListener(this);
		llAllMembers=(LinearLayout) findViewById(R.id.llAllMembers);
		llCell=(LinearLayout) findViewById(R.id.llCell);
		llSrCell=(LinearLayout) findViewById(R.id.llSrCell);
		llPCF=(LinearLayout) findViewById(R.id.llPCF);

		btnAddChurchMaster.setOnClickListener(this);
		btnAddGroupChurchMaster.setOnClickListener(this);
		btnAddZoneMaster.setOnClickListener(this);
		btnAddRegion.setOnClickListener(this);
		btnChurchMaster.setOnClickListener(this);
		btnGroupChurchMaster.setOnClickListener(this);
		btnZoneMaster.setOnClickListener(this);
		btnRegion.setOnClickListener(this);

		llChurch=(LinearLayout) findViewById(R.id.llChurch);
		llGroupChurch=(LinearLayout) findViewById(R.id.llGroupChurch);
		llZone=(LinearLayout) findViewById(R.id.llZone);
		llRegion=(LinearLayout) findViewById(R.id.llRegion);

		mPreferenceHelper=new PreferenceHelper(this);

		str=mPreferenceHelper.getString(Commons.USER_ROLE);

		String str=mPreferenceHelper.getString(Commons.USER_ROLE);

		Log.e(null, "Role--"+str);
		//Role--Cell Leader,Senior Cell Leader
  //btnAddPCFMaster
		//	01-14 18:30:29.183: E/list(30706): Zonal Pastor

		if(str.equalsIgnoreCase("administrator")){
			btnAddRegion.setVisibility(View.VISIBLE);
		}

		if(str.equalsIgnoreCase("Regional Pastor")){
			btnAddRegion.setVisibility(View.GONE);
		}

		if(str.equalsIgnoreCase("Zonal Pastor")){
			llRegion.setVisibility(View.GONE);
			btnAddZoneMaster.setVisibility(View.GONE);
		}

		if(str.equalsIgnoreCase("Group Church Pastor")){
			llRegion.setVisibility(View.GONE);
			llZone.setVisibility(View.GONE);
			btnAddGroupChurchMaster.setVisibility(View.GONE);
		}

		if(str.equalsIgnoreCase("Church Pastor")){
			llRegion.setVisibility(View.GONE);
			llZone.setVisibility(View.GONE);
			llGroupChurch.setVisibility(View.GONE);
			btnAddChurchMaster.setVisibility(View.GONE);

		}

		if(str.equalsIgnoreCase("PCF Leader")){
			llRegion.setVisibility(View.GONE);
			llZone.setVisibility(View.GONE);
			llGroupChurch.setVisibility(View.GONE);
			llChurch.setVisibility(View.GONE);
			btnAddPCFMaster.setVisibility(View.GONE);

		}

		if(str.equalsIgnoreCase("Cell Leader")){
			llRegion.setVisibility(View.GONE);
			llZone.setVisibility(View.GONE);
			llGroupChurch.setVisibility(View.GONE);
			llChurch.setVisibility(View.GONE);
			llPCF.setVisibility(View.GONE);
			llSrCell.setVisibility(View.GONE);
			btnAddCellMaster.setVisibility(View.GONE);

		}

		if(str.equalsIgnoreCase("Member")){
			llRegion.setVisibility(View.GONE);
			llZone.setVisibility(View.GONE);
			llGroupChurch.setVisibility(View.GONE);
			llChurch.setVisibility(View.GONE);
			llPCF.setVisibility(View.GONE);
			llSrCell.setVisibility(View.GONE);
			llCell.setVisibility(View.GONE);
			llAllMembers.setVisibility(View.GONE);
			Toast.makeText(MasterSelectorScreenActivity.this, "NOT ACCESSIBLE FOR MEMBER", Toast.LENGTH_SHORT).show();
		}

		if(str.equals("Senior Cell Leader")){

			llRegion.setVisibility(View.GONE);
			llZone.setVisibility(View.GONE);
			llGroupChurch.setVisibility(View.GONE);
			llChurch.setVisibility(View.GONE);
			llPCF.setVisibility(View.GONE);
			btnAddSrCellMaster.setVisibility(View.GONE);

		}


		mDrawerList = new ArrayList<DrawerItem>();

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//		actionBar.setHomeAsUpIndicator(R.drawable.actiontop);
		getSupportActionBar().setIcon(
				new ColorDrawable(getResources().getColor(android.R.color.transparent)));


		//Adding drawer item
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
	}


	private void addDrawerListData() {

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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private void selectItem(int position) {
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
              /*  Intent intForm1=new Intent(this,ProfileView.class);
                intForm1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intForm1);*/
				break;
//            case 1:
//				Intent intForm1=new Intent(this,MyProfileActivity.class);
//				startActivity(intForm1);
//                break;

			case 3:
				Log.d("NonStop", "Going to Database");
			/*	Intent intForm=new Intent(this,MasterSelectorScreenActivity.class);
				intForm.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				startActivity(intForm);
				finish();*/
				break;

			case 4:
				Log.d("NonStop", "Going to Partnership Record");
				Intent partner=new Intent(this,PartnerShipRecord.class);
				partner.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				startActivity(partner);
				finish();
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
				Intent intLogout=new Intent(this,LogoutActivity.class);
				intLogout.putExtra("classname","MasterSelectorScreenActivity");
				startActivity(intLogout);
				finish();
				break;


			default:
				break;
		}

		mDrawerLayout.closeDrawer(mLvDrawer);
//		getSupportActionBar().invalidateOptionsMenu();
	}

	//Listener class
	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
								long id) {
			selectItem(position);
		}
	}


	//Onclick method when particular button is clicked depending id
	@Override
	public void onClick(View v) {
		switch (v.getId()) {

			case R.id.btnAddRegion:
				Intent intregion=new Intent(this,CreateRegionMasterFormActivity.class);
				startActivity(intregion);
				break;

			case R.id.btnAddZoneMaster:
				Intent intzone=new Intent(this,CreateZoneMasterFormActivity.class);
				startActivity(intzone);
				break;

			case R.id.btnAddGroupChurchMaster:
				Intent intgroupchurch=new Intent(this,CreateGroupChurchMasterFormActivity.class);
				startActivity(intgroupchurch);
				break;

			case R.id.btnAddChurchMaster:
				Intent intchurch=new Intent(this,CreateChurchMasterFormActivity.class);
				startActivity(intchurch);
				break;

			case R.id.btnAddPCFMaster:
				Intent intpcf=new Intent(this,CreatePcfMasterFormActivity.class);
				startActivity(intpcf);
				break;

			case R.id.btnAddSrCellMaster:
				Intent intSrCell=new Intent(this,CreateSeniorCellMasterActivity.class);
				startActivity(intSrCell);
				break;

			case R.id.btnAddCellMaster:
				Log.d("NonStop", "Going to AddCellMaster");
				Intent intCell=new Intent(this,NewCreateCellMasterActivity.class);
				startActivity(intCell);
				break;

			case R.id.btnRegion:
				Intent intRegionlist=new Intent(this,DisplayMastersListActivity.class);
				intRegionlist.putExtra("OptionSelected", "Regions");
				startActivity(intRegionlist);
				break;

			case R.id.btnZoneMaster:
				Intent intZonelist=new Intent(this,DisplayMastersListActivity.class);
				intZonelist.putExtra("OptionSelected", "Zones");
				startActivity(intZonelist);
				break;

			case R.id.btnChurchMaster:
				Intent intChurchlist=new Intent(this,DisplayMastersListActivity.class);
				intChurchlist.putExtra("OptionSelected", "Churches");
				startActivity(intChurchlist);
				break;

			case R.id.btnGroupChurchMaster:
				Intent intGrChurchlist=new Intent(this,DisplayMastersListActivity.class);
				intGrChurchlist.putExtra("OptionSelected", "Group Churches");
				startActivity(intGrChurchlist);
				break;

			case R.id.btnPCFMaster:
				Intent intPcflist=new Intent(this,DisplayMastersListActivity.class);
				intPcflist.putExtra("OptionSelected", "PCF");
				startActivity(intPcflist);
				break;

			case R.id.btnCellMaster:
				Log.d("NonStop", "Going to CellMaster");
				Intent intCelllist=new Intent(this,DisplayMastersListActivity.class);
				intCelllist.putExtra("OptionSelected", "Cell");
				startActivity(intCelllist);
				break;

			case R.id.btnSrCellMaster:
				Intent intSrCelllist=new Intent(this,DisplayMastersListActivity.class);
				intSrCelllist.putExtra("OptionSelected", "Sr Cell");
				startActivity(intSrCelllist);
				break;

			case R.id.btnAllMembers:
				Log.d("NonStop", "Going to All Members");
				Intent intAllMem=new Intent(this,AllMemberListActivity.class);
				intAllMem.putExtra("role", "Member");
				startActivity(intAllMem);
				break;

			case R.id.btnAllfirsttimer:
				Log.d("NonStop", "Going to First Timer in Db");
				Intent intAllfirsttimer=new Intent(this,FirstTimerInDatabaseActivity.class);
				intAllfirsttimer.putExtra("role", "Member");
				startActivity(intAllfirsttimer);
				break;

			case R.id.btnAddmember:
				Log.d("NonStop", "Going to Adding Member");
				Intent createMember=new Intent(this,CreateNewMemberActivity.class);
				startActivity(createMember);
				break;

			case R.id.btnAddFirsttimer:
				Log.d("NonStop", "Going to Adding First Timer");
				Intent createfirsttimer=new Intent(this,CreateFirstTimerActivity.class);
				startActivity(createfirsttimer);
				break;

			default:
				break;
		}
	}

	public void onBackPressed() {
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		if (drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START);
		} else {
			AlertDialog dialog = new AlertDialog.Builder(this)
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
			TextView textView = (TextView) dialog.findViewById(android.R.id.message);
			textView.setTextSize(18);
		}
	}

}

