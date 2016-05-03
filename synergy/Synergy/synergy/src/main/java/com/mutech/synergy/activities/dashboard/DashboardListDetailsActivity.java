package com.mutech.synergy.activities.dashboard;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
import com.google.gson.JsonObject;
import com.mutech.databasedetails.CreateNewMemberActivity;
import com.mutech.synergy.App;
import com.mutech.synergy.R;
import com.mutech.synergy.R.id;
import com.mutech.synergy.R.layout;
import com.mutech.synergy.R.menu;
import com.mutech.synergy.SynergyValues.Commons;
import com.mutech.synergy.SynergyValues.ImageUrl;
import com.mutech.synergy.SynergyValues.Web.DashboardDataService;
import com.mutech.synergy.SynergyValues.Web.DashboardListDataService;
import com.mutech.synergy.SynergyValues.Web.GetCellDetailsService;
import com.mutech.synergy.SynergyValues.Web.GetMemberProfileService;
import com.mutech.synergy.SynergyValues.Web.ProfilePicUploadService;
import com.mutech.synergy.SynergyValues.Web.UpdateAllDetailsService;

import com.mutech.synergy.activities.meeting.MarkMemberAttendanceActivity;
import com.mutech.synergy.activities.profile.MyProfileActivity;
import com.mutech.synergy.models.MeetingListRequestModel;
import com.mutech.synergy.models.ResponseMessageModel;
import com.mutech.synergy.models.ResponseMessageModel2;
import com.mutech.synergy.models.StatusCheckModel;
import com.mutech.synergy.utils.InputValidation;
import com.mutech.synergy.utils.Methods;
import com.mutech.synergy.utils.NetworkHelper;
import com.mutech.synergy.utils.PreferenceHelper;
import com.squareup.picasso.Picasso;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class DashboardListDetailsActivity extends AppCompatActivity {

	int REQUEST_CAMERA = 0, SELECT_FILE = 1;
	
	String name,tblname;
	private PreferenceHelper mPreferenceHelper;
	private Gson gson;
	ImageView imgProfilePic;
	EditText txtMembershipNo,txtMemberfName,txtMemberlName,txtMemberPhone1,txtMemberPhone2,txtEmailID1,txtEmailID2,
	txtMemberHomeAddress,txtOfficeAddress;
	TextView txtMemberDateOfBirth;
	TextView txtMemberDateOfJoining;
	Spinner txtMemberMartialInfo;
	TextView txttitle,lblphoto;
	Button btnSaveMemberInfo;
	Calendar newCalendar;
	private DatePickerDialog fromDatePickerDialog;
	private SimpleDateFormat dateFormatter,dateFormatterService;
	ArrayList<String> martialInfoList;
	ArrayAdapter<String> adapterMartial;

	private ArrayList<String> txtisnewconvertList;
	private Spinner spisnewconvert;

	private Spinner txtGender;
	ArrayAdapter<String> adaptertxtGender;
	private ArrayList<String> genderList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_row_details);

		spisnewconvert=(Spinner) findViewById(R.id.txtisnewconvert);
		String[] iscon = new String[] {"Yes", "No"};
		ArrayAdapter<String> adaptertxtisnewconvert=new ArrayAdapter<String>(DashboardListDetailsActivity.this, android.R.layout.simple_spinner_item, iscon);
		adaptertxtisnewconvert.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spisnewconvert.setAdapter(adaptertxtisnewconvert);

		imgProfilePic=(ImageView) findViewById(R.id.imgProfilePic);
		lblphoto=(TextView) findViewById(R.id.lblphoto);
		txtMembershipNo=(EditText) findViewById(R.id.txtMembershipNo);
		txtMemberfName=(EditText) findViewById(R.id.txtMemberfName);
		txtMemberlName=(EditText) findViewById(R.id.txtMemberlName);
		txtMemberPhone1=(EditText) findViewById(R.id.txtMemberPhone1);
		txtMemberPhone2=(EditText) findViewById(R.id.txtMemberPhone2);
		txtEmailID1=(EditText) findViewById(R.id.txtEmailID1);
		txtEmailID2=(EditText) findViewById(R.id.txtEmailID2);
		txtMemberHomeAddress=(EditText) findViewById(R.id.txtMemberHomeAddress);
		txtOfficeAddress=(EditText) findViewById(R.id.txtOfficeAddress);

		txtGender=(Spinner) findViewById(R.id.txtGender);

		txtMemberDateOfBirth=(TextView) findViewById(R.id.txtMemberDateOfBirth);
		txtMemberDateOfJoining=(TextView) findViewById(R.id.txtDateofJoining);
		txtMemberMartialInfo= (Spinner) findViewById(R.id.txtMemberMartialInfo);
		
		txttitle=(TextView) findViewById(R.id.textView1);
		
		btnSaveMemberInfo=(Button) findViewById(R.id.btnSaveMemberInfo);
		
		Calendar newCalendar = Calendar.getInstance();
		
		dateFormatterService=new SimpleDateFormat("yyyy-MM-dd",Locale.US);
		dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
		newCalendar = Calendar.getInstance();
		
		
		fromDatePickerDialog = new DatePickerDialog(DashboardListDetailsActivity.this, new OnDateSetListener() {

			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
				txtMemberDateOfBirth.setText(dateFormatter.format(newDate.getTime()));
			}

		},newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

		
		
		txtMemberDateOfBirth.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				fromDatePickerDialog.show();
			}
		});


		fromDatePickerDialog = new DatePickerDialog(DashboardListDetailsActivity.this, new OnDateSetListener() {

			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
				txtMemberDateOfJoining.setText(dateFormatter.format(newDate.getTime()));
			}

		},newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));



		txtMemberDateOfJoining.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				fromDatePickerDialog.show();
			}
		});

		imgProfilePic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				openDialog();

			}
		});


		getSupportActionBar().hide();
		gson=new Gson();
		
		mPreferenceHelper=new PreferenceHelper(DashboardListDetailsActivity.this);
		name=getIntent().getStringExtra("name");
		tblname=getIntent().getStringExtra("tblename");
		
		txttitle.setText(getIntent().getStringExtra("title"));

		genderList=new ArrayList<String>();
		genderList.add("Male");
		genderList.add("Female");

		ArrayAdapter<String> adaptertxtGender = new ArrayAdapter<String>(DashboardListDetailsActivity.this, android.R.layout.simple_spinner_item, genderList);
		adaptertxtGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		txtGender.setAdapter(adaptertxtGender);

		martialInfoList=new ArrayList<String>();

		martialInfoList.add("Divorced");
		martialInfoList.add("Married");
		martialInfoList.add("Single");
		martialInfoList.add("Widowed");
		
		adapterMartial = new ArrayAdapter<String>(DashboardListDetailsActivity.this, android.R.layout.simple_spinner_item, martialInfoList);
		adapterMartial.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		txtMemberMartialInfo.setAdapter(adapterMartial);
		
		
		lblphoto.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				selectImage(); 
			}
		});
		
		
		if(NetworkHelper.isOnline(this)){
			Methods.showProgressDialog(this);
			getDashboardDataService();
	
		}
		else
			Methods.longToast("Please connect to Internet", this);
		
		
		
		
		btnSaveMemberInfo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				if(NetworkHelper.isOnline(DashboardListDetailsActivity.this)){
					if (isValid()) {
						Methods.showProgressDialog(DashboardListDetailsActivity.this);
						UpdateDashboardDataService();

					}
				}
				else
					Methods.longToast("Please connect to Internet", DashboardListDetailsActivity.this);
				
			}
		});
		
	}

	public boolean isValid() {

		if(!InputValidation.hasText(txtMemberfName)) {
			AlertDialog dialog = new AlertDialog.Builder(DashboardListDetailsActivity.this)
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
		if(!InputValidation.spnHasText(txtGender, "Gender")){
			AlertDialog dialog = new AlertDialog.Builder(DashboardListDetailsActivity.this)
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
		if(!InputValidation.isPhoneNumber(txtMemberPhone1, false)) {
			AlertDialog dialog = new AlertDialog.Builder(DashboardListDetailsActivity.this)
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
			AlertDialog dialog = new AlertDialog.Builder(DashboardListDetailsActivity.this)
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


		private void getDashboardDataService() {
		StringRequest reqDashboard=new StringRequest(Method.POST,GetCellDetailsService.SERVICE_URL,new Listener<String>() {

			@Override
			public void onResponse(String response) {
				Methods.closeProgressDialog();
				Log.e("droid","get all NewConvertYeardetails----------"+ response);

				if(response.contains("status"))
				{
					ResponseMessageModel2 respModel=gson.fromJson(response, ResponseMessageModel2.class);
					if(respModel.getMessage().getStatus()=="401"){
						Methods.longToast("User name or Password is incorrect", DashboardListDetailsActivity.this);
					}else{
						Methods.longToast(respModel.getMessage().getMessage(), DashboardListDetailsActivity.this);
					}
				}else{
					
					try {
							JSONObject jsonobj=new JSONObject(response);
							JSONArray obj=jsonobj.getJSONArray("message");
							Log.d("NonStop","Response: " + obj.toString());
							//ftv_name,surname
							txtMembershipNo.setText((obj.getJSONObject(0).getString("name").equals("null"))?"":obj.getJSONObject(0).getString("name"));
							txtMemberfName.setText((obj.getJSONObject(0).getString("ftv_name").equals("null"))?"":obj.getJSONObject(0).getString("ftv_name"));
							txtMemberlName.setText((obj.getJSONObject(0).getString("surname").equals("null"))?"":obj.getJSONObject(0).getString("surname"));

							String str=obj.getJSONObject(0).getString("marital_info");
							if(!str.equals(""))
							txtMemberMartialInfo.setSelection(adapterMartial.getPosition(obj.getJSONObject(0).getString("marital_info")));
							
							
							if(!obj.getJSONObject(0).getString("date_of_birth").equals("null")){
								
								Date dob=dateFormatterService.parse(obj.getJSONObject(0).getString("date_of_birth"));
								txtMemberDateOfBirth.setText(dateFormatter.format(dob));
							}

//							if(!obj.getJSONObject(0).getString("date_of_visit").equals("null")){
//
//								Date dob=dateFormatterService.parse(obj.getJSONObject(0).getString("date_of_visit"));
//								txtMemberDateOfJoining.setText(dateFormatter.format(dob));
//							}

							
							txtMemberPhone1.setText((obj.getJSONObject(0).getString("phone_1").equals("null"))?"":obj.getJSONObject(0).getString("phone_1"));
							txtEmailID1.setText((obj.getJSONObject(0).getString("email_id").equals("null"))?"":obj.getJSONObject(0).getString("email_id"));
							txtMemberHomeAddress.setText((obj.getJSONObject(0).getString("address").equals("null"))?"":obj.getJSONObject(0).getString("address"));
							txtOfficeAddress.setText((obj.getJSONObject(0).getString("office_address").equals("null"))?"":obj.getJSONObject(0).getString("office_address"));
							txtMemberPhone2.setText((obj.getJSONObject(0).getString("phone_2").equals("null"))?"":obj.getJSONObject(0).getString("phone_2"));
							txtEmailID2.setText((obj.getJSONObject(0).getString("email_id_2").equals("null")) ? "" : obj.getJSONObject(0).getString("email_id_2"));

//							Log.d("Nonstop", " Gender: " + obj.getJSONObject(0).getString("sex"));
//
//							txtGender.setSelection(obj.getJSONObject(0).getString("sex").equals("null") ? 0 : 1);

	 						Log.e("NonStop Image Url", obj.getJSONObject(0).getString("image"));

							String Imageurl= obj.getJSONObject(0).getString("image");

						    Log.e("NonStop Image Url", ImageUrl.imageUrl + Imageurl);

						if(!Imageurl.equals("null")) {
							Picasso.with(DashboardListDetailsActivity.this).load(ImageUrl.imageUrl+Imageurl).into(imgProfilePic);
						}

							
							
						} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
						} catch (ParseException e) {
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

				MeetingListRequestModel model=new MeetingListRequestModel();
				model.setUsername(mPreferenceHelper.getString(Commons.USER_EMAILID));
				model.setUserpass(mPreferenceHelper.getString(Commons.USER_PASSWORD));

				JSONObject jsonobj=new JSONObject();
				try {
					
					jsonobj.put("username", mPreferenceHelper.getString(Commons.USER_EMAILID));
					jsonobj.put("userpass", mPreferenceHelper.getString(Commons.USER_PASSWORD));
					
					//Membership Strength
					
					if(tblname.equals("Membership Strength")){
					
						 jsonobj.put("tbl", "Membership Strength");
						 
						 JSONObject obj=new JSONObject();
						 
						 obj.put("flag","Total Member Count");
						
						jsonobj.put("filters", obj);
					
					}else{
					
						if(tblname.equals("Invitees and Contacts"))
						  jsonobj.put("tbl", "Invitees and Contacts");
						else
						  jsonobj.put("tbl", "First Timer");
						
					}
					
					jsonobj.put("name", name);
					
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				String dataString=jsonobj.toString();
			//	String dataString=gson.toJson(model, MeetingListRequestModel.class);

				Log.e("droid","data passed is ::::::::"+dataString);
				params.put(DashboardDataService.DATA, dataString);
				return params; 
			}
		};

		App.getInstance().addToRequestQueue(reqDashboard, "reqDashboard");
		reqDashboard.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
	}
	
private void UpdateDashboardDataService() {
	StringRequest reqDashboard=new StringRequest(Method.POST,UpdateAllDetailsService.SERVICE_URL,new Listener<String>() {

		@Override
		public void onResponse(String response) {
			Methods.closeProgressDialog();
			Log.e("droid","get all NewConvertYeardetails----------"+ response);

			if(response.contains("status"))
			{
				
				try {
						JSONObject jsonobj=new JSONObject(response);
						String status=jsonobj.getJSONObject("message").getString("status");
						
						if(status.equals("200")){
							
							Methods.longToast(jsonobj.getJSONObject("message").getString("message"), DashboardListDetailsActivity.this);
							
						}else{
							
							Methods.longToast("Some error occured,please try again later", DashboardListDetailsActivity.this);
						}
						
						
					} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
					}
				
				}
			
			finish();
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

			MeetingListRequestModel model=new MeetingListRequestModel();
			model.setUsername(mPreferenceHelper.getString(Commons.USER_EMAILID));
			model.setUserpass(mPreferenceHelper.getString(Commons.USER_PASSWORD));

			JSONObject jsonobj=new JSONObject();
			try {
				
				jsonobj.put("username", mPreferenceHelper.getString(Commons.USER_EMAILID));
				jsonobj.put("userpass", mPreferenceHelper.getString(Commons.USER_PASSWORD));
				
				if(tblname.equals("Membership Strength")){
					
					 jsonobj.put("tbl", "Membership Strength");
					 
					
				
				}else{
				
					if(tblname.equals("Invitees and Contacts"))
					  jsonobj.put("tbl", "Invitees and Contacts");
					else
					  jsonobj.put("tbl", "First Timer");
					
				}
				
				 JSONObject obj=new JSONObject();
				 obj.put("name", name);
				 obj.put("ftv_name", txtMemberfName.getText().toString());
				 obj.put("surname", txtMemberlName.getText().toString());
				 
				 
				 if(!txtMemberDateOfBirth.getText().toString().equals("")){
						
						Date dob=dateFormatter.parse(txtMemberDateOfBirth.getText().toString());
						obj.put("date_of_birth", dateFormatterService.format(dob));
					}

				if(!txtMemberDateOfJoining.getText().toString().equals("")){

					Date dob=dateFormatter.parse(txtMemberDateOfJoining.getText().toString());
					obj.put("date_of_visit", dateFormatterService.format(dob));
				}

				// obj.put("date_of_birth", txtMemberDateOfBirth.getText().toString());
				
				 
				 obj.put("phone_1", txtMemberPhone1.getText().toString());
				 obj.put("email_id", txtEmailID1.getText().toString());
				 obj.put("address", txtMemberHomeAddress.getText().toString());
				 obj.put("office_address", txtOfficeAddress.getText().toString());
				 obj.put("email_id_2", txtEmailID2.getText().toString());
				 obj.put("phone_2", txtMemberPhone2.getText().toString());
				 obj.put("is_new_convert", spisnewconvert.getSelectedItem().toString());
				 obj.put("marital_info", txtMemberMartialInfo.getSelectedItem().toString());
				 jsonobj.put("records", obj);
				
				
				//marital_info,phone_1,email_id,address,office_address
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			String dataString=jsonobj.toString();
		//	String dataString=gson.toJson(model, MeetingListRequestModel.class);

			Log.e("Request droid","data passed is ::::::::"+dataString);
			params.put(DashboardDataService.DATA, dataString);
			return params; 
		}
	};

	App.getInstance().addToRequestQueue(reqDashboard, "reqDashboard");
	reqDashboard.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
}



private void selectImage() 
{
	final CharSequence[] items = { "Take Photo", "Choose from Library",	"Cancel" };

	AlertDialog.Builder builder = new AlertDialog.Builder(DashboardListDetailsActivity.this);
	builder.setTitle("Add Photo!");
	builder.setItems(items, new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int item) {
			if (items[item].equals("Take Photo")) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(intent, REQUEST_CAMERA);
			} else if (items[item].equals("Choose from Library")) {
				Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				intent.setType("image/*");
				startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
			} else if (items[item].equals("Cancel")) {
				dialog.dismiss();
			}
		}
	});
	builder.show();

}

@Override
public void onActivityResult(int requestCode, int resultCode, Intent data) 
{
	super.onActivityResult(requestCode, resultCode, data);

	if (resultCode == Activity.RESULT_OK) 
	{

		if (requestCode == SELECT_FILE)
			onSelectFromGalleryResult(data);
		else if (requestCode == REQUEST_CAMERA)
			onCaptureImageResult(data);
	}
}

private void onCaptureImageResult(Intent data) 
{
	Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
	ByteArrayOutputStream bytes = new ByteArrayOutputStream();
	thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

	File destination = new File(Environment.getExternalStorageDirectory(),
			System.currentTimeMillis() + ".jpg");

//	filepath = destination.getAbsolutePath();

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
	
	if(NetworkHelper.isOnline(DashboardListDetailsActivity.this)){
		Methods.showProgressDialog(DashboardListDetailsActivity.this);
		uploadProfPicService(tblname);
	
	}else{
	
		Methods.longToast("Please connect to Internet", this);
	
	}
	
}

@SuppressWarnings("deprecation")
private void onSelectFromGalleryResult(Intent data) 
{
	Uri selectedImageUri = data.getData();
	String[] projection = { MediaColumns.DATA };
	Cursor cursor = managedQuery(selectedImageUri, projection, null, null,null);
	int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
	cursor.moveToFirst();

	String selectedImagePath = cursor.getString(column_index);

	//filepath = selectedImagePath;
//	Log.e("TAG", "File Path :" + filepath);

	Bitmap bm;
	BitmapFactory.Options options = new BitmapFactory.Options();
	options.inJustDecodeBounds = true;
	BitmapFactory.decodeFile(selectedImagePath, options);
	final int REQUIRED_SIZE = 200;
	int scale = 1;
	while (options.outWidth / scale / 2 >= REQUIRED_SIZE
			&& options.outHeight / scale / 2 >= REQUIRED_SIZE)
		scale *= 2;
	options.inSampleSize = scale;
	options.inJustDecodeBounds = false;
	bm = BitmapFactory.decodeFile(selectedImagePath, options);

	imgProfilePic.setImageBitmap(bm);
	
	if(NetworkHelper.isOnline(DashboardListDetailsActivity.this)){
		Methods.showProgressDialog(DashboardListDetailsActivity.this);
		uploadProfPicService(tblname);
	}else{
		Methods.longToast("Please connect to Internet", this);
	}
	
	
}

protected void uploadProfPicService(final String tbl) {
	StringRequest reqSendProfilePic=new StringRequest(Method.POST,ProfilePicUploadService.SERVICE_URL,new Listener<String>() {

		@Override
		public void onResponse(String response) {
			Methods.closeProgressDialog();
			Log.d(" responce droid","get reqSendProfilePic ---------------"+ response);

			StatusCheckModel statusCheckModel=gson.fromJson(response, StatusCheckModel.class);
			if(null != statusCheckModel.getStatus() && statusCheckModel.getStatus().trim().length() >0){
				ResponseMessageModel respModel=gson.fromJson(response, ResponseMessageModel.class);
				if(respModel.getStatus()=="401"){
					Methods.longToast("User name or Password is incorrect", DashboardListDetailsActivity.this);
				}else{
					Methods.longToast(respModel.getMessage(), DashboardListDetailsActivity.this);
				}
			}else{
				Methods.longToast("Profile Updated Succesfully", DashboardListDetailsActivity.this);
			}
		//	finish();

		}
	},new ErrorListener() {

		@Override
		public void onErrorResponse(VolleyError error) {
			Methods.closeProgressDialog();
			Log.e("droid","get reqsaveMeeting error---------------"+ error.getCause());
			
		}
	}){
		@Override
		protected Map<String, String> getParams() throws AuthFailureError{
			Map<String, String> params = new HashMap<String, String>();

			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPreferredConfig = Bitmap.Config.ARGB_8888;

			
			
			BitmapDrawable bitmapDrawable = ((BitmapDrawable) imgProfilePic.getDrawable());
			Bitmap bitmap = bitmapDrawable.getBitmap();

			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
			
			
			byte[] byteArray = byteArrayOutputStream .toByteArray();

			String fdata = Base64.encodeToString(byteArray, Base64.DEFAULT);

			MeetingListRequestModel model=new MeetingListRequestModel();
			model.setUsername(mPreferenceHelper.getString(Commons.USER_EMAILID));
			model.setUserpass(mPreferenceHelper.getString(Commons.USER_PASSWORD));
			model.setFilename(txtMembershipNo.getText().toString()+".jpg");
			model.setFdata(fdata);
		//	model.setTbl(tbl);//mPreferenceHelper.getString(Commons.USER_DEFKEY)
			
			if(tblname.equals("Membership Strength")){
				
				model.setTbl("Membership Strength");
				
			}else{
			
				if(tblname.equals("Invitees and Contacts"))
					model.setTbl("Invitees and Contacts");
				else
					model.setTbl("First Timer");
				
			}
			
			model.setName(txtMembershipNo.getText().toString());

			String dataString=gson.toJson(model, MeetingListRequestModel.class);

			Log.e("Request droid", dataString);
			params.put(GetMemberProfileService.DATA, dataString);
			return params;
		}
	};

	App.getInstance().addToRequestQueue(reqSendProfilePic, "reqSendProfilePic");
	reqSendProfilePic.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));

}
	private void openDialog() {
		final CharSequence[] items = { "Take Photo","Choose from Gallery",
				"Cancel" };//

		AlertDialog.Builder builder = new AlertDialog.Builder(DashboardListDetailsActivity.this);
		builder.setTitle("Add Photo!");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				if (items[item].equals("Take Photo")) {
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					startActivityForResult(intent, REQUEST_CAMERA);
				} else if (items[item].equals("Choose from Gallery")) {
					Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					intent.setType("image/*");
					startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
				} else if (items[item].equals("Cancel")) {
					dialog.dismiss();
				}
			}
		});
		builder.show();
	}

}
