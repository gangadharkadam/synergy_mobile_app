package com.mutech.synergy.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

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
import com.mutech.synergy.activities.meeting.MyMeetingListActivity;
import com.mutech.synergy.activities.profile.MyProfileActivity;
import com.mutech.synergy.models.ResponseMessageModel2;
import com.mutech.synergy.utils.InputValidation;
import com.mutech.synergy.utils.Methods;
import com.mutech.synergy.utils.PreferenceHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AttendanceHistory extends ActionBarActivity {

    private Button btnsubmit,btncancel;
    private EditText txtFromDate,txtToDate;
    private DatePickerDialog fromDatePickerDialog,toDatePickerDialog;
    private SimpleDateFormat dateFormatter,dateFormatter01;
    String fdate="",tdate="";
    private Gson gson;
    JSONArray jsonarray;
    private PreferenceHelper mPreferenceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_history);

       /* getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.actiontop);*/

        btnsubmit = (Button) findViewById(R.id.submit);
        btncancel = (Button) findViewById(R.id.cancel);

        txtFromDate=(EditText) findViewById(R.id.txtfromdate);
        txtToDate=(EditText) findViewById(R.id.txttodate);

        jsonarray=new JSONArray();
        mPreferenceHelper=new PreferenceHelper(this);

        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        //2015-05-24 06:22:22
        dateFormatter01=new SimpleDateFormat("yyyy-MM-dd");

        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                txtFromDate.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        toDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                txtToDate.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


        txtFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //	showfromdate();
                fromDatePickerDialog.show();
            }
        });

        txtToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                toDatePickerDialog.show();
            }
        });

        btnsubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isValid()) {
                    Intent Int = new Intent(AttendanceHistory.this, MyMeetingListActivity.class);
                    Int.putExtra("fdate", txtFromDate.getText().toString());
                    Int.putExtra("tdate", txtToDate.getText().toString());
                    Int.putExtra("fromah", "fromah");
                    startActivity(Int);
                    finish();
                }
            }
        });

        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent actInt = new Intent(AttendanceHistory.this, HomeActivity.class);
                startActivity(actInt);
                finish();
            }
        });
    }

    public boolean isValid() {

        if(!InputValidation.hasText(txtFromDate)) {
            AlertDialog dialog =  new AlertDialog.Builder(AttendanceHistory.this)
                    .setCancelable(false)
                    .setTitle("Invalid Input")
                    .setMessage("Please enter valid value in the 'From Date'")
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
        if(!InputValidation.hasText(txtToDate)) {
            AlertDialog dialog =  new AlertDialog.Builder(AttendanceHistory.this)
                    .setCancelable(false)
                    .setTitle("Invalid Input")
                    .setMessage("Please enter valid value in the 'To Date'")
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
}
