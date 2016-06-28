package com.mutech.synergy.activities;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;
import com.mutech.synergy.App;
import com.mutech.synergy.R;
import com.mutech.synergy.SynergyValues;
import com.mutech.synergy.SynergyValues.Commons;
import com.mutech.synergy.SynergyValues.Web.GetUserRolesService;
import com.mutech.synergy.SynergyValues.Web.LoginService;
import com.mutech.synergy.SynergyValues.Web.PushNotification;
import com.mutech.synergy.activities.profile.MyProfileActivity;
import com.mutech.synergy.activities.profile.ProfileView;
import com.mutech.synergy.models.MeetingListRequestModel;
import com.mutech.synergy.models.MemberProfileModel;
import com.mutech.synergy.models.UserRolesReqModel;
import com.mutech.synergy.models.UserRolesResponseModel;
import com.mutech.synergy.models.UserRolesResponseModel.RolesRolesValues;
import com.mutech.synergy.models.UserRolesResponseModel.RolesUserValues;
import com.mutech.synergy.utils.Methods;
import com.mutech.synergy.utils.NetworkHelper;
import com.mutech.synergy.utils.PreferenceHelper;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends Activity implements OnClickListener{

	private EditText txtUserName,txtPassword;
	private TextView lblLoginButton;
	private PreferenceHelper mPreferenceHelper;
	private Gson gson;
	private String msg = "";
	
	
	public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    String SENDER_ID = "870314579977";
    static final String TAG = "GCM Demo";

    TextView mDisplay;
    GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();
    Context context;

    String regid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_login);
		initialize();
		intGCM();
	}

	//[2876] BasicNetwork.performRequest: Unexpected response code 500 for http://loveworldsynergy.org/api/method/login
	
	private void intGCM(){
		 context = getApplicationContext();

	        // Check device for Play Services APK. If check succeeds, proceed with GCM registration.
	        if (checkPlayServices()) {
	            gcm = GoogleCloudMessaging.getInstance(this);
	            regid = getRegistrationId(context);

	            if (regid.length()==0) {
	                registerInBackground();
	            }
	        } else {
	            Log.i(TAG, "No valid Google Play Services APK found.");
	        }
	}
	
	//12-14 12:58:42.302: E/droid(6940): get reqGetpartnershiparms ---------------{"message":[["Child Education"],["Child Health"],["IMM"],["Social Welfare"]]}


	private void initialize() {
		txtUserName=(EditText) findViewById(R.id.txtName);
		txtPassword=(EditText) findViewById(R.id.txtPassword);
		lblLoginButton=(TextView) findViewById(R.id.lblLoginButton);
		lblLoginButton.setOnClickListener(this);

		Typeface tf = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");
		lblLoginButton.setTypeface(tf);

		Typeface tf1 = Typeface.createFromAsset(getAssets(), "Cabin-Regular.ttf");
		txtUserName.setTypeface(tf1);
		txtPassword.setTypeface(tf1);
	//	Id : nikhil.k@indictranstech.com 
//sagar.s@indictranstech.com,region3@gmail.com
		
	//	txtUserName.setText("3AZone@gmail.com");
	//	txtPassword.setText("password");
		


		mPreferenceHelper=new PreferenceHelper(this);
		gson=new Gson();

	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lblLoginButton:

			if(validateFields()){
				 new AsyncTask<Void, Void, String>() {
		                @Override
		                protected String doInBackground(Void... params) {
		                    String msg = "";
		                    try {
		                        Bundle data = new Bundle();
		                        data.putString("my_message", "Hello World");
		                        data.putString("my_action", "com.google.android.gcm.demo.app.ECHO_NOW");
		                        String id = Integer.toString(msgId.incrementAndGet());
		                        gcm.send(SENDER_ID + "@gcm.googleapis.com", id, data);
		                        msg = "Sent message";
		                    } catch (IOException ex) {
		                        msg = "Error :" + ex.getMessage();
		                    }
		                    return msg;
		                }

		                @Override
		                protected void onPostExecute(String msg) {
//		                    mDisplay.append(msg + "\n");
		                }
		            }.execute(null, null, null);
				//HIT SERVICE
				if(NetworkHelper.isOnline(this)){
					Methods.showProgressDialog(this);
					callService();
				}
				else
					Methods.longToast("Please connect to Internet", this);

			}
			break;

		default:
			break;
		}
	}

	private void callService() {
		StringRequest request=new StringRequest(Method.POST,LoginService.SERVICE_URL, new Listener<String>() {

			@Override
			public void onResponse(String response) {			

				Log.d("NonStop", response);
				mPreferenceHelper.addBoolean(Commons.ISUSER_LOGGEDIN, true);
				mPreferenceHelper.addString(Commons.USER_EMAILID, txtUserName.getText().toString().trim());
				mPreferenceHelper.addString(Commons.USER_PASSWORD, txtPassword.getText().toString().trim());

				getLoggedInUserRoles();
				sendGCM();
				getProfileInfo();

				//				Intent intHome=new Intent(LoginActivity.this,HomeActivity.class);
				//				startActivity(intHome);
				//				finish();

			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Methods.closeProgressDialog();

				txtUserName.setText("");
				txtPassword.setText("");

				//Log.d("droid", "onErrorResponse"+error.getCause());
				Methods.longToast("Please Enter valid User Id and Password.", LoginActivity.this);
			}
		})
		{
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();  
				params.put(LoginService.USERNAME, txtUserName.getText().toString().trim());  
				params.put(LoginService.PASSWORD, txtPassword.getText().toString().trim());  
				
				Log.e("Request", params.toString());
				
				return params;  
			}
		};

		App.getInstance().addToRequestQueue(request, "Login");
		request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));

	}


	protected void getLoggedInUserRoles() {
		StringRequest requestRoles=new StringRequest(Method.POST,GetUserRolesService.SERVICE_URL, new Listener<String>() {

			@Override
			public void onResponse(String response) {
				Methods.closeProgressDialog();
				Log.e("login droid", "onResponse roles" + response);
				Methods.longToast("Logged In successfully", LoginActivity.this);

			
				UserRolesResponseModel respModel=gson.fromJson(response, UserRolesResponseModel.class);
				ArrayList<RolesUserValues> roleUserlist=respModel.getMessage().getUser_values();

				Log.d(null, "Rolelist--"+roleUserlist.toString());
				
				

				ArrayList<RolesRolesValues> rolelist=respModel.getMessage().getRoles();

				Log.d(null, "Rolelist--" + respModel.getMessage().getRoles());
				
				for(int i=0;i<rolelist.size();i++){
					if(rolelist.get(i).getRole()!="Member"){
				    Log.e("list",  rolelist.get(i).getRole());
					mPreferenceHelper.addString(Commons.USER_ROLE, rolelist.get(i).getRole());
					mPreferenceHelper.addString(Commons.USER_DEFKEY, roleUserlist.get(i).getDefkey());
					mPreferenceHelper.addString(Commons.USER_DEFVALUE, roleUserlist.get(i).getDefvalue());
					
					
					Log.e("USER_DEFKEY",  rolelist.get(i).getRole());
					Log.e("USER_DEFVALUE",  rolelist.get(i).getRole());
					}
					
				}
				String str=mPreferenceHelper.getString(Commons.USER_ROLE);
				Log.e("str ", str);
//   			mPreferenceHelper.addString(Commons.USER_ROLE, rolelist.get(0).getRole());
				
//				Log.e("gjhgkgkgkjg",  rolelist.get(0).getRole());
//				Log.e("gjh",  rolelist.get(1).getRole());
				if(str.equals("Member")){
					Intent intprof=new Intent(LoginActivity.this,ProfileView.class);
					startActivity(intprof);
					finish();
					Log.e(null, "11111111");
				}else{
					Log.e(null, "2222222");
				Intent intHome=new Intent(LoginActivity.this,HomeActivity.class);
				startActivity(intHome);
				finish();
				}
				
			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Methods.closeProgressDialog();

				Log.d("droid", "onErrorResponse" + error.getCause());
			}
		})
		{
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();  

				UserRolesReqModel model=new UserRolesReqModel();
				model.setUsername(txtUserName.getText().toString().trim());
				model.setUserpass(txtPassword.getText().toString().trim());

				String reqString=gson.toJson(model,UserRolesReqModel.class);

				Log.e("Login request", reqString);
				params.put(GetUserRolesService.DATA, reqString);  
				return params;  
			}
		};
		App.getInstance().addToRequestQueue(requestRoles, "requestRoles");
		requestRoles.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
	}


	private void getProfileInfo() {
		StringRequest reqGetProfile=new StringRequest(Request.Method.POST, SynergyValues.Web.GetMemberProfileService.SERVICE_URL,new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				Methods.closeProgressDialog();
				Log.d("droid","get reqgetTopHierarchy ---------------"+ response);
				try {

					JSONObject jsonobj=new JSONObject(response);
					JSONArray jarray=jsonobj.getJSONArray("message");

					mPreferenceHelper.addString(Commons.USER_NAME, jarray.getJSONObject(0).getString("member_name") + " " +jarray.getJSONObject(0).getString("last_name"));
					mPreferenceHelper.addString(Commons.USER_STATUS, jarray.getJSONObject(0).getString("employment_status"));
					mPreferenceHelper.addString(Commons.USER_DESIGNATION, jarray.getJSONObject(0).getString("member_designation"));
					mPreferenceHelper.addString(Commons.USER_IMAGE, SynergyValues.ImageUrl.imageUrl +jarray.getJSONObject(0).getString("image"));

					Log.d("NonStop", "last_name: " + jarray.getJSONObject(0).getString("last_name"));
					Log.d("NonStop", "name: " + jarray.getJSONObject(0).getString("member_name"));
					Log.d("NonStop", "member_designation: " + jarray.getJSONObject(0).getString("member_designation"));
					Log.d("NonStop", "employment_status: " + jarray.getJSONObject(0).getString("employment_status"));
					Log.d("NonStop", "image: " + SynergyValues.ImageUrl.imageUrl +jarray.getJSONObject(0).getString("image"));

					Log.d("NonStop", "name: " + mPreferenceHelper.getString(Commons.USER_NAME));
					Log.d("NonStop", "member_designation: " + mPreferenceHelper.getString(Commons.USER_DESIGNATION));
					Log.d("NonStop", "employment_status: " + mPreferenceHelper.getString(Commons.USER_STATUS));
					Log.d("NonStop", "image: " + mPreferenceHelper.getString(Commons.USER_IMAGE));

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				Methods.closeProgressDialog();
			}
		},new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Methods.closeProgressDialog();
				Log.d("droid","get reqsaveMeeting error---------------"+ error.getCause());

				if(error.networkResponse.statusCode==403){
					Methods.longToast("No access to update Profile", LoginActivity.this);
				}
				else
					Methods.longToast("Some Error Occured,please try again later", LoginActivity.this);
			}
		}){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();

				MeetingListRequestModel model=new MeetingListRequestModel();
				model.setUsername(mPreferenceHelper.getString(SynergyValues.Commons.USER_EMAILID));
				model.setUserpass(mPreferenceHelper.getString(SynergyValues.Commons.USER_PASSWORD));

				String dataString=gson.toJson(model, MeetingListRequestModel.class);

				Log.e("Request droid", dataString);
				params.put(SynergyValues.Web.GetMemberProfileService.DATA, dataString);
				return params;
			}
		};

		App.getInstance().addToRequestQueue(reqGetProfile, "reqGetProfile");
		reqGetProfile.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
	}
	
	private void sendGCM() {
		StringRequest request=new StringRequest(Method.POST,PushNotification.SERVICE_URL, new Listener<String>() {

			@Override
			public void onResponse(String response) {			

				mPreferenceHelper.addBoolean(Commons.ISUSER_LOGGEDIN, true);
				mPreferenceHelper.addString(Commons.USER_EMAILID, txtUserName.getText().toString().trim());
				mPreferenceHelper.addString(Commons.USER_PASSWORD, txtPassword.getText().toString().trim());
	

			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Methods.closeProgressDialog();

				txtUserName.setText("");
				txtPassword.setText("");

//				Methods.longToast("Can't Login,please try again later", LoginActivity.this);
			}
		})
		{
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();  
				params.put(PushNotification.USERNAME, txtUserName.getText().toString().trim());  
				params.put(PushNotification.PASSWORD, txtPassword.getText().toString().trim());  
				params.put(PushNotification.DEVICE_ID, regid);
				return params;  
			}
		};

		App.getInstance().addToRequestQueue(request, "Login");
		request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));

	}

	
	
	private boolean validateFields() {

		if(txtUserName.getText().toString().trim().length() > 0){
			if(Methods.isValidEmail(txtUserName.getText().toString().trim())){
				if(txtPassword.getText().toString().trim().length() > 0){
					return true;
				}else{
					Methods.longToast("Please enter Password", this);
				}
			}else{
				Methods.longToast("Please enter valid email id", this);
			}
		}else{
			Methods.longToast("Please enter Username", this);
		}

		return false;
	}
	
	private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
 
 private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGcmPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }
 
 private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGcmPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.length()==0) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }
 
 private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;
          
                    sendRegistrationIdToBackend();
                    storeRegistrationId(context, regid);
                    
                    
                    Log.d("Registration ID ***", msg+" ***");
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
//                mDisplay.append(msg + "\n");
            	Log.e(null, "Device_id--"+msg);
            }
        }.execute(null, null, null);
    }
 
 
 private static int getAppVersion(Context context) {
     try {
         PackageInfo packageInfo = context.getPackageManager()
                 .getPackageInfo(context.getPackageName(), 0);
         return packageInfo.versionCode;
     } catch (NameNotFoundException e) {
         // should never happen
         throw new RuntimeException("Could not get package name: " + e);
     }
 }


 private SharedPreferences getGcmPreferences(Context context) {
     return getSharedPreferences(LoginActivity.class.getSimpleName(),
             Context.MODE_PRIVATE);
 }

 private void sendRegistrationIdToBackend() {
   // Your implementation here.
 }
	
	 @Override
	    protected void onResume() {
	        super.onResume();
	        // Check device for Play Services APK.
	        checkPlayServices();
	    }

}
