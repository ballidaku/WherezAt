package ameba.com.wherezat;


import android.annotation.TargetApi;
import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;

import org.json.JSONObject;

import ameba.com.wherezat.activity.MainActivity;
import ameba.com.wherezat.global_classes.Util_Constants;


public class GCMIntentService extends GCMBaseIntentService {
//Browser Key=AIzaSyAeN0xjh8PkcH4F6yln5b04J7b8u1Spsnc
	
	private static final String TAG = "GCMIntentService";
	String status;
	Bundle bun;
	static int flag = 0;
	public GCMIntentService() {
		super(Util_Constants.SENDER_ID);
	}

	@Override
	protected void onRegistered(Context context, String registrationId) {
		Log.i(TAG, "Device registered: regId = " + registrationId);

	}

	@Override
	protected void onUnregistered(Context context, String registrationId) {
		Log.i(TAG, "Device unregistered");
		if (GCMRegistrar.isRegisteredOnServer(context)) {
			
		} else {
			Log.i(TAG, "Ignoring unregister callback");
		}

	}

	@Override
	protected void onMessage(Context context, Intent intent) {
		try {

			bun = intent.getExtras();
			String message = intent.getStringExtra("message");
			Log.e("+GCM message+++++++++", message);




			
			JSONObject jsonNoTi = new JSONObject(message);
			String noti_type = jsonNoTi.getString("NotificationType");


			if(noti_type.equals("invitecontacts"))
			{
				generateNotification(context, "You recieved a request");

				//Util_Constants.time_duration_of_hit = 0;
				Intent i = new Intent(Util_Constants.BROADCAST_UPDATERIGHTSIDE);
				context.sendBroadcast(i);

			}
			else if(noti_type.equals("inviteresponse"))
			{
				generateNotification(context, jsonNoTi.getString("Message"));

				Util_Constants.time_duration_of_hit = 0;

				Intent i = new Intent(Util_Constants.BROADCAST_UPDATEMAP);
				sendBroadcast(i);

				Intent i1 = new Intent(Util_Constants.BROADCAST_UPDATERIGHTSIDE);
				context.sendBroadcast(i1 );

			}
			else if(noti_type.equals("revoke"))
			{
				generateNotification(context, "Revoke");

				Util_Constants.time_duration_of_hit = 0;

				Intent i = new Intent(Util_Constants.BROADCAST_UPDATEMAP);
				sendBroadcast(i);

				Intent i1 = new Intent(Util_Constants.BROADCAST_UPDATERIGHTSIDE);
				context.sendBroadcast(i1);

			}
//				
//			String msgShow=jsonNoTi.getString("message");.
//			
//			if(flag==3)
//			{
//				UtilClass.tab_to_open=1;
//			}
//			else if(flag==1)
//			{
//				UtilClass.tab_to_open=1;
//			}
//			
//			SharedPreferences preferences = context.getSharedPreferences("Preference", Context.MODE_PRIVATE);
//			
//			
//			if(preferences.getBoolean("Notification_status", true))
//			{
//				generateNotification(context, msgShow);
//			}
//			
			
		

		} catch (Exception e) {
			Log.e(TAG, "Inside Exception onMessage -> " + e.toString());
		}
	}

	@Override
	protected void onDeletedMessages(Context context, int total) {
		Log.i(TAG, "Received deleted messages notification");
		String message ="";
		generateNotification(context, message);
	}

	@Override
	public void onError(Context context, String errorId) {
		Log.i(TAG, "Received error: " + errorId);

	}

	@Override
	protected boolean onRecoverableError(Context context, String errorId) {
		// log message
		Log.i(TAG, "Received recoverable error: " + errorId);
		return super.onRecoverableError(context, errorId);
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	private static void generateNotification(Context context, String message) {
		int icon = R.mipmap.mainicon;

	
		try {
			
		
		
		
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		String title ="WhereazAt"+"";
		Intent notificationIntent = new Intent(context, MainActivity.class);

		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent intent = PendingIntent.getActivity(context, 0,
				notificationIntent,  PendingIntent.FLAG_UPDATE_CURRENT);

		 Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		 
		 
		 
		 if(defaultSound == null){
			 defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
             if(defaultSound == null){
            	 defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
             }
         }
		 SharedPreferences preferences = context.getSharedPreferences("Preference", Context.MODE_PRIVATE);
		 if(preferences.getBoolean("Notification_sound", true))
		 {
			
		 }
		 else
		 {
			 defaultSound=null;
		 }
		 
		 Builder builder = new Builder(context)
			.setContentTitle(title).setContentText(message)
			.setContentIntent(intent).setSmallIcon(icon)
			.setLights(Color.YELLOW, 1, 2).setAutoCancel(true)
			.setSound(defaultSound);

	Notification not = new Notification.BigTextStyle(builder).bigText(message).build();

	
	if(defaultSound==null)
	{
		
	}
	else
	{
		not.defaults |= Notification.DEFAULT_VIBRATE;
		not.defaults |= Notification.DEFAULT_SOUND;
	}
	

	notificationManager.notify(0, not);
		
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
