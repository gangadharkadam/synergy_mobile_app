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
import com.mutech.synergy.models.MemberShortProfile;
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

    ArrayList<MemberProfileModel.ProfileSubModel> mProfSubModel;
    private PreferenceHelper mPreferenceHelper;
    private Gson gson;

    private ImageView imgProfilePic;

    private String Imageurl;
    private TextView txtCellLeaderId, txtCellLeaderName, txtCellLeaderEmailId, txtCellLeaderDateOfBirth, txtCellLeaderPhone, txtCellLeaderAddress;
    private TextView txtCellLeaderIdLbl, txtCellLeaderNameLbl, txtCellLeaderEmailIdLbl, txtCellLeaderDateOfBirthLbl, txtCellLeaderPhoneLbl, txtCellLeaderAddressLbl;
    private String cellcode;
    private String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_short_bio);
        getSupportActionBar().setTitle("Cell Leader Profile");

        mProfSubModel=new ArrayList<MemberProfileModel.ProfileSubModel>();
        mPreferenceHelper=new PreferenceHelper(this);
        gson=new Gson();

        role = getIntent().getStringExtra("role");

        imgProfilePic=(ImageView) findViewById(R.id.imgProfilePic);
        txtCellLeaderId = (TextView) findViewById(R.id.txtCellLeaderId);
        txtCellLeaderName = (TextView) findViewById(R.id.txtCellLeaderName);
        txtCellLeaderEmailId = (TextView) findViewById(R.id.txtLeaderEmailId);
        txtCellLeaderDateOfBirth = (TextView) findViewById(R.id.txtCellLeaderDateOfBirth);
        txtCellLeaderPhone = (TextView) findViewById(R.id.txtCellLeaderPhone);
        txtCellLeaderAddress = (TextView) findViewById(R.id.txtCellLeaderAddress);

        txtCellLeaderIdLbl = (TextView) findViewById(R.id.txtCellLeaderIdLbl);
        txtCellLeaderNameLbl = (TextView) findViewById(R.id.txtCellLeaderNameLbl);
        txtCellLeaderEmailIdLbl = (TextView) findViewById(R.id.txtLeaderEmailIdLbl);
        txtCellLeaderDateOfBirthLbl = (TextView) findViewById(R.id.txtCellLeaderDateOfBirthLbl);
        txtCellLeaderPhoneLbl = (TextView) findViewById(R.id.txtCellLeaderPhoneLbl);
        txtCellLeaderAddressLbl = (TextView) findViewById(R.id.txtCellLeaderAddressLbl);

        switch(role) {
            case "Cell Leader":
                getSupportActionBar().setTitle("Cell Leader Profile");
                txtCellLeaderIdLbl.setText("Cell Leader Id");
                txtCellLeaderNameLbl.setText("Cell Leader Name");
                txtCellLeaderEmailIdLbl.setText("Cell Leader Email Id");
                txtCellLeaderDateOfBirthLbl.setText("Date of Birth");
                txtCellLeaderPhoneLbl.setText("Phone Number");
                txtCellLeaderAddressLbl.setText("Home Address");
                break;
            case "Senior Cell Leader":
                getSupportActionBar().setTitle("Senior Cell Leader Profile");
                txtCellLeaderIdLbl.setText("Senior Cell Leader Id");
                txtCellLeaderNameLbl.setText("Senior Cell Leader Name");
                txtCellLeaderEmailIdLbl.setText("Senior Cell Leader Email Id");
                txtCellLeaderDateOfBirthLbl.setText("Date of Birth");
                txtCellLeaderPhoneLbl.setText("Phone Number");
                txtCellLeaderAddressLbl.setText("Home Address");
                break;
            case "PCF Leader":
                getSupportActionBar().setTitle("PCF Leader Profile");
                txtCellLeaderIdLbl.setText("PCF Leader Id");
                txtCellLeaderNameLbl.setText("PCF Leader Name");
                txtCellLeaderEmailIdLbl.setText("PCF Leader Email Id");
                txtCellLeaderDateOfBirthLbl.setText("Date of Birth");
                txtCellLeaderPhoneLbl.setText("Phone Number");
                txtCellLeaderAddressLbl.setText("Home Address");
                break;
            case "Member":
                getSupportActionBar().setTitle("Member Info");
                txtCellLeaderIdLbl.setText("Member Id");
                txtCellLeaderNameLbl.setText("Member Name");
                txtCellLeaderEmailIdLbl.setText("Email Id");
                txtCellLeaderDateOfBirthLbl.setText("Date of Birth");
                txtCellLeaderPhoneLbl.setText("Phone Number");
                txtCellLeaderAddressLbl.setText("Home Address");
                break;
        }

        cellcode=getIntent().getStringExtra("cellcode");

        if(NetworkHelper.isOnline(this)){
            Methods.showProgressDialog(this);
            getCellDetails();
        }
        else
            Methods.longToast("Please connect to Internet", this);

    }


    private void getCellDetails() {

        StringRequest reqgetCellDetails=new StringRequest(Request.Method.POST, SynergyValues.Web.GetMemberShortProfileService.SERVICE_URL,new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Methods.closeProgressDialog();
                Log.d("droid","get reqgetTopHierarchy ---------------"+ response);
                try {

                    JSONObject jsonobj=new JSONObject(response);
                    JSONArray jarray=jsonobj.getJSONArray("message");

                    if(jarray.getJSONObject(0).getString("name") != null) {
                        txtCellLeaderId.setText(jarray.getJSONObject(0).getString("name"));
                    }

                    if(jarray.getJSONObject(0).getString("member_name") != null) {
                        txtCellLeaderName.setText(jarray.getJSONObject(0).getString("member_name"));
                    }

                    if(jarray.getJSONObject(0).getString("email_id") != null) {
                        txtCellLeaderEmailId.setText(jarray.getJSONObject(0).getString("email_id"));
                    }

                    if(jarray.getJSONObject(0).getString("date_of_birth") != null) {
                        txtCellLeaderDateOfBirth.setText(jarray.getJSONObject(0).getString("date_of_birth"));
                    }

                    if(jarray.getJSONObject(0).getString("phone_1") != null) {
                        txtCellLeaderPhone.setText(jarray.getJSONObject(0).getString("phone_1"));
                    }

                    if(jarray.getJSONObject(0).getString("address") != null) {
                        txtCellLeaderAddress.setText(jarray.getJSONObject(0).getString("address"));
                    }

                    Imageurl= jarray.getJSONObject(0).getString("image");
                    if(Imageurl != null) {
                        Picasso.with(ShortBio.this)
                                .load(SynergyValues.ImageUrl.imageUrl + jarray.getJSONObject(0).getString("image"))
                                .placeholder(R.drawable.user) // optional
                                .error(R.drawable.user)         // optional
                                .into(imgProfilePic);
                    }
                    Log.e("Image Url", SynergyValues.ImageUrl.imageUrl + Imageurl);
                    Log.d("NonStop", "Image URL: " + SynergyValues.ImageUrl.imageUrl + Imageurl);

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

                MemberShortProfile model=new MemberShortProfile();
                model.setUsername(mPreferenceHelper.getString(SynergyValues.Commons.USER_EMAILID));
                model.setUserpass(mPreferenceHelper.getString(SynergyValues.Commons.USER_PASSWORD));
                model.setName(cellcode);
                model.setRole(role);
//                model.setRole("Cell Leader");

                String dataString=gson.toJson(model, MemberShortProfile.class);

                Log.d("droid", dataString);
                params.put(SynergyValues.Web.GetHigherHierarchyService.DATA, dataString);
                return params;
            }
        };

        App.getInstance().addToRequestQueue(reqgetCellDetails, "reqgetCellDetails");
        reqgetCellDetails.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));

    }


}
