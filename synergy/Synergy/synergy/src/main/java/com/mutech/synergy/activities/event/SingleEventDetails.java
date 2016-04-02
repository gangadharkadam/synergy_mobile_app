package com.mutech.synergy.activities.event;

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
import com.mutech.synergy.SynergyValues.Web.GetEventMemberListService;
import com.mutech.synergy.models.EventParticipantsModel;
import com.mutech.synergy.models.EventParticipantsModel.EventParticipantSubModel;
import com.mutech.synergy.models.MeetingListRequestModel;
import com.mutech.synergy.models.ResponseMessageModel;
import com.mutech.synergy.models.StatusCheckModel;
import com.mutech.synergy.utils.Methods;
import com.mutech.synergy.utils.NetworkHelper;
import com.mutech.synergy.utils.PreferenceHelper;

public class SingleEventDetails extends ActionBarActivity{

	private ArrayList<EventParticipantSubModel> eventModelList;
	private PreferenceHelper mPreferenceHelper;
	private String mEvent_name,mEventSubj,mEventVenue,mEvent_date;
	private TableLayout tblEventDetails;
	private TableRow tr;
	private Gson gson;
	private TextView lblMeetingNameHeader,lblMeetingName,lblMeetingDateTime,lblMeetingVenue,lblsubject;

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
		tvTitle.setText("Event Details");
		
		
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2E9AFE")));
		
		getSupportActionBar().setDisplayShowCustomEnabled(true);

		tblEventDetails=(TableLayout) findViewById(R.id.tblMemberDetails);
		lblMeetingNameHeader=(TextView) findViewById(R.id.lblMeetingNameHeader);
		lblMeetingNameHeader.setText("Event Name");

		lblMeetingName=(TextView) findViewById(R.id.lblMeetingName);
		lblMeetingDateTime=(TextView) findViewById(R.id.lblMeetingDateTime);
		lblMeetingVenue=(TextView) findViewById(R.id.lblMeetingVenue);
		lblsubject=(TextView) findViewById(R.id.lblMeetingsubject1);
		
		mPreferenceHelper=new PreferenceHelper(this);
		gson=new Gson();

		Bundle bundle=getIntent().getExtras().getBundle("Bundle");
		if(null !=bundle){
			mEvent_name=bundle.getString("Event_name");
			mEventSubj=bundle.getString("EventSubj");
			mEventVenue=bundle.getString("Event_venue");
			mEvent_date=bundle.getString("Event_date");

			lblMeetingName.setText(mEvent_name);
			lblMeetingDateTime.setText(mEvent_date);
			lblMeetingVenue.setText(mEventVenue);
			lblsubject.setText(mEventSubj);
		}


		if (NetworkHelper.isOnline(this)) {
			Methods.showProgressDialog(this);
			getAllEventMembers();
		}else{
			Methods.longToast("Please connect to Internet", this);
		}
	}

	private void addHeaders() {
		tr = new TableRow(this);
		tr.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
		tr.setBackgroundResource(R.drawable.table_header_background);

		TextView name = new TextView(this);
		name.setText("ID");
		name.setTextColor(Color.BLACK);
		name.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
		name.setPadding(10, 10, 10, 10);
		tr.addView(name);  // Adding textView to tablerow.

		/** Creating another textview **/
		TextView type = new TextView(this);
		type.setText("Name");
		type.setTextColor(Color.BLACK);
		type.setPadding(10, 10, 10, 10);
		type.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
		tr.addView(type); // Adding textView to tablerow.

		TextView attendanceStatus = new TextView(this);
		attendanceStatus.setText("Present/ \n Absent");
		attendanceStatus.setTextColor(Color.BLACK);
		attendanceStatus.setPadding(10, 10, 10, 10);
		attendanceStatus.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
		tr.addView(attendanceStatus); // Adding textView to tablerow.

		// Add the TableRow to the TableLayout
		tblEventDetails.addView(tr, new TableLayout.LayoutParams(
				LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
	}

	private void getAllEventMembers() {
		StringRequest reqMeetingList=new StringRequest(Method.POST,GetEventMemberListService.SERVICE_URL,new Listener<String>() {

			@Override
			public void onResponse(String response) {
				Methods.closeProgressDialog();
				Log.e("droid","get all reqMeetingList ---------------"+ response);

				StatusCheckModel statusCheckModel=gson.fromJson(response, StatusCheckModel.class);
				if(null != statusCheckModel.getStatus() && statusCheckModel.getStatus().trim().length() >0){
					ResponseMessageModel respModel=gson.fromJson(response, ResponseMessageModel.class);
					if(respModel.getStatus()=="401"){
						Methods.longToast("User name or Password is incorrect", SingleEventDetails.this);
					}else{
						Methods.longToast(respModel.getMessage(), SingleEventDetails.this);
					}
				}else{
					EventParticipantsModel model=gson.fromJson(response, EventParticipantsModel.class);

					if(null !=model.getStatus() && model.getStatus().trim().length() > 0){
						ResponseMessageModel modelResp=gson.fromJson(response, ResponseMessageModel.class);
						Methods.longToast(modelResp.getMessage(), SingleEventDetails.this);

					}else{
						eventModelList=new ArrayList<EventParticipantSubModel>();
						if(null !=model && null !=model.getMessage() && model.getMessage().size() > 0){
							for(int i=0;i<model.getMessage().size();i++){
								EventParticipantSubModel eventSubModel=model.new EventParticipantSubModel();
								eventSubModel=model.getMessage().get(i);
								eventModelList.add(i,eventSubModel);
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
				Methods.longToast("Can't fetch members,please try again later", SingleEventDetails.this);
			}
		})
		{
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();

				MeetingListRequestModel model=new MeetingListRequestModel();
				model.setUsername(mPreferenceHelper.getString(Commons.USER_EMAILID));
				model.setUserpass(mPreferenceHelper.getString(Commons.USER_PASSWORD));
				model.setEvent_id(mEvent_name);

				String dataString=gson.toJson(model, MeetingListRequestModel.class);

				Log.e("Request droid", dataString);
				params.put(GetEventMemberListService.DATA, dataString);
				return params; 
			}
		}
		;

		App.getInstance().addToRequestQueue(reqMeetingList, "reqMeetingList");
		reqMeetingList.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
	}

	protected void addRows() {
		for(int i=0;i<eventModelList.size();i++){
			tr = new TableRow(this);
			tr.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));

			TextView name = new TextView(this);
			name.setText(eventModelList.get(i).getMember());
			name.setTextColor(Color.GRAY);
			name.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
			name.setPadding(10, 10, 10, 10);
			tr.addView(name);  // Adding textView to tablerow.

			/** Creating another textview **/
			TextView type = new TextView(this);
			type.setText(eventModelList.get(i).getMember_name());
			type.setTextColor(Color.GRAY);
			type.setPadding(10, 10, 10, 10);
			type.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
			tr.addView(type); // Adding textView to tablerow.

			String attendance="Absent";
			if(null !=String.valueOf(eventModelList.get(i).getPresent()) && eventModelList.get(i).getPresent()==1){
				attendance="Present";
			}

			TextView attendanceStatus = new TextView(this);
			attendanceStatus.setText(attendance);
			attendanceStatus.setTextColor(Color.GRAY);
			attendanceStatus.setPadding(10, 10, 10, 10);
			attendanceStatus.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
			tr.addView(attendanceStatus); // Adding textView to tablerow.

			// Add the TableRow to the TableLayout
			tblEventDetails.addView(tr, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
		}
	}

}
