package com.mutech.synergy.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
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
import com.mutech.synergy.activities.cellMasters.PartnerRecordInformation;
import com.mutech.synergy.models.MemberProfileModel;
import com.mutech.synergy.models.MemberShortProfile;
import com.mutech.synergy.utils.Methods;
import com.mutech.synergy.utils.PreferenceHelper;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageLogs extends ActionBarActivity {

    private ListView lvMsgs;
    private ArrayList<String> mResultList;
    ArrayList<MemberProfileModel.ProfileSubModel> mProfSubModel;
    private PreferenceHelper mPreferenceHelper;
    private Gson gson;
    JSONArray jsonarray,jsonarray1,jsonarray2;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_logs);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar);

        context=MessageLogs.this;
        jsonarray=new JSONArray();
        jsonarray1=new JSONArray();
        jsonarray2=new JSONArray();
        mProfSubModel=new ArrayList<MemberProfileModel.ProfileSubModel>();
        mPreferenceHelper=new PreferenceHelper(this);
        gson=new Gson();


        lvMsgs=(ListView) findViewById(R.id.lvviewMsgLogs);

        mResultList=new ArrayList<>();

        mResultList.add("hello");
        mResultList.add("there");
        mResultList.add("world");

        getMessageLogs();

        lvMsgs.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                try {

                    String Message = jsonarray.getJSONObject(position).getString("message");
                    Intent it = new Intent(context, MessageInformation.class);
                    it.putExtra("Message", Message);
                    it.putExtra("to_user",jsonarray.getJSONObject(position).getString("to_user"));
                    it.putExtra("datetime",jsonarray.getJSONObject(position).getString("datetime"));
                    it.putExtra("name",jsonarray.getJSONObject(position).getString("name"));
                    it.putExtra("from_user",jsonarray.getJSONObject(position).getString("from_user"));
                    startActivity(it);

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
    }


    private void getMessageLogs() {

        StringRequest reqgetMessageLogs=new StringRequest(Request.Method.POST, SynergyValues.Web.GetMessageLogs.SERVICE_URL,new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Methods.closeProgressDialog();
                Log.d("droid", "get reqgetTopHierarchy ---------------" + response);
                try {

                    JSONObject jsonobj=new JSONObject(response);

                    jsonarray2=jsonobj.getJSONObject("message").getJSONArray("received");
                    jsonarray=jsonobj.getJSONObject("message").getJSONArray("sent");


                    for (int i = 0; i < jsonarray2.length(); i++) {
                        jsonarray.put(jsonarray2.getJSONObject(i));
                    }

                    String s3 = jsonarray.toString();

                    Log.e("droid","concat---------------"+ s3);

                    if(jsonarray.length()>0){

                        MessagesListAdapter adpter=new MessagesListAdapter(context,jsonarray);
                        lvMsgs.setAdapter(adpter);

                    }else{

                        Methods.longToast("No results found", MessageLogs.this);
                        MessagesListAdapter adpter=new MessagesListAdapter(context,jsonarray);
                        lvMsgs.setAdapter(adpter);
                    }



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
                Log.e("droid","get reqgetTopHierarchy error---------------"+ error.getCause());

                if(error.networkResponse.statusCode==403){
                    Methods.longToast("Access Denied", MessageLogs.this);
                }
                else
                    Methods.longToast("Some Error Occured,please try again later", MessageLogs.this);



            }

        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                MemberShortProfile model=new MemberShortProfile();
                model.setUsername(mPreferenceHelper.getString(SynergyValues.Commons.USER_EMAILID));
                model.setUserpass(mPreferenceHelper.getString(SynergyValues.Commons.USER_PASSWORD));

//                model.setRole("Cell Leader");

                String dataString=gson.toJson(model, MemberShortProfile.class);

                Log.d("droid", dataString);
                params.put(SynergyValues.Web.GetHigherHierarchyService.DATA, dataString);
                return params;
            }
        };

        App.getInstance().addToRequestQueue(reqgetMessageLogs, "reqgetCellDetails");
        reqgetMessageLogs.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));

    }

    class MessagesListAdapter extends BaseAdapter {

        private Context mContext;
        private JSONArray jarray;


        public MessagesListAdapter(Context context,JSONArray jarray) {
            mContext=context;
            this.jarray=jarray;

        }

        @Override
        public int getCount() {
            return jarray.length();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            LayoutInflater layout = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layout.inflate(R.layout.row_messageslist, null);

            TextView txtMsg=(TextView) convertView.findViewById(R.id.txtMsg);
            TextView txtDateandtime=(TextView) convertView.findViewById(R.id.txtDateandtime);

            try {

                txtMsg.setText(jarray.getJSONObject(position).getString("message"));
                txtDateandtime.setText(jarray.getJSONObject(position).getString("datetime"));

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return convertView;
        }
    }

}
