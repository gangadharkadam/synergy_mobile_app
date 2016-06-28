package com.mutech.synergy.activities.cellMasters;

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
//import com.google.android.gms.internal.df;
import com.google.gson.Gson;
import com.mutech.synergy.App;
import com.mutech.synergy.R;
import com.mutech.synergy.R.id;
import com.mutech.synergy.R.layout;
import com.mutech.synergy.R.menu;
import com.mutech.synergy.SynergyValues.Commons;
import com.mutech.synergy.SynergyValues.Web.CreateCellService;
import com.mutech.synergy.SynergyValues.Web.GetHigherHierarchyService;
import com.mutech.synergy.SynergyValues.Web.LowerHierarchyService;
import com.mutech.synergy.SynergyValues.Web.fillLoginSpinner;
import com.mutech.synergy.adapters.MasterSpinnerAdpter;
import com.mutech.synergy.models.CreateCellModel;
import com.mutech.synergy.models.HigherHierarchyRespModel;
import com.mutech.synergy.models.MeetingListRequestModel;
import com.mutech.synergy.models.ResponseMessageModel2;
import com.mutech.synergy.models.HigherHierarchyRespModel.HHSubModel;
import com.mutech.synergy.utils.InputValidation;
import com.mutech.synergy.utils.Methods;
import com.mutech.synergy.utils.NetworkHelper;
import com.mutech.synergy.utils.PreferenceHelper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

public class NewCreateCellMasterActivity extends AppCompatActivity {
	
	
	private EditText txtCellName,txtCellMeetingLoc,txtCellContactPhn,txtCellContactEmailId;
	private Button btnSaveCellMaster,OnclickButton;
	private Spinner spnSeniorCell,spnCellZone,spnCellRegion,spnCellChurch,spnSeniorCellChurchgroup,txtPCF;
	private ArrayList<String> mZoneList,mRegionList,mChurchList,mSeniorCellList,mGrpChurchList,mPCFList;
	private PreferenceHelper mPreferenceHelper;
	private Gson gson;
	String defKey,defVal,defRole;
	Boolean mSelected;
	TextView lblzone,lblgroupchurch,lblchurch,lblpcf,lblseniorcell;
	Boolean zonesp=false,gpchurchsp=false,churchsp=false,pcfsp=false;
	OnItemSelectedListener myListener;

	public boolean isValid() {

	/*	if(!InputValidation.hasText(txtCellCode)) {
			AlertDialog dialog = new AlertDialog.Builder(NewCreateCellMasterActivity.this)
					.setCancelable(false)
					.setTitle("Mandatory Input")
					.setMessage("Please enter Cell Code")
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
		if(!InputValidation.hasText(txtCellName)) {
			AlertDialog dialog = new AlertDialog.Builder(NewCreateCellMasterActivity.this)
					.setCancelable(false)
					.setTitle("Mandatory Input")
					.setMessage("Please enter Cell Name")
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
        if(!InputValidation.spnHasText(spnCellZone, "Zone")) {
			AlertDialog dialog = new AlertDialog.Builder(NewCreateCellMasterActivity.this)
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
        if(!InputValidation.spnHasText(spnCellRegion, "Region")) {
			AlertDialog dialog = new AlertDialog.Builder(NewCreateCellMasterActivity.this)
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
			AlertDialog dialog = new AlertDialog.Builder(NewCreateCellMasterActivity.this)
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
			AlertDialog dialog =   new AlertDialog.Builder(NewCreateCellMasterActivity.this)
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
        if(!InputValidation.spnHasText(txtPCF, "PCF")) {
			AlertDialog dialog =  new AlertDialog.Builder(NewCreateCellMasterActivity.this)
                    .setCancelable(false)
                    .setTitle("Mandatory Input")
                    .setMessage("Please enter PCF")
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
        if(!InputValidation.spnHasText(spnSeniorCell, "Senior Cell")) {
			AlertDialog dialog =  new AlertDialog.Builder(NewCreateCellMasterActivity.this)
                    .setCancelable(false)
                    .setTitle("Mandatory Input")
                    .setMessage("Please enter Senior Cell")
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
        if(!InputValidation.isPhoneNumber(txtCellContactPhn, false)) {
			AlertDialog dialog = new AlertDialog.Builder(NewCreateCellMasterActivity.this)
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
		setContentView(R.layout.activity_new_create_cell_master);
		
		getSupportActionBar().hide();
		
		lblzone=(TextView) findViewById(R.id.lblzone);
		lblgroupchurch=(TextView) findViewById(R.id.lblgroupchurch);
		lblchurch=(TextView) findViewById(R.id.lblchurch);
		lblpcf=(TextView) findViewById(R.id.lblpcf);
		lblseniorcell=(TextView) findViewById(R.id.lblseniorcell);


		//txtCellCode=(EditText) findViewById(R.id.txtCellCode);
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
		btnSaveCellMaster=(Button) findViewById(R.id.btnSaveCellMaster);
		
		
		mPreferenceHelper=new PreferenceHelper(this);
		gson=new Gson();

		defKey=mPreferenceHelper.getString(Commons.USER_DEFKEY);
		defVal=mPreferenceHelper.getString(Commons.USER_DEFVALUE);
		defRole=mPreferenceHelper.getString(Commons.USER_ROLE);
		
		
		mZoneList=new ArrayList<String>();
		mRegionList=new ArrayList<String>();
		mChurchList=new ArrayList<String>();
		mSeniorCellList=new ArrayList<String>();
		mGrpChurchList=new ArrayList<String>();
		mPCFList=new ArrayList<String>();
		
		btnSaveCellMaster.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
                    if(isValid()) {
						if(validateFields()){
							if(NetworkHelper.isOnline(NewCreateCellMasterActivity.this)){
								Methods.showProgressDialog(NewCreateCellMasterActivity.this);
								//getCellTopHierarchy();
								saveCellMaster();
							}else{
								Methods.longToast("Please connect to Internet",NewCreateCellMasterActivity.this);
							}
						}
					}
			}
		});
		
if(mPreferenceHelper.getString(Commons.USER_ROLE).equals("PCF Leader")){
			
			
			lblzone.setVisibility(View.GONE);
			lblgroupchurch.setVisibility(View.GONE);
			lblchurch.setVisibility(View.GONE);
			lblpcf.setVisibility(View.GONE);
			//lblseniorcell.setVisibility(View.GONE);
			
			spnCellZone.setVisibility(View.VISIBLE);
			spnCellRegion.setVisibility(View.VISIBLE);
			spnSeniorCellChurchgroup.setVisibility(View.VISIBLE);
			spnCellChurch.setVisibility(View.VISIBLE);
			txtPCF.setVisibility(View.VISIBLE);
			
			
			spnCellZone.setEnabled(false);
			spnCellRegion.setEnabled(false);
			spnSeniorCellChurchgroup.setEnabled(false);
			spnCellChurch.setEnabled(false);
		//	txtPCF.setEnabled(false);
			
		}
		
      if(mPreferenceHelper.getString(Commons.USER_ROLE).equals("Senior Cell Leader")){
    	  
    	  
    	  	lblzone.setVisibility(View.GONE);
			lblgroupchurch.setVisibility(View.GONE);
			lblchurch.setVisibility(View.GONE);
			lblpcf.setVisibility(View.GONE);
			lblseniorcell.setVisibility(View.GONE);
			
			spnCellZone.setVisibility(View.VISIBLE);
			spnCellRegion.setVisibility(View.VISIBLE);
			spnSeniorCellChurchgroup.setVisibility(View.VISIBLE);
			spnCellChurch.setVisibility(View.VISIBLE);
			txtPCF.setVisibility(View.VISIBLE);
			spnSeniorCell.setVisibility(View.VISIBLE);
			
			spnCellZone.setEnabled(false);
			spnCellRegion.setEnabled(false);
			spnSeniorCellChurchgroup.setEnabled(false);
			spnCellChurch.setEnabled(false);
			txtPCF.setEnabled(false);
			
		}
      
      if(mPreferenceHelper.getString(Commons.USER_ROLE).equals("Church Pastor")){
			
    		lblzone.setVisibility(View.GONE);
			lblgroupchurch.setVisibility(View.GONE);
			lblchurch.setVisibility(View.GONE);
		//	lblpcf.setVisibility(View.GONE);
		//	lblseniorcell.setVisibility(View.GONE);
			
			spnCellZone.setVisibility(View.VISIBLE);
			spnCellRegion.setVisibility(View.VISIBLE);
			spnSeniorCellChurchgroup.setVisibility(View.VISIBLE);
			spnCellChurch.setVisibility(View.VISIBLE);
		//	txtPCF.setVisibility(View.VISIBLE);
		//	spnSeniorCell.setVisibility(View.VISIBLE);
    	  
    	  
    	  
    	  
    	  
			spnCellZone.setEnabled(false);
			spnCellRegion.setEnabled(false);
			spnSeniorCellChurchgroup.setEnabled(false);
			//spnCellChurch.setEnabled(false);
			//txtPCF.setEnabled(false);
			
		}
		
      if(mPreferenceHelper.getString(Commons.USER_ROLE).equals("Group Church Pastor")){
			
    	  
    		lblzone.setVisibility(View.GONE);
			lblgroupchurch.setVisibility(View.GONE);
		//	lblchurch.setVisibility(View.GONE);
		//	lblpcf.setVisibility(View.GONE);
		//	lblseniorcell.setVisibility(View.GONE);
			
			spnCellZone.setVisibility(View.VISIBLE);
			spnCellRegion.setVisibility(View.VISIBLE);
			spnSeniorCellChurchgroup.setVisibility(View.VISIBLE);
		//	spnCellChurch.setVisibility(View.VISIBLE);
		//	txtPCF.setVisibility(View.VISIBLE);
		//	spnSeniorCell.setVisibility(View.VISIBLE);
    	  
    	  
			spnCellZone.setEnabled(false);
			spnCellRegion.setEnabled(false);
		//	spnSeniorCellChurchgroup.setEnabled(false);
			//spnCellChurch.setEnabled(false);
			//txtPCF.setEnabled(false);
			
		}
      
      if(mPreferenceHelper.getString(Commons.USER_ROLE).equals("Zonal Pastor")){
			
    	  
    		lblzone.setVisibility(View.GONE);
			spnCellZone.setVisibility(View.VISIBLE);
			spnCellRegion.setEnabled(false);
	
		}
		
      lblzone.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			if(NetworkHelper.isOnline(NewCreateCellMasterActivity.this)){
				
				String selval=spnCellRegion.getSelectedItem().toString();
				
				Methods.showProgressDialog(NewCreateCellMasterActivity.this);
				getSpinnerData("Regions",selval );

			}else
			{
				Methods.longToast("Please connect to Internet",NewCreateCellMasterActivity.this);
			}
			
			
		}
	});
      
  	
      lblgroupchurch.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			String selval="";
		
			if(NetworkHelper.isOnline(NewCreateCellMasterActivity.this)){
			
				if(lblzone.getVisibility()==View.VISIBLE){
				
					
					Methods.smallToast("Please Select Zone", NewCreateCellMasterActivity.this);
					
					
				
				}else{
					
					selval=spnCellZone.getSelectedItem().toString();
					Methods.showProgressDialog(NewCreateCellMasterActivity.this);
					getSpinnerData("Zones",selval );

				}
				
			
			}else
			{
				Methods.longToast("Please connect to Internet",NewCreateCellMasterActivity.this);
			}
			
			
		}
	});
      
      lblchurch.setOnClickListener(new OnClickListener() {
  		
  		@Override
  		public void onClick(View v) {
  			// TODO Auto-generated method stub
  			
  			String selval="";
  		
  			if(NetworkHelper.isOnline(NewCreateCellMasterActivity.this)){
  			
  				if(lblgroupchurch.getVisibility()==View.VISIBLE){
  					
					Methods.smallToast("Please Select Group Church", NewCreateCellMasterActivity.this);
				
				}else{
					
					selval=spnSeniorCellChurchgroup.getSelectedItem().toString();
					Methods.showProgressDialog(NewCreateCellMasterActivity.this);
					getSpinnerData("Group Churches",selval );
			
				}
  			}else
  			{
  				Methods.longToast("Please connect to Internet",NewCreateCellMasterActivity.this);
  			}
  			
  			
  		}
  	});
    
      lblpcf.setOnClickListener(new OnClickListener() {
    		
    		@Override
    		public void onClick(View v) {
    			// TODO Auto-generated method stub
    			
    			String selval="";
    		
    			if(NetworkHelper.isOnline(NewCreateCellMasterActivity.this)){
    				
    				if(lblchurch.getVisibility()==View.VISIBLE){
      					
    					Methods.smallToast("Please Select Church", NewCreateCellMasterActivity.this);
    				
    				}else{
    				
    					selval=spnCellChurch.getSelectedItem().toString();
    					Methods.showProgressDialog(NewCreateCellMasterActivity.this);
    					getSpinnerData("Churches",selval );
    				}

    			}else
    			{
    				Methods.longToast("Please connect to Internet",NewCreateCellMasterActivity.this);
    			}
    			
    			
    		}
    	});
      
      lblseniorcell.setOnClickListener(new OnClickListener() {
  		
  		@Override
  		public void onClick(View v) {
  			// TODO Auto-generated method stub
  		
  			if(NetworkHelper.isOnline(NewCreateCellMasterActivity.this)){
  				
  				if(lblpcf.getVisibility()==View.VISIBLE){
  					
					Methods.smallToast("Please Select PCF", NewCreateCellMasterActivity.this);
				
				}else{
  				
					String selval=txtPCF.getSelectedItem().toString();
					Methods.showProgressDialog(NewCreateCellMasterActivity.this);
					getSpinnerData("PCFs",selval );
				}

  			}else
  			{
  				Methods.longToast("Please connect to Internet",NewCreateCellMasterActivity.this);
  			}
  			
  			
  		}
  	});
     
		
       myListener=new OnItemSelectedListener() {

          @Override
          public void onItemSelected(AdapterView<?> parent, View arg1, int arg2,long arg3) {
        	  
        	  Spinner spinner = (Spinner) parent;
        	     if(spinner.getId() == R.id.spnCellZone){
        	    	 
        	    	// Methods.longToast("selected",NewCreateCellMasterActivity.this);
   					
						lblgroupchurch.setVisibility(View.VISIBLE);
						lblchurch.setVisibility(View.VISIBLE);
						lblpcf.setVisibility(View.VISIBLE);
						lblseniorcell.setVisibility(View.VISIBLE);
						
					//	spnCellZone.setVisibility(View.GONE);
				//		spnCellRegion.setVisibility(View.VISIBLE);
						spnSeniorCellChurchgroup.setVisibility(View.GONE);
						spnCellChurch.setVisibility(View.GONE);
						txtPCF.setVisibility(View.GONE);
						spnSeniorCell.setVisibility(View.GONE);
        	    	 
        	     }
        	     
        	     if(spinner.getId() == R.id.spnSeniorCellChurchgroup){
        	    	 
        	    	//churchsp
         		  		lblchurch.setVisibility(View.VISIBLE);
						lblpcf.setVisibility(View.VISIBLE);
						lblseniorcell.setVisibility(View.VISIBLE);
				
						spnCellChurch.setVisibility(View.GONE);
						txtPCF.setVisibility(View.GONE);
						spnSeniorCell.setVisibility(View.GONE);
        	    	 
        	     }
        	     
        	     if(spinner.getId() == R.id.spnCellChurch){
        	    	 
        	    	 lblpcf.setVisibility(View.VISIBLE);
						lblseniorcell.setVisibility(View.VISIBLE);
					
						txtPCF.setVisibility(View.GONE);
						spnSeniorCell.setVisibility(View.GONE);
         	    	 
         	     }
        	     
        	     if(spinner.getId() == R.id.txtPCFName){
        	    	 
        	    	 lblseniorcell.setVisibility(View.VISIBLE);
						spnSeniorCell.setVisibility(View.GONE);
         	    	 
         	     }
        	     
        	     
        	     
              /* switch (arg2) {
                  case 1:
                	  
                	  //if(zonesp){
      					
  						
  						
  					//}
  				  				
  					zonesp=true;
  					
                      break;
                  case 2:
                	 
        				
                		  
    						
    				
    			
    				
                	         
                      break;
                  case 3:
                	  //if(churchsp){
          				
                		
    						
    						
    					//}
    			
                	  churchsp=true;  
                      break;
                  case 4:
                	  //if(pcfsp){
            		
                		
  						
  					//}
  			
              	  churchsp=true;  
                      break;
                  case 5:
                      Toast.makeText(NewCreateCellMasterActivity.this, "Spinner 2", Toast.LENGTH_LONG).show();
                      break;
                  }
*/
          }

          @Override
          public void onNothingSelected(AdapterView<?> arg0) {
              // TODO Auto-generated method stub

          }
      };
      
      spnCellZone.setOnItemSelectedListener(myListener);
      spnSeniorCellChurchgroup.setOnItemSelectedListener(myListener);
      spnCellChurch.setOnItemSelectedListener(myListener);
      txtPCF.setOnItemSelectedListener(myListener);
      
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
						Methods.longToast("User name or Password is incorrect", NewCreateCellMasterActivity.this);
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
							if(null !=mHHSubModel.get(i).getPcf())
								mPCFList.add(mHHSubModel.get(i).getPcf());
							if(null !=mHHSubModel.get(i).getSenior_cell())
								mSeniorCellList.add(mHHSubModel.get(i).getSenior_cell());
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
					//		Methods.longToast("Access Denied", CreateSeniorCellMasterActivity.this);
				}
				//else
					//		Methods.longToast("Some Error Occured,please try again later", CreateSeniorCellMasterActivity.this);

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
							Methods.longToast("User name or Password is incorrect", NewCreateCellMasterActivity.this);
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
								}else if(defRole.equalsIgnoreCase("Regional Pastor")){
									mRegionList.add(jsonarray.getJSONObject(i).getString("name"));
								}
							}
							
							setAdapters();
							
						}else{
							
							Methods.longToast("Record Not Found ", NewCreateCellMasterActivity.this);
							
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
	
		
		
		
		
		ArrayAdapter<String> adapterZone = new ArrayAdapter<String>(NewCreateCellMasterActivity.this, android.R.layout.simple_spinner_item, mZoneList);
		adapterZone.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnCellZone.setAdapter(adapterZone);

		ArrayAdapter<String> adapterRegion = new ArrayAdapter<String>(NewCreateCellMasterActivity.this, android.R.layout.simple_spinner_item, mRegionList);
		adapterRegion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnCellRegion.setAdapter(adapterRegion);

		ArrayAdapter<String> adapterChurch = new ArrayAdapter<String>(NewCreateCellMasterActivity.this, android.R.layout.simple_spinner_item, mChurchList);
		adapterChurch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnCellChurch.setAdapter(adapterChurch);

		ArrayAdapter<String> adapterSrCell = new ArrayAdapter<String>(NewCreateCellMasterActivity.this, android.R.layout.simple_spinner_item, mSeniorCellList);
		adapterSrCell.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnSeniorCell.setAdapter(adapterSrCell);

		ArrayAdapter<String> adapterchurchgropu = new ArrayAdapter<String>(NewCreateCellMasterActivity.this, android.R.layout.simple_spinner_item, mGrpChurchList);
		adapterSrCell.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnSeniorCellChurchgroup.setAdapter(adapterchurchgropu);

		ArrayAdapter<String> adapterPCF = new ArrayAdapter<String>(NewCreateCellMasterActivity.this, android.R.layout.simple_spinner_item, mPCFList);
		adapterPCF.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		txtPCF.setAdapter(adapterPCF);
		
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
							Methods.longToast("User name or Password is incorrect", NewCreateCellMasterActivity.this);
					
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
								
								
								for(int i=0;i<jsonarray.length();i++){
									if( tbl.equalsIgnoreCase("PCFs")){
										
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
								}
							//	setAdapters();
											
								if(tbl.equalsIgnoreCase("Regions")){
									
									Log.e("condistion match","condistion match");
								 

									ArrayAdapter<String> adapterZone = new ArrayAdapter<String>(NewCreateCellMasterActivity.this, android.R.layout.simple_spinner_item, mZoneList);
									adapterZone.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
									spnCellZone.setAdapter(adapterZone);
									
									lblzone.setVisibility(View.GONE);
								    spnCellZone.setVisibility(View.VISIBLE);
								
								}else{
									Log.e("condistion match"," not condistion match");
								}
									
								
								if(tbl.equalsIgnoreCase("Zones")){
									Log.e("lable gone", "lable gone");
									
									ArrayAdapter<String> adapterchurchgropu = new ArrayAdapter<String>(NewCreateCellMasterActivity.this, android.R.layout.simple_spinner_item, mGrpChurchList);
									adapterchurchgropu.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
									spnSeniorCellChurchgroup.setAdapter(adapterchurchgropu);

									
									lblgroupchurch.setVisibility(View.GONE);
									spnSeniorCellChurchgroup.setVisibility(View.VISIBLE);
									
									
								}
							
								if(tbl.equalsIgnoreCase("Group Churches")){
									
									ArrayAdapter<String> adapterChurch = new ArrayAdapter<String>(NewCreateCellMasterActivity.this, android.R.layout.simple_spinner_item, mChurchList);
									adapterChurch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
									spnCellChurch.setAdapter(adapterChurch);
									
									lblchurch.setVisibility(View.GONE);
									spnCellChurch.setVisibility(View.VISIBLE);
								}
								
								if(tbl.equalsIgnoreCase("Churches")){
									
									ArrayAdapter<String> adapterPCF = new ArrayAdapter<String>(NewCreateCellMasterActivity.this, android.R.layout.simple_spinner_item, mPCFList);
									adapterPCF.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
									txtPCF.setAdapter(adapterPCF);
									
									lblpcf.setVisibility(View.GONE);
									txtPCF.setVisibility(View.VISIBLE);
								}
								
								if(tbl.equalsIgnoreCase("PCFs")){
									
									ArrayAdapter<String> adapterSrCell = new ArrayAdapter<String>(NewCreateCellMasterActivity.this, android.R.layout.simple_spinner_item, mSeniorCellList);
									adapterSrCell.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
									spnSeniorCell.setAdapter(adapterSrCell);
									
									lblseniorcell.setVisibility(View.GONE);
									spnSeniorCell.setVisibility(View.VISIBLE);
								}
								
								
								
							}else{
								Methods.longToast("Resord Not Found", NewCreateCellMasterActivity.this);
							}
							
					
						
					}
					
					
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				
				
				/*
				if(null != mHHModel.getStatus() && mHHModel.getStatus().trim().length() >0){

					if(mHHModel.getStatus()=="401"){
						Methods.longToast("User name or Password is incorrect", CreateCellMasterActivity.this);
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
				}*/
				//setAdapters();

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

protected void saveCellMaster() {
	StringRequest reqsaveCellMaster=new StringRequest(Method.POST,CreateCellService.SERVICE_URL,new Listener<String>() {

		@Override
		public void onResponse(String response) {
			Methods.closeProgressDialog();
			Log.d("droid","get reqsaveCellMaster ---------------"+ response);

			ResponseMessageModel2 model=gson.fromJson(response, ResponseMessageModel2.class);
			if(null !=model.getMessage().getMessage() && model.getMessage().getMessage().trim().length() >0){
				Methods.longToast(model.getMessage().getMessage(), NewCreateCellMasterActivity.this);
			}
			finish();
			Intent intForm=new Intent(NewCreateCellMasterActivity.this,DisplayMastersListActivity.class);
			intForm.putExtra("OptionSelected", "Cell");
			startActivity(intForm);

		}
	},new ErrorListener() {

		@Override
		public void onErrorResponse(VolleyError error) {
			Methods.closeProgressDialog();
			Log.d("droid","get reqsaveCellMaster error---------------"+ error.getMessage());

			Methods.longToast("Some Error Occured,please try again later", NewCreateCellMasterActivity.this);
		}
	}){
		@Override
		protected Map<String, String> getParams() throws AuthFailureError{
			Map<String, String> params = new HashMap<String, String>();

			CreateCellModel cellModel=new CreateCellModel();
//			cellModel.setCell_code(txtCellCode.getText().toString().trim());
			cellModel.setCell_name(txtCellName.getText().toString().trim());
			cellModel.setContact_phone_no(txtCellContactPhn.getText().toString().trim());
			cellModel.setContact_email_id(txtCellContactEmailId.getText().toString().trim());
			cellModel.setUsername(mPreferenceHelper.getString(Commons.USER_EMAILID));
			cellModel.setUserpass(mPreferenceHelper.getString(Commons.USER_PASSWORD));
			cellModel.setAddress(txtCellMeetingLoc.getText().toString().trim());
			cellModel.setMeeting_location(txtCellMeetingLoc.getText().toString().trim());

			cellModel.setPcf(mPCFList.get(0));
			
			cellModel.setChurch(spnCellChurch.getSelectedItem().toString());
			cellModel.setChurch_group(mGrpChurchList.get(0));
			cellModel.setRegion(spnCellRegion.getSelectedItem().toString());
			cellModel.setZone(spnCellZone.getSelectedItem().toString());	
			cellModel.setSenior_cell(spnSeniorCell.getSelectedItem().toString());
			cellModel.setPcf(txtPCF.getSelectedItem().toString());

			String dataString=gson.toJson(cellModel, CreateCellModel.class);

			Log.d("droid", dataString);


			params.put(CreateCellService.DATA, dataString);
			//				params.put(CreateCellService.CELL_CODE, txtCellCode.getText().toString().trim());
			//				params.put(CreateCellService.CELL_NAME, txtCellName.getText().toString().trim());
			//				params.put(CreateCellService.CHURCH, church.trim());
			//				params.put(CreateCellService.ZONE,zone.trim());
			//				params.put(CreateCellService.REGION, region.trim());
			//				params.put(CreateCellService.CHURCH_GROUP, churchGrpName);
			//				params.put(CreateCellService.CONTACT_EMAIL_ID,mPreferenceHelper.getString(Commons.USER_EMAILID));
			//				params.put(CreateCellService.CONTACT_PHONE_NO, "0");
			//				params.put(CreateCellService.PCF, pcf);
			//				params.put(CreateCellService.SENIOR_CELL, "0");
			return params;  
		}
	};

	App.getInstance().addToRequestQueue(reqsaveCellMaster, "reqsaveCellMaster");
	reqsaveCellMaster.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
}

private boolean validateFields() {
	
	Log.e("mChurchList size", mChurchList.size()+"");
	
	if(mGrpChurchList.size()>0)
	{
	if(mChurchList.size()>0)
	{
	
	if(mPCFList.size()>0)
	{
	
	if(mSeniorCellList.size()>0)
	{

	/*if(txtCellCode.getText().toString().trim().length() > 0)
	{*/
		if(txtCellName.getText().toString().trim().length() >0)
		{

//			if(txtCellContactPhn.getText().toString().trim().length() > 0){
//				if(txtCellContactEmailId.getText().toString().trim().length() > 0){
//					if(Methods.isValidEmail(txtCellContactEmailId.getText().toString())){
//						if(txtCellMeetingLoc.getText().toString().trim().length() > 0)
//						{
//							return true;
//						}else
//						{
//							Methods.longToast("Please enter Meeting location", this);
//						}
//					}else{
//						Methods.longToast("Please enter Valid email id", this);
//					}
//				}else{
//					Methods.longToast("Please enter Email Id", this);
//				}
//			}else{
//				Methods.longToast("Please enter Contact No.", this);
//			}
			return true;

		}else{
			Methods.longToast("Please enter Cell Name", this);
		}
	/*}else
	{
		Methods.longToast("Please enter Cell Code", this);
	}*/
	}else{
		
		Methods.longToast("Senior Cell  not avalible", this);
	}
	}else{
		
		Methods.longToast("Pcf is not avalible", this);
	}
	}else{
		
		Methods.longToast("Church is not avalible", this);
	}
	}else{
		Methods.longToast("Church Group is not avalible", this);
	}
	return false;
}
}
