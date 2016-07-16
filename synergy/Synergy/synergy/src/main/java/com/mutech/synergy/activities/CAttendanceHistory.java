package com.mutech.synergy.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mutech.synergy.R;
import com.mutech.synergy.SynergyValues;
import com.mutech.synergy.activities.meeting.MeetingListActivity;
import com.mutech.synergy.utils.InputValidation;
import com.mutech.synergy.utils.Methods;
import com.mutech.synergy.utils.PreferenceHelper;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class CAttendanceHistory extends ActionBarActivity {

    private Button btnsubmit,btncancel;
    private TextView txtFromDate,txtToDate;
    private DatePickerDialog fromDatePickerDialog,toDatePickerDialog;
    private SimpleDateFormat dateFormatter,dateFormatter01;
    String fdate="",tdate="";
    private Gson gson;
    JSONArray jsonarray;
    private PreferenceHelper mPreferenceHelper;
    private Intent intent;
    private ArrayList<String> mZoneList,mRegionList,mChurchList,mSeniorCellList,mGrpChurchList,mPCFList,mCellList;
    Spinner spresion,spzone,sppcf,spgroupchurch,spchurch,spSeniorCell,spCell;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_church_attendance_history);

        btnsubmit = (Button) findViewById(R.id.submit);
        btncancel = (Button) findViewById(R.id.cancel);

        txtFromDate=(TextView) findViewById(R.id.txtfromdate);
        txtToDate=(TextView) findViewById(R.id.txttodate);

        jsonarray=new JSONArray();
        mPreferenceHelper=new PreferenceHelper(this);

        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        //2015-05-24 06:22:22
        dateFormatter01=new SimpleDateFormat("yyyy-MM-dd");

/*        final TextView spzoneTextView=(TextView) promptView.findViewById(R.id.spzoneTextView);
        final TextView spresionTextView=(TextView) promptView.findViewById(R.id.spresionTextView);
        final TextView spgroupchurchTextView=(TextView) promptView.findViewById(R.id.spgroupchurchTextView);
        final TextView spchurchTextView=(TextView) promptView.findViewById(R.id.spchurchTextView);
        final TextView sppcfTextView=(TextView) promptView.findViewById(R.id.sppcfTextView);
        final TextView spSeniorCellTextView=(TextView) promptView.findViewById(R.id.spSeniorCellTextView);*/
        final TextView spCellTextView=(TextView) findViewById(R.id.spCellTextView);

/*        spresion=(Spinner) promptView.findViewById(R.id.spresion);
        spzone=(Spinner) promptView.findViewById(R.id.spzone);
        sppcf=(Spinner) promptView.findViewById(R.id.sppcf);
        spgroupchurch=(Spinner) promptView.findViewById(R.id.spgroupchurch);
        spchurch =(Spinner) promptView.findViewById(R.id.spchurch);
        spSeniorCell=(Spinner) promptView.findViewById(R.id.spSeniorCell);*/
        spCell=(Spinner) findViewById(R.id.spCell);

        mZoneList=new ArrayList<String>();
        mRegionList=new ArrayList<String>();
        mChurchList=new ArrayList<String>();
        mSeniorCellList=new ArrayList<String>();
        mGrpChurchList=new ArrayList<String>();
        mPCFList=new ArrayList<String>();
        mCellList=new ArrayList<String>();

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
                String cell;
                try{
                    cell=spCell.getSelectedItem().toString();
                }catch(Exception ex){
                    cell="";
                }
                    Intent Int = new Intent(CAttendanceHistory.this, MeetingListActivity.class);
                    Int.putExtra("fdate", txtFromDate.getText().toString());
                    Int.putExtra("tdate", txtToDate.getText().toString());
                    Int.putExtra("cellcode",cell);
                    Int.putExtra("attendance_type", "Cell Meeting");
                    Int.putExtra("role","Cell Leader");
                    Int.putExtra("cellah", "cellah");
                    startActivity(Int);
                    finish();
            }
        });

        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent actInt = new Intent(CAttendanceHistory.this, HomeActivity.class);
                startActivity(actInt);
                finish();
            }
        });

        spCellTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                mCellList.add(mPreferenceHelper.getString(SynergyValues.Commons.USER_DEFVALUE));
                spCell.setVisibility(View.VISIBLE);
                spCellTextView.setVisibility(View.GONE);
                setAdapters();
            }
        });
    }

    private void setAdapters() {

/*        ArrayAdapter<String> adapterZone = new ArrayAdapter<String>(AllMemberListActivity.this, android.R.layout.simple_spinner_item, mZoneList);
        adapterZone.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spzone.setAdapter(adapterZone);

        ArrayAdapter<String> adapterRegion = new ArrayAdapter<String>(AllMemberListActivity.this, android.R.layout.simple_spinner_item, mRegionList);
        adapterRegion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spresion.setAdapter(adapterRegion);

        ArrayAdapter<String> adapterChurch = new ArrayAdapter<String>(AllMemberListActivity.this, android.R.layout.simple_spinner_item, mChurchList);
        adapterChurch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spchurch.setAdapter(adapterChurch);

        ArrayAdapter<String> adapterSrCell = new ArrayAdapter<String>(AllMemberListActivity.this, android.R.layout.simple_spinner_item, mSeniorCellList);
        adapterSrCell.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSeniorCell.setAdapter(adapterSrCell);

        ArrayAdapter<String> adapterchurchgropu = new ArrayAdapter<String>(AllMemberListActivity.this, android.R.layout.simple_spinner_item, mGrpChurchList);
        adapterchurchgropu.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spgroupchurch.setAdapter(adapterchurchgropu);

        ArrayAdapter<String> adapterPCF = new ArrayAdapter<String>(AllMemberListActivity.this, android.R.layout.simple_spinner_item, mPCFList);
        adapterPCF.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sppcf.setAdapter(adapterPCF);*/

        ArrayAdapter<String> adaptercell = new ArrayAdapter<String>(CAttendanceHistory.this, android.R.layout.simple_spinner_item, mCellList);
        adaptercell.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCell.setAdapter(adaptercell);

    }
}
