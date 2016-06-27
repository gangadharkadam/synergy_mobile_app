package com.mutech.synergy.activities.profile;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import com.mutech.synergy.App;
import com.mutech.synergy.R;
import com.mutech.synergy.SynergyValues;
import com.mutech.synergy.models.MeetingListRequestModel;
import com.mutech.synergy.models.MemberProfileModel;
import com.mutech.synergy.utils.Methods;
import com.mutech.synergy.utils.PreferenceHelper;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ProfileView extends ActionBarActivity {

    TextView txtMemberId, txtMemberName, txtMemberRole, txtMemberDOB, txtMemberPhone, txtMemberEmail,
    txtMemberHomeAddress;
    Button buttonEditProf;
    ImageView imgProfPic;
    private Gson gson;
    private PreferenceHelper mPreferenceHelper;
    private String str, Imageurl;
    ArrayList<MemberProfileModel.ProfileSubModel> mProfSubModel;
    private SimpleDateFormat dateFormatter,dateFormatterService,dateFormatter01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setCustomView(R.layout.custom_actionbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.actiontop);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2E9AFE")));
        getSupportActionBar().setTitle("My Profile");
//        getSupportActionBar().setDisplayShowCustomEnabled(true);


        txtMemberId = (TextView) findViewById(R.id.txtMemberId);
        txtMemberName = (TextView) findViewById(R.id.txtMemberName);
        txtMemberRole = (TextView) findViewById(R.id.txtMemberRole);
        txtMemberDOB = (TextView) findViewById(R.id.txtMemberDOB);
        txtMemberPhone = (TextView) findViewById(R.id.txtMemberPhone);
        txtMemberEmail = (TextView) findViewById(R.id.txtMemberEmailId);
        txtMemberHomeAddress = (TextView) findViewById(R.id.txtMemberHomeAddress);

        buttonEditProf = (Button) findViewById(R.id.btnEditProf);
        imgProfPic = (ImageView) findViewById(R.id.imgProfilePic);

        buttonEditProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editProfInt = new Intent(ProfileView.this, MyProfileActivity.class);
                startActivity(editProfInt);
                finish();
            }
        });

        mPreferenceHelper=new PreferenceHelper(this);
        str=mPreferenceHelper.getString(SynergyValues.Commons.USER_ROLE);
        gson=new Gson();
        Calendar newCalendar = Calendar.getInstance();
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        dateFormatterService=new SimpleDateFormat("yyyy-MM-dd",Locale.US);

        if(mPreferenceHelper.getString(SynergyValues.Commons.USER_ROLE) != null)
            txtMemberRole.setText("Role: " + mPreferenceHelper.getString(SynergyValues.Commons.USER_ROLE));
        getProfileInfo();
    }

    private void getProfileInfo() {
        StringRequest reqGetProfile=new StringRequest(Request.Method.POST, SynergyValues.Web.GetMemberProfileService.SERVICE_URL,new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Methods.closeProgressDialog();
                Log.e("responce droid", "get reqGetProfile ---------------" + response);

                MemberProfileModel mProfModel=gson.fromJson(response, MemberProfileModel.class);
                //Object meetingmsg=mMeetingModel.getMessage();

                if(null !=mProfModel.getMessage() && mProfModel.getMessage().size() >0)
                {

                    mProfSubModel=mProfModel.getMessage();

                    Log.d("NonStop", "mProfSumModel Size: " + mProfSubModel.size());
                    for(int i=0;i<mProfSubModel.size();i++){
                        txtMemberId.setText("Member Id: " + mProfSubModel.get(i).getName());

                        txtMemberName.setText("Name: " + mProfSubModel.get(i).getMember_name());
                        try {
                            Date dob = new Date();
                            if(null !=mProfSubModel.get(i).getDate_of_birth()){
                                dob=dateFormatterService.parse(mProfSubModel.get(i).getDate_of_birth());
                                txtMemberDOB.setText("Date of Birth: " + dateFormatter.format(dob));
                            }

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                        txtMemberPhone.setText("Phone No.: " + mProfSubModel.get(i).getPhone_1());

                        if(i<1) {
                            Imageurl= mProfSubModel.get(i).getImage();
                        }

                        if(Imageurl != null) {
                            Picasso.with(ProfileView.this).load(SynergyValues.ImageUrl.imageUrl+Imageurl).into(imgProfPic);
                        }

                        txtMemberEmail.setText("Email Id: " + mProfSubModel.get(i).getEmail_id());
                        txtMemberHomeAddress.setText("Address: " + mProfSubModel.get(i).getAddress());

                        if(null !=mProfSubModel.get(i).getLast_name())
                            txtMemberName.setText("Name: " + mProfSubModel.get(i).getName() + " "  + mProfSubModel.get(i).getLast_name());

                    }
                }
            }
        },new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Methods.closeProgressDialog();
                Log.d("droid","get reqsaveMeeting error---------------"+ error.getCause());

                if(error.networkResponse.statusCode==403){
                    Methods.longToast("No access to update Profile", ProfileView.this);
                }
                else
                    Methods.longToast("Some Error Occured,please try again later", ProfileView.this);
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
