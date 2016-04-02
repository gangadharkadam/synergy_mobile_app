package com.mutech.synergy.activities.task;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
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
import com.mutech.synergy.SynergyValues.Commons;
import com.mutech.synergy.SynergyValues.Web.GetAllMastersService;
import com.mutech.synergy.SynergyValues.Web.GetAllTasksService;
import com.mutech.synergy.SynergyValues.Web.GetHigherHierarchyService;
import com.mutech.synergy.SynergyValues.Web.GetMemberProfileService;
import com.mutech.synergy.SynergyValues.Web.LowerHierarchyService;
import com.mutech.synergy.SynergyValues.Web.ProfilePicUploadService;
import com.mutech.synergy.activities.cellMasters.CreateCellMasterActivity;
import com.mutech.synergy.activities.cellMasters.CreateSeniorCellMasterActivity;
import com.mutech.synergy.activities.profile.MyProfileActivity;
import com.mutech.synergy.models.CellMembersRespModel;
import com.mutech.synergy.models.HigherHierarchyRespModel;
import com.mutech.synergy.models.MeetingListRequestModel;
import com.mutech.synergy.models.MeetingModel;
import com.mutech.synergy.models.StatusCheckModel;
import com.mutech.synergy.models.HigherHierarchyRespModel.HHSubModel;
import com.mutech.synergy.models.MeetingModel.MeetingListModel;
import com.mutech.synergy.models.ResponseMessageModel;
import com.mutech.synergy.models.ResponseMessageModel2;
import com.mutech.synergy.models.SaveTaskRequestModel;
import com.mutech.synergy.utils.InputValidation;
import com.mutech.synergy.utils.Methods;
import com.mutech.synergy.utils.NetworkHelper;
import com.mutech.synergy.utils.PreferenceHelper;

public class CreateTaskActivity extends AppCompatActivity implements
OnClickListener, OnItemSelectedListener {

	private EditText txtTaskDetailsName, txtAssignee, txtPriority;
	private Spinner spnPriority, spnTaskCell, spnAssignedTo;
	private Button btnAttachPhotos, btnSaveTask;
	private TextView txtDueDate;
	ArrayList<MeetingListModel> mCellList;
	private ArrayList<String> membersList;
	private ArrayList<String> useridList;
	private ArrayList<String> idList;
	
	String useremailid;
	
	private SimpleDateFormat dateFormatter,dateFormatter01;
	private DatePickerDialog dueDatePickerDialog;
	private PreferenceHelper mPreferenceHelper;
	private Gson gson;
	private boolean isFirstCall;
	private String assignedTo;

	private LinearLayout llImgsAdded;
	private int SELECT_FILE=0,REQUEST_CAMERA=1;
	private Bitmap thumbnail;
	private ArrayList<Bitmap> bitmapList;

	private ArrayList<String> mPCFList,mSeniorCellList;
	private Spinner spnTaskPCF,spnTaskSrCell;
	private String defKey,defVal,defRole;
	TextView lblCell,lblsrcell,lblpcf;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task);

		initialize();
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	private void initialize() {

		getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		getSupportActionBar().setHomeButtonEnabled(false);
		getSupportActionBar().setCustomView(R.layout.custom_actionbar);
		TextView tvTitle=(TextView)getSupportActionBar().getCustomView().findViewById(R.id.title_text);
		tvTitle.setText("CREATE TASK    ");
		
		
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2E9AFE")));
		
		getSupportActionBar().setDisplayShowCustomEnabled(true);

		mPreferenceHelper = new PreferenceHelper(this);
		gson = new Gson();

		lblCell=(TextView) findViewById(R.id.lblCell);
		lblsrcell=(TextView) findViewById(R.id.lblsrcell);
		lblpcf=(TextView) findViewById(R.id.lblpcf);
		
		txtTaskDetailsName = (EditText) findViewById(R.id.txtTaskDetailsName);
		txtDueDate = (TextView) findViewById(R.id.txtDueDate);
		txtAssignee = (EditText) findViewById(R.id.txtAssignee);
		//txtPriority = (EditText) findViewById(R.id.txtPriority);
		spnPriority = (Spinner) findViewById(R.id.spnPriority);
		spnTaskCell = (Spinner) findViewById(R.id.spnTaskCell);
		spnAssignedTo = (Spinner) findViewById(R.id.spnAssignedTo);

		btnAttachPhotos = (Button) findViewById(R.id.btnAttachPhotos);
		btnSaveTask = (Button) findViewById(R.id.btnSaveTask);
		spnTaskPCF=(Spinner) findViewById(R.id.spnTaskPCF);
		spnTaskSrCell=(Spinner) findViewById(R.id.spnTaskSrCell);

		btnAttachPhotos.setOnClickListener(this);
		btnSaveTask.setOnClickListener(this);
		llImgsAdded=(LinearLayout) findViewById(R.id.llImgsAdded);
		bitmapList=new ArrayList<Bitmap>();

		mPCFList=new ArrayList<String>();
		mSeniorCellList=new ArrayList<String>();

		defKey=mPreferenceHelper.getString(Commons.USER_DEFKEY);
		defVal=mPreferenceHelper.getString(Commons.USER_DEFVALUE);
		defRole=mPreferenceHelper.getString(Commons.USER_ROLE);

		isFirstCall = true;
		txtDueDate.setOnClickListener(this);

		spnTaskCell.setOnItemSelectedListener(this);
		spnAssignedTo.setOnItemSelectedListener(this);
		spnTaskPCF.setOnItemSelectedListener(this);
		spnTaskSrCell.setOnItemSelectedListener(this);
		
		lblCell.setVisibility(View.GONE);
		lblsrcell.setVisibility(View.GONE);
		lblpcf.setVisibility(View.GONE);
		
		spnTaskSrCell.setVisibility(View.GONE);
		spnTaskPCF.setVisibility(View.GONE);
		//spnTaskCell.setVisibility(View.GONE);
		
		
		/*//if(defRole.equals("Cell Leader")){
			
			
			spnTaskPCF.setEnabled(false);
			spnTaskSrCell.setEnabled(false);
			spnTaskCell.setEnabled(false);
		//}
*/		

		ArrayList<String> priorityList=new ArrayList<String>();
		priorityList.add("Low");
		priorityList.add("Medium");
		priorityList.add("High");
		priorityList.add("Urgent");


		ArrayAdapter<String> adapterPriority = new ArrayAdapter<String>(CreateTaskActivity.this, android.R.layout.simple_spinner_item, priorityList);
		adapterPriority.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnPriority.setAdapter(adapterPriority);

		if(NetworkHelper.isOnline(this)){
			Methods.showProgressDialog(CreateTaskActivity.this);
			getData();
		}else{
			Methods.longToast("Please connect to Internet", this);
		}
	}

	@SuppressLint("NewApi")
	private void getData() {
		dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
		dateFormatter01=new SimpleDateFormat("yyyy-MM-dd");
		// 2015-05-24 06:22:22

		Calendar newCalendar = Calendar.getInstance();
		dueDatePickerDialog = new DatePickerDialog(this,
				new OnDateSetListener() {

			public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth) {
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
				txtDueDate.setText(dateFormatter.format(newDate.getTime()));
			}

		},newCalendar.get(Calendar.YEAR),newCalendar.get(Calendar.MONTH),newCalendar.get(Calendar.DAY_OF_MONTH));
		dueDatePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
		txtAssignee.setText(mPreferenceHelper.getString(Commons.USER_EMAILID));

		getMembersOfSelectedCell();
	//	getCellList();
	//	getTopHierarchy();

	}

	private void getCellList() {
		StringRequest reqgetLowerHierarchy=new StringRequest(Method.POST,GetAllMastersService.SERVICE_URL,new Listener<String>() {

			@Override
			public void onResponse(String response) {
				Methods.closeProgressDialog();
				Log.d("droid","get reqgetLowerHierarchy ---------------"+ response);

				if(response.contains("status"))
				{
					ResponseMessageModel2 respModel=gson.fromJson(response, ResponseMessageModel2.class);
					if(respModel.getMessage().getStatus()=="401"){
						Methods.longToast("User name or Password is incorrect", CreateTaskActivity.this);
					}else{
						Methods.longToast(respModel.getMessage().getMessage(), CreateTaskActivity.this);
					}
				}else{
					MeetingModel mMeetingModel=gson.fromJson(response, MeetingModel.class);
					//Object meetingmsg=mMeetingModel.getMessage();
					if(null != mMeetingModel.getStatus() && mMeetingModel.getStatus().trim().length() >0){

						if(mMeetingModel.getStatus()=="401"){
							Methods.longToast("User name or Password is incorrect", CreateTaskActivity.this);
						}
					}else{
						if(null !=mMeetingModel.getMessage() && mMeetingModel.getMessage().size() >0){
							mCellList=new ArrayList<MeetingModel.MeetingListModel>();
							//if(meetingmsg instanceof JSONArray){
							mCellList=mMeetingModel.getMessage();
							Log.d("droid", "isjsonarray----------------------------------");
							if(mCellList.size() > 0){
								MastersListAdapter adapter = new MastersListAdapter(CreateTaskActivity.this, mCellList);
								spnTaskCell.setAdapter(adapter);
								adapter.notifyDataSetChanged();
							}
						}else{
							Methods.longToast("No results found", CreateTaskActivity.this);
						}
					}
				}


			}
		},new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Methods.closeProgressDialog();
				Log.d("droid","get reqgetLowerHierarchy error---------------"+ error.getCause());

				if(error.networkResponse.statusCode==403){
						Methods.longToast("Access Denied", CreateTaskActivity.this);
				}
				//else
				//	Methods.longToast("Some Error Occured,please try again later", CreateSeniorCellMasterActivity.this);

			}

		}){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError{
				Map<String, String> params = new HashMap<String, String>();

				MeetingListRequestModel model=new MeetingListRequestModel();
				model.setUsername(mPreferenceHelper.getString(Commons.USER_EMAILID));
				model.setUserpass(mPreferenceHelper.getString(Commons.USER_PASSWORD));
				model.setTbl("Cells");
				//model.setName(mPreferenceHelper.getString(Commons.USER_DEFVALUE));

				String dataString=gson.toJson(model, MeetingListRequestModel.class);

				Log.d("droid", dataString);
				params.put(GetHigherHierarchyService.DATA, dataString);
				return params;
			}
		};

		App.getInstance().addToRequestQueue(reqgetLowerHierarchy, "reqgetLowerHierarchy");
		reqgetLowerHierarchy.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));


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
						Methods.longToast("User name or Password is incorrect", CreateTaskActivity.this);
					}
				}else{
					if(null !=mHHModel.getMessage() && mHHModel.getMessage().size() >0){
						ArrayList<HHSubModel> mHHSubModel=new ArrayList<HHSubModel>();
						//if(meetingmsg instanceof JSONArray){
						mHHSubModel=mHHModel.getMessage();

						for(int i=0;i<mHHSubModel.size();i++){			
							/*if(null !=mHHSubModel.get(i).getZone())
								mZoneList.add(mHHSubModel.get(i).getZone());
							if(null !=mHHSubModel.get(i).getRegion())
								mRegionList.add(mHHSubModel.get(i).getRegion());
							if(null !=mHHSubModel.get(i).getChurch())
								mChurchList.add(mHHSubModel.get(i).getChurch());
							if(null !=mHHSubModel.get(i).getChurch_group())
								mGrpChurchList.add(mHHSubModel.get(i).getChurch_group());*/
							if(null !=mHHSubModel.get(i).getPcf())
								mPCFList.add(mHHSubModel.get(i).getPcf());
							if(null !=mHHSubModel.get(i).getSenior_cell())
								mSeniorCellList.add(mHHSubModel.get(i).getSenior_cell());
						}

					}else{
					}
				}
				Methods.showProgressDialog(CreateTaskActivity.this);
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

					Methods.showProgressDialog(CreateTaskActivity.this);
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
						Methods.longToast("User name or Password is incorrect", CreateTaskActivity.this);
					}
				}else{
					if(null !=mHHModel.getMessage() && mHHModel.getMessage().size() >0){
						ArrayList<HHSubModel> mHHSubModel=new ArrayList<HHSubModel>();
						//if(meetingmsg instanceof JSONArray){
						mHHSubModel=mHHModel.getMessage();


						//						for(int i=0;i<mHHSubModel.size();i++){	
						//							if(null !=mHHSubModel.get(i).getZone())
						//								mZoneList.add(mHHSubModel.get(i).getZone());
						//							if(null !=mHHSubModel.get(i).getRegion())
						//								mRegionList.add(mHHSubModel.get(i).getRegion());
						//							if(null !=mHHSubModel.get(i).getChurch())
						//								mChurchList.add(mHHSubModel.get(i).getChurch());
						//							if(null !=mHHSubModel.get(i).getChurch_group())
						//								mGrpChurchList.add(mHHSubModel.get(i).getChurch_group());
						//							if(null !=mHHSubModel.get(i).getPcf())
						//								mPCFList.add(mHHSubModel.get(i).getPcf());
						//							if(null !=mHHSubModel.get(i).getSenior_cell())
						//								mSeniorCellList.add(mHHSubModel.get(i).getSenior_cell());
						//						}

						if(null !=mHHSubModel && mHHSubModel.size() > 0){
							for(int i=0;i<mHHSubModel.size();i++)
								if( defKey.equalsIgnoreCase("PCFs")){
									mSeniorCellList.add(mHHModel.getMessage().get(i).getName());
								}else if(defRole.equalsIgnoreCase("Churches")){
									mPCFList.add(mHHModel.getMessage().get(i).getName());
								}
							/*else if(defRole.equalsIgnoreCase("Group Churches")){
									mChurchList.add(mHHModel.getMessage().get(i).getName());
								}else if(defRole.equalsIgnoreCase("Zones")){
									mGrpChurchList.add(mHHModel.getMessage().get(i).getName());
								}else if(defRole.equalsIgnoreCase("Region")){
									mZoneList.add(mHHModel.getMessage().get(i).getName());
								}*/
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

		if(null !=defVal){
			if( defKey.equalsIgnoreCase("PCFs")){
				mPCFList.add(defVal);
			}else if(defKey.equalsIgnoreCase("Senior Cells")){
				mSeniorCellList.add(defVal);
			}
			/*else if(defRole.equalsIgnoreCase("Churches")){
				mChurchList.add(defVal);
			}else if(defRole.equalsIgnoreCase("Group Churches")){
				mGrpChurchList.add(defVal);
			}else if(defRole.equalsIgnoreCase("Zones")){
				mZoneList.add(defVal);
			}*/
		}

		/*ArrayAdapter<String> adapterZone = new ArrayAdapter<String>(CreateTaskActivity.this, android.R.layout.simple_spinner_item, mZoneList);
		adapterZone.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnCellZone.setAdapter(adapterZone);

		ArrayAdapter<String> adapterRegion = new ArrayAdapter<String>(CreateTaskActivity.this, android.R.layout.simple_spinner_item, mRegionList);
		adapterRegion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnCellRegion.setAdapter(adapterRegion);

		ArrayAdapter<String> adapterChurch = new ArrayAdapter<String>(CreateTaskActivity.this, android.R.layout.simple_spinner_item, mChurchList);
		adapterChurch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnCellChurch.setAdapter(adapterChurch);*/

		ArrayAdapter<String> adapterPCF = new ArrayAdapter<String>(CreateTaskActivity.this, android.R.layout.simple_spinner_item, mPCFList);
		adapterPCF.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnTaskPCF.setAdapter(adapterPCF);

		ArrayAdapter<String> adapterSrCell = new ArrayAdapter<String>(CreateTaskActivity.this, android.R.layout.simple_spinner_item, mSeniorCellList);
		adapterSrCell.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnTaskSrCell.setAdapter(adapterSrCell);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnAttachPhotos:
			openDialog();

			break;
		case R.id.btnSaveTask:
			if(NetworkHelper.isOnline(this)){
				if(validateFields()){
					if(InputValidation.hasText(txtTaskDetailsName)) {
						Methods.showProgressDialog(this);
						saveTask();
					} else {
						new AlertDialog.Builder(CreateTaskActivity.this)
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
			}else{
				Methods.longToast("Please connect to Internet", this);
			}
			break;
		case R.id.txtDueDate:
			dueDatePickerDialog.show();
			break;
		default:
			break;
		}
	}

	private boolean validateFields() {
		if(null !=txtTaskDetailsName.getText().toString() && txtTaskDetailsName.getText().toString().trim().length() >0){
//			if(null !=txtDueDate.getText().toString() && txtDueDate.getText().toString().trim().length() >0){
//				return true;
//			}else
//				Methods.longToast("Please Select Task Due date", this);
			return true;
		}else
			Methods.longToast("Please Enter Task Details", this);
		return false;
	}

	private void openDialog(){
		final CharSequence[] items = { "Take Photo","Choose from Gallery",
		"Cancel" };//

		AlertDialog.Builder builder = new AlertDialog.Builder(CreateTaskActivity.this);
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

			LinearLayout.LayoutParams imParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			ImageView imgAdd = new ImageView(this);
			llImgsAdded.addView(imgAdd,imParams);

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

				imgAdd.setImageBitmap(thumbnail);
				bitmapList.add(thumbnail);

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

						imgAdd.setImageBitmap(thumbnail);
						bitmapList.add(thumbnail);

					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {

				}
			}
		}
	}

	private void saveTask() {

		StringRequest reqCreateTasksList = new StringRequest(Method.POST,GetAllTasksService.CREATE_TASK, new Listener<String>() {

			@Override
			public void onResponse(String response) {
				Methods.closeProgressDialog();
				Log.e("droid Responce", "create task list ---------------"+ response);

				if(response.contains("status")){
					ResponseMessageModel2 resp2Model=gson.fromJson(response, ResponseMessageModel2.class);
					Methods.longToast(resp2Model.getMessage().getMessage(), CreateTaskActivity.this);
				}else{
					ResponseMessageModel model=gson.fromJson(response, ResponseMessageModel.class);
					if(null !=model.getMessage() && model.getMessage().trim().length() >0){
						Methods.longToast(model.getMessage(), CreateTaskActivity.this);
					}
				}
				for(int i=0;i<bitmapList.size();i++){
					Methods.showProgressDialog(CreateTaskActivity.this);
					uploadProfPicService(bitmapList.get(i),i);
				}
				finish();
				Intent obj=new Intent(CreateTaskActivity.this, ToDoTaskActivity.class);
				startActivity(obj);

			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Methods.closeProgressDialog();
				Log.d("droid", "create tasks error---------------"+ error.getCause());

			}
		}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();

				SaveTaskRequestModel model = new SaveTaskRequestModel();
				model.setUserName(mPreferenceHelper.getString(Commons.USER_EMAILID));
				model.setUserpass(mPreferenceHelper.getString(Commons.USER_PASSWORD));
				model.setStatus("Open");

			//	MeetingListModel modelSaveCell=(MeetingListModel) spnTaskCell.getSelectedItem();
			//	model.setCell(modelSaveCell.getName());
				model.setDescription(txtTaskDetailsName.getText().toString());
				if(!txtDueDate.getText().toString().contentEquals("")) {
					try {
						model.setExp_end_date(dateFormatter01.format(dateFormatter.parse(txtDueDate.getText().toString())));
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				Log.e(null, "Assignedto--"+spnAssignedTo.getSelectedItem().toString());
			
				model.set_assign(spnAssignedTo.getSelectedItem().toString());
				model.setPriority(spnPriority.getSelectedItem().toString());
				model.setSubject("task");
				model.setAssignee(txtAssignee.getText().toString());
				model.setName(txtTaskDetailsName.getText().toString());

				//model.setPcf(spnTaskPCF.getSelectedItem().toString());
				//model.setSenior_cell(spnTaskSrCell.getSelectedItem().toString());

				String dataString = gson.toJson(model,SaveTaskRequestModel.class);

				Log.e("droid Request", dataString);
				params.put(GetAllTasksService.DATA, dataString);
				return params;
			}
		};

		App.getInstance().addToRequestQueue(reqCreateTasksList, "reqTasksList");
		reqCreateTasksList.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));

	}
//BasicNetwork.performRequest: Unexpected response code 404 for http://verve.indictranstech.com/api/method/church_ministry.church_ministry.doctype.member.member.file_upload/

	
	
	protected void uploadProfPicService(final Bitmap mBitmap,final int iterator) {
		StringRequest reqSendProfilePic=new StringRequest(Method.POST,ProfilePicUploadService.SERVICE_URL,new Listener<String>() {

			@Override
			public void onResponse(String response) {
				Methods.closeProgressDialog();
				Log.d("droid","get reqSendProfilePic ---------------"+ response);

				StatusCheckModel statusCheckModel=gson.fromJson(response, StatusCheckModel.class);
				if(null != statusCheckModel.getStatus() && statusCheckModel.getStatus().trim().length() >0){
					ResponseMessageModel respModel=gson.fromJson(response, ResponseMessageModel.class);
					if(respModel.getStatus()=="401"){
						//	Methods.longToast("User name or Password is incorrect", CreateTaskActivity.this);
					}else{
						//	Methods.longToast(respModel.getMessage(), CreateTaskActivity.this);
					}
				}else{
					//Methods.longToast("Profile Updated Succesfully", CreateTaskActivity.this);
				}

				if((iterator+1) ==bitmapList.size()){
					finish();
				}

			}
		},new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Methods.closeProgressDialog();
				Log.d("droid","get reqSendProfilePic error---------------"+ error.getCause());

				if((iterator+1) ==bitmapList.size()){
					finish();
				}

				if(error.networkResponse.statusCode==403){
					//	Methods.longToast("No access to update Photos", CreateTaskActivity.this);
				}
				//else
				//	Methods.longToast("Some Error Occured,please try again later", CreateTaskActivity.this);
			}
		}){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError{
				Map<String, String> params = new HashMap<String, String>();

				BitmapFactory.Options options = new BitmapFactory.Options();
			
				options.inPreferredConfig = Bitmap.Config.ARGB_8888;

				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				mBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
				byte[] byteArray = byteArrayOutputStream .toByteArray();

				String fdata = Base64.encodeToString(byteArray, Base64.DEFAULT);

				MeetingListRequestModel model=new MeetingListRequestModel();
				model.setUsername(mPreferenceHelper.getString(Commons.USER_EMAILID));
				model.setUserpass(mPreferenceHelper.getString(Commons.USER_PASSWORD));
				model.setFilename("test"+".jpg");
				model.setFdata(fdata);
				model.setTbl("Task");//mPreferenceHelper.getString(Commons.USER_DEFKEY)
				model.setName(txtTaskDetailsName.getText().toString());

				String dataString=gson.toJson(model, MeetingListRequestModel.class);

				Log.d("droid", dataString);
				params.put(GetMemberProfileService.DATA, dataString);
				return params;
			}
		};

		App.getInstance().addToRequestQueue(reqSendProfilePic, "reqSendProfilePic");
		reqSendProfilePic.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));

	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		Spinner spinner = (Spinner) parent;
		switch (spinner.getId()) {
		case R.id.spnTaskCell:
			//if (!isFirstCall) {
			Log.d("droid", "Selected cell " + mCellList.get(position));
		//	getMembersOfSelectedCell(mCellList.get(position).getName());
			//}
			isFirstCall = false;
			break;

		case R.id.spnAssignedTo:
			Log.e(null, "onclick");
			useremailid=useridList.get(position);
			assignedTo = membersList.get(position);
			
			break;

		case R.id.spnTaskPCF:
			break;
		case R.id.spnTaskSrCell:
			break;

		default:
			break;
		}
	}

	private void getMembersOfSelectedCell() {
		StringRequest reqGetCellMember = new StringRequest(Method.POST,GetAllTasksService.FETCH_DATA, new Listener<String>() {
			@Override
			public void onResponse(String response) {
				Methods.closeProgressDialog();
				Log.e("Responce droid", "get all cell members ---------------"
						+ response);

				/*{
			        "user_id": "region3@gmail.com",
			        "ID": "M00000062",
			        "member_name": "Region3 Lead Member"
			    },
				*/
				
				membersList = new ArrayList<String>();
				 useridList=new ArrayList<String>();
				idList=new ArrayList<String>();
					
				gson = new Gson();
				CellMembersRespModel model = gson.fromJson(response,CellMembersRespModel.class);
				if (null != model.getMessage()) {
					for (int i = 0; i < model.getMessage().size(); i++) {
						
						idList.add(model.getMessage().get(i).getID());
						useridList.add(model.getMessage().get(i).getUser_id());
						membersList.add(model.getMessage().get(i).getMemberName());
					}	
				}
				Log.d("droid", "members " + membersList.size());
				MastersListAdapterTask adapter = new MastersListAdapterTask(CreateTaskActivity.this, membersList,idList,useridList);
				spnAssignedTo.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				// }
				/*
				 * } catch (JSONException e) { e.printStackTrace(); }
				 */
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Methods.closeProgressDialog();
				Log.d("droid", "get all cell error---------------"
						+ error.getCause());
			}
		}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Log.d("droid", "get params");
				Map<String, String> params = new HashMap<String, String>();

				MeetingListRequestModel model = new MeetingListRequestModel();
				model.setUsername(mPreferenceHelper.getString(Commons.USER_EMAILID));
				model.setUserpass(mPreferenceHelper.getString(Commons.USER_PASSWORD));
				
				String dataString = gson.toJson(model,MeetingListRequestModel.class);

				Log.e("Request droid", "Params : " + dataString);
				params.put(GetAllTasksService.DATA, dataString);
				return params;
			}
		};
		App.getInstance().addToRequestQueue(reqGetCellMember,"reqGetCellMember");
		reqGetCellMember.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1,1.0f));

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}
	public class MastersListAdapter extends BaseAdapter{

		private Context mContext;
		private ArrayList<MeetingListModel> mList;
		
		public MastersListAdapter(Context context,
				ArrayList<MeetingListModel> mCellList) {
			mContext=context;
			mList=new ArrayList<MeetingListModel>();
			mList=mCellList;
			
		
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
		public View getView(int position, View convertView, ViewGroup parent) {
			Holder mHolder;
			LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
			mHolder = new Holder();
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.row_masterlist, parent, false);
				mHolder.txtMasterName = (TextView) convertView.findViewById(R.id.txtMasterName);
				convertView.setTag(mHolder);
			} else {
				mHolder = (Holder) convertView.getTag();
			}
			mHolder.txtMasterName.setText(mList.get(position).getName());

			return convertView;
		}
	}


	public class MastersListAdapterTask extends BaseAdapter{

		private Context mContext;
		private ArrayList<String> mList;
		private ArrayList<String> idList;
		private ArrayList<String> useridList;

		public MastersListAdapterTask(Context context,
				ArrayList<String> mCellList,ArrayList<String> idList,ArrayList<String> useridList) {
			mContext=context;
			mList=new ArrayList<String>();
			mList=mCellList;
			
			this.idList=new ArrayList<String>();
			this.useridList=new ArrayList<String>();
			
			this.idList=idList;
			this.useridList=useridList;
			
		}

		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			return useridList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Holder mHolder;
			LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
			mHolder = new Holder();
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.row_masterlist, parent, false);
				mHolder.txtMasterName = (TextView) convertView.findViewById(R.id.txtMasterName);
				mHolder.txtuserid=(TextView) convertView.findViewById(R.id.txtuserid);
				mHolder.txtid=(TextView) convertView.findViewById(R.id.txtid);
				
				convertView.setTag(mHolder);
			} else {
				mHolder = (Holder) convertView.getTag();
			}
			mHolder.txtMasterName.setText(mList.get(position));
			mHolder.txtuserid.setText(useridList.get(position));
			mHolder.txtid.setText(idList.get(position));

			return convertView;
		}
	}

	static class Holder{
		private TextView txtMasterName;
		private TextView txtuserid;
		private TextView txtid;
	}

}
