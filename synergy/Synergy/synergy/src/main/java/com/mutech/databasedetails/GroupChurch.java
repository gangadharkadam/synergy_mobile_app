package com.mutech.databasedetails;

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
import android.widget.TextView;

public class GroupChurch extends AppCompatActivity {
	
	EditText txtGrChurchCode,txtGrChurchName,txtGrChurchZone,txtGrChurchRegion,txtGrChurchContactPhone,txtGrChurchContactEmailId;

	private PreferenceHelper mPreferenceHelper;
	private Gson gson;
	String cellcode;
	
	Button btnSavePCF;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_church);
		
		getSupportActionBar().hide();
		
		txtGrChurchCode=(EditText) findViewById(R.id.txtGrChurchCode);
		txtGrChurchName=(EditText) findViewById(R.id.txtGrChurchName);
		txtGrChurchZone=(EditText) findViewById(R.id.txtGrChurchZone);
		txtGrChurchRegion=(EditText) findViewById(R.id.txtGrChurchRegion);
		txtGrChurchContactPhone=(EditText) findViewById(R.id.txtGrChurchContactPhone);
		txtGrChurchContactEmailId=(EditText) findViewById(R.id.txtGrChurchContactEmailId);
		btnSavePCF=(Button) findViewById(R.id.btnSavePCF);
		
		cellcode=getIntent().getStringExtra("cellcode"); 
	
		
		mPreferenceHelper=new PreferenceHelper(this);
		gson=new Gson();
		
		btnSavePCF.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				if(NetworkHelper.isOnline(GroupChurch.this)){
					if(isValid()) {
						Methods.showProgressDialog(GroupChurch.this);
						UpdateDashboardDataService();
					}
				}else{
					Methods.longToast("Please connect to Internet", GroupChurch.this);
				}
				
				
			}
			});
		
		
		if(NetworkHelper.isOnline(this)){
			Methods.showProgressDialog(this);
			getGrChurchDetails();
			
		}else{
			Methods.longToast("Please connect to Internet", this);
		}
	}

	public boolean isValid() {

		if(!InputValidation.hasText(txtGrChurchName)) {
			AlertDialog dialog =new AlertDialog.Builder(GroupChurch.this)
					.setCancelable(false)
					.setTitle("Mandatory Input")
					.setMessage("Please enter Group Church Name")
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
		if(!InputValidation.isPhoneNumber(txtGrChurchContactPhone, false)) {
			AlertDialog dialog =new AlertDialog.Builder(GroupChurch.this)
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

	private void getGrChurchDetails() {

		StringRequest reqgetCellDetails=new StringRequest(Method.POST,GetCellDetailsService.SERVICE_URL,new Listener<String>() {

			@Override
			public void onResponse(String response) {
				Methods.closeProgressDialog();
				Log.e("getGrChurchDetails","getGrChurchDetails ---------------"+ response);
				try {
				
					JSONObject jsonobj=new JSONObject(response);
					JSONArray jarray=jsonobj.getJSONArray("message");
				
					if(!jarray.getJSONObject(0).getString("church_group_code").equals("null"))
						txtGrChurchCode.setText(jarray.getJSONObject(0).getString("church_group_code"));
											
					if(!jarray.getJSONObject(0).getString("church_group").equals("null"));
					txtGrChurchName.setText(jarray.getJSONObject(0).getString("church_group"));
					
					if(!jarray.getJSONObject(0).getString("zone").equals("null"))
						txtGrChurchZone.setText(jarray.getJSONObject(0).getString("zone"));
					
					if(!jarray.getJSONObject(0).getString("region").equals("null"))
						txtGrChurchRegion.setText(jarray.getJSONObject(0).getString("region"));
					
					if(!jarray.getJSONObject(0).getString("contact_phone_no").equals("null"))
						txtGrChurchContactPhone.setText(jarray.getJSONObject(0).getString("contact_phone_no"));
					
					if(!jarray.getJSONObject(0).getString("contact_email_id").equals("null"))
						txtGrChurchContactPhone.setText(jarray.getJSONObject(0).getString("contact_email_id"));
					
					
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
							Methods.longToast("Access Denied", GroupChurch.this);
				}
				else
							Methods.longToast("Some Error Occured,please try again later", GroupChurch.this);

				
				
			}

		}){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError{
				Map<String, String> params = new HashMap<String, String>();

				MeetingListRequestModel model=new MeetingListRequestModel();
				model.setUsername(mPreferenceHelper.getString(Commons.USER_EMAILID));
				model.setUserpass(mPreferenceHelper.getString(Commons.USER_PASSWORD));
				model.setTbl("Group Churches");
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
								
								Methods.longToast(jsonobj.getJSONObject("message").getString("message"), GroupChurch.this);
								finish();
							}else{
								
								Methods.longToast("Some error occured,please try again later", GroupChurch.this);
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
					jsonobj.put("tbl", "Group Churches");
					
					JSONObject obj=new JSONObject();
					
					 obj.put("name", cellcode);
					 obj.put("pcf_name", txtGrChurchName.getText().toString());
					 obj.put("contact_phone_no", txtGrChurchContactPhone.getText().toString());
					 obj.put("contact_email_id",txtGrChurchContactEmailId.getText().toString());
					
					
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
