package com.mutech.synergy;

import android.app.Application;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.mutech.synergy.utils.LruBitmapCache;

public class App extends Application{

	public static final String TAG = App.class.getSimpleName();

	private static App app;
	private RequestQueue mRequestQueue;
	private ImageLoader mImageLoader;

	public static synchronized App getInstance() {
		return app;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		app = this;

		mRequestQueue = Volley.newRequestQueue(this);
		mImageLoader = new ImageLoader(this.mRequestQueue, new ImageLoader.ImageCache() {

			private final LruCache<String, Bitmap> mCache = new LruCache<String, Bitmap>(10);

			public void putBitmap(String url, Bitmap bitmap) {
				mCache.put(url, bitmap);
			}

			public Bitmap getBitmap(String url) {
				return mCache.get(url);
			}
		});
	}

	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		}

		return mRequestQueue;
	}

	public ImageLoader getImageLoader() {
		getRequestQueue();
		if (mImageLoader == null) {
			mImageLoader = new ImageLoader(this.mRequestQueue,
					new LruBitmapCache());
		}
		return this.mImageLoader;
	}

	public <T> void addToRequestQueue(Request<T> req, String tag) {
		// set the default tag if tag is empty
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
		getRequestQueue().add(req);
	}

	public <T> void addToRequestQueue(Request<T> req) {
		req.setTag(TAG);
		getRequestQueue().add(req);
	}

	public void cancelPendingRequests(Object tag) {
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(tag);
		}
	}   


}
