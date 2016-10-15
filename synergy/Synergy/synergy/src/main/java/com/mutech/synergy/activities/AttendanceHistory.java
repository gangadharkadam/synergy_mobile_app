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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.mutech.synergy.App;
import com.mutech.synergy.GraphTestActivity;
import com.mutech.synergy.R;
import com.mutech.synergy.SynergyValues;
import com.mutech.synergy.activities.meeting.MeetingListActivity;
import com.mutech.synergy.activities.meeting.MyMeetingListActivity;
import com.mutech.synergy.activities.profile.MyProfileActivity;
import com.mutech.synergy.models.MeetingListRequestModel;
import com.mutech.synergy.models.ResponseMessageModel2;
import com.mutech.synergy.utils.InputValidation;
import com.mutech.synergy.utils.Methods;
import com.mutech.synergy.utils.PreferenceHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AttendanceHistory extends ActionBarActivity {

    private Button btnsubmit,btncancel;
    private TextView txtFromDate,txtToDate;
    private DatePickerDialog fromDatePickerDialog,toDatePickerDialog;
    private SimpleDateFormat dateFormatter,dateFormatter01;
    String fdate="",tdate="";
    private Gson gson;
    JSONArray jsonarray;
    private PreferenceHelper mPreferenceHelper;
    Spinner spresion,spzone,sppcf,spgroupchurch,spchurch,spSeniorCell,spCell,speventtype;
    private Intent intent;
    private LinearLayout layoutchurch,cellLayout;
    private TextView  spchurchTextView,spCellTextView;
    private ArrayList<String> mZoneList,mRegionList,mChurchList,mSeniorCellList,mGrpChurchList,mPCFList,mCellList;
    String cellcode;
    private String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_history);
        intent = getIntent();
        cellcode=getIntent().getStringExtra("cellcode");
        role = getIntent().getStringExtra("role");

        if(role.contentEquals("Membership Strength"))
        {
            getSupportActionBar().setTitle("Select Date for Membership Strength");
        }
        btnsubmit = (Button) findViewById(R.id.submit);
        btncancel = (Button) findViewById(R.id.cancel);

        txtFromDate=(TextView) findViewById(R.id.txtfromdate);
        txtToDate=(TextView) findViewById(R.id.txttodate);

        spchurch =(Spinner) findViewById(R.id.spchurch);
        spCell=(Spinner) findViewById(R.id.spCell);
        layoutchurch=(LinearLayout) findViewById(R.id.layoutchurch1);
       // cellLayout=(LinearLayout) findViewById(R.id.cellLayout1);
        spchurchTextView=(TextView) findViewById(R.id.spchurchTextView);
        spCellTextView=(TextView) findViewById(R.id.spCellTextView);

        spchurch =(Spinner) findViewById(R.id.spchurch);
        spCell=(Spinner) findViewById(R.id.spCell);

        mChurchList=new ArrayList<String>();
        mCellList=new ArrayList<String>();

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

        if(role.contentEquals("Member"))
        {
            layoutchurch.setVisibility(View.VISIBLE);
        }


        btnsubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                if (isValid()) {
                switch(role) {
                    case "Membership Strength":

                        Intent Int = new Intent(AttendanceHistory.this, GraphTestActivity.class);
                        Int.putExtra("fdate", txtFromDate.getText().toString());
                        Int.putExtra("tdate", txtToDate.getText().toString());
                        Int.putExtra("fromah", "fromah");
                        Int.putExtra("cellcode", getIntent().getStringExtra("cellcode"));
                        Int.putExtra("tbl", getIntent().getStringExtra("tbl"));
                        startActivity(Int);
                        finish();
                        break;

                    case "Cell Leader":
                        Intent Int2 = new Intent(AttendanceHistory.this, MyMeetingListActivity.class);
                        Int2.putExtra("fdate", txtFromDate.getText().toString());
                        Int2.putExtra("tdate", txtToDate.getText().toString());
                        Int2.putExtra("fromah", "fromah");
//                        Int.putExtra("cellcode", getIntent().getStringExtra("cellcode"));
                        startActivity(Int2);
                        finish();
                        break;



                    case "Member":

                        if(isValid()) {
                            String church;
                            try {
                                church = spchurch.getSelectedItem().toString();
                            } catch (Exception ex) {
                                church = "";
                            }
                            Intent Int3 = new Intent(AttendanceHistory.this, MeetingListActivity.class);
                            Int3.putExtra("fdate", txtFromDate.getText().toString());
                            Int3.putExtra("tdate", txtToDate.getText().toString());
                            Int3.putExtra("role", "Member");
                            Int3.putExtra("attendance_type", "church Attendance");
                            Int3.putExtra("fromah", "fromah");
                            Int3.putExtra("cellcode", church);
                            startActivity(Int3);
                            finish();
                        }
                        break;



                    case "Church":
                        Intent Int1 = new Intent(AttendanceHistory.this, MeetingListActivity.class);
                        Int1.putExtra("fdate", txtFromDate.getText().toString());
                        Int1.putExtra("tdate", txtToDate.getText().toString());
                        Int1.putExtra("fromah", "fromah");
                        Int1.putExtra("attendance_type", "church Attendance");
                        Int1.putExtra("role","Church");
                        Int1.putExtra("cellcode", getIntent().getStringExtra("cellcode"));
                        startActivity(Int1);
                        finish();
                        break;

                    case "Group Church":
                        Intent Int4 = new Intent(AttendanceHistory.this, MeetingListActivity.class);
                        Int4.putExtra("fdate", txtFromDate.getText().toString());
                        Int4.putExtra("tdate", txtToDate.getText().toString());
                        Int4.putExtra("role","GroupChurch");
                        Int4.putExtra("attendance_type", "church Attendance");
                        Int4.putExtra("cellcode", getIntent().getStringExtra("cellcode"));
                        startActivity(Int4);
                        finish();
                        break;

                    case "Zone":
                        Intent Int5 = new Intent(AttendanceHistory.this, MeetingListActivity.class);
                        Int5.putExtra("fdate", txtFromDate.getText().toString());
                        Int5.putExtra("tdate", txtToDate.getText().toString());
                        Int5.putExtra("role","Zone");
                        Int5.putExtra("attendance_type", "church Attendance");
                        Int5.putExtra("cellcode", getIntent().getStringExtra("cellcode"));
                        startActivity(Int5);
                        finish();
                        break;

                    case "Region":
                        Intent Int6 = new Intent(AttendanceHistory.this, MeetingListActivity.class);
                        Int6.putExtra("fdate", txtFromDate.getText().toString());
                        Int6.putExtra("tdate", txtToDate.getText().toString());
                        Int6.putExtra("role","Region");
                        Int6.putExtra("attendance_type", "church Attendance");
                        Int6.putExtra("cellcode", getIntent().getStringExtra("cellcode"));
                        startActivity(Int6);
                        finish();
                        break;

                    case "Cells":
                        Intent Int7 = new Intent(AttendanceHistory.this, MeetingListActivity.class);
                        Int7.putExtra("fdate", txtFromDate.getText().toString());
                        Int7.putExtra("tdate", txtToDate.getText().toString());
                        Int7.putExtra("role","Cells");
                        Int7.putExtra("attendance_type", "Cell Meeting");
                        Int7.putExtra("cellcode", getIntent().getStringExtra("cellcode"));
                        startActivity(Int7);
                        finish();
                        break;
                }
                }
           // }
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

        spchurchTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                mChurchList.add(mPreferenceHelper.getString(SynergyValues.Commons.USER_DEFVALUE));
                spchurch.setVisibility(View.VISIBLE);
                spchurchTextView.setVisibility(View.GONE);
                setAdapters();
            }
        });

    }

    private void setAdapters() {

    /*    ArrayAdapter<String> adapterZone = new ArrayAdapter<String>(ShowSearchResultFunctionality.this, android.R.layout.simple_spinner_item, mZoneList);
        adapterZone.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spzone.setAdapter(adapterZone);

        ArrayAdapter<String> adapterRegion = new ArrayAdapter<String>(ShowSearchResultFunctionality.this, android.R.layout.simple_spinner_item, mRegionList);
        adapterRegion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spresion.setAdapter(adapterRegion);*/

        ArrayAdapter<String> adapterChurch = new ArrayAdapter<String>(AttendanceHistory.this, android.R.layout.simple_spinner_item, mChurchList);
        adapterChurch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spchurch.setAdapter(adapterChurch);

 /*       ArrayAdapter<String> adapterSrCell = new ArrayAdapter<String>(ShowSearchResultFunctionality.this, android.R.layout.simple_spinner_item, mSeniorCellList);
        adapterSrCell.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSeniorCell.setAdapter(adapterSrCell);

        ArrayAdapter<String> adapterchurchgropu = new ArrayAdapter<String>(ShowSearchResultFunctionality.this, android.R.layout.simple_spinner_item, mGrpChurchList);
        adapterchurchgropu.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spgroupchurch.setAdapter(adapterchurchgropu);

        ArrayAdapter<String> adapterPCF = new ArrayAdapter<String>(ShowSearchResultFunctionality.this, android.R.layout.simple_spinner_item, mPCFList);
        adapterPCF.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sppcf.setAdapter(adapterPCF);

        ArrayAdapter<String> adaptercell = new ArrayAdapter<String>(ShowSearchResultFunctionality.this, android.R.layout.simple_spinner_item, mCellList);
        adaptercell.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCell.setAdapter(adaptercell);*/

    }

    public boolean isValid() {

        if(!InputValidation.spnHasText(spchurch, "Church")) {
            AlertDialog dialog = new AlertDialog.Builder(AttendanceHistory.this)
                    .setCancelable(false)
                    .setTitle("Invalid Input")
                    .setMessage("Please enter Church")
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
