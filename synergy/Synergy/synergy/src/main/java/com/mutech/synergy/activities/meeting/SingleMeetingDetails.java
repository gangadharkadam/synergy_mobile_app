package com.mutech.synergy.activities.meeting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TableRow;
import android.widget.TextView;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.mutech.synergy.App;
import com.mutech.synergy.R;
import com.mutech.synergy.SynergyValues.Commons;
import com.mutech.synergy.SynergyValues.Web.GetAllMeetingService;
import com.mutech.synergy.SynergyValues.Web.GetMeetingMemberListService;
import com.mutech.synergy.activities.profile.MyProfileActivity;
import com.mutech.synergy.models.MeetingListRequestModel;
import com.mutech.synergy.models.MemberDetailsRespModel;
import com.mutech.synergy.models.StatusCheckModel;
import com.mutech.synergy.models.MemberDetailsRespModel.MemberModel;
import com.mutech.synergy.models.ResponseMessageModel;
import com.mutech.synergy.utils.Methods;
import com.mutech.synergy.utils.NetworkHelper;
import com.mutech.synergy.utils.PreferenceHelper;

public class SingleMeetingDetails extends ActionBarActivity {

	private ArrayList<MemberModel> memModelList;
	private PreferenceHelper mPreferenceHelper;
	private String mMeeting_name,mMeetingVenue,mMeetingDateTime,mMeetingSubject;
	private TableLayout tblMemberDetails;
	private TableRow tr;
	private Gson gson;
	private TextView lblMeetingName,lblMeetingDateTime,lblMeetingVenue,lblMeetingsubject1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashboardcelldetails);
		initialize();
	}

	@SuppressLint("NewApi")
	private void initialize() {

		getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		getSupportActionBar().setHomeButtonEnabled(false);
		getSupportActionBar().setCustomView(R.layout.custom_actionbar);
		TextView tvTitle=(TextView)getSupportActionBar().getCustomView().findViewById(R.id.title_text);
		
		
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2E9AFE")));
		
		tvTitle.setText("Meeting Details   ");
		getSupportActionBar().setDisplayShowCustomEnabled(true);

		tblMemberDetails=(TableLayout) findViewById(R.id.tblMemberDetails);
		lblMeetingName=(TextView) findViewById(R.id.lblMeetingName);
		lblMeetingDateTime=(TextView) findViewById(R.id.lblMeetingDateTime);
		lblMeetingVenue=(TextView) findViewById(R.id.lblMeetingVenue);
		lblMeetingsubject1 =(TextView) findViewById(R.id.lblMeetingsubject1);

		mPreferenceHelper=new PreferenceHelper(this);
		gson=new Gson();
		Bundle bundle=getIntent().getExtras().getBundle("Bundle");
		if(null !=bundle){
			mMeeting_name=bundle.getString("Meeting_name");
			mMeetingVenue=bundle.getString("Meeting_venue");
			mMeetingDateTime=bundle.getString("Meeting_dateTime");
			mMeetingSubject =bundle.getString("MeetingSubj");
		}

		lblMeetingName.setText(mMeeting_name);
		lblMeetingVenue.setText(mMeetingVenue);
		lblMeetingDateTime.setText(mMeetingDateTime);
		lblMeetingsubject1.setText(mMeetingSubject);
		
		if (NetworkHelper.isOnline(this)) {
			Methods.showProgressDialog(this);
			getAllMeetingMembers();
		}else{
			Methods.longToast("Please connect to Internet", this);
		}

	}


	private void addHeaders() {

		tr = new TableRow(this);
		tr.setLayoutParams(new LayoutParams(
				LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		tr.setBackgroundResource(R.drawable.table_header_background);

		TextView name = new TextView(this);
		name.setText("Name");
		name.setTextColor(Color.BLACK);
		name.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
		name.setPadding(10, 10, 10, 10);
		tr.addView(name);  // Adding textView to tablerow.

		/** Creating another textview **/
		TextView type = new TextView(this);
		type.setText("ID");
		type.setTextColor(Color.BLACK);
		type.setPadding(10, 10, 10, 10);
		
		type.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
		tr.addView(type); // Adding textView to tablerow.

		TextView attendanceStatus = new TextView(this);
		attendanceStatus.setText("Present/ \n Absent");
		attendanceStatus.setTextColor(Color.BLACK);
		attendanceStatus.setPadding(5, 5, 5, 5);
		attendanceStatus.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
		tr.addView(attendanceStatus); // Adding textView to tablerow.

		// Add the TableRow to the TableLayout
		tblMemberDetails.addView(tr, new TableLayout.LayoutParams(
				LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));

	}

	private void getAllMeetingMembers() {

		StringRequest reqMeetingList=new StringRequest(Method.POST,GetMeetingMemberListService.SERVICE_URL,new Listener<String>() {

			@Override
			public void onResponse(String response) {
				Methods.closeProgressDialog();
				Log.e("droid","get all reqMeetingList ---------------"+ response);

				StatusCheckModel statusCheckModel=gson.fromJson(response, StatusCheckModel.class);
				if(null != statusCheckModel.getStatus() && statusCheckModel.getStatus().trim().length() >0){
					ResponseMessageModel respModel=gson.fromJson(response, ResponseMessageModel.class);
					if(respModel.getStatus()=="401"){
						Methods.longToast("User name or Password is incorrect", SingleMeetingDetails.this);
					}else{
						Methods.longToast(respModel.getMessage(), SingleMeetingDetails.this);
					}
				}else{
					MemberDetailsRespModel model=gson.fromJson(response, MemberDetailsRespModel.class);

					if(null !=model.getStatus() && model.getStatus().trim().length() > 0){
						ResponseMessageModel modelResp=gson.fromJson(response, ResponseMessageModel.class);
						Methods.longToast(modelResp.getMessage(), SingleMeetingDetails.this);

					}else{
						memModelList=new ArrayList<MemberModel>();
						if(null !=model.getMessage() && model.getMessage().size() > 0 ){
							for(int i=0;i<model.getMessage().size();i++){
								MemberModel memModel=model.new MemberModel();
								memModel=model.getMessage().get(i);
								memModelList.add(i,memModel);
							}
							addHeaders();
							addRows();
						}
					}
				}


			}
		},new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Methods.closeProgressDialog();
				Log.d("droid","get all zones error---------------"+ error.getCause());
				Methods.longToast("Can't fetch members,please try again later", SingleMeetingDetails.this);
			}
		})
		{
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();

				MeetingListRequestModel model=new MeetingListRequestModel();
				model.setUsername(mPreferenceHelper.getString(Commons.USER_EMAILID));
				model.setUserpass(mPreferenceHelper.getString(Commons.USER_PASSWORD));
				model.setMeeting_id(mMeeting_name);

				String dataString=gson.toJson(model, MeetingListRequestModel.class);

				Log.e("Request", dataString);
				params.put(GetAllMeetingService.DATA, dataString);
				return params; 
			}
		}
		;

		App.getInstance().addToRequestQueue(reqMeetingList, "reqMeetingList");
		reqMeetingList.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));

	}

	protected void addRows() {
		for(int i=0;i<memModelList.size();i++){
			tr = new TableRow(this);
			tr.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));

			TextView name = new TextView(this);
			name.setText(memModelList.get(i).getMember_name());
			name.setTextColor(Color.GRAY);
			name.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
			name.setPadding(10, 10, 10, 10);
			tr.addView(name);  // Adding textView to tablerow.

			/** Creating another textview **/
			TextView type = new TextView(this);
			type.setText(memModelList.get(i).getMember());
			type.setTextColor(Color.GRAY);
			type.setPadding(10, 10, 10, 10);
			type.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
			tr.addView(type); // Adding textView to tablerow.
			
			Log.e("status", memModelList.get(i).getPresent());

			String attendance="Absent";
			if(null !=memModelList.get(i).getPresent() && memModelList.get(i).getPresent().equals("1")){
				attendance="Present";
			}

			TextView attendanceStatus = new TextView(this);
			attendanceStatus.setText(attendance);
			attendanceStatus.setTextColor(Color.GRAY);
			attendanceStatus.setPadding(10, 10, 10, 10);
			attendanceStatus.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
			tr.addView(attendanceStatus); // Adding textView to tablerow.

			// Add the TableRow to the TableLayout
			tblMemberDetails.addView(tr, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
		}
	}

}
