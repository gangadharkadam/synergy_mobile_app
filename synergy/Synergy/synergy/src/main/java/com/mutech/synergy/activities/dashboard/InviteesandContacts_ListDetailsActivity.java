package com.mutech.synergy.activities.dashboard;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.mutech.databasedetails.CreateNewMemberActivity;
import com.mutech.synergy.App;
import com.mutech.synergy.R;
import com.mutech.synergy.R.id;
import com.mutech.synergy.R.layout;
import com.mutech.synergy.R.menu;
import com.mutech.synergy.SynergyValues.Commons;
import com.mutech.synergy.SynergyValues.Web.DashboardDataService;
import com.mutech.synergy.SynergyValues.Web.GetCellDetailsService;
import com.mutech.synergy.SynergyValues.Web.UpdateAllDetailsService;
import com.mutech.synergy.models.MeetingListRequestModel;
import com.mutech.synergy.models.ResponseMessageModel2;
import com.mutech.synergy.utils.InputValidation;
import com.mutech.synergy.utils.Methods;
import com.mutech.synergy.utils.NetworkHelper;
import com.mutech.synergy.utils.PreferenceHelper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class InviteesandContacts_ListDetailsActivity extends AppCompatActivity {
	
	String name,tblname;
	private PreferenceHelper mPreferenceHelper;
	private Gson gson;
	ImageView imgProfilePic;
	EditText txtnamenumber,txtinvitee_contact_name,txtMemberPhone1,txtEmailID1,txtInvitedby;
	TextView txtMemberDateOfBirth,txtdate_of_convert;
	Spinner txtMemberMartialInfo;
	TextView txtheadtitle;
	Button btnSaveMemberInfo;
	Spinner spConvertInvitee,spAgegroup,spSourceOfInvite,sptitle;
	private ArrayList<String> titleList,agegroupList,SourceofInvitationList;
	ArrayAdapter<String> adapterTitel,adapterAgeGroup,adpterSourceofInvitation;
	private DatePickerDialog birthDatePickerDialog;
	private SimpleDateFormat dateFormatter,dateFormatterService;
	CheckBox chkConvertInviteeContactToFt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inviteesand_contacts);
		
		chkConvertInviteeContactToFt=(CheckBox) findViewById(R.id.chkConvertInviteeContactToFt);
		spAgegroup=(Spinner) findViewById(R.id.spAgegroup);
		spSourceOfInvite=(Spinner) findViewById(R.id.spSourceOfInvite);
		sptitle=(Spinner) findViewById(R.id.sptitle);
		
		txtnamenumber=(EditText) findViewById(R.id.txtMembershipNo);
		
		txtinvitee_contact_name=(EditText) findViewById(R.id.txtMemberlName);
		txtMemberPhone1=(EditText) findViewById(R.id.txtMemberPhone1);
		txtdate_of_convert=(TextView) findViewById(R.id.txtDateOfConvert);
		txtEmailID1=(EditText) findViewById(R.id.txtEmailID1);
		txtInvitedby=(EditText) findViewById(R.id.txtInvitedby);
		txtMemberDateOfBirth=(TextView) findViewById(R.id.txtMemberDateOfBirth);
		txtheadtitle=(TextView)findViewById(R.id.textView1);
		btnSaveMemberInfo=(Button) findViewById(R.id.btnSaveMemberInfo);
		
		getSupportActionBar().hide();

		mPreferenceHelper=new PreferenceHelper(InviteesandContacts_ListDetailsActivity.this);
		name=getIntent().getStringExtra("name");
		tblname=getIntent().getStringExtra("tblename");
		txtheadtitle.setText(getIntent().getStringExtra("title"));
		
		Calendar newCalendar = Calendar.getInstance();
		dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
		dateFormatterService=new SimpleDateFormat("yyyy-MM-dd",Locale.US);
		
		birthDatePickerDialog = new DatePickerDialog(this, new OnDateSetListener() {

			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
				txtMemberDateOfBirth.setText(dateFormatter.format(newDate.getTime()));
			}

		},newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
		
		txtMemberDateOfBirth.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				birthDatePickerDialog.show();
				
			}
		});
		
		titleList=new ArrayList<String>();
		
	
		titleList.add("Bro");
		titleList.add("Sis");
		titleList.add("Dcn");
		titleList.add("Dcns");
		titleList.add("Pastor");
	
		adapterTitel = new ArrayAdapter<String>(InviteesandContacts_ListDetailsActivity.this, android.R.layout.simple_spinner_item, titleList);
		adapterTitel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sptitle.setAdapter(adapterTitel);
		
		agegroupList=new ArrayList<String>();
		
		agegroupList.add("0 - 12 Years");
		agegroupList.add("13 - 19 Years");
		agegroupList.add("20 - 25 Years");
		agegroupList.add("26 - 35 Years");
		agegroupList.add("35 - 45 Years");
		agegroupList.add("45 - 55 Years");
		agegroupList.add("56 - 65 Years");
		agegroupList.add("65 Years and Above");
		
		adapterAgeGroup = new ArrayAdapter<String>(InviteesandContacts_ListDetailsActivity.this, android.R.layout.simple_spinner_item, agegroupList);
		adapterAgeGroup.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spAgegroup.setAdapter(adapterAgeGroup);
		
		SourceofInvitationList=new ArrayList<String>();
		
		SourceofInvitationList.add("Personal Evangelism");
		SourceofInvitationList.add("Church/Cell Evangelism");
		SourceofInvitationList.add("Reach Out Campaign");
		SourceofInvitationList.add("Others");
		
		
		adpterSourceofInvitation = new ArrayAdapter<String>(InviteesandContacts_ListDetailsActivity.this, android.R.layout.simple_spinner_item, SourceofInvitationList);
		adpterSourceofInvitation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spSourceOfInvite.setAdapter(adpterSourceofInvitation);
		
		
		
		if(NetworkHelper.isOnline(this)){
			Methods.showProgressDialog(this);
			getDashboardDataService();
	
		}
		else
			Methods.longToast("Please connect to Internet", this);
		
	
		
		btnSaveMemberInfo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(NetworkHelper.isOnline(InviteesandContacts_ListDetailsActivity.this)){
					if(InputValidation.isPhoneNumber(txtMemberPhone1, false) &&
							InputValidation.hasText(txtinvitee_contact_name) &&
							InputValidation.hasText(txtInvitedby)) {
						Methods.showProgressDialog(InviteesandContacts_ListDetailsActivity.this);
						UpdateDashboardDataService();
					} else {
						new AlertDialog.Builder(InviteesandContacts_ListDetailsActivity.this)
								.setCancelable(false)
								.setTitle("Invalid Input")
								.setMessage("Please enter valid value in the field marked red")
								.setPositiveButton("OK", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialogInterface, int i) {

									}
								})
								.show();
					}
				}
				else
					Methods.longToast("Please connect to Internet", InviteesandContacts_ListDetailsActivity.this);
				
			}
		});
		
		
	}

	
private void getDashboardDataService() {
		StringRequest reqDashboard=new StringRequest(Method.POST,GetCellDetailsService.SERVICE_URL,new Listener<String>() {

			@Override
			public void onResponse(String response) {
				Methods.closeProgressDialog();
				Log.e("droid","get all NewConvertYeardetails----------"+ response);

				if(response.contains("status"))
				{
					ResponseMessageModel2 respModel=gson.fromJson(response, ResponseMessageModel2.class);
					if(respModel.getMessage().getStatus()=="401"){
						Methods.longToast("User name or Password is incorrect", InviteesandContacts_ListDetailsActivity.this);
					}else{
						Methods.longToast(respModel.getMessage().getMessage(), InviteesandContacts_ListDetailsActivity.this);
					}
				}else{
					
					try {
							JSONObject jsonobj=new JSONObject(response);
							JSONArray obj=jsonobj.getJSONArray("message");
							Log.d("NonStop","Response: " + obj.toString());
							txtnamenumber.setText(obj.getJSONObject(0).getString("name"));
							sptitle.setSelection(adapterTitel.getPosition(obj.getJSONObject(0).getString("title")));
							
							//txttitle.setText(obj.getJSONObject(0).getString("title"));
						
							
							
							txtinvitee_contact_name.setText(obj.getJSONObject(0).getString("invitee_contact_name"));

							Log.d("NonStop", "DOB Before: " + txtMemberDateOfBirth.getText().toString());
							if(!obj.getJSONObject(0).getString("date_of_birth").equals("null") && !obj.getJSONObject(0).getString("date_of_birth").equals("")){
								
								Date dob=dateFormatterService.parse(obj.getJSONObject(0).getString("date_of_birth"));
								Log.d("NonStop", "DOB After: " + dob.toString());
								txtMemberDateOfBirth.setText(dateFormatter.format(dob).toString());
							}

							if(!obj.getJSONObject(0).getString("phone_1").equals("null") && !obj.getJSONObject(0).getString("phone_1").equals("")){
								txtMemberPhone1.setText(obj.getJSONObject(0).getString("phone_1"));
							}

							txtEmailID1.setText(obj.getJSONObject(0).getString("email_id"));
							
							spAgegroup.setSelection(adapterAgeGroup.getPosition(obj.getJSONObject(0).getString("age_group")));
							
							
							
							String chk=obj.getJSONObject(0).getString("convert_invitee_contact_to_ft");
							if(chk.equals("1"))
							 chkConvertInviteeContactToFt.setChecked(true);
							else
								chkConvertInviteeContactToFt.setChecked(false);
							
							
						
							txtdate_of_convert.setText((obj.getJSONObject(0).getString("date_of_convert").equals("null"))?"":obj.getJSONObject(0).getString("date_of_convert"));
							//txtsource_of_invitation.setText(obj.getJSONObject(0).getString("source_of_invitation"));
							spSourceOfInvite.setSelection(adpterSourceofInvitation.getPosition(obj.getJSONObject(0).getString("source_of_invitation")));
							
							txtInvitedby.setText(obj.getJSONObject(0).getString("invited_by").equals("null")?"":obj.getJSONObject(0).getString("invited_by"));

						
						} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
						} catch (ParseException e) {
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
					
					if(tblname.equals("Invitees and Contacts"))
					  jsonobj.put("tbl", "Invitees and Contacts");
					else
					  jsonobj.put("tbl", "First Timer");
					
					jsonobj.put("name", name);
					
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				String dataString=jsonobj.toString();
			//	String dataString=gson.toJson(model, MeetingListRequestModel.class);

				Log.e("droid","data passed is ::::::::"+dataString);
				params.put(DashboardDataService.DATA, dataString);
				return params; 
			}
		};

		App.getInstance().addToRequestQueue(reqDashboard, "reqDashboard");
		reqDashboard.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
	}

private void UpdateDashboardDataService() {
	StringRequest reqDashboard=new StringRequest(Method.POST,UpdateAllDetailsService.SERVICE_URL,new Listener<String>() {

		@Override
		public void onResponse(String response) {
			Methods.closeProgressDialog();
			Log.e("droid Responce","get all NewConvertYeardetails----------"+ response);

			if(response.contains("status"))
			{
				
				try {
						JSONObject jsonobj=new JSONObject(response);
						String status=jsonobj.getJSONObject("message").getString("status");
						
						if(status.equals("200")){
							
							Methods.longToast(jsonobj.getJSONObject("message").getString("message"), InviteesandContacts_ListDetailsActivity.this);
							
						}else{
							
							Methods.longToast("Some error occured,please try again later", InviteesandContacts_ListDetailsActivity.this);
						}
						
						
					} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
					}
				
				}
			
			finish();
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
				
				if(tblname.equals("Invitees and Contacts"))
				  jsonobj.put("tbl", "Invitees and Contacts");
				else
				  jsonobj.put("tbl", "First Timer");
				
				
				JSONObject obj=new JSONObject();
				
				obj.put("name", name);
				
				obj.put("title", sptitle.getSelectedItem().toString());
				obj.put("invitee_contact_name", txtinvitee_contact_name.getText().toString());
				
				if(!txtMemberDateOfBirth.getText().toString().equals("")){
					
					Date dob=dateFormatter.parse(txtMemberDateOfBirth.getText().toString());
					obj.put("date_of_birth", dateFormatterService.format(dob));
				}
				
				obj.put("phone_1", txtMemberPhone1.getText().toString());
				obj.put("email_id", txtEmailID1.getText().toString());
				obj.put("age_group", spAgegroup.getSelectedItem().toString());
				
				if(chkConvertInviteeContactToFt.isChecked())
					obj.put("convert_invitee_contact_to_ft", "1");
				else
					obj.put("convert_invitee_contact_to_ft", "0");
			
				
				obj.put("date_of_convert", txtdate_of_convert.getText().toString());
				obj.put("source_of_invitation", spSourceOfInvite.getSelectedItem().toString());
				obj.put("invited_by", txtInvitedby.getText().toString());
				
				jsonobj.put("records", obj);
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			String dataString=jsonobj.toString();
		//	String dataString=gson.toJson(model, MeetingListRequestModel.class);

			Log.e("droid request","data passed is ::::::::"+dataString);
			params.put(DashboardDataService.DATA, dataString);
			return params; 
		}
	};

	App.getInstance().addToRequestQueue(reqDashboard, "reqDashboard");
	reqDashboard.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
}




}

	

