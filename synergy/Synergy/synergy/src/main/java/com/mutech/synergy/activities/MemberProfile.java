package com.mutech.synergy.activities;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.mutech.synergy.App;
import com.mutech.synergy.R;
import com.mutech.synergy.SynergyValues;
import com.mutech.synergy.models.MemberShortProfile;
import com.mutech.synergy.utils.Methods;
import com.mutech.synergy.utils.NetworkHelper;
import com.mutech.synergy.utils.PreferenceHelper;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MemberProfile extends Activity {

    private String memberName;
    private PreferenceHelper mPreferenceHelper;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_profile);

        memberName=getIntent().getExtras().getString("MemberNo");
        mPreferenceHelper=new PreferenceHelper(this);
        gson=new Gson();

        if(NetworkHelper.isOnline(this)){
            Methods.showProgressDialog(this);
            getCellDetails();
        }
        else
            Methods.longToast("Please connect to Internet", this);

    }

    private void getCellDetails() {

        StringRequest reqgetCellDetails=new StringRequest(Request.Method.POST, SynergyValues.Web.GetMemberShortProfileService.SERVICE_URL,new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Methods.closeProgressDialog();
                Log.d("droid", "get reqgetTopHierarchy ---------------" + response);
                try {

                    JSONObject jsonobj=new JSONObject(response);
                    JSONArray jarray=jsonobj.getJSONArray("message");

/*                    if(jarray.getJSONObject(0).getString("name") != null) {
                        txtCellLeaderId.setText(jarray.getJSONObject(0).getString("name"));
                    }

                    if(jarray.getJSONObject(0).getString("member_name") != null) {
                        txtCellLeaderName.setText(jarray.getJSONObject(0).getString("member_name"));
                    }

                    if(jarray.getJSONObject(0).getString("email_id") != null) {
                        txtCellLeaderEmailId.setText(jarray.getJSONObject(0).getString("email_id"));
                    }

                    if(jarray.getJSONObject(0).getString("date_of_birth") != null) {
                        txtCellLeaderDateOfBirth.setText(jarray.getJSONObject(0).getString("date_of_birth"));
                    }

                    if(jarray.getJSONObject(0).getString("phone_1") != null) {
                        txtCellLeaderPhone.setText(jarray.getJSONObject(0).getString("phone_1"));
                    }

                    if(jarray.getJSONObject(0).getString("address") != null) {
                        txtCellLeaderAddress.setText(jarray.getJSONObject(0).getString("address"));
                    }

                    Imageurl= jarray.getJSONObject(0).getString("image");
                    if(Imageurl != null) {
                        Picasso.with(ShortBio.this)
                                .load(SynergyValues.ImageUrl.imageUrl + jarray.getJSONObject(0).getString("image"))
                                .placeholder(R.drawable.user) // optional
                                .error(R.drawable.user)         // optional
                                .into(imgProfilePic);
                    }
                    Log.e("Image Url", SynergyValues.ImageUrl.imageUrl + Imageurl);
                    Log.d("NonStop", "Image URL: " + SynergyValues.ImageUrl.imageUrl + Imageurl);*/

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
                Log.d("droid","get reqgetTopHierarchy error---------------"+ error.getCause());

                if(error.networkResponse.statusCode==403){
                    Methods.longToast("Access Denied", MemberProfile.this);
                }
                else
                    Methods.longToast("Some Error Occured,please try again later", MemberProfile.this);



            }

        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                MemberShortProfile model=new MemberShortProfile();
                model.setUsername(mPreferenceHelper.getString(SynergyValues.Commons.USER_EMAILID));
                model.setUserpass(mPreferenceHelper.getString(SynergyValues.Commons.USER_PASSWORD));
                model.setName(memberName);
              //  model.setRole("Member");
//                model.setRole("Cell Leader");

                String dataString=gson.toJson(model, MemberShortProfile.class);

                Log.d("droid", dataString);
                params.put(SynergyValues.Web.GetHigherHierarchyService.DATA, dataString);
                return params;
            }
        };

        App.getInstance().addToRequestQueue(reqgetCellDetails, "reqgetCellDetails");
        reqgetCellDetails.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));

    }
}
