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

public class CreateZoneMasterFormActivity extends ActionBarActivity implements OnClickListener{

    private EditText txtSeniorCellName,txtSeniorCellMeetingLoc,txtSrCellContactPhn,txtSrCellContactEmailId;
    private Button btnSeniorCellSave;
    private ArrayList<String> mRegionList,mSrCellList;
    private PreferenceHelper mPreferenceHelper;
    private Gson gson;
    String zone= null,church= null,churchGroup= null,pcf= null,region= null,senior_cell = null;
    private Spinner spnSeniorCellRegion;
    String defKey,defVal,defRole;
    boolean mSelected=false;
    OnItemSelectedListener myListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_zone_master_form);
        initialize();
    }

    public boolean isValid() {

        if(!InputValidation.hasText(txtSeniorCellName)) {
            AlertDialog dialog = new AlertDialog.Builder(CreateZoneMasterFormActivity.this)
                    .setCancelable(false)
                    .setTitle("Mandatory Input")
                    .setMessage("Please enter Zone Name")
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
            AlertDialog dialog = new AlertDialog.Builder(CreateZoneMasterFormActivity.this)
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

        if(!InputValidation.isPhoneNumber(txtSrCellContactPhn, false)) {
            AlertDialog dialog = new AlertDialog.Builder(CreateZoneMasterFormActivity.this)
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
        tvTitle.setText("Zones      ");

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2E9AFE")));

        getSupportActionBar().setDisplayShowCustomEnabled(true);

        //	txtSeniorCellCode=(EditText) findViewById(R.id.txtSeniorCellCode);
        txtSeniorCellName=(EditText) findViewById(R.id.txtZoneName);
        spnSeniorCellRegion=(Spinner) findViewById(R.id.spnSeniorCellRegion);

        txtSeniorCellMeetingLoc=(EditText) findViewById(R.id.txtSeniorCellMeetingLoc);
        txtSrCellContactPhn=(EditText) findViewById(R.id.txtSrCellContactPhn);
        txtSrCellContactEmailId=(EditText) findViewById(R.id.txtSrCellContactEmailId);

        btnSeniorCellSave=(Button) findViewById(R.id.btnSeniorCellSave);
        btnSeniorCellSave.setOnClickListener(this);

        mRegionList=new ArrayList<String>();
        mSrCellList=new ArrayList<String>();

        mPreferenceHelper=new PreferenceHelper(this);
        gson=new Gson();

        setAdapters();
        defKey=mPreferenceHelper.getString(Commons.USER_DEFKEY);
        defVal=mPreferenceHelper.getString(Commons.USER_DEFVALUE);
        defRole=mPreferenceHelper.getString(Commons.USER_ROLE);

        if(mPreferenceHelper.getString(Commons.USER_ROLE).equals("PCF Leader")){

            spnSeniorCellRegion.setVisibility(View.VISIBLE);

            spnSeniorCellRegion.setEnabled(false);

        }
        if(mPreferenceHelper.getString(Commons.USER_ROLE).equals("Senior Cell Leader")){

            spnSeniorCellRegion.setVisibility(View.VISIBLE);
            spnSeniorCellRegion.setEnabled(false);

        }

        if(mPreferenceHelper.getString(Commons.USER_ROLE).equals("Church Pastor")){


            spnSeniorCellRegion.setVisibility(View.VISIBLE);

            spnSeniorCellRegion.setEnabled(false);


        }

        if(mPreferenceHelper.getString(Commons.USER_ROLE).equals("Group Church Pastor")){

            spnSeniorCellRegion.setVisibility(View.VISIBLE);

            spnSeniorCellRegion.setEnabled(false);

        }

        if(mPreferenceHelper.getString(Commons.USER_ROLE).equals("Zonal Pastor")){


            spnSeniorCellRegion.setEnabled(false);
            spnSeniorCellRegion.setEnabled(false);
        }


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
                        Methods.longToast("User name or Password is incorrect", CreateZoneMasterFormActivity.this);
                    }
                }else{
                    if(null !=mHHModel.getMessage() && mHHModel.getMessage().size() >0){
                        ArrayList<HHSubModel> mHHSubModel=new ArrayList<HHSubModel>();
                        //if(meetingmsg instanceof JSONArray){
                        mHHSubModel=mHHModel.getMessage();

                        for(int i=0;i<mHHSubModel.size();i++){
                            if(null !=mHHSubModel.get(i).getRegion())
                                mRegionList.add(mHHSubModel.get(i).getRegion());

                        }

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
                            Methods.longToast("User name or Password is incorrect", CreateZoneMasterFormActivity.this);
                        }

                    }else{

                        JSONArray jsonarray=jsonobj.getJSONArray("message");

                        if(jsonarray.length()>0){

                            for(int i=0;i<jsonarray.length();i++){

                                if(defRole.equalsIgnoreCase("Regional Pastor")){
                                    mRegionList.add(jsonarray.getJSONObject(i).getString("name"));
                                }
                            }

                            setAdapters();

                        }else{

                            Methods.longToast("Record Not Found ", CreateZoneMasterFormActivity.this);
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


        ArrayAdapter<String> adapterRegion = new ArrayAdapter<String>(CreateZoneMasterFormActivity.this, android.R.layout.simple_spinner_item, mRegionList);
        adapterRegion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnSeniorCellRegion.setAdapter(adapterRegion);

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
            Methods.longToast("Please enter Senior Name", this);
        }
		/*}else
		{
			Methods.longToast("Please enter Senior Cell Code", this);
		}*/
        return false;
    }

    protected void saveCellMaster() {
        StringRequest reqsaveCellMaster=new StringRequest(Method.POST, SynergyValues.Web.CreateZoneService.SERVICE_URL,new Listener<String>() {

            @Override
            public void onResponse(String response) {
                Methods.closeProgressDialog();
                Log.d("droid","get reqsaveCellMaster ---------------"+ response);

                if(response.contains("status"))
                {
                    ResponseMessageModel2 model=gson.fromJson(response, ResponseMessageModel2.class);
                    if(null !=model.getMessage().getMessage() && model.getMessage().getMessage().trim().length() >0){
                        Methods.longToast(model.getMessage().getMessage(), CreateZoneMasterFormActivity.this);
                    }
                }else{
                    ResponseMessageModel model=gson.fromJson(response, ResponseMessageModel.class);

                    if(null !=model.getMessage() && model.getMessage().trim().length() >0){
                        Methods.longToast(model.getMessage(), CreateZoneMasterFormActivity.this);
                    }
                    finish();
                  /*  Intent intForm=new Intent(CreateZoneMasterFormActivity.this,DisplayMastersListActivity.class);
                    intForm.putExtra("OptionSelected", "Zones");
                    startActivity(intForm);*/
                }
            }
        },new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Methods.closeProgressDialog();
                Log.d("droid","get reqsaveCellMaster error---------------"+ error.getMessage());

                Methods.longToast("Some Error Occured,please try again later", CreateZoneMasterFormActivity.this);
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                CreateSrCellModel srCellModel=new CreateSrCellModel();
                //	srCellModel.setSenior_cell_code(txtSeniorCellCode.getText().toString().trim());
                srCellModel.setZone(txtSeniorCellName.getText().toString().trim());
                srCellModel.setContact_email_id(txtSrCellContactEmailId.getText().toString().trim());
                srCellModel.setContact_phone_no(txtSrCellContactPhn.getText().toString().trim());

                //spinners on UI
                srCellModel.setRegion(spnSeniorCellRegion.getSelectedItem().toString());

                srCellModel.setMeeting_location(txtSeniorCellMeetingLoc.getText().toString().trim());
                srCellModel.setUsername(mPreferenceHelper.getString(Commons.USER_EMAILID));
                srCellModel.setUserpass(mPreferenceHelper.getString(Commons.USER_PASSWORD));


                String dataString=gson.toJson(srCellModel,CreateSrCellModel.class);

                Log.d("droid", dataString);

                params.put(SynergyValues.Web.CreateZoneService.DATA, dataString);

                return params;
            }
        };
        App.getInstance().addToRequestQueue(reqsaveCellMaster, "reqsaveCellMaster");
        reqsaveCellMaster.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
    }

}
