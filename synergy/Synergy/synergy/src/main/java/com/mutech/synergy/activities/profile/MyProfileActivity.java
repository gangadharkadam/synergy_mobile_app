package com.mutech.synergy.activities.profile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
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
//import com.google.android.gms.internal.ln;
import com.google.gson.Gson;
import com.mutech.databasedetails.CreateNewMemberActivity;
import com.mutech.messagebraudcast.MessageBroadcastActivity;
import com.mutech.synergy.App;
import com.mutech.synergy.R;
import com.mutech.synergy.SynergyValues.Commons;
import com.mutech.synergy.SynergyValues.ImageUrl;
import com.mutech.synergy.SynergyValues.Web.GetMemberProfileService;
import com.mutech.synergy.SynergyValues.Web.ProfilePicUploadService;
import com.mutech.synergy.SynergyValues.Web.SaveMemberProfileService;
import com.mutech.synergy.activities.HomeActivity;
import com.mutech.synergy.activities.LoginActivity;
import com.mutech.synergy.activities.LogoutActivity;
import com.mutech.synergy.activities.cellMasters.MasterSelectorScreenActivity;
import com.mutech.synergy.activities.cellMasters.PartnerShipRecord;
import com.mutech.synergy.activities.cellMasters.SearchFunctionActivity;
import com.mutech.synergy.activities.event.EventListActivity;
import com.mutech.synergy.activities.event.MyEventListActivity;
import com.mutech.synergy.activities.meeting.MyMeetingListActivity;
import com.mutech.synergy.activities.task.ToDoTaskActivity;
import com.mutech.synergy.adapters.CustomDrawerAdapter;
import com.mutech.synergy.models.DrawerItem;
import com.mutech.synergy.models.MeetingListRequestModel;
import com.mutech.synergy.models.MemberProfileModel;
import com.mutech.synergy.models.MemberProfileModel.ProfileSubModel;
import com.mutech.synergy.models.ResponseMessageModel;
import com.mutech.synergy.models.StatusCheckModel;
import com.mutech.synergy.utils.GooglePlacesApiAdapter;
import com.mutech.synergy.utils.InputValidation;
import com.mutech.synergy.utils.Methods;
import com.mutech.synergy.utils.NetworkHelper;
import com.mutech.synergy.utils.PreferenceHelper;
import com.squareup.picasso.Picasso;

public class MyProfileActivity extends ActionBarActivity implements OnClickListener{

	private String mAutocompleteText;
	private GooglePlacesApiAdapter mPlacesApiAdapter;
	private EditText txtMembershipNo,txtMemberName,txtMemberSurname,txtMemberPhone1,txtMemberPhone2,txtEmailID1,txtEmailID2,
	txtPassword,txtMemberHomeAddress,txtOfficeAddress,txtShortbio,txtregion,txtregionname,txtzonename,txtchurch_group_name,
	txtsenior_cell_name,txtcell,txtcell_name,txtCoreCompeteance,txtzone,txtchurch_group,
			txtChurch,txtChurch_name,txtsenior_cell;
//	private EditText txtBaptisedWhere, txtYookosID, txtDesignation, txtYearlyIncome;

	private TextView txtMemberDateOfBirth,txtmemInfo,txtempInfo,txtpcf,txtpcf_name;
//	private TextView txtBaptisedWhen,txtDateofJoining;
	private ImageView imgProfilePic,editEmailId1,editEmailId2,editPassword;
	private Button btnSaveMemberInfo;
	private Spinner spnEducationalQualification,spnExperienceYears,spnIndustrySegment,spnEmploymentStatus,spnMemberMartialInfo;
	ArrayAdapter<String> adapterIndSeg,adapterQual,adapterExp,adapterEmpStatus,adapterMartial,adaptershcoolstatus,adapterBornAgain,adapterholyghost,adapterTitle;
//	ArrayAdapter<String> adapterGender, adapterageGroup, adapterbaptismStstus;
//	CheckBox txt_is_eligibale_for_follow_up;
//	AutoCompleteTextView txtLandmarkforHomeAddress,txtLandmarkforOfficeAddress;
	private ArrayList<String> educationQualList,expYearsList,industrySegList,empStatusList,martialInfoList;
	private PreferenceHelper mPreferenceHelper;
	private Gson gson;
	ArrayList<ProfileSubModel> mProfSubModel;
	private DatePickerDialog birthDatePickerDialog,birthDatePickerDialog2,birthDatePickerDialog3;
	private SimpleDateFormat dateFormatter,dateFormatterService,dateFormatter01;
	
	private int SELECT_FILE=0,REQUEST_CAMERA=1;
	private Bitmap thumbnail;
	
	private ArrayList<DrawerItem> mDrawerList;
	private CustomDrawerAdapter mCustomDrawerAdapter;
	private ListView mLvDrawer;
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	String str;
//	private Spinner txttitle, txtGender, txtAgeGroup, txtBaptismStatus, txtholyghost, txtBornAgain, txtSchoolstatus;
	private ArrayList<String> genderList,agegroupList,baptismStatusList,holyghostList,bornAgainList,SchoolstatusList;
//	private ArrayList<String> titleList;
	String Imageurl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_memberprofile);
		initialize();
	}

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
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2E9AFE")));
		getSupportActionBar().setDisplayShowCustomEnabled(true);

		TextView tvTitle=(TextView)getSupportActionBar().getCustomView().findViewById(R.id.title_text);
		tvTitle.setText("My Profile       ");
		

		mPreferenceHelper=new PreferenceHelper(this);
		
	    str=mPreferenceHelper.getString(Commons.USER_ROLE);
		
		gson=new Gson();
		
//		txt_is_eligibale_for_follow_up=(CheckBox) findViewById(R.id.txt_is_eligibale_for_follow_up);
//		txtLandmarkforHomeAddress=(AutoCompleteTextView) findViewById(R.id.txtLandmarkforHomeAddress);
//		txtLandmarkforOfficeAddress=(AutoCompleteTextView) findViewById(R.id.txtLandmarkforOfficeAddress);
//		txtBaptisedWhen=(TextView) findViewById(R.id.txtBaptisedWhen);
//		txtDateofJoining=(TextView) findViewById(R.id.txtDateofJoining);
//		txtYookosID=(EditText) findViewById(R.id.txtYookosID);
//		txtBaptisedWhere=(EditText) findViewById(R.id.txtBaptisedWhere);
		
		txtcell=(EditText)findViewById(R.id.txtcell);
		txtcell_name=(EditText)findViewById(R.id.txtcell_name);
		
		txtsenior_cell=(EditText)findViewById(R.id.txtsenior_cell);
		txtsenior_cell_name=(EditText)findViewById(R.id.txtsenior_cell_name);
		
		txtpcf=(EditText)findViewById(R.id.txtpcf);
		txtpcf_name=(EditText)findViewById(R.id.txtpcf_name);
		
		txtChurch=(EditText) findViewById(R.id.txtChurch);
		txtChurch_name=(EditText)findViewById(R.id.txtChurch_name);
		
		txtchurch_group=(EditText) findViewById(R.id.txtchurch_group);
		txtchurch_group_name=(EditText)findViewById(R.id.txtchurch_group_name);
		
		txtzone=(EditText) findViewById(R.id.txtzone);
		txtzonename=(EditText)findViewById(R.id.txtzonename);
		
		txtregionname=(EditText)findViewById(R.id.txtregionname);
		txtregion=(EditText)findViewById(R.id.txtregion);
	
		txtShortbio=(EditText) findViewById(R.id.txtShortbio);
//		txtDesignation=(EditText) findViewById(R.id.txtDesignation);
		txtMembershipNo=(EditText) findViewById(R.id.txtMembershipNo);
		txtMemberName=(EditText) findViewById(R.id.txtMemberfName);
		txtMemberSurname=(EditText) findViewById(R.id.txtMemberlName);
		
		txtMemberDateOfBirth=(TextView) findViewById(R.id.txtMemberDateOfBirth);
		spnMemberMartialInfo=(Spinner) findViewById(R.id.txtMemberMartialInfo);
		txtMemberPhone1=(EditText) findViewById(R.id.txtMemberPhone1);
		txtMemberPhone2=(EditText) findViewById(R.id.txtMemberPhone2);
		txtEmailID1=(EditText) findViewById(R.id.txtEmailID1);
		txtEmailID2=(EditText) findViewById(R.id.txtEmailID2);
		txtPassword=(EditText) findViewById(R.id.txtPassword);
		txtMemberHomeAddress=(EditText) findViewById(R.id.txtMemberHomeAddress);
		txtOfficeAddress=(EditText) findViewById(R.id.txtOfficeAddress);
		spnEmploymentStatus=(Spinner) findViewById(R.id.txtEmploymentStatus);
		spnIndustrySegment=(Spinner) findViewById(R.id.txtIndustrySegment);
//		txtYearlyIncome=(EditText) findViewById(R.id.txtYearlyIncome);
		spnExperienceYears=(Spinner) findViewById(R.id.txtExperienceYears);
		txtCoreCompeteance=(EditText) findViewById(R.id.txtCoreCompeteance);
		spnEducationalQualification=(Spinner) findViewById(R.id.txtEducationalQualification);
		
//		txtGender=(Spinner) findViewById(R.id.txtGender);
//		txtAgeGroup=(Spinner) findViewById(R.id.txtAgeGroup);
//		txtBaptismStatus=(Spinner) findViewById(R.id.txtBaptismStatus);
//		txtholyghost=(Spinner) findViewById(R.id.txtholyghost);
//		txtBornAgain=(Spinner) findViewById(R.id.txtBornAgain);
//		txtSchoolstatus=(Spinner) findViewById(R.id.txtSchoolstatus);
//		txttitle=(Spinner) findViewById(R.id.txttitle);
		
	
		txtmemInfo=(TextView) findViewById(R.id.memInfo);
	//	txtperInfo=(TextView) findViewById(R.id.perInfo);
		txtempInfo=(TextView) findViewById(R.id.empInfo);
		
		Calendar newCalendar = Calendar.getInstance();
		dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
		dateFormatterService=new SimpleDateFormat("yyyy-MM-dd",Locale.US);
		
		dateFormatter01=new SimpleDateFormat("yyyy-MM-dd");
		
		birthDatePickerDialog = new DatePickerDialog(this, new OnDateSetListener() {

			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
				txtMemberDateOfBirth.setText(dateFormatter.format(newDate.getTime()));
			}

		},newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
		
		birthDatePickerDialog2 = new DatePickerDialog(this, new OnDateSetListener() {

			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
//				txtBaptisedWhen.setText(dateFormatter.format(newDate.getTime()));
			}

		},newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
		
		birthDatePickerDialog3 = new DatePickerDialog(this, new OnDateSetListener() {

			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
//				txtDateofJoining.setText(dateFormatter.format(newDate.getTime()));
			}

		},newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
		
//		mPlacesApiAdapter=new GooglePlacesApiAdapter(this,R.layout.list_item_autocomplete_for_google_places);
//		txtLandmarkforHomeAddress.setAdapter(mPlacesApiAdapter);
//
//		txtLandmarkforHomeAddress.addTextChangedListener(new TextWatcher() {
//
//			@Override
//			public void afterTextChanged(Editable arg0) {
//			}
//
//			@Override
//			public void beforeTextChanged(CharSequence arg0, int arg1,
//					int arg2, int arg3) {
//			}
//
//			@Override
//			public void onTextChanged(CharSequence s, int arg1, int arg2,
//					int arg3) {
//				mAutocompleteText = s.toString().toLowerCase(Locale.getDefault());
//				mPlacesApiAdapter.getFilter();
//			}
//		});
//
//		txtLandmarkforOfficeAddress.addTextChangedListener(new TextWatcher() {
//
//			@Override
//			public void afterTextChanged(Editable arg0) {
//			}
//
//			@Override
//			public void beforeTextChanged(CharSequence arg0, int arg1,
//					int arg2, int arg3) {
//			}
//
//			@Override
//			public void onTextChanged(CharSequence s, int arg1, int arg2,
//					int arg3) {
//				mAutocompleteText = s.toString().toLowerCase(Locale.getDefault());
//				mPlacesApiAdapter.getFilter();
//			}
//		});
		
		
		
		txtcell.setEnabled(false);
		txtcell_name.setEnabled(false);
		
		txtsenior_cell.setEnabled(false);
		txtsenior_cell_name.setEnabled(false);
		
		txtpcf.setEnabled(false);
		txtpcf_name.setEnabled(false);
		
		txtzone.setEnabled(false);
		txtzonename.setEnabled(false);
		
		txtregion.setEnabled(false);
		txtregionname.setEnabled(false);
		
		
		txtchurch_group.setEnabled(false);
		txtchurch_group_name.setEnabled(false);
		
		txtChurch.setEnabled(false);
		txtChurch_name.setEnabled(false);
		
//		txtDesignation.setEnabled(false);
		
		
	  	
	/*	txtmemInfo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method st
				LinearLayout ln1 = (LinearLayout) findViewById(R.id.linear1);
				if (ln1.getVisibility() == View.GONE) {
					ln1.setVisibility(View.VISIBLE);
					txtmemInfo.setText("- Membership Data");
					}else{
					ln1.setVisibility(View.GONE);	
					txtmemInfo.setText("+ Membership Data");
					}
			}
		});*/
		
      /* txtperInfo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LinearLayout ln1 = (LinearLayout) findViewById(R.id.linear2);
				if (ln1.getVisibility() == View.GONE) {
				ln1.setVisibility(View.VISIBLE);
				txtperInfo.setText("- Personal Information");
				}else{
				ln1.setVisibility(View.GONE);	
				txtperInfo.setText("+ Personal Information");
				}
			
			}
		});*/
		
//		titleList=new ArrayList<String>();
		genderList=new ArrayList<String>();
		agegroupList=new ArrayList<String>();
		baptismStatusList=new ArrayList<String>();
		holyghostList=new ArrayList<String>();
		bornAgainList=new ArrayList<String>();
		SchoolstatusList=new ArrayList<String>();
		
		educationQualList=new ArrayList<String>();
		expYearsList=new ArrayList<String>();
		industrySegList=new ArrayList<String>();
		empStatusList=new ArrayList<String>();
		martialInfoList=new ArrayList<String>();
		
		
		
//		txtDateofJoining.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				birthDatePickerDialog3.show();
//			}
//		});
		
		
//		txtBaptisedWhen.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				birthDatePickerDialog2.show();
//
//			}
//		});
		
		
		txtMemberDateOfBirth.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				birthDatePickerDialog.show();
				
			}
		});
		
		
       txtempInfo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LinearLayout ln1 = (LinearLayout) findViewById(R.id.linear3);
			
				if (ln1.getVisibility() == View.GONE) {
					ln1.setVisibility(View.VISIBLE);
					txtempInfo.setText("- Employment Details");
					}else{
					ln1.setVisibility(View.GONE);	
					txtempInfo.setText("+ Employment Details");
					}
			}
		});
		
		

		imgProfilePic=(ImageView) findViewById(R.id.imgProfilePic);
		btnSaveMemberInfo=(Button) findViewById(R.id.btnSaveMemberInfo);
		editEmailId1=(ImageView) findViewById(R.id.editEmailId1);
		editEmailId2=(ImageView) findViewById(R.id.editEmailId2);
		editPassword=(ImageView) findViewById(R.id.editPassword);
		editEmailId1.setOnClickListener(this);
		editEmailId2.setOnClickListener(this);
		editPassword.setOnClickListener(this);
		txtMemberDateOfBirth.setOnClickListener(this);

		btnSaveMemberInfo.setOnClickListener(this);
		imgProfilePic.setOnClickListener(this);

		mProfSubModel=new ArrayList<ProfileSubModel>();

		educationQualList=new ArrayList<String>();
		expYearsList=new ArrayList<String>();
		industrySegList=new ArrayList<String>();
		empStatusList=new ArrayList<String>();
		martialInfoList=new ArrayList<String>();

		setSpinners();
		
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
				R.drawable.actiontop, 
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
		
		
		
		birthDatePickerDialog = new DatePickerDialog(this, new OnDateSetListener() {

			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
				txtMemberDateOfBirth.setText(dateFormatter.format(newDate.getTime()));
			}

		},newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
		
		birthDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis()-86400000);
		
		if(NetworkHelper.isOnline(this)){
			Methods.showProgressDialog(this);
			getProfileInfo();
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
			item1.setItemName("Partnership \n Record");
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
		item03.setItemName("Partnership \n Record");
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
		mDrawerList.add(item03);	
		
		//mDrawerList.add(item06);
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
			
			Intent intLogout=new Intent(getApplicationContext(),LogoutActivity.class);
			intLogout.putExtra("classname", "MyProfileActivity");
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
//			Intent intForm1=new Intent(this,MyProfileActivity.class);
//			startActivity(intForm1);

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
			Intent intSearchMembers=new Intent(this,SearchFunctionActivity.class);
			intSearchMembers.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(intSearchMembers);
			finish();
			break;
			
		case 9://logout


			Intent intLogout=new Intent(getApplicationContext(),LogoutActivity.class);
			intLogout.putExtra("classname", "MyProfileActivity");
			startActivity(intLogout);
			finish();
			break;


		default:
			break;
		}

	}
		mDrawerLayout.closeDrawer(mLvDrawer);

	}

	

	private class DrawerItemClickListener implements
	ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);

		}
	}
	
	public static final boolean isValidPhoneNumber(CharSequence target) {
	    if (target == null || TextUtils.isEmpty(target)) {
	        return false;
	    } else {
	        return android.util.Patterns.PHONE.matcher(target).matches();
	    }
	}

	public boolean emailValidator(String email) 
	{
	    Pattern pattern;
	    Matcher matcher;
	    final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	    pattern = Pattern.compile(EMAIL_PATTERN);
	    matcher = pattern.matcher(email);
	    return matcher.matches();
	}
	
	private void setSpinners() {
    	
				
		
//		titleList.add("Bro");
//		titleList.add("Sis");
//		titleList.add("Dcn");
//		titleList.add("Dcns");
//		titleList.add("Pastor");
		
		
		genderList.add("Male");
		genderList.add("Female");
		
		agegroupList.add("Child (0 to 12)");
		agegroupList.add("Teen (13 to 19)");
		agegroupList.add("Youngster (20 to 25)");
		agegroupList.add("Young Adult (26 to 35)");
		agegroupList.add("Mid Age (35 to 45)");
		agegroupList.add("Arrived ( 45 to 55)");
		agegroupList.add("Seniors (56 to 65)");
		agegroupList.add("Advanced (65 and Above)");
		
		baptismStatusList.add("Yes");
		baptismStatusList.add("No");
		baptismStatusList.add("Attended Classes but yet to be Baptised");
		
		holyghostList.add("Yes");
		holyghostList.add("No");
		
		bornAgainList.add("Yes");
		bornAgainList.add("No");
		
		SchoolstatusList.add("Nil");
		SchoolstatusList.add("Completed Class 1");
		SchoolstatusList.add("Completed Class 1&2");
		SchoolstatusList.add("Completed Class 1, 2 & 3");
		SchoolstatusList.add("Completed Class 1, 2 , 3 & 4");
		SchoolstatusList.add("Completed Class 1, 2 , 3 , 4 & 5");
		SchoolstatusList.add("Completed Class 1, 2 , 3 , 4 , 5 & 6");
		SchoolstatusList.add("Completed All Classes and Passed Exam");
		
		martialInfoList.add("");
		martialInfoList.add("Divorced");
		martialInfoList.add("Married");
		martialInfoList.add("Single");
		martialInfoList.add("Widowed");
		
//		adapterTitle = new ArrayAdapter<String>(MyProfileActivity.this, android.R.layout.simple_spinner_item, titleList);
//		adapterTitle.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		txttitle.setAdapter(adapterTitle);
		
//		adapterGender = new ArrayAdapter<String>(MyProfileActivity.this, android.R.layout.simple_spinner_item, genderList);
//		adapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		txtGender.setAdapter(adapterGender);
		
//		adapterageGroup = new ArrayAdapter<String>(MyProfileActivity.this, android.R.layout.simple_spinner_item, agegroupList);
//		adapterageGroup.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		txtAgeGroup.setAdapter(adapterageGroup);
		
//		adapterbaptismStstus = new ArrayAdapter<String>(MyProfileActivity.this, android.R.layout.simple_spinner_item, baptismStatusList);
//		adapterbaptismStstus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		txtBaptismStatus.setAdapter(adapterbaptismStstus);
		
//		adapterholyghost = new ArrayAdapter<String>(MyProfileActivity.this, android.R.layout.simple_spinner_item, holyghostList);
//		adapterholyghost.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		txtholyghost.setAdapter(adapterholyghost);
		
//		adapterBornAgain = new ArrayAdapter<String>(MyProfileActivity.this, android.R.layout.simple_spinner_item, bornAgainList);
//		adapterBornAgain.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		txtBornAgain.setAdapter(adapterBornAgain);
		
	
//		adaptershcoolstatus = new ArrayAdapter<String>(MyProfileActivity.this, android.R.layout.simple_spinner_item, SchoolstatusList);
//		adaptershcoolstatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		txtSchoolstatus.setAdapter(adaptershcoolstatus);
		
		
		adapterMartial = new ArrayAdapter<String>(MyProfileActivity.this, android.R.layout.simple_spinner_item, martialInfoList);

		adapterMartial.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnMemberMartialInfo.setAdapter(adapterMartial);

		educationQualList.add("");
		educationQualList.add("City and Guilds");
		educationQualList.add("Doctorate");
		educationQualList.add("First Degree (BSC, BA)");
		educationQualList.add("HND");
		educationQualList.add("OND");
		educationQualList.add("SSCE School Certificate");
		educationQualList.add("Second Degree (MSC, MBA)");
		
		
		adapterQual = new ArrayAdapter<String>(MyProfileActivity.this, android.R.layout.simple_spinner_item, educationQualList);

		adapterQual.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnEducationalQualification.setAdapter(adapterQual);

		expYearsList.add("");
		//expYearsList.add("");
		expYearsList.add("1 - 3 Years");
		expYearsList.add("3 - 6 Years");
		expYearsList.add("6 - 10 Years");
		expYearsList.add("10+ Years");
		
		
		adapterExp = new ArrayAdapter<String>(MyProfileActivity.this, android.R.layout.simple_spinner_item, expYearsList);

		adapterExp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnExperienceYears.setAdapter(adapterExp);
		
		spnExperienceYears.setSelection(0);
		
		industrySegList.add("");
		industrySegList.add("Aviation");
		industrySegList.add("Consulting");
		industrySegList.add("Craftsmanship");
		industrySegList.add("Construction");
		industrySegList.add("Education");
		industrySegList.add("Entertainment");
		industrySegList.add("Engineering");
		industrySegList.add("Finance");
		industrySegList.add("General Merchandise/Trading");
		industrySegList.add("Hospitality");
		industrySegList.add("Insurance");
		industrySegList.add("Information Technology (IT)");
		industrySegList.add("Mining");
		industrySegList.add("Manufacturing");
		industrySegList.add("Medical");
		industrySegList.add("Media");
		industrySegList.add("Oil and Gas");
		industrySegList.add("Security");
		industrySegList.add("Telecoms");
		industrySegList.add("Transportation");
		industrySegList.add("Others");

		adapterIndSeg = new ArrayAdapter<String>(MyProfileActivity.this, android.R.layout.simple_spinner_item, industrySegList);

		adapterIndSeg.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnIndustrySegment.setAdapter(adapterIndSeg);

		
		empStatusList.add("");
		empStatusList.add("Business Man");
		empStatusList.add("Government Worker");
		empStatusList.add("Unemployed");
		empStatusList.add("Professional");
		empStatusList.add("Petty Trade");
		empStatusList.add("Student");		
		empStatusList.add("Trader");
		empStatusList.add("Others");
		


		adapterEmpStatus = new ArrayAdapter<String>(MyProfileActivity.this, android.R.layout.simple_spinner_item, empStatusList);

		adapterEmpStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnEmploymentStatus.setAdapter(adapterEmpStatus);
		

	}

	private void getProfileInfo() {
		StringRequest reqGetProfile=new StringRequest(Method.POST,GetMemberProfileService.SERVICE_URL,new Listener<String>() {

			@Override
			public void onResponse(String response) {
				Methods.closeProgressDialog();
				Log.e("responce droid","get reqGetProfile ---------------"+ response);
			
				MemberProfileModel mProfModel=gson.fromJson(response, MemberProfileModel.class);
				//Object meetingmsg=mMeetingModel.getMessage();

				if(null !=mProfModel.getMessage() && mProfModel.getMessage().size() >0){

					mProfSubModel=mProfModel.getMessage();

					Log.d("NonStop", "mProfSumModel Size: " + mProfSubModel.size());
					for(int i=0;i<mProfSubModel.size();i++){
						txtMembershipNo.setText(mProfSubModel.get(i).getName());
						
						txtMemberName.setText(mProfSubModel.get(i).getMember_name());
						//if(null !=mProfSubModel.get(i).getDate_of_birth()){
							try {
								Date dob=new Date();
								
								if(null !=mProfSubModel.get(i).getDate_of_birth()){
								dob=dateFormatterService.parse(mProfSubModel.get(i).getDate_of_birth());
								txtMemberDateOfBirth.setText(dateFormatter.format(dob));
								}
								
								if(null !=mProfSubModel.get(i).getDate_of_join()){
								dob=dateFormatterService.parse(mProfSubModel.get(i).getDate_of_join());
//								txtDateofJoining.setText(dateFormatter.format(dob));
								}
								
								if(null !=mProfSubModel.get(i).getBaptism_when()){
								dob=dateFormatterService.parse(mProfSubModel.get(i).getBaptism_when());
//								txtBaptisedWhen.setText(dateFormatter.format(dob));
								}
								
							} catch (ParseException e) {
								e.printStackTrace();
							}
						//}
						
//						if(null !=mProfSubModel.get(i).getBaptism_where())
//							txtBaptisedWhere.setText(mProfSubModel.get(i).getBaptism_where());
						
						
						if(null !=mProfSubModel.get(i).getRegion())
							txtregion.setText(mProfSubModel.get(i).getRegion());
						
						if(null !=mProfSubModel.get(i).getRegion_name())
							txtregionname.setText(mProfSubModel.get(i).getRegion());
						
						if(null !=mProfSubModel.get(i).getZone())
							txtzone.setText(mProfSubModel.get(i).getZone());
						
						if(null !=mProfSubModel.get(i).getZone_name())
							txtzonename.setText(mProfSubModel.get(i).getZone_name());
						
						if(null !=mProfSubModel.get(i).getChurch_group())
						    txtchurch_group.setText(mProfSubModel.get(i).getChurch_group());
						
						if(null !=mProfSubModel.get(i).getGroup_church_name())
							txtchurch_group_name.setText(mProfSubModel.get(i).getGroup_church_name());
						
						if(null !=mProfSubModel.get(i).getChurch())
						   txtChurch.setText(mProfSubModel.get(i).getChurch());
						
						if(null !=mProfSubModel.get(i).getChurch_name())
							txtChurch_name.setText(mProfSubModel.get(i).getChurch_name());
						
						if(null !=mProfSubModel.get(i).getPcf())
							txtpcf.setText(mProfSubModel.get(i).getPcf());
						
						if(null !=mProfSubModel.get(i).getPcf_name())
							txtpcf_name.setText(mProfSubModel.get(i).getPcf_name());
						
						
						if(null !=mProfSubModel.get(i).getSenior_cell())
							txtsenior_cell.setText(mProfSubModel.get(i).getSenior_cell());
						
						if(null !=mProfSubModel.get(i).getSenior_cell_name())
							txtsenior_cell_name.setText(mProfSubModel.get(i).getSenior_cell_name());
						
					
						if(null !=mProfSubModel.get(i).getCell())
							txtcell.setText(mProfSubModel.get(i).getCell());
						
						if(null !=mProfSubModel.get(i).getCell_name())
							txtcell_name.setText(mProfSubModel.get(i).getCell_name());
					
						if(null !=mProfSubModel.get(i).getShort_bio())
							txtShortbio.setText(mProfSubModel.get(i).getShort_bio());
					
						
						//getChurch

						txtMemberPhone1.setText(mProfSubModel.get(i).getPhone_1());
						txtMemberPhone2.setText(mProfSubModel.get(i).getPhone_2());
//						txtDesignation.setText(mProfSubModel.get(i).getMember_designation());

						//imgProfilePic.setImageURI(Uri.parse("http://loveworldsynergy.org"+mProfSubModel.get(i).getImage()));
						/*if(null !=mProfSubModel.get(i).getImage() && mProfSubModel.get(i).getImage().trim().length() > 0){
							String Imageurl= mProfSubModel.get(i).getImage();
						    Log.e("Image Url",ImageUrl.imageUrl+Imageurl);
							Picasso.with(MyProfileActivity.this).load("http://verve.indictranstech.com/files/image1.jpg").into(imgProfilePic);
						
						}*/

						if(i<1) {
							Imageurl= mProfSubModel.get(i).getImage();
						}

						if(Imageurl != null) {
							Picasso.with(MyProfileActivity.this).load(ImageUrl.imageUrl+Imageurl).into(imgProfilePic);
						}
						Log.e("Image Url", ImageUrl.imageUrl + Imageurl);
						Log.d("NonStop", "Image URL: " + ImageUrl.imageUrl + Imageurl);


						txtEmailID1.setText(mProfSubModel.get(i).getEmail_id());
						txtEmailID2.setText(mProfSubModel.get(i).getEmail_id2());
						txtPassword.setText(mPreferenceHelper.getString(Commons.USER_PASSWORD));
						txtMemberHomeAddress.setText(mProfSubModel.get(i).getAddress());
						txtOfficeAddress.setText(mProfSubModel.get(i).getOffice_address());

//						txtYearlyIncome.setText(mProfSubModel.get(i).getYearly_income());
						txtCoreCompeteance.setText(mProfSubModel.get(i).getCore_competeance());
						
						if(null !=mProfSubModel.get(i).getLast_name())
						txtMemberSurname.setText(mProfSubModel.get(i).getLast_name());
						
//						if(null !=mProfSubModel.get(i).getYokoo_id())
//						txtYookosID.setText(mProfSubModel.get(i).getYokoo_id());
						
						
//						txtLandmarkforHomeAddress.setText(mProfSubModel.get(i).getHome_address());
//
//						if(null !=mProfSubModel.get(i).getOffice_landmark())
//						txtLandmarkforOfficeAddress.setText(mProfSubModel.get(i).getOffice_landmark());
						
						
						if(null !=mProfSubModel.get(i).getMarital_info() && mProfSubModel.get(i).getMarital_info().trim().length() >0){
							spnMemberMartialInfo.setSelection(adapterMartial.getPosition(mProfSubModel.get(i).getMarital_info()));
						}

						if(null !=mProfSubModel.get(i).getEmployment_status() && mProfSubModel.get(i).getEmployment_status().trim().length() >0)
							spnEmploymentStatus.setSelection(adapterEmpStatus.getPosition(mProfSubModel.get(i).getEmployment_status()));

						if(null !=mProfSubModel.get(i).getIndustry_segment() && mProfSubModel.get(i).getIndustry_segment().trim().length() >0)
							spnIndustrySegment.setSelection(adapterIndSeg.getPosition(mProfSubModel.get(i).getIndustry_segment()));

						if(null !=mProfSubModel.get(i).getExperience_years() && mProfSubModel.get(i).getExperience_years().trim().length() >0)
							spnExperienceYears.setSelection(adapterExp.getPosition(mProfSubModel.get(i).getExperience_years()));

						if(null !=mProfSubModel.get(i).getEducational_qualification() && mProfSubModel.get(i).getEducational_qualification().trim().length() >0)
							spnEducationalQualification.setSelection(adapterQual.getPosition(mProfSubModel.get(i).getEducational_qualification()));
						
//						if(null !=mProfSubModel.get(i).getSex() && mProfSubModel.get(i).getSex().trim().length() >0)
//							txtGender.setSelection(adapterGender.getPosition(mProfSubModel.get(i).getSex()));
						
//						if(null !=mProfSubModel.get(i).getAge_group() && mProfSubModel.get(i).getAge_group().trim().length() >0)
//							txtAgeGroup.setSelection(adapterageGroup.getPosition(mProfSubModel.get(i).getAge_group()));
						
//						if(null !=mProfSubModel.get(i).getBaptisum_status() && mProfSubModel.get(i).getBaptisum_status().trim().length() >0)
//							txtBaptismStatus.setSelection(adapterbaptismStstus.getPosition(mProfSubModel.get(i).getBaptisum_status()));
						
//						if(null !=mProfSubModel.get(i).getFilled_with_holy_ghost() && mProfSubModel.get(i).getFilled_with_holy_ghost().trim().length() >0)
//							txtholyghost.setSelection(adapterholyghost.getPosition(mProfSubModel.get(i).getFilled_with_holy_ghost()));
						
//						if(null !=mProfSubModel.get(i).getIs_new_born() && mProfSubModel.get(i).getIs_new_born().trim().length() >0)
//							txtBornAgain.setSelection(adapterBornAgain.getPosition(mProfSubModel.get(i).getIs_new_born()));
						
//						if(null !=mProfSubModel.get(i).getSchool_status() && mProfSubModel.get(i).getSchool_status().trim().length() >0)
//							txtSchoolstatus.setSelection(adaptershcoolstatus.getPosition(mProfSubModel.get(i).getSchool_status()));
						
//						if(null !=mProfSubModel.get(i).getTitle() && mProfSubModel.get(i).getTitle().trim().length() >0)
//							txttitle.setSelection(adapterTitle.getPosition(mProfSubModel.get(i).getTitle()));
						
						
//					if(mProfSubModel.get(i).getIs_eligibale_for_follow_up().equals("1"))
//						txt_is_eligibale_for_follow_up.setChecked(true);
//
					}

					
					
					
				}

				//	}



			}
		},new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Methods.closeProgressDialog();
				Log.d("droid","get reqsaveMeeting error---------------"+ error.getCause());

				if(error.networkResponse.statusCode==403){
					Methods.longToast("No access to update Profile", MyProfileActivity.this);
				}
				else
					Methods.longToast("Some Error Occured,please try again later", MyProfileActivity.this);
			}
		}){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError{
				Map<String, String> params = new HashMap<String, String>();

				MeetingListRequestModel model=new MeetingListRequestModel();
				model.setUsername(mPreferenceHelper.getString(Commons.USER_EMAILID));
				model.setUserpass(mPreferenceHelper.getString(Commons.USER_PASSWORD));

				String dataString=gson.toJson(model, MeetingListRequestModel.class);

				Log.e("Request droid", dataString);
				params.put(GetMemberProfileService.DATA, dataString);
				return params;
			}
		};

		App.getInstance().addToRequestQueue(reqGetProfile, "reqGetProfile");
		reqGetProfile.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnSaveMemberInfo:
			if(validateFields()){
				if(NetworkHelper.isOnline(this)){
					if(InputValidation.isPhoneNumber(txtMemberPhone1, false) &&
							InputValidation.isPhoneNumber(txtMemberPhone2, false) &&
							InputValidation.hasText(txtMemberName)) {
						String email = txtEmailID2.getText().toString().trim();
						Methods.showProgressDialog(this);
						saveMemberInfo();
					} else {
						new AlertDialog.Builder(MyProfileActivity.this)
								.setCancelable(false)
								.setTitle("Invalid Input")
								.setMessage("Please enter valid value in the field marked red")
								.setPositiveButton("OK", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialogInterface, int i) {

									}
								})
								.show();
					}
				}
				else
					Methods.longToast("Please connect to Internet", this);
			}

			break;
		case R.id.editEmailId1:
			txtEmailID1.setEnabled(true);
			txtEmailID1.requestFocus();
			break;
		case R.id.editEmailId2:
			txtEmailID2.setEnabled(true);
			txtEmailID2.requestFocus();
			break;
		case R.id.editPassword:
			txtPassword.setEnabled(true);
			txtPassword.requestFocus();

			break;
		case R.id.txtMemberDateOfBirth:
			birthDatePickerDialog.show();
			break;

		case R.id.imgProfilePic:
			openDialog();
			break;

		default:
			break;
		}
	}

	private void saveMemberInfo() {
		StringRequest reqSendProfile=new StringRequest(Method.POST,SaveMemberProfileService.SERVICE_URL,new Listener<String>() {

			@Override
			public void onResponse(String response) {
				Methods.closeProgressDialog();
				Log.e("droid","get reqSendProfile ---------------"+ response);

				//				MemberProfileModel mProfModel=gson.fromJson(response, MemberProfileModel.class);
				//				//Object meetingmsg=mMeetingModel.getMessage();
				//				if(null != mProfModel.getStatus() && mProfModel.getStatus().trim().length() >0){
				//
				//					if(mProfModel.getStatus()=="401"){
				//						Methods.longToast("User name or Password is incorrect", MyProfileActivity.this);
				//					}else{
				//						ResponseMessageModel model=gson.fromJson(response, ResponseMessageModel.class);
				//						Methods.longToast(model.getMessage(), MyProfileActivity.this);
				//					}
				//				}else{
				//					if(null !=mProfModel.getMessage() && mProfModel.getMessage().size() >0){
				//						
				//						
				//					}else{
				//					}
				//				}

				StatusCheckModel statusCheckModel=gson.fromJson(response, StatusCheckModel.class);
				if(null != statusCheckModel.getStatus() && statusCheckModel.getStatus().trim().length() >0){
					ResponseMessageModel respModel=gson.fromJson(response, ResponseMessageModel.class);
					if(respModel.getStatus()=="401"){
						Methods.longToast("User name or Password is incorrect", MyProfileActivity.this);
					}else{
						Methods.longToast(respModel.getMessage(), MyProfileActivity.this);
					}
				}else{
					ResponseMessageModel respModel=gson.fromJson(response, ResponseMessageModel.class);
					//Methods.longToast(respModel.getMessage(), MyProfileActivity.this);

					/*if(null !=txtPassword.getText().toString() && txtPassword.getText().toString().trim().length() > 0){
						mPreferenceHelper.addString(Commons.USER_PASSWORD, txtPassword.getText().toString());
					}*/
				}

				if(null !=thumbnail){
					
					if(null !=txtPassword.getText().toString() && txtPassword.getText().toString().trim().length() > 0){
						mPreferenceHelper.addString(Commons.USER_PASSWORD, txtPassword.getText().toString());
					}
					
					//Methods.showProgressDialog(MyProfileActivity.this);
					//uploadProfPicService();
				}else
				{
					
					String Oldpass=mPreferenceHelper.getString(Commons.USER_PASSWORD);
					String newpass=txtPassword.getText().toString();
					
					if(Oldpass.equals(newpass)){
						Methods.longToast("Profile Updated Successfully", MyProfileActivity.this);
						if(null !=txtPassword.getText().toString() && txtPassword.getText().toString().trim().length() > 0){
							mPreferenceHelper.addString(Commons.USER_PASSWORD, txtPassword.getText().toString());
						}
					}else{
						
						Methods.longToast("Password Updated Successfully.Please login with new password.", MyProfileActivity.this);
					
						mPreferenceHelper.addBoolean(Commons.ISUSER_LOGGEDIN, false);
						mPreferenceHelper.addString(Commons.USER_EMAILID, null);
						mPreferenceHelper.addString(Commons.USER_PASSWORD, null);

						startActivity(new Intent(MyProfileActivity.this,LoginActivity.class));
						finish();
					}
				}


			}
		},new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Methods.closeProgressDialog();
				Log.d("droid","get reqsaveMeeting error---------------"+ error.getCause());

				if(error.networkResponse.statusCode==403){
					Methods.longToast("No access to update Profile", MyProfileActivity.this);
				}
				else
					Methods.longToast("Some Error Occured,please try again later", MyProfileActivity.this);
			}
		}){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError{
				Map<String, String> params = new HashMap<String, String>();
				MemberProfileModel mParentModel=new MemberProfileModel();

				ProfileSubModel mSendProfModel=mParentModel.new ProfileSubModel();

				mSendProfModel.setUsername(mPreferenceHelper.getString(Commons.USER_EMAILID));
				mSendProfModel.setUserpass(mPreferenceHelper.getString(Commons.USER_PASSWORD));

				mSendProfModel.setName(txtMembershipNo.getText().toString());
				mSendProfModel.setMember_name(txtMemberName.getText().toString());

				try {
					
					Date dob=new Date();
					
					if(!txtMemberDateOfBirth.getText().toString().equals("")){
					dob = dateFormatter.parse(txtMemberDateOfBirth.getText().toString());
					mSendProfModel.setDate_of_birth(dateFormatterService.format(dob));
				
					}else{
						mSendProfModel.setDate_of_birth("");
					}
					
//					if(!txtBaptisedWhen.getText().toString().equals("")){
//						dob = dateFormatter.parse(txtBaptisedWhen.getText().toString());
//						mSendProfModel.setBaptism_when(dateFormatterService.format(dob));
//					}else{
//						mSendProfModel.setBaptism_when("");
//					}
					
//					if(!txtDateofJoining.getText().toString().equals("")){
//						dob=dateFormatter.parse(txtDateofJoining.getText().toString());
//						mSendProfModel.setDate_of_join(dateFormatterService.format(dob));
//					}else{
//						mSendProfModel.setDate_of_join("");
//					}
					
				} catch (ParseException e) {
					e.printStackTrace();
					mSendProfModel.setDate_of_birth("");
				}

				
				
				mSendProfModel.setMarital_info(spnMemberMartialInfo.getSelectedItem().toString());
				
				mSendProfModel.setPhone_1(txtMemberPhone1.getText().toString());
				
				mSendProfModel.setPhone_2(txtMemberPhone2.getText().toString());

				mSendProfModel.setImage(mProfSubModel.get(0).getImage());
				
				mSendProfModel.setEmail_id(txtEmailID1.getText().toString());
				
				mSendProfModel.setEmail_id2(txtEmailID2.getText().toString());
				
				mSendProfModel.setPassword(txtPassword.getText().toString());
				
//				mSendProfModel.setHome_address(txtLandmarkforHomeAddress.getText().toString());
				
				mSendProfModel.setAddress(txtMemberHomeAddress.getText().toString());
				
				mSendProfModel.setOffice_address(txtOfficeAddress.getText().toString());

				mSendProfModel.setEmployment_status(spnEmploymentStatus.getSelectedItem().toString());
				
				mSendProfModel.setIndustry_segment(spnIndustrySegment.getSelectedItem().toString());
				
//				mSendProfModel.setYearly_income(txtYearlyIncome.getText().toString());
				
				mSendProfModel.setShort_bio(txtShortbio.getText().toString());			
				
				mSendProfModel.setLast_name(txtMemberSurname.getText().toString());
				
				
				/*int i=spnExperienceYears.getSelectedItemPosition();experience_years
				if(i>0)*/
//				mSendProfModel.setYokoo_id(txtYookosID.getText().toString());
				
				mSendProfModel.setExperience_years(spnExperienceYears.getSelectedItem().toString());
				
				
				mSendProfModel.setEducational_qualification(spnEducationalQualification.getSelectedItem().toString());

				mSendProfModel.setCore_competeance(txtCoreCompeteance.getText().toString());

				//mSendProfModel.setAddress(mProfSubModel.get(0).getAddress());
				mSendProfModel.setChurch_group(mProfSubModel.get(0).getChurch_group());
				mSendProfModel.setChurch(mProfSubModel.get(0).getChurch());
				mSendProfModel.setCell(mProfSubModel.get(0).getCell());
				mSendProfModel.setZone(mProfSubModel.get(0).getZone());
				mSendProfModel.setPcf(mProfSubModel.get(0).getPcf());
				mSendProfModel.setRegion(mProfSubModel.get(0).getRegion());

				mSendProfModel.setOffice_state(mProfSubModel.get(0).getOffice_state());
				mSendProfModel.setLon(mProfSubModel.get(0).getLon());
				mSendProfModel.setMember_type(mProfSubModel.get(0).getMember_type());
				
//				mSendProfModel.setSex(txtGender.getSelectedItem().toString());
				
//				mSendProfModel.setBaptism_where(txtBaptisedWhere.getText().toString());
				
				mSendProfModel.setWhen(mProfSubModel.get(0).getWhen());
				
//				mSendProfModel.setOffice_landmark(txtLandmarkforOfficeAddress.getText().toString());
				
				
//				mSendProfModel.setAge_group(txtAgeGroup.getSelectedItem().toString());
				
				mSendProfModel.setIs_eligibale_for_follow_up(mProfSubModel.get(0).getIs_eligibale_for_follow_up());
			
				mSendProfModel.setIs_new_born(mProfSubModel.get(0).getIs_new_born());
				
				
				mSendProfModel.setIs_new_convert(mProfSubModel.get(0).getIs_new_convert());
				
				mSendProfModel.setSenior_cell(mProfSubModel.get(0).getSenior_cell());
				mSendProfModel.setLon1(mProfSubModel.get(0).getLon1());
				mSendProfModel.setFtv_id_no(mProfSubModel.get(0).getFtv_id_no());
				mSendProfModel.setOffice_city(mProfSubModel.get(0).getOffice_city());
				mSendProfModel.setLat(mProfSubModel.get(0).getLat());
				
				mSendProfModel.setUser_id(mProfSubModel.get(0).getUser_id());	
				mSendProfModel.setLat1(mProfSubModel.get(0).getLat1());
				
//				mSendProfModel.setTitle(txttitle.getSelectedItem().toString());
				
//				mSendProfModel.setSchool_status(txtSchoolstatus.getSelectedItem().toString());
				
//				mSendProfModel.setBaptisum_status(txtBaptismStatus.getSelectedItem().toString());
				
				mSendProfModel.setMember_designation(mProfSubModel.get(0).getMember_designation());
				
//				mSendProfModel.setFilled_with_holy_ghost(txtholyghost.getSelectedItem().toString());
				
				mSendProfModel.setWhere(mProfSubModel.get(0).getWhere());
				
				
				
				
				String dataString=gson.toJson(mSendProfModel, ProfileSubModel.class);

				Log.e("droid Request", dataString);
				Log.d("NonStop", "Request: " + dataString);
				params.put(GetMemberProfileService.DATA, dataString);
				return params;
			}
		};

		App.getInstance().addToRequestQueue(reqSendProfile, "reqSendProfile");
		reqSendProfile.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
	}

	protected void uploadProfPicService() {
		StringRequest reqSendProfilePic=new StringRequest(Method.POST,ProfilePicUploadService.SERVICE_URL,new Listener<String>() {

			@Override
			public void onResponse(String response) {
				Methods.closeProgressDialog();
				Log.d("droid","get reqSendProfilePic ---------------"+ response);

				StatusCheckModel statusCheckModel=gson.fromJson(response, StatusCheckModel.class);
				if(null != statusCheckModel.getStatus() && statusCheckModel.getStatus().trim().length() >0){
					ResponseMessageModel respModel=gson.fromJson(response, ResponseMessageModel.class);
					if(respModel.getStatus()=="401"){
						Methods.longToast("User name or Password is incorrect", MyProfileActivity.this);
					}else{
						Methods.longToast(respModel.getMessage(), MyProfileActivity.this);
					}
				}else{
					Methods.longToast("Profile Updated Succesfully", MyProfileActivity.this);
				}
			//	finish();

			}
		},new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Methods.closeProgressDialog();
				Log.d("droid","get reqsaveMeeting error---------------"+ error.getCause());

				if(error.networkResponse.statusCode==403){
					Methods.longToast("No access to update Profile", MyProfileActivity.this);
				}
				else
					Methods.longToast("Some Error Occured,please try again later", MyProfileActivity.this);
			}
		}){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError{
				Map<String, String> params = new HashMap<String, String>();

				BitmapDrawable bitmapDrawable = ((BitmapDrawable) imgProfilePic.getDrawable());
				Bitmap bitmap = bitmapDrawable.getBitmap();
				
				
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inPreferredConfig = Bitmap.Config.ARGB_8888;

				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
				byte[] byteArray = byteArrayOutputStream .toByteArray();

				String fdata = Base64.encodeToString(byteArray, Base64.DEFAULT);

				MeetingListRequestModel model=new MeetingListRequestModel();
				model.setUsername(mPreferenceHelper.getString(Commons.USER_EMAILID));
				model.setUserpass(mPreferenceHelper.getString(Commons.USER_PASSWORD));
				model.setFilename(txtMembershipNo.getText().toString()+".jpg");
				model.setFdata(fdata);
				model.setTbl("Member");//mPreferenceHelper.getString(Commons.USER_DEFKEY)
				model.setName(txtMembershipNo.getText().toString());

				String dataString=gson.toJson(model, MeetingListRequestModel.class);

				Log.e("droid", dataString);
				params.put(GetMemberProfileService.DATA, dataString);
				return params;
			}
		};

		App.getInstance().addToRequestQueue(reqSendProfilePic, "reqSendProfilePic");
		reqSendProfilePic.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));

	}

	private boolean validateFields() {

		if(null !=txtMembershipNo.getText().toString() && txtMembershipNo.getText().toString().trim().length() >0){
			
		//	if(null !=txtMemberSurname.getText().toString() && txtMemberSurname.getText().toString().trim().length() >0){
			
			
			if(null !=txtMemberName.getText().toString() && txtMemberName.getText().toString().trim().length() >0){
				//if(null !=txtMemberDateOfBirth.getText().toString() && txtMemberDateOfBirth.getText().toString().trim().length() >0){
					//	if(null !=txtMemberMartialInfo.getText().toString() && txtMemberMartialInfo.getText().toString().trim().length() >0){
					if(null !=txtEmailID1.getText().toString() && txtEmailID1.getText().toString().trim().length() >0){
						if(Methods.isValidEmail(txtEmailID1.getText().toString().trim())){
							//		if(null !=txtEmailID2.getText().toString() && txtEmailID2.getText().toString().trim().length() >0){
							//			if(Methods.isValidEmail(txtEmailID2.getText().toString().trim())){
							if(null !=txtPassword.getText().toString() && txtPassword.getText().toString().trim().length() >0){
								return true;
							}else
								Methods.smallToast("Password cannot be blank", this);
							//			}else
							//				Methods.smallToast("Please enter Valid Email id", this);
							//		}else
							//			Methods.smallToast("Please enter Email id", this);
						}else
							Methods.smallToast("Please enter Valid Email id", this);
					}else
						Methods.smallToast("Please enter Email id", this);
					//}else
					//	Methods.smallToast("Please enter Member MartialInfo", this);
//				//}else
//					Methods.smallToast("Please enter Member Date of Birth", this);
			}		
			else
				Methods.smallToast("Please enter First Name", this);
		//}		
		//else
		//	Methods.smallToast("Please enter Member Surname", this);
	}else
			
			Methods.smallToast("Please enter Membership No", this);


		return false;
	}

	private void openDialog() {
		final CharSequence[] items = { "Take Photo","Choose from Gallery",
		"Cancel" };//

		AlertDialog.Builder builder = new AlertDialog.Builder(MyProfileActivity.this);
		builder.setTitle("Add Photo!");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				if (items[item].equals("Take Photo")) {
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					startActivityForResult(intent, REQUEST_CAMERA);
				} 
				else if (items[item].equals("Choose from Gallery")) {
					Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					intent.setType("image/*");
					startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
				} 
				else if (items[item].equals("Cancel")) {
					dialog.dismiss();
				}
			}
		});
		builder.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
			if (requestCode == REQUEST_CAMERA) {
				thumbnail = (Bitmap) data.getExtras().get("data");
				ByteArrayOutputStream bytes = new ByteArrayOutputStream();
				thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

				File destination = new File(Environment.getExternalStorageDirectory(),System.currentTimeMillis() + ".jpg");

				FileOutputStream fo;
				try {
					destination.createNewFile();
					fo = new FileOutputStream(destination);
					fo.write(bytes.toByteArray());
					fo.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

				imgProfilePic.setImageBitmap(thumbnail);
				if(NetworkHelper.isOnline(MyProfileActivity.this)){
					Methods.showProgressDialog(this);
					uploadProfPicService();
				}
				else
					Methods.longToast("Please connect to Internet", this);
				

			} else if (requestCode == SELECT_FILE) {
				
				if (data != null) {
					Uri imageUri = data.getData();
					//String imgPathToBundle = Methods.getRealPathFromURI(this, imageUri);

					try {
						Bitmap bitMapToSet = Methods.decodeSampledBitmapFromPath(
								Methods.getRealPathFromURI(this, imageUri), 100,100);

						if (bitMapToSet.getWidth() >= bitMapToSet.getHeight()) {
							thumbnail = Bitmap.createBitmap(bitMapToSet,bitMapToSet.getWidth() / 2- bitMapToSet.getHeight() / 2, 0,
									bitMapToSet.getHeight(),bitMapToSet.getHeight());
						} else {
							thumbnail = Bitmap.createBitmap(bitMapToSet,0,bitMapToSet.getHeight() / 2- bitMapToSet.getWidth() / 2,
									bitMapToSet.getWidth(), bitMapToSet.getWidth());
						}
						imgProfilePic.setImageBitmap(thumbnail);
						
						if(NetworkHelper.isOnline(MyProfileActivity.this)){
							Methods.showProgressDialog(this);
							uploadProfPicService();
						}
						else
							Methods.longToast("Please connect to Internet", this);

					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {

				}
				
				
				/*Uri selectedImage = data.getData();
				String[] filePathColumn = { MediaStore.Images.Media.DATA };

				Cursor cursor = getContentResolver().query(selectedImage,
						filePathColumn, null, null, null);
				cursor.moveToFirst();

				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				String picturePath = cursor.getString(columnIndex);
				cursor.close();
				
				imgProfilePic.setImageBitmap(BitmapFactory.decodeFile(picturePath)); */


				/*BitmapFactory.Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = true;
				BitmapFactory.decodeFile(picturePath, options);
				final int REQUIRED_SIZE = 200;
				int scale = 1;
				while (options.outWidth / scale / 2 >= REQUIRED_SIZE
						&& options.outHeight / scale / 2 >= REQUIRED_SIZE)
					scale *= 2;
				options.inSampleSize = scale;
				options.inJustDecodeBounds = false;
				thumbnail = BitmapFactory.decodeFile(picturePath, options);

				imgProfilePic.setImageBitmap(thumbnail); */
			}
		}
	}

	/* 
	 * ByteArrayOutputStream bao = new ByteArrayOutputStream();
bm.compress(Bitmap.CompressFormat.JPEG, 90, bao);
byte[] ba = bao.toByteArray();
String ba1 = Base64.encodeBytes(ba);
ArrayList < NameValuePair > nameValuePairs = new
ArrayList < NameValuePair > ();
nameValuePairs.add(new BasicNameValuePair("image", ba1));
try {
    HttpClient httpclient = new DefaultHttpClient();
    HttpPost httppost = new
    HttpPost("URL STRING");
    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
    HttpResponse response = httpclient.execute(httppost);
    HttpEntity entity = response.getEntity();
    is = entity.getContent();
    //Toast.makeText(SignUpActivity.this, "Joining Failed", Toast.LENGTH_SHORT).show();
} catch (Exception e) {
    Log.e("log_tag", "Error in http connection " + e.toString());
}*/	

	private class SetProfPicTask extends AsyncTask<String, Void, Bitmap> {
		private String mImgPath;

		public SetProfPicTask(String imgPath) {
			mImgPath=imgPath;//img prob
		}

		protected Bitmap doInBackground(String... urls) {

			Bitmap mIcon11 = null;
			try {
				InputStream in = new java.net.URL("http://loveworldsynergy.org"+mImgPath).openStream();
				mIcon11 = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return mIcon11;
		}

		protected void onPostExecute(Bitmap result) {
			imgProfilePic.setImageBitmap(result);
		}
		
//10-26 12:59:00.720: E/AndroidRuntime(10940): java.lang.StackOverflowError


	}
	
	public void LogoutfinishActivity(){
		
		MyProfileActivity.this.finish();
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
