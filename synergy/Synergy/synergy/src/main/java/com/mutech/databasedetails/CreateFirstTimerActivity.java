package com.mutech.databasedetails;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

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
import com.mutech.synergy.SynergyValues.Web.GetHigherHierarchyService;
import com.mutech.synergy.SynergyValues.Web.GetMemberProfileService;
import com.mutech.synergy.SynergyValues.Web.LowerHierarchyService;
import com.mutech.synergy.SynergyValues.Web.create_ftv;
import com.mutech.synergy.SynergyValues.Web.create_memberService;
import com.mutech.synergy.SynergyValues.Web.fillLoginSpinner;
import com.mutech.synergy.models.HigherHierarchyRespModel;
import com.mutech.synergy.models.MeetingListRequestModel;
import com.mutech.synergy.models.HigherHierarchyRespModel.HHSubModel;
import com.mutech.synergy.utils.GooglePlacesApiAdapter;
import com.mutech.synergy.utils.InputValidation;
import com.mutech.synergy.utils.Methods;
import com.mutech.synergy.utils.NetworkHelper;
import com.mutech.synergy.utils.PreferenceHelper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CreateFirstTimerActivity extends AppCompatActivity {
	
	private PreferenceHelper mPreferenceHelper;
	private Gson gson;
	ImageView imgProfilePic;
	private Button btnSaveMemberInfo;
	private String mAutocompleteText;
	private GooglePlacesApiAdapter mPlacesApiAdapter;
	
	private DatePickerDialog birthDatePickerDialog,birthDatePickerDialog2,birthDatePickerDialog3,birthDatePickerDialog4;
	private SimpleDateFormat dateFormatter,dateFormatterService,dateFormatter01;
	
	private TextView txtMemberDateOfBirth,txtempInfo,txtDateofJoining;
//	private TextView txtBaptisedWhen, txtdueDate;
//	AutoCompleteTextView txtLandmarkforOfficeAddress,txtLandmarkforHomeAddress;
//	EditText txtIntroducedBy, txtBaptisedWhere, txtTaskDescription, txtNextActionPlan, txtYookosID, txtDesignation;
	private EditText txtMemberName,txtMemberSurname,txtMemberPhone1,txtMemberPhone2,txtEmailID1,txtEmailID2,
	txtMemberHomeAddress,txtOfficeAddress,txtShortbio,
	txtYearlyIncome,txtCoreCompeteance;
	
//	private Spinner spnEducationalQualification,spnExperienceYears,spnIndustrySegment,spnEmploymentStatus,
	private Spinner spnMemberMartialInfo;
	
	
	private ArrayList<String> mZoneList,mRegionList,mChurchList,mSeniorCellList,mGrpChurchList,mPCFList,mCellList;
	private ArrayList<String> educationQualList,expYearsList,industrySegList,empStatusList,martialInfoList,IncomeSegmentList;
	private ArrayList<String> ModeofIntroduceList,txtisnewconvertList;
	ArrayAdapter<String> adapterIndSeg,adapterQual,adapterExp,adapterEmpStatus,adapterMartial,adaptertxttitle,adaptertxtGender,adapterbaptismStstus,adapterholyghost,
	adapterBornAgain,adaptershcoolstatus,adapterageGroup,adapterModeofIntroduce,adapterIncomeSegment,adaptertxtftv_type,adaptertxtisnewconvert;
	
	String str;
	private Spinner txtGender,txtisnewconvert;
	private Spinner txtAgeGroup;
//	private Spinner txttitle, txtModeofIntroduce, txtAgeGroup, txtIncomeSegment, txtBaptismStatus, txtholyghost, txtBornAgain, txtSchoolstatus, txtftv_type,
// spnCellZone, spnCellRegion, spnSeniorCellChurchgroup, spnCellChurch;
	private ArrayList<String> genderList,agegroupList,baptismStatusList,holyghostList,bornAgainList,SchoolstatusList,titleList,txtftv_typeList;
//	TextView lblzone;
//	TextView lblgroupchurch,lblchurch,lblpcf,lblseniorcell,lblcell;
//	private Spinner spnSeniorCell,txtPCF,spnCell;
	String defKey,defVal,defRole;
	EditText txtMembershipNo;
	String seniorcell,churchgroup,church,region,zone,groupchurchname,cell,cellname,pcf;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_first_timer);
	
		getSupportActionBar().hide();

//		lblzone=(TextView) findViewById(R.id.lblzone);
//		lblgroupchurch=(TextView) findViewById(R.id.lblgroupchurch);
//		lblchurch=(TextView) findViewById(R.id.lblchurch);
//		lblpcf=(TextView) findViewById(R.id.lblpcf);
//		lblseniorcell=(TextView) findViewById(R.id.lblseniorcell);
//		lblcell=(TextView) findViewById(R.id.lblcell);

//		spnCell=(Spinner) findViewById(R.id.spnCell);
//		spnSeniorCell=(Spinner) findViewById(R.id.spnSeniorCell);
//		spnCellZone=(Spinner) findViewById(R.id.spnCellZone);
//		spnCellRegion=(Spinner) findViewById(R.id.spnCellRegion);
//		spnSeniorCellChurchgroup=(Spinner) findViewById(R.id.spnSeniorCellChurchgroup);
//		spnCellChurch=(Spinner) findViewById(R.id.spnCellChurch);
//		txtPCF = (Spinner) findViewById(R.id.txtPCFName);

		imgProfilePic=(ImageView) findViewById(R.id.imgProfilePic);

		txtMembershipNo=(EditText) findViewById(R.id.txtMembershipNo);
		txtisnewconvert=(Spinner) findViewById(R.id.txtisnewconvert);
//		txtftv_type=(Spinner) findViewById(R.id.txtftv_type);
		btnSaveMemberInfo=(Button) findViewById(R.id.btnSaveMemberInfo);
//		txtIntroducedBy=(EditText) findViewById(R.id.txtIntroducedBy);
//		txtdueDate=(TextView) findViewById(R.id.txtdueDate);
//		txttitle=(Spinner) findViewById(R.id.txttitle);
		txtempInfo=(TextView) findViewById(R.id.empInfo);
//		txtModeofIntroduce=(Spinner) findViewById(R.id.txtModeofIntroduce);
//		txtTaskDescription=(EditText) findViewById(R.id.txtTaskDescription);
//		txtNextActionPlan=(EditText) findViewById(R.id.txtNextActionPlan);

//		txtIncomeSegment=(Spinner) findViewById(R.id.txtIncomeSegment);
		txtGender=(Spinner) findViewById(R.id.txtGender);
		txtAgeGroup=(Spinner) findViewById(R.id.txtAgeGroup);
//		txtBaptismStatus=(Spinner) findViewById(R.id.txtBaptismStatus);
//		txtholyghost=(Spinner) findViewById(R.id.txtholyghost);
//		txtBornAgain=(Spinner) findViewById(R.id.txtBornAgain);
//		txtSchoolstatus=(Spinner) findViewById(R.id.txtSchoolstatus);

//		txtLandmarkforHomeAddress=(AutoCompleteTextView) findViewById(R.id.txtLandmarkforHomeAddress);
//		txtLandmarkforOfficeAddress=(AutoCompleteTextView) findViewById(R.id.txtLandmarkforOfficeAddress);
//		txtBaptisedWhen=(TextView) findViewById(R.id.txtBaptisedWhen);
		txtDateofJoining=(TextView) findViewById(R.id.txtDateofJoining);
//		txtYookosID=(EditText) findViewById(R.id.txtYookosID);
//		txtBaptisedWhere=(EditText) findViewById(R.id.txtBaptisedWhere);




//		txtDesignation=(EditText) findViewById(R.id.txtDesignation);
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
//		spnEmploymentStatus=(Spinner) findViewById(R.id.txtEmploymentStatus);
//		spnIndustrySegment=(Spinner) findViewById(R.id.txtIndustrySegment);
		txtYearlyIncome=(EditText) findViewById(R.id.txtYearlyIncome);
//		spnExperienceYears=(Spinner) findViewById(R.id.txtExperienceYears);
		txtCoreCompeteance=(EditText) findViewById(R.id.txtCoreCompeteance);
//		spnEducationalQualification=(Spinner) findViewById(R.id.txtEducationalQualification);
		txtempInfo=(TextView) findViewById(R.id.empInfo);
		
		mPreferenceHelper=new PreferenceHelper(this);
		
		 str=mPreferenceHelper.getString(Commons.USER_ROLE);
		    defKey=mPreferenceHelper.getString(Commons.USER_DEFKEY);
			defVal=mPreferenceHelper.getString(Commons.USER_DEFVALUE);
			defRole=mPreferenceHelper.getString(Commons.USER_ROLE);
			gson=new Gson();
		
		txtisnewconvertList=new ArrayList<String>();
		txtftv_typeList=new ArrayList<String>();
		IncomeSegmentList=new ArrayList<String>();
		ModeofIntroduceList=new ArrayList<String>();
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
		
		
		fillSpinner();
		
		
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
				txtDateofJoining.setText(dateFormatter.format(newDate.getTime()));
			}

		},newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
		
		birthDatePickerDialog4 = new DatePickerDialog(this, new OnDateSetListener() {

			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
//				txtdueDate.setText(dateFormatter.format(newDate.getTime()));
			}

		},newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

		
		mPlacesApiAdapter=new GooglePlacesApiAdapter(this,R.layout.list_item_autocomplete_for_google_places);
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

//		txtempInfo.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				LinearLayout ln1 = (LinearLayout) findViewById(R.id.linear3);
//
//				if (ln1.getVisibility() == View.GONE) {
//					ln1.setVisibility(View.VISIBLE);
//					txtempInfo.setText("- Employment Details");
//					}else{
//					ln1.setVisibility(View.GONE);
//					txtempInfo.setText("+ Employment Details");
//					}
//			}
//		});

//		 if(mPreferenceHelper.getString(Commons.USER_ROLE).equals("PCF Leader")){
//
//
//				lblzone.setVisibility(View.GONE);
//				lblgroupchurch.setVisibility(View.GONE);
//				lblchurch.setVisibility(View.GONE);
//				lblpcf.setVisibility(View.GONE);
//
//
//				spnCellZone.setVisibility(View.VISIBLE);
//				spnCellRegion.setVisibility(View.VISIBLE);
//				spnSeniorCellChurchgroup.setVisibility(View.VISIBLE);
//				spnCellChurch.setVisibility(View.VISIBLE);
//				txtPCF.setVisibility(View.VISIBLE);
//
//
//				spnCellZone.setEnabled(false);
//				spnCellRegion.setEnabled(false);
//				spnSeniorCellChurchgroup.setEnabled(false);
//				spnCellChurch.setEnabled(false);
//
//
//			}

//	      if(mPreferenceHelper.getString(Commons.USER_ROLE).equals("Senior Cell Leader")){
//
//
//	    	  	lblzone.setVisibility(View.GONE);
//				lblgroupchurch.setVisibility(View.GONE);
//				lblchurch.setVisibility(View.GONE);
//				lblpcf.setVisibility(View.GONE);
//				lblseniorcell.setVisibility(View.GONE);
//
//				spnCellZone.setVisibility(View.VISIBLE);
//				spnCellRegion.setVisibility(View.VISIBLE);
//				spnSeniorCellChurchgroup.setVisibility(View.VISIBLE);
//				spnCellChurch.setVisibility(View.VISIBLE);
//				txtPCF.setVisibility(View.VISIBLE);
//				spnSeniorCell.setVisibility(View.VISIBLE);
//
//				spnCellZone.setEnabled(false);
//				spnCellRegion.setEnabled(false);
//				spnSeniorCellChurchgroup.setEnabled(false);
//				spnCellChurch.setEnabled(false);
//				txtPCF.setEnabled(false);
//
//			}
//
//	      if(mPreferenceHelper.getString(Commons.USER_ROLE).equals("Cell Leader")){
//
//
//	    	  	lblzone.setVisibility(View.GONE);
//				lblgroupchurch.setVisibility(View.GONE);
//				lblchurch.setVisibility(View.GONE);
//				lblpcf.setVisibility(View.GONE);
//				lblseniorcell.setVisibility(View.GONE);
//				lblcell.setVisibility(View.GONE);
//
//				spnCellZone.setVisibility(View.VISIBLE);
//				spnCellRegion.setVisibility(View.VISIBLE);
//				spnSeniorCellChurchgroup.setVisibility(View.VISIBLE);
//				spnCellChurch.setVisibility(View.VISIBLE);
//				txtPCF.setVisibility(View.VISIBLE);
//				spnSeniorCell.setVisibility(View.VISIBLE);
//				spnCell.setVisibility(View.VISIBLE);
//
//				spnCellZone.setEnabled(false);
//				spnCellRegion.setEnabled(false);
//				spnSeniorCellChurchgroup.setEnabled(false);
//				spnCellChurch.setEnabled(false);
//				txtPCF.setEnabled(false);
//				spnSeniorCell.setEnabled(false);
//
//			}


	      //01-27 16:01:11.358: E/(4291): Role--Cell Leader


//	      if(mPreferenceHelper.getString(Commons.USER_ROLE).equals("Church Pastor")){
//
//	    		lblzone.setVisibility(View.GONE);
//				lblgroupchurch.setVisibility(View.GONE);
//				lblchurch.setVisibility(View.GONE);
//
//
//				spnCellZone.setVisibility(View.VISIBLE);
//				spnCellRegion.setVisibility(View.VISIBLE);
//				spnSeniorCellChurchgroup.setVisibility(View.VISIBLE);
//				spnCellChurch.setVisibility(View.VISIBLE);
//				spnCellZone.setEnabled(false);
//				spnCellRegion.setEnabled(false);
//				spnSeniorCellChurchgroup.setEnabled(false);
//
//
//			}
//
//	      if(mPreferenceHelper.getString(Commons.USER_ROLE).equals("Group Church Pastor")){
//
//
//	    		lblzone.setVisibility(View.GONE);
//				lblgroupchurch.setVisibility(View.GONE);
//
//				spnCellZone.setVisibility(View.VISIBLE);
//				spnCellRegion.setVisibility(View.VISIBLE);
//				spnSeniorCellChurchgroup.setVisibility(View.VISIBLE);
//
//
//				spnCellZone.setEnabled(false);
//				spnCellRegion.setEnabled(false);
//				spnSeniorCellChurchgroup.setEnabled(false);
//
//
//			}
//
//	      if(mPreferenceHelper.getString(Commons.USER_ROLE).equals("Zonal Pastor")){
//
//
//	    		lblzone.setVisibility(View.GONE);
//
//				spnCellZone.setVisibility(View.VISIBLE);
//				spnCellRegion.setEnabled(false);
//
//			}
//
//	      lblzone.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//
//				if(NetworkHelper.isOnline(CreateFirstTimerActivity.this)){
//
//					String selval=spnCellRegion.getSelectedItem().toString();
//
//					Methods.showProgressDialog(CreateFirstTimerActivity.this);
//					getSpinnerData("Regions",selval );
//
//				}else
//				{
//					Methods.longToast("Please connect to Internet",CreateFirstTimerActivity.this);
//				}
//
//
//			}
//		});


//	      lblgroupchurch.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//
//				String selval="";
//
//				if(NetworkHelper.isOnline(CreateFirstTimerActivity.this)){
//
//					if(lblzone.getVisibility()==View.VISIBLE){
//
//
//						Methods.smallToast("Please Select Zone", CreateFirstTimerActivity.this);
//
//
//
//					}else{
//
//						selval=spnCellZone.getSelectedItem().toString();
//						Methods.showProgressDialog(CreateFirstTimerActivity.this);
//						getSpinnerData("Zones",selval );
//
//					}
//
//
//				}else
//				{
//					Methods.longToast("Please connect to Internet",CreateFirstTimerActivity.this);
//				}
//
//
//			}
//		});
//
//	      lblchurch.setOnClickListener(new OnClickListener() {
//
//	  		@Override
//	  		public void onClick(View v) {
//	  			// TODO Auto-generated method stub
//
//	  			String selval="";
//
//	  			if(NetworkHelper.isOnline(CreateFirstTimerActivity.this)){
//
//	  				if(lblgroupchurch.getVisibility()==View.VISIBLE){
//
//						Methods.smallToast("Please Select Group Church", CreateFirstTimerActivity.this);
//
//					}else{
//
////						selval=spnSeniorCellChurchgroup.getSelectedItem().toString();
//						Methods.showProgressDialog(CreateFirstTimerActivity.this);
//						getSpinnerData("Group Churches",selval );
//
//					}
//	  			}else
//	  			{
//	  				Methods.longToast("Please connect to Internet",CreateFirstTimerActivity.this);
//	  			}
//
//
//	  		}
//	  	});

//	      lblpcf.setOnClickListener(new OnClickListener() {
//
//	    		@Override
//	    		public void onClick(View v) {
//	    			// TODO Auto-generated method stub
//
//	    			String selval="";
//
//	    			if(NetworkHelper.isOnline(CreateFirstTimerActivity.this)){
//
//	    				if(lblchurch.getVisibility()==View.VISIBLE){
//
//	    					Methods.smallToast("Please Select Church", CreateFirstTimerActivity.this);
//
//	    				}else{
//
//	    					selval=spnCellChurch.getSelectedItem().toString();
//	    					Methods.showProgressDialog(CreateFirstTimerActivity.this);
//	    					getSpinnerData("Churches",selval );
//	    				}
//
//	    			}else
//	    			{
//	    				Methods.longToast("Please connect to Internet",CreateFirstTimerActivity.this);
//	    			}
//
//
//	    		}
//	    	});

//	      lblseniorcell.setOnClickListener(new OnClickListener() {
//
//	  		@Override
//	  		public void onClick(View v) {
//	  			// TODO Auto-generated method stub
//
//	  			if(NetworkHelper.isOnline(CreateFirstTimerActivity.this)){
//
//	  				if(lblpcf.getVisibility()==View.VISIBLE){
//
//						Methods.smallToast("Please Select PCF", CreateFirstTimerActivity.this);
//
//					}else{
//
//						String selval=txtPCF.getSelectedItem().toString();
//						Methods.showProgressDialog(CreateFirstTimerActivity.this);
//						getSpinnerData("PCFs",selval );
//					}
//
//	  			}else
//	  			{
//	  				Methods.longToast("Please connect to Internet",CreateFirstTimerActivity.this);
//	  			}
//
//
//	  		}
//	  	});

//	      lblcell.setOnClickListener(new OnClickListener() {
//
//		  		@Override
//		  		public void onClick(View v) {
//		  			// TODO Auto-generated method stub
//
//		  			if(NetworkHelper.isOnline(CreateFirstTimerActivity.this)){
//
//		  				if(lblseniorcell.getVisibility()==View.VISIBLE){
//
//							Methods.smallToast("Please Select Seniorcell", CreateFirstTimerActivity.this);
//
//						}else{
//
//							String selval=spnSeniorCell.getSelectedItem().toString();
//							Methods.showProgressDialog(CreateFirstTimerActivity.this);
//							getSpinnerData("Senior Cells",selval );
//						}
//
//		  			}else
//		  			{
//		  				Methods.longToast("Please connect to Internet",CreateFirstTimerActivity.this);
//		  			}
//
//
//		  		}
//		  	});


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
	      
	      
	      btnSaveMemberInfo.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					//createNewMember();
					if(validateField()){
						if(NetworkHelper.isOnline(CreateFirstTimerActivity.this)){
							if(isValid()) {
								Methods.showProgressDialog(CreateFirstTimerActivity.this);
								createFtv();
							}
						} else {
							Methods.longToast("Please connect to Internet", CreateFirstTimerActivity.this);
						}
					}
				}
			});
	      
	      txtDateofJoining.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					birthDatePickerDialog3.show();
				}
			});


//			txtBaptisedWhen.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					birthDatePickerDialog2.show();
//
//				}
//			});

			
			txtMemberDateOfBirth.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					birthDatePickerDialog.show();
					
				}
			});

//			txtdueDate.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					birthDatePickerDialog4.show();
//
//				}
//			});
	}

	public boolean isValid() {
        if(!InputValidation.hasText(txtMemberName)) {
			AlertDialog dialog = new AlertDialog.Builder(CreateFirstTimerActivity.this)
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
        if(!InputValidation.spnHasText(txtGender, "Gender")) {
			AlertDialog dialog = new AlertDialog.Builder(CreateFirstTimerActivity.this)
                    .setCancelable(false)
                    .setTitle("Invalid Input")
                    .setMessage("Please enter Gender")
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
        if(!InputValidation.spnHasText(txtAgeGroup, "AgeGroup")) {
			AlertDialog dialog = new AlertDialog.Builder(CreateFirstTimerActivity.this)
                    .setCancelable(false)
                    .setTitle("Invalid Input")
                    .setMessage("Please enter Age Group")
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
		if(!InputValidation.isPhoneNumber(txtMemberPhone1, false)) {
			AlertDialog dialog =new AlertDialog.Builder(CreateFirstTimerActivity.this)
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
		if(!InputValidation.isPhoneNumber(txtMemberPhone2, false)) {
			AlertDialog dialog =new AlertDialog.Builder(CreateFirstTimerActivity.this)
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
		return true;
	}

	public void fillSpinner(){
		
		
		txtisnewconvertList.add("Yes");
		txtisnewconvertList.add("No");
		
		adaptertxtisnewconvert=new ArrayAdapter<String>(CreateFirstTimerActivity.this, android.R.layout.simple_spinner_item, txtisnewconvertList);
		adaptertxtisnewconvert.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		txtisnewconvert.setAdapter(adaptertxtisnewconvert);
		
		
		
		
		txtftv_typeList.add("Church");
		txtftv_typeList.add("Cell");
		
		adaptertxtftv_type=new ArrayAdapter<String>(CreateFirstTimerActivity.this, android.R.layout.simple_spinner_item, txtftv_typeList);
		adaptertxtftv_type.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		txtftv_type.setAdapter(adaptertxtftv_type);
		
		IncomeSegmentList.add("Lower Income");
		IncomeSegmentList.add("Middle Income");
		IncomeSegmentList.add("Upper Income");
		
		adapterIncomeSegment=new ArrayAdapter<String>(CreateFirstTimerActivity.this, android.R.layout.simple_spinner_item, IncomeSegmentList);
		adapterIncomeSegment.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		txtIncomeSegment.setAdapter(adapterIncomeSegment);
		
		
		ModeofIntroduceList.add("Reach Out Nigeria Database");
		ModeofIntroduceList.add("First Timer and New Converts ");
		ModeofIntroduceList.add("Database");
		ModeofIntroduceList.add("Super Sunday (August 2013) ");
		ModeofIntroduceList.add("Invitation Database");
		ModeofIntroduceList.add("Invited by a Member for");
		ModeofIntroduceList.add("Service");
		ModeofIntroduceList.add("Received an SMS");
		ModeofIntroduceList.add("Received a Phone Call");
		ModeofIntroduceList.add("Saw our Program of TV");
		ModeofIntroduceList.add("Relocated from another Christ");
		ModeofIntroduceList.add("Embassy Church");
		ModeofIntroduceList.add("Walked In");
		ModeofIntroduceList.add("Received an Email from Us");
		
		adapterModeofIntroduce = new ArrayAdapter<String>(CreateFirstTimerActivity.this, android.R.layout.simple_spinner_item, ModeofIntroduceList);
		adapterModeofIntroduce.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		txtModeofIntroduce.setAdapter(adapterModeofIntroduce);
		
		
		titleList.add("Bro");
		titleList.add("Sis");
		titleList.add("Dcn");
		titleList.add("Dcns");
		titleList.add("Pastor");
		
//		adaptertxttitle = new ArrayAdapter<String>(CreateFirstTimerActivity.this, android.R.layout.simple_spinner_item, titleList);
//		adaptertxttitle.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		txttitle.setAdapter(adaptertxttitle);
		
		genderList.add("Male");
		genderList.add("Female");
		
		ArrayAdapter<String> adaptertxtGender = new ArrayAdapter<String>(CreateFirstTimerActivity.this, android.R.layout.simple_spinner_item, genderList);
		adaptertxtGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		txtGender.setAdapter(adaptertxtGender);

		agegroupList.add("0 - 12 Years");
		agegroupList.add("13 - 19 Years");
		agegroupList.add("20 - 25 Years");
		agegroupList.add("26 - 35 Years");
		agegroupList.add("35 - 45 Years");
		agegroupList.add("45 - 55 Years");
		agegroupList.add("56 - 65 Years");
		agegroupList.add("65 Years and Above");
		
		adapterageGroup = new ArrayAdapter<String>(CreateFirstTimerActivity.this, android.R.layout.simple_spinner_item, agegroupList);
		adapterageGroup.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		txtAgeGroup.setAdapter(adapterageGroup);
		
		baptismStatusList.add("Yes");
		baptismStatusList.add("No");
		baptismStatusList.add("Attended Classes but yet to be Baptised");
		
//		adapterbaptismStstus = new ArrayAdapter<String>(CreateFirstTimerActivity.this, android.R.layout.simple_spinner_item, baptismStatusList);
//		adapterbaptismStstus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		txtBaptismStatus.setAdapter(adapterbaptismStstus);
		
		holyghostList.add("Yes");
		holyghostList.add("No");
		
//		adapterholyghost = new ArrayAdapter<String>(CreateFirstTimerActivity.this, android.R.layout.simple_spinner_item, holyghostList);
//		adapterholyghost.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		txtholyghost.setAdapter(adapterholyghost);
		
		bornAgainList.add("Yes");
		bornAgainList.add("No");
		
//		adapterBornAgain = new ArrayAdapter<String>(CreateFirstTimerActivity.this, android.R.layout.simple_spinner_item, bornAgainList);
//		adapterBornAgain.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		txtBornAgain.setAdapter(adapterBornAgain);
		
		
		SchoolstatusList.add("Nil");
		SchoolstatusList.add("Completed Class 1");
		SchoolstatusList.add("Completed Class 1&2");
		SchoolstatusList.add("Completed Class 1, 2 & 3");
		SchoolstatusList.add("Completed Class 1, 2 , 3 & 4");
		SchoolstatusList.add("Completed Class 1, 2 , 3 , 4 & 5");
		SchoolstatusList.add("Completed Class 1, 2 , 3 , 4 , 5 & 6");
		SchoolstatusList.add("Completed All Classes and Passed Exam");
		
//		adaptershcoolstatus = new ArrayAdapter<String>(CreateFirstTimerActivity.this, android.R.layout.simple_spinner_item, SchoolstatusList);
//		adaptershcoolstatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		txtSchoolstatus.setAdapter(adaptershcoolstatus);
		
		martialInfoList.add("");
		martialInfoList.add("Divorced");
		martialInfoList.add("Married");
		martialInfoList.add("Single");
		martialInfoList.add("Widowed");
		
		adapterMartial = new ArrayAdapter<String>(CreateFirstTimerActivity.this, android.R.layout.simple_spinner_item, martialInfoList);
		adapterMartial.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnMemberMartialInfo.setAdapter(adapterMartial);
		
		
		
		
	
		educationQualList.add("");
		educationQualList.add("None");
		educationQualList.add("City and Guilds");
		educationQualList.add("SSCE School Certificate");
		educationQualList.add("OND");
		educationQualList.add("HND");
		educationQualList.add("First Degree (BSC, BA)");
		educationQualList.add("Second Degree (MSC MBA)");
		educationQualList.add("Doctorate");
		
//		adapterQual = new ArrayAdapter<String>(CreateFirstTimerActivity.this, android.R.layout.simple_spinner_item, educationQualList);
//		adapterQual.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		spnEducationalQualification.setAdapter(adapterQual);

		expYearsList.add("");
		//expYearsList.add("");
		expYearsList.add("1 - 3 Years");
		expYearsList.add("3 - 6 Years");
		expYearsList.add("6 - 10 Years");
		expYearsList.add("10+ Years");
		
		
//		adapterExp = new ArrayAdapter<String>(CreateFirstTimerActivity.this, android.R.layout.simple_spinner_item, expYearsList);
//		adapterExp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		spnExperienceYears.setAdapter(adapterExp);
//
//		spnExperienceYears.setSelection(0);
		
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

//		adapterIndSeg = new ArrayAdapter<String>(CreateFirstTimerActivity.this, android.R.layout.simple_spinner_item, industrySegList);
//		adapterIndSeg.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		spnIndustrySegment.setAdapter(adapterIndSeg);

		
		empStatusList.add("");
		empStatusList.add("Business Man");
		empStatusList.add("Government Worker");
		empStatusList.add("Unemployed");
		empStatusList.add("Professional");
		empStatusList.add("Petty Trade");
		empStatusList.add("Student");		
		empStatusList.add("Trader");
		empStatusList.add("Others");
		
//		adapterEmpStatus = new ArrayAdapter<String>(CreateFirstTimerActivity.this, android.R.layout.simple_spinner_item, empStatusList);
//		adapterEmpStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		spnEmploymentStatus.setAdapter(adapterEmpStatus);
		
		
	}
	
	public boolean validateField(){

		return true;
//		if(!txtEmailID1.getText().toString().equals("")){
//			return true;
//		}else{
//			Toast.makeText(CreateFirstTimerActivity.this, "Please Enter EmailId", Toast.LENGTH_LONG).show();
//			return false;
//		}
//		if(txtMemberName.getText().toString().trim().length()>0){
//			if(lblzone.getVisibility()!=View.VISIBLE){
//					if(lblgroupchurch.getVisibility()!=View.VISIBLE){
//						if(lblchurch.getVisibility()!=View.VISIBLE){
//							if(!txtEmailID1.getText().toString().equals("")){
//								return true;
//							}else{
//								Toast.makeText(CreateFirstTimerActivity.this, "Please Enter EmailId", Toast.LENGTH_LONG).show();
//								return false;
//							}
//
//						}
//						else
//						{
//							Toast.makeText(CreateFirstTimerActivity.this, "Please select Church", Toast.LENGTH_LONG).show();
//							return false;
//						}
//
//					}
//					else
//					{
//						Toast.makeText(CreateFirstTimerActivity.this, "Please select Group Church", Toast.LENGTH_LONG).show();
//						return false;
//					}
//
//			}else{
//				Toast.makeText(CreateFirstTimerActivity.this, "Please select Zone", Toast.LENGTH_LONG).show();
//				return false;
//			}
//		}
//	else{
//			Toast.makeText(CreateFirstTimerActivity.this, "Please Enter Name", Toast.LENGTH_LONG).show();
//			return false;
//		}
		
	}
	//Firstname,gaegroup,church,grchurch,zone,resion.emailaddress,
	private void createFtv() {
		StringRequest reqGetProfile=new StringRequest(Method.POST,create_ftv.SERVICE_URL,new Listener<String>() {

			@Override
			public void onResponse(String response) {
				Methods.closeProgressDialog();
				Log.d("Nonstop","Response: " + response);
				
				Log.e("responce droid","get reqGetProfile ---------------"+ response);
			
				try {
					
					JSONObject jsonobj=new JSONObject(response);
					
					if(response.contains("status")){
					
						
						String status=jsonobj.getString("status");
						if(status.equals("401")){
							Methods.longToast("User name or Password is incorrect", CreateFirstTimerActivity.this);
						}else{
							Methods.longToast(jsonobj.getString("message"), CreateFirstTimerActivity.this);
						}
						
					
				}else{
					
					String msg=jsonobj.getString("message");
					Methods.longToast(msg,CreateFirstTimerActivity.this);
					
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
				Log.d("droid","get reqsaveMeeting error---------------"+ error.getCause());

				if(error!=null) {
					if(error.networkResponse.statusCode==403){
						Methods.longToast("No access to update Profile", CreateFirstTimerActivity.this);
					}
					else
						Methods.longToast("Some Error Occured,please try again later", CreateFirstTimerActivity.this);
				} else
					Methods.longToast("Some Error Occured,please try again later", CreateFirstTimerActivity.this);
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

//						obj.put("mode_of_introduce", txtModeofIntroduce.getSelectedItem().toString());
//
//						obj.put("introduced_by",txtIntroducedBy.getText().toString());
//
//						obj.put("office_landmark", txtLandmarkforOfficeAddress.getText().toString());
					
						obj.put("surname",txtMemberSurname.getText().toString());

//						obj.put("yearly_income",txtIncomeSegment.getSelectedItem().toString());
//
//						obj.put("task_description", txtTaskDescription.getText().toString());

						obj.put("zone", zone);
						obj.put("group_church_name", groupchurchname);
						obj.put("cell_name", cellname);
						obj.put("email_id", txtEmailID1.getText().toString());
//						obj.put("core_competeance", txtCoreCompeteance.getText().toString());
						obj.put("email_id2", txtEmailID2.getText().toString());
						//obj.put("zone_name", "zone_name");
//						obj.put("employment_status", spnEmploymentStatus.getSelectedItem().toString());
					
						obj.put("cell", mPreferenceHelper.getString(Commons.USER_DEFVALUE));
						obj.put("pcf", pcf);
						
						obj.put("address", txtMemberHomeAddress.getText().toString());

//						obj.put("address_manual", txtLandmarkforHomeAddress.getText().toString());
						obj.put("senior_cell", seniorcell);
						obj.put("church", church);
//						obj.put("title", txttitle.getSelectedItem().toString());
						obj.put("office_address", txtOfficeAddress.getText().toString());
						obj.put("phone_1", txtMemberPhone1.getText().toString());
						obj.put("region",region);

//						obj.put("baptism_where", txtBaptisedWhere.getText().toString());



//						obj.put("education_qualification", spnEducationalQualification.getSelectedItem().toString());
//
						obj.put("age_group", txtAgeGroup.getSelectedItem().toString());
//						obj.put("baptisum_status", txtBaptismStatus.getSelectedItem().toString());
					
						obj.put("ftv_name", txtMemberName.getText().toString());
					
						obj.put("sex", txtGender.getSelectedItem().toString());
						
						
						
						obj.put("phone_2", txtMemberPhone2.getText().toString());
//						obj.put("experience_years",spnExperienceYears.getSelectedItem().toString() );

						//obj.put("member_designation", txtDesignation.getText().toString());
						//obj.put("pcf_name", "pcf_name");

//						obj.put("school_status", txtSchoolstatus.getSelectedItem().toString());
//						obj.put("industry_segment",spnIndustrySegment.getSelectedItem().toString());
						//obj.put("church_name", value)
//						obj.put("filled_with_holy_ghost",txtholyghost.getSelectedItem().toString());
						//obj.put("region_name", "region_name");
//						obj.put("is_new_born", txtBornAgain.getSelectedItem().toString());
						//obj.put("senior_cell_name", value)

						obj.put("first_contact_by", "");
						
						if(!txtMemberDateOfBirth.getText().equals("")){
							String dob=dateFormatter01.format(dateFormatter.parse(txtMemberDateOfBirth.getText().toString()));
							obj.put("date_of_birth", dob);
						}else{
							obj.put("date_of_birth", "");
						}
						
						String doj="";
						if(!txtDateofJoining.getText().equals("")){
							 doj=dateFormatter01.format(dateFormatter.parse(txtDateofJoining.getText().toString()));
							obj.put("date_of_visit",doj);
						}else{
							obj.put("date_of_visit","");
							
						}
						
//						if(!txtdueDate.getText().equals("")){
//						 doj=dateFormatter01.format(dateFormatter.parse(txtdueDate.getText().toString()));
//						 obj.put("due_date",doj);
//						}else{
//							obj.put("due_date","");
//						}
						
						
//						if(!txtBaptisedWhen.getText().equals("")){
//						String dobtism=dateFormatter01.format(dateFormatter.parse(txtBaptisedWhen.getText().toString()));
//						obj.put("baptism_when", dobtism);
//						}else{
//							obj.put("baptism_when", "");
//						}
						
						obj.put("marital_info",spnMemberMartialInfo.getSelectedItem().toString());
//
//						obj.put("yokoo_id", txtYookosID.getText().toString());
						
						obj.put("church_group", churchgroup);
						
//						obj.put("ftv_type", txtftv_type.getSelectedItem().toString());
						
						obj.put("is_new_convert", txtisnewconvert.getSelectedItem().toString());
						
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				
				String dataString=obj.toString();//gson.toJson(model, MeetingListRequestModel.class);

				Log.e("Request droid", dataString);
				params.put(create_ftv.DATA, dataString);
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
							Methods.longToast("User name or Password is incorrect", CreateFirstTimerActivity.this);
					
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
								 
//									lblzone.setVisibility(View.GONE);
//								    spnCellZone.setVisibility(View.VISIBLE);
								}else{
									Log.e("condistion match"," not condistion match");
								}
									
								
//								if(tbl.equalsIgnoreCase("Zones")){
//									lblgroupchurch.setVisibility(View.GONE);
//									spnSeniorCellChurchgroup.setVisibility(View.VISIBLE);
//								}
//
//								if(tbl.equalsIgnoreCase("Group Churches")){
//									lblchurch.setVisibility(View.GONE);
//									spnCellChurch.setVisibility(View.VISIBLE);
//								}
//
//								if(tbl.equalsIgnoreCase("Churches")){
//									lblpcf.setVisibility(View.GONE);
//									txtPCF.setVisibility(View.VISIBLE);
//								}
//
//								if(tbl.equalsIgnoreCase("PCFs")){
//									lblseniorcell.setVisibility(View.GONE);
//									spnSeniorCell.setVisibility(View.VISIBLE);
//								}
//
//								if(tbl.equalsIgnoreCase("Senior Cells")){
//									lblcell.setVisibility(View.GONE);
//									spnCell.setVisibility(View.VISIBLE);
//								}
								
								
							}else{
								Methods.longToast("Resord Not Found", CreateFirstTimerActivity.this);
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
	
//	MasterSpinnerAdpter adapterZone=new MasterSpinnerAdpter(CreateNewMemberActivity.this,mZoneList);
//	spnCellZone.setAdapter(adapterZone);
//
//	MasterSpinnerAdpter adapterRegion=new MasterSpinnerAdpter(CreateNewMemberActivity.this,mRegionList);
//	spnCellRegion.setAdapter(adapterRegion);
//
//	MasterSpinnerAdpter adapterChurch=new MasterSpinnerAdpter(CreateNewMemberActivity.this,mChurchList);
//	spnCellChurch.setAdapter(adapterChurch);
//
//	MasterSpinnerAdpter adapterSrCell=new MasterSpinnerAdpter(CreateNewMemberActivity.this,mSeniorCellList);
//	spnSeniorCell.setAdapter(adapterSrCell);
//
//	MasterSpinnerAdpter adapterchurchgropu=new MasterSpinnerAdpter(CreateNewMemberActivity.this,mGrpChurchList);
//	spnSeniorCellChurchgroup.setAdapter(adapterchurchgropu);
//
//	MasterSpinnerAdpter adapterPCF=new MasterSpinnerAdpter(CreateNewMemberActivity.this,mPCFList);
//	txtPCF.setAdapter(adapterPCF);
	
	
	
	
//	ArrayAdapter<String> adapterZone = new ArrayAdapter<String>(CreateFirstTimerActivity.this, android.R.layout.simple_spinner_item, mZoneList);
//	adapterZone.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//	spnCellZone.setAdapter(adapterZone);
//
//	ArrayAdapter<String> adapterRegion = new ArrayAdapter<String>(CreateFirstTimerActivity.this, android.R.layout.simple_spinner_item, mRegionList);
//	adapterRegion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//	spnCellRegion.setAdapter(adapterRegion);
//
//	ArrayAdapter<String> adapterChurch = new ArrayAdapter<String>(CreateFirstTimerActivity.this, android.R.layout.simple_spinner_item, mChurchList);
//	adapterChurch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//	spnCellChurch.setAdapter(adapterChurch);
//
//	ArrayAdapter<String> adapterSrCell = new ArrayAdapter<String>(CreateFirstTimerActivity.this, android.R.layout.simple_spinner_item, mSeniorCellList);
//	adapterSrCell.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//	spnSeniorCell.setAdapter(adapterSrCell);
//
//	ArrayAdapter<String> adapterchurchgropu = new ArrayAdapter<String>(CreateFirstTimerActivity.this, android.R.layout.simple_spinner_item, mGrpChurchList);
//	adapterSrCell.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//	spnSeniorCellChurchgroup.setAdapter(adapterchurchgropu);
//
//	ArrayAdapter<String> adapterPCF = new ArrayAdapter<String>(CreateFirstTimerActivity.this, android.R.layout.simple_spinner_item, mPCFList);
//	adapterPCF.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//	txtPCF.setAdapter(adapterPCF);
//
//	ArrayAdapter<String> adapterCell = new ArrayAdapter<String>(CreateFirstTimerActivity.this, android.R.layout.simple_spinner_item, mCellList);
//	adapterCell.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//	spnCell.setAdapter(adapterCell);
	
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
					Methods.longToast("User name or Password is incorrect", CreateFirstTimerActivity.this);
				}
			}else{
				if(null !=mHHModel.getMessage() && mHHModel.getMessage().size() >0){
					ArrayList<HHSubModel> mHHSubModel=new ArrayList<HHSubModel>();
					//if(meetingmsg instanceof JSONArray){
					mHHSubModel=mHHModel.getMessage();

					for(int i=0;i<mHHSubModel.size();i++){			
						if(null !=mHHSubModel.get(i).getZone())
							mZoneList.add(mHHSubModel.get(i).getZone());
						      zone=mHHSubModel.get(i).getZone();
						if(null !=mHHSubModel.get(i).getRegion())
							mRegionList.add(mHHSubModel.get(i).getRegion());
						   region=mHHSubModel.get(i).getRegion();
						if(null !=mHHSubModel.get(i).getChurch())
							mChurchList.add(mHHSubModel.get(i).getChurch());
						     church=mHHSubModel.get(i).getChurch();
						if(null !=mHHSubModel.get(i).getChurch_group())
							mGrpChurchList.add(mHHSubModel.get(i).getChurch_group());
						    churchgroup=mHHSubModel.get(i).getChurch_group();
						if(null !=mHHSubModel.get(i).getPcf())
							mPCFList.add(mHHSubModel.get(i).getPcf());
						    pcf=mHHSubModel.get(i).getPcf();
						if(null !=mHHSubModel.get(i).getSenior_cell())
							mSeniorCellList.add(mHHSubModel.get(i).getSenior_cell());
						    seniorcell=mHHSubModel.get(i).getSenior_cell();
						if(null !=mHHSubModel.get(i).getGroup_church_name())
							 groupchurchname= mHHSubModel.get(i).getGroup_church_name();
					/*	if(null !=mHHSubModel.get(i).getCell())
							cell=mHHSubModel.get(i).getCell();
						if(null !=mHHSubModel.get(i).getCell_name())
							cellname=mHHSubModel.get(i).getCell_name();*/
					}
					
				//	setAdapters();

				}else{
					
				}
			}
			Methods.showProgressDialog(CreateFirstTimerActivity.this);
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

			Methods.showProgressDialog(CreateFirstTimerActivity.this);
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
						Methods.longToast("User name or Password is incorrect", CreateFirstTimerActivity.this);
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

							cellname=jsonarray.getJSONObject(i).getString("cell_name");
						}
						
						setAdapters();
						
					}else{
						
						Methods.longToast("Record Not Found ", CreateFirstTimerActivity.this);
						
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
	
}
