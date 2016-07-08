package com.mutech.synergy.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.LargeValueFormatter;
import com.github.mikephil.charting.utils.PercentFormatter;
//import com.google.android.gms.internal.iw;
import com.github.mikephil.charting.utils.ValueFormatter;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mutech.messagebraudcast.MessageBroadcastActivity;
import com.mutech.partnershiprecord.MyPartnershipActivity;
import com.mutech.synergy.App;
import com.mutech.synergy.GraphTestActivity;
import com.mutech.synergy.R;
import com.mutech.synergy.SynergyValues;
import com.mutech.synergy.SynergyValues.Commons;
import com.mutech.synergy.SynergyValues.Web.DashboardDataService;
import com.mutech.synergy.activities.cellMasters.AllMemberListActivity;
import com.mutech.synergy.activities.cellMasters.MasterSelectorScreenActivity;
import com.mutech.synergy.activities.cellMasters.PartnerShipRecord;
import com.mutech.synergy.activities.cellMasters.SearchFunctionActivity;
import com.mutech.synergy.activities.dashboard.FirstTimeMonthActivity;
import com.mutech.synergy.activities.dashboard.FirstTimersYearActivity;
import com.mutech.synergy.activities.dashboard.FirstTimesWeekActivity;
import com.mutech.synergy.activities.dashboard.InviteeandContactsMonthActivity;
import com.mutech.synergy.activities.dashboard.InviteesandContactsYearActivity;
import com.mutech.synergy.activities.dashboard.InviteesandContactsweekActivity;
import com.mutech.synergy.activities.dashboard.MemberStrengthMutliBarChart;
import com.mutech.synergy.activities.dashboard.NewConvertsMonthActivity;
import com.mutech.synergy.activities.dashboard.NewConvertsWeekActivity;
import com.mutech.synergy.activities.dashboard.NewConvertsYearActivity;
import com.mutech.synergy.activities.dashboard.PledgeActivity;
import com.mutech.synergy.activities.dashboard.TotalMemberListActivity;
import com.mutech.synergy.activities.event.EventListActivity;
import com.mutech.synergy.activities.event.MyEventListActivity;
import com.mutech.synergy.activities.meeting.MeetingListActivity;
import com.mutech.synergy.activities.meeting.MyMeetingListActivity;
import com.mutech.synergy.activities.profile.MyProfileActivity;
import com.mutech.synergy.activities.profile.ProfileView;
import com.mutech.synergy.activities.task.ToDoTaskActivity;
import com.mutech.synergy.adapters.CustomDrawerAdapter;
import com.mutech.synergy.models.DashboardDetailsModel;
import com.mutech.synergy.models.DashboardDetailsModel.DashPartnershipModel;
import com.mutech.synergy.models.DashboardDetailsModel.MemStrengthModel;
import com.mutech.synergy.models.DrawerItem;
import com.mutech.synergy.models.MeetingListRequestModel;
import com.mutech.synergy.models.ResponseMessageModel;
import com.mutech.synergy.models.ResponseMessageModel2;
import com.mutech.synergy.utils.Methods;
import com.mutech.synergy.utils.NetworkHelper;
import com.mutech.synergy.utils.PreferenceHelper;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class HomeActivity extends AppCompatActivity {

    private LineChart mChart2,mChart3;
    private BarChart mChart;
    private ArrayList<String> monthnameslist,monthnameslist2,newConvertsList,totalMemberList;

    ArrayList<String> partnermonthnameslist;
    ArrayList<Integer> pledgeValList,givingValList;

    private ArrayList<DrawerItem> mDrawerList;
    private CustomDrawerAdapter mCustomDrawerAdapter;
    private ListView mLvDrawer;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private PreferenceHelper mPreferenceHelper;
    private Gson gson;
    private String Role,Name,Status,Designation,Image;

    private TextView partnershipDummy,memStrengthDummy;
    private LinearLayout llMemberStrengthLayout,newconvertsweeklayout,newconvertsmonthlayout,
            newconvertsyearlayout,firsttimerweeklayout,firsttimermonthlayout,firsttimeryearlayout,inviteeweeklayout,inviteemonthlayout,inviteeyearlayout,bargraph,partnership_records_layout;
    String intentyear,intentweek,intentmonth;

    TextView newWeektxtlable,newMonthtxtlable,newyeartxtlable,firstmonthtxtlable,firstweektxtlable,firstyeartxtlable,
            inviteeWeektxtLable,inviteemonthtxtLable,inviteeYeartxtLable;
    TextView newWeektxt,newMonthtxt,newyeartxt,firstweektxt,firstmonthtxt,
            firstyeartxt,inviteeWeektxt,inviteemonthtxt,inviteeYeartxt;

    private PieChart chartPieVisitors;
    //private TextView lWeekc,lYearc,lmonthc,lWeeki,lYeari,lmonthi,lWeekf,lYearf,lmonthf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.actiontop);
//		getSupportActionBar().setIcon(R.drawable.actiontop);
//		getSupportActionBar().setLogo(R.drawable.actiontop);
//		getSupportActionBar().setDisplayUseLogoEnabled(true);
        setContentView(R.layout.activity_home);
        initialize();
        addDrawerListData();

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @SuppressLint("NewApi")
    private void initialize() {
        mDrawerList = new ArrayList<DrawerItem>();

        mPreferenceHelper=new PreferenceHelper(this);

        mLvDrawer = (ListView) findViewById(R.id.left_drawer);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mCustomDrawerAdapter = new CustomDrawerAdapter(this,R.layout.custom_dashboard_drawer_item, mDrawerList);
        mLvDrawer.setAdapter(mCustomDrawerAdapter);
        mLvDrawer.setOnItemClickListener(new DrawerItemClickListener());

        partnershipDummy=(TextView) findViewById(R.id.partnershipDummy);
        memStrengthDummy=(TextView) findViewById(R.id.memStrengthDummy);
        llMemberStrengthLayout=(LinearLayout) findViewById(R.id.llMemberStrengthLayout);

        monthnameslist2=new ArrayList<String>();
        monthnameslist=new ArrayList<String>();
        newConvertsList=new ArrayList<String>();
        totalMemberList=new ArrayList<String>();

        partnermonthnameslist=new ArrayList<String>();
        pledgeValList=new ArrayList<Integer>();
        givingValList=new ArrayList<Integer>();

        //Liner chart start

        mChart2 = (LineChart) findViewById(R.id.chart2);
        mChart2.setDrawGridBackground(false);


        // no description text
        mChart2.setDescription("");
        mChart2.setNoDataTextDescription("");
        mChart2.setNoDataText("");

        mChart2.setTouchEnabled(false);

        mChart2.setDragEnabled(false);
        mChart2.setScaleEnabled(false);

        mChart2.setPinchZoom(false);

        mChart2.setHighlightEnabled(false);

        YAxis leftAxis2 = mChart2.getAxisLeft();
        leftAxis2.removeAllLimitLines(); // reset all limit lines to avoid
        // overlapping lines
        // leftAxis.setAxisMaxValue(220f);
        // leftAxis.setAxisMinValue(-50f);
        leftAxis2.setStartAtZero(true);
        // leftAxis.setYOffset(20f);
        leftAxis2.enableGridDashedLine(10f, 10f, 0f);

        // limit lines are drawn behind data (and not on top)
        leftAxis2.setDrawLimitLinesBehindData(true);

        mChart2.getAxisRight().setEnabled(false);

        mChart2.animateX(2500, Easing.EasingOption.EaseInOutQuart);

        Legend l2 = mChart2.getLegend();
        // modify the legend ...
        l2.setPosition(LegendPosition.RIGHT_OF_CHART);
        l2.setForm(LegendForm.SQUARE);

        //Liner chart end

		/*//Linear start chart 3

		mChart3 = (LineChart) findViewById(R.id.chart3);
		mChart3.setDrawGridBackground(false);


		// no description text
		mChart3.setDescription("");
		mChart3.setNoDataTextDescription("");
		mChart3.setNoDataText("");

		mChart3.setTouchEnabled(false);

		mChart3.setDragEnabled(false);
		mChart3.setScaleEnabled(false);

		mChart3.setPinchZoom(false);


		YAxis leftAxis = mChart3.getAxisLeft();
		leftAxis.removeAllLimitLines(); // reset all limit lines to avoid
		// overlapping lines
		// leftAxis.setAxisMaxValue(220f);
		// leftAxis.setAxisMinValue(-50f);
		leftAxis.setStartAtZero(true);
		// leftAxis.setYOffset(20f);
		leftAxis.enableGridDashedLine(10f, 10f, 0f);

		// limit lines are drawn behind data (and not on top)
		leftAxis.setDrawLimitLinesBehindData(true);

		mChart3.getAxisRight().setEnabled(false);

		mChart3.animateX(2500, Easing.EasingOption.EaseInOutQuart);

		Legend l = mChart3.getLegend();
		// modify the legend ...
		l.setPosition(LegendPosition.RIGHT_OF_CHART);
		l.setForm(LegendForm.SQUARE);
		*/

        mChart=(BarChart) findViewById(R.id.chart1);

        mChart.getAxisRight().setEnabled(false);
        mChart.setDescription("");
        mChart.setPinchZoom(false);
        mChart.setDrawBarShadow(false);
        mChart.setDrawGridBackground(false);
        mChart.setNoDataText("");
        mChart.setNoDataTextDescription("");

        Legend l1 = mChart.getLegend();
        l1.setPosition(LegendPosition.RIGHT_OF_CHART_INSIDE);

        XAxis x = mChart.getXAxis();

        YAxis leftAxis1 = mChart.getAxisLeft();
        leftAxis1.setLabelCount(2);
     /*   leftAxis1.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return Integer.toString((int) value);
            }
        })*/;

      leftAxis1.setValueFormatter(new MyFormatter());
        leftAxis1.setDrawGridLines(false);
        leftAxis1.setSpaceTop(25f);

        mChart.getAxisRight().setEnabled(false);

        //	llMemberStrengthLayout.setOnClickListener(this);

        gson=new Gson();

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


        TextView tvTitle=(TextView)getSupportActionBar().getCustomView().findViewById(R.id.title_text);
        tvTitle.setText("Dashboard       ");

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2E9AFE")));

        getSupportActionBar().setDisplayShowCustomEnabled(true);


        bargraph=(LinearLayout) findViewById(R.id.bargraph);

        partnership_records_layout=(LinearLayout) findViewById(R.id.partnership_records_layout);

        newconvertsweeklayout=(LinearLayout) findViewById(R.id.newconvertsweeklayout);
        newconvertsmonthlayout=(LinearLayout) findViewById(R.id.newconvertsmonthlayout);
        newconvertsyearlayout=(LinearLayout) findViewById(R.id.newconvertsyearlayout);

        firsttimerweeklayout=(LinearLayout) findViewById(R.id.firsttimerweeklayout);
        firsttimermonthlayout=(LinearLayout) findViewById(R.id.firsttimermonthlayout);
        firsttimeryearlayout=(LinearLayout) findViewById(R.id.firsttimeryearlayout);

        inviteeweeklayout=(LinearLayout) findViewById(R.id.inviteeweeklayout);
        inviteemonthlayout=(LinearLayout) findViewById(R.id.inviteemonthlayout);
        inviteeyearlayout=(LinearLayout) findViewById(R.id.inviteeyearlayout);

        newWeektxtlable=(TextView) findViewById(R.id.newWeektxtlable);
        newMonthtxtlable=(TextView) findViewById(R.id.newMonthtxtlable);
        newyeartxtlable=(TextView) findViewById(R.id.newyeartxtlable);

        newWeektxt=(TextView) findViewById(R.id.newWeektxt);
        newMonthtxt=(TextView) findViewById(R.id.newMonthtxt);
        newyeartxt=(TextView) findViewById(R.id.newyeartxt);

        firstweektxtlable=(TextView) findViewById(R.id.firstweektxtlable);
        firstmonthtxtlable=(TextView) findViewById(R.id.firstmonthtxtlable);
        firstyeartxtlable=(TextView) findViewById(R.id.firstyeartxtlable);


        firstweektxt=(TextView) findViewById(R.id.firstweektxt);
        firstmonthtxt=(TextView) findViewById(R.id.firstmonthtxt);
        firstyeartxt=(TextView) findViewById(R.id.firstyeartxt);

        inviteeWeektxtLable=(TextView) findViewById(R.id.inviteeWeektxtLable);
        inviteemonthtxtLable=(TextView) findViewById(R.id.inviteemonthtxtLable);
        inviteeYeartxtLable=(TextView) findViewById(R.id.inviteeYeartxtLable);

        inviteeWeektxt=(TextView) findViewById(R.id.inviteeWeektxt);
        inviteemonthtxt=(TextView) findViewById(R.id.inviteemonthtxt);
        inviteeYeartxt=(TextView) findViewById(R.id.inviteeYeartxt);


        bargraph.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent it =new Intent(HomeActivity.this, AllMemberListActivity.class);
                startActivity(it);

            }
        });

        partnership_records_layout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent it =new Intent(HomeActivity.this, MyPartnershipActivity.class);
                startActivity(it);

            }
        });

        newconvertsweeklayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent it =new Intent(HomeActivity.this, NewConvertsWeekActivity.class);
                it.putExtra("week", "Week");
                startActivity(it);

            }
        });

        newconvertsmonthlayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent it=new Intent(HomeActivity.this, NewConvertsMonthActivity.class);
                it.putExtra("month","Month");
                startActivity(it);

            }
        });

        newconvertsyearlayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent it=new Intent(HomeActivity.this, NewConvertsYearActivity.class);
                it.putExtra("year","Year");
                startActivity(it);

            }
        });

        firsttimerweeklayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent it =new Intent(HomeActivity.this, FirstTimesWeekActivity.class);
                it.putExtra("week","week");
                startActivity(it);

            }
        });

        firsttimermonthlayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub  intentyear
                Intent it =new Intent(HomeActivity.this, FirstTimeMonthActivity.class);
                it.putExtra("month", "Month");
                startActivity(it);

            }
        });

        firsttimeryearlayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Intent it=new Intent(HomeActivity.this, FirstTimersYearActivity.class);
                it.putExtra("year", "year");
                startActivity(it);
            }
        });

        inviteeweeklayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Intent it=new Intent(HomeActivity.this, InviteesandContactsweekActivity.class);
                it.putExtra("week", "week");
                startActivity(it);

            }
        });


        inviteemonthlayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent it =new Intent(HomeActivity.this, InviteeandContactsMonthActivity.class);
                it.putExtra("month", "month");
                startActivity(it);

            }
        });

        inviteeyearlayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent it =new Intent(HomeActivity.this, InviteesandContactsYearActivity.class);
                it.putExtra("year", "year");
                startActivity(it);

            }
        });


        setPieChartVisitors();

        if(NetworkHelper.isOnline(this)){
            Methods.showProgressDialog(this);
            //getDashboardDataService3();
            getDashboardDataService();

        }
        else
            Methods.longToast("Please connect to Internet", this);


    }

    private void setPieChartVisitors() {

    }




    private void getDashboardDataService() {
        StringRequest reqDashboard=new StringRequest(Method.POST,DashboardDataService.SERVICE_URL,new Listener<String>() {

            @Override
            public void onResponse(String response) {
                Methods.closeProgressDialog();
                Log.e("droid","get all reqDashboard ---------------"+ response);

                if(response.contains("status"))
                {
                    ResponseMessageModel2 respModel=gson.fromJson(response, ResponseMessageModel2.class);
                    if(respModel.getMessage().getStatus()=="401"){
                        Methods.longToast("User name or Password is incorrect", HomeActivity.this);
                    }else{
                        Methods.longToast(respModel.getMessage().getMessage(), HomeActivity.this);
                    }
                }else{
                    DashboardDetailsModel mDetailsModel=gson.fromJson(response, DashboardDetailsModel.class);


                    if(null !=mDetailsModel.getMessage()){

                        try {

                            JSONObject obj=new JSONObject(response);
                            JSONObject json=obj.getJSONObject("message");

                            String week=json.getJSONObject("dates").getString("Week1");
                            String month=json.getJSONObject("dates").getString("Month1");
                            String year=json.getJSONObject("dates").getString("Year1");


                            newWeektxtlable.setText(week);
                            newMonthtxtlable.setText(month);
                            newyeartxtlable.setText(year);

                            firstweektxtlable.setText(week);
                            firstmonthtxtlable.setText(month);
                            firstyeartxtlable.setText(year);

                            inviteeWeektxtLable.setText(week);
                            inviteemonthtxtLable.setText(month);
                            inviteeYeartxtLable.setText(year);

                            String str1=json.getJSONArray("new_converts").getJSONObject(0).getString("Year2");
                            String str2=json.getJSONArray("new_converts").getJSONObject(0).getString("Month2");
                            String str3=json.getJSONArray("new_converts").getJSONObject(0).getString("Week2");

                            newyeartxt.setText(str1);
                            newMonthtxt.setText(str2);
                            newWeektxt.setText(str3);

                            String str21=json.getJSONArray("first_timers").getJSONObject(0).getString("Year3");
                            String str22=json.getJSONArray("first_timers").getJSONObject(0).getString("Month3");
                            String str23=json.getJSONArray("first_timers").getJSONObject(0).getString("Week3");

                            firstyeartxt.setText(str21);
                            firstmonthtxt.setText(str22);
                            firstweektxt.setText(str23);

                            String str31=json.getJSONArray("invities_contacts").getJSONObject(0).getString("Year1");
                            String str32=json.getJSONArray("invities_contacts").getJSONObject(0).getString("Month1");
                            String str33=json.getJSONArray("invities_contacts").getJSONObject(0).getString("Week1");

                            inviteeYeartxt.setText(str31);
                            inviteemonthtxt.setText(str32);
                            inviteeWeektxt.setText(str33);

                            JSONArray jarray =json.getJSONArray("membership_strength");

                            for(int i=0;i<jarray.length();i++){

                                monthnameslist.add(jarray.getJSONObject(i).getString("month"));
                                //	newConvertsList.add(memStrengthList.get(i).getNew_converts()); total_member_count
                                totalMemberList.add(jarray.getJSONObject(i).getString("total_member_count"));
                            }
                            setData();


                            JSONArray jarray2 =json.getJSONArray("partnership");

                            for(int i=0;i<jarray2.length();i++){
                                //pledge
                                monthnameslist2.add(jarray2.getJSONObject(i).getString("Month"));
                                pledgeValList.add(jarray2.getJSONObject(i).getInt("pledge"));
                                givingValList.add(jarray2.getJSONObject(i).getInt("giving"));
                            }

                            setData2();


                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        mDetailsModel.getMessage().getMembership_strength();
                        mDetailsModel.getMessage().getPartnership();

                    }

                }


            }
        },new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Methods.closeProgressDialog();
                Log.d("droid","get all zones error---------------"+ error.getCause());


            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                MeetingListRequestModel model=new MeetingListRequestModel();
                model.setUsername(mPreferenceHelper.getString(Commons.USER_EMAILID));
                model.setUserpass(mPreferenceHelper.getString(Commons.USER_PASSWORD));

                String dataString=gson.toJson(model, MeetingListRequestModel.class);

                Log.d("droid","data passed is ::::::::"+dataString);
                params.put(DashboardDataService.DATA, dataString);
                return params;
            }
        };

        App.getInstance().addToRequestQueue(reqDashboard, "reqDashboard");
        reqDashboard.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
    }

    private void addDrawerListData() {

        if(mPreferenceHelper.getString(SynergyValues.Commons.USER_ROLE) != null){
            Role=mPreferenceHelper.getString(SynergyValues.Commons.USER_ROLE);}

        if(mPreferenceHelper.getString(Commons.USER_NAME) != null){
            Name=mPreferenceHelper.getString(SynergyValues.Commons.USER_NAME);}

        if(mPreferenceHelper.getString(Commons.USER_STATUS) != null){
            Status=mPreferenceHelper.getString(SynergyValues.Commons.USER_STATUS);}

        if(mPreferenceHelper.getString(Commons.USER_DESIGNATION) != null){
            Designation=mPreferenceHelper.getString(SynergyValues.Commons.USER_DESIGNATION);}

        if(mPreferenceHelper.getString(Commons.USER_IMAGE) != null){
            Image=mPreferenceHelper.getString(Commons.USER_IMAGE);}

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
                break;

            case 1:
                break;

            case 2:
                Intent intForm1=new Intent(this,ProfileView.class);
                intForm1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intForm1);
                break;

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


    private void setData(){
        //	ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> yVals2 = new ArrayList<BarEntry>();

		/*for(int i=0;i<newConvertsList.size();i++){
			yVals1.add(new BarEntry(Integer.valueOf(newConvertsList.get(i)),i));
		}*/

        Log.d("NonStop", "totalMemberlist count: " + totalMemberList.size());
//		for(int i=0;i<2;i++){
//			yVals2.add(new BarEntry(40000000,i));
//		}

        int i;
        for(i=0;i<totalMemberList.size();i++){
            yVals2.add(new BarEntry(Integer.valueOf(totalMemberList.get(i)),i));
            Log.d("NonStop", "totalMember: " + new BarEntry(Integer.valueOf(totalMemberList.get(i)),i));
//			yVals2.add(new BarEntry(400050,i));
        }
//        Log.d("NonStop", "totalMemberlist: " +Integer.valueOf(totalMemberList.get(i)));
		/*BarDataSet set1 = new BarDataSet(yVals1, "New Converts");
		set1.setColor(Color.BLACK);*/

        BarDataSet set2 = new BarDataSet(yVals2, "Total Member Count");
        set2.setColor(Color.BLUE);
        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        //	dataSets.add(set1);
        dataSets.add(set2);

        BarData data = new BarData(monthnameslist, dataSets);
        data.setValueFormatter(new LargeValueFormatter());

        // add space between the dataset groups in percent of bar-width
        data.setGroupSpace(40f);
        mChart.setData(data);
        mChart.invalidate();
    }

    private void setData2() {

        ArrayList<Entry> pledgeyVals = new ArrayList<Entry>();

        for(int i=0;i<pledgeValList.size();i++){

            pledgeyVals.add(new Entry(Integer.valueOf(pledgeValList.get(i)), i));
        }


        ArrayList<Entry> givingVals = new ArrayList<Entry>();

        for(int i=0;i<givingValList.size();i++){

            givingVals.add(new Entry(Integer.valueOf(givingValList.get(i)), i));
        }



        Log.d("droid", "pledgeValList ::: "+pledgeValList.size());
        Log.d("droid", "givingValList ::: "+givingValList.size());

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(pledgeyVals,"Pledge");
        // set1.setFillAlpha(110);
        // set1.setFillColor(Color.RED);

        // set the line to be drawn like this "- - - - - -"
        set1.enableDashedLine(10f, 5f, 0f);
        set1.setColor(Color.BLACK);
        set1.setCircleColor(Color.BLACK);
        set1.setLineWidth(1f);
        set1.setCircleSize(3f);
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(9f);
        set1.setFillAlpha(65);
        set1.setFillColor(Color.BLACK);

        LineDataSet set2 = new LineDataSet(givingVals,"giving");
        // set1.setFillAlpha(110);
        // set1.setFillColor(Color.RED);

        // set the line to be drawn like this "- - - - - -"
        set2.enableDashedLine(10f, 5f, 0f);
        set2.setColor(Color.RED);
        set2.setCircleColor(Color.RED);
        set2.setLineWidth(1f);
        set2.setCircleSize(3f);
        set2.setDrawCircleHole(false);
        set2.setValueTextSize(9f);
        set2.setFillAlpha(65);
        set2.setFillColor(Color.RED);

        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(set1); // add the datasets

        dataSets.add(set2);
        // create a data object with the datasets
        LineData data = new LineData(monthnameslist2, dataSets);

        // set data
        mChart2.setData(data);


    }

    private void setData3() {

        ArrayList<Entry> givingyVals = new ArrayList<Entry>();

        for(int i=0;i<givingValList.size();i++){

            givingyVals.add(new Entry(Integer.valueOf(givingValList.get(i)), i));
        }


        Log.d("droid", "pledgeValList ::: "+pledgeValList.size());
        Log.d("droid", "givingValList ::: "+givingValList.size());

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(givingyVals,"Giving");
        // set1.setFillAlpha(110);
        // set1.setFillColor(Color.RED);

        // set the line to be drawn like this "- - - - - -"
        set1.enableDashedLine(10f, 5f, 0f);
        set1.setColor(Color.RED);
        set1.setCircleColor(Color.RED);
        set1.setLineWidth(1f);
        set1.setCircleSize(3f);
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(9f);
        set1.setFillAlpha(65);
        set1.setFillColor(Color.RED);
        // set1.setDrawFilled(true);
        // set1.setShader(new LinearGradient(0, 0, 0, mChart.getHeight(),
        // Color.BLACK, Color.WHITE, Shader.TileMode.MIRROR));

        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(set1); // add the datasets

        // create a data object with the datasets
        LineData data = new LineData(monthnameslist, dataSets);

        // set data


        mChart3.setData(data);
    }

    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog dialog =  new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Closing Activity")
                    .setMessage("Are you sure you want to exit?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                    {
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
