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
import com.google.gson.JsonArray;
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
import com.mutech.synergy.activities.dashboard.DashboardListDetailsActivity;
import com.mutech.synergy.models.HigherHierarchyRespModel;
import com.mutech.synergy.models.MeetingListRequestModel;
import com.mutech.synergy.models.HigherHierarchyRespModel.HHSubModel;
import com.mutech.synergy.utils.InputValidation;
import com.mutech.synergy.utils.Methods;
import com.mutech.synergy.utils.NetworkHelper;
import com.mutech.synergy.utils.PreferenceHelper;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

public class CellDetailsActivity extends AppCompatActivity {

	private EditText txtCellChurchgroup,txtCellChurch,txtCellZone,txtCellRegion,txtCellCode,txtCellName,txtCellMeetingLoc,txtCellContactPhn,txtCellContactEmailId,txtScniorCellName,txtPCFname;
	private Button btnSaveCellMaster,OnclickButton;
	private Spinner spnSeniorCell,spnCellZone,spnCellRegion,spnCellChurch,spnSeniorCellChurchgroup,txtPCF;
	private ArrayList<String> mZoneList,mRegionList,mChurchList,mSeniorCellList,mGrpChurchList,mPCFList;
	private PreferenceHelper mPreferenceHelper;
	private Gson gson;
	
	ImageView filterimg;
	
	String cellcode;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cell_details);
		
		
		mPreferenceHelper=new PreferenceHelper(this);
		gson=new Gson();
		
		btnSaveCellMaster=(Button) findViewById(R.id.btnSaveCellMaster);
		
		txtPCFname=(EditText) findViewById(R.id.txtPCF);
		txtCellZone=(EditText) findViewById(R.id.txtCellZone);
		txtCellRegion=(EditText) findViewById(R.id.txtCellRegion);
		txtCellChurch=(EditText) findViewById(R.id.txtCellChurch);
		txtCellChurchgroup=(EditText) findViewById(R.id.txtCellChurchgroup);
		
		txtScniorCellName=(EditText) findViewById(R.id.txtScniorCellName);
		txtCellCode=(EditText) findViewById(R.id.txtCellCode);
		txtCellName=(EditText) findViewById(R.id.txtCellName);
		spnSeniorCell=(Spinner) findViewById(R.id.spnSeniorCell);
		spnCellZone=(Spinner) findViewById(R.id.spnCellZone);
		spnCellRegion=(Spinner) findViewById(R.id.spnCellRegion);
		spnSeniorCellChurchgroup=(Spinner) findViewById(R.id.spnSeniorCellChurchgroup);
		spnCellChurch=(Spinner) findViewById(R.id.spnCellChurch);
		txtCellMeetingLoc=(EditText) findViewById(R.id.txtCellMeetingLoc);
		txtCellContactPhn=(EditText) findViewById(R.id.txtCellContactPhn);
		txtCellContactEmailId=(EditText) findViewById(R.id.txtCellContactEmailId);
		txtPCF = (Spinner) findViewById(R.id.txtPCFName);
		
		cellcode=getIntent().getStringExtra("cellcode"); 
		getSupportActionBar().hide();
		
		txtCellCode.setEnabled(false);
		txtScniorCellName.setEnabled(false);
		txtPCFname.setEnabled(false);
		txtCellZone.setEnabled(false);
		txtCellRegion.setEnabled(false);
		spnCellRegion.setEnabled(false);
		txtCellChurch.setEnabled(false);
		txtCellChurchgroup.setEnabled(false);
		
		btnSaveCellMaster.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		
				if(NetworkHelper.isOnline(CellDetailsActivity.this)){
					if (isValid()) {
						Methods.showProgressDialog(CellDetailsActivity.this);
						UpdateDashboardDataService();
					}
				}else{
					
					Methods.longToast("Please connect to Internet",CellDetailsActivity.this);
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

	private void getCellDetails() {

		StringRequest reqgetCellDetails=new StringRequest(Method.POST,GetCellDetailsService.SERVICE_URL,new Listener<String>() {

			@Override
			public void onResponse(String response) {
				Methods.closeProgressDialog();
				Log.d("droid","get reqgetTopHierarchy ---------------"+ response);
				try {
				
					JSONObject jsonobj=new JSONObject(response);
					JSONArray jarray=jsonobj.getJSONArray("message");
					
					
					String str=jarray.getJSONObject(0).getString("cell_code");
					
					if(str!=null)
					txtCellCode.setText(jarray.getJSONObject(0).getString("cell_code"));
					
					str=jarray.getJSONObject(0).getString("cell_name");
					
					if(str!=null)					
					txtCellName.setText(jarray.getJSONObject(0).getString("cell_name"));
					
					if(!jarray.getJSONObject(0).getString("senior_cell").equals("null"))
					txtScniorCellName.setText(jarray.getJSONObject(0).getString("senior_cell"));
					
					if(!jarray.getJSONObject(0).getString("pcf").equals("null"))
					txtPCFname.setText(jarray.getJSONObject(0).getString("pcf"));
					
					if(!jarray.getJSONObject(0).getString("zone").equals("null"))
					txtCellZone.setText(jarray.getJSONObject(0).getString("zone"));
					
					if(!jarray.getJSONObject(0).getString("region").equals("null"))
					txtCellRegion.setText(jarray.getJSONObject(0).getString("region"));
					
					if(!jarray.getJSONObject(0).getString("church").equals("null"))
					txtCellChurch.setText(jarray.getJSONObject(0).getString("church"));
					
					if(!jarray.getJSONObject(0).getString("church_group").equals("null"))
					txtCellChurchgroup.setText(jarray.getJSONObject(0).getString("church_group"));
					
					if(!jarray.getJSONObject(0).getString("contact_phone_no").equals("null"))
					txtCellContactPhn.setText(jarray.getJSONObject(0).getString("contact_phone_no"));
					
					if(!jarray.getJSONObject(0).getString("contact_email_id").equals("null"))
					txtCellContactEmailId.setText(jarray.getJSONObject(0).getString("contact_email_id"));
					
					if(!jarray.getJSONObject(0).getString("meeting_location").equals("null"))
						txtCellMeetingLoc.setText(jarray.getJSONObject(0).getString("meeting_location"));
					
					
					
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
							Methods.longToast("Access Denied", CellDetailsActivity.this);
				}
				else
							Methods.longToast("Some Error Occured,please try again later", CellDetailsActivity.this);

				
				
			}

		}){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError{
				Map<String, String> params = new HashMap<String, String>();

				MeetingListRequestModel model=new MeetingListRequestModel();
				model.setUsername(mPreferenceHelper.getString(Commons.USER_EMAILID));
				model.setUserpass(mPreferenceHelper.getString(Commons.USER_PASSWORD));
				model.setTbl("Cells");
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
				Log.e("droid", "get all NewConvertYeardetails----------" + response);

				if(response.contains("status"))
				{
					
					try {
							JSONObject jsonobj=new JSONObject(response);
							String status=jsonobj.getJSONObject("message").getString("status");
							
							if(status.equals("200")){
								
								Methods.longToast(jsonobj.getJSONObject("message").getString("message"), CellDetailsActivity.this);
								finish();
							}else{
								
								Methods.longToast("Some error occured,please try again later", CellDetailsActivity.this);
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
					jsonobj.put("tbl", "Cells");
					
					JSONObject obj=new JSONObject();
					
					 obj.put("name", cellcode);
					 obj.put("cell_name", txtCellName.getText().toString());
					 obj.put("contact_phone_no", txtCellContactPhn.getText().toString());
					 obj.put("contact_email_id", txtCellContactEmailId.getText().toString());
					 obj.put("meeting_location", txtCellMeetingLoc.getText().toString());
					
					 jsonobj.put("records", obj);
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				String dataString=jsonobj.toString();
			//	String dataString=gson.toJson(model, MeetingListRequestModel.class);

				Log.e("Request droid", "data passed is ::::::::" + dataString);
				params.put(DashboardDataService.DATA, dataString);
				return params; 
			}
		};

		App.getInstance().addToRequestQueue(reqDashboard, "reqDashboard");
		reqDashboard.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
	}

	public boolean isValid() {

		if (!InputValidation.isPhoneNumber(txtCellContactPhn, true)) {
			return false;
		}
		if (!InputValidation.hasText(txtCellName)) {
			new AlertDialog.Builder(CellDetailsActivity.this)
					.setCancelable(false)
					.setTitle("Invalid Input")
					.setMessage("Please enter cell name")
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface, int i) {

						}
					})
					.show();
			return false;
		}
		if (!InputValidation.hasText(txtCellContactPhn)) {
			return false;
		}
		return true;
	}

}
