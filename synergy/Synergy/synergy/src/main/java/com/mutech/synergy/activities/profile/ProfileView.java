package com.mutech.synergy.activities.profile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.mutech.messagebraudcast.MessageBroadcastActivity;
import com.mutech.synergy.App;
import com.mutech.synergy.R;
import com.mutech.synergy.SynergyValues;
import com.mutech.synergy.activities.Feedback;
import com.mutech.synergy.activities.HomeActivity;
import com.mutech.synergy.activities.LogoutActivity;
import com.mutech.synergy.activities.MessageLogs;
import com.mutech.synergy.activities.cellMasters.MasterSelectorScreenActivity;
import com.mutech.synergy.activities.cellMasters.PartnerShipRecord;
import com.mutech.synergy.activities.cellMasters.SearchFunctionActivity;
import com.mutech.synergy.activities.event.MyEventListActivity;
import com.mutech.synergy.activities.meeting.MyMeetingListActivity;
import com.mutech.synergy.activities.task.ToDoTaskActivity;
import com.mutech.synergy.adapters.CustomDrawerAdapter;
import com.mutech.synergy.models.DrawerItem;
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

    TextView txtMemberId, txtMemberName, txtMemberDesignation, txtMemberDOB, txtMemberPhone, txtMemberEmail,
    txtMemberHomeAddress,txtshortbio;
    Button buttonEditProf;
    ImageView imgProfPic;
    private Gson gson;
    private PreferenceHelper mPreferenceHelper;
    private String str, Imageurl;
    ArrayList<MemberProfileModel.ProfileSubModel> mProfSubModel;
    private SimpleDateFormat dateFormatter,dateFormatterService,dateFormatter01;
    private ArrayList<DrawerItem> mDrawerList;
    private CustomDrawerAdapter mCustomDrawerAdapter;
    private ListView mLvDrawer;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private String Role,Name,Status,Designation,Image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.actiontop);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2E9AFE")));
        getSupportActionBar().setTitle("My Profile");
//        getSupportActionBar().setDisplayShowCustomEnabled(true);

        mDrawerList = new ArrayList<DrawerItem>();

        mPreferenceHelper=new PreferenceHelper(this);

        mLvDrawer = (ListView) findViewById(R.id.left_drawer);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mCustomDrawerAdapter = new CustomDrawerAdapter(this,R.layout.custom_dashboard_drawer_item, mDrawerList);
        mLvDrawer.setAdapter(mCustomDrawerAdapter);
        mLvDrawer.setOnItemClickListener(new DrawerItemClickListener());

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                R.drawable.actiontop,
                R.string.app_name,
                R.string.app_name
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                //getSupportActionBar().setTitle(mTitle);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //getSupportActionBar().setTitle(mDrawerTitle);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        //  txtMemberId = (TextView) findViewById(R.id.txtMemberId);
        txtMemberName = (TextView) findViewById(R.id.txtMemberName);
        txtMemberDesignation = (TextView) findViewById(R.id.txtMemberDesignation);
        txtMemberDOB = (TextView) findViewById(R.id.txtMemberDOB);
        txtMemberPhone = (TextView) findViewById(R.id.txtMemberPhone);
        txtMemberEmail = (TextView) findViewById(R.id.txtMemberEmailId);
        txtshortbio = (TextView) findViewById(R.id.txtshortbio);

        buttonEditProf = (Button) findViewById(R.id.btnEditProf);
        imgProfPic = (ImageView) findViewById(R.id.imgProfilePic);

        buttonEditProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editProfInt = new Intent(ProfileView.this, MyProfileActivity.class);
                startActivity(editProfInt);
            }
        });

        mPreferenceHelper=new PreferenceHelper(this);
        str=mPreferenceHelper.getString(SynergyValues.Commons.USER_ROLE);
        gson=new Gson();
        Calendar newCalendar = Calendar.getInstance();
        dateFormatter = new SimpleDateFormat("MMMM dd",Locale.US);
        dateFormatterService=new SimpleDateFormat("yyyy-MM-dd",Locale.US);

        addDrawerListData();
       /* if(mPreferenceHelper.getString(SynergyValues.Commons.USER_ROLE) != null)
            txtMemberRole.setText("Role: " + mPreferenceHelper.getString(SynergyValues.Commons.USER_ROLE));*/
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
                     //   txtMemberId.setText("Member Id: " + mProfSubModel.get(i).getName());
                        if(mProfSubModel.get(i).getMember_name() != null) {
                        txtMemberName.setText("Name: " + mProfSubModel.get(i).getMember_name());}

                        if(mProfSubModel.get(i).getShort_bio() != null) {
                        txtshortbio.setText("Short Bio: " + mProfSubModel.get(i).getShort_bio());}

                        try {
                            Date dob = new Date();
                            if(null !=mProfSubModel.get(i).getDate_of_birth()){
                                dob=dateFormatterService.parse(mProfSubModel.get(i).getDate_of_birth());
                                txtMemberDOB.setText("Birthday: " + dateFormatter.format(dob));
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
                        if(mProfSubModel.get(i).getEmail_id() != null) {
                        txtMemberEmail.setText("Email Id: " + mProfSubModel.get(i).getEmail_id());}

                        if(mProfSubModel.get(i).getMember_designation() != null) {
                        txtMemberDesignation.setText("Designation: " + mProfSubModel.get(i).getMember_designation());}

                        if(null !=mProfSubModel.get(i).getLast_name())
                            txtMemberName.setText("Name: " + mProfSubModel.get(i).getMember_name() + " "  + mProfSubModel.get(i).getLast_name());

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

    private void addDrawerListData() {

        if(mPreferenceHelper.getString(SynergyValues.Commons.USER_ROLE) != null){
            Role=mPreferenceHelper.getString(SynergyValues.Commons.USER_ROLE);}

        if(mPreferenceHelper.getString(SynergyValues.Commons.USER_NAME) != null){
            Name=mPreferenceHelper.getString(SynergyValues.Commons.USER_NAME);}

        if(mPreferenceHelper.getString(SynergyValues.Commons.USER_STATUS) != null){
            Status=mPreferenceHelper.getString(SynergyValues.Commons.USER_STATUS);}

        if(mPreferenceHelper.getString(SynergyValues.Commons.USER_DESIGNATION) != null){
            Designation=mPreferenceHelper.getString(SynergyValues.Commons.USER_DESIGNATION);}

        if(mPreferenceHelper.getString(SynergyValues.Commons.USER_IMAGE) != null){
            Image=mPreferenceHelper.getString(SynergyValues.Commons.USER_IMAGE);}

        DrawerItem item00 = new DrawerItem();
        item00.setItemName(Name + "\n" + "Role: " + Role + "\n" + "Designation: " +Designation + "\n" + Status);
        item00.setImgResID(R.drawable.user);

        DrawerItem item01 = new DrawerItem();
        item01.setItemName("Dashboard");
        item01.setImgResID(R.drawable.dashboard);

        DrawerItem item05 = new DrawerItem();
        item05.setItemName("My Profile");
        item05.setImgResID(R.drawable.myprofile);

//		DrawerItem item03 = new DrawerItem();
//		item03.setItemName("Ministry \n Material");
//		item03.setImgResID(R.drawable.ministry_materials);

        DrawerItem item03 = new DrawerItem();
        item03.setItemName("Partnership \n Records");
        item03.setImgResID(R.drawable.partnership_record);

        DrawerItem item04 = new DrawerItem();
        item04.setItemName("Database");
        item04.setImgResID(R.drawable.database);


        DrawerItem item06 = new DrawerItem();
        item06.setItemName("Search");
        item06.setImgResID(R.drawable.search);

        DrawerItem item07 = new DrawerItem();
        item07.setItemName("Attendance");
        item07.setImgResID(R.drawable.my_meetings);

        DrawerItem item08 = new DrawerItem();
        item08.setItemName("Calendar");
        item08.setImgResID(R.drawable.carlender);

        DrawerItem item9 = new DrawerItem();
        item9.setItemName("To Do");
        item9.setImgResID(R.drawable.todo);

        DrawerItem item10 = new DrawerItem();
        item10.setItemName("Broadcast Message");
        item10.setImgResID(R.drawable.msg);

        DrawerItem item11=new DrawerItem();
        item11.setItemName("Feedback");
        item11.setImgResID(R.drawable.msg);

        DrawerItem item12=new DrawerItem();
        item12.setItemName("Message Logs");
        item12.setImgResID(R.drawable.msg);

        DrawerItem item13=new DrawerItem();
        item13.setItemName("Logout");
        item13.setImgResID(R.drawable.signout);

        mDrawerList.add(item00);
        mDrawerList.add(item01);
        mDrawerList.add(item05);
        mDrawerList.add(item04);
//		mDrawerList.add(item02);
//		mDrawerList.add(item03);
        mDrawerList.add(item03);

        //	mDrawerList.add(item06);
        mDrawerList.add(item07);
        mDrawerList.add(item08);
        mDrawerList.add(item9);
        mDrawerList.add(item10);
        mDrawerList.add(item06);
        mDrawerList.add(item11);
        mDrawerList.add(item12);
        mDrawerList.add(item13);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void selectItem(int position) {
        switch (position) {
            case 0:
                //			Intent intForm1=new Intent(this,MyProfileActivity.class);
                //			startActivity(intForm1);
                break;

            case 1:
                Intent int1=new Intent(this,HomeActivity.class);
                int1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(int1);
                finish();
                break;

            case 2:
              /*  Intent intForm1=new Intent(this,ProfileView.class);
                intForm1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intForm1);*/
                break;
//            case 1:
//				Intent intForm1=new Intent(this,MyProfileActivity.class);
//				startActivity(intForm1);
//                break;

            case 3:
                Log.d("NonStop", "Going to Database");
                Intent intForm=new Intent(this,MasterSelectorScreenActivity.class);
                intForm.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intForm);
                finish();
                break;

            case 4:
                Log.d("NonStop", "Going to Partnership Record");
                Intent partner=new Intent(this,PartnerShipRecord.class);
                partner.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(partner);
                finish();
                break;

            case 5:
                //	Intent intMyMeetings=new Intent(this,MeetingListActivity.class);
                //startActivity(intMeeting);
                Log.d("NonStop", "Going to Attendance");
                Intent intMyMeetings=new Intent(this,MyMeetingListActivity.class);
                intMyMeetings.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intMyMeetings);
                finish();
                break;
            case 6:
                Log.d("NonStop", "Going to Calendar");
                Intent intEvents=new Intent(this,MyEventListActivity.class);
                intEvents.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intEvents);
                finish();

                break;
            case 7:
                Log.d("NonStop", "Going to ToDo");
                Intent intentTODO = new Intent(this, ToDoTaskActivity.class);
                intentTODO.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intentTODO);
                finish();
                break;

            case 8:
                Intent intentMsg = new Intent(this, MessageBroadcastActivity.class);
                intentMsg.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intentMsg);
                finish();
                break;

            case 9:
                Log.d("NonStop", "Going to Search");
                Intent intSearchMembers=new Intent(this,SearchFunctionActivity.class);
                intSearchMembers.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intSearchMembers);
                finish();

                break;

            case 10:
//			mPreferenceHelper.addBoolean(Commons.ISUSER_LOGGEDIN, false);
//			mPreferenceHelper.addString(Commons.USER_EMAILID, null);
//			mPreferenceHelper.addString(Commons.USER_PASSWORD, null);

                Intent intfeedback=new Intent(this,Feedback.class);
                intfeedback.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intfeedback);
                finish();
                break;

            case 11:
                Intent intmsglogs=new Intent(this,MessageLogs.class);
                intmsglogs.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intmsglogs);
                break;

            case 12://logout
//			mPreferenceHelper.addBoolean(Commons.ISUSER_LOGGEDIN, false);
//			mPreferenceHelper.addString(Commons.USER_EMAILID, null);
//			mPreferenceHelper.addString(Commons.USER_PASSWORD, null);

                Intent intLogout=new Intent(getApplicationContext(),LogoutActivity.class);
                intLogout.putExtra("classname", "MyProfileActivity");
                startActivity(intLogout);
                finish();
                break;


            default:
                break;
        }

        mDrawerLayout.closeDrawer(mLvDrawer);
//		getSupportActionBar().invalidateOptionsMenu();
    }

    private class DrawerItemClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            selectItem(position);

        }
    }

    public void onBackPressed() {

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Closing Activity")
                        .setMessage("Are you sure you want to exit?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                moveTaskToBack(true);
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
                TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                textView.setTextSize(18);
            }
    }
}
