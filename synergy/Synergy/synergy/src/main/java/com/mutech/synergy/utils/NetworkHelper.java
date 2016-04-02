package com.mutech.synergy.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.apache.http.NameValuePair;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class NetworkHelper {

	// public static boolean isNetworkAvailable(Context ctx) {
	//
	// ConnectivityManager connMgr = (ConnectivityManager)
	// ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
	// NetworkInfo networkInfo =
	// connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	// boolean isWifiConn = networkInfo.isConnected();
	// networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
	//
	// if (null != networkInfo) {
	// boolean isMobileConn = networkInfo.isConnected();
	// networkInfo = connMgr.getActiveNetworkInfo();
	// boolean isActiveNetwork = (networkInfo != null &&
	// networkInfo.isConnected());
	// return ((isWifiConn || isMobileConn) && isActiveNetwork);
	// } else {
	// networkInfo = connMgr.getActiveNetworkInfo();
	// boolean isActiveNetwork = (networkInfo != null &&
	// networkInfo.isConnected());
	// return ((isWifiConn) && isActiveNetwork);
	// }
	// }
	//execute


	public static boolean isOnline(Context cxt) {
		ConnectivityManager cm = (ConnectivityManager) cxt
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

	public static boolean checkWIFI(Context cxt) {
		WifiManager wifi = (WifiManager) cxt
				.getSystemService(Context.WIFI_SERVICE);
		if ((wifi.isWifiEnabled() == true)) {

			WifiInfo wifiInf = wifi.getConnectionInfo();
			/* Get the MAC ADD of WIFI */
			// Commons.MAC_ID = wifiInf.getMacAddress();
			return true;
		} else {
			return false;

		}
	}

	public static boolean checkMobileNET(Context cxt) {
		ConnectivityManager cm = (ConnectivityManager) cxt.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
			return true;
		else
			return false;

	}



	public static void startActionCall(String phoneNumber, Context cxt) {
		Uri phoneNum = Uri.parse("tel:" + phoneNumber);
		cxt.startActivity(new Intent(Intent.ACTION_CALL, phoneNum));
	}


	public static String prepareParameterizedGetUrl(String url,
			List<NameValuePair> params) {

		url+="?";
		if (params != null) {
			for(NameValuePair pair : params){
				try {
					url +=pair.getName()+"="+ URLEncoder.encode(pair.getValue(), "UTF-8")+"&";
				} catch (UnsupportedEncodingException e) {

					e.printStackTrace();
					break;
				}

			}
			url=url.substring(0,url.trim().length()-1);
		}
		return url;
	}

	public static int TYPE_WIFI = 1;
	public static int TYPE_MOBILE = 2;
	public static int TYPE_NOT_CONNECTED = 0;
	public static boolean isConnected;

	public static int getConnectivityStatus(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);


		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if (null != activeNetwork) {
			if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
				return TYPE_WIFI;

			if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
				return TYPE_MOBILE;
		}
		return TYPE_NOT_CONNECTED;
	}

	public static boolean getConnectivityStatusString(Context context) {
		int conn = NetworkHelper.getConnectivityStatus(context);
		String status = null;
		if (conn == NetworkHelper.TYPE_WIFI) {
			status = "Wifi enabled";
			isConnected = true;
		} else if (conn == NetworkHelper.TYPE_MOBILE) {
			status = "Mobile data enabled";
			isConnected = true;
		} else if (conn == NetworkHelper.TYPE_NOT_CONNECTED) {
			status = "Not connected to Internet";
			isConnected = false;
		}
		return isConnected;
	}
}
