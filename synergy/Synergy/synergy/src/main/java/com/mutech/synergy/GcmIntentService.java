
package com.mutech.synergy;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.mutech.synergy.R;
import com.mutech.synergy.activities.LoginActivity;


public class GcmIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public GcmIntentService() {
        super("GcmIntentService");
    }
    public static final String TAG = "GCM Demo";

    @Override
    protected void onHandleIntent(Intent intent) {
    	
        Bundle extras = intent.getExtras();
        String value = extras.getString("Message");
        
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM will be
             * extended in the future with new message types, just ignore any message types you're
             * not interested in, or that you don't recognize.
             */
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
            	sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
            	sendNotification("Deleted messages on server: " + extras.toString());
            // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                // This loop represents the service doing some work.
             
            	for (int i = 0; i < 5; i++) {
                    Log.i(TAG, "Working... " + (i + 1)
                            + "/5 @ " + SystemClock.elapsedRealtime());
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                    }
                }
                Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());
                // Post notification of received message.
                // Here they can send any data which will be present in intent format.
               // sendNotification("Received: " + extras.toString());
                sendNotification(value);
             
                Log.i(TAG, "Received: " + extras.toString());
                
                
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

  
 
	private void sendNotification(String greetMsg) {
		
        Intent resultIntent = new Intent(this,ShowMessageActivity.class);
        resultIntent.putExtra("greetjson", greetMsg);
        resultIntent.setAction(Intent.ACTION_MAIN);
        resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        
        PendingIntent resultPendingIntent = PendingIntent.getActivity(GcmIntentService.this, 0,
                resultIntent, PendingIntent.FLAG_ONE_SHOT);
        
        NotificationCompat.Builder mNotifyBuilder;
        NotificationManager mNotificationManager;
        
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        
        mNotifyBuilder = new NotificationCompat.Builder(GcmIntentService.this)
        .setSmallIcon(R.drawable.icon)
        .setContentTitle("Synergy")
        .setContentText(greetMsg); // Set pending intent
        mNotifyBuilder.setContentIntent(resultPendingIntent);
        
        // Set Vibrate, Sound and Light	        
        int defaults = 0;
        
        
        defaults = defaults | Notification.DEFAULT_LIGHTS;
        defaults = defaults | Notification.DEFAULT_VIBRATE;
        defaults = defaults | Notification.DEFAULT_SOUND;
        
        mNotifyBuilder.setDefaults(defaults);
        // Set the content for Notification 
        mNotifyBuilder.setContentText("New Message From Synergy !");
        // Set autocancel
        mNotifyBuilder.setAutoCancel(true);
        // Post a notification
        mNotificationManager.notify(NOTIFICATION_ID, mNotifyBuilder.build());
}
}
