
package com.mutech.synergy.activities.cellMasters;

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
import com.mutech.synergy.SynergyValues;
import com.mutech.synergy.SynergyValues.Commons;
import com.mutech.synergy.SynergyValues.Web.ShowPartnerShipService;
import com.mutech.synergy.SynergyValues.Web.UpdatePartnerShipService;
import com.mutech.synergy.activities.profile.MyProfileActivity;
import com.mutech.synergy.models.MeetingListRequestModel;
import com.mutech.synergy.models.PartnerShipModel;
import com.mutech.synergy.models.PartnerShipModel.PartnerModel;
import com.mutech.synergy.models.ResponseMessageModel;
import com.mutech.synergy.models.StatusCheckModel;
import com.mutech.synergy.utils.Methods;
import com.mutech.synergy.utils.NetworkHelper;
import com.mutech.synergy.utils.PreferenceHelper;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PartnerRecordInformation extends ActionBarActivity implements OnClickListener{


	private PreferenceHelper mPreferenceHelper;
	private Gson gson;

	String PartnerName;
	private EditText PartnershipArms,Ministry,PartnerCategory,PartnerValue,Date,txtPartner,txt_typeofpledge,txtConversation_Rate;
	private TextView Amount;
	Spinner Giving_Pledge;
	private ArrayList<String> Giving_PledgeList;
	ArrayAdapter<String> adapterGP,currencyArrayAdapter;
	private Button btnPartnerSave;
	private Spinner spCurrency;
	ArrayList<PartnerModel> mPartnerSubModel;
	TextView lblpledge;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.partnership_record);
		initialize();
	}

	@SuppressLint("NewApi")
	private void initialize() {

		getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		getSupportActionBar().setHomeButtonEnabled(false);
		getSupportActionBar().setCustomView(R.layout.custom_actionbar);
		TextView tvTitle=(TextView)getSupportActionBar().getCustomView().findViewById(R.id.title_text);
		tvTitle.setText("Partnership Record  ");

		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2E9AFE")));


		mPreferenceHelper=new PreferenceHelper(this);
		gson=new Gson();

		getSupportActionBar().setDisplayShowCustomEnabled(true);
		PartnerName=getIntent().getExtras().getString("PartnerNo");
		Log.e(null, "PartnerName--"+PartnerName);

		lblpledge=(TextView) findViewById(R.id.lblpledge);
		txt_typeofpledge=(EditText)findViewById(R.id.txt_typeofpledge);
		PartnershipArms=(EditText) findViewById(R.id.txtPartnershipArms);
		Ministry=(EditText) findViewById(R.id.txtMinistry);
		PartnerCategory=(EditText) findViewById(R.id.txtPartnerCategory);
		PartnerValue=(EditText) findViewById(R.id.txtPartnerValue);
		Date=(EditText) findViewById(R.id.txtDate);
		Amount=(TextView) findViewById(R.id.txtAmount);
		Giving_Pledge=(Spinner) findViewById(R.id.txtGiving_Pledge);
		txtPartner=(EditText) findViewById(R.id.txtPartner);
		spCurrency=(Spinner) findViewById(R.id.spCurrency);
        txtConversation_Rate=(EditText) findViewById(R.id.txtConversation_Rate);

		txt_typeofpledge.setEnabled(false);
//		Amount.setEnabled(false);

		Giving_Pledge.setEnabled(false);
		mPartnerSubModel=new ArrayList<PartnerModel>();


		Giving_PledgeList=new ArrayList<String>();
		Giving_PledgeList.add("Giving");
		Giving_PledgeList.add("Pledge");

		adapterGP = new ArrayAdapter<String>(PartnerRecordInformation.this, android.R.layout.simple_spinner_item, Giving_PledgeList);
		adapterGP.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Giving_Pledge.setAdapter(adapterGP);


		btnPartnerSave=(Button) findViewById(R.id.btnPartnerSave);
		btnPartnerSave.setOnClickListener(this);

//		btnPartnerSave.setVisibility(View.GONE);

		if(NetworkHelper.isOnline(this)){
			Methods.showProgressDialog(this);
			Log.d("NonStop", "Getting currency");
			getCurrency();

		}
		else
			Methods.longToast("Please connect to Internet", this);

	}


	private void getPartnerInfo() {
		StringRequest reqGetProfile=new StringRequest(Method.POST,ShowPartnerShipService.SERVICE_URL,new Listener<String>() {

			@Override
			public void onResponse(String response) {
				Methods.closeProgressDialog();
				Log.e("droid","get partnershipinformation ---------------"+ response);

				StatusCheckModel statusCheckModel=gson.fromJson(response, StatusCheckModel.class);
				if(null != statusCheckModel.getStatus() && statusCheckModel.getStatus().trim().length() >0){
					ResponseMessageModel respModel=gson.fromJson(response, ResponseMessageModel.class);
					if(respModel.getStatus()=="401"){
						Methods.longToast("User name or Password is incorrect", PartnerRecordInformation.this);
					}else{
						Methods.longToast(respModel.getMessage(), PartnerRecordInformation.this);
					}
				}else{
					PartnerShipModel mPartnerModel=gson.fromJson(response, PartnerShipModel.class);
					if(null !=mPartnerModel.getMessage() && mPartnerModel.getMessage().size() >0){
						mPartnerSubModel=mPartnerModel.getMessage();

						for(int i=0;i<mPartnerSubModel.size();i++){
							PartnershipArms.setText(mPartnerSubModel.get(i).getpartnership_arms());
							Ministry.setText(mPartnerSubModel.get(i).getministry_year());
							Date.setText(mPartnerSubModel.get(i).getdate());
							Amount.setText(mPartnerSubModel.get(i).getamount());
                           // txtConversation_Rate.setText(mPartnerSubModel.get(i).getConversation_rate());
                            if(mPartnerSubModel.get(i).getcurrency()==null) {
                                spCurrency.setSelection(0);
                            } else {
                                spCurrency.setSelection(currencyArrayAdapter.getPosition(mPartnerSubModel.get(i).getcurrency()));
                            }

							Giving_Pledge.setSelection(adapterGP.getPosition(mPartnerSubModel.get(i).getgiving_or_pledge()));

							if(mPartnerSubModel.get(i).getgiving_or_pledge().equals("Giving")){

								txt_typeofpledge.setVisibility(View.GONE);
								lblpledge.setVisibility(View.GONE);
							}else{

								txt_typeofpledge.setText(mPartnerSubModel.get(i).getType_of_pledge());
							}

							txtPartner.setText(mPartnerSubModel.get(i).getPartner_name());
                        //  txtConversation_Rate.setText(mPartnerSubModel.get(i).getConversation_rate());
                            if(mPartnerSubModel.get(i).getcurrency() !=null){
                                spCurrency.setSelection(currencyArrayAdapter.getPosition(mPartnerSubModel.get(i).getcurrency()));}
                            else { spCurrency.setSelection(0);}
							txtConversation_Rate.setText(mPartnerSubModel.get(i).getConversation_rate());
                            Amount.setText(mPartnerSubModel.get(i).getamount());
							PartnerCategory.setText(mPartnerSubModel.get(i).getIs_member());
							PartnerValue.setText(mPartnerSubModel.get(i).getMember());
							//txt_typeofpledge.setText(mPartnerSubModel.get(i).getType_of_pledge());

						}
					}else{
					}
				}

			}
		},new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Methods.closeProgressDialog();
				Log.d("droid","get reqsaveMeeting error---------------"+ error.getCause());

				if(error.networkResponse.statusCode==403){
					Methods.longToast("No access to update Profile", PartnerRecordInformation.this);
				}
				else
					Methods.longToast("Some Error Occured,please try again later", PartnerRecordInformation.this);
			}
		}){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError{
				Map<String, String> params = new HashMap<String, String>();

				MeetingListRequestModel model=new MeetingListRequestModel();
				model.setUsername(mPreferenceHelper.getString(Commons.USER_EMAILID));
				model.setUserpass(mPreferenceHelper.getString(Commons.USER_PASSWORD));
				model.setName(PartnerName);


				String dataString=gson.toJson(model, MeetingListRequestModel.class);

				Log.e("Request", dataString);
				params.put(ShowPartnerShipService.DATA, dataString);
				return params;
			}
		};

		App.getInstance().addToRequestQueue(reqGetProfile, "reqGetProfile");
		reqGetProfile.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
	}


	private void getCurrency() {
		StringRequest reqGetProfile=new StringRequest(Method.POST, SynergyValues.Web.Getcurrency.SERVICE_URL,new Listener<String>() {

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

					currencyArrayAdapter = new ArrayAdapter<String>(PartnerRecordInformation.this, android.R.layout.simple_spinner_item, str);
					currencyArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spCurrency.setAdapter(currencyArrayAdapter);
                    getPartnerInfo();

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
					Methods.longToast("No access to update Profile", PartnerRecordInformation.this);
				}
				else
					Methods.longToast("Some Error Occured while getting currency,please try again later", PartnerRecordInformation.this);
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
				params.put(SynergyValues.Web.GetMemberProfileService.DATA, dataString);
				return params;
			}
		};

		App.getInstance().addToRequestQueue(reqGetProfile, "reqGetProfile");
		reqGetProfile.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
	}


	private void SavePartnerInfo() {
		StringRequest reqGetProfile=new StringRequest(Method.POST,UpdatePartnerShipService.SERVICE_URL,new Listener<String>() {

			@Override
			public void onResponse(String response) {
				Methods.closeProgressDialog();
				Log.d("droid","get reqGetProfile ---------------"+ response);

				StatusCheckModel statusCheckModel=gson.fromJson(response, StatusCheckModel.class);
//				if(null != statusCheckModel.getStatus() && statusCheckModel.getStatus().trim().length() >0){
					ResponseMessageModel respModel=gson.fromJson(response, ResponseMessageModel.class);
					if(respModel.getStatus()=="401"){
						Methods.longToast("User name or Password is incorrect", PartnerRecordInformation.this);
					}else{
						Methods.longToast(respModel.getMessage(), PartnerRecordInformation.this);
					}
					finish();

			}
		},new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Methods.closeProgressDialog();
				Log.d("droid", "get reqsaveMeeting error---------------" + error.getCause());
//                Methods.longToast("No access to update Profile", PartnerRecordInformation.this);
				if(error.networkResponse.statusCode==403){
					Methods.longToast("No access to update Profile", PartnerRecordInformation.this);
				}
				else
                	Methods.longToast("Some Error Occured,please try again later", PartnerRecordInformation.this);
			    }
		}){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError{
				Map<String, String> params = new HashMap<String, String>();

                MeetingListRequestModel model=new MeetingListRequestModel();
                model.setUsername(mPreferenceHelper.getString(Commons.USER_EMAILID));
                model.setUserpass(mPreferenceHelper.getString(Commons.USER_PASSWORD));
                model.setName(PartnerName);
				model.setGiving_or_pledge(Giving_Pledge.getSelectedItem().toString());
                model.setIs_member("Member");
                model.setMeeting_id("");
                model.setAmount(Amount.getText().toString());
                model.setCurrency(spCurrency.getSelectedItem().toString());
                if(txtConversation_Rate.getText().toString().equals("null") || txtConversation_Rate.getText().toString().equals("") || txtConversation_Rate.getText().toString().equals(null))
                {
                    model.setConversation_rate(0);
                }else{
                model.setConversation_rate(Integer.parseInt(txtConversation_Rate.getText().toString()));}
//				model.setCurrency(txtCurrency.getText().toString());
				model.setMinistry_year(Ministry.getText().toString());
				model.setChurch("");
				model.setDate(Date.getText().toString());
				model.setPartnership_arms(PartnershipArms.getText().toString());


				String dataString=gson.toJson(model, MeetingListRequestModel.class);

				Log.d("droid", dataString);
				params.put(UpdatePartnerShipService.DATA, dataString);
				return params;
			}
		};

		App.getInstance().addToRequestQueue(reqGetProfile, "reqGetProfile");
		reqGetProfile.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnPartnerSave:
			if(NetworkHelper.isOnline(this)){
				Methods.showProgressDialog(this);
				SavePartnerInfo();
			}else{
				Methods.longToast("Please connect to Internet", this);
			}
			break;

		default:
			break;
		}

	}
}