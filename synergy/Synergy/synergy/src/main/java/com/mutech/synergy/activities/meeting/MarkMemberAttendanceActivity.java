package com.mutech.synergy.activities.meeting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
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
import com.mutech.synergy.SynergyValues.Web.MarkMeetingMemberAllAttendanceService;
import com.mutech.synergy.SynergyValues.Web.PushNotificationOption;
import com.mutech.synergy.models.MarkMyAttendanceReqModel;
import com.mutech.synergy.models.MarkMyAttendanceReqModel.RecordModel;
import com.mutech.synergy.models.MeetingListRequestModel;
import com.mutech.synergy.models.MemberDetailsRespModel;
import com.mutech.synergy.models.MemberDetailsRespModel.MemberModel;
import com.mutech.synergy.models.ResponseMessageModel;
import com.mutech.synergy.models.StatusCheckModel;
import com.mutech.synergy.utils.Methods;
import com.mutech.synergy.utils.NetworkHelper;
import com.mutech.synergy.utils.PreferenceHelper;

public class MarkMemberAttendanceActivity extends ActionBarActivity implements OnCheckedChangeListener{

	private String mMeeting_name,mMeetingSubj,mMeetingtime;
	private TextView lblMeetingReason;
	private ListView lvMembers;
	private CheckBox chkMarkAll;
	private PreferenceHelper mPreferenceHelper;
	private MeetingListAdapter mMeetingListAdapter;
	private ArrayList<MemberModel> memModelList;
	private Gson gson;
	private CheckBox chkEmail, chkSms, chkPush;
	String status;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_markmemberattendance);
		initialize();
	}

	@SuppressLint("NewApi")
	private void initialize() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		getSupportActionBar().setHomeButtonEnabled(false);
		getSupportActionBar().setCustomView(R.layout.custom_actionbar);
		TextView tvTitle=(TextView)getSupportActionBar().getCustomView().findViewById(R.id.title_text);
		tvTitle.setText("Mark Members Attendance  ");
		
		
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2E9AFE")));
		
		getSupportActionBar().setDisplayShowCustomEnabled(true);

		lblMeetingReason=(TextView) findViewById(R.id.lblMeetingReason);
		lvMembers=(ListView) findViewById(R.id.lvMembers);
		chkMarkAll=(CheckBox) findViewById(R.id.chkMarkAll);
		chkMarkAll.setOnCheckedChangeListener(this);
		
//		chkEmail = (CheckBox) findViewById(R.id.chkEmail);
//		chkSms = (CheckBox) findViewById(R.id.chkSms);
//		chkPush = (CheckBox) findViewById(R.id.chkPush);

		mPreferenceHelper=new PreferenceHelper(this);
		gson=new Gson();

		Bundle bundle=getIntent().getExtras().getBundle("Bundle");
		if(null !=bundle){
			mMeeting_name=bundle.getString("Meeting_name");
			mMeetingSubj=bundle.getString("MeetingSubj");
			mMeetingtime=bundle.getString("Meetingtime");
		}

		lblMeetingReason.setText(mMeetingSubj);

		if (NetworkHelper.isOnline(this)) {
			Methods.showProgressDialog(this);
			getAllMeetingMembers();
		}else{
			Methods.longToast("Please connect to Internet", this);
		}
	}
	//row_meeting
	private void getAllMeetingMembers() {
		StringRequest reqMeetingList=new StringRequest(Method.POST,GetMeetingMemberListService.SERVICE_URL,new Listener<String>() {

			@Override
			public void onResponse(String response) {
				Methods.closeProgressDialog();
				Log.e("droid Responce","get all reqMeetingList ---------------"+ response);
				if(null !=response && response.length() > 0){

					StatusCheckModel statusCheckModel=gson.fromJson(response, StatusCheckModel.class);
					if(null != statusCheckModel.getStatus() && statusCheckModel.getStatus().trim().length() >0){
						ResponseMessageModel respModel=gson.fromJson(response, ResponseMessageModel.class);
						if(respModel.getStatus()=="401"){
							Methods.longToast("User name or Password is incorrect", MarkMemberAttendanceActivity.this);
						}else{
							Methods.longToast(respModel.getMessage(), MarkMemberAttendanceActivity.this);
						}
					}else{
						MemberDetailsRespModel model=gson.fromJson(response, MemberDetailsRespModel.class);
						if(null !=model.getStatus() && model.getStatus().trim().length() > 0){
							ResponseMessageModel modelResp=gson.fromJson(response, ResponseMessageModel.class);
							Methods.longToast(modelResp.getMessage(), MarkMemberAttendanceActivity.this);

						}else{
							memModelList=new ArrayList<MemberModel>();
							if(null !=model && null !=model.getMessage()){
								for(int i=0;i<model.getMessage().size();i++){
									MemberModel memModel=model.new MemberModel();

									memModel=model.getMessage().get(i);
									Log.d("droid", "memModel :::::"+memModel.getName());
									
									memModelList.add(i,memModel);
									
									Log.d("droid", "memModelList :::::"+memModelList.get(i).getName());
								}
							}
							mMeetingListAdapter=new MeetingListAdapter(MarkMemberAttendanceActivity.this, memModelList);
							lvMembers.setAdapter(mMeetingListAdapter);

						}
					}


				}
		

			}
		},new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Methods.closeProgressDialog();
				Log.d("droid","get all zones error---------------"+ error.getCause());
				Methods.longToast("Can't fetch members,please try again later", MarkMemberAttendanceActivity.this);
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

				Log.e("droid Request", dataString);
				params.put(GetAllMeetingService.DATA, dataString);
				return params; 
			}
		}
		;

		App.getInstance().addToRequestQueue(reqMeetingList, "reqMeetingList");
		reqMeetingList.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));

	}


	class MeetingListAdapter extends BaseAdapter{

		private Context mContext;
		private ArrayList<MemberModel> mList;
		private Holder holder;

		public MeetingListAdapter(Context context,
				ArrayList<MemberModel> memModel) {
			super();
			mContext=context;
			mList=new ArrayList<MemberModel>();
			mList=memModel;
		}

		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {

			LayoutInflater layout = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			holder = new Holder();
			if (convertView == null) {
				convertView = layout.inflate(R.layout.row_meeting, null);

				holder.btnMarkAttendance=(Button) convertView.findViewById(R.id.btnMarkAttendance);
				holder.lblMeetingsubject=(TextView) convertView.findViewById(R.id.lblMeetingsubject);
				holder.lblMeetingName=(TextView) convertView.findViewById(R.id.lblMeetingName);
				holder.lblMeetingTime=(TextView) convertView.findViewById(R.id.lblMeetingTime);
				holder.lblMeetingVenue=(TextView) convertView.findViewById(R.id.lblMeetingVenue);
				convertView.setTag(holder);
			}else{
				holder = (Holder) convertView.getTag();
			}

			holder.btnMarkAttendance.setTag(position);
			
			if(null !=mList.get(position).getMember_name())
				holder.lblMeetingName.setText(mList.get(position).getMember_name());
			
			if(null !=mList.get(position).getMeeting_subject())
				holder.lblMeetingsubject.setText(mList.get(position).getMeeting_subject());
			
//			if(null !=mList.get(position).getMeeting_date())
			Log.e(null, "time-"+mMeetingtime);
				holder.lblMeetingTime.setText(mMeetingtime);
				holder.lblMeetingVenue.setText(mList.get(position).getVenue());
			

		
				
		//	if(null !=mList.get(position).getPresent() && mList.get(position).getPresent()=="1"){
		
			 status=mList.get(position).getPresent();
			
			if(status.equals("1")){
				holder.btnMarkAttendance.setText("Marked Present\nClick to Change");
				holder.btnMarkAttendance.setBackgroundColor(getResources().getColor(R.color.green));
				//Marked Present/Click to Change
			}else{
				holder.btnMarkAttendance.setText("Marked Absent\nClick to Change");
				//Marked Absent/Click to Change
				holder.btnMarkAttendance.setBackgroundColor(getResources().getColor(R.color.red));
			}
		//	}
				holder.btnMarkAttendance.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					if (NetworkHelper.isOnline(MarkMemberAttendanceActivity.this)) {
						Methods.showProgressDialog(MarkMemberAttendanceActivity.this);
				
						int i=Integer.parseInt(v.getTag().toString());
						
						String st=mList.get(i).getPresent();
						
					//	if(null !=mList.get(position).getPresent() && mList.get(position).getPresent()=="1"){
						if(st.equals("1")){
							markSingleUserAttendance(position,0);//marking absent
						}else{
							markSingleUserAttendance(position,1);//marking present
//							pushnotification();
						}
					}else{
						Methods.longToast("Please connect to Internet", MarkMemberAttendanceActivity.this);
					}
				}
			});

			return convertView;
		}

	}

	public static class Holder{
		private TextView lblMeetingName,lblMeetingVenue,lblMeetingTime,lblMeetingsubject;
		private Button btnMarkAttendance;
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if(isChecked){
			if (NetworkHelper.isOnline(this)) {
				if(memModelList.size() >0){
					Methods.showProgressDialog(this);
//					markAllMemberAttendance();
					pushnotification();
				}
				else
					Methods.longToast("No members to mark attendance", this);
			}else{
				Methods.longToast("Please connect to Internet", this);
			}
		}
	}
	
	
	private void pushnotification(){
		final String Email,Sms, Push;
		Email="0";
		Sms="0";
		Push="0";
		
//		if(chkEmail.isChecked()){
//			Email="1";
//		}else{
//			Email="0";
//		}
//		if(chkSms.isChecked()){
//			Sms="1";
//		}else{
//			Sms="0";
//		}
//		if(chkPush.isChecked()){
//			Push="1";
//		}else{
//			Push="0";
//		}
 
		StringRequest reqMeetingList=new StringRequest(Method.POST,PushNotificationOption.SERVICE_URL,new Listener<String>() {

			@Override
			public void onResponse(String response) {
				Methods.closeProgressDialog();
				Log.d("droid","get all reqMeetingList ---------------"+ response);


				StatusCheckModel statusCheckModel=gson.fromJson(response, StatusCheckModel.class);
				if(null != statusCheckModel.getStatus() && statusCheckModel.getStatus().trim().length() >0){
					ResponseMessageModel respModel=gson.fromJson(response, ResponseMessageModel.class);
					if(respModel.getStatus()=="401"){
						Methods.longToast("User name or Password is incorrect", MarkMemberAttendanceActivity.this);
					}else{
						Methods.longToast(respModel.getMessage(), MarkMemberAttendanceActivity.this);
					}
				}else{
					ResponseMessageModel respModel=gson.fromJson(response, ResponseMessageModel.class);
					Methods.longToast(respModel.getMessage(), MarkMemberAttendanceActivity.this);	

					for(int i=0;i<memModelList.size();i++){
						MemberDetailsRespModel parentmodel=new MemberDetailsRespModel();
						MemberModel model=parentmodel.new MemberModel();
						model=memModelList.get(i);
						model.setPresent("1");
//						model.setEmail(Email);
//						model.setSms(Sms);
//						model.setPush(Push);
						memModelList.set(i, model);
					}
					mMeetingListAdapter.notifyDataSetChanged();
				}
			}
		},new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Methods.closeProgressDialog();
				Log.d("droid","get all zones error---------------"+ error.getCause());
				Methods.longToast("Some error occured,please try again later", MarkMemberAttendanceActivity.this);
			}
		})
		{
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();

				MarkMyAttendanceReqModel model=new MarkMyAttendanceReqModel();
				model.setUsername(mPreferenceHelper.getString(Commons.USER_EMAILID));
				model.setUserpass(mPreferenceHelper.getString(Commons.USER_PASSWORD));
				model.setEmail(Email);
				model.setSms(Sms);
				model.setPush(Push);

				ArrayList<RecordModel> newRecList=new ArrayList<RecordModel>();


				for(int i=0;i<memModelList.size();i++){
					RecordModel recModel=model.new RecordModel();
					recModel.setName(memModelList.get(i).getName());
					recModel.setPresent("1");
					Log.d("droid", "recModel ::: "+recModel.getName());
					newRecList.add(i,recModel);
					Log.d("droid", "newRecList ::: "+newRecList.get(i).getName());
				}

				model.setRecords(newRecList);

				String dataString=gson.toJson(model, MarkMyAttendanceReqModel.class);

				Log.d("droid", dataString);
				params.put(PushNotificationOption.DATA, dataString);

				return params;
			}
		}
		;

		App.getInstance().addToRequestQueue(reqMeetingList, "reqMeetingList");
		reqMeetingList.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
	}

	private void markAllMemberAttendance() {
		StringRequest reqMeetingList=new StringRequest(Method.POST,MarkMeetingMemberAllAttendanceService.SERVICE_URL,new Listener<String>() {

			@Override
			public void onResponse(String response) {
				Methods.closeProgressDialog();
				Log.d("droid","get all reqMeetingList ---------------"+ response);


				StatusCheckModel statusCheckModel=gson.fromJson(response, StatusCheckModel.class);
				if(null != statusCheckModel.getStatus() && statusCheckModel.getStatus().trim().length() >0){
					ResponseMessageModel respModel=gson.fromJson(response, ResponseMessageModel.class);
					if(respModel.getStatus()=="401"){
						Methods.longToast("User name or Password is incorrect", MarkMemberAttendanceActivity.this);
					}else{
						Methods.longToast(respModel.getMessage(), MarkMemberAttendanceActivity.this);
					}
				}else{
					ResponseMessageModel respModel=gson.fromJson(response, ResponseMessageModel.class);
					Methods.longToast(respModel.getMessage(), MarkMemberAttendanceActivity.this);	

					for(int i=0;i<memModelList.size();i++){
						MemberDetailsRespModel parentmodel=new MemberDetailsRespModel();
						MemberModel model=parentmodel.new MemberModel();
						model=memModelList.get(i);
						model.setPresent("1");
						memModelList.set(i, model);
					}
					mMeetingListAdapter.notifyDataSetChanged();
				}
			}
		},new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Methods.closeProgressDialog();
				Log.d("droid","get all zones error---------------"+ error.getCause());
				Methods.longToast("Some error occured,please try again later", MarkMemberAttendanceActivity.this);
			}
		})
		{
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();

				MarkMyAttendanceReqModel model=new MarkMyAttendanceReqModel();
				model.setUsername(mPreferenceHelper.getString(Commons.USER_EMAILID));
				model.setUserpass(mPreferenceHelper.getString(Commons.USER_PASSWORD));

				ArrayList<RecordModel> newRecList=new ArrayList<RecordModel>();


				for(int i=0;i<memModelList.size();i++){
					RecordModel recModel=model.new RecordModel();
					recModel.setName(memModelList.get(i).getName());
					recModel.setPresent("1");
					Log.d("droid", "recModel ::: "+recModel.getName());
					newRecList.add(i,recModel);
					Log.d("droid", "newRecList ::: "+newRecList.get(i).getName());
				}

				model.setRecords(newRecList);

				String dataString=gson.toJson(model, MarkMyAttendanceReqModel.class);

				Log.d("droid", dataString);
				params.put(MarkMeetingMemberAllAttendanceService.DATA, dataString);

				return params;
			}
		}
		;

		App.getInstance().addToRequestQueue(reqMeetingList, "reqMeetingList");
		reqMeetingList.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
	}

	protected void markSingleUserAttendance(final int position, final int attendanceVal) {
//		StringBuffer result = new StringBuffer();
//		result.append("IPhone check : ").append(chkEmail.isChecked());
//		result.append("\nAndroid check : ").append(chkSms.isChecked());
//		result.append("\nWindows Mobile check :").append(chkPush.isChecked());
		final String Email,Sms, Push;
		
//		if(chkEmail.isChecked()){
//			Email="1";
//		}else{
//			Email="0";
//		}
//		if(chkSms.isChecked()){
//			Sms="1";
//		}else{
//			Sms="0";
//		}
//		if(chkPush.isChecked()){
//			Push="1";
//		}else{
//			Push="0";
//		}
 
		
		// get all reqMeetingList ---------------{"message":"Updated Attendance","exc":"[\"((u'nikhil.k@indictranstech.com',),)\", \"[[u'M00000069', u'sagar.s@indictranstech.com']]\", \"[u'9890865260']\"]","_server_messages":"[\"['Status=0,success\\\\r\\\\n']\"]"}

		
		StringRequest reqMeetingList=new StringRequest(Method.POST,PushNotificationOption.SERVICE_URL,new Listener<String>() {

			@Override
			public void onResponse(String response) {
				Methods.closeProgressDialog();
				Log.d("droid","get all reqMeetingList ---------------"+ response);

				ResponseMessageModel modelResp=gson.fromJson(response, ResponseMessageModel.class);
				//Object meetingmsg=mMeetingModel.getMessage();
				if(null != modelResp.getStatus() && modelResp.getStatus().trim().length() >0){

					if(modelResp.getStatus()=="401"){
						Methods.longToast("User name or Password is incorrect", MarkMemberAttendanceActivity.this);
					}else{
						Methods.longToast(modelResp.getMessage(), MarkMemberAttendanceActivity.this);
					}
				}else{
					Methods.longToast(modelResp.getMessage(), MarkMemberAttendanceActivity.this);	
				
					MemberDetailsRespModel parentmodel=new MemberDetailsRespModel();
					
					MemberModel model=parentmodel.new MemberModel();
					model=memModelList.get(position);
					model.setPresent(String.valueOf(attendanceVal));
					memModelList.set(position, model);
					mMeetingListAdapter.notifyDataSetChanged();
					chkMarkAll.setChecked(false);
				}
			}
		},new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Methods.closeProgressDialog();
				Log.d("droid","get all zones error---------------"+ error.getCause());
				Methods.longToast("Some error occured,please try again later", MarkMemberAttendanceActivity.this);
			}
		})
		{
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();

				MarkMyAttendanceReqModel model=new MarkMyAttendanceReqModel();
				model.setUsername(mPreferenceHelper.getString(Commons.USER_EMAILID));
				model.setUserpass(mPreferenceHelper.getString(Commons.USER_PASSWORD));
				model.setEmail("0");
				model.setSms("0");
				model.setPush("0");

				ArrayList<RecordModel> newRecList=new ArrayList<MarkMyAttendanceReqModel.RecordModel>();
				RecordModel recModel=model.new RecordModel();

				recModel.setName(memModelList.get(position).getName());
				
				status=memModelList.get(position).getPresent();
				
				if(status.equals("1"))
					recModel.setPresent("0");
				else
					recModel.setPresent("1");
				//recModel.setPresent(String.valueOf(attendanceVal));
				newRecList.add(recModel);
				model.setRecords(newRecList);

				String dataString=gson.toJson(model, MarkMyAttendanceReqModel.class);

				Log.e("droid", dataString);
			
				params.put(PushNotificationOption.DATA, dataString);

				return params;
			}
		}
		;

		App.getInstance().addToRequestQueue(reqMeetingList, "reqMeetingList");
		reqMeetingList.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
	}
}
