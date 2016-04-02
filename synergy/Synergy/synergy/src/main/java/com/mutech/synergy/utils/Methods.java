package com.mutech.synergy.utils;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;


public class Methods {

	private static ProgressDialog progressDialog;
	private static PreferenceHelper mPreferenceHelper;

	public final static boolean isValidEmail(CharSequence target) {
		if (target == null) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
		}
	}

	//Toast
	public static void smallToast(String msg, Context ctx) {
		Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
	}

	public static void longToast(String msg, Context ctx) {
		Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
	}

	public static void vLog(String msg) {
		Log.v("Synergy", msg);
	}

	public static void eLog(String msg) {
		Log.e("Synergy", msg);
	}

	//Progress Dialog
	public static void closeProgressDialog() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}

	public static void showProgressDialog(Context ctx) {
		progressDialog = ProgressDialog.show(ctx, "", "Please Wait", true);
		progressDialog.setCancelable(false);
		progressDialog.show();
	}

	//Date
	public static String getCurrentDateInSpecificFormat(Calendar currentCalDate) {
		String dayNumberSuffix = getDayNumberSuffix(currentCalDate.get(Calendar.DAY_OF_MONTH));
		DateFormat dateFormat = new SimpleDateFormat("dd'" + dayNumberSuffix + "' MMMM yyyy");
		return dateFormat.format(currentCalDate.getTime());
	}

	private static String getDayNumberSuffix(int day) {
		if (day >= 11 && day <= 13) {
			return "th";
		}
		switch (day % 10) {
		case 1:
			return "st";
		case 2:
			return "nd";
		case 3:
			return "rd";
		default:
			return "th";
		}
	}


	public static int convertDpToPixel(float dp, Context context) {
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		float px = dp * (metrics.densityDpi / 160f);
		return (int) px;
	}

	//Encode String
	public static String encodeStringToBase64(String text)
	{
		byte[] data = null;
		try {
			data = text.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String base64 = Base64.encodeToString(data, Base64.DEFAULT);
		return base64;
	}

	// Image related utility methods
	public static Bitmap decodeSampledBitmapFromPath(String path, int reqWidth,
			int reqHeight) {

		final BitmapFactory.Options options = new BitmapFactory.Options(); 
		options.inJustDecodeBounds = true;
		// Calculate inSampleSize
		BitmapFactory.decodeFile(path, options);

		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		options.inDither = true;
		options.inPreferredConfig = Config.RGB_565;

		return BitmapFactory.decodeFile(path, options);
	}

	/** bitmap resize methods **/
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}

	public static String getRealPathFromURI(Context context, Uri contentUri) {
		Cursor cursor = null;
		try {
			String[] proj = { MediaStore.Images.Media.DATA };
			cursor = context.getContentResolver().query(contentUri, proj, null,
					null, null);
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}

	public static boolean notNullOrBlank(String string){

		if(null !=string && string.trim().length() > 0)
			return true;
		return false;
	}

}
