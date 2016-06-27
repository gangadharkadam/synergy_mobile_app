package com.mutech.synergy.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.mutech.databasedetails.CellDetailsActivity;
import com.mutech.synergy.App;
import com.mutech.synergy.R;
import com.mutech.synergy.SynergyValues;
import com.mutech.synergy.models.MeetingListRequestModel;
import com.mutech.synergy.models.MemberProfileModel;
import com.mutech.synergy.utils.Methods;
import com.mutech.synergy.utils.NetworkHelper;
import com.mutech.synergy.utils.PreferenceHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ShortBioCell extends ActionBarActivity {

    ArrayList<MemberProfileModel.ProfileSubModel> mProfSubModel;
    private PreferenceHelper mPreferenceHelper;
    private Gson gson;
    private TextView txtCellName, txtCellLeader, txtSeniorCellName, txtChurchGroup, txtChurch, txtPhone, txtEmailID;
    private String cellcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_short_bio_cell);

        getSupportActionBar().setTitle("Cell Profile");
        cellcode=getIntent().getStringExtra("cellcode");

        mProfSubModel=new ArrayList<MemberProfileModel.ProfileSubModel>();
        mPreferenceHelper=new PreferenceHelper(this);
        gson=new Gson();

        txtCellName=(TextView) findViewById(R.id.txtCellName);
        txtCellLeader=(TextView) findViewById(R.id.txtCellLeader);
        txtSeniorCellName = (TextView) findViewById(R.id.txtSeniorCellName);
        txtChurchGroup = (TextView) findViewById(R.id.txtChurchGroup);
        txtChurch = (TextView) findViewById(R.id.txtChurch);
        txtPhone=(TextView) findViewById(R.id.txtPhone);
        txtEmailID = (TextView) findViewById(R.id.txtEmailID);

        if(NetworkHelper.isOnline(this)){
            Methods.showProgressDialog(this);
            getCellDetails();
        }
        else
            Methods.longToast("Please connect to Internet", this);

    }


    private void getCellDetails() {

        StringRequest reqgetCellDetails=new StringRequest(Request.Method.POST, SynergyValues.Web.GetCellDetailsService.SERVICE_URL,new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Methods.closeProgressDialog();
                Log.d("droid", "get reqgetTopHierarchy ---------------" + response);
                try {

                    JSONObject jsonobj=new JSONObject(response);
                    JSONArray jarray=jsonobj.getJSONArray("message");


                    Log.d("NonStop", "Cell Name: " + jarray.getJSONObject(0).getString("cell_name"));
                    Log.d("NonStop", "Cell Leader: " + jarray.getJSONObject(0).getString("cell_leader"));
                    Log.d("NonStop", "Senior Cell: " + jarray.getJSONObject(0).getString("senior_cell"));
                    Log.d("NonStop", "Church Group: " + jarray.getJSONObject(0).getString("church_group"));
                    Log.d("NonStop", "Church Name: " + jarray.getJSONObject(0).getString("church"));
                    Log.d("NonStop", "Contact Phone: " + jarray.getJSONObject(0).getString("contact_phone_no"));
                    Log.d("NonStop", "Contact Email: " + jarray.getJSONObject(0).getString("contact_email_id"));

                    if(jarray.getJSONObject(0).getString("cell_name") != null)
                        txtCellName.setText(jarray.getJSONObject(0).getString("cell_name"));

                    if(jarray.getJSONObject(0).getString("cell_leader") != null)
                        txtCellLeader.setText(jarray.getJSONObject(0).getString("cell_leader"));

                    if(jarray.getJSONObject(0).getString("senior_cell") != null)
                        txtSeniorCellName.setText(jarray.getJSONObject(0).getString("senior_cell"));

                    if(jarray.getJSONObject(0).getString("church_group") != null)
                        txtChurchGroup.setText(jarray.getJSONObject(0).getString("church_group"));

                    if(jarray.getJSONObject(0).getString("church") != null)
                        txtChurch.setText(jarray.getJSONObject(0).getString("church"));

                    if(jarray.getJSONObject(0).getString("contact_phone_no") != null)
                        txtPhone.setText(jarray.getJSONObject(0).getString("contact_phone_no"));

                    if(jarray.getJSONObject(0).getString("contact_email_id") != null)
                        txtEmailID.setText(jarray.getJSONObject(0).getString("contact_email_id"));

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                Methods.closeProgressDialog();

            }
        },new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Methods.closeProgressDialog();
                Log.d("droid","get reqgetTopHierarchy error---------------"+ error.getCause());

                if(error.networkResponse.statusCode==403){
                    Methods.longToast("Access Denied", ShortBioCell.this);
                }
                else
                    Methods.longToast("Some Error Occured,please try again later", ShortBioCell.this);



            }

        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                MeetingListRequestModel model=new MeetingListRequestModel();
                model.setUsername(mPreferenceHelper.getString(SynergyValues.Commons.USER_EMAILID));
                model.setUserpass(mPreferenceHelper.getString(SynergyValues.Commons.USER_PASSWORD));
                model.setTbl("Cells");
                model.setName(cellcode);

                String dataString=gson.toJson(model, MeetingListRequestModel.class);

                Log.d("droid", dataString);
                params.put(SynergyValues.Web.GetHigherHierarchyService.DATA, dataString);
                return params;
            }
        };

        App.getInstance().addToRequestQueue(reqgetCellDetails, "reqgetCellDetails");
        reqgetCellDetails.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));

    }


}
