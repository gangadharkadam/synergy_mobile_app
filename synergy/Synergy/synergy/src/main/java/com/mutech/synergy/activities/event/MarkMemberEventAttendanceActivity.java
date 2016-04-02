package com.mutech.synergy.activities.event;

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
import com.mutech.synergy.SynergyValues.Web.CreateCellMeetingService;
import com.mutech.synergy.SynergyValues.Web.GetEventMemberListService;
import com.mutech.synergy.SynergyValues.Web.MarkEventMemberAttendanceService;
import com.mutech.synergy.models.EventParticipantsModel;
import com.mutech.synergy.models.EventParticipantsModel.EventParticipantSubModel;
import com.mutech.synergy.models.MarkMyAttendanceReqModel;
import com.mutech.synergy.models.MarkMyAttendanceReqModel.RecordModel;
import com.mutech.synergy.models.MeetingListRequestModel;
import com.mutech.synergy.models.ResponseMessageModel;
import com.mutech.synergy.models.StatusCheckModel;
import com.mutech.synergy.utils.Methods;
import com.mutech.synergy.utils.NetworkHelper;
import com.mutech.synergy.utils.PreferenceHelper;

public class MarkMemberEventAttendanceActivity extends ActionBarActivity implements OnCheckedChangeListener{

	private String mEvent_name,mEventSubj,mEventTime;
	private TextView lblEventReason,lblEventReasonHeader;
	private ListView lvMembers;
	private CheckBox chkMarkAll;
	private PreferenceHelper mPreferenceHelper;
	private EventListAdapter mEventListAdapter;
	private ArrayList<EventParticipantSubModel> eventModelList ;
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
		tvTitle.setText("Mark Members Event Attendance");
		
		
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2E9AFE")));
		
		getSupportActionBar().setDisplayShowCustomEnabled(true);

		lblEventReason=(TextView) findViewById(R.id.lblMeetingReason);
		lblEventReasonHeader=(TextView) findViewById(R.id.lblMeetingReasonHeader);
		lblEventReason.setVisibility(View.GONE);
		lblEventReasonHeader.setVisibility(View.GONE);
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
			mEvent_name=bundle.getString("Event_name");
			mEventSubj=bundle.getString("EventSubj");
			mEventTime=bundle.getString("Event_date");
		}

		if (NetworkHelper.isOnline(this)) {
			Methods.showProgressDialog(this);
			getAllEventMembers();
		}else{
			Methods.longToast("Please connect to Internet", this);
		}
	}
	
	void servertest(){
		
		
		StringRequest request=new StringRequest(Method.POST, GetEventMemberListService.SERVICE_URL,new Listener<String>() {

			@Override
			public void onResponse(String response) {
				// TODO Auto-generated method stub
				
			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
	}
	

	private void getAllEventMembers() {
		StringRequest reqMeetingList=new StringRequest(Method.POST,GetEventMemberListService.SERVICE_URL,new Listener<String>() {

			@Override
			public void onResponse(String response) {
				Methods.closeProgressDialog();
				Log.e("droid responce","get all reqMeetingList ---------------"+ response);

				StatusCheckModel statusCheckModel=gson.fromJson(response, StatusCheckModel.class);
				if(null != statusCheckModel.getStatus() && statusCheckModel.getStatus().trim().length() >0){
					ResponseMessageModel respModel=gson.fromJson(response, ResponseMessageModel.class);
					if(respModel.getStatus()=="401"){
						Methods.longToast("User name or Password is incorrect", MarkMemberEventAttendanceActivity.this);
					}else{
						Methods.longToast(respModel.getMessage(), MarkMemberEventAttendanceActivity.this);
					}
				}else{
					EventParticipantsModel model=gson.fromJson(response, EventParticipantsModel.class);

					if(null !=model.getStatus() && model.getStatus().trim().length() > 0){
						ResponseMessageModel modelResp=gson.fromJson(response, ResponseMessageModel.class);
						Methods.longToast(modelResp.getMessage(), MarkMemberEventAttendanceActivity.this);

					}else{
						eventModelList = new ArrayList<EventParticipantSubModel>();
						if(null !=model && null !=model.getMessage() && model.getMessage().size() > 0){
							for(int i=0;i<model.getMessage().size();i++){
								EventParticipantSubModel eventSubModel=model.new EventParticipantSubModel();
								eventSubModel=model.getMessage().get(i);
								eventModelList.add(i,eventSubModel);
							}
						}
						mEventListAdapter=new EventListAdapter(MarkMemberEventAttendanceActivity.this, eventModelList);
						lvMembers.setAdapter(mEventListAdapter);

					}
				}


			}
		},new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Methods.closeProgressDialog();
				Log.d("droid","get all zones error---------------"+ error.getCause());
				Methods.longToast("Can't fetch members,please try again later", MarkMemberEventAttendanceActivity.this);
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

				Log.e("droid request", dataString);
				params.put(GetEventMemberListService.DATA, dataString);
				return params; 
			}
		}
		;

		App.getInstance().addToRequestQueue(reqMeetingList, "reqMeetingList");
		reqMeetingList.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if(isChecked){
			if (NetworkHelper.isOnline(this)) {
				if(eventModelList.size() >0){
					Methods.showProgressDialog(this);
					markAllParticipantsAttendance();
				}
				else
					Methods.longToast("No members to mark attendance", this);
			}else{
				Methods.longToast("Please connect to Internet", this);
			}
		}
	}

	class EventListAdapter extends BaseAdapter{

		private Context mContext;
		private ArrayList<EventParticipantSubModel> mList;
		private Holder holder;

		public EventListAdapter(Context context,
				ArrayList<EventParticipantSubModel> memModel) {
			mContext=context;
			mList=new ArrayList<EventParticipantSubModel>();
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
				//lblMeetingsubject
				
				holder.btnMarkAttendance=(Button) convertView.findViewById(R.id.btnMarkAttendance);
				holder.lblEventName=(TextView) convertView.findViewById(R.id.lblMeetingName);
				holder.lblMeetingsubject=(TextView) convertView.findViewById(R.id.lblMeetingsubject);
				holder.lblEventTime=(TextView) convertView.findViewById(R.id.lblMeetingTime);
				holder.lblEventVenue=(TextView) convertView.findViewById(R.id.lblMeetingVenue);
				convertView.setTag(holder);
			}else{
				holder = (Holder) convertView.getTag();
			}
						
			holder.lblEventTime.setVisibility(View.GONE);
			
			holder.btnMarkAttendance.setTag(position);
			
			if(null !=mList.get(position).getName())
				holder.lblEventName.setText(mList.get(position).getName());
			
			if(null !=mList.get(position).getMember_name())
				holder.lblMeetingsubject.setText(mList.get(position).getMember_name());
			
			if(null !=mList.get(position).getComments())
				holder.lblEventVenue.setText(mList.get(position).getComments());
		
			
			
			

			/*if(null !=String.valueOf(mList.get(position).getPresent()) && mList.get(position).getPresent()==1){
				holder.btnMarkAttendance.setText("Mark Absent");
			
			
			}else{
				holder.btnMarkAttendance.setText("Mark Present");
			}
			*/
			
				
			if(null !=String.valueOf(mList.get(position).getPresent()) && mList.get(position).getPresent()==1){
					holder.btnMarkAttendance.setText("Marked Present\nClick to Change");
					holder.btnMarkAttendance.setBackgroundColor(getResources().getColor(R.color.green));
					//Marked Present/Click to Change
				}else{
					holder.btnMarkAttendance.setText("Marked Absent\nClick to Change");
					//Marked Absent/Click to Change
					holder.btnMarkAttendance.setBackgroundColor(getResources().getColor(R.color.red));
				}
			
			

			holder.btnMarkAttendance.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					
					int i=Integer.parseInt(v.getTag().toString());

					if (NetworkHelper.isOnline(MarkMemberEventAttendanceActivity.this)) {
						Methods.showProgressDialog(MarkMemberEventAttendanceActivity.this);
						if(null !=String.valueOf(mList.get(i).getPresent()) && mList.get(i).getPresent()==1){
							markSingleParticipantAttendance(i,0);		
						}else{
							markSingleParticipantAttendance(i,1);			
						}
					}else{
						Methods.longToast("Please connect to Internet", MarkMemberEventAttendanceActivity.this);
					}



				}
			});
			return convertView;
		}
	}

	public static class Holder{
		private TextView lblEventName,lblEventVenue,lblEventTime,lblMeetingsubject;
		private Button btnMarkAttendance;
	}

	private void markAllParticipantsAttendance() {
		final String Email,Sms, Push;
		Email="0";
		Push="0";
		Sms="0";
		
		
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
		
		StringRequest reqEventMarkAttnList=new StringRequest(Method.POST,MarkEventMemberAttendanceService.SERVICE_URL,new Listener<String>() {

			@Override
			public void onResponse(String response) {
				Methods.closeProgressDialog();
				Log.e("droid","get all reqMeetingList ---------------"+ response);

				ResponseMessageModel modelResp=gson.fromJson(response, ResponseMessageModel.class);
				//Object meetingmsg=mMeetingModel.getMessage();
				if(null != modelResp.getStatus() && modelResp.getStatus().trim().length() >0){
					if(modelResp.getStatus()=="401"){
						Methods.longToast("User name or Password is incorrect", MarkMemberEventAttendanceActivity.this);
					}else{
						Methods.longToast(modelResp.getMessage(), MarkMemberEventAttendanceActivity.this);
					}
				}else{
					Methods.longToast(modelResp.getMessage(), MarkMemberEventAttendanceActivity.this);	

					//for UI purpose
					for(int i=0;i<eventModelList.size();i++){
						EventParticipantsModel parentmodel=new EventParticipantsModel();
						EventParticipantSubModel model=parentmodel.new EventParticipantSubModel();
						model=eventModelList.get(i);
						model.setPresent(1);
						eventModelList.set(i, model);
					}
					mEventListAdapter.notifyDataSetChanged();
					chkMarkAll.setChecked(false);		
				}
			}
		},new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Methods.closeProgressDialog();
				Log.d("droid","get all zones error---------------"+ error.getCause());
				Methods.longToast("Some error occured,please try again later", MarkMemberEventAttendanceActivity.this);
			}
		})
		{
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();

				MarkMyAttendanceReqModel reqModel=new MarkMyAttendanceReqModel();
				ArrayList<RecordModel> recModelList=new ArrayList<MarkMyAttendanceReqModel.RecordModel>();

				reqModel.setUsername(mPreferenceHelper.getString(Commons.USER_EMAILID));
				reqModel.setUserpass(mPreferenceHelper.getString(Commons.USER_PASSWORD));
				reqModel.setEmail(Email);
				reqModel.setSms(Sms);
				reqModel.setPush(Push);

				for(int i=0;i<eventModelList.size();i++){
					RecordModel recModel=reqModel.new RecordModel();
					recModel.setName(eventModelList.get(i).getName());
					recModel.setPresent("1");
					recModel.setComments(eventModelList.get(i).getComments());
					recModel.setPerson_name(eventModelList.get(i).getPerson_name());
					recModelList.add(recModel);
				}
				reqModel.setRecord(recModelList);

				String dataString=gson.toJson(reqModel,MarkMyAttendanceReqModel.class);

				Log.e("Request", "dataString ::::::::::::"+dataString);

				params.put(CreateCellMeetingService.DATA, dataString);

				return params;
			}
		}
		;

		App.getInstance().addToRequestQueue(reqEventMarkAttnList, "reqEventMarkAttnList");
		reqEventMarkAttnList.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
	}


	private void markSingleParticipantAttendance(final int position, final int attendanceVal) {
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
//		
		
		StringRequest reqEventMarkAttnList=new StringRequest(Method.POST,MarkEventMemberAttendanceService.SERVICE_URL,new Listener<String>() {

			@Override
			public void onResponse(String response) {
				Methods.closeProgressDialog();
				Log.d("droid","get all reqMeetingList ---------------"+ response);

				ResponseMessageModel modelResp=gson.fromJson(response, ResponseMessageModel.class);
				//Object meetingmsg=mMeetingModel.getMessage();
				if(null != modelResp.getStatus() && modelResp.getStatus().trim().length() >0){
					if(modelResp.getStatus()=="401"){
						Methods.longToast("User name or Password is incorrect", MarkMemberEventAttendanceActivity.this);
					}else{
						Methods.longToast(modelResp.getMessage(), MarkMemberEventAttendanceActivity.this);
					}
				}else{

					Methods.longToast(modelResp.getMessage(), MarkMemberEventAttendanceActivity.this);

					Methods.longToast(modelResp.getMessage(), MarkMemberEventAttendanceActivity.this);	
					EventParticipantsModel parentmodel=new EventParticipantsModel();
					EventParticipantSubModel model=parentmodel.new EventParticipantSubModel();
					model=eventModelList.get(position);
					model.setPresent(attendanceVal);
					eventModelList.set(position, model);
					mEventListAdapter.notifyDataSetChanged();

				}
			}
		},new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Methods.closeProgressDialog();
				Log.d("droid","get all zones error---------------"+ error.getCause());
				Methods.longToast("Some error occured,please try again later", MarkMemberEventAttendanceActivity.this);
			}
		})
		{
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();


				MarkMyAttendanceReqModel reqModel=new MarkMyAttendanceReqModel();
				ArrayList<RecordModel> recModelList=new ArrayList<MarkMyAttendanceReqModel.RecordModel>();

				reqModel.setUsername(mPreferenceHelper.getString(Commons.USER_EMAILID));
				reqModel.setUserpass(mPreferenceHelper.getString(Commons.USER_PASSWORD));
				reqModel.setEmail("0");
				reqModel.setSms("0");
				reqModel.setPush("0");


				RecordModel recModel=reqModel.new RecordModel();
				recModel.setName(eventModelList.get(position).getName());
				recModel.setPresent(String.valueOf(attendanceVal));
				recModel.setComments(eventModelList.get(position).getComments());
				recModel.setPerson_name(eventModelList.get(position).getPerson_name());
				recModelList.add(recModel);

				reqModel.setRecord(recModelList);

				String dataString=gson.toJson(reqModel,MarkMyAttendanceReqModel.class);

				Log.e("droid Request", "dataString ::::::::::::"+dataString);

				params.put(CreateCellMeetingService.DATA, dataString);

				return params;
			}
		};

		App.getInstance().addToRequestQueue(reqEventMarkAttnList, "reqEventMarkAttnList");
		reqEventMarkAttnList.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
	}


}
