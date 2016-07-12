//package com.mutech.synergy.activities;
//
//import android.app.AlertDialog;
//import android.app.DatePickerDialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v7.app.ActionBarActivity;
//import android.view.View;
//import android.widget.Button;
//import android.widget.DatePicker;
//import android.widget.EditText;
//import android.widget.TextView;
//
//import com.google.gson.Gson;
//import com.mutech.synergy.R;
//import com.mutech.synergy.activities.meeting.MeetingListActivity;
//import com.mutech.synergy.utils.InputValidation;
//import com.mutech.synergy.utils.PreferenceHelper;
//
//import org.json.JSONArray;
//
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Locale;
//
//public class CAttendanceHistory extends ActionBarActivity {
//
//    private Button btnsubmit,btncancel;
//    private EditText txtFromDate,txtToDate;
//    private DatePickerDialog fromDatePickerDialog,toDatePickerDialog;
//    private SimpleDateFormat dateFormatter,dateFormatter01;
//    String fdate="",tdate="";
//    private Gson gson;
//    JSONArray jsonarray;
//    private PreferenceHelper mPreferenceHelper;
//    private Intent intent;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_church_attendance_history);
//
//        btnsubmit = (Button) findViewById(R.id.submit);
//        btncancel = (Button) findViewById(R.id.cancel);
//
//        txtFromDate=(EditText) findViewById(R.id.txtfromdate);
//        txtToDate=(EditText) findViewById(R.id.txttodate);
//
//        jsonarray=new JSONArray();
//        mPreferenceHelper=new PreferenceHelper(this);
//
//        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
//        //2015-05-24 06:22:22
//        dateFormatter01=new SimpleDateFormat("yyyy-MM-dd");
//
//        Calendar newCalendar = Calendar.getInstance();
//        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
//
//            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                Calendar newDate = Calendar.getInstance();
//                newDate.set(year, monthOfYear, dayOfMonth);
//                txtFromDate.setText(dateFormatter.format(newDate.getTime()));
//            }
//
//        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
//
//        toDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
//
//            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                Calendar newDate = Calendar.getInstance();
//                newDate.set(year, monthOfYear, dayOfMonth);
//                txtToDate.setText(dateFormatter.format(newDate.getTime()));
//            }
//
//        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
//
//
//        txtFromDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                //	showfromdate();
//                fromDatePickerDialog.show();
//            }
//        });
//
//        txtToDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                toDatePickerDialog.show();
//            }
//        });
//
//        btnsubmit.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                if (isValid()) {
//                    /*if(intent.hasExtra("fromallmembers")){
//                        Intent Int = new Intent(AttendanceHistory.this, MeetingListActivity.class);
//                    }*/
//                    Intent Int = new Intent(CAttendanceHistory.this, MeetingListActivity.class);
//                    Int.putExtra("fdate", txtFromDate.getText().toString());
//                    Int.putExtra("tdate", txtToDate.getText().toString());
//                    Int.putExtra("fromah", "fromah");
//                    startActivity(Int);
//                    finish();
//                }
//            }
//        });
//
//        btncancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                Intent actInt = new Intent(CAttendanceHistory.this, HomeActivity.class);
//                startActivity(actInt);
//                finish();
//            }
//        });
//    }
//
//    public boolean isValid() {
//
//        if(!InputValidation.hasText(txtFromDate)) {
//            AlertDialog dialog =  new AlertDialog.Builder(CAttendanceHistory.this)
//                    .setCancelable(false)
//                    .setTitle("Invalid Input")
//                    .setMessage("Please enter valid value in the 'From Date'")
//                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//
//                        }
//                    })
//                    .show();
//            TextView textView = (TextView) dialog.findViewById(android.R.id.message);
//            textView.setTextSize(18);
//            return false;
//        }
//        if(!InputValidation.hasText(txtToDate)) {
//            AlertDialog dialog =  new AlertDialog.Builder(CAttendanceHistory.this)
//                    .setCancelable(false)
//                    .setTitle("Invalid Input")
//                    .setMessage("Please enter valid value in the 'To Date'")
//                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//
//                        }
//                    })
//                    .show();
//            TextView textView = (TextView) dialog.findViewById(android.R.id.message);
//            textView.setTextSize(18);
//            return false;
//        }
//        return true;
//    }
//}
