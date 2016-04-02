package com.mutech.synergy;

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
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.LargeValueFormatter;
import com.google.gson.Gson;
import com.mutech.synergy.SynergyValues.Commons;
import com.mutech.synergy.SynergyValues.Web.DashboardDataService;
import com.mutech.synergy.activities.HomeActivity;
import com.mutech.synergy.models.DashboardDetailsModel;
import com.mutech.synergy.models.MeetingListRequestModel;
import com.mutech.synergy.models.ResponseMessageModel2;
import com.mutech.synergy.models.DashboardDetailsModel.MemStrengthModel;
import com.mutech.synergy.utils.Methods;
import com.mutech.synergy.utils.PreferenceHelper;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class GraphTestActivity extends Activity {

	BarChart mChart;
	private LineChart mChart3;
	private PreferenceHelper mPreferenceHelper;
	private Gson gson;
	private ArrayList<String> monthnameslist,newConvertsList,totalMemberList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_graph_test);
		
		mPreferenceHelper=new PreferenceHelper(this);
		gson=new Gson();
		
		
		monthnameslist=new ArrayList<String>();
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


		Legend l1 = mChart.getLegend();
		l1.setPosition(LegendPosition.RIGHT_OF_CHART_INSIDE);

		XAxis x = mChart.getXAxis();

		YAxis leftAxis1 = mChart.getAxisLeft();
		leftAxis1.setValueFormatter(new LargeValueFormatter());
		leftAxis1.setDrawGridLines(false);
		leftAxis1.setSpaceTop(25f);

		mChart.getAxisRight().setEnabled(false);
	}
	
	private void setData(){
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
		
		
		getDashboardDataService1();
	}
	
	private void getDashboardDataService1() {
		StringRequest reqDashboard=new StringRequest(Method.POST,DashboardDataService.SERVICE_URL,new Listener<String>() {

			@Override
			public void onResponse(String response) {
				Methods.closeProgressDialog();
				Log.e("droid","get all first graph responce ---------------"+ response);

				if(response.contains("status"))
				{
					ResponseMessageModel2 respModel=gson.fromJson(response, ResponseMessageModel2.class);
					if(respModel.getMessage().getStatus()=="401"){
						Methods.longToast("User name or Password is incorrect", GraphTestActivity.this);
					}else{
						Methods.longToast(respModel.getMessage().getMessage(), GraphTestActivity.this);
					}
				}else{
					DashboardDetailsModel mDetailsModel=gson.fromJson(response, DashboardDetailsModel.class);
					if(null !=mDetailsModel.getMessage()){
						ArrayList<MemStrengthModel> memStrengthList = mDetailsModel.getMessage().getMembership_strength();							
						for(int i=0;i<memStrengthList.size();i++){	
							monthnameslist.add(memStrengthList.get(i).getMonth());
						//	newConvertsList.add(memStrengthList.get(i).getNew_converts());
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
	}

	
}
