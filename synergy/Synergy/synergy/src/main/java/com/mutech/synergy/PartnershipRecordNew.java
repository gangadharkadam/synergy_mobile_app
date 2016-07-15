package com.mutech.synergy;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.mutech.messagebraudcast.MessageBroadcastActivity;
import com.mutech.partnershiprecord.CreatePartnershipActivity;
import com.mutech.partnershiprecord.MyPartnershipActivity;
import com.mutech.partnershiprecord.MyPartnershipAdpter;
import com.mutech.partnershiprecord.PartnershipAdpter;
import com.mutech.synergy.App;
import com.mutech.synergy.R;
import com.mutech.synergy.SynergyValues;
import com.mutech.synergy.Todoadpternew;
import com.mutech.synergy.SynergyValues.Commons;
import com.mutech.synergy.SynergyValues.Web.GetPartnerShipService;
import com.mutech.synergy.SynergyValues.Web.ShowAllMembersService;

import com.mutech.synergy.activities.cellMasters.PartnerRecordInformation;
import com.mutech.synergy.adapters.CustomDrawerAdapter;
import com.mutech.synergy.adapters.TODOAdapter;
import com.mutech.synergy.models.AllMembersResponseModel;
import com.mutech.synergy.models.DrawerItem;
import com.mutech.synergy.models.MeetingListRequestModel;
import com.mutech.synergy.models.ResponseMessageModel2;
import com.mutech.synergy.models.AllMembersResponseModel.AllMemSubResponseModel;
import com.mutech.synergy.utils.Methods;
import com.mutech.synergy.utils.NetworkHelper;
import com.mutech.synergy.utils.PreferenceHelper;
import com.mutech.synergy.widget.SlidingTabLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class PartnershipRecordNew extends ActionBarActivity implements AdapterView.OnItemClickListener {

    //private ListView lvAllMembers;
    private ArrayList<String> mMembersList;
    private ArrayList<AllMembersResponseModel.AllMemSubResponseModel> subModelList;

    private PreferenceHelper mPreferenceHelper;
    private Gson gson;

    private ViewPager vpgrHome;
    private SlidingTabLayout mSlidingTabLayout;

    private ArrayList<DrawerItem> mDrawerList;
    private CustomDrawerAdapter mCustomDrawerAdapter;
    private ListView mLvDrawer;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private  String str;
    public static PartnershipAdpter mAdapter;
    private String Role,Name,Status,Designation,Image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partnership_record_new);
        initialize();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_partnership_adpter, menu);


        String UserRoll=mPreferenceHelper.getString(SynergyValues.Commons.USER_ROLE);

        if(UserRoll.equals("Member")||UserRoll.equals("PCF Leader")||UserRoll.equals("Senior Cell Leader")||UserRoll.equals("Cell Leader")){
            MenuItem item = menu.findItem(R.id.allartnership);
            item.setVisible(false);
        }


        return true;
    }

    @SuppressLint("NewApi")
    private void initialize() {

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar);
     //   getSupportActionBar().setHomeAsUpIndicator(R.drawable.actiontop);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2E9AFE")));
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        TextView tvTitle=(TextView)getSupportActionBar().getCustomView().findViewById(R.id.title_text);

        mMembersList=new ArrayList<String>();

        mPreferenceHelper=new PreferenceHelper(this);

        if(mPreferenceHelper.getString(SynergyValues.Commons.FROM_ACTIVITY).equals("true")) {
            tvTitle.setText("Partnership Records    ");
        }else{
            tvTitle.setText("My Partnership Records    ");}

        str=mPreferenceHelper.getString(SynergyValues.Commons.USER_ROLE);

        gson=new Gson();

        vpgrHome = (ViewPager) findViewById(R.id.vpgr_home);
        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setDistributeEvenly(true);

        mAdapter = new PartnershipAdpter(PartnershipRecordNew.this, getSupportFragmentManager());
        vpgrHome.setAdapter(mAdapter);

        mSlidingTabLayout.setViewPager(vpgrHome);
        mSlidingTabLayout.setSelectedIndicatorColors(Color.parseColor("#33B5E5"));

        mDrawerList = new ArrayList<DrawerItem>();


        if(NetworkHelper.isOnline(this)){
            //Methods.showProgressDialog(this);
            //getMemberList();
        }
        else
            Methods.longToast("Please connect to Internet", this);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;

        }

        //	String UserRoll=mPreferenceHelper.getString(Commons.USER_ROLE);
        //	if(UserRoll)

        int id = item.getItemId();
        if (id == R.id.allartnership) {

            startActivity(new Intent(PartnershipRecordNew.this, MyPartnershipActivity.class));
            return true;
        }

        if (id == R.id.action_create) {

            startActivity(new Intent(PartnershipRecordNew.this, CreatePartnershipActivity.class));
            return true;
        }
        //action_create

        return super.onOptionsItemSelected(item);
    }


    private void getMemberList() {

        StringRequest reqGetMembers=new StringRequest(Method.POST,GetPartnerShipService.SERVICE_URL,new Listener<String>() {
            @Override
            public void onResponse(String response) {
                Methods.closeProgressDialog();
                Log.d("droid","getMemberList ---------------"+ response);

                if(response.contains("status")){
                    ResponseMessageModel2 resp2Model=gson.fromJson(response, ResponseMessageModel2.class);
                    Methods.longToast(resp2Model.getMessage().getMessage(), PartnershipRecordNew.this);
                }else{
                    AllMembersResponseModel model=gson.fromJson(response, AllMembersResponseModel.class);
                    if(null !=model.getMessage() && model.getMessage().size() >0){
                        subModelList=new ArrayList<AllMemSubResponseModel>();
                        for(int i=0;i<model.getMessage().size();i++){
                            subModelList.add(model.getMessage().get(i));
                            Log.d("droid", "subModelList :::::::"+model.getMessage().get(i));
                        }
                        Log.d("droid", "subModelList final size ::: "+subModelList.size());
                        MemberListAdapter adapter=new MemberListAdapter(PartnershipRecordNew.this,subModelList);
                        //	lvAllMembers.setAdapter(adapter);
                    }
                }

            }
        },new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Methods.closeProgressDialog();
                Log.d("droid","get all pcf error---------------"+ error.getCause());
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
                params.put(ShowAllMembersService.DATA, dataString);
                return params;
            }
        };
        App.getInstance().addToRequestQueue(reqGetMembers, "reqGetMembers");
        reqGetMembers.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // TODO Auto-generated method stub
        Intent intPartnershipDetails=new Intent(this,PartnerRecordInformation.class);
        intPartnershipDetails.putExtra("PartnerNo", subModelList.get(position).getName());
        startActivity(intPartnershipDetails);
    }


    class MemberListAdapter extends BaseAdapter {

        private Context mContext;
        private ArrayList<AllMembersResponseModel.AllMemSubResponseModel> mList;
        private Holder holder;

        public MemberListAdapter(Context context,
                                 ArrayList<AllMembersResponseModel.AllMemSubResponseModel> meetingList) {
            mContext=context;
            mList=new ArrayList<AllMembersResponseModel.AllMemSubResponseModel>();
            mList=meetingList;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            LayoutInflater layout = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            holder = new Holder();
            if (convertView == null) {
                convertView = layout.inflate(R.layout.row_partnerlist, null);

                holder.txtMasterName=(TextView) convertView.findViewById(R.id.txtMasterName);
                holder.txtPartnershiparm =(TextView) convertView.findViewById(R.id.txtPartnershiparm);
                holder.txtAmount =(TextView) convertView.findViewById(R.id.txtAmount);
                convertView.setTag(holder);
            }else{
                holder = (Holder) convertView.getTag();
            }

            holder.txtMasterName.setText(mList.get(position).getName());
            holder.txtPartnershiparm.setText(mList.get(position).getPartnership_arms());
            holder.txtAmount.setText(mList.get(position).getAmount());
            return convertView;
        }

    }
    public static class Holder{
        private TextView txtMasterName;
        private TextView txtPartnershiparm;
        private TextView txtAmount;
    }


}