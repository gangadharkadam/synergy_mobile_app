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

        btnedit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent Int = new Intent(ShortBio.this, MyProfileActivity.class);
                Int.putExtra("fromshortbio","fromshortbio");
                startActivity(Int);
                finish();
            }
        });


        if(NetworkHelper.isOnline(this)){
            Methods.showProgressDialog(this);
            getProfileInfo();
        }
        else
            Methods.longToast("Please connect to Internet", this);

    }

    private void getProfileInfo() {
        StringRequest reqGetProfile=new StringRequest(Request.Method.POST, SynergyValues.Web.GetMemberProfileService.SERVICE_URL,new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Methods.closeProgressDialog();
                Log.e("responce droid", "get reqGetProfile ---------------" + response);

                MemberProfileModel mProfModel = gson.fromJson(response, MemberProfileModel.class);
                //Object meetingmsg=mMeetingModel.getMessage();

                if (null != mProfModel.getMessage() && mProfModel.getMessage().size() > 0) {

                    mProfSubModel = mProfModel.getMessage();

                    Log.d("NonStop", "mProfSumModel Size: " + mProfSubModel.size());
                    for (int i = 0; i < mProfSubModel.size(); i++) {
                        txtMembershipNo.setText(mProfSubModel.get(i).getName());

                        txtMemberName.setText(mProfSubModel.get(i).getMember_name() + " " +mProfSubModel.get(i).getLast_name());
                        //if(null !=mProfSubModel.get(i).getDate_of_birth()){
                        try {
                            Date dob = new Date();

                            if (null != mProfSubModel.get(i).getDate_of_birth()) {
                                dob = dateFormatterService.parse(mProfSubModel.get(i).getDate_of_birth());
                                txtMemberDateOfBirth.setText(dateFormatter.format(dob));
                            }

                            if (null != mProfSubModel.get(i).getDate_of_join()) {
                                dob = dateFormatterService.parse(mProfSubModel.get(i).getDate_of_join());
//								txtDateofJoining.setText(dateFormatter.format(dob));
                            }

                            if (null != mProfSubModel.get(i).getBaptism_when()) {
                                dob = dateFormatterService.parse(mProfSubModel.get(i).getBaptism_when());
//								txtBaptisedWhen.setText(dateFormatter.format(dob));
                            }

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        //}

//						if(null !=mProfSubModel.get(i).getBaptism_where())
//							txtBaptisedWhere.setText(mProfSubModel.get(i).getBaptism_where());

                        //getChurch

                        txtMemberPhone.setText(mProfSubModel.get(i).getPhone_1());
                        txtEmailID.setText(mProfSubModel.get(i).getEmail_id());
                        txtMemberHomeAddress.setText(mProfSubModel.get(i).getAddress());
                        txtDesignation.setText(mProfSubModel.get(i).getMember_designation());
                        txtstatus.setText(mProfSubModel.get(i).getEmployment_status());
                        txtRole.setText(mProfSubModel.get(i).getRole());


                        if (i < 1) {
                            Imageurl = mProfSubModel.get(i).getImage();
                        }

                        if (Imageurl != null) {
                            Picasso.with(ShortBio.this).load(SynergyValues.ImageUrl.imageUrl + Imageurl).into(imgProfilePic);
                        }
                        Log.e("Image Url", SynergyValues.ImageUrl.imageUrl + Imageurl);
                        Log.d("NonStop", "Image URL: " + SynergyValues.ImageUrl.imageUrl + Imageurl);

                    }

                    //	}

                }
            }
        },new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Methods.closeProgressDialog();
                Log.d("droid","get reqsaveMeeting error---------------"+ error.getCause());

                if(error.networkResponse.statusCode==403){
                    Methods.longToast("No access to update Profile", ShortBio.this);
                }
                else
                    Methods.longToast("Some Error Occured,please try again later", ShortBio.this);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                MeetingListRequestModel model=new MeetingListRequestModel();
                model.setUsername(mPreferenceHelper.getString(SynergyValues.Commons.USER_EMAILID));
                model.setUserpass(mPreferenceHelper.getString(SynergyValues.Commons.USER_PASSWORD));

                String dataString=gson.toJson(model, MeetingListRequestModel.class);

                Log.e("Request droid", dataString);
                params.put(SynergyValues.Web.GetMemberProfileService.DATA, dataString);
                return params;
            }
        };

        App.getInstance().addToRequestQueue(reqGetProfile, "reqGetProfile");
        reqGetProfile.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
    }

}
