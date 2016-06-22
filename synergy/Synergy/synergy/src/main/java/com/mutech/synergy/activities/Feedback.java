package com.mutech.synergy.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mutech.synergy.R;
import com.mutech.synergy.activities.cellMasters.MasterSelectorScreenActivity;
import com.mutech.synergy.activities.cellMasters.PartnerShipRecord;
import com.mutech.synergy.activities.cellMasters.SearchFunctionActivity;
import com.mutech.synergy.activities.event.MyEventListActivity;
import com.mutech.synergy.activities.meeting.MyMeetingListActivity;
import com.mutech.synergy.activities.profile.MyProfileActivity;
import com.mutech.synergy.activities.task.ToDoTaskActivity;
import com.mutech.synergy.adapters.CustomDrawerAdapter;
import com.mutech.synergy.models.DrawerItem;

import java.util.ArrayList;

public class Feedback extends ActionBarActivity  {

    private Button submit,cancel;
    private EditText txtsubject,txtdesc;
    private ArrayList<DrawerItem> mDrawerList;
    private CustomDrawerAdapter mCustomDrawerAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mLvDrawer;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        submit=(Button) findViewById(R.id.submit);
        cancel=(Button) findViewById(R.id.cancel);
        txtsubject=(EditText) findViewById((R.id.edtsubject));
        txtdesc=(EditText) findViewById((R.id.edtdescription));

        mDrawerList = new ArrayList<DrawerItem>();
        addDrawerListData();

        mLvDrawer = (ListView) findViewById(R.id.left_drawer);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mCustomDrawerAdapter = new CustomDrawerAdapter(this,R.layout.custom_dashboard_drawer_item, mDrawerList);
        mLvDrawer.setAdapter(mCustomDrawerAdapter);
        mLvDrawer.setOnItemClickListener(new DrawerItemClickListener());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.actiontop);

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                R.drawable.dashboard,
                R.string.app_name,
                R.string.app_name
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                //getSupportActionBar().setTitle(mTitle);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //getSupportActionBar().setTitle(mDrawerTitle);
            }
        };

        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
//                submit.setEnabled(false);
                new AlertDialog.Builder(Feedback.this)
                        .setCancelable(false)
                        .setTitle("Feedback")
                        .setMessage("Your feedback has been registered successfully. Thank you")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                Intent intent = new Intent(Intent.ACTION_SENDTO); // it's not ACTION_SEND
                                intent.setType("Text/Play");
                                intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
                                intent.putExtra(Intent.EXTRA_TEXT, "Subject: " +  txtsubject.getText().toString() + "\n" + "Description: " + txtdesc.getText().toString());
                                intent.setData(Uri.parse("mailto:poojapatil96km@gmail.com")); // or just "mailto:" for blank
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
                                startActivity(intent);
                                txtsubject.setText("");
                                txtdesc.setText("");
                              /*  Intent emailIntent = new Intent(Intent.ACTION_SEND);
                                emailIntent.putExtra(Intent.EXTRA_EMAIL, "poojapatil96km@gmail.com");
                                emailIntent.setData(Uri.parse("mailto:" + txtsubject.getText().toString() + " " + txtdesc.getText().toString()));
                                emailIntent.setType("text/plain");

                                try {
                                    startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                                    finish();
//                                    Log.i("Finished sending email...", "");
                                }
                                catch (android.content.ActivityNotFoundException ex) {
                                    Toast.makeText(Feedback.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
                                }*/

                              /*  Intent actInt = new Intent(Feedback.this,
                                        HomeActivity.class);
                                startActivity(actInt);
                                finish();*/

                            }
                        })
                        .show();
            }

        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent actInt = new Intent(Feedback.this,
                        HomeActivity.class);
                startActivity(actInt);
                finish();
            }

        });
    }

    private void addDrawerListData() {
        DrawerItem item01 = new DrawerItem();
        item01.setItemName("Dashboard");
        item01.setImgResID(R.drawable.dashboard);

        DrawerItem item05 = new DrawerItem();
        item05.setItemName("My Profile");
        item05.setImgResID(R.drawable.myprofile);

//	DrawerItem item03 = new DrawerItem();
//	item03.setItemName("Ministry \n Material");
//	item03.setImgResID(R.drawable.ministry_materials);

        DrawerItem item03 = new DrawerItem();
        item03.setItemName("Partnership \n Records");
        item03.setImgResID(R.drawable.partnership_record);

        DrawerItem item04 = new DrawerItem();
        item04.setItemName("Database");
        item04.setImgResID(R.drawable.database);


        DrawerItem item06 = new DrawerItem();
        item06.setItemName("Search");
        item06.setImgResID(R.drawable.search);

        DrawerItem item07 = new DrawerItem();
        item07.setItemName("Attendance");
        item07.setImgResID(R.drawable.my_meetings);

        DrawerItem item08 = new DrawerItem();
        item08.setItemName("Calendar");
        item08.setImgResID(R.drawable.carlender);

        DrawerItem item9 = new DrawerItem();
        item9.setItemName("To Do");
        item9.setImgResID(R.drawable.todo);

        DrawerItem item10 = new DrawerItem();
        item10.setItemName("Broadcast Message");
        item10.setImgResID(R.drawable.msg);

        DrawerItem item11=new DrawerItem();
        item11.setItemName("Feedback");
        item11.setImgResID(R.drawable.msg);

        DrawerItem item12=new DrawerItem();
        item12.setItemName("Logout");
        item12.setImgResID(R.drawable.signout);

        mDrawerList.add(item01);
        mDrawerList.add(item05);
        mDrawerList.add(item04);
//	mDrawerList.add(item02);
//	mDrawerList.add(item03);
        mDrawerList.add(item03);

//	mDrawerList.add(item06);
        mDrawerList.add(item07);
        mDrawerList.add(item08);
        mDrawerList.add(item9);
        mDrawerList.add(item10);
        mDrawerList.add(item06);
        mDrawerList.add(item11);
        mDrawerList.add(item12);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void selectItem(int position) {
        switch (position) {
            case 0:

                Intent int1=new Intent(this,HomeActivity.class);
                int1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(int1);
                finish();
                break;
            case 2:
                Intent intForm=new Intent(this,MasterSelectorScreenActivity.class);
                intForm.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intForm);
                finish();
                break;
//	case 2:
//		break;
//	case 3:
//		break;
            case 3:
                Intent partner=new Intent(this,PartnerShipRecord.class);
                partner.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(partner);
                finish();
                break;
            case 1:
                Intent intForm1=new Intent(this,MyProfileActivity.class);
                intForm1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intForm1);
                finish();
                break;

            case 4:
                //Intent intMeeting=new Intent(this,MeetingListActivity.class);
                //startActivity(intMeeting);
                Intent intMyMeetings=new Intent(this,MyMeetingListActivity.class);
                intMyMeetings.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intMyMeetings);
                finish();
                break;
            case 5:
                Intent intEvents=new Intent(this,MyEventListActivity.class);
                intEvents.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intEvents);
                finish();
                break;
            case 6:
                Intent intentTODO = new Intent(this, ToDoTaskActivity.class);
                intentTODO.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intentTODO);
                finish();
                break;

            case 7:
//		Intent intentMsg = new Intent(this, MessageBroadcastActivity.class);
//		startActivity(intentMsg);
                break;

            case 8:
                Intent intSearchMembers=new Intent(this,SearchFunctionActivity.class);
                intSearchMembers.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intSearchMembers);
                finish();
                break;

            case 9:
//			mPreferenceHelper.addBoolean(Commons.ISUSER_LOGGEDIN, false);
//			mPreferenceHelper.addString(Commons.USER_EMAILID, null);
//			mPreferenceHelper.addString(Commons.USER_PASSWORD, null);

                Intent intfeedback=new Intent(this,Feedback.class);
                intfeedback.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intfeedback);
                finish();
                break;

            case 10://logout
//			mPreferenceHelper.addBoolean(Commons.ISUSER_LOGGEDIN, false);
//			mPreferenceHelper.addString(Commons.USER_EMAILID, null);
//			mPreferenceHelper.addString(Commons.USER_PASSWORD, null);
                Intent intLogout=new Intent(this,LogoutActivity.class);
                intLogout.putExtra("classname", "Feedback");
                startActivity(intLogout);
                finish();
                break;

            default:
                break;
        }

        mDrawerLayout.closeDrawer(mLvDrawer);
//	getSupportActionBar().invalidateOptionsMenu();
    }


    private class DrawerItemClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            selectItem(position);

        }
    }


    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog dialog =new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Closing Activity")
                    .setMessage("Are you sure you want to exit?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            moveTaskToBack(true);
                        }

                    })
                    .setNegativeButton("No", null)
                    .show();
            TextView textView = (TextView) dialog.findViewById(android.R.id.message);
            textView.setTextSize(18);
        }
    }
}
