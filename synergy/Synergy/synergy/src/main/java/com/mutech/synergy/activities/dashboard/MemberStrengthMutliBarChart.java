package com.mutech.synergy.activities.dashboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.mikephil.charting.charts.BarChart;
import com.google.gson.Gson;
import com.mutech.synergy.App;
import com.mutech.synergy.R;
import com.mutech.synergy.SynergyValues.Commons;
import com.mutech.synergy.SynergyValues.Web.DashboardDataService;
import com.mutech.synergy.SynergyValues.Web.DashboardListDataService;
import com.mutech.synergy.activities.event.MyEventListActivity.Holder;
import com.mutech.synergy.models.EventParticipantsModel.EventParticipantSubModel;
import com.mutech.synergy.models.MeetingListRequestModel;
import com.mutech.synergy.models.ResponseMessageModel2;
import com.mutech.synergy.utils.Methods;
import com.mutech.synergy.utils.NetworkHelper;
import com.mutech.synergy.utils.PreferenceHelper;

public class MemberStrengthMutliBarChart extends AppCompatActivity {

	private BarChart mChart;
	private PreferenceHelper mPreferenceHelper;
	private Gson gson;
	//private ArrayList<String> monthnameslist,newConvertsList,totalMemberList;

	ListView weeklist;
	JSONObject jsonobj;
	JSONArray jsonarray;
	ImageView filterimg;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_first_time_month);
		initialize();
	}

	@SuppressLint("NewApi")
	private void initialize() {

		/*getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		getSupportActionBar().setHomeButtonEnabled(false);
		getSupportActionBar().setCustomView(R.layout.custom_actionbar);
		TextView tvTitle=(TextView)getSupportActionBar().getCustomView().findViewById(R.id.title_text);
		tvTitle.setText("Member Strength    ");
		
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2E9AFE")));
		
		getSupportActionBar().setDisplayShowCustomEnabled(true);
*/ 		
		TextView textView1=(TextView) findViewById(R.id.textView1);
		filterimg=(ImageView) findViewById(R.id.imageView2);
		weeklist=(ListView) findViewById(R.id.weeklist);
		
		textView1.setText("Member Strength");
		
		mPreferenceHelper=new PreferenceHelper(this);
		gson=new Gson();
		jsonarray=new JSONArray();
		
		

	/*	monthnameslist=new ArrayList<String>();
		newConvertsList=new ArrayList<String>();
		totalMemberList=new ArrayList<String>();

		mChart=(BarChart) findViewById(R.id.chart1);

		mChart.getAxisRight().setEnabled(false);
		mChart.setDescription("");
		mChart.setPinchZoom(false);
		mChart.setDrawBarShadow(false);
		mChart.setDrawGridBackground(false);
		mChart.setNoDataText("");
		mChart.setNoDataTextDescription("");


		Legend l = mChart.getLegend();
		l.setPosition(LegendPosition.RIGHT_OF_CHART_INSIDE);

		XAxis xl = mChart.getXAxis();

		YAxis leftAxis = mChart.getAxisLeft();
		leftAxis.setValueFormatter(new LargeValueFormatter());
		leftAxis.setDrawGridLines(false);
		leftAxis.setSpaceTop(25f);

		mChart.getAxisRight().setEnabled(false);
*/
	
		if(NetworkHelper.isOnline(this)){
			Methods.showProgressDialog(this);
			getDashboardDataService("1");
			
		}
		else
			Methods.longToast("Please connect to Internet", this);
	}

	/*private void getDashboardDataService() {
		StringRequest reqDashboard=new StringRequest(Method.POST,DashboardDataService.SERVICE_URL,new Listener<String>() {

			@Override
			public void onResponse(String response) {
				Methods.closeProgressDialog();
				Log.d("droid","get all reqDashboard ---------------"+ response);

				if(response.contains("status"))
				{
					ResponseMessageModel2 respModel=gson.fromJson(response, ResponseMessageModel2.class);
					if(respModel.getMessage().getStatus()=="401"){
						Methods.longToast("User name or Password is incorrect", MemberStrengthMutliBarChart.this);
					}else{
						Methods.longToast(respModel.getMessage().getMessage(), MemberStrengthMutliBarChart.this);
					}
				}else{
					DashboardDetailsModel mDetailsModel=gson.fromJson(response, DashboardDetailsModel.class);
					if(null !=mDetailsModel.getMessage()){
						ArrayList<MemStrengthModel> memStrengthList = mDetailsModel.getMessage().getMembership_strength();							
						for(int i=0;i<memStrengthList.size();i++){	
							monthnameslist.add(memStrengthList.get(i).getMonth());
							newConvertsList.add(memStrengthList.get(i).getNew_converts());
							totalMemberList.add(memStrengthList.get(i).getTotal_member_count());	
						}

						setData();
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

				String dataString=gson.toJson(model, MeetingListRequestModel.class);

				Log.d("droid","data passed is ::::::::"+dataString);
				params.put(DashboardDataService.DATA, dataString);
				return params; 
			}
		};

		App.getInstance().addToRequestQueue(reqDashboard, "reqDashboard");
		reqDashboard.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
	}*/

	/*private void setData(){
		ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
		ArrayList<BarEntry> yVals2 = new ArrayList<BarEntry>();

		for(int i=0;i<newConvertsList.size();i++){
			yVals1.add(new BarEntry(Integer.valueOf(newConvertsList.get(i)),i));
		}

		for(int i=0;i<totalMemberList.size();i++){
			yVals2.add(new BarEntry(Integer.valueOf(totalMemberList.get(i)),i));
		}

		BarDataSet set1 = new BarDataSet(yVals1, "New Converts");
		set1.setColor(Color.BLACK);
		BarDataSet set2 = new BarDataSet(yVals2, "Total Member Count");
		set2.setColor(Color.BLUE);

		ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
		dataSets.add(set1);
		dataSets.add(set2);

		BarData data = new BarData(monthnameslist, dataSets);
		data.setValueFormatter(new LargeValueFormatter());

		// add space between the dataset groups in percent of bar-width
		data.setGroupSpace(80f);
		mChart.setData(data);
		mChart.invalidate();
	}*/
	
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
						Methods.longToast("User name or Password is incorrect", MemberStrengthMutliBarChart.this);
					}else{
						Methods.longToast(respModel.getMessage().getMessage(), MemberStrengthMutliBarChart.this);
					}
				}else{
					
					try {
							JSONObject jsonobj=new JSONObject(response);
							int i=Integer.parseInt(jsonobj.getJSONObject("message").getString("total_count"));
							
					//		TOTAL_LIST_ITEMS=Integer.parseInt(jsonobj.getJSONObject("message").getString("total_count"));
						
					//		Log.e("totalcount=", ""+TOTAL_LIST_ITEMS);
							
					//		 Btnfooter();
							 
							 jsonarray=jsonobj.getJSONObject("message").getJSONArray("records");
							 
							if(jsonarray.length()>0){
							jsonarray=jsonobj.getJSONObject("message").getJSONArray("records");
							EventListAdapter adpter=new EventListAdapter(MemberStrengthMutliBarChart.this,jsonarray);
							weeklist.setAdapter(adpter);
							}else{
								Methods.longToast("No results found", MemberStrengthMutliBarChart.this);
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
				//	jsonobj.put("page_no",pageno);
					
					JSONObject obj=new JSONObject();
					obj.put("Month", 3);
					obj.put("flag","New Converts");
					
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



}
