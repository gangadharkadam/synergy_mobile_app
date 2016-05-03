package com.mutech.databasedetails;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import com.mutech.synergy.R.layout;
import com.mutech.synergy.SynergyValues.Commons;
import com.mutech.synergy.SynergyValues.ImageUrl;
import com.mutech.synergy.SynergyValues.Web.GetHigherHierarchyService;
import com.mutech.synergy.SynergyValues.Web.GetMemberProfileService;
import com.mutech.synergy.SynergyValues.Web.LowerHierarchyService;
import com.mutech.synergy.SynergyValues.Web.create_memberService;
import com.mutech.synergy.SynergyValues.Web.fillLoginSpinner;
import com.mutech.synergy.activities.cellMasters.CreateCellMasterActivity;
import com.mutech.synergy.activities.dashboard.FirstTimeMonthActivity;
import com.mutech.synergy.activities.profile.MyProfileActivity;
import com.mutech.synergy.adapters.MasterSpinnerAdpter;
import com.mutech.synergy.models.HigherHierarchyRespModel;
import com.mutech.synergy.models.MeetingListRequestModel;
import com.mutech.synergy.models.MemberProfileModel;
import com.mutech.synergy.models.HigherHierarchyRespModel.HHSubModel;
import com.mutech.synergy.models.MemberProfileModel.ProfileSubModel;
import com.mutech.synergy.utils.GooglePlacesApiAdapter;
import com.mutech.synergy.utils.InputValidation;
import com.mutech.synergy.utils.Methods;
import com.mutech.synergy.utils.NetworkHelper;
import com.mutech.synergy.utils.PreferenceHelper;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class CreateNewMemberActivity extends AppCompatActivity {
	
	
	private String mAutocompleteText;
	private GooglePlacesApiAdapter mPlacesApiAdapter;
	AutoCompleteTextView txtLandmarkforOfficeAddress,txtLandmarkforHomeAddress;
	private EditText txtMemberName,txtMemberSurname,txtMemberPhone1,txtMemberPhone2,txtEmailID1,txtEmailID2,
	txtMemberHomeAddress,txtOfficeAddress,txtShortbio,txtYookosID,txtBaptisedWhere,
	txtYearlyIncome,txtCoreCompeteance,txtDesignation;
	private TextView txtMemberDateOfBirth,txtempInfo,txtBaptisedWhen,txtDateofJoining,lblphoto;
	private ImageView imgProfilePic,editEmailId1,editEmailId2,editPassword;
	private Button btnSaveMemberInfo;
	private Spinner spnEducationalQualification,spnExperienceYears,spnIndustrySegment,spnEmploymentStatus,spnMemberMartialInfo;
	ArrayAdapter<String> adapterIndSeg,adapterQual,adapterExp,adapterEmpStatus,adapterMartial;
	private ArrayList<String> mZoneList,mRegionList,mChurchList,mSeniorCellList,mGrpChurchList,mPCFList,mCellList;
	private ArrayList<String> educationQualList,expYearsList,industrySegList,empStatusList,martialInfoList;
	private PreferenceHelper mPreferenceHelper;
	private Gson gson;
	ArrayList<ProfileSubModel> mProfSubModel;
	private DatePickerDialog birthDatePickerDialog,birthDatePickerDialog2,birthDatePickerDialog3;
	private SimpleDateFormat dateFormatter,dateFormatterService,dateFormatter01;
	private int SELECT_FILE=0,REQUEST_CAMERA=1;
	private Bitmap thumbnail;
	String str;
	private Spinner txtGender,txtAgeGroup,txtBaptismStatus,txtholyghost,txtBornAgain,txtSchoolstatus,txttitle;
	private ArrayList<String> genderList,agegroupList,baptismStatusList,holyghostList,bornAgainList,SchoolstatusList,titleList;
	TextView lblzone,lblgroupchurch,lblchurch,lblpcf,lblseniorcell,lblcell;
	private Spinner spnSeniorCell,spnCellZone,spnCellRegion,spnCellChurch,spnSeniorCellChurchgroup,spnCell;
	private Spinner txtPCF;
	String defKey,defVal,defRole;

//	private ArrayList<String> txtisnewconvertList;
//	private Spinner spisnewconvert;

	TextView bapWhrTV, bapWhnTV;
	Calendar dob, doj;
	TextView txtBaptismInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_new_member);
		
		getSupportActionBar().hide();
		mPreferenceHelper=new PreferenceHelper(this);
		
	    str=mPreferenceHelper.getString(Commons.USER_ROLE);
	    defKey=mPreferenceHelper.getString(Commons.USER_DEFKEY);
		defVal=mPreferenceHelper.getString(Commons.USER_DEFVALUE);
		defRole=mPreferenceHelper.getString(Commons.USER_ROLE);
		gson=new Gson();

		lblzone=(TextView) findViewById(R.id.lblzone);
		lblgroupchurch=(TextView) findViewById(R.id.lblgroupchurch);
		lblchurch=(TextView) findViewById(R.id.lblchurch);
		lblpcf=(TextView) findViewById(R.id.lblpcf);
		lblseniorcell=(TextView) findViewById(R.id.lblseniorcell);
		lblcell=(TextView) findViewById(R.id.lblcell);

//		spisnewconvert=(Spinner) findViewById(R.id.txtisnewconvert);
		spnCell=(Spinner) findViewById(R.id.spnCell);
		spnSeniorCell=(Spinner) findViewById(R.id.spnSeniorCell);
		spnCellZone=(Spinner) findViewById(R.id.spnCellZone);
		spnCellRegion=(Spinner) findViewById(R.id.spnCellRegion);
		spnSeniorCellChurchgroup=(Spinner) findViewById(R.id.spnSeniorCellChurchgroup);
		spnCellChurch=(Spinner) findViewById(R.id.spnCellChurch);
		txtPCF = (Spinner) findViewById(R.id.txtPCFName);

		txtLandmarkforHomeAddress=(AutoCompleteTextView) findViewById(R.id.txtLandmarkforHomeAddress);
		txtLandmarkforOfficeAddress=(AutoCompleteTextView) findViewById(R.id.txtLandmarkforOfficeAddress);
		txtBaptisedWhen=(TextView) findViewById(R.id.txtBaptisedWhen);
		txtDateofJoining=(TextView) findViewById(R.id.txtDateofJoining);
		txtYookosID=(EditText) findViewById(R.id.txtYookosID);
		txtBaptisedWhere=(EditText) findViewById(R.id.txtBaptisedWhere);
		
	
	
		txtShortbio=(EditText) findViewById(R.id.txtShortbio);
		txtDesignation=(EditText) findViewById(R.id.txtDesignation);
	//	txtMembershipNo=(EditText) findViewById(R.id.txtMembershipNo);
		txtMemberName=(EditText) findViewById(R.id.txtMemberfName);
		txtMemberSurname=(EditText) findViewById(R.id.txtMemberlName);
		
		txtMemberDateOfBirth=(TextView) findViewById(R.id.txtMemberDateOfBirth);
		spnMemberMartialInfo=(Spinner) findViewById(R.id.txtMemberMartialInfo);
		txtMemberPhone1=(EditText) findViewById(R.id.txtMemberPhone1);
		txtMemberPhone2=(EditText) findViewById(R.id.txtMemberPhone2);
		txtEmailID1=(EditText) findViewById(R.id.txtEmailID1);
		txtEmailID2=(EditText) findViewById(R.id.txtEmailID2);
	//	txtPassword=(EditText) findViewById(R.id.txtPassword);
		txtMemberHomeAddress=(EditText) findViewById(R.id.txtMemberHomeAddress);
		txtOfficeAddress=(EditText) findViewById(R.id.txtOfficeAddress);
		spnEmploymentStatus=(Spinner) findViewById(R.id.txtEmploymentStatus);
		spnIndustrySegment=(Spinner) findViewById(R.id.txtIndustrySegment);
		txtYearlyIncome=(EditText) findViewById(R.id.txtYearlyIncome);
		spnExperienceYears=(Spinner) findViewById(R.id.txtExperienceYears);
		txtCoreCompeteance=(EditText) findViewById(R.id.txtCoreCompeteance);
		spnEducationalQualification=(Spinner) findViewById(R.id.txtEducationalQualification);
		txtempInfo=(TextView) findViewById(R.id.empInfo);
		
		txtGender=(Spinner) findViewById(R.id.txtGender);
		txtAgeGroup=(Spinner) findViewById(R.id.txtAgeGroup);
		txtBaptismStatus=(Spinner) findViewById(R.id.txtBaptismStatus);
		txtholyghost=(Spinner) findViewById(R.id.txtholyghost);
		txtBornAgain=(Spinner) findViewById(R.id.txtBornAgain);
		txtSchoolstatus=(Spinner) findViewById(R.id.txtSchoolstatus);
		txttitle=(Spinner) findViewById(R.id.txttitle);
		btnSaveMemberInfo=(Button) findViewById(R.id.btnSaveMemberInfo);
		lblphoto=(TextView) findViewById(R.id.lblphoto);
		imgProfilePic=(ImageView) findViewById(R.id.imgProfilePic);

		bapWhnTV = (TextView) findViewById(R.id.bapWhnTV);
		bapWhrTV = (TextView) findViewById(R.id.bapWhrTV);

		lblphoto.setVisibility(View.GONE);
		imgProfilePic.setVisibility(View.GONE);
		
		Calendar newCalendar = Calendar.getInstance();
		dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
		dateFormatterService=new SimpleDateFormat("yyyy-MM-dd",Locale.US);
		
		dateFormatter01=new SimpleDateFormat("yyyy-MM-dd");

		txtBaptismInfo = (TextView) findViewById(R.id.baptismInfo);

		txtBaptismStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
				if(txtBaptismStatus.getSelectedItem().toString().contentEquals("Yes")) {
					Log.d("NonStop", "Baptism Status Yes!");
					bapWhnTV.setVisibility(View.VISIBLE);
					bapWhrTV.setVisibility(View.VISIBLE);
					txtBaptisedWhen.setVisibility(View.VISIBLE);
					txtBaptisedWhere.setVisibility(View.VISIBLE);
				} else {
					bapWhnTV.setVisibility(View.GONE);
					bapWhrTV.setVisibility(View.GONE);
					txtBaptisedWhen.setVisibility(View.GONE);
					txtBaptisedWhere.setVisibility(View.GONE);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {

			}
		});

		txtBaptismInfo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				LinearLayout lnbp = (LinearLayout) findViewById(R.id.linear4);

				if (lnbp.getVisibility() == View.GONE) {
					lnbp.setVisibility(View.VISIBLE);
					txtBaptismInfo.setText("- Foundation School / Baptism Status");
				} else {
					lnbp.setVisibility(View.GONE);
					txtBaptismInfo.setText("+ Foundation School / Baptism Status");
				}

			}
		});

		birthDatePickerDialog = new DatePickerDialog(this, new OnDateSetListener() {

			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
				dob = newDate;
				Calendar today = Calendar.getInstance();
				if(dob.after(today)) {
					AlertDialog dialog =new AlertDialog.Builder(CreateNewMemberActivity.this)
							.setCancelable(false)
							.setTitle("Incorrect Date of Birth")
							.setMessage("Date of Birth can not be a future date!")
							.setPositiveButton("OK", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialogInterface, int i) {

								}
							})
							.show();
					TextView textView = (TextView) dialog.findViewById(android.R.id.message);
					textView.setTextSize(18);
				} else {
					txtMemberDateOfBirth.setText(dateFormatter.format(newDate.getTime()));
				}

			}

		},newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

		txtBaptisedWhen.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				txtBaptisedWhen.setText("");
				birthDatePickerDialog2.show();
			}
		});

		birthDatePickerDialog2 = new DatePickerDialog(this, new OnDateSetListener() {

			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
				txtBaptisedWhen.setText(dateFormatter.format(newDate.getTime()));
			}

		},newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

		txtDateofJoining.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				txtDateofJoining.setText("");
				birthDatePickerDialog3.show();
			}
		});

		birthDatePickerDialog3 = new DatePickerDialog(this, new OnDateSetListener() {

			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
				doj = newDate;
				Calendar today = Calendar.getInstance();
				if(doj.after(dob) && doj.before(today)) {
					txtDateofJoining.setText(dateFormatter.format(newDate.getTime()));
				} else {
					AlertDialog dialog =new AlertDialog.Builder(CreateNewMemberActivity.this)
							.setCancelable(false)
							.setTitle("Incorrect Date of joining")
							.setMessage("Date of Joining should be greater than the Date of Birth, " +
									"and lower than Today's Date")
							.setPositiveButton("OK", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialogInterface, int i) {

								}
							})
							.show();
					TextView textView = (TextView) dialog.findViewById(android.R.id.message);
					textView.setTextSize(18);
				}

			}

		},newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
		
		mPlacesApiAdapter=new GooglePlacesApiAdapter(this,R.layout.list_item_autocomplete_for_google_places);
		txtLandmarkforHomeAddress.setAdapter(mPlacesApiAdapter);
		
		txtLandmarkforHomeAddress.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2,
					int arg3) {
				mAutocompleteText = s.toString().toLowerCase(Locale.getDefault());
				mPlacesApiAdapter.getFilter();
			}
		});

		txtLandmarkforOfficeAddress.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2,
					int arg3) {
				mAutocompleteText = s.toString().toLowerCase(Locale.getDefault());
				mPlacesApiAdapter.getFilter();
			}
		});
		
	//firstname,agegroup,sex,memberdigination,emailaddress,	
		
		titleList=new ArrayList<String>();
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
		
		mZoneList=new ArrayList<String>();
		mRegionList=new ArrayList<String>();
		mChurchList=new ArrayList<String>();
		mSeniorCellList=new ArrayList<String>();
		mGrpChurchList=new ArrayList<String>();
		mPCFList=new ArrayList<String>();
		mCellList=new ArrayList<String>();

//		txtisnewconvertList=new ArrayList<String>();
//		txtisnewconvertList.add("Yes");
//		txtisnewconvertList.add("No");

		titleList.add("Bro");
		titleList.add("Sis");
		titleList.add("Dcn");
		titleList.add("Dcns");
		titleList.add("Pastor");
		
		
		genderList.add("Male");
		genderList.add("Female");

		agegroupList.add("0 - 12 Years");
		agegroupList.add("13 - 19 Years");
		agegroupList.add("20 - 25 Years");
		agegroupList.add("26 - 35 Years");
		agegroupList.add("35 - 45 Years");
		agegroupList.add("45 - 55 Years");
		agegroupList.add("56 - 65 Years");
		agegroupList.add("65 Years and Above");
		
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

//		ArrayAdapter<String> adaptertxtisnewconvert=new ArrayAdapter<String>(CreateNewMemberActivity.this, android.R.layout.simple_spinner_item, txtisnewconvertList);
//		adaptertxtisnewconvert.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		spisnewconvert.setAdapter(adaptertxtisnewconvert);

		ArrayAdapter<String> adapterChurch = new ArrayAdapter<String>(CreateNewMemberActivity.this, android.R.layout.simple_spinner_item, titleList);
		adapterChurch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		txttitle.setAdapter(adapterChurch);
		
		ArrayAdapter<String> adapterZone = new ArrayAdapter<String>(CreateNewMemberActivity.this, android.R.layout.simple_spinner_item, genderList);
		adapterZone.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		txtGender.setAdapter(adapterZone);
		
		ArrayAdapter<String> adapterageGroup = new ArrayAdapter<String>(CreateNewMemberActivity.this, android.R.layout.simple_spinner_item, agegroupList);
		adapterageGroup.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		txtAgeGroup.setAdapter(adapterageGroup);
		
		ArrayAdapter<String> adapterbaptismStstus = new ArrayAdapter<String>(CreateNewMemberActivity.this, android.R.layout.simple_spinner_item, baptismStatusList);
		adapterbaptismStstus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		txtBaptismStatus.setAdapter(adapterbaptismStstus);	
		
		ArrayAdapter<String> adapterholyghost = new ArrayAdapter<String>(CreateNewMemberActivity.this, android.R.layout.simple_spinner_item, holyghostList);
		adapterholyghost.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		txtholyghost.setAdapter(adapterholyghost);	
		
		ArrayAdapter<String> adapterBornAgain = new ArrayAdapter<String>(CreateNewMemberActivity.this, android.R.layout.simple_spinner_item, bornAgainList);
		adapterBornAgain.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		txtBornAgain.setAdapter(adapterBornAgain);	
		
	
		ArrayAdapter<String> adaptershcoolstatus = new ArrayAdapter<String>(CreateNewMemberActivity.this, android.R.layout.simple_spinner_item, SchoolstatusList);
		adaptershcoolstatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		txtSchoolstatus.setAdapter(adaptershcoolstatus);	
		
		
		adapterMartial = new ArrayAdapter<String>(CreateNewMemberActivity.this, android.R.layout.simple_spinner_item, martialInfoList);

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
		
		
		adapterQual = new ArrayAdapter<String>(CreateNewMemberActivity.this, android.R.layout.simple_spinner_item, educationQualList);

		adapterQual.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnEducationalQualification.setAdapter(adapterQual);

		expYearsList.add("");
		//expYearsList.add("");
		expYearsList.add("1 - 3 Years");
		expYearsList.add("3 - 6 Years");
		expYearsList.add("6 - 10 Years");
		expYearsList.add("10+ Years");
		
		
		adapterExp = new ArrayAdapter<String>(CreateNewMemberActivity.this, android.R.layout.simple_spinner_item, expYearsList);

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

		adapterIndSeg = new ArrayAdapter<String>(CreateNewMemberActivity.this, android.R.layout.simple_spinner_item, industrySegList);

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
		


		adapterEmpStatus = new ArrayAdapter<String>(CreateNewMemberActivity.this, android.R.layout.simple_spinner_item, empStatusList);

		adapterEmpStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnEmploymentStatus.setAdapter(adapterEmpStatus);
		
		
		
		btnSaveMemberInfo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//createNewMember();

				if(NetworkHelper.isOnline(CreateNewMemberActivity.this)){
					if (isValid()) {
							Methods.showProgressDialog(CreateNewMemberActivity.this);
							createNewMember();
						}
					}
				else
					Methods.longToast("Please connect to Internet", CreateNewMemberActivity.this);
			}
		});
		
		txtDateofJoining.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				birthDatePickerDialog3.show();
			}
		});
		
		
		txtBaptisedWhen.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				birthDatePickerDialog2.show();
				
			}
		});
		
		
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


		 if(mPreferenceHelper.getString(Commons.USER_ROLE).equals("PCF Leader")){
				
				
				lblzone.setVisibility(View.GONE);
				lblgroupchurch.setVisibility(View.GONE);
				lblchurch.setVisibility(View.GONE);
				lblpcf.setVisibility(View.GONE);
				
				
				spnCellZone.setVisibility(View.VISIBLE);
				spnCellRegion.setVisibility(View.VISIBLE);
				spnSeniorCellChurchgroup.setVisibility(View.VISIBLE);
				spnCellChurch.setVisibility(View.VISIBLE);
				txtPCF.setVisibility(View.VISIBLE);
				
				
				spnCellZone.setEnabled(false);
				spnCellRegion.setEnabled(false);
				spnSeniorCellChurchgroup.setEnabled(false);
				spnCellChurch.setEnabled(false);
			
				
			}
			
	      if(mPreferenceHelper.getString(Commons.USER_ROLE).equals("Senior Cell Leader")){
	    	  
	    	  
	    	  	lblzone.setVisibility(View.GONE);
				lblgroupchurch.setVisibility(View.GONE);
				lblchurch.setVisibility(View.GONE);
				lblpcf.setVisibility(View.GONE);
				lblseniorcell.setVisibility(View.GONE);
				
				spnCellZone.setVisibility(View.VISIBLE);
				spnCellRegion.setVisibility(View.VISIBLE);
				spnSeniorCellChurchgroup.setVisibility(View.VISIBLE);
				spnCellChurch.setVisibility(View.VISIBLE);
				txtPCF.setVisibility(View.VISIBLE);
				spnSeniorCell.setVisibility(View.VISIBLE);
				
				spnCellZone.setEnabled(false);
				spnCellRegion.setEnabled(false);
				spnSeniorCellChurchgroup.setEnabled(false);
				spnCellChurch.setEnabled(false);
				txtPCF.setEnabled(false);
				
			}
	      
	      if(mPreferenceHelper.getString(Commons.USER_ROLE).equals("Cell Leader")){
	    	  
	    	  
	    	  	lblzone.setVisibility(View.GONE);
				lblgroupchurch.setVisibility(View.GONE);
				lblchurch.setVisibility(View.GONE);
				lblpcf.setVisibility(View.GONE);
				lblseniorcell.setVisibility(View.GONE);
				lblcell.setVisibility(View.GONE);
				
				spnCellZone.setVisibility(View.VISIBLE);
				spnCellRegion.setVisibility(View.VISIBLE);
				spnSeniorCellChurchgroup.setVisibility(View.VISIBLE);
				spnCellChurch.setVisibility(View.VISIBLE);
				txtPCF.setVisibility(View.VISIBLE);
				spnSeniorCell.setVisibility(View.VISIBLE);
				spnCell.setVisibility(View.VISIBLE);
				
				spnCellZone.setEnabled(false);
				spnCellRegion.setEnabled(false);
				spnSeniorCellChurchgroup.setEnabled(false);
				spnCellChurch.setEnabled(false);
				txtPCF.setEnabled(false);
				spnSeniorCell.setEnabled(false);
				
			}
	      
	      
	      //01-27 16:01:11.358: E/(4291): Role--Cell Leader

	      
	      if(mPreferenceHelper.getString(Commons.USER_ROLE).equals("Church Pastor")){
				
	    		lblzone.setVisibility(View.GONE);
				lblgroupchurch.setVisibility(View.GONE);
				lblchurch.setVisibility(View.GONE);
			
				
				spnCellZone.setVisibility(View.VISIBLE);
				spnCellRegion.setVisibility(View.VISIBLE);
				spnSeniorCellChurchgroup.setVisibility(View.VISIBLE);
				spnCellChurch.setVisibility(View.VISIBLE);
				spnCellZone.setEnabled(false);
				spnCellRegion.setEnabled(false);
				spnSeniorCellChurchgroup.setEnabled(false);
				
				
			}
			
	      if(mPreferenceHelper.getString(Commons.USER_ROLE).equals("Group Church Pastor")){
				
	    	  
	    		lblzone.setVisibility(View.GONE);
				lblgroupchurch.setVisibility(View.GONE);
			
				spnCellZone.setVisibility(View.VISIBLE);
				spnCellRegion.setVisibility(View.VISIBLE);
				spnSeniorCellChurchgroup.setVisibility(View.VISIBLE);
			
	    	  
				spnCellZone.setEnabled(false);
				spnCellRegion.setEnabled(false);
				spnSeniorCellChurchgroup.setEnabled(false);
				
				
			}
	      
	      if(mPreferenceHelper.getString(Commons.USER_ROLE).equals("Zonal Pastor")){
				
	    	  
	    		lblzone.setVisibility(View.GONE);
			
				spnCellZone.setVisibility(View.VISIBLE);
		
				
			}
			
	      lblzone.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(NetworkHelper.isOnline(CreateNewMemberActivity.this)){
					
					String selval=spnCellRegion.getSelectedItem().toString();
					
					Methods.showProgressDialog(CreateNewMemberActivity.this);
					getSpinnerData("Regions",selval );

				}else
				{
					Methods.longToast("Please connect to Internet",CreateNewMemberActivity.this);
				}
				
				
			}
		});
	      
	  	
	      lblgroupchurch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				String selval="";
			
				if(NetworkHelper.isOnline(CreateNewMemberActivity.this)){
				
					if(lblzone.getVisibility()==View.VISIBLE){
					
						
						Methods.smallToast("Please Select Zone", CreateNewMemberActivity.this);
						
						
					
					}else{
						
						selval=spnCellZone.getSelectedItem().toString();
						Methods.showProgressDialog(CreateNewMemberActivity.this);
						getSpinnerData("Zones",selval );

					}
					
				
				}else
				{
					Methods.longToast("Please connect to Internet",CreateNewMemberActivity.this);
				}
				
				
			}
		});
	      
	      lblchurch.setOnClickListener(new OnClickListener() {
	  		
	  		@Override
	  		public void onClick(View v) {
	  			// TODO Auto-generated method stub
	  			
	  			String selval="";
	  		
	  			if(NetworkHelper.isOnline(CreateNewMemberActivity.this)){
	  			
	  				if(lblgroupchurch.getVisibility()==View.VISIBLE){
	  					
						Methods.smallToast("Please Select Group Church", CreateNewMemberActivity.this);
					
					}else{
						
						selval=spnSeniorCellChurchgroup.getSelectedItem().toString();
						Methods.showProgressDialog(CreateNewMemberActivity.this);
						getSpinnerData("Group Churches",selval );
				
					}
	  			}else
	  			{
	  				Methods.longToast("Please connect to Internet",CreateNewMemberActivity.this);
	  			}
	  			
	  			
	  		}
	  	});
	    
	      lblpcf.setOnClickListener(new OnClickListener() {

	    		@Override
	    		public void onClick(View v) {
	    			// TODO Auto-generated method stub

	    			String selval="";

	    			if(NetworkHelper.isOnline(CreateNewMemberActivity.this)){

	    				if(lblchurch.getVisibility()==View.VISIBLE){

	    					Methods.smallToast("Please Select Church", CreateNewMemberActivity.this);

	    				}else{

	    					selval=spnCellChurch.getSelectedItem().toString();
	    					Methods.showProgressDialog(CreateNewMemberActivity.this);
	    					getSpinnerData("Churches",selval );
	    				}

	    			}else
	    			{
	    				Methods.longToast("Please connect to Internet",CreateNewMemberActivity.this);
	    			}


	    		}
	    	});
	      
	      lblseniorcell.setOnClickListener(new OnClickListener() {
	  		
	  		@Override
	  		public void onClick(View v) {
	  			// TODO Auto-generated method stub
	  		
	  			if(NetworkHelper.isOnline(CreateNewMemberActivity.this)){
	  				
	  				if(lblpcf.getVisibility()==View.VISIBLE){

						Methods.smallToast("Please Select PCF", CreateNewMemberActivity.this);

					}else{

						String selval=txtPCF.getSelectedItem().toString();
						Methods.showProgressDialog(CreateNewMemberActivity.this);
						getSpinnerData("PCFs",selval );
					}

	  			}else
	  			{
	  				Methods.longToast("Please connect to Internet",CreateNewMemberActivity.this);
	  			}
	  			
	  			
	  		}
	  	});
	      
	      lblcell.setOnClickListener(new OnClickListener() {
		  		
		  		@Override
		  		public void onClick(View v) {
		  			// TODO Auto-generated method stub
		  		
		  			if(NetworkHelper.isOnline(CreateNewMemberActivity.this)){
		  				
		  				if(lblseniorcell.getVisibility()==View.VISIBLE){
		  					
							Methods.smallToast("Please Select Seniorcell", CreateNewMemberActivity.this);
						
						}else{
		  				
							String selval=spnSeniorCell.getSelectedItem().toString();
							Methods.showProgressDialog(CreateNewMemberActivity.this);
							getSpinnerData("Senior Cells",selval );
						}

		  			}else
		  			{
		  				Methods.longToast("Please connect to Internet",CreateNewMemberActivity.this);
		  			}
		  			
		  			
		  		}
		  	});
	      
	      
	      if(NetworkHelper.isOnline(this)){
				Methods.showProgressDialog(this);
				//getAllSeniorCell();
				//getAllZones();
				
				if(defRole.equals("Regional Pastor")){
					getLowerHierarchy();
			
				}else{
					getTopHierarchy();
				}

			}else
			{
				Methods.longToast("Please connect to Internet", this);
			}
	}
	
	private void createNewMember() {
		StringRequest reqGetProfile=new StringRequest(Method.POST,create_memberService.SERVICE_URL,new Listener<String>() {

			@Override
			public void onResponse(String response) {
				Methods.closeProgressDialog();
				
				
				Log.e("responce droid","get reqGetProfile ---------------"+ response);
			
				try {
					
					JSONObject jsonobj=new JSONObject(response);
					
					if(response.contains("status")){
					
						JSONObject jsonobj1 = jsonobj.getJSONObject("message");
						String status=jsonobj1.getString("status");
						if(status.equals("401")){
							Methods.longToast("User name or Password is incorrect", CreateNewMemberActivity.this);
						}else{
							Methods.longToast(jsonobj.getString("message"), CreateNewMemberActivity.this);
						}
						if(status.equals("402")){
							Methods.longToast(jsonobj.getString("message"), CreateNewMemberActivity.this);
						}

					
				}else{
					
					String msg=jsonobj.getString("message");
					Methods.longToast(msg,CreateNewMemberActivity.this);
					
				}
		
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				finish();
			}
		},new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Methods.closeProgressDialog();
				Log.d("droid", "get reqsaveMeeting error---------------" + error.getCause());

				if(error.networkResponse.statusCode==403){
					Methods.longToast("No access to update Profile", CreateNewMemberActivity.this);
				}
				else
					Methods.longToast("Some Error Occured,please try again later", CreateNewMemberActivity.this);
			}
		}){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError{
				Map<String, String> params = new HashMap<String, String>();

				MeetingListRequestModel model=new MeetingListRequestModel();
				JSONObject obj=new JSONObject();
				
				
				try {
					
						obj.put("username",mPreferenceHelper.getString(Commons.USER_EMAILID));
						obj.put("userpass", mPreferenceHelper.getString(Commons.USER_PASSWORD));
				  	    obj.put("short_bio",txtShortbio.getText().toString());
						obj.put("office_landmark", txtLandmarkforOfficeAddress.getText().toString());
					
						obj.put("surname",txtMemberSurname.getText().toString());
						obj.put("yearly_income",txtYearlyIncome.getText().toString());
						obj.put("zone", "3A_Zone");
						obj.put("group_church_name", "churchgroup");
						obj.put("cell_name", "cell_name");
						obj.put("email_id", txtEmailID1.getText().toString());
						obj.put("core_competeance", txtCoreCompeteance.getText().toString());
						obj.put("email_id2", txtEmailID2.getText().toString());
						//obj.put("zone_name", "zone_name");
						obj.put("employment_status", spnEmploymentStatus.getSelectedItem().toString());
						obj.put("cell", "3A_Zone/CHR0001/CEL0001");
						obj.put("pcf", "3A_Zone/CHR0001/PCF0001");
						
						obj.put("address", txtMemberHomeAddress.getText().toString());
						obj.put("senior_cell", "3A_Zone/CHR0001/SCL0001");
						obj.put("church", "3A_Zone/CHR0001");
						obj.put("title", txttitle.getSelectedItem().toString());
						obj.put("office_address", txtOfficeAddress.getText().toString());
						obj.put("phone_1", txtMemberPhone1.getText().toString());
						obj.put("region","Region 3");
						obj.put("baptism_where", txtBaptisedWhere.getText().toString());

						if(!txtBaptisedWhen.getText().toString().contentEquals("")) {
							String dobtism=dateFormatter01.format(dateFormatter.parse(txtBaptisedWhen.getText().toString()));
							obj.put("baptism_when", dobtism);
						} else {
							obj.put("baptism_when", null);
						}

						obj.put("educational_qualification", spnEducationalQualification.getSelectedItem().toString());
						obj.put("age_group", txtAgeGroup.getSelectedItem().toString());
						obj.put("baptisum_status", txtBaptismStatus.getSelectedItem().toString());
						obj.put("member_name", txtMemberName.getText().toString());
						obj.put("sex", txtGender.getSelectedItem().toString());

                        if(!txtMemberDateOfBirth.getText().toString().contentEquals("")) {
                            String dob = dateFormatter01.format(dateFormatter.parse(txtMemberDateOfBirth.getText().toString()));
                            obj.put("date_of_birth", dob);
                        } else {
                            obj.put("date_of_birth", null);
                        }

						
						obj.put("phone_2", txtMemberPhone2.getText().toString());
						obj.put("experience_years",spnExperienceYears.getSelectedItem().toString() );
						obj.put("member_designation", txtDesignation.getText().toString());
						//obj.put("pcf_name", "pcf_name");
						obj.put("school_status", txtSchoolstatus.getSelectedItem().toString());
						obj.put("industry_segment",spnIndustrySegment.getSelectedItem().toString());
						//obj.put("church_name", value)
						obj.put("filled_with_holy_ghost",txtholyghost.getSelectedItem().toString());
						//obj.put("region_name", "region_name");
						obj.put("is_new_born", txtBornAgain.getSelectedItem().toString());
						//obj.put("senior_cell_name", value)
						obj.put("is_eligibale_for_follow_up", "1");
						
						if(!txtDateofJoining.getText().toString().contentEquals("")) {
                            String doj=dateFormatter01.format(dateFormatter.parse(txtDateofJoining.getText().toString()));
                            obj.put("date_of_join",doj);
                        } else {
                            obj.put("date_of_join",null);
                        }

						obj.put("marital_info",spnMemberMartialInfo.getSelectedItem().toString());
						obj.put("yokoo_id", txtYookosID.getText().toString());


//						obj.put("is_new_convert", spisnewconvert.getSelectedItem().toString());

						obj.put("church_group", "3A_Zone/GRP0001");
						
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				
				String dataString=obj.toString();//gson.toJson(model, MeetingListRequestModel.class);
				Log.d("NonStop", "New Member Request: " + dataString);
				Log.e("Request droid", dataString);
				params.put(create_memberService.DATA, dataString);
				return params;
			}
		};

		App.getInstance().addToRequestQueue(reqGetProfile, "reqGetProfile");
		reqGetProfile.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
	}
	
private void getSpinnerData(final String tbl,final String selval ){
		
		StringRequest reqgetLowerHierarchy=new StringRequest(Method.POST,LowerHierarchyService.SERVICE_URL,new Listener<String>() {

			@Override
			public void onResponse(String response) {
				Methods.closeProgressDialog();
				Log.e("droid","get reqgetLowerHierarchy ---------------"+ response);

				HigherHierarchyRespModel mHHModel=gson.fromJson(response, HigherHierarchyRespModel.class);
				//Object meetingmsg=mMeetingModel.getMessage();
			
				try {
					JSONObject jsonobj=new JSONObject(response);
					if(response.contains("status")){
						
						String status=jsonobj.get("status").toString();
						if(status.equals("401"))						
							Methods.longToast("User name or Password is incorrect", CreateNewMemberActivity.this);
					
					}
					else
					{
							
							JSONArray jsonarray=jsonobj.getJSONArray("message");
							
							if(jsonarray.length()>0){
								
								if(tbl.equalsIgnoreCase("PCFs"))
									mSeniorCellList.clear();
								if(tbl.equalsIgnoreCase("Churches"))
									mPCFList.clear();
								if(tbl.equalsIgnoreCase("Group Churches"))
									mChurchList.clear();
								if(tbl.equalsIgnoreCase("Zones"))
									mGrpChurchList.clear();
								if(tbl.equalsIgnoreCase("Regions"))
									mZoneList.clear();
								if(tbl.equals("Senior Cells"))
									mCellList.clear();
								
								for(int i=0;i<jsonarray.length();i++)
									if(tbl.equalsIgnoreCase("Senior Cells")){
										
										mCellList.add(jsonarray.getJSONObject(i).getString("name"));
									}else if( tbl.equalsIgnoreCase("PCFs")){
										
										mSeniorCellList.add(jsonarray.getJSONObject(i).getString("name"));
									}else if(tbl.equalsIgnoreCase("Churches")){
										
										mPCFList.add(jsonarray.getJSONObject(i).getString("name"));
									}else if(tbl.equalsIgnoreCase("Group Churches")){
										
										mChurchList.add(jsonarray.getJSONObject(i).getString("name"));
									}else if(tbl.equalsIgnoreCase("Zones")){
										
										mGrpChurchList.add(jsonarray.getJSONObject(i).getString("name"));
									}else if(tbl.equalsIgnoreCase("Regions")){
										
										mZoneList.add(jsonarray.getJSONObject(i).getString("name"));
									}
								
								setAdapters();
											
								if(tbl.equalsIgnoreCase("Regions")){
									
									Log.e("condistion match","condistion match");
								 
									lblzone.setVisibility(View.GONE);
								    spnCellZone.setVisibility(View.VISIBLE);
								}else{
									Log.e("condistion match"," not condistion match");
								}
									
								
								if(tbl.equalsIgnoreCase("Zones")){
									lblgroupchurch.setVisibility(View.GONE);
									spnSeniorCellChurchgroup.setVisibility(View.VISIBLE);
								}
							
								if(tbl.equalsIgnoreCase("Group Churches")){
									lblchurch.setVisibility(View.GONE);
									spnCellChurch.setVisibility(View.VISIBLE);
								}
								
								if(tbl.equalsIgnoreCase("Churches")){
									lblpcf.setVisibility(View.GONE);
									txtPCF.setVisibility(View.VISIBLE);
								}
								
								if(tbl.equalsIgnoreCase("PCFs")){
									lblseniorcell.setVisibility(View.GONE);
									spnSeniorCell.setVisibility(View.VISIBLE);
								}
								
								if(tbl.equalsIgnoreCase("Senior Cells")){
									lblcell.setVisibility(View.GONE);
									spnCell.setVisibility(View.VISIBLE);
								}
								
								
							}else{
								Methods.longToast("Resord Not Found", CreateNewMemberActivity.this);
							}
							
					
						
					}
					
					
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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

				model.setTbl(tbl);
				model.setName(selval);
//                model.setName(spnSeniorCell.getSelectedItem().toString());
				String dataString=gson.toJson(model, MeetingListRequestModel.class);

//				Log.e(null, "111--"+spnSeniorCell.getSelectedItemPosition());
				
				Log.e("droid", dataString);
				params.put(GetHigherHierarchyService.DATA, dataString);
				return params;
			}
		};

		App.getInstance().addToRequestQueue(reqgetLowerHierarchy, "reqgetLowerHierarchy");
		reqgetLowerHierarchy.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));


	}

private void setAdapters() {

	Log.e("defKey",mPreferenceHelper.getString(Commons.USER_DEFKEY));
	Log.e("defRole",mPreferenceHelper.getString(Commons.USER_ROLE));
	Log.e("defVal",mPreferenceHelper.getString(Commons.USER_DEFVALUE));
	
	/*if(null !=defVal){
		if( defKey.equalsIgnoreCase("PCFs")){
			mPCFList.add(defVal);
		}else if(defKey.equalsIgnoreCase("Senior Cells")){
			mSeniorCellList.add(defVal);
		}else if(defKey.equalsIgnoreCase("Churches")){
			mChurchList.add(defVal);
		}else if(defKey.equalsIgnoreCase("Group Churches")){
			mGrpChurchList.add(defVal);
		}else if(defKey.equalsIgnoreCase("Zones")){
			mZoneList.add(defVal);
		}else if(defKey.equalsIgnoreCase("Regions")){
			mRegionList.add(defVal);
		}
	}*/
	
	/*MasterSpinnerAdpter adapterZone=new MasterSpinnerAdpter(CreateNewMemberActivity.this,mZoneList);
	spnCellZone.setAdapter(adapterZone);
	
	MasterSpinnerAdpter adapterRegion=new MasterSpinnerAdpter(CreateNewMemberActivity.this,mRegionList);
	spnCellRegion.setAdapter(adapterRegion);
	
	MasterSpinnerAdpter adapterChurch=new MasterSpinnerAdpter(CreateNewMemberActivity.this,mChurchList);
	spnCellChurch.setAdapter(adapterChurch);
	
	MasterSpinnerAdpter adapterSrCell=new MasterSpinnerAdpter(CreateNewMemberActivity.this,mSeniorCellList);
	spnSeniorCell.setAdapter(adapterSrCell);
	
	MasterSpinnerAdpter adapterchurchgropu=new MasterSpinnerAdpter(CreateNewMemberActivity.this,mGrpChurchList);
	spnSeniorCellChurchgroup.setAdapter(adapterchurchgropu);
	
	MasterSpinnerAdpter adapterPCF=new MasterSpinnerAdpter(CreateNewMemberActivity.this,mPCFList);
	txtPCF.setAdapter(adapterPCF);*/


	ArrayAdapter<String> adapterZone = new ArrayAdapter<String>(CreateNewMemberActivity.this, android.R.layout.simple_spinner_item, mZoneList);
	adapterZone.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	spnCellZone.setAdapter(adapterZone);

	ArrayAdapter<String> adapterRegion = new ArrayAdapter<String>(CreateNewMemberActivity.this, android.R.layout.simple_spinner_item, mRegionList);
	adapterRegion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	spnCellRegion.setAdapter(adapterRegion);

	ArrayAdapter<String> adapterChurch = new ArrayAdapter<String>(CreateNewMemberActivity.this, android.R.layout.simple_spinner_item, mChurchList);
	adapterChurch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	spnCellChurch.setAdapter(adapterChurch);

	ArrayAdapter<String> adapterSrCell = new ArrayAdapter<String>(CreateNewMemberActivity.this, android.R.layout.simple_spinner_item, mSeniorCellList);
	adapterSrCell.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	spnSeniorCell.setAdapter(adapterSrCell);

	ArrayAdapter<String> adapterchurchgropu = new ArrayAdapter<String>(CreateNewMemberActivity.this, android.R.layout.simple_spinner_item, mGrpChurchList);
	adapterSrCell.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	spnSeniorCellChurchgroup.setAdapter(adapterchurchgropu);

	ArrayAdapter<String> adapterPCF = new ArrayAdapter<String>(CreateNewMemberActivity.this, android.R.layout.simple_spinner_item, mPCFList);
	adapterPCF.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	txtPCF.setAdapter(adapterPCF);

	ArrayAdapter<String> adapterCell = new ArrayAdapter<String>(CreateNewMemberActivity.this, android.R.layout.simple_spinner_item, mCellList);
	adapterCell.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	spnCell.setAdapter(adapterCell);
	
}

private void getTopHierarchy() {

	StringRequest reqgetTopHierarchy=new StringRequest(Method.POST,GetHigherHierarchyService.SERVICE_URL,new Listener<String>() {

		@Override
		public void onResponse(String response) {
			Methods.closeProgressDialog();
			Log.e("responce","get reqgetTopHierarchy ---------------"+ response);

			HigherHierarchyRespModel mHHModel=gson.fromJson(response, HigherHierarchyRespModel.class);
			//Object meetingmsg=mMeetingModel.getMessage();
			if(null != mHHModel.getStatus() && mHHModel.getStatus().trim().length() >0){

				if(mHHModel.getStatus()=="401"){
					Methods.longToast("User name or Password is incorrect", CreateNewMemberActivity.this);
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
							mGrpChurchList.add(mHHSubModel.get(i).getChurch_group());
						if(null !=mHHSubModel.get(i).getPcf())
							mPCFList.add(mHHSubModel.get(i).getPcf());
						if(null !=mHHSubModel.get(i).getSenior_cell())
							mSeniorCellList.add(mHHSubModel.get(i).getSenior_cell());
					}
					
				//	setAdapters();

				}else{
					
				}
			}
			Methods.showProgressDialog(CreateNewMemberActivity.this);
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
			//else
				//		Methods.longToast("Some Error Occured,please try again later", CreateSeniorCellMasterActivity.this);

			Methods.showProgressDialog(CreateNewMemberActivity.this);
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

			Log.e("Request", dataString);
			params.put(GetHigherHierarchyService.DATA, dataString);
			return params;
		}
	};

	App.getInstance().addToRequestQueue(reqgetTopHierarchy, "reqgetTopHierarchy");
	reqgetTopHierarchy.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));

}


private void getLowerHierarchy(){


	StringRequest reqgetLowerHierarchy=new StringRequest(Method.POST,fillLoginSpinner.SERVICE_URL,new Listener<String>() {

		@Override
		public void onResponse(String response) {
			Methods.closeProgressDialog();
			Log.e("droid","get reqgetLowerHierarchy ---------------"+ response);

			HigherHierarchyRespModel mHHModel=gson.fromJson(response, HigherHierarchyRespModel.class);
			try {
				JSONObject jsonobj=new JSONObject(response);
				
				if(response.contains("status")){
					String status=jsonobj.getString("status");
					if(status.equals("401")){
						Methods.longToast("User name or Password is incorrect", CreateNewMemberActivity.this);
					}
					
				}else{
					
					JSONArray jsonarray=jsonobj.getJSONArray("message");
					
					if(jsonarray.length()>0){
						
						for(int i=0;i<jsonarray.length();i++){
							
							if( defRole.equalsIgnoreCase("Zonal Pastor")){
								mZoneList.add(jsonarray.getJSONObject(i).getString("name"));
							}else if(defRole.equalsIgnoreCase("Group Church Pastor")){
								mGrpChurchList.add(jsonarray.getJSONObject(i).getString("name"));
							}else if(defRole.equalsIgnoreCase("Church Pastor")){
								mChurchList.add(jsonarray.getJSONObject(i).getString("name"));
							}else if(defRole.equalsIgnoreCase("PCF Leader")){
								mPCFList.add(jsonarray.getJSONObject(i).getString("name"));
							}else if(defRole.equalsIgnoreCase("Senior Cell Leader")){
								mSeniorCellList.add(jsonarray.getJSONObject(i).getString("name"));
							}else if(defRole.equalsIgnoreCase("Cell Leader")){
								mCellList.add(jsonarray.getJSONObject(i).getString("name"));
							}else if(defRole.equalsIgnoreCase("Regional Pastor")){
								mRegionList.add(jsonarray.getJSONObject(i).getString("name"));
							}
						}
						
						setAdapters();
						
					}else{
						
						Methods.longToast("Record Not Found ", CreateNewMemberActivity.this);
						
					}
					
				}
			
				
			
			
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
			setAdapters();
		}

	}){
		@Override
		protected Map<String, String> getParams() throws AuthFailureError{
			Map<String, String> params = new HashMap<String, String>();

			MeetingListRequestModel model=new MeetingListRequestModel();
			model.setUsername(mPreferenceHelper.getString(Commons.USER_EMAILID));
			model.setUserpass(mPreferenceHelper.getString(Commons.USER_PASSWORD));
			
			
			
			if( defRole.equalsIgnoreCase("Zonal Pastor")){
				model.setTbl("Zones");
			}else if(defRole.equalsIgnoreCase("Group Church Pastor")){
				model.setTbl("Group Churches");
			}else if(defRole.equalsIgnoreCase("Church Pastor")){
				model.setTbl("Churches");
			}else if(defRole.equalsIgnoreCase("PCF Leader")){
				model.setTbl("PCFs");
			}else if(defRole.equalsIgnoreCase("Senior Cell Leader")){
				model.setTbl("Senior Cells");
			}else if(defRole.equalsIgnoreCase("Cell Leader")){
				model.setTbl("Cells");
			}else if(defRole.equalsIgnoreCase("Regional Pastor")){
				model.setTbl("Regions");
			}
			
			String dataString=gson.toJson(model, MeetingListRequestModel.class);
			Log.e("droid", dataString);
			params.put(GetHigherHierarchyService.DATA, dataString);
			return params;
		}
	};

	App.getInstance().addToRequestQueue(reqgetLowerHierarchy, "reqgetLowerHierarchy");
	reqgetLowerHierarchy.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));


}

	public boolean isValid() {

		if (!InputValidation.hasText(txtMemberName)) {
			AlertDialog dialog =new AlertDialog.Builder(CreateNewMemberActivity.this)
					.setCancelable(false)
					.setTitle("Invalid Input")
					.setMessage("Please enter first name")
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface, int i) {

						}
					})
					.show();
			TextView textView = (TextView) dialog.findViewById(android.R.id.message);
			textView.setTextSize(18);
			return false;
		}
		if (!InputValidation.hasText(txtEmailID1)) {
			AlertDialog dialog =new AlertDialog.Builder(CreateNewMemberActivity.this)
					.setCancelable(false)
					.setTitle("Invalid Input")
					.setMessage("Please enter a valid email address")
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface, int i) {

						}
					})
					.show();
			TextView textView = (TextView) dialog.findViewById(android.R.id.message);
			textView.setTextSize(18);
			return false;
		}
		if(!InputValidation.spnHasText(txtAgeGroup, "AgeGroup"))
		{
			AlertDialog dialog =new AlertDialog.Builder(CreateNewMemberActivity.this)
					.setCancelable(false)
					.setTitle("Invalid Input")
					.setMessage("Please select age group")
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface, int i) {

						}
					})
					.show();
			TextView textView = (TextView) dialog.findViewById(android.R.id.message);
			textView.setTextSize(18);
			return false;
		}
		if(!InputValidation.spnHasText(txtGender, "Gender"))
		{
			AlertDialog dialog =new AlertDialog.Builder(CreateNewMemberActivity.this)
					.setCancelable(false)
					.setTitle("Invalid Input")
					.setMessage("Please select gender")
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface, int i) {

						}
					})
					.show();
			TextView textView = (TextView) dialog.findViewById(android.R.id.message);
			textView.setTextSize(18);
			return false;
		}
		if(!InputValidation.spnHasText(spnCellRegion, "Region"))
		{
			AlertDialog dialog =new AlertDialog.Builder(CreateNewMemberActivity.this)
					.setCancelable(false)
					.setTitle("Invalid Input")
					.setMessage("Please enter a Region")
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface, int i) {

						}
					})
					.show();
			TextView textView = (TextView) dialog.findViewById(android.R.id.message);
			textView.setTextSize(18);
			return false;
		}
		if(!InputValidation.spnHasText(spnSeniorCellChurchgroup, "ChurchGroup"))
		{
			AlertDialog dialog =new AlertDialog.Builder(CreateNewMemberActivity.this)
					.setCancelable(false)
					.setTitle("Invalid Input")
					.setMessage("Please enter a ChurchGroup")
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface, int i) {

						}
					})
					.show();
			TextView textView = (TextView) dialog.findViewById(android.R.id.message);
			textView.setTextSize(18);
			return false;
		}
		if(!InputValidation.spnHasText(spnCellChurch, "Church") )
		{
			AlertDialog dialog =new AlertDialog.Builder(CreateNewMemberActivity.this)
					.setCancelable(false)
					.setTitle("Invalid Input")
					.setMessage("Please enter a Church")
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface, int i) {

						}
					})
					.show();
			TextView textView = (TextView) dialog.findViewById(android.R.id.message);
			textView.setTextSize(18);
			return false;
		}
		if(!InputValidation.spnHasText(txtPCF, "PCF"))
		{
			AlertDialog dialog =new AlertDialog.Builder(CreateNewMemberActivity.this)
					.setCancelable(false)
					.setTitle("Invalid Input")
					.setMessage("Please select PCF")
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface, int i) {

						}
					})
					.show();
			TextView textView = (TextView) dialog.findViewById(android.R.id.message);
			textView.setTextSize(18);
			return false;
		}
		if(!InputValidation.spnHasText(spnSeniorCell, "SeniorCell"))
		{
			AlertDialog dialog =new AlertDialog.Builder(CreateNewMemberActivity.this)
					.setCancelable(false)
					.setTitle("Invalid Input")
					.setMessage("Please select senior cell")
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface, int i) {

						}
					})
					.show();
			TextView textView = (TextView) dialog.findViewById(android.R.id.message);
			textView.setTextSize(18);
			return false;
		}
		if(!InputValidation.spnHasText(spnCell, "Cell"))
		{
			AlertDialog dialog =new AlertDialog.Builder(CreateNewMemberActivity.this)
					.setCancelable(false)
					.setTitle("Invalid Input")
					.setMessage("Please select a Cell")
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface, int i) {

						}
					})
					.show();
			TextView textView = (TextView) dialog.findViewById(android.R.id.message);
			textView.setTextSize(18);
			return false;
		}
		if (!InputValidation.isEmailAddress(txtEmailID1, true)) {
			AlertDialog dialog =new AlertDialog.Builder(CreateNewMemberActivity.this)
					.setCancelable(false)
					.setTitle("Invalid Input")
					.setMessage("Please enter a valid email id")
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface, int i) {

						}
					})
					.show();
			TextView textView = (TextView) dialog.findViewById(android.R.id.message);
			textView.setTextSize(18);
				return false;
		}
		if (!InputValidation.isEmailAddress(txtEmailID2, false)) {
			AlertDialog dialog =new AlertDialog.Builder(CreateNewMemberActivity.this)
					.setCancelable(false)
					.setTitle("Invalid Input")
					.setMessage("Please enter a valid email id")
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface, int i) {

						}
					})
					.show();
			TextView textView = (TextView) dialog.findViewById(android.R.id.message);
			textView.setTextSize(18);
			return false;
		}
		if (!InputValidation.isEmailAddress(txtYookosID, false)) {
			AlertDialog dialog =new AlertDialog.Builder(CreateNewMemberActivity.this)
					.setCancelable(false)
					.setTitle("Invalid Input")
					.setMessage("Please enter a valid email id")
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface, int i) {

						}
					})
					.show();
			TextView textView = (TextView) dialog.findViewById(android.R.id.message);
			textView.setTextSize(18);
			return false;
		}
		if (!InputValidation.isPhoneNumber(txtMemberPhone1, false)) {
			AlertDialog dialog =new AlertDialog.Builder(CreateNewMemberActivity.this)
					.setCancelable(false)
					.setTitle("Invalid Input")
					.setMessage("Please enter a valid phone number")
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface, int i) {

						}
					})
					.show();
			TextView textView = (TextView) dialog.findViewById(android.R.id.message);
			textView.setTextSize(18);
			return false;
		}
		if (!InputValidation.isPhoneNumber(txtMemberPhone2, false)) {
			AlertDialog dialog =new AlertDialog.Builder(CreateNewMemberActivity.this)
					.setCancelable(false)
					.setTitle("Invalid Input")
					.setMessage("Please enter a valid phone number")
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface, int i) {

						}
					})
					.show();
			TextView textView = (TextView) dialog.findViewById(android.R.id.message);
			textView.setTextSize(18);
			return false;
		}
//		if (!InputValidation.hasText(txtMemberPhone1)) {
//			return false;
//		}
//		if (!InputValidation.hasText(txtMemberPhone2)) {
//			return false;
//		}

		return true;
	}
	
}
