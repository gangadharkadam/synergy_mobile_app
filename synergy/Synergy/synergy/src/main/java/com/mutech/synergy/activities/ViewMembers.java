package com.mutech.synergy.activities;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.mutech.synergy.activities.cellMasters.MemberInfoActivtiy;
import com.mutech.synergy.activities.event.CreateEventActivity;
import com.mutech.synergy.activities.event.EventListActivity;
import com.mutech.synergy.adapters.MastersListAdapter;
import com.mutech.synergy.adapters.MessagesListAdapter;
import com.mutech.synergy.adapters.ViewMemberListAdapter;
import com.mutech.synergy.models.AllMembersResponseModel;
import com.mutech.synergy.models.MeetingListRequestModel;
import com.mutech.synergy.models.MeetingModel;
import com.mutech.synergy.models.MessagesModel;
import com.mutech.synergy.models.ResponseMessageModel2;
import com.mutech.synergy.utils.Methods;
import com.mutech.synergy.utils.NetworkHelper;
import com.mutech.synergy.utils.PreferenceHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ViewMembers extends ActionBarActivity implements AdapterView.OnItemClickListener {


    private ListView lvAllMembers;
    private ArrayList<String> mMembersList;
    private ArrayList<AllMembersResponseModel.AllMemSubResponseModel> subModelList;
    private ArrayList<MeetingModel.MeetingListModel> mResultList;
    MeetingListRequestModel model;

    private DatePickerDialog fromDatePickerDialog,toDatePickerDialog;
    private SimpleDateFormat dateFormatter,dateFormatter01;
    Calendar newCalendar;

    private PreferenceHelper mPreferenceHelper;
    private Gson gson;
    String UserRoll;
    public int TOTAL_LIST_ITEMS;
    public int NUM_ITEMS_PAGE   = 20;
    private int noOfBtns;
    private Button[] btns;
    String cellcode;

    JSONObject jsonobj;
    JSONArray jsonarray;
    //ImageView filterimg;
    TextView txtcount;
    TextView txtFromDate,txtToDate;
    Spinner spresion,spzone,sppcf,spgroupchurch,spchurch,spSeniorCell,spCell;
    private ArrayList<String> mZoneList,mRegionList,mChurchList,mSeniorCellList,mGrpChurchList,mPCFList,mCellList;
    private String role;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_members);
        initialize();
    }

    @SuppressLint("NewApi")
    private void initialize() {

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar);

        cellcode=getIntent().getStringExtra("cellcode");
        role = getIntent().getStringExtra("role");
        txtcount=(TextView)getSupportActionBar().getCustomView().findViewById(R.id.title_text);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2E9AFE")));

        txtcount.setText("Members   ");
        getSupportActionBar().setDisplayShowCustomEnabled(true);



        lvAllMembers=(ListView) findViewById(R.id.lvviewMembers);
        lvAllMembers.setOnItemClickListener(this);
        mMembersList=new ArrayList<String>();

        mPreferenceHelper=new PreferenceHelper(this);
        gson=new Gson();


        mZoneList=new ArrayList<String>();
        mRegionList=new ArrayList<String>();
        mChurchList=new ArrayList<String>();
        mSeniorCellList=new ArrayList<String>();
        mGrpChurchList=new ArrayList<String>();
        mPCFList=new ArrayList<String>();
        mCellList=new ArrayList<String>();

        UserRoll=mPreferenceHelper.getString(SynergyValues.Commons.USER_ROLE);

        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        //2015-05-24 06:22:22
        dateFormatter01=new SimpleDateFormat("yyyy-MM-dd");

        newCalendar = Calendar.getInstance();

        fromDatePickerDialog = new DatePickerDialog(ViewMembers.this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                txtFromDate.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        toDatePickerDialog = new DatePickerDialog(ViewMembers.this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                txtToDate.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        fromDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        toDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

       if(NetworkHelper.isOnline(ViewMembers.this)){
            Methods.showProgressDialog(ViewMembers.this);
           switch(role) {
               case "Cell Leader":
                getListNew("Member","1","","","","","","",cellcode,"","");
                   break;

               case "Senior Cell Leader":
                   getListNew("Member","1","","","","","",cellcode,"","","");
                   break;

               case "PCF Leader":
                   getListNew("Member","1","","","","",cellcode,"","","","");
                   break;

           }

        }else{

           Methods.longToast("Please connect to Internet", ViewMembers.this);

        }

    }

    private void getListNew(final String tbl,final String pageno,final String resion,final String zone,final String gchurch,final String church,final String pcf,final String srcell,final String cell,final String fdate,final String todate){

        //	StringRequest reqgetLowerHierarchy=new StringRequest(Method.POST,GetAllMastersService.SERVICE_URL,new Listener<String>() {
        StringRequest reqgetLowerHierarchy=new StringRequest(Request.Method.POST, SynergyValues.Web.GetAllListMastersService.SERVICE_URL,new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Methods.closeProgressDialog();
                Log.e("droid get reqResponce ---------------", response);


                if(response.contains("status"))
                {
                    ResponseMessageModel2 respModel=gson.fromJson(response, ResponseMessageModel2.class);
                    if(respModel.getMessage().getStatus()=="401"){
                        Methods.longToast("User name or Password is incorrect", ViewMembers.this);
                    }else{
                        Methods.longToast(respModel.getMessage().getMessage(), ViewMembers.this);
                    }
                }else{


                    try {

                        jsonobj=new JSONObject(response);
                        jsonobj.getJSONObject("message");

                        int i=Integer.parseInt(jsonobj.getJSONObject("message").getString("total_count"));

                        TOTAL_LIST_ITEMS=Integer.parseInt(jsonobj.getJSONObject("message").getString("total_count"));

                        txtcount.setText("Members ("+i+")");
                        jsonarray=jsonobj.getJSONObject("message").getJSONArray("records");

                        if(jsonarray.length()>0){


                            DetailAdapter adapter=new DetailAdapter(ViewMembers.this,jsonarray);
                            lvAllMembers.setAdapter(adapter);
                            adapter.notifyDataSetChanged();


                        }else{

                            DetailAdapter adapter=new DetailAdapter(ViewMembers.this,jsonarray);
                            lvAllMembers.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                            Methods.longToast("No results found", ViewMembers.this);
                        }


                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }


            }
        },new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Methods.closeProgressDialog();
                Log.d("droid","get reqgetLowerHierarchy error---------------"+ error.getCause());

                if(error!=null) {
                    if(error.networkResponse.statusCode==403){
                        //	Methods.longToast("Access Denied", CreateSeniorCellMasterActivity.this);
                    }
                }
                //else
                //	Methods.longToast("Some Error Occured,please try again later", CreateSeniorCellMasterActivity.this);

            }

        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                Map<String, String> params = new HashMap<String, String>();

                try{

                    jsonobj=new JSONObject();

                    jsonobj.put("username", mPreferenceHelper.getString(SynergyValues.Commons.USER_EMAILID));
                    jsonobj.put("userpass", mPreferenceHelper.getString(SynergyValues.Commons.USER_PASSWORD));

                    jsonobj.put("tbl",tbl);
                    jsonobj.put("page_no",pageno);


                    JSONObject jsonfilter=new JSONObject();

                    if(!resion.equals(""))
                        jsonfilter.put("region", resion);

                    if(!zone.equals(""))
                        jsonfilter.put("zone", zone);

                    if(!gchurch.equals(""))
                        jsonfilter.put("group_church", gchurch);

                    if(!church.equals(""))
                        jsonfilter.put("church", church);

                    if(!pcf.equals(""))
                        jsonfilter.put("pcf", pcf);

                    if(!srcell.equals(""))
                        jsonfilter.put("senior_cell", srcell);

                    if(!cell.equals(""))
                        jsonfilter.put("cell", cell);

                    if(!fdate.equals(""))
                        jsonfilter.put("from_date", fdate);

                    if(!todate.equals(""))
                        jsonfilter.put("to_date", todate);


                    jsonobj.put("filters", jsonfilter);

                }catch(Exception ex){

                }

                String dataString=jsonobj.toString();//gson.toJson(model, MeetingListRequestModel.class);

                Log.e("Request droid", dataString);
                params.put(SynergyValues.Web.GetHigherHierarchyService.DATA, dataString);
                return params;
            }
        };

        App.getInstance().addToRequestQueue(reqgetLowerHierarchy, "reqgetLowerHierarchy");
        reqgetLowerHierarchy.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {

        try {

            Intent intMemberDetails=new Intent(this,MemberInfoActivtiy.class);
            intMemberDetails.putExtra("MemberNo",jsonarray.getJSONObject(position).getString("name"));
            startActivity(intMemberDetails);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    public class DetailAdapter extends BaseAdapter {

        private Context context;
        private JSONArray jsonarray;
        View row;

        public DetailAdapter(Context context, JSONArray Jarray) {
            this.context = context;
            this.jsonarray = Jarray;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return jsonarray.length();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub

            Object obj = null;
            try {
                obj = jsonarray.get(position);
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
            return obj;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @SuppressLint("ViewHolder")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = li.inflate(R.layout.row_meeting, parent, false);


            Button btn=(Button) row.findViewById(R.id.btnMarkAttendance);
            TextView id=(TextView) row.findViewById(R.id.lblMeetingName);
            TextView membername=(TextView) row.findViewById(R.id.lblMeetingTime);
            TextView emailid=(TextView) row.findViewById(R.id.lblMeetingVenue);
            TextView name=(TextView) row.findViewById(R.id.lblMeetingsubject);

            membername.setTextSize(18);
            name.setTextSize(18);
            btn.setVisibility(View.GONE);
            id.setVisibility(View.GONE);
            membername.setVisibility(View.GONE);

            try {

                String str = "";
                if(jsonarray.getJSONObject(position).has("surname"))
                    str=(jsonarray.getJSONObject(position).getString("surname").equals("null"))?"":jsonarray.getJSONObject(position).getString("surname");
                if(jsonarray.getJSONObject(position).has("member_name"))
                    name.setText(jsonarray.getJSONObject(position).getString("member_name")+" "+str);
                else
                    name.setText(jsonarray.getJSONObject(position).getString("name")+" "+str);
                if(jsonarray.getJSONObject(position).has("email_id"))
                    emailid.setText(jsonarray.getJSONObject(position).getString("email_id"));
                else
                    emailid.setText(jsonarray.getJSONObject(position).getString("contact_email_id"));

                //id.setText(jsonarray.getJSONObject(position).getString("name"));

                //	surname.setText((jsonarray.getJSONObject(position).getString("surname").equals("null"))?"":jsonarray.getJSONObject(position).getString("surname"));
                //	name.setText(jsonarray.getJSONObject(position).getString("email_id"));

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return row;
        }
    }


    public static class SpinnerDataFlag{
        public static boolean Regions_flag = true;
        public static boolean Zones_flag = true;
        public static boolean GroupChurches_flag = true;
        public static boolean Churches_flag = true;
        public static boolean PCFs_flag = true;
        public static boolean SeniorCells_flag = true;
        public static boolean Cells_flag = true;
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

        SpinnerDataFlag.Cells_flag=true;
        SpinnerDataFlag.Churches_flag=true;
        SpinnerDataFlag.GroupChurches_flag=true;
        SpinnerDataFlag.PCFs_flag=true;
        SpinnerDataFlag.Regions_flag=true;
        SpinnerDataFlag.SeniorCells_flag=true;
        SpinnerDataFlag.Zones_flag=true;
//	pageflag=true;

    }

}
