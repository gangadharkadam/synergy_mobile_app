package com.mutech.synergy.activities.dashboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.TextView;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.gson.Gson;
import com.mutech.synergy.App;
import com.mutech.synergy.R;
import com.mutech.synergy.SynergyValues.Commons;
import com.mutech.synergy.SynergyValues.Web.DashboardDataService;
import com.mutech.synergy.models.DashboardDetailsModel;
import com.mutech.synergy.models.DashboardDetailsModel.DashPartnershipModel;
import com.mutech.synergy.models.MeetingListRequestModel;
import com.mutech.synergy.models.ResponseMessageModel2;
import com.mutech.synergy.utils.Methods;
import com.mutech.synergy.utils.NetworkHelper;
import com.mutech.synergy.utils.PreferenceHelper;

public class PledgeActivity extends ActionBarActivity {

	private LineChart mChart;
	private int from;
	private PreferenceHelper mPreferenceHelper;
	private String typeGraph;
	private Gson gson;
	ArrayList<String> monthnameslist;
	ArrayList<Integer> pledgeValList,givingValList;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashboard);
		from = getIntent().getIntExtra("FROM", 3);
		initialize();

	}

	@SuppressLint("NewApi")
	private void initialize() {

		getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		getSupportActionBar().setHomeButtonEnabled(false);
		getSupportActionBar().setCustomView(R.layout.custom_actionbar);
		
		
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2E9AFE")));
		
		TextView tvTitle = (TextView) getSupportActionBar().getCustomView().findViewById(R.id.title_text);

		mPreferenceHelper=new PreferenceHelper(this);
		gson=new Gson();

		monthnameslist=new ArrayList<String>();
		pledgeValList=new ArrayList<Integer>();
		givingValList=new ArrayList<Integer>();

		if (from == 0){
			tvTitle.setText("Pledge");
			typeGraph="Pledge";
		}
		else if (from == 1){
			tvTitle.setText("Giving");
			typeGraph="Giving";
		}
		getSupportActionBar().setDisplayShowCustomEnabled(true);

		mChart = (LineChart) findViewById(R.id.chart1);
		mChart.setDrawGridBackground(false);


		// no description text
		mChart.setDescription("");
		mChart.setNoDataTextDescription("");
		mChart.setNoDataText("");

		mChart.setHighlightEnabled(true);

		mChart.setTouchEnabled(true);

		mChart.setDragEnabled(true);
		mChart.setScaleEnabled(true);

		mChart.setPinchZoom(true);

		mChart.setHighlightEnabled(false);

		YAxis leftAxis = mChart.getAxisLeft();
		leftAxis.removeAllLimitLines(); // reset all limit lines to avoid
		// overlapping lines
		// leftAxis.setAxisMaxValue(220f);
		// leftAxis.setAxisMinValue(-50f);
		leftAxis.setStartAtZero(true);
		// leftAxis.setYOffset(20f);
		leftAxis.enableGridDashedLine(10f, 10f, 0f);

		// limit lines are drawn behind data (and not on top)
		leftAxis.setDrawLimitLinesBehindData(true);

		mChart.getAxisRight().setEnabled(false);

		mChart.animateX(2500, Easing.EasingOption.EaseInOutQuart);

		Legend l = mChart.getLegend();
		// modify the legend ...
		l.setPosition(LegendPosition.RIGHT_OF_CHART);
		l.setForm(LegendForm.SQUARE);

		if(NetworkHelper.isOnline(this)){
			Methods.showProgressDialog(this);
			getDashboardDataService();
		}
		else
			Methods.longToast("Please connect to Internet", this);

	}

	private void getDashboardDataService() {
		StringRequest reqDashboard=new StringRequest(Method.POST,DashboardDataService.SERVICE_URL,new Listener<String>() {

			@Override
			public void onResponse(String response) {

				Log.d("droid","get all reqDashboard ---------------"+ response);

				if(response.contains("status"))
				{
					ResponseMessageModel2 respModel=gson.fromJson(response, ResponseMessageModel2.class);
					if(respModel.getMessage().getStatus()=="401"){
						Methods.longToast("User name or Password is incorrect", PledgeActivity.this);
					}else{
						Methods.longToast(respModel.getMessage().getMessage(), PledgeActivity.this);
					}
				}else{
					DashboardDetailsModel mDetailsModel=gson.fromJson(response, DashboardDetailsModel.class);
					if(null !=mDetailsModel.getMessage()){
						ArrayList<DashPartnershipModel> partnership = mDetailsModel.getMessage().getPartnership();	
						Log.e(null, "length"+partnership.size());
						for(int i=0;i<partnership.size();i++){	
							monthnameslist.add(partnership.get(i).getMonth());
							pledgeValList.add(partnership.get(i).getPledge());
							givingValList.add(partnership.get(i).getGiving());
						}

						setData();
					}
				}
				Methods.closeProgressDialog();
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

	private void setData() {

		ArrayList<Entry> yVals = new ArrayList<Entry>();		
		if(from==0){
			for(int i=0;i<pledgeValList.size();i++){
				
				yVals.add(new Entry(Integer.valueOf(pledgeValList.get(i)), i));
			}
		}else{
			for(int i=0;i<givingValList.size();i++){
				
				yVals.add(new Entry(Integer.valueOf(givingValList.get(i)), i));
			}
		}

		Log.d("droid", "pledgeValList ::: "+pledgeValList.size());
		Log.d("droid", "givingValList ::: "+givingValList.size());

		// create a dataset and give it a type
		LineDataSet set1 = new LineDataSet(yVals,typeGraph);
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
		// set1.setDrawFilled(true);
		// set1.setShader(new LinearGradient(0, 0, 0, mChart.getHeight(),
		// Color.BLACK, Color.WHITE, Shader.TileMode.MIRROR));

		ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
		dataSets.add(set1); // add the datasets

		// create a data object with the datasets
		LineData data = new LineData(monthnameslist, dataSets);

		// set data
		mChart.setData(data);
	}
}
