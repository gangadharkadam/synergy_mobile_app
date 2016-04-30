package com.mutech.databasedetails;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.mutech.synergy.R.id;
import com.mutech.synergy.R.layout;
import com.mutech.synergy.R.menu;
import com.mutech.synergy.SynergyValues.Commons;
import com.mutech.synergy.SynergyValues.Web.DashboardDataService;
import com.mutech.synergy.SynergyValues.Web.GetCellDetailsService;
import com.mutech.synergy.SynergyValues.Web.GetHigherHierarchyService;
import com.mutech.synergy.SynergyValues.Web.UpdateAllDetailsService;
import com.mutech.synergy.models.MeetingListRequestModel;
import com.mutech.synergy.utils.InputValidation;
import com.mutech.synergy.utils.Methods;
import com.mutech.synergy.utils.NetworkHelper;
import com.mutech.synergy.utils.PreferenceHelper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class SrCellDetailsActivity extends AppCompatActivity {
	
	private EditText txtChurch,txtChurchgroup,txtpcf,txtRegion,txtZone,txtSeniorCellCode,txtSeniorCellName,txtSeniorCellMeetingLoc,txtSrCellContactPhn,txtSrCellContactEmailId;
	private Button btnSeniorCellSave;
	
	private PreferenceHelper mPreferenceHelper;
	private Gson gson;
	
	String cellcode;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sr_cell_details);
		
		txtSeniorCellCode=(EditText) findViewById(R.id.txtSeniorCellCode);
		txtSeniorCellName=(EditText) findViewById(R.id.txtSeniorCellName);
		txtZone=(EditText) findViewById(R.id.txtZone);
		txtRegion=(EditText) findViewById(R.id.txtRegion);
		txtpcf=(EditText) findViewById(R.id.txtpcf);
		txtChurchgroup=(EditText) findViewById(R.id.txtChurchgroup);
		txtChurch=(EditText) findViewById(R.id.txtChurch);
		txtSrCellContactPhn=(EditText) findViewById(R.id.txtSrCellContactPhn);
		txtSrCellContactEmailId=(EditText) findViewById(R.id.txtSrCellContactEmailId);
		txtSeniorCellMeetingLoc=(EditText) findViewById(R.id.txtSeniorCellMeetingLoc);
		btnSeniorCellSave=(Button) findViewById(R.id.btnSeniorCellSave);
		
		
		
		mPreferenceHelper=new PreferenceHelper(this);
		gson=new Gson();
		getSupportActionBar().hide();
		
		cellcode=getIntent().getStringExtra("cellcode"); 
		
		
		txtChurch.setEnabled(false);
		txtChurchgroup.setEnabled(false);
		txtSeniorCellCode.setEnabled(false);
		txtZone.setEnabled(false);		
		txtRegion.setEnabled(false);
		txtpcf.setEnabled(false);
		
		btnSeniorCellSave.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(NetworkHelper.isOnline(SrCellDetailsActivity.this)){
					if(isValid()) {
						Methods.showProgressDialog(SrCellDetailsActivity.this);
						UpdateDashboardDataService();
					}
				}else{
					Methods.longToast("Please connect to Internet",SrCellDetailsActivity.this);
				}
			
			}
		});
		
		
		if(NetworkHelper.isOnline(this)){
			Methods.showProgressDialog(this);
			getCellDetails();
			
		}else{
			Methods.longToast("Please connect to Internet", this);
		}
	}

	public boolean isValid() {

		if(!InputValidation.hasText(txtSeniorCellName)) {
			new AlertDialog.Builder(SrCellDetailsActivity.this)
					.setCancelable(false)
					.setTitle("Mandatory Input")
					.setMessage("Please enter Sr Cell Name")
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface, int i) {

						}
					})
					.show();
			return false;
		}
		if(!InputValidation.isPhoneNumber(txtSrCellContactPhn, false)) {
			new AlertDialog.Builder(SrCellDetailsActivity.this)
					.setCancelable(false)
					.setTitle("Invalid Input")
					.setMessage("Please enter a valid phone number")
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

	private void getCellDetails() {

		StringRequest reqgetCellDetails=new StringRequest(Method.POST,GetCellDetailsService.SERVICE_URL,new Listener<String>() {

			@Override
			public void onResponse(String response) {
				Methods.closeProgressDialog();
				Log.e("Responce droid","getSrCellDetails ---------------"+ response);
				try {
				
					JSONObject jsonobj=new JSONObject(response);
					JSONArray jarray=jsonobj.getJSONArray("message");
					
					if(!jarray.getJSONObject(0).getString("senior_cell_code").equals("null"))
					txtSeniorCellCode.setText(jarray.getJSONObject(0).getString("senior_cell_code"));
					
					if(!jarray.getJSONObject(0).getString("senior_cell_name").equals("null"))
						txtSeniorCellName.setText(jarray.getJSONObject(0).getString("senior_cell_name"));
					
					if(!jarray.getJSONObject(0).getString("zone").equals("null"))
						txtZone.setText(jarray.getJSONObject(0).getString("zone"));	
					
					if(!jarray.getJSONObject(0).getString("zone").equals("null"))
						txtZone.setText(jarray.getJSONObject(0).getString("zone"));	
					
					
					if(!jarray.getJSONObject(0).getString("region").equals("null"))
						txtRegion.setText(jarray.getJSONObject(0).getString("region"));
					
					if(!jarray.getJSONObject(0).getString("pcf").equals("null"))
						txtpcf.setText(jarray.getJSONObject(0).getString("pcf"));
					
					if(!jarray.getJSONObject(0).getString("church_group").equals("null"))
						txtChurchgroup.setText(jarray.getJSONObject(0).getString("church_group"));
					
					if(!jarray.getJSONObject(0).getString("church").equals("null"))
						txtChurch.setText(jarray.getJSONObject(0).getString("church"));
					
					if(!jarray.getJSONObject(0).getString("contact_phone_no").equals("null"))
						txtSrCellContactPhn.setText(jarray.getJSONObject(0).getString("contact_phone_no"));
					
					if(!jarray.getJSONObject(0).getString("contact_email_id").equals("null"))
						txtSrCellContactEmailId.setText(jarray.getJSONObject(0).getString("contact_email_id"));
					
					if(!jarray.getJSONObject(0).getString("meeting_location").equals("null"))
						txtSeniorCellMeetingLoc.setText(jarray.getJSONObject(0).getString("meeting_location"));
					
				
					
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				Methods.closeProgressDialog();
				
			}
		},new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Methods.closeProgressDialog();
				Log.d("droid","get reqgetSrCellDetails error---------------"+ error.getCause());

				if(error.networkResponse.statusCode==403){
							Methods.longToast("Access Denied", SrCellDetailsActivity.this);
				}
				else
							Methods.longToast("Some Error Occured,please try again later", SrCellDetailsActivity.this);

				
				
			}

		}){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError{
				Map<String, String> params = new HashMap<String, String>();

				MeetingListRequestModel model=new MeetingListRequestModel();
				model.setUsername(mPreferenceHelper.getString(Commons.USER_EMAILID));
				model.setUserpass(mPreferenceHelper.getString(Commons.USER_PASSWORD));
				model.setTbl("Senior Cells");
				model.setName(cellcode);

				String dataString=gson.toJson(model, MeetingListRequestModel.class);

				Log.e("Request droid", dataString);
				params.put(GetHigherHierarchyService.DATA, dataString);
				return params;
			}
		};

		App.getInstance().addToRequestQueue(reqgetCellDetails, "reqgetCellDetails");
		reqgetCellDetails.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));

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
								
								Methods.longToast(jsonobj.getJSONObject("message").getString("message"), SrCellDetailsActivity.this);
								finish();
							}else{
								
								Methods.longToast("Some error occured,please try again later", SrCellDetailsActivity.this);
								finish();
							}
							
							
						} catch (JSONException e) {
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
					jsonobj.put("tbl", "Senior Cells");
					
					JSONObject obj=new JSONObject();
					
					 obj.put("name", cellcode);
					 obj.put("senior_cell_name", txtSeniorCellName.getText().toString());
					 obj.put("contact_phone_no", txtSrCellContactPhn.getText().toString());
					 obj.put("contact_email_id", txtSrCellContactEmailId.getText().toString());
					 obj.put("meeting_location", txtSeniorCellMeetingLoc.getText().toString());
					
					 jsonobj.put("records", obj);
					
				} catch (JSONException e) {
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


}
