package com.mutech.synergy.activities.event;

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
import com.mutech.synergy.SynergyValues.Web.MarkMyEventAttendanceService;
import com.mutech.synergy.models.MarkMyAttendanceReqModel;
import com.mutech.synergy.models.MarkMyAttendanceReqModel.RecordModel;
import com.mutech.synergy.models.ResponseMessageModel;
import com.mutech.synergy.utils.Methods;
import com.mutech.synergy.utils.NetworkHelper;
import com.mutech.synergy.utils.PreferenceHelper;

public class MarkMyEventAttendanceActivity extends ActionBarActivity implements OnClickListener{

	private EditText txtEventCode,txtEventName,txtVenue;
	private TextView txtFromDate,txtToDate;
	private RadioGroup grgpAttended;
	private RadioButton rbtnYes,rbtnNo;
	private SimpleDateFormat dateFormatter;
	private PreferenceHelper mPreferenceHelper;
	private DatePickerDialog fromDatePickerDialog,toDatePickerDialog;
	private String mName,mSubject,mEvent_date,mAddress,mComments,Eventcode,End_date;
	private int mPresent,mPosition_Clicked;
	private Button btnSaveEventDetails;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_eventattendance);
		initialize();
	}

	@SuppressLint("NewApi")
	private void initialize() {

		getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		getSupportActionBar().setHomeButtonEnabled(false);
		getSupportActionBar().setCustomView(R.layout.custom_actionbar);
		TextView tvTitle=(TextView)getSupportActionBar().getCustomView().findViewById(R.id.title_text);
		tvTitle.setText("Mark My Event Attendance");
		
		
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2E9AFE")));
		
		getSupportActionBar().setDisplayShowCustomEnabled(true);

		txtToDate=(TextView) findViewById(R.id.txtToDate);
		txtEventCode=(EditText) findViewById(R.id.txtEventCode);
		txtEventName=(EditText) findViewById(R.id.txtEventName);
		txtFromDate=(TextView) findViewById(R.id.txtFromDate);
		txtVenue=(EditText) findViewById(R.id.txtVenue);
		grgpAttended=(RadioGroup) findViewById(R.id.grgpAttended);
		rbtnYes=(RadioButton) findViewById(R.id.rbtnYes);
		rbtnNo=(RadioButton) findViewById(R.id.rbtnNo);
		btnSaveEventDetails=(Button) findViewById(R.id.btnSaveEventDetails);

		btnSaveEventDetails.setOnClickListener(this);

		mPreferenceHelper=new PreferenceHelper(this);
		dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

		Bundle bundle=getIntent().getExtras().getBundle("Bundle");
		if(null !=bundle){
			mName=bundle.getString("Name");
			mSubject=bundle.getString("Subject");
			mEvent_date=bundle.getString("Event_date");
			mAddress=bundle.getString("Address");
			Eventcode=bundle.getString("Eventcode");
			End_date=bundle.getString("End_date");
			mPresent=bundle.getInt("Present");
			mPosition_Clicked=bundle.getInt("Position_Clicked");

			Log.e("mPresent",mPresent+"");
			
			if(mPresent==1){
				rbtnYes.setChecked(true);
			}else{
				rbtnNo.setChecked(true);
				
			}
			
			
			txtToDate.setText(End_date);
			txtEventCode.setText(Eventcode);
			txtEventName.setText(mSubject);
			txtFromDate.setText(mEvent_date);
			txtVenue.setText(mAddress);	
			
			txtEventCode.setEnabled(false);
			txtEventName.setEnabled(false);
			txtFromDate.setEnabled(false);
			txtVenue.setEnabled(false);
			txtToDate.setEnabled(false);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnSaveEventDetails:
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

	private void markMyAttendance() {
		StringRequest reqMarkMyAttendanceReq=new StringRequest(Method.POST,MarkMyEventAttendanceService.SERVICE_URL,new Listener<String>() {

			@Override
			public void onResponse(String response) {
				Methods.closeProgressDialog();
				Log.d("droid","get all reqMyMeetingList ---------------"+ response);

				Gson gson=new Gson();

				ResponseMessageModel modelResp=gson.fromJson(response, ResponseMessageModel.class);
				//Object meetingmsg=mMeetingModel.getMessage();
				if(null != modelResp.getStatus() && modelResp.getStatus().trim().length() >0){

					if(modelResp.getStatus()=="401"){
						Methods.longToast("User name or Password is incorrect", MarkMyEventAttendanceActivity.this);
					}else{
						Methods.longToast(modelResp.getMessage(), MarkMyEventAttendanceActivity.this);
					}
				}else{
					Methods.longToast(modelResp.getMessage(), MarkMyEventAttendanceActivity.this);	

				}
				Intent intent=new Intent();  
				intent.putExtra("Value",getMeetingCategory()); 
				intent.putExtra("Position_Clicked", mPosition_Clicked);
				setResult(1,intent);  
				finish();

			}
		},new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Methods.closeProgressDialog();
				Log.d("droid","get all zones error---------------"+ error.getCause());
				Methods.longToast("Some error occured,please try again later", MarkMyEventAttendanceActivity.this);
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
				recModel.setName(mName);
				recModel.setPresent(getMeetingCategory());
				recModel.setComments(mComments);

				newRecList.add(recModel);
				model.setRecord(newRecList);

				Gson gson=new Gson();
				String dataString=gson.toJson(model, MarkMyAttendanceReqModel.class);

				Log.e("droid", dataString);
				params.put(MarkMyEventAttendanceService.DATA, dataString);
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
