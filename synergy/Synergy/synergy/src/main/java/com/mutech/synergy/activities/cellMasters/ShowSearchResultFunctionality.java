package com.mutech.synergy.activities.cellMasters;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ActionBar.LayoutParams;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.mutech.databasedetails.CellDetailsActivity;
import com.mutech.databasedetails.ChurchDetail;
import com.mutech.databasedetails.FirstTimerInDatabaseActivity;
import com.mutech.databasedetails.GroupChurch;
import com.mutech.databasedetails.PcfDetailsActivity;
import com.mutech.databasedetails.RegionActivity;
import com.mutech.databasedetails.SrCellDetailsActivity;
import com.mutech.databasedetails.ZoneDetailsActivity;
import com.mutech.synergy.App;
import com.mutech.synergy.GraphTestActivity;
import com.mutech.synergy.R;
import com.mutech.synergy.SynergyValues.Commons;
import com.mutech.synergy.SynergyValues.Web.GetAllListMastersService;
import com.mutech.synergy.SynergyValues.Web.GetHigherHierarchyService;
import com.mutech.synergy.SynergyValues.Web.LowerHierarchyService;
import com.mutech.synergy.SynergyValues.Web.SearchService;
import com.mutech.synergy.SynergyValues.Web.ShowAllMembersService;

import com.mutech.synergy.activities.AttendanceHistory;
import com.mutech.synergy.activities.CellLeaderMsg;
import com.mutech.synergy.activities.ChurchPastorMsg;
import com.mutech.synergy.activities.ShortBioChurch;
import com.mutech.synergy.activities.ViewMembers;
import com.mutech.synergy.activities.event.CreateEventActivity;
import com.mutech.synergy.activities.meeting.MeetingListActivity;
import com.mutech.synergy.models.MeetingListRequestModel;
import com.mutech.synergy.models.ResponseMessageModel2;
import com.mutech.synergy.models.SearchModel;
import com.mutech.synergy.models.SearchResultModel;
import com.mutech.synergy.models.SearchResultModel.SearchResultSubModel;
import com.mutech.synergy.utils.Methods;
import com.mutech.synergy.utils.NetworkHelper;
import com.mutech.synergy.utils.PreferenceHelper;

public class ShowSearchResultFunctionality extends ActionBarActivity{

	
	Spinner spresion,spzone,sppcf,spgroupchurch,spchurch,spSeniorCell,spCell;
	private ArrayList<String> mZoneList,mRegionList,mChurchList,mSeniorCellList,mGrpChurchList,mPCFList,mCellList;
	
	TextView txtFromDate,txtToDate;
	private DatePickerDialog fromDatePickerDialog,toDatePickerDialog;
	private SimpleDateFormat dateFormatter,dateFormatter01;
	Calendar newCalendar;
	String UserRoll;
	//ImageView filterimg;

	private Dialog dialogPopup=null;
	private Dialog dialogPopup1=null;
	private Button viewchurchattendancehistory,viewmembers,viewmembershipstrength,msgchurchpastor,viewnewconverts,viewpartnershipinfo,viewfirsttimers,viewcellspcf,btncell,btnseniorcell,btnpcf;
	
	private ListView lvShowSearch;
	private ArrayList<SearchResultSubModel> subModelList;

	private PreferenceHelper mPreferenceHelper;
	private Gson gson;
	private String zone,region,church,group_church,search,member;
	EditText etName;

	JSONArray jarray;
	
	public int TOTAL_LIST_ITEMS;
    public int NUM_ITEMS_PAGE   = 20;
    private int noOfBtns;
    private Button[] btns;
	
    TextView textView1;
    
    JSONObject jsonobj;
	JSONArray jsonarray;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_allmembers);
		initialize();
	}

	@SuppressLint("NewApi")
	private void initialize() {

		getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		getSupportActionBar().setHomeButtonEnabled(false);
		getSupportActionBar().setCustomView(R.layout.custom_actionbar);
		textView1=(TextView)getSupportActionBar().getCustomView().findViewById(R.id.title_text);
		textView1.setText("Search Result       ");
		
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2E9AFE")));
		
		getSupportActionBar().setDisplayShowCustomEnabled(true);

		
		jarray=new JSONArray();
		
		lvShowSearch=(ListView) findViewById(R.id.lvAllMembers);
		subModelList=new ArrayList<SearchResultSubModel>();

		zone=getIntent().getExtras().getString("Zone");
		region=getIntent().getExtras().getString("Region");
		church=getIntent().getExtras().getString("Church");
		group_church=getIntent().getExtras().getString("GroupChurch");
		search=getIntent().getExtras().getString("Search");
		if(null !=getIntent().getExtras().getString("Member") && getIntent().getExtras().getString("Member").trim().length() >0){
			member=getIntent().getExtras().getString("Member");
		}

		gson=new Gson();
		mPreferenceHelper=new PreferenceHelper(this);
		
	//	filterimg=(ImageView) findViewById(R.id.imageView2);
	//	textView1=(TextView) findViewById(R.id.textView1);
		textView1.setText("Search Result");
	
		mZoneList=new ArrayList<String>();
		mRegionList=new ArrayList<String>();
		mChurchList=new ArrayList<String>();
		mSeniorCellList=new ArrayList<String>();
		mGrpChurchList=new ArrayList<String>();
		mPCFList=new ArrayList<String>();
		mCellList=new ArrayList<String>();
	
		UserRoll=mPreferenceHelper.getString(Commons.USER_ROLE);
		
		dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
		//2015-05-24 06:22:22
		dateFormatter01=new SimpleDateFormat("yyyy-MM-dd");

		newCalendar = Calendar.getInstance();
		
		fromDatePickerDialog = new DatePickerDialog(ShowSearchResultFunctionality.this, new OnDateSetListener() {

			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
				txtFromDate.setText(dateFormatter.format(newDate.getTime()));
			}

		},newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

		toDatePickerDialog = new DatePickerDialog(ShowSearchResultFunctionality.this, new OnDateSetListener() {
			
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
				txtToDate.setText(dateFormatter.format(newDate.getTime()));
			}
		
		},newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

		
		lvShowSearch.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, final long id) {
				// TODO Auto-generated method stub
				
				try {
					final String memberid=jarray.getJSONObject(position).getString("id");
					String type=jarray.getJSONObject(position).getString("type");
					final String name=jarray.getJSONObject(position).getString("id");

					mPreferenceHelper.addString(Commons.FROM_ACTIVITY2, "true");
					dialogPopup = new Dialog(ShowSearchResultFunctionality.this);
					dialogPopup.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialogPopup.setContentView(R.layout.custom_search_dialogbox);

					viewchurchattendancehistory= (Button) dialogPopup.findViewById(R.id.viewchurchattendancehistory);
					viewmembers= (Button) dialogPopup.findViewById(R.id.viewmembers);
					viewmembershipstrength= (Button) dialogPopup.findViewById(R.id.viewmembershipstrength);
					msgchurchpastor= (Button) dialogPopup.findViewById(R.id.msgchurchpastor);
					viewnewconverts= (Button) dialogPopup.findViewById(R.id.viewnewconverts);
					viewpartnershipinfo= (Button) dialogPopup.findViewById(R.id.viewpartnershipinfo);
					viewfirsttimers= (Button) dialogPopup.findViewById(R.id.viewfirsttimers);
					viewcellspcf= (Button) dialogPopup.findViewById(R.id.viewcellspcf);
					
					if(type.equals("Regions")){
						viewchurchattendancehistory.setText("View Region attendance history");
						msgchurchpastor.setText("Send message to the Region Pastor");
						viewcellspcf.setText("View Zones in the Region");
						dialogPopup.show();

						viewchurchattendancehistory.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {

								Intent Int = new Intent(ShowSearchResultFunctionality.this, AttendanceHistory.class);
								Int.putExtra("cellcode", name);
								Int.putExtra("churchah", "churchah");
								Int.putExtra("role", "Region");
								startActivity(Int);
								dialogPopup.dismiss();
							}
						});

						viewmembers.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {

								Intent Int = new Intent(ShowSearchResultFunctionality.this, AllMemberListActivity.class);
								Int.putExtra("cellcode", name);
								Int.putExtra("role","Region");
								startActivity(Int);
								dialogPopup.dismiss();
							}
						});

						viewmembershipstrength.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {

								Intent Int = new Intent(ShowSearchResultFunctionality.this, AttendanceHistory.class);
								Int.putExtra("cellcode", name);
								Int.putExtra("role","Membership Strength");
								Int.putExtra("tbl", "Regions");
								startActivity(Int);
								dialogPopup.dismiss();
							}
						});

						viewnewconverts.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {

								Intent Int = new Intent(ShowSearchResultFunctionality.this, FirstTimerInDatabaseActivity.class);
								Int.putExtra("cellcode", name);
								Int.putExtra("role","Region New Converts");
								startActivity(Int);
								dialogPopup.dismiss();
							}
						});

						viewfirsttimers.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {

								Intent Int = new Intent(ShowSearchResultFunctionality.this, FirstTimerInDatabaseActivity.class);
								Int.putExtra("cellcode", name);
								Int.putExtra("role", "Region First Timer");
								startActivity(Int);
								dialogPopup.dismiss();
							}
						});

						viewpartnershipinfo.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {

								Intent Int = new Intent(ShowSearchResultFunctionality.this, PartnerShipRecord.class);
								mPreferenceHelper.addString(Commons.REGION, name);
								mPreferenceHelper.addString(Commons.USER_REGION, "true");
								mPreferenceHelper.addString(Commons.USER_REGION1, "true");
								Int.putExtra("fromactivity", "Allmemberlist");
								startActivity(Int);
								dialogPopup.dismiss();
							}
						});


						msgchurchpastor.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {

								Intent Int = new Intent(ShowSearchResultFunctionality.this, ChurchPastorMsg.class);
								Int.putExtra("cellcode", name);
								startActivity(Int);
								dialogPopup.dismiss();
							}
						});

						viewcellspcf.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {

								Intent Int = new Intent(ShowSearchResultFunctionality.this, ShortBioChurch.class);
								Int.putExtra("role","Region");
								Int.putExtra("tbl", "Regions");
								Int.putExtra("cellcode", name);
								startActivity(Int);
								dialogPopup.dismiss();
							}
						});
					//	startActivity(new Intent(ShowSearchResultFunctionality.this, RegionActivity.class).putExtra("cellcode", memberid));
					}
					
					if(type.equals("Zones")){
						viewchurchattendancehistory.setText("View Zone attendance history");
						msgchurchpastor.setText("Send message to the Zone Pastor");
						viewcellspcf.setText("View Group Churches in the Zone");
						dialogPopup.show();

						viewchurchattendancehistory.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {

								Intent Int = new Intent(ShowSearchResultFunctionality.this, AttendanceHistory.class);
								Int.putExtra("cellcode", name);
								Int.putExtra("churchah", "churchah");
								Int.putExtra("role", "Zone");
								startActivity(Int);
								dialogPopup.dismiss();
							}
						});

						viewmembers.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {

								Intent Int = new Intent(ShowSearchResultFunctionality.this, AllMemberListActivity.class);
								Int.putExtra("cellcode", name);
								Int.putExtra("role","Zone");
								startActivity(Int);
								dialogPopup.dismiss();
							}
						});

						viewmembershipstrength.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {

								Intent Int = new Intent(ShowSearchResultFunctionality.this, AttendanceHistory.class);
								Int.putExtra("cellcode", name);
								Int.putExtra("role","Membership Strength");
								Int.putExtra("tbl", "Zones");
								startActivity(Int);
								dialogPopup.dismiss();
							}
						});

						viewnewconverts.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {

								Intent Int = new Intent(ShowSearchResultFunctionality.this, FirstTimerInDatabaseActivity.class);
								Int.putExtra("cellcode", name);
								Int.putExtra("role","Zone New Converts");
								startActivity(Int);
								dialogPopup.dismiss();
							}
						});

						viewfirsttimers.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {

								Intent Int = new Intent(ShowSearchResultFunctionality.this, FirstTimerInDatabaseActivity.class);
								Int.putExtra("cellcode", name);
								Int.putExtra("role", "Zone First Timer");
								startActivity(Int);
								dialogPopup.dismiss();
							}
						});

						viewpartnershipinfo.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {

								Intent Int = new Intent(ShowSearchResultFunctionality.this, PartnerShipRecord.class);
								mPreferenceHelper.addString(Commons.ZONE, name);
								mPreferenceHelper.addString(Commons.USER_ZONE, "true");
								mPreferenceHelper.addString(Commons.USER_ZONE1, "true");
								Int.putExtra("fromactivity", "Allmemberlist");
								startActivity(Int);
								dialogPopup.dismiss();
							}
						});


						msgchurchpastor.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {

								Intent Int = new Intent(ShowSearchResultFunctionality.this, ChurchPastorMsg.class);
								Int.putExtra("cellcode", name);
								startActivity(Int);
								dialogPopup.dismiss();
							}
						});

						viewcellspcf.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {

								Intent Int = new Intent(ShowSearchResultFunctionality.this, ShortBioChurch.class);
								Int.putExtra("role","Zone");
								Int.putExtra("tbl", "Zones");
								Int.putExtra("cellcode", name);
								startActivity(Int);
								dialogPopup.dismiss();
							}
						});

					//	startActivity(new Intent(ShowSearchResultFunctionality.this, ZoneDetailsActivity.class).putExtra("cellcode", memberid));
					}
					
					if(type.equals("Group Churches")){
						viewchurchattendancehistory.setText("View Group Church attendance history");
						msgchurchpastor.setText("Send message to the Group Church Pastor");
						viewcellspcf.setText("View churches in the Group Church");
						dialogPopup.show();

						viewchurchattendancehistory.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {

								Intent Int = new Intent(ShowSearchResultFunctionality.this, AttendanceHistory.class);
								Int.putExtra("cellcode", name);
								Int.putExtra("churchah", "churchah");
								Int.putExtra("role", "Group Church");
								startActivity(Int);
								dialogPopup.dismiss();
							}
						});

						viewmembers.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {

								Intent Int = new Intent(ShowSearchResultFunctionality.this, AllMemberListActivity.class);
								Int.putExtra("cellcode", name);
								Int.putExtra("role","Group Church");
								startActivity(Int);
								dialogPopup.dismiss();
							}
						});

						viewmembershipstrength.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {

								Intent Int = new Intent(ShowSearchResultFunctionality.this, AttendanceHistory.class);
								Int.putExtra("cellcode", name);
								Int.putExtra("tbl", "Group Churches");
								Int.putExtra("role","Membership Strength");
								startActivity(Int);
								dialogPopup.dismiss();
							}
						});


						viewnewconverts.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {

								Intent Int = new Intent(ShowSearchResultFunctionality.this, FirstTimerInDatabaseActivity.class);
								Int.putExtra("cellcode", name);
								Int.putExtra("role","Group Church New Converts");
								startActivity(Int);
								dialogPopup.dismiss();
							}
						});

						viewfirsttimers.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {

								Intent Int = new Intent(ShowSearchResultFunctionality.this, FirstTimerInDatabaseActivity.class);
								Int.putExtra("cellcode", name);
								Int.putExtra("role", "Group Church First Timer");
								startActivity(Int);
								dialogPopup.dismiss();
							}
						});

						viewpartnershipinfo.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {

								Intent Int = new Intent(ShowSearchResultFunctionality.this, PartnerShipRecord.class);
								mPreferenceHelper.addString(Commons.CHURCH_GROUP, name);
								mPreferenceHelper.addString(Commons.USER_CHURCH_GROUP, "true");
								mPreferenceHelper.addString(Commons.USER_CHURCH_GROUP1, "true");
								Int.putExtra("fromactivity", "Allmemberlist");
								startActivity(Int);
								dialogPopup.dismiss();
							}
						});


						msgchurchpastor.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {

								Intent Int = new Intent(ShowSearchResultFunctionality.this, ChurchPastorMsg.class);
								Int.putExtra("cellcode", name);
								startActivity(Int);
								dialogPopup.dismiss();
							}
						});

						viewcellspcf.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {

								Intent Int = new Intent(ShowSearchResultFunctionality.this, ShortBioChurch.class);
								Int.putExtra("role","GroupChurch");
								Int.putExtra("tbl", "Group Churches");
								Int.putExtra("cellcode", name);
								startActivity(Int);
								dialogPopup.dismiss();
							}
						});
						//startActivity(new Intent(ShowSearchResultFunctionality.this, GroupChurch.class).putExtra("cellcode", memberid));
					}
					
					if(type.equals("Churches")){
						//final String name=jarray.getJSONObject(position).getString("id");
						//final String name=jarray.getJSONObject(position).getString("name");

						dialogPopup.show();


						viewchurchattendancehistory.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {

								Intent Int = new Intent(ShowSearchResultFunctionality.this, AttendanceHistory.class);
								Int.putExtra("cellcode", name);
								Int.putExtra("churchah", "churchah");
								Int.putExtra("role", "Church");
								startActivity(Int);
								dialogPopup.dismiss();
							}
						});

						viewmembers.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {

								Intent Int = new Intent(ShowSearchResultFunctionality.this, AllMemberListActivity.class);
								Int.putExtra("cellcode", name);
								Int.putExtra("role","Church");
								startActivity(Int);
								dialogPopup.dismiss();
							}
						});

						viewmembershipstrength.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {

								Intent Int = new Intent(ShowSearchResultFunctionality.this, AttendanceHistory.class);
								Int.putExtra("cellcode", name);
								Int.putExtra("role","Membership Strength");
								Int.putExtra("tbl", "Churches");
								startActivity(Int);
								dialogPopup.dismiss();
							}
						});

						viewnewconverts.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {

								Intent Int = new Intent(ShowSearchResultFunctionality.this, FirstTimerInDatabaseActivity.class);
								Int.putExtra("cellcode", name);
								Int.putExtra("role","New Converts");
								startActivity(Int);
								dialogPopup.dismiss();
							}
						});

						viewfirsttimers.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {

								Intent Int = new Intent(ShowSearchResultFunctionality.this, FirstTimerInDatabaseActivity.class);
								Int.putExtra("cellcode", name);
								Int.putExtra("role","First Timer");
								startActivity(Int);
								dialogPopup.dismiss();
							}
						});

						viewpartnershipinfo.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {

								Intent Int = new Intent(ShowSearchResultFunctionality.this, PartnerShipRecord.class);
								mPreferenceHelper.addString(Commons.CHURCH, name);
								mPreferenceHelper.addString(Commons.USER_CHURCH, "true");
								mPreferenceHelper.addString(Commons.USER_CHURCH1, "true");
								Int.putExtra("fromactivity", "Allmemberlist");
								startActivity(Int);
								dialogPopup.dismiss();
							}
						});


						msgchurchpastor.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {

								Intent Int = new Intent(ShowSearchResultFunctionality.this, ChurchPastorMsg.class);
								Int.putExtra("cellcode", name);
								startActivity(Int);
								dialogPopup.dismiss();
							}
						});

						viewcellspcf.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {

								dialogPopup1 = new Dialog(ShowSearchResultFunctionality.this);
								dialogPopup1.requestWindowFeature(Window.FEATURE_NO_TITLE);
								dialogPopup1.setContentView(R.layout.custom_cell_pcf_dialogbox);

								btncell= (Button) dialogPopup1.findViewById(R.id.cell);
								btnpcf= (Button) dialogPopup1.findViewById(R.id.pcf);
								btnseniorcell= (Button) dialogPopup1.findViewById(R.id.seniorcell);

								dialogPopup1.show();
								dialogPopup.dismiss();

								btnseniorcell.setOnClickListener(new View.OnClickListener() {
									@Override
									public void onClick(View v) {

										Intent Int = new Intent(ShowSearchResultFunctionality.this, ShortBioChurch.class);
										Int.putExtra("role", "Church");
										Int.putExtra("cellcode", name);
										Int.putExtra("tbl", "Churches");
										Int.putExtra("key", "Senior Cells");
										startActivity(Int);
										dialogPopup1.dismiss();
									}
								});

								btncell.setOnClickListener(new View.OnClickListener() {
									@Override
									public void onClick(View v) {

										Intent Int = new Intent(ShowSearchResultFunctionality.this, ShortBioChurch.class);
										Int.putExtra("role", "Church");
										Int.putExtra("cellcode", name);
										Int.putExtra("tbl", "Churches");
										Int.putExtra("key", "Cells");
										startActivity(Int);
										dialogPopup1.dismiss();
									}
								});

								btnpcf.setOnClickListener(new View.OnClickListener() {
									@Override
									public void onClick(View v) {

										Intent Int = new Intent(ShowSearchResultFunctionality.this, ShortBioChurch.class);
										Int.putExtra("role", "Church");
										Int.putExtra("cellcode", name);
										Int.putExtra("tbl", "Churches");
										Int.putExtra("key", "PCFs");
										startActivity(Int);
										dialogPopup1.dismiss();
									}
								});
							}
						});


							//startActivity(new Intent(ShowSearchResultFunctionality.this, ChurchDetail.class).putExtra("cellcode", memberid));
					}

					
					if(type.equals("PCFs")){
						startActivity(new Intent(ShowSearchResultFunctionality.this, PcfDetailsActivity.class).putExtra("cellcode", memberid));
					}
					
					if(type.equals("Senior Cells")){
						startActivity(new Intent(ShowSearchResultFunctionality.this, SrCellDetailsActivity.class).putExtra("cellcode", memberid));
					}

					if(type.equals("Cells")){

						viewchurchattendancehistory.setText("View Cell attendance history");
						msgchurchpastor.setText("Send message to the Cell Pastor");
						viewcellspcf.setVisibility(View.GONE);
					//	viewcellspcf.setText("View Cell in the Region");
						dialogPopup.show();


						viewchurchattendancehistory.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {

								Intent Int = new Intent(ShowSearchResultFunctionality.this, AttendanceHistory.class);
								Int.putExtra("cellcode", name);
								Int.putExtra("role", "Cells");
								startActivity(Int);
								dialogPopup.dismiss();
							}
						});

				/*		viewmembers.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {

								Intent Int = new Intent(ShowSearchResultFunctionality.this, AllMemberListActivity.class);
								Int.putExtra("cellcode", name);
								Int.putExtra("role","Cell Leader");
								startActivity(Int);
								dialogPopup.dismiss();
							}
						});

						viewmembershipstrength.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {

								Intent Int = new Intent(ShowSearchResultFunctionality.this, AttendanceHistory.class);
								Int.putExtra("cellcode", name);
								Int.putExtra("role", "Membership Strength");
								Int.putExtra("tbl", "Cells");
								startActivity(Int);
								dialogPopup.dismiss();
							}
						});

						viewnewconverts.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {

								Intent Int = new Intent(ShowSearchResultFunctionality.this, FirstTimerInDatabaseActivity.class);
								Int.putExtra("cellcode", name);
								Int.putExtra("role", "Cell Leader New Converts");
								startActivity(Int);
								dialogPopup.dismiss();
							}
						});

						viewfirsttimers.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {

								Intent Int = new Intent(ShowSearchResultFunctionality.this, FirstTimerInDatabaseActivity.class);
								Int.putExtra("cellcode", name);
								Int.putExtra("role", "Cell Leader First Timer");
								startActivity(Int);
								dialogPopup.dismiss();
							}
						});

						viewpartnershipinfo.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {

								Intent Int = new Intent(ShowSearchResultFunctionality.this, PartnerShipRecord.class);
								mPreferenceHelper.addString(Commons.REGION, name);
								mPreferenceHelper.addString(Commons.USER_REGION, "true");
								mPreferenceHelper.addString(Commons.USER_REGION1, "true");
								Int.putExtra("fromactivity", "Allmemberlist");
								startActivity(Int);
								dialogPopup.dismiss();
							}
						});


						msgchurchpastor.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {

								Intent Int = new Intent(ShowSearchResultFunctionality.this, ChurchPastorMsg.class);
								Int.putExtra("cellcode", name);
								startActivity(Int);
								dialogPopup.dismiss();
							}
						});*/


					/*	Intent Int = new Intent(ShowSearchResultFunctionality.this, AttendanceHistory.class);
						Int.putExtra("cellcode", name);
						Int.putExtra("role", "Cells");
						startActivity(Int)*/;

					//	startActivity(new Intent(ShowSearchResultFunctionality.this, CellDetailsActivity.class).putExtra("cellcode", memberid));
					}

					if(type.equals("Members")){
						
						Intent intMemberDetails=new Intent(ShowSearchResultFunctionality.this,MemberInfoActivtiy.class);
						intMemberDetails.putExtra("MemberNo",memberid);
						startActivity(intMemberDetails);
					}
					
					
					
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				
			}
		});


		if(NetworkHelper.isOnline(this)){
			Methods.showProgressDialog(this);
			callsearchUsers("1");
		}else
		{
			Methods.longToast("Please connect to Internet", this);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_todo, menu);
		String roll=mPreferenceHelper.getString(Commons.USER_ROLE);
	
		
		
		
			MenuItem item = (MenuItem) menu.findItem(R.id.menu_addTo);
            item.setVisible(false);
		
            MenuItem item1 = (MenuItem) menu.findItem(R.id.menu_option2);
            item1.setVisible(false);
		
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
		case R.id.menu_addTo:
			
			Intent intCreateEvent=new Intent(ShowSearchResultFunctionality.this,CreateEventActivity.class);
			startActivity(intCreateEvent);
			
			break;

		case R.id.menu_option2:
			Intent intMyEventList=new Intent(ShowSearchResultFunctionality.this,MeetingListActivity.class);
			startActivity(intMyEventList);
			break;
			
		case R.id.menu_option3:
		
			showDialog();	
			
			
			break;

		default:
			break;
		}
		return false;
	}
	
	private void callsearchUsers(final String pageno) {

		StringRequest reqPostSearch=new StringRequest(Method.POST,SearchService.SERVICE_URL,new Listener<String>() {
			@Override
			public void onResponse(String response) {
				Methods.closeProgressDialog();
				Log.e("Responce","get all pcf ---------------"+ response);

				if(response.contains("status")){
					ResponseMessageModel2 resp2Model=gson.fromJson(response, ResponseMessageModel2.class);
					Methods.longToast(resp2Model.getMessage().getMessage(), ShowSearchResultFunctionality.this);
				}else{
					/*SearchResultModel model=gson.fromJson(response, SearchResultModel.class);
					if(null !=model.getMessage() && model.getMessage().size() >0){
						subModelList=new ArrayList<SearchResultSubModel>();
						for(int i=0;i<model.getMessage().size();i++){
							subModelList.add(model.getMessage().get(i));
							Log.d("droid", "subModelList :::::::"+model.getMessage().get(i));
						}
						Log.d("droid", "subModelList final size ::: "+subModelList.size());
						
						
						SearchListAdapter adapter=new SearchListAdapter(ShowSearchResultFunctionality.this,subModelList);
						lvShowSearch.setAdapter(adapter);
						
					}else{
						
						Toast.makeText(ShowSearchResultFunctionality.this, "Member Not exist", Toast.LENGTH_LONG).show();
					}
*/					
					try {
						JSONObject jsonobj=new JSONObject(response);
						
						int i=Integer.parseInt(jsonobj.getJSONObject("message").getString("total_count"));
						
						TOTAL_LIST_ITEMS=Integer.parseInt(jsonobj.getJSONObject("message").getString("total_count"));
						Log.e("totalcount=", ""+TOTAL_LIST_ITEMS);
						
						
						
						jarray=jsonobj.getJSONObject("message").getJSONArray("records");
						
						if(jarray.length()>0){
							
							textView1.setText("Search Result("+i+")");
							Btnfooter();
							
							SearchListAdapter adapter=new SearchListAdapter(ShowSearchResultFunctionality.this,jarray);
							lvShowSearch.setAdapter(adapter);
								
						}else{
							
							Toast.makeText(ShowSearchResultFunctionality.this, "No Record Found", Toast.LENGTH_LONG).show();
						}
					
					
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
					
				}

			}
		},new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Methods.closeProgressDialog();
				Log.d("droid","get all pcf error---------------"+ error.getCause());
			}
		})
		{
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();

				SearchModel model=new SearchModel();
				model.setUsername(mPreferenceHelper.getString(Commons.USER_EMAILID));
				model.setUserpass(mPreferenceHelper.getString(Commons.USER_PASSWORD));
				
				if(!zone.equals(""))
				  model.setZone(zone);
				
				if(!region.equals(""))
				  model.setRegion(region);
				
				if(!church.equals(""))
				  model.setChurch(church);
				
				if(!group_church.equals(""))
					  model.setGroup_church(group_church);
					
				model.setSearch(search);
				
				model.setPage_no(pageno);
				
				if(null !=member && member.trim().length() > 0)
					model.setMember(member);

				String dataString=gson.toJson(model, SearchModel.class);

				Log.e("Request","data passed is ::::::::"+dataString);
				params.put(ShowAllMembersService.DATA, dataString);
				return params; 
			}
		};
		App.getInstance().addToRequestQueue(reqPostSearch, "reqPostSearch");
		reqPostSearch.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
	}


	class SearchListAdapter extends BaseAdapter{

		private Context mContext;
	//	private ArrayList<SearchResultSubModel> mList;
		JSONArray jarray;
		private Holder holder;

		public SearchListAdapter(Context context,JSONArray jarray) {
			mContext=context;
			this.jarray=jarray;
		}

		@Override
		public int getCount() {
			return jarray.length();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {

			LayoutInflater layout = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			holder = new Holder();
			if (convertView == null) {
				convertView = layout.inflate(R.layout.row_dashboardmeetingdetails, null);

				holder.lblMemberName=(TextView) convertView.findViewById(R.id.lblCellName);
				holder.lblEmailId=(TextView) convertView.findViewById(R.id.lblCellDetail);
				holder.lblPhoneNo=(TextView) convertView.findViewById(R.id.lblPresent);

				holder.lblChurchGroup=(TextView) convertView.findViewById(R.id.lblPresentCount);
				holder.lblChurch=(TextView) convertView.findViewById(R.id.lblAbsent);

				holder.lblZone=(TextView) convertView.findViewById(R.id.lblAbsentCount);
				holder.lblRegion=(TextView) convertView.findViewById(R.id.lblExtra);

				convertView.setTag(holder);
			}else{
				holder = (Holder) convertView.getTag();
			}
			
			try {
				holder.lblMemberName.setText(jarray.getJSONObject(position).getString("name"));
				holder.lblEmailId.setText(jarray.getJSONObject(position).getString("id"));
				holder.lblPhoneNo.setText(jarray.getJSONObject(position).getString("type"));
				holder.lblChurchGroup.setText((jarray.getJSONObject(position).getString("contact_email_id")).equals("null")?"":jarray.getJSONObject(position).getString("contact_email_id"));
				holder.lblChurch.setText(jarray.getJSONObject(position).getString("contact_phone_no").equals("null")?"":jarray.getJSONObject(position).getString("contact_phone_no"));
				
				
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
/*
 * 
 * 
 * 
			if(search.equalsIgnoreCase("Member")){
				holder.lblRegion.setVisibility(View.VISIBLE);
				holder.lblMemberName.setText(mList.get(position).getMember_name());
				holder.lblEmailId.setText(mList.get(position).getEmail_id());
				holder.lblPhoneNo.setText(mList.get(position).getPhone_1());
				holder.lblChurchGroup.setText(mList.get(position).getChurch_group());
				holder.lblChurch.setText(mList.get(position).getChurch());
				holder.lblRegion.setText(mList.get(position).getRegion());
				holder.lblZone.setText(mList.get(position).getZone());
			}
			else if(search.equalsIgnoreCase("Leader")){
				holder.lblRegion.setVisibility(View.GONE);
				holder.lblEmailId.setVisibility(View.GONE);
				holder.lblZone.setVisibility(View.GONE);

				holder.lblMemberName.setText(mList.get(position).getMember_name());
				holder.lblPhoneNo.setText(mList.get(position).getPhone_no());
				holder.lblChurchGroup.setText(mList.get(position).getGroup_type());
				holder.lblChurch.setText(mList.get(position).getChurch());

			}else if(search.equalsIgnoreCase("Group")){
				holder.lblChurch.setVisibility(View.GONE);
				holder.lblRegion.setVisibility(View.GONE);
				holder.lblZone.setVisibility(View.GONE);

				holder.lblMemberName.setText(mList.get(position).getSenior_cell());
				holder.lblEmailId.setText(mList.get(position).getCell());
				holder.lblPhoneNo.setText(mList.get(position).getPcf());
				holder.lblChurchGroup.setText(mList.get(position).getChurch());

			}*/
			return convertView;
		}

	}
	public static class Holder{
		private TextView lblMemberName,lblEmailId,lblPhoneNo,lblChurchGroup,lblChurch,lblZone,lblRegion;
	}
	
private void Btnfooter()
{
	    int val = TOTAL_LIST_ITEMS%NUM_ITEMS_PAGE;
	    val = val==0?0:1;
	    noOfBtns=TOTAL_LIST_ITEMS/NUM_ITEMS_PAGE+val;
	     
	    LinearLayout ll = (LinearLayout)findViewById(R.id.btnLay);
	    ll.removeAllViews();
	    
	    
	    btns    =new Button[noOfBtns];
	     
	    for(int i=0;i<noOfBtns;i++)
	    {
	        btns[i] =   new Button(this);
	        btns[i].setBackgroundColor(getResources().getColor(android.R.color.transparent));
	        btns[i].setText(""+(i+1));
	         
	        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	        ll.addView(btns[i], lp);
	         
	        final int j = i;
	        btns[j].setOnClickListener(new OnClickListener() {
	             
	            public void onClick(View v) 
	            {
	               	if(NetworkHelper.isOnline(ShowSearchResultFunctionality.this)){
	        			Methods.showProgressDialog(ShowSearchResultFunctionality.this);
	        			callsearchUsers( btns[j].getText().toString());
	        			
	        		}else{
	        			
	        			Methods.longToast("Please connect to Internet", ShowSearchResultFunctionality.this);
	        		
	        		}
	            	
	                CheckBtnBackGroud(j);
	            }
	        });
	        
	       
	    }
	     
	}

private void CheckBtnBackGroud(int index)
{
	  
	    for(int i=0;i<noOfBtns;i++)
	    {
	        if(i==index)
	        {
	            btns[index].setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
	            btns[i].setTextColor(getResources().getColor(android.R.color.white));
	        }
	        else
	        {
	            btns[i].setBackgroundColor(getResources().getColor(android.R.color.transparent));
	            btns[i].setTextColor(getResources().getColor(android.R.color.black));
	        }
	    }
	     
	}

public void showDialog(){
	
	LayoutInflater layoutInflater = LayoutInflater.from(ShowSearchResultFunctionality.this);
	View promptView = layoutInflater.inflate(R.layout.filter_dialog, null);
	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ShowSearchResultFunctionality.this);
	
	LinearLayout pcflayout=(LinearLayout) promptView.findViewById(R.id.pcflayout);
	LinearLayout srlayout=(LinearLayout) promptView.findViewById(R.id.srlayout);
	LinearLayout cellLayout=(LinearLayout) promptView.findViewById(R.id.cellLayout);
	LinearLayout layoutRegion=(LinearLayout) promptView.findViewById(R.id.layoutRegion);
	LinearLayout layoutzone=(LinearLayout) promptView.findViewById(R.id.layoutzone);
	LinearLayout layoutchurchgroup=(LinearLayout) promptView.findViewById(R.id.layoutchurchgroup);
	LinearLayout layoutchurch=(LinearLayout) promptView.findViewById(R.id.layoutchurch);
	
	
	
	final TextView spzoneTextView=(TextView) promptView.findViewById(R.id.spzoneTextView);
	final TextView spresionTextView=(TextView) promptView.findViewById(R.id.spresionTextView);
	final TextView spgroupchurchTextView=(TextView) promptView.findViewById(R.id.spgroupchurchTextView);
	final TextView spchurchTextView=(TextView) promptView.findViewById(R.id.spchurchTextView);
	final TextView sppcfTextView=(TextView) promptView.findViewById(R.id.sppcfTextView);
	final TextView spSeniorCellTextView=(TextView) promptView.findViewById(R.id.spSeniorCellTextView);
	final TextView spCellTextView=(TextView) promptView.findViewById(R.id.spCellTextView);
	
	txtFromDate=(TextView) promptView.findViewById(R.id.txtFromDate);
	txtToDate=(TextView) promptView.findViewById(R.id.txtToDate);
	spresion=(Spinner) promptView.findViewById(R.id.spresion);
	spzone=(Spinner) promptView.findViewById(R.id.spzone);
	sppcf=(Spinner) promptView.findViewById(R.id.sppcf);
	spgroupchurch=(Spinner) promptView.findViewById(R.id.spgroupchurch);
	spchurch =(Spinner) promptView.findViewById(R.id.spchurch);
	spSeniorCell=(Spinner) promptView.findViewById(R.id.spSeniorCell);
	spCell=(Spinner) promptView.findViewById(R.id.spCell);
	etName =(EditText) promptView.findViewById(R.id.etName);

	String str=mPreferenceHelper.getString(Commons.USER_DEFVALUE);
	Log.e("default user", str);
	
	
	
	spzone.setOnItemSelectedListener(new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			
			String str=spzone.getSelectedItem().toString();
			Methods.showProgressDialog(ShowSearchResultFunctionality.this);
			getUpdatedSpinnerData("Zones",str);
			
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {}
	});
	
	spgroupchurch.setOnItemSelectedListener(new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			
			String str=spgroupchurch.getSelectedItem().toString();
			Methods.showProgressDialog(ShowSearchResultFunctionality.this);
			getUpdatedSpinnerData("Group Churches",str);
			
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {}
	});		

	spchurch.setOnItemSelectedListener(new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			
			String str=spchurch.getSelectedItem().toString();
			Methods.showProgressDialog(ShowSearchResultFunctionality.this);
			getUpdatedSpinnerData("Churches",str);
			
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {}
	});	
	
	sppcf.setOnItemSelectedListener(new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			
			String str=sppcf.getSelectedItem().toString();
			Methods.showProgressDialog(ShowSearchResultFunctionality.this);
			getUpdatedSpinnerData("PCFs",str);
			
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {}
	});	
	
	spSeniorCell.setOnItemSelectedListener(new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			
			String str=spSeniorCell.getSelectedItem().toString();
			Methods.showProgressDialog(ShowSearchResultFunctionality.this);
			getUpdatedSpinnerData("Senior Cells",str);
			
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {}
	});	
	
	spresionTextView.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(SpinnerDataFlag.Regions_flag){
				spresionTextView.setVisibility(View.GONE);
				mRegionList.add(mPreferenceHelper.getString(Commons.USER_DEFVALUE));
				spresion.setVisibility(View.VISIBLE);
				setAdapters();
				SpinnerDataFlag.Regions_flag=false;
			}else{
				spresionTextView.setVisibility(View.GONE);
				spresion.setVisibility(View.VISIBLE);
				setAdapters();
				
			}
		}
	});
	
	spzoneTextView.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
				
			
			if(SpinnerDataFlag.Zones_flag){
				
				if(mPreferenceHelper.getString(Commons.USER_ROLE).equals("Zonal Pastor")){
				
					mZoneList.add(mPreferenceHelper.getString(Commons.USER_DEFVALUE));
					spzone.setVisibility(View.VISIBLE);
					spzoneTextView.setVisibility(View.GONE);
					setAdapters();
					SpinnerDataFlag.Zones_flag=false;
				}else{
					
					try{
					
						String str=spresion.getSelectedItem().toString();
						Log.e("selected value", str);
						
						Methods.showProgressDialog(ShowSearchResultFunctionality.this);
						getSpinnerData("Regions",str);
						
						spzone.setVisibility(View.VISIBLE);
						spzoneTextView.setVisibility(View.GONE);
						SpinnerDataFlag.Zones_flag=false;
					
					}catch(NullPointerException ex){
						
						Toast.makeText(ShowSearchResultFunctionality.this, "Please Select Resion", Toast.LENGTH_LONG).show();
					
					}
					
				}
				
			}
			else{
				
				if(spresionTextView.getVisibility()!=View.VISIBLE){
					spzone.setVisibility(View.VISIBLE);
					spzoneTextView.setVisibility(View.GONE);
					setAdapters();
				}
				else{
					
					Toast.makeText(ShowSearchResultFunctionality.this, "Please Select Resion", Toast.LENGTH_LONG).show();
					
				}
			}
		}
	});
	
	spgroupchurchTextView.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			if(SpinnerDataFlag.GroupChurches_flag){
				
				if(mPreferenceHelper.getString(Commons.USER_ROLE).equals("Group Church Pastor")){
				
					mGrpChurchList.add(mPreferenceHelper.getString(Commons.USER_DEFVALUE));
					spgroupchurch.setVisibility(View.VISIBLE);
					spgroupchurchTextView.setVisibility(View.GONE);
					setAdapters();
					SpinnerDataFlag.GroupChurches_flag=false;
					
				}else{
					
					try{
					
						String str=spzone.getSelectedItem().toString();
						Log.e("selected value", str);
						
						Methods.showProgressDialog(ShowSearchResultFunctionality.this);
						getSpinnerData("Zones",str);
						
						spgroupchurch.setVisibility(View.VISIBLE);
						spgroupchurchTextView.setVisibility(View.GONE);
						SpinnerDataFlag.GroupChurches_flag=false;
						
					}catch(NullPointerException ex){
						
						Toast.makeText(ShowSearchResultFunctionality.this, "Please Select Zone", Toast.LENGTH_LONG).show();
					
					}
					
				}
				
			
			}else{
				if(spzoneTextView.getVisibility()!=View.VISIBLE){
				
					spgroupchurch.setVisibility(View.VISIBLE);
					spgroupchurchTextView.setVisibility(View.GONE);
					setAdapters();
				
				}else{
					
					Toast.makeText(ShowSearchResultFunctionality.this, "Please Select Zone", Toast.LENGTH_LONG).show();
				}
			}
		}
	});

	spchurchTextView.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			if(SpinnerDataFlag.Churches_flag){
				
				if(mPreferenceHelper.getString(Commons.USER_ROLE).equals("Church Pastor")){
				
					mChurchList.add(mPreferenceHelper.getString(Commons.USER_DEFVALUE));
					spchurch.setVisibility(View.VISIBLE);
					spchurchTextView.setVisibility(View.GONE);
					setAdapters();
					SpinnerDataFlag.Churches_flag=false;
					
				}else{
					
					try{
						//TvFolk1.getVisibility() == View.VISIBLE
						if(spgroupchurchTextView.getVisibility()!=View.VISIBLE){
							String str=spgroupchurch.getSelectedItem().toString();
							Log.e("selected value", str);
						
							Methods.showProgressDialog(ShowSearchResultFunctionality.this);
							getSpinnerData("Group Churches",str);
						
							spchurch.setVisibility(View.VISIBLE);
							spchurchTextView.setVisibility(View.GONE);
							SpinnerDataFlag.Churches_flag=false;
						
						}else{
							
							Toast.makeText(ShowSearchResultFunctionality.this, "Please Select Group Church", Toast.LENGTH_LONG).show();
						}
					}catch(NullPointerException ex){
						
						Toast.makeText(ShowSearchResultFunctionality.this, "Please Select Group Church", Toast.LENGTH_LONG).show();
					
					}
					
				}
				
			}else{
				
				if(spgroupchurchTextView.getVisibility()!=View.VISIBLE){
					spchurch.setVisibility(View.VISIBLE);
					spchurchTextView.setVisibility(View.GONE);
					setAdapters();
				}else{
					
					Toast.makeText(ShowSearchResultFunctionality.this, "Please Select Group Church", Toast.LENGTH_LONG).show();
					
				}
			}
		}
	});

	sppcfTextView.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			if(SpinnerDataFlag.PCFs_flag){
				
				if(mPreferenceHelper.getString(Commons.USER_ROLE).equals("PCF Leader")){
				
					mPCFList.add(mPreferenceHelper.getString(Commons.USER_DEFVALUE));
					sppcf.setVisibility(View.VISIBLE);
					sppcfTextView.setVisibility(View.GONE);
					setAdapters();
					SpinnerDataFlag.PCFs_flag=false;
					
				}else{
					
					try{
					
						String str=spchurch.getSelectedItem().toString();
						Log.e("selected value", str);
						
						Methods.showProgressDialog(ShowSearchResultFunctionality.this);
						getSpinnerData("Churches",str);
						
						sppcf.setVisibility(View.VISIBLE);
						sppcfTextView.setVisibility(View.GONE);
						SpinnerDataFlag.PCFs_flag=false;
				
					}catch(NullPointerException ex){
						
						Toast.makeText(ShowSearchResultFunctionality.this, "Please Select Church", Toast.LENGTH_LONG).show();
					
					}
					
				}
				
			}else{
				
				if(spchurchTextView.getVisibility()!=View.VISIBLE){
				
					sppcf.setVisibility(View.VISIBLE);
					sppcfTextView.setVisibility(View.GONE);
					setAdapters();
				
				}else{
					
					Toast.makeText(ShowSearchResultFunctionality.this, "Please Select Church", Toast.LENGTH_LONG).show();
				}
			}
		}
	});
	
	spSeniorCellTextView.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			if(SpinnerDataFlag.SeniorCells_flag){
				
				if(mPreferenceHelper.getString(Commons.USER_ROLE).equals("Senior Cell Leader")){
				
					mSeniorCellList.add(mPreferenceHelper.getString(Commons.USER_DEFVALUE));
					spSeniorCell.setVisibility(View.VISIBLE);
					spSeniorCellTextView.setVisibility(View.GONE);
					setAdapters();
					SpinnerDataFlag.SeniorCells_flag=false;
					
				}else{
					
					try{
					
						String str=sppcf.getSelectedItem().toString();
						Log.e("selected value", str);
						
						Methods.showProgressDialog(ShowSearchResultFunctionality.this);
						getSpinnerData("PCFs",str);
						
						spSeniorCell.setVisibility(View.VISIBLE);
						spSeniorCellTextView.setVisibility(View.GONE);
						SpinnerDataFlag.SeniorCells_flag=false;
						
					}catch(NullPointerException ex){
						
						Toast.makeText(ShowSearchResultFunctionality.this, "Please Select Pcf", Toast.LENGTH_LONG).show();
					
					}
					
				}
				
			
			}else{
				
				spSeniorCell.setVisibility(View.VISIBLE);
				spSeniorCellTextView.setVisibility(View.GONE);
				setAdapters();
				
				
			}
		}
	});

	spCellTextView.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			if(SpinnerDataFlag.Cells_flag){
				
				if(mPreferenceHelper.getString(Commons.USER_ROLE).equals("Cell Leader")){
				
					mCellList.add(mPreferenceHelper.getString(Commons.USER_DEFVALUE));
					spCell.setVisibility(View.VISIBLE);
					spCellTextView.setVisibility(View.GONE);
					setAdapters();
					SpinnerDataFlag.Cells_flag=false;
					
				}else{
					
					try{
					
						String str=spSeniorCell.getSelectedItem().toString();
						Log.e("selected value", str);
						
						Methods.showProgressDialog(ShowSearchResultFunctionality.this);
						getSpinnerData("Senior Cells",str);
						
						spCell.setVisibility(View.VISIBLE);
						spCellTextView.setVisibility(View.GONE);
						SpinnerDataFlag.Cells_flag=false;	
						
					}catch(NullPointerException ex){
						
						Toast.makeText(ShowSearchResultFunctionality.this, "Please Select Senior cell", Toast.LENGTH_LONG).show();
					
					}
					
				}
				
			}else{
				
				spCell.setVisibility(View.VISIBLE);
				spCellTextView.setVisibility(View.GONE);
				setAdapters();
			}
		}
	});
	
	
	if(UserRoll.equals("PCF Leader")){
		layoutRegion.setVisibility(View.GONE);
		layoutzone.setVisibility(View.GONE);
		layoutchurchgroup.setVisibility(View.GONE);
		layoutchurch.setVisibility(View.GONE);
		
		
	}
	
	if(UserRoll.equals("Zonal Pastor")){
		
		layoutRegion.setVisibility(View.GONE);
		/*srlayout.setVisibility(View.GONE);
		cellLayout.setVisibility(View.GONE);*/
	}
	
	if(UserRoll.equals("Group Church Pastor")){
		/*layoutRegion.setVisibility(View.GONE);
		layoutzone.setVisibility(View.GONE);
		*/
		layoutRegion.setVisibility(View.GONE);
		layoutzone.setVisibility(View.GONE);
	/*
		pcflayout.setVisibility(View.GONE);
		srlayout.setVisibility(View.GONE);
		cellLayout.setVisibility(View.GONE);*/
	}

			
	if(UserRoll.equals("Church Pastor")){
		layoutRegion.setVisibility(View.GONE);
		layoutzone.setVisibility(View.GONE);
		layoutchurchgroup.setVisibility(View.GONE);
	}
	
	
	if(UserRoll.equals("Senior Cell Leader")){
	
		layoutRegion.setVisibility(View.GONE);
		layoutzone.setVisibility(View.GONE);
		layoutchurchgroup.setVisibility(View.GONE);
		layoutchurch.setVisibility(View.GONE);
		pcflayout.setVisibility(View.GONE);
		
	}
	
	if(UserRoll.equals("Member")){
		layoutRegion.setVisibility(View.GONE);
		layoutzone.setVisibility(View.GONE);
		layoutchurchgroup.setVisibility(View.GONE);
		layoutchurch.setVisibility(View.GONE);
		pcflayout.setVisibility(View.GONE);
		srlayout.setVisibility(View.GONE);
		cellLayout.setVisibility(View.GONE);
	
	}
	
	if(UserRoll.equals("Cell Leader")){
		layoutRegion.setVisibility(View.GONE);
		layoutzone.setVisibility(View.GONE);
		layoutchurchgroup.setVisibility(View.GONE);
		layoutchurch.setVisibility(View.GONE);
		pcflayout.setVisibility(View.GONE);
		srlayout.setVisibility(View.GONE);
	
	}
	
	
	
	txtFromDate.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
		//	showfromdate();
			fromDatePickerDialog.show();
			
		}
	});
	
	txtToDate.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			toDatePickerDialog.show();
			
		}
	});
	
			
	alertDialogBuilder.setView(promptView);
	alertDialogBuilder.setCancelable(false);
	alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
	
	public void onClick(DialogInterface dialog, int id) {
		
		String resion="",zone="",groupchurch="",church="",pcf="",srcell="",cell="",fdate="",tdate="",etname="";
		
		try{
			resion=spresion.getSelectedItem().toString();
		}catch(Exception ex){
			resion="";
		}
		
		try{
		 zone=spzone.getSelectedItem().toString();
		}catch(Exception ex){
			zone="";
		}
		try{
		 groupchurch=spgroupchurch.getSelectedItem().toString();
		}catch(Exception ex){
			groupchurch="";
		}
		
		try{
		church=spchurch.getSelectedItem().toString();
		}catch(Exception ex){
			church="";
		}
		try{
		 pcf=sppcf.getSelectedItem().toString();
		}catch(Exception ex){
			pcf="";
		}
		
		try{
		 srcell=spSeniorCell.getSelectedItem().toString();
		}catch(Exception ex){
			srcell="";
		}
		try{
		 cell=spCell.getSelectedItem().toString();
		}catch(Exception ex){
			cell="";
		}
		
		 fdate=txtFromDate.getText().toString();
		 tdate=txtToDate.getText().toString();
		 etname=etName.getText().toString();
		
		 if(!checkValidation(resion,zone,groupchurch,church,pcf,srcell,cell,fdate,tdate,etname)){
				
			 Methods.longToast("Please select any Filter",ShowSearchResultFunctionality.this);
			 
		 }
		 else
		 {
			 if(NetworkHelper.isOnline(ShowSearchResultFunctionality.this)){
		 
				 Methods.showProgressDialog(ShowSearchResultFunctionality.this);
				 
				 getUpdatedListMethod("First Timer",resion,zone,groupchurch,church,pcf,srcell,cell,fdate,tdate,etname);
		
				 dialog.cancel();
			
		}else{
		
			Methods.longToast("Please connect to Internet",ShowSearchResultFunctionality.this);
		
		}
	 }
	}
 })
   .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

	   public void onClick(DialogInterface dialog, int id) {

		   dialog.cancel();

	   }

   });
	AlertDialog alertD = alertDialogBuilder.create();
	alertD.show();
//	TextView textView = (TextView) alertD.findViewById(android.R.id.message);
//	textView.setTextSize(18);

}
private void getUpdatedSpinnerData(final String tblname,final String name) {
Methods.closeProgressDialog();
StringRequest reqgetTopHierarchy=new StringRequest(Method.POST,LowerHierarchyService.SERVICE_URL,new Listener<String>() {

	@Override
	public void onResponse(String response) {
		Methods.closeProgressDialog();
		Log.e("droid","get getSpinnerData ---------------"+ response);

		try {
			JSONObject obj=new JSONObject(response);
			JSONArray jarray=obj.getJSONArray("message");
			
			if(tblname.equals("Regions")){
				mZoneList.clear();
			for(int i=0;i<jarray.length();i++){
				mZoneList.add(jarray.getJSONObject(i).getString("name"));
				}
			}
			
			if(tblname.equals("Zones")){
				mGrpChurchList.clear();
				for(int i=0;i<jarray.length();i++){
					mGrpChurchList.add(jarray.getJSONObject(i).getString("name"));
				}
				
				ArrayAdapter<String> adapterchurchgropu = new ArrayAdapter<String>(ShowSearchResultFunctionality.this, android.R.layout.simple_spinner_item, mGrpChurchList);
				adapterchurchgropu.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spgroupchurch.setAdapter(adapterchurchgropu);
			
			}
			
			if(tblname.equals("Group Churches")){
				mChurchList.clear();
				
				for(int i=0;i<jarray.length();i++){
					mChurchList.add(jarray.getJSONObject(i).getString("name"));
				}
				
				ArrayAdapter<String> adapterChurch = new ArrayAdapter<String>(ShowSearchResultFunctionality.this, android.R.layout.simple_spinner_item, mChurchList);
				adapterChurch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spchurch.setAdapter(adapterChurch);
				
				
			}
			
			if(tblname.equals("Churches")){
				mPCFList.clear();
				for(int i=0;i<jarray.length();i++){
					mPCFList.add(jarray.getJSONObject(i).getString("name"));
				}
				
				ArrayAdapter<String> adaptercell = new ArrayAdapter<String>(ShowSearchResultFunctionality.this, android.R.layout.simple_spinner_item, mCellList);
				adaptercell.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spCell.setAdapter(adaptercell);
			}
			
			if(tblname.equals("PCFs")){
				
				mSeniorCellList.clear();
				for(int i=0;i<jarray.length();i++){
					mSeniorCellList.add(jarray.getJSONObject(i).getString("name"));
				}
				
				ArrayAdapter<String> adapterSrCell = new ArrayAdapter<String>(ShowSearchResultFunctionality.this, android.R.layout.simple_spinner_item, mSeniorCellList);
				adapterSrCell.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spSeniorCell.setAdapter(adapterSrCell);
			}
			
			if(tblname.equals("Senior Cells")){
				
				mCellList.clear();
				
				for(int i=0;i<jarray.length();i++){
					mCellList.add(jarray.getJSONObject(i).getString("name"));
				}
				
				ArrayAdapter<String> adaptercell = new ArrayAdapter<String>(ShowSearchResultFunctionality.this, android.R.layout.simple_spinner_item, mCellList);
				adaptercell.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spCell.setAdapter(adaptercell);
			}
			
			//setAdapters();
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
},new ErrorListener() {

	@Override
	public void onErrorResponse(VolleyError error) {
		Methods.closeProgressDialog();
		Log.d("droid","get reqgetTopHierarchy error---------------"+ error.getCause());

		if(error.networkResponse.statusCode==403){
			Methods.longToast("Access Denied", ShowSearchResultFunctionality.this);
		}
		else
	
			Methods.closeProgressDialog();				
	}

}){
	@Override
	protected Map<String, String> getParams() throws AuthFailureError{
		Map<String, String> params = new HashMap<String, String>();
		MeetingListRequestModel model=new MeetingListRequestModel();
		model.setUsername(mPreferenceHelper.getString(Commons.USER_EMAILID));
		model.setUserpass(mPreferenceHelper.getString(Commons.USER_PASSWORD));
		model.setTbl(tblname);
		model.setName(name);

		String dataString=gson.toJson(model, MeetingListRequestModel.class);

		Log.d("droid", dataString);
		params.put(LowerHierarchyService.DATA, dataString);
		return params;
	}
};

App.getInstance().addToRequestQueue(reqgetTopHierarchy, "reqgetTopHierarchy");
reqgetTopHierarchy.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));

}

private void setAdapters() {

ArrayAdapter<String> adapterZone = new ArrayAdapter<String>(ShowSearchResultFunctionality.this, android.R.layout.simple_spinner_item, mZoneList);
adapterZone.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
spzone.setAdapter(adapterZone);

ArrayAdapter<String> adapterRegion = new ArrayAdapter<String>(ShowSearchResultFunctionality.this, android.R.layout.simple_spinner_item, mRegionList);
adapterRegion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
spresion.setAdapter(adapterRegion);

ArrayAdapter<String> adapterChurch = new ArrayAdapter<String>(ShowSearchResultFunctionality.this, android.R.layout.simple_spinner_item, mChurchList);
adapterChurch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
spchurch.setAdapter(adapterChurch);

ArrayAdapter<String> adapterSrCell = new ArrayAdapter<String>(ShowSearchResultFunctionality.this, android.R.layout.simple_spinner_item, mSeniorCellList);
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
spCell.setAdapter(adaptercell);

}

private void getSpinnerData(final String tblname,final String name) {
Methods.closeProgressDialog();
StringRequest reqgetTopHierarchy=new StringRequest(Method.POST,LowerHierarchyService.SERVICE_URL,new Listener<String>() {

	@Override
	public void onResponse(String response) {
		Methods.closeProgressDialog();
		Log.e("droid","get getSpinnerData ---------------"+ response);

		try {
			JSONObject obj=new JSONObject(response);
			JSONArray jarray=obj.getJSONArray("message");
			
			if(tblname.equals("Regions")){
			for(int i=0;i<jarray.length();i++){
				mZoneList.add(jarray.getJSONObject(i).getString("name"));
				}
			}
			
			if(tblname.equals("Zones")){
				for(int i=0;i<jarray.length();i++){
					mGrpChurchList.add(jarray.getJSONObject(i).getString("name"));
				}
			
			}
			
			if(tblname.equals("Group Churches")){
				for(int i=0;i<jarray.length();i++){
					mChurchList.add(jarray.getJSONObject(i).getString("name"));
				}
			}
			
			if(tblname.equals("Churches")){
				for(int i=0;i<jarray.length();i++){
					mPCFList.add(jarray.getJSONObject(i).getString("name"));
				}
			}
			
			if(tblname.equals("PCFs")){
				for(int i=0;i<jarray.length();i++){
					mSeniorCellList.add(jarray.getJSONObject(i).getString("name"));
				}
			}
			
			if(tblname.equals("Senior Cells")){
				for(int i=0;i<jarray.length();i++){
					mCellList.add(jarray.getJSONObject(i).getString("name"));
				}
			}
			
			setAdapters();
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
},new ErrorListener() {

	@Override
	public void onErrorResponse(VolleyError error) {
		Methods.closeProgressDialog();
		Log.d("droid","get reqgetTopHierarchy error---------------"+ error.getCause());

		if(error.networkResponse.statusCode==403){
			Methods.longToast("Access Denied", ShowSearchResultFunctionality.this);
		}
		else
	
			Methods.closeProgressDialog();				
	}

}){
	@Override
	protected Map<String, String> getParams() throws AuthFailureError{
		Map<String, String> params = new HashMap<String, String>();
		MeetingListRequestModel model=new MeetingListRequestModel();
		model.setUsername(mPreferenceHelper.getString(Commons.USER_EMAILID));
		model.setUserpass(mPreferenceHelper.getString(Commons.USER_PASSWORD));
		model.setTbl(tblname);
		model.setName(name);

		String dataString=gson.toJson(model, MeetingListRequestModel.class);

		Log.d("droid", dataString);
		params.put(LowerHierarchyService.DATA, dataString);
		return params;
	}
};

App.getInstance().addToRequestQueue(reqgetTopHierarchy, "reqgetTopHierarchy");
reqgetTopHierarchy.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));

}

private void getUpdatedListMethod(final String tbl,final String resion,final String zone,final String gchurch,final String church,final String pcf,final String srcell,final String cell,final String fdate,final String todate,final String etname){

StringRequest reqgetLowerHierarchy=new StringRequest(Method.POST,SearchService.SERVICE_URL,new Listener<String>() {

@Override
public void onResponse(String response) {
	Methods.closeProgressDialog();
	Log.e("droid get reqgetList Redord--------", response);

	

	if(response.contains("status"))
	{
		ResponseMessageModel2 respModel=gson.fromJson(response, ResponseMessageModel2.class);
	
		if(respModel.getMessage().getStatus()=="401"){
			Methods.longToast("User name or Password is incorrect", ShowSearchResultFunctionality.this);
		}else{
			Methods.longToast(respModel.getMessage().getMessage(), ShowSearchResultFunctionality.this);
		}
	}else{
		
			
			try {
				
				 jsonobj=new JSONObject(response);
				 
				 jsonobj.getJSONObject("message");
					
				TOTAL_LIST_ITEMS=Integer.parseInt(jsonobj.getJSONObject("message").getString("total_count"));
				
					Log.e("totalcount=", ""+TOTAL_LIST_ITEMS);
				
				//if(pageflag)
				 Btnfooter();
				 
				 jsonarray=jsonobj.getJSONObject("message").getJSONArray("records");
				 textView1.setText("Search Result("+TOTAL_LIST_ITEMS+")");
				 
				if(jsonarray.length()>0){
					
					SearchListAdapter adapter=new SearchListAdapter(ShowSearchResultFunctionality.this,jsonarray);
					lvShowSearch.setAdapter(adapter);
					adapter.notifyDataSetChanged();
				
				}else{
					
						Methods.longToast("No results found", ShowSearchResultFunctionality.this);
						SearchListAdapter adapter=new SearchListAdapter(ShowSearchResultFunctionality.this,jsonarray);
						lvShowSearch.setAdapter(adapter);
						adapter.notifyDataSetChanged();
				}
			
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}


}
},new ErrorListener() {

@Override
public void onErrorResponse(VolleyError error) {
	Methods.closeProgressDialog();
	Log.d("droid","get reqgetLowerHierarchy error---------------"+ error.getCause());

	if(error.networkResponse.statusCode==403){
		//	Methods.longToast("Access Denied", CreateSeniorCellMasterActivity.this);
	}
	//else
	//	Methods.longToast("Some Error Occured,please try again later", CreateSeniorCellMasterActivity.this);

}

}){
@Override
protected Map<String, String> getParams() throws AuthFailureError{
	Map<String, String> params = new HashMap<String, String>();

	
	JSONObject jsonobj=new JSONObject();
	try {
	
		jsonobj.put("username", mPreferenceHelper.getString(Commons.USER_EMAILID));
		jsonobj.put("userpass", mPreferenceHelper.getString(Commons.USER_PASSWORD));
		jsonobj.put("search", search);
		
		
		//resion, zone,gchurch, church, pcf,srcell,cell,fdate, todate)
		
		JSONObject jsonfilter=new JSONObject();
		
		
		if(!resion.equals(""))
			jsonfilter.put("region", resion);
		
		if(!zone.equals(""))
			jsonfilter.put("zone", zone);
		
		if(!gchurch.equals(""))
			jsonfilter.put("group_church", gchurch);
		
		if(!church.equals(""))
			jsonfilter.put("church", church);
		
		if(!pcf.equals(""))
			jsonfilter.put("pcf", pcf);
		
		if(!srcell.equals(""))
			jsonfilter.put("senior_cell", srcell);
		
		if(!cell.equals(""))
			jsonfilter.put("cell", cell);

		if(!fdate.equals(""))
			jsonfilter.put("from_date", fdate);
		
		if(!todate.equals(""))
			jsonfilter.put("to_date", todate);

		if(!etname.equals(""))
			jsonfilter.put("by_name", etname);

		jsonobj.put("filters", jsonfilter);
		
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	String dataString=jsonobj.toString();

	Log.e("droid", dataString);
	params.put(GetHigherHierarchyService.DATA, dataString);
	return params;
}
};

App.getInstance().addToRequestQueue(reqgetLowerHierarchy, "reqgetLowerHierarchy");
reqgetLowerHierarchy.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));

}

public static class SpinnerDataFlag{
public static boolean Regions_flag = true;
public static boolean Zones_flag = true;
public static boolean GroupChurches_flag = true;
public static boolean Churches_flag = true;
public static boolean PCFs_flag = true;
public static boolean SeniorCells_flag = true;
public static boolean Cells_flag = true;
}

@Override
protected void onDestroy() {
// TODO Auto-generated method stub
super.onDestroy();

SpinnerDataFlag.Cells_flag=true;
SpinnerDataFlag.Churches_flag=true;
SpinnerDataFlag.GroupChurches_flag=true;
SpinnerDataFlag.PCFs_flag=true;
SpinnerDataFlag.Regions_flag=true;
SpinnerDataFlag.SeniorCells_flag=true;
SpinnerDataFlag.Zones_flag=true;
//pageflag=true;

}

	boolean checkValidation(String resion,String zone,String groupchurch,String church,String pcf,String srcell,String cell,String fdate,String tdate,String etname){
		if(resion.equals("")){
			if(zone.equals("")){
				if(groupchurch.equals("")){
					if(church.equals("")){
						if(pcf.equals("")){
							if(srcell.equals("")){
								if(cell.equals("")){
									if(fdate.equals("") && tdate.equals("")){
										if(etname.equals("") ){
											return false;
										}else{
											return true;
										}
									}else{
										return true;
									}
								}else{
									return true;
								}
							}else{
								return true;
							}
						}else{
							return true;
						}
					}else{
						return true;
					}
				}else{
					return true;
				}
			}else{
				return true;
			}
		}else{
			return true;
		}
	}
	
}
