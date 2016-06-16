package com.mutech.synergy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.mutech.synergy.activities.profile.MyProfileActivity;
import com.mutech.synergy.models.MeetingListRequestModel;
import com.mutech.synergy.models.MemberProfileModel;
import com.mutech.synergy.utils.Methods;
import com.mutech.synergy.utils.NetworkHelper;
import com.mutech.synergy.utils.PreferenceHelper;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ShortBio extends ActionBarActivity {

    private Button btnedit;
    ArrayList<MemberProfileModel.ProfileSubModel> mProfSubModel;
    private PreferenceHelper mPreferenceHelper;
    private Gson gson;
    private ImageView imgProfilePic;
    String Imageurl;
    private SimpleDateFormat dateFormatter,dateFormatterService;
    private TextView txtMemberDateOfBirth;
    private TextView txtMembershipNo,txtMemberName,txtMemberPhone,txtEmailID,txtMemberHomeAddress,txtDesignation,txtstatus,txtRole;

    String cellcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_short_bio);

        mProfSubModel=new ArrayList<MemberProfileModel.ProfileSubModel>();
        mPreferenceHelper=new PreferenceHelper(this);
        gson=new Gson();
        imgProfilePic=(ImageView) findViewById(R.id.imgProfilePic);
        btnedit = (Button) findViewById(R.id.btnEdit);
        txtMemberDateOfBirth=(TextView) findViewById(R.id.txtMemberDateOfBirth);
        txtMembershipNo=(TextView) findViewById(R.id.txtMembershipNo);
        txtMemberName=(TextView) findViewById(R.id.txtName);
        txtMemberPhone=(TextView) findViewById(R.id.txtMemberPhone1);
        txtEmailID=(TextView) findViewById(R.id.txtEmailID1);
        txtDesignation=(TextView) findViewById(R.id.txtDesignation);
        txtMemberHomeAddress=(TextView) findViewById(R.id.txtHomeAddress);
        txtstatus=(TextView) findViewById(R.id.txtStatus);
        txtRole=(TextView) findViewById(R.id.txtRole);
        dateFormatterService=new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        cellcode=getIntent().getStringExtra("cellcode");

        btnedit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent Int = new Intent(ShortBio.this, CellDetailsActivity.class);
                Int.putExtra("cellcode", cellcode);
                Int.putExtra("fromshortbio","fromshortbio");
                startActivity(Int);
                finish();
            }
        });


        if(NetworkHelper.isOnline(this)){
            Methods.showProgressDialog(this);
            //getProfileInfo();
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
                Log.d("droid","get reqgetTopHierarchy ---------------"+ response);
                try {

                    JSONObject jsonobj=new JSONObject(response);
                    JSONArray jarray=jsonobj.getJSONArray("message");


                    String str=jarray.getJSONObject(0).getString("cell_code");

                    if(str!=null)
                        txtMembershipNo.setText(jarray.getJSONObject(0).getString("cell_code"));

                    str=jarray.getJSONObject(0).getString("cell_name");

                    if(str!=null)
                        txtMemberName.setText(jarray.getJSONObject(0).getString("cell_name"));

                    if(!jarray.getJSONObject(0).getString("contact_phone_no").equals("null"))
                        txtMemberPhone.setText(jarray.getJSONObject(0).getString("contact_phone_no"));

                    if(!jarray.getJSONObject(0).getString("contact_email_id").equals("null"))
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
                    Methods.longToast("Access Denied", ShortBio.this);
                }
                else
                    Methods.longToast("Some Error Occured,please try again later", ShortBio.this);



            }

        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
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
