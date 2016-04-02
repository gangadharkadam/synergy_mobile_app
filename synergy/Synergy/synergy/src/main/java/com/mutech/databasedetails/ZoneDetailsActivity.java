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

public class ZoneDetailsActivity extends AppCompatActivity {
	
	EditText txtZoneCode,txtzoneName,txtzoneRegion,txtzoneContactPhone,txtzoneContactEmailId;

	private PreferenceHelper mPreferenceHelper;
	private Gson gson;
	String cellcode;
	
	Button btnSavePCF;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zone_details);
	
		getSupportActionBar().hide();
		
		txtZoneCode=(EditText) findViewById(R.id.txtZoneCode);
		txtzoneName=(EditText) findViewById(R.id.txtzoneName);
		txtzoneRegion=(EditText) findViewById(R.id.txtzoneRegion);
		txtzoneContactPhone=(EditText) findViewById(R.id.txtzoneContactPhone);
		txtzoneContactEmailId=(EditText) findViewById(R.id.txtzoneContactEmailId);
		
		btnSavePCF=(Button) findViewById(R.id.btnSavePCF);
		
		cellcode=getIntent().getStringExtra("cellcode"); 
	
		
		mPreferenceHelper=new PreferenceHelper(this);
		gson=new Gson();
		
		btnSavePCF.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				if(NetworkHelper.isOnline(ZoneDetailsActivity.this)){
					if(InputValidation.isPhoneNumber(txtzoneContactPhone, false) &&
							InputValidation.hasText(txtzoneName)) {
						Methods.showProgressDialog(ZoneDetailsActivity.this);
						UpdateDashboardDataService();
					} else {
						new AlertDialog.Builder(ZoneDetailsActivity.this)
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
				}else{
					Methods.longToast("Please connect to Internet", ZoneDetailsActivity.this);
				}
				
				
			}
			});
		
		if(NetworkHelper.isOnline(this)){
			Methods.showProgressDialog(this);
			getZoneDetails();
			
		}else{
			Methods.longToast("Please connect to Internet", this);
		}
		
	}

	private void getZoneDetails() {

		StringRequest reqgetCellDetails=new StringRequest(Method.POST,GetCellDetailsService.SERVICE_URL,new Listener<String>() {

			@Override
			public void onResponse(String response) {
				Methods.closeProgressDialog();
				Log.e("getZoneDetails ","getZoneDetails ---------------"+ response);
				try {
				
					JSONObject jsonobj=new JSONObject(response);
					JSONArray jarray=jsonobj.getJSONArray("message");
				
					if(!jarray.getJSONObject(0).getString("zone_code").equals("null"))
						txtZoneCode.setText(jarray.getJSONObject(0).getString("zone_code"));
											
					if(!jarray.getJSONObject(0).getString("zone_name").equals("null"));
					txtzoneName.setText(jarray.getJSONObject(0).getString("zone_name"));
					
					if(!jarray.getJSONObject(0).getString("region").equals("null"))
						txtzoneRegion.setText(jarray.getJSONObject(0).getString("region"));
					
					if(!jarray.getJSONObject(0).getString("contact_phone_no").equals("null"))
						txtzoneContactPhone.setText(jarray.getJSONObject(0).getString("contact_phone_no"));
					
					if(!jarray.getJSONObject(0).getString("contact_email_id").equals("null"))
						txtzoneContactEmailId.setText(jarray.getJSONObject(0).getString("contact_email_id"));
					
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
							Methods.longToast("Access Denied", ZoneDetailsActivity.this);
				}
				else
							Methods.longToast("Some Error Occured,please try again later", ZoneDetailsActivity.this);

				
				
			}

		}){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError{
				Map<String, String> params = new HashMap<String, String>();

				MeetingListRequestModel model=new MeetingListRequestModel();
				model.setUsername(mPreferenceHelper.getString(Commons.USER_EMAILID));
				model.setUserpass(mPreferenceHelper.getString(Commons.USER_PASSWORD));
				model.setTbl("Zones");
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
								
								Methods.longToast(jsonobj.getJSONObject("message").getString("message"), ZoneDetailsActivity.this);
								finish();
							}else{
								
								Methods.longToast("Some error occured,please try again later", ZoneDetailsActivity.this);
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
					jsonobj.put("tbl", "Zones");
					
					JSONObject obj=new JSONObject();
					
					 obj.put("name", cellcode);
					 obj.put("pcf_name", txtzoneName.getText().toString());
					 obj.put("contact_phone_no", txtzoneContactPhone.getText().toString());
					 obj.put("contact_email_id",txtzoneContactEmailId.getText().toString());
					
					
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
