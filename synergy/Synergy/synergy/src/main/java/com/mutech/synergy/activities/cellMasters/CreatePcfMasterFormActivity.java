package com.mutech.synergy.activities.cellMasters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.opengl.Visibility;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

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
import com.mutech.synergy.SynergyValues.Web.CreatePCFService;
import com.mutech.synergy.SynergyValues.Web.GetAllChurchesService;
import com.mutech.synergy.SynergyValues.Web.GetAllRegionsService;
import com.mutech.synergy.SynergyValues.Web.GetAllZonesService;
import com.mutech.synergy.SynergyValues.Web.GetHigherHierarchyService;
import com.mutech.synergy.SynergyValues.Web.GetTopHierarchyChurchesService;
import com.mutech.synergy.SynergyValues.Web.LowerHierarchyService;
import com.mutech.synergy.SynergyValues.Web.fillLoginSpinner;
import com.mutech.synergy.models.CreatePCFModel;
import com.mutech.synergy.models.CreateSrCellModel;
import com.mutech.synergy.models.HigherHierarchyRespModel;
import com.mutech.synergy.models.MeetingListRequestModel;
import com.mutech.synergy.models.HigherHierarchyRespModel.HHSubModel;
import com.mutech.synergy.models.ResponseMessageModel;
import com.mutech.synergy.models.ResponseMessageModel2;
import com.mutech.synergy.utils.InputValidation;
import com.mutech.synergy.utils.Methods;
import com.mutech.synergy.utils.NetworkHelper;
import com.mutech.synergy.utils.PreferenceHelper;

public class CreatePcfMasterFormActivity extends ActionBarActivity implements OnClickListener{

	private EditText txtPCFName,txtPCFContactPhn,txtPCFContactEmailId;
	private Spinner spnPCFZone,spnPCFRegion,spnCellChurch,spnSeniorCellChurchgroup;
	private Button btnSavePCF;
	private ArrayList<String> mZoneList,mRegionList,mChurchList,mGrpChurchList;
	private PreferenceHelper mPreferenceHelper;
	private Gson gson;
	String defKey,defVal,defRole;
	TextView lblzone,lblgroupchurch,lblchurch;
	OnItemSelectedListener myListener;

    public boolean isValid() {

     /*   if(!InputValidation.hasText(txtPCFCode)) {
			AlertDialog dialog = new AlertDialog.Builder(CreatePcfMasterFormActivity.this)
                    .setCancelable(false)
                    .setTitle("Mandatory Input")
                    .setMessage("Please enter Senior Cell Code")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .show();
			TextView textView = (TextView) dialog.findViewById(android.R.id.message);
			textView.setTextSize(18);
            return false;
        }*/
        if(!InputValidation.hasText(txtPCFName)) {
			AlertDialog dialog = new AlertDialog.Builder(CreatePcfMasterFormActivity.this)
                    .setCancelable(false)
                    .setTitle("Mandatory Input")
                    .setMessage("Please enter Senior Cell Name")
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
        if(!InputValidation.spnHasText(spnPCFZone, "Zone")) {
			AlertDialog dialog =new AlertDialog.Builder(CreatePcfMasterFormActivity.this)
                    .setCancelable(false)
                    .setTitle("Mandatory Input")
                    .setMessage("Please enter Zone")
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
        if(!InputValidation.spnHasText(spnPCFRegion, "Region")) {
			AlertDialog dialog = new AlertDialog.Builder(CreatePcfMasterFormActivity.this)
                    .setCancelable(false)
                    .setTitle("Mandatory Input")
                    .setMessage("Please enter Region")
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
        if(!InputValidation.spnHasText(spnSeniorCellChurchgroup, "Group Church")) {
			AlertDialog dialog = new AlertDialog.Builder(CreatePcfMasterFormActivity.this)
                    .setCancelable(false)
                    .setTitle("Mandatory Input")
                    .setMessage("Please enter Group Church")
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
        if(!InputValidation.spnHasText(spnCellChurch, "Church")) {
			AlertDialog dialog =  new AlertDialog.Builder(CreatePcfMasterFormActivity.this)
                    .setCancelable(false)
                    .setTitle("Mandatory Input")
                    .setMessage("Please enter Church")
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
			AlertDialog dialog = new AlertDialog.Builder(CreatePcfMasterFormActivity.this)
                    .setCancelable(false)
                    .setTitle("Invalid Input")
                    .setMessage("Please enter valid phone number")
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

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_pcf);

		initialize();
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void initialize() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		getSupportActionBar().setHomeButtonEnabled(false);
		getSupportActionBar().setCustomView(R.layout.custom_actionbar);
		TextView tvTitle=(TextView)getSupportActionBar().getCustomView().findViewById(R.id.title_text);
		tvTitle.setText("PCFs        ");
		
		
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2E9AFE")));
		getSupportActionBar().setDisplayShowCustomEnabled(true);

		lblzone=(TextView) findViewById(R.id.lblzone);
		lblgroupchurch=(TextView) findViewById(R.id.lblgroupchurch);
		lblchurch=(TextView) findViewById(R.id.lblchurch);
		
		
		//txtPCFCode=(EditText) findViewById(R.id.txtPCFCode);
		txtPCFName=(EditText) findViewById(R.id.txtPCFName);
		spnPCFZone=(Spinner) findViewById(R.id.spnPCFZone);
		spnPCFRegion=(Spinner) findViewById(R.id.spnPCFRegion);
		spnCellChurch=(Spinner) findViewById(R.id.spnPCFChurch);
		spnSeniorCellChurchgroup=(Spinner) findViewById(R.id.spnGroupChurch);
		
		
		txtPCFContactPhn=(EditText) findViewById(R.id.txtPCFContactPhn);
		txtPCFContactEmailId=(EditText) findViewById(R.id.txtPCFContactEmailId);

		btnSavePCF=(Button) findViewById(R.id.btnSavePCF);
		btnSavePCF.setOnClickListener(this);

		mZoneList=new ArrayList<String>();
		mRegionList=new ArrayList<String>();
		mChurchList=new ArrayList<String>();
		mGrpChurchList=new ArrayList<String>();
		
		mPreferenceHelper=new PreferenceHelper(this);
		gson=new Gson();

		defKey=mPreferenceHelper.getString(Commons.USER_DEFKEY);
		defVal=mPreferenceHelper.getString(Commons.USER_DEFVALUE);
		defRole=mPreferenceHelper.getString(Commons.USER_ROLE);
		
		if(mPreferenceHelper.getString(Commons.USER_ROLE).equals("PCF Leader")){
			
			
			lblzone.setVisibility(View.GONE);
			lblgroupchurch.setVisibility(View.GONE);
			lblchurch.setVisibility(View.GONE);
			
			
			spnPCFZone.setVisibility(View.VISIBLE);
			spnPCFRegion.setVisibility(View.VISIBLE);
			spnSeniorCellChurchgroup.setVisibility(View.VISIBLE);
			spnCellChurch.setVisibility(View.VISIBLE);
		
			
			
		//	spnPCFZone.setEnabled(false);
			spnPCFRegion.setEnabled(false);
			spnSeniorCellChurchgroup.setEnabled(false);
			spnCellChurch.setEnabled(false);
		//	txtPCF.setEnabled(false);
			
		}
		
     
      
  	    if(mPreferenceHelper.getString(Commons.USER_ROLE).equals("Church Pastor")){
			
    		lblzone.setVisibility(View.GONE);
			lblgroupchurch.setVisibility(View.GONE);
			//lblchurch.setVisibility(View.GONE);
	
			spnPCFZone.setVisibility(View.VISIBLE);
			spnPCFRegion.setVisibility(View.VISIBLE);
			spnSeniorCellChurchgroup.setVisibility(View.VISIBLE);
			//spnCellChurch.setVisibility(View.VISIBLE);
		//	txtPCF.setVisibility(View.VISIBLE);
		//	spnSeniorCell.setVisibility(View.VISIBLE);
    	  
    	  
    	  
    	  
    	  
			spnPCFZone.setEnabled(false);
			spnPCFRegion.setEnabled(false);
			spnSeniorCellChurchgroup.setEnabled(false);
			//spnCellChurch.setEnabled(false);
			//txtPCF.setEnabled(false);
			
		}
		
		if(mPreferenceHelper.getString(Commons.USER_ROLE).equals("Group Church Pastor")){
			
    	  
    		lblzone.setVisibility(View.GONE);
		//	lblgroupchurch.setVisibility(View.GONE);
		//	lblchurch.setVisibility(View.GONE);
		//	lblpcf.setVisibility(View.GONE);
		//	lblseniorcell.setVisibility(View.GONE);
			
			spnPCFZone.setVisibility(View.VISIBLE);
			spnPCFRegion.setVisibility(View.VISIBLE);
		//	spnSeniorCellChurchgroup.setVisibility(View.VISIBLE);
		//	spnCellChurch.setVisibility(View.VISIBLE);
		//	txtPCF.setVisibility(View.VISIBLE);
		//	spnSeniorCell.setVisibility(View.VISIBLE);
    	  
    	  
			spnPCFZone.setEnabled(false);
			spnPCFRegion.setEnabled(false);
			
			//spnCellChurch.setEnabled(false);
			//txtPCF.setEnabled(false);
			
		}

		if(mPreferenceHelper.getString(Commons.USER_ROLE).equals("Zonal Pastor")){
			
    	  
    		lblzone.setVisibility(View.GONE);
			spnPCFZone.setVisibility(View.VISIBLE);
    		spnPCFRegion.setEnabled(false);
		}

		lblzone.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			if(NetworkHelper.isOnline(CreatePcfMasterFormActivity.this)){
				
				String selval=spnPCFRegion.getSelectedItem().toString();
				
				Methods.showProgressDialog(CreatePcfMasterFormActivity.this);
				getSpinnerData("Regions",selval );

			}else
			{
				Methods.longToast("Please connect to Internet",CreatePcfMasterFormActivity.this);
			}
			
			
			}
		});
      
		lblgroupchurch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				String selval = "";

				if (NetworkHelper.isOnline(CreatePcfMasterFormActivity.this)) {

					if (lblzone.getVisibility() == View.VISIBLE) {


						Methods.smallToast("Please Select Zone", CreatePcfMasterFormActivity.this);


					} else {

						selval = spnPCFZone.getSelectedItem().toString();
						Methods.showProgressDialog(CreatePcfMasterFormActivity.this);
						getSpinnerData("Zones", selval);

					}


				} else {
					Methods.longToast("Please connect to Internet", CreatePcfMasterFormActivity.this);
				}


			}
		});

		lblchurch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				String selval = "";

				if (NetworkHelper.isOnline(CreatePcfMasterFormActivity.this)) {

					if (lblgroupchurch.getVisibility() == View.VISIBLE) {

						Methods.smallToast("Please Select Group Church", CreatePcfMasterFormActivity.this);

					} else {

						selval = spnSeniorCellChurchgroup.getSelectedItem().toString();
						Methods.showProgressDialog(CreatePcfMasterFormActivity.this);
						getSpinnerData("Group Churches", selval);

					}
				} else {
					Methods.longToast("Please connect to Internet", CreatePcfMasterFormActivity.this);
				}
			}
		});
    
		myListener=new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View arg1, int arg2,long arg3) {

				Spinner spinner = (Spinner) parent;

				if(spinner.getId() == R.id.spnPCFZone){

						lblgroupchurch.setVisibility(View.VISIBLE);
						lblchurch.setVisibility(View.VISIBLE);

						spnSeniorCellChurchgroup.setVisibility(View.GONE);
						spnCellChurch.setVisibility(View.GONE);


				 }

			/*	if(spinner.getId() == R.id.spnGroupChurch){

					lblchurch.setVisibility(View.VISIBLE);

					spnCellChurch.setVisibility(View.GONE);

			 }*/

			}

			@Override
          	public void onNothingSelected(AdapterView<?> arg0) {
              // TODO Auto-generated method stub

          	}
      	};
      
		spnPCFZone.setOnItemSelectedListener(myListener);
		spnSeniorCellChurchgroup.setOnItemSelectedListener(myListener);
		spnCellChurch.setOnItemSelectedListener(myListener);
   
		if(NetworkHelper.isOnline(this)){
			Methods.showProgressDialog(this);
		
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


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnSavePCF:
			if(isValid()) {
                if(validateFields()){
                    if(NetworkHelper.isOnline(this)){
                        Methods.showProgressDialog(this);
                        savePCFMaster();
                    }else{
                        Methods.longToast("Please connect to Internet", this);
                    }
                }
			}
			break;
		default:
			break;
		}
	}

	private boolean validateFields() {
	/*	if(txtPCFCode.getText().toString().trim().length() > 0)
		{*/
			if(txtPCFName.getText().toString().trim().length() >0)
			{
				if(txtPCFContactPhn.getText().toString().trim().length() > 0){
					if(txtPCFContactEmailId.getText().toString().trim().length() > 0){
						if(Methods.isValidEmail(txtPCFContactEmailId.getText().toString())){
							return true;
						}else{
							Methods.longToast("Please enter Valid email id", this);
						}
					}else{
						Methods.longToast("Please enter Email Id", this);
					}
				}else{
					Methods.longToast("Please enter Contact No.", this);
				}
			}else{
				Methods.longToast("Please enter PCF Name", this);
			}
		/*}else
		{
			Methods.longToast("Please enter PCF Code", this);
		}*/
		return false;
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
						Methods.longToast("User name or Password is incorrect", CreatePcfMasterFormActivity.this);
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
							
						}
						
					//	setAdapters();

					}else{
						
					}
				}
				//Methods.showProgressDialog(CreateCellMasterActivity.this);
				getLowerHierarchy();
			}
		},new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Methods.closeProgressDialog();
				Log.d("droid","get reqgetTopHierarchy error---------------"+ error.getCause());

				if(error.networkResponse.statusCode==403){
					//		Methods.longToast("Access Denied", CreatePcfMasterFormActivity.this);
				}
				//else
					//		Methods.longToast("Some Error Occured,please try again later", CreatePcfMasterFormActivity.this);

				//	Methods.showProgressDialog(CreateCellMasterActivity.this);
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
							Methods.longToast("User name or Password is incorrect", CreatePcfMasterFormActivity.this);
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
								}else if(defRole.equalsIgnoreCase("Regional Pastor")){
									mRegionList.add(jsonarray.getJSONObject(i).getString("name"));
								}
							}
							
							setAdapters();
							
						}else{
							
							Methods.longToast("Record Not Found ", CreatePcfMasterFormActivity.this);
							
						}
						
					}
				
					
				
				
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				
				
				/*//Object meetingmsg=mMeetingModel.getMessage();
				if(null != mHHModel.getStatus() && mHHModel.getStatus().trim().length() >0){

					if(mHHModel.getStatus()=="401"){
						Methods.longToast("User name or Password is incorrect", CreateCellMasterActivity.this);
					}
				}else{
					if(null !=mHHModel.getMessage() && mHHModel.getMessage().size() >0){
						ArrayList<HHSubModel> mHHSubModel=new ArrayList<HHSubModel>();
						//if(meetingmsg instanceof JSONArray){
						mHHSubModel=mHHModel.getMessage();


						if(null !=mHHSubModel && mHHSubModel.size() > 0){
							for(int i=0;i<mHHSubModel.size();i++)
								if( defKey.equalsIgnoreCase("PCFs")){
									mSeniorCellList.add(mHHModel.getMessage().get(i).getName());
								}else if(defKey.equalsIgnoreCase("Churches")){
									mPCFList.add(mHHModel.getMessage().get(i).getName());
								}else if(defKey.equalsIgnoreCase("Group Churches")){
									mChurchList.add(mHHModel.getMessage().get(i).getName());
								}else if(defKey.equalsIgnoreCase("Zones")){
									mGrpChurchList.add(mHHModel.getMessage().get(i).getName());
								}else if(defKey.equalsIgnoreCase("Region")){
									mZoneList.add(mHHModel.getMessage().get(i).getName());
								}
						}

					}else{
					}
				}
				setAdapters();*/

			}
		},new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Methods.closeProgressDialog();
				Log.d("droid","get reqgetLowerHierarchy error---------------"+ error.getCause());

				if(error.networkResponse.statusCode==403){
					//	Methods.longToast("Access Denied", CreatePcfMasterFormActivity.this);
				}
				//else
				//	Methods.longToast("Some Error Occured,please try again later", CreatePcfMasterFormActivity.this);
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
	
	private void setAdapters() {
		String defKey=mPreferenceHelper.getString(Commons.USER_DEFKEY);
		String defVal=mPreferenceHelper.getString(Commons.USER_DEFVALUE);
		String defRole=mPreferenceHelper.getString(Commons.USER_ROLE);


	/*	ArrayAdapter<String> adapterZone = new ArrayAdapter<String>(CreatePcfMasterFormActivity.this, android.R.layout.simple_spinner_item, mZoneList);
		adapterZone.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnPCFZone.setAdapter(adapterZone);*/
		//lblzone.setText(spnPCFZone.getSelectedItem().toString());

		ArrayAdapter<String> adapterRegion = new ArrayAdapter<String>(CreatePcfMasterFormActivity.this, android.R.layout.simple_spinner_item, mRegionList);
		adapterRegion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnPCFRegion.setAdapter(adapterRegion);

		ArrayAdapter<String> adapterChurch = new ArrayAdapter<String>(CreatePcfMasterFormActivity.this, android.R.layout.simple_spinner_item, mChurchList);
		adapterChurch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnCellChurch.setAdapter(adapterChurch);
		//lblchurch.setText(spnCellChurch.getSelectedItem().toString());

		ArrayAdapter<String> adapterchurchgropu = new ArrayAdapter<String>(CreatePcfMasterFormActivity.this, android.R.layout.simple_spinner_item, mGrpChurchList);
		adapterchurchgropu.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnSeniorCellChurchgroup.setAdapter(adapterchurchgropu);
		//lblgroupchurch.setText(spnSeniorCellChurchgroup.getSelectedItem().toString());

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
							Methods.longToast("User name or Password is incorrect", CreatePcfMasterFormActivity.this);
					
					}
					else
					{
							
							JSONArray jsonarray=jsonobj.getJSONArray("message");
							
							if(jsonarray.length()>0){
								
								if(tbl.equalsIgnoreCase("Group Churches"))
									mChurchList.clear();
								if(tbl.equalsIgnoreCase("Zones"))
									mGrpChurchList.clear();
								if(tbl.equalsIgnoreCase("Regions"))
									mZoneList.clear();
								
								
								for(int i=0;i<jsonarray.length();i++){
									 if(tbl.equalsIgnoreCase("Group Churches")){
										
										mChurchList.add(jsonarray.getJSONObject(i).getString("name"));
										
									}else if(tbl.equalsIgnoreCase("Zones")){
										
										mGrpChurchList.add(jsonarray.getJSONObject(i).getString("name"));
										
									}else if(tbl.equalsIgnoreCase("Regions")){
										
										mZoneList.add(jsonarray.getJSONObject(i).getString("name"));
										
									}
								}
							//	setAdapters();
											
								if(tbl.equalsIgnoreCase("Regions")){
									
									Log.e("condistion match","condistion match");
								 

									ArrayAdapter<String> adapterZone = new ArrayAdapter<String>(CreatePcfMasterFormActivity.this, android.R.layout.simple_spinner_item, mZoneList);
									adapterZone.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
									spnPCFZone.setAdapter(adapterZone);
									
									lblzone.setVisibility(View.GONE);
									spnPCFZone.setVisibility(View.VISIBLE);
								
								}else{
									Log.e("condistion match"," not condistion match");
								}
									
								
								if(tbl.equalsIgnoreCase("Zones")){
									Log.e("lable gone", "lable gone");
									
									ArrayAdapter<String> adapterchurchgropu = new ArrayAdapter<String>(CreatePcfMasterFormActivity.this, android.R.layout.simple_spinner_item, mGrpChurchList);
									adapterchurchgropu.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
									spnSeniorCellChurchgroup.setAdapter(adapterchurchgropu);

									lblgroupchurch.setVisibility(View.GONE);
									spnSeniorCellChurchgroup.setVisibility(View.VISIBLE);
									
									
								}
							
								if(tbl.equalsIgnoreCase("Group Churches")){
									
									ArrayAdapter<String> adapterChurch = new ArrayAdapter<String>(CreatePcfMasterFormActivity.this, android.R.layout.simple_spinner_item, mChurchList);
									adapterChurch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
									spnCellChurch.setAdapter(adapterChurch);

									lblchurch.setVisibility(View.GONE);
									spnCellChurch.setVisibility(View.VISIBLE);
								}
						
							}else{
								Methods.longToast("Resord Not Found", CreatePcfMasterFormActivity.this);
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
					//	Methods.longToast("Access Denied", CreatePcfMasterFormActivity.this);
				}
				//else
				//	Methods.longToast("Some Error Occured,please try again later", CreatePcfMasterFormActivity.this);
				//setAdapters();
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

	protected void savePCFMaster() {
		StringRequest reqsaveCellMaster=new StringRequest(Method.POST, SynergyValues.Web.CreatePCFService.SERVICE_URL,new Listener<String>() {

			@Override
			public void onResponse(String response) {
				Methods.closeProgressDialog();
				Log.d("droid", "get reqsaveCellMaster ---------------" + response);

				if(response.contains("status"))
				{
					ResponseMessageModel2 model=gson.fromJson(response, ResponseMessageModel2.class);
					if(null !=model.getMessage().getMessage() && model.getMessage().getMessage().trim().length() >0){
						Methods.longToast(model.getMessage().getMessage(), CreatePcfMasterFormActivity.this);
					}
				}else{
					ResponseMessageModel model=gson.fromJson(response, ResponseMessageModel.class);

					if(null !=model.getMessage() && model.getMessage().trim().length() >0){
						Methods.longToast(model.getMessage(), CreatePcfMasterFormActivity.this);
					}
					finish();
					Intent intForm=new Intent(CreatePcfMasterFormActivity.this,DisplayMastersListActivity.class);
					intForm.putExtra("OptionSelected", "PCF");
					startActivity(intForm);
				}
			}
		},new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Methods.closeProgressDialog();
				Log.d("droid","get reqsavePCFMaster error---------------"+ error.getMessage());

				Methods.longToast("Some Error Occured,please try again later", CreatePcfMasterFormActivity.this);
			}
		})
		{
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();

				CreatePCFModel pcfModel = new CreatePCFModel();
				//pcfModel.setPcf_code(txtPCFCode.getText().toString().trim());
				pcfModel.setPcf_name(txtPCFName.getText().toString().trim());
				pcfModel.setZone(spnPCFZone.getSelectedItem().toString().trim());
				pcfModel.setRegion(spnPCFRegion.getSelectedItem().toString().trim());
				pcfModel.setChurch_group(spnSeniorCellChurchgroup.getSelectedItem().toString().trim());
				pcfModel.setChurch(spnCellChurch.getSelectedItem().toString().trim());
				pcfModel.setContact_phone_no(txtPCFContactPhn.getText().toString().trim());
				pcfModel.setContact_email_id(txtPCFContactEmailId.getText().toString().trim());
				pcfModel.setUsername(mPreferenceHelper.getString(Commons.USER_EMAILID));
				pcfModel.setUserpass(mPreferenceHelper.getString(Commons.USER_PASSWORD));

				String dataString=gson.toJson(pcfModel,CreatePCFModel.class);

				Log.d("droid", dataString);

				params.put(SynergyValues.Web.CreatePCFService.DATA, dataString);
				return params;
			}
		};
		App.getInstance().addToRequestQueue(reqsaveCellMaster, "reqsavePCFMaster");
		reqsaveCellMaster.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
	}

}
