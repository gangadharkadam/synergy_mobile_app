package com.mutech.synergy.activities.cellMasters;

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

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.mutech.synergy.App;
import com.mutech.synergy.R;
import com.mutech.synergy.SynergyValues;
import com.mutech.synergy.SynergyValues.Commons;
import com.mutech.synergy.SynergyValues.ImageUrl;
import com.mutech.synergy.SynergyValues.Web.GetMemberProfileService;
import com.mutech.synergy.SynergyValues.Web.ProfilePicUploadService;
import com.mutech.synergy.SynergyValues.Web.ShowMembersDetailsService;
import com.mutech.synergy.activities.profile.MyProfileActivity;
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

import org.json.JSONException;
import org.json.JSONObject;

public class MemberInfoActivtiy extends ActionBarActivity{

	private PreferenceHelper mPreferenceHelper;
	private Gson gson;
	//txtMemberName
	
	private int SELECT_FILE=0,REQUEST_CAMERA=1;
	private Bitmap thumbnail;
	private String mAutocompleteText;
	private GooglePlacesApiAdapter mPlacesApiAdapter;
	
	AutoCompleteTextView txtLandmarkforHomeAddress,txtLandmarkforOfficeAddress;
	private EditText txtMemberPhone1,txtMemberPhone2,txtzone,txtEmailID1,txtEmailID2,txtsenior_cell,txtChurch,txtchurch_group,
	txtMemberHomeAddress,txtOfficeAddress,txtregion,txtMemberfName,txtMemberlName,txtShortbio,txtCoreCompeteance,txtpcf,txtcell,txtYookosID;
//	private EditText txtMembershipNo, txtBaptisedWhere, txtPassword, txtregionname, txtzonename, txtchurch_group_name,
// txtChurch_name, txtpcf_name, txtsenior_cell_name, txtcell_name, txtYearlyIncome;
	private TextView txtmemInfo,txtperInfo,txtempInfo, txtBaptismInfo, txtBaptisedWhen, txtBaptisedWhere, txtDateofJoining;
    private TextView txtDesignation;
	private TextView txtMemberDateOfBirth;
	private ImageView imgProfilePic,editEmailId1,editEmailId2;
//	private ImageView editPassword;
	private Button btnSaveMemberInfo;
	private Spinner spnEducationalQualification,spnExperienceYears,spnIndustrySegment,spnEmploymentStatus,txtMemberMartialInfo;
	ArrayAdapter<String> adapterIndSeg,adapterQual,adapterExp,adapterEmpStatus,adapterMartial,adaptershcoolstatus,adapterBornAgain,adapterholyghost,adapterbaptismStstus,adapterageGroup,adapterGender,adapterTitle;
	CheckBox txt_is_eligibale_for_follow_up;
	private Spinner txtGender,txtAgeGroup,txtBaptismStatus,txtholyghost,txtBornAgain,txtSchoolstatus,txttitle;
	private ArrayList<String> educationQualList,expYearsList,industrySegList,empStatusList,martialInfoList;
	ArrayList<ProfileSubModel> mProfSubModel;
	private String memberName;
	private DatePickerDialog birthDatePickerDialog,birthDatePickerDialog2,birthDatePickerDialog3;
	private SimpleDateFormat dateFormatter,dateFormatterService,dateFormatter01;
	private ArrayList<String> genderList,agegroupList,baptismStatusList,holyghostList,bornAgainList,SchoolstatusList,titleList;
	String Imageurl;

    String yearlyIncome, nameJSON, imageName;
    Calendar dob, doj;

	TextView bapWhrTV, bapWhnTV;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_memberprofile2);
		initialize();
	}

	@SuppressLint("NewApi")
	private void initialize() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		getSupportActionBar().setHomeButtonEnabled(false);
		getSupportActionBar().setCustomView(R.layout.custom_actionbar);
		TextView tvTitle=(TextView)getSupportActionBar().getCustomView().findViewById(R.id.title_text);
		tvTitle.setText("Member Info     ");
		
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2E9AFE")));
		
		getSupportActionBar().setDisplayShowCustomEnabled(true);

		memberName=getIntent().getExtras().getString("MemberNo");

		mPreferenceHelper=new PreferenceHelper(this);
		gson=new Gson();
		
		txt_is_eligibale_for_follow_up=(CheckBox) findViewById(R.id.txt_is_eligibale_for_follow_up);
		txtLandmarkforHomeAddress=(AutoCompleteTextView) findViewById(R.id.txtLandmarkforHomeAddress);
		txtLandmarkforOfficeAddress=(AutoCompleteTextView) findViewById(R.id.txtLandmarkforOfficeAddress);
		txtBaptisedWhen=(TextView) findViewById(R.id.txtBaptisedWhen);
		txtBaptisedWhere=(TextView) findViewById(R.id.txtBaptisedWhere);
		txtDateofJoining=(TextView) findViewById(R.id.txtDateofJoining);
		txtYookosID=(EditText) findViewById(R.id.txtYookosID);
//		txtBaptisedWhere=(EditText) findViewById(R.id.txtBaptisedWhere);
		
		txtcell=(EditText)findViewById(R.id.txtcell);
//		txtcell_name=(EditText)findViewById(R.id.txtcell_name);
		
		txtsenior_cell=(EditText)findViewById(R.id.txtsenior_cell);
//		txtsenior_cell_name=(EditText)findViewById(R.id.txtsenior_cell_name);
		
		txtpcf=(EditText)findViewById(R.id.txtpcf);
//		txtpcf_name=(EditText)findViewById(R.id.txtpcf_name);

		txtChurch=(EditText) findViewById(R.id.txtChurch);
//		txtChurch_name=(EditText)findViewById(R.id.txtChurch_name);
		
		txtchurch_group=(EditText) findViewById(R.id.txtchurch_group);
//		txtchurch_group_name=(EditText)findViewById(R.id.txtchurch_group_name);
		
		txtzone=(EditText) findViewById(R.id.txtzone);
//		txtzonename=(EditText)findViewById(R.id.txtzonename);
		
//		txtregionname=(EditText)findViewById(R.id.txtregionname);
		txtregion=(EditText)findViewById(R.id.txtregion);
		
		txtShortbio=(EditText) findViewById(R.id.txtShortbio);
		
		txtMemberfName=(EditText) findViewById(R.id.txtMemberfName);
		txtMemberlName=(EditText) findViewById(R.id.txtMemberlName);
//		txtMembershipNo=(EditText) findViewById(R.id.txtMembershipNo);
	//	txtMemberName=(EditText) findViewById(R.id.txtMemberName);
		txtMemberDateOfBirth=(TextView) findViewById(R.id.txtMemberDateOfBirth);
		txtMemberMartialInfo=(Spinner) findViewById(R.id.txtMemberMartialInfo);
		txtMemberPhone1=(EditText) findViewById(R.id.txtMemberPhone1);
		txtMemberPhone2=(EditText) findViewById(R.id.txtMemberPhone2);
		txtEmailID1=(EditText) findViewById(R.id.txtEmailID1);
		txtEmailID2=(EditText) findViewById(R.id.txtEmailID2);
//		txtPassword=(EditText) findViewById(R.id.txtPassword);
		txtMemberHomeAddress=(EditText) findViewById(R.id.txtMemberHomeAddress);
		txtOfficeAddress=(EditText) findViewById(R.id.txtOfficeAddress);
		spnEmploymentStatus=(Spinner) findViewById(R.id.txtEmploymentStatus);
		spnIndustrySegment=(Spinner) findViewById(R.id.txtIndustrySegment);
//		txtYearlyIncome=(EditText) findViewById(R.id.txtYearlyIncome);
		spnExperienceYears=(Spinner) findViewById(R.id.txtExperienceYears);
		txtCoreCompeteance=(EditText) findViewById(R.id.txtCoreCompeteance);
		spnEducationalQualification=(Spinner) findViewById(R.id.txtEducationalQualification);

		imgProfilePic=(ImageView) findViewById(R.id.imgProfilePic);
		btnSaveMemberInfo=(Button) findViewById(R.id.btnSaveMemberInfo);
		editEmailId1=(ImageView) findViewById(R.id.editEmailId1);
		editEmailId2=(ImageView) findViewById(R.id.editEmailId2);
//		editPassword=(ImageView) findViewById(R.id.editPassword);
		editEmailId1.setVisibility(View.GONE);
		editEmailId2.setVisibility(View.GONE);
//		editPassword.setVisibility(View.GONE);

		txtGender=(Spinner) findViewById(R.id.txtGender);
		txtAgeGroup=(Spinner) findViewById(R.id.txtAgeGroup);
		txtBaptismStatus=(Spinner) findViewById(R.id.txtBaptismStatus);
		txtholyghost=(Spinner) findViewById(R.id.txtholyghost);
		txtBornAgain=(Spinner) findViewById(R.id.txtBornAgain);
		txtSchoolstatus=(Spinner) findViewById(R.id.txtSchoolstatus);
		txttitle=(Spinner) findViewById(R.id.txttitle);

        txtDesignation = (TextView) findViewById(R.id.txtDesignation);

	//	txtmemInfo=(TextView) findViewById(R.id.memInfo);
	//	txtperInfo=(TextView) findViewById(R.id.perInfo);
		txtempInfo=(TextView) findViewById(R.id.empInfo);
		txtBaptismInfo = (TextView) findViewById(R.id.baptismInfo);

		bapWhnTV = (TextView) findViewById(R.id.bapWhnTV);
		bapWhrTV = (TextView) findViewById(R.id.bapWhrTV);
//		bapWhnTV.setEnabled(false);
//		bapWhrTV.setEnabled(false);

		txttitle.setEnabled(true);
		txtMemberfName.setEnabled(true);
		txtMemberlName.setEnabled(true);
		txtShortbio.setEnabled(true);
		txtMemberMartialInfo.setEnabled(true);
		txtGender.setEnabled(true);
		txtAgeGroup.setEnabled(true);
		txtMemberPhone1.setEnabled(true);
		txtMemberPhone2.setEnabled(true);
		txtEmailID2.setEnabled(true);
		txtYookosID.setEnabled(true);
		txtMemberHomeAddress.setEnabled(true);
		txtLandmarkforHomeAddress.setEnabled(true);
		txtOfficeAddress.setEnabled(true);
		txtLandmarkforOfficeAddress.setEnabled(true);
		txtBaptismStatus.setEnabled(true);
		txtSchoolstatus.setEnabled(true);
		txtBornAgain.setEnabled(true);
		txtholyghost.setEnabled(true);
		txtBaptisedWhen.setEnabled(true);
		txtBaptisedWhere.setEnabled(true);
        spnEducationalQualification.setEnabled(true);
        spnEmploymentStatus.setEnabled(true);
        spnExperienceYears.setEnabled(true);
        spnIndustrySegment.setEnabled(true);
        txtMemberDateOfBirth.setEnabled(true);
        txtDateofJoining.setEnabled(true);

        txtEmailID1.setEnabled(false);
        txt_is_eligibale_for_follow_up.setEnabled(false);
        txtDesignation.setEnabled(false);
        txtregion.setEnabled(false);
        txtzone.setEnabled(false);
        txtchurch_group.setEnabled(false);
        txtChurch.setEnabled(false);
        txtpcf.setEnabled(false);
        txtsenior_cell.setEnabled(false);
        txtcell.setEnabled(false);

        btnSaveMemberInfo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(NetworkHelper.isOnline(MemberInfoActivtiy.this)){
					if (isValid()) {
							Methods.showProgressDialog(MemberInfoActivtiy.this);
							updateProfile();

					}
				}
                else
                    Methods.longToast("Please connect to Internet", MemberInfoActivtiy.this);
            }
        });
//        btnSaveMemberInfo.setVisibility(View.GONE);

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
		
		imgProfilePic.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                openDialog();

            }
        });
		
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
		
		
		Calendar newCalendar = Calendar.getInstance();
		dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
		dateFormatterService=new SimpleDateFormat("yyyy-MM-dd",Locale.US);
		
		dateFormatter01=new SimpleDateFormat("yyyy-MM-dd");

        txtMemberDateOfBirth.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                txtMemberDateOfBirth.setText("");
                birthDatePickerDialog.show();
            }
        });

		birthDatePickerDialog = new DatePickerDialog(this, new OnDateSetListener() {

			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				
				Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                dob = newDate;
				Calendar today = Calendar.getInstance();
				if(dob.after(today)) {
					new AlertDialog.Builder(MemberInfoActivtiy.this)
							.setCancelable(false)
							.setTitle("Incorrect Date of Birth")
							.setMessage("Date of Birth can not be a future date!")
							.setPositiveButton("OK", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialogInterface, int i) {

								}
							})
							.show();
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
                    new AlertDialog.Builder(MemberInfoActivtiy.this)
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
                }
			}

		},newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
		
		
    
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
		

		mProfSubModel=new ArrayList<ProfileSubModel>();

		
		
//		txtcell_name.setEnabled(false);

//		txtsenior_cell_name.setEnabled(false);
		
//		txtpcf_name.setEnabled(false);
		
//		txtzonename.setEnabled(false);
		
//		txtregionname.setEnabled(false);
		
		
//		txtchurch_group_name.setEnabled(false);

//		txtChurch_name.setEnabled(false);
		
	/*//	txtMemberName.setEnabled(false);
		txtMemberMartialInfo.setEnabled(false);
		txtMemberDateOfBirth.setEnabled(false);
		txtMemberHomeAddress.setEnabled(false);
		txtMemberPhone1.setEnabled(false);
		txtMemberPhone2.setEnabled(false);
		txtOfficeAddress.setEnabled(false);
		txtCoreCompeteance.setEnabled(false);*/
	//	txtYearlyIncome.setEnabled(false);

		setSpinners();

		if(NetworkHelper.isOnline(this)){
			Methods.showProgressDialog(this);
			getMemberInfo();
		}
		else
			Methods.longToast("Please connect to Internet", this);
	}
	
	private void openDialog() {
		final CharSequence[] items = { "Take Photo","Choose from Gallery",
		"Cancel" };//

		AlertDialog.Builder builder = new AlertDialog.Builder(MemberInfoActivtiy.this);
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
				if(NetworkHelper.isOnline(MemberInfoActivtiy.this)){
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
						
						if(NetworkHelper.isOnline(MemberInfoActivtiy.this)){
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
				
				
				
			}
		}
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
						Methods.longToast("User name or Password is incorrect", MemberInfoActivtiy.this);
					}else{
						Methods.longToast(respModel.getMessage(), MemberInfoActivtiy.this);
					}
				}else{
					Methods.longToast("Profile Updated Succesfully", MemberInfoActivtiy.this);
				}
			//	finish();

			}
		},new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Methods.closeProgressDialog();
				Log.d("droid","get reqsaveMeeting error---------------"+ error.getCause());

				if(error.networkResponse.statusCode==403){
					Methods.longToast("No access to update Profile", MemberInfoActivtiy.this);
				}
				else
					Methods.longToast("Some Error Occured,please try again later", MemberInfoActivtiy.this);
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
				model.setFilename(nameJSON + ".jpg");
				model.setFdata(fdata);
				model.setTbl("Member");//mPreferenceHelper.getString(Commons.USER_DEFKEY)
				model.setName(nameJSON);
//				model.setName(txtMembershipNo.getText().toString());

				String dataString=gson.toJson(model, MeetingListRequestModel.class);

				Log.e("droid", dataString);
				params.put(GetMemberProfileService.DATA, dataString);
				return params;
			}
		};

		App.getInstance().addToRequestQueue(reqSendProfilePic, "reqSendProfilePic");
		reqSendProfilePic.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));

	}
	
	private void setSpinners() {

		educationQualList.add("");
		educationQualList.add("None");
		educationQualList.add("City and Guilds");
		educationQualList.add("SSCE School Certificate");
		educationQualList.add("OND");
		educationQualList.add("HND");
		educationQualList.add("First Degree (BSC, BA)");
		educationQualList.add("Second Degree (MSC MBA)");
		educationQualList.add("Doctorate");

        adapterQual = new ArrayAdapter<String>(MemberInfoActivtiy.this, android.R.layout.simple_spinner_item, educationQualList);

		adapterQual.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnEducationalQualification.setAdapter(adapterQual);

		expYearsList.add("");
		expYearsList.add("1 - 3 Years");
		expYearsList.add("3 - 6 Years");
		expYearsList.add("6 - 10 Years");
		expYearsList.add("10+ Years");

        adapterExp = new ArrayAdapter<String>(MemberInfoActivtiy.this, android.R.layout.simple_spinner_item, expYearsList);

		adapterExp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnExperienceYears.setAdapter(adapterExp);

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

        adapterIndSeg = new ArrayAdapter<String>(MemberInfoActivtiy.this, android.R.layout.simple_spinner_item, industrySegList);

		adapterIndSeg.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnIndustrySegment.setAdapter(adapterIndSeg);

        empStatusList.add("");
        empStatusList.add("Businessman");
        empStatusList.add("Government Worker");
		empStatusList.add("Unemployed");
        empStatusList.add("Professional");
        empStatusList.add("Petty Trade");
        empStatusList.add("Student");
        empStatusList.add("Trader");
		empStatusList.add("Others");


        adapterEmpStatus = new ArrayAdapter<String>(MemberInfoActivtiy.this, android.R.layout.simple_spinner_item, empStatusList);

		adapterEmpStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnEmploymentStatus.setAdapter(adapterEmpStatus);

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

		baptismStatusList.add("");
		baptismStatusList.add("Yes");
		baptismStatusList.add("No");
		baptismStatusList.add("Attended Classes but yet to be Baptised");

		holyghostList.add("");
		holyghostList.add("Yes");
		holyghostList.add("No");

		bornAgainList.add("");
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
		
		adapterTitle = new ArrayAdapter<String>(MemberInfoActivtiy.this, android.R.layout.simple_spinner_item, titleList);
		adapterTitle.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		txttitle.setAdapter(adapterTitle);
		
		adapterGender = new ArrayAdapter<String>(MemberInfoActivtiy.this, android.R.layout.simple_spinner_item, genderList);
		adapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		txtGender.setAdapter(adapterGender);
		
		adapterageGroup = new ArrayAdapter<String>(MemberInfoActivtiy.this, android.R.layout.simple_spinner_item, agegroupList);
		adapterageGroup.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		txtAgeGroup.setAdapter(adapterageGroup);
		
		adapterbaptismStstus = new ArrayAdapter<String>(MemberInfoActivtiy.this, android.R.layout.simple_spinner_item, baptismStatusList);
		adapterbaptismStstus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		txtBaptismStatus.setAdapter(adapterbaptismStstus);	
		
		adapterholyghost = new ArrayAdapter<String>(MemberInfoActivtiy.this, android.R.layout.simple_spinner_item, holyghostList);
		adapterholyghost.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		txtholyghost.setAdapter(adapterholyghost);	
		
		adapterBornAgain = new ArrayAdapter<String>(MemberInfoActivtiy.this, android.R.layout.simple_spinner_item, bornAgainList);
		adapterBornAgain.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		txtBornAgain.setAdapter(adapterBornAgain);	
		
	
		adaptershcoolstatus = new ArrayAdapter<String>(MemberInfoActivtiy.this, android.R.layout.simple_spinner_item, SchoolstatusList);
		adaptershcoolstatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		txtSchoolstatus.setAdapter(adaptershcoolstatus);	
		
		
		adapterMartial = new ArrayAdapter<String>(MemberInfoActivtiy.this, android.R.layout.simple_spinner_item, martialInfoList);

		adapterMartial.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		txtMemberMartialInfo.setAdapter(adapterMartial);

		educationQualList.add("");
		educationQualList.add("City and Guilds");
		educationQualList.add("Doctorate");
		educationQualList.add("First Degree (BSC, BA)");
		educationQualList.add("HND");
		educationQualList.add("OND");
		educationQualList.add("SSCE School Certificate");
		educationQualList.add("Second Degree (MSC, MBA)");
		
		
		adapterQual = new ArrayAdapter<String>(MemberInfoActivtiy.this, android.R.layout.simple_spinner_item, educationQualList);

		adapterQual.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnEducationalQualification.setAdapter(adapterQual);

//		expYearsList.add("");
//		//expYearsList.add("");
//		expYearsList.add("1 - 3 Years");
//		expYearsList.add("3 - 6 Years");
//		expYearsList.add("6 - 10 Years");
//		expYearsList.add("10+ Years");
//
//
//		adapterExp = new ArrayAdapter<String>(MemberInfoActivtiy.this, android.R.layout.simple_spinner_item, expYearsList);

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

		adapterIndSeg = new ArrayAdapter<String>(MemberInfoActivtiy.this, android.R.layout.simple_spinner_item, industrySegList);

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
		


		adapterEmpStatus = new ArrayAdapter<String>(MemberInfoActivtiy.this, android.R.layout.simple_spinner_item, empStatusList);

		adapterEmpStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnEmploymentStatus.setAdapter(adapterEmpStatus);

	}

	private void getMemberInfo() {
		StringRequest reqGetProfile=new StringRequest(Method.POST,ShowMembersDetailsService.SERVICE_URL,new Listener<String>() {

			@Override
			public void onResponse(String response) {
				Methods.closeProgressDialog();
				Log.e("droid","get reqGetProfile ---------------"+ response);

				StatusCheckModel statusCheckModel=gson.fromJson(response, StatusCheckModel.class);
				if(null != statusCheckModel.getStatus() && statusCheckModel.getStatus().trim().length() >0){
					ResponseMessageModel respModel=gson.fromJson(response, ResponseMessageModel.class);
					if(respModel.getStatus()=="401"){
						Methods.longToast("User name or Password is incorrect", MemberInfoActivtiy.this);
					}else{
						Methods.longToast(respModel.getMessage(), MemberInfoActivtiy.this);
					}
				}else{
					MemberProfileModel mProfModel=gson.fromJson(response, MemberProfileModel.class);
					if(null !=mProfModel.getMessage() && mProfModel.getMessage().size() >0){

						mProfSubModel=mProfModel.getMessage();

						for(int i=0;i<mProfSubModel.size();i++){
//							txtMembershipNo.setText(mProfSubModel.get(i).getName());
					//		txtMemberName.setText(mProfSubModel.get(i).getMember_name());

                            yearlyIncome = mProfSubModel.get(i).getYearly_income();
                            nameJSON = mProfSubModel.get(i).getName();
                            imageName = mProfSubModel.get(i).getImage();
                            if(imageName != null)
                                imageName = imageName.substring(1);

                            txtMemberDateOfBirth.setText(mProfSubModel.get(i).getDate_of_birth());
							txtMemberPhone1.setText(mProfSubModel.get(i).getPhone_1());
							txtMemberPhone2.setText(mProfSubModel.get(i).getPhone_2());

							if(null !=mProfSubModel.get(i).getImage() && mProfSubModel.get(i).getImage().trim().length() > 0) {
                                Log.d("NonStop","Setting Profile Image!");
                                imgProfilePic.setImageURI(Uri.parse(SynergyValues.Web.BASE_URL + mProfSubModel.get(i).getImage()));
                                new SetProfPicTask(mProfSubModel.get(i).getImage()).execute();
                            }
							txtShortbio.setText(mProfSubModel.get(i).getShort_bio());
							txtMemberlName.setText(mProfSubModel.get(i).getSurname());
							txtMemberfName.setText(mProfSubModel.get(i).getMember_name());
							txtEmailID1.setText(mProfSubModel.get(i).getEmail_id());
							txtEmailID2.setText(mProfSubModel.get(i).getEmail_id2());
//							txtPassword.setText(mPreferenceHelper.getString(Commons.USER_PASSWORD));
							txtMemberHomeAddress.setText(mProfSubModel.get(i).getAddress());
							txtOfficeAddress.setText(mProfSubModel.get(i).getOffice_address());

//							txtYearlyIncome.setText(mProfSubModel.get(i).getYearly_income());

							txtCoreCompeteance.setText(mProfSubModel.get(i).getCore_competeance());
							txtYookosID.setText(mProfSubModel.get(i).getYokoo_id());
							
							txtLandmarkforHomeAddress.setText(mProfSubModel.get(i).getHome_address());
							txtLandmarkforOfficeAddress.setText(mProfSubModel.get(i).getOffice_landmark());
						
							
							try {
								Date dob=new Date();
								if(null !=mProfSubModel.get(i).getDate_of_birth()){
								dob=dateFormatterService.parse(mProfSubModel.get(i).getDate_of_birth());
								txtMemberDateOfBirth.setText(dateFormatter.format(dob));
								}
								
								if(null !=mProfSubModel.get(i).getDate_of_join()){
								dob=dateFormatterService.parse(mProfSubModel.get(i).getDate_of_join());
								txtDateofJoining.setText(dateFormatter.format(dob));
								}
								
								if(null !=mProfSubModel.get(i).getBaptism_when()){
								dob=dateFormatterService.parse(mProfSubModel.get(i).getBaptism_when());
								txtBaptisedWhen.setText(dateFormatter.format(dob));
								}

								if(null !=mProfSubModel.get(i).getBaptism_where()){
									txtBaptisedWhere.setText(mProfSubModel.get(i).getBaptism_where());
								}

							} catch (ParseException e) {
								e.printStackTrace();
							}
							
							
							if(i<1) {
								Imageurl= mProfSubModel.get(i).getImage();
							}
							
						    Log.e("Image Url",ImageUrl.imageUrl+Imageurl);
							
							if(Imageurl != null) {
								if(!Imageurl.equals("null"))
									Picasso.with(MemberInfoActivtiy.this).load(ImageUrl.imageUrl+Imageurl).into(imgProfilePic);
							}

							
//							if(null !=mProfSubModel.get(i).getBaptism_where())
//								txtBaptisedWhere.setText(mProfSubModel.get(i).getBaptism_where());
							
							
							
							
							if(null !=mProfSubModel.get(i).getRegion())
								txtregion.setText(mProfSubModel.get(i).getRegion());
							
//							if(null !=mProfSubModel.get(i).getRegion_name())
//								txtregionname.setText(mProfSubModel.get(i).getRegion());
							
							if(null !=mProfSubModel.get(i).getZone())
								txtzone.setText(mProfSubModel.get(i).getZone());
							
//							if(null !=mProfSubModel.get(i).getZone_name())
//								txtzonename.setText(mProfSubModel.get(i).getZone_name());
							
							if(null !=mProfSubModel.get(i).getChurch_group())
							    txtchurch_group.setText(mProfSubModel.get(i).getChurch_group());
							
//							if(null !=mProfSubModel.get(i).getGroup_church_name())
//								txtchurch_group_name.setText(mProfSubModel.get(i).getGroup_church_name());
							
							if(null !=mProfSubModel.get(i).getChurch())
							   txtChurch.setText(mProfSubModel.get(i).getChurch());
							
//							if(null !=mProfSubModel.get(i).getChurch_name())
//								txtChurch_name.setText(mProfSubModel.get(i).getChurch_name());
							
							if(null !=mProfSubModel.get(i).getPcf())
								txtpcf.setText(mProfSubModel.get(i).getPcf());
							
//							if(null !=mProfSubModel.get(i).getPcf_name())
//								txtpcf_name.setText(mProfSubModel.get(i).getPcf_name());
							
							
							if(null !=mProfSubModel.get(i).getSenior_cell())
								txtsenior_cell.setText(mProfSubModel.get(i).getSenior_cell());
							
//							if(null !=mProfSubModel.get(i).getSenior_cell_name())
//								txtsenior_cell_name.setText(mProfSubModel.get(i).getSenior_cell_name());
							
						
							if(null !=mProfSubModel.get(i).getCell())
								txtcell.setText(mProfSubModel.get(i).getCell());
							
//							if(null !=mProfSubModel.get(i).getCell_name())
//								txtcell_name.setText(mProfSubModel.get(i).getCell_name());
							
							if(null !=mProfSubModel.get(i).getShort_bio())
								txtShortbio.setText(mProfSubModel.get(i).getShort_bio());
							
							
							if(null !=mProfSubModel.get(i).getEmployment_status() && mProfSubModel.get(i).getEmployment_status().trim().length() >0)
								spnEmploymentStatus.setSelection(adapterEmpStatus.getPosition(mProfSubModel.get(i).getEmployment_status()));
							else
								spnEmploymentStatus.setSelection(0);
							if(null !=mProfSubModel.get(i).getIndustry_segment() && mProfSubModel.get(i).getIndustry_segment().trim().length() >0)
								spnIndustrySegment.setSelection(adapterIndSeg.getPosition(mProfSubModel.get(i).getIndustry_segment()));
							else
								spnIndustrySegment.setSelection(0);
							if(null !=mProfSubModel.get(i).getExperience_years() && mProfSubModel.get(i).getExperience_years().trim().length() >0)
								spnExperienceYears.setSelection(adapterExp.getPosition(mProfSubModel.get(i).getExperience_years()));
							else
								spnExperienceYears.setSelection(0);
							if(null !=mProfSubModel.get(i).getEducational_qualification() && mProfSubModel.get(i).getEducational_qualification().trim().length() >0)
								spnEducationalQualification.setSelection(adapterQual.getPosition(mProfSubModel.get(i).getEducational_qualification()));
							else
								spnEducationalQualification.setSelection(0);

							if(null !=mProfSubModel.get(i).getMarital_info() && mProfSubModel.get(i).getMarital_info().trim().length() >0)
								txtMemberMartialInfo.setSelection(adapterQual.getPosition(mProfSubModel.get(i).getMarital_info()));
							else
								txtMemberMartialInfo.setSelection(0);
							
							if(null !=mProfSubModel.get(i).getSex() && mProfSubModel.get(i).getSex().trim().length() >0)
								txtGender.setSelection(adapterGender.getPosition(mProfSubModel.get(i).getSex()));
							
							if(null !=mProfSubModel.get(i).getAge_group() && mProfSubModel.get(i).getAge_group().trim().length() >0)
								txtAgeGroup.setSelection(adapterageGroup.getPosition(mProfSubModel.get(i).getAge_group()));
							
							if((null !=mProfSubModel.get(i).getBaptisum_status() && mProfSubModel.get(i).getBaptisum_status().trim().length() >0)) {
								txtBaptismStatus.setSelection(adapterbaptismStstus.getPosition(mProfSubModel.get(i).getBaptisum_status()));
								if(mProfSubModel.get(i).getBaptisum_status().contentEquals("Yes")) {
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
							} else {
								bapWhnTV.setVisibility(View.GONE);
								bapWhrTV.setVisibility(View.GONE);
								txtBaptisedWhen.setVisibility(View.GONE);
								txtBaptisedWhere.setVisibility(View.GONE);
							}

							if(null !=mProfSubModel.get(i).getFilled_with_holy_ghost() && mProfSubModel.get(i).getFilled_with_holy_ghost().trim().length() >0)
								txtholyghost.setSelection(adapterholyghost.getPosition(mProfSubModel.get(i).getFilled_with_holy_ghost()));
							
							if(null !=mProfSubModel.get(i).getIs_new_born() && mProfSubModel.get(i).getIs_new_born().trim().length() >0)
								txtBornAgain.setSelection(adapterBornAgain.getPosition(mProfSubModel.get(i).getIs_new_born()));
							
							if(null !=mProfSubModel.get(i).getSchool_status() && mProfSubModel.get(i).getSchool_status().trim().length() >0)
								txtSchoolstatus.setSelection(adaptershcoolstatus.getPosition(mProfSubModel.get(i).getSchool_status()));
							
							if(null !=mProfSubModel.get(i).getTitle() && mProfSubModel.get(i).getTitle().trim().length() >0)
								txttitle.setSelection(adapterTitle.getPosition(mProfSubModel.get(i).getTitle()));
							
							
						if(mProfSubModel.get(i).getIs_eligibale_for_follow_up().equals("1"))
							txt_is_eligibale_for_follow_up.setChecked(true);
							
						}

						
					}else{
					}
				}	

			}
		},new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Methods.closeProgressDialog();
				Log.d("droid","get reqsaveMeeting error---------------"+ error.getCause());

				if(error.networkResponse.statusCode==403){
					Methods.longToast("No access to update Profile", MemberInfoActivtiy.this);
				}
				else
					Methods.longToast("Some Error Occured,please try again later", MemberInfoActivtiy.this);
			}
		}){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError{
				Map<String, String> params = new HashMap<String, String>();

				MeetingListRequestModel model=new MeetingListRequestModel();
				model.setUsername(mPreferenceHelper.getString(Commons.USER_EMAILID));
				model.setUserpass(mPreferenceHelper.getString(Commons.USER_PASSWORD));
				model.setName(memberName);


				String dataString=gson.toJson(model, MeetingListRequestModel.class);

				Log.d("droid", dataString);
				params.put(ShowMembersDetailsService.DATA, dataString);
				return params;
			}
		};

		App.getInstance().addToRequestQueue(reqGetProfile, "reqGetProfile");
		reqGetProfile.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));		
	}

	private class SetProfPicTask extends AsyncTask<String, Void, Bitmap> {
		private String mImgPath;

		public SetProfPicTask(String imgPath) {
			mImgPath=imgPath;
		}

		protected Bitmap doInBackground(String... urls) {

			Bitmap mIcon11 = null;
			try {
//				InputStream in = new java.net.URL("http://loveworldsynergy.org"+mImgPath).openStream();
				InputStream in = new java.net.URL(ImageUrl.imageUrl + mImgPath).openStream();
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
	}

    private void updateProfile() {
        StringRequest reqGetProfile=new StringRequest(Method.POST, SynergyValues.Web.MemberUpdate.SERVICE_URL,new Listener<String>() {

            @Override
            public void onResponse(String response) {
                Methods.closeProgressDialog();


                Log.e("responce droid","get reqGetProfile ---------------"+ response);

                try {

                    JSONObject jsonobj=new JSONObject(response);

                    if(response.contains("status")){


                        String status=jsonobj.getString("status");
                        if(status.equals("401")){
                            Methods.longToast("User name or Password is incorrect", MemberInfoActivtiy.this);
                        }else{
                            Methods.longToast(jsonobj.getString("message"), MemberInfoActivtiy.this);
                        }
                        if(status.equals("402")){
                            Methods.longToast(jsonobj.getString("message"), MemberInfoActivtiy.this);
                        }


                    }else{

                        String msg=jsonobj.getString("message");
                        Methods.longToast(msg,MemberInfoActivtiy.this);

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
                Log.d("droid","get reqUpdateMember error---------------"+ error.getCause());

                if(error.networkResponse.statusCode==403){
                    Methods.longToast("No access to update Profile", MemberInfoActivtiy.this);
                }
                else
                    Methods.longToast("Some Error Occured,please try again later", MemberInfoActivtiy.this);
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

                    obj.put("title",txttitle.getSelectedItem().toString());
                    obj.put("member_name", txtMemberfName.getText().toString());
                    obj.put("surname",txtMemberlName.getText().toString());
                    obj.put("short_bio",txtShortbio.getText().toString());
					if(!txtMemberDateOfBirth.getText().toString().contentEquals(""))
                    	obj.put("date_of_birth", dateFormatter01.format(dateFormatter.parse(txtMemberDateOfBirth.getText().toString())));
                    else
                        obj.put("date_of_birth",null);
					if(!txtDateofJoining.getText().toString().contentEquals(""))
                    	obj.put("date_of_join",dateFormatter01.format(dateFormatter.parse(txtDateofJoining.getText().toString())));
                    else
                        obj.put("date_of_join",null);
                    obj.put("marital_info", txtMemberMartialInfo.getSelectedItem().toString());
                    obj.put("sex", txtGender.getSelectedItem().toString());
                    obj.put("age_group", txtAgeGroup.getSelectedItem().toString());
                    obj.put("phone_1", txtMemberPhone1.getText().toString());
                    obj.put("phone_2", txtMemberPhone2.getText().toString());
					obj.put("email_id2", txtEmailID2.getText().toString());
                    obj.put("yokoo_id", txtYookosID.getText().toString());
                    obj.put("address", txtMemberHomeAddress.getText().toString());
                    obj.put("home_address", txtMemberHomeAddress.getText().toString());
                    obj.put("office_address", txtOfficeAddress.getText().toString());
                    obj.put("office_landmark", txtLandmarkforOfficeAddress.getText().toString());


                    obj.put("employment_status", spnEmploymentStatus.getSelectedItem().toString());
                    obj.put("industry_segment",spnIndustrySegment.getSelectedItem().toString());
                    obj.put("experience_years",spnExperienceYears.getSelectedItem().toString());
                    obj.put("core_competeance", txtCoreCompeteance.getText().toString());
                    obj.put("educational_qualification", spnEducationalQualification.getSelectedItem().toString());

                    obj.put("baptisum_status", txtBaptismStatus.getSelectedItem().toString());
                    obj.put("baptism_where", txtBaptisedWhere.getText().toString());
                    if(!txtBaptisedWhen.getText().toString().contentEquals(""))
                        obj.put("baptism_when", dateFormatter01.format(dateFormatter.parse(txtBaptisedWhen.getText().toString())));
                    else
                        obj.put("baptism_when",null);
                    obj.put("school_status", txtSchoolstatus.getSelectedItem().toString());
                    obj.put("is_new_born", txtBornAgain.getSelectedItem().toString());
                    obj.put("filled_with_holy_ghost",txtholyghost.getSelectedItem().toString());

                    obj.put("yearly_income", yearlyIncome);
                    obj.put("name", nameJSON);
                    obj.put("image",imageName);


                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                String dataString=obj.toString();//gson.toJson(model, MeetingListRequestModel.class);
                Log.d("NonStop", "Member Update Request: " + dataString);
                Log.e("Request droid", dataString);
                params.put(SynergyValues.Web.MemberUpdate.DATA, dataString);
                return params;
            }
        };

        App.getInstance().addToRequestQueue(reqGetProfile, "reqGetProfile");
        reqGetProfile.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
    }

	public boolean isValid() {

		if (!InputValidation.isEmailAddress(txtEmailID1, true)) {
			return false;
		}
		if (!InputValidation.isEmailAddress(txtYookosID, true)) {
			return false;
		}
		if (!InputValidation.isPhoneNumber(txtMemberPhone1, true)) {
			return false;
		}
		if (!InputValidation.isPhoneNumber(txtMemberPhone2, true)) {
			return false;
		}
		if (!InputValidation.hasText(txtMemberfName)) {
			new AlertDialog.Builder(MemberInfoActivtiy.this)
					.setCancelable(false)
					.setTitle("Invalid Input")
					.setMessage("Please enter first name")
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface, int i) {

						}
					})
					.show();
			return false;
		}
		if (!InputValidation.hasText(txtMemberPhone1)) {
			return false;
		}
		if (!InputValidation.hasText(txtMemberPhone2)) {
			return false;
		}
		if(!InputValidation.spnHasText(txtAgeGroup, "AgeGroup"))
		{
			new AlertDialog.Builder(MemberInfoActivtiy.this)
					.setCancelable(false)
					.setTitle("Invalid Input")
					.setMessage("Please select age group")
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface, int i) {

						}
					})
					.show();
			return false;
		}
		if(!InputValidation.spnHasText(txtGender, "Gender"))
		{
			new AlertDialog.Builder(MemberInfoActivtiy.this)
					.setCancelable(false)
					.setTitle("Invalid Input")
					.setMessage("Please select gender")
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface, int i) {

						}
					})
					.show();
			return false;
		}

		return true;
	}

}