package com.mutech.partnershiprecord;

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
import com.mutech.partnershiprecord.MyPartnershipGivingActivity.GivingListAdapter;
import com.mutech.synergy.App;
import com.mutech.synergy.R;
import com.mutech.synergy.R.id;
import com.mutech.synergy.R.layout;
import com.mutech.synergy.R.menu;
import com.mutech.synergy.SynergyValues;
import com.mutech.synergy.SynergyValues.Commons;
import com.mutech.synergy.SynergyValues.ImageUrl;
import com.mutech.synergy.SynergyValues.Web.CreatePartnershipRecord;
import com.mutech.synergy.SynergyValues.Web.GetAllTasksService;
import com.mutech.synergy.SynergyValues.Web.GetMemberProfileService;
import com.mutech.synergy.SynergyValues.Web.Getgetpartnershiparms;
import com.mutech.synergy.SynergyValues.Web.Getcurrency;
import com.mutech.synergy.SynergyValues.Web.Partnership_giving_or_pledge;
import com.mutech.synergy.activities.event.CreateEventActivity;
import com.mutech.synergy.activities.profile.MyProfileActivity;
import com.mutech.synergy.models.MeetingListRequestModel;
import com.mutech.synergy.models.MemberProfileModel;
import com.mutech.synergy.utils.InputValidation;
import com.mutech.synergy.utils.Methods;
import com.mutech.synergy.utils.NetworkHelper;
import com.mutech.synergy.utils.PreferenceHelper;
import com.squareup.picasso.Picasso;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CreatePartnershipActivity extends AppCompatActivity {
	
	EditText txtMinistryYear,txtPartnercategory,txtmember,txtamount,txtgivingtype,txtInstrumentNo,txtInstrumentdate,txtbankname,txtbranch,txtConversation_Rate;
	TextView txtdate;
	Spinner txtGiving_Pledge,spplagetype,spPartnershipArm,spCurrency;
	
	Button btnPartnerSave;
	private Gson gson;
	LinearLayout pledgelayout,givinglayout;
	private DatePickerDialog fromDatePickerDialog,toDatePickerDialog;
	private SimpleDateFormat dateFormatter,dateFormatterService;
	private PreferenceHelper mPreferenceHelper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_partnership);
		
		//txtPartnershipArms=(EditText) findViewById(R.id.txtPartnershipArms);
		
		getSupportActionBar().hide();
		spPartnershipArm=(Spinner) findViewById(R.id.spPartnershipArm);
        spCurrency=(Spinner) findViewById(R.id.spCurrency);

		txtMinistryYear=(EditText) findViewById(R.id.txtMinistryYear);
		txtPartnercategory=(EditText) findViewById(R.id.txtPartnercategory);
		txtmember=(EditText) findViewById(R.id.txtmember);
		txtdate=(TextView) findViewById(R.id.txtdate);
		txtamount=(EditText) findViewById(R.id.txtamount);
		txtgivingtype=(EditText) findViewById(R.id.txtgivingtype);
		txtInstrumentdate=(EditText) findViewById(R.id.txtInstrumentdate);
		txtInstrumentNo=(EditText) findViewById(R.id.txtInstrumentNo);
		txtbankname=(EditText) findViewById(R.id.txtbankname);
		txtbranch=(EditText) findViewById(R.id.txtbranch);
        txtConversation_Rate=(EditText) findViewById(R.id.txtConversation_Rate);
		
		btnPartnerSave=(Button) findViewById(R.id.btnPartnerSave);
		
		spplagetype=(Spinner) findViewById(R.id.spplagetype);
		txtGiving_Pledge=(Spinner) findViewById(R.id.txtGiving_Pledge);
		pledgelayout=(LinearLayout) findViewById(R.id.pledge);
		givinglayout=(LinearLayout) findViewById(R.id.giving);
		
		txtPartnercategory.setEnabled(false);
		txtmember.setEnabled(false);
		txtGiving_Pledge.setEnabled(false);
		
		mPreferenceHelper=new PreferenceHelper(CreatePartnershipActivity.this);
		
		pledgelayout.setVisibility(View.GONE);
		givinglayout.setVisibility(View.GONE);
		
		dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
		dateFormatterService=new SimpleDateFormat("yyyy-MM-dd");
		Calendar newCalendar = Calendar.getInstance();
		
		Calendar newDate = Calendar.getInstance();
		newDate.set(newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
		txtdate.setText(dateFormatter.format(newDate.getTime()));
		
		gson=new Gson();
		
	
		fromDatePickerDialog = new DatePickerDialog(this, new OnDateSetListener() {

			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
				txtdate.setText(dateFormatter.format(newDate.getTime()));
			}

		},newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

		
		txtdate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				fromDatePickerDialog.show();
				
			}
		});
		
		btnPartnerSave.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
//				if(validatefield()){
                if (NetworkHelper.isOnline(CreatePartnershipActivity.this)) {
                    if(isValid()) {
        				if(validatefield()) {
                            Methods.showProgressDialog(CreatePartnershipActivity.this);
                            getPledge();
                        }
                    }
                } else {
                    Methods.longToast("Please connect to Internet", CreatePartnershipActivity.this);
                }
//				}
			}
		});
		
		if (NetworkHelper.isOnline(CreatePartnershipActivity.this)) {
			Methods.showProgressDialog(CreatePartnershipActivity.this);

            getCurrency();
            getProfileInfo();
			getPartnershiparms();
			
		} else {
			Methods.longToast("Please connect to Internet", CreatePartnershipActivity.this);
		}
		
	
		
		ArrayList<String> typeList=new ArrayList<String>();
	//	typeList.add("Giving");
		typeList.add("Pledge");
		
		ArrayList<String> pledgeList=new ArrayList<String>();
		pledgeList.add("Monthly");
		pledgeList.add("Quarterly");
		
		pledgeList.add("Half Yearly");
		pledgeList.add("Yearly");
		
		

		ArrayAdapter<String> adapterPriority = new ArrayAdapter<String>(CreatePartnershipActivity.this, android.R.layout.simple_spinner_item, typeList);
		adapterPriority.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		txtGiving_Pledge.setAdapter(adapterPriority);
		
		ArrayAdapter<String> adapterpledgeList = new ArrayAdapter<String>(CreatePartnershipActivity.this, android.R.layout.simple_spinner_item, pledgeList);
		adapterpledgeList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spplagetype.setAdapter(adapterpledgeList);
		
		txtGiving_Pledge.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				String str=txtGiving_Pledge.getSelectedItem().toString();
			
				if(str.equals("Giving")){
					givinglayout.setVisibility(View.VISIBLE);
					pledgelayout.setVisibility(View.GONE);
				}else{
					pledgelayout.setVisibility(View.VISIBLE);
					givinglayout.setVisibility(View.GONE);
				}
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {	}
			
		});
		
	}

	public boolean isValid() {

		if(!InputValidation.spnHasText(spPartnershipArm, "Partnership ARM")) {
			AlertDialog dialog =new AlertDialog.Builder(CreatePartnershipActivity.this)
					.setCancelable(false)
					.setTitle("Mandatory field cannot be blank")
					.setMessage("Please enter Partnership ARM")
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
		if(!InputValidation.hasText(txtamount)) {
			AlertDialog dialog =new AlertDialog.Builder(CreatePartnershipActivity.this)
					.setCancelable(false)
					.setTitle("Mandatory field cannot be blank")
					.setMessage("Please enter an amount")
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

		if(!InputValidation.spnHasText(spCurrency, "Currency")) {
			AlertDialog dialog =new AlertDialog.Builder(CreatePartnershipActivity.this)
					.setCancelable(false)
					.setTitle("Mandatory field cannot be blank")
					.setMessage("Please select currency")
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
	private void getPledge() {

		StringRequest reqTasksList=new StringRequest(Method.POST,CreatePartnershipRecord.SERVICE_URL,new Listener<String>() {

			@Override
			public void onResponse(String response) {
				Methods.closeProgressDialog();
				Log.e("droid Respnce","Create Partnership record-----"+ response);
				
				try {
					
					JSONObject obj=new JSONObject(response);
					String str=obj.getString("message");
					
					Log.e("responve", str);
						
					Methods.smallToast("Record Created successfully..", CreatePartnershipActivity.this);
					PledgeActivity.makepladge=true;
					finish();
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		},new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Methods.closeProgressDialog();
				Log.d("droid","get all tasks error MyTaskFragment---------------"+ error.getCause());


			}
		})
		{
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();

				JSONObject jsonobj=new JSONObject();
				try {
					jsonobj.put("member_name",txtPartnercategory.getText().toString());
					jsonobj.put("username", mPreferenceHelper.getString(Commons.USER_EMAILID));
					jsonobj.put("userpass", mPreferenceHelper.getString(Commons.USER_PASSWORD));
					jsonobj.put("partnership_arms",spPartnershipArm.getSelectedItem().toString());
				//	jsonobj.put("ministry_year",txtMinistryYear.getText().toString());
					
					String dobtism=dateFormatterService.format(dateFormatter.parse(txtdate.getText().toString()));
					jsonobj.put("date", dobtism);
					
					jsonobj.put("member",txtmember.getText().toString());
					jsonobj.put("amount",txtamount.getText().toString());
					if(txtConversation_Rate.getText().toString().equals("")){}else{
                    jsonobj.put("conversation_rate",Integer.parseInt(txtConversation_Rate.getText().toString()));}
					jsonobj.put("currency",spCurrency.getSelectedItem().toString());
					jsonobj.put("giving_or_pledge",txtGiving_Pledge.getSelectedItem().toString());
					
					String str=txtGiving_Pledge.getSelectedItem().toString();
					
					if(str.equals("Giving")){
						
						jsonobj.put("giving_type",txtgivingtype.getText().toString());
						jsonobj.put("instrument_no",txtInstrumentNo.getText().toString());
						jsonobj.put("instrument_date",txtInstrumentdate.getText().toString());
						jsonobj.put("bank_name",txtbankname.getText().toString());
						jsonobj.put("branch",txtbranch.getText().toString());
						
					}else{
						
						jsonobj.put("type_of_pledge",spplagetype.getSelectedItem().toString());
					}
					
					
				
					
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			//{"amount":"20000","member":"M00000069","username":"sagar.s@indictranstech.com","partnership_arms":"ncbxhf","ministry_year":"2015","userpass":"password","giving_or_pledge":"Pledge","type_of_pledge":"Monthly"}
				
				String dataString=jsonobj.toString();//gson.toJson(model, MeetingListRequestModel.class);

				Log.e("droid Request", dataString);
				params.put(GetAllTasksService.DATA, dataString);
				return params; 
			}
		}
		;

		App.getInstance().addToRequestQueue(reqTasksList, "reqTasksList");
		reqTasksList.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));			
	}
	
	private void getProfileInfo() {
		StringRequest reqGetProfile=new StringRequest(Method.POST,GetMemberProfileService.SERVICE_URL,new Listener<String>() {

			@Override
			public void onResponse(String response) {
				Methods.closeProgressDialog();
				Log.e("droid","get reqGetProfile ---------------"+ response);
			
				MemberProfileModel mProfModel=gson.fromJson(response, MemberProfileModel.class);
				//Object meetingmsg=mMeetingModel.getMessage();

				if(null !=mProfModel.getMessage() && mProfModel.getMessage().size() >0){

					try {
						JSONObject obj=new JSONObject(response);
						JSONArray jsonoarray=obj.getJSONArray("message");
						txtmember.setText(jsonoarray.getJSONObject(0).getString("name"));
						txtPartnercategory.setText(jsonoarray.getJSONObject(0).getString("member_name"));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	
				}

				//	}



			}
		},new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Methods.closeProgressDialog();
				Log.d("droid","get reqsaveMeeting error---------------"+ error.getCause());

				if(error.networkResponse.statusCode==403){
					Methods.longToast("No access to update Profile", CreatePartnershipActivity.this);
				}
				else
					Methods.longToast("Some Error Occured,please try again later", CreatePartnershipActivity.this);
			}
		}){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError{
				Map<String, String> params = new HashMap<String, String>();

				MeetingListRequestModel model=new MeetingListRequestModel();
				model.setUsername(mPreferenceHelper.getString(Commons.USER_EMAILID));
				model.setUserpass(mPreferenceHelper.getString(Commons.USER_PASSWORD));

				String dataString=gson.toJson(model, MeetingListRequestModel.class);

				Log.d("droid", dataString);
				params.put(GetMemberProfileService.DATA, dataString);
				return params;
			}
		};

		App.getInstance().addToRequestQueue(reqGetProfile, "reqGetProfile");
		reqGetProfile.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
	}

	
	private void getPartnershiparms() {
		StringRequest reqGetProfile=new StringRequest(Method.POST,Getgetpartnershiparms.SERVICE_URL,new Listener<String>() {

			@Override
			public void onResponse(String response) {
				Methods.closeProgressDialog();
				Log.e("droid","get reqGetpartnershiparms ---------------"+ response);
		
					try {
						JSONObject obj=new JSONObject(response);
						JSONArray jsonoarray=obj.getJSONArray("message");

						String[] str=new String[jsonoarray.length()];
						
						for(int i=0;i<jsonoarray.length();i++){
							str[i]=jsonoarray.getJSONObject(i).getString("name");
						}
						
						ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(CreatePartnershipActivity.this, android.R.layout.simple_spinner_item, str); 
						spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						spPartnershipArm.setAdapter(spinnerArrayAdapter);
					
						
						Log.e("droid","get reqGetpartnershiparms ---------------"+ jsonoarray.toString());
						
					//	txtmember.setText(jsonoarray.getJSONObject(0).getString("name"));
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	
				
			}
		},new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Methods.closeProgressDialog();
				Log.d("droid","get reqsaveMeeting error---------------"+ error.getCause());

				if(error.networkResponse.statusCode==403){
					Methods.longToast("No access to update Profile", CreatePartnershipActivity.this);
				}
				else
					Methods.longToast("Some Error Occured,please try again later", CreatePartnershipActivity.this);
			}
		}){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError{
				Map<String, String> params = new HashMap<String, String>();

				MeetingListRequestModel model=new MeetingListRequestModel();
				model.setUsername(mPreferenceHelper.getString(Commons.USER_EMAILID));
				model.setUserpass(mPreferenceHelper.getString(Commons.USER_PASSWORD));
				String dataString=gson.toJson(model, MeetingListRequestModel.class);

				Log.d("droid", dataString);
				params.put(GetMemberProfileService.DATA, dataString);
				return params;
			}
		};

		App.getInstance().addToRequestQueue(reqGetProfile, "reqGetProfile");
		reqGetProfile.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
	}


    private void getCurrency() {
        StringRequest reqGetProfile=new StringRequest(Method.POST,Getcurrency.SERVICE_URL,new Listener<String>() {

            @Override
            public void onResponse(String response) {
                Methods.closeProgressDialog();
                Log.e("droid","get reqGetCurrency ---------------"+ response);

                try {
                    JSONObject obj=new JSONObject(response);
                    JSONObject obj1=obj.getJSONObject("message");
                    JSONArray jsonoarray=obj1.getJSONArray("currency_details");

                    String[] str=new String[jsonoarray.length()];

                    for(int i=0;i<jsonoarray.length();i++){
                        str[i]=jsonoarray.getJSONObject(i).getString("name");
                    }

                    ArrayAdapter<String> currencyArrayAdapter = new ArrayAdapter<String>(CreatePartnershipActivity.this, android.R.layout.simple_spinner_item, str);
                    currencyArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spCurrency.setAdapter(currencyArrayAdapter);


                    Log.e("droid","get reqGetCurrency ---------------"+ jsonoarray.toString());

                    //	txtmember.setText(jsonoarray.getJSONObject(0).getString("name"));

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


            }
        },new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Methods.closeProgressDialog();
                Log.d("droid","get reqGetCurrency error---------------"+ error.getCause());

                if(error.networkResponse.statusCode==403){
                    Methods.longToast("No access to update Profile", CreatePartnershipActivity.this);
                }
                else
                    Methods.longToast("Some Error Occured,please try again later", CreatePartnershipActivity.this);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                Map<String, String> params = new HashMap<String, String>();

                MeetingListRequestModel model=new MeetingListRequestModel();
                model.setUsername(mPreferenceHelper.getString(Commons.USER_EMAILID));
                model.setUserpass(mPreferenceHelper.getString(Commons.USER_PASSWORD));
                String dataString=gson.toJson(model, MeetingListRequestModel.class);

                Log.d("droid", dataString);
                params.put(GetMemberProfileService.DATA, dataString);
                return params;
            }
        };

        App.getInstance().addToRequestQueue(reqGetProfile, "reqGetProfile");
        reqGetProfile.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
    }

	boolean validatefield(){
		
		//if(txtMinistryYear.getText().toString().length()>0){
			
			if(txtamount.getText().toString().length()>0){
				
				String str=txtamount.getText().toString();
				char[] arr=str.toCharArray();
				
				if(arr[0]=='0'){
					Toast.makeText(CreatePartnershipActivity.this, "Enter Valid Amount", Toast.LENGTH_LONG).show();
					return false;
				
				}else{
					
					return true;	
				}
					
				
			}else{
				Toast.makeText(CreatePartnershipActivity.this, "Enter Amount ", Toast.LENGTH_LONG).show();
				return false;
			}
				
		/*}else{
			Toast.makeText(CreatePartnershipActivity.this, "Enter Ministry Year", Toast.LENGTH_LONG).show();
			return false;
		}*/
		
	}

}
