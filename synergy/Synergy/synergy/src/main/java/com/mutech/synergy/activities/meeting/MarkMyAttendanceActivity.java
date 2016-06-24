package com.mutech.synergy.activities.meeting;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.mutech.synergy.SynergyValues.Web.MarkMyMeetingAttendanceService;
import com.mutech.synergy.models.MarkMyAttendanceReqModel;
import com.mutech.synergy.models.MarkMyAttendanceReqModel.RecordModel;
import com.mutech.synergy.models.ResponseMessageModel;
import com.mutech.synergy.utils.Methods;
import com.mutech.synergy.utils.NetworkHelper;
import com.mutech.synergy.utils.PreferenceHelper;

public class MarkMyAttendanceActivity extends ActionBarActivity implements OnClickListener{

	private RadioGroup grgpAttended;
	private RadioButton rbtnYes,rbtnNo;
	private EditText txtMeetingCategory,txtMeetingSubject,txtVenue;
	private TextView txtFromDate,txtToDate;
	private Button btnSaveMettingAttendance;
	private DatePickerDialog fromDatePickerDialog,toDatePickerDialog;
	private SimpleDateFormat dateFormatter;
	private PreferenceHelper mPreferenceHelper;

	int status;
	private String MeetingCategory,MeetingSubject,FromDate,ToDate,Venue,Name;
	private int mPosition_Clicked;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_meetingattendance);

		initialize();
	}

	@SuppressLint("NewApi")
	private void initialize() {

		getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		getSupportActionBar().setHomeButtonEnabled(false);
		getSupportActionBar().setCustomView(R.layout.custom_actionbar);
		TextView tvTitle=(TextView)getSupportActionBar().getCustomView().findViewById(R.id.title_text);
		tvTitle.setText("Mark My Attendance   ");
		
		
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2E9AFE")));
		
		getSupportActionBar().setDisplayShowCustomEnabled(true);


		grgpAttended=(RadioGroup) findViewById(R.id.grgpAttended);
		rbtnYes=(RadioButton) findViewById(R.id.rbtnYes);
		rbtnNo=(RadioButton) findViewById(R.id.rbtnNo);
		txtMeetingCategory=(EditText) findViewById(R.id.txtMeetingCategory);
		txtMeetingSubject=(EditText) findViewById(R.id.txtMeetingSubject);
		txtFromDate=(TextView) findViewById(R.id.txtFromDate);
		txtToDate=(TextView) findViewById(R.id.txtToDate);
		txtVenue=(EditText) findViewById(R.id.txtVenue);
		btnSaveMettingAttendance=(Button) findViewById(R.id.btnSaveMettingAttendance);

		btnSaveMettingAttendance.setOnClickListener(this);

		mPreferenceHelper=new PreferenceHelper(this);

		dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

		Bundle bundle=getIntent().getExtras().getBundle("Bundle");
		if(null !=bundle){
			MeetingCategory=bundle.getString("MeetingCategory");
			MeetingSubject=bundle.getString("MeetingSubject");
			FromDate=bundle.getString("FromDate");
			ToDate=bundle.getString("ToDate");
			Venue=bundle.getString("Venue");
			Name=bundle.getString("Name");
			status=bundle.getInt("status");
			
			mPosition_Clicked=bundle.getInt("Position_Clicked");
			
			
			if(status==1){
				
				rbtnYes.setChecked(true);
			}
			else{
				
				rbtnNo.setChecked(true);
			}
			txtMeetingCategory.setText(MeetingCategory);
			txtMeetingSubject.setText(MeetingSubject);
			txtFromDate.setText(FromDate);
			txtToDate.setText(ToDate);
			txtVenue.setText(Venue);
			
			txtMeetingCategory.setEnabled(false);
			txtMeetingSubject.setEnabled(false);
			txtFromDate.setEnabled(false);
			txtToDate.setEnabled(false);
			txtVenue.setEnabled(false);	
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.btnSaveMettingAttendance:
			if(NetworkHelper.isOnline(this)){
				if(grgpAttended.getCheckedRadioButtonId() !=-1){
					Methods.showProgressDialog(this);
					markMyAttendance();
				}else{
					Methods.longToast("Please mark your attendance", this);
				}
			}else
			{
				Methods.longToast("Please connect to Internet", this);
			}

			break;
		default:
			break;
		}
	}

	
	//-{"message":"Updated Attendance","exc":"[\"[u'9890865260']\"]","_server_messages":"[\"['Status=0,success\\\\r\\\\n']\"]"}

	
	protected void markMyAttendance() {

		StringRequest reqMarkMyAttendanceReq=new StringRequest(Method.POST,MarkMyMeetingAttendanceService.SERVICE_URL,new Listener<String>() {

			@Override
			public void onResponse(String response) {
				Methods.closeProgressDialog();
				Log.d("droid","get all reqMyMeetingList ---------------"+ response);

				Gson gson=new Gson();

				ResponseMessageModel modelResp=gson.fromJson(response, ResponseMessageModel.class);
				//Object meetingmsg=mMeetingModel.getMessage();
				if(null != modelResp.getStatus() && modelResp.getStatus().trim().length() >0){

					if(modelResp.getStatus()=="401"){
						Methods.longToast("User name or Password is incorrect", MarkMyAttendanceActivity.this);
					}else{
						Methods.longToast(modelResp.getMessage(), MarkMyAttendanceActivity.this);
					}
				}else{
					Methods.longToast(modelResp.getMessage(), MarkMyAttendanceActivity.this);	

				}
				Intent intent=new Intent();  
                intent.putExtra("Value",getMeetingCategory()); 
                intent.putExtra("Position_Clicked", mPosition_Clicked);
                setResult(2,intent);  
				finish();

			}
		},new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Methods.closeProgressDialog();
				Log.d("droid","get all zones error---------------"+ error.getCause());
				Methods.longToast("Some error occured,please try again later", MarkMyAttendanceActivity.this);
			}
		})
		{
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();

				MarkMyAttendanceReqModel model=new MarkMyAttendanceReqModel();
				model.setUsername(mPreferenceHelper.getString(Commons.USER_EMAILID));
				model.setUserpass(mPreferenceHelper.getString(Commons.USER_PASSWORD));

				ArrayList<RecordModel> newRecList=new ArrayList<MarkMyAttendanceReqModel.RecordModel>();
				RecordModel recModel=model.new RecordModel();
				Log.d("NonStop", "Name: " + Name);
				recModel.setName(Name);
				recModel.setPresent(getMeetingCategory());

				newRecList.add(recModel);
				model.setRecords(newRecList);

				Gson gson=new Gson();
				String dataString=gson.toJson(model, MarkMyAttendanceReqModel.class);

				Log.e("droid", dataString);
				params.put(MarkMyMeetingAttendanceService.DATA, dataString);
				return params; 
			}
		};

		App.getInstance().addToRequestQueue(reqMarkMyAttendanceReq, "reqMarkMyAttendanceReq");
		reqMarkMyAttendanceReq.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));

	}

	private String getMeetingCategory() {
		int id = grgpAttended.getCheckedRadioButtonId();
		switch (id) {
		case R.id.rbtnYes:
			return "1";
		case R.id.rbtnNo:
			return "0";

		default:
			break;
		}
		return "0";
	}

}
