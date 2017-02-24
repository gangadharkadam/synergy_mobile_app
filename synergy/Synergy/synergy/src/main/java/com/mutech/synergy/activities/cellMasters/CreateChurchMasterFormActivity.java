package com.mutech.synergy.activities.cellMasters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.mutech.synergy.SynergyValues.Web.GetHigherHierarchyService;
import com.mutech.synergy.SynergyValues.Web.LowerHierarchyService;
import com.mutech.synergy.SynergyValues.Web.fillLoginSpinner;
import com.mutech.synergy.models.CreateSrCellModel;
import com.mutech.synergy.models.HigherHierarchyRespModel;
import com.mutech.synergy.models.HigherHierarchyRespModel.HHSubModel;
import com.mutech.synergy.models.MeetingListRequestModel;
import com.mutech.synergy.models.ResponseMessageModel;
import com.mutech.synergy.models.ResponseMessageModel2;
import com.mutech.synergy.utils.InputValidation;
import com.mutech.synergy.utils.Methods;
import com.mutech.synergy.utils.NetworkHelper;
import com.mutech.synergy.utils.PreferenceHelper;

public class CreateChurchMasterFormActivity extends ActionBarActivity implements OnClickListener{

    private EditText txtSeniorCellName,txtSeniorCellMeetingLoc,txtSrCellContactPhn,txtSrCellContactEmailId;
    private Button btnSeniorCellSave;
    private ArrayList<String> mZoneList,mRegionList,mGrpChurchList,mSrCellList;
    private PreferenceHelper mPreferenceHelper;
    private Gson gson;
    String zone= null,church= null,churchGroup= null,pcf= null,region= null,senior_cell = null;
    private Spinner spnSeniorCellZone,spnSeniorCellRegion,spnSeniorCellChurchgroup;
    String defKey,defVal,defRole;
    boolean mSelected=false;
    OnItemSelectedListener myListener;
    TextView lblzone,lblgroupchurch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_church_master_form);
        initialize();
    }

    public boolean isValid() {

        if(!InputValidation.hasText(txtSeniorCellName)) {
            AlertDialog dialog = new AlertDialog.Builder(CreateChurchMasterFormActivity.this)
                    .setCancelable(false)
                    .setTitle("Mandatory Input")
                    .setMessage("Please enter Church Name")
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
        if(!InputValidation.spnHasText(spnSeniorCellZone, "Zone")) {
            AlertDialog dialog = new AlertDialog.Builder(CreateChurchMasterFormActivity.this)
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
        if(!InputValidation.spnHasText(spnSeniorCellRegion, "Region")) {
            AlertDialog dialog = new AlertDialog.Builder(CreateChurchMasterFormActivity.this)
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
            AlertDialog dialog = new AlertDialog.Builder(CreateChurchMasterFormActivity.this)
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

        if(!InputValidation.isPhoneNumber(txtSrCellContactPhn, false)) {
            AlertDialog dialog = new AlertDialog.Builder(CreateChurchMasterFormActivity.this)
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
    @SuppressLint("NewApi")
    private void initialize() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar);
        TextView tvTitle=(TextView)getSupportActionBar().getCustomView().findViewById(R.id.title_text);
        tvTitle.setText("Churchs      ");

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2E9AFE")));

        getSupportActionBar().setDisplayShowCustomEnabled(true);

        lblzone=(TextView) findViewById(R.id.lblzone);
        lblgroupchurch=(TextView) findViewById(R.id.lblgroupchurch);


        //	txtSeniorCellCode=(EditText) findViewById(R.id.txtSeniorCellCode);
        txtSeniorCellName=(EditText) findViewById(R.id.txtChurchName);
        spnSeniorCellZone=(Spinner) findViewById(R.id.spnSeniorCellZone);
        spnSeniorCellRegion=(Spinner) findViewById(R.id.spnSeniorCellRegion);
        spnSeniorCellChurchgroup=(Spinner) findViewById(R.id.spnSeniorCellChurchgroup);

        txtSeniorCellMeetingLoc=(EditText) findViewById(R.id.txtSeniorCellMeetingLoc);
        txtSrCellContactPhn=(EditText) findViewById(R.id.txtSrCellContactPhn);
        txtSrCellContactEmailId=(EditText) findViewById(R.id.txtSrCellContactEmailId);

        btnSeniorCellSave=(Button) findViewById(R.id.btnSeniorCellSave);
        btnSeniorCellSave.setOnClickListener(this);

        mZoneList=new ArrayList<String>();
        mRegionList=new ArrayList<String>();
        mGrpChurchList=new ArrayList<String>();
        mSrCellList=new ArrayList<String>();

        mPreferenceHelper=new PreferenceHelper(this);
        gson=new Gson();

        setAdapters();
        defKey=mPreferenceHelper.getString(Commons.USER_DEFKEY);
        defVal=mPreferenceHelper.getString(Commons.USER_DEFVALUE);
        defRole=mPreferenceHelper.getString(Commons.USER_ROLE);

        if(mPreferenceHelper.getString(Commons.USER_ROLE).equals("PCF Leader")){


            lblzone.setVisibility(View.GONE);
            lblgroupchurch.setVisibility(View.GONE);

            //lblseniorcell.setVisibility(View.GONE);

            spnSeniorCellZone.setVisibility(View.VISIBLE);
            spnSeniorCellRegion.setVisibility(View.VISIBLE);
            spnSeniorCellChurchgroup.setVisibility(View.VISIBLE);


            spnSeniorCellZone.setEnabled(false);
            spnSeniorCellRegion.setEnabled(false);
            spnSeniorCellChurchgroup.setEnabled(false);

        }
        if(mPreferenceHelper.getString(Commons.USER_ROLE).equals("Senior Cell Leader")){


            lblzone.setVisibility(View.GONE);
            lblgroupchurch.setVisibility(View.GONE);


            spnSeniorCellZone.setVisibility(View.VISIBLE);
            spnSeniorCellRegion.setVisibility(View.VISIBLE);
            spnSeniorCellChurchgroup.setVisibility(View.VISIBLE);


            spnSeniorCellZone.setEnabled(false);
            spnSeniorCellRegion.setEnabled(false);
            spnSeniorCellChurchgroup.setEnabled(false);

        }

        if(mPreferenceHelper.getString(Commons.USER_ROLE).equals("Church Pastor")){

            lblzone.setVisibility(View.GONE);
            lblgroupchurch.setVisibility(View.GONE);
            //	lblpcf.setVisibility(View.GONE);
            //	lblseniorcell.setVisibility(View.GONE);

            spnSeniorCellZone.setVisibility(View.VISIBLE);
            spnSeniorCellRegion.setVisibility(View.VISIBLE);
            spnSeniorCellChurchgroup.setVisibility(View.VISIBLE);
            //	txtPCF.setVisibility(View.VISIBLE);
            //	spnSeniorCell.setVisibility(View.VISIBLE);


            spnSeniorCellZone.setEnabled(false);
            spnSeniorCellRegion.setEnabled(false);
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

            spnSeniorCellZone.setVisibility(View.VISIBLE);
            spnSeniorCellRegion.setVisibility(View.VISIBLE);
            spnSeniorCellChurchgroup.setVisibility(View.VISIBLE);
            //	spnCellChurch.setVisibility(View.VISIBLE);
            //	txtPCF.setVisibility(View.VISIBLE);
            //	spnSeniorCell.setVisibility(View.VISIBLE);


            spnSeniorCellZone.setEnabled(false);
            spnSeniorCellRegion.setEnabled(false);
            //	spnSeniorCellChurchgroup.setEnabled(false);
            //spnCellChurch.setEnabled(false);
            //txtPCF.setEnabled(false);

        }

        if(mPreferenceHelper.getString(Commons.USER_ROLE).equals("Zonal Pastor")){

            lblzone.setVisibility(View.GONE);

            spnSeniorCellRegion.setEnabled(false);
            spnSeniorCellZone.setVisibility(View.VISIBLE);
            spnSeniorCellRegion.setEnabled(false);
        }

        lblzone.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if(NetworkHelper.isOnline(CreateChurchMasterFormActivity.this)){

                    String selval=spnSeniorCellRegion.getSelectedItem().toString();

                    Methods.showProgressDialog(CreateChurchMasterFormActivity.this);
                    getSpinnerData("Regions",selval );

                }else
                {
                    Methods.longToast("Please connect to Internet",CreateChurchMasterFormActivity.this);
                }


            }
        });


        lblgroupchurch.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                String selval="";

                if(NetworkHelper.isOnline(CreateChurchMasterFormActivity.this)){

                    if(lblzone.getVisibility()==View.VISIBLE){
                        Methods.smallToast("Please Select Zone", CreateChurchMasterFormActivity.this);

                    }else{
                        selval=spnSeniorCellZone.getSelectedItem().toString();
                        Methods.showProgressDialog(CreateChurchMasterFormActivity.this);
                        getSpinnerData("Zones",selval );

                    }

                }else
                {
                    Methods.longToast("Please connect to Internet",CreateChurchMasterFormActivity.this);
                }


            }
        });

        myListener=new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1, int arg2,long arg3) {


                Spinner spinner = (Spinner) parent;
                if(spinner.getId() == R.id.spnSeniorCellZone){

                    lblgroupchurch.setVisibility(View.VISIBLE);

                    //	spnCellZone.setVisibility(View.GONE);
                    //		spnCellRegion.setVisibility(View.VISIBLE);
                    spnSeniorCellChurchgroup.setVisibility(View.GONE);

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        };

        spnSeniorCellZone.setOnItemSelectedListener(myListener);
        spnSeniorCellChurchgroup.setOnItemSelectedListener(myListener);


        if(NetworkHelper.isOnline(this)){
            Methods.showProgressDialog(this);

            if(defRole.equals("Regional Pastor")){
                getLowerHierarchy();
            }else{
                getTopHierarchy();
            }

            //
        }else
        {
            Methods.longToast("Please connect to Internet", this);
        }

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
                            Methods.longToast("User name or Password is incorrect", CreateChurchMasterFormActivity.this);

                    }
                    else
                    {

                        JSONArray jsonarray=jsonobj.getJSONArray("message");

                        if(jsonarray.length()>0){


                            if(tbl.equalsIgnoreCase("Zones"))
                                mGrpChurchList.clear();
                            if(tbl.equalsIgnoreCase("Regions"))
                                mZoneList.clear();


                            for(int i=0;i<jsonarray.length();i++){
                                if(tbl.equalsIgnoreCase("Zones")){

                                    mGrpChurchList.add(jsonarray.getJSONObject(i).getString("name"));

                                }else if(tbl.equalsIgnoreCase("Regions")){

                                    mZoneList.add(jsonarray.getJSONObject(i).getString("name"));

                                }
                            }
                            //	setAdapters();

                            if(tbl.equalsIgnoreCase("Regions")){

                                Log.e("condistion match","condistion match");


                                ArrayAdapter<String> adapterZone = new ArrayAdapter<String>(CreateChurchMasterFormActivity.this, android.R.layout.simple_spinner_item, mZoneList);
                                adapterZone.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spnSeniorCellZone.setAdapter(adapterZone);

                                lblzone.setVisibility(View.GONE);
                                spnSeniorCellZone.setVisibility(View.VISIBLE);

                            }else{
                                Log.e("condistion match"," not condistion match");
                            }


                            if(tbl.equalsIgnoreCase("Zones")){
                                Log.e("lable gone", "lable gone");

                                ArrayAdapter<String> adapterchurchgropu = new ArrayAdapter<String>(CreateChurchMasterFormActivity.this, android.R.layout.simple_spinner_item, mGrpChurchList);
                                adapterchurchgropu.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spnSeniorCellChurchgroup.setAdapter(adapterchurchgropu);


                                lblgroupchurch.setVisibility(View.GONE);
                                spnSeniorCellChurchgroup.setVisibility(View.VISIBLE);
                            }

                        }else{
                            Methods.longToast("Resord Not Found", CreateChurchMasterFormActivity.this);
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
                        Methods.longToast("User name or Password is incorrect", CreateChurchMasterFormActivity.this);
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
                    //		Methods.longToast("Access Denied", CreateSeniorCellMasterActivity.this);
                }

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
                            Methods.longToast("User name or Password is incorrect", CreateChurchMasterFormActivity.this);
                        }

                    }else{

                        JSONArray jsonarray=jsonobj.getJSONArray("message");

                        if(jsonarray.length()>0){

                            for(int i=0;i<jsonarray.length();i++){

                                if( defRole.equalsIgnoreCase("Zonal Pastor")){
                                    mZoneList.add(jsonarray.getJSONObject(i).getString("name"));
                                }else if(defRole.equalsIgnoreCase("Group Church Pastor")){
                                    mGrpChurchList.add(jsonarray.getJSONObject(i).getString("name"));
                                }else if(defRole.equalsIgnoreCase("Regional Pastor")){
                                    mRegionList.add(jsonarray.getJSONObject(i).getString("name"));
                                }
                            }

                            setAdapters();

                        }else{

                            Methods.longToast("Record Not Found ", CreateChurchMasterFormActivity.this);

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
        String defKey=mPreferenceHelper.getString(Commons.USER_DEFKEY);
        String defVal=mPreferenceHelper.getString(Commons.USER_DEFVALUE);
        String defRole=mPreferenceHelper.getString(Commons.USER_ROLE);

        Log.e("defval",defKey);

        ArrayAdapter<String> adapterZone = new ArrayAdapter<String>(CreateChurchMasterFormActivity.this, android.R.layout.simple_spinner_item, mZoneList);
        adapterZone.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnSeniorCellZone.setAdapter(adapterZone);

        ArrayAdapter<String> adapterRegion = new ArrayAdapter<String>(CreateChurchMasterFormActivity.this, android.R.layout.simple_spinner_item, mRegionList);
        adapterRegion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnSeniorCellRegion.setAdapter(adapterRegion);

        ArrayAdapter<String> adapterchurchgroup = new ArrayAdapter<String>(CreateChurchMasterFormActivity.this, android.R.layout.simple_spinner_item, mGrpChurchList);
        adapterchurchgroup.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnSeniorCellChurchgroup.setAdapter(adapterchurchgroup);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSeniorCellSave:
                if(isValid()) {
                    if(validateFields()){
                        if(NetworkHelper.isOnline(this)){
                            Methods.showProgressDialog(this);
                            saveCellMaster();
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
		/*if(txtSeniorCellCode.getText().toString().trim().length() > 0)
		{*/
        if(txtSeniorCellName.getText().toString().trim().length() >0)
        {

            if(txtSrCellContactPhn.getText().toString().trim().length() > 0){
                if(txtSrCellContactEmailId.getText().toString().trim().length() > 0){
                    if(Methods.isValidEmail(txtSrCellContactEmailId.getText().toString())){
                        if(txtSeniorCellMeetingLoc.getText().toString().trim().length() >0)
                        {
                            return true;
                        }else
                        {
                            Methods.longToast("Please enter Meeting Location", this);
                        }
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
            Methods.longToast("Please enter Church Name", this);
        }
		/*}else
		{
			Methods.longToast("Please enter Senior Cell Code", this);
		}*/
        return false;
    }

    protected void saveCellMaster() {
        StringRequest reqsaveCellMaster=new StringRequest(Method.POST, SynergyValues.Web.CreateChurchService.SERVICE_URL,new Listener<String>() {

            @Override
            public void onResponse(String response) {
                Methods.closeProgressDialog();
                Log.d("droid","get reqsaveCellMaster ---------------"+ response);

                if(response.contains("status"))
                {
                    ResponseMessageModel2 model=gson.fromJson(response, ResponseMessageModel2.class);
                    if(null !=model.getMessage().getMessage() && model.getMessage().getMessage().trim().length() >0){
                        Methods.longToast(model.getMessage().getMessage(), CreateChurchMasterFormActivity.this);
                    }
                }else{
                    ResponseMessageModel model=gson.fromJson(response, ResponseMessageModel.class);

                    if(null !=model.getMessage() && model.getMessage().trim().length() >0){
                        Methods.longToast(model.getMessage(), CreateChurchMasterFormActivity.this);
                    }
                    finish();
                 /*   Intent intForm=new Intent(CreateChurchMasterFormActivity.this,DisplayMastersListActivity.class);
                    intForm.putExtra("OptionSelected", "Churches");
                    startActivity(intForm);*/
                }
            }
        },new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Methods.closeProgressDialog();
                Log.d("droid","get reqsaveCellMaster error---------------"+ error.getMessage());

                Methods.longToast("Some Error Occured,please try again later", CreateChurchMasterFormActivity.this);
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                CreateSrCellModel srCellModel=new CreateSrCellModel();
                //	srCellModel.setSenior_cell_code(txtSeniorCellCode.getText().toString().trim());
                srCellModel.setChurch(txtSeniorCellName.getText().toString().trim());
                srCellModel.setContact_email_id(txtSrCellContactEmailId.getText().toString().trim());
                srCellModel.setContact_phone_no(txtSrCellContactPhn.getText().toString().trim());

                srCellModel.setChurch_group(mGrpChurchList.get(0));
                //spinners on UI
                srCellModel.setRegion(spnSeniorCellRegion.getSelectedItem().toString());
                srCellModel.setZone(spnSeniorCellZone.getSelectedItem().toString());

                srCellModel.setMeeting_location(txtSeniorCellMeetingLoc.getText().toString().trim());
                srCellModel.setUsername(mPreferenceHelper.getString(Commons.USER_EMAILID));
                srCellModel.setUserpass(mPreferenceHelper.getString(Commons.USER_PASSWORD));


                String dataString=gson.toJson(srCellModel,CreateSrCellModel.class);

                Log.d("droid", dataString);

                params.put(SynergyValues.Web.CreateChurchService.DATA, dataString);

                return params;
            }
        };
        App.getInstance().addToRequestQueue(reqsaveCellMaster, "reqsaveCellMaster");
        reqsaveCellMaster.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
    }

}
