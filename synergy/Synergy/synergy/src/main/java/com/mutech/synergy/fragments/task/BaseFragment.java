package com.mutech.synergy.fragments.task;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.http.message.BasicNameValuePair;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mutech.synergy.BaseActivity;



/**
 * The Class BaseFragment.
 */
public abstract class BaseFragment extends Fragment {

	/** The m root view. */
	private View mRootView;

	/** The res id. */
	private int resID = 0;

	/**
	 * Instantiates a new base fragment.
	 */
	public BaseFragment() {
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
	 * android.view.ViewGroup, android.os.Bundle) please call setcontentview()
	 * before calling this
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (resID != 0) {
			mRootView = inflater.inflate(resID, container, false);
		} else {
			throw new IllegalStateException(
					"Layout Resource not specified : Call setContentResource(your layout Resource ID) before calling super.onCreateView()");
		}
		initializeComponents(mRootView);
		setListeners();
		return mRootView;
	}

	/**
	 * Gets the inflated view.
	 *
	 * @return the inflated view
	 */
	protected View getInflatedView(){
		return mRootView;
	}

	/**
	 * Gets the text view.
	 *
	 * @param resId the res id
	 * @return the text view
	 */
	protected TextView getTextView(int resId){
		return (TextView) getInflatedView().findViewById(resId);
	}

	/**
	 * Gets the container activity.
	 * 
	 * @return the container activity
	 */
	public BaseActivity getBaseActivity() {		
		return (BaseActivity) getActivity();
	}

	/**
	 * Communicate.
	 *
	 * @param data the data
	 */
	public void communicate(Object data) {
	}

	/**
	 * Sets the content resource. call to this method should happen before
	 * super.onCreateView() call otherwise it will throw Exception.
	 * 
	 * @param layoutResID
	 *            the new content resource
	 */
	protected void setContentResource(int layoutResID) {
		resID = layoutResID;
	}

	/**
	 * Gets the content view of this fragment.
	 * 
	 * @return the content view
	 */
	public View getContentView() {
		return mRootView;
	}

	/**
	 * Gets the basic name value pair.
	 * 
	 * @param key
	 *            the key
	 * @param value
	 *            the value
	 * @return the basic name value pair
	 */
	protected BasicNameValuePair getBasicNameValuePair(String key, String value) {
		return new BasicNameValuePair(key, value);
	}

	/**
	 * Initialize the UI components. This will be called implicitely form within
	 * onCreateView()
	 * 
	 * @param rootView
	 *            the root view
	 */
	protected abstract void initializeComponents(View rootView);

	/**
	 * Set the listeners to all the view within this fragment here.
	 */
	protected abstract void setListeners();

	

	
	/**
	 * Gets the array list from array.
	 *
	 * @param arrId the arr id
	 * @return the array list from array
	 */
	public ArrayList<String> getArrayListFromArray(int arrId){
		return (ArrayList<String>) Arrays.asList(getResources().getStringArray(arrId));
	}

	/**
	 * On back pressed.
	 *
	 * @return true, if successful
	 */
	public boolean onBackPressed(){
		return false;
	}

	/**
	 * Show toast.
	 *
	 * @param msg the msg
	 */
	protected void showToast(String msg){
		Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
	}

	/**
	 * Gets the default selection.
	 *
	 * @param dataArr the data arr
	 * @param stringToFind the string to find
	 * @return the default selection
	 */
	public int getdefaultSelection(int dataArr, String stringToFind) {
		int index = (Arrays.asList(getResources().getStringArray(dataArr))).indexOf(stringToFind); 
		return index/* < 0 ? 0 : index*/;
	}
	
	/**
	 * On frag resume.
	 */
	public void onFragResume(){}

}
