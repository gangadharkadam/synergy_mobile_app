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
import com.mutech.synergy.SynergyValues;
import com.mutech.synergy.SynergyValues.Commons;
import com.mutech.synergy.SynergyValues.Web.DashboardDataService;
import com.mutech.synergy.SynergyValues.Web.GetCellDetailsService;
import com.mutech.synergy.SynergyValues.Web.UpdateAllDetailsService;
import com.mutech.synergy.SynergyValues.Web.UpdateInvitesContactsDetailsService;
import com.mutech.synergy.SynergyValues.Web.GetInvitesContactsDetailsService;
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
	EditText txtnamenumber,txtinvitee_contact_name,txtinvitee_surname,txtMemberPhone1,txtEmailID1;
//	EditText txtInvitedby;
	TextView txtMemberDateOfBirth,txtdate_of_convert;
	Spinner txtMemberMartialInfo;
	TextView txtheadtitle;
	Button btnSaveMemberInfo;
	Spinner spConvertInvitee,spAgegroup,spSourceOfInvite,sptitle, spInvitedBy;
	private ArrayList<String> titleList,agegroupList,SourceofInvitationList;
	ArrayAdapter<String> adapterTitel,adapterAgeGroup,adpterSourceofInvitation;
	private DatePickerDialog birthDatePickerDialog;
	private SimpleDateFormat dateFormatter,dateFormatterService;
	CheckBox chkConvertInviteeContactToFt;

    private ArrayList<String> invitedByAL;
    ArrayAdapter<String> invitedByAdap;
    JSONObject jsonobj, respJSON;
    JSONArray jsonarray;
    public int TOTAL_LIST_ITEMS;

	public boolean isValid() {

		if(!InputValidation.hasText(txtinvitee_contact_name)) {
			AlertDialog dialog = new AlertDialog.Builder(InviteesandContacts_ListDetailsActivity.this)
					.setCancelable(false)
					.setTitle("Mandatory Input")
					.setMessage("Please enter Invitee/Contact First Name")
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface, int i) {

						}
					})
					.show();
			TextView textView = (TextView) dialog.findViewById(android.R.id.message);
			textView.setTextSize(18);
			return false;
		}
		if(!InputValidation.spnHasText(spInvitedBy, "Invited By")) {
			AlertDialog dialog = new AlertDialog.Builder(InviteesandContacts_ListDetailsActivity.this)
					.setCancelable(false)
					.setTitle("Mandatory Input")
					.setMessage("Please enter 'Invited By'")
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface, int i) {

						}
					})
					.show();
			TextView textView = (TextView) dialog.findViewById(android.R.id.message);
			textView.setTextSize(18);
			return false;
		}
		if(!InputValidation.isPhoneNumber(txtMemberPhone1, false)) {
			AlertDialog dialog = new AlertDialog.Builder(InviteesandContacts_ListDetailsActivity.this)
					.setCancelable(false)
					.setTitle("Invalid Input")
					.setMessage("Please enter a valid phone number")
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface, int i) {

						}
					})
					.show();
			TextView textView = (TextView) dialog.findViewById(android.R.id.message);
			textView.setTextSize(18);
			return false;
		}

		if(!InputValidation.hasText(txtinvitee_surname)) {
			AlertDialog dialog = new AlertDialog.Builder(InviteesandContacts_ListDetailsActivity.this)
					.setCancelable(false)
					.setTitle("Mandatory Input")
					.setMessage("Please enter Invitee/Contact Surname")
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface, int i) {

						}
					})
					.show();
			TextView textView = (TextView) dialog.findViewById(android.R.id.message);
			textView.setTextSize(18);
			return false;
		}
		return true;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inviteesand_contacts);
		
		chkConvertInviteeContactToFt=(CheckBox) findViewById(R.id.chkConvertInviteeContactToFt);
		spAgegroup=(Spinner) findViewById(R.id.spAgegroup);
		spSourceOfInvite=(Spinner) findViewById(R.id.spSourceOfInvite);
		sptitle=(Spinner) findViewById(R.id.sptitle);
		spInvitedBy = (Spinner) findViewById(id.spInvitedby);

		txtnamenumber=(EditText) findViewById(R.id.txtMembershipNo);
		
		txtinvitee_contact_name=(EditText) findViewById(R.id.txtMemberfName);
		txtinvitee_surname=(EditText) findViewById(R.id.txtMemberSurname);

		txtMemberPhone1=(EditText) findViewById(R.id.txtMemberPhone1);
		txtdate_of_convert=(TextView) findViewById(R.id.txtDateOfConvert);
		txtEmailID1=(EditText) findViewById(R.id.txtEmailID1);
//		txtInvitedby=(EditText) findViewById(R.id.txtInvitedby);
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

        invitedByAL = new ArrayList<String>();
        if(NetworkHelper.isOnline(this)){
            Methods.showProgressDialog(this);
            Log.d("NonStop", "Getting Member list");
            getListNew("Member", "1", "", "", "", "", "", "", "", "", "");
//            Log.d("NonStop", "Setting Invited By dropdown. AL size: " + invitedByAL.size());
//            invitedByAdap = new ArrayAdapter<String>(InviteesandContacts_ListDetailsActivity.this, android.R.layout.simple_spinner_item, invitedByAL);
//            invitedByAdap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            spInvitedBy.setAdapter(invitedByAdap);
//            Log.d("NonStop", "Zeroth Entry: " + spInvitedBy.getSelectedItem());
            Methods.closeProgressDialog();
        }
        else
            Methods.longToast("Please connect to Internet", this);

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

		SourceofInvitationList.add("");
		SourceofInvitationList.add("Personal Evangelism");
		SourceofInvitationList.add("Church/Cell Evangelism");
		SourceofInvitationList.add("Reach Out Campaign");
		SourceofInvitationList.add("Others");
		
		
		adpterSourceofInvitation = new ArrayAdapter<String>(InviteesandContacts_ListDetailsActivity.this, android.R.layout.simple_spinner_item, SourceofInvitationList);
		adpterSourceofInvitation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spSourceOfInvite.setAdapter(adpterSourceofInvitation);
		
		
		
//		if(NetworkHelper.isOnline(this)){
//			Methods.showProgressDialog(this);
//			getDashboardDataService();
//
//		}
//		else
//			Methods.longToast("Please connect to Internet", this);
		
	
		
		btnSaveMemberInfo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(NetworkHelper.isOnline(InviteesandContacts_ListDetailsActivity.this)){
                    if(isValid()) {
                        Methods.showProgressDialog(InviteesandContacts_ListDetailsActivity.this);
                        UpdateDashboardDataService();
                    }
				}
				else
					Methods.longToast("Please connect to Internet", InviteesandContacts_ListDetailsActivity.this);
			}
		});
	}

    private void getListNew(final String tbl,final String pageno,final String resion,final String zone,final String gchurch,final String church,final String pcf,final String srcell,final String cell,final String fdate,final String todate){

        //	StringRequest reqgetLowerHierarchy=new StringRequest(Method.POST,GetAllMastersService.SERVICE_URL,new Listener<String>() {
        StringRequest reqgetLowerHierarchy=new StringRequest(Method.POST, SynergyValues.Web.GetAllListMastersService.SERVICE_URL,new Listener<String>() {

            @Override
            public void onResponse(String response) {
                Methods.closeProgressDialog();
                Log.e("droid get reqResponce ---------------", response);
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

                        jsonobj=new JSONObject(response);
                        jsonobj.getJSONObject("message");

                        int i=Integer.parseInt(jsonobj.getJSONObject("message").getString("total_count"));

                        TOTAL_LIST_ITEMS=Integer.parseInt(jsonobj.getJSONObject("message").getString("total_count"));

                        jsonarray=jsonobj.getJSONObject("message").getJSONArray("records");

                        invitedByAL = new ArrayList<String>();
                        for(int respCnt = 0; respCnt < jsonarray.length(); respCnt++) {
                            String respName = jsonarray.getJSONObject(respCnt).getString("name");
                            invitedByAL.add(respName);
                            Log.d("NonStop", "Added " + respName + " to ArrayList");
                            Log.d("NonStop", "Setting Invited By dropdown. AL size: " + invitedByAL.size());
                            Log.d("NonStop", "Zeroth Entry: " + spInvitedBy.getSelectedItem());
                            invitedByAdap = new ArrayAdapter<String>(InviteesandContacts_ListDetailsActivity.this, android.R.layout.simple_spinner_item, invitedByAL);
                            invitedByAdap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spInvitedBy.setAdapter(invitedByAdap);
                        }

                        getDashboardDataService();

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        },new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Methods.closeProgressDialog();
                Log.d("droid","get reqgetLowerHierarchy error---------------"+ error.getCause());

                if(error!=null) {
                    if(error.networkResponse.statusCode==403){
                        //	Methods.longToast("Access Denied", CreateSeniorCellMasterActivity.this);
                    }
                }
                //else
                //	Methods.longToast("Some Error Occured,please try again later", CreateSeniorCellMasterActivity.this);

            }

        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                Map<String, String> params = new HashMap<String, String>();

			    /*model=new MeetingListRequestModel();
				model.setUsername(mPreferenceHelper.getString(Commons.USER_EMAILID));
				model.setUserpass(mPreferenceHelper.getString(Commons.USER_PASSWORD));
				model.setTbl(tbl);
		//		model.setRecord_name(tbl);
				model.setPage_no(pageno);
			*/
                //model.setName(mPreferenceHelper.getString(Commons.USER_DEFVALUE));
                try{

                    jsonobj=new JSONObject();

                    jsonobj.put("username", mPreferenceHelper.getString(Commons.USER_EMAILID));
                    jsonobj.put("userpass", mPreferenceHelper.getString(Commons.USER_PASSWORD));

                    jsonobj.put("tbl",tbl);
                    jsonobj.put("page_no",pageno);


                    JSONObject jsonfilter=new JSONObject();

                    if(!resion.equals(""))
                        jsonfilter.put("region", resion);

                    if(!zone.equals(""))
                        jsonfilter.put("zone", zone);

                    if(!gchurch.equals(""))
                        jsonfilter.put("group_church", gchurch);

                    if(!church.equals(""))
                        jsonfilter.put("church", church);

                    if(!pcf.equals(""))
                        jsonfilter.put("pcf", pcf);

                    if(!srcell.equals(""))
                        jsonfilter.put("senior_cell", srcell);

                    if(!cell.equals(""))
                        jsonfilter.put("cell", cell);

                    if(!fdate.equals(""))
                        jsonfilter.put("from_date", fdate);

                    if(!todate.equals(""))
                        jsonfilter.put("to_date", todate);


                    jsonobj.put("filters", jsonfilter);

                }catch(Exception ex){

                }

                String dataString=jsonobj.toString();//gson.toJson(model, MeetingListRequestModel.class);

                Log.e("Request droid", dataString);
                params.put(SynergyValues.Web.GetHigherHierarchyService.DATA, dataString);
                return params;
            }
        };

        App.getInstance().addToRequestQueue(reqgetLowerHierarchy, "reqgetLowerHierarchy");
        reqgetLowerHierarchy.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
    }

    private void getDashboardDataService() {
		StringRequest reqDashboard=new StringRequest(Method.POST,GetInvitesContactsDetailsService.SERVICE_URL,new Listener<String>() {

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
                        Log.d("NonStop", "Response: " + obj.toString());
							txtnamenumber.setText(obj.getJSONObject(0).getString("name"));
							sptitle.setSelection(adapterTitel.getPosition(obj.getJSONObject(0).getString("title")));
							
							//txttitle.setText(obj.getJSONObject(0).getString("title"));


							txtinvitee_contact_name.setText(obj.getJSONObject(0).getString("invitee_contact_name"));

						if(!obj.getJSONObject(0).getString("surname").equals("null") && !obj.getJSONObject(0).getString("surname").equals("")){
						    txtinvitee_surname.setText(obj.getJSONObject(0).getString("surname"));}

							Log.d("NonStop", "DOB Before: " + txtMemberDateOfBirth.getText().toString());
							if(!obj.getJSONObject(0).getString("date_of_birth").equals("null") && !obj.getJSONObject(0).getString("date_of_birth").equals("")){
								
								Date dob=dateFormatterService.parse(obj.getJSONObject(0).getString("date_of_birth"));
								Log.d("NonStop", "DOB After: " + dob.toString());
								txtMemberDateOfBirth.setText(dateFormatter.format(dob).toString());
							}

							if(!obj.getJSONObject(0).getString("phone_1").equals("null") && !obj.getJSONObject(0).getString("phone_1").equals("")){
								txtMemberPhone1.setText(obj.getJSONObject(0).getString("phone_1"));
							}

							Log.d("NonStop", "Printing email id");
							if(!obj.getJSONObject(0).getString("email_id").equals("null") && !obj.getJSONObject(0).getString("email_id").equals("")){
								txtEmailID1.setText(obj.getJSONObject(0).getString("email_id"));
							}

							spAgegroup.setSelection(adapterAgeGroup.getPosition(obj.getJSONObject(0).getString("age_group")));

							String chk=obj.getJSONObject(0).getString("convert_invitee_contact_to_ft");
							if(chk.equals("1"))
							 chkConvertInviteeContactToFt.setChecked(true);
							else
								chkConvertInviteeContactToFt.setChecked(false);

							txtdate_of_convert.setText((obj.getJSONObject(0).getString("date_of_convert").equals("null")) ? "" : obj.getJSONObject(0).getString("date_of_convert"));
							//txtsource_of_invitation.setText(obj.getJSONObject(0).getString("source_of_invitation"));
							spSourceOfInvite.setSelection(adpterSourceofInvitation.getPosition(obj.getJSONObject(0).getString("source_of_invitation")));

                            Log.d("NonStop", "Invited By AL item: " + invitedByAL.size());
                            Log.d("NonStop", "Invited by: " + obj.getJSONObject(0).getString("invited_by"));
							spInvitedBy.setSelection(invitedByAdap.getPosition(obj.getJSONObject(0).getString("invited_by")));

//							txtInvitedby.setText(obj.getJSONObject(0).getString("invited_by").equals("null")?"":obj.getJSONObject(0).getString("invited_by"));

						
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
	StringRequest reqDashboard=new StringRequest(Method.POST,UpdateInvitesContactsDetailsService.SERVICE_URL,new Listener<String>() {

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

                obj.put("surname", txtinvitee_surname.getText().toString());
				
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
                obj.put("invited_by", spInvitedBy.getSelectedItem().toString());
//				obj.put("invited_by", txtInvitedby.getText().toString());
				
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

	

