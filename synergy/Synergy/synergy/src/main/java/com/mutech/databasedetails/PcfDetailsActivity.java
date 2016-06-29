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
import com.mutech.synergy.activities.cellMasters.CreateCellMasterActivity;
import com.mutech.synergy.models.HigherHierarchyRespModel;
import com.mutech.synergy.models.MeetingListRequestModel;
import com.mutech.synergy.models.HigherHierarchyRespModel.HHSubModel;
import com.mutech.synergy.utils.InputValidation;
import com.mutech.synergy.utils.Methods;
import com.mutech.synergy.utils.NetworkHelper;
import com.mutech.synergy.utils.PreferenceHelper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.provider.ContactsContract.CommonDataKinds;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class PcfDetailsActivity extends AppCompatActivity {
	
	
	EditText txtPCFName,txtPCFContactPhn,txtPCFContactEmailId,txtPCFZone,txtPCFRegion,txtGroupChurch,txtChurch;
	Spinner spnPCFZone,spnPCFRegion,spnPCFChurch;
	
	Button btnSavePCF;
	private PreferenceHelper mPreferenceHelper;
	private Gson gson;
	
	String cellcode;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pcf_details);
		
		mPreferenceHelper=new PreferenceHelper(this);
		gson=new Gson();
	
		txtChurch=(EditText) findViewById(R.id.txtChurch);
		txtPCFRegion=(EditText) findViewById(R.id.txtPCFRegion);
		txtPCFZone=(EditText) findViewById(R.id.txtPCFZone);
		txtGroupChurch=(EditText) findViewById(R.id.txtGroupChurch);
		
		//txtPCFCode=(EditText) findViewById(R.id.txtPCFCode);
		txtPCFName=(EditText) findViewById(R.id.txtPCFName);
		txtPCFContactPhn=(EditText) findViewById(R.id.txtPCFContactPhn);
		txtPCFContactEmailId=(EditText) findViewById(R.id.txtPCFContactEmailId);
		
		spnPCFZone=(Spinner) findViewById(R.id.spnPCFZone);
		spnPCFRegion=(Spinner) findViewById(R.id.spnPCFRegion);
		spnPCFChurch=(Spinner) findViewById(R.id.spnPCFChurch);
		
		
		btnSavePCF=(Button) findViewById(R.id.btnSavePCF);
				
		
		cellcode=getIntent().getStringExtra("cellcode");
		getSupportActionBar().hide();
		
		txtPCFZone.setEnabled(false);
		txtChurch.setEnabled(false);
		txtPCFRegion.setEnabled(false);
		txtGroupChurch.setEnabled(false);
		
		
		
		if(NetworkHelper.isOnline(this)){
			Methods.showProgressDialog(this);
			getCellDetails();
			
		}else{
			Methods.longToast("Please connect to Internet", this);
		}
		
		
		btnSavePCF.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(NetworkHelper.isOnline(PcfDetailsActivity.this)){
					if(isValid()) {
						Methods.showProgressDialog(PcfDetailsActivity.this);
						UpdateDashboardDataService();
					}
				}else{
					Methods.longToast("Please connect to Internet",PcfDetailsActivity.this);
				}
				
			}
		});
		
	}

	public boolean isValid() {

		if(!InputValidation.hasText(txtPCFName)) {
			AlertDialog dialog =new AlertDialog.Builder(PcfDetailsActivity.this)
					.setCancelable(false)
					.setTitle("Mandatory Input")
					.setMessage("Please enter PCF Name")
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
		if(!InputValidation.isPhoneNumber(txtPCFContactPhn, false)) {
			AlertDialog dialog =new AlertDialog.Builder(PcfDetailsActivity.this)
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

	private void updatePcfDetails() {

		StringRequest reqgetCellDetails=new StringRequest(Method.POST,UpdateAllDetailsService.SERVICE_URL,new Listener<String>() {

			@Override
			public void onResponse(String response) {
				Methods.closeProgressDialog();
				Log.d("droid","getpcfDetails ---------------"+ response);
			
				try {
				
					JSONObject jsonobj=new JSONObject(response);
					String status=jsonobj.getJSONObject("message").getString("status");
					if(status.equals("200")){
						Methods.longToast(jsonobj.getJSONObject("message").getString("message"), PcfDetailsActivity.this);
					}else{
						
						Methods.longToast("Some Error Occured,please try again later", PcfDetailsActivity.this);
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
				Log.d("droid","get reqgetTopHierarchy error---------------"+ error.getCause());

				if(error.networkResponse.statusCode==403){
							Methods.longToast("Access Denied", PcfDetailsActivity.this);
				}
				else
							Methods.longToast("Some Error Occured,please try again later", PcfDetailsActivity.this);

				
				
			}

		}){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError{
				Map<String, String> params = new HashMap<String, String>();

				MeetingListRequestModel model=new MeetingListRequestModel();
				/*model.setUsername(mPreferenceHelper.getString(Commons.USER_EMAILID));
				model.setUserpass(mPreferenceHelper.getString(Commons.USER_PASSWORD));
				model.setTbl("PCFs");
				model.setName(cellcode);
*/
				JSONObject jsonobj=new JSONObject();
				
				
				try {
					
					jsonobj.put("username", mPreferenceHelper.getString(Commons.USER_EMAILID));
					jsonobj.put("userpass", mPreferenceHelper.getString(Commons.USER_PASSWORD));
					jsonobj.put("tbl", "PCFs");
					jsonobj.put("name", cellcode);
					
					//jsonobj.put("pcf_code",txtPCFCode.getText().toString());
					jsonobj.put("pcf_name",txtPCFName.getText().toString());
				
					jsonobj.put("zone",txtPCFZone.getText().toString());
					jsonobj.put("region",txtPCFRegion.getText().toString());
					jsonobj.put("church_group",txtGroupChurch.getText().toString());
					
					jsonobj.put("church",txtChurch.getText().toString());
					jsonobj.put("contact_email_id",txtPCFContactEmailId.getText().toString());
					jsonobj.put("contact_phone_no",txtPCFContactPhn.getText().toString());
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
				
				String dataString=jsonobj.toString();//gson.toJson(model, MeetingListRequestModel.class);

				Log.e("droid", dataString);
				params.put(GetHigherHierarchyService.DATA, dataString);
				return params;
			}
		};

		App.getInstance().addToRequestQueue(reqgetCellDetails, "reqgetCellDetails");
		reqgetCellDetails.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));

	}

	
	private void getCellDetails() {

		StringRequest reqgetCellDetails=new StringRequest(Method.POST,GetCellDetailsService.SERVICE_URL,new Listener<String>() {

			@Override
			public void onResponse(String response) {
				Methods.closeProgressDialog();
				Log.d("droid","getpcfDetails ---------------"+ response);
				try {
				
					JSONObject jsonobj=new JSONObject(response);
					JSONArray jarray=jsonobj.getJSONArray("message");
				/*	if(!jarray.getJSONObject(0).getString("pcf_code").equals("null"))
						txtPCFCode.setText(jarray.getJSONObject(0).getString("pcf_code"));*/
											
					if(!jarray.getJSONObject(0).getString("pcf_name").equals("null"));
						txtPCFName.setText(jarray.getJSONObject(0).getString("pcf_name"));
					
					if(!jarray.getJSONObject(0).getString("zone").equals("null"))
						txtPCFZone.setText(jarray.getJSONObject(0).getString("zone"));
					
					if(!jarray.getJSONObject(0).getString("region").equals("null"))
						txtPCFRegion.setText(jarray.getJSONObject(0).getString("zone"));
					
					if(!jarray.getJSONObject(0).getString("church_group").equals("null"))
						txtGroupChurch.setText(jarray.getJSONObject(0).getString("church_group"));
					
					
					if(!jarray.getJSONObject(0).getString("church").equals("null"))
					txtChurch.setText(jarray.getJSONObject(0).getString("church"));
					
					if(!jarray.getJSONObject(0).getString("contact_phone_no").equals("null"))
					txtPCFContactPhn.setText(jarray.getJSONObject(0).getString("contact_phone_no"));
					
					if(!jarray.getJSONObject(0).getString("contact_email_id").equals("null"))
					txtPCFContactEmailId.setText(jarray.getJSONObject(0).getString("contact_email_id"));
					
					
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
				Log.d("droid","get reqgetTopHierarchy error---------------"+ error.getCause());

				if(error.networkResponse.statusCode==403){
							Methods.longToast("Access Denied", PcfDetailsActivity.this);
				}
				else
							Methods.longToast("Some Error Occured,please try again later", PcfDetailsActivity.this);

				
				
			}

		}){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError{
				Map<String, String> params = new HashMap<String, String>();

				MeetingListRequestModel model=new MeetingListRequestModel();
				model.setUsername(mPreferenceHelper.getString(Commons.USER_EMAILID));
				model.setUserpass(mPreferenceHelper.getString(Commons.USER_PASSWORD));
				model.setTbl("PCFs");
				model.setName(cellcode);

				String dataString=gson.toJson(model, MeetingListRequestModel.class);

				Log.d("droid", dataString);
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
								
								Methods.longToast(jsonobj.getJSONObject("message").getString("message"), PcfDetailsActivity.this);
								finish();
							}else{
								
								Methods.longToast("Some error occured,please try again later", PcfDetailsActivity.this);
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

				JSONObject jsonobj=new JSONObject();
				try {
					
					jsonobj.put("username", mPreferenceHelper.getString(Commons.USER_EMAILID));
					jsonobj.put("userpass", mPreferenceHelper.getString(Commons.USER_PASSWORD));
					jsonobj.put("tbl", "PCFs");
					
					JSONObject obj=new JSONObject();
					
					 obj.put("name", cellcode);
					 obj.put("pcf_name", txtPCFName.getText().toString());
					 obj.put("contact_phone_no", txtPCFContactPhn.getText().toString());
					 obj.put("contact_email_id",txtPCFContactEmailId.getText().toString());
					
					
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
