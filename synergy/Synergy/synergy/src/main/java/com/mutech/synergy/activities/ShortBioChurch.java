package com.mutech.synergy.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
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
import com.mutech.synergy.models.MemberShortProfile;
import com.mutech.synergy.models.ResponseMessageModel2;
import com.mutech.synergy.utils.Methods;
import com.mutech.synergy.utils.PreferenceHelper;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ShortBioChurch extends ActionBarActivity {

    ArrayList<MemberProfileModel.ProfileSubModel> mProfSubModel;
    private PreferenceHelper mPreferenceHelper;
    private Gson gson;
    private TextView txtCellName, txtCellLeader, txtSeniorCellName, txtChurchGroup, txtChurch, txtPhone, txtEmailID;
    private String cellcode;
    JSONObject jsonobj;
    JSONArray jsonarray;
    //ImageView filterimg;
    TextView txtcount;
    public int TOTAL_LIST_ITEMS;
    public int NUM_ITEMS_PAGE   = 20;
    private ListView lvAllMembers;
    private String role;
    private String tbl;
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_short_bio_church);

        cellcode=getIntent().getStringExtra("cellcode");

        mProfSubModel=new ArrayList<MemberProfileModel.ProfileSubModel>();
        mPreferenceHelper=new PreferenceHelper(this);
        gson=new Gson();
        role = getIntent().getStringExtra("role");
        tbl = getIntent().getStringExtra("tbl");
        key = getIntent().getStringExtra("key");

        switch(role) {
            case "Church":
                getSupportActionBar().setTitle("Cells/PCF");
                break;

            case "GroupChurch":
                getSupportActionBar().setTitle("Churches");
                break;

            case "Zone":
                getSupportActionBar().setTitle("Group Churches");
                break;

            case "Region":
                getSupportActionBar().setTitle("Zones");
                break;
        }

        lvAllMembers=(ListView) findViewById(R.id.lvviewMembers);

        getCellDetails("1");

    }

    private void getCellDetails(final String pageno) {

        StringRequest reqgetCellDetails=new StringRequest(Request.Method.POST, SynergyValues.Web.GetCellPcf.SERVICE_URL,new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Methods.closeProgressDialog();

                Log.d("droid", "get shortbio ---------------" + response);
                if(response.contains("status"))
                {
                    ResponseMessageModel2 respModel=gson.fromJson(response, ResponseMessageModel2.class);

                    if(respModel.getMessage().getStatus()=="401"){
                        Methods.longToast("User name or Password is incorrect", ShortBioChurch.this);
                    }else{
                        Methods.longToast(respModel.getMessage().getMessage(), ShortBioChurch.this);
                    }
                }else{


                    try {

                        jsonobj=new JSONObject(response);
                        jsonobj.getJSONObject("message");

                        int i=Integer.parseInt(jsonobj.getJSONObject("message").getString("total_count"));

                        TOTAL_LIST_ITEMS=Integer.parseInt(jsonobj.getJSONObject("message").getString("total_count"));

                        jsonarray=jsonobj.getJSONObject("message").getJSONArray("records");

                        if(jsonarray.length()>0){


                            DetailAdapter adapter=new DetailAdapter(ShortBioChurch.this,jsonarray);
                            lvAllMembers.setAdapter(adapter);
                            adapter.notifyDataSetChanged();


                        }else{

                            DetailAdapter adapter=new DetailAdapter(ShortBioChurch.this,jsonarray);
                            lvAllMembers.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                            Methods.longToast("No results found", ShortBioChurch.this);
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
                Log.d("droid","get reqgetTopHierarchy error---------------"+ error.getCause());

                if(error.networkResponse.statusCode==403){
                    Methods.longToast("Access Denied", ShortBioChurch.this);
                }
                else
                    Methods.longToast("Some Error Occured,please try again later", ShortBioChurch.this);



            }

        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                MeetingListRequestModel model=new MeetingListRequestModel();
                model.setUsername(mPreferenceHelper.getString(SynergyValues.Commons.USER_EMAILID));
                model.setUserpass(mPreferenceHelper.getString(SynergyValues.Commons.USER_PASSWORD));
                model.setTbl(tbl);
                model.setName(cellcode);
                if(role.contentEquals("Church")){
                model.setKey(key);}
                else{
                model.setRole(role);}
                model.setPage_no(pageno);
//                model.setRole("Cell Leader");

                String dataString=gson.toJson(model, MeetingListRequestModel.class);

                Log.d("droid", dataString);
                params.put(SynergyValues.Web.GetHigherHierarchyService.DATA, dataString);
                return params;
            }
        };

        App.getInstance().addToRequestQueue(reqgetCellDetails, "reqgetCellDetails");
        reqgetCellDetails.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));

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
            name.setTextSize(14);
            emailid.setTextSize(12);
            btn.setVisibility(View.GONE);
            id.setVisibility(View.GONE);
            membername.setVisibility(View.GONE);

            try {

                if(jsonarray.getJSONObject(position).has("name"))
                    emailid.setText(jsonarray.getJSONObject(position).getString("name"));

                if(jsonarray.getJSONObject(position).has("senior_cell_name"))
                    name.setText(jsonarray.getJSONObject(position).getString("senior_cell_name"));

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
}
