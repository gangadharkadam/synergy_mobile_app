package com.mutech.synergy.utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mutech.synergy.SynergyValues;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Filter;
/**
 * @Admin Class to display the auto complete addresses , data fetching from
 *        google place api.
 *
 */
public class GooglePlacesApiAdapter extends ArrayAdapter<String> {
	// For Auto complete ..
	private ArrayList<String> resultList;
	private static final String LOG_TAG = "DemoAddress";
	private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
	private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
	private static final String OUT_JSON = "/json";
	Context context;
	String countryName;

	// Constructor
	public GooglePlacesApiAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
		this.context = context;
	}
	@Override
	public int getCount() {
		return resultList.size();
	}

	@Override
	public String getItem(int position) {
		return resultList.get(position);
	}

	@Override
	public Filter getFilter() {
		Filter filter = new Filter() {
			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				FilterResults filterResults = new FilterResults();
				if (constraint != null) {
					// Retrieve the auto complete results.
					resultList = autocomplete(constraint.toString());
					// Assign the data to filter results
					filterResults.values = resultList;
					filterResults.count = resultList.size();
				}
				return filterResults;
			}

			@Override
			protected void publishResults(CharSequence constraint,FilterResults results) {
				if(results != null && results.count > 0) {
					notifyDataSetChanged();
				}else{
					notifyDataSetInvalidated();
				}
			}
		};
		return filter;
	}

	// get the array list of addresses
	private ArrayList<String> autocomplete(String input) {
		ArrayList<String> resultList = null;
		HttpURLConnection conn = null;
		StringBuilder jsonResults = new StringBuilder();
		try{
			StringBuilder sb = new StringBuilder(PLACES_API_BASE+ TYPE_AUTOCOMPLETE + OUT_JSON);
			sb.append("?key="+SynergyValues.GoogleApiKeys.PLACES_API_KEY+"&sensor=false");
			// for current country.Get the country code by SIM
			//If you run this in emulator then it will get country name is "us".
			String cName=getCountryCode();
			if(cName!=null){
				countryName=cName;
			}else{
				countryName="in";
			}
			//sb.append("&components=country:"+countryName);
			sb.append("&countryName");
			sb.append("&input=" + URLEncoder.encode(input, "utf8"));
			sb.append("&types=(regions)");

			Log.e("droid", " sb.toString() ::::::::"+sb.toString());
			
			URL url =new URL(sb.toString());

			//URL url=new URL("https://maps.googleapis.com/maps/api/place/autocomplete/json?input=Fisher&key=AIzaSyDliKqwecMwpjh3MuB1ot98DrvZvjb_c6w&sensor=false");
			conn = (HttpURLConnection) url.openConnection();

			InputStreamReader in = new InputStreamReader(conn.getInputStream());

			// Load the results into a StringBuilder
			int read;
			char[] buff = new char[1024];
			while ((read = in.read(buff)) != -1) {
				jsonResults.append(buff, 0, read);
			}

		} catch (MalformedURLException e) {
			return resultList;

		} catch (IOException e) {
			return resultList;

		}finally{
			if (conn != null) {
				conn.disconnect();
			}

		}

		try {
			// Create a JSON object hierarchy from the results
			JSONObject jsonObj = new JSONObject(jsonResults.toString());
			JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");
			// Extract the Place descriptions from the results
			resultList = new ArrayList<String>(predsJsonArray.toString().trim().length());
			for (int i = 0; i < predsJsonArray.toString().trim().length(); i++) {
				resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
			}
		} catch (JSONException e) {
		}catch (Exception e) {
		}
		return resultList;
	}

	// function to identify the country code by SIM.

	private String getCountryCode() {
		TelephonyManager tel = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return tel.getNetworkCountryIso();
	}

}

