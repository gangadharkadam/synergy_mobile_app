package com.mutech.synergy.activities.dashboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.mutech.synergy.App;
import com.mutech.synergy.R;
import com.mutech.synergy.R.id;
import com.mutech.synergy.R.layout;
import com.mutech.synergy.R.menu;
import com.mutech.synergy.SynergyValues.Commons;
import com.mutech.synergy.SynergyValues.Web.DashboardDataService;
import com.mutech.synergy.SynergyValues.Web.DashboardListDataService;
import com.mutech.synergy.activities.event.MyEventListActivity.Holder;
import com.mutech.synergy.models.MeetingListRequestModel;
import com.mutech.synergy.models.ResponseMessageModel2;
import com.mutech.synergy.models.EventParticipantsModel.EventParticipantSubModel;
import com.mutech.synergy.utils.Methods;
import com.mutech.synergy.utils.NetworkHelper;
import com.mutech.synergy.utils.PreferenceHelper;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class MembershipStrengthDetailsActivity extends Activity {
	
	private PreferenceHelper mPreferenceHelper;
	private Gson gson;
	
	public int TOTAL_LIST_ITEMS;
    public int NUM_ITEMS_PAGE   = 20;
    private int noOfBtns;
    private Button[] btns;
    JSONArray jsonarray;
    
    ListView weeklist;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_membership_strength_details);
		
		mPreferenceHelper=new PreferenceHelper(this);
		gson=new Gson();
		weeklist=(ListView) findViewById(R.id.weeklist);
	}

	
	
private void getDashboardDataService(final String pageno) {
		StringRequest reqDashboard=new StringRequest(Method.POST,DashboardListDataService.SERVICE_URL,new Listener<String>() {

			@Override
			public void onResponse(String response) {
				Methods.closeProgressDialog();
				Log.e("droid","get all First Timer Week---------"+ response);

				if(response.contains("status"))
				{
					ResponseMessageModel2 respModel=gson.fromJson(response, ResponseMessageModel2.class);
					if(respModel.getMessage().getStatus()=="401"){
						Methods.longToast("User name or Password is incorrect", MembershipStrengthDetailsActivity.this);
					}else{
						Methods.longToast(respModel.getMessage().getMessage(), MembershipStrengthDetailsActivity.this);
					}
				}else{
					
					try {
							JSONObject jsonobj=new JSONObject(response);
							int i=Integer.parseInt(jsonobj.getJSONObject("message").getString("total_count"));
							
							TOTAL_LIST_ITEMS=Integer.parseInt(jsonobj.getJSONObject("message").getString("total_count"));
						
							Log.e("totalcount=", ""+TOTAL_LIST_ITEMS);
							
							 Btnfooter();
							 
							 jsonarray=jsonobj.getJSONObject("message").getJSONArray("records");
							 
							if(jsonarray.length()>0){
							jsonarray=jsonobj.getJSONObject("message").getJSONArray("records");
							EventListAdapter adpter=new EventListAdapter(MembershipStrengthDetailsActivity.this,jsonarray);
							weeklist.setAdapter(adpter);
							}else{
								Methods.longToast("No results found", MembershipStrengthDetailsActivity.this);
							}
							
							
						} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
						}
					
					}
				
				
				//}
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

				JSONObject jsonobj=new JSONObject();
				try {
					
					jsonobj.put("username", mPreferenceHelper.getString(Commons.USER_EMAILID));
					jsonobj.put("userpass", mPreferenceHelper.getString(Commons.USER_PASSWORD));
					jsonobj.put("tbl", "Membership Strength");
					jsonobj.put("page_no",pageno);
					
					JSONObject obj=new JSONObject();
						
					jsonobj.put("filters", obj);
					
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				String dataString=jsonobj.toString();
			//	String dataString=gson.toJson(model, MeetingListRequestModel.class);

				Log.d("droid","data passed is ::::::::"+dataString);
				params.put(DashboardDataService.DATA, dataString);
				return params; 
			}
		};

		App.getInstance().addToRequestQueue(reqDashboard, "reqDashboard");
		reqDashboard.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
}

private class EventListAdapter extends BaseAdapter{

	private Context mContext;
	private ArrayList<EventParticipantSubModel> mList;
	private Holder holder;
	JSONArray jsonarray;
	
	public EventListAdapter(Context context,JSONArray jsonarray) {
		mContext=context;
		this.jsonarray=jsonarray;
	}

	@Override
	public int getCount() {
		return jsonarray.length();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater layout = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = layout.inflate(R.layout.row_meeting, null);
		
		
		
		Button btn=(Button) convertView.findViewById(R.id.btnMarkAttendance);
		TextView name=(TextView) convertView.findViewById(R.id.lblMeetingName);
		TextView sex=(TextView) convertView.findViewById(R.id.lblMeetingTime);
		TextView ftv_name=(TextView) convertView.findViewById(R.id.lblMeetingVenue);
		TextView email=(TextView) convertView.findViewById(R.id.lblMeetingsubject);
		
		btn.setVisibility(View.GONE);
		try {
			
			name.setText(jsonarray.getJSONObject(position).getString("name"));
			email.setText(jsonarray.getJSONObject(position).getString("email_id"));
			ftv_name.setText(jsonarray.getJSONObject(position).getString("ftv_name"));
			sex.setText(jsonarray.getJSONObject(position).getString("sex"));
			
		
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return convertView;

	}
}

private void Btnfooter()
{
    int val = TOTAL_LIST_ITEMS%NUM_ITEMS_PAGE;
    val = val==0?0:1;
    noOfBtns=TOTAL_LIST_ITEMS/NUM_ITEMS_PAGE+val;
     
    LinearLayout ll = (LinearLayout)findViewById(R.id.btnLay);
    ll.removeAllViews();
    
    
    btns    =new Button[noOfBtns];
     
    for(int i=0;i<noOfBtns;i++)
    {
        btns[i] =   new Button(this);
        btns[i].setBackgroundColor(getResources().getColor(android.R.color.transparent));
        btns[i].setText(""+(i+1));
         
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        ll.addView(btns[i], lp);
         
        final int j = i;
        btns[j].setOnClickListener(new OnClickListener() {
             
            public void onClick(View v) 
            {
               	if(NetworkHelper.isOnline(MembershipStrengthDetailsActivity.this)){
        			Methods.showProgressDialog(MembershipStrengthDetailsActivity.this);
        			getDashboardDataService( btns[j].getText().toString());
        			
        		}else{
        			
        			Methods.longToast("Please connect to Internet", MembershipStrengthDetailsActivity.this);
        		
        		}
            	
                CheckBtnBackGroud(j);
            }
        });
        
       
    }
     
}

private void CheckBtnBackGroud(int index)
{
  
    for(int i=0;i<noOfBtns;i++)
    {
        if(i==index)
        {
            btns[index].setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
            btns[i].setTextColor(getResources().getColor(android.R.color.white));
        }
        else
        {
            btns[i].setBackgroundColor(getResources().getColor(android.R.color.transparent));
            btns[i].setTextColor(getResources().getColor(android.R.color.black));
        }
    }
     
}

}
