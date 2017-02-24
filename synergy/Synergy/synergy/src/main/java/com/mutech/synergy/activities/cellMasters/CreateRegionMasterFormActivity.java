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

public class CreateRegionMasterFormActivity extends ActionBarActivity implements OnClickListener{

    private EditText txtSeniorCellName,txtSeniorCellMeetingLoc,txtSrCellContactPhn,txtSrCellContactEmailId;
    private Button btnSeniorCellSave;
    private PreferenceHelper mPreferenceHelper;
    private Gson gson;
    String zone= null,church= null,churchGroup= null,pcf= null,region= null,senior_cell = null;
    String defKey,defVal,defRole;
    boolean mSelected=false;
    OnItemSelectedListener myListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_region_master_form);
        initialize();
    }

    public boolean isValid() {

        if(!InputValidation.hasText(txtSeniorCellName)) {
            AlertDialog dialog = new AlertDialog.Builder(CreateRegionMasterFormActivity.this)
                    .setCancelable(false)
                    .setTitle("Mandatory Input")
                    .setMessage("Please enter Region Name")
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
            AlertDialog dialog = new AlertDialog.Builder(CreateRegionMasterFormActivity.this)
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
        tvTitle.setText("Regions      ");

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2E9AFE")));

        getSupportActionBar().setDisplayShowCustomEnabled(true);

        //	txtSeniorCellCode=(EditText) findViewById(R.id.txtSeniorCellCode);
        txtSeniorCellName=(EditText) findViewById(R.id.txtRegionName);

        txtSeniorCellMeetingLoc=(EditText) findViewById(R.id.txtSeniorCellMeetingLoc);
        txtSrCellContactPhn=(EditText) findViewById(R.id.txtSrCellContactPhn);
        txtSrCellContactEmailId=(EditText) findViewById(R.id.txtSrCellContactEmailId);

        btnSeniorCellSave=(Button) findViewById(R.id.btnSeniorCellSave);
        btnSeniorCellSave.setOnClickListener(this);


        mPreferenceHelper=new PreferenceHelper(this);
        gson=new Gson();

        defKey=mPreferenceHelper.getString(Commons.USER_DEFKEY);
        defVal=mPreferenceHelper.getString(Commons.USER_DEFVALUE);
        defRole=mPreferenceHelper.getString(Commons.USER_ROLE);

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
            Methods.longToast("Please enter Region Name", this);
        }

        return false;
    }

    protected void saveCellMaster() {
        StringRequest reqsaveCellMaster=new StringRequest(Method.POST, SynergyValues.Web.CreateRegionService.SERVICE_URL,new Listener<String>() {

            @Override
            public void onResponse(String response) {
                Methods.closeProgressDialog();
                Log.d("droid","get reqsaveCellMaster ---------------"+ response);

                if(response.contains("status"))
                {
                    ResponseMessageModel2 model=gson.fromJson(response, ResponseMessageModel2.class);
                    if(null !=model.getMessage().getMessage() && model.getMessage().getMessage().trim().length() >0){
                        Methods.longToast(model.getMessage().getMessage(), CreateRegionMasterFormActivity.this);
                    }
                }else{
                    ResponseMessageModel model=gson.fromJson(response, ResponseMessageModel.class);

                    if(null !=model.getMessage() && model.getMessage().trim().length() >0){
                        Methods.longToast(model.getMessage(), CreateRegionMasterFormActivity.this);
                    }
                    finish();
                  /*  Intent intForm=new Intent(CreateRegionMasterFormActivity.this,DisplayMastersListActivity.class);
                    intForm.putExtra("OptionSelected", "Regions");
                    startActivity(intForm);*/
                }
            }
        },new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Methods.closeProgressDialog();
                Log.d("droid","get reqsaveCellMaster error---------------"+ error.getMessage());

                Methods.longToast("Some Error Occured,please try again later", CreateRegionMasterFormActivity.this);
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                CreateSrCellModel srCellModel=new CreateSrCellModel();
                //	srCellModel.setSenior_cell_code(txtSeniorCellCode.getText().toString().trim());
                srCellModel.setRegion(txtSeniorCellName.getText().toString().trim());
                srCellModel.setContact_email_id(txtSrCellContactEmailId.getText().toString().trim());
                srCellModel.setContact_phone_no(txtSrCellContactPhn.getText().toString().trim());
                srCellModel.setMeeting_location(txtSeniorCellMeetingLoc.getText().toString().trim());
                srCellModel.setUsername(mPreferenceHelper.getString(Commons.USER_EMAILID));
                srCellModel.setUserpass(mPreferenceHelper.getString(Commons.USER_PASSWORD));


                String dataString=gson.toJson(srCellModel,CreateSrCellModel.class);

                Log.d("droid", dataString);

                params.put(SynergyValues.Web.CreateRegionService.DATA, dataString);

                return params;
            }
        };
        App.getInstance().addToRequestQueue(reqsaveCellMaster, "reqsaveCellMaster");
        reqsaveCellMaster.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
    }

}
