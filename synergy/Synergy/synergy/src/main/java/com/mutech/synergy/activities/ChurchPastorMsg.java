package com.mutech.synergy.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
import com.mutech.synergy.models.CellLeaderMsgModel;
import com.mutech.synergy.models.ResponseMessageModel;
import com.mutech.synergy.models.ResponseMessageModel2;
import com.mutech.synergy.models.StatusCheckModel;
import com.mutech.synergy.utils.Methods;
import com.mutech.synergy.utils.NetworkHelper;
import com.mutech.synergy.utils.PreferenceHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChurchPastorMsg extends ActionBarActivity {

    private EditText edtmsg;
    private PreferenceHelper mPreferenceHelper;
    private Gson gson;
    String cellcode;
    private Button btnsend,btncancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_church_pastor_msg);

        edtmsg=(EditText)findViewById(R.id.edmsg);
        btnsend=(Button)findViewById(R.id.btnsend);
        btncancel=(Button)findViewById(R.id.cancel);
        cellcode=getIntent().getStringExtra("cellcode");
        mPreferenceHelper=new PreferenceHelper(this);
        gson=new Gson();

        btnsend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(NetworkHelper.isOnline(ChurchPastorMsg.this)){
                    Methods.showProgressDialog(ChurchPastorMsg.this);
                    sendCellLeaderMsg();
                }
                else
                    Methods.longToast("Please connect to Internet", ChurchPastorMsg.this);
            }

        });

        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });

    }

    private void sendCellLeaderMsg() {

        StringRequest sendCellLeaderMsg=new StringRequest(Request.Method.POST, SynergyValues.Web.SendCellLeaderMsg.SERVICE_URL,new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Nonstop", "send church pastor leader message  ---------------" + response);
                Methods.closeProgressDialog();
                try {
                    JSONObject jsonobj=new JSONObject(response);

                    JSONObject jObj= jsonobj.getJSONObject("message");
                    if( jObj.getString("status").equals("200")) {
                        Methods.longToast("Message Sent Successfully. ", ChurchPastorMsg.this);
                        finish();
                    }else{
                        Methods.longToast("Some Error Occured,please try again later", ChurchPastorMsg.this);
                    }
                }
                catch(Exception e)
                {
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
                    Methods.longToast("Access Denied", ChurchPastorMsg.this);
                }
                else
                    Methods.longToast("Some Error Occured,please try again later", ChurchPastorMsg.this);

            }

        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                CellLeaderMsgModel model=new CellLeaderMsgModel();
                model.setUsername(mPreferenceHelper.getString(SynergyValues.Commons.USER_EMAILID));
                model.setUserpass(mPreferenceHelper.getString(SynergyValues.Commons.USER_PASSWORD));
                model.setName(cellcode);
                model.setRole("Church Pastor");
                if(edtmsg.getText().toString() != null) {
                    String SendMsg = edtmsg.getText().toString();
                    model.setMessage(edtmsg.getText().toString());
                } else {
                    model.setMessage("NonStop Test");
                }

                String dataString=gson.toJson(model, CellLeaderMsgModel.class);

                Log.d("droid", dataString);
                params.put(SynergyValues.Web.GetHigherHierarchyService.DATA, dataString);
                return params;
            }
        };

        App.getInstance().addToRequestQueue(sendCellLeaderMsg, "sendCellLeaderMsg");
        sendCellLeaderMsg.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));

    }
}
