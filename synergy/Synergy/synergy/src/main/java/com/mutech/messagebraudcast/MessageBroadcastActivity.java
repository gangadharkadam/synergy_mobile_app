package com.mutech.messagebraudcast;

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
import com.google.gson.Gson;
import com.mutech.synergy.App;
import com.mutech.synergy.R;
import com.mutech.synergy.SynergyValues.Commons;
import com.mutech.synergy.SynergyValues.Web.MessageBoardCast;
import com.mutech.synergy.SynergyValues.Web.SendBoardCast;
import com.mutech.synergy.activities.HomeActivity;
import com.mutech.synergy.activities.LoginActivity;
import com.mutech.synergy.activities.LogoutActivity;
import com.mutech.synergy.activities.cellMasters.MasterSelectorScreenActivity;
import com.mutech.synergy.activities.cellMasters.PartnerShipRecord;
import com.mutech.synergy.activities.cellMasters.SearchFunctionActivity;
import com.mutech.synergy.activities.event.EventListActivity;
import com.mutech.synergy.activities.event.MyEventListActivity;
import com.mutech.synergy.activities.meeting.MyMeetingListActivity;
import com.mutech.synergy.activities.profile.MyProfileActivity;
import com.mutech.synergy.activities.task.ToDoTaskActivity;
import com.mutech.synergy.adapters.CustomDrawerAdapter;
import com.mutech.synergy.models.AllMembersResponseModel;
import com.mutech.synergy.models.DrawerItem;
import com.mutech.synergy.models.MeetingListRequestModel;
import com.mutech.synergy.models.ResponseMessageModel;
import com.mutech.synergy.models.ResponseMessageModel2;
import com.mutech.synergy.models.StatusCheckModel;
import com.mutech.synergy.models.AllMembersResponseModel.AllMemSubResponseModel;
import com.mutech.synergy.utils.Methods;
import com.mutech.synergy.utils.NetworkHelper;
import com.mutech.synergy.utils.PreferenceHelper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MessageBroadcastActivity extends ActionBarActivity {
	
	ListAdapter listadapter;
	private ArrayList<DrawerItem> mDrawerList;
	private CustomDrawerAdapter mCustomDrawerAdapter;
	private ListView mLvDrawer;
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	private PreferenceHelper mPreferenceHelper;
	private Gson gson;
	private ArrayList<String> ListOption;
	ArrayAdapter<String> adapter;
	private Spinner OptionSpinner;
	Button btnsend;
	private EditText msgTxt;
	private ListView lvAllMembers;
	private ArrayList<String> mMembersList;
	private ArrayList<AllMemSubResponseModel> subModelList;
	String message,type,recipents;
	String msg,Selecteditem; 
	private CheckBox chkEmail, chkSms, chkPush;
	String Email,Sms,Push;
	private CheckBox SelectedAll; 
	CheckBox cbBuy;
	String str;
	SparseBooleanArray mChecked = new SparseBooleanArray();

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message_broadcast);
		
		OptionSpinner=(Spinner) findViewById(R.id.spselectuser);
		
		ListOption=new ArrayList<String>();
		btnsend=(Button) findViewById(R.id.send);
		
		msgTxt=(EditText) findViewById(R.id.txtmessage);
		chkEmail = (CheckBox) findViewById(R.id.chkemail);
		chkSms = (CheckBox) findViewById(R.id.chksms);
		chkPush = (CheckBox) findViewById(R.id.chkpush);
		
//		SelectedAll = (CheckBox) findViewById(R.id.chkMarkAll);
//		
//		SelectedAll.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//		
//                for (int i = 0; i < adapter.getCount(); i++) {
//                    mChecked.put(i, SelectedAll.isChecked());
//                }            				
//			}
//		});

		
		
		btnsend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				message = msgTxt.getText().toString();			
				
				if(chkEmail.isChecked()){
					Email="1";
				}else{
					Email="0";
				}
				if(chkSms.isChecked()){
					Sms="1";
				}else{
					Sms="0";
				}
				if(chkPush.isChecked()){
					Push="1";
				}else{
					Push="0";
				}
							
				 String result = "";
			        for ( AllMemSubResponseModel p : listadapter.getBox()) {
			          if (p.isVal()){
			        	  result = result+p.getEmail_id()+",";  			        	
			          }
			        }
			        recipents = result;
			        Log.e(null, "res-"+result);
			        Log.e(null, "msg-"+message);
//			        Toast.makeText(MessageBroadcastActivity.this, result, Toast.LENGTH_LONG).show();
			       if(Email=="1"||Sms=="1"||Push=="1"){
			    	   if(!result.equals("") && !message.equals("")){
			    	  
			    		//  Toast.makeText(MessageBroadcastActivity.this, "Message sent successfully", Toast.LENGTH_LONG).show();
			    	
			    		   if(NetworkHelper.isOnline(MessageBroadcastActivity.this)){
			    		   Methods.showProgressDialog(MessageBroadcastActivity.this); 
			    		   sendInfo() ;
			    		   }else{
			    			   Methods.longToast("Please connect to Internet",MessageBroadcastActivity.this);   
			    		   }
			    	   }else{
			    		   Toast.makeText(MessageBroadcastActivity.this, "Mandetory Fields Message,Message Mode,Users", Toast.LENGTH_LONG).show();
			    	   }
			       }else{
			    	Toast.makeText(MessageBroadcastActivity.this, "Mandetory Fields Message,Message Mode,Users", Toast.LENGTH_LONG).show();
			       }
			      }			
		});
		
	
		lvAllMembers=(ListView) findViewById(R.id.lvMembers);
//		lvAllMembers.setOnItemClickListener(this);
		mMembersList=new ArrayList<String>();
		
		mDrawerList = new ArrayList<DrawerItem>();
		addDrawerListData();

		mLvDrawer = (ListView) findViewById(R.id.left_drawer);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		mCustomDrawerAdapter = new CustomDrawerAdapter(this,R.layout.custom_dashboard_drawer_item, mDrawerList);
		mLvDrawer.setAdapter(mCustomDrawerAdapter);
		mLvDrawer.setOnItemClickListener(new DrawerItemClickListener());
		
		mPreferenceHelper=new PreferenceHelper(this);
		
		 str=mPreferenceHelper.getString(Commons.USER_ROLE);
		
		gson=new Gson();

		mDrawerToggle = new ActionBarDrawerToggle(
				this,
				mDrawerLayout,
				R.drawable.dashboard, 
				R.string.app_name, 
				R.string.app_name 
				) {

			/** Called when a drawer has settled in a completely closed state. */
			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
				//getSupportActionBar().setTitle(mTitle);
			}

			/** Called when a drawer has settled in a completely open state. */
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				//getSupportActionBar().setTitle(mDrawerTitle);
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setCustomView(R.layout.custom_actionbar);
		getSupportActionBar().setHomeAsUpIndicator(R.drawable.actiontop);

//		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//		getSupportActionBar().setHomeAsUpIndicator(R.drawable.actiontop);
//		getSupportActionBar().setIcon(
//				   new ColorDrawable(getResources().getColor(android.R.color.transparent)));
//		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//		getSupportActionBar().setHomeButtonEnabled(true);
//		getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon);
//		getSupportActionBar().setCustomView(R.layout.custom_actionbar);
		TextView tvTitle=(TextView)getSupportActionBar().getCustomView().findViewById(R.id.title_text);
	
		tvTitle.setText("Broadcast Message    ");
		
		
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2E9AFE")));
		getSupportActionBar().setDisplayShowCustomEnabled(true);

		setSpinners();
		getMemberList();
	}

	
	
		private void setSpinners() {
			ListOption.add("ALL Leader");
			ListOption.add("FT");
			ListOption.add("Member");
			
			adapter = new ArrayAdapter<String>(MessageBroadcastActivity.this, android.R.layout.simple_spinner_item, ListOption);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			OptionSpinner.setAdapter(adapter);
			
			OptionSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			    @Override
			    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
			    	if(position==0){
			    		Selecteditem = "ALL leader";
			    		getMemberList();
			    	}else if(position==1){
			    		Selecteditem = "FT";
			    		getMemberList();
			    	}else{
			    		Selecteditem = "Member";
			    		getMemberList();
			    	}
			    }

			    @Override
			    public void onNothingSelected(AdapterView<?> parentView) {
			        // your code here
			    }

			});	
		}
	
	
	
private void getMemberList() {

	StringRequest reqGetMembers=new StringRequest(Method.POST,MessageBoardCast.SERVICE_URL,new Listener<String>() {
		@Override
		public void onResponse(String response) {
			Methods.closeProgressDialog();
			Log.d("droid","getMemberList ---------------"+ response);

			if(response.contains("status")){
				ResponseMessageModel2 resp2Model=gson.fromJson(response, ResponseMessageModel2.class);
				Methods.longToast(resp2Model.getMessage().getMessage(), MessageBroadcastActivity.this);
			}else{
				AllMembersResponseModel model=gson.fromJson(response, AllMembersResponseModel.class);
				if(null !=model.getMessage() && model.getMessage().size() >0){
					subModelList=new ArrayList<AllMemSubResponseModel>();
					for(int i=0;i<model.getMessage().size();i++){
						subModelList.add(model.getMessage().get(i));
						Log.d("droid", "subModelList :::::::"+model.getMessage().get(i));
					}
					Log.d("droid", "subModelList final size ::: "+subModelList.size());
					listadapter=new ListAdapter(MessageBroadcastActivity.this,subModelList);
					lvAllMembers.setAdapter(listadapter);
				}
			}

		}
	},new ErrorListener() {
		@Override
		public void onErrorResponse(VolleyError error) {
			Methods.closeProgressDialog();
			Log.d("droid","get all pcf error---------------"+ error.getCause());
		}
	})
	{
		@Override
		protected Map<String, String> getParams() throws AuthFailureError {
			Map<String, String> params = new HashMap<String, String>();

			MeetingListRequestModel model=new MeetingListRequestModel();
			model.setUsername(mPreferenceHelper.getString(Commons.USER_EMAILID));
			model.setUserpass(mPreferenceHelper.getString(Commons.USER_PASSWORD));
			model.setTbl(Selecteditem);
			

			String dataString=gson.toJson(model, MeetingListRequestModel.class);

			Log.d("droid","data passed is ::::::::"+dataString);
			params.put(MessageBoardCast.DATA, dataString);
			return params; 
		}
	};
	App.getInstance().addToRequestQueue(reqGetMembers, "reqGetMembers");
	reqGetMembers.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
}








private void sendInfo() {
	StringRequest reqGetProfile=new StringRequest(Method.POST,SendBoardCast.SERVICE_URL,new Listener<String>() {

		@Override
		public void onResponse(String response) {
			Methods.closeProgressDialog();
			Log.e("Responce droid","get reqGetProfile ---------------"+ response);

			StatusCheckModel statusCheckModel=gson.fromJson(response, StatusCheckModel.class);
		
			if(null != statusCheckModel.getStatus() && statusCheckModel.getStatus().trim().length() >0){
				ResponseMessageModel respModel=gson.fromJson(response, ResponseMessageModel.class);
				if(respModel.getStatus()=="401"){
					Methods.longToast("User name or Password is incorrect", MessageBroadcastActivity.this);
				}
				
				if(respModel.getStatus()=="402"){
					
					Methods.longToast(respModel.getMessage(), MessageBroadcastActivity.this);
				}
				
			}else{
				
				Methods.longToast("Message Send Successfully. ", MessageBroadcastActivity.this);
//			
				//PartnerShipModel mPartnerModel=gson.fromJson(response, PartnerShipModel.class);
//				if(null !=mPartnerModel.getMessage() && mPartnerModel.getMessage().size() >0){
//
//					mPartnerSubModel=mPartnerModel.getMessage();
//
//					for(int i=0;i<mPartnerSubModel.size();i++){
//						PartnershipArms.setText(mPartnerSubModel.get(i).getpartnership_arms());
//						Ministry.setText(mPartnerSubModel.get(i).getministry_year());
//						Date.setText(mPartnerSubModel.get(i).getdate());
//						Amount.setText(mPartnerSubModel.get(i).getamount());
//						Giving_Pledge.setText(mPartnerSubModel.get(i).getgiving_or_pledge());
//			
//					}
//				}else{
//				}
			}	

		}
	},new ErrorListener() {

		@Override
		public void onErrorResponse(VolleyError error) {
			Methods.closeProgressDialog();
			Log.d("droid","get reqsaveMeeting error---------------"+ error.getCause());

			if(error.networkResponse.statusCode==403){
				Methods.longToast("No access to update Profile", MessageBroadcastActivity.this);
			}
			else
				Methods.longToast("Some Error Occured,please try again later", MessageBroadcastActivity.this);
		}
	}){
		@Override
		protected Map<String, String> getParams() throws AuthFailureError{
			Map<String, String> params = new HashMap<String, String>();

			MeetingListRequestModel model=new MeetingListRequestModel();
			model.setUsername(mPreferenceHelper.getString(Commons.USER_EMAILID));
			model.setUserpass(mPreferenceHelper.getString(Commons.USER_PASSWORD));
		
			if(Email.equals("1"))			
			model.setEmail(Email);
			
			if(Sms.equals("1"))
			model.setSms(Sms);
			
			if(Push.equals("1"))
			model.setPush(Push);
			
			
			model.setMessage(message);
			model.setRecipents(recipents);

			String dataString=gson.toJson(model, MeetingListRequestModel.class);

			Log.e("Request droid", dataString);
			params.put(SendBoardCast.DATA, dataString);
			return params;
		}
	};

	App.getInstance().addToRequestQueue(reqGetProfile, "reqGetProfile");
	reqGetProfile.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));		
}

  
private void addDrawerListData() {
	DrawerItem item01 = new DrawerItem();
	item01.setItemName("Dashboard");
	item01.setImgResID(R.drawable.dashboard);		

	DrawerItem item05 = new DrawerItem();
	item05.setItemName("My Profile");
	item05.setImgResID(R.drawable.myprofile);

//	DrawerItem item03 = new DrawerItem();
//	item03.setItemName("Ministry \n Material");
//	item03.setImgResID(R.drawable.ministry_materials);

	DrawerItem item03 = new DrawerItem();
	item03.setItemName("Partnership \n Records");
	item03.setImgResID(R.drawable.partnership_record);

	DrawerItem item04 = new DrawerItem();
	item04.setItemName("Database");
	item04.setImgResID(R.drawable.database);


	DrawerItem item06 = new DrawerItem();
	item06.setItemName("Search");
	item06.setImgResID(R.drawable.search);

	DrawerItem item07 = new DrawerItem();
	item07.setItemName("Attendance");
	item07.setImgResID(R.drawable.my_meetings);

	DrawerItem item08 = new DrawerItem();
	item08.setItemName("Calendar");
	item08.setImgResID(R.drawable.carlender);

	DrawerItem item9 = new DrawerItem();
	item9.setItemName("To Do");
	item9.setImgResID(R.drawable.todo);
	
	DrawerItem item10 = new DrawerItem();
	item10.setItemName("Broadcast Message");
	item10.setImgResID(R.drawable.msg);

	DrawerItem item11=new DrawerItem();
	item11.setItemName("Logout");
	item11.setImgResID(R.drawable.signout);

	mDrawerList.add(item01);
	mDrawerList.add(item05);
	mDrawerList.add(item04);
//	mDrawerList.add(item02);
//	mDrawerList.add(item03);
	mDrawerList.add(item03);	
	
//	mDrawerList.add(item06);
	mDrawerList.add(item07);
	mDrawerList.add(item08);
	mDrawerList.add(item9);
	mDrawerList.add(item10);
	mDrawerList.add(item06);
	mDrawerList.add(item11);
}

@Override
public boolean onOptionsItemSelected(MenuItem item) {
	if (mDrawerToggle.onOptionsItemSelected(item)) {
		return true;
	}

	return super.onOptionsItemSelected(item);
}

private void selectItem(int position) {
	switch (position) {
	case 0:
		
		Intent int1=new Intent(this,HomeActivity.class);
		int1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivity(int1);
		finish();
		break;
	case 2:	
		Intent intForm=new Intent(this,MasterSelectorScreenActivity.class);
		intForm.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivity(intForm);
		finish();
		break;
//	case 2:
//		break;
//	case 3:
//		break;
	case 3:
		Intent partner=new Intent(this,PartnerShipRecord.class);
		partner.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivity(partner);
		finish();
		break;
	case 1:
		Intent intForm1=new Intent(this,MyProfileActivity.class);
		intForm1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivity(intForm1);
		finish();
		break;
	
	case 4:
		//Intent intMeeting=new Intent(this,MeetingListActivity.class);
		//startActivity(intMeeting);
		Intent intMyMeetings=new Intent(this,MyMeetingListActivity.class);
		intMyMeetings.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivity(intMyMeetings);
		finish();
		break;
	case 5:
		Intent intEvents=new Intent(this,MyEventListActivity.class);
		intEvents.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivity(intEvents);
		finish();
		break;
	case 6:
		Intent intentTODO = new Intent(this, ToDoTaskActivity.class);
		intentTODO.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivity(intentTODO);
		finish();
		break;
		
	case 7:
//		Intent intentMsg = new Intent(this, MessageBroadcastActivity.class);
//		startActivity(intentMsg);
		break;
		
	case 8:
		Intent intSearchMembers=new Intent(this,SearchFunctionActivity.class);
		intSearchMembers.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivity(intSearchMembers);
		finish();
		break;
		
	case 9://logout
//		mPreferenceHelper.addBoolean(Commons.ISUSER_LOGGEDIN, false);
//		mPreferenceHelper.addString(Commons.USER_EMAILID, null);
//		mPreferenceHelper.addString(Commons.USER_PASSWORD, null);

		Intent intLogout=new Intent(this,LogoutActivity.class);
	//	finish();
		startActivity(intLogout);
		break;


	default:
		break;
	}

	mDrawerLayout.closeDrawer(mLvDrawer);
//	getSupportActionBar().invalidateOptionsMenu();
}


	private class DrawerItemClickListener implements
	ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);

		}
	}
	
	
	public class ListAdapter extends BaseAdapter {
	    Context ctx;
	    LayoutInflater lInflater;
	    ArrayList<AllMemSubResponseModel> objects;
	    int positiontemp;

	    ListAdapter(Context context, ArrayList<AllMemSubResponseModel> products) {
	        ctx = context;
	        objects = products;
	        lInflater = (LayoutInflater) ctx
	                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    }

	    @Override
	    public int getCount() {
	        return objects.size();
	    }

	    @Override
	    public Object getItem(int position) {
	        return objects.get(position);
	    }

	    @Override
	    public long getItemId(int position) {
	        return position;
	    }

	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	    	positiontemp = position;
	        View view = convertView;
	        if (view == null) {
	            view = lInflater.inflate(R.layout.row_messageboard, parent, false);
	        }

	        AllMemSubResponseModel p = getProduct(position);

	        ((TextView) view.findViewById(R.id.txtTitle)).setText(p.getFtv_name());
	        
	        ((TextView) view.findViewById(R.id.txtId)).setText(p.getName());	        
	      
	        cbBuy = (CheckBox) view.findViewById(R.id.chkbox);
	        cbBuy.setOnCheckedChangeListener(myCheckChangList);
	        cbBuy.setTag(position);
	        cbBuy.setChecked(p.isVal());	        
	        return view;
	        	        
	    }

	    AllMemSubResponseModel getProduct(int position) {
	        return ((AllMemSubResponseModel) getItem(position));
	    }

	    ArrayList<AllMemSubResponseModel> getBox() {
	        ArrayList<AllMemSubResponseModel> box = new ArrayList<AllMemSubResponseModel>();
	        for (AllMemSubResponseModel p : objects) {
	            if (p.isVal())
	                box.add(p);
	        }
	        return box;
	    }

	    OnCheckedChangeListener myCheckChangList = new OnCheckedChangeListener() {
	        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
	            getProduct((Integer)buttonView.getTag()).setVal(isChecked);
          
	           }	
	    };
   
	}

	public void onBackPressed() {
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		if (drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START);
		} else {
			new AlertDialog.Builder(this)
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setTitle("Closing Activity")
					.setMessage("Are you sure you want to exit?")
					.setPositiveButton("Yes", new DialogInterface.OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialog, int which) {
							moveTaskToBack(true);
						}

					})
					.setNegativeButton("No", null)
					.show();
		}
	}

}
